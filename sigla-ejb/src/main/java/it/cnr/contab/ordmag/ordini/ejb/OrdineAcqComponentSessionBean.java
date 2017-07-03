package it.cnr.contab.ordmag.ordini.ejb;
import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.comp.OrdineAcqComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
@Stateless(name="CNRORDMAG00_EJB_OrdineAcqComponentSession")
public class OrdineAcqComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements OrdineAcqComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new OrdineAcqComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new OrdineAcqComponentSessionBean();
}
public Boolean isUtenteAbilitatoOrdine(UserContext usercontext, OrdineAcqBulk ordine) throws ComponentException, PersistencyException,javax.ejb.EJBException {
	pre_component_invocation(usercontext,componentObj);
	try {
		Boolean result = ((OrdineAcqComponent)componentObj).isUtenteAbilitatoOrdine(usercontext, ordine);
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
public Boolean isUtenteAbilitatoValidazioneOrdine(UserContext usercontext, OrdineAcqBulk ordine) throws ComponentException, PersistencyException,javax.ejb.EJBException {
	pre_component_invocation(usercontext,componentObj);
	try {
		Boolean result = ((OrdineAcqComponent)componentObj).isUtenteAbilitatoValidazioneOrdine(usercontext, ordine);
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
public void gestioneStampaOrdine(UserContext userContext, OrdineAcqBulk ordine) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException, RemoteException {
	pre_component_invocation(userContext,componentObj);
	try {
		((OrdineAcqComponent)componentObj).gestioneStampaOrdine(userContext, ordine);
		component_invocation_succes(userContext,componentObj);
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(userContext,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(userContext,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(userContext,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(userContext,componentObj,e);
	}
}
public void completaOrdine(UserContext userContext, OrdineAcqBulk ordine) throws PersistencyException,ComponentException, javax.ejb.EJBException, RemoteException {
	pre_component_invocation(userContext,componentObj);
	try {
		((OrdineAcqComponent)componentObj).completaOrdine(userContext, ordine);
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
