/*
* Creted by Generator 1.0
* Date 07/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class OrganoHome extends BulkHome {
	public OrganoHome(java.sql.Connection conn) {
		super(OrganoBulk.class, conn);
	}
	public OrganoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(OrganoBulk.class, conn, persistentCache);
	}
}