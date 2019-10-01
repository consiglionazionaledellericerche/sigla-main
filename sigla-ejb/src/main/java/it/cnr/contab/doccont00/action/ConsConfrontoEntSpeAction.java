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


import it.cnr.contab.doccont00.bp.ConsConfrontoEntSpeBP;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_confronto_ent_speBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;

public class ConsConfrontoEntSpeAction extends ConsultazioniAction{

	public Forward doConsulta(ActionContext context, String livelloDestinazione) {
		try {
			ConsConfrontoEntSpeBP bp = (ConsConfrontoEntSpeBP)context.getBusinessProcess();
			bp.setSelection(context);
			long selectElements = bp.getSelection().size();

			if (selectElements == 0)
				selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
			
			if (selectElements == 0) {
				bp.setMessage("Non è stata selezionata nessuna riga.");
				return context.findDefaultForward();
			}
			ConsConfrontoEntSpeBP consultazioneBP = (ConsConfrontoEntSpeBP)context.createBusinessProcess("ConsConfrontoEntSpeBP");
			consultazioneBP.initVariabili(context, bp.getPathConsultazione(), livelloDestinazione);
			if ((bp.getElementsCount()!=selectElements)||!(bp.getBaseclause().toString().equals(consultazioneBP.getBaseclause().toString()))||bp.getFindclause()!=null)
				consultazioneBP.addToBaseclause(bp.getSelezione(context));
				consultazioneBP.setIterator(context,bp.createConsConfrontoEntSpeComponentSession().findConsultazioneModulo(context.getUserContext(),consultazioneBP.getPathConsultazione(),livelloDestinazione,consultazioneBP.getBaseclause(),bp.getFindclause()));
			return context.addBusinessProcess(consultazioneBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}


	public Forward doConsultaModulo(ActionContext context) {
		return doConsulta(context, ConsConfrontoEntSpeBP.LIV_BASEMOD);
	}
	public Forward doConsultaGae(ActionContext context) {
		return doConsulta(context, ConsConfrontoEntSpeBP.LIV_BASEMODGAE);
		}
	public Forward doConsultaVoce(ActionContext context) {
		return doConsulta(context, ConsConfrontoEntSpeBP.LIV_BASEMODGAEVOCE);
	}
	/*public Forward doConsultaDett(ActionContext context) {
		return doConsulta(context, ConsConfrontoEntSpeBP.LIV_BASEMODGAEVOCEDET);
	}*/
	
	
	
	
	public Forward doCloseForm(ActionContext context) throws BusinessProcessException {
		Forward appoForward = super.doCloseForm(context);
		if (context.getBusinessProcess() instanceof ConsConfrontoEntSpeBP) {
			ConsConfrontoEntSpeBP bp = (ConsConfrontoEntSpeBP)context.getBusinessProcess();
			bp.setTitle();
		}
		return appoForward;
	}
}
