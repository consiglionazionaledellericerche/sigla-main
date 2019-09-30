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

package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.bp.CRUDBonusBP;
import it.cnr.contab.compensi00.bp.CRUDCompensoBP;
import it.cnr.contab.compensi00.docs.bulk.BonusBulk;
import it.cnr.contab.compensi00.docs.bulk.Bonus_nucleo_famBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.OptionBP;

public class CRUDBonusAction extends it.cnr.jada.util.action.CRUDAction {

	public CRUDBonusAction() {
		super();
	}
	public Forward doOnCodice_fiscaleChange(ActionContext context) {
	    try {
	        
	        fillModel(context);
	        CRUDBonusBP bp = (CRUDBonusBP)getBusinessProcess(context);
	    	BonusBulk bonus=(BonusBulk)bp.getModel();
	    	try{
			bp.validaCodiceFiscale(context,bonus);
	        } catch (ApplicationException e) {
				bonus.setCodice_fiscale(null);
				bp.setMessage(e.getMessage());
			}
		} catch (Throwable t) {
		    return handleException(context, t);
		}
	return context.findDefaultForward();
	}
	public Forward doOnImRedditoChange(ActionContext context) {
	    try {
	        fillModel(context);
	        CRUDBonusBP bp = (CRUDBonusBP)getBusinessProcess(context);
	        bp.eseguiCalcoloTot(context,(BonusBulk)bp.getModel());
		} catch (Throwable t) { 
		    return handleException(context, t);
		}
		return context.findDefaultForward();
	}
	public Forward doOnIm_reddito_componenteChange(ActionContext context) {
	    try {
	    	CRUDBonusBP bp = (CRUDBonusBP)getBusinessProcess(context);
	    	java.math.BigDecimal red_comp=((Bonus_nucleo_famBulk)bp.getCrudBonusNucleoFamBP().getModel()).getIm_reddito_componente();
	        fillModel(context); 
	       
	        if(!bp.createComponentSession().verificaLimiteFamiliareCarico(context.getUserContext(), (Bonus_nucleo_famBulk)bp.getCrudBonusNucleoFamBP().getModel()))
	        	{
	        	Bonus_nucleo_famBulk det=(Bonus_nucleo_famBulk)bp.getCrudBonusNucleoFamBP().getModel();
	        	det.setFl_handicap(false);
	        	if(det.getTipo_componente_nucleo()!=null && det.getTipo_componente_nucleo().compareTo(det.CONIUGE)!=0){
	        		det.setIm_reddito_componente(red_comp);
	        		bp.setMessage("Il componente non è a carico");
	        	}
	       }
	        bp.eseguiCalcoloTot(context,(BonusBulk)bp.getModel());
		} catch (Throwable t) { 
		    return handleException(context, t);
		}
		return context.findDefaultForward();
	}
	public Forward doOnCf_componente_nucleoChange(ActionContext context) {
	    try {
	        fillModel(context);
	        CRUDBonusBP bp = (CRUDBonusBP)getBusinessProcess(context);
        	Bonus_nucleo_famBulk det=(Bonus_nucleo_famBulk)bp.getCrudBonusNucleoFamBP().getModel();
	        try {
	        	bp.validaCodiceFiscaleComponente(context,det);
			} catch (ApplicationException e) {
				det.setCf_componente_nucleo(null);
				bp.setMessage(e.getMessage());
			}
		} catch (Throwable t) {
			return handleException(context, t);
		}
	return context.findDefaultForward();
	}
	public Forward doRemoveFromCRUDMain_dettagliCRUDController(ActionContext context){
		try{
			CRUDBonusBP bp =(CRUDBonusBP)getBusinessProcess(context);
			getController(context,"main.dettagliCRUDController").remove(context);
			BonusBulk bonus = (BonusBulk)bp.getModel();
			if (bonus!=null)
				bp.eseguiCalcoloTot(context, bonus);
		} catch(Throwable e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}
	public Forward doAddToCRUDMain_dettagliCRUDController(ActionContext context){
		try {
			CRUDBonusBP bp =(CRUDBonusBP)getBusinessProcess(context);
			BonusBulk bonus = (BonusBulk)bp.getModel();
			bp.validataTestata(context,bonus);		
			getController(context,"main.dettagliCRUDController").add(context);
		} catch(Throwable e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}
	public Forward doCompletaBonus(ActionContext context){
		
		try {
			fillModel(context);
			CRUDBonusBP bp =(CRUDBonusBP)getBusinessProcess(context);
			
			BonusBulk bonus = (BonusBulk)bp.getModel();
			//if (bonus!=null && bonus.getCodice_fiscale()==null )
				//	throw new ApplicationException("Inserire prima codice fiscale richiedente");
			bp.validataTestata(context, bonus);
			if (bonus.getBonusNucleoFamColl()!= null && bonus.getBonusNucleoFamColl().size()>0){ 
				getController(context,"main.dettagliCRUDController").validate(context);
			}
			try {
				bp.setModel(context,(BonusBulk)bp.completaBonus(context,(BonusBulk)bp.getModel()));
			}catch (ApplicationException e) {
				bonus.setIm_bonus(null);
				bonus.setBonus_condizioni(null);
				bp.setModel(context,bonus);
				return handleException(context,e);
			}				
		} catch(Throwable e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}
	public Forward doSalva(ActionContext context){
		try {
			fillModel(context);
			CRUDBonusBP bp = (CRUDBonusBP)getBusinessProcess(context);
			BonusBulk bonus = (BonusBulk)bp.getModel();
			try{
				bp.validataTestata(context,bonus);
				getController(context,"main.dettagliCRUDController").validate(context);
				bp.setModel(context,(BonusBulk)bp.completaBonus(context,(BonusBulk)bp.getModel()));
			}catch (ApplicationException e) {
				bonus.setIm_bonus(null);
				bonus.setBonus_condizioni(null);
				bp.setModel(context,bonus);
				return handleException(context,e);
			  }
			catch (ValidationException e) {
				bonus.setIm_bonus(null);
				bonus.setBonus_condizioni(null);
				bp.setModel(context,bonus);
				return handleException(context,e);
			  }
			return super.doSalva(context);
		}catch(Throwable e){ 
			return handleException(context,e);
		}
		

	}
	public CRUDCompensoBP creaCompensoBP(ActionContext context, boolean setSafePoint) throws BusinessProcessException {
		CRUDCompensoBP compensoBP = (CRUDCompensoBP)context.getUserInfo().createBusinessProcess(
				context,
				"CRUDCompensoBP",
				new Object[] { "MRSWTh" }
			);
		if (setSafePoint)
			compensoBP.setSavePoint(context, CRUDBonusBP.SAVE_POINT_NAME);
		return compensoBP;
	}
	
	
	public Forward doCreaCompenso(ActionContext context) {

		try	{
			fillModel(context);
			CRUDBonusBP bp = (CRUDBonusBP)getBusinessProcess(context);
			BonusBulk bonus = (BonusBulk)bp.getModel();
			context.addHookForward("bringback",this,"doBringBackCompenso");
			CRUDCompensoBP compensoBP = creaCompensoBP(context, true);
			try {
				compensoBP.reset(context);
				CompensoBulk compenso = (CompensoBulk)compensoBP.getModel();
				compenso.setIm_lordo_percipiente(new java.math.BigDecimal(0));
		
				it.cnr.contab.compensi00.ejb.CompensoComponentSession component = (it.cnr.contab.compensi00.ejb.CompensoComponentSession)bp.createComponentSession("CNRCOMPENSI00_EJB_CompensoComponentSession",it.cnr.contab.compensi00.ejb.CompensoComponentSession.class );
				compenso = component.inizializzaCompensoPerBonus(
												context.getUserContext(), 
												compenso, 
												bonus);
				compensoBP.setModel(context, compenso);
			} catch (Throwable t) {
				compensoBP.rollbackToSavePoint(context, bp.SAVE_POINT_NAME);
				throw t;
			}
				
			return context.addBusinessProcess(compensoBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doVisualizzaCompenso(ActionContext context) throws BusinessProcessException {
		try	{
			fillModel(context);
			CRUDBonusBP bp = (CRUDBonusBP)getBusinessProcess(context);
			BonusBulk bonus = (BonusBulk)bp.getModel();
			
			if(!bonus.isModificabile()){
				context.addHookForward("bringback",this,"doBringBackVisualizzaCompenso");
	
				CRUDCompensoBP compensoBP = (CRUDCompensoBP)context.createBusinessProcess(
													"CRUDCompensoBP",
													new Object[] { "VRSWTh" });
				compensoBP.setSavePoint(context, bp.SAVE_POINT_NAME);
				try {
					it.cnr.contab.compensi00.ejb.BonusComponentSession component = (it.cnr.contab.compensi00.ejb.BonusComponentSession)bp.createComponentSession("CNRCOMPENSI00_EJB_BonusComponentSession",it.cnr.contab.compensi00.ejb.BonusComponentSession.class );
					CompensoBulk compenso = component.cercaCompensoPerBonus(context.getUserContext(),bonus);
					compensoBP.edit(context, compenso);
					((CompensoBulk)compensoBP.getModel()).setBonus(bonus);
				} catch (Throwable t) {
					compensoBP.rollbackToSavePoint(context, bp.SAVE_POINT_NAME);
					throw t;
				}
				return context.addBusinessProcess(compensoBP);
			}else{
				return context.findDefaultForward();
			}
				
			} catch(Throwable e) {
				return handleException(context,e);
			}
		
	}
	/**
	 * Creo un nuovo compenso da associare alle rate selezionate e lo apro in modalità 
	 * inserimento. Viene validata la selezione con il metodo 'validaSelezionePerAssociazioneCompenso'
	 */

	public Forward doBringBackVisualizzaCompenso(ActionContext context) {

		try	{
			creaCompensoBP(context, false).rollbackToSavePoint(
														context, 
														CRUDBonusBP.SAVE_POINT_NAME);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doBringBackCompenso(ActionContext context) {

		HookForward caller = (HookForward)context.getCaller();
		CompensoBulk compenso = (CompensoBulk)caller.getParameter("bringback");
		
		CRUDBonusBP bp = (CRUDBonusBP)getBusinessProcess(context);
		if(compenso == null) {
			try {
				creaCompensoBP(context, false).rollbackToSavePoint(context, bp.SAVE_POINT_NAME);
			} catch (BusinessProcessException e) {
				return handleException(context, e);
			}
			return context.findDefaultForward();
		}else{
			((BonusBulk)bp.getModel()).setModificabile(false);
			bp.setDirty(true);
		}
		return context.findDefaultForward();
	}
	public Forward doOnFlHandicapChange(ActionContext context){
		try {
			fillModel(context);
			CRUDBonusBP bp = (CRUDBonusBP)getBusinessProcess(context);
			bp.setDirty(true);
		}catch (Throwable ex) {
			return handleException(context, ex);
		}
		return context.findDefaultForward();
	}
	public Forward doOnTipoComponenteNucleoChange(ActionContext context){
		try {
			CRUDBonusBP bp = (CRUDBonusBP)getBusinessProcess(context);
			Bonus_nucleo_famBulk det =(Bonus_nucleo_famBulk)bp.getCrudBonusNucleoFamBP().getModel();
	    	String tipoComp=det.getTipo_componente_nucleo();
			fillModel(context);
			if (!bp.createComponentSession().verificaLimiteFamiliareCarico(context.getUserContext(), det) && 
					det.getTipo_componente_nucleo()!=null && det.getTipo_componente_nucleo().compareTo(Bonus_nucleo_famBulk.CONIUGE)!=0){
				det.setTipo_componente_nucleo(tipoComp);
				bp.setMessage("Il componente non è a carico");
			}
			bp.setDirty(true);
		}catch (Throwable ex) {
			return handleException(context, ex);
		}
		return context.findDefaultForward();
	}
	public Forward doEstrai(ActionContext context) {
		try {
			CRUDBP bp = getBusinessProcess(context);
			fillModel(context);
			if (bp.isDirty())
				return openContinuePrompt(context,"doConfermaEstrazione");
			return doConfermaEstrazione(context,OptionBP.YES_BUTTON);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doConfermaEstrazione(ActionContext context,int option) {
		try {
			if (option == OptionBP.YES_BUTTON) {
				CRUDBonusBP bp = (CRUDBonusBP)getBusinessProcess(context);
				bp.Estrazione(context);
			}
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}		
	public Forward doConfermaInvio(ActionContext context) {
		try {
			
			CRUDBonusBP bp = (CRUDBonusBP)getBusinessProcess(context);
			bp.confermaInvio(context);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}		
}
