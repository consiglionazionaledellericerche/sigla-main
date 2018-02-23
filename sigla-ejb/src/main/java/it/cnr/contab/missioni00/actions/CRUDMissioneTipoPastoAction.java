package it.cnr.contab.missioni00.actions;

import it.cnr.contab.missioni00.bp.*;
import it.cnr.contab.missioni00.tabrif.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.action.*;

/**
 * Insert the type's description here.
 * Creation date: (22/11/2001 11.06.59)
 * @author: Paola sala
 */
public class CRUDMissioneTipoPastoAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDMissioneTipoPastoAction constructor comment.
 */
public CRUDMissioneTipoPastoAction() {
	super();
}
/**
 * Gestisce un comando di cancellazione.
 */
public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {

	try {
		fillModel(context);

		CRUDMissioneTipoPastoBP bp = (CRUDMissioneTipoPastoBP)getBusinessProcess(context);
		if (!bp.isEditing()) {
			bp.setMessage("Non è possibile cancellare in questo momento");
		} else {
			bp.delete(context);
			Missione_tipo_pastoBulk tipoPasto = (Missione_tipo_pastoBulk)bp.getModel();
			if (tipoPasto.getDt_inizio_validita().compareTo(CompensoBulk.getDataOdierna())>0){
				bp.reset(context);
				bp.setMessage("Cancellazione effettuata");
			}else{
				bp.edit(context, tipoPasto);
				bp.setMessage("Annullamento effettuato");
			}
		}
		return context.findDefaultForward();

	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 16.35.06)
 * @return it.cnr.jada.action.Forward
 * @param context it.cnr.jada.action.ActionContext
 */
public Forward doSelezioneTipoAreaGeografica(ActionContext context) {

	try{
		CRUDMissioneTipoPastoBP bp = (CRUDMissioneTipoPastoBP)getBusinessProcess(context);
		fillModel(context);
		
		bp.gestioneNazione(context);
	
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
