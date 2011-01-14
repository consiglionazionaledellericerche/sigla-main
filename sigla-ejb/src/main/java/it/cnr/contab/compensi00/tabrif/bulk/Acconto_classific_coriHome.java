/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 16/04/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Iterator;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SimpleFindClause;
public class Acconto_classific_coriHome extends BulkHome {
	public Acconto_classific_coriHome(Connection conn) {
		super(Acconto_classific_coriBulk.class, conn);
	}
	public Acconto_classific_coriHome(Connection conn, PersistentCache persistentCache) {
		super(Acconto_classific_coriBulk.class, conn, persistentCache);
	}

	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
	throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addSQLClause("AND","ACCONTO_CLASSIFIC_CORI.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
		return sql;
	}
}