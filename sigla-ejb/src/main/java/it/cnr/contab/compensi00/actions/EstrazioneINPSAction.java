package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.docs.bulk.EstrazioneINPSBulk;
import it.cnr.contab.compensi00.bp.EstrazioneINPSBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
/**
 * Insert the type's description here.
 * Creation date: (16/03/2004 15.30.24)
 * @author: Gennaro Borriello
 */
public class EstrazioneINPSAction extends it.cnr.jada.util.action.BulkAction {
/**
 * EstrazioneINPSAction constructor comment.
 */
public EstrazioneINPSAction() {
	super();
}
public Forward doElaboraINPS(ActionContext context) {

    try {
        fillModel(context);
        EstrazioneINPSBP bp = (EstrazioneINPSBP) context.getBusinessProcess();

        bp.doElaboraINPS(context);        
		
        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
}
