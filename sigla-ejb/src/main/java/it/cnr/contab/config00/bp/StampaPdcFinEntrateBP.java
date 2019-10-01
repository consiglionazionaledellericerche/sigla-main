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
