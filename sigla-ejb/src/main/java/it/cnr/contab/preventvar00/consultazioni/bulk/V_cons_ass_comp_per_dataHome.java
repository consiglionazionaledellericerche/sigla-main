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
* Created by Generator 1.0
* Date 09/11/2006
*/
package it.cnr.contab.preventvar00.consultazioni.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_cons_ass_comp_per_dataHome extends BulkHome {
	public V_cons_ass_comp_per_dataHome(java.sql.Connection conn) {
		super(V_cons_ass_comp_per_dataBulk.class, conn);
	}
	public V_cons_ass_comp_per_dataHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_ass_comp_per_dataBulk.class, conn, persistentCache);
	}
}