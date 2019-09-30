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

package it.cnr.contab.pdg00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.anagraf00.comp.AnagraficoComponent;
import it.cnr.contab.cori00.comp.Liquid_coriComponent;
import it.cnr.contab.pdg00.cdip.bulk.V_cnr_estrazione_coriBulk;
import it.cnr.contab.pdg00.cdip.bulk.V_stipendi_cofi_dettBulk;
import it.cnr.contab.pdg00.comp.ElaboraFileStipendiComponent;

/**
 * Bean implementation class for Enterprise Bean: CNRGESTIVA00_EJB_LiquidIvaInterfComponentSession
 */
@Stateless(name="CNRPDG00_EJB_ElaboraFileStipendiComponentSession")
public class ElaboraFileStipendiComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ElaboraFileStipendiComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.pdg00.comp.ElaboraFileStipendiComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new ElaboraFileStipendiComponentSessionBean();
	}
	public V_stipendi_cofi_dettBulk cercaFileStipendi(it.cnr.jada.UserContext param0,V_stipendi_cofi_dettBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			V_stipendi_cofi_dettBulk result = ((ElaboraFileStipendiComponent)componentObj).cercaFileStipendi(param0,param1);
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
	public V_stipendi_cofi_dettBulk elaboraFile(it.cnr.jada.UserContext param0,V_stipendi_cofi_dettBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			V_stipendi_cofi_dettBulk result = ((ElaboraFileStipendiComponent)componentObj).elaboraFile(param0,param1);
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
	public V_stipendi_cofi_dettBulk annullaElaborazione(it.cnr.jada.UserContext param0,V_stipendi_cofi_dettBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			V_stipendi_cofi_dettBulk result = ((ElaboraFileStipendiComponent)componentObj).annullaElaborazione(param0,param1);
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
	public it.cnr.jada.bulk.OggettoBulk cercaBatch(it.cnr.jada.UserContext param0,V_stipendi_cofi_dettBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
			try {
				it.cnr.jada.bulk.OggettoBulk result = ((ElaboraFileStipendiComponent)componentObj).cercaBatch(param0,param1);
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
	public V_cnr_estrazione_coriBulk cercaCnrEstrazioneCori(it.cnr.jada.UserContext param0,V_cnr_estrazione_coriBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			V_cnr_estrazione_coriBulk result = ((ElaboraFileStipendiComponent)componentObj).cercaCnrEstrazioneCori(param0,param1);
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
	public V_cnr_estrazione_coriBulk elaboraStralcioMensile(it.cnr.jada.UserContext param0,V_cnr_estrazione_coriBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			V_cnr_estrazione_coriBulk result = ((ElaboraFileStipendiComponent)componentObj).elaboraStralcioMensile(param0,param1);
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
	public boolean esisteStralcioNegativo(it.cnr.jada.UserContext param0,V_cnr_estrazione_coriBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((ElaboraFileStipendiComponent)componentObj).esisteStralcioNegativo(param0,param1);
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
