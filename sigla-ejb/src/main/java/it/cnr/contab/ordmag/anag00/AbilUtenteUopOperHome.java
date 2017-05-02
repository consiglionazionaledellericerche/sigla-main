/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class AbilUtenteUopOperHome extends BulkHome {
	public AbilUtenteUopOperHome(Connection conn) {
		super(AbilUtenteUopOperBulk.class, conn);
	}
	public AbilUtenteUopOperHome(Connection conn, PersistentCache persistentCache) {
		super(AbilUtenteUopOperBulk.class, conn, persistentCache);
	}
}