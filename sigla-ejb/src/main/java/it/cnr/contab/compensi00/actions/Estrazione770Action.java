package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.docs.bulk.Estrazione770Bulk;
import it.cnr.contab.compensi00.bp.Estrazione770BP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
/**
 * Creation date: (24/09/2004)
 * @author: Aurelio D'Amico
 * @version: 1.0
 */
public class Estrazione770Action extends it.cnr.jada.util.action.BulkAction {
/**
 * Estrazione770Action constructor comment.
 */
public Estrazione770Action() {
	super();
}
public Forward doElabora770(ActionContext context) {

	try {
		fillModel(context);
		Estrazione770BP bp = (Estrazione770BP) context.getBusinessProcess();

		bp.doElabora770(context);        
		
		return context.findDefaultForward();
	} catch (Throwable t) {
		return handleException(context, t);
	}
}
}
