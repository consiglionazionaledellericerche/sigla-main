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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 08/02/2007
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Ldap_serverKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String hostname;
	private java.lang.Integer port;
	public Ldap_serverKey() {
		super();
	}
	public Ldap_serverKey(java.lang.String hostname, java.lang.Integer port) {
		super();
		this.hostname=hostname;
		this.port=port;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ldap_serverKey)) return false;
		Ldap_serverKey k = (Ldap_serverKey) o;
		if (!compareKey(getHostname(), k.getHostname())) return false;
		if (!compareKey(getPort(), k.getPort())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getHostname());
		i = i + calculateKeyHashCode(getPort());
		return i;
	}
	public void setHostname(java.lang.String hostname)  {
		this.hostname=hostname;
	}
	public java.lang.String getHostname() {
		return hostname;
	}
	public void setPort(java.lang.Integer port)  {
		this.port=port;
	}
	public java.lang.Integer getPort() {
		return port;
	}
}