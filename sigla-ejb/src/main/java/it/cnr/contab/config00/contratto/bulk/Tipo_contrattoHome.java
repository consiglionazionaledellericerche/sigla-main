/*
* Creted by Generator 1.0
* Date 07/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Tipo_contrattoHome extends BulkHome {
	public Tipo_contrattoHome(java.sql.Connection conn) {
		super(Tipo_contrattoBulk.class, conn);
	}
	public Tipo_contrattoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Tipo_contrattoBulk.class, conn, persistentCache);
	}
}