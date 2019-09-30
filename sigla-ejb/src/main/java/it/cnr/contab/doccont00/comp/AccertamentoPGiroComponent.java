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
import it.cnr.contab.config00.bulk.*;
import it.cnr.contab.config00.ejb.*;
import it.cnr.contab.doccont00.ejb.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.config00.pdcfin.bulk.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import it.cnr.contab.doccont00.core.bulk.*;

import java.io.Serializable;

import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * Classe che ridefinisce alcune operazioni di CRUD su AccertamentoPGiroBulk
 */

/* Gestisce documenti di tipo
	ACR con fl_pgiro = 'Y' - bilancio Ente
	ACR_RES con fl_pgiro = 'Y'- bilancio Ente
	ACR_PGIRO - bilancio Cds
*/	
 
public class AccertamentoPGiroComponent extends it.cnr.jada.comp.CRUDComponent implements IDocumentoContabileMgr,ICRUDMgr,Cloneable,Serializable,IAccertamentoPGiroMgr
{
	private final static int INSERIMENTO = 1;
	private final static int MODIFICA    = 2;
	private final static int CANCELLAZIONE    = 3;		


//@@<< CONSTRUCTORCST
    public  AccertamentoPGiroComponent()
    {
//>>

//<< CONSTRUCTORCSTL
        /*Default constructor*/
//>>

//<< CONSTRUCTORCSTT

    }
/** 
  *  creazione 
  *    PreCondition:
  *      Un accertamento e' stato creato
  *    PostCondition:
  *      Viene chiamato il metodo 'aggiornaSaldiInInserimento' che, per la voce del piano utilizzata nell'accertamento calcola
  *      l'importo dell' aggiornamento da apportare ai saldi e richiama il metodo sulla Component di Gestione Saldi (SaldoComponent) che
  *      effettua tale aggiornamento
  *  modifica/eliminazione accertamento
  *    PreCondition:
  *      Un accertamento e' stato modificato/eliminato
  *    PostCondition:
  *      Viene chiamato il metodo 'aggiornaSaldiInModifica' che, per la voce del piano utilizzata nell'accertamento calcola
  *      l'importo dell' aggiornamento da apportare ai saldi e richiama il metodo sulla Component di Gestione Saldi (SaldoComponent) che
  *      effettua tale aggiornamento
  *
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param accertamento <code>AccertamentoBulk</code> l'accertamento da salvare
  * @param azione indica l'azione effettuata sull'obbligazione e puo' assumere i valori INSERIMENTO, MODIFICA, CANCELLAZIONE    
  *
 */
private void aggiornaCapitoloSaldoAccertamento (UserContext aUC,AccertamentoPGiroBulk accertamento, int azione ) throws ComponentException
{
	try
	{
		// non si aggiornano i saldi di accertamenti con esercizio di competenza diverso da esercizio di creazione
		if ( accertamento.getEsercizio().compareTo( accertamento.getEsercizio_competenza()) != 0 )
			return;
		if ( azione == INSERIMENTO )
			aggiornaSaldiInInserimento( aUC, accertamento, false);
		else if ( azione == MODIFICA )
			aggiornaSaldiInModifica( aUC, 
											 accertamento, 
											 accertamento.getPg_ver_rec(),
											 false);
		else if ( azione == CANCELLAZIONE )
			aggiornaSaldiInModifica( aUC, 
											 accertamento, 
											 new Long(accertamento.getPg_ver_rec().longValue() + 1),
											 false);		
			
						
/*
		SaldoComponentSession session = createSaldoComponentSession();
		AccertamentoBulk accDaDB;
		Voce_fBulk voce = new Voce_fBulk( accertamento.getCd_voce(), accertamento.getEsercizio(), accertamento.getTi_appartenenza(), accertamento.getTi_gestione() );
		switch( accertamento.getCrudStatus() )
		{
			case accertamento.TO_BE_CREATED:
				session.aggiornaObbligazioniAccertamenti( aUC, voce, accertamento.getCd_cds(), accertamento.getIm_accertamento());
				break;
			case accertamento.TO_BE_DELETED:
				//rileggo il valore precedente e lo aggiorno
				accDaDB = (AccertamentoBulk) getHome(aUC, AccertamentoBulk.class).findByPrimaryKey( accertamento);
				session.aggiornaObbligazioniAccertamenti( aUC, voce, accDaDB.getCd_cds(), accDaDB.getIm_accertamento().negate());
				break;
			case accertamento.TO_BE_UPDATED:
				//rileggo il valore precedente e lo aggiorno
				accDaDB = (AccertamentoBulk) getHome(aUC, AccertamentoBulk.class).findByPrimaryKey( accertamento);
				if ( accDaDB.getIm_accertamento().compareTo( accertamento.getIm_accertamento()) != 0 )
					session.aggiornaObbligazioniAccertamenti( aUC, voce, accDaDB.getCd_cds(), accertamento.getIm_accertamento().subtract( accDaDB.getIm_accertamento()));
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
		if ( docContabile instanceof AccertamentoPGiroBulk)
		{
			AccertamentoPGiroBulk accertamento = (AccertamentoPGiroBulk) docContabile;
			Long pg_ver_rec = (Long) values.get("pg_ver_rec");
			if ( pg_ver_rec == null )
				throw new ApplicationException( "Aggiornamento in differita dello stato coge/coan dei documenti contabili impossibile (pg_ver_rec nullo)");
			if ( accertamento.getPg_accertamento().longValue() >= 0 ) //accertamento non temporaneo
				callDoRiprocAcc( userContext, accertamento, pg_ver_rec );			
		}
	}
	catch ( Exception e )
	{
		throw handleException(e);
	}
}
/**
 * Aggiornamento in differita dei saldi dell'accertamento su partita di giro.
 * Un documento amministrativo di entrata che agisce in modalità transazionale ha creato/modificato gli importi 
 * relativi ad un accertamento pgiro; i saldi di tale accertamento non possono essere aggiornati subito in quanto
 * tale operazione genererebbe dei lock sulle voci del piano che non ne consentirebbero l'utilizzo ad altri utenti;
 * pertanto l'aggiornamento dei saldi dell'accertamento pgiro viene differito al momento del salvataggio
 * del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi per accertamento su partita di giro creato 
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un accertamento su capitoli di
 *       partita di giro che e' stato creato nel contesto transazionale del documento amministrativo ( progressivo
 *       accertamento pgiro < 0)
 * Post: I saldi dell'accertamento pgiro sono stati aggiornati nel metodo 'aggiornaSaldiInInserimento'
 *
 * Nome: Aggiorna saldi per accertamento su partita di giro esistente
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un accertamento su capitoli di
 *       partita di giro che non e' stato creato nel contesto transazionale del documento amministrativo ( progressivo
 *       accertamento pgiro > 0)
 * Post: I saldi dell'accertamento pgiro sono stati aggiornati nel metodo 'aggiornaSaldiInModifica'
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	docContabile	il documento contabile di tipo AccertamentoPGiroBulk per cui aggiornare i saldi
 * @param	values	la Map che contiene il "pg_ver_rec" iniziale dell'accertamento
 * @param	param paramtero non utilizzato per gli accertamenti
 *
*/
public void aggiornaSaldiInDifferita( UserContext userContext, IDocumentoContabileBulk docContabile, Map values, OptionRequestParameter param ) throws ComponentException
{
	try
	{
		if ( docContabile instanceof AccertamentoPGiroBulk )
		{
			AccertamentoPGiroBulk acc_pgiro = (AccertamentoPGiroBulk) docContabile;
			Long pg_ver_rec = (Long) values.get("pg_ver_rec");
			if ( pg_ver_rec == null )
				throw new ApplicationException( "Aggiornamento in differita dei saldi dei documenti contabili impossibile (pg_ver_rec nullo)");
			if ( acc_pgiro.getPg_accertamento().longValue() < 0 ) //accertamento appena inserito
				aggiornaSaldiInInserimento( userContext, acc_pgiro, true );
			else
				aggiornaSaldiInModifica( userContext, acc_pgiro, pg_ver_rec, true );			
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
 * Pre:  Un accertamento e' stato creato
 * Post: Per la Voce del piano presente nell'accertamento viene richiamato il metodo sulla Component di gestione dei Saldi (SaldoComponent) per incrementare
 *       il saldo del capitolo corrispondente
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	acc_pgiro	l'AccertamentoPGiroBulk per cui aggiornare i saldi 
 */
private void aggiornaSaldiInInserimento(
	UserContext userContext, 
	AccertamentoPGiroBulk acc_pgiro,
	boolean aggiornaControparte) throws ComponentException, java.rmi.RemoteException, it.cnr.jada.persistency.PersistencyException
{
	SaldoComponentSession session = createSaldoComponentSession();
	Voce_fBulk voce = (Voce_fBulk) acc_pgiro.getCapitolo();
	voce.setEsercizio( acc_pgiro.getEsercizio()); /**?????????***/
	BigDecimal im_voce = acc_pgiro.getIm_accertamento();
	session.aggiornaObbligazioniAccertamenti( userContext, voce, acc_pgiro.getCd_cds(), im_voce, it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk.TIPO_COMPETENZA);
	/*
	 * Aggiorno i Saldi per CDR/Linea
	 */
	Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk)getHome(userContext,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)));
	boolean isNuovoPdg = ((Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class)).isNuovoPdg(userContext);
	Accertamento_scad_voceBulk asv;
	Accertamento_scadenzarioBulk as;
	for ( Iterator j = acc_pgiro.getAccertamento_scadenzarioColl().iterator(); j.hasNext(); )
	{
	  as = (Accertamento_scadenzarioBulk) j.next();
	  for ( int index = as.getAccertamento_scad_voceColl().size() - 1; index >= 0 ; index--)
	  {
		 asv = (Accertamento_scad_voceBulk) as.getAccertamento_scad_voceColl().get( index );
		 session.aggiornaObbligazioniAccertamenti( userContext, asv.getCd_centro_responsabilita(), asv.getCd_linea_attivita(), voce, acc_pgiro.getEsercizio_originale(),acc_pgiro.isAccertamentoResiduo()?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_COMPETENZA,asv.getIm_voce(),acc_pgiro.getCd_tipo_documento_cont());
	  }
	}		
	if (aggiornaControparte && !acc_pgiro.isFl_isTronco() && !acc_pgiro.isResiduo()) {
		Ass_partita_giroBulk ass_pgiro = ((Ass_partita_giroHome)getHome(userContext, Ass_partita_giroBulk.class)).getAssociazionePGiroFor(acc_pgiro);
		Voce_fBulk voce_f = null;
		if (!isNuovoPdg) {
			 voce_f = new Voce_fBulk(ass_pgiro.getCd_voce_clg(), ass_pgiro.getEsercizio(), ass_pgiro.getTi_appartenenza_clg(), ass_pgiro.getTi_gestione_clg());
		} 
		session.aggiornaObbligazioniAccertamenti(userContext, voce_f, acc_pgiro.getAssociazione().getImpegno().getCd_cds(), im_voce, ReversaleBulk.TIPO_COMPETENZA);
		/*
		 * Aggiorno i Saldi per CDR/Linea
		 */
		Obbligazione_scad_voceBulk osv;
		Obbligazione_scadenzarioBulk os;		 
		for ( Iterator j = acc_pgiro.getAssociazione().getImpegno().getObbligazione_scadenzarioColl().iterator(); j.hasNext(); )
		{
		  os = (Obbligazione_scadenzarioBulk) j.next();
		  for ( int index = os.getObbligazione_scad_voceColl().size() - 1; index >= 0 ; index--)
		  {
			 osv = (Obbligazione_scad_voceBulk) os.getObbligazione_scad_voceColl().get( index );
			 voce = new Voce_fBulk( osv.getCd_voce(), osv.getEsercizio(), osv.getTi_appartenenza(), osv.getTi_gestione());
			 session.aggiornaObbligazioniAccertamenti( userContext, osv.getCd_centro_responsabilita(), osv.getCd_linea_attivita(), voce, acc_pgiro.getAssociazione().getImpegno().getEsercizio_originale(),acc_pgiro.getAssociazione().getImpegno().isObbligazioneResiduoImproprio()?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO,osv.getIm_voce(),acc_pgiro.getAssociazione().getImpegno().getCd_tipo_documento_cont());

		  }
		}		
	}

}
/**
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi
 * Pre:  Un accertamento e' stato modificato/eliminato
 * Post: Per ogni V_mod_saldi_accertBulk presente nel database a fronte dell'accertamento e del suo pg_ver_rec
 *       e' stato richiamato il metodo sulla Component di gestione dei Saldi (SaldoCompoennt) per aggiornare
 *       il saldo del capitolo corrispondente; se necessario
 *       anche i saldi relativi alle reversali e all'incassato vengono aggiornati.
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	acc_pgiro	l'AccertamentoPGiroBulk per cui aggiornare i saldi
 * @param	pg_ver_rec	il pg_ver_rec iniziale dell'accertamento
 * 
 */
private void aggiornaSaldiInModifica( 
	UserContext userContext, 
	AccertamentoPGiroBulk acc_pgiro, 
	Long pg_ver_rec,
	boolean aggiornaControparte) throws it.cnr.jada.persistency.PersistencyException, ComponentException, java.rmi.RemoteException
{
	SaldoComponentSession session = createSaldoComponentSession();			
	List saldiDaAggiornare = ((V_mod_saldi_accertHome)getHome( userContext, V_mod_saldi_accertBulk.class )).findModificheSaldiFor( acc_pgiro, pg_ver_rec );
	if ( userContext.isTransactional() && saldiDaAggiornare.size() == 0 )
		throw new ApplicationException( "Attenzione! Il saldo relativo all'accertamento " + acc_pgiro.getEsercizio_originale() + "/" + acc_pgiro.getPg_accertamento() + " non può essere aggiornato perchè l'accertamento non e' presente nello storico.");

	String ti_competenza_residuo;
	if ( acc_pgiro.isResiduo() )
		ti_competenza_residuo = ReversaleBulk.TIPO_RESIDUO;
	else
		ti_competenza_residuo = ReversaleBulk.TIPO_COMPETENZA;
		
	//e' sempre uno solo
	for ( Iterator i = saldiDaAggiornare.iterator(); i.hasNext(); )
	{
		V_mod_saldi_accertBulk modSaldo = (V_mod_saldi_accertBulk) i.next();
		if ( modSaldo.getIm_delta_voce().compareTo( new BigDecimal(0)) != 0 )
		{		
			Voce_fBulk voce = new Voce_fBulk( modSaldo.getCd_voce(), acc_pgiro.getEsercizio(), modSaldo.getTi_appartenenza(), modSaldo.getTi_gestione() );
			session.aggiornaObbligazioniAccertamenti( userContext, voce, acc_pgiro.getCd_cds(), modSaldo.getIm_delta_voce(), ti_competenza_residuo);
			if (aggiornaControparte && !acc_pgiro.isFl_isTronco() && !acc_pgiro.isResiduo()) {
				Ass_partita_giroBulk ass_pgiro = ((Ass_partita_giroHome)getHome(userContext, Ass_partita_giroBulk.class)).getAssociazionePGiroFor(acc_pgiro);
				Voce_fBulk voce_f = new Voce_fBulk(ass_pgiro.getCd_voce_clg(), ass_pgiro.getEsercizio(), ass_pgiro.getTi_appartenenza_clg(), ass_pgiro.getTi_gestione_clg());
				session.aggiornaObbligazioniAccertamenti(userContext, voce_f, acc_pgiro.getAssociazione().getImpegno().getCd_cds(), modSaldo.getIm_delta_voce(), ReversaleBulk.TIPO_COMPETENZA);
			}
			
			if ( modSaldo.getIm_delta_rev_voce().compareTo( new BigDecimal(0) ) != 0 )
				session.aggiornaMandatiReversali( userContext, voce, acc_pgiro.getCd_cds(), modSaldo.getIm_delta_rev_voce(), ti_competenza_residuo);

			if ( modSaldo.getIm_delta_inc_voce().compareTo( new BigDecimal(0) ) != 0 )
				session.aggiornaPagamentiIncassi( userContext, voce, acc_pgiro.getCd_cds(), modSaldo.getIm_delta_inc_voce(), ti_competenza_residuo);
		}		
	}
	/*
	* Aggiorno i Saldi per CDR/Linea
	*/	

	List saldiDaAggiornareCdrLinea = ((V_mod_saldi_accert_scad_voceHome)getHome( userContext, V_mod_saldi_accert_scad_voceBulk.class )).findModificheSaldiFor( acc_pgiro, pg_ver_rec );
	if ( userContext.isTransactional() && saldiDaAggiornareCdrLinea.size() == 0 )
		throw new ApplicationException( "Attenzione! Il saldo relativo all'accertamento " + acc_pgiro.getEsercizio_originale() + "/" + acc_pgiro.getPg_accertamento() + " non può essere aggiornato perchè l'accertamento non e' presente nello storico.");
	Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk)getHome(userContext,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(acc_pgiro.getEsercizio()));
	
	for ( Iterator i = saldiDaAggiornareCdrLinea.iterator(); i.hasNext(); )
	{
		V_mod_saldi_accert_scad_voceBulk modSaldo = (V_mod_saldi_accert_scad_voceBulk) i.next();
		boolean isNuovoPdg = ((Parametri_cnrHome)getHome(userContext,Parametri_cnrBulk.class)).isNuovoPdg(userContext);
		IVoceBilancioBulk voce = null;
        if (!isNuovoPdg)
        	voce = new Voce_fBulk( modSaldo.getCd_voce(), acc_pgiro.getEsercizio(), modSaldo.getTi_appartenenza(), modSaldo.getTi_gestione() );
        else
        	voce = new Elemento_voceBulk( modSaldo.getCd_voce(), acc_pgiro.getEsercizio(), modSaldo.getTi_appartenenza(), modSaldo.getTi_gestione() );

        if ( modSaldo.getIm_delta_voce().compareTo( new BigDecimal(0)) != 0 )
		{		
			session.aggiornaObbligazioniAccertamenti( userContext, modSaldo.getCd_centro_responsabilita(), modSaldo.getCd_linea_attivita(), voce, modSaldo.getEsercizio_originale(),modSaldo.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_ACR_RES)?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_COMPETENZA,modSaldo.getIm_delta_voce(),modSaldo.getCd_tipo_documento_cont());
			if (aggiornaControparte && !acc_pgiro.isFl_isTronco() && !acc_pgiro.isResiduo()) {
				Obbligazione_scad_voceBulk osv;
				Obbligazione_scadenzarioBulk os;		 
				for ( Iterator j = acc_pgiro.getAssociazione().getImpegno().getObbligazione_scadenzarioColl().iterator(); j.hasNext(); )
				{
				  os = (Obbligazione_scadenzarioBulk) j.next();
				  for ( int index = os.getObbligazione_scad_voceColl().size() - 1; index >= 0 ; index--)
				  {
					 osv = (Obbligazione_scad_voceBulk) os.getObbligazione_scad_voceColl().get( index );
					 if(!isNuovoPdg)
						 voce = new Voce_fBulk( osv.getCd_voce(), osv.getEsercizio(), osv.getTi_appartenenza(), osv.getTi_gestione());
					 else
						 voce = new Elemento_voceBulk( osv.getCd_voce(), osv.getEsercizio(), osv.getTi_appartenenza(), osv.getTi_gestione());

					 session.aggiornaObbligazioniAccertamenti( userContext, osv.getCd_centro_responsabilita(), osv.getCd_linea_attivita(), voce, acc_pgiro.getAssociazione().getImpegno().getEsercizio_originale(),acc_pgiro.getAssociazione().getImpegno().isObbligazioneResiduoImproprio()?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO,osv.getIm_voce(),acc_pgiro.getAssociazione().getImpegno().getCd_tipo_documento_cont());
				  }
				}
			}
		}	
    	
		if ( modSaldo.getIm_delta_rev_voce().compareTo( new BigDecimal(0) ) != 0 )
			session.aggiornaMandatiReversali( userContext,modSaldo.getCd_centro_responsabilita(),modSaldo.getCd_linea_attivita(),voce,modSaldo.getEsercizio_originale(),modSaldo.getIm_delta_rev_voce(),modSaldo.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_ACR_RES)?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_COMPETENZA);

		if ( modSaldo.getIm_delta_inc_voce().compareTo( new BigDecimal(0) ) != 0 )
			session.aggiornaPagamentiIncassi( userContext,modSaldo.getCd_centro_responsabilita(),modSaldo.getCd_linea_attivita(),voce,modSaldo.getEsercizio_originale(),modSaldo.getIm_delta_inc_voce());
	}		
}
/** 
  *  aggiornamento Stato COAN o COGE dei Documenti Amministrativi
  *    PreCondition:
  *      Nel caso un accertamento pgiro sia stato eliminato o modificato, occorre
  *		 aggiornare lo stato di eventuali documenti amministrativi che fanno
  *		 riferimento all'accertamento stesso.
  *	   PostCondition:
  *		 Vengono quindi aggiornati i documenti amministrativi tramite una stored procedure
  *		 (doRiprocAcc)
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param docContabile <code>AccertamentoBulk</code> l'accertamento modificato o eliminato
  *
  */
