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

package it.cnr.contab.docamm00.ejb;

import it.cnr.contab.client.docamm.FatturaAttiva;
import it.cnr.contab.client.docamm.FatturaAttivaException_Exception;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService( name="FatturaAttivaComponentWS",targetNamespace="http://contab.cnr.it/sigla")
@Remote
public interface FatturaAttivaComponentSessionWS extends java.rmi.Remote{
	@WebMethod @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<FatturaAttiva> InserimentoFatture(
			 @WebParam (name="fatturaAttiva") java.util.ArrayList<FatturaAttiva> fatturaAttiva) ;
	@WebMethod @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") FatturaAttiva InserimentoFattura(
			 @WebParam (name="fatturaAttiva") FatturaAttiva fatturaAttiva) throws FatturaAttivaException_Exception ;
	@WebMethod @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") FatturaAttiva RicercaFattura(
			 @WebParam (name="user") String user,
			 @WebParam (name="esercizio") Long esercizio,
			 @WebParam (name="cds") String cds,
			 @WebParam (name="uo") String uo,
			 @WebParam (name="pg") Long pg);			 
}
