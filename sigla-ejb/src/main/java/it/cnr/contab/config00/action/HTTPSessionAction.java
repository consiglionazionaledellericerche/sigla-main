package it.cnr.contab.config00.action;

import java.rmi.RemoteException;

import it.cnr.contab.config00.bp.HTTPSessionBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.FormController;

public class HTTPSessionAction extends ConsultazioniAction {

	public Forward doEliminaSessioni(ActionContext actioncontext) throws RemoteException {
		try{
			fillModel(actioncontext);
			HTTPSessionBP bp = (HTTPSessionBP)actioncontext.getBusinessProcess();
			bp.saveSelection(actioncontext);
			if (bp.getSelectedElements(actioncontext).isEmpty()) {
				bp.setMessage("Non è stata selezionata nessuna riga.");
				return actioncontext.findDefaultForward();
			}							
			bp.disconnettiSessioni(actioncontext);
            bp.refresh(actioncontext);
            bp.setMessage("Sessioni terminate");
			return actioncontext.findDefaultForward();	
		}catch(Throwable throwable){
			 return handleException(actioncontext, throwable);
		}
	}

	public Forward doAggiorna(ActionContext actioncontext) throws RemoteException {
		try{
			HTTPSessionBP bp = (HTTPSessionBP)actioncontext.getBusinessProcess();
            bp.refresh(actioncontext);
			return actioncontext.findDefaultForward();	
		}catch(Throwable throwable){
			 return handleException(actioncontext, throwable);
		}
	}

}
