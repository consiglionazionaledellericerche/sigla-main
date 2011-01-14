/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/02/2008
 */
package it.cnr.contab.config00.file.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Ass_tipo_gruppo_fileHome extends BulkHome {
	public Ass_tipo_gruppo_fileHome(Connection conn) {
		super(Ass_tipo_gruppo_fileBulk.class, conn);
	}
	public Ass_tipo_gruppo_fileHome(Connection conn, PersistentCache persistentCache) {
		super(Ass_tipo_gruppo_fileBulk.class, conn, persistentCache);
	}
}