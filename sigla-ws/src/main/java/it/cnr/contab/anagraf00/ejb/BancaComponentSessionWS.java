package it.cnr.contab.anagraf00.ejb;

import it.cnr.contab.client.docamm.Banca;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import javax.jws.WebResult;
@WebService( name="BancaComponentWS",targetNamespace="http://contab.cnr.it/sigla")
@Remote

public interface BancaComponentSessionWS extends  java.rmi.Remote{
		
		/* @WebMethod  @WebResult(name="result") String  cercaBancheXml(
				 @WebParam (name="terzo")String terzo,
				 @WebParam (name="modalita")String modalita,
				 @WebParam (name="query")String query,
				 @WebParam (name="dominio") String dominio,
				 @WebParam (name="numMax") String numMax,
				 @WebParam (name="user") String user,
				 @WebParam (name="ricerca") String ricerca);
		*/	
		 @WebMethod  @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<Banca>  cercaBanche(
				 @WebParam (name="terzo")Integer terzo,
				 @WebParam (name="modalita")String modalita,
				 @WebParam (name="query")String query,
				 @WebParam (name="dominio") String dominio,
				 @WebParam (name="numMax") Integer numMax,
				 @WebParam (name="user") String user,
				 @WebParam (name="ricerca") String ricerca);
		}

