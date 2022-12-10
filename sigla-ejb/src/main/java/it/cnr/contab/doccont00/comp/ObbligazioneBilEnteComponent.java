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

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteHome;
import it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk;
import it.cnr.contab.doccont00.core.bulk.Ass_partita_giroBulk;
import it.cnr.contab.doccont00.core.bulk.Ass_partita_giroHome;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.ImpegnoBulk;
import it.cnr.contab.doccont00.core.bulk.ImpegnoHome;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voceHome;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioHome;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.doccont00.core.bulk.V_doc_passivo_obbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.V_mod_saldi_obbligBulk;
import it.cnr.contab.doccont00.core.bulk.V_mod_saldi_obbligHome;
import it.cnr.contab.doccont00.core.bulk.V_mod_saldi_obblig_scad_voceBulk;
import it.cnr.contab.doccont00.core.bulk.V_mod_saldi_obblig_scad_voceHome;
import it.cnr.contab.doccont00.ejb.AccertamentoPGiroComponentSession;
import it.cnr.contab.doccont00.ejb.SaldoComponentSession;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashMap;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * Classe che ridefinisce alcune operazioni di CRUD su ImpegnoBulk
 */

/* Gestisce documenti di tipo
	IMP con fl_pgiro = 'Y' - bilancio Ente
	IMP_RES con fl_pgiro = 'Y'- bilancio Ente
	OBB_PGIRO - bilancio Cds
*/	

public class ObbligazioneBilEnteComponent extends it.cnr.jada.comp.CRUDComponent implements IDocumentoContabileMgr,ICRUDMgr,Cloneable,Serializable
{
	private final static int INSERIMENTO 	= 1;
	private final static int MODIFICA    	= 2;
	private final static int CANCELLAZIONE = 3;		

	
//@@<< CONSTRUCTORCST
    public  ObbligazioneBilEnteComponent()
    {
//>>

//<< CONSTRUCTORCSTL
        /*Default constructor*/
//>>

//<< CONSTRUCTORCSTT

    }
public OggettoBulk creaConBulk_ (UserContext userContext, OggettoBulk bulk) throws ComponentException
{
	try
	{
/*
		ImpegnoBulk imp = ( ImpegnoBulk ) bulk;
		//imp.setCrudStatus(OggettoBulk.NORMAL);
		imp = (ImpegnoBulk) super.creaConBulk( userContext, imp );
		//callCreaImpegno (userContext, imp);
		return imp;
*/
		ImpegnoBulk imp = ( ImpegnoBulk ) bulk;
		imp.setCd_cds( imp.getUnita_organizzativa().getCd_unita_padre() );
				
		if ( imp.getCd_unita_organizzativa().equals( imp.getCd_uo_ente()) )
			imp.setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_IMP);
		else
			imp.setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_OBB_PGIRO);
				
		verificaObbligazione( userContext, imp );
		Obbligazione_scadenzarioBulk obblig_scad = creaObbligazione_scadenzario( userContext, imp);
		creaObbligazione_scad_voce( userContext, obblig_scad );
		
		imp = (ImpegnoBulk) super.creaConBulk( userContext, imp );
		
		if ( !userContext.isTransactional() )
			//aggiorna i saldi
			aggiornaCapitoloSaldoObbligazione( userContext, imp, INSERIMENTO );
				
				
		//AccertamentoPGiroBulk accert_pgiro = createAccertamentoPGiroComponent().creaAccertamento( userContext, imp);
			
		//Ass_obb_acr_pgiroBulk ass_oa_pgiro = (Ass_obb_acr_pgiroBulk) creaAss_obb_acr_pgiro( userContext, imp, accert_pgiro);
		verificaStatoEsercizio( 
							userContext, 
							((CNRUserContext)userContext).getEsercizio(), 
							imp.getCd_cds());
		return imp;
	}
	catch ( Exception e )
	{
		throw handleException( bulk, e );
	}	

}

public OggettoBulk modificaConBulk_ (UserContext userContext, OggettoBulk bulk) throws ComponentException
{			
	try
	{
		ImpegnoBulk imp = (ImpegnoBulk) bulk;
		imp = (ImpegnoBulk) super.modificaConBulk( userContext, imp );
		callCreaImpegno (userContext, imp);
		return imp;
	} 
	catch ( Exception e )
	{
		throw handleException( bulk, e );
	}
}

