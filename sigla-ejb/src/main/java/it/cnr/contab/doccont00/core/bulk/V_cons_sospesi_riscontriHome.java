/*
* Created by Generator 1.0
* Date 07/02/2006
*/
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_cons_sospesi_riscontriHome extends BulkHome {
	public V_cons_sospesi_riscontriHome(java.sql.Connection conn) {
		super(V_cons_sospesi_riscontriBulk.class, conn);
	}
	public V_cons_sospesi_riscontriHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_sospesi_riscontriBulk.class, conn, persistentCache);
	}
}