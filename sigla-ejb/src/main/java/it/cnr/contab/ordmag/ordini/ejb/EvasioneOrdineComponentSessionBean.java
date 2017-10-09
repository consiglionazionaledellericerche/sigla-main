package it.cnr.contab.ordmag.ordini.ejb;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.contab.ordmag.ordini.comp.EvasioneOrdineComponent;
import it.cnr.contab.ordmag.ordini.comp.OrdineAcqComponent;
@Stateless(name="CNRORDMAG00_EJB_EvasioneOrdineComponentSession")
public class EvasioneOrdineComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements EvasioneOrdineComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new EvasioneOrdineComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new EvasioneOrdineComponentSessionBean();
}
public EvasioneOrdineBulk cercaOrdini(it.cnr.jada.UserContext param0,EvasioneOrdineBulk evasioneOrdine) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		EvasioneOrdineBulk result = ((EvasioneOrdineComponent)componentObj).cercaOrdini(param0,evasioneOrdine);
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
