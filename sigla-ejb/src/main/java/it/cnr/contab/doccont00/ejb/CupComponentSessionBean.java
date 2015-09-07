package it.cnr.contab.doccont00.ejb;
import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import it.cnr.contab.doccont00.comp.CupComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
@Stateless(name="CNRDOCCONT00_EJB_CupComponentSession")
public class CupComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements CupComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.doccont00.comp.CupComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new CupComponentSessionBean();
}
public String recuperoCds(UserContext param0) throws ComponentException,
		PersistencyException, IntrospectionException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		String result = ((CupComponent)componentObj).recuperoCds(param0);
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
	}}

}
