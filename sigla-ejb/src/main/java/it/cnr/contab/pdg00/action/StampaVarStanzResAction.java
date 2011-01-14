/*
 * Created on Oct 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.action;

import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Stampa_var_stanz_resBulk;
import it.cnr.contab.pdg00.bulk.Var_stanz_resBulk;
import it.cnr.contab.reports.action.ParametricPrintAction;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.contab.reports.bp.PrintSpoolerBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;



public class StampaVarStanzResAction extends ParametricPrintAction {

	public Forward doPrint(ActionContext context) {
		ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
		Forward forward = super.doPrint(context);
		if (context.getBusinessProcess() instanceof OfflineReportPrintBP){
			OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.getBusinessProcess();
			Stampa_var_stanz_resBulk pbulk = (Stampa_var_stanz_resBulk) bp.getModel();
			Var_stanz_resBulk bulk = pbulk.getVariazioneForPrint();
			// impostazone dell'ufficio di competenza della stampa, utile all'invio della PEC
			printbp.initCdServizioPEC(PrintSpoolerBP.PEC_BILANCIO);
			// impostazone della descrizione del documento, utile all'invio della PEC
			String ds = ("Variazione allo Stanziamento Residuo "+bulk.getEsercizio()+"/"+bulk.getCd_centro_responsabilita()+"/"+bulk.getPg_variazione()+", "+bulk.getDs_variazione());
			if (ds.length()>400)
				ds=ds.substring(0, 399);
			printbp.initDsOggettoPEC(ds);

		}
		return forward;
	}
}