private void aggiornaStatoCOAN_COGEDocAmm( UserContext userContext, AccertamentoBulk docContabile  )  throws  ComponentException
{
	callDoRiprocAcc(userContext, docContabile, null);
}
/** 
  *  cancellazione (logica)
  *    PreCondition:
  *      L'utente richiede la cancellazione di un accertamento su partita di giro
  *    PostCondition:
  *     Alla component che gestisce l'obbligazione su pgiro viene inoltrata la richiesta di cancellazione (logica) 
  *		dell'obbligazione associata all'impegno (metodo eliminaObbligazione), l'accertamento (con la sua scadenza 
  *		e il suo dettaglio scadenza) viene cancellato (metodo eliminaAccertamento)
  *  errore - doc.amm.associati
  *    PreCondition:
  *      La richiesta di cancellazione di un accertamento su partita di giro e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'accertamento
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la cancellazione
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param accert_pgiro <code>AccertamentoPGiroBulk</code> l'accertamento su partita di giro da cancellare (logicamente)
  * @return accert_pgiro <code>AccertamentoPGiroBulk</code> l'accertamento su partita di giro annullato
  *
 */
public AccertamentoPGiroBulk annullaAccertamento(UserContext userContext, AccertamentoPGiroBulk accert_pgiro ) throws ComponentException
{

	try
	{
		verificaStatoEsercizio( userContext, accert_pgiro.getEsercizio(), accert_pgiro.getCd_cds());
		//	segnalo impossibilità di annullare un residuo se l'esercizio precedente è ancora aperto
		if ( accert_pgiro.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_ACR_RES))
			verificaStatoEsercizioEsPrecedente( userContext, accert_pgiro.getEsercizio(), accert_pgiro.getCd_cds());
		

		if ( accert_pgiro.isAssociataADocAmm() )
			throw new ApplicationException( "Impossibile cancellare l'Annotazione d'Entrata su Partita di Giro perche' ha documenti amministrativi associati");

		// annullo l'impegno di partita di giro corrispondente
		if ( !accert_pgiro.isFl_isTronco() )
			createObbligazionePGiroComponent().eliminaObbligazione( userContext, accert_pgiro.getAssociazione().getImpegno());

		// annullo il record relativo all'accertamento partita di giro
		eliminaAccertamento( userContext, accert_pgiro);

		return accert_pgiro;

	}
	catch ( Exception e )
	{
		throw handleException( accert_pgiro, e );
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

public void callDoRiprocAcc(
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
			"CNRCTB215.doRiprocAcc(?, ?, ?, ?, ?)",false,this.getClass());
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
  *		Il sistema ha reindirizzato la richiesta alla component delle ObbligazioniPGiro
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param doc <code>IDocumentoContabileBulk</code> doc.contabile da riportare
  *
  */

public void callRiportaAvanti (UserContext userContext,IDocumentoContabileBulk doc) throws it.cnr.jada.comp.ComponentException
{
	try
	{
		createObbligazionePGiroComponent().callRiportaAvanti(userContext, doc );
	}
	catch (Exception e )
	{
		throw handleException(e);
	}		
}
/** 
  *  riporta indietro dall'esercizio successivo di un doc.contabile
  *    PreCondition:
  *      E' stata inoltrata una richiesta di riportare indietro dall'esercizio successivo un documento contabile
  *	 PostCondition:
  *		Il sisteme reindirizza la richiesta sulla Component ObbligazionePGiroComponent
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param doc <code>IDocumentoContabileBulk</code> doc.contabile da riportare
  *
  */

public void callRiportaIndietro (UserContext userContext,IDocumentoContabileBulk doc) throws ComponentException
{
	try
	{
		createObbligazionePGiroComponent().callRiportaIndietro(userContext, doc );
	}
	catch (Exception e )
	{
		throw handleException(e);
	}		
}
/** 
  *  creazione
  *    PreCondition:
  *      Un Impegno su partita di giro e' stato creato ed e' necessario creare il corrispondente Accertamento
  *    PostCondition:
  *      L'accertamento (AccertamentoPGiroBulk) viene creato con importo pari a quello dell'impegno, codice terzo
  *      recuperato dalla Configurazione CNR come codice DIVERSI per PARTITA di GIRO, capitolo di entrata ricavato
  *      (metodo findVoce_f) dall'associazione fra Capitoli di Entrata e di Spesa per Partita di Giro, data di
  *		 scadenza uguale a quella della scadenza dell'obbligazione su partita di giro.
  *      Viene inoltre creata una scadenza (metodo creaAccertamento_scadenzario) e
  *      un dettaglio di scadenza (metodo creaAccertamento_scad_voce). I saldi relativi alla voce del piano
  *      dell'accertamento vengono aggiornati (metodo aggiornaCapitoloSaldoAccertamento).
  *		 Viene infine validato l'Accertamento prima della sua creazione (metodo verificaAccertamento)
  *  errore - Configurazione CNR per codice DIVERSI su PGIRO
  *    PreCondition:
  *      non e' presente in Configurazione CNR la definizione del CODICE DIVERSI per PGIRO
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente la mancata definizione in Configurazione CNR del CODICE DIVERSI 
  *		 per PGIRO
  *  errore - Anagrafica per codice DIVERSI su PGIRO
  *    PreCondition:
  *      non e' presente in Anagrafica il codice terzo presente in Configurazione CNR come CODICE DIVERSI per PGIRO
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente la mancata definizione in Anagrafica
  *  errore - Associazione capitoli entrata/spese
  *    PreCondition:
  *      non e' presente (Ass_partita_giroBulk) l'associazione fra il capitolo di spesa dell'impegno e un capitolo 
  *		 di entrata
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'assenza dell'associazione
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param imp_pgiro <code>ImpegnoPGiroBulk</code> l'impegno su partita di giro creato
  *
  * @return accert_pgiro L'accertamento su partita di giro creato in corrispondenza dell'impegno
*/

public AccertamentoPGiroBulk creaAccertamento(UserContext uc,ImpegnoPGiroBulk imp_pgiro) throws ComponentException
{
	try
	{
		/* ACCERTAMENTO PARTITA GIRO BULK */		
		AccertamentoPGiroBulk accert_pgiro = new AccertamentoPGiroBulk();
		accert_pgiro.setUser( imp_pgiro.getUser() );
		accert_pgiro.setToBeCreated();

		// campi chiave
		accert_pgiro.setEsercizio( imp_pgiro.getEsercizio() );
		accert_pgiro.setCd_cds( imp_pgiro.getCd_cds() );
		accert_pgiro.setEsercizio_originale( imp_pgiro.getEsercizio_originale() );

		// altri campi...
		accert_pgiro.setEsercizio_competenza( accert_pgiro.getEsercizio());
		accert_pgiro.setDt_scadenza( imp_pgiro.getDt_scadenza());
		if ( imp_pgiro.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_IMP))
			accert_pgiro.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_ACR);
		else
			accert_pgiro.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_ACR_PGIRO);

		
		accert_pgiro.setCd_unita_organizzativa( imp_pgiro.getCd_unita_organizzativa() );
		accert_pgiro.setCd_cds_origine( imp_pgiro.getCd_cds_origine() );
		accert_pgiro.setCd_uo_origine( imp_pgiro.getCd_uo_origine() );
		accert_pgiro.setDt_registrazione( imp_pgiro.getDt_registrazione() );
		accert_pgiro.setDs_accertamento( "Annotazione d'Entrata su Partita di Giro creata in automatico" );

		//gestione x accertamenti tronchi
		if ( !imp_pgiro.isFl_isTronco() )
			accert_pgiro.setIm_accertamento( imp_pgiro.getIm_obbligazione() );
		else
		{
			accert_pgiro.setIm_accertamento( new BigDecimal(0) );
			accert_pgiro.setFl_isTronco( true );
			accert_pgiro.setDt_cancellazione( imp_pgiro.getDt_registrazione());
		}	
		accert_pgiro.setRiportato("N");
		// ...e in particolare il campo cd_terzo
		it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = createConfigurazioneCnrComponentSession().getConfigurazione( uc, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_TERZO_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_CODICE_DIVERSI_PGIRO );
		if ( config == null  )
			throw new ApplicationException("Configurazione CNR: manca la definizione del CODICE DIVERSI per partite di giro");

		if ( config.getIm01() == null )
			throw new ApplicationException("Configurazione CNR: manca il CODICE TERZI nella definizione del codice diversi per partite di giro");

		SQLBuilder sql = getHomeCache(uc).getHome( it.cnr.contab.anagraf00.core.bulk.TerzoBulk.class ).createSQLBuilder();
		sql.addClause("AND","cd_terzo", sql.EQUALS, new Integer(config.getIm01().intValue()));
		List result = getHomeCache(uc).getHome( it.cnr.contab.anagraf00.core.bulk.TerzoBulk.class ).fetchAll( sql );
		if ( result.size() > 0)
			accert_pgiro.setCd_terzo( ((it.cnr.contab.anagraf00.core.bulk.TerzoBulk) result.get(0)).getCd_terzo());
		else
			throw new ApplicationException("Il terzo DIVERSI per partita di giro non e' presente in anagrafica");			

		Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk)getHome(uc,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(imp_pgiro.getEsercizio()));
		Ass_partita_giroBulk ass_pgiro =null;
		if (parametriCnr.getFl_nuova_gestione_pg().booleanValue() ) {
			
			if(imp_pgiro.getElemento_voceContr()!=null && imp_pgiro.getElemento_voceContr().getCd_elemento_voce()!=null){
				if ( imp_pgiro.getElemento_voceContr().getEsercizio()!=null)
					accert_pgiro.setEsercizio( imp_pgiro.getElemento_voceContr().getEsercizio() );
				if ( imp_pgiro.getElemento_voceContr().getTi_appartenenza()!=null)
					accert_pgiro.setTi_appartenenza( imp_pgiro.getElemento_voceContr().getTi_appartenenza() );
				if ( imp_pgiro.getElemento_voceContr().getTi_gestione()!=null)
					accert_pgiro.setTi_gestione(  imp_pgiro.getElemento_voceContr().getTi_gestione() );
				if ( imp_pgiro.getElemento_voceContr().getCd_elemento_voce()!=null)
					accert_pgiro.setCd_voce(imp_pgiro.getElemento_voceContr().getCd_elemento_voce() );
			} 
			else if(imp_pgiro.isFl_isTronco()){
				Ass_partita_giroHome ass_pgiroHome = (Ass_partita_giroHome) getHome( uc, Ass_partita_giroBulk.class );
				ass_pgiro = ass_pgiroHome.getAssociazionePGiroFor(imp_pgiro);
				accert_pgiro.setTi_appartenenza( ass_pgiro.getTi_appartenenza() );
				accert_pgiro.setTi_gestione( ass_pgiro.getTi_gestione() );
				accert_pgiro.setCd_elemento_voce( ass_pgiro.getCd_voce() );
			}
			else 
				throw new it.cnr.jada.comp.ApplicationException("Indicare la voce del Piano Contr.");
			/*	{Ass_partita_giroHome ass_pgiroHome = (Ass_partita_giroHome) getHome( uc, Ass_partita_giroBulk.class );
				ass_pgiro = ass_pgiroHome.getAssociazionePGiroFor(imp_pgiro);
				accert_pgiro.setTi_appartenenza( ass_pgiro.getTi_appartenenza() );
				accert_pgiro.setTi_gestione( ass_pgiro.getTi_gestione() );
				accert_pgiro.setCd_elemento_voce( ass_pgiro.getCd_voce() );
			}*/
			
		}else{
			Ass_partita_giroHome ass_pgiroHome = (Ass_partita_giroHome) getHome( uc, Ass_partita_giroBulk.class );
			ass_pgiro = ass_pgiroHome.getAssociazionePGiroFor(imp_pgiro);
			accert_pgiro.setTi_appartenenza( ass_pgiro.getTi_appartenenza() );
			accert_pgiro.setTi_gestione( ass_pgiro.getTi_gestione() );
			accert_pgiro.setCd_elemento_voce( ass_pgiro.getCd_voce() );
		}
		
		if (parametriCnr==null || !parametriCnr.getFl_nuovo_pdg()) {
			Voce_fBulk voce_f = this.findVoce_f(uc, imp_pgiro, ass_pgiro);
			if ( voce_f == null )
				throw new ApplicationException("Impossibile recuperare la voce per il capitolo: " + ( (Ass_partita_giroBulk)result.get(0) ).getCd_voce());			
			accert_pgiro.setCd_voce( voce_f.getCd_voce() );
		} else {
			if (ass_pgiro!=null){
				accert_pgiro.setCd_voce( ass_pgiro.getCd_voce() );
				accert_pgiro.getCapitolo().setCd_elemento_voce( ass_pgiro.getCd_voce() );
			}
			else{
				accert_pgiro.setCd_elemento_voce(accert_pgiro.getCd_voce());
				accert_pgiro.getCapitolo().setCd_elemento_voce(accert_pgiro.getCd_voce());
			}
		}
		
		Accertamento_scadenzarioBulk accert_scad = creaAccertamento_scadenzario( uc, accert_pgiro );
		// creaAccertamento_scad_voce( uc, accert_scad, (Obbligazione_scad_voceBulk)(((Obbligazione_scadenzarioBulk)imp_pgiro.getObbligazione_scadenzarioColl().get(0)).getObbligazione_scad_voceColl().get(0)));
		creaAccertamento_scad_voce( uc, accert_scad );
			
		verificaAccertamento( uc, accert_pgiro );
		accert_pgiro = (AccertamentoPGiroBulk) super.creaConBulk( uc, accert_pgiro );
		if ( !uc.isTransactional() )
			//aggiorna i saldi 
			aggiornaCapitoloSaldoAccertamento( uc, accert_pgiro, INSERIMENTO);
		
		return accert_pgiro;
	}
	catch ( Exception e )
	{
		throw handleException( e )	;

	}	
}
/** 
  *  creazione
  *    PreCondition:
  *      una scadenza di un accertamento pgiro e' stata creata ed e' necessario creare il suo dettaglio
  *    PostCondition:
  *      Il dettaglio di scadenza (Accertamento_scad_voceBulk) viene creato
  *      con linea attività uguale alla linea di attività definita nella Configurazione CNR come Linea attività Entrata ENTE
  *      e importo uguale all'importo della scadenza dell'accertamento
  *  errore - Configurazione CNR
  *    PreCondition:
  *      una richiesta di creazione di un dettaglio di scadenza di un accertamento pgiro e' stata generata
  *      ma non e' stata definita in Configurazione CNR la Linea attività Entrata ENTE
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente la mancata definizione in Configurazione CNR della linea di attivita' Entrata ENTE
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param accert_scad <code>Accertamento_scadenzarioBulk</code> la scadenza dell'accertamento pgiro creata
  *
  * @return accert_scad_voce Il dettaglio di scadenza dell'accertamento pgiro creato
 */

