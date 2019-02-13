package it.cnr.contab.docamm00.ejb;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.xml.datatype.XMLGregorianCalendar;

import it.cnr.contab.docamm00.comp.FatturaElettronicaAttivaComponent;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.NoRollbackException;
import it.cnr.jada.persistency.PersistencyException;
@Stateless(name="CNRDOCAMM00_EJB_FatturaElettronicaAttivaComponentSession")
public class FatturaElettronicaAttivaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements FatturaElettronicaAttivaComponentSession {
@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.docamm00.comp.FatturaElettronicaAttivaComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new FatturaElettronicaAttivaComponentSessionBean();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Fattura_attivaBulk aggiornaFatturaRicevutaConsegnaInvioSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva, String codiceSdi, XMLGregorianCalendar dataConsegnaSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException{
        pre_component_invocation(userContext, componentObj);
        try{
        	Fattura_attivaBulk result = ((FatturaElettronicaAttivaComponent)componentObj).aggiornaFatturaRicevutaConsegnaInvioSDI(userContext, fatturaAttiva, codiceSdi, dataConsegnaSdi);
            component_invocation_succes(userContext, componentObj);
            return result;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(userContext, componentObj, error);
        }
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Fattura_attivaBulk aggiornaFatturaRifiutataDestinatarioSDI(UserContext userContext, Fattura_attivaBulk fattura, String noteSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException{
        pre_component_invocation(userContext, componentObj);
        try{
        	Fattura_attivaBulk result = ((FatturaElettronicaAttivaComponent)componentObj).aggiornaFatturaRifiutataDestinatarioSDI(userContext, fattura, noteSdi);
            component_invocation_succes(userContext, componentObj);
            return result;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(userContext, componentObj, error);
        }
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Fattura_attivaBulk aggiornaFatturaConsegnaSDI(UserContext userContext, Fattura_attivaBulk fattura, Date dataConsegna) throws PersistencyException, ComponentException,java.rmi.RemoteException{
        pre_component_invocation(userContext, componentObj);
        try{
        	Fattura_attivaBulk result = ((FatturaElettronicaAttivaComponent)componentObj).aggiornaFatturaConsegnaSDI(userContext, fattura, dataConsegna);
            component_invocation_succes(userContext, componentObj);
            return result;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(userContext, componentObj, error);
        }
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Fattura_attivaBulk aggiornaFatturaScartoSDI(UserContext userContext, Fattura_attivaBulk fattura, String codiceInvioSdi, String noteSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException{
        pre_component_invocation(userContext, componentObj);
        try{
        	Fattura_attivaBulk result = ((FatturaElettronicaAttivaComponent)componentObj).aggiornaFatturaScartoSDI(userContext, fattura, codiceInvioSdi, noteSdi);
            component_invocation_succes(userContext, componentObj);
            return result;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(userContext, componentObj, error);
        }
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Fattura_attivaBulk aggiornaFatturaMancataConsegnaInvioSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva, String codiceSdi, String noteInvioSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException{
        pre_component_invocation(userContext, componentObj);
        try{
        	Fattura_attivaBulk result = ((FatturaElettronicaAttivaComponent)componentObj).aggiornaFatturaMancataConsegnaInvioSDI(userContext, fatturaAttiva, codiceSdi, noteInvioSdi);
            component_invocation_succes(userContext, componentObj);
            return result;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(userContext, componentObj, error);
        }
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Fattura_attivaBulk aggiornaFatturaDecorrenzaTerminiSDI(UserContext userContext, Fattura_attivaBulk fattura, String noteSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException{
        pre_component_invocation(userContext, componentObj);
        try{
        	Fattura_attivaBulk result = ((FatturaElettronicaAttivaComponent)componentObj).aggiornaFatturaDecorrenzaTerminiSDI(userContext, fattura, noteSdi);
            component_invocation_succes(userContext, componentObj);
            return result;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(userContext, componentObj, error);
        }
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Fattura_attivaBulk aggiornaFatturaEsitoAccettatoSDI(UserContext userContext, Fattura_attivaBulk fattura) throws PersistencyException, ComponentException,java.rmi.RemoteException{
        pre_component_invocation(userContext, componentObj);
        try{
        	Fattura_attivaBulk result = ((FatturaElettronicaAttivaComponent)componentObj).aggiornaFatturaEsitoAccettatoSDI(userContext, fattura);
            component_invocation_succes(userContext, componentObj);
            return result;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(userContext, componentObj, error);
        }
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Fattura_attivaBulk aggiornaFatturaTrasmissioneNonRecapitataSDI(UserContext userContext, Fattura_attivaBulk fattura, String codiceSdi, String noteInvioSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException{
        pre_component_invocation(userContext, componentObj);
        try{
        	Fattura_attivaBulk result = ((FatturaElettronicaAttivaComponent)componentObj).aggiornaFatturaTrasmissioneNonRecapitataSDI(userContext, fattura, codiceSdi, noteInvioSdi);
            component_invocation_succes(userContext, componentObj);
            return result;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(userContext, componentObj, error);
        }
	}
	public void gestioneInvioMailNonRecapitabilita(UserContext param0, Fattura_attivaBulk fatturaAttiva) throws ComponentException,java.rmi.RemoteException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((FatturaElettronicaAttivaComponentSession)componentObj).gestioneInvioMailNonRecapitabilita(param0,fatturaAttiva);
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