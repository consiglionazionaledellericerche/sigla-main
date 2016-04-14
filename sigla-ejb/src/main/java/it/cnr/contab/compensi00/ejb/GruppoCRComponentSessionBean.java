package it.cnr.contab.compensi00.ejb;
import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.compensi00.comp.GruppoCRComponent;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_crBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_cr_detBulk;
import it.cnr.jada.ejb.CRUDComponentSessionBean;
@Stateless(name="CNRCOMPENSI00_EJB_GruppoCRComponentSession")
public class GruppoCRComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements GruppoCRComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.compensi00.comp.GruppoCRComponent();
}
public static CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new GruppoCRComponentSessionBean();
}
public void CreaperTutteUOSAC(it.cnr.jada.UserContext param0,Gruppo_crBulk param1) throws it.cnr.jada.comp.ComponentException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		((GruppoCRComponent)componentObj).CreaperTutteUOSAC(param0,param1);
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
public Gruppo_cr_detBulk completaTerzo(it.cnr.jada.UserContext param0,Gruppo_cr_detBulk param1, TerzoBulk param2) throws it.cnr.jada.comp.ComponentException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		Gruppo_cr_detBulk result = ((GruppoCRComponent)componentObj).completaTerzo(param0,param1,param2);
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
public java.util.List findListaBanche(it.cnr.jada.UserContext param0,Gruppo_cr_detBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		java.util.List result = ((GruppoCRComponent)componentObj).findListaBanche(param0,param1);
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
public java.util.Collection findModalitaOptions(it.cnr.jada.UserContext param0,Gruppo_cr_detBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		java.util.Collection result = ((GruppoCRComponent)componentObj).findModalitaOptions(param0,param1);
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
public void validaCancellazioneDettaglio(it.cnr.jada.UserContext param0,Gruppo_cr_detBulk param1) throws it.cnr.jada.comp.ComponentException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		((GruppoCRComponent)componentObj).validaCancellazioneDettaglio(param0,param1);
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
public boolean controllaEsistenzaGruppo(it.cnr.jada.UserContext param0,Gruppo_crBulk param1) throws it.cnr.jada.comp.ComponentException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		boolean result = ((GruppoCRComponent)componentObj).controllaEsistenzaGruppo(param0,param1);
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
