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
/**
 * Business Process per la gestione della stampa dello stato dei PDG
 */
 
public class PdgStampaStatoBP extends it.cnr.contab.reports.bp.OfflineReportPrintBP {
public PdgStampaStatoBP() {
	super();
}

public PdgStampaStatoBP(String function) {
	super();
}

protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);

	setReportName("/preventivo/pdg/pdg_stato.rpt");
	
	setReportParameter(0, it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context).toString());
	setReportParameter(1, ((it.cnr.contab.utenze00.bulk.CNRUserInfo)context.getUserInfo()).getCdr().getCd_centro_responsabilita());
}
}