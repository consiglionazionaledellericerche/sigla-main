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

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.FormField;
/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 15.31.13)
 * @author: Roberto Fantino
 */
public class StampaRegistroAnnotazioneEntratePGiroAction extends it.cnr.contab.reports.action.ParametricPrintAction {
/**
 * StampaRegistroAnnotazioneEntratePGiroAction constructor comment.
 */
public StampaRegistroAnnotazioneEntratePGiroAction() {
	super();
}
public Forward doBlankSearchFindCdsOrigineForPrint(ActionContext context, Stampa_registro_annotazione_entrate_pgiroBulk stampa) {

	try {
        fillModel(context);
        FormField field = getFormField(context, "main.findCdsOrigineForPrint");        
		
        stampa.setUoForPrint(new Unita_organizzativaBulk());
        blankSearch(context, field, createEmptyModelForSearchTool(context, field));

        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
public Forward doBlankSearchFindUoForPrint(ActionContext context, Stampa_registro_annotazione_entrate_pgiroBulk stampa) {

	if (stampa!=null)
		stampa.setUoForPrint(new Unita_organizzativaBulk());
	return context.findDefaultForward();
}
public Forward doBringBackSearchFindCdsOrigineForPrint(ActionContext context, Stampa_registro_annotazione_entrate_pgiroBulk stampa, CdsBulk cdsOrigine) {

	//if (cdsOrigine!=null){
		//stampa.setCdsOrigineForPrint(cdsOrigine);
		//stampa.setUoForPrint(new Unita_organizzativaBulk());
	//}
	//return context.findDefaultForward();

	try {
	    it.cnr.contab.config00.sto.bulk.CdsBulk old_cds = stampa.getCdsOrigineForPrint();
        fillModel(context);
    
       if (cdsOrigine != null && old_cds!= null) {
	       stampa.setUoForPrint(new Unita_organizzativaBulk());
	       stampa.setCdsOrigineForPrint(cdsOrigine);
       }
       
       return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
public Forward doBringBackSearchFindUoForPrint(ActionContext context, Stampa_registro_annotazione_entrate_pgiroBulk stampa, Unita_organizzativaBulk uo) {

	if (uo!=null){
		stampa.setUoForPrint(uo);
	}
	return context.findDefaultForward();
}
}
