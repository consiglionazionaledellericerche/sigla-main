package it.cnr.contab.stampe00.comp;

import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.FatturaNonProtocollataException;
import it.cnr.jada.comp.FatturaNonTrovataException;
import it.cnr.jada.comp.GenerazioneReportException;
import it.cnr.jada.persistency.PersistencyException;

import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.soap.*;
import javax.xml.ws.soap.SOAPFaultException;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import org.jboss.ws.annotation.EndpointConfig;
import org.jboss.wsf.spi.annotation.WebContext;


@Stateless
@WebService(endpointInterface="it.cnr.contab.stampe00.comp.StampaComponentSessionWS")
@DeclareRoles({"WSUserRole","IITRole"})
// annotation proprietarie di JBoss, purtroppo in JBoss 4.2.2 non funzionano i corrispondenti tag in jboss.xml
@EndpointConfig(configName = "Standard WSSecurity Endpoint")
@WebContext(contextRoot="/SIGLA-SIGLAEJB")
public class StampaComponentWS {
	
@RolesAllowed({"WSUserRole","IITRole"})
public byte[] DownloadFattura(String user,String esercizio,String cds,String uo,String pg) throws NumberFormatException, PersistencyException, ComponentException, RemoteException, EJBException, Exception {
    	UserContext userContext = new WSUserContext(user,null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
    	 if(cds==null||uo==null||pg==null||esercizio==null)
    		 throw new SOAPFaultException(faultChiaveFatturaNonCompleta());
		try{	
			String FName = ((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).lanciaStampa(userContext, new Long(esercizio), cds, uo, new Long(pg));
			File f = new File(FName);
			byte[] file = new byte[(int) f.length()];
			FileInputStream fileInputStream = new FileInputStream(f);
			fileInputStream.read(file);
		   return file;
		}catch(GenerazioneReportException e){
			throw new SOAPFaultException(faultGenerazioneStampa());
		}catch(FatturaNonTrovataException e){
			throw new SOAPFaultException(faultFatturaNonTrovata());
		}catch(FatturaNonProtocollataException e){
			throw new SOAPFaultException(faultFatturaNonProtocollata());
		}
  }
	private SOAPFault faultChiaveFatturaNonCompleta() throws SOAPException{
		return generaFault("001","Identificativo Fattura non valido e/o incompleto");
	}
	private SOAPFault faultFatturaNonTrovata() throws SOAPException{
		return generaFault("002","Fattura non trovata");
	}
	private SOAPFault faultFatturaNonProtocollata() throws SOAPException{
		return generaFault("003","Fattura non Protocollata");
	}
	private SOAPFault faultGenerazioneStampa() throws SOAPException{
		return generaFault("004","Generazione stampa non riuscita");
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

