/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 05/11/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;
public class V_incarichi_elenco_fpHome extends V_incarichi_elencoHome {
	public V_incarichi_elenco_fpHome(Class clazz, java.sql.Connection conn) {
		super(clazz, conn);
	}
	public V_incarichi_elenco_fpHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(clazz, conn, persistentCache);
	}
	public V_incarichi_elenco_fpHome(Connection conn) {
		super(V_incarichi_elenco_fpBulk.class, conn);
	}
	public V_incarichi_elenco_fpHome(Connection conn, PersistentCache persistentCache) {
		super(V_incarichi_elenco_fpBulk.class, conn, persistentCache);
	}
}