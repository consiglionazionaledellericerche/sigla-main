package it.cnr.contab.anagraf00.ejb;
import it.cnr.contab.anagraf00.comp.AbiCabComponent;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;

@Stateless(name="CNRANAGRAF00_EJB_AbiCabComponentSession")
public class AbiCabComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements AbiCabComponentSession {
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new it.cnr.contab.anagraf00.comp.AbiCabComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}
	
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new AbiCabComponentSessionBean();
	}
	public it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk findCaps(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk result = ((AbiCabComponent)componentObj).findCaps(param0,param1);
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
	public boolean isCancellatoLogicamente(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((AbiCabComponent)componentObj).isCancellatoLogicamente(param0,param1);
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
	public it.cnr.contab.anagraf00.core.bulk.BancaBulk caricaStrutturaIban(it.cnr.jada.UserContext param0, it.cnr.contab.anagraf00.core.bulk.BancaBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.anagraf00.core.bulk.BancaBulk result = ((AbiCabComponent)componentObj).caricaStrutturaIban(param0,param1);
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
