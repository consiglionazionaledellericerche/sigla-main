/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/03/2008
 */
package it.cnr.contab.utenze00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Ass_tipo_ruolo_privilegioHome extends BulkHome {
	public Ass_tipo_ruolo_privilegioHome(Connection conn) {
		super(Ass_tipo_ruolo_privilegioBulk.class, conn);
	}
	public Ass_tipo_ruolo_privilegioHome(Connection conn, PersistentCache persistentCache) {
		super(Ass_tipo_ruolo_privilegioBulk.class, conn, persistentCache);
	}
}