/*
* Creted by Generator 1.0
* Date 24/02/2005
*/
package it.cnr.contab.progettiric00.core.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Commessa_spesaHome extends BulkHome {
	public Commessa_spesaHome(java.sql.Connection conn) {
		super(Commessa_spesaBulk.class, conn);
	}
	public Commessa_spesaHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Commessa_spesaBulk.class, conn, persistentCache);
	}
}