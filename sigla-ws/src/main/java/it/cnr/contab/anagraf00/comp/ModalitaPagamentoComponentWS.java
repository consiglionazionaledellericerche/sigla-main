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

import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.client.docamm.Modalita;
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
@WebService(endpointInterface = "it.cnr.contab.anagraf00.ejb.ModalitaPagamentoComponentSessionWS")
@XmlSeeAlso({java.util.ArrayList.class})


public class ModalitaPagamentoComponentWS {
    @EJB
    FatturaAttivaSingolaComponentSession fatturaAttivaSingolaComponentSession;


    public java.util.ArrayList<Modalita> cercaModalita(Integer terzo,
                                                       String query, String dominio, Integer numMax, String user,
                                                       String ricerca) throws Exception {
        java.util.ArrayList<Modalita> listaModalita = new ArrayList<Modalita>();
        List modalitaDiPagamento = null;
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
            if (terzo == null)
                throw new SOAPFaultException(faultTerzoNonDefinito());
            if (query == null) {
                throw new SOAPFaultException(faultQueryNonDefinita());
            } else if (dominio == null
                    || (!dominio.equalsIgnoreCase("codice") && !dominio
                    .equalsIgnoreCase("descrizione"))) {
                throw new SOAPFaultException(faultDominioNonDefinito());
            } else {
                try {
                    TerzoBulk terzo_db = new TerzoBulk();
                    terzo_db = (((TerzoBulk) fatturaAttivaSingolaComponentSession
                            .completaOggetto(userContext, new TerzoBulk(
                                    new Integer(terzo)))));
                    if (terzo_db == null)
                        throw new SOAPFaultException(faultTerzoNonDefinito());
                    else
                        modalitaDiPagamento = fatturaAttivaSingolaComponentSession
                                .findListaModalitaPagamentoWS(userContext,
                                        terzo.toString(), query, dominio,
                                        ricerca);
                } catch (ComponentException e) {
                    throw new SOAPFaultException(faultGenerico());
                } catch (RemoteException e) {
                    throw new SOAPFaultException(faultGenerico());
                }
            }

            int num = 0;
            if (modalitaDiPagamento != null && !modalitaDiPagamento.isEmpty()) {
                for (Iterator i = modalitaDiPagamento.iterator(); i.hasNext()
                        && num < new Integer(numMax).intValue(); ) {
                    Modalita_pagamentoBulk modalita_pagamento = (Modalita_pagamentoBulk) i
                            .next();
                    modalita_pagamento
                            .setRif_modalita_pagamento((Rif_modalita_pagamentoBulk) fatturaAttivaSingolaComponentSession
                                    .completaOggetto(
                                            userContext,
                                            modalita_pagamento
                                                    .getRif_modalita_pagamento()));
                    Modalita modalita = new Modalita();
                    modalita.setCodice(modalita_pagamento
                            .getRif_modalita_pagamento().getCd_modalita_pag());
                    modalita.setDescrizione(modalita_pagamento
                            .getRif_modalita_pagamento().getDs_modalita_pag());
                    listaModalita.add(modalita);
                    num++;
                }
            }
            return listaModalita;
        } catch (SOAPFaultException e) {
            throw e;
        } catch (Exception e) {
            throw new SOAPFaultException(faultGenerico());
        }
    }


    public String cercaModalitaXml(String terzo, String query, String dominio,
                                   String numMax, String user, String ricerca) throws Exception {
        List Modalita = null;
        UserContext userContext = new WSUserContext(user, null, new Integer(
                java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),
                null, null, null);
        if (terzo == null)
            throw new SOAPFaultException(faultTerzoNonDefinito());
        if (query == null) {
            throw new SOAPFaultException(faultQueryNonDefinita());
        } else if (dominio == null
                || (!dominio.equalsIgnoreCase("codice") && !dominio
                .equalsIgnoreCase("descrizione"))) {
            throw new SOAPFaultException(faultDominioNonDefinito());
        } else {
            try {
                TerzoBulk terzo_db = new TerzoBulk();
                terzo_db = (((TerzoBulk) fatturaAttivaSingolaComponentSession
                        .completaOggetto(userContext, new TerzoBulk(
                                new Integer(terzo)))));
                if (terzo_db == null)
                    throw new SOAPFaultException(faultTerzoNonDefinito());
                else
                    Modalita = fatturaAttivaSingolaComponentSession
                            .findListaModalitaPagamentoWS(userContext, terzo,
                                    query, dominio, ricerca);
            } catch (ComponentException e) {
                throw new SOAPFaultException(faultGenerico());
            } catch (RemoteException e) {
                throw new SOAPFaultException(faultGenerico());
            }
        }
        try {
            return generaXML(numMax, userContext, Modalita);
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

    private SOAPFault faultTerzoNonDefinito() throws SOAPException {
        return generaFault(Costanti.ERRORE_WS_103.toString(),
                Costanti.erroriWS.get(Costanti.ERRORE_WS_103));
    }

    public String generaXML(String numMax, UserContext userContext,
                            List Modalita) throws ParserConfigurationException,
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
                "https://contab.cnr.it/SIGLA/schema/cercamodalita.xsd");
        root.appendChild(generaNumeroModalita(xmldoc, Modalita));
        int num = 0;
        if (Modalita != null && !Modalita.isEmpty()) {
            for (Iterator i = Modalita.iterator(); i.hasNext()
                    && num < new Integer(numMax).intValue(); ) {
                Modalita_pagamentoBulk modalita_pagamento = (Modalita_pagamentoBulk) i
                        .next();
                modalita_pagamento
                        .setRif_modalita_pagamento((Rif_modalita_pagamentoBulk) fatturaAttivaSingolaComponentSession
                                .completaOggetto(userContext,
                                        modalita_pagamento
                                                .getRif_modalita_pagamento()));
                root.appendChild(generaDettaglioModalita(xmldoc,
                        modalita_pagamento.getRif_modalita_pagamento()
                                .getCd_modalita_pag(), modalita_pagamento
                                .getRif_modalita_pagamento()
                                .getDs_modalita_pag()));
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

    private Element generaNumeroModalita(Document xmldoc, List Modalita) {
        Element e = xmldoc.createElement("numris");
        Node n = xmldoc.createTextNode(new Integer(Modalita.size()).toString());
        e.appendChild(n);
        return e;
    }

    private Element generaDettaglioModalita(Document xmldoc, String codice,
                                            String descrizione) {

        Element element = xmldoc.createElement("modalita");

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
