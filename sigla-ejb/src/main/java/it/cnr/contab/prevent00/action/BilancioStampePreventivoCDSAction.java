package it.cnr.contab.prevent00.action;

import it.cnr.contab.pdg00.bulk.*;
import it.cnr.contab.reports.bp.*;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

/**
 * Action gestione stampe preventivo
 */

public class BilancioStampePreventivoCDSAction extends it.cnr.jada.util.action.BulkAction {
public BilancioStampePreventivoCDSAction() {
	super();
}
/**
 * Gestione della stampa del preventivo CDS parte entrate
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doStampaEntrata(ActionContext context) {
	try {
		
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/preventivo/bilancio_fin_cds_entrata.jasper");
		it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk preventivo = (it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk)bp.getModel();
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("esercizio");
		param.setValoreParam(preventivo.getEsercizio().toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("cds");
		param.setValoreParam(preventivo.getCd_cds());
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);				
		return context.addBusinessProcess(printbp);
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}
/**
 * Gestione della stampa del preventivo CDS parte entrate con importi diversi da zero
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doStampaEntrataDZ(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/preventivo/bilancio_fin_cds_entrata_dz.jasper");
		it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk preventivo = (it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk)bp.getModel();
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("esercizio");
		param.setValoreParam(preventivo.getEsercizio().toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("cds");
		param.setValoreParam(preventivo.getCd_cds());
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);		
		return context.addBusinessProcess(printbp);
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}
/**
 * Gestione della stampa del preventivo CDS parte spese
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doStampaSpesa(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/preventivo/bilancio_fin_cds_spesa.jasper");
		it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk preventivo = (it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk)bp.getModel();
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("esercizio");
		param.setValoreParam(preventivo.getEsercizio().toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("cds");
		param.setValoreParam(preventivo.getCd_cds());
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);		
		return context.addBusinessProcess(printbp);
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}
/**
 * Gestione della stampa del preventivo CDS parte spese con importi diversi da zero
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doStampaSpesaDZ(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/preventivo/bilancio_fin_cds_spesa_dz.jasper");
		it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk preventivo = (it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk)bp.getModel();
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("esercizio");
		param.setValoreParam(preventivo.getEsercizio().toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("cds");
		param.setValoreParam(preventivo.getCd_cds());
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);		
		return context.addBusinessProcess(printbp);
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}
}
