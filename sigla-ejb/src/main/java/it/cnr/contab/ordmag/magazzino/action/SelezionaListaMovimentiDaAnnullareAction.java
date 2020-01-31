/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.ordmag.magazzino.action;

import it.cnr.contab.ordmag.magazzino.bp.SelezionatoreMovimentiDaAnnullareBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.SelezionatoreListaAction;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2002 12:39:49 PM)
 * @author: Roberto Peli
 */
public class SelezionaListaMovimentiDaAnnullareAction extends SelezionatoreListaAction {
	/**
	 * DocumentiAmministrativiProtocollabiliAction constructor comment.
	 */
	public SelezionaListaMovimentiDaAnnullareAction() {
		super();
	}

	public Forward doAnnullaMovimenti(ActionContext context) {
		SelezionatoreMovimentiDaAnnullareBP bp = (SelezionatoreMovimentiDaAnnullareBP)context.getBusinessProcess();
		try {
			fillModel(context);
			bp.setSelection(context);
			bp.annullaMovimenti(context);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	@Override
	public Forward basicDoBringBack(ActionContext actioncontext) throws BusinessProcessException {
		return actioncontext.findDefaultForward();
	}
}
