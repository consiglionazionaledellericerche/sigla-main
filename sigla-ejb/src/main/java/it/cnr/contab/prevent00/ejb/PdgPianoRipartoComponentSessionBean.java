package it.cnr.contab.prevent00.ejb;

import it.cnr.contab.prevent00.comp.PdgPianoRipartoComponent;

import javax.annotation.PostConstruct;
import javax.ejb.*;
/**
 * Bean implementation class for Enterprise Bean: CNRPDG00_EJB_PdgPianoRipartoComponentSession
 */
@Stateless(name="CNRPDG00_EJB_PdgPianoRipartoComponentSession")
public class PdgPianoRipartoComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements PdgPianoRipartoComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.prevent00.comp.PdgPianoRipartoComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
		return new PdgPianoRipartoComponentSessionBean();
	}
	public it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk findParametriLivelli(it.cnr.jada.UserContext param0, java.lang.Integer param1)  throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk result = ((PdgPianoRipartoComponent)componentObj).findParametriLivelli(param0, param1);
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

	public it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk aggiornaTotaleGeneraleImpdaRipartire(it.cnr.jada.UserContext param0, it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk param1)  throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk result = ((PdgPianoRipartoComponent)componentObj).aggiornaTotaleGeneraleImpdaRipartire(param0, param1);
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
	public boolean isPdgPianoRipartoDefinitivo(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((PdgPianoRipartoComponent)componentObj).isPdgPianoRipartoDefinitivo(param0,param1);
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
	public it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk rendiPdgPianoRipartoDefinitivo(it.cnr.jada.UserContext param0, it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk result = ((PdgPianoRipartoComponent)componentObj).rendiPdgPianoRipartoDefinitivo(param0, param1);
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
	public it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk rendiPdgPianoRipartoProvvisorio(it.cnr.jada.UserContext param0, it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk result = ((PdgPianoRipartoComponent)componentObj).rendiPdgPianoRipartoProvvisorio(param0, param1);
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
	public it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk caricaStruttura(it.cnr.jada.UserContext param0, it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk result = ((PdgPianoRipartoComponent)componentObj).caricaStruttura(param0, param1);
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
