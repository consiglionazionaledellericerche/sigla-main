package it.cnr.contab.inventario00.actions;

/**
 * Insert the type's description here.
 * Creation date: (03/12/2001 12.13.02)
 * @author: CNRADM
 */
import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.inventario00.bp.*;

import it.cnr.contab.inventario00.docs.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;
 
public class CRUDInventarioApChAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDInventarioApChAction constructor comment.
 */
public CRUDInventarioApChAction() {
	super();
}
/**
 * Richiamato nel caso che la data di inizio validità del Consegnatario non sia valida.
 *	Il controllo viene effettuato al momento del salvataggio e verifica che la data di apertura 
 *	dell'Inventario NON sia antecedente alla data dui inizio validità del primo Consegnatario
 *	specificato per l'Inventario.
 */

public Forward doOnCheckDataConsegnatarioFailed(
    ActionContext context,
    int option) {

    if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) {
        CRUDInventarioApChBP bp = (CRUDInventarioApChBP) getBusinessProcess(context);
        try {           
            OptionRequestParameter userConfirmation = new OptionRequestParameter();
            userConfirmation.setCheckDataConsegnatarioRequired(Boolean.FALSE);
            bp.setUserConfirm(userConfirmation);
             
            doSalva(context);
            
            Inventario_ap_chBulk invApCh = (Inventario_ap_chBulk)bp.getModel();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }
    return context.findDefaultForward();
}
}
