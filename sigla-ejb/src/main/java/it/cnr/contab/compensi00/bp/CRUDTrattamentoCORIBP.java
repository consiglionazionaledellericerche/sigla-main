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

package it.cnr.contab.compensi00.bp;

import it.cnr.contab.compensi00.tabrif.bulk.*;
import 	it.cnr.contab.compensi00.ejb.*;
import it.cnr.jada.util.action.*;

/**
 * Insert the type's description here.
 * Creation date: (14/03/2002 10.17.43)
 * @author: Roberto Fantino
 */
public class CRUDTrattamentoCORIBP extends it.cnr.jada.util.action.SimpleCRUDBP {

	private final SimpleDetailCRUDController righeCRUDController = new SimpleDetailCRUDController("righeCRUDController",Trattamento_coriBulk.class,"righe",this);
/**
 * CRUDTrattamentoCORIBP constructor comment.
 */
public CRUDTrattamentoCORIBP() {
	super();
}
/**
 * CRUDTrattamentoCORIBP constructor comment.
 * @param function java.lang.String
 */
public CRUDTrattamentoCORIBP(String function) {
	super(function);
	righeCRUDController.setReadonly(false);
	righeCRUDController.setEnabled(false);
}
public void fillAllRows(it.cnr.jada.action.ActionContext context, Trattamento_coriBulk trattCORI) throws it.cnr.jada.action.BusinessProcessException{

	try{
		TrattamentoCORIComponentSession session = (TrattamentoCORIComponentSession)createComponentSession();
		Trattamento_coriBulk newModel = session.fillAllRows(context.getUserContext(), trattCORI);

		setModel(context, newModel);

	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 16.51.41)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getRigheCRUDController() {
	return righeCRUDController;
}
}
