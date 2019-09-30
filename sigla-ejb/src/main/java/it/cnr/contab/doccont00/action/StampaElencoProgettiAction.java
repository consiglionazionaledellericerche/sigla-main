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
