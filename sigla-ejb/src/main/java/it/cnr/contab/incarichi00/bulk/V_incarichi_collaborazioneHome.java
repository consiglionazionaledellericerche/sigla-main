/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 29/10/2007
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_incarichi_collaborazioneHome extends BulkHome {
	public V_incarichi_collaborazioneHome(Connection conn) {
		super(V_incarichi_collaborazioneBulk.class, conn);
	}
	public V_incarichi_collaborazioneHome(Connection conn, PersistentCache persistentCache) {
		super(V_incarichi_collaborazioneBulk.class, conn, persistentCache);
	}
}