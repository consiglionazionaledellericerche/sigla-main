package it.cnr.contab.config00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.config00.comp.Parametri_cdsComponent;

/**
 * Bean implementation class for Enterprise Bean: CNRCONFIG00_EJB_Parametri_cnrComponentSession
 */
@Stateless(name="CNRCONFIG00_EJB_Parametri_cdsComponentSession")
public class Parametri_cdsComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements Parametri_cdsComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.config00.comp.Parametri_cdsComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new Parametri_cdsComponentSessionBean();
	}
	public it.cnr.contab.config00.bulk.Parametri_cdsBulk getParametriCds(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.Integer param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.config00.bulk.Parametri_cdsBulk result = ((Parametri_cdsComponent)componentObj).getParametriCds(param0,param1,param2);
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
