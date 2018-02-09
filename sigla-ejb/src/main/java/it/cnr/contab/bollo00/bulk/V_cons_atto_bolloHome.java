package it.cnr.contab.bollo00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

public class V_cons_atto_bolloHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public V_cons_atto_bolloHome(java.sql.Connection conn) {
		super(V_cons_atto_bolloBulk.class, conn);
	}
	public V_cons_atto_bolloHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_atto_bolloBulk.class, conn, persistentCache);
	}
}