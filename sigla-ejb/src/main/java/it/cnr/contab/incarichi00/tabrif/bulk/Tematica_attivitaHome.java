/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 14/09/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Tematica_attivitaHome extends BulkHome {
	public Tematica_attivitaHome(Connection conn) {
		super(Tematica_attivitaBulk.class, conn);
	}
	public Tematica_attivitaHome(Connection conn, PersistentCache persistentCache) {
		super(Tematica_attivitaBulk.class, conn, persistentCache);
	}
}