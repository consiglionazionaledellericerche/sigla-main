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

import it.cnr.contab.client.docamm.Gae;
import it.cnr.contab.config00.ejb.Linea_attivitaComponentSession;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.utenze00.bp.Costanti;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Questa classe svolge le operazioni fondamentali di lettura, scrittura e
 * filtro dei dati immessi o richiesti dall'utente. In oltre sovrintende alla
 * gestione e creazione dati a cui l'utente stesso non ha libero accesso e/o non
 * gli sono trasparenti.
 */
@Stateless
@WebService(endpointInterface = "it.cnr.contab.anagraf00.ejb.GAEComponentSessionWS")
@XmlSeeAlso({java.util.ArrayList.class})


public class GAEComponentWS {
    @EJB
    FatturaAttivaSingolaComponentSession fatturaAttivaSingolaComponentSession;
    @EJB
    Linea_attivitaComponentSession lineaAttivitaComponentSession;


    public String cercaGAEXml(String cdr, String tipo, String query,
                              String dominio, String numMax, String user, String ricerca)
            throws Exception {
        List GAE = null;
        UserContext userContext = new WSUserContext(user, null, new Integer(
                java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),
                null, null, cdr);
        if (cdr == null)
            throw new SOAPFaultException(faultCDRNonDefinito());
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
                CdrBulk cdr_db = new CdrBulk();
                cdr_db = (((CdrBulk) fatturaAttivaSingolaComponentSession
                        .completaOggetto(userContext, new CdrBulk(cdr))));
                if (cdr_db == null)
                    throw new SOAPFaultException(faultCDRNonDefinito());
                else
                    GAE = lineaAttivitaComponentSession
                            .findListaGAEWS(userContext, cdr, tipo, query,
                                    dominio, ricerca);
            } catch (ComponentException e) {
                throw new SOAPFaultException(faultGenerico());
            } catch (RemoteException e) {
                throw new SOAPFaultException(faultGenerico());
            }
        }
        try {
            return generaXML(numMax, userContext, GAE);
        } catch (Exception e) {
            throw new SOAPFaultException(faultGenerico());
        }
    }


    public List cercaGAE(String cdr, String tipo, String query, String dominio,
                         Integer numMax, String user, String ricerca) throws Exception {
        java.util.ArrayList<Gae> listaGae = new ArrayList<Gae>();
        List GAE = null;
        try {
            if (user == null)
                user = "IIT";
            if (ricerca == null)
                ricerca = "selettiva";
            if (numMax == null)
                numMax = 20;
            UserContext userContext = new WSUserContext(user, null,
                    new Integer(java.util.Calendar.getInstance().get(
                            java.util.Calendar.YEAR)), null, null, cdr);
            if (cdr == null)
                throw new SOAPFaultException(faultCDRNonDefinito());
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
                    CdrBulk cdr_db = new CdrBulk();
                    cdr_db = (((CdrBulk) fatturaAttivaSingolaComponentSession
                            .completaOggetto(userContext, new CdrBulk(cdr))));
                    if (cdr_db == null)
                        throw new SOAPFaultException(faultCDRNonDefinito());
                    else
                        GAE = lineaAttivitaComponentSession
                                .findListaGAEWS(userContext, cdr, tipo, query,
                                        dominio, ricerca);
                } catch (ComponentException e) {
                    throw new SOAPFaultException(faultGenerico());
                } catch (RemoteException e) {
                    throw new SOAPFaultException(faultGenerico());
                }
            }

            int num = 0;
            if (GAE != null && !GAE.isEmpty()) {
                for (Iterator i = GAE.iterator(); i.hasNext()
                        && num < new Integer(numMax).intValue(); ) {
                    WorkpackageBulk linea = (WorkpackageBulk) i.next();
                    linea = lineaAttivitaComponentSession
                            .completaOggetto(userContext, linea);
                    Gae gae = new Gae();
                    gae.setCdr(linea.getCd_centro_responsabilita());
                    gae.setCodice(linea.getCd_linea_attivita());
                    gae.setDescrizione(linea.getDs_linea_attivita());
                    gae.setCodicemodulo(linea.getProgetto().getCd_progetto());
                    gae.setDescrizionemodulo(linea.getProgetto()
                            .getDs_progetto());
                    gae.setCodicecommessa(linea.getProgettopadre()
                            .getCd_progetto());
                    gae.setDescrizionecommessa(linea.getProgettopadre()
                            .getDs_progetto());
                    listaGae.add(gae);
                    num++;
                }
            }
            return listaGae;
        } catch (SOAPFaultException e) {
            throw e;
        } catch (Exception e) {
            throw new SOAPFaultException(faultGenerico());
        }
    }

    private SOAPFault faultGenerico() throws SOAPException {
        return generaFault(Costanti.ERRORE_WS_100.toString(),
                Costanti.erroriWS.get(Costanti.ERRORE_WS_100));
    }

    private SOAPFault faultQueryNonDefinita() throws SOAPException {
        return generaFault(Costanti.ERRORE_WS_101.toString(),
                Costanti.erroriWS.get(Costanti.ERRORE_WS_101));
    }

    private SOAPFault faultDominioNonDefinito() throws SOAPException {
        return generaFault(Costanti.ERRORE_WS_102.toString(),
                Costanti.erroriWS.get(Costanti.ERRORE_WS_102));
    }

    private SOAPFault faultCDRNonDefinito() throws SOAPException {
        return generaFault(Costanti.ERRORE_WS_108.toString(),
                Costanti.erroriWS.get(Costanti.ERRORE_WS_108));
    }

    private SOAPFault faultTipoNonDefinito() throws SOAPException {
        return generaFault(Costanti.ERRORE_WS_109.toString(),
                Costanti.erroriWS.get(Costanti.ERRORE_WS_109));
    }

    public String generaXML(String numMax, UserContext userContext, List GAE)
            throws ParserConfigurationException, TransformerException,
            ComponentException, PersistencyException, RemoteException,
            EJBException {
        if (numMax == null)
            numMax = new Integer(20).toString();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation impl = builder.getDOMImplementation();
        Document xmldoc = impl.createDocument(null, "root", null);
        Element root = xmldoc.getDocumentElement();
        root.appendChild(generaNumeroGAE(xmldoc, GAE));
        root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance",
                "xsi:noNamespaceSchemaLocation",
                "https://contab.cnr.it/SIGLA/schema/cercagae.xsd");

        int num = 0;
        if (GAE != null && !GAE.isEmpty()) {
            for (Iterator i = GAE.iterator(); i.hasNext()
                    && num < new Integer(numMax).intValue(); ) {
                WorkpackageBulk linea = (WorkpackageBulk) i.next();
                linea = lineaAttivitaComponentSession
                        .completaOggetto(userContext, linea);
                root.appendChild(generaDettaglioGAE(xmldoc, linea
                                .getCd_centro_responsabilita(), linea
                                .getCd_linea_attivita(), linea.getDs_linea_attivita(),
                        linea.getProgetto().getCd_progetto(), linea
                                .getProgetto().getDs_progetto(), linea
                                .getProgettopadre().getCd_progetto(), linea
                                .getProgettopadre().getDs_progetto()));
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

    private Element generaNumeroGAE(Document xmldoc, List GAE) {
        Element e = xmldoc.createElement("numris");
        Node n = xmldoc.createTextNode(new Integer(GAE.size()).toString());
        e.appendChild(n);
        return e;
    }

    private Element generaDettaglioGAE(Document xmldoc, String cdr,
                                       String codice, String descrizione, String cod_mod, String desc_mod,
                                       String cod_com, String desc_com) {

        Element element = xmldoc.createElement("gae");

        Element elementCdr = xmldoc.createElement("cdr");
        Node nodeCdr = xmldoc.createTextNode(cdr);
        elementCdr.appendChild(nodeCdr);
        element.appendChild(elementCdr);

        Element elementCodice = xmldoc.createElement("codice");
        Node nodeCodice = xmldoc.createTextNode(codice);
        elementCodice.appendChild(nodeCodice);
        element.appendChild(elementCodice);

        Element elementDenominazione = xmldoc.createElement("descrizione");
        Node nodeDenominazione = xmldoc.createTextNode(descrizione == null ? ""
                : descrizione);
        elementDenominazione.appendChild(nodeDenominazione);
        element.appendChild(elementDenominazione);

        Element elementcod_mod = xmldoc.createElement("codicemodulo");
        Node nodecod_mod = xmldoc.createTextNode(cod_mod);
        elementcod_mod.appendChild(nodecod_mod);
        element.appendChild(elementcod_mod);

        Element elementdesc_mod = xmldoc.createElement("descrizionemodulo");
        Node nodedesc_mod = xmldoc.createTextNode(desc_mod == null ? ""
                : desc_mod);
        elementdesc_mod.appendChild(nodedesc_mod);
        element.appendChild(elementdesc_mod);

        Element elementcod_com = xmldoc.createElement("codicecommessa");
        Node nodecod_com = xmldoc.createTextNode(cod_com);
        elementcod_com.appendChild(nodecod_com);
        element.appendChild(elementcod_com);

        Element elementdesc_com = xmldoc.createElement("descrizionecommessa");
        Node nodedesc_com = xmldoc.createTextNode(desc_com == null ? ""
                : desc_com);
        elementdesc_com.appendChild(nodedesc_com);
        element.appendChild(elementdesc_com);

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
