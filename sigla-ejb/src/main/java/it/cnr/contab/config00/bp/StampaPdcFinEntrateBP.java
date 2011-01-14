package it.cnr.contab.config00.bp;

import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.action.*;
/**
 * Insert the type's description here.
 * Creation date: (19/12/2002 16.22.13)
 * @author: Simonetta Costa
 */
public class StampaPdcFinEntrateBP extends it.cnr.contab.reports.bp.OfflineReportPrintBP {
/**
 * StampaPdcFinEntrateBP constructor comment.
 */
public StampaPdcFinEntrateBP() {
	super();
}
/**
 * StampaPdcFinEntrateBP constructor comment.
 * @param function java.lang.String
 */
public StampaPdcFinEntrateBP(String function) {
	super(function);
}
public void init(Config config, ActionContext context) throws BusinessProcessException{

	super.init(config, context);
	OfflineReportPrintBP printbp = (OfflineReportPrintBP)this;
	printbp.setReportName("/cnrconfigurazione/pdc/piano_dei_conti_finanziario_entrate_cnr_esercizio.jasper");
	Print_spooler_paramBulk param;
	param = new Print_spooler_paramBulk();
	param.setNomeParam("esercizio");
	param.setValoreParam(CNRUserInfo.getEsercizio(context).toString());
	param.setParamType("java.lang.Integer");
	printbp.addToPrintSpoolerParam(param);
}
}
