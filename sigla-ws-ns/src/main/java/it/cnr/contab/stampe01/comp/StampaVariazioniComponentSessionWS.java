package it.cnr.contab.stampe01.comp;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
@WebService( name="StampaVariazioniComponentWS",targetNamespace="http://contab.cnr.it/SIGLA/")
@Remote

@SOAPBinding(use=SOAPBinding.Use.LITERAL)
public interface StampaVariazioniComponentSessionWS {
	
	@WebMethod @WebResult(name="result") byte[] DownloadVariazione(
			 @WebParam (name="user") String user,
			 @WebParam (name="esercizio") String esercizio,
			 @WebParam (name="pgVariazione") String pgVariazione,
			 @WebParam (name="tipoVariazione") String tipoVariazione);
}