private Accertamento_scad_voceBulk creaAccertamento_scad_voce (UserContext uc,Accertamento_scadenzarioBulk accert_scad ) throws ComponentException
{
	try
	{
		Accertamento_scad_voceBulk accert_scad_voce = new Accertamento_scad_voceBulk();
		accert_scad_voce.setUser( accert_scad.getAccertamento().getUser() );
		accert_scad_voce.setToBeCreated();

		// campi chiave
		accert_scad_voce.setAccertamento_scadenzario( accert_scad );

		// altri campi chiave
		it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = createConfigurazioneCnrComponentSession().getConfigurazione( uc, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LINEA_ATTIVITA_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_LINEA_ATTIVITA_ENTRATA_ENTE );
		if ( config != null  )
		{
			// MITODO - da correggere, da problemi con accertamento_scad_voceBulk
			WorkpackageHome wphome = (WorkpackageHome) getHome(uc, WorkpackageBulk.class);
			WorkpackageBulk wp = (WorkpackageBulk) wphome.findByPrimaryKey(new WorkpackageBulk(config.getVal01(),config.getVal02()));
			//accert_scad_voce.setCd_linea_attivita( config.getVal02() );
			//accert_scad_voce.setCd_centro_responsabilita(config.getVal01() );
			accert_scad_voce.setLinea_attivita(wp);		
		}
		else
			throw new ApplicationException("Configurazione CNR: manca la definizione del GAE ENTRATA ENTE");

		// altri campi
		accert_scad_voce.setIm_voce( accert_scad.getAccertamento().getIm_accertamento() );
		// accert_scad_voce.setCd_fondo_ricerca( accert_pgiro.getCd_fondo_ricerca() );
	
		accert_scad.getAccertamento_scad_voceColl().add( accert_scad_voce );

		return accert_scad_voce;
		
	}catch ( Exception e )
		{
			throw handleException( accert_scad.getAccertamento(), e );
		}
}
/** 
  *  creazione
  *    PreCondition:
  *      un accertamento pgiro e' stato creato ed e' necessario creare la scadenza ad esso associata
  *    PostCondition:
  *      Viene creata una scadenza (Accertamento_scadenzarioBulk) di accertamento su partita di giro 
  *		 con data uguale alla data di scadenza dell'accertamento e con importo pari a quello dell'accertamento.
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param accert_pgiro <code>AccertamentoPGiroBulk</code> l'accertamento su partita di giro creato
  *
  * @return accert_scad La scadenza dell'accertamento pgiro creata
 */

