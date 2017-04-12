/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2016
 */
package it.cnr.contab.docamm00.consultazioni.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VDocAttiviDaRiscontrareHome extends BulkHome {
	public VDocAttiviDaRiscontrareHome(Connection conn) {
		super(VDocAttiviDaRiscontrareBulk.class, conn);
	}
	public VDocAttiviDaRiscontrareHome(Connection conn, PersistentCache persistentCache) {
		super(VDocAttiviDaRiscontrareBulk.class, conn, persistentCache);
	}
}