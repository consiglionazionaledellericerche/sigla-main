package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.compensi00.bp.*;
import it.cnr.jada.action.*;

public class CRUDEsenzioni_addcomAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDEsenzioni_addcomAction constructor comment.
 */
public CRUDEsenzioni_addcomAction() {
	super();
}
/**
 * Gestisce un comando di cancellazione.
 */
public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {

	try {
		fillModel(context);

		CRUDEsenzioni_addcomBP bp = (CRUDEsenzioni_addcomBP)getBusinessProcess(context);
		if (!bp.isEditing()) {
			bp.setMessage("Non è possibile cancellare in questo momento");
		} else {
			bp.delete(context);
			Esenzioni_addcomBulk esenzione = (Esenzioni_addcomBulk)bp.getModel();
			if (esenzione.getDt_inizio_validita().compareTo(CompensoBulk.getDataOdierna())>0){
				bp.reset(context);
				bp.setMessage("Cancellazione effettuata");
			}else{
				bp.edit(context, esenzione);
				bp.setMessage("Annullamento effettuato");
			}
		}
		return context.findDefaultForward();

	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
