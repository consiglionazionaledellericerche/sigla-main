package it.cnr.contab.ordmag.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdBulk;
import it.cnr.contab.ordmag.comp.NumeratoriOrdMagComponent;

@Stateless(name="CNRORDMAG_EJB_NumeratoriOrdMagComponentSession")
public class NumeratoriOrdMagComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements NumeratoriOrdMagComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new NumeratoriOrdMagComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new NumeratoriOrdMagComponentSessionBean();
	}
	public java.lang.Long getNextPG(it.cnr.jada.UserContext param0,NumerazioneMagBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.lang.Long result = ((NumeratoriOrdMagComponent)componentObj).getNextPG(param0,param1);
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
	public java.lang.Integer getNextPG(it.cnr.jada.UserContext param0,NumerazioneOrdBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.lang.Integer result = ((NumeratoriOrdMagComponent)componentObj).getNextPG(param0,param1);
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
