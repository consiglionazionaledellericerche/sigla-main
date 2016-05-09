package it.cnr.contab.anagraf00.comp;

import it.cnr.contab.client.docamm.ModalitaIncasso;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.intrastat.bulk.Modalita_incassoBulk;
import it.cnr.contab.utenze00.bp.Costanti;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
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

/**
 * Questa classe svolge le operazioni fondamentali di lettura, scrittura e
 * filtro dei dati immessi o richiesti dall'utente. In oltre sovrintende alla
 * gestione e creazione dati a cui l'utente stesso non ha libero accesso e/o non
 * gli sono trasparenti.
 */
@Stateless
@WebService(endpointInterface = "it.cnr.contab.anagraf00.ejb.ModalitaIncassoComponentSessionWS")
@XmlSeeAlso({ java.util.ArrayList.class })
@DeclareRoles({ "WSUserRole", "IITRole" })
@WebContext(authMethod = "WSSE", contextRoot = "SIGLA-SIGLAEJB")
public class ModalitaIncassoComponentWS {
	@EJB FatturaAttivaSingolaComponentSession fatturaAttivaSingolaComponentSession;

	public java.util.ArrayList<ModalitaIncasso> cercaModalitaIncasso(
			String query, Integer esercizio, String dominio, Integer numMax,
			String user, String ricerca) throws Exception {
		try {
			java.util.ArrayList<ModalitaIncasso> listaModalitaIncasso = new ArrayList<ModalitaIncasso>();
			List listaModalita = null;
			if (user == null)
				user = "IIT";
			if (ricerca == null)
				ricerca = "selettiva";
			if (numMax == null)
				numMax = 20;

			UserContext userContext = new WSUserContext(user, null,
					(esercizio), null, null, null);

			if (query == null) {
				throw new SOAPFaultException(faultQueryNonDefinita());
			} else if (esercizio == null)
				throw new SOAPFaultException(faultEsercizioNonDefinito());
			else if (dominio == null
					|| (!dominio.equalsIgnoreCase("codice") && !dominio
							.equalsIgnoreCase("descrizione"))) {
				throw new SOAPFaultException(faultDominioNonDefinito());
			} else {
				try {
					listaModalita = fatturaAttivaSingolaComponentSession
							.findListaModalitaIncassoWS(userContext, query,
									dominio, ricerca);
				} catch (ComponentException e) {
					throw new SOAPFaultException(faultGenerico());
				} catch (RemoteException e) {
					throw new SOAPFaultException(faultGenerico());
				}
			}

			int num = 0;
			if (listaModalita != null && !listaModalita.isEmpty()) {
				for (Iterator i = listaModalita.iterator(); i.hasNext()
						&& num < new Integer(numMax).intValue();) {
					Modalita_incassoBulk modinc = (Modalita_incassoBulk) i
							.next();
					ModalitaIncasso mi = new ModalitaIncasso();
					mi.setEsercizio(modinc.getEsercizio());
					mi.setCodice(modinc.getCd_modalita_incasso());
					mi.setDescrizione(modinc.getDs_modalita_incasso());
					listaModalitaIncasso.add(mi);
					num++;
				}
			}
			return listaModalitaIncasso;
		} catch (SOAPFaultException e) {
			throw e;
		} catch (Exception e) {
			throw new SOAPFaultException(faultGenerico());
		}
	}

	private SOAPFault faultGenerico() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_100.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_100));
	}

	private SOAPFault faultQueryNonDefinita() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_101.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_101));
	}

	private SOAPFault faultDominioNonDefinito() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_102.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_102));
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

	private SOAPFault faultEsercizioNonDefinito() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_107.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_107));
	}
}
