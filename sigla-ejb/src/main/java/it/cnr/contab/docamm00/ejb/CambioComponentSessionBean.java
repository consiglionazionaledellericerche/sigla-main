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

package it.cnr.contab.docamm00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.docamm00.comp.CambioComponent;
@Stateless(name="CNRDOCAMM00_EJB_CambioComponentSession")
public class CambioComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements CambioComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.docamm00.comp.CambioComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new CambioComponentSessionBean();
}
public boolean validaCambio(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.CambioBulk param1) throws javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		boolean result = ((CambioComponent)componentObj).validaCambio(param0,param1);
		component_invocation_succes(param0,componentObj);
	return result;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
}
