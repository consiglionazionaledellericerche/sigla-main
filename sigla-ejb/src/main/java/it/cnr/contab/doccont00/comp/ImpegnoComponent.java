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

import it.cnr.contab.config00.esercizio.bulk.*;
import it.cnr.contab.config00.ejb.*;
import it.cnr.contab.config00.bulk.*;
import it.cnr.contab.doccont00.ejb.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.config00.pdcfin.bulk.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import it.cnr.contab.doccont00.core.bulk.*;
import java.io.Serializable;
import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * Classe che ridefinisce alcune operazioni di CRUD su ImpegnoPGiroBulk
 */

 /* Gestisce documenti di tipo
	IMP_RES - bilancio CNR
*/	


public class ImpegnoComponent extends it.cnr.jada.comp.CRUDComponent implements IDocumentoContabileMgr,ICRUDMgr,Cloneable,Serializable,IImpegnoMgr
{
	private final static int INSERIMENTO 	= 1;
	private final static int MODIFICA    	= 2;
	private final static int CANCELLAZIONE = 3;		

	
//@@<< CONSTRUCTORCST
    public  ImpegnoComponent()
    {
//>>

//<< CONSTRUCTORCSTL
        /*Default constructor*/
//>>

//<< CONSTRUCTORCSTT

    }
/*
  *  creazione impegno
  *    PreCondition:
  *      CASO MAI VERIFICABILE (viene mantenuto per omogeneità con gli altri documenti contabili)
  *    PostCondition:
  *      Viene chiamato il metodo 'aggiornaSaldiInInserimento' che, per la voce del piano utilizzata nell'obbligazione calcola
  *      l'importo dell' aggiornamento da apportare ai saldi e richiama il metodo sulla Component di Gestione Saldi (SaldoComponent) che
  *      effettua tale aggiornamento
  *  modifica/eliminazione obbligazione
  *    PreCondition:
  *      Un impegno è stato modificato
  *      Un impegno è stato cancellato - CASO MAI VERIFICABILE (viene mantenuto per omogeneità con gli altri documenti contabili)
  *    PostCondition:
  *      Viene chiamato il metodo 'aggiornaSaldiInModifica' che, per la voce del piano utilizzata nell'impegno calcola
  *      l'importo dell' aggiornamento da apportare ai saldi e richiama il metodo sulla Component di Gestione Saldi (SaldoComponent) che
  *      effettua tale aggiornamento
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param impegno <code>ImpegnoResiduoBulk</code> l'impegno da salvare
  * @param azione indica l'azione effettuata sull'impegno e puo' assumere solo il valore MODIFICA
  *
 */

private void aggiornaCapitoloSaldoObbligazione (UserContext aUC,ImpegnoResiduoBulk obbligazione, int azione) throws ComponentException
{
	try
	{
		Obbligazione_scad_voceBulk osv, osvDaDB;
		// non si aggiornano i saldi di obbligazioni con esercizio di competenza diverso da esercizio di creazione
		if ( obbligazione.getEsercizio().compareTo( obbligazione.getEsercizio_competenza()) != 0 )
			return;
		if ( azione == INSERIMENTO )
			aggiornaSaldiInInserimento( aUC, obbligazione );
		else if ( azione == MODIFICA )
			aggiornaSaldiInModifica( aUC, 
											 obbligazione, 
											 obbligazione.getPg_ver_rec());
		else if ( azione == CANCELLAZIONE )
			aggiornaSaldiInModifica( aUC, 
											 obbligazione, 
											 new Long(obbligazione.getPg_ver_rec().longValue() + 1));		

	}
	catch ( Exception e )
	{
		throw handleException( e );
	}	
}

public void aggiornaCogeCoanInDifferita(it.cnr.jada.UserContext userContext, it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docContabile, java.util.Map values) throws it.cnr.jada.comp.ComponentException {
	
	try
	{
		if ( docContabile instanceof ImpegnoResiduoBulk )
		{
			ImpegnoResiduoBulk impegnoRes = (ImpegnoResiduoBulk) docContabile;
			Long pg_ver_rec = (Long) values.get("pg_ver_rec");
			if ( pg_ver_rec == null )
				throw new ApplicationException( "Aggiornamento in differita dello stato coge/coan dei documenti contabili impossibile (pg_ver_rec nullo)");
			if ( impegnoRes.getPg_obbligazione().longValue() >= 0 ) //obbligazione non temporanea
				callDoRiprocObb(userContext, impegnoRes, pg_ver_rec );			
		}
	}
	catch ( Exception e )
	{
		throw handleException(e);
	}
}
/**
 * Aggiornamento in differita dei saldi degli impegni residui
 * Un documento amministrativo di spesa che agisce in modalità transazionale ha creato/modificato gli importi 
 * relativi ad un impegno pgiro; i saldi di tale impegno non possono essere aggiornati subito in quanto
 * tale operazione genererebbe dei lock sulle voci del piano che non ne consentirebbere l'utilizzo ad altri utenti;
 * pertanto l'aggiornamento dei saldi dell'obbligazione pgiro viene differito al momento del salvataggio
 * del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi per impegno residuo creato
 * Pre:  MAI VERIFICABILE - mantenuto x omogeneità con tutti i doc. contabili
 *
 * Nome: Aggiorna saldi per impegno residuo esistente
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un impegno residuo
 *       che non e' stata creata nel contesto transazionale del documento amministrativo ( progressivo
 *       impegno  > 0)
 * Post: I saldi dell'impegno sono stati aggiornati nel metodo 'aggiornaSaldiInModifica'
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	docContabile	l'ImpegnoResiduoBulk per cui aggiornare i saldi
 * @param	values	la Map che contiene il "pg_ver_rec" iniziale dell'impegno 
 * @param	param parametro non utilizzato per impegni
 * 
*/
public void aggiornaSaldiInDifferita( UserContext userContext, IDocumentoContabileBulk docContabile, Map values, OptionRequestParameter param ) throws ComponentException
{
	try
	{
		if ( docContabile instanceof ImpegnoResiduoBulk )
		{
			ImpegnoResiduoBulk imp = (ImpegnoResiduoBulk) docContabile;
			Long pg_ver_rec = (Long) values.get("pg_ver_rec");
			if ( pg_ver_rec == null )
				throw new ApplicationException( "Aggiornamento in differita dei saldi dei documenti contabili impossibile (pg_ver_rec nullo)");
			if ( imp.getPg_obbligazione().longValue() < 0 ) //obbligazione appena inserita
				aggiornaSaldiInInserimento( userContext, imp );
			else
				aggiornaSaldiInModifica( userContext, imp, pg_ver_rec );			
		}
	}
	catch ( Exception e )
	{
		throw handleException(e);
	}		
}
/**
 *
 * non usata perchè gli impegni residui non vengono mai inserito dall'on-line
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	imp	l'ImpegnoResiduoBulk per cui aggiornare i saldi
 * 
 */
private void aggiornaSaldiInInserimento( UserContext userContext, ImpegnoResiduoBulk imp) throws ComponentException, java.rmi.RemoteException
{
	SaldoComponentSession session = createSaldoComponentSession();
	PrimaryKeyHashMap saldiDaAggiornare;
	try {
		saldiDaAggiornare = imp.getVociMap(((Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class)).isNuovoPdg(userContext));
	} catch (PersistencyException e) {
		throw handleException(e);
	}
	for ( Iterator i = saldiDaAggiornare.keySet().iterator(); i.hasNext(); )
	{
		Voce_fBulk voce = (Voce_fBulk) i.next();
		BigDecimal im_voce = (BigDecimal) saldiDaAggiornare.get(voce);
		session.aggiornaObbligazioniAccertamenti( userContext, voce, imp.getCd_cds(), im_voce, it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk.TIPO_COMPETENZA);
	}
	/*
	* Aggiorno i Saldi per CDR/Linea
	*/
	PrimaryKeyHashMap saldiDaAggiornareCdrLinea = imp.getObbligazione_scad_voceMap();
	for ( Iterator i = saldiDaAggiornareCdrLinea.keySet().iterator(); i.hasNext(); )
	{
		Obbligazione_scad_voceBulk osv = (Obbligazione_scad_voceBulk) i.next();
		BigDecimal im_voce = (BigDecimal) saldiDaAggiornareCdrLinea.get(osv);
		Voce_fBulk voce = new Voce_fBulk( osv.getCd_voce(), osv.getEsercizio(), osv.getTi_appartenenza(), osv.getTi_gestione());
		session.aggiornaObbligazioniAccertamenti( userContext, osv.getCd_centro_responsabilita(), osv.getCd_linea_attivita(), voce, imp.getEsercizio_originale(),imp.isObbligazioneResiduoImproprio()?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO,im_voce,imp.getCd_tipo_documento_cont());
	}	
}
/**
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi
 * Pre:  Un'obbligazione e' stata modificata/eliminata
 * Post: Per ogni V_mod_saldi_obbligBulk presente nel database a fronte dell'obbligazione e del suo pg_ver_rec
 *       e' stato richiamato il metodo sulla Component di gestione dei Saldi (SaldoCompoennt) per aggiornare
 *       il saldo del capitolo corrispondente; se necessario
 *       anche i saldi relativi ai mandati e al pagato vengono aggiornati.
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	imp	l'ImpegnoResiduoBulk per cui aggiornare i saldi
 * @param	pg_ver_rec	il "pg_ver_rec" iniziale dell'impegno 
 */
private void aggiornaSaldiInModifica( UserContext userContext, ImpegnoResiduoBulk imp, Long pg_ver_rec ) throws it.cnr.jada.persistency.PersistencyException, ComponentException, java.rmi.RemoteException
{
	SaldoComponentSession session = createSaldoComponentSession();

	String ti_competenza_residuo;
	if ( imp.isResiduo() )
		ti_competenza_residuo = ReversaleBulk.TIPO_RESIDUO;
	else
		ti_competenza_residuo = ReversaleBulk.TIPO_COMPETENZA;

	List saldiDaAggiornare = ((V_mod_saldi_obbligHome)getHome( userContext, V_mod_saldi_obbligBulk.class )).findModificheSaldiFor( imp, pg_ver_rec );
	if ( userContext.isTransactional() && saldiDaAggiornare.size() == 0 )
		throw new ApplicationException( "Attenzione! I saldi relativi all'impegno " + imp.getEsercizio_originale() + "/" + imp.getPg_obbligazione() + " non possono essere aggiornati perchè l'impegno non e' presente nello storico.");
	for ( Iterator i = saldiDaAggiornare.iterator(); i.hasNext(); )
	{
		V_mod_saldi_obbligBulk modSaldo = (V_mod_saldi_obbligBulk) i.next();
		Voce_fBulk voce = new Voce_fBulk( modSaldo.getCd_voce(), imp.getEsercizio(), modSaldo.getTi_appartenenza(), modSaldo.getTi_gestione() );
		if ( modSaldo.getIm_delta_voce().compareTo( new BigDecimal(0)) != 0 )
		{

			/* il check della disponabilità di cassa deve essere eseguito solo se 
			    l'importo delta del saldo e' positivo e
		 	   l'utente non ha ancora avuto il warning sulla disp.cassa oppure
			    l'utente ha avuto il warning sulla disp.cassa e ha risposto no */
			session.aggiornaObbligazioniAccertamenti( userContext, voce, imp.getCd_cds(), modSaldo.getIm_delta_voce(), ti_competenza_residuo);

			if ( modSaldo.getIm_delta_man_voce().compareTo( new BigDecimal(0) ) != 0 )
				session.aggiornaMandatiReversali( userContext, voce, imp.getCd_cds(), modSaldo.getIm_delta_man_voce(), ti_competenza_residuo);

			if ( modSaldo.getIm_delta_pag_voce().compareTo( new BigDecimal(0) ) != 0 )
				session.aggiornaPagamentiIncassi( userContext, voce, imp.getCd_cds(), modSaldo.getIm_delta_pag_voce(), ti_competenza_residuo);
		}		
		
	}	
	/*
	* Aggiorno i Saldi per CDR/Linea
	*/	
	List saldiDaAggiornareCdrLinea = ((V_mod_saldi_obblig_scad_voceHome)getHome( userContext, V_mod_saldi_obblig_scad_voceBulk.class )).findModificheSaldiFor( imp, pg_ver_rec );
	if ( userContext.isTransactional() && saldiDaAggiornareCdrLinea.size() == 0 )
		throw new ApplicationException( "Attenzione! I saldi relativi all'impegno " + imp.getEsercizio_originale() + "/" + imp.getPg_obbligazione() + " non possono essere aggiornati perchè l'impegno non e' presente nello storico.");
	for ( Iterator i = saldiDaAggiornareCdrLinea.iterator(); i.hasNext(); )
	{
		V_mod_saldi_obblig_scad_voceBulk modSaldo = (V_mod_saldi_obblig_scad_voceBulk) i.next();
		Voce_fBulk voce = new Voce_fBulk( modSaldo.getCd_voce(), imp.getEsercizio(), modSaldo.getTi_appartenenza(), modSaldo.getTi_gestione() );
		if ( modSaldo.getIm_delta_voce().compareTo( new BigDecimal(0)) != 0 )
			session.aggiornaObbligazioniAccertamenti( userContext, modSaldo.getCd_centro_responsabilita(), modSaldo.getCd_linea_attivita(), voce, modSaldo.getEsercizio_originale(),modSaldo.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA)?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO,modSaldo.getIm_delta_voce(),modSaldo.getCd_tipo_documento_cont());
		if ( modSaldo.getIm_delta_man_voce().compareTo( new BigDecimal(0) ) != 0 )
			session.aggiornaMandatiReversali( userContext,modSaldo.getCd_centro_responsabilita(),modSaldo.getCd_linea_attivita(),voce,modSaldo.getEsercizio_originale(),modSaldo.getIm_delta_man_voce(),modSaldo.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA)?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO );
		if ( modSaldo.getIm_delta_pag_voce().compareTo( new BigDecimal(0) ) != 0 )
			session.aggiornaPagamentiIncassi( userContext,modSaldo.getCd_centro_responsabilita(),modSaldo.getCd_linea_attivita(),voce,modSaldo.getEsercizio_originale(), modSaldo.getIm_delta_pag_voce());
	}
}
/** 
  *  aggiornamento Stato COAN o COGE dei Documenti Amministrativi
  *    PreCondition:
  *      Nel caso un impegno residuo sia stato modificato, occorre
  *		 aggiornare lo stato di eventuali documenti amministrativi che fanno
  *		 riferimento all'impegno stesso.
  *	   PostCondition:
  *		 Vengono quindi aggiornati i documenti amministrativi tramite una stored procedure
  *		 (doRiprocObb)
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param docContabile <code>ObbligazioneBulk</code> l'obbligazione modificata o eliminata
  *
  */
