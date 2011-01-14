/*
* Created by Generator 1.0
* Date 22/11/2005
*/
package it.cnr.contab.anagraf00.core.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Cnr_anadipHome extends BulkHome {
	public Cnr_anadipHome(java.sql.Connection conn) {
		super(Cnr_anadipBulk.class, conn);
	}
	public Cnr_anadipHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Cnr_anadipBulk.class, conn, persistentCache);
	}
}