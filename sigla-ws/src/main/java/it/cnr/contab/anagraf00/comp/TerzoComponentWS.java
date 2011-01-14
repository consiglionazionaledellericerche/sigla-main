package it.cnr.contab.anagraf00.comp;

import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.soap.SOAPFaultException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import it.cnr.contab.anagraf00.core.bulk.V_terzo_anagrafico_sipBulk;
import it.cnr.contab.client.docamm.Terzo;
import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import org.jboss.ws.annotation.EndpointConfig;
import org.jboss.wsf.spi.annotation.WebContext;

/**
 * Questa classe svolge le operazioni fondamentali di lettura, scrittura e filtro dei dati
 * immessi o richiesti dall'utente. In oltre sovrintende alla gestione e creazione dati a cui
 * l'utente stesso non ha libero accesso e/o non gli sono trasparenti.
 */
@Stateless
@WebService(endpointInterface="it.cnr.contab.anagraf00.ejb.TerzoComponentSessionWS")
@XmlSeeAlso({java.util.ArrayList.class}) 
@DeclareRoles({"WSUserRole","IITRole"})
// annotation proprietarie di JBoss, purtroppo in JBoss 4.2.2 non funzionano i corrispondenti tag in jboss.xml
@EndpointConfig(configName = "Standard WSSecurity Endpoint")
@WebContext(contextRoot="/SIGLA-SIGLAEJB")
public class TerzoComponentWS  {


public  TerzoComponentWS() {
}
/*
public String inserisciTerzo(
		  String tipoterzo,
		  String numMax,
		  String user,
		  String via,
		  String civico,
		  String cap,
		  String nazione,
		  String comune,
		  String ragione_sociale,
		  String partita_iva,
		  String cognome,
		  String nome,
		  String codice_fiscale,
		  String data_nascita,
		  String nazione_nascita,
		  String comune_nascita,
		  String sesso)
{
	if(user== null)
		codiceErrore = Constants.ERRORE_SIP_110;
	if(tipoterzo== null||(tipoterzo.equalsIgnoreCase("fisica")&&!tipoterzo.equalsIgnoreCase("giuridica")))
		codiceErrore = Constants.ERRORE_SIP_104;
	if(via==null || civico== null || cap == null ||nazione == null || comune == null)
		codiceErrore = Constants.ERRORE_SIP_107;
	if (tipoterzo.equalsIgnoreCase("fisica")){
		if(cognome==null||nome==null||codice_fiscale==null||data_nascita==null||nazione_nascita==null||comune_nascita==null||sesso==null)
			codiceErrore = Constants.ERRORE_SIP_107;
	}
	if (tipoterzo.equalsIgnoreCase("giuridica")){
		if(ragione_sociale==null || partita_iva== null)
			codiceErrore = Constants.ERRORE_SIP_107;
	}
	AnagraficoBulk anagrafico = new AnagraficoBulk();
	
	UserContext userContext = new WSUserContext(user,null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);	
	try {
		anagrafico = (AnagraficoBulk)((AnagraficoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRANAGRAF00_EJB_AnagraficoComponentSession",AnagraficoComponentSession.class)).inizializzaBulkPerInserimento(userContext,anagrafico);
	} catch (ComponentException e1) {
		codiceErrore = Constants.ERRORE_SIP_100;
	} catch (RemoteException e1) {
		codiceErrore = Constants.ERRORE_SIP_100;
	}
	anagrafico.setVia_fiscale(via);
	anagrafico.setNum_civico_fiscale(civico);
	anagrafico.setCap_comune_fiscale(cap);
	anagrafico.setNazionalita(new NazioneBulk(new Long(nazione)));
	anagrafico.setComune_fiscale(new ComuneBulk(new Long(comune)));
	anagrafico.setFl_occasionale(Boolean.FALSE);
	anagrafico.setFl_fatturazione_differita(Boolean.FALSE);
	anagrafico.setTi_entita_persona_struttura(AnagraficoBulk.ENTITA_PERSONA);
	anagrafico.setTi_entita(tipoterzo.equalsIgnoreCase("fisica")?AnagraficoBulk.FISICA:AnagraficoBulk.GIURIDICA);
	if (tipoterzo.equalsIgnoreCase("fisica")){
		anagrafico.setTi_entita_fisica(AnagraficoBulk.ALTRO);
		anagrafico.setFl_soggetto_iva(Boolean.FALSE);
		anagrafico.setCognome(cognome);
		anagrafico.setNome(nome);
		anagrafico.setCodice_fiscale(codice_fiscale.toUpperCase());
		try {
			anagrafico.setDt_nascita(new Timestamp(new SimpleDateFormat("yyyy/MM/dd").parse(data_nascita).getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		anagrafico.setComune_nascita(new ComuneBulk(new Long(comune_nascita)));
		anagrafico.setTi_sesso(sesso);			
	}else if (tipoterzo.equalsIgnoreCase("giuridica")){
		anagrafico.setTi_entita_giuridica(AnagraficoBulk.ALTRO);
		anagrafico.setFl_soggetto_iva(Boolean.TRUE);
		anagrafico.setRagione_sociale(ragione_sociale);
		anagrafico.setPartita_iva(partita_iva);
	}
	anagrafico.setToBeCreated();
	try {
		
		setTerzi(((AnagraficoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRANAGRAF00_EJB_AnagraficoComponentSession",AnagraficoComponentSession.class)).bulkForSIP(userContext,anagrafico));
	} catch (ComponentException e) {
		if (e.getDetail() instanceof DuplicateKeyException)
			codiceErrore = Constants.ERRORE_SIP_106;
		else
			codiceErrore = Constants.ERRORE_SIP_105;
	} catch (RemoteException e) {
		codiceErrore = Constants.ERRORE_SIP_100;
	}
	try {
		return  generaXML(tipoterzo,numMax);
	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (TransformerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 		
	return null;
}



public String eliminaTerzo(
		  String tipoterzo,
		  String numMax,
		  String user,
		  String cd_terzo){
	if(tipoterzo== null||(!tipoterzo.equalsIgnoreCase("fisica")&&!tipoterzo.equalsIgnoreCase("giuridica")))
		codiceErrore = Constants.ERRORE_SIP_104;
			
	if(cd_terzo==null)
		codiceErrore = Constants.ERRORE_SIP_107;

	UserContext userContext = new WSUserContext(user,null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);	
	try {
		((AnagraficoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRANAGRAF00_EJB_AnagraficoComponentSession",AnagraficoComponentSession.class)).eliminaBulkForSIP(userContext,cd_terzo);
		setTerzi(new BulkList());
	} catch (ComponentException e) {
		if (e instanceof TerzoNonPresenteSIPException)
			codiceErrore = Constants.ERRORE_SIP_108;
		else
			codiceErrore = Constants.ERRORE_SIP_109;
	} catch (RemoteException e) {
		codiceErrore = Constants.ERRORE_SIP_100;
	}	
	try {
		return generaXML(tipoterzo,numMax);
	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (TransformerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}
public String modificaTerzo(
		  String tipoterzo,
		  String numMax,
		  String user,
		  String via,
		  String civico,
		  String cap,
		  String nazione,
		  String comune,
		  String ragione_sociale,
		  String partita_iva,
		  String cognome,
		  String nome,
		  String codice_fiscale,
		  String data_nascita,
		  String nazione_nascita,
		  String comune_nascita,
		  String sesso,
		  String cd_terzo){

	if(tipoterzo== null||(!tipoterzo.equalsIgnoreCase("fisica")&&!tipoterzo.equalsIgnoreCase("giuridica")))
		codiceErrore = Constants.ERRORE_SIP_104;
		
	if(cd_terzo==null)
		codiceErrore = Constants.ERRORE_SIP_107;
	
	UserContext userContext = new WSUserContext(user,null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);	
	TerzoBulk terzo = new TerzoBulk(new Integer(cd_terzo));
	try {
	List l =Utility.createTerzoComponentSession().findListaTerziSIP(userContext,cd_terzo,"cd_terzo",tipoterzo,"selettiva");
	if (l == null ||l.isEmpty()||l.size() != 1)
		codiceErrore = Constants.ERRORE_SIP_108;
	terzo = (TerzoBulk)l.get(0);
	} catch (ComponentException e1) {
		codiceErrore = Constants.ERRORE_SIP_100;
	} catch (RemoteException e1) {
		codiceErrore = Constants.ERRORE_SIP_100;
	}
	if(via==null || civico== null || cap == null ||nazione == null || comune == null)
		codiceErrore = Constants.ERRORE_SIP_107;

	if (tipoterzo.equalsIgnoreCase("fisica")){
		if(cognome==null||nome== null||codice_fiscale==null||data_nascita==null||nazione_nascita==null||comune_nascita==null||sesso==null)
			codiceErrore = Constants.ERRORE_SIP_107;
	}
	if (tipoterzo.equalsIgnoreCase("giuridica")){
		if(ragione_sociale==null || partita_iva== null)
			codiceErrore = Constants.ERRORE_SIP_107;
	}
	AnagraficoBulk anagrafico = terzo.getAnagrafico();
	try {
		anagrafico = (AnagraficoBulk)((AnagraficoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRANAGRAF00_EJB_AnagraficoComponentSession",AnagraficoComponentSession.class)).inizializzaBulkPerModifica(userContext,anagrafico);
	} catch (ComponentException e1) {
		codiceErrore = Constants.ERRORE_SIP_100;
	} catch (RemoteException e1) {
		codiceErrore = Constants.ERRORE_SIP_100;
	}
	anagrafico.setVia_fiscale(via);
	anagrafico.setNum_civico_fiscale(civico);
	anagrafico.setCap_comune_fiscale(cap);
	anagrafico.setNazionalita(new NazioneBulk(new Long(nazione)));
	anagrafico.setComune_fiscale(new ComuneBulk(new Long(comune)));
	anagrafico.getComune_fiscale().setNazione(anagrafico.getNazionalita());
	anagrafico.setFl_occasionale(Boolean.FALSE);
	anagrafico.setFl_fatturazione_differita(Boolean.FALSE);
	anagrafico.setTi_entita_persona_struttura(AnagraficoBulk.ENTITA_PERSONA);
	anagrafico.setTi_entita(tipoterzo.equalsIgnoreCase("fisica")?AnagraficoBulk.FISICA:AnagraficoBulk.GIURIDICA);
	if (tipoterzo.equalsIgnoreCase("fisica")){
		anagrafico.setTi_entita_fisica(AnagraficoBulk.ALTRO);
		anagrafico.setFl_soggetto_iva(Boolean.FALSE);
		anagrafico.setCognome(cognome);
		anagrafico.setNome(nome);
		anagrafico.setCodice_fiscale(codice_fiscale.toUpperCase());
		try {
			anagrafico.setDt_nascita(new Timestamp(new SimpleDateFormat("yyyy/MM/dd").parse(data_nascita).getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		anagrafico.setComune_nascita(new ComuneBulk(new Long(comune_nascita)));
		anagrafico.setTi_sesso(sesso);			
	}else if (tipoterzo.equalsIgnoreCase("giuridica")){
		anagrafico.setTi_entita_giuridica(AnagraficoBulk.ALTRO);
		anagrafico.setFl_soggetto_iva(Boolean.TRUE);
		anagrafico.setRagione_sociale(ragione_sociale);
		anagrafico.setPartita_iva(partita_iva);
	}
	anagrafico.setToBeUpdated();
	try{
	  setTerzi(((AnagraficoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRANAGRAF00_EJB_AnagraficoComponentSession",AnagraficoComponentSession.class)).bulkForSIP(userContext,anagrafico));
	} catch (ComponentException e) {
		if (e.getDetail() instanceof DuplicateKeyException)
			codiceErrore = Constants.ERRORE_SIP_106;
		else
			codiceErrore = Constants.ERRORE_SIP_105;
	} catch (RemoteException e) {
		codiceErrore = Constants.ERRORE_SIP_100;
	}
   
	try {
		return generaXML(tipoterzo,numMax);
	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (TransformerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}
*/
@RolesAllowed({"WSUserRole","IITRole"})
public java.util.ArrayList<Terzo>  cercaTerzo(String query,
		  String dominio,
		  String tipoterzo,
		  Integer numMax,
		  String user,
		  String ricerca) throws Exception{
	List terzi=null;
	java.util.ArrayList<Terzo> listaterzi=new ArrayList<Terzo>();
	try{
	if(user== null)
		user="IIT";
	if(ricerca== null)
		ricerca="selettiva";
	 if(numMax==null)
		 numMax=20;
			 	
	
	UserContext userContext = new WSUserContext(user,null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
	if(query== null){
		throw new SOAPFaultException(faultQueryNonDefinita());
	}else if(dominio== null||(!dominio.equalsIgnoreCase("cd_terzo")&&!dominio.equalsIgnoreCase("denominazione"))){
		throw new SOAPFaultException(faultDominioNonDefinito());
	}else if(tipoterzo== null||(!tipoterzo.equalsIgnoreCase("fisica")&&!tipoterzo.equalsIgnoreCase("giuridica"))){
		throw new SOAPFaultException(faultSoggettoTerzoNonDefinito());
	}else{
		try {
			terzi=(Utility.createTerzoComponentSession().findListaTerziSIP(userContext,query,dominio,tipoterzo,ricerca));			
		} catch (ComponentException e) {
			throw new SOAPFaultException(faultGenerico());
		} catch (RemoteException e) {
			throw new SOAPFaultException(faultGenerico());
		}
	}
			int num = 0;
    		if (terzi != null && !terzi.isEmpty()){
	    		for (Iterator i = terzi.iterator();i.hasNext()&&num< new Integer(numMax).intValue();){
	    			V_terzo_anagrafico_sipBulk terzo = (V_terzo_anagrafico_sipBulk)i.next();
	    			Terzo t=new Terzo();
	    			t.setCodice(terzo.getCd_terzo());
	    			if (tipoterzo.equalsIgnoreCase("fisica")){
	    				t.setNome(terzo.getNome());
	    				t.setCognome(terzo.getCognome());
	    			}else if (tipoterzo.equalsIgnoreCase("giuridica")){
	    				t.setDenominazione(terzo.getDenominazione_sede());
	    			}
	    			t.setCodicefiscale(terzo.getCodice_fiscale_pariva());
	    			t.setPartitaiva(terzo.getCodice_fiscale_pariva());
	    			listaterzi.add(t);
	    			num++;
	    		}
    		}
			
			return listaterzi;
		}catch (SOAPFaultException e) {
			throw e;			
		} catch (Exception e) {
			throw new SOAPFaultException(faultGenerico());
		}
	}
@RolesAllowed({"WSUserRole","IITRole"})
public String  cercaTerzoXml(String query,
		  String dominio,
		  String tipoterzo,
		  String numMax,
		  String user,
		  String ricerca) throws Exception{
	List terzi=null;
	UserContext userContext = new WSUserContext(user,null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
	if(query== null){
		throw new SOAPFaultException(faultQueryNonDefinita());
	}else if(dominio== null||(!dominio.equalsIgnoreCase("cd_terzo")&&!dominio.equalsIgnoreCase("denominazione"))){
		throw new SOAPFaultException(faultDominioNonDefinito());
	}else if(tipoterzo== null||(!tipoterzo.equalsIgnoreCase("fisica")&&!tipoterzo.equalsIgnoreCase("giuridica"))){
		throw new SOAPFaultException(faultSoggettoTerzoNonDefinito());
	}else{
		try {
			terzi=(Utility.createTerzoComponentSession().findListaTerziSIP(userContext,query,dominio,tipoterzo,ricerca));			
		} catch (ComponentException e) {
			throw new SOAPFaultException(faultGenerico());
		} catch (RemoteException e) {
			throw new SOAPFaultException(faultGenerico());
		}
	}
		try {
			return generaXML(tipoterzo,numMax,terzi);
		} catch (Exception e) {
			throw new SOAPFaultException(faultGenerico());
		}
	}
private SOAPFault faultQueryNonDefinita() throws SOAPException {
	return generaFault(new String(Constants.ERRORE_SIP_101.toString()),
			Constants.erroriSIP.get(Constants.ERRORE_SIP_101));
}
private SOAPFault faultDominioNonDefinito() throws SOAPException {
	return generaFault(new String(Constants.ERRORE_SIP_102.toString()),
			Constants.erroriSIP.get(Constants.ERRORE_SIP_102));
}
private SOAPFault faultSoggettoTerzoNonDefinito() throws SOAPException {
	return generaFault(new String(Constants.ERRORE_SIP_104.toString()),
			Constants.erroriSIP.get(Constants.ERRORE_SIP_104));
		
}
private SOAPFault faultGenerico() throws SOAPException {
	return generaFault(new String(Constants.ERRORE_SIP_100.toString()),
			Constants.erroriSIP.get(Constants.ERRORE_SIP_100));
}

public String  generaXML(String tipoterzo,String numMax,List terzi) throws ParserConfigurationException, TransformerException{
		if (numMax==null)
			numMax=new Integer(20).toString();
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder builder = factory.newDocumentBuilder();
    	DOMImplementation impl = builder.getDOMImplementation();
    	Document xmldoc = impl.createDocument("http://gestioneistituti.cnr.it/cercaterzi", "cercaterzi:root", null);
    	Element root = xmldoc.getDocumentElement();
    	//if (codiceErrore!= null){
    		//root.appendChild(generaErrore(xmldoc));
    		
    	//}else{
    		root.appendChild(generaNumeroTerzi(xmldoc,terzi));
    		int num = 0;
    		if (terzi != null && !terzi.isEmpty()){
	    		for (Iterator i = terzi.iterator();i.hasNext()&&num< new Integer(numMax).intValue();){
	    			V_terzo_anagrafico_sipBulk terzo = (V_terzo_anagrafico_sipBulk)i.next();
	    			if (tipoterzo.equalsIgnoreCase("fisica")){
	    				root.appendChild(generaDettaglioTerziFisica(xmldoc,terzo.getCd_terzo(),terzo.getCognome(),terzo.getNome(),terzo.getCodice_fiscale_pariva()));
	    			}else if (tipoterzo.equalsIgnoreCase("giuridica")){
	    				root.appendChild(generaDettaglioTerziGiuridica(xmldoc,terzo.getCd_terzo(),terzo.getDenominazione_sede(),terzo.getCodice_fiscale_pariva()));
	    			}
	    			num++;
	    		}
    		}
    	//}
    	
    	DOMSource domSource = new DOMSource(xmldoc);
    	StringWriter domWriter = new StringWriter();
    	StreamResult streamResult = new StreamResult(domWriter);

    	TransformerFactory tf = TransformerFactory.newInstance();
    	Transformer serializer = tf.newTransformer();
    	serializer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
    	serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"http://150.146.206.250/DTD/cercaterzi.dtd");
    	serializer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,"cercaterzi");
    	serializer.setOutputProperty(OutputKeys.INDENT,"yes");
    	serializer.setOutputProperty(OutputKeys.STANDALONE,"no");
    	serializer.transform(domSource, streamResult);
        return domWriter.toString();
	
}
private Element generaNumeroTerzi(Document xmldoc,List terzi){
	Element e = xmldoc.createElement("cercaterzi:numris");
	Node n = xmldoc.createTextNode(new Integer(terzi.size()).toString());
	e.appendChild(n);
	return e;	
}
private Element generaDettaglioTerziGiuridica(Document xmldoc, Integer codice, String denominazione, String partitaiva){
	Element elementTerzo = xmldoc.createElement("cercaterzi:terzo");
	Element elementCodice = xmldoc.createElement("cercaterzi:codice");
	Node nodeCodice = xmldoc.createTextNode(codice.toString());
	elementCodice.appendChild(nodeCodice);
	elementTerzo.appendChild(elementCodice);

	Element elementDenominazione = xmldoc.createElement("cercaterzi:denominazione");
	Node nodeDenominazione = xmldoc.createTextNode(denominazione==null?"":denominazione);
	elementDenominazione.appendChild(nodeDenominazione);
	elementTerzo.appendChild(elementDenominazione);
	
	Element elementPartitaiva = xmldoc.createElement("cercaterzi:partitaiva");
	Node nodePartitaiva;
	if (partitaiva!= null)
		nodePartitaiva = xmldoc.createTextNode(partitaiva);
	else
		nodePartitaiva = xmldoc.createTextNode("");
	elementPartitaiva.appendChild(nodePartitaiva);
	elementTerzo.appendChild(elementPartitaiva);
	
	return elementTerzo;
}
private Element generaDettaglioTerziFisica(Document xmldoc, Integer codice, String cognome, String nome, String codicefiscale){
	Element elementTerzo = xmldoc.createElement("cercaterzi:terzo");
	Element elementCodice = xmldoc.createElement("cercaterzi:codice");
	Node nodeCodice = xmldoc.createTextNode(codice.toString());
	elementCodice.appendChild(nodeCodice);
	elementTerzo.appendChild(elementCodice);

	Element elementCognome = xmldoc.createElement("cercaterzi:cognome");
	Node nodeCognome = xmldoc.createTextNode(cognome==null?"":cognome);
	elementCognome.appendChild(nodeCognome);
	elementTerzo.appendChild(elementCognome);

	Element elementNome = xmldoc.createElement("cercaterzi:nome");
	Node nodeNome;
	if (nome!= null)
		nodeNome = xmldoc.createTextNode(nome);
	else
		nodeNome = xmldoc.createTextNode("");
	elementNome.appendChild(nodeNome);
	elementTerzo.appendChild(elementNome);
	
	Element elementCodicefiscale = xmldoc.createElement("cercaterzi:codicefiscale");
	Node nodeCodicefiscale;
	if (codicefiscale!= null)
		nodeCodicefiscale = xmldoc.createTextNode(codicefiscale);
	else
		nodeCodicefiscale = xmldoc.createTextNode("");
	elementCodicefiscale.appendChild(nodeCodicefiscale);
	elementTerzo.appendChild(elementCodicefiscale);
	
	return elementTerzo;
}
private SOAPFault generaFault(String localName,String stringFault) throws SOAPException{
	MessageFactory factory = MessageFactory.newInstance();
	SOAPMessage message = factory.createMessage(); 
	SOAPFactory soapFactory = SOAPFactory.newInstance();
	SOAPBody body = message.getSOAPBody(); 
	SOAPFault fault = body.addFault();
	Name faultName = soapFactory.createName(localName,"", SOAPConstants.URI_NS_SOAP_ENVELOPE);
	fault.setFaultCode(faultName);
	fault.setFaultString(stringFault);
	return fault;
}

}