private void aggiornaStatoCOAN_COGEDocAmm( UserContext userContext, ObbligazioneBulk docContabile  )  throws  ComponentException
{
	callDoRiprocObb(userContext, docContabile, null);
}
/** 
  *  riprocessa lo stato coge/coan di documenti associati al doc. contabile
  *    PreCondition:
  *      E' stata inoltrata una richiesta di riprocessare lo stato coge/coan di doc. amm. associati al documento contabile
  *	 PostCondition:
  *      Vengono cambiati gli stati coge/coan dei doc amm associati al doc. cont
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param doc <code>IDocumentoContabileBulk</code> doc.contabile da utilizzare
  * @param pg_ver_rec <code>Long</code> pg_ver_rec di riferimento
  *
  */

public void callDoRiprocObb(
	UserContext userContext,
	IDocumentoContabileBulk doc,
	Long pg_ver_rec)
	throws it.cnr.jada.comp.ComponentException
{
	try
	{
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ), 
			"call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
			"CNRCTB215.doRiprocObb(?, ?, ?, ?, ?)",false,this.getClass());
		try
		{
			cs.setInt( 1, doc.getEsercizio().intValue());
			cs.setString( 2, doc.getCd_cds());			
			cs.setInt( 3, doc.getEsercizio_originale().intValue());
			cs.setLong( 4, doc.getPg_doc_contabile().longValue());
			if (pg_ver_rec == null)
				cs.setNull( 5, Types.DECIMAL);
			else
				cs.setLong( 5, pg_ver_rec.longValue());
			cs.executeQuery();
			
		}
		catch ( SQLException e )
		{
			throw handleException( e );
		}	
		finally
		{
			cs.close();
		}
	}
	catch ( SQLException e )
	{
		throw handleException( e );
	}	
}
/** 
  *  riporta all'esercizio successivo di doc.contabile
  *    PreCondition:
  *      E' stata inoltrata una richiesta di riportare all'esercizio successivo un documento contabile
  *	 PostCondition:
  *		Il doc.contabile è stato riportato all'esercizio successivo richiamando 
  *      la stored procedure CNRCTB046.riportoEsNextDocCont
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param doc <code>IDocumentoContabileBulk</code> doc.contabile da riportare
  *
  */

