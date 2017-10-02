package it.cnr.contab.docamm00.docs.bulk;
import java.sql.Connection;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class VSpesometroNewHome extends BulkHome {
	public VSpesometroNewHome(Connection conn) {
		super(VSpesometroNewBulk.class, conn);
	}
	public VSpesometroNewHome(Connection conn, PersistentCache persistentCache) {
		super(VSpesometroNewBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
	    sql.addClause("AND","esercizio",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext));
		return sql;
	}
}