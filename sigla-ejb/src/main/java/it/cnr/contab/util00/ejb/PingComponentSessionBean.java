package it.cnr.contab.util00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.*;
@Stateless(name="CNRUTIL00_EJB_PingComponentSession")
public class PingComponentSessionBean extends it.cnr.jada.ejb.GenericComponentSessionBean  implements PingComponentSession{
	private it.cnr.contab.util00.comp.PingComponent componentObj;
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.util00.comp.PingComponent();
	}
	@Remove
	public void ejbRemove() throws EJBException {
		componentObj.release();
	}
	public static PingComponentSessionBean newInstance() throws EJBException {
		return new PingComponentSessionBean();
	}
	public boolean ping(String param0, Integer param1) throws javax.ejb.EJBException {
		pre_component_invocation(null,componentObj);
		try {
			boolean result = componentObj.ping(param0, param1);
			component_invocation_succes(null,componentObj);
			return result;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(null,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(null,componentObj,e);
		}
	}
}
