/*
* Created by Generator 1.0
* Date 09/05/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Procedure_amministrativeHome extends BulkHome {
	public Procedure_amministrativeHome(java.sql.Connection conn) {
		super(Procedure_amministrativeBulk.class, conn);
	}
	public Procedure_amministrativeHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Procedure_amministrativeBulk.class, conn, persistentCache);
	}
}