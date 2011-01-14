package it.cnr.contab.config00.action;

import java.rmi.RemoteException;

import it.cnr.contab.config00.bp.LockObjectBP;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.utenze00.bulk.LockedObjectBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.MessageToUser;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.FormController;

public class LockObjectAction extends CRUDAction{
	public it.cnr.jada.action.Forward doBringBackSearchCds(it.cnr.jada.action.ActionContext actionContext,LockedObjectBulk lockedObject, CdsBulk cds) throws BusinessProcessException {
		LockObjectBP bp = (LockObjectBP)actionContext.getBusinessProcess();
		lockedObject.setCds(cds);
		if (cds != null){
			bp.getUtenti().getSelection().clearSelection();
			bp.riempiListaUtenti(actionContext);
		}
		lockedObject.setOggetti(null);
		return actionContext.findDefaultForward();
	}
	
	public it.cnr.jada.action.Forward doSelectUtenti(it.cnr.jada.action.ActionContext actionContext) throws BusinessProcessException{
		LockObjectBP bp = (LockObjectBP)actionContext.getBusinessProcess();
		try {
			bp.getUtenti().setSelection(actionContext);
		} catch (BusinessProcessException e) {
			return handleException(actionContext, e);
		} catch (ValidationException e) {
			return handleException(actionContext, e);
		}
		LockedObjectBulk lockedObject = (LockedObjectBulk) bp.getModel();
		lockedObject.setUtente( (UtenteBulk)bp.getUtenti().getModel());
		bp.selezionaUtente(actionContext);
		return actionContext.findDefaultForward();	
	}

	public it.cnr.jada.action.Forward doBlankSearchCds(it.cnr.jada.action.ActionContext actionContext, LockedObjectBulk lockedObject) throws BusinessProcessException {
		LockObjectBP bp = (LockObjectBP)actionContext.getBusinessProcess();
		lockedObject.setCds(new CdsBulk());
		lockedObject.setUtenti(null);
		lockedObject.setOggetti(null);
		return actionContext.findDefaultForward();	
	}
	@Override
	public Forward doElimina(ActionContext actioncontext) throws RemoteException {
		try{
			LockObjectBP bp = (LockObjectBP)actioncontext.getBusinessProcess();
			fillModel(actioncontext);
			bp.disconnettiUtenti(actioncontext);
            bp.reset(actioncontext);
            bp.setStatus(FormController.SEARCH);
            bp.setMessage("Cancellazione effettuata");
			return actioncontext.findDefaultForward();	
		}catch(Throwable throwable){
			 return handleException(actioncontext, throwable);
		}
	}
}
