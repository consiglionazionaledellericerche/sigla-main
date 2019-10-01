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
