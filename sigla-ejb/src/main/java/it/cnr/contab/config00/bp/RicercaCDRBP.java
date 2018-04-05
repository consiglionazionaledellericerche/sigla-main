package it.cnr.contab.config00.bp;

import it.cnr.contab.config00.ejb.CDRComponentSession;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
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

public class RicercaCDRBP extends BusinessProcess implements ResponseXMLBP {
    private transient static final Logger logger = LoggerFactory.getLogger(RicercaCDRBP.class);

    private String query;
    private String dominio;
    private Integer codiceErrore;
    private Integer numMax;
    private String user;
    private List CDR;
    private String uo;
    private String ricerca;

    public RicercaCDRBP() {
        super();
    }

    public RicercaCDRBP(String s) {
        super(s);
    }

    private Element generaErrore(Document xmldoc) {
        Element e = xmldoc.createElement("errore");
        e.setAttribute("codice", codiceErrore.toString());
        Node n = xmldoc.createTextNode(Constants.erroriSIP.get(codiceErrore));
        e.appendChild(n);
        return e;
    }

    private Element generaNumeroCDR(Document xmldoc) {
        Element e = xmldoc.createElement("numris");
        Node n = xmldoc.createTextNode(new Integer(getCDR().size()).toString());
        e.appendChild(n);
        return e;
    }

    private Element generaDettaglioCDR(Document xmldoc, String codice, String descrizione) {

        Element element = xmldoc.createElement("cdr");

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


    public List getCDR() {
        return CDR;
    }

    public void setCDR(List cdr) {
        CDR = cdr;
    }

    public void generaXML(PageContext pagecontext) throws IOException, ServletException {
        try {
            if (getNumMax() == null)
                setNumMax(new Integer(20));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();
            //Document xmldoc = impl.createDocument("https://contab.cnr.it/SIGLA/schema/cercacdr.xsd","root", null);
            //Element root = xmldoc.getDocumentElement();
            Document xmldoc = impl.createDocument(null, "root", null);
            Element root = xmldoc.getDocumentElement();
            root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:noNamespaceSchemaLocation", "https://contab.cnr.it/SIGLA/schema/cercacdr.xsd");

            if (codiceErrore != null) {
                root.appendChild(generaErrore(xmldoc));
            } else {
                root.appendChild(generaNumeroCDR(xmldoc));
                int num = 0;
                if (getCDR() != null && !getCDR().isEmpty()) {
                    for (Iterator a = getCDR().iterator(); a.hasNext() && num < getNumMax().intValue(); ) {
                        CdrBulk cdr = (CdrBulk) a.next();
                        root.appendChild(generaDettaglioCDR(xmldoc, cdr.getCd_centro_responsabilita(), cdr.getDs_cdr()));
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
        } catch (ParserConfigurationException|TransformerException|BusinessProcessException e) {
            logger.error("GeneraXML error -> ", e);
        }
    }

    public void eseguiRicerca(it.cnr.jada.action.ActionContext context) throws BusinessProcessException {
        if (uo == null) {
            codiceErrore = Constants.ERRORE_SIP_113;
            return;
        } else if ((getQuery() == null) && (getDominio() != null)) {
            codiceErrore = Constants.ERRORE_SIP_101;
            return;
        } else if (getQuery() != null && (getDominio() == null || (!getDominio().equalsIgnoreCase("codice") && !getDominio().equalsIgnoreCase("descrizione")))) {
            codiceErrore = Constants.ERRORE_SIP_102;
            return;
        } else {
            try {
                setCDR(((CDRComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_CDRComponentSession", CDRComponentSession.class))
                        .findListaCDRWS(context.getUserContext(false), uo, query, dominio, ricerca));
            } catch (ComponentException e) {
                codiceErrore = Constants.ERRORE_SIP_100;
            } catch (RemoteException e) {
                codiceErrore = Constants.ERRORE_SIP_100;
            }
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

    public String getUo() {
        return uo;
    }

    public void setUo(String uo) {
        this.uo = uo;
    }
}