package it.cnr.contab.config00.bp;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;

import it.cnr.contab.anagraf00.core.bulk.V_anagrafico_terzoBulk;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.bulk.RicercaContrattoBulk;
import it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.ejb.ContrattoComponentSession;
import it.cnr.contab.config00.ejb.Parametri_enteComponentSession;
import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class RicercaContrattoBP extends BusinessProcess implements ResponseXMLBP{
	private transient static final Logger logger = LoggerFactory.getLogger(RicercaContrattoBP.class);

	private String[] uo;
	private String oggetto;
	private String giuridica;
	private String stato;
	private Integer esercizio_da;
	private Integer esercizio_a;
	private Integer esercizio;
	private Long id;
	private Integer num;
	private Integer daNum;
	private String user;
	private Integer codiceErrore=null;
	private String descErroreDett=null;
	private List Contratti;
	private String prefix="sc:";
	public RicercaContrattoBP() {
		super();
	}

	public RicercaContrattoBP(String s) {
		super(s);
	}
	
	private Element generaErrore(Document xmldoc){
		Element e = xmldoc.createElement(prefix+"Errore");
		
		Element el = xmldoc.createElement(prefix+"codice");
		Node node = xmldoc.createTextNode(codiceErrore==null?"":codiceErrore.toString());
		el.appendChild(node);
		e.appendChild(el);
		
		Element elementDesc = xmldoc.createElement(prefix+"messaggio");
    	Node nodeDesc2 = xmldoc.createTextNode(Constants.erroriCON.get(codiceErrore).concat(getDescErroreDett()==null?"":getDescErroreDett()));
    	elementDesc.appendChild(nodeDesc2);
   	    e.appendChild(elementDesc);
		return e;
	}
	private Element generaNumeroContratti(Document xmldoc){
		Element e = xmldoc.createElement(prefix+"numris");
		int size;
		if (getContratti()!=null)
			size=getContratti().size();
		else
			size=0;
		Node n = xmldoc.createTextNode(new Integer(size).toString());
    	e.appendChild(n);
		return e;	
	}
	
	private Element generaDettaglioContratto(Document xmldoc, ContrattoBulk contratto){

	java.sql.Connection conn=null;
	try{
		conn= EJBCommonServices.getConnection();
		
		it.cnr.jada.persistency.sql.HomeCache homeCache = new it.cnr.jada.persistency.sql.HomeCache(conn);
		V_anagrafico_terzoBulk terzo=null;
		Ass_contratto_uoBulk ass_contr_uo=null;	
		Element element = xmldoc.createElement(prefix+"Contratto");
	
		Element elementid = xmldoc.createElement(prefix+"Id");
		Node node = xmldoc.createTextNode(contratto.getPg_contratto()==null?"":contratto.getPg_contratto().toString());
		elementid.appendChild(node);
		element.appendChild(elementid);
		
		elementid = xmldoc.createElement(prefix+"Esercizio");
		node = xmldoc.createTextNode(contratto.getEsercizio()==null?"":contratto.getEsercizio().toString());
		elementid.appendChild(node);
		element.appendChild(elementid);
	
		elementid = xmldoc.createElement(prefix+"Stato");
		node = xmldoc.createTextNode(contratto.getStato()==null?"":contratto.getStato());
		elementid.appendChild(node);
		element.appendChild(elementid);
	
		
		elementid = xmldoc.createElement(prefix+"Uo");
		node = xmldoc.createTextNode(contratto.getCd_unita_organizzativa()==null?"":contratto.getCd_unita_organizzativa());
		elementid.appendChild(node);
		element.appendChild(elementid);
		
		ass_contr_uo=new Ass_contratto_uoBulk(); 
		ass_contr_uo.setContratto(contratto);
		CompoundFindClause clauses = new CompoundFindClause();
		clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, ass_contr_uo.getEsercizio());
		clauses.addClause("AND", "stato_contratto", SQLBuilder.EQUALS, contratto.getStato());
		clauses.addClause("AND", "pg_contratto", SQLBuilder.EQUALS, ass_contr_uo.getPg_contratto());
		List ass = homeCache.getHome(Ass_contratto_uoBulk.class).findByClause(clauses);
		for(Iterator i=ass.iterator();i.hasNext();){
			ass_contr_uo=(Ass_contratto_uoBulk)i.next();
			elementid = xmldoc.createElement(prefix+"Uo");
			node = xmldoc.createTextNode(ass_contr_uo.getCd_unita_organizzativa()==null?"":ass_contr_uo.getCd_unita_organizzativa());
			elementid.appendChild(node);
			element.appendChild(elementid);	
		}
		elementid = xmldoc.createElement(prefix+"Oggetto");
		node = xmldoc.createTextNode(contratto.getOggetto()==null?"":RemoveAccent.convert(contratto.getOggetto()).replace('"',' ' ));
		elementid.appendChild(node);
		element.appendChild(elementid);
		
		elementid = xmldoc.createElement(prefix+"FiguraEsterna");
		Element elementidt = xmldoc.createElement(prefix+"CodiceTerzo");
		node = xmldoc.createTextNode(contratto.getFig_giur_est()==null?"":contratto.getFig_giur_est().toString());
		elementidt.appendChild(node);
		elementid.appendChild(elementidt);
		element.appendChild(elementid);
		
		elementidt = xmldoc.createElement(prefix+"Denominazione");
		if(contratto.getFig_giur_est()!=null){
			terzo=new V_anagrafico_terzoBulk(contratto.getFig_giur_est()); 
			terzo = (V_anagrafico_terzoBulk)homeCache.getHome(V_anagrafico_terzoBulk.class).findByPrimaryKey(terzo);
		}else{
			terzo=new V_anagrafico_terzoBulk();
		}
		node = xmldoc.createTextNode(terzo.getRagione_sociale()==null?(terzo.getCognome()==null?"":terzo.getCognome()).concat(" ").concat((terzo.getNome()==null?"":terzo.getNome())):terzo.getRagione_sociale());
		
		elementidt.appendChild(node);
		elementid.appendChild(elementidt);
		element.appendChild(elementid);
	
		if(contratto.getFirmatario()!=null){
			elementid = xmldoc.createElement(prefix+"Firmatario");
			elementidt = xmldoc.createElement(prefix+"CodiceTerzo");
			if(contratto.getFirmatario()!=null && contratto.getCd_terzo_firmatario()!=null)
				node = xmldoc.createTextNode(contratto.getCd_terzo_firmatario()==null?"":contratto.getCd_terzo_firmatario().toString());
			else
				node = xmldoc.createTextNode("");
			elementidt.appendChild(node);
			elementid.appendChild(elementidt);
			element.appendChild(elementid);
			
			elementidt = xmldoc.createElement(prefix+"Denominazione");
			if(contratto.getFirmatario()!=null && contratto.getCd_terzo_firmatario()!=null){
				terzo=new V_anagrafico_terzoBulk(contratto.getCd_terzo_firmatario()); 
				terzo = (V_anagrafico_terzoBulk)homeCache.getHome(V_anagrafico_terzoBulk.class).findByPrimaryKey(terzo);
			}else{
				terzo=new V_anagrafico_terzoBulk();
			}
			node = xmldoc.createTextNode(terzo.getRagione_sociale()==null?(terzo.getCognome()==null?"":terzo.getCognome()).concat(" ").concat((terzo.getNome()==null?"":terzo.getNome())):terzo.getRagione_sociale());
			elementidt.appendChild(node);
			elementid.appendChild(elementidt);
			element.appendChild(elementid);
		}
		if(contratto.getResponsabile()!=null){
			elementid = xmldoc.createElement(prefix+"Responsabile");
			elementidt = xmldoc.createElement(prefix+"CodiceTerzo");
			if(contratto.getResponsabile()!=null && contratto.getCd_terzo_resp()!=null)
				node = xmldoc.createTextNode(contratto.getCd_terzo_resp()==null?"":contratto.getCd_terzo_resp().toString());
			else
				node = xmldoc.createTextNode("");
			elementidt.appendChild(node);
			elementid.appendChild(elementidt);
			element.appendChild(elementid);
			
			elementidt = xmldoc.createElement(prefix+"Denominazione");
			if(contratto.getResponsabile()!=null && contratto.getCd_terzo_resp()!=null){
				terzo=new V_anagrafico_terzoBulk(contratto.getCd_terzo_resp()); 
				terzo = (V_anagrafico_terzoBulk)homeCache.getHome(V_anagrafico_terzoBulk.class).findByPrimaryKey(terzo);
			}else{
				terzo=new V_anagrafico_terzoBulk();
			}
			node = xmldoc.createTextNode(terzo.getRagione_sociale()==null?(terzo.getCognome()==null?"":terzo.getCognome()).concat(" ").concat((terzo.getNome()==null?"":terzo.getNome())):terzo.getRagione_sociale());
			elementidt.appendChild(node);
			elementid.appendChild(elementidt);
			element.appendChild(elementid);
		}
		elementid = xmldoc.createElement(prefix+"Protocollo");
		if(contratto.getEsercizio_protocollo()!=null){
			elementidt = xmldoc.createElement(prefix+"EsercizioProtocollo");
			node = xmldoc.createTextNode(contratto.getEsercizio_protocollo()==null?"":contratto.getEsercizio_protocollo().toString());
			elementidt.appendChild(node);
			elementid.appendChild(elementidt);
			element.appendChild(elementid);
		}
		if(contratto.getCd_protocollo_generale()!=null){
			elementidt = xmldoc.createElement(prefix+"CodProtocolloGenerale");
			node = xmldoc.createTextNode(contratto.getCd_protocollo_generale()==null?"":contratto.getCd_protocollo_generale());	
			elementidt.appendChild(node);
			elementid.appendChild(elementidt);
			element.appendChild(elementid);
		}
		java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
	    if(contratto.getDt_stipula()!=null){
			elementid = xmldoc.createElement(prefix+"DataStipula");
			node = xmldoc.createTextNode(contratto.getDt_stipula()==null?"": sdf.format(contratto.getDt_stipula().getTime()));
			elementid.appendChild(node);
			element.appendChild(elementid);
	    }
		if(contratto.getDt_inizio_validita()!=null){
			elementid = xmldoc.createElement(prefix+"DataInizioValidita");
			node = xmldoc.createTextNode(contratto.getDt_inizio_validita()==null?"": sdf.format(contratto.getDt_inizio_validita().getTime()));
			elementid.appendChild(node);
			element.appendChild(elementid);
		}
		if(contratto.getDt_fine_validita()!=null){
			elementid = xmldoc.createElement(prefix+"DataFineValidita");
			node = xmldoc.createTextNode(contratto.getDt_fine_validita()==null?"": sdf.format(contratto.getDt_fine_validita().getTime()));
			elementid.appendChild(node);
			element.appendChild(elementid);
		}
		if(contratto.getIm_contratto_attivo()!=null){
			elementid = xmldoc.createElement(prefix+"ImpAttivo");
			node = xmldoc.createTextNode(contratto.getIm_contratto_attivo()==null?"":  contratto.getIm_contratto_attivo().toString());
			elementid.appendChild(node);
			element.appendChild(elementid);
		}
		if(contratto.getIm_contratto_passivo()!=null){
			elementid = xmldoc.createElement(prefix+"ImpPassivo");
			node = xmldoc.createTextNode(contratto.getIm_contratto_passivo()==null?"": contratto.getIm_contratto_passivo().toString());
			elementid.appendChild(node);
			element.appendChild(elementid);
		}
		return element;
	}
	catch (EJBException e1) {
	}
	catch (SQLException e1) {
	} catch (PersistencyException e) {		
	}
	
	 finally {
			   if (conn!=null)
				  try{conn.close();}catch( java.sql.SQLException e ){};
	}
	return null;
}
public void generaXML(PageContext pagecontext) throws IOException, ServletException{
	try {
			Parametri_enteBulk parametriEnte=null;
	    	java.sql.Connection conn=null;
	    	try{
	    		conn= EJBCommonServices.getConnection();
	    		
	    		it.cnr.jada.persistency.sql.HomeCache homeCache = new it.cnr.jada.persistency.sql.HomeCache(conn);
	    		parametriEnte = (Parametri_enteBulk)homeCache.getHome(Parametri_enteBulk.class).findAll().get(0);
	    	}catch (EJBException e1) {
	    	}
	    	catch (SQLException e1) {
	    	} catch (PersistencyException e) {		
	    	}
	    	
	    	 finally {
	    			   if (conn!=null)
	    				  try{conn.close();}catch( java.sql.SQLException e ){};
	    	}
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    DOMImplementation impl = builder.getDOMImplementation();
		    Document xmldoc=null;
	    	Element root =null;
			if (parametriEnte.getTipo_db().compareTo(Parametri_enteBulk.DB_PRODUZIONE)==0){

		    	xmldoc = impl.createDocument("https://contab.cnr.it/SIGLA/schema/selezioneContratti",prefix+"SelezioneContratti", null);
		    	root = xmldoc.getDocumentElement();
		    	root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation", "https://contab.cnr.it/SIGLA/schema/selezioneContratti selezioneContratti.xsd");
	    	}
	    	else
	    	{
	    		xmldoc = impl.createDocument("https://formcontab.cedrc.cnr.it/SIGLA/schema/selezioneContratti",prefix+"SelezioneContratti", null);
		    	root = xmldoc.getDocumentElement();
		    	root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation", "https://formcontab.cedrc.cnr.it/SIGLA/schema/selezioneContratti selezioneContratti.xsd");
	    	}
	    	java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");	
	    	Element elementTime = xmldoc.createElement(prefix+"timestamp");
	    	Node nodeTime = xmldoc.createTextNode( sdf.format(java.util.GregorianCalendar.getInstance().getTime()));
	    	elementTime.appendChild(nodeTime);
	    	root.appendChild(elementTime);
	    	root.appendChild(generaNumeroContratti(xmldoc));
	    	root.appendChild(generaRichiesta(xmldoc));
	    	if (codiceErrore!= null){
	    		root.appendChild(generaErrore(xmldoc));
	    	}else{
	    		int nr = 0;
	    			if (getContratti() != null && !getContratti().isEmpty()){
	    				Element element = xmldoc.createElement(prefix+"Contratti");
	    				root.appendChild(element);	
			    		for (Iterator i = getContratti().iterator();i.hasNext()&& nr<getNum()+getDaNum();){
			    			nr++;
			    			ContrattoBulk contratto = (ContrattoBulk)i.next();
			    			if(nr>=getDaNum())
			    				element.appendChild(generaDettaglioContratto( xmldoc,contratto));
			    			
			    		}
		    		}else{
		    			Element element = xmldoc.createElement(prefix+"Contratti");
	    				root.appendChild(element);	
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
	} catch (ParserConfigurationException|TransformerException|BusinessProcessException e) {
		logger.error("GeneraXML error -> ", e);
	}
}
	
private Node generaRichiesta(Document xmldoc) {
		
	Element e = xmldoc.createElement(prefix+"Richiesta");
	
	if(getUo()!=null){
		for(int s=0; s<getUo().length; s++){
	    	 String uo_sel = getUo()[s];
	    	 Element elementUo = xmldoc.createElement(prefix+"Uo");
	    	 Node nodeUo = xmldoc.createTextNode(uo_sel);
	    	 elementUo.appendChild(nodeUo);
	    	 e.appendChild(elementUo);
		}
	 }
	 Element elementOg;
	 Node nodeog;
	 if(getOggetto()!=null){
		elementOg = xmldoc.createElement(prefix+"Oggetto");
	 	nodeog = xmldoc.createTextNode(getOggetto());
	 	elementOg.appendChild(nodeog);
	 	e.appendChild(elementOg);
	 }
	 if(getGiuridica()!=null){
		 Element elementdaN = xmldoc.createElement(prefix+"Giuridica");
	  	 Node nodeDaNum = xmldoc.createTextNode(getGiuridica()==null?"":getGiuridica());
	  	 elementdaN.appendChild(nodeDaNum);
	  	 e.appendChild(elementdaN);
	 }
  	 if(getStato()!=null){
		 Element elementSt = xmldoc.createElement(prefix+"Stato");
		 Node nodeSt = xmldoc.createTextNode(getStato()==null?"":getStato());
		 elementSt.appendChild(nodeSt);
		 e.appendChild(elementSt);
  	 }
	 if(getEsercizio_da()!=null){
		 Element elementId = xmldoc.createElement(prefix+"Esercizio_da");
		 Node nodeId = xmldoc.createTextNode(getEsercizio_da()==null?"":getEsercizio_da().toString());
		 elementId.appendChild(nodeId);
		 e.appendChild(elementId);
	 }
	 if(getEsercizio_a()!=null){
		 Element elementId = xmldoc.createElement(prefix+"Esercizio_a");
		 Node nodeId = xmldoc.createTextNode(getEsercizio_a()==null?"":getEsercizio_a().toString());
		 elementId.appendChild(nodeId);
		 e.appendChild(elementId);
	 }
	 if(getId()!=null){
		 Element elementId = xmldoc.createElement(prefix+"Id");
		 Node nodeId = xmldoc.createTextNode(getId()==null?"":getId().toString());
		 elementId.appendChild(nodeId);
		 e.appendChild(elementId);
	 }
	 if(getEsercizio()==null){
		 if(getDaNum()!=null){
			 Element elementdaN = xmldoc.createElement(prefix+"DaNum");
			 Node nodeDaNum = xmldoc.createTextNode(getDaNum()==null?"":getDaNum().toString());
			 elementdaN.appendChild(nodeDaNum);
			 e.appendChild(elementdaN);
		 }
		 if(getNum()!=null){
			 Element elementN = xmldoc.createElement(prefix+"Num");
			 Node nodenum = xmldoc.createTextNode(getNum()==null?"":getNum().toString());
			 elementN.appendChild(nodenum);
			 e.appendChild(elementN);
		 }
	 }
	 if(getEsercizio()!=null){
		 Element elementN = xmldoc.createElement(prefix+"Esercizio");
		 Node nodenum = xmldoc.createTextNode(getEsercizio()==null?"":getEsercizio().toString());
		 elementN.appendChild(nodenum);
		 e.appendChild(elementN);
	 }
	 return e;	
}

public void eseguiRicerca(it.cnr.jada.action.ActionContext context)throws BusinessProcessException{
try{
  if(esercizio==null){// nel caso diverso da null viene fatta la richiesta per chiave (esercizio,id)
	if(uo== null){
		codiceErrore = Constants.ERRORE_CON_201;
		descErroreDett =" - UO";
		return;
	}
	if(oggetto== null&& giuridica==null){
		codiceErrore = Constants.ERRORE_CON_201;
		descErroreDett =" - Oggetto o Figura Giuridica";
		return;
	}
	if(stato!= null){
			if (!(stato.compareTo(ContrattoBulk.STATO_CESSSATO)==0 ||
				stato.compareTo(ContrattoBulk.STATO_DEFINITIVO)==0 ||
				stato.compareTo(ContrattoBulk.STATO_PROVVISORIO)==0 )) {
			codiceErrore = Constants.ERRORE_CON_202;
			descErroreDett =" - Stato del contratto";
			return;
			}
	}
	if(esercizio_da!= null){
		Integer esercizio=new Integer(esercizio_da);
		if (esercizio <1900){
			codiceErrore = Constants.ERRORE_CON_202;
			descErroreDett =" - Esercizio Da";
			return;
		}
	}else 
		setEsercizio_da(1900);
	if(esercizio_a!= null){
		Integer esercizio=new Integer(esercizio_a);
		if (esercizio >2999){
			codiceErrore = Constants.ERRORE_CON_202;
			descErroreDett =" - Esercizio A";
			return;
		}
	}else
		setEsercizio_a(2999);
	if(id!= null){
		Long i=new Long(id);
		if (i <1){
			codiceErrore = Constants.ERRORE_CON_202;
			descErroreDett =" - Id";
			return;
		}
	}
	if(num!= null){
		Integer i=new Integer(num);
		if (i <1){
			codiceErrore = Constants.ERRORE_CON_202;
			descErroreDett =" - Numero risultati";
			return;
		}
	}else
		setNum(20);
	if(daNum!= null){
		Integer i=new Integer(daNum);
		if (i <1){
			codiceErrore = Constants.ERRORE_CON_202;
			descErroreDett =" - dal Numero";
			return;
		}
	}else 
	    setDaNum(1);
}else{
	Integer es=new Integer(esercizio);
	if (es >2999||es<1900){
		codiceErrore = Constants.ERRORE_CON_202;
		descErroreDett =" - Esercizio";
		return;
	}
	if(id!= null){
		Long i=new Long(id);
		if (i <1){
			codiceErrore = Constants.ERRORE_CON_202;
			descErroreDett =" - Id";
			return;
		}
	}else{
		codiceErrore = Constants.ERRORE_CON_201;
		descErroreDett =" - Id";
		return;
	}	
	 setDaNum(1);
	 setNum(1);
  }
		RicercaContrattoBulk bulk=new RicercaContrattoBulk();
		bulk.setEsercizio_a(getEsercizio_a());
		bulk.setEsercizio_da(getEsercizio_da());
		bulk.setId(getId());
		bulk.setGiuridica(getGiuridica());
		bulk.setOggetto(getOggetto());
		bulk.setStato(getStato());
		bulk.setListaUo(getUo());
		bulk.setEsercizio(getEsercizio());
		if(getUo()!=null){
			for(int s=0; s<getUo().length; s++){
				String uo_sel = getUo()[s];
				try {
		    	 if(uo_sel.substring(3,4).compareTo(".")!=0){
		    		codiceErrore = Constants.ERRORE_CON_202;
		 			descErroreDett =" - Uo";
		 			return;
		    	 }
		    	 Integer test=new Integer(uo_sel.substring(0,3));
		    	 	     test=new Integer(uo_sel.substring(4,7));
				} catch (NumberFormatException e) {
					codiceErrore = Constants.ERRORE_CON_202;
		 			descErroreDett =" - Uo";
		 			return;
				}
			}
		}
		try {
			setContratti(((ContrattoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_ContrattoComponentSession",ContrattoComponentSession.class))
					.findListaContrattiSIP(context.getUserContext(false), bulk));
		} catch (ComponentException e) {
				codiceErrore = Constants.ERRORE_CON_200;
		} catch (RemoteException e) {
				codiceErrore = Constants.ERRORE_CON_200;
		}
	
	} catch (NumberFormatException e) {
		codiceErrore = Constants.ERRORE_CON_202;
	} catch (Exception e) {
		codiceErrore = Constants.ERRORE_CON_200;
	}
}

public String getOggetto() {
	return oggetto;
}

public void setOggetto(String oggetto) {
	this.oggetto = oggetto;
}


public String getStato() {
	return stato;
}

public void setStato(String stato) {
	this.stato = stato;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public Integer getNum() {
	return num;
}

public void setNum(Integer num) {
	this.num = num;
}

public Integer getDaNum() {
	return daNum;
}

public void setDaNum(Integer daNum) {
	this.daNum = daNum;
}

public String getUser() {
	return user;
}

public void setUser(String user) {
	this.user = user;
}

public Integer getCodiceErrore() {
	return codiceErrore;
}

public void setCodiceErrore(Integer codiceErrore) {
	this.codiceErrore = codiceErrore;
}

public List getContratti() {
	return Contratti;
}

public void setContratti(List contratti) {
	Contratti = contratti;
}

public String[] getUo() {
	return uo;
}

public void setUo(String[] uo) {
	this.uo = uo;
}

public String getDescErroreDett() {
	return descErroreDett;
}

public void setDescErroreDett(String descErroreDett) {
	this.descErroreDett = descErroreDett;
}

public String getGiuridica() {
	return giuridica;
}

public void setGiuridica(String giuridica) {
	this.giuridica = giuridica;
}

public Integer getEsercizio_da() {
	return esercizio_da;
}

public void setEsercizio_da(Integer esercizio_da) {
	this.esercizio_da = esercizio_da;
}

public Integer getEsercizio_a() {
	return esercizio_a;
}

public void setEsercizio_a(Integer esercizio_a) {
	this.esercizio_a = esercizio_a;
}

public Integer getEsercizio() {
	return esercizio;
}

public void setEsercizio(Integer esercizio) {
	this.esercizio = esercizio;
}


}