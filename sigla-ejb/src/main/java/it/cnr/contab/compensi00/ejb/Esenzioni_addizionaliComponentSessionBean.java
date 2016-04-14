package it.cnr.contab.compensi00.ejb;
import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.compensi00.comp.Esenzioni_addizionaliComponent;
import it.cnr.contab.compensi00.tabrif.bulk.Esenzioni_addizionaliBulk;
import it.cnr.jada.ejb.CRUDComponentSessionBean;
@Stateless(name="CNRCOMPENSI00_EJB_Esenzioni_addizionaliComponentSession")
public class Esenzioni_addizionaliComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements Esenzioni_addizionaliComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.compensi00.comp.Esenzioni_addizionaliComponent();
}
public static CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new Esenzioni_addizionaliComponentSessionBean();
}
public Esenzioni_addizionaliBulk verifica_aggiornamento(it.cnr.jada.UserContext param0,Esenzioni_addizionaliBulk param1) throws it.cnr.jada.comp.ComponentException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		Esenzioni_addizionaliBulk result = ((Esenzioni_addizionaliComponent)componentObj).verifica_aggiornamento(param0,param1);
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
public void Aggiornamento(it.cnr.jada.UserContext param0,Esenzioni_addizionaliBulk param1) throws it.cnr.jada.comp.ComponentException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		((Esenzioni_addizionaliComponent)componentObj).Aggiornamento(param0,param1);
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
		((Esenzioni_addizionaliComponent)componentObj).cancella_pendenti(param0);
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
