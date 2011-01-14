/*
* Created by Generator 1.0
* Date 17/05/2005
*/
package it.cnr.contab.coepcoan00.core.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Chiusura_coepHome extends BulkHome {
	public Chiusura_coepHome(java.sql.Connection conn) {
		super(Chiusura_coepBulk.class, conn);
	}
	public Chiusura_coepHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Chiusura_coepBulk.class, conn, persistentCache);
	}
}