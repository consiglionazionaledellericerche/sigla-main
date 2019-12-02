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

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.Stampa_registro_accertamentiBulk;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.FormField;
/**
 * Insert the type's description here.
 * Creation date: (28/03/2003 15.04.50)
 * @author: Gennaro Borriello
 */
public class Stampa_registro_accertamentiAction extends it.cnr.contab.reports.action.ParametricPrintAction {
/**
 * Stampa_registro_accertamentiAction constructor comment.
 */
public Stampa_registro_accertamentiAction() {
	super();
}
/**
 * Gestisce .......
 */

public Forward doBlankSearchFindCdsForPrint(ActionContext context, Stampa_registro_accertamentiBulk stampa) {

    try {
        fillModel(context);
        FormField field = getFormField(context, "main.findCdsForPrint");        
		
        stampa.setUo_cds_origine(new Unita_organizzativaBulk());
        blankSearch(context, field, createEmptyModelForSearchTool(context, field));

        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
/**
 * Gestisce .......
 */
				 
public Forward doBringBackSearchFindCdsForPrint(ActionContext context, Stampa_registro_accertamentiBulk stampa, it.cnr.contab.config00.sto.bulk.CdsBulk cds) {

    try {
	    it.cnr.contab.config00.sto.bulk.CdsBulk old_cds = stampa.getCds_origine();
        fillModel(context);
    
       if (cds != null && old_cds!= null) {
	       stampa.setUo_cds_origine(new Unita_organizzativaBulk());
	       stampa.setCds_origine(cds);
       }
       
       return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}

}
