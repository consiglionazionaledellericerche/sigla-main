package it.cnr.contab.incarichi00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.incarichi00.comp.IncarichiRichiestaComponent;

/**
 * Bean implementation class for Enterprise Bean: CNRINCARICHI00_EJB_IncarichiRichiestaComponentSession
 */
@Stateless(name="CNRINCARICHI00_EJB_IncarichiRichiestaComponentSession")
public class IncarichiRichiestaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements IncarichiRichiestaComponentSession {
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new it.cnr.contab.incarichi00.comp.IncarichiRichiestaComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new IncarichiRichiestaComponentSessionBean();
	}
	public it.cnr.jada.bulk.OggettoBulk pubblicaSulSito(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((IncarichiRichiestaComponent)componentObj).pubblicaSulSito(param0,param1);
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
	public it.cnr.jada.util.RemoteIterator  findListaIncarichiRichiesta(it.cnr.jada.UserContext param0,String param1,String param2,Integer param3,String param4,String param5,String param6) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((IncarichiRichiestaComponent)componentObj).findListaIncarichiRichiesta(param0,param1,param2,param3,param4,param5,param6);
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
	public it.cnr.jada.util.RemoteIterator findListaIncarichiCollaborazione(it.cnr.jada.UserContext param0,String param1,String param2,Integer param3,String param4,String param5,String param6) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((IncarichiRichiestaComponent)componentObj).findListaIncarichiCollaborazione(param0,param1,param2,param3,param4,param5,param6);
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
	public it.cnr.jada.util.RemoteIterator findListaIncarichiElenco(it.cnr.jada.UserContext param0,String param1,String param2,Integer param3,String param4,String param5,String param6) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((IncarichiRichiestaComponent)componentObj).findListaIncarichiElenco(param0,param1,param2,param3,param4,param5,param6);
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
	public it.cnr.jada.util.RemoteIterator findListaIncarichiElencoArt18(it.cnr.jada.UserContext param0,String param1,String param2,Integer param3,String param4,String param5,String param6) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((IncarichiRichiestaComponent)componentObj).findListaIncarichiElencoArt18(param0,param1,param2,param3,param4,param5,param6);
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
	public java.util.List completaListaIncarichiRichiesta(it.cnr.jada.UserContext param0,java.util.List param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.util.List result = ((IncarichiRichiestaComponent)componentObj).completaListaIncarichiRichiesta(param0,param1);
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
	public java.util.List completaListaIncarichiCollaborazione(it.cnr.jada.UserContext param0,java.util.List param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.util.List result = ((IncarichiRichiestaComponent)componentObj).completaListaIncarichiCollaborazione(param0,param1);
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
	public java.util.List completaListaIncarichiElenco(it.cnr.jada.UserContext param0,java.util.List param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.util.List result = ((IncarichiRichiestaComponent)componentObj).completaListaIncarichiElenco(param0,param1);
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