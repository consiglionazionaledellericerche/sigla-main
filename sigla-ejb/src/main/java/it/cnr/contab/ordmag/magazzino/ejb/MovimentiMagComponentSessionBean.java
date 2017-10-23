package it.cnr.contab.ordmag.magazzino.ejb;
import java.rmi.RemoteException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.magazzino.comp.MovimentiMagComponent;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
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
public List<MovimentiMagBulk> caricoDaOrdine(UserContext userContext, EvasioneOrdineBulk evasioneOrdine,
		OrdineAcqConsegnaBulk consegna, OrdineAcqBulk ordine, EvasioneOrdineRigaBulk evasioneOrdineRiga, 
		List<MovimentiMagBulk> listaMovimentiScarico)throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	pre_component_invocation(userContext,componentObj);
	try {
		List<MovimentiMagBulk> result = ((MovimentiMagComponent)componentObj).caricoDaOrdine(userContext, evasioneOrdine,
				consegna, ordine, evasioneOrdineRiga, listaMovimentiScarico);
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
