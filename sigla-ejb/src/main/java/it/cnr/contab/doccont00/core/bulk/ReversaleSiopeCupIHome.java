/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class ReversaleSiopeCupIHome extends ReversaleSiopeCupHome {
		public ReversaleSiopeCupIHome(Connection conn) {
			super(ReversaleSiopeCupIBulk.class, conn);
		}
		public ReversaleSiopeCupIHome(Connection conn, PersistentCache persistentCache) {
			super(ReversaleSiopeCupIBulk.class, conn, persistentCache);
		} 
}
