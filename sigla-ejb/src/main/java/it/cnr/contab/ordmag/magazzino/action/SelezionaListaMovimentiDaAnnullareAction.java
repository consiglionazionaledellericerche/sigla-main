package it.cnr.contab.ordmag.magazzino.action;

import it.cnr.contab.ordmag.magazzino.bp.SelezionatoreMovimentiDaAnnullareBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.SelezionatoreListaAction;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2002 12:39:49 PM)
 * @author: Roberto Peli
 */
public class SelezionaListaMovimentiDaAnnullareAction extends SelezionatoreListaAction {
	/**
	 * DocumentiAmministrativiProtocollabiliAction constructor comment.
	 */
	public SelezionaListaMovimentiDaAnnullareAction() {
		super();
	}

	public Forward doAnnullaMovimenti(ActionContext context) {
		SelezionatoreMovimentiDaAnnullareBP bp = (SelezionatoreMovimentiDaAnnullareBP)context.getBusinessProcess();
		try {
			fillModel(context);
			bp.setSelection(context);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	@Override
	public Forward basicDoBringBack(ActionContext actioncontext) throws BusinessProcessException {
		return actioncontext.findDefaultForward();
	}
}
