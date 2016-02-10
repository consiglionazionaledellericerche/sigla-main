/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 05/03/2014
 */
package it.cnr.contab.inventario00.consultazioni.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VInventarioRicognizioneHome extends BulkHome {
	public VInventarioRicognizioneHome(Connection conn) {
		super(VInventarioRicognizioneBulk.class, conn);
	}
	public VInventarioRicognizioneHome(Connection conn, PersistentCache persistentCache) {
		super(VInventarioRicognizioneBulk.class, conn, persistentCache);
	}
}