package it.cnr.contab.docamm00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.docamm00.comp.CambioComponent;
@Stateless(name="CNRDOCAMM00_EJB_CambioComponentSession")
public class CambioComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements CambioComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.docamm00.comp.CambioComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new CambioComponentSessionBean();
}
public boolean validaCambio(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.CambioBulk param1) throws javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		boolean result = ((CambioComponent)componentObj).validaCambio(param0,param1);
		component_invocation_succes(param0,componentObj);
	return result;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
}
