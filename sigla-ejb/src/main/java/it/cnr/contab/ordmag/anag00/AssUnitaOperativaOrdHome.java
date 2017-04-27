/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class AssUnitaOperativaOrdHome extends BulkHome {
	public AssUnitaOperativaOrdHome(Connection conn) {
		super(AssUnitaOperativaOrdBulk.class, conn);
	}
	public AssUnitaOperativaOrdHome(Connection conn, PersistentCache persistentCache) {
		super(AssUnitaOperativaOrdBulk.class, conn, persistentCache);
	}
}