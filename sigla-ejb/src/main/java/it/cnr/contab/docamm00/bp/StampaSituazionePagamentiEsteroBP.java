package it.cnr.contab.docamm00.bp;

import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;

/**
 * Insert the type's description here.
 * Creation date: (03/02/2003 15.06.54)
 * @author: CNRADM
 */
public class StampaSituazionePagamentiEsteroBP extends it.cnr.contab.reports.bp.OfflineReportPrintBP {
/**
 * StampaSituazionePagamentiEsteroBP constructor comment.
 */
public StampaSituazionePagamentiEsteroBP() {
	super();
}
/**
 * StampaSituazionePagamentiEsteroBP constructor comment.
 * @param function java.lang.String
 */
public StampaSituazionePagamentiEsteroBP(String function) {
	super(function);
}
/**
 * Insert the method's description here.
 * Creation date: (03/02/2003 15.15.56)
 * @param config it.cnr.jada.util.Config
 * @param context it.cnr.jada.action.ActionContext
 */
public void init(it.cnr.jada.action.Config config, it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config, context);
	OfflineReportPrintBP printbp = (OfflineReportPrintBP) this;
	printbp.setReportName("/cnrdoccont/doccont/pagamenti_estero.jasper");
	Print_spooler_paramBulk param;
	param = new Print_spooler_paramBulk();
	param.setNomeParam("esercizio");
	param.setValoreParam(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).toString());
	param.setParamType("java.lang.Integer");
	printbp.addToPrintSpoolerParam(param);
	
	param = new Print_spooler_paramBulk();
	param.setNomeParam("cds");
	param.setValoreParam(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(context.getUserContext()).toString());
	param.setParamType("java.lang.String");
	printbp.addToPrintSpoolerParam(param);
}
}
