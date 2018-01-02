package it.cnr.contab.config00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import it.cnr.contab.config00.comp.EsercizioComponent;
@Stateless(name="CNRCONFIG00_EJB_EsercizioComponentSession")
public class EsercizioComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements EsercizioComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.config00.comp.EsercizioComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new EsercizioComponentSessionBean();
}
public it.cnr.contab.config00.esercizio.bulk.EsercizioBulk apriPianoDiGestione(it.cnr.jada.UserContext param0,it.cnr.contab.config00.esercizio.bulk.EsercizioBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.config00.esercizio.bulk.EsercizioBulk result = ((EsercizioComponent)componentObj).apriPianoDiGestione(param0,param1);
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
public it.cnr.contab.config00.esercizio.bulk.EsercizioBulk cambiaStatoConBulk(it.cnr.jada.UserContext param0,it.cnr.contab.config00.esercizio.bulk.EsercizioBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.config00.esercizio.bulk.EsercizioBulk result = ((EsercizioComponent)componentObj).cambiaStatoConBulk(param0,param1);
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
public boolean isEsercizioChiuso(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, EJBException{
	pre_component_invocation(param0,componentObj);
	try {
		boolean result = ((EsercizioComponent)componentObj).isEsercizioChiuso(param0);
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
public it.cnr.contab.config00.esercizio.bulk.EsercizioBulk getLastEsercizioOpen(it.cnr.jada.UserContext param0 ) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.config00.esercizio.bulk.EsercizioBulk result = ((EsercizioComponent)componentObj).getLastEsercizioOpen(param0);
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
public boolean isEsercizioAperto(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, EJBException{
	pre_component_invocation(param0,componentObj);
	try {
		boolean result = ((EsercizioComponent)componentObj).isEsercizioAperto(param0);
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
public it.cnr.contab.config00.esercizio.bulk.EsercizioBulk getEsercizio(it.cnr.jada.UserContext param0 ) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.config00.esercizio.bulk.EsercizioBulk result = ((EsercizioComponent)componentObj).getEsercizio(param0);
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
