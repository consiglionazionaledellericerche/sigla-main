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

import it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fHome;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteHome;
import it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroHome;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scad_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scad_voceHome;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioHome;
import it.cnr.contab.doccont00.core.bulk.Ass_obb_acr_pgiroBulk;
import it.cnr.contab.doccont00.core.bulk.Ass_obb_acr_pgiroHome;
import it.cnr.contab.doccont00.core.bulk.Ass_partita_giroBulk;
import it.cnr.contab.doccont00.core.bulk.Ass_partita_giroHome;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.ImpegnoPGiroBulk;
import it.cnr.contab.doccont00.core.bulk.ImpegnoPGiroHome;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voceHome;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioHome;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.doccont00.core.bulk.V_doc_attivo_accertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.V_doc_passivo_obbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.V_mod_saldi_obbligBulk;
import it.cnr.contab.doccont00.core.bulk.V_mod_saldi_obbligHome;
import it.cnr.contab.doccont00.core.bulk.V_mod_saldi_obblig_scad_voceBulk;
import it.cnr.contab.doccont00.core.bulk.V_mod_saldi_obblig_scad_voceHome;
import it.cnr.contab.doccont00.core.bulk.V_obbligazione_im_mandatoBulk;
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
 * Classe che ridefinisce alcune operazioni di CRUD su ImpegnoPGiroBulk
 */

/* Gestisce documenti di tipo
	IMP con fl_pgiro = 'Y' - bilancio Ente
	IMP_RES con fl_pgiro = 'Y'- bilancio Ente
	OBB_PGIRO - bilancio Cds
*/	

