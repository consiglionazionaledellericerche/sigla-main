/*
* Created by Generator 1.0
* Date 14/09/2006
*/
package it.cnr.contab.preventvar00.consultazioni.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_cons_var_pdggHome extends BulkHome {
	public V_cons_var_pdggHome(java.sql.Connection conn) {
		super(V_cons_var_pdggBulk.class, conn);
	}
	public V_cons_var_pdggHome(java.sql.Connection conn, PersistentCache persistentCache)  {
		super(V_cons_var_pdggBulk.class, conn, persistentCache);
	}
}