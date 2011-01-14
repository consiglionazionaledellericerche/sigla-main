/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 28/05/2008
 */
package it.cnr.contab.pdg00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Var_stanz_resHome extends BulkHome {
	public Var_stanz_resHome(Connection conn) {
		super(Var_stanz_resBulk.class, conn);
	}
	public Var_stanz_resHome(Connection conn, PersistentCache persistentCache) {
		super(Var_stanz_resBulk.class, conn, persistentCache);
	}
}