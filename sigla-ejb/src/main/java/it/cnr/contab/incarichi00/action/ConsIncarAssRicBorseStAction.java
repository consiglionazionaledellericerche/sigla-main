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

package it.cnr.contab.incarichi00.action;
import it.cnr.contab.incarichi00.bp.ConsIncarAssRicBorseStBP;
import it.cnr.contab.incarichi00.bulk.VIncarichiAssRicBorseStBulk;
import it.cnr.contab.incarichi00.ejb.ConsIncarAssRicBorseStComponentSession;
import it.cnr.contab.preventvar00.consultazioni.bp.ConsVarStanzCompetenzaBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.BulkAction;
import it.cnr.jada.util.action.SelezionatoreListaBP;

import java.rmi.RemoteException;

import javax.ejb.RemoveException;

/**
 * Action di gestione delle variazioni di bilancio preventivo
 */

public class ConsIncarAssRicBorseStAction extends BulkAction {
public ConsIncarAssRicBorseStAction() {
	super();
}



//public Forward doCambiaGestione(ActionContext context){
//	try {
//		fillModel(context);
//		ConsVarStanzCompetenzaBP bp = (ConsVarStanzCompetenzaBP)context.getBusinessProcess();
//	
//		VIncarichiAssRicBorseStBulk bulk = (VIncarichiAssRicBorseStBulk)bp.getModel();
//			bp.getParametriLivelli(context);
//		
//		return context.findDefaultForward();
//	} catch(Throwable e) {
//		return handleException(context,e);
//	}
//}
//
//	

public Forward doSeleziona(ActionContext context) {
	try{
		fillModel(context);
		ConsIncarAssRicBorseStBP bp = (ConsIncarAssRicBorseStBP)context.getBusinessProcess();
		VIncarichiAssRicBorseStBulk bulk = (VIncarichiAssRicBorseStBulk)bp.getModel();
//		bulk.selezionaRagruppamenti();
		return context.findDefaultForward();
	}catch(it.cnr.jada.bulk.FillException ex){
		return handleException(context, ex);
	}
}
public Forward doCerca(ActionContext context) throws RemoteException, InstantiationException, RemoveException{
	ConsIncarAssRicBorseStBP bp= (ConsIncarAssRicBorseStBP) context.getBusinessProcess();
	try {
		VIncarichiAssRicBorseStBulk incarichi = (VIncarichiAssRicBorseStBulk)bp.getModel();
		bp.fillModel(context); 
		bp.controlloSelezioni(context,incarichi);
//		bp.valorizzaTotVariazione(context,variazioni);
//		bp.valorizzaTi_Gestione(context,incarichi);
//		bp.valorizzaCd_livello1(context,variazioni);
		
	
			it.cnr.jada.util.RemoteIterator ri = ((ConsIncarAssRicBorseStComponentSession)bp.createComponentSession()).findIncarichi(context.getUserContext(),incarichi);
			ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
			if (ri.countElements() == 0) {
					it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: Nessun dato disponibile.");
			}
			SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
			nbp.setIterator(context,ri);
			nbp.disableSelection();
			nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(VIncarichiAssRicBorseStBulk.class)); 
			HookForward hook = (HookForward)context.findForward("seleziona"); 
			return context.addBusinessProcess(nbp); 
				
		
	} catch (Exception e) {
			return handleException(context,e); 
	}
	
}
public Forward doCloseForm(ActionContext actioncontext)
		throws BusinessProcessException
	{
		try
		{
				return doConfirmCloseForm(actioncontext, 4);
		}
		catch(Throwable throwable)
		{
			return handleException(actioncontext, throwable);
		}
	}


}