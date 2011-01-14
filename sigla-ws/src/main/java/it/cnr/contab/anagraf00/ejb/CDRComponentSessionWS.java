package it.cnr.contab.anagraf00.ejb;

import it.cnr.contab.client.docamm.Cdr;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.WebResult;
@WebService( name="CDRComponentWS",targetNamespace="http://contab.cnr.it/sigla")
@Remote

public interface CDRComponentSessionWS extends  java.rmi.Remote{
		
		/* @WebMethod  @WebResult(name="result") String  cercaCDRXml(
				 @WebParam (name="uo") String uo,
				 @WebParam (name="query")String query,
				 @WebParam (name="dominio") String dominio,
				 @WebParam (name="numMax") String numMax,
				 @WebParam (name="user") String user,
				 @WebParam (name="ricerca") String ricerca);
				 */
		 @WebMethod  @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<Cdr>  cercaCDR(
				 @WebParam (name="uo") String uo,
				 @WebParam (name="query")String query,
				 @WebParam (name="dominio") String dominio,
				 @WebParam (name="numMax") Integer numMax,
				 @WebParam (name="user") String user,
				 @WebParam (name="ricerca") String ricerca);
		}

