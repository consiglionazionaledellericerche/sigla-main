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

package it.cnr.contab.bollo00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.bollo00.comp.AttoBolloComponent;

/**
 * Bean implementation class for Enterprise Bean: CNRBOLLO00_EJB_AttoBolloComponentSession
 */
@Stateless(name="CNRBOLLO00_EJB_AttoBolloComponentSession")
public class AttoBolloComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements it.cnr.contab.bollo00.ejb.AttoBolloComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.bollo00.comp.AttoBolloComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new AttoBolloComponentSessionBean();
	}
	public it.cnr.jada.util.RemoteIterator findConsultazioneDettaglio(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4, boolean param5) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((AttoBolloComponent)componentObj).findConsultazioneDettaglio(param0,param1,param2,param3,param4,param5);
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
