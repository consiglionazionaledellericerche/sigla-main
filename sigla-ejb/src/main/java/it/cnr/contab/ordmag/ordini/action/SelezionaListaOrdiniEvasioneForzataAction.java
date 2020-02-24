package it.cnr.contab.ordmag.ordini.action;

import it.cnr.contab.ordmag.magazzino.bp.SelezionatoreMovimentiDaAnnullareBP;
import it.cnr.contab.ordmag.ordini.bp.SelezionatoreEvasioneForzataBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.SelezionatoreListaAction;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2002 12:39:49 PM)
 * @author: Roberto Peli
 */
public class SelezionaListaOrdiniEvasioneForzataAction extends SelezionatoreListaAction {
	/**
	 * DocumentiAmministrativiProtocollabiliAction constructor comment.
	 */
	public SelezionaListaOrdiniEvasioneForzataAction() {
		super();
	}

	public Forward doAChiusuraForzataOrdini(ActionContext context) {
		SelezionatoreEvasioneForzataBP bp = (SelezionatoreEvasioneForzataBP)context.getBusinessProcess();
		try {
			fillModel(context);
			bp.setSelection(context);
			bp.chiusuraForzataOrdini(context);
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
