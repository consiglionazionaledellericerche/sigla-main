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

package it.cnr.contab.inventario00.bp;

import java.rmi.RemoteException;

import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;

public class StampaRegistroInventarioBP extends it.cnr.contab.reports.bp.ParametricPrintBP {

	private boolean ufficiale;
	
	public boolean isUfficiale() {
		return ufficiale;
	}
	public void setUfficiale(boolean ufficiale) {
		this.ufficiale = ufficiale;
	}
protected void initialize(ActionContext context) throws BusinessProcessException {
	try {
		setUfficiale(UtenteBulk.isInventarioUfficiale(context.getUserContext()));
	} catch (ComponentException e1) {
		throw handleException(e1);
	} catch (RemoteException e1) {
		throw handleException(e1);
	}
	super.initialize(context);
}
public StampaRegistroInventarioBP(){
	super();
}
public StampaRegistroInventarioBP(String function){
	super(function);
}
}
