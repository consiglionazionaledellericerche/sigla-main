package it.cnr.contab.docamm00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import it.cnr.contab.docamm00.comp.BeneServizioComponent;
@Stateless(name="CNRDOCAMM00_EJB_BeneServizioComponentSession")
public class BeneServizioComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements BeneServizioComponentSession {
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new it.cnr.contab.docamm00.comp.BeneServizioComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}
	
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new BeneServizioComponentSessionBean();
	}
	public it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk completaElementoVoceOf(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk result = ((BeneServizioComponent)componentObj).completaElementoVoceOf(param0,param1);
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
