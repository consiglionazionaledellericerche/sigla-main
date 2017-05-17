/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class NotaPrecodificataHome extends BulkHome {
	public NotaPrecodificataHome(Connection conn) {
		super(NotaPrecodificataBulk.class, conn);
	}
	public NotaPrecodificataHome(Connection conn, PersistentCache persistentCache) {
		super(NotaPrecodificataBulk.class, conn, persistentCache);
	}
}