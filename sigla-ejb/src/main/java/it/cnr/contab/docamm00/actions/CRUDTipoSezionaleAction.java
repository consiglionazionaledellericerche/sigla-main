package it.cnr.contab.docamm00.actions;

import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.SimpleCRUDBP;
public class CRUDTipoSezionaleAction extends it.cnr.jada.util.action.CRUDAction {
	/**
 * CRUDFatturaAttivaAction constructor comment.
 */
public CRUDTipoSezionaleAction() {
	super();
}
/**
 * Gestisce un HookForward di ritorno da un risultato di una ricerca (SelezionatoreBP)
 */
public Forward doOnFlAutofatturaChange(ActionContext context)
	throws java.rmi.RemoteException {

	return doOnTiAcquistiVenditeChange(context);
}
/**
 * Gestisce un HookForward di ritorno da un risultato di una ricerca (SelezionatoreBP)
 */
public Forward doOnTiAcquistiVenditeChange(ActionContext context)
	throws java.rmi.RemoteException {

    try {
	    fillModel(context);
        SimpleCRUDBP bp = (SimpleCRUDBP) context.getBusinessProcess();
        Tipo_sezionaleBulk tipoSezionale = (Tipo_sezionaleBulk) bp.getModel();
		if (!tipoSezionale.isAcquisti() && !tipoSezionale.isAutofattura())
			tipoSezionale.setTi_bene_servizio(tipoSezionale.BENE_SERVIZIO);
			
        return context.findDefaultForward();
    } catch (Throwable e) {
        return handleException(context, e);
    }

}
}
