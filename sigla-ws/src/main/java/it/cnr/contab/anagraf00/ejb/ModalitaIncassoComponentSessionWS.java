package it.cnr.contab.anagraf00.ejb;

import it.cnr.contab.client.docamm.ModalitaIncasso;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import javax.jws.WebResult;
@WebService( name="ModalitaIncassoComponentWS",targetNamespace="http://contab.cnr.it/sigla")
@Remote

public interface ModalitaIncassoComponentSessionWS extends  java.rmi.Remote{
	
		 @WebMethod  @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<ModalitaIncasso>  cercaModalitaIncasso(
				 @WebParam (name="query")String query,
				 @WebParam (name="esercizio") Integer esercizio,
				 @WebParam (name="dominio") String dominio,
				 @WebParam (name="numMax") Integer numMax,
				 @WebParam (name="user") String user,
				 @WebParam (name="ricerca") String ricerca);
		}
