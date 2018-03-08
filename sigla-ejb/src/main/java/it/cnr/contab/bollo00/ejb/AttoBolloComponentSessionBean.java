package it.cnr.contab.bollo00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.bollo00.comp.AttoBolloComponent;

/**
 * Bean implementation class for Enterprise Bean: CNRBOLLO00_EJB_AttoBolloComponentSession
 */
@Stateless(name="CNRBOLLO00_EJB_AttoBolloComponentSession")
public class AttoBolloComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements it.cnr.contab.bollo00.ejb.AttoBolloComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.bollo00.comp.AttoBolloComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new AttoBolloComponentSessionBean();
	}
	public it.cnr.jada.util.RemoteIterator findConsultazioneDettaglio(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4, boolean param5) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((AttoBolloComponent)componentObj).findConsultazioneDettaglio(param0,param1,param2,param3,param4,param5);
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
