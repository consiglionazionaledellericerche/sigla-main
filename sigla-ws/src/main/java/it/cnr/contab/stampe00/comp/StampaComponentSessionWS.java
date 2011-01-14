package it.cnr.contab.stampe00.comp;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.WebFault;
@WebService( name="StampaComponentWS",targetNamespace="http://contab.cnr.it/SIGLA")
@Remote

@SOAPBinding(style=Style.RPC, use=SOAPBinding.Use.LITERAL)
public interface StampaComponentSessionWS {
	  
	 @WebMethod @WebResult(name="result") byte[] DownloadFattura(
			 @WebParam (name="user") String user,
			 @WebParam (name="esercizio") String esercizio,
			 @WebParam (name="cds") String cds,
			 @WebParam (name="uo") String uo,
			 @WebParam (name="pg") String pg) ;
}
