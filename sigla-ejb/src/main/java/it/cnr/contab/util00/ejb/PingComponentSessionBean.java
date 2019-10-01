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

package it.cnr.contab.util00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.*;
@Stateless(name="CNRUTIL00_EJB_PingComponentSession")
public class PingComponentSessionBean extends it.cnr.jada.ejb.GenericComponentSessionBean  implements PingComponentSession{
	private it.cnr.contab.util00.comp.PingComponent componentObj;
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.util00.comp.PingComponent();
	}
	@Remove
	public void ejbRemove() throws EJBException {
		componentObj.release();
	}
	public static PingComponentSessionBean newInstance() throws EJBException {
		return new PingComponentSessionBean();
	}
	public boolean ping(String param0, Integer param1) throws javax.ejb.EJBException {
		pre_component_invocation(null,componentObj);
		try {
			boolean result = componentObj.ping(param0, param1);
			component_invocation_succes(null,componentObj);
			return result;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(null,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(null,componentObj,e);
		}
	}
}