public void callCreaImpegno (UserContext userContext, ImpegnoBulk imp) throws it.cnr.jada.comp.ComponentException
{
	try
	{

		LoggableStatement cs = new LoggableStatement(getConnection( userContext ), 
			"call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
			"CNRCTB030.creaImpegnoBilEnte(?, ?)",false,this.getClass());
		try
		{
			cs.setObject( 1, imp.getEsercizio());			
			cs.setString( 2, imp.getVoce_f().getCd_voce());
			//cs.setString( 3, ((CNRUserContext)userContext).getUser() );
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

/*
  *  creazione obbligazione 
  *    PreCondition:
  *      Un'obbligazione e' stata creata
  *    PostCondition:
  *      Viene chiamato il metodo 'aggiornaSaldiInInserimento' che, per la voce del piano utilizzata nell'obbligazione calcola
  *      l'importo dell' aggiornamento da apportare ai saldi e richiama il metodo sulla Component di Gestione Saldi (SaldoComponent) che
  *      effettua tale aggiornamento
  *  modifica/eliminazione obbligazione
  *    PreCondition:
  *      Un'obbligazione e' stata modificata/eliminata
  *    PostCondition:
  *      Viene chiamato il metodo 'aggiornaSaldiInModifica' che, per la voce del piano utilizzata nell'obbligazione calcola
  *      l'importo dell' aggiornamento da apportare ai saldi e richiama il metodo sulla Component di Gestione Saldi (SaldoComponent) che
  *      effettua tale aggiornamento
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione da salvare
  * @param azione indica l'azione effettuata sull'obbligazione e puo' assumere i valori INSERIMENTO, MODIFICA, CANCELLAZIONE  
  *
 */

private void aggiornaCapitoloSaldoObbligazione (UserContext aUC,ImpegnoBulk obbligazione, int azione) throws ComponentException
{
	try
	{
		Obbligazione_scad_voceBulk osv, osvDaDB;
		// non si aggiornano i saldi di obbligazioni con esercizio di competenza diverso da esercizio di creazione
		if ( obbligazione.getEsercizio().compareTo( obbligazione.getEsercizio_competenza()) != 0 )
			return;
		if ( azione == INSERIMENTO )
			aggiornaSaldiInInserimento( aUC, obbligazione, false );
		else if ( azione == MODIFICA )
			aggiornaSaldiInModifica( aUC, 
											 obbligazione, 
											 obbligazione.getPg_ver_rec(),
											 false);
		else if ( azione == CANCELLAZIONE )
			aggiornaSaldiInModifica( aUC, 
											 obbligazione, 
											 new Long(obbligazione.getPg_ver_rec().longValue() + 1),
											 false);		
			
			

		/*	
		SaldoComponentSession session = createSaldoComponentSession();
		osv = (Obbligazione_scad_voceBulk)
			((Obbligazione_scadenzarioBulk)obbligazione.getObbligazione_scadenzarioColl().get(0)).getObbligazione_scad_voceColl().get(0);

		Voce_fBulk voce = new Voce_fBulk( osv.getCd_voce(), osv.getEsercizio(), osv.getTi_appartenenza(), osv.getTi_gestione() );
		switch( osv.getCrudStatus() )
		{
			case osv.TO_BE_CREATED:
				session.aggiornaObbligazioniAccertamenti( aUC, voce, osv.getCd_cds(), osv.getIm_voce());
				break;
			case osv.TO_BE_DELETED:
				osvDaDB = (Obbligazione_scad_voceBulk) getHome(aUC, Obbligazione_scad_voceBulk.class).findByPrimaryKey( osv);
				session.aggiornaObbligazioniAccertamenti( aUC, voce, osvDaDB.getCd_cds(), osvDaDB.getIm_voce().negate());
				break;
			case osv.TO_BE_UPDATED:
				//rileggo il valore precedente e lo aggiorno
				osvDaDB = (Obbligazione_scad_voceBulk) getHome(aUC, Obbligazione_scad_voceBulk.class).findByPrimaryKey( osv);
				if ( osvDaDB.getIm_voce().compareTo( osv.getIm_voce() ) != 0 )
					session.aggiornaObbligazioniAccertamenti( aUC, voce, osv.getCd_cds(), osv.getIm_voce().subtract( osvDaDB.getIm_voce()));								
				break;
		}
		*/
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}	
}
/**
 * aggiornaCogeCoanInDifferita method comment.
 */
public void aggiornaCogeCoanInDifferita(it.cnr.jada.UserContext userContext, it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docContabile, java.util.Map values) throws it.cnr.jada.comp.ComponentException {
	
	try
	{
		if ( docContabile instanceof ImpegnoBulk )
		{
			ImpegnoBulk impegno = (ImpegnoBulk) docContabile;
			Long pg_ver_rec = (Long) values.get("pg_ver_rec");
			if ( pg_ver_rec == null )
				throw new ApplicationException( "Aggiornamento in differita dello stato coge/coan dei documenti contabili impossibile (pg_ver_rec nullo)");
			if ( impegno.getPg_obbligazione().longValue() >= 0 ) //accertamento non temporaneo
				callDoRiprocObb(userContext, impegno, pg_ver_rec );			
		}
	}
	catch ( Exception e )
	{
		throw handleException(e);
	}
}
/**
 * Aggiornamento in differita dei saldi dell'impegno su bilancio ente.
 * Un documento amministrativo di spesa che agisce in modalità transazionale ha creato/modificato gli importi 
 * relativi ad un impegno; i saldi di tale impegno non possono essere aggiornati subito in quanto
 * tale operazione genererebbe dei lock sulle voci del piano che non ne consentirebbere l'utilizzo ad altri utenti;
 * pertanto l'aggiornamento dei saldi dell'impegno su bilancio ente viene differito al momento del salvataggio
 * del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi per obbligazione su impegno su bilancio ente creata 
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un'impegno su capitoli
 *       che e' stata creata nel contesto transazionale del documento amministrativo ( progressivo
 *       impegno pgiro < 0)
 * Post: I saldi dell'impegno su bilancio ente sono stati aggiornati nel metodo 'aggiornaSaldiInInserimento'
 *
 * Nome: Aggiorna saldi per obbligazione su partita di giro esistente
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un'obbligazione su capitoli di
 *       partita di giro che non e' stata creata nel contesto transazionale del documento amministrativo ( progressivo
 *       impegno pgiro > 0)
 * Post: I saldi dell'obbligazione pgiro sono stati aggiornati nel metodo 'aggiornaSaldiInModifica'
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	docContabile	l'ImpegnoBulk per cui aggiornare i saldi
 * @param	values	la Map che contiene il "pg_ver_rec" iniziale dell'impegno 
 * @param	param parametro non utilizzato per le partite di giro
 * 
*/
public void aggiornaSaldiInDifferita( UserContext userContext, IDocumentoContabileBulk docContabile, Map values, OptionRequestParameter param ) throws ComponentException
{
	try
	{
		if ( docContabile instanceof ImpegnoBulk )
		{
			ImpegnoBulk imp = (ImpegnoBulk) docContabile;
			Long pg_ver_rec = (Long) values.get("pg_ver_rec");
			if ( pg_ver_rec == null )
				throw new ApplicationException( "Aggiornamento in differita dei saldi dei documenti contabili impossibile (pg_ver_rec nullo)");
			if ( imp.getPg_obbligazione().longValue() < 0 ) //obbligazione appena inserita
				aggiornaSaldiInInserimento( userContext, imp, true );
			else
				aggiornaSaldiInModifica( userContext, imp, pg_ver_rec, true );			
		}
	}
	catch ( Exception e )
	{
		throw handleException(e);
	}		
}
/**
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi 
 * Pre:  Un'obbligazione e' stata creata
 * Post: Per la Voce del piano presente nell'obbligazione viene richiamato il metodo sulla Component di gestione dei Saldi (SaldoComponent) per incrementare
 *       il saldo del capitolo corrispondente
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	imp	l'ImpegnoBulk per cui aggiornare i saldi
 * 
 */
private void aggiornaSaldiInInserimento(
	UserContext userContext, 
	ImpegnoBulk imp,
	boolean aggiornaControparte)
	throws ComponentException, it.cnr.jada.persistency.PersistencyException, java.rmi.RemoteException
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
 * @param	imp	l'ImpegnoBulk per cui aggiornare i saldi
 * @param	pg_ver_rec	il "pg_ver_rec" iniziale dell'impegno 
 */
private void aggiornaSaldiInModifica(
	UserContext userContext, 
	ImpegnoBulk imp, 
	Long pg_ver_rec,
	boolean aggiornaControparte) throws it.cnr.jada.persistency.PersistencyException, ComponentException, java.rmi.RemoteException
{
	SaldoComponentSession session = createSaldoComponentSession();			
	List saldiDaAggiornare = ((V_mod_saldi_obbligHome)getHome( userContext, V_mod_saldi_obbligBulk.class )).findModificheSaldiFor( imp, pg_ver_rec );
	if ( userContext.isTransactional() && saldiDaAggiornare.size() == 0 )
		throw new ApplicationException( "Attenzione! I saldi relativi all'impegno " + imp.getEsercizio_originale() + "/" + imp.getPg_obbligazione() + " non possono essere aggiornati perchè l'impegno non e' presente nello storico.");

	String ti_competenza_residuo;
	if ( imp.isResiduo() )
		ti_competenza_residuo = ReversaleBulk.TIPO_RESIDUO;
	else
		ti_competenza_residuo = ReversaleBulk.TIPO_COMPETENZA;
		
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
		    session.aggiornaMandatiReversali( userContext,modSaldo.getCd_centro_responsabilita(),modSaldo.getCd_linea_attivita(),voce,modSaldo.getEsercizio_originale(),modSaldo.getIm_delta_man_voce(),modSaldo.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA)?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO);

		if ( modSaldo.getIm_delta_pag_voce().compareTo( new BigDecimal(0) ) != 0 )
		    session.aggiornaPagamentiIncassi( userContext,modSaldo.getCd_centro_responsabilita(),modSaldo.getCd_linea_attivita(),voce,modSaldo.getEsercizio_originale(),modSaldo.getIm_delta_pag_voce());
		
	}
}
/** 
  *  aggiornamento Stato COAN o COGE dei Documenti Amministrativi
  *    PreCondition:
  *      Nel caso un'impegno sia stato eliminato o modificato, occorre
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
  *  cancellazione (logica)
  *    PreCondition:
  *      L'utente richiede la cancellazione di un impegno su bilancio ente
  *  errore - doc.amm.associati
  *    PreCondition:
  *      La richiesta di cancellazione di un impegno su bilancio ente e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'obbligazione
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la cancellazione
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param imp <code>ImpegnoBulk</code> l'impegno su bilancio ente da cancellare (logicamente)
  * @return imp <code>ImpegnoBulk</code> l'impegno su bilancio ente annullato
  *
 */
public ImpegnoBulk annullaObbligazione(UserContext userContext, ImpegnoBulk imp ) throws ComponentException
{
	try
	{
		verificaStatoEsercizio( userContext, imp.getEsercizio(), imp.getCd_cds());
		//	segnalo impossibilità di annullare un residuo se l'esercizio precedente è ancora aperto
		if ( imp.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_IMP_RES))
			verificaStatoEsercizioEsPrecedente( userContext, imp.getEsercizio(), imp.getCd_cds());
		
			
		if ( imp.isAssociataADocAmm() )
			throw new ApplicationException( "Impossibile cancellare l'Impegno su bilancio Ente perche' ha documenti amministrativi associati");

		// annullo il record relativo all'accertamento partita di giro
		//if ( !imp.isFl_isTronco() )
		//	createAccertamentoPGiroComponent().eliminaAccertamento( userContext, imp.getAssociazione().getAccertamento());

		// annullo il record relativo all'obbligazione partita di giro
		eliminaObbligazione( userContext, imp);
		
		return imp;

	}
	catch ( Exception e )
	{
		throw handleException(imp, e)	;
	}

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
		LoggableStatement cs =new LoggableStatement(getConnection( userContext ), 
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
  *		Il sistema identifica quale delle 2 parti della partita di giro deve essere passata come parametro
  *      alla stored procedure (metodo 'findPGiroDaRiportareAvanti').
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
  *  riporta indietro dall'esercizio successivo di un doc.contabile
  *    PreCondition:
  *      E' stata inoltrata una richiesta di riportare indietro dall'esercizio successivo un documento contabile
  *	 PostCondition:
  *		Il sistema identifica quale delle 2 parti della partita di giro deve essere passata come parametro
  *      alla stored procedure (metodo 'findPGiroDaRiportareIndietro').
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
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ), 
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
  *  creazione
  *    PreCondition:
  *      L'utente richiede la creazione di un nuovo impegno su partita di giro
  *    PostCondition:
  *      L'impegno, dopo essere stato validato (metodo verificaObbligazione), viene creato e in automatico viene creata una 
  *		 scadenza (metodo creaObbligazione_scadenzario) e un dettaglio di scadenza (metodo creaObbligazione_scad_voce). 
  *		 I saldi relativi alla voce del piano dell'obbligazione vengono aggiornati (metodo aggiornaCapitoloSaldoObbligazione). 
  *		 Alla componente che gestisce gli AccertamentiPGiro viene chiesta la creazione di un Accertamento (metodo creaAccertamento). 
  *		 Viene creata l'associazione (Ass_obb_acr_pgiroBulk) fra l'impegno e l'accertamento su partita di giro (metodo creaAss_obb_acr_pgiro)
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno su partita di giro da creare
  *
  * @return imp L'impegno su partita di giro creato
 */
public OggettoBulk creaConBulk (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	try
	{
		ImpegnoBulk imp = ( ImpegnoBulk ) bulk;

		// controlliamo che non esista già un impegno su bilancio ente per la voce selezionata
		//ObbligazioneHome vocehome =(ObbligazioneHome)getHome(uc, ObbligazioneBulk.class);
		Obbligazione_scad_voceHome vocehome =(Obbligazione_scad_voceHome)getHome(uc, Obbligazione_scad_voceBulk.class);
		SQLBuilder sql = vocehome.createSQLBuilder();
		sql.addTableToHeader("OBBLIGAZIONE");
		sql.addSQLJoin("OBBLIGAZIONE_SCAD_VOCE.CD_CDS","OBBLIGAZIONE.CD_CDS");
		sql.addSQLJoin("OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO","OBBLIGAZIONE.ESERCIZIO");
		sql.addSQLJoin("OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO_ORIGINALE","OBBLIGAZIONE.ESERCIZIO_ORIGINALE");
		sql.addSQLJoin("OBBLIGAZIONE_SCAD_VOCE.PG_OBBLIGAZIONE","OBBLIGAZIONE.PG_OBBLIGAZIONE");
		sql.addClause("AND","esercizio",sql.EQUALS, imp.getEsercizio() );
		sql.addClause("AND","cd_cds",sql.EQUALS, imp.getUnita_organizzativa().getCd_unita_padre() );
		sql.addClause("AND","cd_voce",sql.EQUALS, imp.getVoce_f().getCd_voce() );
		sql.addSQLClause("AND","cd_tipo_documento_cont",sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP);

		List result = vocehome.fetchAll( sql );
		if (!result.isEmpty())
			throw new ApplicationException("Impossibile creare l'impegno con la voce del piano selezionata poichè è già stato creato un impegno su bilancio ente per tale voce.");

		imp.setCd_cds( imp.getUnita_organizzativa().getCd_unita_padre() );
		
		if ( imp.getCd_unita_organizzativa().equals( imp.getCd_uo_ente()) )
			imp.setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_IMP);
		else
			imp.setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_OBB_PGIRO);
		
		verificaObbligazione( uc, imp );

		// impostiamo la voce del piano in testata coerentemente con quella nelle scadenze
		// viene già impostato nel doBringBack ma lo rifacciamo
		if (imp.getVoce_f()!=null)
			imp.setElemento_voce(((ImpegnoHome)getHome(uc, ImpegnoBulk.class)).findElementoVoceFor(imp.getVoce_f()));

		Obbligazione_scadenzarioBulk obblig_scad = creaObbligazione_scadenzario( uc, imp);
		creaObbligazione_scad_voce( uc, obblig_scad );

		imp = (ImpegnoBulk) super.creaConBulk( uc, imp );

		if ( !uc.isTransactional() )
			//aggiorna i saldi
			aggiornaCapitoloSaldoObbligazione( uc, imp, INSERIMENTO );
		
		
		//AccertamentoPGiroBulk accert_pgiro = createAccertamentoPGiroComponent().creaAccertamento( uc, imp);
	
		//Ass_obb_acr_pgiroBulk ass_oa_pgiro = (Ass_obb_acr_pgiroBulk) creaAss_obb_acr_pgiro( uc, imp, accert_pgiro);
		verificaStatoEsercizio( 
							uc, 
							((CNRUserContext)uc).getEsercizio(), 
							imp.getCd_cds());

		return imp;
	}
	catch ( Exception e )
	{
		throw handleException( bulk, e );
	}	

}
/** 
  *  creazione per il Cds
  *    PreCondition:
  *      Un Accertamento su partita di giro per il Cds e' stato creato ed e' necessario creare il corrispondente Impegno
  *    PostCondition:
  *      L'impegno (ImpegnoBulk) viene creato con importo pari a quello dell'accertamento, codice terzo
  *      recuperato dalla Configurazione CNR come codice DIVERSI per PARTITA di GIRO, capitolo di entrata ricavato
  *      (metodo findVoce_f) dall'associazione fra Capitoli di Entrata e di Spesa per Partita di Giro, cds e unità
  *		 organizzativa di appartenenza e di origine uguali a quelli dell'accertamento, data di scadenza uguale a 
  *		 quella della scadenza dell'accertamento su partita di giro.
  *      Viene inoltre creata una scadenza (metodo creaObbligazione_scadenzario) e
  *      un dettaglio di scadenza (metodo creaObbligazione_scad_voce). I saldi relativi alla voce del piano
  *      dell'obbligazione vengono aggiornati (metodo aggiornaCapitoloSaldoObbligazione).
  *		 Viene infine validata l'Obbligazione prima della sua creazione (metodo verificaObbligazione)
  *  creazione per il CNR
  *    PreCondition:
  *      Un Accertamento su partita di giro per il CNR e' stato creato ed e' necessario creare il corrispondente Impegno
  *    PostCondition:
  *      L'impegno (ImpegnoBulk) viene creato con importo pari a quello dell'accertamento, codice terzo
  *      recuperato dalla Configurazione CNR come codice DIVERSI per PARTITA di GIRO, capitolo di entrata ricavato
  *      (metodo findVoce_f) dall'associazione fra Capitoli di Entrata e di Spesa per Partita di Giro, cds e unità
  *		 organizzativa di appartenenza uguali a quelli dell'accertamento e cds e unità organizzativa di origine
  *		 uguali a quelli dell'ente.
  *      Viene inoltre creata una scadenza (metodo creaObbligazione_scadenzario) e
  *      un dettaglio di scadenza (metodo creaObbligazione_scad_voce). I saldi relativi alla voce del piano
  *      dell'obbligazione vengono aggiornati (metodo aggiornaCapitoloSaldoObbligazione).
  *		 Viene infine validata l'Obbligazione prima della sua creazione (metodo verificaObbligazione)
  *  errore - Configurazione CNR per codice DIVERSI su PGIRO
  *    PreCondition:
  *      non e' presente in Configurazione CNR la definizione del CODICE DIVERSI per PGIRO
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente la mancata definizione in Configurazione CNR del CODICE DIVERSI per PGIRO
  *  errore - Anagrafica per codice DIVERSI su PGIRO
  *    PreCondition:
  *      non e' presente in Anagrafica il codice terzo presente in Configurazione CNR come CODICE DIVERSI per PGIRO
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente la mancata definizione in Anagrafica
  *  errore - Associazione capitoli entrata/spese
  *    PreCondition:
  *      non e' presente (Ass_partita_giroBulk) l'associazione fra il capitolo di entrata dell'accertamento e un capitolo di spesa
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'assenza dell'associazione
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param accert_pgiro <code>AccertamentoPGiroBulk</code> l'accertamento su partita di giro creato
  *
  * @return imp L'impegno su partita di giro creato in corrispondenza dell'accertamento
*/
public ImpegnoBulk creaObbligazione(
    UserContext uc,
    AccertamentoPGiroBulk accert_pgiro)
    throws ComponentException {
    try {
        /* IMPEGNO PARTITA GIRO BULK */
        ImpegnoBulk imp = new ImpegnoBulk();
        imp.setUser(accert_pgiro.getUser());
        imp.setToBeCreated();

        // campi chiave
        imp.setEsercizio(accert_pgiro.getEsercizio());
        imp.setCd_cds(accert_pgiro.getCd_cds());
		imp.setEsercizio_originale(accert_pgiro.getEsercizio_originale());

        // altri campi...
        imp.setDt_scadenza(accert_pgiro.getDt_scadenza());
        if (accert_pgiro
            .getCd_tipo_documento_cont()
            .equals(Numerazione_doc_contBulk.TIPO_ACR))
            imp.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_IMP);
        else
            imp.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_OBB_PGIRO);

        imp.setCd_unita_organizzativa(accert_pgiro.getCd_unita_organizzativa());
        /* 17.5.2002 modificato Simona
        if ( imp.getCd_unita_organizzativa().equals( accert_pgiro.getCd_uo_ente()))
        {
        	imp.setCd_cds_origine( accert_pgiro.getCd_cds() );
        	imp.setCd_uo_origine( accert_pgiro.getCd_unita_organizzativa() );
        }
        else
        {
        	imp.setCd_cds_origine( accert_pgiro.getCd_cds_origine() );
        	imp.setCd_uo_origine( accert_pgiro.getCd_uo_origine() );
        }
        */
        imp.setCd_cds_origine(accert_pgiro.getCd_cds_origine());
        imp.setCd_uo_origine(accert_pgiro.getCd_uo_origine());

        imp.setDt_registrazione(accert_pgiro.getDt_registrazione());
        imp.setDs_obbligazione(
            "Annotazione di Spesa su Partita di Giro creata in automatico dal CdS: "
                .concat(
                accert_pgiro.getCd_cds_origine()));
        //gestione x impegni tronchi
        if (!accert_pgiro.isFl_isTronco())
        {
	       imp.setIm_obbligazione(accert_pgiro.getIm_accertamento());
	       imp.setStato_obbligazione(imp.STATO_OBB_DEFINITIVO);            
        }   
        else 
        {
            imp.setIm_obbligazione(new BigDecimal(0));
            imp.setFl_isTronco(true);
	       	imp.setStato_obbligazione(imp.STATO_OBB_STORNATO);                        
            imp.setDt_cancellazione(accert_pgiro.getDt_registrazione());
        }
        imp.setFl_calcolo_automatico(Boolean.TRUE);

        imp.setFl_spese_costi_altrui(Boolean.FALSE);
        imp.setFl_gara_in_corso(Boolean.FALSE);
		imp.setFl_determina_allegata(Boolean.FALSE);
        imp.setEsercizio_competenza(accert_pgiro.getEsercizio());
        imp.setIm_costi_anticipati(new java.math.BigDecimal(0));
        imp.setRiportato("N");

        // ...e in particolare il campo cd_terzo
        it.cnr.contab.config00.bulk.Configurazione_cnrBulk config =
            createConfigurazioneCnrComponentSession().getConfigurazione(
                uc,
                null,
                null,
                it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_TERZO_SPECIALE,
                it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_CODICE_DIVERSI_PGIRO);
        if (config == null)
            throw new ApplicationException("Configurazione CNR: manca la definizione del CODICE DIVERSI per partite di giro");

        if (config.getIm01() == null)
            throw new ApplicationException("Configurazione CNR: manca il CODICE TERZI nella definizione del codice diversi per partite di giro");

        SQLBuilder sql =
            getHomeCache(uc)
                .getHome(it.cnr.contab.anagraf00.core.bulk.TerzoBulk.class)
                .createSQLBuilder();
        sql.addClause(
            "AND",
            "cd_terzo",
            sql.EQUALS,
            new Integer(config.getIm01().intValue()));
        List result =
            getHomeCache(uc)
                .getHome(it.cnr.contab.anagraf00.core.bulk.TerzoBulk.class)
                .fetchAll(sql);
        if (result.size() == 0)
            throw new ApplicationException("Il terzo DIVERSI per partita di giro non e' presente in anagrafica");

        imp.setCd_terzo(
            ((it.cnr.contab.anagraf00.core.bulk.TerzoBulk) result.get(0)).getCd_terzo());
        Ass_partita_giroHome ass_pgiroHome =
            (Ass_partita_giroHome) getHome(uc, Ass_partita_giroBulk.class);
		Ass_partita_giroBulk ass_pgiro = ass_pgiroHome.getAssociazionePGiroFor(accert_pgiro);
		
        imp.setTi_appartenenza(ass_pgiro.getTi_appartenenza_clg());
        imp.setTi_gestione(ass_pgiro.getTi_gestione_clg());
        imp.setCd_elemento_voce(ass_pgiro.getCd_voce_clg());

        Obbligazione_scadenzarioBulk obblig_scad =
            creaObbligazione_scadenzario(uc, imp);
        // creaAccertamento_scad_voce( uc, accert_scad, (Obbligazione_scad_voceBulk)(((Obbligazione_scadenzarioBulk)imp.getObbligazione_scadenzarioColl().get(0)).getObbligazione_scad_voceColl().get(0)));
        creaObbligazione_scad_voce(uc, obblig_scad);

        verificaObbligazione(uc, imp);

        imp = (ImpegnoBulk) super.creaConBulk(uc, imp);

        if (!uc.isTransactional())
            aggiornaCapitoloSaldoObbligazione(uc, imp, INSERIMENTO);

        return imp;
    } catch (Exception e) {
        throw handleException(e);

    }
}
/** 
  *  creazione
  *    PreCondition:
  *      una scadenza di un impegno pgiro e' stata creata ed e' necessario creare il suo dettaglio
  *    PostCondition:
  *      Il dettaglio di scadenza (Obbligazione_scad_voceBulk) viene creato
  *      con linea attività uguale alla linea di attività definita nella Configurazione CNR come Linea attività Spesa ENTE
  *      e importo uguale all'importo della scadenza dell'obbligazione
  *  errore - Configurazione CNR
  *    PreCondition:
  *      una richiesta di creazione di un dettaglio di scadenza di un impegno pgiro e' stata generata
  *      ma non e' stata definita in Configurazione CNR la Linea attività Spesa ENTE
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente la mancata definizione in Configurazione CNR della linea di attivita' Spesa ENTE
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param obblig_scad <code>Obbligazione_scadenzarioBulk</code> la scadenza dell'impegno pgiro creata
  *
  * @return obblig_scad_voce Il dettaglio di scadenza dell'impegno pgiro creato
 */
