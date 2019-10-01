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

package it.cnr.contab.preventvar00.action;
import java.rmi.RemoteException;

import javax.ejb.RemoveException;

import it.cnr.contab.doccont00.bp.ConsDispCompResDipIstBP;
import it.cnr.contab.doccont00.bp.ConsDispCompResIstCdrGaeBP;
import it.cnr.contab.doccont00.bp.ConsDispCompResIstVoceBP;
import it.cnr.contab.doccont00.bp.ConsDispCompResVoceNatBP;
import it.cnr.contab.doccont00.bp.ConsDispCompetenzaResiduoIstitutoBP;
import it.cnr.contab.preventvar00.consultazioni.bp.ConsAssCompPerDataBP;
import it.cnr.contab.preventvar00.consultazioni.bp.ConsAssCompPerDataDettagliBP;
import it.cnr.contab.preventvar00.consultazioni.bp.ConsVarStanzCompetenzaBP;
import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_ass_comp_per_dataBulk;
import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_var_pdggBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.BulkAction;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;


public class ConsAssCompPerDataDettagliAction extends ConsultazioniAction {



public Forward doConsulta(ActionContext context, String livelloDestinazione) {
	try {
		ConsAssCompPerDataDettagliBP bp = (ConsAssCompPerDataDettagliBP)context.getBusinessProcess();
		bp.setSelection(context);
		long selectElements = bp.getSelection().size();
		if (selectElements == 0)
			selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
		
		if (selectElements == 0) {
			bp.setMessage("Non è stata selezionata nessuna riga.");
			return context.findDefaultForward();
		}
		ConsAssCompPerDataDettagliBP consultazioneBP = (ConsAssCompPerDataDettagliBP)context.createBusinessProcess("ConsAssCompPerDataDettagliBP");
		consultazioneBP.initVariabili(context, bp.getPathConsultazione(), livelloDestinazione);
		if ((bp.getElementsCount()!=selectElements)||!(bp.getBaseclause().toString().equals(consultazioneBP.getBaseclause().toString()))||bp.getFindclause()!=null)
			consultazioneBP.addToBaseclause(bp.getSelezione(context));
		V_cons_ass_comp_per_dataBulk assestato = (V_cons_ass_comp_per_dataBulk)bp.getModel();
		consultazioneBP.setIterator(context,bp.createConsAssCompPerDataComponentSession().findVariazioniDettaglio(context.getUserContext(),consultazioneBP.getPathConsultazione(),livelloDestinazione,consultazioneBP.getBaseclause(),bp.getFindclause(),assestato));
		return context.addBusinessProcess(consultazioneBP);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}


public Forward doConsultaVarPiu(ActionContext context) {
	return doConsulta(context, ConsAssCompPerDataDettagliBP.LIV_BASEVARPIU);
	
}

public Forward doConsultaVarMeno(ActionContext context) {
	return doConsulta(context, ConsAssCompPerDataDettagliBP.LIV_BASEVARMENO);
	
}

public Forward doConsultaStanziamento(ActionContext context) {
	return doConsulta(context, ConsAssCompPerDataDettagliBP.LIV_BASESTANZ);
	
}

public Forward doCloseForm(ActionContext context) throws BusinessProcessException {
		ConsAssCompPerDataDettagliBP bp = (ConsAssCompPerDataDettagliBP)context.getBusinessProcess();
		bp.setTitle();
	return super.doCloseForm(context);
}


}