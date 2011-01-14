/*
* Created by Generator 1.0
* Date 23/06/2006
*/
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Obbligazione_mod_voceHome extends BulkHome {
	public Obbligazione_mod_voceHome(java.sql.Connection conn) {
		super(Obbligazione_mod_voceBulk.class, conn);
	}
	public Obbligazione_mod_voceHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Obbligazione_mod_voceBulk.class, conn, persistentCache);
	}
}