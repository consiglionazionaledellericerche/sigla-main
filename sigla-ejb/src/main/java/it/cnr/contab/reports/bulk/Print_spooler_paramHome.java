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

package it.cnr.contab.reports.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Print_spooler_paramHome extends BulkHome {
public Print_spooler_paramHome(java.sql.Connection conn) {
	super(Print_spooler_paramBulk.class,conn);
}
public Print_spooler_paramHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Print_spooler_paramBulk.class,conn,persistentCache);
}
public BulkList<Print_spooler_paramBulk> getParamsFromPgStampa(Long pgStampa) throws PersistencyException {
	SQLBuilder sql = this.createSQLBuilder();
	sql.addSQLClause(FindClause.AND, "PRINT_SPOOLER_PARAM.pg_stampa", SQLBuilder.EQUALS, pgStampa);
	return new BulkList<Print_spooler_paramBulk>( this.fetchAll(sql));
}
}