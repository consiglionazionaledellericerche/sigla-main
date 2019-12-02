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

package it.cnr.contab.docamm00.consultazioni.action;

import java.rmi.RemoteException;
import java.util.List;

import it.cnr.contab.docamm00.consultazioni.bp.ConsTerziDaConguagliareBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;

public class ConsTerziDaConguagliareAction extends ConsultazioniAction {

	public Forward doCdSDaConguagliare(ActionContext actioncontext) throws RemoteException {
		try {
			actioncontext.addBusinessProcess(actioncontext.createBusinessProcess("ConsCdSDaConguagliareBP"));
		} catch (BusinessProcessException e) {
			handleException(actioncontext, e);
		}
		return actioncontext.findDefaultForward();
	}
}
