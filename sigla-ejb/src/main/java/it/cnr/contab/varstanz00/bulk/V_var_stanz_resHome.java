/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 24/04/2013
 */
package it.cnr.contab.varstanz00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;
public class V_var_stanz_resHome extends BulkHome {
	public V_var_stanz_resHome(Class class1, Connection conn) {
		super(class1, conn);
	}
	public V_var_stanz_resHome(Connection conn, PersistentCache persistentCache) {
		super(V_var_stanz_resBulk.class, conn, persistentCache);
	}
	public V_var_stanz_resHome(Class class1,Connection connection,PersistentCache persistentcache) {
		super(class1, connection, persistentcache);
	}

}