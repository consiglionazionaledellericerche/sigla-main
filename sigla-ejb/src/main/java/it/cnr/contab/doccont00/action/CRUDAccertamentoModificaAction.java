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

import it.cnr.contab.doccont00.bp.CRUDAccertamentoModificaBP;
import it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.varstanz00.bp.CRUDVar_stanz_resBP;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;

public class CRUDAccertamentoModificaAction extends it.cnr.jada.util.action.CRUDAction {

	public Forward doRiporta(ActionContext context) 
	{
		try 
		{
			fillModel(context);
		    CRUDAccertamentoModificaBP bp = (CRUDAccertamentoModificaBP) getBusinessProcess(context);
		    Accertamento_modificaBulk acrMod = (Accertamento_modificaBulk) bp.getModel();
	
		    if (!bp.isUoEnte()) {
				Var_stanz_resBulk var = acrMod.getVariazione();
				if (var!=null && (!var.isPropostaDefinitiva() || !var.isApprovazioneControllata())) {
					CRUDVar_stanz_resBP newbp = (CRUDVar_stanz_resBP) bp.avviaVariazStanzRes(context);
					context.addHookForward("bringback",this,"doBringBackVariazioniWindow");
					//context.addHookForward("close",this,"doBringBackAccertamentiModificaWindow");
					return context.addBusinessProcess(newbp);
				}
		    }
		} catch(Exception e) {
			return handleException(context,e);
		}
		return super.doRiporta(context);
	}

	public Forward doBringBackVariazioniWindow(ActionContext context) {

		try {
			CRUDAccertamentoModificaBP bp=null;

			if (getBusinessProcess(context) instanceof CRUDAccertamentoModificaBP)
				bp = (CRUDAccertamentoModificaBP) getBusinessProcess(context);
			else if (getBusinessProcess(context) instanceof CRUDVar_stanz_resBP)
				bp = (CRUDAccertamentoModificaBP) getBusinessProcess(context).getParent();
		    Accertamento_modificaBulk acrMod = (Accertamento_modificaBulk) bp.getModel();
			HookForward caller = (HookForward)context.getCaller();
			Var_stanz_resBulk var = (Var_stanz_resBulk)caller.getParameter("bringback");
			if (var!=null) {
				acrMod.setVariazione(var);
				if (!var.isPropostaDefinitiva() || !var.isApprovazioneControllata()) {
					if (getBusinessProcess(context) instanceof CRUDVar_stanz_resBP)
						context.closeBusinessProcess();
					CRUDVar_stanz_resBP newbp = (CRUDVar_stanz_resBP) bp.avviaVariazStanzRes(context);
					context.addHookForward("bringback",this,"doBringBackVariazioniWindow");
					return context.addBusinessProcess(newbp);
				}
				else {
					return super.doRiporta(context);
				}
			}
			else {
				if (acrMod.getVariazione()!=null && acrMod.getVariazione().getPg_variazione()!=null && acrMod.getVariazione().isTemporaneo())
					bp.cancellaVariazioneTemporanea(context, acrMod.getVariazione());
				acrMod.setVariazione(new Var_stanz_resBulk());
			}

		} catch(Throwable ex) {
			return handleException(context, ex);
		}
		return context.findDefaultForward();
	}
	public Forward doVisualizzaVariazione(ActionContext context) 
	{
		try 
		{
			fillModel(context);
		    CRUDAccertamentoModificaBP bp = (CRUDAccertamentoModificaBP) getBusinessProcess(context);
		    Accertamento_modificaBulk acrMod = (Accertamento_modificaBulk) bp.getModel();
	
			Var_stanz_resBulk var = acrMod.getVariazione();
			CRUDVar_stanz_resBP newbp = (CRUDVar_stanz_resBP) bp.avviaVariazStanzRes(context, var);
			return context.addBusinessProcess(newbp);
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
}
