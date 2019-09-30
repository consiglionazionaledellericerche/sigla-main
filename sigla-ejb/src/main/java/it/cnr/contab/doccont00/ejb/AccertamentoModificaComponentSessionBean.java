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

/*
 * Created on Jun 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import it.cnr.contab.doccont00.comp.AccertamentoModificaComponent;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
@Stateless(name="CNRDOCCONT00_EJB_AccertamentoModificaComponentSession")
public class AccertamentoModificaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements AccertamentoModificaComponentSession{
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.doccont00.comp.AccertamentoModificaComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}
	
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new AccertamentoModificaComponentSessionBean();
	}
	public void cancellaVariazioneTemporanea(it.cnr.jada.UserContext param0, it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			((AccertamentoModificaComponent)componentObj).cancellaVariazioneTemporanea(param0,param1);
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
}
