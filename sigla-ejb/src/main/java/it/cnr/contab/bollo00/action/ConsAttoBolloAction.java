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
 * Created on Nov 9, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.bollo00.action;

import java.rmi.RemoteException;

import it.cnr.contab.bollo00.bp.ConsAttoBolloBP;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;

public class ConsAttoBolloAction extends ConsultazioniAction {
	private static final long serialVersionUID = 1L;

	public Forward doConsulta(ActionContext context, String livelloDestinazione) {
		try {
			ConsAttoBolloBP bp = (ConsAttoBolloBP)context.getBusinessProcess();
			bp.setSelection(context);
			long selectElements = bp.getSelection().size();

			if (selectElements == 0)
				selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
			
			if (selectElements == 0) {
				bp.setMessage("Non è stata selezionata nessuna riga.");
				return context.findDefaultForward();
			}
			ConsAttoBolloBP consultazioneBP = (ConsAttoBolloBP)context.createBusinessProcess("ConsAttoBolloBP");
			context.closeBusinessProcess(bp);
			
			consultazioneBP.initVariabili(context, bp.getPathConsultazione(), livelloDestinazione);
			if ((bp.getElementsCount()!=selectElements)||!(bp.getBaseclause().toString().equals(consultazioneBP.getBaseclause().toString()))||bp.getFindclause()!=null)
				consultazioneBP.addToBaseclause(bp.getSelezione(context));
			consultazioneBP.setIterator(context,Utility.createAttoBolloComponentSession().findConsultazioneDettaglio(context.getUserContext(),bp.getPathDestinazione(livelloDestinazione),livelloDestinazione,consultazioneBP.getBaseclause(),null,false));			
			
			return context.addBusinessProcess(consultazioneBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doConsultaUo(ActionContext context) {
		return doConsulta(context, ConsAttoBolloBP.LIVELLO_UO);
	}
	public Forward doConsultaTipoAtto(ActionContext context) {
		return doConsulta(context, ConsAttoBolloBP.LIVELLO_TIP);
	}
	public Forward doConsultaDettagli(ActionContext context) {
		return doConsulta(context, ConsAttoBolloBP.LIVELLO_DET);
	}
	public Forward doCloseForm(ActionContext context) throws BusinessProcessException {
		Forward appoForward = super.doCloseForm(context);
		if (context.getBusinessProcess() instanceof ConsAttoBolloBP) {
			ConsAttoBolloBP bp = (ConsAttoBolloBP)context.getBusinessProcess();
			bp.setTitle();
		}
		return appoForward;
	}
	public Forward doCambiaVisibilita(ActionContext actioncontext)
			throws RemoteException {
		ConsAttoBolloBP bp = (ConsAttoBolloBP)actioncontext.getBusinessProcess();
		try {
			fillModel(actioncontext);
			bp.cambiaVisibilita(actioncontext);
			return actioncontext.findDefaultForward();
		} catch(Throwable e) {
			return handleException(actioncontext,e);
		}
	}
}