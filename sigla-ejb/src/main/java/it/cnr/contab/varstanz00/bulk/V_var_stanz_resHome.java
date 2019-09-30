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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 24/04/2013
 */
package it.cnr.contab.varstanz00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;
public class V_var_stanz_resHome extends BulkHome {
	public V_var_stanz_resHome(Class class1, Connection conn) {
		super(class1, conn);
	}
	public V_var_stanz_resHome(Connection conn, PersistentCache persistentCache) {
		super(V_var_stanz_resBulk.class, conn, persistentCache);
	}
	public V_var_stanz_resHome(Class class1,Connection connection,PersistentCache persistentcache) {
		super(class1, connection, persistentcache);
	}

}