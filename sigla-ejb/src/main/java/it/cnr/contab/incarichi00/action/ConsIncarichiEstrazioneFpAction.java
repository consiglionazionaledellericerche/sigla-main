package it.cnr.contab.incarichi00.action;


import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;

public class ConsIncarichiEstrazioneFpAction extends it.cnr.jada.util.action.ConsultazioniAction{
	public ConsIncarichiEstrazioneFpAction() {
		super();
	}

    public Forward doGeneraXML(ActionContext actioncontext) throws BusinessProcessException {
	    try
	    {
			SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.getBusinessProcess();
			selezionatorelistabp.saveSelection(actioncontext);
	    	HookForward forward = (HookForward)actioncontext.findForward("generaXML");
            forward.addParameter("selectedElements", ((ConsultazioniBP)actioncontext.getBusinessProcess()).getSelectedElements(actioncontext));
	        actioncontext.closeBusinessProcess();
	        return forward;
	    }
	    catch(Throwable throwable)
	    {
	        return handleException(actioncontext, throwable);
	    }
    }
}
