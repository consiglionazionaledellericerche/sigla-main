package it.cnr.contab.config00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.config00.bulk.ServizioPecBulk;
import it.cnr.contab.config00.comp.Parametri_cnrComponent;
import it.cnr.contab.config00.comp.Parametri_enteComponent;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.UnitaOrganizzativaPecBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

/**
 * Bean implementation class for Enterprise Bean: CNRCONFIG00_EJB_Parametri_enteComponentSession
 */
@Stateless(name="CNRCONFIG00_EJB_Parametri_enteComponentSession")
public class Parametri_enteComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements Parametri_enteComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.config00.comp.Parametri_enteComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new Parametri_enteComponentSessionBean();
	}
	public it.cnr.contab.config00.bulk.Parametri_enteBulk getParametriEnte(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.config00.bulk.Parametri_enteBulk result = ((Parametri_enteComponent)componentObj).getParametriEnte(param0);
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
	public int getGiorniRimanenti(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			int result = ((Parametri_enteComponent)componentObj).getGiorniRimanenti(param0);
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
	public ServizioPecBulk getServizioPec(it.cnr.jada.UserContext param0, String param1) throws it.cnr.jada.comp.ComponentException {
		pre_component_invocation(param0,componentObj);
		try {
			ServizioPecBulk result = ((Parametri_enteComponent)componentObj).getServizioPec(param0,param1);
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
	public CdsBulk getCds(it.cnr.jada.UserContext param0, String param1) throws it.cnr.jada.comp.ComponentException {
		pre_component_invocation(param0,componentObj);
		try {
			CdsBulk result = ((Parametri_enteComponent)componentObj).getCds(param0,param1);
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
	public Unita_organizzativaBulk getUo(it.cnr.jada.UserContext param0, String param1) throws it.cnr.jada.comp.ComponentException {
		pre_component_invocation(param0,componentObj);
		try {
			Unita_organizzativaBulk result = ((Parametri_enteComponent)componentObj).getUo(param0,param1);
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
	public CdrBulk getCdr(it.cnr.jada.UserContext param0, String param1) throws it.cnr.jada.comp.ComponentException {
		pre_component_invocation(param0,componentObj);
		try {
			CdrBulk result = ((Parametri_enteComponent)componentObj).getCdr(param0,param1);
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
	public UnitaOrganizzativaPecBulk getUnitaOrganizzativaPec(it.cnr.jada.UserContext param0, String param1) throws it.cnr.jada.comp.ComponentException {
		pre_component_invocation(param0,componentObj);
		try {
			UnitaOrganizzativaPecBulk result = ((Parametri_enteComponent)componentObj).getUnitaOrganizzativaPec(param0,param1);
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
	public boolean isProgettoPianoEconomicoEnabled(it.cnr.jada.UserContext param0, int param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException{
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((Parametri_enteComponent)componentObj).isProgettoPianoEconomicoEnabled(param0, param1);
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
