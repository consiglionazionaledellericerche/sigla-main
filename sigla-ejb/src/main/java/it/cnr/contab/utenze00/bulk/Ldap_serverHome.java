/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 08/02/2007
 */
package it.cnr.contab.utenze00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Ldap_serverHome extends BulkHome {
	public Ldap_serverHome(Connection conn) {
		super(Ldap_serverBulk.class, conn);
	}
	public Ldap_serverHome(Connection conn, PersistentCache persistentCache) {
		super(Ldap_serverBulk.class, conn, persistentCache);
	}
}