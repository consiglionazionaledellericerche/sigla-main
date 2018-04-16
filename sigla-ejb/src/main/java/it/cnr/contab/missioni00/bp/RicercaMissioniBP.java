package it.cnr.contab.missioni00.bp;

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

import it.cnr.contab.config00.bp.RicercaGAEFEBP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;

import it.cnr.contab.config00.bp.ResponseXMLBP;
import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.missioni00.docs.bulk.VMissioneSIPBulk;
import it.cnr.contab.missioni00.ejb.MissioneComponentSession;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.DateUtils;


public class RicercaMissioniBP extends BusinessProcess implements ResponseXMLBP{
	private transient static final Logger logger = LoggerFactory.getLogger(RicercaMissioniBP.class);

	private String query;
	private String dominio;
	private Integer codiceErrore;
	private Integer numMax;
	private String user;
	private List missioni;
	private String ricerca;	
	private String uo;
	private String terzo;
	private String prefix="cercamissione:";
	//????
	private String voce;
	private String cdr;
	private String gae;
	private String dt_inizio_rend;
	private String dt_fine_rend;
	public RicercaMissioniBP() {
		super();
	}

	public RicercaMissioniBP(String s) {
		super(s);
	}
	private Element generaErrore(Document xmldoc){
		Element e = xmldoc.createElement(prefix+"errore");
		e.setAttribute("codice",codiceErrore.toString());
		Node n = xmldoc.createTextNode(Constants.erroriSIP.get(codiceErrore));
		e.appendChild(n);
		return e;
	}
	private Element generaNumeroMissioni(Document xmldoc){
		Element e = xmldoc.createElement("numris");
		Node n = xmldoc.createTextNode(new Integer(getMissioni().size()).toString());
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
	    	//Document xmldoc = impl.createDocument("https://contab.cnr.it/SIGLA/schema/cercamissione.xsd","root", null);
	    	//Element root = xmldoc.getDocumentElement();
	    	Document xmldoc = impl.createDocument(null, "root", null);
	    	Element root = xmldoc.getDocumentElement();
	    	root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:noNamespaceSchemaLocation", "https://contab.cnr.it/SIGLA/schema/cercamissione.xsd");

	    	if (codiceErrore!= null){
	    		root.appendChild(generaErrore(xmldoc));
	    	}else{
	    		root.appendChild(generaNumeroMissioni(xmldoc));
	    		int num = 0;
	    		if (getMissioni() != null && !getMissioni().isEmpty()){
		    		for (Iterator a= getMissioni().iterator();a.hasNext()&&num<getNumMax().intValue();){
		    			VMissioneSIPBulk missione = (VMissioneSIPBulk)a.next();
		    			root.appendChild(generaDettaglio(xmldoc,missione));
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

	private Node generaDettaglio(Document xmldoc, VMissioneSIPBulk miss) {
		
		Element elementMissione = xmldoc.createElement("missione");
		
		Element element = xmldoc.createElement("esercizio");
		Node node = xmldoc.createTextNode(miss.getEsercizio().toString());
		element.appendChild(node);
		elementMissione.appendChild(element);
		
        element = xmldoc.createElement("cds");
		node = xmldoc.createTextNode(miss.getCd_cds());
		element.appendChild(node);
		elementMissione.appendChild(element);
		
		element = xmldoc.createElement("uo");
		node = xmldoc.createTextNode(miss.getCd_unita_organizzativa());
		element.appendChild(node);
		elementMissione.appendChild(element);
		
		element = xmldoc.createElement("pg_missione");
		node = xmldoc.createTextNode(miss.getPg_missione().toString());
		element.appendChild(node);
		elementMissione.appendChild(element);
		
		element = xmldoc.createElement("codiceterzo");
		node = xmldoc.createTextNode(miss.getCd_terzo().toString());
		element.appendChild(node);
		elementMissione.appendChild(element);
		
		element = xmldoc.createElement("denominazioneterzo");
		node = xmldoc.createTextNode(miss.getDenominazione());
		element.appendChild(node);
		elementMissione.appendChild(element);
		
		element = xmldoc.createElement("codicefiscale");
		node = xmldoc.createTextNode(miss.getCodice_fiscale()==null?"":miss.getCodice_fiscale());
		element.appendChild(node);
		elementMissione.appendChild(element);
		
		element = xmldoc.createElement("partitaiva");
		node = xmldoc.createTextNode(miss.getPartita_iva()==null?"":miss.getPartita_iva());
		element.appendChild(node);
		elementMissione.appendChild(element);
		
		element = xmldoc.createElement("tipo");
		node = xmldoc.createTextNode(miss.getTipo()=="D"?"Dipendente":"Altro");
		element.appendChild(node);
		elementMissione.appendChild(element);
		
		element = xmldoc.createElement("matricola");
		node = xmldoc.createTextNode(miss.getMatricola()==null?"":miss.getMatricola().toString());
		element.appendChild(node);
		elementMissione.appendChild(element);

		element = xmldoc.createElement("motivo");
		node = xmldoc.createTextNode(miss.getDs_missione()==null?"":miss.getDs_missione());
		element.appendChild(node);
		elementMissione.appendChild(element);

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");

		element = xmldoc.createElement("datainizio");
		node = xmldoc.createTextNode(miss.getDt_inizio_missione()==null?"":formatter.format(miss.getDt_inizio_missione()));
		element.appendChild(node);
		elementMissione.appendChild(element);


		element = xmldoc.createElement("datafine");
		node = xmldoc.createTextNode(miss.getDt_fine_missione()==null?"":formatter.format(miss.getDt_fine_missione()));
		element.appendChild(node);
		elementMissione.appendChild(element);

		Long giorni= (DateUtils.daysBetweenDates(miss.getDt_inizio_missione(), miss.getDt_fine_missione())+1);
		element = xmldoc.createElement("giorni");
		node = xmldoc.createTextNode(giorni.toString());
		element.appendChild(node);
		elementMissione.appendChild(element);

        element = xmldoc.createElement("totalemissione");
		node = xmldoc.createTextNode(miss.getIm_totale_missione()==null?"":miss.getIm_totale_missione().setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString());//new it.cnr.contab.util.EuroFormat().format(miss.getIm_totale_missione()));
		element.appendChild(node);
		elementMissione.appendChild(element);
		  
		return elementMissione;
	}

	public void eseguiRicerca(it.cnr.jada.action.ActionContext context)throws BusinessProcessException{
		 if(getQuery()== null){
			codiceErrore = Constants.ERRORE_SIP_101;
			return;
		}else if(getDominio()== null||(!getDominio().equalsIgnoreCase("pg_missione")&&!getDominio().equalsIgnoreCase("descrizione"))){
			codiceErrore = Constants.ERRORE_SIP_102;
			return;
		}else if(getUo()== null){
			codiceErrore = Constants.ERRORE_SIP_113;
			return;
		}else if (getDt_inizio_rend()==null || getDt_fine_rend()==null){
			codiceErrore = Constants.ERRORE_SIP_115;
			return;
		}else{
			try{	 	
				setMissioni(((MissioneComponentSession)createComponentSession("CNRMISSIONI00_EJB_MissioneComponentSession",MissioneComponentSession.class))
						.findListaMissioniSIP(context.getUserContext(false), getQuery(), getDominio(), getUo(), getTerzo(),getVoce(), getCdr(), getGae(), getRicerca(),new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse(getDt_inizio_rend()).getTime()),new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse(getDt_fine_rend()).getTime())));
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

	public List getMissioni() {
		return missioni;
	}

	public void setMissioni(List missioni) {
		this.missioni = missioni;
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
