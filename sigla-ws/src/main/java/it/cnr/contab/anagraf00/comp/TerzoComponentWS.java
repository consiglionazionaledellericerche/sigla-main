/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.anagraf00.comp;

import it.cnr.contab.WSAttributes;
import it.cnr.contab.anagraf00.core.bulk.V_terzo_anagrafico_sipBulk;
import it.cnr.contab.anagraf00.ejb.TerzoComponentSession;
import it.cnr.contab.client.docamm.Terzo;
import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.soap.SOAPFaultException;

import org.jboss.ws.api.annotation.WebContext;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Questa classe svolge le operazioni fondamentali di lettura, scrittura e
 * filtro dei dati immessi o richiesti dall'utente. In oltre sovrintende alla
 * gestione e creazione dati a cui l'utente stesso non ha libero accesso e/o non
 * gli sono trasparenti.
 */
@Stateless
@WebService(endpointInterface = "it.cnr.contab.anagraf00.ejb.TerzoComponentSessionWS")
@XmlSeeAlso({ java.util.ArrayList.class })


public class TerzoComponentWS {
	@EJB TerzoComponentSession terzoComponentSession;
	
	@RolesAllowed({ WSAttributes.WSUSERROLE, WSAttributes.IITROLE })
	public java.util.ArrayList<Terzo> cercaTerzo(String query, String dominio,
			String tipoterzo, Integer numMax, String user, String ricerca)
			throws Exception {
		List terzi = null;
		java.util.ArrayList<Terzo> listaterzi = new ArrayList<Terzo>();
		try {
			if (user == null)
				user = "IIT";
			if (ricerca == null)
				ricerca = "selettiva";
			if (numMax == null)
				numMax = 20;

			UserContext userContext = new WSUserContext(user, null,
					new Integer(java.util.Calendar.getInstance().get(
							java.util.Calendar.YEAR)), null, null, null);
			if (query == null) {
				throw new SOAPFaultException(faultQueryNonDefinita());
			} else if (dominio == null
					|| (!dominio.equalsIgnoreCase("cd_terzo") && !dominio
							.equalsIgnoreCase("denominazione"))) {
				throw new SOAPFaultException(faultDominioNonDefinito());
			} else if (tipoterzo == null
					|| (!tipoterzo.equalsIgnoreCase("fisica") && !tipoterzo
							.equalsIgnoreCase("giuridica"))) {
				throw new SOAPFaultException(faultSoggettoTerzoNonDefinito());
			} else {
				try {
					terzi = terzoComponentSession
							.findListaTerziSIP(userContext, query, dominio,
									tipoterzo, ricerca);
				} catch (ComponentException e) {
					throw new SOAPFaultException(faultGenerico());
				} catch (RemoteException e) {
					throw new SOAPFaultException(faultGenerico());
				}
			}
			int num = 0;
			if (terzi != null && !terzi.isEmpty()) {
				for (Iterator i = terzi.iterator(); i.hasNext()
						&& num < new Integer(numMax).intValue();) {
					V_terzo_anagrafico_sipBulk terzo = (V_terzo_anagrafico_sipBulk) i
							.next();
					Terzo t = new Terzo();
					t.setCodice(terzo.getCd_terzo());
					if (tipoterzo.equalsIgnoreCase("fisica")) {
						t.setNome(terzo.getNome());
						t.setCognome(terzo.getCognome());
					} else if (tipoterzo.equalsIgnoreCase("giuridica")) {
						t.setDenominazione(terzo.getDenominazione_sede());
					}
					t.setCodicefiscale(terzo.getCodice_fiscale_pariva());
					t.setPartitaiva(terzo.getCodice_fiscale_pariva());
					listaterzi.add(t);
					num++;
				}
			}

			return listaterzi;
		} catch (SOAPFaultException e) {
			throw e;
		} catch (Exception e) {
			throw new SOAPFaultException(faultGenerico());
		}
	}

	@RolesAllowed({ WSAttributes.WSUSERROLE, WSAttributes.IITROLE })
	public String cercaTerzoXml(String query, String dominio, String tipoterzo,
			String numMax, String user, String ricerca) throws Exception {
		List terzi = null;
		UserContext userContext = new WSUserContext(user, null, new Integer(
				java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),
				null, null, null);
		if (query == null) {
			throw new SOAPFaultException(faultQueryNonDefinita());
		} else if (dominio == null
				|| (!dominio.equalsIgnoreCase("cd_terzo") && !dominio
						.equalsIgnoreCase("denominazione"))) {
			throw new SOAPFaultException(faultDominioNonDefinito());
		} else if (tipoterzo == null
				|| (!tipoterzo.equalsIgnoreCase("fisica") && !tipoterzo
						.equalsIgnoreCase("giuridica"))) {
			throw new SOAPFaultException(faultSoggettoTerzoNonDefinito());
		} else {
			try {
				terzi = terzoComponentSession
						.findListaTerziSIP(userContext, query, dominio,
								tipoterzo, ricerca);
			} catch (ComponentException e) {
				throw new SOAPFaultException(faultGenerico());
			} catch (RemoteException e) {
				throw new SOAPFaultException(faultGenerico());
			}
		}
		try {
			return generaXML(tipoterzo, numMax, terzi);
		} catch (Exception e) {
			throw new SOAPFaultException(faultGenerico());
		}
	}

	private SOAPFault faultQueryNonDefinita() throws SOAPException {
		return generaFault(new String(Constants.ERRORE_SIP_101.toString()),
				Constants.erroriSIP.get(Constants.ERRORE_SIP_101));
	}