private Obbligazione_scad_voceBulk creaObbligazione_scad_voce (UserContext uc,Obbligazione_scadenzarioBulk obblig_scad) throws ComponentException
{
	try
	{
		Obbligazione_scad_voceBulk obblig_scad_voce = new Obbligazione_scad_voceBulk();
		obblig_scad_voce.setUser( obblig_scad.getObbligazione().getUser() );
		obblig_scad_voce.setToBeCreated();

		// campi chiave
		obblig_scad_voce.setObbligazione_scadenzario( obblig_scad );

		// altri campi chiave
		obblig_scad_voce.setTi_appartenenza( obblig_scad.getObbligazione().getTi_appartenenza() );
		obblig_scad_voce.setTi_gestione( obblig_scad.getObbligazione().getTi_gestione() );
		obblig_scad_voce.setCd_voce(((ImpegnoBulk)(obblig_scad.getObbligazione())).getVoce_f().getCd_voce() );

		it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = createConfigurazioneCnrComponentSession().getConfigurazione( uc, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LINEA_ATTIVITA_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_LINEA_ATTIVITA_SPESA_ENTE );
		if ( config != null  )
		{
			it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt = 
				new it.cnr.contab.config00.latt.bulk.WorkpackageBulk( config.getVal01(), config.getVal02());	
				obblig_scad_voce.setLinea_attivita( latt );
		}
		else
			throw new ApplicationException("Configurazione CNR: manca la definizione del GAE SPESA ENTE");

		
		// altri campi
		obblig_scad_voce.setIm_voce( obblig_scad.getObbligazione().getIm_obbligazione()  );
		//obblig_scad_voce.setCd_fondo_ricerca( obblig_scad.getObbligazione().getCd_fondo_ricerca() );
	
		obblig_scad.getObbligazione_scad_voceColl().add( obblig_scad_voce );

		return obblig_scad_voce;
		
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}
}
/** 
  *  creazione
  *    PreCondition:
  *      un impegno pgiro e' stato creato ed e' necessario creare la scadenza ad esso associata
  *    PostCondition:
  *      Viene creata una scadenza (Obbligazione_scadenzarioBulk) di obbligazione su partita di giro 
  *		 con data uguale alla data di scadenza dell'obbligazione e con importo pari a quello dell'obbligazione.
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param imp <code>ImpegnoBulk</code> l'impegno su partita di giro creato
  *
  * @return obblig_scad La scadenza dell'impegno pgiro creata
 */
