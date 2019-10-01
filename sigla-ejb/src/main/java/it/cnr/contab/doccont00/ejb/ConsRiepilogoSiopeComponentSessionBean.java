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

import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import it.cnr.contab.doccont00.comp.ConsRiepilogoSiopeComponent;
import it.cnr.contab.preventvar00.comp.ConsAssCompPerDataComponent;
import it.cnr.contab.preventvar00.ejb.ConsAssCompPerDataComponentSessionBean;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;

/**
 * Bean implementation class for Enterprise Bean: CNRDOCCONT00_EJB_ConsRiepilogoSiopeComponentSession
 */
@Stateless(name="CNRDOCCONT00_EJB_ConsRiepilogoSiopeComponentSession")
public class ConsRiepilogoSiopeComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ConsRiepilogoSiopeComponentSession{
	
	@PostConstruct
	public void ejbCreate() {
		componentObj = new ConsRiepilogoSiopeComponent();
	}
	
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new ConsAssCompPerDataComponentSessionBean();
	}
	
	
	public it.cnr.jada.util.RemoteIterator findSiopeDettaglioMandati(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4, OggettoBulk param5) 
			throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((ConsRiepilogoSiopeComponent)componentObj).findSiopeDettaglioMandati(param0,param1,param2,param3,param4,param5);
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
	
	public it.cnr.jada.util.RemoteIterator findSiopeDettaglioReversali(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4, OggettoBulk param5) 
			throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((ConsRiepilogoSiopeComponent)componentObj).findSiopeDettaglioReversali(param0,param1,param2,param3,param4,param5);
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

	public it.cnr.jada.util.RemoteIterator findSiopeMandati(UserContext param0, OggettoBulk param1) 
			throws  ComponentException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((ConsRiepilogoSiopeComponent)componentObj).findSiopeMandati(param0,param1);
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

	public it.cnr.jada.util.RemoteIterator findSiopeReversali(UserContext param0, OggettoBulk param1) 
		throws  ComponentException, RemoteException {
			pre_component_invocation(param0,componentObj);
			try {
				it.cnr.jada.util.RemoteIterator result = ((ConsRiepilogoSiopeComponent)componentObj).findSiopeReversali(param0,param1);
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
