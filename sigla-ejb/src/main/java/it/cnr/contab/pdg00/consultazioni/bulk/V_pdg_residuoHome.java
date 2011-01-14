/*
* Created by Generator 1.0
* Date 29/06/2005
*/
package it.cnr.contab.pdg00.consultazioni.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_pdg_residuoHome extends BulkHome {
	public V_pdg_residuoHome(java.sql.Connection conn) {
		super(V_pdg_residuoBulk.class, conn);
	}
	public V_pdg_residuoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_pdg_residuoBulk.class, conn, persistentCache);
	}
}