private Accertamento_scadenzarioBulk creaAccertamento_scadenzario (UserContext uc, AccertamentoPGiroBulk accert_pgiro) throws ComponentException
{

		// gc.set((accert_pgiro.getEsercizio()).intValue(),1,1);
		Accertamento_scadenzarioBulk accert_scad = new Accertamento_scadenzarioBulk();
		/* simona 12/7 */
		// accert_pgiro.getAccertamento_scadenzarioColl().add( accert_scad );
		accert_pgiro.addToAccertamento_scadenzarioColl( accert_scad );
		
		accert_scad.setUser( accert_pgiro.getUser() );
		accert_scad.setToBeCreated();

		// campi chiave
		// accert_scad.setAccertamento( accert_pgiro );
 
		// altri campi
		accert_scad.setDt_scadenza_incasso( accert_pgiro.getDt_scadenza() );
		/*
		// controllo se l'anno di emissione dell'accertamento per partita di giro è uguale all'anno corrente

		gc.set(java.util.GregorianCalendar.MONTH,1);
		gc.set(java.util.GregorianCalendar.DATE,1);
		
		if (accert_pgiro.getEsercizio().intValue() > gc.get(java.util.GregorianCalendar.YEAR) )
			accert_scad.setDt_scadenza_incasso( java.sql.Timestamp.valueOf( accert_pgiro.getEsercizio().intValue() + "-" + 
																   		    gc.get(java.util.GregorianCalendar.MONTH) + "-" +
																   		    gc.get(java.util.GregorianCalendar.DATE) +" 00:00:00.0" ) );
		else
			accert_scad.setDt_scadenza_incasso( accert_pgiro.getDt_registrazione() );
		*/	
		accert_scad.setIm_scadenza( accert_pgiro.getIm_accertamento() );
		accert_scad.setDs_scadenza( accert_pgiro.getDs_accertamento() );
		accert_scad.setDt_scadenza_emissione_fattura( accert_scad.getDt_scadenza_incasso() );

		return accert_scad;
}
public AccertamentoPGiroBulk creaAccertamentoDiIncassoIVA( UserContext userContext, ReversaleBulk reversale,boolean split ) throws ComponentException
{
	try
	{
		AccertamentoPGiroBulk accert_pgiro = new AccertamentoPGiroBulk();
		accert_pgiro.setToBeCreated();
		accert_pgiro.setFl_pgiro( Boolean.TRUE );
		/*segnalazione 616 - begin */
		accert_pgiro.setFl_isTronco( true );
		/*segnalazione 616 - end */		
		accert_pgiro.setUser( reversale.getUser() );
		accert_pgiro.setEsercizio( reversale.getEsercizio());
		accert_pgiro.setEsercizio_originale( reversale.getEsercizio());
		accert_pgiro.setUnita_organizzativa( reversale.getUnita_organizzativa());
		accert_pgiro.setEsercizio_competenza( reversale.getEsercizio());
		accert_pgiro.setDt_scadenza( reversale.getDt_emissione());
		accert_pgiro.setCd_cds_origine( reversale.getCd_cds_origine() );
		accert_pgiro.setCd_uo_origine( reversale.getCd_uo_origine() );
		accert_pgiro.setDt_registrazione( reversale.getDt_emissione() );
		accert_pgiro.setDs_accertamento( "Annotazione d'Entrata su Partita di Giro creata in automatico per Incasso IVA" );
		accert_pgiro.setIm_accertamento( reversale.getIm_reversale() );
		accert_pgiro.setRiportato("N");
//		accert_pgiro.setCd_terzo( reversale.getReversale_terzo().getCd_terzo());
		accert_pgiro.setDebitore( reversale.getReversale_terzo().getTerzo());
		
		Configurazione_cnrBulk config = createConfigurazioneCnrComponentSession().getConfigurazione( userContext, reversale.getEsercizio(), null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_ELEMENTO_VOCE_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_VOCE_IVA_FATTURA_ESTERA );
		if ( config == null  || config.getVal01() == null ||(split && config.getVal02() == null))
			throw new ApplicationException("Configurazione CNR: manca la definizione del CAPITOLO FINANZIARIO per l'annotazione di entrata su partita di giro");

		String tiVoce = null;
		Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk)getHome(userContext,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(reversale.getEsercizio()));
        if (parametriCnr==null || !parametriCnr.getFl_nuovo_pdg())
        	tiVoce = Elemento_voceHome.APPARTENENZA_CDS;
       	else
        	tiVoce = Elemento_voceHome.APPARTENENZA_CNR;
        V_voce_f_partita_giroBulk voce_f;
        if (split)
        	voce_f= new V_voce_f_partita_giroBulk( config.getVal02(), reversale.getEsercizio(), tiVoce, Elemento_voceHome.GESTIONE_ENTRATE );
        else
        	voce_f= new V_voce_f_partita_giroBulk( config.getVal01(), reversale.getEsercizio(), tiVoce, Elemento_voceHome.GESTIONE_ENTRATE );
		voce_f = (V_voce_f_partita_giroBulk) getHome( userContext, V_voce_f_partita_giroBulk.class ).findByPrimaryKey( voce_f );
		if ( voce_f == null )
			throw new ApplicationException("Impossibile recuperare CAPITOLO FINANZIARIO per l'annotazione di entrata su partita di giro");
		accert_pgiro.setTi_appartenenza( voce_f.getTi_appartenenza() );
		accert_pgiro.setTi_gestione( voce_f.getTi_gestione() );
		accert_pgiro.setCd_elemento_voce( voce_f.getCd_voce() );
		accert_pgiro.setCd_voce( voce_f.getCd_voce() );
		accert_pgiro.setCapitolo( voce_f );

		return (AccertamentoPGiroBulk) creaConBulk( userContext, accert_pgiro );
		
	}
	catch ( Exception e )
	{
		throw handleException( e )	;

	}	
}
/** 
  *  creazione
  *    PreCondition:
  *      un accertamento pgiro ed un impegno pgiro sono stati creati ed e' necessario creare 
  *      l'associazione (Ass_obb_acr_pgiroBulk) fra i due
  *    PostCondition:
  *      L'associazione fra impegno e accertamento su partita di giro e' stata creata
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param accert_pgiro <code>AccertamentoPGiroBulk</code> l'accertamento su partita di giro da associare ad un impegno
  * @param imp_pgiro <code>ImpegnoPGiroBulk</code> l'impegno da associare ad un accertamento su partita di giro
  *
  * @return <code>Ass_obb_acr_pgiroBulk</code> L'associazione obbligazione-accertamento su partita di giro da creare
 */

public Ass_obb_acr_pgiroBulk creaAss_obb_acr_pgiro(UserContext uc, AccertamentoPGiroBulk accert_pgiro, ImpegnoPGiroBulk imp_pgiro ) throws ComponentException
{
		Ass_obb_acr_pgiroBulk ass_ao_pgiro = new Ass_obb_acr_pgiroBulk();
		ass_ao_pgiro.setUser( accert_pgiro.getUser() );
		ass_ao_pgiro.setToBeCreated();
		
		ass_ao_pgiro.setEsercizio_ori_accertamento( accert_pgiro.getEsercizio_originale());
		ass_ao_pgiro.setPg_accertamento( accert_pgiro.getPg_accertamento());

		ass_ao_pgiro.setEsercizio_ori_obbligazione( imp_pgiro.getEsercizio_originale());
		ass_ao_pgiro.setPg_obbligazione( imp_pgiro.getPg_obbligazione());

		// campi chiave
		ass_ao_pgiro.setEsercizio( accert_pgiro.getEsercizio() );
		ass_ao_pgiro.setCd_cds( accert_pgiro.getCd_cds() );
		ass_ao_pgiro.setTi_origine( ass_ao_pgiro.TIPO_ENTRATA );

		return (Ass_obb_acr_pgiroBulk) super.creaConBulk( uc, ass_ao_pgiro );
		
}
/** 
  *  creazione
  *    PreCondition:
  *      L'utente richiede la creazione di un nuovo accertamento su partita di giro
  *    PostCondition:
  *      L'accertamento, dopo essere stato validato (metodo verificaAccertamento), viene creato e in automatico 
  *		 viene creata una scadenza (metodo creaAccertamento_scadenzario) e
  *      un dettaglio di scadenza (metodo creaAccertamento_scad_voce). I saldi relativi alla voce del piano
  *      dell'accertamento vengono aggiornati (metodo aggiornaCapitoloSaldoAccertamento). Alla componente che 
  *		 gestisce gli ImpegniPGiro viene chiesta la creazione di un Impegno (metodo creaObbligazione). 
  *		 Viene creata l'associazione (Ass_obb_acr_pgiroBulk) fra l'accertamento
  *      e l'impegno su partita di giro (metodo creaAss_obb_acr_pgiro)
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'accertamento su partita di giro da creare
  *
  * @return accert_pgiro L'accertamento su partita di giro creato
 */