public class ObbligazionePGiroComponent extends it.cnr.jada.comp.CRUDComponent implements IDocumentoContabileMgr,ICRUDMgr,Cloneable,Serializable,IObbligazionePGiroMgr
{
	private final static int INSERIMENTO 	= 1;
	private final static int MODIFICA    	= 2;
	private final static int CANCELLAZIONE = 3;		

	
//@@<< CONSTRUCTORCST
    public  ObbligazionePGiroComponent()
    {
//>>

//<< CONSTRUCTORCSTL
        /*Default constructor*/
//>>

//<< CONSTRUCTORCSTT

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

private void aggiornaCapitoloSaldoObbligazione (UserContext aUC,ImpegnoPGiroBulk obbligazione, int azione) throws ComponentException
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
		if ( docContabile instanceof ImpegnoPGiroBulk )
		{
			ImpegnoPGiroBulk impegno = (ImpegnoPGiroBulk) docContabile;
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
 * Aggiornamento in differita dei saldi dell'obbligazione su partita di giro.
 * Un documento amministrativo di spesa che agisce in modalità transazionale ha creato/modificato gli importi 
 * relativi ad un impegno pgiro; i saldi di tale impegno non possono essere aggiornati subito in quanto
 * tale operazione genererebbe dei lock sulle voci del piano che non ne consentirebbere l'utilizzo ad altri utenti;
 * pertanto l'aggiornamento dei saldi dell'obbligazione pgiro viene differito al momento del salvataggio
 * del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi per obbligazione su partita di giro creata 
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un'obbligazione su capitoli di
 *       partita di giro che e' stata creata nel contesto transazionale del documento amministrativo ( progressivo
 *       impegno pgiro < 0)
 * Post: I saldi dell'obbligazione pgiro sono stati aggiornati nel metodo 'aggiornaSaldiInInserimento'
 *
 * Nome: Aggiorna saldi per obbligazione su partita di giro esistente
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un'obbligazione su capitoli di
 *       partita di giro che non e' stata creata nel contesto transazionale del documento amministrativo ( progressivo
 *       impegno pgiro > 0)
 * Post: I saldi dell'obbligazione pgiro sono stati aggiornati nel metodo 'aggiornaSaldiInModifica'
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	docContabile	l'ImpegnoPGiroBulk per cui aggiornare i saldi
 * @param	values	la Map che contiene il "pg_ver_rec" iniziale dell'impegno 
 * @param	param parametro non utilizzato per le partite di giro
 * 
*/
public void aggiornaSaldiInDifferita( UserContext userContext, IDocumentoContabileBulk docContabile, Map values, OptionRequestParameter param ) throws ComponentException
{
	try
	{
		if ( docContabile instanceof ImpegnoPGiroBulk )
		{
			ImpegnoPGiroBulk imp_pgiro = (ImpegnoPGiroBulk) docContabile;
			Long pg_ver_rec = (Long) values.get("pg_ver_rec");
			if ( pg_ver_rec == null )
				throw new ApplicationException( "Aggiornamento in differita dei saldi dei documenti contabili impossibile (pg_ver_rec nullo)");
			if ( imp_pgiro.getPg_obbligazione().longValue() < 0 ) //obbligazione appena inserita
				aggiornaSaldiInInserimento( userContext, imp_pgiro, true );
			else
				aggiornaSaldiInModifica( userContext, imp_pgiro, pg_ver_rec, true );			
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
 * @param	imp_pgiro	l'ImpegnoPGiroBulk per cui aggiornare i saldi
 * 
 */
private void aggiornaSaldiInInserimento(
	UserContext userContext, 
	ImpegnoPGiroBulk imp_pgiro,
	boolean aggiornaControparte)
	throws ComponentException, it.cnr.jada.persistency.PersistencyException, java.rmi.RemoteException
{
	SaldoComponentSession session = createSaldoComponentSession();
    Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk)getHome(userContext,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)));
	PrimaryKeyHashMap saldiDaAggiornare;
	try {
		saldiDaAggiornare = imp_pgiro.getVociMap(((Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class)).isNuovoPdg(userContext));
	} catch (PersistencyException e) {
		throw handleException(e);
	}
	/*
	 * Aggiorna VOCE_F_SALDI_CMP
	 */
	for ( Iterator i = saldiDaAggiornare.keySet().iterator(); i.hasNext(); )
	{
		IVoceBilancioBulk voce = (IVoceBilancioBulk) i.next();
		if (voce instanceof Voce_fBulk) {
			BigDecimal im_voce = (BigDecimal) saldiDaAggiornare.get(voce);
			session.aggiornaObbligazioniAccertamenti( userContext, (Voce_fBulk)voce, imp_pgiro.getCd_cds(), im_voce, it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk.TIPO_COMPETENZA);
			if (aggiornaControparte && !imp_pgiro.isFl_isTronco() && !imp_pgiro.isResiduo()) {
				Ass_partita_giroBulk ass_pgiro = ((Ass_partita_giroHome)getHome(userContext, Ass_partita_giroBulk.class)).getAssociazionePGiroFor(imp_pgiro);
				Voce_fHome voce_fHome = (Voce_fHome) getHome( userContext, Voce_fBulk.class );
				String cd_uo = null;
				if ( imp_pgiro.getCd_unita_organizzativa().equals( imp_pgiro.getCd_uo_ente()) ) //CNR
					cd_uo = ((CNRUserContext)userContext).getCd_unita_organizzativa();
				Voce_fBulk voce_f = voce_fHome.findVoce_fFor(ass_pgiro, cd_uo);
	
				session.aggiornaObbligazioniAccertamenti(userContext, voce_f, imp_pgiro.getAssociazione().getAccertamento().getCd_cds(), im_voce, ReversaleBulk.TIPO_COMPETENZA);
			}
		}
	}
	/*
	* Aggiorno i Saldi per CDR/Linea
	*/	
	PrimaryKeyHashMap saldiDaAggiornareCdrLinea = imp_pgiro.getObbligazione_scad_voceMap();
	for ( Iterator i = saldiDaAggiornareCdrLinea.keySet().iterator(); i.hasNext(); )
	{
		Obbligazione_scad_voceBulk osv = (Obbligazione_scad_voceBulk) i.next();
		BigDecimal im_voce = (BigDecimal) saldiDaAggiornareCdrLinea.get(osv);

		boolean isNuovoPdg = ((Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class)).isNuovoPdg(userContext);

		if (!isNuovoPdg) {
			Voce_fBulk voce = new Voce_fBulk( osv.getCd_voce(), osv.getEsercizio(), osv.getTi_appartenenza(), osv.getTi_gestione());
			session.aggiornaObbligazioniAccertamenti( userContext, osv.getCd_centro_responsabilita(), osv.getCd_linea_attivita(), voce, imp_pgiro.getEsercizio_originale(),imp_pgiro.isObbligazioneResiduoImproprio()?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO,im_voce,imp_pgiro.getCd_tipo_documento_cont());
		} else {
			Elemento_voceBulk voce = new Elemento_voceBulk( osv.getCd_voce(), osv.getEsercizio(), osv.getTi_appartenenza(), osv.getTi_gestione());
			session.aggiornaObbligazioniAccertamenti( userContext, osv.getCd_centro_responsabilita(), osv.getCd_linea_attivita(), voce, imp_pgiro.getEsercizio_originale(),imp_pgiro.isObbligazioneResiduoImproprio()?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO,im_voce,imp_pgiro.getCd_tipo_documento_cont());
		}
		
		if (aggiornaControparte && !imp_pgiro.isFl_isTronco() && !imp_pgiro.isResiduo()) {
			Ass_partita_giroBulk ass_pgiro = ((Ass_partita_giroHome)getHome(userContext, Ass_partita_giroBulk.class)).getAssociazionePGiroFor(imp_pgiro);

			IVoceBilancioBulk voce = null;
			if (!isNuovoPdg) {
				Voce_fHome voce_fHome = (Voce_fHome) getHome( userContext, Voce_fBulk.class );
				String cd_uo = null;
				if ( imp_pgiro.getCd_unita_organizzativa().equals( imp_pgiro.getCd_uo_ente()) ) //CNR
					cd_uo = ((CNRUserContext)userContext).getCd_unita_organizzativa();
				voce = voce_fHome.findVoce_fFor(ass_pgiro, cd_uo);
			} else
				if(ass_pgiro!=null)
					voce = new Elemento_voceBulk( ass_pgiro.getCd_voce(), ass_pgiro.getEsercizio(), ass_pgiro.getTi_appartenenza(), ass_pgiro.getTi_gestione());
				else
					if(imp_pgiro.getElemento_voceContr()!=null)
						voce = new Elemento_voceBulk( imp_pgiro.getElemento_voceContr().getCd_elemento_voce(), imp_pgiro.getElemento_voceContr().getEsercizio(), imp_pgiro.getElemento_voceContr().getTi_appartenenza(), imp_pgiro.getElemento_voceContr().getTi_gestione());
					else
						throw new ApplicationException( "Attenzione! I saldi relativi all'accertamento corrispondente non possono essere aggiornati, voce non recuperta.");

			/*
			 * Aggiorno i Saldi per CDR/Linea
			 */
			Accertamento_scad_voceBulk asv;
			Accertamento_scadenzarioBulk as;		 
			for ( Iterator j = imp_pgiro.getAssociazione().getAccertamento().getAccertamento_scadenzarioColl().iterator(); j.hasNext(); )
			{
			  as = (Accertamento_scadenzarioBulk) j.next();
			  for ( int index = as.getAccertamento_scad_voceColl().size() - 1; index >= 0 ; index--)
			  {
				 asv = (Accertamento_scad_voceBulk) as.getAccertamento_scad_voceColl().get( index );
				 session.aggiornaObbligazioniAccertamenti( userContext, asv.getCd_centro_responsabilita(), asv.getCd_linea_attivita(), voce, imp_pgiro.getAssociazione().getAccertamento().getEsercizio_originale(),imp_pgiro.getAssociazione().getAccertamento().isAccertamentoResiduo()?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_COMPETENZA,asv.getIm_voce(),imp_pgiro.getCd_tipo_documento_cont());
			  }
			}
		}
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
 * @param	imp_pgiro	l'ImpegnoPGiroBulk per cui aggiornare i saldi
 * @param	pg_ver_rec	il "pg_ver_rec" iniziale dell'impegno 
 */
private void aggiornaSaldiInModifica(
	UserContext userContext, 
	ImpegnoPGiroBulk imp_pgiro, 
	Long pg_ver_rec,
	boolean aggiornaControparte) throws it.cnr.jada.persistency.PersistencyException, ComponentException, java.rmi.RemoteException
{
	SaldoComponentSession session = createSaldoComponentSession();			
	List saldiDaAggiornare = ((V_mod_saldi_obbligHome)getHome( userContext, V_mod_saldi_obbligBulk.class )).findModificheSaldiFor( imp_pgiro, pg_ver_rec );
	if ( userContext.isTransactional() && saldiDaAggiornare.size() == 0 )
		throw new ApplicationException( "Attenzione! I saldi relativi all'impegno " + imp_pgiro.getEsercizio_originale() + "/" + imp_pgiro.getPg_obbligazione() + " non possono essere aggiornati perchè l'impegno non e' presente nello storico.");

	String ti_competenza_residuo;
	if ( imp_pgiro.isResiduo() )
		ti_competenza_residuo = ReversaleBulk.TIPO_RESIDUO;
	else
		ti_competenza_residuo = ReversaleBulk.TIPO_COMPETENZA;
		
	boolean isNuovoPdg = ((Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class)).isNuovoPdg(userContext);
	
	if (!isNuovoPdg) {
		/*
		* Aggiorno i Saldi VOCE_F_SALDI_CMP
		*/	
		for ( Iterator i = saldiDaAggiornare.iterator(); i.hasNext(); )
		{
			V_mod_saldi_obbligBulk modSaldo = (V_mod_saldi_obbligBulk) i.next();
			Voce_fBulk voce = new Voce_fBulk( modSaldo.getCd_voce(), imp_pgiro.getEsercizio(), modSaldo.getTi_appartenenza(), modSaldo.getTi_gestione() );
			if ( modSaldo.getIm_delta_voce().compareTo( new BigDecimal(0)) != 0 )
			{
	
				/* il check della disponabilità di cassa deve essere eseguito solo se 
				    l'importo delta del saldo e' positivo e
			 	   l'utente non ha ancora avuto il warning sulla disp.cassa oppure
				    l'utente ha avuto il warning sulla disp.cassa e ha risposto no */
				session.aggiornaObbligazioniAccertamenti( userContext, voce, imp_pgiro.getCd_cds(), modSaldo.getIm_delta_voce(), ti_competenza_residuo);
				if (aggiornaControparte && !imp_pgiro.isFl_isTronco() && !imp_pgiro.isResiduo()) {
					Ass_partita_giroBulk ass_pgiro = ((Ass_partita_giroHome)getHome(userContext, Ass_partita_giroBulk.class)).getAssociazionePGiroFor(imp_pgiro);
					Voce_fHome voce_fHome = (Voce_fHome) getHome( userContext, Voce_fBulk.class );
					String cd_uo = null;
					if ( imp_pgiro.getCd_unita_organizzativa().equals( imp_pgiro.getCd_uo_ente()) ) //CNR
						cd_uo = ((CNRUserContext)userContext).getCd_unita_organizzativa();
					Voce_fBulk voce_f = voce_fHome.findVoce_fFor(ass_pgiro, cd_uo);
	
					session.aggiornaObbligazioniAccertamenti(userContext, voce_f, imp_pgiro.getAssociazione().getAccertamento().getCd_cds(), modSaldo.getIm_delta_voce(), ReversaleBulk.TIPO_COMPETENZA);
				}
	
				if ( modSaldo.getIm_delta_man_voce().compareTo( new BigDecimal(0) ) != 0 )
					session.aggiornaMandatiReversali( userContext, voce, imp_pgiro.getCd_cds(), modSaldo.getIm_delta_man_voce(), ti_competenza_residuo);
	
				if ( modSaldo.getIm_delta_pag_voce().compareTo( new BigDecimal(0) ) != 0 )
					session.aggiornaPagamentiIncassi( userContext, voce, imp_pgiro.getCd_cds(), modSaldo.getIm_delta_pag_voce(), ti_competenza_residuo);
			}		
		}
	}
	/*
	* Aggiorno i Saldi per CDR/Linea
	*/	
	List saldiDaAggiornareCdrLinea = ((V_mod_saldi_obblig_scad_voceHome)getHome( userContext, V_mod_saldi_obblig_scad_voceBulk.class )).findModificheSaldiFor( imp_pgiro, pg_ver_rec );
	if ( userContext.isTransactional() && saldiDaAggiornareCdrLinea.size() == 0 )
		throw new ApplicationException( "Attenzione! I saldi relativi all'impegno " + imp_pgiro.getEsercizio_originale() + "/" + imp_pgiro.getPg_obbligazione() + " non possono essere aggiornati perchè l'impegno non e' presente nello storico.");
	for ( Iterator i = saldiDaAggiornareCdrLinea.iterator(); i.hasNext(); )
	{
		V_mod_saldi_obblig_scad_voceBulk modSaldo = (V_mod_saldi_obblig_scad_voceBulk) i.next();
		IVoceBilancioBulk voce = null;
		if (!isNuovoPdg)
			voce = new Voce_fBulk( modSaldo.getCd_voce(), imp_pgiro.getEsercizio(), modSaldo.getTi_appartenenza(), modSaldo.getTi_gestione() );
		else
			voce = new Elemento_voceBulk( modSaldo.getCd_voce(), imp_pgiro.getEsercizio(), modSaldo.getTi_appartenenza(), modSaldo.getTi_gestione() );

		if ( modSaldo.getIm_delta_voce().compareTo( new BigDecimal(0)) != 0 )
		{
			session.aggiornaObbligazioniAccertamenti( userContext, modSaldo.getCd_centro_responsabilita(), modSaldo.getCd_linea_attivita(), voce, modSaldo.getEsercizio_originale(),modSaldo.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA)?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO,modSaldo.getIm_delta_voce(),modSaldo.getCd_tipo_documento_cont());

			if (aggiornaControparte && !imp_pgiro.isFl_isTronco() && !imp_pgiro.isResiduo()) {
				Ass_partita_giroBulk ass_pgiro = ((Ass_partita_giroHome)getHome(userContext, Ass_partita_giroBulk.class)).getAssociazionePGiroFor(imp_pgiro);

				IVoceBilancioBulk voceEntrata = null;
				if (!isNuovoPdg) {
					Voce_fHome voce_fHome = (Voce_fHome) getHome( userContext, Voce_fBulk.class );
					String cd_uo = null;
					if ( imp_pgiro.getCd_unita_organizzativa().equals( imp_pgiro.getCd_uo_ente()) ) //CNR
						cd_uo = ((CNRUserContext)userContext).getCd_unita_organizzativa();
					voceEntrata = voce_fHome.findVoce_fFor(ass_pgiro, cd_uo);
				} else
					if(ass_pgiro!=null)
						voceEntrata = new Elemento_voceBulk( ass_pgiro.getCd_voce(), ass_pgiro.getEsercizio(), ass_pgiro.getTi_appartenenza(), ass_pgiro.getTi_gestione());
					else
						if(imp_pgiro.getElemento_voceContr()!=null)
							voceEntrata = new Elemento_voceBulk( imp_pgiro.getElemento_voceContr().getCd_elemento_voce(), imp_pgiro.getElemento_voceContr().getEsercizio(), imp_pgiro.getElemento_voceContr().getTi_appartenenza(), imp_pgiro.getElemento_voceContr().getTi_gestione());
						else
							throw new ApplicationException( "Attenzione! I saldi relativi all'accertamento corrispondente non possono essere aggiornati, voce non recuperta.");					
				Accertamento_scad_voceBulk asv;
				Accertamento_scadenzarioBulk as;		 
				for ( Iterator j = imp_pgiro.getAssociazione().getAccertamento().getAccertamento_scadenzarioColl().iterator(); j.hasNext(); )
				{
				  as = (Accertamento_scadenzarioBulk) j.next();
				  for ( int index = as.getAccertamento_scad_voceColl().size() - 1; index >= 0 ; index--)
				  {
					 asv = (Accertamento_scad_voceBulk) as.getAccertamento_scad_voceColl().get( index );
					 session.aggiornaObbligazioniAccertamenti( userContext, asv.getCd_centro_responsabilita(), asv.getCd_linea_attivita(), voceEntrata, imp_pgiro.getAssociazione().getAccertamento().getEsercizio_originale(),imp_pgiro.getAssociazione().getAccertamento().isAccertamentoResiduo()?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_COMPETENZA,asv.getIm_voce(),imp_pgiro.getCd_tipo_documento_cont());
				  }
				}
			}
		}		
		if ( modSaldo.getIm_delta_man_voce().compareTo( new BigDecimal(0) ) != 0 )
			session.aggiornaMandatiReversali( userContext,modSaldo.getCd_centro_responsabilita(),modSaldo.getCd_linea_attivita(),voce,modSaldo.getEsercizio_originale(),modSaldo.getIm_delta_man_voce(),modSaldo.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA)?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO);

		if ( modSaldo.getIm_delta_pag_voce().compareTo( new BigDecimal(0) ) != 0 )
			session.aggiornaPagamentiIncassi( userContext,modSaldo.getCd_centro_responsabilita(),modSaldo.getCd_linea_attivita(),voce,modSaldo.getEsercizio_originale(),modSaldo.getIm_delta_pag_voce());
	}
}
/** 
  *  aggiornamento Stato COAN o COGE dei Documenti Amministrativi
  *    PreCondition:
  *      Nel caso un'obbligazione pgiro sia stata eliminata o modificata, occorre
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
  *      L'utente richiede la cancellazione di un impegno su partita di giro
  *    PostCondition:
  *     Alla component che gestisce l'accertamento su pgiro viene inoltrata la richiesta di cancellazione (logica) 
  *		dell'accertamento associato all'accertamento pgiro (metodo eliminaAccertamento), l'obbligazione (con la sua 
  *		scadenza e il suo dettaglio scadenza) viene cancellata (metodo eliminaObbligazione)
  *  errore - doc.amm.associati
  *    PreCondition:
  *      La richiesta di cancellazione di un'obbligazione su partita di giro e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'obbligazione
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la cancellazione
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param imp_pgiro <code>ImpegnoPGiroBulk</code> l'impegno su partita di giro da cancellare (logicamente)
  * @return imp_pgiro <code>ImpegnoPGiroBulk</code> l'impegno su partita di giro annullato
  *
 */
public ImpegnoPGiroBulk annullaObbligazione(UserContext userContext, ImpegnoPGiroBulk imp_pgiro ) throws ComponentException
{
	try
	{
		verificaStatoEsercizio( userContext, imp_pgiro.getEsercizio(), imp_pgiro.getCd_cds());
		//	segnalo impossibilità di annullare un residuo se l'esercizio precedente è ancora aperto
		if ( imp_pgiro.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_IMP_RES))
			verificaStatoEsercizioEsPrecedente( userContext, imp_pgiro.getEsercizio(), imp_pgiro.getCd_cds());
		
			
		if ( imp_pgiro.isAssociataADocAmm() )
			throw new ApplicationException( "Impossibile cancellare l'Annotazione di Spesa su Partita di Giro perche' ha documenti amministrativi associati");

		// annullo il record relativo all'accertamento partita di giro
		if ( !imp_pgiro.isFl_isTronco() )
			createAccertamentoPGiroComponent().eliminaAccertamento( userContext, imp_pgiro.getAssociazione().getAccertamento());

		// annullo il record relativo all'obbligazione partita di giro
		eliminaObbligazione( userContext, imp_pgiro);
		
		return imp_pgiro;

	}
	catch ( Exception e )
	{
		throw handleException(imp_pgiro, e)	;
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
/*		if ( doc instanceof ImpegnoPGiroBulk )
		{
			//per le partite di giro bisogna passare alla procedura PL/SQL sempre
			//la parte che ha aperto la partita di giro
			ImpegnoPGiroBulk imp = (ImpegnoPGiroBulk) doc;
			if ( imp.getAssociazione() != null && imp.getAssociazione().getTi_origine().equals( doc.TI_ENTRATA ) &&
				  imp.getAssociazione().getAccertamento() != null )
				doc = imp.getAssociazione().getAccertamento();
		}
*/
		doc = findPGiroDaRiportareAvanti( userContext, doc );
		if ( doc == null )
			throw new ApplicationException( "Documento non riportabile");	
		
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
		/*
		if ( doc instanceof ImpegnoPGiroBulk )
		{
			//per le partite di giro bisogna passare alla procedura PL/SQL sempre
			//la parte che ha aperto la partita di giro
			ImpegnoPGiroBulk imp = (ImpegnoPGiroBulk) doc;
			if ( imp.getAssociazione() != null && imp.getAssociazione().getTi_origine().equals( doc.TI_ENTRATA ) &&
				  imp.getAssociazione().getAccertamento() != null )
				doc = imp.getAssociazione().getAccertamento();
		}
		*/
		doc = findPGiroDaRiportareIndietro( userContext, doc );
		if ( doc == null )
			throw new ApplicationException( "Documento non riportabile indietro");	
		
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
  *      un impegno pgiro ed un accertamento pgiro sono stati creati ed e' necessario creare 
  *      l'associazione (Ass_obb_acr_pgiroBulk) fra i due
  *    PostCondition:
  *      L'associazione fra accertamento e impegno su partita di giro e' stata creata
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param imp_pgiro <code>ImpegnoPGiroBulk</code> l'impegno da associare ad un accertamento su partita di giro
  * @param accert_pgiro <code>AccertamentoPGiroBulk</code> l'accertamento su partita di giro da associare ad un impegno
  *
  * @return <code>Ass_obb_acr_pgiroBulk</code> L'associazione obbligazione-accertamento su partita di giro da creare
 */
public Ass_obb_acr_pgiroBulk creaAss_obb_acr_pgiro(UserContext uc,ImpegnoPGiroBulk imp_pgiro, AccertamentoPGiroBulk accert_pgiro ) throws ComponentException
{
		Ass_obb_acr_pgiroBulk ass_oa_pgiro = new Ass_obb_acr_pgiroBulk();
		ass_oa_pgiro.setUser( imp_pgiro.getUser() );
		ass_oa_pgiro.setToBeCreated();
		
		ass_oa_pgiro.setEsercizio_ori_accertamento( accert_pgiro.getEsercizio_originale());
		ass_oa_pgiro.setPg_accertamento( accert_pgiro.getPg_accertamento());
		
		ass_oa_pgiro.setEsercizio_ori_obbligazione( imp_pgiro.getEsercizio_originale());
		ass_oa_pgiro.setPg_obbligazione( imp_pgiro.getPg_obbligazione());

		// campi chiave
		ass_oa_pgiro.setEsercizio( imp_pgiro.getEsercizio() );
		ass_oa_pgiro.setCd_cds( imp_pgiro.getCd_cds() );
		ass_oa_pgiro.setTi_origine( ass_oa_pgiro.TIPO_SPESA );

		return (Ass_obb_acr_pgiroBulk) super.creaConBulk( uc, ass_oa_pgiro );
		
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
  * @return imp_pgiro L'impegno su partita di giro creato
 */
public OggettoBulk creaConBulk (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	try
	{
		ImpegnoPGiroBulk imp_pgiro = ( ImpegnoPGiroBulk ) bulk;
		
		Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk)getHome(uc,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(imp_pgiro.getEsercizio()));
		
		if (parametriCnr.getFl_nuova_gestione_pg().booleanValue() ) {
			if(imp_pgiro.getElemento_voceContr()!=null && imp_pgiro.getElemento_voceContr().getCd_elemento_voce()!=null)
				imp_pgiro.setElemento_voceContr((Elemento_voceBulk)getHome(uc,Elemento_voceBulk.class).findByPrimaryKey(new Elemento_voceBulk(imp_pgiro.getElemento_voceContr().getCd_elemento_voce(),imp_pgiro.getEsercizio(),Elemento_voceHome.APPARTENENZA_CNR, Elemento_voceHome.GESTIONE_ENTRATE)));
		}
		
		imp_pgiro.setCd_cds( imp_pgiro.getUnita_organizzativa().getCd_unita_padre() );
		
		if ( imp_pgiro.getCd_unita_organizzativa().equals( imp_pgiro.getCd_uo_ente()) )
			imp_pgiro.setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_IMP);
		else
			imp_pgiro.setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_OBB_PGIRO);
		
		verificaObbligazione( uc, imp_pgiro );
		Obbligazione_scadenzarioBulk obblig_scad = creaObbligazione_scadenzario( uc, imp_pgiro);
		creaObbligazione_scad_voce( uc, obblig_scad );

		imp_pgiro = (ImpegnoPGiroBulk) super.creaConBulk( uc, imp_pgiro );

		if ( !uc.isTransactional() )
			//aggiorna i saldi
			aggiornaCapitoloSaldoObbligazione( uc, imp_pgiro, INSERIMENTO );
		
		AccertamentoPGiroBulk accert_pgiro = createAccertamentoPGiroComponent().creaAccertamento( uc, imp_pgiro);
	
		Ass_obb_acr_pgiroBulk ass_oa_pgiro = (Ass_obb_acr_pgiroBulk) creaAss_obb_acr_pgiro( uc, imp_pgiro, accert_pgiro);
		verificaStatoEsercizio( 
							uc, 
							((CNRUserContext)uc).getEsercizio(), 
							imp_pgiro.getCd_cds());
		return imp_pgiro;
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
  *      L'impegno (ImpegnoPGiroBulk) viene creato con importo pari a quello dell'accertamento, codice terzo
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
  *      L'impegno (ImpegnoPGiroBulk) viene creato con importo pari a quello dell'accertamento, codice terzo
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
  * @return imp_pgiro L'impegno su partita di giro creato in corrispondenza dell'accertamento
*/
public ImpegnoPGiroBulk creaObbligazione(
    UserContext uc,
    AccertamentoPGiroBulk accert_pgiro)
    throws ComponentException {
    try {
        /* IMPEGNO PARTITA GIRO BULK */
        ImpegnoPGiroBulk imp_pgiro = new ImpegnoPGiroBulk();
        imp_pgiro.setUser(accert_pgiro.getUser());
        imp_pgiro.setToBeCreated();

        // campi chiave
        imp_pgiro.setEsercizio(accert_pgiro.getEsercizio());
        imp_pgiro.setCd_cds(accert_pgiro.getCd_cds());
		imp_pgiro.setEsercizio_originale(accert_pgiro.getEsercizio_originale());

        // altri campi...
        imp_pgiro.setDt_scadenza(accert_pgiro.getDt_scadenza());
        if (accert_pgiro
            .getCd_tipo_documento_cont()
            .equals(Numerazione_doc_contBulk.TIPO_ACR))
            imp_pgiro.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_IMP);
        else
            imp_pgiro.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_OBB_PGIRO);

        imp_pgiro.setCd_unita_organizzativa(accert_pgiro.getCd_unita_organizzativa());
        /* 17.5.2002 modificato Simona
        if ( imp_pgiro.getCd_unita_organizzativa().equals( accert_pgiro.getCd_uo_ente()))
        {
        	imp_pgiro.setCd_cds_origine( accert_pgiro.getCd_cds() );
        	imp_pgiro.setCd_uo_origine( accert_pgiro.getCd_unita_organizzativa() );
        }
        else
        {
        	imp_pgiro.setCd_cds_origine( accert_pgiro.getCd_cds_origine() );
        	imp_pgiro.setCd_uo_origine( accert_pgiro.getCd_uo_origine() );
        }
        */
        imp_pgiro.setCd_cds_origine(accert_pgiro.getCd_cds_origine());
        imp_pgiro.setCd_uo_origine(accert_pgiro.getCd_uo_origine());

        imp_pgiro.setDt_registrazione(accert_pgiro.getDt_registrazione());
        imp_pgiro.setDs_obbligazione(
            "Annotazione di Spesa su Partita di Giro creata in automatico dal CdS: "
                .concat(
                accert_pgiro.getCd_cds_origine()));
        //gestione x impegni tronchi
        if (!accert_pgiro.isFl_isTronco())
        {
	       imp_pgiro.setIm_obbligazione(accert_pgiro.getIm_accertamento());
	       imp_pgiro.setStato_obbligazione(imp_pgiro.STATO_OBB_DEFINITIVO);            
        }   
        else 
        {
            imp_pgiro.setIm_obbligazione(new BigDecimal(0));
            imp_pgiro.setFl_isTronco(true);
	       	imp_pgiro.setStato_obbligazione(imp_pgiro.STATO_OBB_STORNATO);                        
            imp_pgiro.setDt_cancellazione(accert_pgiro.getDt_registrazione());
        }
        imp_pgiro.setFl_calcolo_automatico(new Boolean(true));

        imp_pgiro.setFl_spese_costi_altrui(new Boolean(false));
        imp_pgiro.setFl_gara_in_corso(new Boolean(false));
        imp_pgiro.setEsercizio_competenza(accert_pgiro.getEsercizio());
        imp_pgiro.setIm_costi_anticipati(new java.math.BigDecimal(0));
        imp_pgiro.setRiportato("N");

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

        imp_pgiro.setCd_terzo(
            ((it.cnr.contab.anagraf00.core.bulk.TerzoBulk) result.get(0)).getCd_terzo());
        //
        
		Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk)getHome(uc,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(imp_pgiro.getEsercizio()));
		Ass_partita_giroBulk ass_pgiro =null;
		if (parametriCnr.getFl_nuova_gestione_pg().booleanValue() ) {
			if(accert_pgiro.getElemento_voceContr()!=null && accert_pgiro.getElemento_voceContr().getCd_elemento_voce()!=null){
				if ( accert_pgiro.getElemento_voceContr().getEsercizio()!=null )
					imp_pgiro.setEsercizio( accert_pgiro.getElemento_voceContr().getEsercizio() );
				if ( accert_pgiro.getElemento_voceContr().getTi_appartenenza()!=null)
					imp_pgiro.setTi_appartenenza( accert_pgiro.getElemento_voceContr().getTi_appartenenza() );
				if ( accert_pgiro.getElemento_voceContr().getTi_gestione()!=null)
					imp_pgiro.setTi_gestione(  accert_pgiro.getElemento_voceContr().getTi_gestione() );
				if ( accert_pgiro.getElemento_voceContr().getCd_elemento_voce()!=null)
					imp_pgiro.setCd_elemento_voce(accert_pgiro.getElemento_voceContr().getCd_elemento_voce() );
			}
			else if(accert_pgiro.isFl_isTronco())
			{
				Ass_partita_giroHome ass_pgiroHome = (Ass_partita_giroHome) getHome( uc, Ass_partita_giroBulk.class );
				ass_pgiro = ass_pgiroHome.getAssociazionePGiroFor(accert_pgiro);
			    imp_pgiro.setTi_appartenenza(ass_pgiro.getTi_appartenenza_clg());
		        imp_pgiro.setTi_gestione(ass_pgiro.getTi_gestione_clg());
		        imp_pgiro.setCd_elemento_voce(ass_pgiro.getCd_voce_clg());
			}
			else
				throw new it.cnr.jada.comp.ApplicationException("Indicare la voce del Piano Contr.");
			/*{
					Ass_partita_giroHome ass_pgiroHome = (Ass_partita_giroHome) getHome( uc, Ass_partita_giroBulk.class );
					ass_pgiro = ass_pgiroHome.getAssociazionePGiroFor(accert_pgiro);
					
			        imp_pgiro.setTi_appartenenza(ass_pgiro.getTi_appartenenza_clg());
			        imp_pgiro.setTi_gestione(ass_pgiro.getTi_gestione_clg());
			        imp_pgiro.setCd_elemento_voce(ass_pgiro.getCd_voce_clg());
				}*/

		}
		else{
			Ass_partita_giroHome ass_pgiroHome = (Ass_partita_giroHome) getHome( uc, Ass_partita_giroBulk.class );
			ass_pgiro = ass_pgiroHome.getAssociazionePGiroFor(accert_pgiro);
			
	        imp_pgiro.setTi_appartenenza(ass_pgiro.getTi_appartenenza_clg());
	        imp_pgiro.setTi_gestione(ass_pgiro.getTi_gestione_clg());
	        imp_pgiro.setCd_elemento_voce(ass_pgiro.getCd_voce_clg());
		}
		        
