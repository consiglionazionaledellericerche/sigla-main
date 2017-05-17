/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class TipoMovimentoMagAzHome extends BulkHome {
	public TipoMovimentoMagAzHome(Connection conn) {
		super(TipoMovimentoMagAzBulk.class, conn);
	}
	public TipoMovimentoMagAzHome(Connection conn, PersistentCache persistentCache) {
		super(TipoMovimentoMagAzBulk.class, conn, persistentCache);
	}
}