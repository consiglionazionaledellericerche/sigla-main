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
