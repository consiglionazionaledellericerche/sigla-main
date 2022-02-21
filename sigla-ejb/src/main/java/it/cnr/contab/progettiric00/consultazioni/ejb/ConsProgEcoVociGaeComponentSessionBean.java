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

package it.cnr.contab.progettiric00.consultazioni.ejb;

import it.cnr.contab.progettiric00.consultazioni.bulk.ConsProgettiEcoVociGaeBulk;
import it.cnr.contab.progettiric00.consultazioni.comp.ConsProgEcoVociGaeComponent;
import it.cnr.jada.UserContext;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRPROGETTIRIC00_EJB_ConsProgEcoVociGaeComponentSession")
public class ConsProgEcoVociGaeComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ConsProgEcoVociGaeComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new ConsProgEcoVociGaeComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new ConsProgEcoVociGaeComponentSessionBean();
	}
	public it.cnr.jada.util.RemoteIterator findProgetti(UserContext param0, ConsProgettiEcoVociGaeBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException{
		pre_component_invocation(param0,componentObj);
		try {
 			it.cnr.jada.util.RemoteIterator result = ((ConsProgEcoVociGaeComponent)componentObj).findProgetti(param0,param1);
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