        Obbligazione_scadenzarioBulk obblig_scad =
            creaObbligazione_scadenzario(uc, imp_pgiro);
        // creaAccertamento_scad_voce( uc, accert_scad, (Obbligazione_scad_voceBulk)(((Obbligazione_scadenzarioBulk)imp_pgiro.getObbligazione_scadenzarioColl().get(0)).getObbligazione_scad_voceColl().get(0)));
        creaObbligazione_scad_voce(uc, obblig_scad);

        verificaObbligazione(uc, imp_pgiro);

        imp_pgiro = (ImpegnoPGiroBulk) super.creaConBulk(uc, imp_pgiro);

        if (!uc.isTransactional())
            aggiornaCapitoloSaldoObbligazione(uc, imp_pgiro, INSERIMENTO);

        return imp_pgiro;
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
		obblig_scad_voce.setCd_voce( obblig_scad.getObbligazione().getElemento_voce().getCd_elemento_voce() );

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
  * @param imp_pgiro <code>ImpegnoPGiroBulk</code> l'impegno su partita di giro creato
  *
  * @return obblig_scad La scadenza dell'impegno pgiro creata
 */
private Obbligazione_scadenzarioBulk creaObbligazione_scadenzario (UserContext uc, ImpegnoPGiroBulk imp_pgiro) throws ComponentException
{

	// gc.set((imp_pgiro.getEsercizio_competenza()).intValue(),1,1);
	//Timestamp time = new Timestamp(gc.MILLISECOND);

		Obbligazione_scadenzarioBulk obblig_scad = new Obbligazione_scadenzarioBulk();
		/* simona 12/7 
		imp_pgiro.getObbligazione_scadenzarioColl().add( obblig_scad ); */
		imp_pgiro.addToObbligazione_scadenzarioColl( obblig_scad );
		
		obblig_scad.setUser( imp_pgiro.getUser() );
		obblig_scad.setToBeCreated();

		// campi chiave
		// obblig_scad.setObbligazione( imp_pgiro );
 
		// altri campi
		obblig_scad.setDt_scadenza( imp_pgiro.getDt_scadenza() );
		/*
		// controllo se l'anno di emissione dell'impegno è maggiore dell'anno corrente
		gc.set(java.util.GregorianCalendar.MONTH,1);
		gc.set(java.util.GregorianCalendar.DATE,1);
		
		if (imp_pgiro.getEsercizio_competenza().intValue() > gc.get(java.util.GregorianCalendar.YEAR) )
			obblig_scad.setDt_scadenza( java.sql.Timestamp.valueOf( imp_pgiro.getEsercizio_competenza().intValue() + "-" + 
																   		    gc.get(java.util.GregorianCalendar.MONTH) + "-" +
																   		    gc.get(java.util.GregorianCalendar.DATE) +" 00:00:00.0" ) );
		else
			obblig_scad.setDt_scadenza( imp_pgiro.getDt_registrazione() );
		*/	
		
		obblig_scad.setDs_scadenza( imp_pgiro.getDs_obbligazione() );
		obblig_scad.setIm_scadenza( imp_pgiro.getIm_obbligazione() );
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
		annullaObbligazione( aUC, (ImpegnoPGiroBulk) bulk);
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
  * @param imp_pgiro <code>ImpegnoPGiroBulk</code> l'impegno su partita di giro da cancellare (logicamente)
  *
 */
public void eliminaObbligazione(UserContext aUC,ImpegnoPGiroBulk imp_pgiro ) throws ComponentException
{
	try
	{
		if ( imp_pgiro.isAssociataADocAmm() )
			throw new ApplicationException( "Impossibile cancellare l'Annotazione di Spesa su Partita di Giro perche' ha documenti amministrativi associati");

		/*	
		imp_pgiro.setToBeDeleted();
		makeBulkPersistent( aUC, imp_pgiro);
		aggiornaCapitoloSaldoObbligazione( aUC, imp_pgiro, CANCELLAZIONE);
		*/
		imp_pgiro.setCrudStatus(OggettoBulk.NORMAL);		
        imp_pgiro.storna();
    //    imp_pgiro.setDt_cancellazione( getHome(aUC, imp_pgiro.getClass()).getServerDate());
        imp_pgiro.setDt_cancellazione( DateServices.getDt_valida(aUC));    

	  // aggiornamento obbligazione + scadenze + dettagli
      makeBulkPersistent( aUC, imp_pgiro);

      //aggiorno i saldi
      aggiornaCapitoloSaldoObbligazione(aUC, imp_pgiro, MODIFICA);		
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}
}

/* verifico nella vista V_OBB_ACC_RIPORTA quali delle 2 parti della partita di giro devo passare alla
   stored procedure che effettua il riporta avanti */

protected IDocumentoContabileBulk findPGiroDaRiportareAvanti(UserContext userContext,IDocumentoContabileBulk doc) throws ComponentException
{
	try
	{
		SQLBuilder sql = selectPGiroDaRiportare( userContext, doc, "V_OBB_ACC_RIPORTA" );
		List result = getHome( userContext, V_obb_acc_xxxBulk.class, "V_OBB_ACC_RIPORTA" ).fetchAll(sql);
		if ( result != null  && result.size() > 0 )
			return doc;
		if ( doc instanceof ImpegnoPGiroBulk )
		{
			//per le partite di giro bisogna passare alla procedura PL/SQL sempre
			//la parte che ha aperto la partita di giro
			ImpegnoPGiroBulk imp = (ImpegnoPGiroBulk) doc;
			if ( imp.getAssociazione() != null && 
				  imp.getAssociazione().getAccertamento() != null )
			{
				doc = imp.getAssociazione().getAccertamento();
				sql = selectPGiroDaRiportare( userContext, doc, "V_OBB_ACC_RIPORTA" );
				result = getHome( userContext, V_obb_acc_xxxBulk.class, "V_OBB_ACC_RIPORTA" ).fetchAll(sql);
				if ( result != null  && result.size() > 0 )
				return doc;
			}	
		}
		else if ( doc instanceof AccertamentoPGiroBulk )
		{
			AccertamentoPGiroBulk acr = (AccertamentoPGiroBulk) doc;
			if ( acr.getAssociazione() != null && 
				  acr.getAssociazione().getImpegno() != null )
			{
				doc = acr.getAssociazione().getImpegno();
				sql = selectPGiroDaRiportare( userContext, doc, "V_OBB_ACC_RIPORTA" );
				result = getHome( userContext, V_obb_acc_xxxBulk.class, "V_OBB_ACC_RIPORTA" ).fetchAll(sql);
				if ( result != null  && result.size() > 0 )
				return doc;
			}	
		}
		
		return null;	

	}
	catch (Exception e )
	{
		throw handleException( e );
	}	
	

}
/* verifico nella vista V_OBB_ACC_DERIPORTA quali delle 2 parti della partita di giro devo passare alla
   stored procedure che effettua il riporta indietro */

protected IDocumentoContabileBulk findPGiroDaRiportareIndietro(UserContext userContext,IDocumentoContabileBulk doc) throws ComponentException
{
	try
	{
		SQLBuilder sql = selectPGiroDaRiportare( userContext, doc, "V_OBB_ACC_DERIPORTA" );
		List result = getHome( userContext, V_obb_acc_xxxBulk.class, "V_OBB_ACC_DERIPORTA" ).fetchAll(sql);
		if ( result != null  && result.size() > 0 )
			return doc;
		if ( doc instanceof ImpegnoPGiroBulk )
		{
			ImpegnoPGiroBulk imp = (ImpegnoPGiroBulk) doc;
			if ( imp.getAssociazione() != null && 
				  imp.getAssociazione().getAccertamento() != null )
			{
				doc = imp.getAssociazione().getAccertamento();
				sql = selectPGiroDaRiportare( userContext, doc, "V_OBB_ACC_DERIPORTA" );
				result = getHome( userContext, V_obb_acc_xxxBulk.class, "V_OBB_ACC_DERIPORTA" ).fetchAll(sql);
				if ( result != null  && result.size() > 0 )
				return doc;
			}	
		}
		else if ( doc instanceof AccertamentoPGiroBulk )
		{
			AccertamentoPGiroBulk acr = (AccertamentoPGiroBulk) doc;
			if ( acr.getAssociazione() != null && 
				  acr.getAssociazione().getImpegno() != null )
			{
				doc = acr.getAssociazione().getImpegno();
				sql = selectPGiroDaRiportare( userContext, doc, "V_OBB_ACC_DERIPORTA" );
				result = getHome( userContext, V_obb_acc_xxxBulk.class, "V_OBB_ACC_DERIPORTA" ).fetchAll(sql);
				if ( result != null  && result.size() > 0 )
				return doc;
			}	
		}
		
		return null;	

	}
	catch (Exception e )
	{
		throw handleException( e );
	}	
	

}
/** 
  *  ricerca Unità Organizzative
  *    PreCondition:
  *      La richiesta di identificazione delle Unità Organizzative per cui e' possibile creare un'obbliga-
  *      zione PGIRO e' stata generata
  *    PostCondition:
  *      Una lista contente l'UO Ente + l'UO di scrivania (se diverso da Ente) viene restituita
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param impegno <code>ImpegnoPGiroBulk</code> l'impegno su partita di giro da creare
  *
  * @return result la lista delle unità organizzative definite per l'impegno su partita di giro
 */
public List findUnitaOrganizzativaOptions (UserContext userContext,ImpegnoPGiroBulk impegno) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
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
  *      La richiesta di inizializzazione di un ImpegnoPGiroBulk per inserimento
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
	ImpegnoPGiroBulk imp_pgiro = (ImpegnoPGiroBulk) bulk;

	try
	{
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk)getHome( aUC, Unita_organizzativa_enteBulk.class).findAll().get(0);
			
		imp_pgiro.setDt_registrazione( DateServices.getDt_valida( aUC) );
		imp_pgiro.setCds( (CdsBulk) getHome( aUC, CdsBulk.class).findByPrimaryKey( imp_pgiro.getUnita_organizzativa().getUnita_padre() ));
		imp_pgiro.setCd_cds_origine( ((CNRUserContext) aUC).getCd_cds() );
		verificaStatoEsercizio( aUC, imp_pgiro.getEsercizio(), imp_pgiro.getCd_cds());
		imp_pgiro.setDt_scadenza( DateServices.getDt_valida( aUC) );
		
		/* if (!((ObbligazioneHome)getHome(aUC, obbligazione.getClass())).verificaStatoEsercizio(obbligazione))
			throw handleException( new ApplicationException( "Non e' possibile creare obbligazioni: esercizio non ancora aperto!") );*/
			
		// se l'unità organizzativa è uguale a quella dell'Ente, non è possibile creare documenti su partita di giro
		if ( imp_pgiro.getCd_unita_organizzativa().equals( uoEnte.getCd_unita_organizzativa() ))
			throw new ApplicationException("Funzione non consentita per utente abilitato a " + uoEnte.getCd_unita_organizzativa() ); 
		return super.inizializzaBulkPerInserimento( aUC, imp_pgiro );
	}
	catch ( Exception e )
	{
		throw handleException(imp_pgiro, e);
	}
}
/** 
  *  inizializzazione per modifica
  *    PreCondition:
  *      La richiesta di inizializzazione di un ImpegnoPGiroBulk per modifica
  *      e' stata generata
  *    PostCondition:
  *      Vengono recuperati la scadenza e il dettaglio di scadenza associati all'impegno.
  *      Viene recuperata l'associazione fra l'impegno e l'accertamento
  *		 Viene recuperato l'accertamento associato all'impegno e la relativa scadenza e dettaglio scadenza
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno su partita di giro da inizializzare per la modifica
  *
  * @return imp_pgiro l'impegno su partita di giro inizializzato per la modifica
 */
