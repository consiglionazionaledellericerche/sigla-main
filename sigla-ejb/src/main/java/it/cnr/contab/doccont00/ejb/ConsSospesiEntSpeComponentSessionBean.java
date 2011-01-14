package it.cnr.contab.doccont00.ejb;

import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.doccont00.comp.ConsSospesiEntSpeComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.sql.CompoundFindClause;

/**
 * Bean implementation class for Enterprise Bean: CNRDOCCONT00_EJB_ConsSospesiEntSpeComponentSession
 */
@Stateless(name="CNRDOCCONT00_EJB_ConsSospesiEntSpeComponentSession")
public class ConsSospesiEntSpeComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ConsSospesiEntSpeComponentSession{
	
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new ConsSospesiEntSpeComponent();
	}
	
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new ConsSospesiEntSpeComponentSessionBean();
	}
	
	
	public it.cnr.jada.util.RemoteIterator findConsSospesiSpesa(UserContext param0,String param1,String param2,CompoundFindClause param3,CompoundFindClause param4) 
			throws ComponentException, RemoteException, IntrospectionException{
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((ConsSospesiEntSpeComponent)componentObj).findConsSospesiSpesa(param0,param1,param2,param3,param4);
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

	public it.cnr.jada.util.RemoteIterator findConsSospesiEntrata(UserContext param0,String param1,String param2,CompoundFindClause param3,CompoundFindClause param4) 
			throws ComponentException, RemoteException, IntrospectionException{
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((ConsSospesiEntSpeComponent)componentObj).findConsSospesiEntrata(param0,param1,param2,param3,param4);
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

