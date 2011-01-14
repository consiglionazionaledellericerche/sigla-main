package it.cnr.contab.progettiric00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.*;

/**
 * Bean implementation class for Enterprise Bean: CNRPROGETTIRIC00_EJB_ProgettoRicercaPadreComponentSession
 */
@Stateless(name="CNRPROGETTIRIC00_EJB_ProgettoRicercaPadreComponentSession")
public class ProgettoRicercaPadreComponentSessionBean extends it.cnr.contab.progettiric00.ejb.ProgettoRicercaComponentSessionBean implements ProgettoRicercaPadreComponentSession{
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.progettiric00.comp.ProgettoRicercaPadreComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
	return new ProgettoRicercaComponentSessionBean();
}
public void aggiornaGECO(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		((it.cnr.contab.progettiric00.comp.ProgettoRicercaPadreComponent)componentObj).aggiornaGECO(param0);
		component_invocation_succes(param0,componentObj);
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
