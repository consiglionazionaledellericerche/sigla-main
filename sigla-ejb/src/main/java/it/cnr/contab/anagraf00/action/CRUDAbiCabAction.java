package it.cnr.contab.anagraf00.action;

import it.cnr.contab.anagraf00.ejb.*;
import it.cnr.contab.anagraf00.tabter.bulk.*;
import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.contab.anagraf00.bp.*;
import it.cnr.jada.action.*;
/**
 * Insert the type's description here.
 * Creation date: (30/10/2002 15.22.45)
 * @author: CNRADM
 */
public class CRUDAbiCabAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDAbiCabAction constructor comment.
 */
public CRUDAbiCabAction() {
	super();
}
public Forward doBlankSearchFind_comune(ActionContext context, AbicabBulk abicab) {

	if (abicab!=null)
		abicab.resetComune();
	return context.findDefaultForward();
}
public Forward doBringBackSearchFind_comune(ActionContext context, AbicabBulk abicab, ComuneBulk comune) {

	try{
		if (abicab!=null && comune!=null){
			CRUDAbiCabBP bp = (CRUDAbiCabBP)getBusinessProcess(context);

			abicab.setComune(comune);
			bp.findCaps(context);
		}
		return context.findDefaultForward();

	}catch(BusinessProcessException ex){
		return handleException(context, ex);
	}
	
}
/**
 * Gestisce un comando di cancellazione.
 */
public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {

	try {
		fillModel(context);

		CRUDAbiCabBP bp = (CRUDAbiCabBP)getBusinessProcess(context);
		if (!bp.isEditing()) {
			bp.setMessage("Non è possibile cancellare in questo momento");
		} else {
			bp.delete(context);
			AbicabBulk abicab = (AbicabBulk)bp.getModel();
			AbiCabComponentSession session = (AbiCabComponentSession)bp.createComponentSession();
			if (session.isCancellatoLogicamente(context.getUserContext(), abicab)){
				bp.edit(context, abicab);
				bp.setMessage("Annullamento effettuato");
			}else{
				bp.reset(context);
				bp.setMessage("Cancellazione effettuata");
			}
		}
		return context.findDefaultForward();

	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