private Obbligazione_scadenzarioBulk creaObbligazione_scadenzario (UserContext uc, ImpegnoBulk imp) throws ComponentException
{

	// gc.set((imp.getEsercizio_competenza()).intValue(),1,1);
	//Timestamp time = new Timestamp(gc.MILLISECOND);

		Obbligazione_scadenzarioBulk obblig_scad = new Obbligazione_scadenzarioBulk();
		/* simona 12/7 
		imp.getObbligazione_scadenzarioColl().add( obblig_scad ); */
		imp.addToObbligazione_scadenzarioColl( obblig_scad );
		
		obblig_scad.setUser( imp.getUser() );
		obblig_scad.setToBeCreated();

		// campi chiave
		// obblig_scad.setObbligazione( imp );
 
		// altri campi
		obblig_scad.setDt_scadenza( imp.getDt_scadenza() );
		/*
		// controllo se l'anno di emissione dell'impegno è maggiore dell'anno corrente
		gc.set(java.util.GregorianCalendar.MONTH,1);
		gc.set(java.util.GregorianCalendar.DATE,1);
		
		if (imp.getEsercizio_competenza().intValue() > gc.get(java.util.GregorianCalendar.YEAR) )
			obblig_scad.setDt_scadenza( java.sql.Timestamp.valueOf( imp.getEsercizio_competenza().intValue() + "-" + 
																   		    gc.get(java.util.GregorianCalendar.MONTH) + "-" +
																   		    gc.get(java.util.GregorianCalendar.DATE) +" 00:00:00.0" ) );
		else
			obblig_scad.setDt_scadenza( imp.getDt_registrazione() );
		*/	
		
		obblig_scad.setDs_scadenza( imp.getDs_obbligazione() );
		obblig_scad.setIm_scadenza( imp.getIm_obbligazione() );
		return obblig_scad;
		
}
/**
 * Crea la ComponentSession da usare per effettuare le operazioni di CRUD su Accertamenti in Partita di Giro
 *
 * @return AccertamentoComponentSession l'istanza di <code>AccertamentoComponentSession</code> che serve per gestire un accertamento
 */
