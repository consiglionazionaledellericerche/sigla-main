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

import it.cnr.contab.compensi00.comp.BonusComponent;
import it.cnr.contab.compensi00.docs.bulk.BonusBulk;
import it.cnr.contab.compensi00.docs.bulk.Bonus_nucleo_famBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRCOMPENSI00_EJB_BonusComponentSession")
public class BonusComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements BonusComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.compensi00.comp.BonusComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new BonusComponentSessionBean();
}
public void checkCodiceFiscale(UserContext param0, BonusBulk param1)
		throws ComponentException, RemoteException, SQLException {
	pre_component_invocation(param0,componentObj);
	try {
		((BonusComponent)componentObj).checkCodiceFiscale(param0,param1);
		component_invocation_succes(param0,componentObj);
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
	
}
public void checkCodiceFiscaleComponente(UserContext param0,
		Bonus_nucleo_famBulk param1) throws ComponentException,
		RemoteException, SQLException {
	pre_component_invocation(param0,componentObj);
	try {
		((BonusComponent)componentObj).checkCodiceFiscaleComponente(param0,param1);
		component_invocation_succes(param0,componentObj);
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public BonusBulk recuperoDati(UserContext param0, BonusBulk param1)
		throws ComponentException, RemoteException, SQLException {
	pre_component_invocation(param0,componentObj);
	try {
		BonusBulk result=((BonusComponent)componentObj).recuperoDati(param0,param1);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public Boolean verificaLimiteFamiliareCarico(UserContext param0,
		Bonus_nucleo_famBulk param1) throws ComponentException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		Boolean result=((BonusComponent)componentObj).verificaLimiteFamiliareCarico(param0,param1);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public BonusBulk completaBonus(UserContext param0, BonusBulk param1)
		throws ComponentException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		BonusBulk result=((BonusComponent)componentObj).completaBonus(param0,param1);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public CompensoBulk cercaCompensoPerBonus(UserContext param0,
		BonusBulk param1) throws ComponentException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		CompensoBulk result=((BonusComponent)componentObj).cercaCompensoPerBonus(param0,param1);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public List estraiDettagli(UserContext param0, BonusBulk param1)
		throws ComponentException, PersistencyException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		java.util.List result=((BonusComponent)componentObj).estraiDettagli(param0,param1);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public List estraiLista(UserContext param0) throws ComponentException,
		PersistencyException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		java.util.List result=((BonusComponent)componentObj).estraiLista(param0);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public String recuperaCodiceFiscaleInvio(UserContext param0) throws ComponentException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		String result=((BonusComponent)componentObj).recuperaCodiceFiscaleInvio(param0);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public void aggiornaInvio(UserContext param0) throws ComponentException,
		RemoteException,PersistencyException {
	
		pre_component_invocation(param0,componentObj);
		try {
			((BonusComponent)componentObj).aggiornaInvio(param0);
			component_invocation_succes(param0,componentObj);
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