/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.anagraf00.ejb;

import it.cnr.contab.client.docamm.Contratto;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.WebResult;
@WebService( name="ContrattoComponentWS",targetNamespace="http://contab.cnr.it/sigla")
@Remote

public interface ContrattoComponentSessionWS extends  java.rmi.Remote{
		
		 /* @WebMethod  @WebResult(name="result") String  cercaContrattiXml(
				 @WebParam (name="esercizio") String esercizio,
				 @WebParam (name="uo") String uo,
				 @WebParam (name="tipo") String tipo,//A o P
				 @WebParam (name="query")String query,
				 @WebParam (name="dominio") String dominio,
				 @WebParam (name="numMax") String numMax,
				 @WebParam (name="user") String user,
				 @WebParam (name="ricerca") String ricerca);
				 */
		 @WebMethod  @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<Contratto> cercaContratti(
				 @WebParam (name="esercizio") Integer esercizio,
				 @WebParam (name="uo") String uo,
				 @WebParam (name="tipo") String tipo,//A o P
				 @WebParam (name="query")String query,
				 @WebParam (name="dominio") String dominio,
				 @WebParam (name="numMax") Integer numMax,
				 @WebParam (name="user") String user,
				 @WebParam (name="ricerca") String ricerca);
		}