private AccertamentoPGiroComponentSession createAccertamentoPGiroComponent( ) throws it.cnr.jada.comp.ComponentException
{
	try
	{
		return (AccertamentoPGiroComponentSession)EJBCommonServices.createEJB("CNRDOCCONT00_EJB_AccertamentoPGiroComponentSession");

	}
	catch (Exception e )
	{
		throw handleException( e ) ;
	}		
}
/**
 * Crea la ComponentSession da usare per effettuare le operazioni di lettura della Configurazione CNR
 *
 * @return Configurazione_cnrComponentSession l'istanza di <code>Configurazione_cnrComponentSession</code> che serve per leggere i parametri di configurazione del CNR
 */
private Configurazione_cnrComponentSession createConfigurazioneCnrComponentSession() throws ComponentException 
{
	try
	{
		return (Configurazione_cnrComponentSession)EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
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
  *  cancellazione (logica)
  *    PreCondition:
  *      L'utente richiede la cancellazione di un impegno su partita di giro
  *    PostCondition:
  *     Viene inoltrata la richiesta di cancellazione (logica) dell'impegno su partita di giro
  *		(metodo annullaObbligazione)
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno su partita di giro da cancellare (logicamente)
  *
 */
public void eliminaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	try
	{
		annullaObbligazione( aUC, (ImpegnoBulk) bulk);
	}
	catch ( Exception e )
	{
		throw handleException(bulk, e)	;
	}
}
/** 
  *  cancellazione (logica)
  *    PreCondition:
  *      La richiesta di cancellazione di un'obbligazione su partita di giro e' stata generata
  *    PostCondition:
  *      L'obbligazione, la sua scadenza e il suo dettaglio vengono cancellati (logicamente). I saldi relativi 
  *		ai documenti contabili vengono aggiornati (metodo aggiornaCapitoloSaldoObbligazione).
  *  errore - doc.amm.associati
  *    PreCondition:
  *      La richiesta di cancellazione di un'obbligazione su partita di giro e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'obbligazione
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la cancellazione
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param imp <code>ImpegnoBulk</code> l'impegno su partita di giro da cancellare (logicamente)
  *
 */
public void eliminaObbligazione(UserContext aUC,ImpegnoBulk imp ) throws ComponentException
{
	try
	{
		if ( imp.isAssociataADocAmm() )
			throw new ApplicationException( "Impossibile cancellare l'Impegno su bilancio Ente perche' ha documenti amministrativi associati");

		/*	
		imp.setToBeDeleted();
		makeBulkPersistent( aUC, imp);
		aggiornaCapitoloSaldoObbligazione( aUC, imp, CANCELLAZIONE);
		*/
		imp.setCrudStatus(OggettoBulk.NORMAL);		
        imp.storna();
    //    imp.setDt_cancellazione( getHome(aUC, imp.getClass()).getServerDate());
        imp.setDt_cancellazione( DateServices.getDt_valida(aUC));    

	  // aggiornamento obbligazione + scadenze + dettagli
      makeBulkPersistent( aUC, imp);

      //aggiorno i saldi
      aggiornaCapitoloSaldoObbligazione(aUC, imp, MODIFICA);		
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}
}
/** 
  *  ricerca Unità Organizzative
  *    PreCondition:
  *      La richiesta di identificazione delle Unità Organizzative per cui e' possibile creare un'impegno
  *      è stata generata
  *    PostCondition:
  *      Una lista contente l'UO Ente + l'UO di scrivania (se diverso da Ente) viene restituita
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param impegno <code>ImpegnoBulk</code> l'impegno su partita di giro da creare
  *
  * @return result la lista delle unità organizzative definite per l'impegno su partita di giro
 */
public List findUnitaOrganizzativaOptions (UserContext userContext,ImpegnoBulk impegno) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
{
	SQLBuilder sql = getHome( userContext, Unita_organizzativa_enteBulk.class ).createSQLBuilder();
	List result = getHome( userContext, Unita_organizzativa_enteBulk.class ).fetchAll( sql );
	impegno.setCd_uo_ente( ((Unita_organizzativaBulk)result.get(0)).getCd_unita_organizzativa());
	if ( !((Unita_organizzativaBulk)result.get(0)).getCd_unita_organizzativa().equals( impegno.getUnita_organizzativa().getCd_unita_organizzativa()))
		result.add( impegno.getUnita_organizzativa() );
	return result;
}
/** 
  *  inizializzazione per inserimento
  *    PreCondition:
  *      La richiesta di inizializzazione di un ImpegnoBulk per inserimento
  *      e' stata generata
  *    PostCondition:
  *      Viene impostata la data di registrazione dell'obbligazione con la data odierna, 
  *		 il codice Cds e il codice Cds di origine con il codice Cds di scrivania
  *  inizializzazione per inserimento - errore
  *    PreCondition:
  *      L'unità organizzativa è uguale a quella dell'Ente
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente che l'Ente non è abilitato a creare documenti su partita di giro
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno su partita di giro da inizializzare per l'inserimento
  *
  * @return <code>OggettoBulk</code> l'impegno su partita di giro inizializzato per l'inserimento
 */
public OggettoBulk inizializzaBulkPerInserimento (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	ImpegnoBulk imp = (ImpegnoBulk) bulk;

	try
	{
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk)getHome( aUC, Unita_organizzativa_enteBulk.class).findAll().get(0);
			
		it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = createConfigurazioneCnrComponentSession().getConfigurazione( aUC, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_TERZO_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_CODICE_DIVERSI_IMPEGNI);
		if ( config == null  )
			throw new ApplicationException("Configurazione CNR: manca la definizione del TERZO_SPECIALE");	
		if ( config.getIm01() == null )
			throw new ApplicationException("Configurazione CNR: non sono stati impostati i valori per TERZO_SPECIALE - CODICE_DIVERSI_IMPEGNI");			
		TerzoHome terzohome = (TerzoHome) getHome(aUC, TerzoBulk.class);
		TerzoBulk cred = (TerzoBulk) terzohome.findByPrimaryKey(new TerzoBulk(new Integer(config.getIm01().intValue())));
		imp.setCreditore(cred);

		imp.setDt_registrazione( DateServices.getDt_valida( aUC) );
		imp.setDt_scadenza(DateServices.getDt_valida( aUC));
		imp.setCds( (CdsBulk) getHome( aUC, CdsBulk.class).findByPrimaryKey( imp.getUnita_organizzativa().getUnita_padre() ));
		imp.setCd_cds_origine( ((CNRUserContext) aUC).getCd_cds() );
		verificaStatoEsercizio( aUC, imp.getEsercizio(), imp.getCd_cds());
		/* if (!((ObbligazioneHome)getHome(aUC, obbligazione.getClass())).verificaStatoEsercizio(obbligazione))
			throw handleException( new ApplicationException( "Non e' possibile creare obbligazioni: esercizio non ancora aperto!") );*/
			
		// se l'unità organizzativa NON è uguale a quella dell'Ente, non è possibile creare documenti su partita di giro
		if ( !imp.getCd_unita_organizzativa().equals( uoEnte.getCd_unita_organizzativa() ))
			throw new ApplicationException("Funzione consentita unicamente per utente abilitato a " + uoEnte.getCd_unita_organizzativa() ); 
		return super.inizializzaBulkPerInserimento( aUC, imp );
	}
	catch ( Exception e )
	{
		throw handleException(imp, e);
	}
}
/** 
  *  inizializzazione per modifica
  *    PreCondition:
  *      La richiesta di inizializzazione di un ImpegnoBulk per modifica
  *      e' stata generata
  *    PostCondition:
  *      Vengono recuperati la scadenza e il dettaglio di scadenza associati all'impegno.
  *      Viene recuperata l'associazione fra l'impegno e l'accertamento
  *		 Viene recuperato l'accertamento associato all'impegno e la relativa scadenza e dettaglio scadenza
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno su partita di giro da inizializzare per la modifica
  *
  * @return imp l'impegno su partita di giro inizializzato per la modifica
 */
