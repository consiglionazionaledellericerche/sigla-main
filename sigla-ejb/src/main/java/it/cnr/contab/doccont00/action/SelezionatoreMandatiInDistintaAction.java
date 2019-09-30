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

import it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP;
import it.cnr.contab.doccont00.bp.RicercaMandatoReversaleBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.AbstractSelezionatoreBP;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.action.SelezionatoreListaAction;
import it.cnr.jada.util.action.SelezionatoreListaBP;

public class SelezionatoreMandatiInDistintaAction extends
		SelezionatoreListaAction {

	public SelezionatoreMandatiInDistintaAction() {
		super();
	}

	public Forward doSort(ActionContext actioncontext, String s, String s1)
		throws BusinessProcessException
	{
		return super.doSort(actioncontext, s, s1);
	}

	public Forward doConfirmSort(ActionContext actioncontext, OptionBP optionbp)
		throws BusinessProcessException
    {
		SelezionatoreListaBP nbp = (SelezionatoreListaBP) actioncontext.getBusinessProcess();
		RicercaMandatoReversaleBP bp = (RicercaMandatoReversaleBP) nbp.getParent();
		CRUDDistintaCassiereBP bpp = (CRUDDistintaCassiereBP) bp.getParent();

		Forward fw = super.doConfirmSort(actioncontext, optionbp);
		bp.inserisciMandatiCORI(actioncontext, bp, bpp, nbp);
		return fw;
    }
}