	private SOAPFault faultDominioNonDefinito() throws SOAPException {
		return generaFault(new String(Constants.ERRORE_SIP_102.toString()),
				Constants.erroriSIP.get(Constants.ERRORE_SIP_102));
	}

	private SOAPFault faultSoggettoTerzoNonDefinito() throws SOAPException {
		return generaFault(new String(Constants.ERRORE_SIP_104.toString()),
				Constants.erroriSIP.get(Constants.ERRORE_SIP_104));

	}

	private SOAPFault faultGenerico() throws SOAPException {
		return generaFault(new String(Constants.ERRORE_SIP_100.toString()),
				Constants.erroriSIP.get(Constants.ERRORE_SIP_100));
	}

	public String generaXML(String tipoterzo, String numMax, List terzi)
			throws ParserConfigurationException, TransformerException {
		if (numMax == null)
			numMax = new Integer(20).toString();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation impl = builder.getDOMImplementation();
		Document xmldoc = impl.createDocument(
				"http://gestioneistituti.cnr.it/cercaterzi", "cercaterzi:root",
				null);
		Element root = xmldoc.getDocumentElement();
		// if (codiceErrore!= null){
		// root.appendChild(generaErrore(xmldoc));

		// }else{
		root.appendChild(generaNumeroTerzi(xmldoc, terzi));
		int num = 0;
		if (terzi != null && !terzi.isEmpty()) {
			for (Iterator i = terzi.iterator(); i.hasNext()
					&& num < new Integer(numMax).intValue();) {
				V_terzo_anagrafico_sipBulk terzo = (V_terzo_anagrafico_sipBulk) i
						.next();
				if (tipoterzo.equalsIgnoreCase("fisica")) {
					root.appendChild(generaDettaglioTerziFisica(xmldoc,
							terzo.getCd_terzo(), terzo.getCognome(),
							terzo.getNome(), terzo.getCodice_fiscale_pariva()));
				} else if (tipoterzo.equalsIgnoreCase("giuridica")) {
					root.appendChild(generaDettaglioTerziGiuridica(xmldoc,
							terzo.getCd_terzo(), terzo.getDenominazione_sede(),
							terzo.getCodice_fiscale_pariva()));
				}
				num++;
			}
		}
		// }

		DOMSource domSource = new DOMSource(xmldoc);
		StringWriter domWriter = new StringWriter();
		StreamResult streamResult = new StreamResult(domWriter);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
				"http://150.146.206.250/DTD/cercaterzi.dtd");
		serializer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "cercaterzi");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.STANDALONE, "no");
		serializer.transform(domSource, streamResult);
		return domWriter.toString();

	}

	private Element generaNumeroTerzi(Document xmldoc, List terzi) {
		Element e = xmldoc.createElement("cercaterzi:numris");
		Node n = xmldoc.createTextNode(new Integer(terzi.size()).toString());
		e.appendChild(n);
		return e;
	}

	private Element generaDettaglioTerziGiuridica(Document xmldoc,
			Integer codice, String denominazione, String partitaiva) {
		Element elementTerzo = xmldoc.createElement("cercaterzi:terzo");
		Element elementCodice = xmldoc.createElement("cercaterzi:codice");
		Node nodeCodice = xmldoc.createTextNode(codice.toString());
		elementCodice.appendChild(nodeCodice);
		elementTerzo.appendChild(elementCodice);

		Element elementDenominazione = xmldoc
				.createElement("cercaterzi:denominazione");
		Node nodeDenominazione = xmldoc
				.createTextNode(denominazione == null ? "" : denominazione);
		elementDenominazione.appendChild(nodeDenominazione);
		elementTerzo.appendChild(elementDenominazione);

		Element elementPartitaiva = xmldoc
				.createElement("cercaterzi:partitaiva");
		Node nodePartitaiva;
		if (partitaiva != null)
			nodePartitaiva = xmldoc.createTextNode(partitaiva);
		else
			nodePartitaiva = xmldoc.createTextNode("");
		elementPartitaiva.appendChild(nodePartitaiva);
		elementTerzo.appendChild(elementPartitaiva);

		return elementTerzo;
	}

	private Element generaDettaglioTerziFisica(Document xmldoc, Integer codice,
			String cognome, String nome, String codicefiscale) {
		Element elementTerzo = xmldoc.createElement("cercaterzi:terzo");
		Element elementCodice = xmldoc.createElement("cercaterzi:codice");
		Node nodeCodice = xmldoc.createTextNode(codice.toString());
		elementCodice.appendChild(nodeCodice);
		elementTerzo.appendChild(elementCodice);

		Element elementCognome = xmldoc.createElement("cercaterzi:cognome");
		Node nodeCognome = xmldoc
				.createTextNode(cognome == null ? "" : cognome);
		elementCognome.appendChild(nodeCognome);
		elementTerzo.appendChild(elementCognome);

		Element elementNome = xmldoc.createElement("cercaterzi:nome");
		Node nodeNome;
		if (nome != null)
			nodeNome = xmldoc.createTextNode(nome);
		else
			nodeNome = xmldoc.createTextNode("");
		elementNome.appendChild(nodeNome);
		elementTerzo.appendChild(elementNome);

		Element elementCodicefiscale = xmldoc
				.createElement("cercaterzi:codicefiscale");
		Node nodeCodicefiscale;
		if (codicefiscale != null)
			nodeCodicefiscale = xmldoc.createTextNode(codicefiscale);
		else
			nodeCodicefiscale = xmldoc.createTextNode("");
		elementCodicefiscale.appendChild(nodeCodicefiscale);
		elementTerzo.appendChild(elementCodicefiscale);

		return elementTerzo;
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