public OggettoBulk inizializzaBulkPerModifica (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	try
	{
		bulk = super.inizializzaBulkPerModifica( aUC, bulk );
		
		ImpegnoBulk imp = (ImpegnoBulk) bulk;
		AccertamentoPGiroBulk accert_pgiro = null;		

		imp.setCd_terzo_iniziale( imp.getCd_terzo());
		imp.setIm_iniziale_obbligazione( imp.getIm_obbligazione());
	
	//query per recuperare la scadenza dell'obbligazione
	//impegnoPgiro.getObbligazione_scadenzarioColl().add( scadenza );
		Obbligazione_scadenzarioHome obblig_scadHome = (Obbligazione_scadenzarioHome) getHome( aUC, Obbligazione_scadenzarioBulk.class );

		SQLBuilder sql = obblig_scadHome.createSQLBuilder();
		sql.addClause("AND","cd_cds",sql.EQUALS, imp.getCd_cds() );
		sql.addClause("AND","esercizio",sql.EQUALS, imp.getEsercizio() );
		sql.addClause("AND","esercizio_originale",sql.EQUALS, imp.getEsercizio_originale() );
		sql.addClause("AND","pg_obbligazione",sql.EQUALS, imp.getPg_obbligazione() );

		List result = obblig_scadHome.fetchAll( sql );
		Obbligazione_scadenzarioBulk obblig_scad = (Obbligazione_scadenzarioBulk) result.get(0);

		imp.setDt_scadenza( obblig_scad.getDt_scadenza());
		imp.getObbligazione_scadenzarioColl().add( obblig_scad );
		obblig_scad.setObbligazione( imp);

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

		//query per recuperare cd_uo_ente dell'impegno
		Unita_organizzativa_enteHome uo_enteHome = (Unita_organizzativa_enteHome) getHome( aUC, Unita_organizzativa_enteBulk.class );
		sql = uo_enteHome.createSQLBuilder();
		result = uo_enteHome.fetchAll( sql );
		Unita_organizzativa_enteBulk uo_ente = (Unita_organizzativa_enteBulk) result.get(0);

		imp.setCd_uo_ente( uo_ente.getCd_unita_organizzativa() );
		if ( accert_pgiro!= null )
		{
			accert_pgiro.setCd_uo_ente( uo_ente.getCd_unita_organizzativa() );

			if (( accert_pgiro.getDt_cancellazione() == null && imp.getDt_cancellazione() != null ) ||
				 ( accert_pgiro.getDt_cancellazione() != null && imp.getDt_cancellazione() == null ) )
			{	 
				 accert_pgiro.setFl_isTronco( true );
			 	imp.setFl_isTronco( true );
			}
		}	
		
		// valorizziamo la voce presa dall'unica scadenza-voce inserita
		if (imp.hasDettagli()) {
			Voce_fBulk voce = ((Obbligazione_scad_voceBulk)((Obbligazione_scadenzarioBulk)imp.getObbligazione_scadenzarioColl().get(0)).getObbligazione_scad_voceColl().get(0)).getVoce_f();
			voce = (Voce_fBulk) getHome(aUC, voce).findByPrimaryKey(voce);
			imp.setVoce_f(voce);
		}

	return imp;
	}
	catch ( Exception e )
	{
		throw handleException(bulk, e)	;
	}
}
/** 
  *  inizializzazione per ricerca
  *    PreCondition:
  *      La richiesta di inizializzazione di un ImpegnoBulk per ricerca
  *      e' stata generata
  *    PostCondition:
  *      Vengono impostati il codice Cds e il codice Cds di origine con il codice Cds di scrivania
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno su partita di giro da inizializzare per la ricerca
  *
  * @return <code>OggettoBulk</code> l'impegno su partita di giro inizializzato per la ricerca
 */