public OggettoBulk inizializzaBulkPerModifica (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	try
	{
		if ( bulk instanceof V_obbligazione_im_mandatoBulk ) {
			V_obbligazione_im_mandatoBulk v_obbligazione = (V_obbligazione_im_mandatoBulk) bulk;

			bulk = (OggettoBulk) getHome( aUC, ImpegnoPGiroBulk.class).findByPrimaryKey( new ImpegnoPGiroBulk( v_obbligazione.getCd_cds(), v_obbligazione.getEsercizio(), v_obbligazione.getEsercizio_originale(), v_obbligazione.getPg_obbligazione() ));

			if ( bulk == null )
				throw new ApplicationException( "L'impegno e' stato cancellato" );
		}		

		bulk = super.inizializzaBulkPerModifica( aUC, bulk );
		
		ImpegnoPGiroBulk imp_pgiro = (ImpegnoPGiroBulk) bulk;
		AccertamentoPGiroBulk accert_pgiro = null;		

		imp_pgiro.setCd_terzo_iniziale( imp_pgiro.getCd_terzo());
		imp_pgiro.setIm_iniziale_obbligazione( imp_pgiro.getIm_obbligazione());
	
	//query per recuperare la scadenza dell'obbligazione
	//impegnoPgiro.getObbligazione_scadenzarioColl().add( scadenza );
		Obbligazione_scadenzarioHome obblig_scadHome = (Obbligazione_scadenzarioHome) getHome( aUC, Obbligazione_scadenzarioBulk.class );

		SQLBuilder sql = obblig_scadHome.createSQLBuilder();
		sql.addClause("AND","cd_cds",sql.EQUALS, imp_pgiro.getCd_cds() );
		sql.addClause("AND","esercizio",sql.EQUALS, imp_pgiro.getEsercizio() );
		sql.addClause("AND","esercizio_originale",sql.EQUALS, imp_pgiro.getEsercizio_originale() );
		sql.addClause("AND","pg_obbligazione",sql.EQUALS, imp_pgiro.getPg_obbligazione() );

		List result = obblig_scadHome.fetchAll( sql );
		Obbligazione_scadenzarioBulk obblig_scad = (Obbligazione_scadenzarioBulk) result.get(0);

		imp_pgiro.setDt_scadenza( obblig_scad.getDt_scadenza());
		imp_pgiro.getObbligazione_scadenzarioColl().add( obblig_scad );
		obblig_scad.setObbligazione( imp_pgiro);

		//carico l'eventuale doc.amministrativo legato alla scadenza
		V_doc_passivo_obbligazioneBulk docPassivo = obblig_scadHome.findDoc_passivo( obblig_scad );
		if ( docPassivo != null)
		{
			obblig_scad.setEsercizio_doc_passivo( docPassivo.getEsercizio());
			obblig_scad.setPg_doc_passivo( docPassivo.getPg_documento_amm());
			imp_pgiro.setEsercizio_doc_passivo( docPassivo.getEsercizio());
			imp_pgiro.setPg_doc_passivo( docPassivo.getPg_documento_amm());
			imp_pgiro.setCd_tipo_documento_amm( docPassivo.getCd_tipo_documento_amm());
		}

		//carico l'eventuale mandato associato
		Mandato_rigaBulk mandato = obblig_scadHome.findMandato( obblig_scad );
		if ( mandato != null )
		{
			imp_pgiro.setEsercizio_mandato( mandato.getEsercizio());
			imp_pgiro.setPg_mandato( mandato.getPg_mandato());
		}
		
	//query per recuperare scad_voce dell'obbligazione
	//scadenza.getObbligazione_scad_voceColl().add( scad_voce );
		Obbligazione_scad_voceHome obblig_scad_voceHome = (Obbligazione_scad_voceHome) getHome( aUC, Obbligazione_scad_voceBulk.class );

		sql = obblig_scad_voceHome.createSQLBuilder();
		sql.addClause("AND","cd_cds",sql.EQUALS, imp_pgiro.getCd_cds() );
		sql.addClause("AND","esercizio",sql.EQUALS, imp_pgiro.getEsercizio() );
		sql.addClause("AND","esercizio_originale",sql.EQUALS, imp_pgiro.getEsercizio_originale() );
		sql.addClause("AND","pg_obbligazione",sql.EQUALS, imp_pgiro.getPg_obbligazione() );

		result = obblig_scad_voceHome.fetchAll( sql );
		Obbligazione_scad_voceBulk obblig_scad_voce = (Obbligazione_scad_voceBulk) result.get(0);

		obblig_scad.getObbligazione_scad_voceColl().add( obblig_scad_voce );
		obblig_scad_voce.setObbligazione_scadenzario( obblig_scad );

	//query associazione
	//impegnoPgiro.setAssociazione( associazione );
	//associazione.setObbligazione ( impegnoPgiro);
		Ass_obb_acr_pgiroHome associazioneHome = (Ass_obb_acr_pgiroHome) getHome( aUC, Ass_obb_acr_pgiroBulk.class );
		
		sql = associazioneHome.createSQLBuilder();
		sql.addClause("AND","cd_cds",sql.EQUALS, imp_pgiro.getCd_cds() );
		sql.addClause("AND","esercizio",sql.EQUALS, imp_pgiro.getEsercizio() );
		sql.addClause("AND","esercizio_ori_obbligazione",sql.EQUALS, imp_pgiro.getEsercizio_originale() );
		sql.addClause("AND","pg_obbligazione",sql.EQUALS, imp_pgiro.getPg_obbligazione() );

		result = associazioneHome.fetchAll( sql );
		if ( result.size() > 0 )
		{
			Ass_obb_acr_pgiroBulk associazione = (Ass_obb_acr_pgiroBulk) result.get(0);

			imp_pgiro.setAssociazione( associazione );
			associazione.setImpegno( imp_pgiro );

		//query per recuperare l'accertamentoPgiro
		//associazione.setAccertamento( accertamentoPgiro);
		//accertamentoPgiro.setAssociazione( associazione )
			AccertamentoPGiroHome accert_pgiroHome = (AccertamentoPGiroHome) getHome( aUC, AccertamentoPGiroBulk.class );

			sql = accert_pgiroHome.createSQLBuilder();
			sql.addClause("AND","cd_cds",sql.EQUALS, associazione.getCd_cds() );
			sql.addClause("AND","esercizio",sql.EQUALS, associazione.getEsercizio());
			sql.addClause("AND","esercizio_originale",sql.EQUALS, associazione.getEsercizio_ori_accertamento());
			sql.addClause("AND","pg_accertamento",sql.EQUALS, associazione.getPg_accertamento() );

			result = accert_pgiroHome.fetchAll( sql );
			accert_pgiro = (AccertamentoPGiroBulk) result.get(0);

			accert_pgiro.setAssociazione( associazione );
			associazione.setAccertamento( accert_pgiro );
			if(accert_pgiro.getCapitolo()!=null) {
				Elemento_voceBulk ev=(Elemento_voceBulk)getHome(aUC, Elemento_voceBulk.class).findByPrimaryKey(
						new Elemento_voceBulk(accert_pgiro.getCapitolo().getCd_voce(),accert_pgiro.getCapitolo().getEsercizio(),
					  accert_pgiro.getCapitolo().getTi_appartenenza(),accert_pgiro.getCapitolo().getTi_gestione()));
			  imp_pgiro.setElemento_voceContr(ev);
			}
			  
		//query per recuperare la scadenza dell'accertamento
		//accertamentoPgiro.getAccertamento_scadenzarioColl().add( scadenza );
			Accertamento_scadenzarioHome accert_scadHome = (Accertamento_scadenzarioHome) getHome( aUC, Accertamento_scadenzarioBulk.class );

			sql = accert_scadHome.createSQLBuilder();
			sql.addClause("AND","cd_cds",sql.EQUALS, accert_pgiro.getCd_cds() );
			sql.addClause("AND","esercizio",sql.EQUALS, accert_pgiro.getEsercizio() );
			sql.addClause("AND","esercizio_originale",sql.EQUALS, accert_pgiro.getEsercizio_originale());
			sql.addClause("AND","pg_accertamento",sql.EQUALS, accert_pgiro.getPg_accertamento() );

			result = accert_scadHome.fetchAll( sql );
			Accertamento_scadenzarioBulk accert_scad = (Accertamento_scadenzarioBulk) result.get(0);

			accert_pgiro.setDt_scadenza( accert_scad.getDt_scadenza_incasso());
			accert_pgiro.getAccertamento_scadenzarioColl().add( accert_scad );
			accert_scad.setAccertamento( accert_pgiro );

			//carico l'eventuale doc.amministrativo legato alla scadenza
			V_doc_attivo_accertamentoBulk docAttivo = accert_scadHome.findDoc_attivo( accert_scad );
			if ( docAttivo != null)
			{
				accert_scad.setEsercizio_doc_attivo( docAttivo.getEsercizio());
				accert_scad.setPg_doc_attivo( docAttivo.getPg_documento_amm());
			}

		
		//query per recuperare scad_voce dell'accertamento
		//scadenza.getAccertamento_scad_voceColl().add( scad_voce );
			Accertamento_scad_voceHome accert_scad_voceHome = (Accertamento_scad_voceHome) getHome( aUC, Accertamento_scad_voceBulk.class );

			sql = accert_scad_voceHome.createSQLBuilder();
			sql.addClause("AND","cd_cds",sql.EQUALS, accert_pgiro.getCd_cds() );
			sql.addClause("AND","esercizio",sql.EQUALS, accert_pgiro.getEsercizio() );
			sql.addClause("AND","esercizio_originale",sql.EQUALS, accert_pgiro.getEsercizio_originale());
			sql.addClause("AND","pg_accertamento",sql.EQUALS, accert_pgiro.getPg_accertamento() );

			result = accert_scad_voceHome.fetchAll( sql );
			Accertamento_scad_voceBulk accert_scad_voce = (Accertamento_scad_voceBulk) result.get(0);

			accert_scad.getAccertamento_scad_voceColl().add( accert_scad_voce );
			accert_scad_voce.setAccertamento_scadenzario( accert_scad );
		}	

		//query per recuperare cd_uo_ente dell'impegno
		Unita_organizzativa_enteHome uo_enteHome = (Unita_organizzativa_enteHome) getHome( aUC, Unita_organizzativa_enteBulk.class );
		sql = uo_enteHome.createSQLBuilder();
		result = uo_enteHome.fetchAll( sql );
		Unita_organizzativa_enteBulk uo_ente = (Unita_organizzativa_enteBulk) result.get(0);

		imp_pgiro.setCd_uo_ente( uo_ente.getCd_unita_organizzativa() );
		if ( accert_pgiro!= null )
		{
			accert_pgiro.setCd_uo_ente( uo_ente.getCd_unita_organizzativa() );

			if (( accert_pgiro.getDt_cancellazione() == null && imp_pgiro.getDt_cancellazione() != null ) ||
				 ( accert_pgiro.getDt_cancellazione() != null && imp_pgiro.getDt_cancellazione() == null ) )
			{	 
				 accert_pgiro.setFl_isTronco( true );
			 	imp_pgiro.setFl_isTronco( true );
			}
		}	
			

	return imp_pgiro;
	}
	catch ( Exception e )
	{
		throw handleException(bulk, e)	;
	}
}
/** 
  *  inizializzazione per ricerca
  *    PreCondition:
  *      La richiesta di inizializzazione di un ImpegnoPGiroBulk per ricerca
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
	ImpegnoPGiroBulk imp_pgiro = (ImpegnoPGiroBulk) bulk;
	try
	{
		/*
		imp_pgiro.setCds( (CdsBulk) getHome( aUC, CdsBulk.class).findByPrimaryKey( imp_pgiro.getUnita_organizzativa().getUnita_padre() ));
		imp_pgiro.setCd_cds_origine( ((CNRUserContext) aUC).getCd_cds() );
		// if (!((ObbligazioneHome)getHome(aUC, obbligazione.getClass())).verificaStatoEsercizio(obbligazione))
		//	throw handleException( new ApplicationException( "Non e' possibile creare obbligazioni: esercizio non ancora aperto!") );
		return super.inizializzaBulkPerRicerca( aUC, imp_pgiro );
		*/
		imp_pgiro = (ImpegnoPGiroBulk) super.inizializzaBulkPerRicerca( userContext, imp_pgiro );
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0); 
		//imposto cds e uo origine
		if ( !uoEnte.getCd_unita_organizzativa().equals(((CNRUserContext)userContext).getCd_unita_organizzativa()))
		{
			Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHome( userContext, Unita_organizzativaBulk.class ).findByPrimaryKey( new Unita_organizzativaBulk( ((CNRUserContext)userContext).getCd_unita_organizzativa()));
			imp_pgiro.setCd_uo_origine( uoScrivania.getCd_unita_organizzativa());		
			imp_pgiro.setCd_cds_origine( uoScrivania.getCd_unita_padre());
			if ( imp_pgiro.isResiduo() )
			{
				imp_pgiro.setCd_cds( uoEnte.getCd_unita_padre());
				imp_pgiro.setCd_unita_organizzativa( uoEnte.getCd_unita_organizzativa());
			}	
		}
		else
		{
			imp_pgiro.setCd_cds( uoEnte.getCd_unita_padre());
			imp_pgiro.setCd_unita_organizzativa( uoEnte.getCd_unita_organizzativa());					
		}
		return imp_pgiro;
	}
	catch ( it.cnr.jada.persistency.PersistencyException e )
	{
		throw handleException(imp_pgiro, e);
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
  *  modifica importo
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di un impegno su partita di giro
  *    PostCondition:
  *      L'importo dell'impegno viene modificato e, in cascata, vengono modificati gli importi relativi
  *      alla scadenza e al dettaglio scadenza. I saldi relativi ai documenti contabili vengono aggiornati
  *		 (metodo aggiornaCapitoloSaldoObbligazione).
  *      Alla component che gestisce l'accertamento su partita di giro viene inoltrata la richiesta di modifica
  *      dell'accertamento associato all'impegno (metodo modificaAccertamento)
  *		 Vengono aggiornati gli stati COAN e COGE degli eventuali documenti amministrativi associati 
  *		 (metodo aggiornaStatoCOAN_COGEDocAmm)
  *  errore modifca importo - doc.amm.associati
  *    PreCondition:
  *      La richiesta di modifica dell'importo di un impegno su partita di giro e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'obbligazione
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la modifica
  *  modifica descrizione
  *    PreCondition:
  *      L'utente richiede la modifica della descrizione di un impegno su partita di giro
  *    PostCondition:
  *      La descrizione dell'obbligazione e della scadenza di obbligazione vengono aggiornate
  *  modifica capitolo
  *    PreCondition:
  *      L'utente richiede la modifica del capitolo di un impegno su partita di giro
  *    PostCondition:
  *      Il capitolo viene aggiornato e viene inoltrata la richiesta di modifica del capitolo
  *      dell'accertamento associato all'impegno (metodo modificaAccertamento)
  *  modifica data di scadenza
  *    PreCondition:
  *      L'utente richiede la modifica della data di scadenza di un'obbligazione su partita di giro
  *    PostCondition:
  *      La data della scadenza dell'obbligazione su partita di giro viene aggiornata.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno su partita di giro da modificare
  *
  * @return imp_pgiro <code>OggettoBulk</code> l'impegno su partita di giro modificato
 */
public OggettoBulk modificaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException
{			
	try
	{
		ImpegnoPGiroBulk imp_pgiro = (ImpegnoPGiroBulk) bulk;

		verificaStatoEsercizio( aUC, imp_pgiro.getEsercizio(), imp_pgiro.getCd_cds());		

		//segnalo impossibilità di modificare importo se ci sono doc amministrativi associati
		if ( !imp_pgiro.isFromDocAmm() && 
			imp_pgiro.isAssociataADocAmm() && imp_pgiro.getIm_iniziale_obbligazione() != null &&
			imp_pgiro.getIm_iniziale_obbligazione().compareTo( imp_pgiro.getIm_obbligazione()) != 0 ){
			imp_pgiro.setIm_obbligazione(imp_pgiro.getIm_iniziale_obbligazione());
			throw new ApplicationException( "Impossibile variare importo Annotazione di Spesa su Partita di Giro perche' e' associata a doc. amministrativi");
		}
		//segnalo impossibilità di modificare importo se ci sono mandati associati
		if ( imp_pgiro.isFromDocAmm() && 
			imp_pgiro.isAssociataADocAmm() && imp_pgiro.getIm_iniziale_obbligazione() != null &&
			imp_pgiro.getIm_iniziale_obbligazione().compareTo( imp_pgiro.getIm_obbligazione()) != 0 &&
			imp_pgiro.getPg_mandato() != null ){
				imp_pgiro.setIm_obbligazione(imp_pgiro.getIm_iniziale_obbligazione());
				throw new ApplicationException( "Impossibile variare importo Annotazione di Spesa su Partita di Giro perche' e' associata a mandato");
		}
		
		//	segnalo impossibilità di modificare un residuo se l'esercizio precedente è ancora aperto
		if ( imp_pgiro.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_IMP_RES))
			verificaStatoEsercizioEsPrecedente( aUC, imp_pgiro.getEsercizio(), imp_pgiro.getCd_cds());
			
		//importo
		Obbligazione_scadenzarioBulk obblig_scadenzario = (Obbligazione_scadenzarioBulk)imp_pgiro.getObbligazione_scadenzarioColl().get(0);
		obblig_scadenzario.setIm_scadenza( imp_pgiro.getIm_obbligazione() );
		obblig_scadenzario.setToBeUpdated();
		
		Obbligazione_scad_voceBulk obblig_scad_voce = (Obbligazione_scad_voceBulk)obblig_scadenzario.getObbligazione_scad_voceColl().get(0);
		obblig_scad_voce.setIm_voce(imp_pgiro.getIm_obbligazione() );
		obblig_scad_voce.setToBeUpdated();

		// descrizione
		obblig_scadenzario.setDs_scadenza( imp_pgiro.getDs_obbligazione() );

		//dt_scadenza
		obblig_scadenzario.setDt_scadenza( imp_pgiro.getDt_scadenza());


		// CHIAMARE IL METODO modificaAccertamento() su AccertamentoPGiroComponent
		if ( !imp_pgiro.isFl_isTronco() && !imp_pgiro.isResiduo())
			createAccertamentoPGiroComponent().modificaAccertamento( aUC, imp_pgiro);

		//aggiorna il db:

		imp_pgiro.setUser( aUC.getUser());		
		updateBulk( aUC, imp_pgiro );
		obblig_scadenzario.setUser( aUC.getUser());		
		updateBulk( aUC, obblig_scadenzario );
		obblig_scad_voce.setUser( aUC.getUser());
		if ( 	obblig_scad_voce.getCd_voce().equals( imp_pgiro.getCd_elemento_voce()))
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
			newObblig_scad_voce.setCd_voce(imp_pgiro.getCd_elemento_voce());
			newObblig_scad_voce.setUser(obblig_scad_voce.getUser());
			newObblig_scad_voce.setCd_fondo_ricerca(obblig_scad_voce.getCd_fondo_ricerca());															
			insertBulk( aUC, newObblig_scad_voce );
			deleteBulk( aUC, obblig_scad_voce);
		}	

	    if ( !aUC.isTransactional() )
		//aggiorna i saldi
		{
			aggiornaCapitoloSaldoObbligazione( aUC, imp_pgiro, MODIFICA );
	    
			aggiornaStatoCOAN_COGEDocAmm( aUC, imp_pgiro );
		}	
		

		verificaStatoEsercizio( 
							aUC, 
							((CNRUserContext)aUC).getEsercizio(), 
							imp_pgiro.getCd_cds());
		return imp_pgiro;
	
	} 
	catch ( Exception e )
	{
		throw handleException( bulk, e );
	}
}
/** 
  *  modifica importo
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di un accertamento su partita di giro
  *    PostCondition:
  *      L'importo dell'impegno associato all'accertamento pgiro viene modificato e, in cascata, vengono modificati gli importi 
  *      relativi alla scadenza e al dettaglio scadenza dell'obbligazione. I saldi relativi ai documenti contabili vengono
  *		 aggiornati (metodo aggiornaCapitoloSaldoObbligazione). Vengono aggiornati gli stati COAN e COGE degli eventuali
  *		 documenti amministrativi associati (metodo aggiornaStatoCOAN_COGEDocAmm).
  *  errore modifca importo - doc.amm.associati
  *    PreCondition:
  *      La richiesta di modifica dell'importo di un accertamento su partita di giro e' stata generata ma esistono
  *      documenti amministrativi contabilizzati sulla scadenza dell'obbligazione associata all'accertamento pgiro
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la modifica
  *  modifica capitolo
  *    PreCondition:
  *      L'utente richiede la modifica del capitolo di un accertamento su partita di giro
  *    PostCondition:
  *      Il capitolo dell'impegno associato all'accertamento pgiro viene aggiornato col valore
  *      presente nell'associazione fra capitolo di spesa e capitolo di entrata su partite di giro
  *  modifica capitolo - errore
  *    PreCondition:
  *      L'utente richiede la modifica del capitolo di un accertamento su partita di giro ma non esiste
  *      l'associazione fra il nuovo capitolo di entrata dell'accertamento pgiro e un capitolo di spesa
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la modifica
  *  modifica data di scadenza
  *    PreCondition:
  *      L'utente richiede la modifica della data di scadenza di un accertamento su partita di giro
  *    PostCondition:
  *      La data della scadenza dell'obbligazione associata all'accertamento su partita di giro viene aggiornata.
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param accert_pgiro <code>AccertamentoPGiroBulk</code> l'accertamento su partita di giro a cui è associato l'impegno pgiro da modificare
  *
  * @return imp_pgiro l'impegno su partita di giro modificato
 */
