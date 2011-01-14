/*
* Created by Generator 1.0
* Date 30/08/2005
*/
package it.cnr.contab.inventario00.docs.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_associazioni_disponibiliHome extends BulkHome {
	public V_associazioni_disponibiliHome(java.sql.Connection conn) {
		super(V_associazioni_disponibiliBulk.class, conn);
	}
	public V_associazioni_disponibiliHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_associazioni_disponibiliBulk.class, conn, persistentCache);
	}
}