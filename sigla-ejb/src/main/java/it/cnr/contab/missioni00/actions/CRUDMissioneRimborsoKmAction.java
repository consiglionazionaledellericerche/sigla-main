package it.cnr.contab.missioni00.actions;

import it.cnr.contab.missioni00.tabrif.bulk.*;
import it.cnr.contab.missioni00.bp.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.action.*;
/**
 * Insert the type's description here.
 * Creation date: (15/02/2002 12.19.19)
 * @author: Roberto Fantino
 */
public class CRUDMissioneRimborsoKmAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDMissioneRimborsoKmAction constructor comment.
 */
public CRUDMissioneRimborsoKmAction() {
	super();
}
/**
 * Gestisce un comando di cancellazione.
 */
public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {

	try {
		fillModel(context);

		CRUDMissioneRimborsoKmBP bp = (CRUDMissioneRimborsoKmBP)getBusinessProcess(context);
		if (!bp.isEditing()) {
			bp.setMessage("Non è possibile cancellare in questo momento");
		} else {
			bp.delete(context);
			Missione_rimborso_kmBulk rimborsoKm = (Missione_rimborso_kmBulk )bp.getModel();
			if (rimborsoKm.getDt_inizio_validita().compareTo(CompensoBulk.getDataOdierna())>0){
				bp.reset(context);
				bp.setMessage("Cancellazione effettuata");
			}else{
				bp.edit(context, rimborsoKm);
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
		CRUDMissioneRimborsoKmBP bp = (CRUDMissioneRimborsoKmBP)getBusinessProcess(context);
		fillModel(context);
		
		bp.gestioneNazione(context);
		
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
