package it.cnr.contab.config00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.config00.comp.CRUDConfigAssEvoldEvnewComponent;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.jada.UserContext;

@Stateless(name="CNRCONFIG00_EJB_CRUDConfigAssEvoldEvnewComponentSession")
public class CRUDConfigAssEvoldEvnewComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements CRUDConfigAssEvoldEvnewComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.config00.comp.CRUDConfigAssEvoldEvnewComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new CRUDConfigAssEvoldEvnewComponentSessionBean();
	}
	public Elemento_voceBulk getCurrentElementoVoce(UserContext param0, Elemento_voceBulk param1, int param2) throws it.cnr.jada.comp.ComponentException {
		pre_component_invocation(param0,componentObj);
		try {
			Elemento_voceBulk result = ((CRUDConfigAssEvoldEvnewComponent)componentObj).getCurrentElementoVoce(param0,param1,param2);
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
