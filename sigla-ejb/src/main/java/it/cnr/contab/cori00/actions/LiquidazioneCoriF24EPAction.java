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

package it.cnr.contab.cori00.actions;

import it.cnr.contab.cori00.bp.CRUDLiquidazioneCORIBP;
import it.cnr.contab.cori00.bp.LiquidazioneCoriF24EPBP;
import it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;


public class LiquidazioneCoriF24EPAction extends it.cnr.jada.util.action.SelezionatoreListaAction {

	public LiquidazioneCoriF24EPAction() {
	}
	public Forward doVisualizzaDettagli(ActionContext context)throws BusinessProcessException {	

		LiquidazioneCoriF24EPBP bp = (LiquidazioneCoriF24EPBP)context.getBusinessProcess();

		Liquid_coriBulk liq = (Liquid_coriBulk)bp.getFocusedElement();
		if (liq == null) {
			bp.setMessage("E' necessario selezionare una liquidazione.");
			return context.findDefaultForward();
		}

		if (liq.getCd_cds() == null ||
			liq.getCd_unita_organizzativa() == null ||
			liq.getEsercizio() == null ||
			liq.getPg_liquidazione() == null) {
			bp.setMessage("E' necessario selezionare una liquidazione.");
			return context.findDefaultForward();
		}
			
		CRUDLiquidazioneCORIBP liqBP = CRUDLiquidazioneCORIBP.getBusinessProcessFor(context, liq, "VRSWTh");
		
		liqBP.edit(context,liq);

		return context.addBusinessProcess(liqBP);
	}
	public Forward doBringBack(ActionContext context) {
		return context.findDefaultForward();
	}
	public Forward doF24EPTot(ActionContext context) throws BusinessProcessException {
		LiquidazioneCoriF24EPBP bp = (LiquidazioneCoriF24EPBP)context.getBusinessProcess();
		bp.saveSelection(context);
		long selectElements = bp.getSelection().size();

		if (selectElements == 0)
			selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
		
		if (selectElements == 0) {
			bp.setMessage("E' necessario selezionare almeno una liquidazione.");
			return context.findDefaultForward();
		}
		 try {
			bp.EstrazioneTot(context);
		} catch(Throwable e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}
	public Forward doReset(ActionContext context) {

		try {
			LiquidazioneCoriF24EPBP bp= (LiquidazioneCoriF24EPBP) context.getBusinessProcess();
			bp.refresh(context);

			return context.findDefaultForward();
		} catch (Throwable e) {
			return handleException(context, e);
		}
	}
}
