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
