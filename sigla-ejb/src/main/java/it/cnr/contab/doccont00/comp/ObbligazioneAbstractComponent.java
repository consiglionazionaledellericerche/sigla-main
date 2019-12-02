/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.doccont00.comp;

import java.math.BigDecimal;
import it.cnr.contab.doccont00.ejb.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import java.sql.*;

import java.util.*;

import it.cnr.contab.doccont00.core.DatiFinanziariScadenzeDTO;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;
public class ObbligazioneAbstractComponent extends it.cnr.jada.comp.CRUDComponent implements IDocumentoContabileMgr,IObbligazioneAbstractMgr,ICRUDMgr,Cloneable,Serializable
{

//@@<< CONSTRUCTORCST
    public  ObbligazioneAbstractComponent()
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
public void aggiornaCogeCoanInDifferita(it.cnr.jada.UserContext userContext, it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docContabile, java.util.Map values) throws it.cnr.jada.comp.ComponentException {

	ObbligazioneBulk obbligazione = (ObbligazioneBulk)docContabile;
	try
	{
		if (obbligazione.getFl_pgiro() == null)
			obbligazione = caricaObbligazione(userContext, obbligazione);
		if (!obbligazione.isInitialized())
			obbligazione = (ObbligazioneBulk)inizializzaBulkPerModifica(userContext, obbligazione);

		if (obbligazione.getFl_pgiro().booleanValue() )	{
			ImpegnoPGiroBulk obbPGiro = (ImpegnoPGiroBulk) caricaObbligazione( userContext, obbligazione );
			createObbligazionePGiroComponentSession().aggiornaCogeCoanInDifferita(userContext, obbPGiro, values);
		} else if (obbligazione.isObbligazioneResiduo() ) {
			createObbligazioneResComponentSession().aggiornaCogeCoanInDifferita( userContext, obbligazione, values);
		} else 
			createObbligazioneComponentSession().aggiornaCogeCoanInDifferita( userContext, obbligazione, values);
	} catch ( Exception e ) {
		throw handleException( obbligazione, e );
	}	
}
/**
 * Aggiornamento in differita dei saldi dell'obbligazione .
 * Un documento amministrativo di spesa che agisce in modalità transazionale ha creato/modificato gli importi 
 * relativi ad un'obbligazione; i saldi di tale obbligazione non possono essere aggiornati subito in quanto
 * tale operazione genererebbe dei lock sulle voci del piano che non ne consentirebbere l'utilizzo ad altri utenti;
 * pertanto l'aggiornamento dei saldi dell'obbligazione viene differito al momento del salvataggio
 * del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi per obbligazione
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per una obbligazione su capitoli di
 *       bilancio
 * Post: La richiesta e' stata indirizzata alla component che gestisce le obbligazioni su capitoli di bilancio
 *       (ObbligazioneComponent)
 *
 * Nome: Aggiorna saldi per obbligazione su partita di giro
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per una obbligazione su capitoli di
 *       partita di giro
 * Post: La richiesta e' stata indirizzata alla component che gestisce le obbligazioni su capitoli di partita di giro
 *       (ObbligazionePGiroComponent)
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	docContabile	il documento contabile di tipo ObbligazioneBulk o ImpegnoPGirobulk per cui aggiornare lo stato
 * @param	values	la Map che contiene il "pg_ver_rec" iniziale dell'obbligazione e il "checkDisponibilitaCassaEseguito" che indica se
 *          l'utente ha richiesto la forzatura della disponibilità di cassa (parametro impostato dalla Gestione Obbligazione)
 * @param	param il parametro che indica se il controllo della disp. di cassa e' necessario (parametro impostato dalla Gestione dei doc. amm.)
 
 */	
public void aggiornaSaldiInDifferita( UserContext userContext, IDocumentoContabileBulk docContabile, Map values, OptionRequestParameter param ) throws ComponentException {

	ObbligazioneBulk obbligazione = (ObbligazioneBulk)docContabile;
	try
	{
		// dato che caricaObbligazione e inizializzaBulkPerModifica cancellano
		// i valori impostati sul ObblihazionBulk li prelevo e li reimposto
		Obbligazione_modificaBulk obbMod=null;
		boolean saldiDaAggiornare=false;
		if (obbligazione.isObbligazioneResiduo()) {
			obbMod = ((ObbligazioneResBulk)obbligazione).getObbligazione_modifica();
			saldiDaAggiornare = ((ObbligazioneResBulk)obbligazione).isSaldiDaAggiornare();
		}
		if (obbligazione.getFl_pgiro() == null)
			obbligazione = caricaObbligazione(userContext, obbligazione);
		if (!obbligazione.isInitialized())
			obbligazione = (ObbligazioneBulk)inizializzaBulkPerModifica(userContext, obbligazione);

		if (obbligazione.getFl_pgiro().booleanValue() )	{
			ImpegnoPGiroBulk obbPGiro = (ImpegnoPGiroBulk) caricaObbligazione( userContext, obbligazione );
			// reimposto i valori
			if (obbligazione.isObbligazioneResiduo()) {
				((ObbligazioneResBulk)obbligazione).setObbligazione_modifica(obbMod);
				((ObbligazioneResBulk)obbligazione).setSaldiDaAggiornare(saldiDaAggiornare);
			}
			createObbligazionePGiroComponentSession().aggiornaSaldiInDifferita(userContext, obbPGiro, values, param);
		} else {
			// reimposto i valori
			if (obbligazione.isObbligazioneResiduo()) {
				((ObbligazioneResBulk)obbligazione).setObbligazione_modifica(obbMod);
				((ObbligazioneResBulk)obbligazione).setSaldiDaAggiornare(saldiDaAggiornare);
			}
			createObbligazioneComponentSession().aggiornaSaldiInDifferita( userContext, obbligazione, values, param );
		}
	} catch ( Exception e ) {
		throw handleException( obbligazione, e );
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
/**
 * @param uc lo <code>UserContext</code> che ha generato la richiesta
 * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione
 *
 * @return <code>ObbligazioneBulk</code> L'obbligazione inizializzata
 */
private ObbligazioneBulk caricaObbligazione(UserContext uc, ObbligazioneBulk obbligazione)
	throws it.cnr.jada.persistency.PersistencyException, ComponentException {

	// i valori impostati sul ObbligazioneBulk li prelevo e li reimposto
	Obbligazione_modificaBulk obbMod=null;
	boolean saldiDaAggiornare=false;
	if (obbligazione instanceof ObbligazioneResBulk && obbligazione.isObbligazioneResiduo()) {
		obbMod = ((ObbligazioneResBulk)obbligazione).getObbligazione_modifica();
		saldiDaAggiornare = ((ObbligazioneResBulk)obbligazione).isSaldiDaAggiornare();
	}
	ObbligazioneBulk obbl = (ObbligazioneBulk)inizializzaBulkPerModifica(uc, (ObbligazioneBulk)getHome(uc, obbligazione).findByPrimaryKey(obbligazione));
	
	// reimposto i valori
	if (obbligazione instanceof ObbligazioneResBulk && obbligazione.isObbligazioneResiduo()) {
		((ObbligazioneResBulk)obbligazione).setObbligazione_modifica(obbMod);
		((ObbligazioneResBulk)obbligazione).setSaldiDaAggiornare(saldiDaAggiornare);
	}

	return obbl;
}
/** 
  *  Creazione di obbligazioni generiche provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la creazione di un'obbligazione generica provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'creaConBulk' su ObbligazioneComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *  Creazione di obbligazioni per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la creazione di un'obbligazione per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'creaConBulk' su ObbligazionePGiroComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'obbligazione da creare
  *
  * @return <code>OggettoBulk</code> L'obbligazione creata
 */

public OggettoBulk creaConBulk (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	try
	{
		ObbligazioneBulk obbligazione = (ObbligazioneBulk) bulk;
		if (obbligazione.getFl_pgiro() == null)
			obbligazione = caricaObbligazione(uc, obbligazione);
		if (!obbligazione.isInitialized())
			obbligazione = (ObbligazioneBulk)inizializzaBulkPerModifica(uc, obbligazione);
		if (obbligazione.getFl_pgiro().booleanValue() )
		{
			ImpegnoPGiroBulk impPGiro =	findObbligazionePGiro( uc, obbligazione );
			return createObbligazionePGiroComponentSession().creaConBulk( uc, impPGiro);
		}
		else 
		{
			ObbligazioneBulk obb = findObbligazione( uc, obbligazione );
			return createObbligazioneComponentSession().creaConBulk( uc, obb);
		}
	}
	catch ( Exception e )
	{
		throw handleException( bulk, e  );
	}	
	
}
/**
 * Crea la ComponentSession da usare per effettuare le operazioni di CRUD su Obbligazioni
 *
 * @return ObbligazioneComponentSession l'istanza di <code>ObbligazioneComponentSession</code> che serve per gestire un'obbligazione
 */
private it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession createObbligazioneComponentSession() throws ComponentException 
{
	try
	{
		return (ObbligazioneComponentSession)EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ObbligazioneComponentSession");
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}	
}
/**
 * Crea la ComponentSession da usare per effettuare le operazioni di CRUD su Obbligazioni Residue Proprie
 *
 * @return ObbligazioneComponentSession l'istanza di <code>ObbligazioneResComponentSession</code> che serve per gestire un'obbligazione residuo proprio
 */
private it.cnr.contab.doccont00.ejb.ObbligazioneResComponentSession createObbligazioneResComponentSession() throws ComponentException 
{
	try
	{
		return (ObbligazioneResComponentSession)EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ObbligazioneResComponentSession");
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}	
}
/**
 * Crea la ComponentSession da usare per effettuare le operazioni di CRUD su Impegni in Partita di giro
 *
 * @return ObbligazionePGiroComponentSession l'istanza di <code>ObbligazionePGiroComponentSession</code> che serve per gestire un'impegno
 */
private it.cnr.contab.doccont00.ejb.ObbligazionePGiroComponentSession createObbligazionePGiroComponentSession() throws ComponentException 
{
	try
	{
		return (ObbligazionePGiroComponentSession)EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ObbligazionePGiroComponentSession");
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}	
}
/** 
  *  Eliminazione di obbligazioni generiche provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'eliminazione di un'obbligazione generica provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'eliminaConBulk' su ObbligazioneComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *  Eliminazione di obbligazioni per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'eliminazione di un'obbligazione per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'eliminaConBulk' su ObbligazionePGiroComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'obbligazione da cancellare
  *
 */

public void eliminaConBulk (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	try
	{
		ObbligazioneBulk obbligazione = (ObbligazioneBulk) bulk;
		if (obbligazione.getFl_pgiro() == null)
			obbligazione = caricaObbligazione(uc, obbligazione);
		if (!obbligazione.isInitialized())
			obbligazione = (ObbligazioneBulk)inizializzaBulkPerModifica(uc, obbligazione);
		if (obbligazione.getFl_pgiro().booleanValue() )
		{
			ImpegnoPGiroBulk impPGiro =	findObbligazionePGiro( uc, obbligazione );
			createObbligazionePGiroComponentSession().eliminaConBulk( uc, impPGiro);
		}
		else
		{
			ObbligazioneBulk obb =	findObbligazione( uc, obbligazione );
			createObbligazioneComponentSession().eliminaConBulk( uc, obb);
		}
	}
	catch ( Exception e )
	{
		throw handleException( bulk, e  );
	}	

}
/**
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione da cercare
  *
  * @return obbl l'obbligazione cercata
  */
private ObbligazioneBulk findObbligazione (UserContext uc,ObbligazioneBulk obbligazione) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
{
	if (obbligazione.isObbligazioneResiduo())
		return findObbligazioneRes( uc, obbligazione );
	else if (obbligazione.isObbligazioneResiduoImproprio())
		return findObbligazioneResImpropria( uc, obbligazione );
	else
		return findObbligazioneOrd( uc, obbligazione );
}
/**
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione da cercare
  *
  * @return obbl l'obbligazione cercata
  */
private ObbligazioneOrdBulk findObbligazioneOrd (UserContext uc,ObbligazioneBulk obbligazione) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
{
	
	ObbligazioneOrdBulk obbl = ((ObbligazioneHome)getHome( uc, ObbligazioneOrdBulk.class)).findObbligazioneOrd(obbligazione);
	getHomeCache(uc).fetchAll(uc);
	return obbl;
}
/**
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione da cercare
  *
  * @return obbl l'obbligazione cercata
  */
private ObbligazioneResBulk findObbligazioneRes (UserContext uc,ObbligazioneBulk obbligazione) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
{
	ObbligazioneResBulk obbl = ((ObbligazioneHome)getHome( uc, ObbligazioneResBulk.class)).findObbligazioneRes(obbligazione);
	getHomeCache(uc).fetchAll(uc);
	return obbl;
}
/**
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione da cercare
  *
  * @return obbl l'obbligazione cercata
  */
private ObbligazioneRes_impropriaBulk findObbligazioneResImpropria (UserContext uc,ObbligazioneBulk obbligazione) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
{
	
	ObbligazioneRes_impropriaBulk obbl = ((ObbligazioneHome)getHome( uc, ObbligazioneRes_impropriaBulk.class)).findObbligazioneRes_impropria(obbligazione);
	getHomeCache(uc).fetchAll(uc);
	return obbl;
}
/**
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'impegno da cercare
  *
  * @return impegno l'impegno cercato
  */
private ImpegnoPGiroBulk findObbligazionePGiro (UserContext uc,ObbligazioneBulk obbligazione) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
{
	ImpegnoPGiroBulk impegno = ((ObbligazioneHome)getHome( uc, ImpegnoPGiroBulk.class)).findObbligazionePGiro(obbligazione);
	getHomeCache(uc).fetchAll(uc);
	return impegno;
}
/** 
  *  Inizializzazione per inserimento di obbligazioni generiche provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'inizializzazione per inserimento di un'obbligazione generica provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'inizializzaBulkPerInserimento' su ObbligazioneComponent
  *  Inizializzazione per inserimento di obbligazioni per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'inizializzazione per inserimento di un'obbligazione per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'inizializzaBulkPerInserimento' su ObbligazionePGiroComponent
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'obbligazione da inizializzare per l'inserimento
  *
  * @return <code>OggettoBulk</code> l'obbligazione inizializzata per l'inserimento
 */

public OggettoBulk inizializzaBulkPerInserimento (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	try
	{
		ObbligazioneBulk obbligazione = (ObbligazioneBulk) bulk;
		if (obbligazione.getFl_pgiro() == null)
			obbligazione = caricaObbligazione(uc, obbligazione);
		if (obbligazione.getFl_pgiro().booleanValue() )
		{
			ImpegnoPGiroBulk impPGiro =	findObbligazionePGiro( uc, obbligazione );
			return createObbligazionePGiroComponentSession().inizializzaBulkPerInserimento( uc, impPGiro);
		}
		else
		{
			ObbligazioneBulk obb =	findObbligazione( uc, obbligazione );
			return createObbligazioneComponentSession().inizializzaBulkPerInserimento( uc, obb);
		}
	}
	catch ( Exception e )
	{
		throw handleException( bulk, e  );
	}	

}
/** 
  *  Inizializzazione per modifica di obbligazioni generiche provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'inizializzazione per modifica di un'obbligazione generica provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'inizializzaBulkPerModifica' su ObbligazioneComponent
  *  Inizializzazione per modifica di obbligazioni per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'inizializzazione per modifica di un'obbligazione per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'inizializzaBulkPerModifica' su ObbligazionePGiroComponent
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'obbligazione da inizializzare per la modifica
  *
  * @return <code>OggettoBulk</code> l'obbligazione inizializzata per la modifica
 */

public OggettoBulk inizializzaBulkPerModifica (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	try
	{
		ObbligazioneBulk obbligazione = (ObbligazioneBulk) bulk;
		if (obbligazione.getFl_pgiro() == null)
			obbligazione = caricaObbligazione(uc, obbligazione);
		if (obbligazione.getFl_pgiro().booleanValue() )
		{
			ImpegnoPGiroBulk impPGiro =	findObbligazionePGiro( uc, obbligazione );
			return createObbligazionePGiroComponentSession().inizializzaBulkPerModifica( uc, impPGiro);
		}
		else if (obbligazione.isObbligazioneResiduo())
		{
			ObbligazioneBulk obb =	findObbligazione( uc, obbligazione );
			return createObbligazioneResComponentSession().inizializzaBulkPerModifica( uc, obb);
		}
		else 
		{
			ObbligazioneBulk obb =	findObbligazione( uc, obbligazione );
			return createObbligazioneComponentSession().inizializzaBulkPerModifica( uc, obb);
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
  *			E' stato richiesto l'inserimento di un lock sulla scadenza di un'obbligazione
  *    PostCondition:
  *  		Il record relativo alla scadenza e' stato messo in lock e non e' pertanto consentito ad altre transazioni
  *         l'accesso a tale scadenza
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param scadenza <code>Obbligazione_scadenzarioBulk</code> la scadenza di obbligazione per cui mettere il lock
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
/** 
  *  Modifica di obbligazioni generiche provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la modifica di un'obbligazione generica provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'modificaConBulk' su ObbligazioneComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *  Modifica di obbligazioni per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la modifica di un'obbligazione per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'modificaConBulk' su ObbligazionePGiroComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'obbligazione da modificare
  *
  * @return <code>OggettoBulk</code> l'obbligazione modificata
 */

public OggettoBulk modificaConBulk (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	try
	{
		ObbligazioneBulk obbligazione = (ObbligazioneBulk) bulk;
		if (obbligazione.getFl_pgiro() == null)
			obbligazione = caricaObbligazione(uc, obbligazione);
		if (!obbligazione.isInitialized())
			obbligazione = (ObbligazioneBulk)inizializzaBulkPerModifica(uc, obbligazione);
		if (obbligazione.getFl_pgiro().booleanValue() )
		{
			ImpegnoPGiroBulk impPGiro =	findObbligazionePGiro( uc, obbligazione );
			return createObbligazionePGiroComponentSession().modificaConBulk( uc, impPGiro);
		}
		else
		{
			ObbligazioneBulk obb =	findObbligazione( uc, obbligazione );
			return createObbligazioneComponentSession().modificaConBulk( uc, obb);
		}
	}
	catch ( Exception e )
	{
		throw handleException( bulk, e  );
	}	

}
/** 
  *  Modifica in automatico di scadenze di obbligazioni generiche provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la modifica in automatico di scadenze di un'obbligazione generica provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'modificaScadenzaInAutomatico' su ObbligazioneComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *  Modifica in automatico di scadenze di obbligazioni per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la modifica in automatico di scadenze di un'obbligazione per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'modificaScadenzaInAutomatico' su ObbligazionePGiroComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  * 
  * @param userContext lo userContext che ha generato la richiesta
  * @param scadenza (con importo originale)
  * @param nuovoImporto che deve assumere la scadenza
  * @param modificaScadenzaSuccessiva se true indica il fatto che la testata dell'obbligazione non deve essere modificata
  *                                   e che la differenza fra l'importo attuale e il vecchio importo deve essere riportata sulla
  *									 scadenza successiva
  * @param modificaScadenzaSuccessiva se false indica il fatto che deve essere modificato l'importo della scadenza e della testata
  *                                   dell'obbligazione
  * @return la scadenza 
  */

public IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico( UserContext userContext,	IScadenzaDocumentoContabileBulk scadenza,	BigDecimal nuovoImporto, boolean modificaScadenzaSuccessiva) throws ComponentException 
{
	Obbligazione_scadenzarioBulk scad = (Obbligazione_scadenzarioBulk)scadenza;
	try
	{
		ObbligazioneBulk obbligazione = scad.getObbligazione();
		if (obbligazione.getFl_pgiro() == null)
			obbligazione = caricaObbligazione(userContext, obbligazione);
		if (!obbligazione.isInitialized()) {
			obbligazione = (ObbligazioneBulk)inizializzaBulkPerModifica(userContext, obbligazione);
			if (BulkCollections.containsByPrimaryKey(obbligazione.getObbligazione_scadenzarioColl(), scad))
				scad = (Obbligazione_scadenzarioBulk)obbligazione.getObbligazione_scadenzarioColl().get(BulkCollections.indexOfByPrimaryKey(obbligazione.getObbligazione_scadenzarioColl(), scad));
			else
				throw new ApplicationException("Impossibile ottenere dall'impegno la scadenza da modificare in automatico!");
		}
		if (obbligazione.getFl_pgiro().booleanValue() )
		{
			ImpegnoPGiroBulk obbPGiro = (ImpegnoPGiroBulk) caricaObbligazione( userContext, obbligazione );
			return createObbligazionePGiroComponentSession().modificaScadenzaInAutomatico( userContext, (Obbligazione_scadenzarioBulk) obbPGiro.getObbligazione_scadenzarioColl().get(0), nuovoImporto, modificaScadenzaSuccessiva);
		}
		else
			return createObbligazioneComponentSession().modificaScadenzaInAutomatico( userContext, scad, nuovoImporto, modificaScadenzaSuccessiva);
	}
	catch ( Exception e )
	{
		throw handleException( scad, e  );
	}	

}
/**
 * Annulla le modifiche apportate all'obbligazione e ritorna al savepoint impostato in precedenza
 *
 * Pre-post-conditions:
 *
 * Nome: Rollback to savePoint
 * Pre:  Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata 
 * Post: Tutte le modifiche effettuate sull'obbligazione vengono annullate, mentre rimangono valide le
 *       modifiche apportate al doc. amministrativo
 * @param	uc	lo UserContext che ha generato la richiesta
 */	
public void rollbackToSavePoint(it.cnr.jada.UserContext userContext) 
	throws ComponentException {

	try {
		rollbackToSavepoint(userContext, "OBBL_ABS_SP");
	} catch (SQLException e) {
		if (e.getErrorCode() != 1086)
			throw handleException(e);
	}
}
/**
 * Imposta un savepoint che consente di salvare le modifiche apportate al doc.amministrativo fino a quel momento
 * in modo che se gli aggiornamenti apportati all'obbligazione non venissero confermati (rollback), comunque non verrebbero persi
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
		setSavepoint(userContext, "OBBL_ABS_SP");
	} catch (SQLException e) {
		throw handleException(e);
	}
}
public IScadenzaDocumentoContabileBulk sdoppiaScadenzaInAutomatico( UserContext userContext,	IScadenzaDocumentoContabileBulk scad,	BigDecimal nuovoImporto) throws ComponentException 
{
	DatiFinanziariScadenzeDTO dati = new DatiFinanziariScadenzeDTO();
	dati.setNuovoImportoScadenzaVecchia(nuovoImporto);
	return sdoppiaScadenzaInAutomatico(userContext, scad, dati);
}
public IScadenzaDocumentoContabileBulk sdoppiaScadenzaInAutomatico( UserContext userContext,	IScadenzaDocumentoContabileBulk scad,	DatiFinanziariScadenzeDTO dati) throws ComponentException 
{
	Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)scad;
	try
	{
		ObbligazioneBulk obbligazione = scadenza.getObbligazione();
		if (obbligazione.getFl_pgiro() == null)
			obbligazione = caricaObbligazione(userContext, obbligazione);
		if (!obbligazione.isInitialized()) {
			obbligazione = (ObbligazioneBulk)inizializzaBulkPerModifica(userContext, obbligazione);
			if (BulkCollections.containsByPrimaryKey(obbligazione.getObbligazione_scadenzarioColl(), scadenza))
				scadenza = (Obbligazione_scadenzarioBulk)obbligazione.getObbligazione_scadenzarioColl().get(BulkCollections.indexOfByPrimaryKey(obbligazione.getObbligazione_scadenzarioColl(), scadenza));
			else
				throw new ApplicationException("Impossibile ottenere dall'impegno la scadenza da modificare in automatico!");
		}
		if (obbligazione.getFl_pgiro().booleanValue() )
		{
			throw new ApplicationException("Impossibile sdoppiare in automatico una scadenza appartenente ad un impegno creato su partita di giro!");
		}	
		else
		{
			return createObbligazioneComponentSession().sdoppiaScadenzaInAutomatico( userContext, scadenza, dati);
		}	
	}
	catch ( Exception e )
	{
		throw handleException( scadenza, e  );
	}	
}
}
