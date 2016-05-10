/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 20/02/2013
 */
package it.cnr.contab.doccont00.intcass.bulk;
import java.sql.Connection;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class V_distinte_totHome extends BulkHome {
	public V_distinte_totHome(Connection conn) {
		super(V_distinte_totBulk.class, conn);
	}
	public V_distinte_totHome(Connection conn, PersistentCache persistentCache) {
		super(V_distinte_totBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
		return sql;
	}		
}