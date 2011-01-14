/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Tipo_attivitaHome extends BulkHome {
	public Tipo_attivitaHome(Connection conn) {
		super(Tipo_attivitaBulk.class, conn);
	}
	public Tipo_attivitaHome(Connection conn, PersistentCache persistentCache) {
		super(Tipo_attivitaBulk.class, conn, persistentCache);
	}
}