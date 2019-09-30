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

import it.cnr.contab.client.docamm.RigaperNC;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.WebResult;
@WebService( name="RigheperNCComponentWS",targetNamespace="http://contab.cnr.it/sigla")
@Remote


public interface RigheperNCComponentSessionWS extends  java.rmi.Remote{
		
		 @WebMethod  @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<RigaperNC>  cercaRighe(
				 @WebParam (name="uo") String uo,
				 @WebParam (name="terzo") Integer terzo,
				 @WebParam (name="ti_causale") String ti_causale,//T(Tariffario) o C(Contratto) o L(Libera)
				 @WebParam (name="esercizio")Integer esercizio,
				 @WebParam (name="query")String query,
				 @WebParam (name="dominio") String dominio,
				 @WebParam (name="numMax") Integer numMax,
				 @WebParam (name="user") String user,
				 @WebParam (name="ricerca") String ricerca);
		 
		 /*@WebMethod  @WebResult(name="result") String  cercaRigheXml(
				 @WebParam (name="uo") String uo,
				 @WebParam (name="terzo") String terzo,
				 @WebParam (name="ti_causale") String ti_causale,//T(Tariffario) o C(Contratto) o L(Libera)
				 @WebParam (name="esercizio")String esercizio,
				 @WebParam (name="query")String query,
				 @WebParam (name="dominio") String dominio,
				 @WebParam (name="numMax") String numMax,
				 @WebParam (name="user") String user,
				 @WebParam (name="ricerca") String ricerca);*/
		}

