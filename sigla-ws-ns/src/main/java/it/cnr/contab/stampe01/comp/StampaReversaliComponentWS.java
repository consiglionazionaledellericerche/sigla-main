package it.cnr.contab.stampe01.comp;

import it.cnr.contab.doccont00.ejb.ReversaleComponentSession;
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
@WebService(endpointInterface = "it.cnr.contab.stampe01.comp.StampaReversaliComponentSessionWS")
@WebContext(contextRoot = "/SIGLA-SIGLAEJB_1")
public class StampaReversaliComponentWS {
	@EJB ReversaleComponentSession reversaleComponentSession;

	public byte[] DownloadReversale(String user, String cds, String esercizio,
			String pgReversale) throws NumberFormatException,
			PersistencyException, ComponentException, RemoteException,
			EJBException, Exception {
		try {
			UserContext userContext = new WSUserContext(user, null,
					new Integer(java.util.Calendar.getInstance().get(
							java.util.Calendar.YEAR)), null, null, null);
			return reversaleComponentSession.lanciaStampa(
					userContext, cds, new Integer(esercizio), new Long(
							pgReversale));
		} catch (FatturaNonTrovataException e) {
			throw new SOAPFaultException(faultFatturaNonTrovata());
		} catch (GenerazioneReportException e) {
			throw new SOAPFaultException(faultGenerazioneStampa());
		}

	}

	private SOAPFault faultFatturaNonTrovata() throws SOAPException {
		return generaFault("Reversale non trovata");
	}

	private SOAPFault faultGenerazioneStampa() throws SOAPException {
		return generaFault("Generazione stampa non riuscita");
	}

	private SOAPFault generaFault(String stringFault) throws SOAPException {
		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage message = factory.createMessage();
		SOAPFactory soapFactory = SOAPFactory.newInstance();
		SOAPBody body = message.getSOAPBody();
		SOAPFault fault = body.addFault();
		Name faultName = soapFactory.createName("", "",
				SOAPConstants.URI_NS_SOAP_ENVELOPE);
		fault.setFaultCode(faultName);
		fault.setFaultString(stringFault);
		return fault;
	}
}
