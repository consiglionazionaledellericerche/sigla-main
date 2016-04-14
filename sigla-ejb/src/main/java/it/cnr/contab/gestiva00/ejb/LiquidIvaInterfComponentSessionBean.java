package it.cnr.contab.gestiva00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.gestiva00.comp.LiquidIvaInterfComponent;

/**
 * Bean implementation class for Enterprise Bean: CNRGESTIVA00_EJB_LiquidIvaInterfComponentSession
 */
@Stateless(name="CNRGESTIVA00_EJB_LiquidIvaInterfComponentSession")
public class LiquidIvaInterfComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements LiquidIvaInterfComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.gestiva00.comp.LiquidIvaInterfComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new LiquidIvaInterfComponentSessionBean();
	}
	public boolean contaRiga(it.cnr.jada.UserContext param0,it.cnr.contab.gestiva00.core.bulk.Liquid_iva_interfBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((LiquidIvaInterfComponent)componentObj).contaRiga(param0,param1);
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
	public void inserisciRighe(it.cnr.jada.UserContext param0,it.cnr.contab.gestiva00.core.bulk.Liquid_iva_interfBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((LiquidIvaInterfComponent)componentObj).inserisciRighe(param0,param1);
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
