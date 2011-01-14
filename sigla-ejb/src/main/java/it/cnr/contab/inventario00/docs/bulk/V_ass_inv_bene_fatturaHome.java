/*
* Created by Generator 1.0
* Date 30/08/2005
*/
package it.cnr.contab.inventario00.docs.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_ass_inv_bene_fatturaHome extends BulkHome {
	public V_ass_inv_bene_fatturaHome(java.sql.Connection conn) {
		super(V_ass_inv_bene_fatturaBulk.class, conn);
	}
	public V_ass_inv_bene_fatturaHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_ass_inv_bene_fatturaBulk.class, conn, persistentCache);
	}
}