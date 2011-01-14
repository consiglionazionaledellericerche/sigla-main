package it.cnr.contab.prevent01.consultazioni.bulk;
import java.sql.Connection;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_cons_pdgg_areaHome extends BulkHome {
	public V_cons_pdgg_areaHome(java.sql.Connection conn) {
		super(V_cons_pdgg_areaBulk.class, conn);
	}
	public V_cons_pdgg_areaHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_pdgg_areaBulk.class, conn, persistentCache);
	}
}