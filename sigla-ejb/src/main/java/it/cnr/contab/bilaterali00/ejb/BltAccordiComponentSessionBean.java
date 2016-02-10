package it.cnr.contab.bilaterali00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

/**
 * Bean implementation class for Enterprise Bean: CNRBILATERALI00_EJB_BltAccordiComponentSession
 */
@Stateless(name="CNRBILATERALI00_EJB_BltAccordiComponentSession")
public class BltAccordiComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements it.cnr.contab.bilaterali00.ejb.BltAccordiComponentSession {
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new it.cnr.contab.bilaterali00.comp.BltAccordiComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new BltAccordiComponentSessionBean();
	}
	public it.cnr.contab.bilaterali00.bulk.Blt_autorizzatiBulk setComuneEnteDiAppartenenza(it.cnr.jada.UserContext param0,it.cnr.contab.bilaterali00.bulk.Blt_autorizzatiBulk param1,it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.bilaterali00.bulk.Blt_autorizzatiBulk result = ((it.cnr.contab.bilaterali00.comp.BltAccordiComponent)componentObj).setComuneEnteDiAppartenenza(param0,param1,param2);
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
	public it.cnr.contab.bilaterali00.bulk.Blt_progettiBulk setComuneEnteResponsIta(it.cnr.jada.UserContext param0,it.cnr.contab.bilaterali00.bulk.Blt_progettiBulk param1,it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.bilaterali00.bulk.Blt_progettiBulk result = ((it.cnr.contab.bilaterali00.comp.BltAccordiComponent)componentObj).setComuneEnteResponsIta(param0,param1,param2);
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
