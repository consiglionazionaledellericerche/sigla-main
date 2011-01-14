package it.cnr.contab.docamm00.actions;

import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
/**
 * Insert the type's description here.
 * Creation date: (9/5/2001 5:02:18 PM)
 * @author: Ardire Alfonso
 */
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.docamm00.bp.*;
import it.cnr.contab.docamm00.ejb.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.BulkBP;


public class CRUDTariffarioAction extends it.cnr.jada.util.action.CRUDAction {
	/**
 * CRUDFatturaAttivaAction constructor comment.
 */
public CRUDTariffarioAction() {
	super();
}
/**
 * Gestisce un HookForward di ritorno da un risultato di una ricerca (SelezionatoreBP)
 */
public Forward doEliminaTariffario(ActionContext context) throws java.rmi.RemoteException {

    try {
        CRUDTariffarioBP bp = (CRUDTariffarioBP) context.getBusinessProcess();
        TariffarioBulk tariffario = (TariffarioBulk) bp.getModel();

		try {
			java.sql.Timestamp dataAttuale = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
	        tariffario.setDataFineValidita(dataAttuale);
	        bp.setModel(context, tariffario);
	        if (tariffario.getDt_ini_validita().after(dataAttuale))
	            doElimina(context);
	        else
	        	doSalva(context);
		} catch (javax.ejb.EJBException e) {
			throw new it.cnr.jada.DetailedRuntimeException(e);
		}

        return context.findDefaultForward();
    } catch (Throwable e) {
        return handleException(context, e);
    }

}
}
