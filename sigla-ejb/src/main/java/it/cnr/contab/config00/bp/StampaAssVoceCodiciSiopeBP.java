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
