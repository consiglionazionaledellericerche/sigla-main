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

package it.cnr.contab.compensi00.ejb;
import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import it.cnr.contab.compensi00.comp.ConsRiepilogoCompensiComponent;
import it.cnr.contab.docamm00.consultazioni.bulk.VConsRiepCompensiBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;
@Stateless(name="CNRCOMPENSI00_EJB_ConsRiepilogoCompensiComponentSession")
public class ConsRiepilogoCompensiComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ConsRiepilogoCompensiComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new ConsRiepilogoCompensiComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
	return new ConsRiepilogoCompensiComponentSessionBean();
}
public RemoteIterator findRiepilogoCompensi(UserContext param0, VConsRiepCompensiBulk param1) throws PersistencyException, IntrospectionException, ComponentException, RemoteException{
	pre_component_invocation(param0,componentObj);
	try {
		RemoteIterator result = ((ConsRiepilogoCompensiComponent)componentObj).findRiepilogoCompensi(param0,param1);
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
public VConsRiepCompensiBulk impostaDatiIniziali(UserContext param0, VConsRiepCompensiBulk param1) throws ComponentException{
	pre_component_invocation(param0,componentObj);
	try {
		VConsRiepCompensiBulk bulk = ((ConsRiepilogoCompensiComponent)componentObj).impostaDatiIniziali(param0,param1);
		component_invocation_succes(param0,componentObj);
		return bulk;
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
