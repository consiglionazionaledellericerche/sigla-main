/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 23/04/2007
 */
package it.cnr.contab.config00.bulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;
public class Codici_siopeHome extends BulkHome {
	public Codici_siopeHome(Connection conn) {
		super(Codici_siopeBulk.class, conn);
	}
	public Codici_siopeHome(Connection conn, PersistentCache persistentCache) {
		super(Codici_siopeBulk.class, conn, persistentCache);
	} 

	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
	    sql.addClause("AND","esercizio",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext));
		return sql;
	}
}