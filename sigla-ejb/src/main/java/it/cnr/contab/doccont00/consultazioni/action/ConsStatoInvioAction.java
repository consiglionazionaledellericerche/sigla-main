package it.cnr.contab.doccont00.consultazioni.action;

import it.cnr.contab.doccont00.consultazioni.bp.ConsStatoInvioBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.SelezionatoreListaAction;

public class ConsStatoInvioAction extends SelezionatoreListaAction {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ConsStatoInvioAction() {
        super();
    }

    @Override
    protected Forward perform(ActionContext actioncontext, String s) {
        Forward forward = super.perform(actioncontext, s);
        BusinessProcess bp = actioncontext.getBusinessProcess();
        try {
            if (bp.getName().equals("ConsStatoInvioMandatiBP") || bp.getName().equals("ConsStatoInvioReversaliBP"))
                ((ConsStatoInvioBP) bp).setContabiliEnabled(actioncontext);
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
        return forward;
    }

    @Override
    public Forward basicDoBringBack(ActionContext actioncontext) throws BusinessProcessException {
        return actioncontext.findDefaultForward();
    }
}
