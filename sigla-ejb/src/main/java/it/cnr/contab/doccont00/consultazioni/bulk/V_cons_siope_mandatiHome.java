/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 06/07/2007
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_cons_siope_mandatiHome extends BulkHome {
	public V_cons_siope_mandatiHome(Connection conn) {
		super(V_cons_siope_mandatiBulk.class, conn);
	}
	public V_cons_siope_mandatiHome(Connection conn, PersistentCache persistentCache) {
		super(V_cons_siope_mandatiBulk.class, conn, persistentCache);
	}
}