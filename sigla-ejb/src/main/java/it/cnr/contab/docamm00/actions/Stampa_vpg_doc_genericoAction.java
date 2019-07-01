package it.cnr.contab.docamm00.actions;

import it.cnr.contab.docamm00.docs.bulk.Stampa_vpg_doc_genericoBulk;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
/**
 * Insert the type's description here.
 * Creation date: (20/03/2003 11.12.29)
 * @author: Gennaro Borriello
 */
public class Stampa_vpg_doc_genericoAction extends it.cnr.contab.reports.action.ParametricPrintAction {
/**
 * Stampa_vpg_doc_genericoAction constructor comment.
 */
public Stampa_vpg_doc_genericoAction() {
	super();
}
/**
 * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
 * figlio del ricevente
 */

public Forward doOnTipoDocumentoChange(ActionContext context) {

    try {
        fillModel(context);
        
        ParametricPrintBP bp= (ParametricPrintBP) context.getBusinessProcess();
        Stampa_vpg_doc_genericoBulk stampa = (Stampa_vpg_doc_genericoBulk) bp.getModel();

        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
public Forward doOnTipoDocumentoGenericoChange(ActionContext context) {

    try {
        fillModel(context);
        
        ParametricPrintBP bp= (ParametricPrintBP) context.getBusinessProcess();
        Stampa_vpg_doc_genericoBulk stampa = (Stampa_vpg_doc_genericoBulk) bp.getModel();

        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
}
