package it.cnr.contab.incarichi00.action;

import java.rmi.RemoteException;
import java.util.List;

import it.cnr.contab.incarichi00.bp.ConsCdSDaCompletareBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;

public class ConsCdSDaCompletareAction extends ConsultazioniAction {

	public Forward doEMail(ActionContext actioncontext) throws RemoteException {
		ConsCdSDaCompletareBP bp = (ConsCdSDaCompletareBP)actioncontext.getBusinessProcess();
		try{
			if (bp.getSelection() != null){
				bp.setSelection(actioncontext);
				List cds = bp.getSelectedElements(actioncontext);
				if ( cds == null )
					return (Forward)actioncontext.findDefaultForward();
			
				if (cds.isEmpty()) {
					bp.setMessage("Non è stata selezionata nessuna riga.");
					return actioncontext.findDefaultForward();
				}							
				bp.inviaEMail(actioncontext, cds);
				bp.setMessage("Invio E-Mail effettuato con successo.");
				bp.clearSelection(actioncontext);
			}	
		}catch(BusinessProcessException e){
			handleException(actioncontext, e);
		}
		return actioncontext.findDefaultForward();
	}
}
