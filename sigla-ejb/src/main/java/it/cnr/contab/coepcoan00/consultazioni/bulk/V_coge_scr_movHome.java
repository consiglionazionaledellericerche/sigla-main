/*
* Created by Generator 1.0
* Date 11/07/2005
*/
package it.cnr.contab.coepcoan00.consultazioni.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_coge_scr_movHome extends BulkHome {
	public V_coge_scr_movHome(java.sql.Connection conn) {
		super(V_coge_scr_movBulk.class, conn);
	}
	public V_coge_scr_movHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_coge_scr_movBulk.class, conn, persistentCache);
	}
}