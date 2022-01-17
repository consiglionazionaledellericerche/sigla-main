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
import it.cnr.contab.client.docamm.Contratto;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.ejb.ContrattoComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.utenze00.bp.Costanti;
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
@WebService(endpointInterface = "it.cnr.contab.anagraf00.ejb.ContrattoComponentSessionWS")
@XmlSeeAlso({ java.util.ArrayList.class })


public class ContrattoComponentWS {
	@EJB FatturaAttivaSingolaComponentSession fatturaAttivaSingolaComponentSession;
	@EJB ContrattoComponentSession contrattoComponentSession;

	@RolesAllowed({ WSAttributes.WSUSERROLE, WSAttributes.IITROLE })
	public java.util.ArrayList<Contratto> cercaContratti(Integer esercizio,
			String uo, String tipo, String query, String dominio,
			Integer numMax, String user, String ricerca) throws Exception {
		java.util.ArrayList<Contratto> listaContratti = new ArrayList<Contratto>();
		List contratti = null;
		try {
			if (user == null)
				user = "IIT";
			if (ricerca == null)
				ricerca = "selettiva";
			if (numMax == null)
				numMax = 20;
			if (esercizio == null)
				throw new SOAPFaultException(faultEsercizioNonDefinito());
			UserContext userContext = new WSUserContext(user, null,
					(esercizio), null, null, null);
			if (uo == null)
				throw new SOAPFaultException(faultUONonDefinita());
			if (tipo == null)
				throw new SOAPFaultException(faultTipoNonDefinito());
			if (query == null) {
				throw new SOAPFaultException(faultQueryNonDefinita());
			} else if (dominio == null
					|| (!dominio.equalsIgnoreCase("codice") && !dominio
							.equalsIgnoreCase("descrizione"))) {
				throw new SOAPFaultException(faultDominioNonDefinito());
			} else {
				try {
					Unita_organizzativaBulk uo_db = new Unita_organizzativaBulk();
					uo_db = (((Unita_organizzativaBulk) fatturaAttivaSingolaComponentSession
							.completaOggetto(userContext,
									new Unita_organizzativaBulk(uo))));
					if (uo_db == null)
						throw new SOAPFaultException(faultUONonDefinita());
					else {
						contratti =contrattoComponentSession
								.findListaContrattiWS(userContext, uo, tipo,
										query, dominio, ricerca);
					}
				} catch (ComponentException e) {
					throw new SOAPFaultException(faultGenerico());
				} catch (RemoteException e) {
					throw new SOAPFaultException(faultGenerico());
				}
			}
			int num = 0;
			if (contratti != null && !contratti.isEmpty()) {
				for (Iterator i = contratti.iterator(); i.hasNext()
						&& num < new Integer(numMax).intValue();) {
					ContrattoBulk contratto = (ContrattoBulk) i.next();
					Contratto c = new Contratto();
					c.setEsercizio(contratto.getEsercizio());
					c.setStato(contratto.getStato());
					c.setPg_contratto(contratto.getPg_contratto());
					c.setCodiceterzo(contratto.getFig_giur_est());
					c.setDescrizione(contratto.getOggetto());
					c.setNatura(contratto.getNatura_contabile());
					c.setIm_contratto_attivo(contratto.getIm_contratto_attivo());
					c.setIm_contratto_passivo(contratto
							.getIm_contratto_passivo());
					listaContratti.add(c);
					num++;
				}
			}
			return listaContratti;
		} catch (NumberFormatException e) {
			throw new SOAPFaultException(faultFormato());
		} catch (SOAPFaultException e) {
			throw e;
		} catch (Exception e) {
			throw new SOAPFaultException(faultGenerico());
		}
	}

