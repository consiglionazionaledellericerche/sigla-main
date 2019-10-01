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

package it.cnr.contab.segnalazioni00.action;

import java.rmi.RemoteException;

import it.cnr.contab.segnalazioni00.bp.CRUDAttivitaSiglaBP;
import it.cnr.contab.segnalazioni00.bulk.Attivita_siglaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.OptionBP;

public class CRUDAttivitaSiglaAction extends CRUDAction {

	public Forward doSalva(ActionContext context) throws RemoteException {
		try {
			if (context.getBusinessProcess() instanceof CRUDAttivitaSiglaBP) {
				CRUDAttivitaSiglaBP bp = (CRUDAttivitaSiglaBP)getBusinessProcess(context);
			Attivita_siglaBulk attivita = (Attivita_siglaBulk)bp.getModel();
			fillModel( context );
			bp.validaAttivita(context, attivita);	
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
		
		return super.doSalva(context);
	}
	
}
