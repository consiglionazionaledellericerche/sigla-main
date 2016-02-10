package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.core.bulk.Stampa_elenco_progetti_laBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.reports.action.ParametricPrintAction;
import it.cnr.jada.action.ActionContext;
import it.cnr.contab.doccont00.bp.StampaElencoProgettiBP;
import it.cnr.jada.action.Forward;

public class StampaElencoProgettiAction extends ParametricPrintAction {
	
	public Forward doBlankSearchFindProgettoForPrint(ActionContext context, Stampa_elenco_progetti_laBulk stampa) {
		stampa.setProgettoForPrint(new ProgettoBulk());
		stampa.setCommessaForPrint(new ProgettoBulk());
		stampa.setModuloForPrint(new ProgettoBulk());
		return context.findDefaultForward();
	}
	public Forward doBringBackSearchFindProgettoForPrint(ActionContext context, Stampa_elenco_progetti_laBulk stampa, ProgettoBulk progetto) {
		if (progetto != null && progetto.getCd_progetto()!=null){
		  stampa.setProgettoForPrint(progetto);	
		  stampa.setCommessaForPrint(new ProgettoBulk());
		  stampa.setModuloForPrint(new ProgettoBulk());
		}		  
		return context.findDefaultForward();	
	}	
	
	public Forward doSearchFindProgettoForPrint(ActionContext actioncontext) {
		if (((StampaElencoProgettiBP)actioncontext.getBusinessProcess()).getParametriCnr().getFl_nuovo_pdg()){
			return search(actioncontext, getFormField(actioncontext, "main.findProgettoForPrint"),"filtro_ricerca_aree");
		}
		else
			return search(actioncontext ,getFormField(actioncontext, "main.findProgettoForPrint"),"filtro_ricerca_progetti");
	}
	public Forward doSearchFindCommessaForPrint(ActionContext actioncontext) {
		if (((StampaElencoProgettiBP)actioncontext.getBusinessProcess()).getParametriCnr().getFl_nuovo_pdg()){
			return search(actioncontext, getFormField(actioncontext, "main.findCommessaForPrint"),"filtro_ricerca_prog2");
		}
		else
			return search(actioncontext ,getFormField(actioncontext, "main.findCommessaForPrint"),"filtro_ricerca_commesse");
	}
}
