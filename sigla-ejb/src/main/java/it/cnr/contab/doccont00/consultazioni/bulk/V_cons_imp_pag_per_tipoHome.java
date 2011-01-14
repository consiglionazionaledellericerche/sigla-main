/*
* Creted by Generator 1.0
* Date 08/11/2005
*/
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_cons_imp_pag_per_tipoHome extends BulkHome {
	public V_cons_imp_pag_per_tipoHome(java.sql.Connection conn) {
		super(V_cons_imp_pag_per_tipoBulk.class, conn);
	}
	public V_cons_imp_pag_per_tipoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_imp_pag_per_tipoBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
			throws PersistencyException
		{
			SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
			sql.addSQLClause("AND","V_IMP_PAG_PER_TIPO.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
			//if (compoundfindclause!= null)
			  // sql.addClause(compoundfindclause);
			return sql;
		}
}