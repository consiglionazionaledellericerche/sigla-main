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
 * Date 05/11/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;
public class V_incarichi_elenco_fpHome extends V_incarichi_elencoHome {
	public V_incarichi_elenco_fpHome(Class clazz, java.sql.Connection conn) {
		super(clazz, conn);
	}
	public V_incarichi_elenco_fpHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(clazz, conn, persistentCache);
	}
	public V_incarichi_elenco_fpHome(Connection conn) {
		super(V_incarichi_elenco_fpBulk.class, conn);
	}
	public V_incarichi_elenco_fpHome(Connection conn, PersistentCache persistentCache) {
		super(V_incarichi_elenco_fpBulk.class, conn, persistentCache);
	}
}