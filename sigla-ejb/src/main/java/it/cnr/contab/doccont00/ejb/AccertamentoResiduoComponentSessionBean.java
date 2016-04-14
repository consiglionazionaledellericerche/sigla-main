package it.cnr.contab.doccont00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import it.cnr.contab.doccont00.comp.AccertamentoResiduoComponent;
@Stateless(name="CNRDOCCONT00_EJB_AccertamentoResiduoComponentSession")
public class AccertamentoResiduoComponentSessionBean extends it.cnr.contab.doccont00.ejb.AccertamentoComponentSessionBean implements AccertamentoResiduoComponentSession{
@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.doccont00.comp.AccertamentoResiduoComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}

	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new AccertamentoResiduoComponentSessionBean();
	}
	public String controllaDettagliScadenzaAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1,it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			String result = ((AccertamentoResiduoComponent)componentObj).controllaDettagliScadenzaAccertamento(param0,param1,param2);
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
	public void cancellaAccertamentoModTemporanea(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			((AccertamentoResiduoComponent)componentObj).cancellaAccertamentoModTemporanea(param0,param1);
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
