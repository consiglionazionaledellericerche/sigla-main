package it.cnr.contab.config00.bp;

import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.*;
/**
 * Insert the type's description here.
 * Creation date: (08/06/2007)
 * @author: fgiardina
 */
public class StampaAssVoceCodiciSiopeBP extends OfflineReportPrintBP {
	protected void init(Config config, ActionContext context) throws BusinessProcessException {	
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("Esercizio");
		param.setValoreParam(CNRUserContext.getEsercizio(context.getUserContext()).toString());
		param.setParamType("java.lang.Integer");
		addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("Ti_gestione");
		
		if (this.getName().equals("StampaAssVoceCodiciSiopeEntrateBP"))
			param.setValoreParam("E");
		if (this.getName().equals("StampaAssVoceCodiciSiopeSpeseBP"))
			param.setValoreParam("S");
		
		param.setParamType("java.lang.String");
		addToPrintSpoolerParam(param);
        setReportName(config.getInitParameter("reportName"));
		super.init(config, context);
		
	}
}