public void callRiportaAvanti (UserContext userContext,IDocumentoContabileBulk doc) throws it.cnr.jada.comp.ComponentException
{
	try
	{
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ), 
			"call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
			"CNRCTB046.riportoEsNextDocCont(?, ?, ?, ?, ?, ?)",false,this.getClass());
		try
		{
			cs.setString( 1, doc.getCd_cds());			
			cs.setObject( 2, doc.getEsercizio());
			cs.setObject( 3, doc.getEsercizio_originale());
			cs.setObject( 4, doc.getPg_doc_contabile());
			cs.setString( 5, doc.getTi_entrata_spesa());					
			cs.setString( 6, ((CNRUserContext)userContext).getUser() );
			cs.executeQuery();
			
		}		catch ( SQLException e )
		{
			throw handleException( e );
		}	
		finally
		{
			cs.close();
		}
	}
	catch ( SQLException e )
	{
		throw handleException( e );
	}	
}
/** 
  *  riporta indietro dall'esercizio successivo di un doc.contabile
  *    PreCondition:
  *      E' stata inoltrata una richiesta di riportare indietro dall'esercizio successivo un documento contabile
  *	 PostCondition:
  *		Il doc.contabile è stato riportato all'esercizio successivo richiamando 
  *      la stored procedure CNRCTB046.deriportoEsNextDocCont
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param doc <code>IDocumentoContabileBulk</code> doc.contabile da riportare
  *
  */

