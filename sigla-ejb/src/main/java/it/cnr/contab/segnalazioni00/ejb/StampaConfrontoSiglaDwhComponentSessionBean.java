package it.cnr.contab.segnalazioni00.ejb;

import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSessionBean;
import it.cnr.contab.segnalazioni00.bulk.Stampa_attivita_siglaBulk;
import it.cnr.contab.segnalazioni00.bulk.Stampa_confronto_sigla_dwhBulk;
import it.cnr.contab.segnalazioni00.comp.StampaConfrontoSiglaDwhComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

/**
 * Bean implementation class for Enterprise Bean: CNRSEGNALAZIONI00_EJB_ContrattoComponentSession
 */
@Stateless(name="CNRSEGNALAZIONI00_EJB_StampaConfrontoSiglaDwhComponentSession")
public class StampaConfrontoSiglaDwhComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements StampaConfrontoSiglaDwhComponentSession{
//	private StampaConfrontoSiglaDwhComponent componentObj;
	
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new StampaConfrontoSiglaDwhComponent();
	}
	
	
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new StampaConfrontoSiglaDwhComponentSessionBean();
	}
	
	
	public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((StampaConfrontoSiglaDwhComponent)componentObj).inizializzaBulkPerStampa(param0,param1);
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
	
	public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((StampaConfrontoSiglaDwhComponent)componentObj).stampaConBulk(param0,param1);
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
	
	public OggettoBulk inizializzaDate(UserContext param0, Stampa_confronto_sigla_dwhBulk param1) throws ComponentException {
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((StampaConfrontoSiglaDwhComponent)componentObj).stampaConBulk(param0,param1);
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
	
	
	public OggettoBulk inizializzaEsercizio(UserContext param0, Stampa_attivita_siglaBulk param1) throws ComponentException {
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((StampaConfrontoSiglaDwhComponent)componentObj).stampaConBulk(param0,param1);
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