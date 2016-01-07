package it.cnr.contab.doccont00.action;

import java.rmi.RemoteException;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.CRUDAction;

public class AllegatiDocContAction extends CRUDAction {
	private static final long serialVersionUID = 1L;
	
	@Override
	public Forward doElimina(ActionContext actioncontext)
			throws RemoteException {
		super.doElimina(actioncontext);
        HookForward hookforward = (HookForward)actioncontext.findForward("close");
        if(hookforward != null)
            return hookforward;
        else
            return actioncontext.findDefaultForward();
		
	}

}
