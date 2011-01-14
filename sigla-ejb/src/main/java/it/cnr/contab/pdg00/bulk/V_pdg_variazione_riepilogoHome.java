/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 13/02/2008
 */
package it.cnr.contab.pdg00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_pdg_variazione_riepilogoHome extends BulkHome {
	public V_pdg_variazione_riepilogoHome(Connection conn) {
		super(V_pdg_variazione_riepilogoBulk.class, conn);
	}
	public V_pdg_variazione_riepilogoHome(Connection conn, PersistentCache persistentCache) {
		super(V_pdg_variazione_riepilogoBulk.class, conn, persistentCache);
	}
}