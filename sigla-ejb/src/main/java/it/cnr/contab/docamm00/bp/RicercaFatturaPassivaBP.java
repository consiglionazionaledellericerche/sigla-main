package it.cnr.contab.docamm00.bp;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;

import it.cnr.contab.config00.bp.ResponseXMLBP;
import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.docamm00.docs.bulk.VFatturaPassivaSIPBulk;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;

public class RicercaFatturaPassivaBP extends BusinessProcess implements ResponseXMLBP{
	private transient static final Logger logger = LoggerFactory.getLogger(RicercaFatturaPassivaBP.class);

	private String query;
	private String dominio;
	private Integer codiceErrore;
	private Integer numMax;
	private String user;
	private List fatture;
	private String ricerca;
	//Parametri per l'inserimento di un nuovo terzo 	
	private String uo;
	private String terzo;
	private String voce;
	private String cdr;
	private String gae;
	private String prefix="cercafatturapassiva:";
	private String dt_inizio_rend;
	private String dt_fine_rend;
	public RicercaFatturaPassivaBP() {
		super();
	}

	public RicercaFatturaPassivaBP(String s) {
		super(s);
	}
	private Element generaErrore(Document xmldoc){
		Element e = xmldoc.createElement(prefix+"errore");
		e.setAttribute("codice",codiceErrore.toString());
		Node n = xmldoc.createTextNode(Constants.erroriSIP.get(codiceErrore));
		e.appendChild(n);
		return e;
	}
	private Element generaNumeroFatture(Document xmldoc){
		Element e = xmldoc.createElement("numris");
		Node n = xmldoc.createTextNode(new Integer(getFatture().size()).toString());
    	e.appendChild(n);
    	return e;	
	}
	public void generaXML(PageContext pagecontext) throws IOException, ServletException{
		try {
			if (getNumMax()==null)
				setNumMax(new Integer(20));
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder builder = factory.newDocumentBuilder();
	    	DOMImplementation impl = builder.getDOMImplementation();
	    	//Document xmldoc = impl.createDocument("https://contab.cnr.it/SIGLA/schema/cercafatturapassiva.xsd","root", null);
	    	//Element root = xmldoc.getDocumentElement();
	    	Document xmldoc = impl.createDocument(null, "root", null);
	    	Element root = xmldoc.getDocumentElement();
	    	root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:noNamespaceSchemaLocation", "https://contab.cnr.it/SIGLA/schema/cercafatturapassiva.xsd");

	    	if (codiceErrore!= null){
	    		root.appendChild(generaErrore(xmldoc));
	    	}else{
	    		root.appendChild(generaNumeroFatture(xmldoc));
	    		int num = 0;
	    		if (getFatture() != null && !getFatture().isEmpty()){
		    		for (Iterator a= getFatture().iterator();a.hasNext()&&num<getNumMax().intValue();){
		    			VFatturaPassivaSIPBulk fatt = (VFatturaPassivaSIPBulk)a.next();
		    			root.appendChild(generaDettaglio(xmldoc,fatt));
		    			num++;
		    		}
	    		}
	    	}
	    	DOMSource domSource = new DOMSource(xmldoc);
	    	StreamResult streamResult = new StreamResult(pagecontext.getOut());
	    	TransformerFactory tf = TransformerFactory.newInstance();
	    	Transformer serializer = tf.newTransformer();
	    	serializer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
	    	serializer.setOutputProperty(OutputKeys.INDENT,"yes");
	    	serializer.setOutputProperty(OutputKeys.STANDALONE,"no");
	    	serializer.transform(domSource, streamResult);
			closed();
		} catch (ParserConfigurationException | TransformerException | BusinessProcessException e) {
			logger.error("GeneraXML error -> ", e);
		}
    }
	private Node generaDettaglio(Document xmldoc, VFatturaPassivaSIPBulk fatt) {
		
		Element elementFattura = xmldoc.createElement("fattura");
		
		Element element = xmldoc.createElement("esercizio");
		Node node = xmldoc.createTextNode(fatt.getEsercizio().toString());
		element.appendChild(node);
		elementFattura.appendChild(element);
		
        element = xmldoc.createElement("cds");
		node = xmldoc.createTextNode(fatt.getCd_cds());
		element.appendChild(node);
		elementFattura.appendChild(element);
		
		element = xmldoc.createElement("uo");
		node = xmldoc.createTextNode(fatt.getCd_unita_organizzativa());
		element.appendChild(node);
		elementFattura.appendChild(element);
		
		element = xmldoc.createElement("pg_fattura");
		node = xmldoc.createTextNode(fatt.getPg_fattura().toString());
		element.appendChild(node);
		elementFattura.appendChild(element);
		
		element = xmldoc.createElement("codiceterzo");
		node = xmldoc.createTextNode(fatt.getCd_terzo().toString());
		element.appendChild(node);
		elementFattura.appendChild(element);
		
		element = xmldoc.createElement("denominazioneterzo");
		node = xmldoc.createTextNode(fatt.getDenominazione());
		element.appendChild(node);
		elementFattura.appendChild(element);
		
		element = xmldoc.createElement("indirizzo");
		node = xmldoc.createTextNode(fatt.getIndirizzo());
		element.appendChild(node);
		elementFattura.appendChild(element);
		
		element = xmldoc.createElement("partitaiva");
		node = xmldoc.createTextNode(fatt.getPartita_iva()==null?"":fatt.getPartita_iva());
		element.appendChild(node);
		elementFattura.appendChild(element);
		
		element = xmldoc.createElement("codicefiscale");
		node = xmldoc.createTextNode(fatt.getCodice_fiscale()==null?"":fatt.getCodice_fiscale());
		element.appendChild(node);
		elementFattura.appendChild(element);
		
		element = xmldoc.createElement("nrfatturafornitore");
		node = xmldoc.createTextNode(fatt.getNr_fattura_fornitore()==null?"":fatt.getNr_fattura_fornitore());
		element.appendChild(node);
		elementFattura.appendChild(element);
		
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
		element = xmldoc.createElement("datafatturafornitore");
		node = xmldoc.createTextNode(fatt.getDt_fattura_fornitore()==null?"":formatter.format(fatt.getDt_fattura_fornitore()));
		element.appendChild(node);
		elementFattura.appendChild(element);
		
		element = xmldoc.createElement("oggetto");
		node = xmldoc.createTextNode(fatt.getDs_fattura_passiva()==null?"":fatt.getDs_fattura_passiva());
		element.appendChild(node);
		elementFattura.appendChild(element);
		
		element = xmldoc.createElement("totalefattura");
		node = xmldoc.createTextNode(fatt.getIm_totale_fattura()==null?"":fatt.getIm_totale_fattura().setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString());//new it.cnr.contab.util.EuroFormat().format(fatt.getIm_totale_fattura()));
		element.appendChild(node);
		elementFattura.appendChild(element);
		
		return elementFattura;
	}

