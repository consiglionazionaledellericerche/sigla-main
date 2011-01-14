package it.cnr.contab.anagraf00.comp;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.soap.SOAPFaultException;

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.client.docamm.Nazione;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;

import it.cnr.contab.utenze00.bp.Costanti;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import javax.annotation.security.DeclareRoles;
import org.jboss.ws.annotation.EndpointConfig;
import org.jboss.wsf.spi.annotation.WebContext;
/**
 * Questa classe svolge le operazioni fondamentali di lettura, scrittura e filtro dei dati
 * immessi o richiesti dall'utente. In oltre sovrintende alla gestione e creazione dati a cui
 * l'utente stesso non ha libero accesso e/o non gli sono trasparenti.
 */
@Stateless
@WebService(endpointInterface="it.cnr.contab.anagraf00.ejb.NazioneComponentSessionWS")
@XmlSeeAlso({java.util.ArrayList.class}) 
@DeclareRoles({"WSUserRole","IITRole"})
// annotation proprietarie di JBoss, purtroppo in JBoss 4.2.2 non funzionano i corrispondenti tag in jboss.xml
@EndpointConfig(configName = "Standard WSSecurity Endpoint")
@WebContext(contextRoot="/SIGLA-SIGLAEJB")

public class NazioneComponentWS  {
	
public  NazioneComponentWS() {
}

public java.util.ArrayList<Nazione> cercaNazione(
		  String query,
		  String dominio,
		  Integer numMax,
		  String user,
		  String ricerca) throws Exception{
try {
	java.util.ArrayList<Nazione> listaNazione=new ArrayList<Nazione>();
	List lista=null;
	if(user== null)
		user="IIT";
	if(ricerca== null)
		ricerca="selettiva";
	 if(numMax==null)
		 numMax=20;
	
	UserContext userContext = new WSUserContext(user,null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
	
	if(query== null){
		throw new SOAPFaultException(faultQueryNonDefinita());
	}
	else if(dominio== null||(!dominio.equalsIgnoreCase("codice")&&!dominio.equalsIgnoreCase("descrizione"))){
		throw new SOAPFaultException(faultDominioNonDefinito());
	}else{
		try {
			lista=(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).findListaNazioneWS(userContext,query,dominio, ricerca));
		} catch (ComponentException e) {
			throw new SOAPFaultException(faultGenerico());
		} catch (RemoteException e) {
			throw new SOAPFaultException(faultGenerico());
		}
	} 
	
		int num = 0;
		if (lista != null && !lista.isEmpty()){
    		for (Iterator i = lista.iterator();i.hasNext()&&num< new Integer(numMax).intValue();){
    			NazioneBulk oggetto_db = (NazioneBulk)i.next();
    			Nazione oggetto=new Nazione();
    			oggetto.setCodice(oggetto_db.getCd_catastale());
    			oggetto.setDescrizione(oggetto_db.getDs_nazione());
    			oggetto.setId(oggetto_db.getPg_nazione());
    			listaNazione.add(oggetto);
    			num++;
    		} 
		}
		return listaNazione;
	}catch (SOAPFaultException e) {
		throw e;
	} 
	catch (Exception e) {
		throw new SOAPFaultException(faultGenerico());
	}
}

private SOAPFault faultGenerico() throws SOAPException {
	return generaFault(new String(Costanti.ERRORE_WS_100.toString()),
			Costanti.erroriWS.get(Costanti.ERRORE_WS_100));
}
private SOAPFault faultQueryNonDefinita() throws SOAPException {
	return generaFault(new String(Costanti.ERRORE_WS_101.toString()),
			Costanti.erroriWS.get(Costanti.ERRORE_WS_101));
}

private SOAPFault faultDominioNonDefinito() throws SOAPException {
	return generaFault(new String(Costanti.ERRORE_WS_102.toString()),
			Costanti.erroriWS.get(Costanti.ERRORE_WS_102));
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
