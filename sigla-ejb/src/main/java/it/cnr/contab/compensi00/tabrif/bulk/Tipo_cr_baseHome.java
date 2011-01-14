/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 17/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Tipo_cr_baseHome extends BulkHome {
	public Tipo_cr_baseHome(Connection conn) {
		super(Tipo_cr_baseBulk.class, conn);
	}
	public Tipo_cr_baseHome(Connection conn, PersistentCache persistentCache) {
		super(Tipo_cr_baseBulk.class, conn, persistentCache);
	}
}