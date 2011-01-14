/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 17/03/2009
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.sql.Connection;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_cons_giornaliera_cassaHome extends BulkHome {
	public V_cons_giornaliera_cassaHome(Connection conn) {
		super(V_cons_giornaliera_cassaBulk.class, conn);
	}
	public V_cons_giornaliera_cassaHome(Connection conn, PersistentCache persistentCache) {
		super(V_cons_giornaliera_cassaBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
	throws PersistencyException
{
	SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
	sql.addSQLClause("AND","V_CONS_GIORNALIERA_CASSA.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
	return sql;
}
}