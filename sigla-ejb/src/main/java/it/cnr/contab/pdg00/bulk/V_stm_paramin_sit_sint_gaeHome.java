/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 25/03/2008
 */
package it.cnr.contab.pdg00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_stm_paramin_sit_sint_gaeHome extends BulkHome {
	public V_stm_paramin_sit_sint_gaeHome(Connection conn) {
		super(V_stm_paramin_sit_sint_gaeBulk.class, conn);
	}
	public V_stm_paramin_sit_sint_gaeHome(Connection conn, PersistentCache persistentCache) {
		super(V_stm_paramin_sit_sint_gaeBulk.class, conn, persistentCache);
	}
}