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

package it.cnr.contab.ordmag.ordini.ejb;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.contab.ordmag.ordini.comp.EvasioneOrdineComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
@Stateless(name="CNRORDMAG00_EJB_EvasioneOrdineComponentSession")
public class EvasioneOrdineComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements EvasioneOrdineComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new EvasioneOrdineComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new EvasioneOrdineComponentSessionBean();
}
public EvasioneOrdineBulk cercaOrdini(it.cnr.jada.UserContext param0,EvasioneOrdineBulk evasioneOrdine) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		EvasioneOrdineBulk result = ((EvasioneOrdineComponent)componentObj).cercaOrdini(param0,evasioneOrdine);
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

public List<BollaScaricoMagBulk> evadiOrdine(UserContext userContext, EvasioneOrdineBulk evasioneOrdine)throws ComponentException, PersistencyException,javax.ejb.EJBException {
	pre_component_invocation(userContext,componentObj);
	try {
		List<BollaScaricoMagBulk> result = ((EvasioneOrdineComponent)componentObj).evadiOrdine(userContext,evasioneOrdine);
		component_invocation_succes(userContext,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(userContext,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(userContext,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(userContext,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(userContext,componentObj,e);
	}
}
}
