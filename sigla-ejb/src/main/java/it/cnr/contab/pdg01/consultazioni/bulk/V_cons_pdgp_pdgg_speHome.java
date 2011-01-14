/*
* Created by Generator 1.0
* Date 22/11/2005
*/
package it.cnr.contab.pdg01.consultazioni.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_cons_pdgp_pdgg_speHome extends BulkHome {
	public V_cons_pdgp_pdgg_speHome(java.sql.Connection conn) {
		super(V_cons_pdgp_pdgg_speBulk.class, conn);
	}
	public V_cons_pdgp_pdgg_speHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_pdgp_pdgg_speBulk.class, conn, persistentCache);
	}
}