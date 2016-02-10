/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Blt_regole_diariaHome extends BulkHome {
	public Blt_regole_diariaHome(Connection conn) {
		super(Blt_regole_diariaBulk.class, conn);
	}
	public Blt_regole_diariaHome(Connection conn, PersistentCache persistentCache) {
		super(Blt_regole_diariaBulk.class, conn, persistentCache);
	}
}