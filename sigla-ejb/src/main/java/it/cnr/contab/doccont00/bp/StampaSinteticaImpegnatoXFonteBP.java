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
