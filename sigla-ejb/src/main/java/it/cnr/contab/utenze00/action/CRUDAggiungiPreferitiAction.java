package it.cnr.contab.utenze00.action;

import java.rmi.RemoteException;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.FormController;

public class CRUDAggiungiPreferitiAction extends CRUDAction {
	@Override
	public Forward doElimina(ActionContext actioncontext) throws RemoteException {
        CRUDBP crudbp = getBusinessProcess(actioncontext);
		Forward fwd = super.doElimina(actioncontext);
		crudbp.setStatus(FormController.SEARCH);
		return fwd;
	}
}
