/*
* Creted by Generator 1.0
* Date 18/04/2005
*/
package it.cnr.contab.config00.consultazioni.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_cons_commesse_contrattiHome extends BulkHome {
	public V_cons_commesse_contrattiHome(java.sql.Connection conn) {
		super(V_cons_commesse_contrattiBulk.class, conn);
	}
	public V_cons_commesse_contrattiHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_commesse_contrattiBulk.class, conn, persistentCache);
	}
}