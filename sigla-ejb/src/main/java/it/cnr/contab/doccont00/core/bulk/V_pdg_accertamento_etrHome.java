/*
* Created by Generator 1.0
* Date 02/05/2005
*/
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_pdg_accertamento_etrHome extends BulkHome {
	public V_pdg_accertamento_etrHome(java.sql.Connection conn) {
		super(V_pdg_accertamento_etrBulk.class, conn);
	}
	public V_pdg_accertamento_etrHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_pdg_accertamento_etrBulk.class, conn, persistentCache);
	}
}