	public void eseguiRicerca(it.cnr.jada.action.ActionContext context)throws BusinessProcessException{
		if(getQuery()== null){
			codiceErrore = Constants.ERRORE_SIP_101;
			return;
		}else if(getDominio()== null||(!getDominio().equalsIgnoreCase("pg_fattura")&&!getDominio().equalsIgnoreCase("descrizione"))){
			codiceErrore = Constants.ERRORE_SIP_102;
			return;
		}else if(getUo()== null){
			codiceErrore = Constants.ERRORE_SIP_113;
			return;
		}else if (getDt_inizio_rend()==null || getDt_fine_rend()==null){
			codiceErrore = Constants.ERRORE_SIP_115;
			return;
		}else{
			try {
				setFatture(((FatturaPassivaComponentSession)createComponentSession("CNRDOCAMM00_EJB_FatturaPassivaComponentSession",FatturaPassivaComponentSession.class))
						.findListaFattureSIP(context.getUserContext(false), getQuery(), getDominio(),getUo(), getTerzo(), getVoce(), getCdr(), getGae(), getRicerca(),new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse(getDt_inizio_rend()).getTime()),new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse(getDt_fine_rend()).getTime())));
			} catch (ComponentException e) {
				codiceErrore = Constants.ERRORE_SIP_100;
			} catch (RemoteException e) {
				codiceErrore = Constants.ERRORE_SIP_100;
			} catch (ParseException e) {
				codiceErrore = Constants.ERRORE_SIP_116;
			}
		}
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getDominio() {
		return dominio;
	}

	public void setDominio(String dominio) {
		this.dominio = dominio;
	}

	public Integer getCodiceErrore() {
		return codiceErrore;
	}

	public void setCodiceErrore(Integer codiceErrore) {
		this.codiceErrore = codiceErrore;
	}

	public Integer getNumMax() {
		return numMax;
	}

	public void setNumMax(Integer numMax) {
		this.numMax = numMax;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List getFatture() {
		return fatture;
	}

	public void setFatture(List fatture) {
		this.fatture = fatture;
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

	public String getTerzo() {
		return terzo;
	}

	public void setTerzo(String terzo) {
		this.terzo = terzo;
	}

	public String getVoce() {
		return voce;
	}

	public void setVoce(String voce) {
		this.voce = voce;
	}

	public String getCdr() {
		return cdr;
	}

	public void setCdr(String cdr) {
		this.cdr = cdr;
	}

	public String getGae() {
		return gae;
	}

	public void setGae(String gae) {
		this.gae = gae;
	}

	public String getDt_inizio_rend() {
		return dt_inizio_rend;
	}

	public void setDt_inizio_rend(String dt_inizio_rend) {
		this.dt_inizio_rend = dt_inizio_rend;
	}

	public String getDt_fine_rend() {
		return dt_fine_rend;
	}

	public void setDt_fine_rend(String dt_fine_rend) {
		this.dt_fine_rend = dt_fine_rend;
	}
	
}
