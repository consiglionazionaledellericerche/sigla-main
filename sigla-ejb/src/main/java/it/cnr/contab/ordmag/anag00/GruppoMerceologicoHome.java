/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class GruppoMerceologicoHome extends BulkHome {
	public GruppoMerceologicoHome(Connection conn) {
		super(GruppoMerceologicoBulk.class, conn);
	}
	public GruppoMerceologicoHome(Connection conn, PersistentCache persistentCache) {
		super(GruppoMerceologicoBulk.class, conn, persistentCache);
	}
}