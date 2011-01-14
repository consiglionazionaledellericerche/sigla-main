package it.cnr.contab.compensi00.ejb;
import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import it.cnr.contab.compensi00.comp.AddizionaliComponent;
import it.cnr.contab.compensi00.tabrif.bulk.AddizionaliBulk;
import it.cnr.jada.ejb.CRUDComponentSessionBean;
@Stateless(name="CNRCOMPENSI00_EJB_AddizionaliComponentSession")
public class AddizionaliComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements AddizionaliComponentSession{
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new it.cnr.contab.compensi00.comp.AddizionaliComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}

	public static CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new AddizionaliComponentSessionBean();
	}
	public AddizionaliBulk verifica_aggiornamento(it.cnr.jada.UserContext param0,AddizionaliBulk param1) throws it.cnr.jada.comp.ComponentException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			AddizionaliBulk result = ((AddizionaliComponent)componentObj).verifica_aggiornamento(param0,param1);
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
	public void Aggiornamento_scaglione(it.cnr.jada.UserContext param0,AddizionaliBulk param1) throws it.cnr.jada.comp.ComponentException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			((AddizionaliComponent)componentObj).Aggiornamento_scaglione(param0,param1);
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
	public void cancella_pendenti(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			((AddizionaliComponent)componentObj).cancella_pendenti(param0);
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
