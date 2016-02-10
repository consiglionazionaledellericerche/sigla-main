/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Tipo_attivita_fpHome extends BulkHome {
	public Tipo_attivita_fpHome(Connection conn) {
		super(Tipo_attivita_fpBulk.class, conn);
	}
	public Tipo_attivita_fpHome(Connection conn, PersistentCache persistentCache) {
		super(Tipo_attivita_fpBulk.class, conn, persistentCache);
	}
}