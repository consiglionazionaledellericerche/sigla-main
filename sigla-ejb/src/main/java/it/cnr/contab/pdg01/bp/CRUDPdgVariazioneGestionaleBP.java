package it.cnr.contab.pdg01.bp;

import java.rmi.RemoteException;

import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.pdg01.ejb.CRUDPdgVariazioneGestionaleComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.ejb.VariazioniStanziamentoResiduoComponentSession;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.jsp.Button;

/**
 * Business Process per la gestione della testata delle variazioni al PDG
 */

public class CRUDPdgVariazioneGestionaleBP extends it.cnr.contab.pdg00.bp.PdGVariazioneBP {
	public CRUDPdgVariazioneGestionaleBP() {
		super();
	}

	public CRUDPdgVariazioneGestionaleBP(String function) {
		super(function);
	}

	protected void validaAccessoBP(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.action.BusinessProcessException {
		try	{
			if(!isUoEnte() && !isUoArea()) {
				PdGVariazioniComponentSession comp = (PdGVariazioniComponentSession)createComponentSession();
				comp.controllaBilancioPreventivoCdsApprovato(context.getUserContext(), ((CNRUserInfo)context.getUserInfo()).getCdr());
			}
		}
		catch(Throwable e){
			throw handleException(e);
		}		
	}

	public void approva(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		try {
			CRUDPdgVariazioneGestionaleComponentSession comp = (CRUDPdgVariazioneGestionaleComponentSession)createComponentSession();
			Pdg_variazioneBulk varPdg = (Pdg_variazioneBulk)comp.approva(context.getUserContext(), (Pdg_variazioneBulk)getModel());
			varPdg = (Pdg_variazioneBulk)comp.esitaVariazioneBilancio(context.getUserContext(), varPdg);
			edit(context,varPdg);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	
	protected Button[] createToolbar() {
		Button[] toolbar = super.createToolbar();
		Button[] newToolbar = new Button[ toolbar.length + 3];
		int i;
		for ( i = 0; i < toolbar.length; i++ )
			newToolbar[i] = toolbar[i];
		newToolbar[ i ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.statoPrecedente");
		newToolbar[ i+1 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.assestatoEntrate");
		newToolbar[ i+2 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.assestatoSpese");
		return newToolbar;
	}

	private boolean isAssestatoButtonHidden() {
		/*
		 * P.R. Temporaneamente disabilitato
		 */
		return true;
		/*
		Pdg_variazioneBulk pdg = (Pdg_variazioneBulk)getModel();
		if (!(isEditable() && isEditing() && !isDirty()))
		   return true;
		if (pdg == null)
		  return true;
		if (pdg.getTipologia() == null)
		  return true;
		if (!isEditableDettagliVariazione())
		  return true;
		return false;
		*/
	}

	protected String tipoVariazione(UserContext userContext, Pdg_variazioneBulk pdg) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.comp.ComponentException {
		return pdg.getTipologia();
	}

	public boolean isAssestatoEntrateButtonHidden() {
		return isAssestatoButtonHidden() || 
			   !((Pdg_variazioneBulk)getModel()).isGestioneEntrateEnable();
	}
	public boolean isAssestatoSpeseButtonHidden() {
		return isAssestatoButtonHidden() || 
			   !((Pdg_variazioneBulk)getModel()).isGestioneSpeseEnable();
	}
}