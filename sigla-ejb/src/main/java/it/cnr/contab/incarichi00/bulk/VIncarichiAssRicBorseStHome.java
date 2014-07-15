/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/07/2014
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VIncarichiAssRicBorseStHome extends BulkHome {
	public VIncarichiAssRicBorseStHome(Connection conn) {
		super(VIncarichiAssRicBorseStBulk.class, conn);
	}
	public VIncarichiAssRicBorseStHome(Connection conn, PersistentCache persistentCache) {
		super(VIncarichiAssRicBorseStBulk.class, conn, persistentCache);
	}
}