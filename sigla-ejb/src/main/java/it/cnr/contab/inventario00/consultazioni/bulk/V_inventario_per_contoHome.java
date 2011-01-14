/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/11/2007
 */
package it.cnr.contab.inventario00.consultazioni.bulk;
import java.sql.Connection;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_inventario_per_contoHome extends BulkHome {
	public V_inventario_per_contoHome(Connection conn) {
		super(V_inventario_per_contoBulk.class, conn);
	}
	public V_inventario_per_contoHome(Connection conn, PersistentCache persistentCache) {
		super(V_inventario_per_contoBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
		return sql;
	}	
}