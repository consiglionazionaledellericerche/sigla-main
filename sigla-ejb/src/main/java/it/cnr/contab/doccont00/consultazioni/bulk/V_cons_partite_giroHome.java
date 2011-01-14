/*
* Created by Generator 1.0
* Date 19/05/2005
*/
package it.cnr.contab.doccont00.consultazioni.bulk;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_cons_partite_giroHome extends BulkHome {
	protected V_cons_partite_giroHome(Class clazz,java.sql.Connection connection) {
		super(clazz,connection);
	}
	protected V_cons_partite_giroHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
		super(clazz,connection,persistentCache);
	}
	public V_cons_partite_giroHome(java.sql.Connection conn) {
		super(V_cons_partite_giroBulk.class, conn);
	}
	public V_cons_partite_giroHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_partite_giroBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addSQLClause("AND","V_CONS_PARTITE_GIRO.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
		return sql;
	}	
}