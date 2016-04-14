package it.cnr.contab.doccont00.ejb;


import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import it.cnr.contab.doccont00.comp.ConsConfrontoEntSpeComponent;
import it.cnr.jada.ejb.GenericComponentSessionBean;
import it.cnr.jada.persistency.IntrospectionException;

/**
 * Bean implementation class for Enterprise Bean: CNRDOCCONT00_EJB_ConsConfrontoEntSpeComponentSession
 */

@Stateless(name="CNRDOCCONT00_EJB_ConsConfrontoEntSpeComponentSession")
public class ConsConfrontoEntSpeComponentSessionBean extends GenericComponentSessionBean implements ConsConfrontoEntSpeComponentSession {
	private ConsConfrontoEntSpeComponent componentObj;
	
	@Remove	
	public void ejbRemove() throws EJBException {
		componentObj.release();
	}
	@PostConstruct
	public void ejbCreate() {
		componentObj = new ConsConfrontoEntSpeComponent();
	}
	
	public static ConsConfrontoEntSpeComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new ConsConfrontoEntSpeComponentSessionBean();
	}
	public it.cnr.jada.util.RemoteIterator findConsultazioneModulo(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.DetailedRuntimeException,it.cnr.jada.comp.ComponentException,java.rmi.RemoteException, IntrospectionException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = componentObj.findConsultazioneModulo(param0,param1,param2,param3,param4);
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
