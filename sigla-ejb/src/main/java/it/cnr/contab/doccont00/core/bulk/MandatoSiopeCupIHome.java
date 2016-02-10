/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class MandatoSiopeCupIHome extends MandatoSiopeCupHome {
	public MandatoSiopeCupIHome(Connection conn) {
		super(MandatoSiopeCupIBulk.class, conn);
	}
	public MandatoSiopeCupIHome(Connection conn, PersistentCache persistentCache) {
		super(MandatoSiopeCupIBulk.class, conn, persistentCache);
	} 
}