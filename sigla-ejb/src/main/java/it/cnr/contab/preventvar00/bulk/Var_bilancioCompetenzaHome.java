/**
 * 
 */
package it.cnr.contab.preventvar00.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;

/**
 * @author mspasiano
 *
 */
public class Var_bilancioCompetenzaHome extends BulkHome {

	/**
	 * @param conn
	 */
	public Var_bilancioCompetenzaHome(Connection conn) {
		super(Var_bilancioCompetenzaBulk.class,conn);
	}

	/**
	 * @param conn
	 * @param persistentCache
	 */
	public Var_bilancioCompetenzaHome(Connection conn,PersistentCache persistentCache) {
		super(Var_bilancioCompetenzaBulk.class,conn,persistentCache);
	}
	
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addClause("AND","esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(usercontext));
		sql.addJoin("esercizio","esercizio_importi");
		return sql;
	}
}
