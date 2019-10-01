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

package it.cnr.contab.prevent00.ejb;
import it.cnr.contab.prevent00.comp.BilancioPreventivoComponent;

import javax.annotation.PostConstruct;
import javax.ejb.*;
@Stateless(name="CNRPREVENT00_EJB_BilancioPreventivoComponentSession")
public class BilancioPreventivoComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements BilancioPreventivoComponentSession{
@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.prevent00.comp.BilancioPreventivoComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}

	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
		return new BilancioPreventivoComponentSessionBean();
	}
	public it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk approvaBilancioPreventivo(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk result = ((BilancioPreventivoComponent)componentObj).approvaBilancioPreventivo(param0,param1);
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
	public it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk caricaBilancioPreventivo(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk result = ((BilancioPreventivoComponent)componentObj).caricaBilancioPreventivo(param0,param1);
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
	public it.cnr.jada.bulk.OggettoBulk cercaCdsEnte(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((BilancioPreventivoComponent)componentObj).cercaCdsEnte(param0,param1);
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
	public it.cnr.jada.bulk.OggettoBulk cercaCdsEnte(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((BilancioPreventivoComponent)componentObj).cercaCdsEnte(param0,param1);
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
	public void creaStanziamentiInizialiCNR(it.cnr.jada.UserContext param0,short param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BilancioPreventivoComponent)componentObj).creaStanziamentiInizialiCNR(param0,param1);
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
	public void creaStanziamentiInizialiCdS(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdsBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BilancioPreventivoComponent)componentObj).creaStanziamentiInizialiCdS(param0,param1);
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
	public it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk predisponeBilancioPreventivoCNR(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk result = ((BilancioPreventivoComponent)componentObj).predisponeBilancioPreventivoCNR(param0,param1);
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
	public it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk predisponeBilancioPreventivoCdS(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk result = ((BilancioPreventivoComponent)componentObj).predisponeBilancioPreventivoCdS(param0,param1);
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