public void callRiportaIndietro (UserContext userContext,IDocumentoContabileBulk doc) throws ComponentException
{
	try
	{
		LoggableStatement cs =new LoggableStatement(getConnection( userContext ), 
			"call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
			"CNRCTB046.deriportoEsNextDocCont(?, ?, ?, ?, ?, ?)",false,this.getClass());
		try
		{
			cs.setString( 1, doc.getCd_cds());			
			cs.setObject( 2, doc.getEsercizio());
			cs.setObject( 3, doc.getEsercizio_originale());
			cs.setObject( 4, doc.getPg_doc_contabile());
			cs.setString( 5, doc.getTi_entrata_spesa());					
			cs.setString( 6, ((CNRUserContext)userContext).getUser() );
			cs.executeQuery();
			
		}
		catch ( SQLException e )
		{
			throw handleException( e );
		}	
		finally
		{
			cs.close();
		}
	}
	catch ( SQLException e )
	{
		throw handleException( e );
	}	
}
/**
 * Crea la ComponentSession da usare per effettuare le operazioni di aggiornamento saldi documenti contabili
 *
 * @return SaldoComponentSession l'istanza di <code>SaldoComponentSession</code> che serve per aggiornare un saldo
 */
private it.cnr.contab.doccont00.ejb.SaldoComponentSession createSaldoComponentSession() throws ComponentException 
{
	try
	{
		return (SaldoComponentSession)EJBCommonServices.createEJB("CNRDOCCONT00_EJB_SaldoComponentSession");
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}	
}
/** 
  *  inizializzazione per modifica
  *    PreCondition:
  *      La richiesta di inizializzazione di un ImpegnoResiduoBulk per modifica
  *      e' stata generata
  *    PostCondition:
  *      Vengono recuperati la scadenza e il dettaglio di scadenza associati all'impegno.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno residuo da inizializzare per la modifica
  *
  * @return l'impegno residuo inizializzato per la modifica
 */
