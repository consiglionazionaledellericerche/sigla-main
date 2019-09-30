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
* Creted by Generator 1.0
* Date 24/02/2005
*/
package it.cnr.contab.progettiric00.core.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Commessa_spesaHome extends BulkHome {
	public Commessa_spesaHome(java.sql.Connection conn) {
		super(Commessa_spesaBulk.class, conn);
	}
	public Commessa_spesaHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Commessa_spesaBulk.class, conn, persistentCache);
	}
}