public OggettoBulk creaConBulk (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	try
	{
		AccertamentoPGiroBulk accert_pgiro = ( AccertamentoPGiroBulk ) bulk;
		Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk)getHome(uc,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(accert_pgiro.getEsercizio()));
		
		if (parametriCnr.getFl_nuova_gestione_pg().booleanValue() ) {
			if(accert_pgiro.getElemento_voceContr()!=null  && accert_pgiro.getElemento_voceContr().getCd_elemento_voce()!=null)
				accert_pgiro.setElemento_voceContr((Elemento_voceBulk)getHome(uc,Elemento_voceBulk.class).findByPrimaryKey(new Elemento_voceBulk(accert_pgiro.getElemento_voceContr().getCd_elemento_voce(),accert_pgiro.getEsercizio(),Elemento_voceHome.APPARTENENZA_CDS, Elemento_voceHome.GESTIONE_SPESE)));
		}
		
		accert_pgiro.setCd_cds( accert_pgiro.getUnita_organizzativa().getCd_unita_padre() );
		
		if ( accert_pgiro.getCd_uo_ente() == null )
			accert_pgiro.setCd_uo_ente(((Unita_organizzativa_enteBulk)getHome( uc, Unita_organizzativa_enteBulk.class ).findAll().get(0)).getCd_unita_organizzativa());
			
		if ( accert_pgiro.getCd_unita_organizzativa().equals( accert_pgiro.getCd_uo_ente()) )
			accert_pgiro.setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_ACR);
		else
			accert_pgiro.setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_ACR_PGIRO);

		accert_pgiro.setCd_elemento_voce( accert_pgiro.getCapitolo().getCd_titolo_capitolo() );

		if (! ((AccertamentoHome)getHome(uc, AccertamentoBulk.class)).verificaStatoEsercizio((AccertamentoBulk) bulk))
			throw handleException( new ApplicationException( "Non e' possibile creare accertamenti: esercizio del Cds non ancora aperto!") );

		verificaAccertamento( uc, accert_pgiro );
		Accertamento_scadenzarioBulk accert_scad = creaAccertamento_scadenzario( uc, accert_pgiro);
		creaAccertamento_scad_voce( uc, accert_scad );
	
		super.creaConBulk( uc, accert_pgiro );

		if ( !uc.isTransactional() )
			//aggiorna i saldi 
			aggiornaCapitoloSaldoAccertamento( uc, accert_pgiro, INSERIMENTO);		
			
		// ImpegnoPGiroBulk imp_pgiro = (ImpegnoPGiroBulk) getObbligazionePGiroComponent().creaConBulk( uc, accert_pgiro, obblig_scad_voce);
		ImpegnoPGiroBulk imp_pgiro = createObbligazionePGiroComponent().creaObbligazione( uc, accert_pgiro);
	
		Ass_obb_acr_pgiroBulk ass_ao_pgiro = (Ass_obb_acr_pgiroBulk) creaAss_obb_acr_pgiro( uc, accert_pgiro, imp_pgiro);

		// Verifico che l'esercizio del CDS sia stato aperto
		if (! ((AccertamentoHome)getHome(uc, AccertamentoBulk.class)).verificaStatoEsercizio((AccertamentoBulk) bulk))
			throw handleException( new ApplicationException( "Non e' possibile creare accertamenti: esercizio del Cds non ancora aperto!") );

		return accert_pgiro;
	}
	catch ( Exception e ) 
	{
		throw handleException( bulk, e );
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
 * Crea la ComponentSession da usare per effettuare le operazioni di CRUD su Obbligazioni in Partita di Giro
 *
 * @return ObbligazionePGiroComponentSession l'istanza di <code>ObbligazionePGiroComponentSession</code> che serve per gestire un impegno
 */
private ObbligazionePGiroComponentSession createObbligazionePGiroComponent( ) throws it.cnr.jada.comp.ComponentException
{
	try
	{
		return (ObbligazionePGiroComponentSession) EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ObbligazionePGiroComponentSession");

	}
	catch (Exception e )
	{
		throw handleException( e ) ;
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
  *      La richiesta di cancellazione di un accertamento su partita di giro e' stata generata
  *    PostCondition:
  *      L'accertamento, la sua scadenza e il suo dettaglio vengono cancellati (logicamente). I saldi relativi 
  *		ai documenti contabili vengono aggiornati (metodo aggiornaCapitoloSaldoAccertamento).
  *  errore - doc.amm.associati
  *    PreCondition:
  *      La richiesta di cancellazione di un accertamento su partita di giro e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'accertamento
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la cancellazione
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param accert_pgiro <code>AccertamentoPGiroBulk</code> l'accertamento su partita di giro da cancellare (logicamente)
  *
 */
public void eliminaAccertamento(UserContext aUC,AccertamentoPGiroBulk accert_pgiro ) throws ComponentException
{
	try
	{
		if ( accert_pgiro.isAssociataADocAmm() )
			throw new ApplicationException( "Impossibile cancellare l'Annotazione d'Entrata su Partita di Giro perche' ha documenti amministrativi associati");


		accert_pgiro.setCrudStatus(OggettoBulk.NORMAL);
      	accert_pgiro.setDt_cancellazione( DateServices.getDt_valida( aUC));
		
		// azzero tutti gli importi	
		accert_pgiro.storna();
		// aggiornamento accertamento + scadenze + dettagli
		makeBulkPersistent( aUC, accert_pgiro);

		//aggiorno i saldi
		aggiornaCapitoloSaldoAccertamento( aUC, accert_pgiro, MODIFICA );


		/*
		accert_pgiro.setToBeDeleted();		
		makeBulkPersistent( aUC, accert_pgiro );
		aggiornaCapitoloSaldoAccertamento( aUC, accert_pgiro, CANCELLAZIONE )	;
		*/

	}
	catch ( Exception e )
	{
		throw handleException( e );
	}
}
/** 
  *  cancellazione (logica)
  *    PreCondition:
  *      L'utente richiede la cancellazione di un accertamento su partita di giro
  *    PostCondition:
  *     Viene inoltrata la richiesta di cancellazione (logica) dell'accertamento su partita di giro
  *		(metodo annullaAccertamento)
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'accertamento su partita di giro da cancellare (logicamente)
  *
 */
public void eliminaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	try
	{
		annullaAccertamento( aUC, (AccertamentoPGiroBulk) bulk );
	}
	catch ( Exception e )
	{
		throw handleException( bulk, e );
	}
}
/** 
  *  ricerca CDS SAC
  *    PreCondition:
  *      La richiesta di identificazione del Cds di tipo SAC e' stata generata
  *    PostCondition:
  *      Un'istanza di CdsBulk viene restituita con impostati tutti i datio del Cds SAC
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  *
  * @return CdsBulk il centro di spesa con tipologia SAC
  *			null nessun centro di spesa con tipologia SAC identificato
 */


public CdsBulk findCdsSAC (UserContext userContext) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
{
	CdsHome cdsHome = (CdsHome) getHome( userContext, CdsBulk.class );
	SQLBuilder sql = cdsHome.createSQLBuilder();
	sql.addClause("AND","cd_tipo_unita",sql.EQUALS, (Tipo_unita_organizzativaHome.TIPO_UO_SAC) );
	sql.addClause("AND","fl_cds",sql.EQUALS, new Boolean(true) );
	List result = cdsHome.fetchAll( sql );
	if (result.size() > 0)
		return (CdsBulk) result.get(0);		
	return null;
}
/** 
  *  ricerca Unità Organizzative
  *    PreCondition:
  *      La richiesta di identificazione delle Unità Organizzative per cui e' possibile creare un accerta-
  *      mento PGIRO e' stata generata
  *    PostCondition:
  *      Una lista contente l'UO Ente + l'UO di scrivania (se diverso da Ente) viene restituita
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param accert_pgiro <code>AccertamentoPGiroBulk</code> l'accertamento su partita di giro da creare
  *
  * @return result la lista delle unità organizzative definite per l'accertamento su partita di giro
 */

public List findUnitaOrganizzativaOptions (UserContext userContext,AccertamentoPGiroBulk accert_pgiro) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
{
	SQLBuilder sql = getHome( userContext, Unita_organizzativa_enteBulk.class ).createSQLBuilder();
	List result = getHome( userContext, Unita_organizzativa_enteBulk.class ).fetchAll( sql );
	accert_pgiro.setCd_uo_ente( ((Unita_organizzativaBulk)result.get(0)).getCd_unita_organizzativa());
	if ( !accert_pgiro.getCd_uo_ente().equals(  ((CNRUserContext) userContext).getCd_unita_organizzativa()))
		result.add( accert_pgiro.getUnita_organizzativa() );
	return result;
	
}
/**
  *  ricerca Voce del piano per accertamento pgiro del CNR
  *    PreCondition:
  *      La richiesta di identificazione di tutti i dati del capitolo di entrata del CNR su partite di giro
  *      data un'associazione fra Capitoli di Entrata e di Spesa per Partita di Giro e' stata generata
  *    PostCondition:
  *      Sono stati recuperati tutti i dati relativi alla voce del piano di entrata del CNR con flag mastrino = true, 
  *      codice titolo-capitolo uguale a quello specificato nell'associazione entrate/spese per partite di giro, e
  *      codice Cds uguale al codice del Cds SAC
  *
  *  ricerca Voce del piano per accertamento pgiro del Cds
  *    PreCondition:
  *      La richiesta di identificazione di tutti i dati del capitolo di entrata di un Cds su partite di giro
  *      data un'associazione fra Capitoli di Entrata e di Spesa per Partita di Giro e' stata generata
  *    PostCondition:
  *      Sono stati recuperati tutti i dati relativi alla voce del piano di entrata del CdS con flag mastrino = true, 
  *      codice titolo-capitolo uguale a quello specificato nell'associazione entrate/spese per partite di giro
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param impegno lo <code>ImpegnoPGiroBulk</code> l'impegno su partita di giro
  * @param ass_pgiro lo <code>Ass_partita_giroBulk</code> l'associazione enatrate-spese dei capitoli su partita di giro
  *
  * @return Voce_fBulk La voce del piano dei conti identificata
  *			null nessuna voce del piano dei conti identificata
 */
public Voce_fBulk findVoce_f(UserContext userContext,ImpegnoPGiroBulk impegno, Ass_partita_giroBulk ass_pgiro) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
{
	
	Voce_fHome voce_fHome = (Voce_fHome) getHome( userContext, Voce_fBulk.class );

	String cd_uo = null;
	if ( impegno.getCd_unita_organizzativa().equals( impegno.getCd_uo_ente()) ) //CNR
		cd_uo = ((CNRUserContext)userContext).getCd_unita_organizzativa();

	return voce_fHome.findVoce_fFor(ass_pgiro, cd_uo);
}
/** 
  *  inizializzazione per inserimento
  *    PreCondition:
  *      La richiesta di inizializzazione di un AccertamentoPGiroBulk per inserimento
  *      e' stata generata
  *    PostCondition:
  *      Viene impostata la data di registrazione dell'accertamento con la data odierna, il codice Cds e il codice Cds di origine 
  *      con il codice Cds di scrivania
  *  inizializzazione per inserimento - errore
  *    PreCondition:
  *      L'unità organizzativa è uguale a quella dell'Ente
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente che l'Ente non è abilitato a creare documenti su partita di giro
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'accertamento su partita di giro da inizializzare per l'inserimento
  *
  * @return <code>OggettoBulk</code> l'accertamento su partita di giro inizializzato per l'inserimento
 */

public OggettoBulk inizializzaBulkPerInserimento (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	AccertamentoPGiroBulk accert_pgiro = (AccertamentoPGiroBulk) bulk;
	try
	{
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk)getHome( aUC, Unita_organizzativa_enteBulk.class).findAll().get(0);
		
		accert_pgiro.setDt_registrazione( DateServices.getDt_valida(aUC) );
		// 	imp_pgiro.setCds( (CdsBulk) getHome( aUC, CdsBulk.class).findByPrimaryKey( imp_pgiro.getUnita_organizzativa().getUnita_padre() ));
		accert_pgiro.setCd_cds(((CNRUserContext) aUC).getCd_cds() );
		accert_pgiro.setCd_cds_origine( accert_pgiro.getCd_cds() );
		verificaStatoEsercizio( aUC, accert_pgiro.getEsercizio(), accert_pgiro.getCd_cds());
		accert_pgiro.setDt_scadenza(DateServices.getDt_valida(aUC));
		/* if (!((ObbligazioneHome)getHome(aUC, obbligazione.getClass())).verificaStatoEsercizio(obbligazione))
			throw handleException( new ApplicationException( "Non e' possibile creare obbligazioni: esercizio non ancora aperto!") );*/

		// se l'unità organizzativa è uguale a quella dell'Ente, non è possibile creare documenti su partita di giro
		if ( accert_pgiro.getCd_unita_organizzativa().equals( uoEnte.getCd_unita_organizzativa() ))
			throw new ApplicationException("Funzione non consentita per utente abilitato a " + uoEnte.getCd_unita_organizzativa() ); 		
		return super.inizializzaBulkPerInserimento( aUC, accert_pgiro );
	}
	catch ( Exception e )
	{
		throw handleException(accert_pgiro, e);
	}
	
}
/** 
  *  inizializzazione per modifica
  *    PreCondition:
  *      La richiesta di inizializzazione di un AccertamentoPGiroBulk per modifica
  *      e' stata generata
  *    PostCondition:
  *      Vengono recuperati la scadenza e il dettaglio di scadenza associati all'accertamento.
  *      Viene recuperata l'associazione fra l'accertamento e l'impegno
  *		 Viene recuperato l'impegno associato all'accertamento e la relativa scadenza e dettaglio scadenza
  *
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'accertamento su partita di giro da inizializzare per la modifica
  *
  * @return accert_pgiro l'accertamento su partita di giro inizializzato per la modifica
 */

public OggettoBulk inizializzaBulkPerModifica (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	try
	{
		bulk = super.inizializzaBulkPerModifica( aUC, bulk );
		
		AccertamentoPGiroBulk accert_pgiro = (AccertamentoPGiroBulk) bulk;
		ImpegnoPGiroBulk imp_pgiro = null;
		accert_pgiro.setCd_terzo_iniziale( accert_pgiro.getCd_terzo());
		accert_pgiro.setIm_iniziale_accertamento(accert_pgiro.getIm_accertamento());
	
	//query per recuperare la scadenza dell'accertamento
	//accertamentoPgiro.getAccertamento_scadenzarioColl().add( scadenza );
		Accertamento_scadenzarioHome accert_scadHome = (Accertamento_scadenzarioHome) getHome( aUC, Accertamento_scadenzarioBulk.class );

		SQLBuilder sql = accert_scadHome.createSQLBuilder();
		sql.addClause("AND","cd_cds",sql.EQUALS, accert_pgiro.getCd_cds() );
		sql.addClause("AND","esercizio",sql.EQUALS, accert_pgiro.getEsercizio() );
		sql.addClause("AND","esercizio_originale",sql.EQUALS, accert_pgiro.getEsercizio_originale() );
		sql.addClause("AND","pg_accertamento",sql.EQUALS, accert_pgiro.getPg_accertamento());

		List result = accert_scadHome.fetchAll( sql );
		Accertamento_scadenzarioBulk accert_scad = (Accertamento_scadenzarioBulk) result.get(0);

		accert_pgiro.setDt_scadenza( accert_scad.getDt_scadenza_incasso());
		accert_pgiro.getAccertamento_scadenzarioColl().add( accert_scad );
		accert_scad.setAccertamento( accert_pgiro);

		//carico l'eventuale doc.amministrativo legato alla scadenza
		V_doc_attivo_accertamentoBulk docAttivo = accert_scadHome.findDoc_attivo( accert_scad );
		if ( docAttivo != null)
		{
			accert_scad.setEsercizio_doc_attivo( docAttivo.getEsercizio());
			accert_scad.setPg_doc_attivo( docAttivo.getPg_documento_amm());
			accert_pgiro.setEsercizio_doc_attivo( docAttivo.getEsercizio());
			accert_pgiro.setPg_doc_attivo( docAttivo.getPg_documento_amm());
			accert_pgiro.setCd_tipo_documento_amm( docAttivo.getCd_tipo_documento_amm());
		}

		//carico l'eventuale reversale associata
		Reversale_rigaBulk reversale = accert_scadHome.findReversale( accert_scad );
		if ( reversale != null )
		{
			accert_pgiro.setEsercizio_reversale( reversale.getEsercizio());
			accert_pgiro.setPg_reversale( reversale.getPg_reversale());
		}		
		
	//query per recuperare scad_voce dell'accertamento
	//scadenza.getAccertamento_scad_voceColl().add( scad_voce );
		Accertamento_scad_voceHome accert_scad_voceHome = (Accertamento_scad_voceHome) getHome( aUC, Accertamento_scad_voceBulk.class );

		sql = accert_scad_voceHome.createSQLBuilder();
		sql.addClause("AND","cd_cds",sql.EQUALS, accert_pgiro.getCd_cds() );
		sql.addClause("AND","esercizio",sql.EQUALS, accert_pgiro.getEsercizio() );
		sql.addClause("AND","esercizio_originale",sql.EQUALS, accert_pgiro.getEsercizio_originale() );
		sql.addClause("AND","pg_accertamento",sql.EQUALS, accert_pgiro.getPg_accertamento());

		result = accert_scad_voceHome.fetchAll( sql );
		Accertamento_scad_voceBulk accert_scad_voce = (Accertamento_scad_voceBulk) result.get(0);

		accert_scad.getAccertamento_scad_voceColl().add( accert_scad_voce );
		accert_scad_voce.setAccertamento_scadenzario( accert_scad);

	//query associazione
	//accertamentoPgiro.setAssociazione( associazione );
	//associazione.setAccertamento ( accertamentoPgiro);
		Ass_obb_acr_pgiroHome associazioneHome = (Ass_obb_acr_pgiroHome) getHome( aUC, Ass_obb_acr_pgiroBulk.class );
		
		sql = associazioneHome.createSQLBuilder();
		sql.addClause("AND","cd_cds",sql.EQUALS, accert_pgiro.getCd_cds() );
		sql.addClause("AND","esercizio",sql.EQUALS, accert_pgiro.getEsercizio() );
		sql.addClause("AND","esercizio_ori_accertamento",sql.EQUALS, accert_pgiro.getEsercizio_originale() );
		sql.addClause("AND","pg_accertamento",sql.EQUALS, accert_pgiro.getPg_accertamento() );

		result = associazioneHome.fetchAll( sql );
		if ( result.size() > 0 )
		{
			Ass_obb_acr_pgiroBulk associazione = (Ass_obb_acr_pgiroBulk) result.get(0);

			accert_pgiro.setAssociazione( associazione );
			associazione.setAccertamento( accert_pgiro );
            
		//query per recuperare l'impegnoPgiro
		//associazione.setImpegno( impegnoPgiro);
		//impegnoPgiro.setAssociazione( associazione )
			ImpegnoPGiroHome imp_pgiroHome = (ImpegnoPGiroHome) getHome( aUC, ImpegnoPGiroBulk.class );

			sql = imp_pgiroHome.createSQLBuilder();
			sql.addClause("AND","cd_cds",sql.EQUALS, associazione.getCd_cds() );
			sql.addClause("AND","esercizio",sql.EQUALS, associazione.getEsercizio());
			sql.addClause("AND","esercizio_originale",sql.EQUALS, associazione.getEsercizio_ori_obbligazione());
			sql.addClause("AND","pg_obbligazione",sql.EQUALS, associazione.getPg_obbligazione() );

			result = imp_pgiroHome.fetchAll( sql );

			imp_pgiro = (ImpegnoPGiroBulk) result.get(0);

			imp_pgiro.setAssociazione( associazione );
			associazione.setImpegno( imp_pgiro );

			
			if(imp_pgiro.getElemento_voce()!=null) {
				Elemento_voceBulk ev=(Elemento_voceBulk)getHome(aUC, Elemento_voceBulk.class).findByPrimaryKey(
						new Elemento_voceBulk(imp_pgiro.getElemento_voce().getCd_elemento_voce(),imp_pgiro.getElemento_voce().getEsercizio(),
								imp_pgiro.getElemento_voce().getTi_appartenenza(),imp_pgiro.getElemento_voce().getTi_gestione()));
				accert_pgiro.setElemento_voceContr(ev);
			}
		//query per recuperare la scadenza dell'obbligazione
		//impegnoPgiro.getObbligazione_scadenzarioColl().add( scadenza );
			Obbligazione_scadenzarioHome obblig_scadHome = (Obbligazione_scadenzarioHome) getHome( aUC, Obbligazione_scadenzarioBulk.class );

			sql = obblig_scadHome.createSQLBuilder();
			sql.addClause("AND","cd_cds",sql.EQUALS, imp_pgiro.getCd_cds() );
			sql.addClause("AND","esercizio",sql.EQUALS, imp_pgiro.getEsercizio() );
			sql.addClause("AND","esercizio_originale",sql.EQUALS, imp_pgiro.getEsercizio_originale() );
			sql.addClause("AND","pg_obbligazione",sql.EQUALS, imp_pgiro.getPg_obbligazione());

			result = obblig_scadHome.fetchAll( sql );
			Obbligazione_scadenzarioBulk obblig_scad = (Obbligazione_scadenzarioBulk) result.get(0);

			imp_pgiro.setDt_scadenza( obblig_scad.getDt_scadenza());

			imp_pgiro.getObbligazione_scadenzarioColl().add( obblig_scad );
			obblig_scad.setObbligazione(imp_pgiro);

			//carico l'eventuale doc.amministrativo legato alla scadenza
			V_doc_passivo_obbligazioneBulk docPassivo = obblig_scadHome.findDoc_passivo( obblig_scad );
			if ( docPassivo != null)
			{
				obblig_scad.setEsercizio_doc_passivo( docPassivo.getEsercizio());
				obblig_scad.setPg_doc_passivo( docPassivo.getPg_documento_amm());
			}
		

		//query per recuperare scad_voce dell'obbligazione
		//scadenza.getObbligazione_scad_voceColl().add( scad_voce );
			Obbligazione_scad_voceHome obblig_scad_voceHome = (Obbligazione_scad_voceHome) getHome( aUC, Obbligazione_scad_voceBulk.class );

			sql = obblig_scad_voceHome.createSQLBuilder();
			sql.addClause("AND","cd_cds",sql.EQUALS, imp_pgiro.getCd_cds() );
			sql.addClause("AND","esercizio",sql.EQUALS, imp_pgiro.getEsercizio() );
			sql.addClause("AND","esercizio_originale",sql.EQUALS, imp_pgiro.getEsercizio_originale() );
			sql.addClause("AND","pg_obbligazione",sql.EQUALS, imp_pgiro.getPg_obbligazione());

			result = obblig_scad_voceHome.fetchAll( sql );
			Obbligazione_scad_voceBulk obblig_scad_voce = (Obbligazione_scad_voceBulk) result.get(0);

			obblig_scad.getObbligazione_scad_voceColl().add( obblig_scad_voce );
			obblig_scad_voce.setObbligazione_scadenzario( obblig_scad);
		}			
		
	//query per recuperare cd_uo_ente dell'accertamento
		Unita_organizzativa_enteHome uo_enteHome = (Unita_organizzativa_enteHome) getHome( aUC, Unita_organizzativa_enteBulk.class );

		sql = uo_enteHome.createSQLBuilder();
		result = uo_enteHome.fetchAll( sql );
		Unita_organizzativa_enteBulk uo_ente = (Unita_organizzativa_enteBulk) result.get(0);

		accert_pgiro.setCd_uo_ente( uo_ente.getCd_unita_organizzativa() );
		if ( imp_pgiro != null )
		{
			imp_pgiro.setCd_uo_ente( uo_ente.getCd_unita_organizzativa() );

			if (( accert_pgiro.getDt_cancellazione() == null && imp_pgiro.getDt_cancellazione() != null ) ||
				 ( accert_pgiro.getDt_cancellazione() != null && imp_pgiro.getDt_cancellazione() == null ) )
			{	 
				 accert_pgiro.setFl_isTronco( true );
			 	imp_pgiro.setFl_isTronco( true );
			}
		}	

		return accert_pgiro;
	}
	catch ( Exception e )
	{
		throw handleException(bulk, e)	;
	}
}
/** 
  *  Inizializzazione di un accertamento per ricerca - cds non ente
  *    PreCondition:
  *      La richiesta di inizializzare un accertamento per ricerca e' stata generata
  *      L'uo di scrivania è diversa dall'ente
  *    PostCondition:
  *      L'accertamento e' stato inizializzato con il cds e l'uo di origine uguali a quelli di scrivania
  *
  *  Inizializzazione di un accertamento per ricerca - cds ente
  *    PreCondition:
  *      La richiesta di inizializzare un accertamento per ricerca e' stata generata
  *      L'uo di scrivania è l'ente
  *    PostCondition:
  *      L'accertamento e' stato inizializzato e il cds e l'uo di origine non vengono valorizzati
  *
  *  
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'istanza di AccertamentoPGiroBulk da inizializzare
  * @return <code>OggettoBulk</code> l'istanza di AccertamentoPGiroBulk inizializzata
  *
 */

public OggettoBulk inizializzaBulkPerRicerca (UserContext userContext,OggettoBulk bulk) throws ComponentException
{
	try 
	{
		AccertamentoPGiroBulk accertamento = (AccertamentoPGiroBulk) super.inizializzaBulkPerRicerca( userContext, bulk );
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0); 
		//imposto cds e uo origine
		if ( !uoEnte.getCd_unita_organizzativa().equals(((CNRUserContext)userContext).getCd_unita_organizzativa()))
		{
			Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHome( userContext, Unita_organizzativaBulk.class ).findByPrimaryKey( new Unita_organizzativaBulk( ((CNRUserContext)userContext).getCd_unita_organizzativa()));
			accertamento.setCd_uo_origine( uoScrivania.getCd_unita_organizzativa());		
			accertamento.setCd_cds_origine( uoScrivania.getCd_unita_padre());
			if ( accertamento.isResiduo() )
			{
				accertamento.setCd_cds( uoEnte.getCd_unita_padre());
				accertamento.setCd_unita_organizzativa( uoEnte.getCd_unita_organizzativa());
			}	
			
		}
		else
		{
			accertamento.setCd_cds( uoEnte.getCd_unita_padre());
			accertamento.setCd_unita_organizzativa( uoEnte.getCd_unita_organizzativa());					
		}		

		return accertamento;
	}
	catch( Exception e )
	{
		throw handleException( e );
	}		
}
/** 
  *  Inizializzazione di un accertamento per ricerca - cds non ente
  *    PreCondition:
  *      La richiesta di inizializzare un accertamento per ricerca e' stata generata
  *      L'uo di scrivania è diversa dall'ente
  *    PostCondition:
  *      L'accertamento e' stato inizializzato con il cds e l'uo di origine uguali a quelli di scrivania
  *
  *  Inizializzazione di un accertamento per ricerca - cds ente
  *    PreCondition:
  *      La richiesta di inizializzare un accertamento per ricerca e' stata generata
  *      L'uo di scrivania è l'ente
  *    PostCondition:
  *      L'accertamento e' stato inizializzato e il cds e l'uo di origine non vengono valorizzati
  *
  *  
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'istanza di AccertamentoPGiroBulk da inizializzare
  * @return <code>OggettoBulk</code> l'istanza di AccertamentoPGiroBulk inizializzata
  *
 */

