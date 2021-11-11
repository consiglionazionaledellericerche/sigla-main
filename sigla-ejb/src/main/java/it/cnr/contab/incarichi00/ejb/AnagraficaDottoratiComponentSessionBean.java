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

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.compensi00.comp.MinicarrieraComponent;
import it.cnr.contab.incarichi00.bulk.AnagraficaDottoratiBulk;
import it.cnr.contab.incarichi00.comp.AnagraficaDottoratiComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import java.rmi.RemoteException;
import java.util.List;

@Stateless(name="CNRINCARICHI00_EJB_AnagraficaDottoratiComponentSession")
public class AnagraficaDottoratiComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements AnagraficaDottoratiComponentSession {
@PostConstruct
	public void ejbCreate() {
		componentObj = new AnagraficaDottoratiComponent();
	}

	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new AnagraficaDottoratiComponentSessionBean();
	}

	@Override
	public AnagraficaDottoratiBulk completaTerzo(UserContext param0, AnagraficaDottoratiBulk param1, TerzoBulk param2) throws ComponentException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			AnagraficaDottoratiBulk result = ((AnagraficaDottoratiComponent)componentObj).completaTerzo(param0,param1,param2);
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

	@Override
	public List findListaBanche(UserContext param0, AnagraficaDottoratiBulk param1) throws ComponentException, RemoteException {
		return null;
	}

	public it.cnr.contab.incarichi00.bulk.AnagraficaDottoratiBulk generaRate(it.cnr.jada.UserContext param0,it.cnr.contab.incarichi00.bulk.AnagraficaDottoratiBulk param1,boolean param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.incarichi00.bulk.AnagraficaDottoratiBulk result = ((AnagraficaDottoratiComponent)componentObj).generaRate(param0,param1,param2);
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
