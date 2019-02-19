package it.cnr.contab.doccont00.comp;

import java.math.BigDecimal;
import it.cnr.contab.doccont00.ejb.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import java.sql.*;
import java.util.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;
public class AccertamentoAbstractComponent extends it.cnr.jada.comp.CRUDComponent implements IDocumentoContabileMgr,IAccertamentoAbstractMgr,ICRUDMgr,Cloneable,Serializable
{
//@@<< CONSTRUCTORCST
    public  AccertamentoAbstractComponent()
    {
//>>

//<< CONSTRUCTORCSTL
        /*Default constructor*/
//>>

//<< CONSTRUCTORCSTT

    }
/**
 * aggiornaCogeCoanInDifferita method comment.
 */
public void aggiornaCogeCoanInDifferita(
	it.cnr.jada.UserContext userContext, 
	it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docContabile,
	java.util.Map values)
	throws it.cnr.jada.comp.ComponentException {

	AccertamentoBulk accertamento = (AccertamentoBulk)docContabile;
	try	{
		if (accertamento.getFl_pgiro() == null)
			accertamento = caricaAccertamento(userContext, accertamento);
		if (!accertamento.isInitialized())
			accertamento = (AccertamentoBulk)inizializzaBulkPerModifica(userContext, accertamento);
			
		if (accertamento.getFl_pgiro().booleanValue() ) {
			AccertamentoPGiroBulk accPGiro = (AccertamentoPGiroBulk) caricaAccertamento( userContext, accertamento );
			createAccertamentoPGiroComponentSession().aggiornaCogeCoanInDifferita(userContext, accPGiro, values);
		} else 
			createAccertamentoComponentSession().aggiornaCogeCoanInDifferita(userContext, accertamento, values);
	} catch ( Exception e )	{
		throw handleException( accertamento, e  );
	}	

}
/**
 * Aggiornamento in differita dei saldi dell'accertamento .
 * Un documento amministrativo di entrata che agisce in modalità transazionale ha creato/modificato gli importi 
 * relativi ad un accertamento; i saldi di tale accertamento non possono essere aggiornati subito in quanto
 * tale operazione genererebbe dei lock sulle voci del piano che non ne consentirebbere l'utilizzo ad altri utenti;
 * pertanto l'aggiornamento dei saldi dell'accertamento viene differito al momento del salvataggio
 * del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi per accertamento
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un accertamento su capitoli di
 *       bilancio
 * Post: La richiesta e' stata indirizzata alla component che gestisce gli accertamenti su capitoli di bilancio
 *       (AccertamentoComponent)
 *
 * Nome: Aggiorna saldi per accertamento su partita di giro
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un accertamento su capitoli di
 *       partita di giro
 * Post: La richiesta e' stata indirizzata alla component che gestisce gli accertamenti su capitoli di partita di giro
 *       (AccertamentoPGiroComponent)
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	docContabile il documento contabile di tipo AccertamentoBulk o AccertamentoPGiroBulk per cui aggiornare lo stato
 * @param	values	la Map che contiene il "pg_ver_rec" iniziale dell'accertamento
 * @param	param paramtero non utilizzato per gli accertamenti 
 */	
public void aggiornaSaldiInDifferita( UserContext userContext, IDocumentoContabileBulk docContabile, Map values, OptionRequestParameter param )  throws ComponentException {

	AccertamentoBulk accertamento = (AccertamentoBulk)docContabile;
	try	{
		// dato che caricaAccertamento e inizializzaBulkPerModifica cancellano
		// i valori impostati sul ObblihazionBulk li prelevo e li reimposto
		Accertamento_modificaBulk obbMod=null;
		boolean saldiDaAggiornare=false;
		if (accertamento.isAccertamentoResiduo()) {
			obbMod = ((AccertamentoResiduoBulk)accertamento).getAccertamento_modifica();
			saldiDaAggiornare = ((AccertamentoResiduoBulk)accertamento).isSaldiDaAggiornare();
		}
		if (accertamento.getFl_pgiro() == null)
			accertamento = caricaAccertamento(userContext, accertamento);
		if (!accertamento.isInitialized())
			accertamento = (AccertamentoBulk)inizializzaBulkPerModifica(userContext, accertamento);
			
		if (accertamento.getFl_pgiro().booleanValue() ) {
			// reimposto i valori
			if (accertamento.isAccertamentoResiduo()) {
				((AccertamentoResiduoBulk)accertamento).setAccertamento_modifica(obbMod);
				((AccertamentoResiduoBulk)accertamento).setSaldiDaAggiornare(saldiDaAggiornare);
			}
			AccertamentoPGiroBulk accPGiro = (AccertamentoPGiroBulk) caricaAccertamento( userContext, accertamento );
			createAccertamentoPGiroComponentSession().aggiornaSaldiInDifferita(userContext, accPGiro, values, param);
		} else {
			// reimposto i valori
			if (accertamento.isAccertamentoResiduo()) {
				((AccertamentoResiduoBulk)accertamento).setAccertamento_modifica(obbMod);
				((AccertamentoResiduoBulk)accertamento).setSaldiDaAggiornare(saldiDaAggiornare);
			}
			createAccertamentoComponentSession().aggiornaSaldiInDifferita(userContext, accertamento, values, param );
		}
	} catch ( Exception e )	{
		throw handleException( accertamento, e  );
	}	

}
/** 
  *  riporta all'esercizio successivo di doc.contabile
  *    PreCondition:
  *      E' stata inoltrata una richiesta di riportare all'esercizio successivo un documento contabile
  *	 PostCondition:
  *		Il sistema segnala con un errore che questa funzione non è richiamabile in modalità transazionele
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param doc <code>IDocumentoContabileBulk</code> doc.contabile da riportare
  *
  */

public void callRiportaAvanti (UserContext userContext,IDocumentoContabileBulk doc) throws it.cnr.jada.comp.ComponentException
{
	throw new ApplicationException( "Le funzioni di Riporta Avanti e Indietro non sono supporatate in modalità transazionale");
}
/** 
  *  riporta indietro dall'esercizio successivo di un doc.contabile
  *    PreCondition:
  *      E' stata inoltrata una richiesta di riportare indietro dall'esercizio successivo un documento contabile
  *	 PostCondition:
  *		Il sistema segnala con un errore che questa funzione non è richiamabile in modalità transazionele
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param doc <code>IDocumentoContabileBulk</code> doc.contabile da riportare
  *
  */

public void callRiportaIndietro (UserContext userContext,IDocumentoContabileBulk doc) throws ComponentException
{
	throw new ApplicationException( "Le funzioni di Riporta Avanti e Indietro non sono supporatate in modalità transazionale");
}
private AccertamentoBulk caricaAccertamento(UserContext uc, AccertamentoBulk accertamento)
	throws it.cnr.jada.persistency.PersistencyException, ComponentException {

	return (AccertamentoBulk)inizializzaBulkPerModifica(uc, (AccertamentoBulk)getHome(uc, accertamento).findByPrimaryKey(accertamento));
}
//^^@@
/** 
  *  Creazione di accertamenti generici provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la creazione di un'accertamento generico provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'creaConBulk' su AccertamentoComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *  Creazione di accertamenti per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la creazione di un'accertamento per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'creaConBulk' su AccertamentoPGiroComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
 */
//^^@@

public OggettoBulk creaConBulk (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	try
	{
		AccertamentoBulk accertamento = (AccertamentoBulk) bulk;
		if (accertamento.getFl_pgiro() == null)
			accertamento = caricaAccertamento(uc, accertamento);
		if (!accertamento.isInitialized())
			accertamento = (AccertamentoBulk)inizializzaBulkPerModifica(uc, accertamento);
		if (accertamento.getFl_pgiro().booleanValue() )
		{
			AccertamentoPGiroBulk accPGiro =	findAccertamentoPGiro( uc, accertamento );
			return createAccertamentoPGiroComponentSession().creaConBulk( uc, accPGiro);
		}
		else
		{
			AccertamentoOrdBulk accOrd =	findAccertamentoOrd( uc, accertamento );
			return createAccertamentoComponentSession().creaConBulk( uc, accOrd);
		}
	}
	catch ( Exception e )
	{
		throw handleException( bulk, e  );
	}	
	
}
/**
 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
 */
private it.cnr.contab.doccont00.ejb.AccertamentoComponentSession createAccertamentoComponentSession() throws ComponentException 
{
	try
	{
		return (AccertamentoComponentSession)EJBCommonServices.createEJB("CNRDOCCONT00_EJB_AccertamentoComponentSession");
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}	
}
/**
 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
 */
private it.cnr.contab.doccont00.ejb.AccertamentoPGiroComponentSession createAccertamentoPGiroComponentSession() throws ComponentException 
{
	try
	{
		return (AccertamentoPGiroComponentSession)EJBCommonServices.createEJB("CNRDOCCONT00_EJB_AccertamentoPGiroComponentSession");
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}	
}
//^^@@
/** 
  *  Eliminazione di accertamenti generici provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'eliminazione di un'accertamento generico provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'eliminaConBulk' su AccertamentoComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *  Eliminazione di accertamenti per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'eliminazione di un'accertamento per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'eliminaConBulk' su AccertamentoPGiroComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
 */
//^^@@

public void eliminaConBulk (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	try
	{
		AccertamentoBulk accertamento = (AccertamentoBulk) bulk;
		if (accertamento.getFl_pgiro() == null)
			accertamento = caricaAccertamento(uc, accertamento);
		if (!accertamento.isInitialized())
			accertamento = (AccertamentoBulk)inizializzaBulkPerModifica(uc, accertamento);
		if (accertamento.getFl_pgiro().booleanValue() )
		{
			AccertamentoPGiroBulk accPGiro =	findAccertamentoPGiro( uc, accertamento );
			createAccertamentoPGiroComponentSession().eliminaConBulk( uc, accPGiro);
		}
		else
		{
			AccertamentoOrdBulk accOrd =	findAccertamentoOrd( uc, accertamento );
			createAccertamentoComponentSession().eliminaConBulk( uc, accOrd);
		}
	}
	catch ( Exception e )
	{
		throw handleException( bulk, e  );
	}

}
public AccertamentoCdsBulk findAccertamentoCds(UserContext uc,AccertamentoBulk accertamento) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
{
	AccertamentoCdsBulk acc = ((AccertamentoHome)getHome( uc, AccertamentoCdsBulk.class)).findAccertamentoCds(accertamento);
	getHomeCache(uc).fetchAll(uc);
	return acc;	
}
public AccertamentoOrdBulk findAccertamentoOrd (UserContext uc,AccertamentoBulk accertamento) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
{
	AccertamentoOrdBulk acc = ((AccertamentoHome)getHome( uc, AccertamentoOrdBulk.class)).findAccertamentoOrd(accertamento);
	getHomeCache(uc).fetchAll(uc);
	return acc;	
}
public AccertamentoPGiroBulk findAccertamentoPGiro (UserContext uc,AccertamentoBulk accertamento) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
{
    AccertamentoPGiroBulk acc = ((AccertamentoHome)getHome( uc, AccertamentoPGiroBulk.class)).findAccertamentoPGiro(accertamento);
	getHomeCache(uc).fetchAll(uc);
	return acc;	
}
public AccertamentoResiduoBulk findAccertamentoRes (UserContext uc,AccertamentoBulk accertamento) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
{
	AccertamentoResiduoBulk acc = ((AccertamentoResiduoBulk)(getHome( uc, AccertamentoResiduoBulk.class)).findByPrimaryKey(accertamento));
	getHomeCache(uc).fetchAll(uc);
	return acc;	
}
//^^@@
/** 
  *  Inizializzazione per inserimento di accertamenti generici provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'inizializzazione per inserimento di un'accertamento generico provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'inizializzaBulkPerInserimento' su AccertamentoComponent
  *  Inizializzazione per inserimento di accertamenti per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'inizializzazione per inserimento di un'accertamento per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'inizializzaBulkPerInserimento' su AccertamentoPGiroComponent
 */
//^^@@

public OggettoBulk inizializzaBulkPerInserimento (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	try
	{
		AccertamentoBulk accertamento = (AccertamentoBulk) bulk;
		if (accertamento.getFl_pgiro() == null)
			accertamento = caricaAccertamento(uc, accertamento);
		if (accertamento.getFl_pgiro().booleanValue() )
		{
			AccertamentoPGiroBulk accPGiro =	findAccertamentoPGiro( uc, accertamento );
			return createAccertamentoPGiroComponentSession().inizializzaBulkPerInserimento( uc, accPGiro);
		}
		else
		{
			AccertamentoOrdBulk accOrd =	findAccertamentoOrd( uc, accertamento );
			return createAccertamentoComponentSession().inizializzaBulkPerInserimento( uc, accOrd);
		}
	}
	catch ( Exception e )
	{
		throw handleException( bulk, e  );
	}	

}
//^^@@
/** 
  *  Inizializzazione per modifica di accertamenti generici provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'inizializzazione per modifica di un'accertamento generico provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'inizializzaBulkPerModifica' su AccertamentoComponent
  *  Inizializzazione per modifica di accertamenti per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'inizializzazione per modifica di un'accertamento per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'inizializzaBulkPerModifica' su AccertamentoPGiroComponent
 */
//^^@@

public OggettoBulk inizializzaBulkPerModifica (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	try
	{
		AccertamentoBulk accertamento = (AccertamentoBulk) bulk;
		if (accertamento.getFl_pgiro() == null)
			accertamento = caricaAccertamento(uc, accertamento);
		if (accertamento.getFl_pgiro().booleanValue() ) {
			AccertamentoPGiroBulk accPGiro =	findAccertamentoPGiro( uc, accertamento );
			return createAccertamentoPGiroComponentSession().inizializzaBulkPerModifica( uc, accPGiro);
		} else {
			if ( accertamento.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_ACR_SIST) )
			{
				AccertamentoCdsBulk accCds =	findAccertamentoCds( uc, accertamento );
				return createAccertamentoComponentSession().inizializzaBulkPerModifica( uc, accCds);
			}			
			else if ( accertamento.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_ACR_RES) )
			{
				AccertamentoResiduoBulk accRes =	findAccertamentoRes( uc, accertamento );
				return createAccertamentoComponentSession().inizializzaBulkPerModifica( uc, accRes);
			}			
			else
			{
				AccertamentoOrdBulk accOrd =	findAccertamentoOrd( uc, accertamento );
				return createAccertamentoComponentSession().inizializzaBulkPerModifica( uc, accOrd);
			}
		}
	}
	catch ( Exception e )
	{
		throw handleException( bulk, e  );
	}
	

}
/** 
  *  Lock scadenza
  *		PreCondition:
  *			E' stato richiesto l'inserimento di un lock sulla scadenza di un accertamento
  *    PostCondition:
  *  		Il record relativo alla scadenza e' stato messo in lock e non e' pertanto consentito ad altre transazioni
  *         l'accesso a tale scadenza
 */

