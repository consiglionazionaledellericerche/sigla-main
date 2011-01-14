package it.cnr.contab.compensi00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.compensi00.comp.TrattamentoCORIComponent;
@Stateless(name="CNRCOMPENSI00_EJB_TrattamentoCORIComponentSession")
public class TrattamentoCORIComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements TrattamentoCORIComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.compensi00.comp.TrattamentoCORIComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new TrattamentoCORIComponentSessionBean();
}
public it.cnr.contab.compensi00.tabrif.bulk.Trattamento_coriBulk fillAllRows(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.tabrif.bulk.Trattamento_coriBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.compensi00.tabrif.bulk.Trattamento_coriBulk result = ((TrattamentoCORIComponent)componentObj).fillAllRows(param0,param1);
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
