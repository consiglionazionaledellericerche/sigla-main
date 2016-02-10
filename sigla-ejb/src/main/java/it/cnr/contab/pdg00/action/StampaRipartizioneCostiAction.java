/*
 * Created on Apr 5, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.action;

import it.cnr.contab.pdg00.bp.StampaRipartizioneCostiBP;
import it.cnr.contab.pdg00.cdip.bulk.Stampa_ripartizione_costiVBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.reports.action.ParametricPrintAction;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StampaRipartizioneCostiAction extends ParametricPrintAction {
	
	public Forward doBlankSearchFindCommessaForPrint(ActionContext context, Stampa_ripartizione_costiVBulk stampa) {
		stampa.setCommessaForPrint(new ProgettoBulk());
		stampa.setModuloForPrint(new ProgettoBulk());
		return context.findDefaultForward();
	}
	public Forward doBringBackSearchFindCommessaForPrint(ActionContext context, Stampa_ripartizione_costiVBulk stampa, ProgettoBulk commessa) {
		if (commessa != null && commessa.getCd_progetto()!=null){
		  stampa.setCommessaForPrint(commessa);	
		  stampa.setModuloForPrint(new ProgettoBulk());
		}		  
		return context.findDefaultForward();	
	}	
	
	public Forward doSearchFindCommessaForPrint(ActionContext actioncontext) {
		if (((StampaRipartizioneCostiBP)actioncontext.getBusinessProcess()).getParametriCnr().getFl_nuovo_pdg()){
			return search(actioncontext, getFormField(actioncontext, "main.findCommessaForPrint"),"filtro_ricerca_aree");
		}
		else
			return search(actioncontext ,getFormField(actioncontext, "main.findCommessaForPrint"),"filtro_ricerca_commesse");
	}
	public Forward doSearchFindModuloForPrint(ActionContext actioncontext) {
		if (((StampaRipartizioneCostiBP)actioncontext.getBusinessProcess()).getParametriCnr().getFl_nuovo_pdg()){
			return search(actioncontext, getFormField(actioncontext, "main.findModuloForPrint"),"filtro_ricerca_prog2");
		}
		else
			return search(actioncontext ,getFormField(actioncontext, "main.findModuloForPrint"),"filtro_ricerca_moduli");
	}
}
