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
 * Created on Sep 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.action;

import java.rmi.RemoteException;
import java.util.Iterator;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.prevent01.bp.CRUDDettagliModuloCostiBP;
import it.cnr.contab.prevent01.bp.CRUDPdg_Modulo_EntrateBP;
import it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateBulk;
import it.cnr.contab.prevent01.ejb.PdgModuloEntrateComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.OptionBP;

/**
 * @author rpucciarelli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDPdg_Modulo_EntrateAction extends CRUDAction {
	public CRUDPdg_Modulo_EntrateAction() {
			super();
		}
	
public Forward doBringBackSearchLinea_attivita(ActionContext context, Pdg_Modulo_EntrateBulk dettaglio, WorkpackageBulk linea) throws RemoteException, BusinessProcessException, ValidationException {
	try {

			CRUDPdg_Modulo_EntrateBP bp = (CRUDPdg_Modulo_EntrateBP) context.getBusinessProcess();
			
			if (linea != null) {
				dettaglio.setLinea_attivita(linea);
			}
			return context.findDefaultForward();
		} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doBlankSearchLinea_attivita(ActionContext context, Pdg_Modulo_EntrateBulk dettaglio) throws java.rmi.RemoteException {	
		try {
			dettaglio.setLinea_attivita(new WorkpackageBulk());
			return context.findDefaultForward();		
			} catch(Exception e) {
				return handleException(context,e);
		}
}
	public Forward doBringBackSearchFind_classificazione_voci(ActionContext context, Pdg_Modulo_EntrateBulk dettaglio, V_classificazione_vociBulk clas) {
	try {
		fillModel(context);		
		if (clas != null) {
			dettaglio.setClassificazione_voci(clas);
		}
		java.util.Collection natura = ((PdgModuloEntrateComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPREVENT01_EJB_Pdg_Modulo_EntrateComponentSession",it.cnr.contab.prevent01.ejb.PdgModuloEntrateComponentSession.class)).findNatura(context.getUserContext(), dettaglio);
																															 														
        dettaglio.setNature(natura);
		return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doBringBackSearchFind_classificazione_voci_codlast(ActionContext context, Pdg_Modulo_EntrateBulk dettaglio, V_classificazione_vociBulk clas) {
	try {
		fillModel(context);		
		if (clas != null) {
			dettaglio.setClassificazione_voci(clas);
		}
		java.util.Collection natura = ((PdgModuloEntrateComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPREVENT01_EJB_Pdg_Modulo_EntrateComponentSession",it.cnr.contab.prevent01.ejb.PdgModuloEntrateComponentSession.class)).findNatura(context.getUserContext(), dettaglio);
																															 														
        dettaglio.setNature(natura);
		return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doSalva(ActionContext actioncontext){
		try {
			fillModel(actioncontext);
			
			CRUDPdg_Modulo_EntrateBP bp = (CRUDPdg_Modulo_EntrateBP)getBusinessProcess(actioncontext);
			if ((Pdg_Modulo_EntrateBulk)bp.getCrudDettagliEntrate().getModel() != null)
			((Pdg_Modulo_EntrateBulk)bp.getCrudDettagliEntrate().getModel()).validate(actioncontext.getUserContext());
			
			return super.doSalva(actioncontext);
		}
		catch(ValidationException e) 
		{
			getBusinessProcess(actioncontext).setErrorMessage(e.getMessage());
			return actioncontext.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(actioncontext,e);
		}
		
	}
	public Forward doConfermaEliminamodulo(ActionContext context, int option)
		{
			try
			{
				CRUDPdg_Modulo_EntrateBP bp = (CRUDPdg_Modulo_EntrateBP)context.getBusinessProcess();		
				if (option == OptionBP.YES_BUTTON){ 
					fillModel(context);
					doRemoveAllFromCRUD(context, "main.dettagliCRUDController");
				}			
				return context.findDefaultForward();
			} catch(Throwable e) {
				return handleException(context,e);
			}
	
		}	
		public Forward doEliminamodulo(ActionContext actioncontext) throws BusinessProcessException {
			return openConfirm(actioncontext,"Tutti i dettagli di entrata relativi al modulo verranno cancellati definitivamente. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfermaEliminamodulo");		
		}
		public Forward doEliminaProgetto(ActionContext actioncontext) throws BusinessProcessException {
			return openConfirm(actioncontext,"Tutti i dettagli di entrata relativi al progetto verranno cancellati definitivamente. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfermaEliminamodulo");		
		}
}