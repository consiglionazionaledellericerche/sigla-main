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

package it.cnr.contab.anagraf00.action;

import java.rmi.RemoteException;

import it.cnr.contab.anagraf00.bp.CRUDTipologieIstatBP;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipologie_istatBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDAction;

/**
 * Insert the type's description here.
 * Creation date: (24/04/2007)
 * @author: fgiardina
 */
public class CRUDTipologieIstatAction extends CRUDAction {

public CRUDTipologieIstatAction() {
	super();
}


public Forward doSalva(ActionContext context) throws RemoteException {
	try {	
		CRUDTipologieIstatBP bp = (CRUDTipologieIstatBP)getBusinessProcess(context);
		Tipologie_istatBulk tipologieIstat = (Tipologie_istatBulk)bp.getModel();
		fillModel( context );
		bp.validainserimento(context, tipologieIstat);
		
	} catch(Throwable e) {
		return handleException(context, e);
	}
	return super.doSalva(context);
}


}

