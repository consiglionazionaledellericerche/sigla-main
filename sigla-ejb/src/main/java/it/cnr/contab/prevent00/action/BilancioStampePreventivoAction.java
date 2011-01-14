package it.cnr.contab.prevent00.action;
import it.cnr.contab.pdg00.bulk.*;
import it.cnr.contab.reports.bp.*;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

/**
 * Action gestione stampe preventivo
 */

public class BilancioStampePreventivoAction extends it.cnr.jada.util.action.BulkAction {
public BilancioStampePreventivoAction() {
	super();
}

/**
 * Gestione della stampa del preventivo parte entrate
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doStampaEntrataArticolo(ActionContext context) {
	try {
	
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/preventivo/bilancio_fin_cnr_entrata.jasper");
		it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk preventivo = (it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk)bp.getModel();
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("esercizio");
		param.setValoreParam(preventivo.getEsercizio().toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("controllo");
		param.setValoreParam("A");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);		
		
		return context.addBusinessProcess(printbp);
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}

/**
 * Gestione della stampa del preventivo parte entrate  con importi diversi da zero
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doStampaEntrataArticoloDZ(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/preventivo/bilancio_fin_cnr_entrata_dz.jasper");
		it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk preventivo = (it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk)bp.getModel();
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("esercizio");
		param.setValoreParam(preventivo.getEsercizio().toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("controllo");
		param.setValoreParam("A");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);		
		return context.addBusinessProcess(printbp);
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}

/**
 * Gestione della stampa del preventivo parte entrate
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doStampaEntrataCapitolo(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/preventivo/bilancio_fin_cnr_entrata.jasper");
		it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk preventivo = (it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk)bp.getModel();
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("esercizio");
		param.setValoreParam(preventivo.getEsercizio().toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("controllo");
		param.setValoreParam("C");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);		
		
		return context.addBusinessProcess(printbp);
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}

/**
 * Gestione della stampa del preventivo parte entrate con importi diversi da zero
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doStampaEntrataCapitoloDZ(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/preventivo/bilancio_fin_cnr_entrata_dz.jasper");
		it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk preventivo = (it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk)bp.getModel();
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("esercizio");
		param.setValoreParam(preventivo.getEsercizio().toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("controllo");
		param.setValoreParam("C");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);		
		return context.addBusinessProcess(printbp);
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}

/**
 *
 * Gestione della stampa del preventivo parte entrate
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doStampaEntrataSottoarticolo(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/preventivo/bilancio_fin_cnr_entrata.jasper");
		it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk preventivo = (it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk)bp.getModel();
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("esercizio");
		param.setValoreParam(preventivo.getEsercizio().toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("controllo");
		param.setValoreParam("E");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);		
		return context.addBusinessProcess(printbp);
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}

/**
 *
 * Gestione della stampa del preventivo parte entrate con importi diversi da zero
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doStampaEntrataSottoarticoloDZ(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/preventivo/bilancio_fin_cnr_entrata_dz.jasper");
		it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk preventivo = (it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk)bp.getModel();
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("esercizio");
		param.setValoreParam(preventivo.getEsercizio().toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("controllo");
		param.setValoreParam("E");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);		
		
		return context.addBusinessProcess(printbp);
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}

/**
 * Gestione della stampa del preventivo parte spese
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doStampaSpesaCapitolo(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/preventivo/bilancio_fin_cnr_spesa.jasper");
		it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk preventivo = (it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk)bp.getModel();
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("esercizio");
		param.setValoreParam(preventivo.getEsercizio().toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("controllo");
		param.setValoreParam("C");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);		
		return context.addBusinessProcess(printbp);
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}

/**
 * Gestione della stampa del preventivo parte spese con importi diversi da zero
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doStampaSpesaCapitoloDZ(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/preventivo/bilancio_fin_cnr_spesa_dz.jasper");
		it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk preventivo = (it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk)bp.getModel();
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("esercizio");
		param.setValoreParam(preventivo.getEsercizio().toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("controllo");
		param.setValoreParam("C");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);		
		return context.addBusinessProcess(printbp);
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}

/**
 * Gestione della stampa del preventivo parte spese
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doStampaSpesaSottoarticolo(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/preventivo/bilancio_fin_cnr_spesa.jasper");
		it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk preventivo = (it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk)bp.getModel();
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("esercizio");
		param.setValoreParam(preventivo.getEsercizio().toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("controllo");
		param.setValoreParam("E");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);		
		return context.addBusinessProcess(printbp);
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}

/**
 * Gestione della stampa del preventivo parte spese con importi diversi da zero
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doStampaSpesaSottoarticoloDZ(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/preventivo/bilancio_fin_cnr_spesa_dz.jasper");
		it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk preventivo = (it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk)bp.getModel();
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("esercizio");
		param.setValoreParam(preventivo.getEsercizio().toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("controllo");
		param.setValoreParam("E");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
		
		return context.addBusinessProcess(printbp);
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}
}