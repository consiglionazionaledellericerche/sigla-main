/*
* Created by Generator 1.0
* Date 22/09/2005
*/
package it.cnr.contab.logregistry00.logs.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class L_esercizioHome extends BulkHome {
	public L_esercizioHome(java.sql.Connection conn) {
		super(L_esercizioBulk.class, conn);
	}
	public L_esercizioHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(L_esercizioBulk.class, conn, persistentCache);
	}
}