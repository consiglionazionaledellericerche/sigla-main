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
package it.cnr.contab.config00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class ApplicationServerKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String hostname;
	private java.lang.String session_id;
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Table name: APPLICATION_SERVER
	 **/
	public ApplicationServerKey() {
		super();
	}
	public ApplicationServerKey(java.lang.String hostname, java.lang.String session_id) {
		super();
		this.hostname=hostname;
		this.session_id=session_id;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof ApplicationServerKey)) return false;
		ApplicationServerKey k = (ApplicationServerKey) o;
		if (!compareKey(getHostname(), k.getHostname())) return false;
		if (!compareKey(getSession_id(), k.getSession_id())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getHostname());
		i = i + calculateKeyHashCode(getSession_id());
		return i;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Restituisce il valore di: [hostname]
	 **/
	public void setHostname(java.lang.String hostname)  {
		this.hostname=hostname;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Setta il valore di: [hostname]
	 **/
	public java.lang.String getHostname() {
		return hostname;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Restituisce il valore di: [session_id]
	 **/
	public void setSession_id(java.lang.String session_id)  {
		this.session_id=session_id;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Setta il valore di: [session_id]
	 **/
	public java.lang.String getSession_id() {
		return session_id;
	}
}