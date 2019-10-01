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
