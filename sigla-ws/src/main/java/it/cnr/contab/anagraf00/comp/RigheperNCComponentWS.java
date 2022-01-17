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
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.client.docamm.RigaperNC;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
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
@WebService(endpointInterface = "it.cnr.contab.anagraf00.ejb.RigheperNCComponentSessionWS")
@XmlSeeAlso({ java.util.ArrayList.class })


public class RigheperNCComponentWS {
	@EJB FatturaAttivaSingolaComponentSession fatturaAttivaSingolaComponentSession;

	@RolesAllowed({ WSAttributes.WSUSERROLE, WSAttributes.IITROLE })
	public java.util.ArrayList<RigaperNC> cercaRighe(String uo,
			Integer terzo,
			String ti_causale,// T(Tariffario) o C(Contratto) o L(Libera)
			Integer esercizio, String query, String dominio, Integer numMax,
			String user, String ricerca) throws Exception {
		java.util.ArrayList<RigaperNC> listarighe = new ArrayList<RigaperNC>();
		List righe = null;
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
					new Integer(new Integer(java.util.Calendar.getInstance()
							.get(java.util.Calendar.YEAR))), null, null, null);
			if (uo == null)
				throw new SOAPFaultException(faultUONonDefinita());
			else if (terzo == null)
				throw new SOAPFaultException(faultTerzoNonDefinito());
			else if (query == null)
				throw new SOAPFaultException(faultQueryNonDefinita());
			else if (dominio == null
					|| (!dominio.equalsIgnoreCase("codice") && !dominio
							.equalsIgnoreCase("descrizione")))
				throw new SOAPFaultException(faultDominioNonDefinito());
			else {
				try {
					Unita_organizzativaBulk uo_db = new Unita_organizzativaBulk();
					uo_db = (((Unita_organizzativaBulk) fatturaAttivaSingolaComponentSession
							.completaOggetto(userContext,
									new Unita_organizzativaBulk(uo))));
					if (uo_db == null)
						throw new SOAPFaultException(faultUONonDefinita());
					else {
						TerzoBulk terzo_db = new TerzoBulk();
						terzo_db = (((TerzoBulk) fatturaAttivaSingolaComponentSession
								.completaOggetto(userContext, new TerzoBulk(
										new Integer(terzo)))));
						if (terzo_db == null)
							throw new SOAPFaultException(
									faultTerzoNonDefinito());
						else
							righe = fatturaAttivaSingolaComponentSession
									.findListaRigheperNCWS(userContext, uo,
											terzo.toString(), ti_causale,
											esercizio.toString(), query,
											dominio, ricerca);
					}
				} catch (ComponentException e) {
					throw new SOAPFaultException(faultGenerico());
				} catch (RemoteException e) {
					throw new SOAPFaultException(faultGenerico());
				}
			}

			int num = 0;

