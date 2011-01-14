/*
* Created by Generator 1.0
* Date 30/08/2005
*/
package it.cnr.contab.inventario00.docs.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_impegni_inventarioHome extends BulkHome {
	public V_impegni_inventarioHome(java.sql.Connection conn) {
		super(V_impegni_inventarioBulk.class, conn);
	}
	public V_impegni_inventarioHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_impegni_inventarioBulk.class, conn, persistentCache);
	}
}