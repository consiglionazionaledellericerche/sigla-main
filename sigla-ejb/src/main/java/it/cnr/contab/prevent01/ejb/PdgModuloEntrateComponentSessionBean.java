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

package it.cnr.contab.prevent01.ejb;
import it.cnr.contab.prevent01.comp.CRUDPdg_Modulo_EntrateComponent;

import javax.annotation.PostConstruct;
import javax.ejb.*;

/**
 * Bean implementation class for Enterprise Bean: CNR_PREVENT01_EJB_PdgModuloEntrateComponentSession
 */
@Stateless(name="CNRPREVENT01_EJB_Pdg_Modulo_EntrateComponentSession")
public class PdgModuloEntrateComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements PdgModuloEntrateComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new CRUDPdg_Modulo_EntrateComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
		return new PdgModuloEntrateComponentSessionBean();
	}
	public it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk findParametriLivelli(it.cnr.jada.UserContext param0, java.lang.Integer param1)  throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
			pre_component_invocation(param0,componentObj);
			try {
				it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk result = ((CRUDPdg_Modulo_EntrateComponent)componentObj).findParametriLivelli(param0, param1);
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
	public it.cnr.contab.config00.bulk.Parametri_cnrBulk parametriCnr(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.config00.bulk.Parametri_cnrBulk result = ((CRUDPdg_Modulo_EntrateComponent)componentObj).parametriCnr(param0);
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
	public java.util.Collection findNatura(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.util.Collection result = ((CRUDPdg_Modulo_EntrateComponent)componentObj).findNatura(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.persistency.PersistencyException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	
	public boolean isUtenteEnte(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((CRUDPdg_Modulo_EntrateComponent)componentObj).isUtenteEnte(param0);
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
	public it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateBulk getPdgModuloEntrateBulk(it.cnr.jada.UserContext param0, it.cnr.contab.pdg01.consultazioni.bulk.V_cons_pdgp_pdgg_etrBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateBulk result = ((CRUDPdg_Modulo_EntrateComponent)componentObj).getPdgModuloEntrateBulk(param0,param1);
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
