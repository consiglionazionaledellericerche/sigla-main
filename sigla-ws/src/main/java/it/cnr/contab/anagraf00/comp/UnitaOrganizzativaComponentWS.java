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

import it.cnr.contab.client.docamm.Uo;
import it.cnr.contab.config00.ejb.Unita_organizzativaComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
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
@WebService(endpointInterface = "it.cnr.contab.anagraf00.ejb.UnitaOrganizzativaComponentSessionWS")
@XmlSeeAlso({java.util.ArrayList.class})


public class UnitaOrganizzativaComponentWS {
    @EJB
    Unita_organizzativaComponentSession unitaOrganizzativaComponentSession;


    public java.util.ArrayList<Uo> cercaUO(String query, String dominio,
                                           Integer numMax, String user, String ricerca) throws Exception {
        List UO = null;
        java.util.ArrayList<Uo> listaUo = new ArrayList<Uo>();
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
                    || (!dominio.equalsIgnoreCase("codice") && !dominio
                    .equalsIgnoreCase("descrizione"))) {
                throw new SOAPFaultException(faultDominioNonDefinito());
            } else {
                try {
                    UO = unitaOrganizzativaComponentSession
                            .findListaUOWS(userContext, query, dominio, ricerca, null);
                } catch (ComponentException e) {
                    throw new SOAPFaultException(faultGenerico());
                } catch (RemoteException e) {
                    throw new SOAPFaultException(faultGenerico());
                }
            }

            int num = 0;
            if (UO != null && !UO.isEmpty()) {
                for (Iterator i = UO.iterator(); i.hasNext()
                        && num < new Integer(numMax).intValue(); ) {
                    Unita_organizzativaBulk uo = (Unita_organizzativaBulk) i
                            .next();
                    Uo elem = new Uo();
                    elem.setCodice(uo.getCd_unita_organizzativa());
                    elem.setDescrizione(uo.getDs_unita_organizzativa());
                    listaUo.add(elem);
                    num++;
                }
            }
            return listaUo;
        } catch (SOAPFaultException e) {
            throw e;
        } catch (Exception e) {
            throw new SOAPFaultException(faultGenerico());
        }
    }


    public String cercaUOXml(String query, String dominio, String numMax,
                             String user, String ricerca) throws Exception {
        List UO = null;
        UserContext userContext = new WSUserContext(user, null, new Integer(
                java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),
                null, null, null);
        if (query == null) {
            throw new SOAPFaultException(faultQueryNonDefinita());
        } else if (dominio == null
                || (!dominio.equalsIgnoreCase("codice") && !dominio
                .equalsIgnoreCase("descrizione"))) {
            throw new SOAPFaultException(faultDominioNonDefinito());
        } else {
            try {
                UO = unitaOrganizzativaComponentSession
                        .findListaUOWS(userContext, query, dominio, ricerca, null);
            } catch (ComponentException e) {
                throw new SOAPFaultException(faultGenerico());
            } catch (RemoteException e) {
                throw new SOAPFaultException(faultGenerico());
            }
        }
        try {
            return generaXML(numMax, userContext, UO);
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

    public String generaXML(String numMax, UserContext userContext, List UO)
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
                "https://contab.cnr.it/SIGLA/schema/cercaUO.xsd");
        root.appendChild(generaNumeroUO(xmldoc, UO));
        int num = 0;
        if (UO != null && !UO.isEmpty()) {
            for (Iterator i = UO.iterator(); i.hasNext()
                    && num < new Integer(numMax).intValue(); ) {
                Unita_organizzativaBulk uo = (Unita_organizzativaBulk) i.next();
                root.appendChild(generaDettaglioUO(xmldoc,
                        uo.getCd_unita_organizzativa(),
                        uo.getDs_unita_organizzativa()));
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

    private Element generaNumeroUO(Document xmldoc, List UO) {
        Element e = xmldoc.createElement("numris");
        Node n = xmldoc.createTextNode(new Integer(UO.size()).toString());
        e.appendChild(n);
        return e;
    }

    private Element generaDettaglioUO(Document xmldoc, String codice,
                                      String descrizione) {

        Element element = xmldoc.createElement("uo");

        Element elementCodice = xmldoc.createElement("codice");
        Node nodeCodice = xmldoc.createTextNode(codice);
        elementCodice.appendChild(nodeCodice);
        element.appendChild(elementCodice);

        Element elementStato = xmldoc.createElement("descrizione");
        Node nodeStato = xmldoc.createTextNode(descrizione);
        elementStato.appendChild(nodeStato);
        element.appendChild(elementStato);
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