public OggettoBulk inizializzaBulkPerModifica (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	try
	{
		bulk = super.inizializzaBulkPerModifica( aUC, bulk );
		
		ImpegnoResiduoBulk imp = (ImpegnoResiduoBulk) bulk;

		imp.setCd_terzo_iniziale( imp.getCd_terzo());
	
	//query per recuperare la scadenza dell'obbligazione
		Obbligazione_scadenzarioHome obblig_scadHome = (Obbligazione_scadenzarioHome) getHome( aUC, Obbligazione_scadenzarioBulk.class );

		SQLBuilder sql = obblig_scadHome.createSQLBuilder();
		sql.addClause("AND","cd_cds",sql.EQUALS, imp.getCd_cds() );
		sql.addClause("AND","esercizio",sql.EQUALS, imp.getEsercizio() );
		sql.addClause("AND","esercizio_originale",sql.EQUALS, imp.getEsercizio_originale() );
		sql.addClause("AND","pg_obbligazione",sql.EQUALS, imp.getPg_obbligazione() );

		List result = obblig_scadHome.fetchAll( sql );
		Obbligazione_scadenzarioBulk obblig_scad = (Obbligazione_scadenzarioBulk) result.get(0);

		imp.getObbligazione_scadenzarioColl().add( obblig_scad );
		obblig_scad.setObbligazione( imp);

		//se im_associato doc amm > 0 esiste certamente un doc amm. associato, ne imposto uno fittizio
		//per flaggare l'impegno e non consentire la modifiac del terzo
		if ( obblig_scad.getIm_associato_doc_amm().compareTo( new BigDecimal(0)) > 0 )
			obblig_scad.setPg_doc_passivo( new Long(1)); 
		/*
		//carico l'eventuale doc.amministrativo legato alla scadenza
		V_doc_passivo_obbligazioneBulk docPassivo = obblig_scadHome.findDoc_passivo( obblig_scad );
		if ( docPassivo != null)
		{
			obblig_scad.setEsercizio_doc_passivo( docPassivo.getEsercizio());
			obblig_scad.setPg_doc_passivo( docPassivo.getPg_documento_amm());
			imp.setEsercizio_doc_passivo( docPassivo.getEsercizio());
			imp.setPg_doc_passivo( docPassivo.getPg_documento_amm());
			imp.setCd_tipo_documento_amm( docPassivo.getCd_tipo_documento_amm());
		}

		//carico l'eventuale mandato associato
		Mandato_rigaBulk mandato = obblig_scadHome.findMandato( obblig_scad );
		if ( mandato != null )
		{
			imp.setEsercizio_mandato( mandato.getEsercizio());
			imp.setPg_mandato( mandato.getPg_mandato());
		}
*/		
	//query per recuperare scad_voce dell'obbligazione
	//scadenza.getObbligazione_scad_voceColl().add( scad_voce );
		Obbligazione_scad_voceHome obblig_scad_voceHome = (Obbligazione_scad_voceHome) getHome( aUC, Obbligazione_scad_voceBulk.class );

		sql = obblig_scad_voceHome.createSQLBuilder();
		sql.addClause("AND","cd_cds",sql.EQUALS, imp.getCd_cds() );
		sql.addClause("AND","esercizio",sql.EQUALS, imp.getEsercizio() );
		sql.addClause("AND","esercizio_originale",sql.EQUALS, imp.getEsercizio_originale() );
		sql.addClause("AND","pg_obbligazione",sql.EQUALS, imp.getPg_obbligazione() );

		result = obblig_scad_voceHome.fetchAll( sql );
		Obbligazione_scad_voceBulk obblig_scad_voce = (Obbligazione_scad_voceBulk) result.get(0);

		obblig_scad.getObbligazione_scad_voceColl().add( obblig_scad_voce );
		obblig_scad_voce.setObbligazione_scadenzario( obblig_scad );

		Voce_fBulk voce = new Voce_fBulk( obblig_scad_voce.getCd_voce(),obblig_scad_voce.getEsercizio(), obblig_scad_voce.getTi_appartenenza(), obblig_scad_voce.getTi_gestione());
		voce = (Voce_fBulk) getHome( aUC, Voce_fBulk.class).findByPrimaryKey( voce );
		if ( voce == null )
			throw new ApplicationException( "Voce_f non trovata");
		imp.setVoce( voce );
			

		/*
		//query per recuperare cd_uo_ente dell'impegno
		Unita_organizzativa_enteHome uo_enteHome = (Unita_organizzativa_enteHome) getHome( aUC, Unita_organizzativa_enteBulk.class );
		sql = uo_enteHome.createSQLBuilder();
		result = uo_enteHome.fetchAll( sql );
		Unita_organizzativa_enteBulk uo_ente = (Unita_organizzativa_enteBulk) result.get(0);

		imp.setCd_uo_ente( uo_ente.getCd_unita_organizzativa() );
		*/

	return imp;
	}
	catch ( Exception e )
	{
		throw handleException(bulk, e)	;
	}
}
public OggettoBulk inizializzaBulkPerRicerca (UserContext userContext,OggettoBulk bulk) throws ComponentException
{

	ImpegnoResiduoBulk imp = (ImpegnoResiduoBulk) super.inizializzaBulkPerRicerca( userContext, bulk );
	try
	{
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk)getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		// se l'unità organizzativa è diversa da quella dell'Ente, non è possibile ricercare/modificare impegni residui
		if ( !((CNRUserContext)userContext).getCd_unita_organizzativa().equals( uoEnte.getCd_unita_organizzativa() ))
				throw new ApplicationException("Funzione consentita solo per utente abilitato a " + uoEnte.getCd_unita_organizzativa() ); 
		
		imp.setCd_uo_origine( uoEnte.getCd_unita_organizzativa());		
		imp.setCd_cds_origine( uoEnte.getCd_unita_padre());
		imp.setCd_cds( uoEnte.getCd_unita_padre());
		imp.setCd_unita_organizzativa( uoEnte.getCd_unita_organizzativa());
		return imp;
	}
	catch ( it.cnr.jada.persistency.PersistencyException e )
	{
		throw handleException(imp, e);
	}
}
public OggettoBulk inizializzaBulkPerRicercaLibera (UserContext userContext,OggettoBulk bulk) throws ComponentException
{
	return inizializzaBulkPerRicerca( userContext, bulk );
}
/** 
  *  Lock scadenza
  *		PreCondition:
  *			E' stato richiesto l'inserimento di un lock sulla scadenza di un'obbligazione
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
/**
  *  errore modifca importo - doc.amm.associati
  *    PreCondition:
  *      La richiesta di modifica dell'importo di un impegno su partita di giro e' stata generata ma l'importo totale
  *      dei documenti amministrativi contabilizzati sulla scadenza supera il nuovo importo dell'impegno residuo
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la modifica in quanto l'importo dell'impegno
  *      deve sempre essere >= importo associato a doc.amm.
  *  modifica importo
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di un impegno residuo
  *    PostCondition:
  *      L'importo dell'impegno viene modificato e, in cascata, vengono modificati gli importi relativi
  *      alla scadenza e al dettaglio scadenza. I saldi relativi ai documenti contabili vengono aggiornati
  *		(metodo aggiornaCapitoloSaldoObbligazione).
  *		Vengono aggiornati gli stati COAN e COGE degli eventuali documenti amministrativi associati 
  *		(metodo aggiornaStatoCOAN_COGEDocAmm)
  *      Viene scatenata la scrittura di una variazione formale
  *  modifica descrizione
  *    PreCondition:
  *      L'utente richiede la modifica della descrizione di un impegno su partita di giro
  *    PostCondition:
  *      La descrizione dell'obbligazione e della scadenza di obbligazione vengono aggiornate
  *  modifica capitolo
  *    PreCondition:
  *      L'utente richiede la modifica del capitolo di un impegno residuo
  *    PostCondition:
  *      Il capitolo viene aggiornato sia in testata che nello scad_voce
  *      Viene scatenata la scrittura di una variazione formale
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno  da modificare
  *
  * @return <code>OggettoBulk</code> l'impegno modificato
 */
