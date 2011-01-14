package it.cnr.contab.util00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.*;
@Stateless(name="CNRUTIL00_EJB_ProcedureComponentSession")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ProcedureComponentSessionBean extends it.cnr.jada.ejb.GenericComponentSessionBean implements ProcedureComponentSession {
	private it.cnr.contab.util00.comp.ProcedureComponent componentObj;

	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new it.cnr.contab.util00.comp.ProcedureComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}
	
	public static ProcedureComponentSessionBean newInstance() throws EJBException {
		return new ProcedureComponentSessionBean();
	}
	
	public void aggiornaApprovazioneFormale(it.cnr.jada.UserContext param0, java.util.List param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(null,componentObj);
		try {
			componentObj.aggiornaApprovazioneFormale(param0, param1);
			component_invocation_succes(null,componentObj);
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(null,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(null,componentObj,e);
		}
	}
	public void aggiornaApprovazioneFormale(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(null,componentObj);
		try {
			componentObj.aggiornaApprovazioneFormale(param0, param1);
			component_invocation_succes(null,componentObj);
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(null,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(null,componentObj,e);
		}
	}
	public void aggiornaApponiVisto(it.cnr.jada.UserContext param0, java.util.List param1, it.cnr.contab.config00.sto.bulk.DipartimentoBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(null,componentObj);
		try {
			componentObj.aggiornaApponiVisto(param0, param1, param2);
			component_invocation_succes(null,componentObj);
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(null,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(null,componentObj,e);
		}
	}
	public void aggiornaApponiVisto(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.contab.config00.sto.bulk.DipartimentoBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(null,componentObj);
		try {
			componentObj.aggiornaApponiVisto(param0, param1, param2);
			component_invocation_succes(null,componentObj);
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(null,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(null,componentObj,e);
		}
	}
	public it.cnr.contab.preventvar00.bulk.Var_bilancioBulk salvaDefinitivo(it.cnr.jada.UserContext param0,it.cnr.contab.preventvar00.bulk.Var_bilancioBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.preventvar00.bulk.Var_bilancioBulk result = (new it.cnr.contab.preventvar00.comp.VarBilancioComponent()).salvaDefinitivo(param0,param1);
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
