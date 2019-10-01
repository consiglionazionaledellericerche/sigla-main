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

package it.cnr.contab.config00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.config00.comp.Parametri_uoComponent;
/**
 * Bean implementation class for Enterprise Bean: CNRCONFIG00_EJB_Parametri_uoComponentSession
 */
@Stateless(name="CNRCONFIG00_EJB_Parametri_uoComponentSession")
public class Parametri_uoComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements Parametri_uoComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.config00.comp.Parametri_uoComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new Parametri_uoComponentSessionBean();
	}
	public boolean isFlGestioneModuliEnabled(it.cnr.jada.UserContext param0, it.cnr.contab.config00.bulk.Parametri_uoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((Parametri_uoComponent)componentObj).isFlGestioneModuliEnabled(param0,param1);
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
	public it.cnr.contab.config00.bulk.Parametri_uoBulk getParametriUo(it.cnr.jada.UserContext param0, java.lang.Integer param1, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.config00.bulk.Parametri_uoBulk result = ((Parametri_uoComponent)componentObj).getParametriUo(param0,param1,param2);
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
