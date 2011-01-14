package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.bp.*;
import it.cnr.contab.compensi00.ejb.*;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.jada.action.*;

/**
 * Insert the type's description here.
 * Creation date: (05/06/2002 13.31.29)
 * @author: CNRADM
 */
public class CRUDTipoTrattamentoAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDTipoTrattamentoAction constructor comment.
 */
public CRUDTipoTrattamentoAction() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (05/06/2002 13.32.14)
 * @return it.cnr.jada.action.Forward
 */
public Forward doCaricaIntervalli(ActionContext context) {

	try {
		fillModel(context);
		CRUDTipoTrattamentoBP bp = (CRUDTipoTrattamentoBP)getBusinessProcess(context);
		Tipo_trattamentoBulk tratt = (Tipo_trattamentoBulk)bp.getModel();

		java.util.List l = null;
		if (tratt.getCd_trattamento()!=null){
			TipoTrattamentoComponentSession session = (TipoTrattamentoComponentSession)bp.createComponentSession();
			l = session.caricaIntervalli(context.getUserContext(), tratt);
		}else
			tratt.setDs_ti_trattamento(null);
		
		tratt.setIntervalli(l);
		if (tratt.getIntervalli()!=null && !tratt.getIntervalli().isEmpty()){
			Tipo_trattamentoBulk obj = (Tipo_trattamentoBulk)tratt.getIntervalli().get(0);
			tratt.setDs_ti_trattamento(obj.getDs_ti_trattamento());
		}

		return context.findDefaultForward();

	}catch(Throwable ex){
		return handleException(context, ex);
	}
}
/**
 * Gestisce un comando di cancellazione.
 */
public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {
	try {
		fillModel(context);

		CRUDTipoTrattamentoBP bp = (CRUDTipoTrattamentoBP)getBusinessProcess(context);
		bp.delete(context);
		Tipo_trattamentoBulk tratt = (Tipo_trattamentoBulk)bp.getModel();
		if (tratt.getDt_ini_validita().compareTo(CompensoBulk.getDataOdierna())>0){
			bp.reset(context);
			bp.setMessage("Cancellazione effettuata");
		}else{
			bp.edit(context, tratt);
			bp.setMessage("Annullamento effettuato");
		}
		return context.findDefaultForward();

	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
