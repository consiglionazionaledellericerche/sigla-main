/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 23/08/2013
 */
package it.cnr.contab.config00.consultazioni.bulk;
import java.sql.Connection;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class VContrattiTotaliDetHome extends BulkHome {
	public VContrattiTotaliDetHome(Connection conn) {
		super(VContrattiTotaliDetBulk.class, conn);
	}
	public VContrattiTotaliDetHome(Connection conn, PersistentCache persistentCache) {
		super(VContrattiTotaliDetBulk.class, conn, persistentCache);
	}
	
}