package it.cnr.contab.progettiric00.ejb.geco;

import it.cnr.contab.config00.geco.bulk.Geco_dipartimentiIBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_commessaIBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_moduloIBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_progettoIBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.NoRollbackException;
import it.cnr.jada.persistency.Persistent;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless(name="CNRPROGETTIRIC00_EJB_GECO_ProgettoGecoComponentSession")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ProgettoGecoComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ProgettoGecoComponentSession{
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new it.cnr.contab.progettiric00.comp.geco.ProgettoGecoComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
		return new ProgettoGecoComponentSessionBean();
	}
	
	public List<Geco_progettoIBulk>  cercaProgettiGeco(it.cnr.jada.UserContext param0, OggettoBulk oggettoBulk, Class<? extends OggettoBulk> bulkClass) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		componentObj.initialize();
		try {
			List<Geco_progettoIBulk> result = ((it.cnr.contab.progettiric00.comp.geco.ProgettoGecoComponent)componentObj).cercaProgettiGeco(param0, oggettoBulk, bulkClass);
			componentObj.release(param0);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			componentObj.release(param0);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			componentObj.release(param0);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	
	public List<Geco_commessaIBulk>  cercaCommesseGeco(it.cnr.jada.UserContext param0, OggettoBulk oggettoBulk, Class<? extends OggettoBulk> bulkClass) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		try {
			componentObj.initialize();
			List<Geco_commessaIBulk> result = ((it.cnr.contab.progettiric00.comp.geco.ProgettoGecoComponent)componentObj).cercaCommesseGeco(param0, oggettoBulk, bulkClass);
			componentObj.release(param0);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			componentObj.release(param0);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			componentObj.release(param0);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}

	public List<Geco_moduloIBulk>  cercaModuliGeco(it.cnr.jada.UserContext param0, OggettoBulk oggettoBulk, Class<? extends OggettoBulk> bulkClass) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		try {
			componentObj.initialize();
			List<Geco_moduloIBulk> result = ((it.cnr.contab.progettiric00.comp.geco.ProgettoGecoComponent)componentObj).cercaModuliGeco(param0, oggettoBulk, bulkClass);
			componentObj.release(param0);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			componentObj.release(param0);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			componentObj.release(param0);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public List<Geco_dipartimentiIBulk>  cercaDipartimentiGeco(it.cnr.jada.UserContext param0, OggettoBulk oggettoBulk, Class<? extends OggettoBulk> bulkClass) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		try {
			componentObj.initialize();
			List<Geco_dipartimentiIBulk> result = ((it.cnr.contab.progettiric00.comp.geco.ProgettoGecoComponent)componentObj).cercaDipartimentiGeco(param0, oggettoBulk, bulkClass);
			componentObj.release(param0);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			componentObj.release(param0);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			componentObj.release(param0);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	@Override
	public Persistent findByPrimaryKey(UserContext userContext, OggettoBulk oggettoBulk) throws ComponentException, EJBException {
        try{
        	componentObj.initialize();
        	Persistent persistent = ((it.cnr.contab.progettiric00.comp.geco.ProgettoGecoComponent)componentObj).findByPrimaryKey(userContext, oggettoBulk);
        	componentObj.release(userContext);
            return persistent;
        }catch(NoRollbackException norollbackexception){
        	componentObj.release(userContext);
            throw norollbackexception;
        }catch(ComponentException componentexception){
        	componentObj.release(userContext);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(userContext, componentObj, error);
        }
	}
	
}
