/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 31/03/2011
 */
package it.cnr.contab.docamm00.docs.bulk;
import java.sql.Connection;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class VFatcomBlacklistHome extends BulkHome {
	public VFatcomBlacklistHome(Connection conn) {
		super(VFatcomBlacklistBulk.class, conn);
	}
	public VFatcomBlacklistHome(Connection conn, PersistentCache persistentCache) {
		super(VFatcomBlacklistBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
	    sql.addClause("AND","esercizio",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext));
		return sql;
	}
}