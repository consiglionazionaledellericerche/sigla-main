/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/07/2011
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Quadri_770Home extends BulkHome {
	public Quadri_770Home(Connection conn) {
		super(Quadri_770Bulk.class, conn);
	}
	
	public Quadri_770Home(Connection conn, PersistentCache persistentCache) {
		super(Quadri_770Bulk.class, conn, persistentCache);
	}
}