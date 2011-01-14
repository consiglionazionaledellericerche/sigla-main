/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 14/06/2010
 */
package it.cnr.contab.docamm00.intrastat.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class FatturaPassivaIntraSHome extends BulkHome {
	public FatturaPassivaIntraSHome(Connection conn) {
		super(FatturaPassivaIntraSBulk.class, conn);
	}
	public FatturaPassivaIntraSHome(Connection conn, PersistentCache persistentCache) {
		super(FatturaPassivaIntraSBulk.class, conn, persistentCache);
	}
}