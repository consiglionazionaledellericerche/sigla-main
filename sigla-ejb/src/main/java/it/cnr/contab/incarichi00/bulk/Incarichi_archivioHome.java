/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class Incarichi_archivioHome extends BulkHome {
	public Incarichi_archivioHome(Connection conn) {
		super(Incarichi_procedura_archivioBulk.class, conn);
	}
	public Incarichi_archivioHome(Connection conn, PersistentCache persistentCache) {
		super(Incarichi_procedura_archivioBulk.class, conn, persistentCache);
	}
}