public ImpegnoPGiroBulk modificaObbligazione(UserContext uc,AccertamentoPGiroBulk accert_pgiro) throws ComponentException
{
	try
	{
		if ( accert_pgiro.getAssociazione() == null )
			return null;
		//importo
		ImpegnoPGiroBulk imp_pgiro = (ImpegnoPGiroBulk)accert_pgiro.getAssociazione().getImpegno();

		//segnalo impossibilità di modificare importo se ci sono doc amministrativi associati
		if ( /* ERRORE !accert_pgiro.isFromDocAmm() && */
			imp_pgiro.isAssociataADocAmm() && accert_pgiro.getIm_iniziale_accertamento() != null &&
			accert_pgiro.getIm_iniziale_accertamento().compareTo( accert_pgiro.getIm_accertamento()) != 0 )
			throw new ApplicationException( "Impossibile variare importo Annotazione di Spesa su Partita di Giro perche' e' associata a doc. amministrativi");

		
		// imp_pgiro.setCd_terzo( accert_pgiro.getCd_terzo() );
		imp_pgiro.setCd_riferimento_contratto( accert_pgiro.getCd_riferimento_contratto() );
		imp_pgiro.setCd_fondo_ricerca( accert_pgiro.getCd_fondo_ricerca() );
		imp_pgiro.setDt_scadenza_contratto( accert_pgiro.getDt_scadenza_contratto() );
		imp_pgiro.setIm_obbligazione( accert_pgiro.getIm_accertamento() );
		imp_pgiro.setToBeUpdated();
		
		Obbligazione_scadenzarioBulk obblig_scadenzario = (Obbligazione_scadenzarioBulk) accert_pgiro.getAssociazione().getImpegno().getObbligazione_scadenzarioColl().get(0);
		obblig_scadenzario.setIm_scadenza(accert_pgiro.getIm_accertamento() );
		obblig_scadenzario.setToBeUpdated();
		
		Obbligazione_scad_voceBulk obblig_scad_voce = (Obbligazione_scad_voceBulk)obblig_scadenzario.getObbligazione_scad_voceColl().get(0);
		obblig_scad_voce.setIm_voce( accert_pgiro.getIm_accertamento() );
		obblig_scad_voce.setToBeUpdated();
		
		//capitolo
		//query per recuperare il nuovo capitolo per l'obbligazione
		//set capitolo obbligazione
		//set capitolo obbligazione_scad_voce
		
		Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk)getHome(uc,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(imp_pgiro.getEsercizio()));
		Ass_partita_giroBulk ass_pgiro =null;
		if (parametriCnr.getFl_nuova_gestione_pg().booleanValue() ) {
			if(accert_pgiro.getElemento_voceContr()!=null ){
				if ( accert_pgiro.getElemento_voceContr().getEsercizio()!=null)
					imp_pgiro.setEsercizio( accert_pgiro.getElemento_voceContr().getEsercizio() );
				if ( accert_pgiro.getElemento_voceContr().getTi_appartenenza()!=null)
					imp_pgiro.setTi_appartenenza( accert_pgiro.getElemento_voceContr().getTi_appartenenza() );
				if ( accert_pgiro.getElemento_voceContr().getTi_gestione()!=null)
					imp_pgiro.setTi_gestione(  accert_pgiro.getElemento_voceContr().getTi_gestione() );
				if ( accert_pgiro.getElemento_voceContr().getCd_elemento_voce()!=null)
					imp_pgiro.setCd_elemento_voce(accert_pgiro.getElemento_voceContr().getCd_elemento_voce() );
			}
			else{
					Ass_partita_giroHome ass_pgiroHome = (Ass_partita_giroHome) getHome( uc, Ass_partita_giroBulk.class );
					ass_pgiro = ass_pgiroHome.getAssociazionePGiroFor(accert_pgiro);
					
			        imp_pgiro.setTi_appartenenza(ass_pgiro.getTi_appartenenza_clg());
			        imp_pgiro.setTi_gestione(ass_pgiro.getTi_gestione_clg());
			        imp_pgiro.setCd_elemento_voce(ass_pgiro.getCd_voce_clg());
				}

		}
		else{
			Ass_partita_giroHome ass_pgiroHome = (Ass_partita_giroHome) getHome( uc, Ass_partita_giroBulk.class );
			ass_pgiro = ass_pgiroHome.getAssociazionePGiroFor(accert_pgiro);
			
	        imp_pgiro.setTi_appartenenza(ass_pgiro.getTi_appartenenza_clg());
	        imp_pgiro.setTi_gestione(ass_pgiro.getTi_gestione_clg());
	        imp_pgiro.setCd_elemento_voce(ass_pgiro.getCd_voce_clg());
		}
//		obblig_scad_voce.setCd_voce( ( (Ass_partita_giroBulk)result.get(0) ).getCd_voce_clg() );

		//data scadenza
		imp_pgiro.setDt_scadenza( accert_pgiro.getDt_scadenza());
		obblig_scadenzario.setDt_scadenza( accert_pgiro.getDt_scadenza());
	

		//aggiorna il db:
		//e' necessario aggiornare prima i dettagli e poi la testata per consentire lo storico delle modifiche
		imp_pgiro.setUser( uc.getUser());		
		updateBulk( uc, imp_pgiro );		
		obblig_scadenzario.setUser( uc.getUser());		
		updateBulk( uc, obblig_scadenzario );
		obblig_scad_voce.setUser( uc.getUser());
		if ( 	obblig_scad_voce.getCd_voce().equals( imp_pgiro.getCd_elemento_voce()))
			updateBulk( uc, obblig_scad_voce );
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
			newObblig_scad_voce.setCd_voce(imp_pgiro.getCd_elemento_voce());
			newObblig_scad_voce.setUser(obblig_scad_voce.getUser());
			newObblig_scad_voce.setCd_fondo_ricerca(obblig_scad_voce.getCd_fondo_ricerca());															
			insertBulk( uc, newObblig_scad_voce );
			deleteBulk( uc, obblig_scad_voce);
		}	


	    if ( !uc.isTransactional() )
	    {
			//aggiorna i saldi
			aggiornaCapitoloSaldoObbligazione( uc, imp_pgiro, MODIFICA );
		    
			aggiornaStatoCOAN_COGEDocAmm( uc, imp_pgiro );
	    }	
		

		return imp_pgiro;
	}
	catch ( Exception e )
	{
		throw handleException( e )	;

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
	ImpegnoPGiroBulk imp_pgiro = (ImpegnoPGiroBulk) scadenza.getObbligazione();
	if ( imp_pgiro.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_IMP_RES) )
		throw handleException( new ApplicationException( "Non è consentita la modifica dell'importo di testata di un'annotazione residua." ));					
	
	imp_pgiro.setIm_obbligazione( nuovoImporto );
	imp_pgiro.setToBeUpdated();

	imp_pgiro.setFromDocAmm( true );
	modificaConBulk( userContext, scadenza.getObbligazione());
	return scadenza;

}
/*
 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite su Impegni su partite di giro
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
 *       clausole che l'obbligazione abbia esercizio di creazione uguale a quello di scrivania
 *       e UO di origine uguale a quella di scrivania e che la controparte dell'obbligazione esista e sia annullata
 *
 * Nome: Richiesta di ricerca di un'annotazione su pgiro non tronca 
 * Pre:  E' stata generata la richiesta di ricerca di un'annotazione su pgiro non tronca 
 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
 *       clausole che l'obbligazione abbia esercizio di creazione uguale a quello di scrivania
 *       e UO di origine uguale a quella di scrivania e che la controparte dell'obbligazione esista e non sia annullata;
 *
 * Nome: Richiesta di ricerca di un'annotazione su pgiro residua
 * Pre:  E' stata generata la richiesta di ricerca di un'annotazione su pgiro residua del bilancio Ente
 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
 *       clausole che l'obbligazione abbia esercizio di creazione uguale a quello di scrivania
 *       e UO di origine uguale a quella di scrivania e che il tipo di documento sia IMP_RES
 * 
 * @param userContext lo userContext che ha generato la richiesta
 * @param clauses clausole di ricerca gia' specificate dall'utente
 * @param bulk istanza di CdsBulk o Unita_organizzativaBulk che deve essere utilizzata per la ricerca
 * @return sql Query con le clausole aggiuntive 
 */

protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	ImpegnoPGiroBulk imp = (ImpegnoPGiroBulk) bulk;
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
	else if ( !imp.isFl_isTronco() )
	{
		//ricerco la parte con controparte
		sql.addTableToHeader( "ACCERTAMENTO");
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
		sql.openParenthesis( "AND");//obbligazione non tronchi
		sql.addSQLClause( "AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", sql.ISNULL, null );
		sql.addSQLClause( "AND", "ACCERTAMENTO.DT_CANCELLAZIONE", sql.ISNULL, null );		
		sql.closeParenthesis();
		sql.openParenthesis( "OR");//tutti obbligazioni cancellate (tronche e non)
		sql.addSQLClause( "AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", sql.ISNOTNULL, null );
		sql.addSQLClause( "AND", "ACCERTAMENTO.DT_CANCELLAZIONE", sql.ISNOTNULL, null );		
		sql.closeParenthesis();
		sql.closeParenthesis();
		return sql;	
	
	}
	else	//obbligazioni tronche
	{
		sql.setDistinctClause( true );
		sql.addTableToHeader( "ACCERTAMENTO");
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
		sql.openParenthesis( "AND"); //tutti obbligazioni cancellate (tronche e non)
		sql.addSQLClause( "AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", sql.ISNOTNULL, null );
		sql.addSQLClause( "AND", "ACCERTAMENTO.DT_CANCELLAZIONE", sql.ISNOTNULL, null );		
		sql.closeParenthesis();
		sql.openParenthesis( "OR"); //obbligazioni tronche 
		sql.addSQLClause( "AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", sql.ISNULL, null );
		sql.addSQLClause( "AND", "ACCERTAMENTO.DT_CANCELLAZIONE", sql.ISNOTNULL, null );		
		sql.closeParenthesis();
		sql.openParenthesis( "OR"); //obbligazioni legati a accertamenti tronchi
		sql.addSQLClause( "AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", sql.ISNOTNULL, null );
		sql.addSQLClause( "AND", "ACCERTAMENTO.DT_CANCELLAZIONE", sql.ISNULL, null );		
		sql.closeParenthesis();
		sql.closeParenthesis();
		return sql;		
		}

}
protected SQLBuilder selectPGiroDaRiportare(UserContext userContext,IDocumentoContabileBulk doc, String vista) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	SQLBuilder sql = getHome( userContext, V_obb_acc_xxxBulk.class, vista ).createSQLBuilder();
	sql.addSQLClause( "AND", "esercizio", sql.EQUALS, doc.getEsercizio());
	sql.addSQLClause( "AND", "cd_cds", sql.EQUALS, doc.getCd_cds());
	sql.addSQLClause( "AND", "ti_gestione", sql.EQUALS, doc.getTi_entrata_spesa());
	sql.addSQLClause( "AND", "esercizio_ori_acc_obb", sql.EQUALS, doc.getEsercizio_originale());
	sql.addSQLClause( "AND", "pg_acc_obb", sql.EQUALS, doc.getPg_doc_contabile());
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
  * @param impegno <code>ImpegnoPGiroBulk</code> l'obbligazione su partita di giro da validare
  *
 */
protected void verificaObbligazione(UserContext userContext, ImpegnoPGiroBulk impegno ) throws it.cnr.jada.persistency.PersistencyException, ComponentException, ApplicationException, javax.ejb.EJBException
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

		Timestamp dataUltObbligazione = ((ImpegnoPGiroHome) getHome( userContext, ImpegnoPGiroBulk.class )).findDataUltimaObbligazionePerCds( impegno );
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
			
}
