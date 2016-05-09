package it.cnr.contab.stampe01.comp;

import it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.FatturaNonTrovataException;
import it.cnr.jada.comp.GenerazioneReportException;
import it.cnr.jada.persistency.PersistencyException;

import java.rmi.RemoteException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.soap.SOAPFaultException;

import org.jboss.ws.api.annotation.WebContext;

@Stateless
@WebService(endpointInterface = "it.cnr.contab.stampe01.comp.StampaVariazioniComponentSessionWS")
@WebContext(contextRoot = "/SIGLA-SIGLAEJB_1")
public class StampaVariazioniComponentWS {
	@EJB PdGVariazioniComponentSession pdGVariazioniComponentSession;
	
	public byte[] DownloadVariazione(String user, String esercizio, String pg,
			String tipoVar) throws NumberFormatException, PersistencyException,
			ComponentException, RemoteException, EJBException, Exception {
		UserContext userContext = new WSUserContext(user, null, new Integer(
				java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),
				null, null, null);
		try {
			return pdGVariazioniComponentSession.lanciaStampa(
					userContext, new Integer(esercizio), new Integer(pg),
					new String(tipoVar));
		} catch (GenerazioneReportException e) {
			throw new SOAPFaultException(faultGenerazioneStampa());
		} catch (FatturaNonTrovataException e) {
			throw new SOAPFaultException(faultFatturaNonTrovata());
		}
	}

	private SOAPFault faultFatturaNonTrovata() throws SOAPException {
		return generaFault("002", "Variazione non trovata");
	}

	private SOAPFault faultGenerazioneStampa() throws SOAPException {
		return generaFault("004", "Generazione stampa non riuscita");
	}

	private SOAPFault generaFault(String localName, String stringFault)
			throws SOAPException {
		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage message = factory.createMessage();
		SOAPFactory soapFactory = SOAPFactory.newInstance();
		SOAPBody body = message.getSOAPBody();
		SOAPFault fault = body.addFault();
		Name faultName = soapFactory.createName(localName, "",
				SOAPConstants.URI_NS_SOAP_ENVELOPE);

		fault.setFaultCode(faultName);
		fault.setFaultString(stringFault);
		return fault;
	}
}
