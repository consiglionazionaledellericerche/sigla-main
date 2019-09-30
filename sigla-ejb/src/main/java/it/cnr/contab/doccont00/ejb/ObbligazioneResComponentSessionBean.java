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

package it.cnr.contab.doccont00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.doccont00.comp.ObbligazioneResComponent;
@Stateless(name="CNRDOCCONT00_EJB_ObbligazioneResComponentSession")
public class ObbligazioneResComponentSessionBean extends ObbligazioneComponentSessionBean implements ObbligazioneResComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.doccont00.comp.ObbligazioneResComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new ObbligazioneResComponentSessionBean();
}
public String controllaDettagliScadenzaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		String result = ((ObbligazioneResComponent)componentObj).controllaDettagliScadenzaObbligazione(param0,param1,param2);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public void cancellaObbligazioneModTemporanea(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		((ObbligazioneResComponent)componentObj).cancellaObbligazioneModTemporanea(param0,param1);
		component_invocation_succes(param0,componentObj);
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
}