public OggettoBulk inizializzaBulkPerRicerca (UserContext userContext,OggettoBulk bulk) throws ComponentException
{
	ImpegnoBulk imp = (ImpegnoBulk) bulk;
	try
	{
		/*
		imp.setCds( (CdsBulk) getHome( aUC, CdsBulk.class).findByPrimaryKey( imp.getUnita_organizzativa().getUnita_padre() ));
		imp.setCd_cds_origine( ((CNRUserContext) aUC).getCd_cds() );
		// if (!((ObbligazioneHome)getHome(aUC, obbligazione.getClass())).verificaStatoEsercizio(obbligazione))
		//	throw handleException( new ApplicationException( "Non e' possibile creare obbligazioni: esercizio non ancora aperto!") );
		return super.inizializzaBulkPerRicerca( aUC, imp );
		*/
		imp = (ImpegnoBulk) super.inizializzaBulkPerRicerca( userContext, imp );
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0); 
		//imposto cds e uo origine
		if ( !uoEnte.getCd_unita_organizzativa().equals(((CNRUserContext)userContext).getCd_unita_organizzativa()))
		{
			Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHome( userContext, Unita_organizzativaBulk.class ).findByPrimaryKey( new Unita_organizzativaBulk( ((CNRUserContext)userContext).getCd_unita_organizzativa()));
			imp.setCd_uo_origine( uoScrivania.getCd_unita_organizzativa());		
			imp.setCd_cds_origine( uoScrivania.getCd_unita_padre());
			if ( imp.isResiduo() )
			{
				imp.setCd_cds( uoEnte.getCd_unita_padre());
				imp.setCd_unita_organizzativa( uoEnte.getCd_unita_organizzativa());
			}	
		}
		else
		{
			imp.setCd_cds( uoEnte.getCd_unita_padre());
			imp.setCd_unita_organizzativa( uoEnte.getCd_unita_organizzativa());					
		}
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

public OggettoBulk modificaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException
{			
	try
	{
		ImpegnoBulk imp = (ImpegnoBulk) bulk;

		verificaStatoEsercizio( aUC, imp.getEsercizio(), imp.getCd_cds());		

		// controlliamo che non esista già un impegno su bilancio ente per la voce selezionata
		//ObbligazioneHome vocehome =(ObbligazioneHome)getHome(uc, ObbligazioneBulk.class);
		Obbligazione_scad_voceHome vocehome =(Obbligazione_scad_voceHome)getHome(aUC, Obbligazione_scad_voceBulk.class);
		SQLBuilder sql = vocehome.createSQLBuilder();
		sql.addTableToHeader("OBBLIGAZIONE");
		sql.addSQLJoin("OBBLIGAZIONE_SCAD_VOCE.CD_CDS","OBBLIGAZIONE.CD_CDS");
		sql.addSQLJoin("OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO","OBBLIGAZIONE.ESERCIZIO");
		sql.addSQLJoin("OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO_ORIGINALE","OBBLIGAZIONE.ESERCIZIO_ORIGINALE");
		sql.addSQLJoin("OBBLIGAZIONE_SCAD_VOCE.PG_OBBLIGAZIONE","OBBLIGAZIONE.PG_OBBLIGAZIONE");
		sql.addClause("AND","esercizio",sql.EQUALS, imp.getEsercizio() );
		sql.addClause("AND","cd_cds",sql.EQUALS, imp.getUnita_organizzativa().getCd_unita_padre() );
		sql.addClause("AND","cd_voce",sql.EQUALS, imp.getVoce_f().getCd_voce() );
		sql.addSQLClause("AND","cd_tipo_documento_cont",sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP);
		// deve essere escluso l'impegno che si sta modificando
		sql.openParenthesis("AND");
		sql.addClause("AND","esercizio_originale",sql.NOT_EQUALS, imp.getEsercizio_originale() );
		sql.addClause("OR","pg_obbligazione",sql.NOT_EQUALS, imp.getPg_obbligazione() );
		sql.closeParenthesis();

		List result = vocehome.fetchAll( sql );
		if (!result.isEmpty())
			throw new ApplicationException("Impossibile creare l'impegno con la voce del piano selezionata poichè è già stato creato un impegno su bilancio ente per tale voce.");

		//segnalo impossibilità di modificare importo se ci sono doc amministrativi associati
		if ( !imp.isFromDocAmm() && 
			imp.isAssociataADocAmm() && imp.getIm_iniziale_obbligazione() != null &&
			imp.getIm_iniziale_obbligazione().compareTo( imp.getIm_obbligazione()) != 0 )
			throw new ApplicationException( "Impossibile variare importo dell'Impegno su bilancio Ente perche' e' associata a doc. amministrativi");

		//segnalo impossibilità di modificare importo se ci sono mandati associati
		if ( imp.isFromDocAmm() && 
			imp.isAssociataADocAmm() && imp.getIm_iniziale_obbligazione() != null &&
			imp.getIm_iniziale_obbligazione().compareTo( imp.getIm_obbligazione()) != 0 &&
			imp.getPg_mandato() != null )
			throw new ApplicationException( "Impossibile variare importo dell'Impegno su bilancio Ente perche' e' associata a mandato");
		
		//	segnalo impossibilità di modificare un residuo se l'esercizio precedente è ancora aperto
		if ( imp.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_IMP_RES))
			verificaStatoEsercizioEsPrecedente( aUC, imp.getEsercizio(), imp.getCd_cds());
			
		//importo
		Obbligazione_scadenzarioBulk obblig_scadenzario = (Obbligazione_scadenzarioBulk)imp.getObbligazione_scadenzarioColl().get(0);
		obblig_scadenzario.setIm_scadenza( imp.getIm_obbligazione() );
		obblig_scadenzario.setToBeUpdated();
		
		Obbligazione_scad_voceBulk obblig_scad_voce = (Obbligazione_scad_voceBulk)obblig_scadenzario.getObbligazione_scad_voceColl().get(0);
		obblig_scad_voce.setIm_voce(imp.getIm_obbligazione() );
		obblig_scad_voce.setToBeUpdated();

		// descrizione
		obblig_scadenzario.setDs_scadenza( imp.getDs_obbligazione() );

		//dt_scadenza
		obblig_scadenzario.setDt_scadenza( imp.getDt_scadenza());


		// CHIAMARE IL METODO modificaAccertamento() su AccertamentoPGiroComponent
		//if ( !imp.isFl_isTronco() && !imp.isResiduo())
		//	createAccertamentoPGiroComponent().modificaAccertamento( aUC, imp);

		//aggiorna il db:

		// impostiamo la voce del piano in testata coerentemente con quella nelle scadenze
		// viene già impostato nel doBringBack ma lo rifacciamo
		if (imp.getVoce_f()!=null)
			imp.setElemento_voce(((ImpegnoHome)getHome(aUC, ImpegnoBulk.class)).findElementoVoceFor(imp.getVoce_f()));
		
		imp.setUser( aUC.getUser());		
		updateBulk( aUC, imp );
		obblig_scadenzario.setUser( aUC.getUser());		
		updateBulk( aUC, obblig_scadenzario );
		obblig_scad_voce.setUser( aUC.getUser());
		if ( obblig_scad_voce.getCd_voce().equals( imp.getVoce_f().getCd_voce()))
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
			// aggiorniamo la voce con quella impostata in mappa
			newObblig_scad_voce.setCd_voce(imp.getVoce_f().getCd_voce());
			newObblig_scad_voce.setUser(obblig_scad_voce.getUser());
			newObblig_scad_voce.setCd_fondo_ricerca(obblig_scad_voce.getCd_fondo_ricerca());															
			insertBulk( aUC, newObblig_scad_voce );
			deleteBulk( aUC, obblig_scad_voce);
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
 * Nome: Scadenza successiva - Errore ultima scadenza
 * Pre:  E' stata generata la richiesta di modifica della scadenza successiva
 * Post: Viene generata un'ApplicationException in quanto le obbligazioni su partita di giro hanno un'unica scadenza
 *
 * Nome: Modifica scadenza
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza
 * Post: Vengono aggiornati l'importo in testata, in scadenza e in scad_voce e la controparte per l'accertamento su pgiro
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
	Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)scad;
	if ( modificaScadenzaSuccessiva )
		throw new ApplicationException(" Non esiste scadenza successiva" );
	if ( scadenza.getIm_scadenza().compareTo( nuovoImporto ) == 0 )
		throw handleException( new ApplicationException( "Aggiornamento in automatico non necessario" ));
	if (  nuovoImporto.compareTo( new BigDecimal(0)) < 0  )
		throw handleException( new ApplicationException( "L'importo della scadenza deve essere maggiore di 0" ));					

	//aggiorno importo testata
	ImpegnoBulk imp = (ImpegnoBulk) scadenza.getObbligazione();
	if ( imp.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_IMP_RES) )
		throw handleException( new ApplicationException( "Non è consentita la modifica dell'importo di testata di un'annotazione residua." ));					
	
	imp.setIm_obbligazione( nuovoImporto );
	imp.setToBeUpdated();

	imp.setFromDocAmm( true );
	modificaConBulk( userContext, scadenza.getObbligazione());
	return scadenza;

}
protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	ImpegnoBulk imp = (ImpegnoBulk) bulk;
	SQLBuilder sql = (SQLBuilder) super.select( userContext, clauses, bulk );
