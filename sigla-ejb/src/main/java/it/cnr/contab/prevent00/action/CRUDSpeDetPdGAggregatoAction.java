package it.cnr.contab.prevent00.action;
import it.cnr.contab.reports.bp.*;

/**
 * Adatta e implementa la {@link it.cnr.jada.util.action.CRUDAction } per le
 * 		funzionalità offerte dal Dettaglio Spese
 * 
 * @author: Bisquadro Vincenzo
 */
public class CRUDSpeDetPdGAggregatoAction extends it.cnr.jada.util.action.CRUDAction {

/**
 * Costruttore standard di CRUDSpeDetPdGAggregatoAction.
 */
public CRUDSpeDetPdGAggregatoAction() {
	super();
}

/**
 * Stampa del dell'aggregato PDG parte spese
 */
public it.cnr.jada.action.Forward doPrint(it.cnr.jada.action.ActionContext context) {
	try {
		it.cnr.jada.util.action.BulkBP bp = (it.cnr.jada.util.action.BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/pdg/pdg_aggregato_spe.rpt");
		it.cnr.contab.prevent00.bulk.Pdg_aggregato_spe_detBulk detSpeBulk = (it.cnr.contab.prevent00.bulk.Pdg_aggregato_spe_detBulk)bp.getModel();
		printbp.setReportParameter(0,detSpeBulk.getEsercizio().toString());
		printbp.setReportParameter(1,detSpeBulk.getCd_centro_responsabilita());
		return context.addBusinessProcess(printbp);
	} catch(it.cnr.jada.action.BusinessProcessException e) {
		throw new it.cnr.jada.action.ActionPerformingError(e);
	}
}
}