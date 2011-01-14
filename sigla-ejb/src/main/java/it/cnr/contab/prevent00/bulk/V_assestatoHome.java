/*
* Created by Generator 1.0
* Date 05/05/2006
*/
package it.cnr.contab.prevent00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_assestatoHome extends BulkHome {
	public V_assestatoHome(java.sql.Connection conn) {
		super(V_assestatoBulk.class, conn);
	}
	public V_assestatoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_assestatoBulk.class, conn, persistentCache);
	}
}