	@RolesAllowed({ WSAttributes.WSUSERROLE, WSAttributes.IITROLE })
	public String cercaContrattiXml(String esercizio, String uo, String tipo,
			String query, String dominio, String numMax, String user,
			String ricerca) throws Exception {
		List contratti = null;
		try {
			if (esercizio == null)
				throw new SOAPFaultException(faultEsercizioNonDefinito());
			UserContext userContext = new WSUserContext(user, null,
					new Integer(esercizio), null, null, null);
			if (uo == null)
				throw new SOAPFaultException(faultUONonDefinita());
			if (tipo == null)
				throw new SOAPFaultException(faultTipoNonDefinito());
			if (query == null) {
				throw new SOAPFaultException(faultQueryNonDefinita());
			} else if (dominio == null
					|| (!dominio.equalsIgnoreCase("codice") && !dominio
							.equalsIgnoreCase("descrizione"))) {
				throw new SOAPFaultException(faultDominioNonDefinito());
			} else {
				try {
					Unita_organizzativaBulk uo_db = new Unita_organizzativaBulk();
					uo_db = (((Unita_organizzativaBulk) fatturaAttivaSingolaComponentSession
							.completaOggetto(userContext,
									new Unita_organizzativaBulk(uo))));
					if (uo_db == null)
						throw new SOAPFaultException(faultUONonDefinita());
					else {
						contratti = contrattoComponentSession
								.findListaContrattiWS(userContext, uo, tipo,
										query, dominio, ricerca);
					}
				} catch (ComponentException e) {
					throw new SOAPFaultException(faultGenerico());
				} catch (RemoteException e) {
					throw new SOAPFaultException(faultGenerico());
				}
			}
			return generaXML(numMax, tipo, contratti);
		} catch (NumberFormatException e) {
			throw new SOAPFaultException(faultFormato());
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

	private SOAPFault faultUONonDefinita() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_105.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_105));
	}

	private SOAPFault faultTipoNonDefinito() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_106.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_106));
	}

	private SOAPFault faultEsercizioNonDefinito() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_107.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_107));
	}

	private SOAPFault faultFormato() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_113.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_113));
	}

	public String generaXML(String numMax, String tipo, List Contratti)
			throws ParserConfigurationException, TransformerException {
		if (numMax == null)
			numMax = new Integer(20).toString();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation impl = builder.getDOMImplementation();
		Document xmldoc = impl.createDocument(null, "root", null);
		Element root = xmldoc.getDocumentElement();
		root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance",
				"xsi:noNamespaceSchemaLocation",
				"https://contab.cnr.it/SIGLA/schema/cercacontratti.xsd");
		root.appendChild(generaNumeroContratti(xmldoc, Contratti));
		int num = 0;
		if (Contratti != null && !Contratti.isEmpty()) {
			for (Iterator i = Contratti.iterator(); i.hasNext()
					&& num < new Integer(numMax).intValue();) {
				ContrattoBulk contratto = (ContrattoBulk) i.next();
				root.appendChild(generaDettaglioContratti(xmldoc, contratto
						.getEsercizio().toString(), contratto.getStato(),
						contratto.getPg_contratto().toString(), contratto
								.getFig_giur_est().toString(), contratto
								.getOggetto(), contratto.getNatura_contabile(),
						contratto.getIm_contratto_attivo().toString(),
						contratto.getIm_contratto_passivo().toString()));
				num++;
			}
		}

		DOMSource domSource = new DOMSource(xmldoc);
		StringWriter domWriter = new StringWriter();
		StreamResult streamResult = new StreamResult(domWriter);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		// serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"http://150.146.206.250/DTD/cercaterzi.dtd");
		// serializer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,"cercatariffari");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.STANDALONE, "no");
		serializer.transform(domSource, streamResult);
		return domWriter.toString();
	}

	private Element generaNumeroContratti(Document xmldoc, List Contratti) {
		Element e = xmldoc.createElement("numris");
		Node n = xmldoc
				.createTextNode(new Integer(Contratti.size()).toString());
		e.appendChild(n);
		return e;
	}

	private Element generaDettaglioContratti(Document xmldoc, String esercizio,
			String stato, String pg, String cliente, String oggetto,
			String natura, String im_contratto_attivo,
			String im_contratto_passivo) {

		Element element = xmldoc.createElement("contratto");

		Element elementCodice = xmldoc.createElement("esercizio");
		Node nodeCodice = xmldoc.createTextNode(esercizio);
		elementCodice.appendChild(nodeCodice);
		element.appendChild(elementCodice);

		Element elementStato = xmldoc.createElement("stato");
		Node nodeStato = xmldoc.createTextNode(stato);
		elementStato.appendChild(nodeStato);
		element.appendChild(elementStato);

		Element elementPg = xmldoc.createElement("pg_contratto");
		Node nodePg = xmldoc.createTextNode(pg);
		elementPg.appendChild(nodePg);
		element.appendChild(elementPg);

		Element elementCliente = xmldoc.createElement("codiceterzo");
		Node nodeCliente = xmldoc.createTextNode(cliente);
		elementCliente.appendChild(nodeCliente);
		element.appendChild(elementCliente);

		Element elementDenominazione = xmldoc.createElement("descrizione");
		Node nodeDenominazione = xmldoc.createTextNode(oggetto == null ? ""
				: oggetto);
		elementDenominazione.appendChild(nodeDenominazione);
		element.appendChild(elementDenominazione);

		Element elementNat = xmldoc.createElement("natura");
		Node nodeNat = xmldoc
				.createTextNode(ContrattoBulk.ti_natura_contabileKeys.get(
						natura).toString());
		elementNat.appendChild(nodeNat);
		element.appendChild(elementNat);

		Element elementIm = xmldoc.createElement("im_contratto_attivo");
		Node nodeIm = xmldoc.createTextNode(im_contratto_attivo);
		elementIm.appendChild(nodeIm);
		element.appendChild(elementIm);

		elementIm = xmldoc.createElement("im_contratto_passivo");
		nodeIm = xmldoc.createTextNode(im_contratto_passivo);
		elementIm.appendChild(nodeIm);
		element.appendChild(elementIm);
		return element;
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