public void lockScadenza( UserContext userContext,IScadenzaDocumentoContabileBulk scadenza) throws ComponentException
{
	try
	{
		getHome( userContext, scadenza.getClass()).lock( (OggettoBulk)scadenza );
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}	
}	
//^^@@
/** 
  *  Modifica di accertamenti generici provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la modifica di un'accertamento generico provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'modificaConBulk' su AccertamentoComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *  Modifica di accertamenti per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la modifica di un'accertamento per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'modificaConBulk' su AccertamentoPGiroComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
 */
//^^@@

public OggettoBulk modificaConBulk (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	try
	{
		AccertamentoBulk accertamento = (AccertamentoBulk) bulk;
		if (accertamento.getFl_pgiro() == null)
			accertamento = caricaAccertamento(uc, accertamento);
		if (!accertamento.isInitialized())
			accertamento = (AccertamentoBulk)inizializzaBulkPerModifica(uc, accertamento);
		if (accertamento.getFl_pgiro().booleanValue() )
		{
			AccertamentoPGiroBulk accPGiro =	findAccertamentoPGiro( uc, accertamento );
			return createAccertamentoPGiroComponentSession().modificaConBulk( uc, accPGiro);
		}
		else
		{
			AccertamentoOrdBulk accOrd =	findAccertamentoOrd( uc, accertamento );
			return createAccertamentoComponentSession().modificaConBulk( uc, accOrd);
		}
	}
	catch ( Exception e )
	{
		throw handleException( bulk, e  );
	}

}
//^^@@
/** 
  *  Modifica in automatico di scadenze di accertamenti generici provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la modifica in automatico di scadenze di un'accertamento generico provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'modificaScadenzaInAutomatico' su AccertamentoComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *  Modifica in automatico di scadenze di accertamenti per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la modifica in automatico di scadenze di un'accertamento per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'modificaScadenzaInAutomatico' su AccertamentoPGiroComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
 */
