package it.cnr.contab.doccont00.bp;

import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;

public class StampaSinteticaImpegnatoXFonteBP extends OfflineReportPrintBP {
	protected void init(Config config, ActionContext context) throws BusinessProcessException {
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("Esercizio");
		param.setValoreParam(CNRUserContext.getEsercizio(context.getUserContext()).toString());
		param.setParamType("java.lang.Integer");
		addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("CDR");
		param.setValoreParam(CNRUserContext.getCd_cdr(context.getUserContext()));
		param.setParamType("java.lang.String");
		addToPrintSpoolerParam(param);
        setReportName(config.getInitParameter("reportName"));
		super.init(config, context);
	}
}
