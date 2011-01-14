package it.cnr.contab.config00.action;

import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.config00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;
/**
 * Insert the type's description here.
 * Creation date: (19/12/2002 16.08.47)
 * @author: Simonetta Costa
 */
public class StampaPdcFinSpeseAction extends it.cnr.jada.util.action.BulkAction {
/**
 * StampaPdcFinSpeseAction constructor comment.
 */
public StampaPdcFinSpeseAction() {
	super();
}
/**
 * Gestisce un comando di stampa
 */
public Forward doPrint(ActionContext context) {
	try {

		StampaPdcFinSpeseBP bp = (StampaPdcFinSpeseBP)context.getBusinessProcess();
		fillModel(context);
		V_stampa_pdc_fin_speseBulk obj = (V_stampa_pdc_fin_speseBulk)bp.getModel();
		obj.validate();
		return doConfirmPrint(context,OptionBP.YES_BUTTON);

	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
