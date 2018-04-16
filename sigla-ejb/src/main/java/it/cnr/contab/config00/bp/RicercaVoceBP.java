package it.cnr.contab.config00.bp;

import it.cnr.contab.config00.ejb.PDCFinComponentSession;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.V_voce_f_partita_giroBulk;
import it.cnr.contab.config00.util.Constants;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

public class RicercaVoceBP extends BusinessProcess implements ResponseXMLBP {
    private transient static final Logger logger = LoggerFactory.getLogger(RicercaVoceBP.class);

    private String query;
    private String dominio;
    private Integer codiceErrore;
    private Integer numMax;
    private String user;
    private String uo;
    private String tipo;
    private List Voci;
    private String ricerca;
    private String esercizio;
    private String filtro;

    public RicercaVoceBP() {
        super();
    }

    public RicercaVoceBP(String s) {
        super(s);
    }

    private Element generaErrore(Document xmldoc) {
        Element e = xmldoc.createElement("errore");
        e.setAttribute("codice", codiceErrore.toString());
        Node n = xmldoc.createTextNode(Constants.erroriSIP.get(codiceErrore));
        e.appendChild(n);
        return e;
    }

    private Element generaNumeroVoci(Document xmldoc) {
        Element e = xmldoc.createElement("numris");
        Node n = xmldoc.createTextNode(new Integer(getVoci().size()).toString());
        e.appendChild(n);
        return e;
    }

    private Element generaDettaglioVoci(Document xmldoc, String tipo, String voce, String descrizione) {

        Element element = xmldoc.createElement("voce");

        Element elementTipo = xmldoc.createElement("tipo");
        Node nodeTipo = xmldoc.createTextNode(tipo.compareTo(Elemento_voceHome.GESTIONE_ENTRATE) == 0 ? "Entrata" : "Spesa");
        elementTipo.appendChild(nodeTipo);
        element.appendChild(elementTipo);

        Element elementCodice = xmldoc.createElement("codice");
        Node nodeCodice = xmldoc.createTextNode(voce);
        elementCodice.appendChild(nodeCodice);
        element.appendChild(elementCodice);


        Element elementDenominazione = xmldoc.createElement("descrizione");
        Node nodeDenominazione = xmldoc.createTextNode(descrizione == null ? "" : descrizione);
        elementDenominazione.appendChild(nodeDenominazione);
        element.appendChild(elementDenominazione);

        return element;
    }


    public void generaXML(PageContext pagecontext) throws IOException, ServletException {
        try {
            if (getNumMax() == null)
                setNumMax(new Integer(20));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();
            //Document xmldoc = impl.createDocument("https://contab.cnr.it/SIGLA/schema/cercavoci.xsd","root", null);
            //Element root = xmldoc.getDocumentElement();
            Document xmldoc = impl.createDocument(null, "root", null);
            Element root = xmldoc.getDocumentElement();
            root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:noNamespaceSchemaLocation", "https://contab.cnr.it/SIGLA/schema/cercavoci.xsd");

            if (codiceErrore != null) {
                root.appendChild(generaErrore(xmldoc));
            } else {
                root.appendChild(generaNumeroVoci(xmldoc));
                int num = 0;
                if (getVoci() != null && !getVoci().isEmpty()) {
                    for (Iterator i = getVoci().iterator(); i.hasNext() && num < new Integer(numMax).intValue(); ) {
                        if (getTipo().compareTo(Elemento_voceHome.GESTIONE_ENTRATE) == 0) {
                            V_voce_f_partita_giroBulk voce = (V_voce_f_partita_giroBulk) i.next();
                            root.appendChild(generaDettaglioVoci(xmldoc, voce.getTi_gestione(), voce.getCd_voce(), voce.getDs_titolo_capitolo()));
                        } else {
                            Elemento_voceBulk voce = (Elemento_voceBulk) i.next();
                            root.appendChild(generaDettaglioVoci(xmldoc, voce.getTi_gestione(), voce.getCd_elemento_voce(), voce.getDs_elemento_voce()));
                        }
                        num++;
                    }
                }
            }
            DOMSource domSource = new DOMSource(xmldoc);
            StreamResult streamResult = new StreamResult(pagecontext.getOut());
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.STANDALONE, "no");
            serializer.transform(domSource, streamResult);
            closed();
        } catch (ParserConfigurationException | TransformerException | BusinessProcessException e) {
            logger.error("GeneraXML error -> ", e);
        }
    }

    public void eseguiRicerca(it.cnr.jada.action.ActionContext context) throws BusinessProcessException {
        try {

            if (esercizio == null) {
                codiceErrore = Constants.ERRORE_SIP_114;
                return;
            } else if (uo == null) {
                codiceErrore = Constants.ERRORE_SIP_113;
                return;
            } else if (tipo == null) {
                codiceErrore = Constants.ERRORE_SIP_118;
                return;
            } else if (getQuery() == null) {
                codiceErrore = Constants.ERRORE_SIP_101;
                return;
            } else if (getDominio() == null || (!getDominio().equalsIgnoreCase("codice") && !getDominio().equalsIgnoreCase("descrizione"))) {
                codiceErrore = Constants.ERRORE_SIP_102;
                return;
            } else {
                try {
                    setVoci(((PDCFinComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_PDCFinComponentSession", PDCFinComponentSession.class))
                            .findListaVociWS(context.getUserContext(false), getUo(), getTipo(), getQuery(), getDominio(), getRicerca(), getFiltro()));
                } catch (ComponentException e) {
                    codiceErrore = Constants.ERRORE_SIP_100;
                } catch (RemoteException e) {
                    codiceErrore = Constants.ERRORE_SIP_100;
                }
            }
        } catch (NumberFormatException e) {
            codiceErrore = Constants.ERRORE_SIP_100;
        } catch (Exception e) {
            codiceErrore = Constants.ERRORE_SIP_100;
        }
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getNumMax() {
        return numMax;
    }

    public void setNumMax(Integer numMax) {
        this.numMax = numMax;
    }

    public Integer getCodiceErrore() {
        return codiceErrore;
    }

    public void setCodiceErrore(Integer codiceErrore) {
        this.codiceErrore = codiceErrore;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRicerca() {
        return ricerca;
    }

    public void setRicerca(String ricerca) {
        this.ricerca = ricerca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List getVoci() {
        return Voci;
    }

    public void setVoci(List voci) {
        Voci = voci;
    }

    public String getUo() {
        return uo;
    }

    public void setUo(String uo) {
        this.uo = uo;
    }

    public String getEsercizio() {
        return esercizio;
    }

    public void setEsercizio(String esercizio) {
        this.esercizio = esercizio;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }
}