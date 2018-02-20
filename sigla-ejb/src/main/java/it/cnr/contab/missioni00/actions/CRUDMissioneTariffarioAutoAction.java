package it.cnr.contab.missioni00.actions;

import it.cnr.contab.missioni00.tabrif.bulk.*;
import it.cnr.contab.missioni00.bp.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.action.*;
/**
 * Insert the type's description here.
 * Creation date: (25/07/2002 17.00.47)
 * @author: Roberto Fantino
 */
public class CRUDMissioneTariffarioAutoAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDMissioneTariffarioAutoAction constructor comment.
 */
public CRUDMissioneTariffarioAutoAction() {
	super();
}
/**
 * Gestisce un comando di cancellazione.
 */
public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {

	try {
		fillModel(context);

		CRUDMissioneTariffarioAutoBP bp = (CRUDMissioneTariffarioAutoBP )getBusinessProcess(context);
		if (!bp.isEditing()) {
			bp.setMessage("Non è possibile cancellare in questo momento");
		} else {
			bp.delete(context);
			Missione_tariffario_autoBulk tariffario = (Missione_tariffario_autoBulk)bp.getModel();
			if (tariffario.getDt_inizio_validita().compareTo(CompensoBulk.getDataOdierna())>0){
				bp.reset(context);
				bp.setMessage("Cancellazione effettuata");
			}else{
				bp.edit(context, tariffario);
				bp.setMessage("Annullamento effettuato");
			}
		}
		return context.findDefaultForward();

	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
