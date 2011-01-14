/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 23/04/2007
 */
package it.cnr.contab.config00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Codici_siopeHome extends BulkHome {
	public Codici_siopeHome(Connection conn) {
		super(Codici_siopeBulk.class, conn);
	}
	public Codici_siopeHome(Connection conn, PersistentCache persistentCache) {
		super(Codici_siopeBulk.class, conn, persistentCache);
	} 
}