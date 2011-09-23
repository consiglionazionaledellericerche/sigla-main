/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2011
 */
package it.cnr.contab.missioni00.tabrif.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class MissioneQuotaRimborsoHome extends BulkHome {
	public MissioneQuotaRimborsoHome(Connection conn) {
		super(MissioneQuotaRimborsoBulk.class, conn);
	}
	public MissioneQuotaRimborsoHome(Connection conn, PersistentCache persistentCache) {
		super(MissioneQuotaRimborsoBulk.class, conn, persistentCache);
	}
}