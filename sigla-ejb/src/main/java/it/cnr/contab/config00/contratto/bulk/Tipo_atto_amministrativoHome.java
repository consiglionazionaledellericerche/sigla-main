/*
* Creted by Generator 1.0
* Date 07/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Tipo_atto_amministrativoHome extends BulkHome {
	public Tipo_atto_amministrativoHome(java.sql.Connection conn) {
		super(Tipo_atto_amministrativoBulk.class, conn);
	}
	public Tipo_atto_amministrativoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Tipo_atto_amministrativoBulk.class, conn, persistentCache);
	}
}