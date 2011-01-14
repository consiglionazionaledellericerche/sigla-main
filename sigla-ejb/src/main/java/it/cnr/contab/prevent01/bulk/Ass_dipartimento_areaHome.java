/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 20/01/2007
 */
package it.cnr.contab.prevent01.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Ass_dipartimento_areaHome extends BulkHome {
	public Ass_dipartimento_areaHome(Connection conn) {
		super(Ass_dipartimento_areaBulk.class, conn);
	}
	public Ass_dipartimento_areaHome(Connection conn, PersistentCache persistentCache) {
		super(Ass_dipartimento_areaBulk.class, conn, persistentCache);
	}
}