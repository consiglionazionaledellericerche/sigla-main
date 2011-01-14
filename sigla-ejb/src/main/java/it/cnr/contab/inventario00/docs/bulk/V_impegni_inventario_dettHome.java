/*
* Created by Generator 1.0
* Date 14/12/2006
*/
package it.cnr.contab.inventario00.docs.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_impegni_inventario_dettHome extends BulkHome {
	public V_impegni_inventario_dettHome(java.sql.Connection conn) {
		super(V_impegni_inventario_dettBulk.class, conn);
	}
	public V_impegni_inventario_dettHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_impegni_inventario_dettBulk.class, conn, persistentCache);
	}
}