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

package it.cnr.contab.prevent00.action;
import it.cnr.contab.reports.bp.*;

/**
 * Adatta e implementa la {@link it.cnr.jada.util.action.CRUDAction } per le
 * 		funzionalità del dettaglio Entrate
 * 
 * @author: Bisquadro Vincenzo
 */
public class CRUDEtrDetPdGAggregatoAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * Costruttore standard di CRUDEtrDetPdGAggregatoAction.
 */
public CRUDEtrDetPdGAggregatoAction() {
	super();
}

/**
 * Stampa del dell'aggregato PDG parte entrate
 */
public it.cnr.jada.action.Forward doPrint(it.cnr.jada.action.ActionContext context) {
	try {
		it.cnr.jada.util.action.BulkBP bp = (it.cnr.jada.util.action.BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/pdg/pdg_aggregato_etr.rpt");
		it.cnr.contab.prevent00.bulk.Pdg_aggregato_etr_detBulk detEtrBulk = (it.cnr.contab.prevent00.bulk.Pdg_aggregato_etr_detBulk)bp.getModel();
		printbp.setReportParameter(0,detEtrBulk.getEsercizio().toString());
		printbp.setReportParameter(1,detEtrBulk.getCd_centro_responsabilita());
		return context.addBusinessProcess(printbp);
	} catch(it.cnr.jada.action.BusinessProcessException e) {
		throw new it.cnr.jada.action.ActionPerformingError(e);
	}	
}
}