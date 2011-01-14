/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/01/2009
 */
package it.cnr.contab.anagraf00.core.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Ass_rapp_impiegoHome extends BulkHome {
	public Ass_rapp_impiegoHome(Connection conn) {
		super(Ass_rapp_impiegoBulk.class, conn);
	}
	public Ass_rapp_impiegoHome(Connection conn, PersistentCache persistentCache) {
		super(Ass_rapp_impiegoBulk.class, conn, persistentCache);
	}
}