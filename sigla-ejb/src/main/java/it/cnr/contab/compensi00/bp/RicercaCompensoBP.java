package it.cnr.contab.compensi00.bp;

import java.io.IOException;
import java.math.BigDecimal;
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

import it.cnr.contab.compensi00.docs.bulk.VCompensoSIPBulk;
import it.cnr.contab.compensi00.ejb.CompensoComponentSession;
import it.cnr.contab.config00.bp.ResponseXMLBP;
import it.cnr.contab.config00.util.Constants;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.DateUtils;

public class RicercaCompensoBP extends BusinessProcess implements ResponseXMLBP{
	private transient static final Logger logger = LoggerFactory.getLogger(RicercaCompensoBP.class);

	private String query;
	private String dominio;
	private Integer codiceErrore;
	private Integer numMax;
	private String user;
	private List compensi;
	private String ricerca;	
	private String uo;
	private String terzo;
	private String prefix="cercacompenso:";
	//????
	private String voce;
	private String cdr;
	private String gae;
	private String dt_inizio_rend;
	private String dt_fine_rend;
	public RicercaCompensoBP() {
		super();
	}

	public RicercaCompensoBP(String s) {
		super(s);
	}
	private Element generaErrore(Document xmldoc){
		Element e = xmldoc.createElement(prefix+"errore");
		e.setAttribute("codice",codiceErrore.toString());
		Node n = xmldoc.createTextNode(Constants.erroriSIP.get(codiceErrore));
		e.appendChild(n);
		return e;
	}
	private Element generaNumeroCompensi(Document xmldoc){
		Element e = xmldoc.createElement("numris");
		Node n = xmldoc.createTextNode(new Integer(getCompensi().size()).toString());
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
	    	//Document xmldoc = impl.createDocument("https://contab.cnr.it/SIGLA/schema/cercacompenso.xsd","root", null);
	    	//Element root = xmldoc.getDocumentElement();
	    	Document xmldoc = impl.createDocument(null, "root", null);
	    	Element root = xmldoc.getDocumentElement();
	    	root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:noNamespaceSchemaLocation", "https://contab.cnr.it/SIGLA/schema/cercacompenso.xsd");

	    	if (codiceErrore!= null){
	    		root.appendChild(generaErrore(xmldoc));
	    	}else{
	    		root.appendChild(generaNumeroCompensi(xmldoc));
	    		int num = 0;
	    		if (getCompensi() != null && !getCompensi().isEmpty()){
		    		for (Iterator a= getCompensi().iterator();a.hasNext()&&num<getNumMax().intValue();){
		    			VCompensoSIPBulk comp = (VCompensoSIPBulk)a.next();
		    			root.appendChild(generaDettaglio(xmldoc,comp));
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
	private Node generaDettaglio(Document xmldoc, VCompensoSIPBulk comp) {
		Element elementCompenso = xmldoc.createElement("compenso");
		
		Element element = xmldoc.createElement("esercizio");
		Node node = xmldoc.createTextNode(comp.getEsercizio().toString());
		element.appendChild(node);
		elementCompenso.appendChild(element);
	
        element = xmldoc.createElement("cds");
		node = xmldoc.createTextNode(comp.getCd_cds());
		element.appendChild(node);
		elementCompenso.appendChild(element);
		
		element = xmldoc.createElement("uo");
		node = xmldoc.createTextNode(comp.getCd_unita_organizzativa());
		element.appendChild(node);
		elementCompenso.appendChild(element);
		
		element = xmldoc.createElement("pg_compenso");
		node = xmldoc.createTextNode(comp.getPg_compenso().toString());
		element.appendChild(node);
		elementCompenso.appendChild(element);
		
		element = xmldoc.createElement("codiceterzo");
		node = xmldoc.createTextNode(comp.getCd_terzo().toString());
		element.appendChild(node);
		elementCompenso.appendChild(element);
		
		element = xmldoc.createElement("denominazioneterzo");
		node = xmldoc.createTextNode(comp.getDenominazione()==null?" ":comp.getDenominazione());
		element.appendChild(node);
		elementCompenso.appendChild(element);
		
		element = xmldoc.createElement("codicefiscale");
		node = xmldoc.createTextNode(comp.getCodice_fiscale()==null?" ":comp.getCodice_fiscale());
		element.appendChild(node);
		elementCompenso.appendChild(element); 
		
		element = xmldoc.createElement("partitaiva");
		node = xmldoc.createTextNode(comp.getPartita_iva()==null?" ":comp.getPartita_iva());
		element.appendChild(node);
		elementCompenso.appendChild(element); 
		
		element = xmldoc.createElement("tipo");
		node = xmldoc.createTextNode(comp.getTipo()=="D"?"Dipendente":"Altro");
		element.appendChild(node);
		elementCompenso.appendChild(element);
		
		element = xmldoc.createElement("matricola");
		node = xmldoc.createTextNode(comp.getMatricola()==null?" ":comp.getMatricola().toString());
		element.appendChild(node);
		elementCompenso.appendChild(element); 
		
		element = xmldoc.createElement("descrizione");
		node = xmldoc.createTextNode(comp.getDs_compenso()==null?"":comp.getDs_compenso());
		element.appendChild(node);
		elementCompenso.appendChild(element);

		int mesi= (DateUtils.monthsBetweenDates(comp.getDt_da_competenza_coge(),comp.getDt_a_competenza_coge()));
		element = xmldoc.createElement("mesi");
		node = xmldoc.createTextNode(new Long(mesi).toString());
		element.appendChild(node);
		elementCompenso.appendChild(element);

		element = xmldoc.createElement("totalemensile");
		node = xmldoc.createTextNode(comp.getIm_totale_compenso().setScale(2, java.math.BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(mesi),2, java.math.BigDecimal.ROUND_HALF_UP).toString());//new it.cnr.contab.util.EuroFormat().format(comp.getIm_totale_compenso().divide(new BigDecimal(mesi),2, java.math.BigDecimal.ROUND_HALF_UP)));
		element.appendChild(node);
		elementCompenso.appendChild(element);
		
        element = xmldoc.createElement("totalecompenso");
        node = xmldoc.createTextNode(comp.getIm_totale_compenso()==null?"": comp.getIm_totale_compenso().setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString());//new it.cnr.contab.util.EuroFormat().format(comp.getIm_totale_compenso()));
		element.appendChild(node);
		elementCompenso.appendChild(element);
	
		return elementCompenso;
	}

	public void eseguiRicerca(it.cnr.jada.action.ActionContext context)throws BusinessProcessException{
		 if(getQuery()== null){
			codiceErrore = Constants.ERRORE_SIP_101;
			return;
		}else if(getDominio()== null||(!getDominio().equalsIgnoreCase("pg_compenso")&&!getDominio().equalsIgnoreCase("descrizione"))){
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
				setCompensi(((CompensoComponentSession)createComponentSession("CNRCOMPENSI00_EJB_CompensoComponentSession",CompensoComponentSession.class))
						.findListaCompensiSIP(context.getUserContext(false), getQuery(), getDominio(), getUo(), getTerzo(),getVoce(), getCdr(), getGae(), getRicerca(),new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse(getDt_inizio_rend()).getTime()),new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse(getDt_fine_rend()).getTime())));
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

	public List getCompensi() {
		return compensi;
	}

	public void setCompensi(List compensi) {
		this.compensi = compensi;
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
