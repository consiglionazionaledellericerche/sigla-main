package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

public class Ruolo_bloccoHome extends BulkHome {
	public Ruolo_bloccoHome(java.sql.Connection conn) {
		super(Ruolo_bloccoBulk.class,conn);
	}
	public Ruolo_bloccoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Ruolo_bloccoBulk.class,conn,persistentCache);
	}
}
