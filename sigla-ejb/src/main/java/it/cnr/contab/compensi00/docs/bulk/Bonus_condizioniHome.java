/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 19/02/2009
 */
package it.cnr.contab.compensi00.docs.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

public class Bonus_condizioniHome extends BulkHome {
	
	public Bonus_condizioniHome(Connection conn) {
		super(Bonus_condizioniBulk.class, conn);
	}
	public Bonus_condizioniHome(Connection conn, PersistentCache persistentCache) {
		super(Bonus_condizioniBulk.class, conn, persistentCache);
	}
	
}