public OggettoBulk inizializzaBulkPerRicercaLibera (UserContext userContext,OggettoBulk bulk) throws ComponentException
{
	return inizializzaBulkPerRicerca( userContext, bulk );
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
/** 
  *  modifica importo
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di un impegno su partita di giro
  *    PostCondition:
  *      L'importo dell'accertamento associato all'impegno viene modificato e, in cascata, vengono modificati gli importi 
  *      relativi alla scadenza e al dettaglio scadenza dell'accertamento. I saldi relativi ai documenti contabili vengono
  *		 aggiornati (metodo aggiornaCapitoloSaldoAccertamento). Vengono aggiornati gli stati COAN e COGE degli eventuali
  *		 documenti amministrativi associati (metodo aggiornaStatoCOAN_COGEDocAmm)
  *  errore modifca importo - doc.amm.associati
  *    PreCondition:
  *      La richiesta di modifica dell'importo di un impegno su partita di giro e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'accertamento associato all'impegno
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la modifica
  *  modifica capitolo
  *    PreCondition:
  *      L'utente richiede la modifica del capitolo di un impegno su partita di giro
  *    PostCondition:
  *      Il capitolo dell'accertamento associato all'impegno viene aggiornato col valore
  *      presente nell'associazione fra capitolo di spesa e capitolo di entrata su partite di giro
  *  modifica capitolo - errore
  *    PreCondition:
  *      L'utente richiede la modifica del capitolo di un impegno su partita di giro ma non esiste
  *      l'associazione fra il nuovo capitolo di spesa dell'impegno e un capitolo di entrata
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la modifica
  *  modifica data di scadenza
  *    PreCondition:
  *      L'utente richiede la modifica della data di scadenza di un'obbligazione su partita di giro
  *    PostCondition:
  *      La data della scadenza dell'accertamento associato all'impegno viene aggiornata.
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param imp_pgiro <code>ImpegnoPGiroBulk</code> l'impegno su partita di giro a cui è associato l'accertamento pgiro da modificare
  *
  * @return accert_pgiro l'accertamento su partita di giro modificato
 */

public AccertamentoPGiroBulk modificaAccertamento(UserContext uc,ImpegnoPGiroBulk imp_pgiro) throws ComponentException
{
	try
	{
		if ( imp_pgiro.getAssociazione() == null )
			return null;
		
		// importo
		AccertamentoPGiroBulk accert_pgiro = (AccertamentoPGiroBulk)imp_pgiro.getAssociazione().getAccertamento();

		//segnalo impossibilità di modificare importo se ci sono doc amministrativi associati
		if ( 
			 /*errore !imp_pgiro.isFromDocAmm() && */ 
			accert_pgiro.isAssociataADocAmm() && imp_pgiro.getIm_iniziale_obbligazione() != null &&
			imp_pgiro.getIm_iniziale_obbligazione().compareTo( imp_pgiro.getIm_obbligazione()) != 0 )
			throw new ApplicationException( "Impossibile variare importo dell'Annotazione d'Entrata su Partita di Giro perche' e' associata a doc. amministrativi");


		// accert_pgiro.setCd_terzo( imp_pgiro.getCd_terzo() );
		accert_pgiro.setCd_riferimento_contratto( imp_pgiro.getCd_riferimento_contratto() );
		accert_pgiro.setCd_fondo_ricerca( imp_pgiro.getCd_fondo_ricerca() );
		accert_pgiro.setDt_scadenza_contratto( imp_pgiro.getDt_scadenza_contratto() );
		accert_pgiro.setIm_accertamento( imp_pgiro.getIm_obbligazione() );
		accert_pgiro.setDt_scadenza( imp_pgiro.getDt_scadenza());
		accert_pgiro.setToBeUpdated();
		
		Accertamento_scadenzarioBulk accert_scadenzario = (Accertamento_scadenzarioBulk) imp_pgiro.getAssociazione().getAccertamento().getAccertamento_scadenzarioColl().get(0);
		accert_scadenzario.setIm_scadenza(imp_pgiro.getIm_obbligazione() );
		accert_scadenzario.setDt_scadenza_incasso( imp_pgiro.getDt_scadenza());
		accert_scadenzario.setToBeUpdated();
		
		Accertamento_scad_voceBulk accert_scad_voce = (Accertamento_scad_voceBulk)accert_scadenzario.getAccertamento_scad_voceColl().get(0);
		accert_scad_voce.setIm_voce( imp_pgiro.getIm_obbligazione() );
		accert_scad_voce.setToBeUpdated();
		
		//capitolo
		//query per recuperare il nuovo capitolo per l'obbligazione_scad_voce
		//set capitolo obbligazione_scad_voce
		//query per recuperare il nuovo capitolo per l'accertamento
		//set capitolo accertamento
		
		Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk)getHome(uc,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(imp_pgiro.getEsercizio()));
		Ass_partita_giroBulk ass_pgiro =null;
		if (parametriCnr.getFl_nuova_gestione_pg().booleanValue() ) {
			if(imp_pgiro.getElemento_voceContr()!=null ){
				if ( imp_pgiro.getElemento_voceContr().getEsercizio()!=null)
					accert_pgiro.setEsercizio( imp_pgiro.getElemento_voceContr().getEsercizio() );
				if ( imp_pgiro.getElemento_voceContr().getTi_appartenenza()!=null)
					accert_pgiro.setTi_appartenenza( imp_pgiro.getElemento_voceContr().getTi_appartenenza() );
				if ( imp_pgiro.getElemento_voceContr().getTi_gestione()!=null)
					accert_pgiro.setTi_gestione(  imp_pgiro.getElemento_voceContr().getTi_gestione() );
				if ( imp_pgiro.getElemento_voceContr().getCd_elemento_voce()!=null)
					accert_pgiro.setCd_voce(imp_pgiro.getElemento_voceContr().getCd_elemento_voce() );
			}
				else{
					Ass_partita_giroHome ass_pgiroHome = (Ass_partita_giroHome) getHome( uc, Ass_partita_giroBulk.class );
					ass_pgiro = ass_pgiroHome.getAssociazionePGiroFor(imp_pgiro);
					accert_pgiro.setTi_appartenenza( ass_pgiro.getTi_appartenenza() );
					accert_pgiro.setTi_gestione( ass_pgiro.getTi_gestione() );
					accert_pgiro.setCd_elemento_voce( ass_pgiro.getCd_voce() );
				}
		}else{
			Ass_partita_giroHome ass_pgiroHome = (Ass_partita_giroHome) getHome( uc, Ass_partita_giroBulk.class );
			ass_pgiro = ass_pgiroHome.getAssociazionePGiroFor(imp_pgiro);
			accert_pgiro.setTi_appartenenza( ass_pgiro.getTi_appartenenza() );
			accert_pgiro.setTi_gestione( ass_pgiro.getTi_gestione() );
			accert_pgiro.setCd_elemento_voce( ass_pgiro.getCd_voce() );
		}
		
		/*
		Ass_partita_giroHome ass_pgiroHome = (Ass_partita_giroHome) getHome( uc, Ass_partita_giroBulk.class );
		Ass_partita_giroBulk ass_pgiro = ass_pgiroHome.getAssociazionePGiroFor(imp_pgiro);
		
		accert_pgiro.setTi_appartenenza( ass_pgiro.getTi_appartenenza() );
		accert_pgiro.setTi_gestione( ass_pgiro.getTi_gestione() );
		accert_pgiro.setCd_elemento_voce( ass_pgiro.getCd_voce() );
		
        Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk)getHome(uc,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(imp_pgiro.getEsercizio()));
        */
        if (parametriCnr==null || !parametriCnr.getFl_nuovo_pdg()) {
        	Voce_fBulk voce_f = this.findVoce_f(uc, imp_pgiro, ass_pgiro);
        	accert_pgiro.setCd_voce( voce_f.getCd_voce() );
        } else {
        	if (ass_pgiro!=null){
        		accert_pgiro.setCd_voce( ass_pgiro.getCd_voce() );
        		accert_pgiro.getCapitolo().setCd_elemento_voce( ass_pgiro.getCd_voce() );
        	}
        	else{
        		accert_pgiro.setCd_voce( accert_pgiro.getCd_voce());
				accert_pgiro.setCd_elemento_voce(accert_pgiro.getCd_voce());

			}
        }
        
		accert_pgiro.setUser( uc.getUser() );	
		updateBulk( uc, accert_pgiro );
		accert_scadenzario.setUser( uc.getUser() );			
		updateBulk( uc, accert_scadenzario );
		accert_scad_voce.setUser( uc.getUser() );			
		updateBulk( uc, accert_scad_voce );
      if ( !uc.isTransactional() )
	   {
			//aggiorna i saldi 
			aggiornaCapitoloSaldoAccertamento( uc, accert_pgiro, MODIFICA);

			aggiornaStatoCOAN_COGEDocAmm( uc, accert_pgiro );
	   }
		
		return accert_pgiro;
	}
	catch ( Exception e )
	{
		throw handleException( e )	;

	}	
}
/** 
  *  modifica importo
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di un accertamento su partita di giro
  *    PostCondition:
  *      L'importo dell'accertamento viene modificato e, in cascata, vengono modificati gli importi relativi
  *      alla scadenza e al dettaglio scadenza. I saldi relativi ai documenti contabili vengono aggiornati.
  *      Alla component che gestisce l'obbligazione su partita di giro viene inoltrata la richiesta di modifica
  *      dell'obbligazione associata all'accertamento (metodo modificaObbligazione)
  *		 I saldi relativi ai documenti contabili vengono aggiornati (metodo aggiornaCapitoloSaldoAccertamento).
  *		 Vengono aggiornati gli stati COAN e COGE degli eventuali documenti amministrativi associati 
  *		 (metodo aggiornaStatoCOAN_COGEDocAmm)
  *  errore modifca importo - doc.amm.associati
  *    PreCondition:
  *      La richiesta di modifica dell'importo di un accertamento su partita di giro e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'accertamento
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la modifica
  *  modifica descrizione
  *    PreCondition:
  *      L'utente richiede la modifica della descrizione di un accertamento su partita di giro
  *    PostCondition:
  *      La descrizione dell'accertamento e della scadenza di accertamento vengono aggiornate
  *  modifica capitolo
  *    PreCondition:
  *      L'utente richiede la modifica del capitolo di un accertamento su partita di giro
  *    PostCondition:
  *      Il capitolo viene aggiornato e viene inoltrata la richiesta di modifica del capitolo
  *      dell'obbligazione associata all'accertamento (metodo modificaObbligazione)
  *  modifica data di scadenza
  *    PreCondition:
  *      L'utente richiede la modifica della data di scadenza di un accertamento su partita di giro
  *    PostCondition:
  *      La data della scadenza dell'accertamento su partita di giro viene aggiornata.
  *
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'accertamento su partita di giro da modificare
  *
  * @return accert_pgiro <code>OggettoBulk</code> l'accertamento su partita di giro modificato
 */

