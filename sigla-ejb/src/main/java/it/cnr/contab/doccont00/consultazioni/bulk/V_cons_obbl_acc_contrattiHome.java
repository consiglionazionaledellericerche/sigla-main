/*
* Creted by Generator 1.0
* Date 19/04/2005
*/
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_cons_obbl_acc_contrattiHome extends BulkHome {
	public V_cons_obbl_acc_contrattiHome(java.sql.Connection conn) {
		super(V_cons_obbl_acc_contrattiBulk.class, conn);
	}
	public V_cons_obbl_acc_contrattiHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_obbl_acc_contrattiBulk.class, conn, persistentCache);
	}
}