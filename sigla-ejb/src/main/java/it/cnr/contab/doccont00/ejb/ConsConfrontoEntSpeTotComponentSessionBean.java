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
import javax.ejb.EJBException;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import it.cnr.contab.doccont00.comp.ConsConfrontoEntSpeTotComponent;
import it.cnr.jada.ejb.GenericComponentSessionBean;
import it.cnr.jada.persistency.IntrospectionException;

/**
 * Bean implementation class for Enterprise Bean: CNRDOCCONT00_EJB_ConsConfrontoEntSpeTotComponentSession
 */

@Stateless(name="CNRDOCCONT00_EJB_ConsConfrontoEntSpeTotComponentSession")
public class ConsConfrontoEntSpeTotComponentSessionBean extends GenericComponentSessionBean implements ConsConfrontoEntSpeTotComponentSession {
	private ConsConfrontoEntSpeTotComponent componentObj;
	
	@Remove	
	public void ejbRemove() throws EJBException {
		componentObj.release();
	}
	@PostConstruct
	public void ejbCreate() {
		componentObj = new ConsConfrontoEntSpeTotComponent();
	}
	
	public static ConsConfrontoEntSpeTotComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new ConsConfrontoEntSpeTotComponentSessionBean();
	}
	public it.cnr.jada.util.RemoteIterator findConsultazioneModulo(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.DetailedRuntimeException,it.cnr.jada.comp.ComponentException,java.rmi.RemoteException, IntrospectionException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = componentObj.findConsultazioneModulo(param0,param1,param2,param3,param4);
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
