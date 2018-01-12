package it.cnr.contab.ordmag.magazzino.ejb;
import java.rmi.RemoteException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoBulk;
import it.cnr.contab.ordmag.magazzino.comp.MovimentiMagComponent;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.comp.EvasioneOrdineComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;
@Stateless(name="CNRORDMAG00_EJB_MovimentiMagComponentSession")
public class MovimentiMagComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements MovimentiMagComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new MovimentiMagComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new MovimentiMagComponentSessionBean();
}
public MovimentiMagBulk caricoDaOrdine(UserContext userContext, OrdineAcqConsegnaBulk consegna, EvasioneOrdineRigaBulk evasioneOrdineRiga) 
		throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	pre_component_invocation(userContext,componentObj);
	try {
		MovimentiMagBulk result = ((MovimentiMagComponent)componentObj).caricoDaOrdine(userContext, consegna, evasioneOrdineRiga);
		component_invocation_succes(userContext,componentObj);
		return result;
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

public List<BollaScaricoMagBulk> generaBolleScarico(UserContext userContext, List<MovimentiMagBulk> listaMovimentiScarico)
		throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	pre_component_invocation(userContext,componentObj);
	try {
		List<BollaScaricoMagBulk> result = ((MovimentiMagComponent)componentObj).generaBolleScarico(userContext, listaMovimentiScarico);
		component_invocation_succes(userContext,componentObj);
		return result;
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
public ScaricoMagazzinoBulk scaricaMagazzino(UserContext userContext, ScaricoMagazzinoBulk scaricoMagazzino)
		throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	pre_component_invocation(userContext,componentObj);
	try {
		ScaricoMagazzinoBulk result = ((MovimentiMagComponent)componentObj).scaricaMagazzino(userContext, scaricoMagazzino);
		component_invocation_succes(userContext,componentObj);
		return result;
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

public ScaricoMagazzinoBulk initializeScaricoMagazzino(UserContext userContext, ScaricoMagazzinoBulk scaricoMagazzino)
		throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	pre_component_invocation(userContext,componentObj);
	try {
		ScaricoMagazzinoBulk result = ((MovimentiMagComponent)componentObj).initializeScaricoMagazzino(userContext, scaricoMagazzino);
		component_invocation_succes(userContext,componentObj);
		return result;
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

public RemoteIterator preparaQueryBolleScaricoDaVisualizzare(UserContext userContext, List<BollaScaricoMagBulk> bolle)throws ComponentException,javax.ejb.EJBException {
	pre_component_invocation(userContext,componentObj);
	try {
		RemoteIterator result = ((MovimentiMagComponent)componentObj).preparaQueryBolleScaricoDaVisualizzare(userContext, bolle);
		component_invocation_succes(userContext,componentObj);
		return result;
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
}
