
/*
* Created by Generator 1.0
* Date 10/02/2006
*/
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_cons_disp_comp_resHome extends BulkHome {
	public V_cons_disp_comp_resHome(java.sql.Connection conn) {
		super(V_cons_disp_comp_resBulk.class, conn);
	}
	public V_cons_disp_comp_resHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_disp_comp_resBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext,CompoundFindClause compoundfindclause) throws PersistencyException {
		return super.selectByClause(usercontext, compoundfindclause);
	}
}