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
import it.cnr.contab.client.docamm.Sezionale;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.ejb.SezionaleComponentSession;
import it.cnr.contab.docamm00.tabrif.bulk.SezionaleBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.contab.utenze00.bp.Costanti;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBException;
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

@Stateless
@WebService(endpointInterface = "it.cnr.contab.anagraf00.ejb.SezionaleComponentSessionWS")
@XmlSeeAlso({ java.util.ArrayList.class })


public class SezionaleComponentWS {
	@EJB FatturaAttivaSingolaComponentSession fatturaAttivaSingolaComponentSession;
	@EJB SezionaleComponentSession sezionaleComponentSession;
	
	@RolesAllowed({ WSAttributes.WSUSERROLE, WSAttributes.IITROLE })
	public java.util.ArrayList<Sezionale> cercaSezionale(Integer esercizio,
			String uo, String tipo, String tipo_fattura, String query,
			String dominio, Integer numMax, String user, String ricerca)
			throws Exception {
		List Sezionali = null;
		java.util.ArrayList<Sezionale> listaSezionali = new ArrayList<Sezionale>();
		try {
			if (user == null)
				user = "IIT";
			if (ricerca == null)
				ricerca = "selettiva";
			if (numMax == null)
				numMax = 20;
			if (esercizio == null)
				throw new SOAPFaultException(faultEsercizioNonDefinito());
			UserContext userContext = new WSUserContext(user, null, esercizio,
					null, null, null);
			if (uo == null)
				throw new SOAPFaultException(faultUONonDefinita());
			if (tipo == null)
				throw new SOAPFaultException(faultTipoNonDefinito());
			if (tipo_fattura == null)
				throw new SOAPFaultException(faultTipo_fatturaNonDefinito());
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
					else
						Sezionali = sezionaleComponentSession
								.findListaSezionaleWS(userContext, tipo,
										tipo_fattura, uo, query, dominio,
										ricerca);
				} catch (ComponentException e) {
					throw new SOAPFaultException(faultGenerico());
				} catch (RemoteException e) {
					throw new SOAPFaultException(faultGenerico());
				}
			}
			int num = 0;
			if (Sezionali != null && !Sezionali.isEmpty()) {
				for (Iterator i = Sezionali.iterator(); i.hasNext()
						&& num < new Integer(numMax).intValue();) {
					SezionaleBulk sezionale = (SezionaleBulk) i.next();
					sezionale
							.setTipo_sezionale((Tipo_sezionaleBulk) fatturaAttivaSingolaComponentSession
									.completaOggetto(userContext,
											sezionale.getTipo_sezionale()));
					Sezionale sez = new Sezionale();
					sez.setCodice(sezionale.getTipo_sezionale()
							.getCd_tipo_sezionale());
					sez.setDescrizione(sezionale.getTipo_sezionale()
							.getDs_tipo_sezionale());
					sez.setTipo(sezionale
							.getTipo_sezionale()
							.getTi_istituz_commercKeys()
							.get(sezionale.getTipo_sezionale()
									.getTi_istituz_commerc()).toString());
					listaSezionali.add(sez);
					num++;
				}
			}
			return listaSezionali;
		} catch (NumberFormatException e) {
			throw new SOAPFaultException(faultFormato());
		} catch (SOAPFaultException e) {
			throw e;
		} catch (Exception e) {
			throw new SOAPFaultException(faultGenerico());
		}
	}

	@RolesAllowed({ WSAttributes.WSUSERROLE, WSAttributes.IITROLE })
	public String cercaSezionaleXml(String esercizio, String uo, String tipo,
			String tipo_fattura, String query, String dominio, String numMax,
			String user, String ricerca) throws Exception {
		try {
			List Sezionali = null;
			if (esercizio == null)
				throw new SOAPFaultException(faultEsercizioNonDefinito());
			UserContext userContext = new WSUserContext(user, null,
					new Integer(esercizio), null, null, null);
			if (uo == null)
				throw new SOAPFaultException(faultUONonDefinita());
			if (tipo == null)
				throw new SOAPFaultException(faultTipoNonDefinito());
			if (tipo_fattura == null)
				throw new SOAPFaultException(faultTipo_fatturaNonDefinito());
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
					else
						Sezionali = sezionaleComponentSession
								.findListaSezionaleWS(userContext, tipo,
										tipo_fattura, uo, query, dominio,
										ricerca);
				} catch (ComponentException e) {
					throw new SOAPFaultException(faultGenerico());
				} catch (RemoteException e) {
					throw new SOAPFaultException(faultGenerico());
				}
			}

			return generaXML(numMax, userContext, Sezionali);
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

	private SOAPFault faultEsercizioNonDefinito() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_107.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_107));
	}

	private SOAPFault faultTipoNonDefinito() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_110.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_110));
	}

	private SOAPFault faultTipo_fatturaNonDefinito() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_111.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_111));
	}

	private SOAPFault faultFormato() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_113.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_113));
	}

	public String generaXML(String numMax, UserContext userContext,
			List Sezionali) throws ParserConfigurationException,
			TransformerException, PersistencyException, ComponentException,
			RemoteException, EJBException {
		if (numMax == null)
			numMax = new Integer(20).toString();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation impl = builder.getDOMImplementation();
		Document xmldoc = impl.createDocument(null, "root", null);
		Element root = xmldoc.getDocumentElement();
		root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance",
				"xsi:noNamespaceSchemaLocation",
				"https://contab.cnr.it/SIGLA/schema/cercasezionale.xsd");

		root.appendChild(generaNumeroSezionale(xmldoc, Sezionali));
		int num = 0;
		if (Sezionali != null && !Sezionali.isEmpty()) {
			for (Iterator i = Sezionali.iterator(); i.hasNext()
					&& num < new Integer(numMax).intValue();) {
				SezionaleBulk sezionale = (SezionaleBulk) i.next();
				sezionale
						.setTipo_sezionale((Tipo_sezionaleBulk) fatturaAttivaSingolaComponentSession
								.completaOggetto(userContext,
										sezionale.getTipo_sezionale()));
				root.appendChild(generaDettaglioSezionale(
						xmldoc,
						sezionale.getTipo_sezionale().getCd_tipo_sezionale(),
						sezionale.getTipo_sezionale().getDs_tipo_sezionale(),
						sezionale
								.getTipo_sezionale()
								.getTi_istituz_commercKeys()
								.get(sezionale.getTipo_sezionale()
										.getTi_istituz_commerc()).toString()));
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

	private Element generaNumeroSezionale(Document xmldoc, List Sezionali) {
		Element e = xmldoc.createElement("numris");
		Node n = xmldoc
				.createTextNode(new Integer(Sezionali.size()).toString());
		e.appendChild(n);
		return e;
	}

	private Element generaDettaglioSezionale(Document xmldoc, String codice,
			String descrizione, String tipo) {

		Element element = xmldoc.createElement("sezionale");

		Element elementCodice = xmldoc.createElement("codice");
		Node nodeCodice = xmldoc.createTextNode(codice);
		elementCodice.appendChild(nodeCodice);
		element.appendChild(elementCodice);

		Element elementStato = xmldoc.createElement("descrizione");
		Node nodeStato = xmldoc.createTextNode(descrizione);
		elementStato.appendChild(nodeStato);
		element.appendChild(elementStato);

		Element elementTipo = xmldoc.createElement("IC");
		Node nodetipo = xmldoc.createTextNode(tipo);
		elementTipo.appendChild(nodetipo);
		element.appendChild(elementTipo);
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
