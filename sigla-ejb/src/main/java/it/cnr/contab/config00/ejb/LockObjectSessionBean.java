package it.cnr.contab.config00.ejb;

import it.cnr.contab.config00.comp.LockObjectComponent;
import it.cnr.contab.config00.comp.Parametri_cdsComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRCONFIG00_EJB_LockObjectSession")
public class LockObjectSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements LockObjectSession{
	@PostConstruct
	public void ejbCreate() {
		componentObj = new LockObjectComponent();
	}
	
	public OggettoBulk riempiListaUtenti(it.cnr.jada.UserContext param0, OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException{
		pre_component_invocation(param0,componentObj);
		try {
			OggettoBulk result = ((LockObjectComponent)componentObj).riempiListaUtenti(param0,param1);
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

	public OggettoBulk riempiListaOggetti(it.cnr.jada.UserContext param0, OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException{
		pre_component_invocation(param0,componentObj);
		try {
			OggettoBulk result = ((LockObjectComponent)componentObj).riempiListaOggetti(param0,param1);
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
	public void terminaSessioni(UserContext param0, BulkList param1) throws ComponentException {
		pre_component_invocation(param0,componentObj);
		try {
			((LockObjectComponent)componentObj).terminaSessioni(param0,param1);
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
