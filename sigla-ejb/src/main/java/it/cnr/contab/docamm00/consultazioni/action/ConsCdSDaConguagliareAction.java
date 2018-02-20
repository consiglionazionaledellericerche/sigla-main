package it.cnr.contab.docamm00.consultazioni.action;

import java.rmi.RemoteException;
import java.util.List;

import it.cnr.contab.docamm00.consultazioni.bp.ConsCdSDaConguagliareBP;
import it.cnr.contab.docamm00.consultazioni.bp.ConsTerziDaConguagliareBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;

public class ConsCdSDaConguagliareAction extends ConsultazioniAction {

	public Forward doEMail(ActionContext actioncontext) throws RemoteException {
		ConsCdSDaConguagliareBP bp = (ConsCdSDaConguagliareBP)actioncontext.getBusinessProcess();
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
