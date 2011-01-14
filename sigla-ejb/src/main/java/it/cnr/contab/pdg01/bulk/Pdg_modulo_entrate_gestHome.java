/*
* Created by Generator 1.0
* Date 23/11/2005
*/
package it.cnr.contab.pdg01.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Pdg_modulo_entrate_gestHome extends BulkHome {
	public Pdg_modulo_entrate_gestHome(java.sql.Connection conn) {
		super(Pdg_modulo_entrate_gestBulk.class, conn);
	}
	public Pdg_modulo_entrate_gestHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Pdg_modulo_entrate_gestBulk.class, conn, persistentCache);
	}
}