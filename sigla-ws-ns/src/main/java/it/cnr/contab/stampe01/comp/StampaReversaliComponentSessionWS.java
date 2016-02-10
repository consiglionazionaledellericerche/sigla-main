package it.cnr.contab.stampe01.comp;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
@WebService( name="StampaReversaliComponentWS",targetNamespace="http://contab.cnr.it/SIGLA")
@Remote

@SOAPBinding(use=SOAPBinding.Use.LITERAL)
public interface StampaReversaliComponentSessionWS {
	@WebMethod  @WebResult(targetNamespace="http://contab.cnr.it/SIGLA/",name="result")
//    @WebMethod @WebResult(name="result") 
	byte[] DownloadReversale(
			 @WebParam (name="user") String user,
			 @WebParam (name="cds") String cds,
			 @WebParam (name="esercizio") String esercizio,
			 @WebParam (name="pgReversale") String pgReversale);
}
