/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 30/05/2013
 */
package it.cnr.contab.doccont00.intcass.bulk;
import java.sql.Connection;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class ExtCassiereCdsHome extends BulkHome {
	public ExtCassiereCdsHome(Connection conn) {
		super(ExtCassiereCdsBulk.class, conn);
	}
	public ExtCassiereCdsHome(Connection conn, PersistentCache persistentCache) {
		super(ExtCassiereCdsBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
			throws PersistencyException
		{
			
			SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,((CNRUserContext)usercontext).getEsercizio());
			return sql;
		}	
}