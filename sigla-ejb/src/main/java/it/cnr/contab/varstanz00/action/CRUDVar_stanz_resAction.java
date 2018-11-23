/*
 * Created on Feb 16, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.varstanz00.action;

import java.math.BigDecimal;

import it.cnr.contab.anagraf00.bp.CRUDAbiCabBP;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.doccont00.bp.CRUDAccertamentoBP;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.pdg00.bp.PdGVariazioneBP;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.varstanz00.bp.CRUDVar_stanz_resBP;
import it.cnr.contab.varstanz00.bp.CRUDVar_stanz_resRigaBP;
import it.cnr.contab.varstanz00.bp.SelezionatoreAssestatoResiduoBP;
import it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.CRUDBP;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDVar_stanz_resAction extends CRUDAction {

	/**
	 * 
	 */
	public CRUDVar_stanz_resAction() {
		super();
	}
	/**
	 * Gestione della richiesta di salvataggio di una variazione come definitiva
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doSalvaDefinitivo(ActionContext context) {

		try {
			fillModel(context);
			CRUDVar_stanz_resBP bp = (CRUDVar_stanz_resBP)getBusinessProcess(context);
			bp.salvaDefinitivo(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
	/**
	 * Gestione della richiesta di approvazione una variazione definitiva
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doApprova(ActionContext context) {

		try {
			fillModel(context);
			CRUDVar_stanz_resBP bp = (CRUDVar_stanz_resBP)getBusinessProcess(context);
			bp.approva(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}

	}
	public Forward doStatoPrecedente(ActionContext context) {

		try {
			fillModel(context);
			CRUDVar_stanz_resBP bp = (CRUDVar_stanz_resBP)getBusinessProcess(context);
			bp.statoPrecedente(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}

	}
	/**
	 * Gestione della richiesta di respingere una variazione definitiva
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doRespingi(ActionContext context) {

		try {
			fillModel(context);
			CRUDVar_stanz_resBP bp = (CRUDVar_stanz_resBP)getBusinessProcess(context);
			bp.respingi(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
	/**
	 * Gestisce un comando di cancellazione.
	 */
	public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {

		try {
			fillModel(context);
			CRUDVar_stanz_resBP bp = (CRUDVar_stanz_resBP)getBusinessProcess(context);
			if (!bp.isEditing()) {
				bp.setMessage("Non è possibile cancellare in questo momento");
			} else {
				bp.delete(context);
				bp.edit(context, bp.getModel());
				bp.setMessage("Annullamento effettuato");
			}
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}		
	public it.cnr.jada.action.Forward doDettagliSpe(it.cnr.jada.action.ActionContext context,int option) throws BusinessProcessException {
		if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON){
			CRUDVar_stanz_resBP testataBP = (CRUDVar_stanz_resBP)getBusinessProcess(context);
			Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)testataBP.getModel();
			testataBP.edit(context, var_stanz_res);
			return doDettagliSpe(context,false);			
		}
		return context.findDefaultForward();	
	}			
	public it.cnr.jada.action.Forward doDettagliSpe(it.cnr.jada.action.ActionContext context) {
		return doDettagliSpe(context,true);
	}	

	public Forward doOnChangeTipologiaFin(ActionContext context) {
		CRUDVar_stanz_resBP testataBP = (CRUDVar_stanz_resBP)getBusinessProcess(context);
		Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)testataBP.getModel();
		String tipologiaFin = var_stanz_res.getTipologia_fin();
		try {
			fillModel(context);
			testataBP.validaOrigineFontiPerAnnoResiduo(context);
			return context.findDefaultForward();
		}catch(Throwable ex){
			var_stanz_res.setTipologia_fin(tipologiaFin);
			return handleException(context, ex);
		}			
	}	
	
	/**
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doDettagliSpe(it.cnr.jada.action.ActionContext context,boolean flag) {
		try {
			fillModel(context);
			CRUDVar_stanz_resBP testataBP = (CRUDVar_stanz_resBP)getBusinessProcess(context);
			if(testataBP.isDirty() && flag)
				return openContinuePrompt(context, "doDettagliSpe");
			Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)testataBP.getModel();
			String status = testataBP.isEditing()?"M":"V";
			Ass_var_stanz_res_cdrBulk ass_var_stanz_res_cdr = (Ass_var_stanz_res_cdrBulk)(var_stanz_res.getAssociazioneCDR().get(testataBP.getCrudAssCDR().getSelection().getFocus()));
			boolean isCdrDiScrivania = ass_var_stanz_res_cdr.getCentro_di_responsabilita().equalsByPrimaryKey(testataBP.getCentro_responsabilita_scrivania());
			boolean tipoView =  var_stanz_res.getTipologia().equalsIgnoreCase(Var_stanz_resBulk.TIPOLOGIA_ECO)?
			  testataBP.getCentro_responsabilita_scrivania().getLivello().intValue() == 1? 
			    !ass_var_stanz_res_cdr.getCentro_di_responsabilita().getCd_cds().equalsIgnoreCase(testataBP.getCentro_responsabilita_scrivania().getCd_cds())
			  :!isCdrDiScrivania			
			:!isCdrDiScrivania;
			if (tipoView){
				if (isVariazioneFromLiquidazioneIvaDaModificare(context, isCdrDiScrivania, testataBP, var_stanz_res, ass_var_stanz_res_cdr)){
					tipoView = false;
				}
			}
			String trStatus = "";
			if (testataBP.isDaAccertamentoModifica())
				trStatus = "RSWTh";
			CRUDVar_stanz_resRigaBP bp = (CRUDVar_stanz_resRigaBP)context.createBusinessProcess("CRUDVar_stanz_resRigaBP", 
			   new Object[]{(testataBP.getStatus() == testataBP.VIEW || 
				            tipoView ? "V": "M") + trStatus,
			   	            var_stanz_res,
			   	            ass_var_stanz_res_cdr.getCentro_di_responsabilita()});
			context.addHookForward("close",this,"doBringBackOpenDettagliWindow");
			context.addHookForward("bringback",this,"doBringBackDettagliWindow");
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}	

	private boolean isVariazioneFromLiquidazioneIvaDaModificare(
			it.cnr.jada.action.ActionContext context,boolean isCdrDiScrivania, 
			CRUDVar_stanz_resBP bp, Var_stanz_resBulk pdg_variazione,
			Ass_var_stanz_res_cdrBulk ass_pdg_variazione)
			throws BusinessProcessException {
		boolean variazioneFromLiquidazioneIva = false;
		if (ass_pdg_variazione.getCentro_di_responsabilita().getCd_cds().equalsIgnoreCase(bp.getCentro_responsabilita_scrivania().getCd_cds()) && isCdrDiScrivania){
			variazioneFromLiquidazioneIva = bp.isVariazioneFromLiquidazioneIvaDaModificare(context, pdg_variazione);
		}
		return variazioneFromLiquidazioneIva;
	}	

	public Forward doBringBackDettagliWindow(ActionContext context) {
		try {
			CRUDVar_stanz_resBP testataBP = (CRUDVar_stanz_resBP)getBusinessProcess(context);
			Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)testataBP.getModel();
			testataBP.edit(context, var_stanz_res);
			testataBP.setTab("tab","tabCDR");
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public it.cnr.jada.action.Forward doAssestatoResiduo(it.cnr.jada.action.ActionContext context,int option) throws BusinessProcessException {
		if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON){
			CRUDVar_stanz_resBP testataBP = (CRUDVar_stanz_resBP)getBusinessProcess(context);
			Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)testataBP.getModel();
			testataBP.edit(context, var_stanz_res);
			return doAssestatoResiduo(context,false);			
		}
		return context.findDefaultForward();	
	}				
	public Forward doAssestatoResiduo(ActionContext context) throws BusinessProcessException {
		return doAssestatoResiduo(context,true);
	}	
	public Forward doAssestatoResiduo(ActionContext context,boolean flag) throws BusinessProcessException {
		try {
			fillModel(context);
			CRUDVar_stanz_resBP testataBP = (CRUDVar_stanz_resBP)getBusinessProcess(context);
			Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)testataBP.getModel();
			if(testataBP.isDirty() && flag)
				return openContinuePrompt(context, "doAssestatoResiduo");
			if (testataBP.getCrudAssCDR().getSelection().getFocus() !=-1){
				Ass_var_stanz_res_cdrBulk ass_var_stanz_res = (Ass_var_stanz_res_cdrBulk)testataBP.getCrudAssCDR().getModel();
				if (ass_var_stanz_res != null ){
					var_stanz_res.setCdr(ass_var_stanz_res.getCentro_di_responsabilita());
				}			
			}else{
				var_stanz_res.setCdr(null);
			}
			String trStatus = "";
			if (testataBP.isDaAccertamentoModifica())
				trStatus = "RSWTh";
			SelezionatoreAssestatoResiduoBP bp = (SelezionatoreAssestatoResiduoBP)context.createBusinessProcess("SelezionatoreAssestatoResiduoBP",
												new Object[]{ 
												"M" + trStatus,
												var_stanz_res});
			context.addHookForward("seleziona",this,"doBringBackOpenDettagliWindow");		
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
		
	}	
	public Forward doBringBackOpenDettagliWindow(ActionContext context) {
		try {
			fillModel(context);
			CRUDVar_stanz_resBP testataBP = (CRUDVar_stanz_resBP)getBusinessProcess(context);
			Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)testataBP.getModel();
			testataBP.edit(context, var_stanz_res);
			testataBP.setTab("tab","tabCDR");
			return context.findDefaultForward();
		}catch(java.lang.ClassCastException ex){
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}			
	}	
	public Forward doValidaSpesa(ActionContext context) 
	{
		CRUDVar_stanz_resBP testataBP = (CRUDVar_stanz_resBP)getBusinessProcess(context);
		Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)testataBP.getModel();
		Ass_var_stanz_res_cdrBulk ass_var_stanz_res = (Ass_var_stanz_res_cdrBulk)testataBP.getCrudAssCDR().getModel();
		
		try
		{	// per ora non faccio nulla, tranne il post collegato
			// serve per "forzare" la disabilitazione del tasto
			// salvaDefinitivo prima di effettuare il salvataggio normale
			BigDecimal spesaVecchia = ass_var_stanz_res.getIm_spesa();
				
			fillModel(context);

			BigDecimal spesa = ass_var_stanz_res.getIm_spesa();
			
			return context.findDefaultForward();
		}	
		catch(Throwable e) 
		{
			return handleException(context,e);
		}	
	}

	public Forward doOnChangeMapMotivazioneVariazione(ActionContext context) {
		try {
			fillModel(context);
			((CRUDVar_stanz_resBP)getBusinessProcess(context)).aggiornaMotivazioneVariazione(context);
			return context.findDefaultForward();
		}catch(java.lang.ClassCastException ex){
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}			
	}
	public Forward doOnChangeTipologia(ActionContext context) {
		CRUDVar_stanz_resBP testataBP = (CRUDVar_stanz_resBP)getBusinessProcess(context);
		Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)testataBP.getModel();
		try{
			fillModel(context);
			if (!(Var_stanz_resBulk.TIPOLOGIA_STO.equals(var_stanz_res.getTipologia())||Var_stanz_resBulk.TIPOLOGIA_STO_INT.equals(var_stanz_res.getTipologia())))
			   var_stanz_res.setDs_causale(null);
			return context.findDefaultForward();
		}catch(Throwable e){ 
			return handleException(context,e);
		}		
	}
}