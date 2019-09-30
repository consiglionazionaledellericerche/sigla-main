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

import it.cnr.contab.client.docamm.Terzo;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.transform.Source;
import javax.xml.ws.WebFault;
import javax.jws.WebResult;
@WebService( name="TerzoComponentWS",targetNamespace="http://contab.cnr.it/sigla")
@Remote

public interface TerzoComponentSessionWS extends  java.rmi.Remote{
	
		/* @WebMethod @WebResult(name="result") String inserisciTerzo(	
				 @WebParam (name="tipoterzo") String tipoterzo,
				 @WebParam (name="numMax") String numMax,
				 @WebParam (name="user") String user,
				 @WebParam (name="via") String via,
				 @WebParam (name="civico") String civico,
				 @WebParam (name="cap") String cap,
				 @WebParam (name="nazione") String nazione,
				 @WebParam (name="comune") String comune,
				 @WebParam (name="ragione_sociale") String ragione_sociale,
				 @WebParam (name="partita_iva") String partita_iva,
				 @WebParam (name="cognome") String cognome,
				 @WebParam (name="nome") String nome,
				 @WebParam (name="codice_fiscale") String codice_fiscale,
				 @WebParam (name="data_nascita") String data_nascita,
				 @WebParam (name="nazione_nascita") String nazione_nascita,
				 @WebParam (name="comune_nascita") String comune_nascita,
				 @WebParam (name="sesso") String sesso);

		 @WebMethod @WebResult(name="result") String eliminaTerzo(
				 @WebParam (name="tipoterzo") String tipoterzo,
				 @WebParam (name="numMax") String numMax,
				 @WebParam (name="user") String user,
				 @WebParam (name="cd_terzo") String cd_terzo);
		 
		 @WebMethod @WebResult(name="result") String modificaTerzo(
				 @WebParam (name="tipoterzo") String tipoterzo,
				 @WebParam (name="numMax") String numMax,
				 @WebParam (name="user") String user,
				 @WebParam (name="via") String via,
				 @WebParam (name="civico") String civico,
				 @WebParam (name="cap") String cap,
				 @WebParam (name="nazione") String nazione,
				 @WebParam (name="comune") String comune,
				 @WebParam (name="ragione_sociale") String ragione_sociale,
				 @WebParam (name="partita_iva") String partita_iva,
				 @WebParam (name="cognome") String cognome,
				 @WebParam (name="nome") String nome,
				 @WebParam (name="codice_fiscale") String codice_fiscale,
				 @WebParam (name="data_nascita") String data_nascita,
				 @WebParam (name="nazione_nascita") String nazione_nascita,
				 @WebParam (name="comune_nascita") String comune_nascita,
				 @WebParam (name="sesso") String sesso,
				 @WebParam (name="cd_terzo") String cd_terzo);
		
		 @WebMethod  @WebResult(name="result") String  cercaTerzoXml(
				 @WebParam (name="query")String query,
				 @WebParam (name="dominio") String dominio,
				 @WebParam (name="tipoterzo") String tipoterzo,
				 @WebParam (name="numMax") String numMax,
				 @WebParam (name="user") String user,
				 //@WebParam (name="cd_terzo") String cd_terzo,
				 @WebParam (name="ricerca") String ricerca);
		 */
		 @WebMethod  @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<Terzo>  cercaTerzo(
				 @WebParam (name="query")String query,
				 @WebParam (name="dominio") String dominio,
				 @WebParam (name="tipoterzo") String tipoterzo,
				 @WebParam (name="numMax") Integer numMax,
				 @WebParam (name="user") String user,
				 //@WebParam (name="cd_terzo") String cd_terzo,
				 @WebParam (name="ricerca") String ricerca);
		}

