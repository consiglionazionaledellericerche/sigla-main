package it.cnr.contab.anagraf00.action;

import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * Insert the type's description here.
 * Creation date: (13/03/2002 13.12.33)
 * @author: CNRADM
 */
public class CRUDTipoRapportoAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDTipoRapportoAction constructor comment.
 */
public CRUDTipoRapportoAction() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (13/03/2002 13.13.18)
 * @return it.cnr.jada.action.Forward
 * @param context it.cnr.jada.action.ActionContext
 */
public it.cnr.jada.action.Forward doOnTipoAnagraficoClick(it.cnr.jada.action.ActionContext context) {

	try{
		fillModel(context);

		SimpleCRUDBP bp = (SimpleCRUDBP)getBusinessProcess(context);
		Tipo_rapportoBulk tipoRapporto = (Tipo_rapportoBulk)bp.getModel();

		tipoRapporto.setTi_rapporto_altro(null);

		return context.findDefaultForward();

	}catch(Throwable ex){
		return handleException(context, ex);
	}
}
}
