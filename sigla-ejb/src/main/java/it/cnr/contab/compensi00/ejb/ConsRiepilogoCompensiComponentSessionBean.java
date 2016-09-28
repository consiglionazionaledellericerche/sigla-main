package it.cnr.contab.compensi00.ejb;
import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import it.cnr.contab.compensi00.comp.ConsRiepilogoCompensiComponent;
import it.cnr.contab.docamm00.consultazioni.bulk.VConsRiepCompensiBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;
@Stateless(name="CNRCOMPENSI00_EJB_ConsRiepilogoCompensiComponentSession")
public class ConsRiepilogoCompensiComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ConsRiepilogoCompensiComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new ConsRiepilogoCompensiComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
	return new ConsRiepilogoCompensiComponentSessionBean();
}
public RemoteIterator findRiepilogoCompensi(UserContext param0, VConsRiepCompensiBulk param1) throws PersistencyException, IntrospectionException, ComponentException, RemoteException{
	pre_component_invocation(param0,componentObj);
	try {
		RemoteIterator result = ((ConsRiepilogoCompensiComponent)componentObj).findRiepilogoCompensi(param0,param1);
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
public VConsRiepCompensiBulk impostaDatiIniziali(UserContext param0, VConsRiepCompensiBulk param1) throws ComponentException{
	pre_component_invocation(param0,componentObj);
	try {
		VConsRiepCompensiBulk bulk = ((ConsRiepilogoCompensiComponent)componentObj).impostaDatiIniziali(param0,param1);
		component_invocation_succes(param0,componentObj);
		return bulk;
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
