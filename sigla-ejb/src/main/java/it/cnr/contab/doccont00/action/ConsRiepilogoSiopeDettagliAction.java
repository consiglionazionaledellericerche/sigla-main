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

import it.cnr.contab.doccont00.bp.ConsDispCompetenzaResiduoIstitutoBP;
import it.cnr.contab.doccont00.bp.ConsRiepilogoSiopeMandatiBP;
import it.cnr.contab.doccont00.bp.ConsRiepilogoSiopeMandatiDettagliBP;
import it.cnr.contab.doccont00.bp.ConsRiepilogoSiopeReversaliDettagliBP;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_siope_mandatiBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_siope_reversaliBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;

public class ConsRiepilogoSiopeDettagliAction extends ConsultazioniAction{

	public Forward doConsultaMandatiDett(ActionContext context) {
		String livelloDestinazione = ConsRiepilogoSiopeMandatiDettagliBP.LIV_BASEDETT;
		try {
			ConsRiepilogoSiopeMandatiDettagliBP bp = (ConsRiepilogoSiopeMandatiDettagliBP)context.getBusinessProcess();
			bp.setSelection(context);
			long selectElements = bp.getSelection().size();

			if (selectElements == 0)
				selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
			
			if (selectElements == 0) {
				bp.setMessage("Non è stata selezionata nessuna riga.");
				return context.findDefaultForward();
			}

			ConsRiepilogoSiopeMandatiDettagliBP consultazioneBP = (ConsRiepilogoSiopeMandatiDettagliBP)context.createBusinessProcess("ConsRiepilogoSiopeMandatiDettagliBP");
			consultazioneBP.initVariabili(context, bp.getPathConsultazione(), livelloDestinazione);
			if ((bp.getElementsCount()!=selectElements)||!(bp.getBaseclause().toString().equals(consultazioneBP.getBaseclause().toString()))||bp.getFindclause()!=null)
				consultazioneBP.addToBaseclause(bp.getSelezione(context));
			V_cons_siope_mandatiBulk mandati = (V_cons_siope_mandatiBulk)bp.getModel();
			consultazioneBP.setIterator(context,bp.createConsRiepilogoSiopeComponentSession().findSiopeDettaglioMandati(context.getUserContext(),consultazioneBP.getPathConsultazione(),livelloDestinazione,consultazioneBP.getBaseclause(),bp.getFindclause(),mandati));
			return context.addBusinessProcess(consultazioneBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	
	public Forward doConsultaReversaliDett(ActionContext context) {
		String livelloDestinazione = ConsRiepilogoSiopeReversaliDettagliBP.LIV_BASEDETT;
		try {
			ConsRiepilogoSiopeReversaliDettagliBP bp = (ConsRiepilogoSiopeReversaliDettagliBP)context.getBusinessProcess();
			bp.setSelection(context);
			long selectElements = bp.getSelection().size();

			if (selectElements == 0)
				selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
			
			if (selectElements == 0) {
				bp.setMessage("Non è stata selezionata nessuna riga.");
				return context.findDefaultForward();
			}
			ConsRiepilogoSiopeReversaliDettagliBP consultazioneBP = (ConsRiepilogoSiopeReversaliDettagliBP)context.createBusinessProcess("ConsRiepilogoSiopeReversaliDettagliBP");
			consultazioneBP.initVariabili(context, bp.getPathConsultazione(), livelloDestinazione);
			if ((bp.getElementsCount()!=selectElements)||!(bp.getBaseclause().toString().equals(consultazioneBP.getBaseclause().toString()))||bp.getFindclause()!=null)
				consultazioneBP.addToBaseclause(bp.getSelezione(context));
			V_cons_siope_reversaliBulk reversali = (V_cons_siope_reversaliBulk)bp.getModel();
			consultazioneBP.setIterator(context,bp.createConsRiepilogoSiopeComponentSession().findSiopeDettaglioReversali(context.getUserContext(),consultazioneBP.getPathConsultazione(),livelloDestinazione,consultazioneBP.getBaseclause(),bp.getFindclause(),reversali));
			return context.addBusinessProcess(consultazioneBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}



/*	public Forward doConsultaDett(ActionContext context) {
		return doConsulta(context, ConsRiepilogoSiopeMandatiDettagliBP.LIV_BASEDETT);
		
	}*/

	
	public Forward doCloseForm(ActionContext context) throws BusinessProcessException {
		Forward appoForward = super.doCloseForm(context);
		if (context.getBusinessProcess() instanceof ConsRiepilogoSiopeMandatiDettagliBP){
			ConsRiepilogoSiopeMandatiDettagliBP bp = (ConsRiepilogoSiopeMandatiDettagliBP)context.getBusinessProcess();
			bp.setTitle();
		}
		if (context.getBusinessProcess() instanceof ConsRiepilogoSiopeReversaliDettagliBP){
			ConsRiepilogoSiopeReversaliDettagliBP bp = (ConsRiepilogoSiopeReversaliDettagliBP)context.getBusinessProcess();
			bp.setTitle();
		}
		return appoForward;
	}
	
}
