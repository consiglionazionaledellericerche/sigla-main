/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 29/11/2006
 */
package it.cnr.contab.config00.geco.bulk;
import java.sql.Connection;
import java.sql.SQLException;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.util.ejb.EJBCommonServices;
public class Geco_dipartimentiHome extends BulkHome {
	public Geco_dipartimentiHome(Connection conn) {
		super(Geco_dipartimentiBulk.class, conn);
	}
	public Geco_dipartimentiHome(Connection conn, PersistentCache persistentCache) {
		super(Geco_dipartimentiBulk.class, conn, persistentCache);
	}
}