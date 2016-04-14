package it.cnr.contab.config00.tabnum.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.*;
@Stateless(name="CNRCONFIG00_TABNUM_EJB_TREQUIRED_Numerazione_baseComponentSession")
public class TRequiredNumerazione_baseComponentSessionBean extends it.cnr.jada.ejb.GenericComponentSessionBean implements Numerazione_baseComponentSession {
	private it.cnr.contab.config00.tabnum.comp.Numerazione_baseComponent componentObj;
	@Remove
	public void ejbRemove() throws EJBException {
		componentObj.release();
	}
	@PostConstruct
		public void ejbCreate() {
		componentObj = new it.cnr.contab.config00.tabnum.comp.Numerazione_baseComponent();
	}
	public static 
	TRequiredNumerazione_baseComponentSessionBean newInstance() throws EJBException {
		return new TRequiredNumerazione_baseComponentSessionBean();
	}
	public java.lang.Long creaNuovoProgressivo(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.bulk.BusyResourceException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.lang.Long result = componentObj.creaNuovoProgressivo(param0,param1,param2,param3,param4);
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
	public java.lang.Long creaNuovoProgressivoTemp(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.bulk.BusyResourceException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.lang.Long result = componentObj.creaNuovoProgressivoTemp(param0,param1,param2,param3,param4);
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
