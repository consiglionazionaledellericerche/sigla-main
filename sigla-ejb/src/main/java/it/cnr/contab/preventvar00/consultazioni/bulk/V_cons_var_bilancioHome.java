/*
* Created by Generator 1.0
* Date 23/05/2006
*/
package it.cnr.contab.preventvar00.consultazioni.bulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_cons_var_bilancioHome extends BulkHome {
	public V_cons_var_bilancioHome(java.sql.Connection conn) {
		super(V_cons_var_bilancioBulk.class, conn);
	}
	public V_cons_var_bilancioHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_var_bilancioBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException
{
	SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
	sql.addClause("AND","esercizio",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
	sql.addClause("AND","cd_cds",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(usercontext));

	return sql;
}	
}