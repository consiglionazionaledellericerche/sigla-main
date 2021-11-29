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

package it.cnr.contab.reports.action;

import it.cnr.contab.reports.bp.*;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Action che gestisce le operazioni di CRUD (Create,Read,Update e Delete)
 * per un CRUDBusinessProcess.
 * <p>Implementa i seguenti comandi:
 * <ul>
 * <il> doCerca
 * <il> doElimina
 * <il> doNuovo
 * <il> doSalva
 * <il> doRiportaSelezione
 * </ul>
 * <code>CRUDAction</code> funziona solo se il business process corrente 
 * è un <code>CRUDBusinessProcess</code>.
 */
public class ParametricPrintAction extends BulkAction  implements java.io.Serializable {
public ParametricPrintAction() {
	super();
}
public Forward doPrint(ActionContext context) {
	ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
	try {
		fillModel(context);
		bp.completeSearchTools(context,bp.getController());
		bp.validate(context);
		bp.print(context,bp.getModel());
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess("OfflineReportPrintBP");
		printbp.setReportName(bp.getReportName());
		printbp.setRepotWhithDsOffLine( bp.getRepotWhitDsOffLine());
		for (Enumeration e = bp.getBulkInfo().getPrintFieldProperties(bp.getReportName());e.hasMoreElements();) {
			PrintFieldProperty printFieldProperty = (PrintFieldProperty)e.nextElement();
			Object value = printFieldProperty.getValueFrom(bp.getModel());
			Print_spooler_paramBulk param = new Print_spooler_paramBulk();
			if (bp.getReportName().endsWith("jasper")){
				param.setNomeParam(printFieldProperty.getParamNameJR());
				param.setParamType(printFieldProperty.getParamTypeJR());
			}else{
				param.setNomeParam("prompt"+printFieldProperty.getParameterPosition());
			}
			switch(printFieldProperty.getParamType()) {
				case PrintFieldProperty.TYPE_DATE:
					param.setValoreParam(ReportPrintBP.DATE_FORMAT.format((java.sql.Timestamp)value));
					break;
				case PrintFieldProperty.TYPE_TIMESTAMP:
					param.setValoreParam(ReportPrintBP.TIMESTAMP_FORMAT.format((java.sql.Timestamp)value));
					break;
				case PrintFieldProperty.TYPE_STRING:
				default:
					if (value == null)
						param.setValoreParam("");
					else
						param.setValoreParam(value.toString());
					break;
			}								
			printbp.addToPrintSpoolerParam(param);
		}
		bp.setDirty(false);
		return context.addBusinessProcess(printbp);
	} catch(ValidationException e) {
		bp.setErrorMessage(e.getMessage());
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}