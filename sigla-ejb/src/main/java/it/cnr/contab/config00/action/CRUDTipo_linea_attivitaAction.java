package it.cnr.contab.config00.action;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.contab.config00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

/**
 * <!-- @TODO: da completare -->
 */

public class CRUDTipo_linea_attivitaAction extends it.cnr.jada.util.action.CRUDAction {
public CRUDTipo_linea_attivitaAction() {
	super();
}

/**
 * Gestisce una richiesta di cancellazione dal controller "cdrAssociati"
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doAddToCRUDMain_cdrAssociati(ActionContext context) {
	try {
		fillModel(context);
		CRUDTipo_linea_attivitaBP bp = (CRUDTipo_linea_attivitaBP)context.getBusinessProcess();
		it.cnr.jada.util.RemoteIterator ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,((it.cnr.contab.config00.ejb.Tipologia_linea_attivitaComponentSession)bp.createComponentSession()).cercaCdrAssociabili(context.getUserContext(),(Tipo_linea_attivitaBulk)bp.getModel()));
		int count = ri.countElements();
		if (count == 0) {
			bp.setMessage("Nessun cdr associabile");
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
		} else {
			SelezionatoreListaBP slbp = select(context,ri,it.cnr.jada.bulk.BulkInfo.getBulkInfo(CdrBulk.class),null,"doSelezionaCdr_associati",null,bp);
			slbp.setMultiSelection(true);
		}
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}

/**
 * Seleziona i CDR associati al tipo di linea di attività in processo
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doSelezionaCdr_associati(ActionContext context) {
	try {
		CRUDTipo_linea_attivitaBP bp = (CRUDTipo_linea_attivitaBP)context.getBusinessProcess();
		bp.getCdrAssociati().reset(context);
		bp.setDirty(true);
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
}