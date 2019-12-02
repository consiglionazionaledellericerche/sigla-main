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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_anagrafico_terzoHome extends BulkHome {
public V_anagrafico_terzoHome(Class clazz,java.sql.Connection conn) {
	super(clazz,conn);
}
public V_anagrafico_terzoHome(Class clazz,java.sql.Connection conn,PersistentCache persistentCache) {
	super( clazz,conn,persistentCache);
}
public V_anagrafico_terzoHome(java.sql.Connection conn) {
	super(V_anagrafico_terzoBulk.class,conn);
}
public V_anagrafico_terzoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_anagrafico_terzoBulk.class,conn,persistentCache);
}
}
