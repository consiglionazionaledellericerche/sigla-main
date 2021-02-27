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

import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk;
import it.cnr.contab.incarichi00.comp.IncarichiEstrazioneFpComponent;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.math.BigDecimal;

/**
 * Bean implementation class for Enterprise Bean: CNRINCARICHI00_EJB_IncarichiEstrazioneFpComponentSession
 */
@Stateless(name="CNRINCARICHI00_EJB_IncarichiEstrazioneFpComponentSession")
public class IncarichiEstrazioneFpComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements IncarichiEstrazioneFpComponentSession  {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.incarichi00.comp.IncarichiEstrazioneFpComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
		return new IncarichiEstrazioneFpComponentSessionBean();
	}
	public it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk completaIncaricoElencoFP(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk param1) throws it.cnr.jada.comp.ComponentException{
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk result = ((IncarichiEstrazioneFpComponent)componentObj).completaIncaricoElencoFP(param0,param1);
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
	public void aggiornaIncarichiComunicatiFP(it.cnr.jada.UserContext param0, java.util.List<it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk> param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((IncarichiEstrazioneFpComponent)componentObj).aggiornaIncarichiComunicatiFP(param0,param1);
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
	public void aggiornaIncarichiComunicatiFP(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((IncarichiEstrazioneFpComponent)componentObj).aggiornaIncarichiComunicatiFP(param0,param1);
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
	public it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk getIncarichiComunicatiAggFP(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk param1) throws it.cnr.jada.comp.ComponentException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk result = ((IncarichiEstrazioneFpComponent)componentObj).getIncarichiComunicatiAggFP(param0,param1);
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
	public it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk getIncarichiComunicatiAggFP(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk param1) throws it.cnr.jada.comp.ComponentException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk result = ((IncarichiEstrazioneFpComponent)componentObj).getIncarichiComunicatiAggFP(param0,param1);
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
	public java.util.List<it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk> getIncarichiComunicatiEliminatiFP(it.cnr.jada.UserContext param0, Integer param1, Integer param2) throws it.cnr.jada.comp.ComponentException {
		pre_component_invocation(param0,componentObj);
		try {
			java.util.List<it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk> result = ((IncarichiEstrazioneFpComponent)componentObj).getIncarichiComunicatiEliminatiFP(param0,param1,param2);
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
	public java.util.List<it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fp_detBulk> getPagatoPerSemestre(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param1) throws it.cnr.jada.comp.ComponentException {
		pre_component_invocation(param0,componentObj);
		try {
			java.util.List<it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fp_detBulk> result = ((IncarichiEstrazioneFpComponent)componentObj).getPagatoPerSemestre(param0,param1);
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
	public BigDecimal getPagatoIncarico(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param1) throws it.cnr.jada.comp.ComponentException {
		pre_component_invocation(param0,componentObj);
		try {
			BigDecimal result = ((IncarichiEstrazioneFpComponent)componentObj).getPagatoIncarico(param0,param1);
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
	public void comunicaPerla2018(it.cnr.jada.UserContext param0, Incarichi_repertorioBulk incaricoRepertorio) throws it.cnr.jada.comp.ComponentException {
		pre_component_invocation(param0,componentObj);
		try {
			((IncarichiEstrazioneFpComponent)componentObj).comunicaPerla2018(param0,incaricoRepertorio);
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
	public void comunicaPerla2018(it.cnr.jada.UserContext param0, V_incarichi_elenco_fpBulk incaricoElenco) throws it.cnr.jada.comp.ComponentException {
		pre_component_invocation(param0,componentObj);
		try {
			((IncarichiEstrazioneFpComponent)componentObj).comunicaPerla2018(param0,incaricoElenco);
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