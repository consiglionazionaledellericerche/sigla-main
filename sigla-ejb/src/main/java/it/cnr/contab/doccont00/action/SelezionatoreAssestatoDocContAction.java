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

import it.cnr.contab.doccont00.bp.SelezionatoreAssestatoDocContBP;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.pdg01.action.SelezionatoreAssestatoAction;
import it.cnr.contab.pdg01.bp.SelezionatoreAssestatoBP;
import it.cnr.contab.prevent00.bulk.V_assestatoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.FillException;

import java.math.BigDecimal;

public class SelezionatoreAssestatoDocContAction extends SelezionatoreAssestatoAction {
	public Forward doAssegnaImporto(ActionContext actioncontext){
		SelezionatoreAssestatoBP bp = (SelezionatoreAssestatoBP)actioncontext.getBusinessProcess();
		V_assestatoBulk saldo = (V_assestatoBulk)bp.getModel();
		try {
			BigDecimal impOld = saldo.getImp_da_assegnare();
			bp.fillModels(actioncontext);
			if (bp.getBulkCaller() instanceof ObbligazioneBulk) {
				if (Utility.nvl(saldo.getImp_da_assegnare()).compareTo(Utility.ZERO) < 0) {
					bp.setMessage("L'importo selezionato deve essere positivo.");
					saldo.setImp_da_assegnare(impOld);
					return actioncontext.findDefaultForward();
				}
				if (Utility.nvl(saldo.getImporto_disponibile_netto()).compareTo(Utility.ZERO) < 0) {
					bp.setMessage("Disponibilità insufficiente. L'importo utilizzabile non può essere superiore a "+ new it.cnr.contab.util.EuroFormat().format(Utility.nvl(saldo.getImporto_disponibile()).add(Utility.nvl(saldo.getDb_imp_utilizzato()))) + ".");
					saldo.setImp_da_assegnare(impOld);
					return actioncontext.findDefaultForward();
			    }
			}
			return super.doAssegnaImporto(actioncontext);
		} catch (FillException e) {
			bp.setMessage(e.getMessage());
		}
		return actioncontext.findDefaultForward();
    }
	
	public Forward doAssegnaPercentualiBilancio(ActionContext actioncontext) {
		SelezionatoreAssestatoDocContBP bp = (SelezionatoreAssestatoDocContBP)actioncontext.getBusinessProcess();
		try {
			fillModel( actioncontext );
			bp.saveSelection(actioncontext);
			bp.fillModels( actioncontext );
			bp.assegnaPercentualiBilancio(actioncontext);
		} catch (BusinessProcessException e) {
			bp.setMessage(e.getMessage());
		} catch (FillException e) {
			bp.setMessage(e.getMessage());
		}
		return actioncontext.findDefaultForward();
	}

	public Forward doAssegnaPercentualiPdg(ActionContext actioncontext) {
		SelezionatoreAssestatoDocContBP bp = (SelezionatoreAssestatoDocContBP)actioncontext.getBusinessProcess();
		try {
			fillModel( actioncontext );
			bp.saveSelection(actioncontext);
			bp.fillModels( actioncontext );
			bp.assegnaPercentualiPdg(actioncontext);
		} catch (BusinessProcessException e) {
			bp.setMessage(e.getMessage());
		} catch (FillException e) {
			bp.setMessage(e.getMessage());
		}
		return actioncontext.findDefaultForward();
	}
}
