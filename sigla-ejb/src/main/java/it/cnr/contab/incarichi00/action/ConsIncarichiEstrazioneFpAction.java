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

package it.cnr.contab.incarichi00.action;


import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;

public class ConsIncarichiEstrazioneFpAction extends it.cnr.jada.util.action.ConsultazioniAction{
	public ConsIncarichiEstrazioneFpAction() {
		super();
	}

    public Forward doGeneraXML(ActionContext actioncontext) throws BusinessProcessException {
	    try
	    {
			SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.getBusinessProcess();
			selezionatorelistabp.saveSelection(actioncontext);
	    	HookForward forward = (HookForward)actioncontext.findForward("generaXML");
            forward.addParameter("selectedElements", ((ConsultazioniBP)actioncontext.getBusinessProcess()).getSelectedElements(actioncontext));
	        actioncontext.closeBusinessProcess();
	        return forward;
	    }
	    catch(Throwable throwable)
	    {
	        return handleException(actioncontext, throwable);
	    }
    }
}
