package it.cnr.contab.config00.bp;

import it.cnr.contab.config00.ejb.Linea_attivitaComponentSession;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.util.Constants;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.ejb.EJBException;
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

public class RicercaGAEBP extends BusinessProcess implements ResponseXMLBP {
    private transient static final Logger logger = LoggerFactory.getLogger(RicercaGAEBP.class);

    private String query;
    private String dominio;
    private Integer codiceErrore;
    private Integer numMax;
    private String user;
    private String cdr;
    private String tipo;
    private List GAE;
    private List GAE_old;
    private String ricerca;
    private String esercizio;
    private String filtro;

    public RicercaGAEBP() {
        super();
    }

    public RicercaGAEBP(String s) {
        super(s);
    }

    private Element generaErrore(Document xmldoc) {
        Element e = xmldoc.createElement("errore");
        e.setAttribute("codice", codiceErrore.toString());
        Node n = xmldoc.createTextNode(Constants.erroriSIP.get(codiceErrore));
        e.appendChild(n);
        return e;
    }

    private Element generaNumeroGAE(Document xmldoc) {
        Element e = xmldoc.createElement("numris");
        Node n = xmldoc.createTextNode(new Integer(getGAE().size()).toString());
        e.appendChild(n);
        return e;
    }

    private Element generaDettaglioGAE(Document xmldoc, String cdr, String codice, String descrizione,
                                       String cod_mod, String desc_mod, String cod_com, String desc_com) {

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
        Node nodeDenominazione = xmldoc.createTextNode(descrizione == null ? "" : descrizione);
        elementDenominazione.appendChild(nodeDenominazione);
        element.appendChild(elementDenominazione);

        Element elementcod_mod = xmldoc.createElement("codicemodulo");
        Node nodecod_mod = xmldoc.createTextNode(cod_mod == null ? "" : cod_mod);
        elementcod_mod.appendChild(nodecod_mod);
        element.appendChild(elementcod_mod);

        Element elementdesc_mod = xmldoc.createElement("descrizionemodulo");
        Node nodedesc_mod = xmldoc.createTextNode(desc_mod == null ? "" : desc_mod);
        elementdesc_mod.appendChild(nodedesc_mod);
        element.appendChild(elementdesc_mod);

        Element elementcod_com = xmldoc.createElement("codicecommessa");
        Node nodecod_com = xmldoc.createTextNode(cod_com == null ? "" : cod_com);
        elementcod_com.appendChild(nodecod_com);
        element.appendChild(elementcod_com);

        Element elementdesc_com = xmldoc.createElement("descrizionecommessa");
        Node nodedesc_com = xmldoc.createTextNode(desc_com == null ? "" : desc_com);
        elementdesc_com.appendChild(nodedesc_com);
        element.appendChild(elementdesc_com);

        return element;
    }


    public void generaXML(PageContext pagecontext) throws IOException, ServletException {
        try {
            if (getNumMax() == null)
                setNumMax(new Integer(20));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();
            //Document xmldoc = impl.createDocument("https://contab.cnr.it/SIGLA/schema/cercagae.xsd","root", null);
            //Element root = xmldoc.getDocumentElement();
            Document xmldoc = impl.createDocument(null, "root", null);
            Element root = xmldoc.getDocumentElement();
            root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:noNamespaceSchemaLocation", "https://contab.cnr.it/SIGLA/schema/cercagae.xsd");

            if (codiceErrore != null) {
                root.appendChild(generaErrore(xmldoc));
            } else {
                root.appendChild(generaNumeroGAE(xmldoc));
                int num = 0;
                if (getGAE() != null && !getGAE().isEmpty()) {
                    for (Iterator i = getGAE().iterator(); i.hasNext() && num < new Integer(numMax).intValue(); ) {
                        WorkpackageBulk linea = (WorkpackageBulk) i.next();
                        root.appendChild(generaDettaglioGAE(xmldoc, linea.getCd_centro_responsabilita(), linea.getCd_linea_attivita(), linea.getDs_linea_attivita(),
                                linea.getProgetto().getCd_progetto(), linea.getProgetto().getDs_progetto(), linea.getProgettopadre().getCd_progetto(),
                                linea.getProgettopadre().getDs_progetto()));
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

        if (cdr == null) {
            codiceErrore = Constants.ERRORE_SIP_117;
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
                GAE_old = (((Linea_attivitaComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Linea_attivitaComponentSession", Linea_attivitaComponentSession.class))
                        .findListaGAEWS(context.getUserContext(false), getCdr(), getTipo(), getQuery(), getDominio(), getRicerca(), getFiltro()));
                setGAE(GAE_old);
                int num = 0;
                for (Iterator i = GAE_old.iterator(); i.hasNext(); ) {
                    WorkpackageBulk linea = (WorkpackageBulk) i.next();
                    linea = (((Linea_attivitaComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Linea_attivitaComponentSession", Linea_attivitaComponentSession.class))
                            .completaOggetto(context.getUserContext(false), linea));
                    getGAE().set(num, linea);
                    num++;
                }
            } catch (ComponentException e) {
                codiceErrore = Constants.ERRORE_SIP_100;
            } catch (RemoteException e) {
                codiceErrore = Constants.ERRORE_SIP_100;
            } catch (PersistencyException e) {
                codiceErrore = Constants.ERRORE_SIP_100;
            } catch (EJBException e) {
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

    public String getCdr() {
        return cdr;
    }

    public void setCdr(String cdr) {
        this.cdr = cdr;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List getGAE() {
        return GAE;
    }

    public void setGAE(List gae) {
        GAE = gae;
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