package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.core.bulk.Stampa_obbligazioni_LAVBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.FormField;
/**
 * Insert the type's description here.
 * Creation date: (30/06/2004 17.28.09)
 * @author: Gennaro Borriello
 */
public class StampaObbligazioniLAAction extends it.cnr.contab.reports.action.ParametricPrintAction {
/**
 * StampaObbligazioniLAAction constructor comment.
 */
public StampaObbligazioniLAAction() {
	super();
}
public Forward doBlankSearchFindCdrForPrint(ActionContext context, Stampa_obbligazioni_LAVBulk stampa) {

	try {
        fillModel(context);
        stampa.setLineaAttForPrint(new it.cnr.contab.config00.latt.bulk.WorkpackageBulk());

        
        FormField field = getFormField(context, "main.findCdrForPrint");
        blankSearch(context, field, createEmptyModelForSearchTool(context, field));
        
        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
}
