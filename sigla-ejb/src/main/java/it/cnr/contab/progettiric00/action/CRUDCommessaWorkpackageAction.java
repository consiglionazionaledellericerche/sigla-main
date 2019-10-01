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
 * Created on Oct 10, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.progettiric00.action;

import java.rmi.RemoteException;

import javax.ejb.RemoveException;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.progettiric00.bp.CommessaWorkpackageBP;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.OptionBP;

public class CRUDCommessaWorkpackageAction extends it.cnr.jada.util.action.CRUDAction {

	
	/**
	 * CRUDAnagraficaAction constructor comment.
	 */
	public CRUDCommessaWorkpackageAction() {
		super();
	}

	public Forward doBlankSearchFind_wp_per_commessa(ActionContext context, ProgettoBulk commessa) {
		return doNuovaRicerca(context);
	}

	public Forward doSearchFind_wp_per_commessa(ActionContext context) {
		return doCerca(context);
	}

	public Forward doFreeSearchFind_wp_per_commessa(ActionContext context) {
		return doRicercaLibera(context);
	}

	public Forward doCerca(ActionContext context) {
		try {
			Forward forward = super.doCerca(context);
			BusinessProcess bp = context.getBusinessProcess();
			if (bp instanceof CommessaWorkpackageBP)
				caricaTabWorkpackages(context);
			return (forward);
		} catch (RemoteException e) {
			return handleException(context,e);
		} catch (InstantiationException e) {
			return handleException(context,e);
		} catch (RemoveException e) {
			return handleException(context,e);
		}
	}
	 
	public Forward doRiportaSelezione(ActionContext context) throws RemoteException {
		Forward forward = super.doRiportaSelezione(context);
		caricaTabWorkpackages(context);
		return forward;
	}
	
	public Forward doBringBackSearchResult(ActionContext context) {
		return null;
	}

	public Forward caricaTabWorkpackages(ActionContext context)  throws java.rmi.RemoteException 
	{
		try
		{
			CommessaWorkpackageBP bp = (CommessaWorkpackageBP)context.getBusinessProcess();		
			bp.cercaWorkpackages(context);	
			return context.findDefaultForward();
		} catch(Exception ex) 
		{
				return handleException(context,ex);
		}
	
	}

	public Forward doAggiungiWorkpackage(ActionContext context) 
	{
		CommessaWorkpackageBP bp = (CommessaWorkpackageBP)context.getBusinessProcess();
		ProgettoBulk commessa = (ProgettoBulk)bp.getModel();
		int[] indexes = bp.getCrudWorkpackage_disponibili().getSelectedRows(context);
	
		java.util.Arrays.sort( indexes );
		for (int i = indexes.length - 1 ;i >= 0 ;i--) 
		{	
			WorkpackageBulk wp = commessa.addToWorkpackage_collegati(indexes[i]);
			wp.setToBeUpdated();
			wp.setUser(context.getUserInfo().getUserid());
		}
		return context.findDefaultForward();
	}


	public Forward doRimuoviWorkpackage(ActionContext context) {

		CommessaWorkpackageBP bp = (CommessaWorkpackageBP)context.getBusinessProcess();
		ProgettoBulk commessa = (ProgettoBulk)bp.getModel();
		int[] indexes = bp.getCrudWorkpackage_collegati().getSelectedRows(context);
	
		java.util.Arrays.sort( indexes );
		for (int i = indexes.length - 1 ;i >= 0 ;i--) 
		{	
			WorkpackageBulk wp = commessa.addToWorkpackage_disponibili(indexes[i]);
			wp.setToBeUpdated();
			wp.setUser(context.getUserInfo().getUserid());
		}
		return context.findDefaultForward();
	}

	public Forward doSalva(ActionContext context) throws RemoteException {

		String message = "Il modulo di attività sui workpackage selezionati verrà cambiato.\n"
						+ "Vuoi continuare?";
		try {
			openConfirm(context, message, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doSalvaConfermato");
		} catch (BusinessProcessException ex) {
			return handleException(context,ex);
		}
		return context.findDefaultForward();
	}

	public Forward doSalvaConfermato(ActionContext context, int opt) throws RemoteException {
		CommessaWorkpackageBP bp = (CommessaWorkpackageBP)context.getBusinessProcess();		
		ProgettoBulk commessa = (ProgettoBulk)bp.getModel();
		if (opt == OptionBP.YES_BUTTON) {
			try {
				bp.updateWorkpackages(context.getUserContext(), commessa.getWorkpackage_collegati());
				bp.updateWorkpackages(context.getUserContext(), commessa.getWorkpackage_disponibili());
			} catch (BusinessProcessException ex) {
				return handleException(context,ex);
			}
			super.doSalva(context);
			caricaTabWorkpackages(context);
		}
		return context.findDefaultForward();
	}
}
