/*
* Created by Generator 1.0
* Date 17/07/2006
*/
package it.cnr.contab.inventario00.docs.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_buono_carico_scaricoHome extends BulkHome {
	public V_buono_carico_scaricoHome(java.sql.Connection conn) {
		super(V_buono_carico_scaricoBulk.class, conn);
	}
	public V_buono_carico_scaricoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_buono_carico_scaricoBulk.class, conn, persistentCache);
	}
}