//^^@@

public IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico( UserContext userContext,	IScadenzaDocumentoContabileBulk scad,	BigDecimal nuovoImporto, boolean aggiornaScadenzaSuccessiva) throws ComponentException 
{
	return modificaScadenzaInAutomatico(userContext, scad, nuovoImporto, aggiornaScadenzaSuccessiva, false);
}
public IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico( UserContext userContext,	IScadenzaDocumentoContabileBulk scad,	BigDecimal nuovoImporto, boolean aggiornaScadenzaSuccessiva, Boolean aggiornaCalcoloAutomatico) throws ComponentException 
{
	Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk)scad;
	try
	{
		AccertamentoBulk accertamento = scadenza.getAccertamento();
		if (accertamento.getFl_pgiro() == null)
			accertamento = caricaAccertamento(userContext, accertamento);
		if (!accertamento.isInitialized()) {
			accertamento = (AccertamentoBulk)inizializzaBulkPerModifica(userContext, accertamento);
			if (BulkCollections.containsByPrimaryKey(accertamento.getAccertamento_scadenzarioColl(), scadenza))
				scadenza = (Accertamento_scadenzarioBulk)accertamento.getAccertamento_scadenzarioColl().get(BulkCollections.indexOfByPrimaryKey(accertamento.getAccertamento_scadenzarioColl(), scadenza));
			else
				throw new ApplicationException("Impossibile ottenere dall'accertamento la scadenza da modificare in automatico!");
		}
		if (accertamento.getFl_pgiro().booleanValue() )
		{
			AccertamentoPGiroBulk accPGiro = (AccertamentoPGiroBulk) caricaAccertamento( userContext, accertamento );
			return createAccertamentoPGiroComponentSession().modificaScadenzaInAutomatico( userContext, (Accertamento_scadenzarioBulk) accPGiro.getAccertamento_scadenzarioColl().get(0), nuovoImporto, aggiornaScadenzaSuccessiva);
		}	
		else
		{
			return createAccertamentoComponentSession().modificaScadenzaInAutomatico( userContext, scadenza, nuovoImporto, aggiornaScadenzaSuccessiva, aggiornaCalcoloAutomatico);
		}	
	}
	catch ( Exception e )
	{
		throw handleException( scadenza, e  );
	}	

}
public IScadenzaDocumentoContabileBulk sdoppiaScadenzaInAutomatico( UserContext userContext,	IScadenzaDocumentoContabileBulk scad,	BigDecimal nuovoImporto) throws ComponentException 
{
	Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk)scad;
	try
	{
		AccertamentoBulk accertamento = scadenza.getAccertamento();
		if (accertamento.getFl_pgiro() == null)
			accertamento = caricaAccertamento(userContext, accertamento);
		if (!accertamento.isInitialized()) {
			accertamento = (AccertamentoBulk)inizializzaBulkPerModifica(userContext, accertamento);
			if (BulkCollections.containsByPrimaryKey(accertamento.getAccertamento_scadenzarioColl(), scadenza))
				scadenza = (Accertamento_scadenzarioBulk)accertamento.getAccertamento_scadenzarioColl().get(BulkCollections.indexOfByPrimaryKey(accertamento.getAccertamento_scadenzarioColl(), scadenza));
			else
				throw new ApplicationException("Impossibile ottenere dall'accertamento la scadenza da modificare in automatico!");
		}
		if (accertamento.getFl_pgiro().booleanValue() )
		{
			throw new ApplicationException("Impossibile sdoppiare in automatico una scadenza appartenente ad un accertamento creato su partita di giro!");
		}	
		else
		{
			return createAccertamentoComponentSession().sdoppiaScadenzaInAutomatico( userContext, scadenza, nuovoImporto);
		}	
	}
	catch ( Exception e )
	{
		throw handleException( scadenza, e  );
	}	

}
/**
 * Annulla le modifiche apportate all'accertamento e ritorna al savepoint impostato in precedenza
 *
 * Pre-post-conditions:
 *
 * Nome: Rollback to savePoint
 * Pre:  Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata 
 * Post: Tutte le modifiche effettuate sull'accertamento vengono annullate, mentre rimangono valide le
 *       modifiche apportate al doc. amministrativo
 * @param	uc	lo UserContext che ha generato la richiesta
 */	
public void rollbackToSavePoint(it.cnr.jada.UserContext userContext) 
	throws ComponentException {

	try {
		rollbackToSavepoint(userContext, "ACC_ABS_SP");
	} catch (SQLException e) {
		if (e.getErrorCode() != 1086)
			throw handleException(e);
	}
}
/**
 * Imposta un savepoint che consente di salvare le modifiche apportate al doc.amministrativo fino a quel momento
 * in modo che se gli aggiornamenti apportati all'accertamento non venissero confermati (rollback), comunque non verrebbero persi
 * anche quelli del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Imposta savePoint
 * Pre:  Una richiesta di impostare un savepoint e' stata generata 
 * Post: Un savepoint e' stato impostato in modo che le modifiche apportate al doc. amministrativo vengono consolidate
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 */
public void setSavePoint(it.cnr.jada.UserContext userContext) 
	throws ComponentException {

	try {
		setSavepoint(userContext, "ACC_ABS_SP");
	} catch (SQLException e) {
		throw handleException(e);
	}
}
}
