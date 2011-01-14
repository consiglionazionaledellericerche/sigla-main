/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 16/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Ass_tipo_cori_voce_epHome extends BulkHome {
	public Ass_tipo_cori_voce_epHome(Connection conn) {
		super(Ass_tipo_cori_voce_epBulk.class, conn);
	}
	public Ass_tipo_cori_voce_epHome(Connection conn, PersistentCache persistentCache) {
		super(Ass_tipo_cori_voce_epBulk.class, conn, persistentCache);
	}
}