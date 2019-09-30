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

package it.cnr.contab.pdg00.bp;

import it.cnr.contab.pdg00.bulk.StampaRendFinCNRVBulk;
import it.cnr.contab.pdg00.cdip.bulk.Stampa_rendiconto_finanziarioVBulk;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;

/**
 * Insert the type's description here.
 * Creation date: (06/11/2003 16.21.29)
 * @author: Gennaro Borriello
 */
public class StampaRendicontoFinanziarioBP extends it.cnr.contab.reports.bp.OfflineReportPrintBP {

	private final String TYPE_CDS_E = "CDS_E";
	private final String TYPE_CDS_S = "CDS_S";
	private final String TYPE_SAC_S = "SAC_S";
/**
 * StampaRendicontoFinanziarioBP constructor comment.
 */
public StampaRendicontoFinanziarioBP() {
	super();
}
/**
 * StampaRendicontoFinanziarioBP constructor comment.
 * @param function java.lang.String
 */
public StampaRendicontoFinanziarioBP(String function) {
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

	// Controllo il tipo di report
	String type = config.getInitParameter("reportType");

	// Imposta il nome del report così come è impostato nel file RES
	setReportName(config.getInitParameter("reportName"));
		it.cnr.contab.reports.bp.ReportPrintBP printbp = (it.cnr.contab.reports.bp.ReportPrintBP)this;
	if (TYPE_CDS_E.equals(type) || TYPE_CDS_S.equals(type)){
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
	} else if (TYPE_SAC_S.equals(type)){
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("esercizio");
		param.setValoreParam(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);
	}
}
}
