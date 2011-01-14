package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDVBulk;
import it.cnr.contab.compensi00.bp.EstrazioneCUDBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
/**
 * Insert the type's description here.
 * Creation date: (02/02/2004 16.44.25)
 * @author: Gennaro Borriello
 */
public class EstrazioneCUDAction extends it.cnr.jada.util.action.BulkAction {
/**
 * EstrazioneCUDAction constructor comment.
 */
public EstrazioneCUDAction() {
	super();
}
public Forward doElaboraCUD(ActionContext context) {

    try {
        fillModel(context);
        EstrazioneCUDBP bp = (EstrazioneCUDBP) context.getBusinessProcess();

        bp.doElaboraCUD(context);        
		
        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
}
