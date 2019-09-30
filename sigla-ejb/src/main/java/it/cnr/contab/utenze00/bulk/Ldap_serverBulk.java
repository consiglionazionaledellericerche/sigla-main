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
public class Ldap_serverBulk extends Ldap_serverBase {
	public Ldap_serverBulk() {
		super();
	}
	public Ldap_serverBulk(java.lang.String hostname, java.lang.Integer port) {
		super(hostname, port);
	}

	public boolean isAttivo() {
		return getFl_attivo() != null && getFl_attivo().booleanValue();
	}
	public boolean isMaster() {
		return getFl_master() != null && getFl_master().booleanValue();
	}
}