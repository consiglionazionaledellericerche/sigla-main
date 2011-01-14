/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 14/06/2010
 */
package it.cnr.contab.config00.sto.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class UnitaOrganizzativaPecHome extends BulkHome {
	public UnitaOrganizzativaPecHome(Connection conn) {
		super(UnitaOrganizzativaPecBulk.class, conn);
	}
	public UnitaOrganizzativaPecHome(Connection conn, PersistentCache persistentCache) {
		super(UnitaOrganizzativaPecBulk.class, conn, persistentCache);
	}
}