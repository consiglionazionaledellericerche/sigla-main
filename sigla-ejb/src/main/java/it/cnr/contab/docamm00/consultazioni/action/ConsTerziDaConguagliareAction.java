package it.cnr.contab.docamm00.consultazioni.action;

import java.rmi.RemoteException;
import java.util.List;

import it.cnr.contab.docamm00.consultazioni.bp.ConsTerziDaConguagliareBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;

public class ConsTerziDaConguagliareAction extends ConsultazioniAction {

	public Forward doCdSDaConguagliare(ActionContext actioncontext) throws RemoteException {
		try {
			actioncontext.addBusinessProcess(actioncontext.createBusinessProcess("ConsCdSDaConguagliareBP"));
		} catch (BusinessProcessException e) {
			handleException(actioncontext, e);
		}
		return actioncontext.findDefaultForward();
	}
}
