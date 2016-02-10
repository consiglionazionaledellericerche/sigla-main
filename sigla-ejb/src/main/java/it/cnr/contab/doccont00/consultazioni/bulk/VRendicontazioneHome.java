/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 31/01/2014
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VRendicontazioneHome extends BulkHome {
	public VRendicontazioneHome(Connection conn) {
		super(VRendicontazioneBulk.class, conn);
	}
	public VRendicontazioneHome(Connection conn, PersistentCache persistentCache) {
		super(VRendicontazioneBulk.class, conn, persistentCache);
	}
}