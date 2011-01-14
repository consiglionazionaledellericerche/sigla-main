package it.cnr.contab.logregistry00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import it.cnr.contab.logregistry00.comp.LogRegistryComponent;
@Stateless(name="CNRLOGREGISTRY00_EJB_LogRegistryComponentSession")
public class LogRegistryComponentSessionBean extends it.cnr.jada.ejb.RicercaComponentSessionBean implements LogRegistryComponentSession {
	@PostConstruct
		public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new it.cnr.contab.logregistry00.comp.LogRegistryComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}
	public static it.cnr.jada.ejb.RicercaComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new LogRegistryComponentSessionBean();
	}
	public it.cnr.jada.util.RemoteIterator cercaTabelleDiLog(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.jada.bulk.OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((LogRegistryComponent)componentObj).cercaTabelleDiLog(param0,param1,param2);
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
