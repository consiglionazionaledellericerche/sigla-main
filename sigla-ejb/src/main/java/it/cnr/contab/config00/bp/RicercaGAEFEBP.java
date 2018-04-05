package it.cnr.contab.config00.bp;

import it.cnr.contab.config00.ejb.Linea_attivitaComponentSession;
import it.cnr.contab.config00.latt.bulk.CostantiTi_gestione;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

public class RicercaGAEFEBP extends BusinessProcess implements ResponseXMLBP {
    private transient static final Logger logger = LoggerFactory.getLogger(RicercaGAEFEBP.class);

    private Integer codiceErrore;
    private Integer modulo;
    private Integer esercizio;


    private String user;
    private String cdr;
    private List GAE;

    public RicercaGAEFEBP() {
        super();
    }

    public RicercaGAEFEBP(String s) {
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

    private Element generaDettaglioGAE(Document xmldoc, String cdr, String descrizioneCdr, String codice, String descrizioneGae, String denominazioneGae,
                                       String natura, String tipoNatura, Integer esercizioInizio, Integer esercizioFine, String tipo) {

        Element element = xmldoc.createElement("gae");

        Element elementCdr = xmldoc.createElement("cdr");
        Node nodeCdr = xmldoc.createTextNode(cdr);
        elementCdr.appendChild(nodeCdr);
        element.appendChild(elementCdr);

        Element elementDescrizioneCdr = xmldoc.createElement("descrizioneCdr");
        Node nodeDescrizioneCdr = xmldoc.createTextNode(descrizioneCdr == null ? "" : descrizioneCdr);
        elementDescrizioneCdr.appendChild(nodeDescrizioneCdr);
        element.appendChild(elementDescrizioneCdr);

        Element elementCodice = xmldoc.createElement("codiceGae");
        Node nodeCodice = xmldoc.createTextNode(codice);
        elementCodice.appendChild(nodeCodice);
        element.appendChild(elementCodice);

        Element elementDescrizioneGae = xmldoc.createElement("descrizioneGae");
        Node nodeDescrizioneGae = xmldoc.createTextNode(descrizioneGae == null ? "" : descrizioneGae);
        elementDescrizioneGae.appendChild(nodeDescrizioneGae);
        element.appendChild(elementDescrizioneGae);

        Element elementDenominazioneGae = xmldoc.createElement("denominazioneGae");
        Node nodeDenominazioneGae = xmldoc.createTextNode(denominazioneGae == null ? "" : denominazioneGae);
        elementDenominazioneGae.appendChild(nodeDenominazioneGae);
        element.appendChild(elementDenominazioneGae);

        Element elementNatura = xmldoc.createElement("natura");
        Node nodeNatura = xmldoc.createTextNode(natura == null ? "" : natura);
        elementNatura.appendChild(nodeNatura);
        element.appendChild(elementNatura);

        Element elementTipoNatura = xmldoc.createElement("tipoNatura");
        Node nodeTipoNatura = xmldoc.createTextNode(tipoNatura == null ? "" : tipoNatura.compareTo(NaturaBulk.TIPO_NATURA_FONTI_ESTERNE) == 0 ? "Fonti Esterne" : "Fonti Interne");
        elementTipoNatura.appendChild(nodeTipoNatura);
        element.appendChild(elementTipoNatura);

        Element elementEsercizioInizio = xmldoc.createElement("esercizioInizio");
        Node nodeEsercizioInizio = xmldoc.createTextNode(esercizioInizio == null ? "" : esercizioInizio.toString());
        elementEsercizioInizio.appendChild(nodeEsercizioInizio);
        element.appendChild(elementEsercizioInizio);

        Element elementEsercizioFine = xmldoc.createElement("esercizioFine");
        Node nodeEsercizioFine = xmldoc.createTextNode(esercizioFine == null ? "" : esercizioFine.toString());
        elementEsercizioFine.appendChild(nodeEsercizioFine);
        element.appendChild(elementEsercizioFine);

        Element elementTipo = xmldoc.createElement("tipo");
        Node nodeTipo = xmldoc.createTextNode(tipo == null ? "" : tipo.compareTo(CostantiTi_gestione.TI_GESTIONE_ENTRATE) == 0 ? "Entrata" : "Spesa");
        elementTipo.appendChild(nodeTipo);
        element.appendChild(elementTipo);

        return element;
    }

    public void generaXML(PageContext pagecontext) throws IOException, ServletException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();
            Document xmldoc = impl.createDocument(null, "root", null);
            Element root = xmldoc.getDocumentElement();
            root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:noNamespaceSchemaLocation", "https://contab.cnr.it/SIGLA/schema/cercagaefe.xsd");
            if (codiceErrore != null) {
                root.appendChild(generaErrore(xmldoc));
            } else {
                root.appendChild(generaNumeroGAE(xmldoc));
                int num = 0;
                if (getGAE() != null && !getGAE().isEmpty()) {
                    for (Iterator i = getGAE().iterator(); i.hasNext(); ) {
                        WorkpackageBulk linea = (WorkpackageBulk) i.next();
                        root.appendChild(generaDettaglioGAE(xmldoc, linea.getCd_centro_responsabilita(), linea.getCentro_responsabilita().getDs_cdr(), linea.getCd_linea_attivita(), linea.getDs_linea_attivita(),
                                linea.getDenominazione(), linea.getCd_natura(), linea.getNatura().getTipo(), linea.getEsercizio_inizio(), linea.getEsercizio_fine(), linea.getTi_gestione()));
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

        if (modulo == null) {
            codiceErrore = Constants.ERRORE_SIP_120;
            return;
        } else {
            try {
                GAE = (((Linea_attivitaComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Linea_attivitaComponentSession", Linea_attivitaComponentSession.class))
                        .findListaGAEFEWS(context.getUserContext(false), getCdr(), getModulo()));
                int num = 0;
                for (Iterator i = GAE.iterator(); i.hasNext(); ) {
                    WorkpackageBulk linea = (WorkpackageBulk) i.next();
                    //linea=(((Linea_attivitaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Linea_attivitaComponentSession",Linea_attivitaComponentSession.class)).completaOggetto(context.getUserContext(), linea));
                    linea.setCentro_responsabilita((CdrBulk) (((FatturaAttivaSingolaComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession", FatturaAttivaSingolaComponentSession.class))
                            .completaOggetto(context.getUserContext(false), linea.getCentro_responsabilita())));
                    linea.setNatura((NaturaBulk) (((FatturaAttivaSingolaComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession", FatturaAttivaSingolaComponentSession.class))
                            .completaOggetto(context.getUserContext(false), linea.getNatura())));
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

    public Integer getEsercizio() {
        return esercizio;
    }

    public void setEsercizio(Integer esercizio) {
        this.esercizio = esercizio;
    }

    public String getCdr() {
        return cdr;
    }

    public void setCdr(String cdr) {
        this.cdr = cdr;
    }

    public List getGAE() {
        return GAE;
    }

    public void setGAE(List gae) {
        GAE = gae;
    }

    public Integer getModulo() {
        return modulo;
    }

    public void setModulo(Integer modulo) {
        this.modulo = modulo;
    }

}