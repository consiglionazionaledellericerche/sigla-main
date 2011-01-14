/*
 * Created by BulkGenerator 1.5 [30/07/2008]
 * Date 17/10/2008
 */
package it.cnr.contab.utenze00.bulk;
import java.sql.Connection;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class PreferitiHome extends BulkHome {
	public PreferitiHome(Connection conn) {
		super(PreferitiBulk.class, conn);
	}
	public PreferitiHome(Connection conn, PersistentCache persistentCache) {
		super(PreferitiBulk.class, conn, persistentCache);
	}
	@Override
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
        sql.addClause(FindClause.AND, "cd_utente", SQLBuilder.EQUALS, usercontext.getUser());		
		sql.addOrderBy("duva");
		return sql;
	}
}