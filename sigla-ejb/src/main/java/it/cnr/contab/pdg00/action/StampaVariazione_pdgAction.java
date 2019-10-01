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

/*
 * Created on Oct 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.action;

import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Stampa_pdg_variazioneBulk;
import it.cnr.contab.reports.action.ParametricPrintAction;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.contab.reports.bp.PrintSpoolerBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;



public class StampaVariazione_pdgAction extends ParametricPrintAction {

	public Forward doPrint(ActionContext context) {
		ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
		Forward forward = super.doPrint(context);
		if (context.getBusinessProcess() instanceof OfflineReportPrintBP){
			OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.getBusinessProcess();
			Stampa_pdg_variazioneBulk pbulk = (Stampa_pdg_variazioneBulk) bp.getModel();
			Pdg_variazioneBulk bulk = pbulk.getPdg_variazioneForPrint();
			// impostazone dell'ufficio di competenza della stampa, utile all'invio della PEC
			printbp.initCdServizioPEC(PrintSpoolerBP.PEC_BILANCIO);
			// impostazone della descrizione del documento, utile all'invio della PEC
			String ds = ("Variazione al PdG di competenza "+bulk.getEsercizio()+"/"+bulk.getCd_centro_responsabilita()+"/"+bulk.getPg_variazione_pdg()+", "+bulk.getDs_variazione());
			if (ds.length()>400)
				ds=ds.substring(0, 399);
			printbp.initDsOggettoPEC(ds);
			printbp.initDsNumregPEC(bulk.getPg_variazione_pdg()+"-"+bulk.getEsercizio());

		}
		return forward;
	}
}
