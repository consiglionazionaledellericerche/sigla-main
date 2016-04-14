package it.cnr.contab.doccont00.consultazioni.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.doccont00.consultazioni.comp.ConsFileCassiereComponent;


@Stateless(name="CNRDOCCONT00_EJB_ConsFileCassiereComponentSession")
public class ConsFileCassiereComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ConsFileCassiereComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.doccont00.consultazioni.comp.ConsFileCassiereComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new ConsFileCassiereComponentSessionBean();
	}
	public it.cnr.jada.util.RemoteIterator findConsultazione(it.cnr.jada.UserContext param0, java.lang.String param1, it.cnr.jada.persistency.sql.CompoundFindClause param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((ConsFileCassiereComponent)componentObj).findConsultazione(param0,param1,param2,param3);
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

