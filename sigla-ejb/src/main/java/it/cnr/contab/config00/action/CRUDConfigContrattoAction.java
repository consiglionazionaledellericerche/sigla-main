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

/*
 * Created on Apr 12, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.action;

import it.cnr.contab.config00.bp.CRUDConfigAnagContrattoBP;
import it.cnr.contab.config00.bp.CRUDConfigAnagContrattoMasterBP;
import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.config00.contratto.bulk.*;
import it.cnr.contab.config00.ejb.ContrattoComponentSession;
import it.cnr.contab.config00.service.ContrattoService;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.upload.UploadedFile;
import it.cnr.si.spring.storage.StorageObject;

import java.math.BigDecimal;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDConfigContrattoAction extends CRUDAction {

	/**
	 * Construtor from superclass
	 *
	 */
	public CRUDConfigContrattoAction() {
		super();
	}
	/**
	 * Ripulisce il searchtool del Tipo Contratto
	 * @author mspasiano
	 * @return it.cnr.jada.action.Forward
	 * @param context it.cnr.jada.action.ActionContext
	 */

	private BigDecimal getQuantitaOldCheck(Dettaglio_contrattoBulk riga, boolean originQuantitaMax ){
		if ( originQuantitaMax)
			return riga.getQuantitaMax();
		return riga.getQuantitaMin();
	}
	private void setQuantitaOldCheck(Dettaglio_contrattoBulk riga, BigDecimal quantitaOld,boolean originQuantitaMax ){
		if ( originQuantitaMax)
			riga.setQuantitaMax(quantitaOld);
		riga.setQuantitaMin(quantitaOld);
	}
	private void checkQuantitaMaxMin(ActionContext context,boolean originQuantitaMax) throws FillException, ApplicationException {
		CRUDConfigAnagContrattoBP bp = (CRUDConfigAnagContrattoBP) getBusinessProcess(context);
		ContrattoBulk model=(ContrattoBulk)bp.getModel();
		Dettaglio_contrattoBulk riga = (Dettaglio_contrattoBulk) bp.getCrudDettaglio_contratto().getModel();
		BigDecimal quantitaOld=getQuantitaOldCheck(riga,originQuantitaMax);
		fillModel(context);
		if ( riga.getQuantitaMin().compareTo(riga.getQuantitaMax())>0) {
			setQuantitaOldCheck( riga,quantitaOld,originQuantitaMax);
			if ( originQuantitaMax)
				throw new ApplicationException("La Quantità Massima deve essere maggiore o uguale della Quantità Minima");
			throw new ApplicationException("La Quantità Minima deve essere minore o uguale della Quantità Massima");
		}
	}
	public Forward doOnQuantitaMinChange(ActionContext context) {
		try {
			checkQuantitaMaxMin(context,false);
			return context.findDefaultForward();
		} catch (Throwable e) {
			return handleException(context, e);
		}
	}

	public Forward doOnQuantitaMaxChange(ActionContext context) {

		try {
			checkQuantitaMaxMin( context,true);
			return context.findDefaultForward();
		} catch (Throwable e) {
			return handleException(context, e);
		}
	}
	public Forward doOnTipoChange(ActionContext context) {

		try{
			fillModel(context);
			SimpleCRUDBP bp = (SimpleCRUDBP)getBusinessProcess(context);
			ContrattoBulk contratto = (ContrattoBulk)bp.getModel();
			contratto.setTipo_contratto(null);
			contratto.setFl_mepa(null);
			if (contratto.getNatura_contabile()!= null &&
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_SENZA_FLUSSI_FINANZIARI)){
				contratto.setIm_contratto_attivo(null);
				contratto.setIm_contratto_passivo(null);
				contratto.setProgetto(null);
			}else if (contratto.getNatura_contabile()!= null &&
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO)){
				contratto.setIm_contratto_passivo(null);
			}else if (contratto.getNatura_contabile()!= null &&
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO)){
				contratto.setIm_contratto_attivo(null);
				contratto.setProgetto(null);
			}
			return context.findDefaultForward();

		}catch(it.cnr.jada.bulk.FillException ex){
			return handleException(context, ex);
		}
	}
	public it.cnr.jada.action.Forward doAggiungiUO(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			CRUDConfigAnagContrattoBP bp = (CRUDConfigAnagContrattoBP)getBusinessProcess(context);
			ContrattoBulk contratto = (ContrattoBulk)bp.getModel();
			int[] indexes = bp.getCrudAssUODisponibili().getSelectedRows(context);

			java.util.Arrays.sort( indexes );
			if(indexes.length > 0)
				bp.setDirty(true);
			for (int i = 0;i < indexes.length;i++)
			{
				Unita_organizzativaBulk uo = (Unita_organizzativaBulk)contratto.getAssociazioneUODisponibili().get(indexes[i]);
				Ass_contratto_uoBulk ass_contratto_uo = new Ass_contratto_uoBulk(contratto.getEsercizio(),contratto.getStato(),contratto.getPg_contratto(),uo.getCd_unita_organizzativa());
				ass_contratto_uo.setUnita_organizzativa(uo);
				ass_contratto_uo.setToBeCreated();
				contratto.addToAssociazioneUO(ass_contratto_uo);
			}
			for (int i = indexes.length - 1 ;i >= 0 ;i--)
			{
				contratto.removeFromAssociazioneUODisponubili(indexes[i]);
			}
			bp.getCrudAssUODisponibili().getSelection().clearSelection();
		} catch (FillException ex) {
			return handleException(context, ex);
		}
		return context.findDefaultForward();
	}
	public it.cnr.jada.action.Forward doRimuoviUO(it.cnr.jada.action.ActionContext context) {
		CRUDConfigAnagContrattoBP bp = (CRUDConfigAnagContrattoBP)getBusinessProcess(context);
		try {
			fillModel(context);
			ContrattoBulk contratto = (ContrattoBulk)bp.getModel();
			int[] indexes = bp.getCrudAssUO().getSelectedRows(context);

			java.util.Arrays.sort( indexes );
			if(indexes.length > 0)
				bp.setDirty(true);
			for (int i = indexes.length - 1 ;i >= 0 ;i--)
			{
				Ass_contratto_uoBulk ass_contratto_uo = (Ass_contratto_uoBulk)contratto.getAssociazioneUO().get(indexes[i]);
				bp.controllaCancellazioneAssociazioneUo(context,ass_contratto_uo);
				Unita_organizzativaBulk uo = new Unita_organizzativaBulk(ass_contratto_uo.getCd_unita_organizzativa());
				uo.setDs_unita_organizzativa(ass_contratto_uo.getUnita_organizzativa().getDs_unita_organizzativa());
				ass_contratto_uo.setToBeDeleted();
				contratto.addToAssociazioneUODisponibili(uo);
				contratto.removeFromAssociazioneUO(indexes[i]);
			}
		} catch (FillException ex) {
			return handleException(context, ex);
		} catch (BusinessProcessException ex) {
			return handleException(context, ex);
		}finally{
			bp.getCrudAssUO().getSelection().clearSelection();
		}
		return context.findDefaultForward();
	}

	/**
	 * Gestisce un comando di cancellazione.
	 */
	public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {

		try {
			fillModel(context);
			ContrattoService contrattoService = SpringUtil.getBean("contrattoService",
					ContrattoService.class);
			SimpleCRUDBP bp = (SimpleCRUDBP)getBusinessProcess(context);
			if (!bp.isEditing()) {
				bp.setMessage("Non è possibile cancellare in questo momento");
			} else {
				if(bp.getModel() instanceof ContrattoBulk){
					if(((ContrattoBulk)bp.getModel()).isProvvisorio()){
						return super.doElimina(context);
					}
					ContrattoBulk contratto = (ContrattoBulk)bp.getModel();
					bp.setDirty(false);
					if (contratto.getDs_annullamento() == null)
						throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(contratto.getClass()).getFieldProperty("ds_annullamento").getLabel());
					if(contratto.getAtto_annullamento() == null)
						throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(contratto.getClass()).getFieldProperty("atto_annullamento").getLabel());
					if(contratto.isDs_atto_ann_non_definitoVisible() && contratto.getDs_atto_ann_non_definito() == null)
						throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(contratto.getClass()).getFieldProperty("ds_atto_ann_non_definito").getLabel());
					if(contratto.getOrgano_annullamento() == null)
						throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(contratto.getClass()).getFieldProperty("organo_annullamento").getLabel());
					if(contratto.isDs_organo_ann_non_definitoVisible() && contratto.getDs_organo_ann_non_definito() == null)
						throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(contratto.getClass()).getFieldProperty("ds_organo_ann_non_definito").getLabel());
				}
				StorageObject folder = contrattoService.getFolderContratto((ContrattoBulk) bp.getModel());
				bp.delete(context);
				if(bp.getModel() instanceof ContrattoBulk && ((ContrattoBulk)bp.getModel()).isDefinitivo()){
					bp.edit(context,((ContrattoComponentSession)bp.createComponentSession()).cercaContrattoCessato(context.getUserContext(), bp.getModel()));
				}else
					bp.edit(context, bp.getModel());
				if (folder != null){
					contrattoService.updateProperties((ContrattoBulk) bp.getModel(), folder);
					contrattoService.changeProgressivoNodeRef(folder, (ContrattoBulk) bp.getModel());
					contrattoService.addAspect(folder, "P:sigla_contratti_aspect:stato_annullato");
					contrattoService.removeConsumerToEveryone(folder);
					bp.setModel(context,bp.initializeModelForEdit(context, bp.getModel()));
				}

				bp.setMessage("Annullamento effettuato");
			}
			return context.findDefaultForward();

		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestione apertura della consultazione del totale dei documenti contabili per Commessa e Contratto
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doVisualizzaDocContForCommessaContratto(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			SimpleCRUDBP rootbp = (SimpleCRUDBP)getBusinessProcess(context);
			ContrattoBulk contratto = (ContrattoBulk)rootbp.getModel();
			CompoundFindClause clauses = new CompoundFindClause();
			it.cnr.jada.util.action.ConsultazioniBP bp = (it.cnr.jada.util.action.ConsultazioniBP)context.createBusinessProcess("ConsTotaleDocContCommessaContrattoBP");
			if (contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO) ||
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO)||
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO)){
				clauses.addClause("AND","esercizio_contratto",SQLBuilder.EQUALS,contratto.getEsercizio());
				clauses.addClause("AND","stato_contratto",SQLBuilder.EQUALS,contratto.getStato());
				clauses.addClause("AND","pg_contratto",SQLBuilder.EQUALS,contratto.getPg_contratto());
				bp.setSearchResultColumnSet("Commessa");
			}else{
				clauses.addClause("AND","esercizio_contratto_padre",SQLBuilder.EQUALS,contratto.getEsercizio());
				clauses.addClause("AND","stato_contratto_padre",SQLBuilder.EQUALS,contratto.getStato());
				clauses.addClause("AND","pg_contratto_padre",SQLBuilder.EQUALS,contratto.getPg_contratto());
				bp.setSearchResultColumnSet("Commessa_contratto");
			}
			bp.addToBaseclause(clauses);
			bp.openIterator(context);
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestione apertura della consultazione dei documenti contabili per Commessa e Contratto
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doVisualizzaDocContEtr(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			SimpleCRUDBP rootbp = (SimpleCRUDBP)getBusinessProcess(context);
			ContrattoBulk contratto = (ContrattoBulk)rootbp.getModel();
			CompoundFindClause clauses = new CompoundFindClause();
			clauses.addClause("AND","tipo",SQLBuilder.EQUALS,"ETR");
			it.cnr.jada.util.action.ConsultazioniBP bp = (it.cnr.jada.util.action.ConsultazioniBP)context.createBusinessProcess("ConsDocContCommessaContrattoBP");
			if (contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO) ||
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO)||
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO)){
				clauses.addClause("AND","esercizio_contratto",SQLBuilder.EQUALS,contratto.getEsercizio());
				clauses.addClause("AND","stato_contratto",SQLBuilder.EQUALS,contratto.getStato());
				clauses.addClause("AND","pg_contratto",SQLBuilder.EQUALS,contratto.getPg_contratto());
				bp.setSearchResultColumnSet("Accertamenti");
			}else{
				clauses.addClause("AND","esercizio_contratto_padre",SQLBuilder.EQUALS,contratto.getEsercizio());
				clauses.addClause("AND","stato_contratto_padre",SQLBuilder.EQUALS,contratto.getStato());
				clauses.addClause("AND","pg_contratto_padre",SQLBuilder.EQUALS,contratto.getPg_contratto());
				bp.setSearchResultColumnSet("AccertamentiContratti");
			}
			bp.addToBaseclause(clauses);
			bp.openIterator(context);
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestione apertura della consultazione dei documenti contabili per Commessa e Contratto
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doVisualizzaDocContSpe(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			SimpleCRUDBP rootbp = (SimpleCRUDBP)getBusinessProcess(context);
			ContrattoBulk contratto = (ContrattoBulk)rootbp.getModel();
			CompoundFindClause clauses = new CompoundFindClause();
			clauses.addClause("AND","tipo",SQLBuilder.EQUALS,"SPE");
			it.cnr.jada.util.action.ConsultazioniBP bp = (it.cnr.jada.util.action.ConsultazioniBP)context.createBusinessProcess("ConsDocContCommessaContrattoBP");
			if (contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO) ||
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO)||
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO)){
				clauses.addClause("AND","esercizio_contratto",SQLBuilder.EQUALS,contratto.getEsercizio());
				clauses.addClause("AND","stato_contratto",SQLBuilder.EQUALS,contratto.getStato());
				clauses.addClause("AND","pg_contratto",SQLBuilder.EQUALS,contratto.getPg_contratto());
				bp.setSearchResultColumnSet("Obbligazioni");
			}else{
				clauses.addClause("AND","esercizio_contratto_padre",SQLBuilder.EQUALS,contratto.getEsercizio());
				clauses.addClause("AND","stato_contratto_padre",SQLBuilder.EQUALS,contratto.getStato());
				clauses.addClause("AND","pg_contratto_padre",SQLBuilder.EQUALS,contratto.getPg_contratto());
				bp.setSearchResultColumnSet("ObbligazioniContratti");
			}
			bp.addToBaseclause(clauses);
			bp.openIterator(context);
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestione della richiesta di salvataggio di un contratto come definitivo
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doSalvaDefinitivo(ActionContext context) {

		try {
			fillModel(context);
			CRUDConfigAnagContrattoBP bp = (CRUDConfigAnagContrattoBP)getBusinessProcess(context);
			bp.salvaDefinitivo(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}

	}

	public Forward doPubblicaContratto(ActionContext context) {
		try {
			fillModel(context);
			CRUDConfigAnagContrattoBP bp = (CRUDConfigAnagContrattoBP)getBusinessProcess(context);
			bp.pubblicaContratto(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Pubblicazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
	public Forward doUnpublishContratto(ActionContext context) {
		try {
			fillModel(context);
			if (!(getBusinessProcess(context) instanceof CRUDConfigAnagContrattoMasterBP))
				return context.findDefaultForward();

			CRUDConfigAnagContrattoMasterBP bp = (CRUDConfigAnagContrattoMasterBP)getBusinessProcess(context);
			bp.unpublishContratto(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Pubblicazione rimossa con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}

	/**
	 * Gestisce la validazione di nuovo atto creato
	 * @param context <code>ActionContext</code> in uso.
	 * @param contratto Oggetto di tipo <code>ContrattoBulk</code>
	 * @param atto Oggetto di tipo <code>Tipo_atto_amministrativoBulk</code> che rappresenta il nuovo atto creato
	 *
	 * @return <code>Forward</code>
	 */
	public Forward doBringBackCRUDCrea_atto(ActionContext context, ContrattoBulk contratto, Tipo_atto_amministrativoBulk atto)
	{
		try
		{
			if (atto != null )
			{
				if (atto.isCancellatoLogicamente())
					throw new it.cnr.jada.action.MessageToUser("Atto non valido!");
				contratto.setAtto( atto );
			}
			return context.findDefaultForward();
		}
		catch(it.cnr.jada.action.MessageToUser e)
		{
			getBusinessProcess(context).setErrorMessage(e.getMessage());
			return context.findDefaultForward();
		}

		catch(Throwable e) {return handleException(context,e);}
	}
	public Forward doBringBackCRUDCrea_atto_annullamento(ActionContext context, ContrattoBulk contratto, Tipo_atto_amministrativoBulk atto)
	{
		return doBringBackCRUDCrea_atto(context, contratto, atto);
	}
	/**
	 * Gestisce la validazione di nuovo atto creato
	 * @param context <code>ActionContext</code> in uso.
	 * @param contratto Oggetto di tipo <code>ContrattoBulk</code>
	 * @param organo Oggetto di tipo <code>OrganoBulk</code> che rappresenta il nuovo organo creato
	 *
	 * @return <code>Forward</code>
	 */
	public Forward doBringBackCRUDCrea_organo(ActionContext context, ContrattoBulk contratto, OrganoBulk organo)
	{
		try
		{
			if (organo != null )
			{
				if (organo.isCancellatoLogicamente())
					throw new it.cnr.jada.action.MessageToUser("Atto non valido!");
				contratto.setOrgano( organo );
			}
			return context.findDefaultForward();
		}
		catch(it.cnr.jada.action.MessageToUser e)
		{
			getBusinessProcess(context).setErrorMessage(e.getMessage());
			return context.findDefaultForward();
		}

		catch(Throwable e) {return handleException(context,e);}
	}
	public Forward doBringBackCRUDCrea_organo_annullamento(ActionContext context, ContrattoBulk contratto, OrganoBulk organo)
	{
		return doBringBackCRUDCrea_organo(context, contratto, organo);
	}
	/**
	 * Gestisce la validazione di nuovo atto creato
	 * @param context <code>ActionContext</code> in uso.
	 * @param contratto Oggetto di tipo <code>ContrattoBulk</code>
	 * @param tipo_contratto Oggetto di tipo <code>Tipo_contrattoBulk</code> che rappresenta il nuovo tipo contratto creato
	 *
	 * @return <code>Forward</code>
	 */
	public Forward doBringBackCRUDCrea_tipo_contratto(ActionContext context, ContrattoBulk contratto, Tipo_contrattoBulk tipo_contratto)
	{
		try
		{
			if (tipo_contratto != null )
			{
				if (tipo_contratto.isCancellatoLogicamente() || !contratto.getNatura_contabile().equals(tipo_contratto.getNatura_contabile()))
					throw new it.cnr.jada.action.MessageToUser("Tipo contratto non valido!");
				contratto.setTipo_contratto( tipo_contratto );
			}
			return context.findDefaultForward();
		}
		catch(it.cnr.jada.action.MessageToUser e)
		{
			getBusinessProcess(context).setErrorMessage(e.getMessage());
			return context.findDefaultForward();
		}

		catch(Throwable e) {return handleException(context,e);}
	}
	/**
	 * Gestisce la validazione di nuovo atto creato
	 * @param context <code>ActionContext</code> in uso.
	 * @param contratto Oggetto di tipo <code>ContrattoBulk</code>
	 * @param procedura_amministrativa Oggetto di tipo <code>Procedure_amministrativeBulk</code> che rappresenta la nuova Procedura amministrativa creata
	 *
	 * @return <code>Forward</code>
	 */
	public Forward doBringBackCRUDCrea_procedura_amministrativa(ActionContext context, ContrattoBulk contratto, Procedure_amministrativeBulk procedura_amministrativa)
	{
		try
		{
			if (procedura_amministrativa != null )
			{
				if (procedura_amministrativa.isCancellatoLogicamente())
					throw new it.cnr.jada.action.MessageToUser("Procedura amministrativa non valida!");
				contratto.setProcedura_amministrativa( procedura_amministrativa );
			}
			return context.findDefaultForward();
		}
		catch(it.cnr.jada.action.MessageToUser e)
		{
			getBusinessProcess(context).setErrorMessage(e.getMessage());
			return context.findDefaultForward();
		}

		catch(Throwable e) {return handleException(context,e);}
	}
	public Forward doBringBackCRUDCrea_cig(ActionContext context, ContrattoBulk contratto, CigBulk cig)
	{
		CRUDConfigAnagContrattoBP bp = (CRUDConfigAnagContrattoBP)getBusinessProcess(context);
		try
		{
			if (cig != null )
			{
				it.cnr.jada.util.RemoteIterator ri = ((ContrattoComponentSession)bp.createComponentSession()).
						findContrattoByCig(context.getUserContext(),contratto,cig);
				ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
				if (ri.countElements() == 0) {
					it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
					throw new it.cnr.jada.action.MessageToUser("CIG non valido!");
				}
				contratto.setCig(cig);
			}
			return context.findDefaultForward();
		}
		catch(it.cnr.jada.action.MessageToUser e)
		{
			getBusinessProcess(context).setErrorMessage(e.getMessage());
			return context.findDefaultForward();
		}

		catch(Throwable e) {return handleException(context,e);}
	}

	public Forward doTab(ActionContext actioncontext, String s, String s1)
	{
		if (s1.equals("tabAss_contratto_uo")){
			try {
				CRUDConfigAnagContrattoBP bp = (CRUDConfigAnagContrattoBP)getBusinessProcess(actioncontext);
				bp.initializzaUnita_Organizzativa(actioncontext);
			} catch (BusinessProcessException exception) {
				return handleException(actioncontext, exception);
			}
		}
		return super.doTab(actioncontext, s, s1);
	}
	public it.cnr.jada.action.Forward doChangeTipologia(it.cnr.jada.action.ActionContext context) {
		CRUDConfigAnagContrattoBP bp = (CRUDConfigAnagContrattoBP)getBusinessProcess(context);
		try {
			fillModel(context);
			ContrattoBulk contratto = (ContrattoBulk)bp.getModel();
			AllegatoContrattoDocumentBulk allegato = (AllegatoContrattoDocumentBulk) bp.getCrudArchivioAllegati().getModel();
			if (!allegato.isTipoAllegatoDuplicabile()){
				for (AllegatoContrattoDocumentBulk child : contratto.getArchivioAllegati()) {
					if (!child.equals(allegato) && child.getType().equals(allegato.getType())){
						setErrorMessage(context,"Attenzione! Il tipo selezionato risulta gia' presente!");
						allegato.setType(null);
						break;
					}
				}
			}
			if (contratto.isFromFlussoAcquisti() && allegato.isTipoAllegatoContratto()){
				setErrorMessage(context,"Attenzione! Contratto proveniente da flusso acquisti, non è possibile allegare il contratto!");
				allegato.setType(null);
			}
		} catch (FillException ex) {
			return handleException(context, ex);
		}
		return context.findDefaultForward();
	}
	public it.cnr.jada.action.Forward doVisualizzaDoccontContEtr(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			SimpleCRUDBP rootbp = (SimpleCRUDBP)getBusinessProcess(context);
			ContrattoBulk contratto = (ContrattoBulk)rootbp.getModel();
			CompoundFindClause clauses = new CompoundFindClause();
			clauses.addClause("AND","tipo",SQLBuilder.EQUALS,"ETR");
			it.cnr.jada.util.action.ConsultazioniBP bp = (it.cnr.jada.util.action.ConsultazioniBP)context.createBusinessProcess("ConsDocContrattoBP");
			if (contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO) ||
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO)||
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO)){
				clauses.addClause("AND","esercizioContratto",SQLBuilder.EQUALS,contratto.getEsercizio());
				clauses.addClause("AND","statoContratto",SQLBuilder.EQUALS,contratto.getStato());
				clauses.addClause("AND","pgContratto",SQLBuilder.EQUALS,contratto.getPg_contratto());
				clauses.addClause("AND","pgManRev",SQLBuilder.ISNOTNULL,null);
				bp.setSearchResultColumnSet("Doccont");
			}else{
				clauses.addClause("AND","esercizioContrattoPadre",SQLBuilder.EQUALS,contratto.getEsercizio());
				clauses.addClause("AND","statoContrattoPadre",SQLBuilder.EQUALS,contratto.getStato());
				clauses.addClause("AND","pgContrattoPadre",SQLBuilder.EQUALS,contratto.getPg_contratto());
				clauses.addClause("AND","pgManRev",SQLBuilder.ISNOTNULL,null);
				bp.setSearchResultColumnSet("Doccont");
			}
			bp.addToBaseclause(clauses);
			bp.openIterator(context);
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestione apertura della consultazione dei documenti contabili
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doVisualizzaDoccontContSpe(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			SimpleCRUDBP rootbp = (SimpleCRUDBP)getBusinessProcess(context);
			ContrattoBulk contratto = (ContrattoBulk)rootbp.getModel();
			CompoundFindClause clauses = new CompoundFindClause();
			clauses.addClause("AND","tipo",SQLBuilder.EQUALS,"SPE");
			it.cnr.jada.util.action.ConsultazioniBP bp = (it.cnr.jada.util.action.ConsultazioniBP)context.createBusinessProcess("ConsDocContrattoBP");
			if (contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO) ||
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO)||
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO)){
				clauses.addClause("AND","esercizioContratto",SQLBuilder.EQUALS,contratto.getEsercizio());
				clauses.addClause("AND","statoContratto",SQLBuilder.EQUALS,contratto.getStato());
				clauses.addClause("AND","pgContratto",SQLBuilder.EQUALS,contratto.getPg_contratto());
				clauses.addClause("AND","pgManRev",SQLBuilder.ISNOTNULL,null);
				bp.setSearchResultColumnSet("Doccont");
			}else{
				clauses.addClause("AND","esercizioContrattoPadre",SQLBuilder.EQUALS,contratto.getEsercizio());
				clauses.addClause("AND","statoContrattoPadre",SQLBuilder.EQUALS,contratto.getStato());
				clauses.addClause("AND","pgContrattoPadre",SQLBuilder.EQUALS,contratto.getPg_contratto());
				clauses.addClause("AND","pgManRev",SQLBuilder.ISNOTNULL,null);
				bp.setSearchResultColumnSet("Doccont");
			}
			bp.addToBaseclause(clauses);
			bp.openIterator(context);
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}

	}
	public it.cnr.jada.action.Forward doVisualizzaDocammContEtr(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			SimpleCRUDBP rootbp = (SimpleCRUDBP)getBusinessProcess(context);
			ContrattoBulk contratto = (ContrattoBulk)rootbp.getModel();
			CompoundFindClause clauses = new CompoundFindClause();
			clauses.addClause("AND","tipo",SQLBuilder.EQUALS,"ETR");
			it.cnr.jada.util.action.ConsultazioniBP bp = (it.cnr.jada.util.action.ConsultazioniBP)context.createBusinessProcess("ConsDocContrattoBP");
			if (contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO) ||
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO)||
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO)){
				clauses.addClause("AND","esercizioContratto",SQLBuilder.EQUALS,contratto.getEsercizio());
				clauses.addClause("AND","statoContratto",SQLBuilder.EQUALS,contratto.getStato());
				clauses.addClause("AND","pgContratto",SQLBuilder.EQUALS,contratto.getPg_contratto());
				clauses.addClause("AND","pgManRev",SQLBuilder.ISNULL,null);
				clauses.addClause("AND","pgDocAmm",SQLBuilder.ISNOTNULL,null);
				bp.setSearchResultColumnSet("Docamm");
			}else{
				clauses.addClause("AND","esercizioContrattoPadre",SQLBuilder.EQUALS,contratto.getEsercizio());
				clauses.addClause("AND","statoContrattoPadre",SQLBuilder.EQUALS,contratto.getStato());
				clauses.addClause("AND","pgContrattoPadre",SQLBuilder.EQUALS,contratto.getPg_contratto());
				clauses.addClause("AND","pgManRev",SQLBuilder.ISNULL,null);
				clauses.addClause("AND","pgDocAmm",SQLBuilder.ISNOTNULL,null);
				bp.setSearchResultColumnSet("Docamm");
			}

			bp.addToBaseclause(clauses);
			bp.openIterator(context);
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestione apertura della consultazione dei documenti contabili per Commessa e Contratto
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doVisualizzaDocammContSpe(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			SimpleCRUDBP rootbp = (SimpleCRUDBP)getBusinessProcess(context);
			ContrattoBulk contratto = (ContrattoBulk)rootbp.getModel();
			CompoundFindClause clauses = new CompoundFindClause();
			clauses.addClause("AND","tipo",SQLBuilder.EQUALS,"SPE");
			it.cnr.jada.util.action.ConsultazioniBP bp = (it.cnr.jada.util.action.ConsultazioniBP)context.createBusinessProcess("ConsDocContrattoBP");
			if (contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO) ||
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO)||
					contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO)){
				clauses.addClause("AND","esercizioContratto",SQLBuilder.EQUALS,contratto.getEsercizio());
				clauses.addClause("AND","statoContratto",SQLBuilder.EQUALS,contratto.getStato());
				clauses.addClause("AND","pgContratto",SQLBuilder.EQUALS,contratto.getPg_contratto());
				clauses.addClause("AND","pgManRev",SQLBuilder.ISNULL,null);
				clauses.addClause("AND","pgDocAmm",SQLBuilder.ISNOTNULL,null);
				bp.setSearchResultColumnSet("Docamm");
			}else{
				clauses.addClause("AND","esercizioContrattoPadre",SQLBuilder.EQUALS,contratto.getEsercizio());
				clauses.addClause("AND","statoContrattoPadre",SQLBuilder.EQUALS,contratto.getStato());
				clauses.addClause("AND","pgContrattoPadre",SQLBuilder.EQUALS,contratto.getPg_contratto());
				clauses.addClause("AND","pgManRev",SQLBuilder.ISNULL,null);
				clauses.addClause("AND","pgDocAmm",SQLBuilder.ISNOTNULL,null);
				bp.setSearchResultColumnSet("Docamm");
			}

			bp.addToBaseclause(clauses);
			bp.openIterator(context);
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doCaricaDitteInvitate(ActionContext context) {

		try{
			fillModel(context);
			CRUDConfigAnagContrattoBP bp = (CRUDConfigAnagContrattoBP)getBusinessProcess(context);
			it.cnr.jada.action.HttpActionContext httpContext = (it.cnr.jada.action.HttpActionContext)context;

			UploadedFile file =httpContext.getMultipartParameter("file");

			if (file == null || file.getName().equals("")){
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare un File da caricare.");
			}
			if(file.getFile().getAbsolutePath().endsWith(".xls"))
				bp.caricaDitteInvitate(context,file.getFile());
			else
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: estensione File da caricare errata.");
			return context.findDefaultForward();
		}
		catch(Throwable ex){
			return handleException(context, ex);
		}
	}

	/**
	 * Metodo utilizzato per gestire la conferma dell'inserimento/modifica di un importo che sfonda
	 * il totale accertamenti associati
	 *
	 * @param context <code>ActionContext</code> in uso.
	 * @param option  Esito della risposta alla richiesta di sfondamento
	 * @return <code>Forward</code>
	 * @throws <code>RemoteException</code>
	 */

	public Forward doOnCheckDisponibilitaContrattoFailed(ActionContext context, int option) {
		if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) {
			try {
				CRUDBP bp = getBusinessProcess(context);
				((ContrattoBulk) bp.getModel()).setCheckDisponibilitaContrattoEseguito(true);
				if (bp.isBringBack())
					return doConfermaRiporta(context, option);
				else
					doSalva(context);
				((ContrattoBulk) bp.getModel()).setCheckDisponibilitaContrattoEseguito(false);
			} catch (Throwable e) {
				return handleException(context, e);
			}
		}
		return context.findDefaultForward();
	}
	public Forward doBlankSearchFindBeneServizio(ActionContext context, Dettaglio_contrattoBulk dettaglio) throws java.rmi.RemoteException {

		try {
			//imposta i valori di default per il tariffario
			dettaglio.setBeneServizio(new Bene_servizioBulk());
			dettaglio.setUnitaMisura(null);
			dettaglio.setCoefConv(null);
			return context.findDefaultForward();

		} catch (Exception e) {
			return handleException(context, e);
		}
	}

	public Forward doBringBackSearchFindBeneServizio(ActionContext context,
													 Dettaglio_contrattoBulk dettaglio,
													 Bene_servizioBulk bene)
			throws java.rmi.RemoteException {

		dettaglio.setBeneServizio(bene);
		((CRUDBP) context.getBusinessProcess()).setDirty(true);
		if (bene != null) {
			dettaglio.setCdBeneServizio(bene.getCd_bene_servizio());
			if (bene.getUnitaMisura() != null) {
				dettaglio.setUnitaMisura(bene.getUnitaMisura());
				dettaglio.setCoefConv(BigDecimal.ONE);
			}

		}
		return context.findDefaultForward();
	}
	public Forward doBringBackSearchFindUnitaMisura(ActionContext context,
													Dettaglio_contrattoBulk dettaglio,
													UnitaMisuraBulk unitaMisura)
			throws java.rmi.RemoteException {

		dettaglio.setUnitaMisura(unitaMisura);
		((CRUDBP) context.getBusinessProcess()).setDirty(true);

		try {
			if (unitaMisura != null && dettaglio.getBeneServizio() != null && dettaglio.getBeneServizio().getUnitaMisura() != null && unitaMisura.getCdUnitaMisura().equals(dettaglio.getBeneServizio().getUnitaMisura().getCdUnitaMisura())) {
				dettaglio.setCoefConv(BigDecimal.ONE);
			} else {
				dettaglio.setCoefConv(null);
			}
			return context.findDefaultForward();

		} catch (Exception e) {
			return handleException(context, e);
		}
	}




}
