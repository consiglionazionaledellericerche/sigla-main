/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 27/09/2006
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Accertamento_mod_voceHome extends BulkHome {
	public Accertamento_mod_voceHome(Connection conn) {
		super(Accertamento_mod_voceBulk.class, conn);
	}
	public Accertamento_mod_voceHome(Connection conn, PersistentCache persistentCache) {
		super(Accertamento_mod_voceBulk.class, conn, persistentCache);
	}
}