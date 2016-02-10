package it.cnr.contab.config00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.config00.comp.LimiteSpesaComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
@Stateless(name="CNRCONFIG00_EJB_LimiteSpesaComponentSession")
public class LimiteSpesaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements LimiteSpesaComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.config00.comp.LimiteSpesaComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new LimiteSpesaComponentSessionBean();
}
public void validaCds(UserContext param0, OggettoBulk param1)
		throws ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((LimiteSpesaComponent)componentObj).validaCds(param0,param1);
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
