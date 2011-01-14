/* 
* Created by Generator 1.0
* Date 09/11/2006
*/
package it.cnr.contab.preventvar00.consultazioni.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_cons_ass_comp_per_dataHome extends BulkHome {
	public V_cons_ass_comp_per_dataHome(java.sql.Connection conn) {
		super(V_cons_ass_comp_per_dataBulk.class, conn);
	}
	public V_cons_ass_comp_per_dataHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_ass_comp_per_dataBulk.class, conn, persistentCache);
	}
}