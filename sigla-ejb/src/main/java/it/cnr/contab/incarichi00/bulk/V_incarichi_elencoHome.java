/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 05/11/2007
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_incarichi_elencoHome extends BulkHome {
	public V_incarichi_elencoHome(Class clazz, java.sql.Connection conn) {
		super(clazz,conn);
	}
	public V_incarichi_elencoHome(Class clazz, java.sql.Connection conn,PersistentCache persistentCache) {
		super(clazz,conn,persistentCache);
	}
	public V_incarichi_elencoHome(Connection conn) {
		super(V_incarichi_elencoBulk.class, conn);
	}
	public V_incarichi_elencoHome(Connection conn, PersistentCache persistentCache) {
		super(V_incarichi_elencoBulk.class, conn, persistentCache);
	}
}