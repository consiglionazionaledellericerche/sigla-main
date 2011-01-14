package it.cnr.contab.doccont00.consultazioni.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.doccont00.consultazioni.comp.ConsGAEComResSintComponent;


@Stateless(name="CNRDOCCONT00_EJB_ConsGAEComResSintComponentSession")
public class ConsGAEComResSintComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ConsGAEComResSintComponentSession {
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new it.cnr.contab.doccont00.consultazioni.comp.ConsGAEComResSintComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new ConsGAEComResSintComponentSessionBean();
	}
	public it.cnr.jada.util.RemoteIterator findConsultazione(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((ConsGAEComResSintComponent)componentObj).findConsultazione(param0,param2,param3);
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

