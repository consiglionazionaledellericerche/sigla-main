/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 07/10/2013
 */
package it.cnr.contab.config00.latt.bulk;
import java.sql.Connection;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class CofogHome extends BulkHome {
	public CofogHome(Connection conn) {
		super(CofogBulk.class, conn);
	}
	public CofogHome(Connection conn, PersistentCache persistentCache) {
		super(CofogBulk.class, conn, persistentCache);
	}
	
}