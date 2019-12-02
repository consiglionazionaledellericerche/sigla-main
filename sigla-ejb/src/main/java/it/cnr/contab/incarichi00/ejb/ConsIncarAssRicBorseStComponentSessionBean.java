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

package it.cnr.contab.incarichi00.ejb;
import it.cnr.contab.incarichi00.bulk.VIncarichiAssRicBorseStBulk;
import it.cnr.contab.incarichi00.comp.ConsIncarAssRicBorseStComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;

import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
@Stateless(name="CNRINCARICHI00_EJB_ConsIncarAssRicBorseStComponentSession")
public class ConsIncarAssRicBorseStComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ConsIncarAssRicBorseStComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new ConsIncarAssRicBorseStComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
	return new ConsIncarAssRicBorseStComponentSessionBean();
}
public RemoteIterator findIncarichi(UserContext param0, VIncarichiAssRicBorseStBulk param1) throws PersistencyException, IntrospectionException, ComponentException, RemoteException{
	pre_component_invocation(param0,componentObj);
	try {
		RemoteIterator result = ((ConsIncarAssRicBorseStComponent)componentObj).findIncarichi(param0,param1);
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
public VIncarichiAssRicBorseStBulk impostaDatiIniziali(UserContext param0, VIncarichiAssRicBorseStBulk param1) throws ComponentException{
	pre_component_invocation(param0,componentObj);
	try {
		VIncarichiAssRicBorseStBulk bulk = ((ConsIncarAssRicBorseStComponent)componentObj).impostaDatiIniziali(param0,param1);
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
