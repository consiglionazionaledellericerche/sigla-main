/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/02/2008
 */
package it.cnr.contab.config00.file.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Tipo_fileHome extends BulkHome {
	public Tipo_fileHome(Connection conn) {
		super(Tipo_fileBulk.class, conn);
	}
	public Tipo_fileHome(Connection conn, PersistentCache persistentCache) {
		super(Tipo_fileBulk.class, conn, persistentCache);
	}
}