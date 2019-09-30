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

import java.rmi.RemoteException;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.CRUDAction;

public class AllegatiDocContAction extends CRUDAction {
	private static final long serialVersionUID = 1L;
	
	@Override
	public Forward doElimina(ActionContext actioncontext)
			throws RemoteException {
		super.doElimina(actioncontext);
        HookForward hookforward = (HookForward)actioncontext.findForward("close");
        if(hookforward != null)
            return hookforward;
        else
            return actioncontext.findDefaultForward();
		
	}

}
