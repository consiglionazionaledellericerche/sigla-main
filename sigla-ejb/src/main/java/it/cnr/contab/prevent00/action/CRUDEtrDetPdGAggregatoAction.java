package it.cnr.contab.prevent00.action;
import it.cnr.contab.reports.bp.*;

/**
 * Adatta e implementa la {@link it.cnr.jada.util.action.CRUDAction } per le
 * 		funzionalità del dettaglio Entrate
 * 
 * @author: Bisquadro Vincenzo
 */
public class CRUDEtrDetPdGAggregatoAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * Costruttore standard di CRUDEtrDetPdGAggregatoAction.
 */
public CRUDEtrDetPdGAggregatoAction() {
	super();
}

/**
 * Stampa del dell'aggregato PDG parte entrate
 */
public it.cnr.jada.action.Forward doPrint(it.cnr.jada.action.ActionContext context) {
	try {
		it.cnr.jada.util.action.BulkBP bp = (it.cnr.jada.util.action.BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/pdg/pdg_aggregato_etr.rpt");
		it.cnr.contab.prevent00.bulk.Pdg_aggregato_etr_detBulk detEtrBulk = (it.cnr.contab.prevent00.bulk.Pdg_aggregato_etr_detBulk)bp.getModel();
		printbp.setReportParameter(0,detEtrBulk.getEsercizio().toString());
		printbp.setReportParameter(1,detEtrBulk.getCd_centro_responsabilita());
		return context.addBusinessProcess(printbp);
	} catch(it.cnr.jada.action.BusinessProcessException e) {
		throw new it.cnr.jada.action.ActionPerformingError(e);
	}	
}
}