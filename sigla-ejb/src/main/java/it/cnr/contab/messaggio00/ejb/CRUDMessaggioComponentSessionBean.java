package it.cnr.contab.messaggio00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.messaggio00.comp.CRUDMessaggioComponent;
@Stateless(name="CNRMESSAGGIO00_EJB_CRUDMessaggioComponentSession")
public class CRUDMessaggioComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements CRUDMessaggioComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.messaggio00.comp.CRUDMessaggioComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new CRUDMessaggioComponentSessionBean();
}
public void setMessaggioVisionato(it.cnr.jada.UserContext param0,it.cnr.contab.messaggio00.bulk.MessaggioBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		((CRUDMessaggioComponent)componentObj).setMessaggioVisionato(param0,param1);
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
public boolean isMessaggioVisionato(it.cnr.jada.UserContext param0,it.cnr.contab.messaggio00.bulk.MessaggioBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		boolean result = ((CRUDMessaggioComponent)componentObj).isMessaggioVisionato(param0,param1);
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