			if (righe != null && !righe.isEmpty()) {
				for (Iterator i = righe.iterator(); i.hasNext()
						&& num < new Integer(numMax).intValue();) {
					Fattura_attiva_rigaIBulk riga = (Fattura_attiva_rigaIBulk) i
							.next();
					riga.setFattura_attivaI((Fattura_attiva_IBulk) fatturaAttivaSingolaComponentSession
							.completaOggetto(userContext,
									riga.getFattura_attivaI()));
					riga.getFattura_attivaI()
							.setCliente(
									(TerzoBulk) fatturaAttivaSingolaComponentSession
											.completaOggetto(userContext, riga
													.getFattura_attivaI()
													.getCliente()));
					RigaperNC riga_nc = new RigaperNC();
					riga_nc.setEsercizio(riga.getEsercizio());
					riga_nc.setCds(riga.getCd_cds());
					riga_nc.setUo(riga.getCd_unita_organizzativa());
					riga_nc.setPg_fattura(riga.getPg_fattura_attiva());
					riga_nc.setProgressivoriga(riga.getProgressivo_riga());
					riga_nc.setDescrizione(riga.getFattura_attivaI()
							.getDs_fattura_attiva());
					riga_nc.setDataregistrazione(riga.getFattura_attivaI()
							.getDt_registrazione());
					riga_nc.setCognome(riga.getFattura_attivaI().getCliente()
							.getAnagrafico().getCognome());
					riga_nc.setNome(riga.getFattura_attivaI().getCliente()
							.getAnagrafico().getNome());
					riga_nc.setRagionesociale(riga.getFattura_attivaI()
							.getCliente().getAnagrafico().getRagione_sociale());
					riga_nc.setImp_disponibile_nc(riga.getIm_diponibile_nc());
					riga_nc.setVoce_iva(riga.getCd_voce_iva());
					riga_nc.setCausale(riga
							.getFattura_attivaI()
							.getTi_causale_emissioneKeys()
							.get(riga.getFattura_attivaI()
									.getTi_causale_emissione()).toString());
					riga_nc.setTariffario(riga.getCd_tariffario());
					riga_nc.setTiposezionale(riga.getFattura_attivaI()
							.getTipo_sezionale().getCd_tipo_sezionale());
					riga_nc.setFl_extra(riga.getFattura_attivaI()
							.getFl_extra_ue());
					riga_nc.setFl_intra(riga.getFattura_attivaI()
							.getFl_intra_ue());
					riga_nc.setFl_san_marino(riga.getFattura_attivaI()
							.getFl_san_marino());
					riga_nc.setFl_liquidazione_differita(riga
							.getFattura_attivaI()
							.getFl_liquidazione_differita());
					listarighe.add(riga_nc);
					num++;
				}
			}
			return listarighe;
		} catch (NumberFormatException e) {
			throw new SOAPFaultException(faultFormato());
		} catch (SOAPFaultException e) {
			throw e;
		} catch (Exception e) {
			throw new SOAPFaultException(faultGenerico());
		}
	}

	@RolesAllowed({ WSAttributes.WSUSERROLE, WSAttributes.IITROLE })
	public String cercaRigheXml(String uo, String terzo,
			String ti_causale,// T(Tariffario) o C(Contratto) o L(Libera)
			String esercizio, String query, String dominio, String numMax,
			String user, String ricerca) throws Exception {
		try {
			List righe = null;
			if (esercizio == null)
				throw new SOAPFaultException(faultEsercizioNonDefinito());
			UserContext userContext = new WSUserContext(user, null,
					new Integer(new Integer(java.util.Calendar.getInstance()
							.get(java.util.Calendar.YEAR))), null, null, null);
			if (uo == null)
				throw new SOAPFaultException(faultUONonDefinita());
			else if (terzo == null)
				throw new SOAPFaultException(faultTerzoNonDefinito());
			else if (query == null)
				throw new SOAPFaultException(faultQueryNonDefinita());
			else if (dominio == null
					|| (!dominio.equalsIgnoreCase("codice") && !dominio
							.equalsIgnoreCase("descrizione")))
				throw new SOAPFaultException(faultDominioNonDefinito());
			else {
				try {
					Unita_organizzativaBulk uo_db = new Unita_organizzativaBulk();
					uo_db = (((Unita_organizzativaBulk) fatturaAttivaSingolaComponentSession
							.completaOggetto(userContext,
									new Unita_organizzativaBulk(uo))));
					if (uo_db == null)
						throw new SOAPFaultException(faultUONonDefinita());
					else {
						TerzoBulk terzo_db = new TerzoBulk();
						terzo_db = (((TerzoBulk) fatturaAttivaSingolaComponentSession
								.completaOggetto(userContext, new TerzoBulk(
										new Integer(terzo)))));
						if (terzo_db == null)
							throw new SOAPFaultException(
									faultTerzoNonDefinito());
						else
							righe = fatturaAttivaSingolaComponentSession
									.findListaRigheperNCWS(userContext, uo,
											terzo, ti_causale, esercizio,
											query, dominio, ricerca);
					}
				} catch (ComponentException e) {
					throw new SOAPFaultException(faultGenerico());
				} catch (RemoteException e) {
					throw new SOAPFaultException(faultGenerico());
				}
			}

			return generaXML(userContext, numMax, righe);
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

	private SOAPFault faultTerzoNonDefinito() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_103.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_103));
	}

	private SOAPFault faultEsercizioNonDefinito() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_107.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_107));
	}

	private SOAPFault faultFormato() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_113.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_113));
	}

	public String generaXML(UserContext userContext, String numMax, List righe)
			throws ParserConfigurationException, TransformerException,
			PersistencyException, ComponentException, RemoteException,
			EJBException {
		if (numMax == null)
			numMax = new Integer(20).toString();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation impl = builder.getDOMImplementation();

		Document xmldoc = impl.createDocument(null, "root", null);
		Element root = xmldoc.getDocumentElement();
		root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance",
				"xsi:noNamespaceSchemaLocation",
				"https://contab.cnr.it/SIGLA/schema/cercarigheperNC.xsd");
		root.appendChild(generaNumeroRighe(xmldoc, righe));
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"dd/MM/yyyy");
		int num = 0;

		if (righe != null && !righe.isEmpty()) {
			for (Iterator i = righe.iterator(); i.hasNext()
					&& num < new Integer(numMax).intValue();) {
				Fattura_attiva_rigaIBulk riga = (Fattura_attiva_rigaIBulk) i
						.next();
				riga.setFattura_attivaI((Fattura_attiva_IBulk) fatturaAttivaSingolaComponentSession
						.completaOggetto(userContext, riga.getFattura_attivaI()));
				riga.getFattura_attivaI()
						.setCliente(
								(TerzoBulk) fatturaAttivaSingolaComponentSession
										.completaOggetto(userContext, riga
												.getFattura_attivaI()
												.getCliente()));
				root.appendChild(generaDettaglioRighe(
						xmldoc,
						riga.getEsercizio().toString(),
						riga.getCd_cds(),
						riga.getCd_unita_organizzativa(),
						riga.getPg_fattura_attiva().toString(),
						riga.getProgressivo_riga().toString(),
						riga.getFattura_attivaI().getDs_fattura_attiva(),
						sdf.format(riga.getFattura_attivaI()
								.getDt_registrazione()),
						riga.getFattura_attivaI().getCliente().getAnagrafico()
								.getCognome(),
						riga.getFattura_attivaI().getCliente().getAnagrafico()
								.getNome(),
						riga.getFattura_attivaI().getCliente().getAnagrafico()
								.getRagione_sociale(),
						riga.getIm_diponibile_nc().toString(),
						riga.getCd_voce_iva(),
						riga.getFattura_attivaI()
								.getTi_causale_emissioneKeys()
								.get(riga.getFattura_attivaI()
										.getTi_causale_emissione()).toString(),
						riga.getCd_tariffario(),
						riga.getFattura_attivaI().getTipo_sezionale()
								.getCd_tipo_sezionale(),
						(riga.getFattura_attivaI().getFl_intra_ue()
								.compareTo(Boolean.FALSE) == 0 ? "N" : "Y"),
						(riga.getFattura_attivaI().getFl_extra_ue()
								.compareTo(Boolean.FALSE) == 0 ? "N" : "Y"),
						(riga.getFattura_attivaI().getFl_san_marino()
								.compareTo(Boolean.FALSE) == 0 ? "N" : "Y"),
						(riga.getFattura_attivaI()
								.getFl_liquidazione_differita()
								.compareTo(Boolean.FALSE) == 0 ? "N" : "Y")));
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
		// serializer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,"cercaterzi");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.STANDALONE, "no");
		serializer.transform(domSource, streamResult);
		return domWriter.toString();

	}

	private Element generaDettaglioRighe(Document xmldoc, String esercizio,
			String cd_cds, String cd_unita_organizzativa, String pg_fattura,
			String riga, String ds_fattura_attiva, String data, String cognome,
			String nome, String ragione_sociale, String imp_disponibile_nc,
			String voce_iva, String causale, String tariffario,
			String sezionale, String fl_intra, String fl_extra,
			String fl_san_marino, String fl_liquidazione_differita) {

		Element element = xmldoc.createElement("righe");

		Element elementCodice = xmldoc.createElement("esercizio");
		Node nodeCodice = xmldoc.createTextNode(esercizio);
		elementCodice.appendChild(nodeCodice);
		element.appendChild(elementCodice);

		elementCodice = xmldoc.createElement("cds");
		nodeCodice = xmldoc.createTextNode(cd_cds);
		elementCodice.appendChild(nodeCodice);
		element.appendChild(elementCodice);

		elementCodice = xmldoc.createElement("uo");
		nodeCodice = xmldoc.createTextNode(cd_unita_organizzativa);
		elementCodice.appendChild(nodeCodice);
		element.appendChild(elementCodice);

		elementCodice = xmldoc.createElement("pg_fattuta");
		nodeCodice = xmldoc.createTextNode(pg_fattura);
		elementCodice.appendChild(nodeCodice);
		element.appendChild(elementCodice);

		elementCodice = xmldoc.createElement("progressivoriga");
		nodeCodice = xmldoc.createTextNode(riga);
		elementCodice.appendChild(nodeCodice);
		element.appendChild(elementCodice);

		Element elementDesc = xmldoc.createElement("descrizione");
		Node nodeDesc = xmldoc.createTextNode(ds_fattura_attiva == null ? ""
				: ds_fattura_attiva);
		elementDesc.appendChild(nodeDesc);
		element.appendChild(elementDesc);

		elementDesc = xmldoc.createElement("dataregistrazione");
		nodeDesc = xmldoc.createTextNode(data);
		elementDesc.appendChild(nodeDesc);
		element.appendChild(elementDesc);

		if (cognome != null) {
			elementDesc = xmldoc.createElement("cognome");
			nodeDesc = xmldoc.createTextNode(cognome);
			elementDesc.appendChild(nodeDesc);
			element.appendChild(elementDesc);

			elementDesc = xmldoc.createElement("nome");
			nodeDesc = xmldoc.createTextNode(nome);
			elementDesc.appendChild(nodeDesc);
			element.appendChild(elementDesc);
		} else {
			elementDesc = xmldoc.createElement("ragionesociale");
			nodeDesc = xmldoc.createTextNode(ragione_sociale);
			elementDesc.appendChild(nodeDesc);
			element.appendChild(elementDesc);
		}
		elementDesc = xmldoc.createElement("imp_disponibile_nc");
		nodeDesc = xmldoc.createTextNode(imp_disponibile_nc);
		elementDesc.appendChild(nodeDesc);
		element.appendChild(elementDesc);

		elementDesc = xmldoc.createElement("voce_iva");
		nodeDesc = xmldoc.createTextNode(voce_iva);
		elementDesc.appendChild(nodeDesc);
		element.appendChild(elementDesc);

		elementDesc = xmldoc.createElement("causale");
		nodeDesc = xmldoc.createTextNode(causale);
		elementDesc.appendChild(nodeDesc);
		element.appendChild(elementDesc);

		elementDesc = xmldoc.createElement("tariffario");
		nodeDesc = xmldoc.createTextNode(tariffario == null ? "" : tariffario);
		elementDesc.appendChild(nodeDesc);
		element.appendChild(elementDesc);

		elementDesc = xmldoc.createElement("tiposezionale");
		nodeDesc = xmldoc.createTextNode(sezionale);
		elementDesc.appendChild(nodeDesc);
		element.appendChild(elementDesc);

		elementDesc = xmldoc.createElement("fl_intra");
		nodeDesc = xmldoc.createTextNode(fl_intra);
		elementDesc.appendChild(nodeDesc);
		element.appendChild(elementDesc);

		elementDesc = xmldoc.createElement("fl_extra");
		nodeDesc = xmldoc.createTextNode(fl_extra);
		elementDesc.appendChild(nodeDesc);
		element.appendChild(elementDesc);

		elementDesc = xmldoc.createElement("fl_san_marino");
		nodeDesc = xmldoc.createTextNode(fl_san_marino);
		elementDesc.appendChild(nodeDesc);
		element.appendChild(elementDesc);

		elementDesc = xmldoc.createElement("fl_liquidazione_differita");
		nodeDesc = xmldoc.createTextNode(fl_liquidazione_differita);
		elementDesc.appendChild(nodeDesc);
		element.appendChild(elementDesc);

		return element;
	}

	private Element generaNumeroRighe(Document xmldoc, List righe) {
		Element e = xmldoc.createElement("numris");
		Node n = xmldoc.createTextNode(new Integer(righe.size()).toString());
		e.appendChild(n);
		return e;
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