public OggettoBulk modificaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	try
	{
		AccertamentoPGiroBulk accert_pgiro = (AccertamentoPGiroBulk) bulk;
		
		verificaStatoEsercizio( aUC, accert_pgiro.getEsercizio(), accert_pgiro.getCd_cds());		

		//segnalo impossibilità di modificare importo se ci sono doc amministrativi associati
		if ( !accert_pgiro.isFromDocAmm() && 
			accert_pgiro.isAssociataADocAmm() && accert_pgiro.getIm_iniziale_accertamento() != null &&
			accert_pgiro.getIm_iniziale_accertamento().compareTo( accert_pgiro.getIm_accertamento()) != 0 )
			throw new ApplicationException( "Impossibile variare importo dell'Annotazione d'Entrata su Partita di Giro perche' e' associata a doc. amministrativi");

		//segnalo impossibilità di modificare importo se ci sono mandati associati
		if ( accert_pgiro.isFromDocAmm() && 
			accert_pgiro.isAssociataADocAmm() && accert_pgiro.getIm_iniziale_accertamento() != null &&
			accert_pgiro.getIm_iniziale_accertamento().compareTo( accert_pgiro.getIm_accertamento()) != 0 &&
			accert_pgiro.getPg_reversale() != null )
			throw new ApplicationException( "Impossibile variare importo dell'Annotazione d'Entrata su Partita di Giro perche' e' associata a reversale");

		//	segnalo impossibilità di modificare un residuo se l'esercizio precedente è ancora aperto
		if ( accert_pgiro.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_ACR_RES))
			verificaStatoEsercizioEsPrecedente( aUC, accert_pgiro.getEsercizio(), accert_pgiro.getCd_cds());

		//importo
		Accertamento_scadenzarioBulk accert_scadenzario = (Accertamento_scadenzarioBulk)accert_pgiro.getAccertamento_scadenzarioColl().get(0);
		accert_scadenzario.setIm_scadenza( accert_pgiro.getIm_accertamento() );
		accert_scadenzario.setDt_scadenza_incasso( accert_pgiro.getDt_scadenza());
		accert_scadenzario.setToBeUpdated();
		
		Accertamento_scad_voceBulk accert_scad_voce = (Accertamento_scad_voceBulk)accert_scadenzario.getAccertamento_scad_voceColl().get(0);
		accert_scad_voce.setIm_voce(accert_pgiro.getIm_accertamento() );
		accert_scad_voce.setToBeUpdated();

		// descrizione
		accert_scadenzario.setDs_scadenza( accert_pgiro.getDs_accertamento() );

		accert_pgiro.setCd_elemento_voce( accert_pgiro.getCapitolo().getCd_titolo_capitolo());

		// CHIAMARE IL METODO modificaObbligazione() su ObbligazionePGiroComponent
		if ( !accert_pgiro.isFl_isTronco() && !accert_pgiro.isResiduo())
			createObbligazionePGiroComponent().modificaObbligazione( aUC, accert_pgiro);


		accert_pgiro.setUser( aUC.getUser());
		updateBulk( aUC, accert_pgiro );
	    accert_scadenzario.setUser( aUC.getUser());		
		updateBulk( aUC, accert_scadenzario );
		accert_scad_voce.setUser( aUC.getUser());		
		updateBulk( aUC, accert_scad_voce );
	    if ( !aUC.isTransactional() )
	    {

			//aggiorna i saldi 
			aggiornaCapitoloSaldoAccertamento( aUC, accert_pgiro, MODIFICA);
			aggiornaStatoCOAN_COGEDocAmm( aUC, accert_pgiro );
	    }	
			
		// Verifico che l'esercizio del CDS sia stato aperto
		if (! ((AccertamentoHome)getHome(aUC, AccertamentoBulk.class)).verificaStatoEsercizio((AccertamentoBulk) bulk))
			throw handleException( new ApplicationException( "Non e' possibile salvare accertamenti: esercizio del Cds non ancora aperto!") );

	return accert_pgiro;
	
	} 
	
	catch ( Exception e )
	{
		throw handleException( bulk, e );
	}
}
/*
 * Modifica l'importo di una scadenza e della testata dell'accertamento
 *	
 * Pre-post-conditions:
 *
 * Nome: Scadenza successiva - Errore ultima scadenza
 * Pre:  E' stata generata la richiesta di modifica della scadenza successiva
 * Post: Viene generata un'ApplicationException in quanto gli accertamenti su partita di giro hanno un'unica scadenza
 *
 * Nome: Modifica scadenza
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza
 * Post: Vengono aggiornati l'importo in testata, in scadenza e in scad_voce e la controparte per l'impegno
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param scad (con importo originale)
 * @param nuovoImporto che deve assumere la scadenza
 * @param modificaScadenzaSuccessiva se true indica il fatto che la testata dell'accertamento non deve essere modificata
 *                                   e che la differenza fra l'importo attuale e il vecchio importo deve essere riportata sulla
 *									 scadenza successiva
 * @param modificaScadenzaSuccessiva se false indica il fatto che deve essere modificato l'importo della scadenza e della testata
 *                                   dell'accertamento
 * @return la scadenza 
 */

public IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico( UserContext userContext,	IScadenzaDocumentoContabileBulk scad,	BigDecimal nuovoImporto, boolean modificaScadenzaSuccessiva ) throws ComponentException 
{
	Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk)scad;
	if ( modificaScadenzaSuccessiva )
		throw new ApplicationException(" Non esiste scadenza successiva" );
	if ( scadenza.getIm_scadenza().compareTo( nuovoImporto ) == 0 )
		throw handleException( new ApplicationException( "Aggiornamento in automatico non necessario" ));
	if (  nuovoImporto.compareTo( new BigDecimal(0)) < 0  )
		throw handleException( new ApplicationException( "L'importo della scadenza deve essere maggiore di 0" ));
		
		
	//aggiorno importo testata
	AccertamentoPGiroBulk acc_pgiro = (AccertamentoPGiroBulk) scadenza.getAccertamento();
	if ( acc_pgiro.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_ACR_RES) )
		throw handleException( new ApplicationException( "Non è consentita la modifica dell'importo di testata di un'annotazione residua." ));					

	acc_pgiro.setIm_accertamento( nuovoImporto );
	acc_pgiro.setToBeUpdated();
	
	acc_pgiro.setFromDocAmm( true );
	modificaConBulk( userContext, acc_pgiro);
	return scadenza;

}
/*
 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite su Accertamenti su partite di giro
 *
 * Le partite di giro sono di 3 possibili tipi:
 * 1 - normali - hanno la controparte che ha sempre lo stesso stato e importo
 * 2 - tronche - hanno la controparte che ha sempre lo stesso annullato  e importo uguale a 0
 * 3 - residue (solo nel bilancio CNR) - non hanno alcuna controparte
 *	
 * Pre-post-conditions:
 *
 * Nome: Richiesta di ricerca di un'annotazione su pgiro tronca
 * Pre:  E' stata generata la richiesta di ricerca di un'annotazione su pgiro tronca
 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
 *       clausole che l'accertamento abbia esercizio di creazione uguale a quello di scrivania
 *       e UO di origine uguale a quella di scrivania e che la controparte dell'accertamento esista e sia annullata
 *
 * Nome: Richiesta di ricerca di un'annotazione su pgiro non tronca 
 * Pre:  E' stata generata la richiesta di ricerca di un'annotazione su pgiro non tronca 
 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
 *       clausole che l'accertamento abbia esercizio di creazione uguale a quello di scrivania
 *       e UO di origine uguale a quella di scrivania e che la controparte dell'accertamento esista e non sia annullata;
 *
 * Nome: Richiesta di ricerca di un'annotazione su pgiro residua
 * Pre:  E' stata generata la richiesta di ricerca di un'annotazione su pgiro residua del bilancio Ente
 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
 *       clausole che l'accertamento abbia esercizio di creazione uguale a quello di scrivania
 *       e UO di origine uguale a quella di scrivania e che il tipo di documento sia ACR_RES
 * 
 * @param userContext lo userContext che ha generato la richiesta
 * @param clauses clausole di ricerca gia' specificate dall'utente
 * @param bulk istanza di AccertamentoPGiroBulk che deve essere utilizzata per la ricerca
 * @return sql Query con le clausole aggiuntive 
 */

protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	SQLBuilder sql = (SQLBuilder) super.select( userContext, clauses, bulk );
//	sql.addClause( "AND", "cd_uo_origine", sql.EQUALS, ((CNRUserContext)userContext).getCd_unita_organizzativa()); 
	sql.addClause( "AND", "esercizio", sql.EQUALS, ((CNRUserContext)userContext).getEsercizio());

	AccertamentoPGiroBulk acr = (AccertamentoPGiroBulk) bulk;
	if ( acr.isResiduo() )
	{	//accertamenti residui: non hanno controparte
		sql.addSQLClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_ACR_RES);
		return sql;
	}	
	else if ( !acr.isFl_isTronco() )
	{	//accertamenti con controparte
		sql.addTableToHeader( "OBBLIGAZIONE");
		sql.addTableToHeader( "ASS_OBB_ACR_PGIRO");		
		sql.addSQLJoin( "ASS_OBB_ACR_PGIRO.CD_CDS", "ACCERTAMENTO.CD_CDS");
		sql.addSQLJoin( "ASS_OBB_ACR_PGIRO.ESERCIZIO", "ACCERTAMENTO.ESERCIZIO");
		sql.addSQLJoin( "ASS_OBB_ACR_PGIRO.ESERCIZIO_ORI_ACCERTAMENTO", "ACCERTAMENTO.ESERCIZIO_ORIGINALE");
		sql.addSQLJoin( "ASS_OBB_ACR_PGIRO.PG_ACCERTAMENTO", "ACCERTAMENTO.PG_ACCERTAMENTO");
		sql.addSQLJoin( "ASS_OBB_ACR_PGIRO.CD_CDS", "OBBLIGAZIONE.CD_CDS");
		sql.addSQLJoin( "ASS_OBB_ACR_PGIRO.ESERCIZIO", "OBBLIGAZIONE.ESERCIZIO");
		sql.addSQLJoin( "ASS_OBB_ACR_PGIRO.ESERCIZIO_ORI_OBBLIGAZIONE", "OBBLIGAZIONE.ESERCIZIO_ORIGINALE");
		sql.addSQLJoin( "ASS_OBB_ACR_PGIRO.PG_OBBLIGAZIONE", "OBBLIGAZIONE.PG_OBBLIGAZIONE");
		sql.openParenthesis( "AND");
		sql.openParenthesis( "AND");//accertamenti non tronchi
		sql.addSQLClause( "AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", sql.ISNULL, null );
		sql.addSQLClause( "AND", "ACCERTAMENTO.DT_CANCELLAZIONE", sql.ISNULL, null );		
		sql.closeParenthesis();
		sql.openParenthesis( "OR");//tutti accertamenti cancellati (tronchi e non)
		sql.addSQLClause( "AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", sql.ISNOTNULL, null );
		sql.addSQLClause( "AND", "ACCERTAMENTO.DT_CANCELLAZIONE", sql.ISNOTNULL, null );		
		sql.closeParenthesis();
		sql.closeParenthesis();
		return sql;	
	}
	else	//accertamenti tronchi
	{
		sql.setDistinctClause( true );
		sql.addTableToHeader( "OBBLIGAZIONE");
		sql.addTableToHeader( "ASS_OBB_ACR_PGIRO");		
		sql.addSQLJoin( "ASS_OBB_ACR_PGIRO.CD_CDS", "ACCERTAMENTO.CD_CDS");
		sql.addSQLJoin( "ASS_OBB_ACR_PGIRO.ESERCIZIO", "ACCERTAMENTO.ESERCIZIO");
		sql.addSQLJoin( "ASS_OBB_ACR_PGIRO.ESERCIZIO_ORI_ACCERTAMENTO", "ACCERTAMENTO.ESERCIZIO_ORIGINALE");
		sql.addSQLJoin( "ASS_OBB_ACR_PGIRO.PG_ACCERTAMENTO", "ACCERTAMENTO.PG_ACCERTAMENTO");
		sql.addSQLJoin( "ASS_OBB_ACR_PGIRO.CD_CDS", "OBBLIGAZIONE.CD_CDS");
		sql.addSQLJoin( "ASS_OBB_ACR_PGIRO.ESERCIZIO", "OBBLIGAZIONE.ESERCIZIO");
		sql.addSQLJoin( "ASS_OBB_ACR_PGIRO.ESERCIZIO_ORI_OBBLIGAZIONE", "OBBLIGAZIONE.ESERCIZIO_ORIGINALE");
		sql.addSQLJoin( "ASS_OBB_ACR_PGIRO.PG_OBBLIGAZIONE", "OBBLIGAZIONE.PG_OBBLIGAZIONE");
		sql.openParenthesis( "AND");
		sql.openParenthesis( "AND"); //tutti accertamenti cancellati (tronchi e non)
		sql.addSQLClause( "AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", sql.ISNOTNULL, null );
		sql.addSQLClause( "AND", "ACCERTAMENTO.DT_CANCELLAZIONE", sql.ISNOTNULL, null );		
		sql.closeParenthesis();
		sql.openParenthesis( "OR"); //accertamenti tronchi 
		sql.addSQLClause( "AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", sql.ISNULL, null );
		sql.addSQLClause( "AND", "ACCERTAMENTO.DT_CANCELLAZIONE", sql.ISNOTNULL, null );		
		sql.closeParenthesis();
		sql.openParenthesis( "OR"); //accertamenti legati a obbligazione tronche
		sql.addSQLClause( "AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", sql.ISNOTNULL, null );
		sql.addSQLClause( "AND", "ACCERTAMENTO.DT_CANCELLAZIONE", sql.ISNULL, null );		
		sql.closeParenthesis();
		sql.closeParenthesis();
		return sql;		
		}
}
/** 
  *  Tutti controlli superati
  *    PreCondition:
  *      La data di registrazione dell'accertamento su partita di giro è corretta.
  *    PostCondition:
  *      L'accertamento su partita di giro è valido. E' consentito eseguire l'attività di salvataggio.
  *  La data di registrazione dell'accertamento su partita di giro non è corretta.
  *    PreCondition:
  *     E' stata inserita dall'utente una data di registrazione antecedente a quella dell'ultimo accertamento pgiro
  *		salvato sul database
  *    PostCondition:
  *      L'utente viene avvisato tramite un messaggio di errore che non è possibile inserire un accertamento su partita 
  *		 di giro con data anteriore a quella dell'ultimo accertamento salvato su database. L'attività non è consentita.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param acc <code>AccertamentoPGiroBulk</code> l'accertamento su partita di giro da validare
  *
 */
protected void verificaAccertamento(UserContext userContext, AccertamentoPGiroBulk acc ) throws it.cnr.jada.persistency.PersistencyException, ComponentException, ApplicationException, javax.ejb.EJBException
{
	if ( acc.isToBeCreated() )
	{
		Timestamp lastDayOfTheYear = DateServices.getLastDayOfYear( acc.getEsercizio().intValue());

		if ( acc.getDt_registrazione().before(DateServices.getFirstDayOfYear( acc.getEsercizio().intValue())) ||
			  acc.getDt_registrazione().after(lastDayOfTheYear))
			throw  new ApplicationException( "La data di registrazione deve appartenere all'esercizio di scrivania" );
		Timestamp today = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
		if ( today.after(lastDayOfTheYear ) &&
			  acc.getDt_registrazione().compareTo( lastDayOfTheYear) != 0 )
			throw  new ApplicationException( "La data di registrazione deve essere " +
		   									java.text.DateFormat.getDateInstance().format( lastDayOfTheYear ));					

		Timestamp dataUltAcc = ((AccertamentoPGiroHome) getHome( userContext, AccertamentoPGiroBulk.class )).findDataUltimoAccertamentoPerCds( acc );
		if ( dataUltAcc != null && dataUltAcc.after( acc.getDt_registrazione() ) )
			throw  new ApplicationException( "Non è possibile inserire un'Annotazione d'Entrata su Partita di Giro con data anteriore a " +  
   									java.text.DateFormat.getDateTimeInstance().format( dataUltAcc ));
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
			throw handleException( new ApplicationException( "Inserimento impossibile: esercizio inesistente!") );
	if ( !esercizio.STATO_APERTO.equals(esercizio.getSt_apertura_chiusura()))
			throw handleException( new ApplicationException( "Inserimento impossibile: esercizio non aperto!") );
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
			
}
