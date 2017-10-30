/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class AbilitBeneServMagHome extends BulkHome {
	public AbilitBeneServMagHome(Connection conn) {
		super(AbilitBeneServMagBulk.class, conn);
	}
	public AbilitBeneServMagHome(Connection conn, PersistentCache persistentCache) {
		super(AbilitBeneServMagBulk.class, conn, persistentCache);
	}
}