//	sql.addClause( "AND", "cd_uo_origine", sql.EQUALS, ((CNRUserContext)userContext).getCd_unita_organizzativa()); 
	sql.addClause( "AND", "esercizio", sql.EQUALS, ((CNRUserContext)userContext).getEsercizio()); 
	if ( imp.getStato_obbligazione() != null )
		sql.addClause( "AND", "stato_obbligazione", sql.EQUALS, imp.getStato_obbligazione());
	Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0); 
	//imposto cds e uo origine
	if ( !uoEnte.getCd_unita_organizzativa().equals(((CNRUserContext)userContext).getCd_unita_organizzativa()))
	{
		Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHome( userContext, Unita_organizzativaBulk.class ).findByPrimaryKey( new Unita_organizzativaBulk( ((CNRUserContext)userContext).getCd_unita_organizzativa()));
	    sql.addClause( "AND", "cd_uo_origine", sql.EQUALS, uoScrivania.getCd_unita_organizzativa());
	    sql.addClause( "AND", "cd_cds_origine", sql.EQUALS, uoScrivania.getCd_unita_padre());			    
	}
	else
	{
	    sql.addClause( "AND", "cd_unita_organizzativa", sql.EQUALS, uoEnte.getCd_unita_organizzativa());
	    sql.addClause( "AND", "cd_cds", sql.EQUALS, uoEnte.getCd_unita_padre());	    
	}

	if ( imp.isResiduo() )
	{
		sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP_RES);
		return sql;
	}	
	return sql;
}
/** 
  *  Tutti controlli superati
  *    PreCondition:
  *      La data di registrazione dell'obbligazione su partita di giro è corretta.
  *    PostCondition:
  *      L'obbligazione su partita di giro è valida. E' consentito eseguire l'attività di salvataggio.
  *  La data di registrazione dell'obbligazione su partita di giro non è corretta.
  *    PreCondition:
  *     E' stata inserita dall'utente una data di registrazione antecedente a quella dell'ultima obbligazione pgiro
  *		salvata sul database
  *    PostCondition:
  *      L'utente viene avvisato tramite un messaggio di errore che non è possibile inserire un'obbligazione su partita 
  *		 di giro con data anteriore a quella dell'ultima obbligazione salvata su database. L'attività non è consentita.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param impegno <code>ImpegnoBulk</code> l'obbligazione su partita di giro da validare
  *
 */
protected void verificaObbligazione(UserContext userContext, ImpegnoBulk impegno ) throws it.cnr.jada.persistency.PersistencyException, ComponentException, ApplicationException, javax.ejb.EJBException
{
	if ( impegno.isToBeCreated() )
	{
		Timestamp lastDayOfTheYear = DateServices.getLastDayOfYear( impegno.getEsercizio().intValue());

		if ( impegno.getDt_registrazione().before(DateServices.getFirstDayOfYear( impegno.getEsercizio().intValue())) ||
			  impegno.getDt_registrazione().after(lastDayOfTheYear))
			throw  new ApplicationException( "La data di registrazione deve appartenere all'esercizio di scrivania" );

		Timestamp today = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
		if ( today.after(lastDayOfTheYear ) &&
			  impegno.getDt_registrazione().compareTo( lastDayOfTheYear) != 0 )
			throw  new ApplicationException( "La data di registrazione deve essere " +
		   									java.text.DateFormat.getDateInstance().format( lastDayOfTheYear ));					

		Timestamp dataUltObbligazione = ((ImpegnoHome) getHome( userContext, ImpegnoBulk.class )).findDataUltimaObbligazionePerCds( impegno );
		if ( dataUltObbligazione != null && dataUltObbligazione.after( impegno.getDt_registrazione() ) )
			throw  new ApplicationException( "Non è possibile inserire un'Annotazione di Spesa su Partita di Giro con data anteriore a " +  
   									java.text.DateFormat.getDateTimeInstance().format( dataUltObbligazione ));
		}

}
/**
 * Verifica dello stato dell'esercizio 
 *
 * @param userContext <code>UserContext</code> 
 *
 * @return FALSE se per il cds interessato non è stato inserito nessun esercizio o se l'esercizio non è in stato di "aperto"
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
}			
			
/**
 * Verifica dello stato dell'esercizio precedenet per residui
 *
 * @param userContext <code>UserContext</code> 
 *
 * @return FALSE se per il cds interessato l'esercizio precedente non è in stato di "chiuso"
 *		   TRUE in tutti gli altri casi
 *
 */

void verificaStatoEsercizioEsPrecedente( UserContext userContext, Integer es, String cd_cds ) throws ComponentException, it.cnr.jada.persistency.PersistencyException
{
	EsercizioBulk esPrec = (EsercizioBulk) getHome(userContext, EsercizioBulk.class).findByPrimaryKey( 
																									new EsercizioBulk( cd_cds, new Integer( es.intValue() - 1 )));
	if (esPrec != null && !esPrec.STATO_CHIUSO_DEF.equals(esPrec.getSt_apertura_chiusura()))
			throw handleException( new ApplicationException( "Operazione impossibile: esercizio precedente non chiuso!") );
			
}	
/**
 *  Recupera l'elemento voce di tipo categoria, da cui discende la voce
 *  passata come parametro, questo elemento voce verrà inserito in testata
 *  all'impegno per bilancio ente corrispondente alla voce inserita nel
 *  dettaglio della scadenza
 * @param voce
 * @return
 * @throws ComponentException
 */
public Elemento_voceBulk findElementoVoceFor(UserContext userContext, Voce_fBulk voce) throws ComponentException {

	try{
		ImpegnoHome impHome = (ImpegnoHome)getHome(userContext,ImpegnoBulk.class);
		return impHome.findElementoVoceFor(voce);
		
	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(voce,ex);
	}
}
}
