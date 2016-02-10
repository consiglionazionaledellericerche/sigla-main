package it.cnr.contab.stampe00.comp;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.WebFault;
@WebService( name="StampaComponentWS",targetNamespace="http://contab.cnr.it/sigla")
@Remote

@SOAPBinding(style=Style.RPC, use=SOAPBinding.Use.LITERAL)
public interface StampaComponentSessionWS {
	  
	 @WebMethod @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") byte[] DownloadFattura(
			 @WebParam (name="user") String user,
			 @WebParam (name="pg_stampa") Long pg_stampa) ;
	 @WebMethod @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") Long inserisciDatiPerStampa(
			 @WebParam (name="user") String user,
			 @WebParam (name="esercizio") String esercizio,
			 @WebParam (name="cds") String cds,
			 @WebParam (name="uo") String uo,
			 @WebParam (name="pg") String pg) ;
}
