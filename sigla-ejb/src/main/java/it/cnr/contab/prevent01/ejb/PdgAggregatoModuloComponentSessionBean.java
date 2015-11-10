package it.cnr.contab.prevent01.ejb;
import it.cnr.contab.prevent01.comp.PdgAggregatoModuloComponent;

import javax.annotation.PostConstruct;
import javax.ejb.*;
@Stateless(name="CNRPREVENT01_EJB_PdgAggregatoModuloComponentSession")
public class PdgAggregatoModuloComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements PdgAggregatoModuloComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.prevent01.comp.PdgAggregatoModuloComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
	return new PdgAggregatoModuloComponentSessionBean();
}
public it.cnr.contab.config00.sto.bulk.CdrBulk cdrFromUserContext(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.config00.sto.bulk.CdrBulk result = ((PdgAggregatoModuloComponent)componentObj).cdrFromUserContext(param0);
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
public boolean isPdGAggregatoModificabile(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_moduloBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		boolean result = ((PdgAggregatoModuloComponent)componentObj).isPdGAggregatoModificabile(param0,param1);
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
public it.cnr.jada.bulk.OggettoBulk modificaStatoPdg_aggregato(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_moduloBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.bulk.OggettoBulk result = ((PdgAggregatoModuloComponent)componentObj).modificaStatoPdg_aggregato(param0,param1);
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
public it.cnr.contab.prevent01.bulk.Pdg_moduloBulk findAndInsertBulkForMacro(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_moduloBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.prevent01.bulk.Pdg_moduloBulk result = ((PdgAggregatoModuloComponent)componentObj).findAndInsertBulkForMacro(param0,param1);
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
public it.cnr.jada.bulk.OggettoBulk scaricaDipendentiSuPdGP(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.bulk.OggettoBulk result = ((PdgAggregatoModuloComponent)componentObj).scaricaDipendentiSuPdGP(param0,param1);
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
public it.cnr.jada.bulk.OggettoBulk annullaScaricaDipendentiSuPdGP(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.bulk.OggettoBulk result = ((PdgAggregatoModuloComponent)componentObj).annullaScaricaDipendentiSuPdGP(param0,param1);
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
public it.cnr.jada.bulk.OggettoBulk getCdrPdGP(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.bulk.OggettoBulk result = ((PdgAggregatoModuloComponent)componentObj).getCdrPdGP(param0,param1);
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
public void validaCancellazionePdgModulo(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_moduloBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		((PdgAggregatoModuloComponent)componentObj).validaCancellazionePdgModulo(param0,param1);
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
