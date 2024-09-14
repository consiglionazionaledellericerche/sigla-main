package it.cnr.contab.doccont00.consultazioni.action;

import it.cnr.contab.doccont00.consultazioni.bulk.ControlliPCCParams;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.BulkAction;
import it.cnr.jada.util.action.BulkBP;

public class ControlliPCCParamsAction extends BulkAction {

    public Forward doConferma(ActionContext actioncontext) throws BusinessProcessException {
        final BulkBP bulkBP = (BulkBP)actioncontext.getBusinessProcess();
        final ControlliPCCParams controlliPCCParams = (ControlliPCCParams)bulkBP.getModel();
        try {
            fillModel(actioncontext);
            controlliPCCParams.validate();
            HookForward hookforward = (HookForward) actioncontext.findForward("model");
            hookforward.addParameter("model", controlliPCCParams);
            actioncontext.closeBusinessProcess();
            return hookforward;
        } catch (ValidationException | FillException e) {
            return handleException(actioncontext, e);
        }
    }

    public Forward doAnnulla(ActionContext actioncontext) throws BusinessProcessException {
        actioncontext.closeBusinessProcess();
        return actioncontext.findDefaultForward();
    }
}
