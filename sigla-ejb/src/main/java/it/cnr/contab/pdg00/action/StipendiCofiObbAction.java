package it.cnr.contab.pdg00.action;

import it.cnr.contab.pdg00.bp.StipendiCofiObbBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
/**
 * Insert the type's description here.
 * Creation date: (10/04/2003 12.04.09)
 * @author: Gennaro Borriello
 */
public class StipendiCofiObbAction extends it.cnr.jada.util.action.CRUDAction {

public StipendiCofiObbAction() {
	super();
}
public Forward doReset(ActionContext context) {

    try {
    	StipendiCofiObbBP bp= (StipendiCofiObbBP) context.getBusinessProcess();

        bp.resetDati(context);

        return context.findDefaultForward();
    } catch (Throwable e) {
        return handleException(context, e);
    }
}
}