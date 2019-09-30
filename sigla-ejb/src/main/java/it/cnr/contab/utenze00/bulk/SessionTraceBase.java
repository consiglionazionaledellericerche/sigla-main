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

/*
 * Created by BulkGenerator 1.5 [30/07/2008]
 * Date 31/07/2008
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;
import it.cnr.jada.bulk.annotation.InputType;
import it.cnr.jada.persistency.Keyed;
public class SessionTraceBase extends SessionTraceKey implements Keyed {
//    SERVER_URL VARCHAR(100) NOT NULL
	@FieldPropertyAnnotation(name="serverUrl",
			inputType=InputType.TEXTAREA,
			cols=60,
			rows=5, 
			maxLength=100,
			nullable=false,
			label="Server Url")
	private java.lang.String server_url;
 
//    CD_UTENTE VARCHAR(20)
	@FieldPropertyAnnotation(name="cdUtente",
			inputType=InputType.TEXT,
			inputSize=20,
			maxLength=20,
			label="Utente")
	private java.lang.String cd_utente;
 
//  CD_CDS VARCHAR(30)
	@FieldPropertyAnnotation(name="cdCds",
			inputType=InputType.TEXT,
			inputSize=3,
			maxLength=3,
			label="CdS")
	private java.lang.String cd_cds;
	
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Table name: SESSION_TRACE
	 **/
	public SessionTraceBase() {
		super();
	}
	public SessionTraceBase(java.lang.String id_sessione) {
		super(id_sessione);
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Restituisce il valore di: [server_url]
	 **/
	public java.lang.String getServer_url() {
		return server_url;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Setta il valore di: [server_url]
	 **/
	public void setServer_url(java.lang.String server_url)  {
		this.server_url=server_url;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Restituisce il valore di: [cd_utente]
	 **/
	public java.lang.String getCd_utente() {
		return cd_utente;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Setta il valore di: [cd_utente]
	 **/
	public void setCd_utente(java.lang.String cd_utente)  {
		this.cd_utente=cd_utente;
	}

	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds) {
		this.cd_cds = cd_cds;
	}
}