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