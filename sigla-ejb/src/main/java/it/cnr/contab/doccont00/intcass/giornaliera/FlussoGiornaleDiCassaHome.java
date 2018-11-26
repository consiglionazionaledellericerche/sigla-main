/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/10/2018
 */
package it.cnr.contab.doccont00.intcass.giornaliera;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class FlussoGiornaleDiCassaHome extends BulkHome {
	public FlussoGiornaleDiCassaHome(Connection conn) {
		super(FlussoGiornaleDiCassaBulk.class, conn);
	}
	public FlussoGiornaleDiCassaHome(Connection conn, PersistentCache persistentCache) {
		super(FlussoGiornaleDiCassaBulk.class, conn, persistentCache);
	}
}