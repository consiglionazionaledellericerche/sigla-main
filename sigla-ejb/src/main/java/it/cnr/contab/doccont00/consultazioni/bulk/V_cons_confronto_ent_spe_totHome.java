/*
* Created by Generator 1.0
* Date 14/02/2007
*/
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_cons_confronto_ent_spe_totHome extends BulkHome {
	public V_cons_confronto_ent_spe_totHome(java.sql.Connection conn) {
		super(V_cons_confronto_ent_spe_totBulk.class, conn);
	}
	public V_cons_confronto_ent_spe_totHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_confronto_ent_spe_totBulk.class, conn, persistentCache);
	} 
}