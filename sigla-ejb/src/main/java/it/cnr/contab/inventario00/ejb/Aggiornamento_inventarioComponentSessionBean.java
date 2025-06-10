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

package it.cnr.contab.inventario00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import it.cnr.contab.inventario00.comp.Aggiornamento_inventarioComponent;
@Stateless(name="CNRINVENTARIO00_EJB_Aggiornamento_inventarioComponentSession")
public class Aggiornamento_inventarioComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements Aggiornamento_inventarioComponentSession{
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.inventario00.comp.Aggiornamento_inventarioComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new Aggiornamento_inventarioComponentSessionBean();
	}
	public it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,java.lang.Class param2,it.cnr.jada.bulk.OggettoBulk param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((Aggiornamento_inventarioComponent)componentObj).cerca(param0,param1,param2,param3,param4);
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

	public void modificaBeniAggiornati(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((Aggiornamento_inventarioComponent)componentObj).modificaBeniAggiornati(param0,param1,param2,param3,param4);
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
	public it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk aggiornaTuttiBeni(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk param1,it.cnr.jada.persistency.sql.CompoundFindClause param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk result = ((Aggiornamento_inventarioComponent)componentObj).aggiornaTuttiBeni(param0,param1,param2);
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
	public it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk aggiornaBeni(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk param1) throws	it.cnr.jada.comp.ComponentException,javax.ejb.EJBException, it.cnr.jada.persistency.PersistencyException,it.cnr.jada.bulk.OutdatedResourceException,it.cnr.jada.bulk.BusyResourceException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk result=((Aggiornamento_inventarioComponent)componentObj).aggiornaBeni(param0,param1);
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
	public it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk aggiornaStatoBeni(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk param1) throws	it.cnr.jada.comp.ComponentException,javax.ejb.EJBException, it.cnr.jada.persistency.PersistencyException,it.cnr.jada.bulk.OutdatedResourceException,it.cnr.jada.bulk.BusyResourceException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk result=((Aggiornamento_inventarioComponent)componentObj).aggiornaStatoBeni(param0,param1);
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
	public it.cnr.jada.util.RemoteIterator cercaBeniAggiornabili(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk param1,it.cnr.jada.persistency.sql.CompoundFindClause param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((Aggiornamento_inventarioComponent)componentObj).cercaBeniAggiornabili(param0,param1,param2);
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
}

