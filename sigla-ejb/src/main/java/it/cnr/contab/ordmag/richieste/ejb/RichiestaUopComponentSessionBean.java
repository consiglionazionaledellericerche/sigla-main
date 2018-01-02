package it.cnr.contab.ordmag.richieste.ejb;
import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.contab.ordmag.richieste.comp.RichiestaUopComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
@Stateless(name="CNRORDMAG00_EJB_RichiestaUopComponentSession")
public class RichiestaUopComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements RichiestaUopComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new RichiestaUopComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new RichiestaUopComponentSessionBean();
}
public Boolean isUtenteAbilitatoRichiesta(UserContext usercontext, RichiestaUopBulk richiesta) throws ComponentException, PersistencyException,javax.ejb.EJBException {
	pre_component_invocation(usercontext,componentObj);
	try {
		Boolean result = ((RichiestaUopComponent)componentObj).isUtenteAbilitatoRichiesta(usercontext, richiesta);
		component_invocation_succes(usercontext,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(usercontext,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(usercontext,componentObj);
		throw e;
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		component_invocation_failure(usercontext,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(usercontext,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(usercontext,componentObj,e);
	}
}
public Boolean isUtenteAbilitatoValidazioneRichiesta(UserContext usercontext, RichiestaUopBulk richiesta) throws ComponentException, PersistencyException,javax.ejb.EJBException {
	pre_component_invocation(usercontext,componentObj);
	try {
		Boolean result = ((RichiestaUopComponent)componentObj).isUtenteAbilitatoValidazioneRichiesta(usercontext, richiesta);
		component_invocation_succes(usercontext,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(usercontext,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(usercontext,componentObj);
		throw e;
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		component_invocation_failure(usercontext,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(usercontext,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(usercontext,componentObj,e);
	}
}
public void completaRichiesta(UserContext userContext, RichiestaUopBulk richiesta) throws PersistencyException,ComponentException, javax.ejb.EJBException, RemoteException {
	pre_component_invocation(userContext,componentObj);
	try {
		((RichiestaUopComponent)componentObj).completaRichiesta(userContext, richiesta);
		component_invocation_succes(userContext,componentObj);
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(userContext,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(userContext,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(userContext,componentObj,e);
	}
}
}
