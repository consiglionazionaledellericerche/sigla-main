/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;

import it.cnr.jada.persistency.PersistentCache;
public class MandatoAccreditamento_siopeHome extends Mandato_siopeHome {
	public MandatoAccreditamento_siopeHome(Connection conn) {
		super(MandatoAccreditamento_siopeBulk.class, conn);
	}
	public MandatoAccreditamento_siopeHome(Connection conn, PersistentCache persistentCache) {
		super(MandatoAccreditamento_siopeBulk.class, conn, persistentCache);
	}
}