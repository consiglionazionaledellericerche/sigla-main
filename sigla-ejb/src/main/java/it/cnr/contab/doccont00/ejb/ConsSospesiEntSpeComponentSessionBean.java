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
import javax.ejb.Stateless;

import it.cnr.contab.doccont00.comp.ConsSospesiEntSpeComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.sql.CompoundFindClause;

/**
 * Bean implementation class for Enterprise Bean: CNRDOCCONT00_EJB_ConsSospesiEntSpeComponentSession
 */
@Stateless(name="CNRDOCCONT00_EJB_ConsSospesiEntSpeComponentSession")
public class ConsSospesiEntSpeComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ConsSospesiEntSpeComponentSession{
	
	@PostConstruct
	public void ejbCreate() {
		componentObj = new ConsSospesiEntSpeComponent();
	}
	
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new ConsSospesiEntSpeComponentSessionBean();
	}
	
	
	public it.cnr.jada.util.RemoteIterator findConsSospesiSpesa(UserContext param0,String param1,String param2,CompoundFindClause param3,CompoundFindClause param4) 
			throws ComponentException, RemoteException, IntrospectionException{
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((ConsSospesiEntSpeComponent)componentObj).findConsSospesiSpesa(param0,param1,param2,param3,param4);
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

	public it.cnr.jada.util.RemoteIterator findConsSospesiEntrata(UserContext param0,String param1,String param2,CompoundFindClause param3,CompoundFindClause param4) 
			throws ComponentException, RemoteException, IntrospectionException{
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((ConsSospesiEntSpeComponent)componentObj).findConsSospesiEntrata(param0,param1,param2,param3,param4);
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

