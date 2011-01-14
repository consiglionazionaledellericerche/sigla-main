package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.bp.EstrazioneINPSMensileBP;
import it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDVBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
/**
 * Insert the type's description here.
 * Creation date: (02/02/2004 16.44.25)
 * @author: Gennaro Borriello
 */
public class EstrazioneINPSMensileAction extends it.cnr.jada.util.action.BulkAction {
/**
 * EstrazioneINPSMensileAction constructor comment.
 */
public EstrazioneINPSMensileAction() {
	super();
}
public Forward doElaboraINPSMensile(ActionContext context) {

    try {
        fillModel(context);
        EstrazioneINPSMensileBP bp = (EstrazioneINPSMensileBP) context.getBusinessProcess();

        bp.doElaboraINPSMensile(context);        
		
        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
}
