/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.ordmag.magazzino.action;

import it.cnr.contab.docamm00.bp.DocumentiAmministrativiFatturazioneElettronicaBP;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.SelezionatoreListaAction;

public class ListaBolleScaricoGenerateAction extends SelezionatoreListaAction {
		/**
		 * DocumentiAmministrativiProtocollabiliAction constructor comment.
		 */
		public ListaBolleScaricoGenerateAction() {
			super();
		}
		public Forward basicDoBringBack(ActionContext actioncontext)
				throws BusinessProcessException
			{
				return null;
			}

		private Forward doStampaBollaScarico(
			ActionContext context,
			BollaScaricoMagBulk bolla)
			throws BusinessProcessException {

			DocumentiAmministrativiFatturazioneElettronicaBP bp = (DocumentiAmministrativiFatturazioneElettronicaBP)context.getBusinessProcess();
			OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp(), new Object[] { "Th" });
			printbp.setReportName("/docamm/docamm/fatturaattiva_ncd.jasper");
			Print_spooler_paramBulk param;
			param = new Print_spooler_paramBulk();
			param.setNomeParam("Ti_stampa");
			param.setValoreParam("S");
			param.setParamType("java.lang.String");
			printbp.addToPrintSpoolerParam(param);
			
			param = new Print_spooler_paramBulk();
			param.setNomeParam("id_report");
//			param.setValoreParam(filtro.getPgStampa().toString());
			param.setParamType("java.lang.Long");
			printbp.addToPrintSpoolerParam(param);
			
			context.addHookForward("close", this, "doStampaAnnullata");
			printbp.setMessage(
				it.cnr.jada.util.action.OptionBP.MESSAGE,
				"Il protocollo IVA è stato assegnato correttamente. Per confermare eseguire la stampa.");

			return context.addBusinessProcess(printbp);
		}

}