public OggettoBulk modificaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException
{			
	try
	{

		ImpegnoResiduoBulk imp = (ImpegnoResiduoBulk) bulk;

		verificaStatoEsercizio( aUC, imp.getEsercizio(), imp.getCd_cds());

		Obbligazione_scadenzarioBulk scad = (Obbligazione_scadenzarioBulk) imp.getObbligazione_scadenzarioColl().get(0);
		
		//verifico importo
		if ( imp.getIm_obbligazione().compareTo( scad.getIm_associato_doc_amm()) < 0 )
			throw new ApplicationException( "Impossibile variare importo perchè esistono documenti amministrativi collegati per un importo pari a " + scad.getIm_associato_doc_amm() );

		//importo
		Obbligazione_scadenzarioBulk obblig_scadenzario = (Obbligazione_scadenzarioBulk)imp.getObbligazione_scadenzarioColl().get(0);
		obblig_scadenzario.setIm_scadenza( imp.getIm_obbligazione() );
		obblig_scadenzario.setToBeUpdated();
		
		Obbligazione_scad_voceBulk obblig_scad_voce = (Obbligazione_scad_voceBulk)obblig_scadenzario.getObbligazione_scad_voceColl().get(0);
		obblig_scad_voce.setIm_voce(imp.getIm_obbligazione() );
		obblig_scad_voce.setToBeUpdated();

		// descrizione
		obblig_scadenzario.setDs_scadenza( imp.getDs_obbligazione() );

		//voce_f
		if ( !obblig_scad_voce.getCd_voce().equals( imp.getVoce().getCd_voce()))
			imp.setCd_elemento_voce( imp.getVoce().getCd_titolo_capitolo());
		
		//aggiorna il db:

		imp.setUser( aUC.getUser());		
		updateBulk( aUC, imp );
		obblig_scadenzario.setUser( aUC.getUser());		
		updateBulk( aUC, obblig_scadenzario );
		obblig_scad_voce.setUser( aUC.getUser());
		if ( obblig_scad_voce.getCd_voce().equals( imp.getVoce().getCd_voce()))
			updateBulk( aUC, obblig_scad_voce );
		else
			// se e' stato modificato il capitolo e' necessario ricreare lo scad_voce 
			// perchè non e' possibile aggiornare la chiave
		{
			Obbligazione_scad_voceBulk newObblig_scad_voce = new Obbligazione_scad_voceBulk();
			newObblig_scad_voce.setLinea_attivita( obblig_scad_voce.getLinea_attivita());
			newObblig_scad_voce.setObbligazione_scadenzario( obblig_scad_voce.getObbligazione_scadenzario());
			newObblig_scad_voce.setIm_voce(obblig_scad_voce.getIm_voce());
			newObblig_scad_voce.setTi_appartenenza(obblig_scad_voce.getTi_appartenenza());
			newObblig_scad_voce.setTi_gestione(obblig_scad_voce.getTi_gestione());
			newObblig_scad_voce.setCd_voce(imp.getVoce().getCd_voce());
			newObblig_scad_voce.setUser(obblig_scad_voce.getUser());
			newObblig_scad_voce.setCd_fondo_ricerca(obblig_scad_voce.getCd_fondo_ricerca());
			deleteBulk( aUC, obblig_scad_voce);			
			insertBulk( aUC, newObblig_scad_voce );

		}	

	    if ( !aUC.isTransactional() )
		//aggiorna i saldi
		{
			aggiornaCapitoloSaldoObbligazione( aUC, imp, MODIFICA );
	    
			aggiornaStatoCOAN_COGEDocAmm( aUC, imp );
		}	
		
		verificaStatoEsercizio( 
							aUC, 
							((CNRUserContext)aUC).getEsercizio(), 
							imp.getCd_cds());

		return imp;
	
	} 
	catch ( Exception e )
	{
		throw handleException( bulk, e );
	}
}
/*
 * Modifica l'importo di una scadenza e della testata dell'obbligazione
 *	
 * Pre-post-conditions:
 *
 * Nome: NON SI PUO' MAI VERIFICARE
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param scad la scadenza (con importo originale)
 * @param nuovoImporto che deve assumere la scadenza
 * @param modificaScadenzaSuccessiva se true indica il fatto che la testata dell'obbligazione non deve essere modificata
 *                                   e che la differenza fra l'importo attuale e il vecchio importo deve essere riportata sulla
 *									 scadenza successiva
 * @param modificaScadenzaSuccessiva se false indica il fatto che deve essere modificato l'importo della scadenza e della testata
 *                                   dell'obbligazione
 * @return la scadenza 
 */

public IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico( UserContext userContext,	IScadenzaDocumentoContabileBulk scad,	BigDecimal nuovoImporto, boolean modificaScadenzaSuccessiva ) throws ComponentException 
{
	throw new ApplicationException(" Funzione non consentita" );
	/*
	Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)scad;
	if ( modificaScadenzaSuccessiva )
		throw new ApplicationException(" Non esiste scadenza successiva" );
	if ( scadenza.getIm_scadenza().compareTo( nuovoImporto ) == 0 )
		throw handleException( new ApplicationException( "Aggiornamento in automatico non necessario" ));
	if (  nuovoImporto.compareTo( new BigDecimal(0)) < 0  )
		throw handleException( new ApplicationException( "L'importo della scadenza deve essere maggiore di 0" ));					

	//aggiorno importo testata
	ImpegnoResiduoBulk imp = (ImpegnoResiduoBulk) scadenza.getObbligazione();
	if ( imp.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_IMP_RES) )
		throw handleException( new ApplicationException( "Non è consentita la modifica dell'importo di testata di un'annotazione residua." ));					
	
	imp.setIm_obbligazione( nuovoImporto );
	imp.setToBeUpdated();

	imp.setFromDocAmm( true );
	modificaConBulk( userContext, scadenza.getObbligazione());
	return scadenza;
	*/
}

protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	SQLBuilder sql = (SQLBuilder) super.select( userContext, clauses, bulk );
	sql.addSQLClause( "AND", "esercizio", sql.EQUALS, ((CNRUserContext)userContext).getEsercizio()); 
	sql.addSQLClause( "AND", "fl_pgiro", sql.EQUALS, "N");
	sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP_RES);
	return sql;
}
/**
 * Verifica dello stato dell'esercizio 
 *
 * @param userContext <code>UserContext</code> 
 *
 * @return FALSE se per il cds interessato non è stato inserito nessun esercizio o se l'esercizio non è in stato di "aperto"
 *          o l'esercizio precedente non è chiuso
 *		   TRUE in tutti gli altri casi
 *
 */

void verificaStatoEsercizio( UserContext userContext, Integer es, String cd_cds ) throws ComponentException, it.cnr.jada.persistency.PersistencyException
{
	EsercizioBulk esercizio = (EsercizioBulk) getHome(userContext, EsercizioBulk.class).findByPrimaryKey( 
																									new EsercizioBulk( cd_cds, es ));
	if (esercizio == null )
			throw handleException( new ApplicationException( "Operazione impossibile: esercizio inesistente!") );
	if ( !esercizio.STATO_APERTO.equals(esercizio.getSt_apertura_chiusura()))
			throw handleException( new ApplicationException( "Operazione impossibile: esercizio non aperto!") );

	EsercizioBulk esPrec = (EsercizioBulk) getHome(userContext, EsercizioBulk.class).findByPrimaryKey( 
																									new EsercizioBulk( cd_cds, new Integer( es.intValue() - 1 )));
	if (esPrec != null && !esPrec.STATO_CHIUSO_DEF.equals(esPrec.getSt_apertura_chiusura()))
			throw handleException( new ApplicationException( "Operazione impossibile: esercizio precedente non chiuso!") );

			
}			
			
}
