package it.cnr.contab.anagraf00.ejb;

import javax.ejb.Remote;
import it.cnr.contab.client.docamm.*;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.WebResult;
@WebService( name="TariffarioComponentWS",targetNamespace="http://contab.cnr.it/sigla")
@Remote


public interface TariffarioComponentSessionWS extends  java.rmi.Remote{
	 @WebMethod  @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<Tariffario>  cercaTariffario(
			 @WebParam (name="uo")String uo,
			 @WebParam (name="query")String query,
			 @WebParam (name="dominio") String dominio,
			 @WebParam (name="numMax") Integer numMax,
			 @WebParam (name="user") String user,
			 @WebParam (name="ricerca") String ricerca);
			 /*
		 @WebMethod  @WebResult(name="result") String  cercaTariffarioXml(
				 @WebParam (name="uo")String uo,
				 @WebParam (name="query")String query,
				 @WebParam (name="dominio") String dominio,
				 @WebParam (name="numMax") String numMax,
				 @WebParam (name="user") String user,
				 @WebParam (name="ricerca") String ricerca);
				 */
		}

