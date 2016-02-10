package it.cnr.contab.firma.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.util.action.BulkAction;
import it.cnr.jada.util.action.BulkBP;

public class FirmaOTPAction extends BulkAction {
	private static final long serialVersionUID = 1L;

	public Forward doAnnulla(ActionContext context) {
		try {
			context.closeBusinessProcess();
		} catch (BusinessProcessException e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}	

	public Forward doConferma(ActionContext context) {
		try {
			BulkBP bp = (BulkBP)context.getBusinessProcess();
			bp.fillModel(context);
	        HookForward hookforward = (HookForward)context.findForward("firmaOTP");
	        hookforward.addParameter("firmaOTP", bp.getModel());
	        context.closeBusinessProcess();
	        return hookforward;
		}  catch (FillException e) {
			return handleException(context,e);
		} catch (BusinessProcessException e) {
			return handleException(context,e);
		}
	}	

}
