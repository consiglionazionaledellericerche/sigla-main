/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/10/2018
 */
package it.cnr.contab.doccont00.intcass.giornaliera;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class InformazioniContoEvidenzaHome extends BulkHome {
	public InformazioniContoEvidenzaHome(Connection conn) {
		super(InformazioniContoEvidenzaBulk.class, conn);
	}
	public InformazioniContoEvidenzaHome(Connection conn, PersistentCache persistentCache) {
		super(InformazioniContoEvidenzaBulk.class, conn, persistentCache);
	}
}