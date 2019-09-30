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

package it.cnr.contab.inventario01.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.comp.NumerazioneTempBuonoComponent;
@Stateless(name="CNRINVENTARIO01_EJB_NumerazioneTempBuonoComponentSession")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class NumerazioneTempBuonoComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements NumerazioneTempBuonoComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new NumerazioneTempBuonoComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new NumerazioneTempBuonoComponentSessionBean();
}
public java.lang.Long getNextTempPG(it.cnr.jada.UserContext param0,Buono_carico_scaricoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		java.lang.Long result = ((NumerazioneTempBuonoComponent)componentObj).getNextTempPG(param0,param1);
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
