/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.config00.bp;

import it.cnr.contab.logs.bulk.Batch_log_tstaBulk;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bp.ReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.AbstractPrintBP;
/**
 * Insert the type's description here.
 * Creation date: (19/12/2002 16.50.52)
 * @author: Simonetta Costa
 */
public class StampaPdcEconomicoBP extends it.cnr.contab.reports.bp.OfflineReportPrintBP {
/**
 * StampaPdcEconomicoBP constructor comment.
 */
public StampaPdcEconomicoBP() {
	super();
}
/**
 * StampaPdcEconomicoBP constructor comment.
 * @param function java.lang.String
 */
public StampaPdcEconomicoBP(String function) {
	super(function);
}
public void init(Config config, ActionContext context) throws BusinessProcessException{

	super.init(config, context);
	OfflineReportPrintBP printbp = (OfflineReportPrintBP)this;
	printbp.setReportName("/cnrconfigurazione/pdc/piano_dei_conti_economico.jasper");
//	setReportParameter(0, CNRUserInfo.getEsercizio(context).toString());
	Print_spooler_paramBulk param;
    param = new Print_spooler_paramBulk();
    param.setNomeParam("Esercizio");
    param.setValoreParam(CNRUserInfo.getEsercizio(context).toString());
    param.setParamType("java.lang.Integer");
    printbp.addToPrintSpoolerParam(param);
}



/*protected void initializePrintBP(AbstractPrintBP abstractprintbp)
{
    ReportPrintBP reportprintbp = (ReportPrintBP)abstractprintbp;
    reportprintbp.setReportName("/cnrconfigurazione/pdc/piano_dei_conti_economico.jasper");

    Print_spooler_paramBulk param;
    param = new Print_spooler_paramBulk();
    param.setNome_param("Esercizio");
    param.setValore_param(CNRUserInfo.getEsercizio(context).toString());
    param.setParam_type("java.lang.Integer");
    reportprintbp.addToPrintSpoolerParam(param);

}*/
}
