package it.cnr.contab.config00.bp;

import it.cnr.contab.anagraf00.ejb.ComuneComponentSession;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneEsteroBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk;
import it.cnr.contab.config00.util.Constants;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RicercaNaProComBP extends BusinessProcess implements ResponseXMLBP {

    private transient static final Logger logger = LoggerFactory.getLogger(RicercaNaProComBP.class);
    public static String NAZIONI = "nazioni";
    public static String PROVINCE = "province";
    public static String COMUNI = "comuni";
    private String servizio;
    private Integer codiceErrore;
    private Integer numMax;
    private String user;
    private List comuni = new ArrayList();
    private List nazioni = new ArrayList();
    private List province = new ArrayList();
    //Parametri per l'inserimento di un nuovo comune
    private String ds_comune;
    private Integer pg_nazione;
    private String provincia;
    private Long nazione;

    public Integer getCodiceErrore() {
        return codiceErrore;
    }

    public void setCodiceErrore(Integer codiceErrore) {
        this.codiceErrore = codiceErrore;
    }

    public List getComuni() {
        return comuni;
    }

    public void setComuni(List comuni) {
        this.comuni = comuni;
    }

    public String getDs_comune() {
        return ds_comune;
    }

    public void setDs_comune(String ds_comune) {
        this.ds_comune = ds_comune;
    }

    public List getNazioni() {
        return nazioni;
    }

    public void setNazioni(List nazioni) {
        this.nazioni = nazioni;
    }

    public Integer getNumMax() {
        return numMax;
    }

    public void setNumMax(Integer numMax) {
        this.numMax = numMax;
    }

    public Integer getPg_nazione() {
        return pg_nazione;
    }

    public void setPg_nazione(Integer pg_nazione) {
        this.pg_nazione = pg_nazione;
    }

    public List getProvince() {
        return province;
    }

    public void setProvince(List province) {
        this.province = province;
    }

    public String getServizio() {
        return servizio;
    }

    public void setServizio(String servizio) {
        this.servizio = servizio;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private Element generaErrore(Document xmldoc, String tipo) {
        Element e = xmldoc.createElement(tipo + ":errore");
        e.setAttribute("codice", codiceErrore.toString());
        Node n = xmldoc.createTextNode(Constants.erroriSIP.get(codiceErrore));
        e.appendChild(n);
        return e;
    }

    private Element generaDettaglioNazioni(Document xmldoc, Long pg_nazione, String ds_nazione) {
        Element elementNazione = xmldoc.createElement("nazioni:nazione");
        Element elementPg = xmldoc.createElement("nazioni:pg");
        Node nodeCodice = xmldoc.createTextNode(pg_nazione.toString());
        elementPg.appendChild(nodeCodice);
        elementNazione.appendChild(elementPg);

        Element elementDs = xmldoc.createElement("nazioni:ds");
        Node nodeCognome = xmldoc.createTextNode(ds_nazione);
        elementDs.appendChild(nodeCognome);
        elementNazione.appendChild(elementDs);

        return elementNazione;
    }

    private Element generaDettaglioProvince(Document xmldoc, String cd_provincia, String ds_provincia) {
        Element elementProvincia = xmldoc.createElement("province:provincia");
        Element elementCd = xmldoc.createElement("province:cd");
        Node nodeCodice = xmldoc.createTextNode(cd_provincia);
        elementCd.appendChild(nodeCodice);
        elementProvincia.appendChild(elementCd);

        Element elementDs = xmldoc.createElement("province:ds");
        Node nodeCognome = xmldoc.createTextNode(ds_provincia);
        elementDs.appendChild(nodeCognome);
        elementProvincia.appendChild(elementDs);

        return elementProvincia;
    }

    private Element generaDettaglioComuni(Document xmldoc, Long pg_comune, String ds_comune) {
        Element elementComune = xmldoc.createElement("comuni:comune");
        Element elementPg = xmldoc.createElement("comuni:pg");
        Node nodeCodice = xmldoc.createTextNode(pg_comune.toString());
        elementPg.appendChild(nodeCodice);
        elementComune.appendChild(elementPg);

        Element elementDs = xmldoc.createElement("comuni:ds");
        Node nodeCognome = xmldoc.createTextNode(ds_comune);
        elementDs.appendChild(nodeCognome);
        elementComune.appendChild(elementDs);

        return elementComune;
    }

    private String getTipoServizio() {
        if (getServizio().equalsIgnoreCase("caricaNazioni"))
            return NAZIONI;
        if (getServizio().equalsIgnoreCase("caricaProvince"))
            return PROVINCE;
        if (getServizio().equalsIgnoreCase("caricaComuni"))
            return COMUNI;
        if (getServizio().equalsIgnoreCase("nuovoComune"))
            return COMUNI;
        return "";
    }

    public void generaXML(PageContext pagecontext) throws IOException, ServletException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();
            Document xmldoc = impl.createDocument("http://gestioneistituti.cnr.it/ns/" + getTipoServizio(), getTipoServizio() + ":root", null);
            Element root = xmldoc.getDocumentElement();
            if (codiceErrore != null) {
                root.appendChild(generaErrore(xmldoc, getTipoServizio()));
            } else {
                if (getTipoServizio().equalsIgnoreCase(NAZIONI)) {
                    if (getNazioni() != null && !getNazioni().isEmpty()) {
                        for (Iterator i = getNazioni().iterator(); i.hasNext(); ) {
                            NazioneBulk nazione = (NazioneBulk) i.next();
                            root.appendChild(generaDettaglioNazioni(xmldoc, nazione.getPg_nazione(), nazione.getDs_nazione()));
                        }
                    }
                } else if (getTipoServizio().equalsIgnoreCase(PROVINCE)) {
                    if (getProvince() != null && !getProvince().isEmpty()) {
                        for (Iterator i = getProvince().iterator(); i.hasNext(); ) {
                            ProvinciaBulk provincia = (ProvinciaBulk) i.next();
                            root.appendChild(generaDettaglioProvince(xmldoc, provincia.getCd_provincia(), provincia.getDs_provincia()));
                        }
                    }
                } else if (getTipoServizio().equalsIgnoreCase(COMUNI)) {
                    if (getComuni() != null && !getComuni().isEmpty()) {
                        for (Iterator i = getComuni().iterator(); i.hasNext(); ) {
                            ComuneBulk comune = (ComuneBulk) i.next();
                            root.appendChild(generaDettaglioComuni(xmldoc, comune.getPg_comune(), comune.getDs_comune()));
                        }
                    }
                }
            }
            DOMSource domSource = new DOMSource(xmldoc);
            StreamResult streamResult = new StreamResult(pagecontext.getOut());
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://150.146.206.250/DTD/nazprocom.dtd");
            serializer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, getTipoServizio());
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.STANDALONE, "no");
            serializer.transform(domSource, streamResult);
            closed();
        } catch (ParserConfigurationException | TransformerException | BusinessProcessException e) {
            logger.error("GeneraXML error -> ", e);
        }
    }


    public void caricaNazioni(ActionContext actioncontext) {
        try {
            RemoteIterator iterator = EJBCommonServices.openRemoteIterator(actioncontext, ((CRUDComponentSession) createComponentSession("JADAEJB_CRUDComponentSession", CRUDComponentSession.class))
                    .cerca(actioncontext.getUserContext(false), null, new NazioneBulk()));
            ((it.cnr.jada.util.RemoteOrderable) iterator).setOrderBy("ds_nazione", 1);
            while (iterator.hasMoreElements())
                getNazioni().add(iterator.nextElement());
            EJBCommonServices.closeRemoteIterator(actioncontext, iterator);
        } catch (ComponentException e) {
            codiceErrore = Constants.ERRORE_SIP_100;
        } catch (RemoteException e) {
            codiceErrore = Constants.ERRORE_SIP_100;
        } catch (BusinessProcessException e) {
            codiceErrore = Constants.ERRORE_SIP_100;
        }
    }

    public void caricaProvice(ActionContext actioncontext) {
        try {
            RemoteIterator iterator = EJBCommonServices.openRemoteIterator(actioncontext, ((CRUDComponentSession) createComponentSession("JADAEJB_CRUDComponentSession", CRUDComponentSession.class))
                    .cerca(actioncontext.getUserContext(false), null, new ProvinciaBulk()));
            ((it.cnr.jada.util.RemoteOrderable) iterator).setOrderBy("ds_provincia", 1);
            while (iterator.hasMoreElements())
                getProvince().add(iterator.nextElement());
            EJBCommonServices.closeRemoteIterator(actioncontext, iterator);
        } catch (ComponentException e) {
            codiceErrore = Constants.ERRORE_SIP_100;
        } catch (RemoteException e) {
            codiceErrore = Constants.ERRORE_SIP_100;
        } catch (BusinessProcessException e) {
            codiceErrore = Constants.ERRORE_SIP_100;
        }
    }

    public void caricaComuni(ActionContext actioncontext) {
        try {
            ComuneBulk comune = new ComuneBulk();
            comune.setNazione(new NazioneBulk(getNazione()));
            comune.setProvincia(new ProvinciaBulk(getProvincia()));
            RemoteIterator iterator = EJBCommonServices.openRemoteIterator(actioncontext, ((CRUDComponentSession) createComponentSession("JADAEJB_CRUDComponentSession", CRUDComponentSession.class))
                    .cerca(actioncontext.getUserContext(false), null, comune));
            ((it.cnr.jada.util.RemoteOrderable) iterator).setOrderBy("ds_comune", 1);
            while (iterator.hasMoreElements())
                getComuni().add(iterator.nextElement());
            EJBCommonServices.closeRemoteIterator(actioncontext, iterator);
        } catch (ComponentException e) {
            codiceErrore = Constants.ERRORE_SIP_100;
        } catch (RemoteException e) {
            codiceErrore = Constants.ERRORE_SIP_100;
        } catch (BusinessProcessException e) {
            codiceErrore = Constants.ERRORE_SIP_100;
        }
    }

    public void nuovoComune(ActionContext actioncontext) {
        try {
            ComuneBulk comune = new ComuneEsteroBulk();
            comune.setNazione(new NazioneBulk(getNazione()));
            comune.setDs_comune(getDs_comune());
            comune.setTi_italiano_estero(ComuneBulk.COMUNE_ESTERO);
            comune.setCd_catastale(ComuneBulk.CODICE_CATASTALE_ESTERO);
            comune.setToBeCreated();
            comune = (ComuneBulk) ((ComuneComponentSession) createComponentSession("CNRANAGRAF00_EJB_ComuneComponentSession", ComuneComponentSession.class))
                    .inizializzaBulkPerInserimento(actioncontext.getUserContext(false), comune);
            comune = (ComuneBulk) ((ComuneComponentSession) createComponentSession("CNRANAGRAF00_EJB_ComuneComponentSession", ComuneComponentSession.class))
                    .creaConBulk(actioncontext.getUserContext(false), comune);
            RemoteIterator iterator = EJBCommonServices.openRemoteIterator(actioncontext, ((CRUDComponentSession) createComponentSession("JADAEJB_CRUDComponentSession", CRUDComponentSession.class))
                    .cerca(actioncontext.getUserContext(false), null, comune));
            while (iterator.hasMoreElements())
                getComuni().add(iterator.nextElement());
            EJBCommonServices.closeRemoteIterator(actioncontext, iterator);
        } catch (ComponentException e) {
            codiceErrore = Constants.ERRORE_SIP_100;
        } catch (RemoteException e) {
            codiceErrore = Constants.ERRORE_SIP_100;
        } catch (BusinessProcessException e) {
            codiceErrore = Constants.ERRORE_SIP_100;
        }
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Long getNazione() {
        return nazione;
    }

    public void setNazione(Long nazione) {
        this.nazione = nazione;
    }

}
