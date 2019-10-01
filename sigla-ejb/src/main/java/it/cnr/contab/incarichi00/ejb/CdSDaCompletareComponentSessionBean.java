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
import java.util.List;

import it.cnr.contab.incarichi00.bulk.V_terzi_da_completareBulk;
import it.cnr.contab.incarichi00.comp.CdSDaCompletareComponent;

import javax.annotation.PostConstruct;
import javax.ejb.*;

/**
 * Bean implementation class for Enterprise Bean: CNRINCARICHI00_EJB_CdSDaCompletareComponentSession
 */
@Stateless(name="CNRINCARICHI00_EJB_CdSDaCompletareComponentSession")
public class CdSDaCompletareComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements CdSDaCompletareComponentSession  {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.incarichi00.comp.CdSDaCompletareComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
		return new CdSDaCompletareComponentSessionBean();
	}
	public List<V_terzi_da_completareBulk> findTerzi(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdsBulk param1) throws it.cnr.jada.comp.ComponentException{
		pre_component_invocation(param0,componentObj);
		try {
			List<V_terzi_da_completareBulk> result = ((CdSDaCompletareComponent)componentObj).findTerzi(param0,param1);
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
