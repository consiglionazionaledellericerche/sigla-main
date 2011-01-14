package it.cnr.contab.docamm00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRDOCAMM00_EJB_SezionaleComponentSession")
public class SezionaleComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements SezionaleComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.docamm00.comp.SezionaleComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new SezionaleComponentSessionBean();
}
public java.util.List findListaSezionaleWS(it.cnr.jada.UserContext param0,String param1,String param2,String param3,String param4,String param5,String param6) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		java.util.List result = ((it.cnr.contab.docamm00.comp.SezionaleComponent)componentObj).findListaSezionaleWS(param0,param1,param2,param3,param4,param5,param6);
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

