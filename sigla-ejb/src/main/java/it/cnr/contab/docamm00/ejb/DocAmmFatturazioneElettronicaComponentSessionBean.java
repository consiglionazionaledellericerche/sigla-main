package it.cnr.contab.docamm00.ejb;

import it.cnr.contab.docamm00.comp.DocAmmFatturazioneElettronicaComponent;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaElettronicaType;

import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.mail.PasswordAuthentication;
import javax.xml.bind.JAXBElement;

/**
 * Bean implementation class for Enterprise Bean: CNRDOCAMM00_EJB_DocAmmFatturazioneElettronicaComponentSession
 */
@Stateless(name="CNRDOCAMM00_EJB_DocAmmFatturazioneElettronicaComponentSession")
public class DocAmmFatturazioneElettronicaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements DocAmmFatturazioneElettronicaComponentSession  {
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new DocAmmFatturazioneElettronicaComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
		return new DocAmmFatturazioneElettronicaComponentSessionBean();
	}
	
	public PasswordAuthentication getAuthenticatorFromUo(UserContext userContext, String uo) throws it.cnr.jada.comp.ComponentException {
		pre_component_invocation(userContext,componentObj);
		try {
			PasswordAuthentication result = ((DocAmmFatturazioneElettronicaComponent)componentObj).getAuthenticatorFromUo(userContext, uo);
			component_invocation_succes(userContext,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}
	public JAXBElement<FatturaElettronicaType> creaFatturaElettronicaType(UserContext userContext, Fattura_attivaBulk fattura)  throws it.cnr.jada.comp.ComponentException{
		pre_component_invocation(userContext,componentObj);
		try {
			JAXBElement<FatturaElettronicaType> result = ((DocAmmFatturazioneElettronicaComponent)componentObj).creaFatturaElettronicaType(userContext, fattura);
			component_invocation_succes(userContext,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}

	public String recuperoNomeFileXml(UserContext userContext, Fattura_attivaBulk fattura)  throws RemoteException, it.cnr.jada.comp.ComponentException{
		pre_component_invocation(userContext,componentObj);
		try {
			String result = ((DocAmmFatturazioneElettronicaComponent)componentObj).recuperoNomeFileXml(userContext, fattura);
			component_invocation_succes(userContext,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}

	public String recuperoInizioNomeFile(UserContext userContext) throws RemoteException,ComponentException {
		pre_component_invocation(userContext,componentObj);
		try {
			String result = ((DocAmmFatturazioneElettronicaComponent)componentObj).recuperoInizioNomeFile(userContext);
			component_invocation_succes(userContext,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}
}
