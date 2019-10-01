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
import java.sql.Connection;
import java.util.List;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class ApplicationServerHome extends BulkHome {
	public ApplicationServerHome(Connection conn) {
		super(ApplicationServerBulk.class, conn);
	}
	public ApplicationServerHome(Connection conn, PersistentCache persistentCache) {
		super(ApplicationServerBulk.class, conn, persistentCache);
	}
	
	public ApplicationServerBulk cercaServer(ApplicationServerBulk server) throws PersistencyException{
		SQLBuilder sql = createSQLBuilder();
		sql.addClause(FindClause.AND, "hostname", SQLBuilder.EQUALS, server.getHostname());
		List servers = fetchAll(sql);
		if (servers.isEmpty())
			return null;
		return (ApplicationServerBulk)servers.get(0);
	}
}