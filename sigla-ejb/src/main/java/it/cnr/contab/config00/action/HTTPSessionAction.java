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

package it.cnr.contab.config00.action;

import java.rmi.RemoteException;

import it.cnr.contab.config00.bp.HTTPSessionBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.FormController;

public class HTTPSessionAction extends ConsultazioniAction {

	public Forward doEliminaSessioni(ActionContext actioncontext) throws RemoteException {
		try{
			fillModel(actioncontext);
			HTTPSessionBP bp = (HTTPSessionBP)actioncontext.getBusinessProcess();
			bp.saveSelection(actioncontext);
			if (bp.getSelectedElements(actioncontext).isEmpty()) {
				bp.setMessage("Non è stata selezionata nessuna riga.");
				return actioncontext.findDefaultForward();
			}							
			bp.disconnettiSessioni(actioncontext);
            bp.refresh(actioncontext);
            bp.setMessage("Sessioni terminate");
			return actioncontext.findDefaultForward();	
		}catch(Throwable throwable){
			 return handleException(actioncontext, throwable);
		}
	}

	public Forward doAggiorna(ActionContext actioncontext) throws RemoteException {
		try{
			HTTPSessionBP bp = (HTTPSessionBP)actioncontext.getBusinessProcess();
            bp.refresh(actioncontext);
			return actioncontext.findDefaultForward();	
		}catch(Throwable throwable){
			 return handleException(actioncontext, throwable);
		}
	}

}
