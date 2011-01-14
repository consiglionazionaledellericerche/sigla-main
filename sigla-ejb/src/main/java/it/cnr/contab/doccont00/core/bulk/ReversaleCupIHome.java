/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;

import it.cnr.contab.doccont00.tabrif.bulk.CupBulk;
import it.cnr.contab.doccont00.tabrif.bulk.CupHome;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class ReversaleCupIHome extends ReversaleCupHome {
	public ReversaleCupIHome(Connection conn) {
		super(ReversaleCupIBulk.class, conn);
	}
	public ReversaleCupIHome(Connection conn, PersistentCache persistentCache) {
		super(ReversaleCupIBulk.class, conn, persistentCache);
	}
}