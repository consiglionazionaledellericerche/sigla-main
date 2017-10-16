package it.cnr.contab.ordmag.magazzino.ejb;
import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.magazzino.comp.MovimentiMagComponent;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
@Stateless(name="CNRORDMAG00_EJB_MovimentiMagComponentSession")
public class MovimentiMagComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements MovimentiMagComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new MovimentiMagComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new MovimentiMagComponentSessionBean();
}
public MovimentiMagBulk caricaOrdine(it.cnr.jada.UserContext userContext, EvasioneOrdineBulk evasioneOrdine) throws RemoteException,ComponentException, PersistencyException{
	pre_component_invocation(userContext,componentObj);
	try {
		MovimentiMagBulk result = ((MovimentiMagComponent)componentObj).caricaOrdine(userContext, evasioneOrdine);
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
