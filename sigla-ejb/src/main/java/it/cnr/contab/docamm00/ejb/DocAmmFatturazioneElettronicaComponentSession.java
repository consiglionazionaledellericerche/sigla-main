package it.cnr.contab.docamm00.ejb;

import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaElettronicaType;

import java.rmi.RemoteException;

import javax.ejb.Remote;
import javax.mail.PasswordAuthentication;
import javax.xml.bind.JAXBElement;

@Remote
public interface DocAmmFatturazioneElettronicaComponentSession extends
		it.cnr.jada.ejb.CRUDComponentSession {
	public JAXBElement<FatturaElettronicaType> creaFatturaElettronicaType(
			UserContext userContext, Fattura_attivaBulk fattura)
			throws RemoteException, it.cnr.jada.comp.ComponentException;

	public String recuperoNomeFileXml(UserContext userContext,
			Fattura_attivaBulk fattura) throws RemoteException,
			ComponentException;

	public PasswordAuthentication getAuthenticatorFromUo(
			UserContext userContext, String uo) throws RemoteException,
			ComponentException;

	public String recuperoInizioNomeFile(UserContext userContext)
			throws RemoteException, ComponentException;
}
