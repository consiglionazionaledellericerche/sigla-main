/*
* Created by Generator 1.0
* Date 01/11/2005
*/
package it.cnr.contab.prevent01.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_pdg_piano_ripartoHome extends BulkHome {
	public V_pdg_piano_ripartoHome(java.sql.Connection conn) {
		super(V_pdg_piano_ripartoBulk.class, conn);
	}
	public V_pdg_piano_ripartoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_pdg_piano_ripartoBulk.class, conn, persistentCache);
	}
}