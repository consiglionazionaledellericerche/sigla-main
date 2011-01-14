/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 11/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Ass_tipo_cori_evHome extends BulkHome {
	public Ass_tipo_cori_evHome(Connection conn) {
		super(Ass_tipo_cori_evBulk.class, conn);
	}
	public Ass_tipo_cori_evHome(Connection conn, PersistentCache persistentCache) {
		super(Ass_tipo_cori_evBulk.class, conn, persistentCache);
	}
}