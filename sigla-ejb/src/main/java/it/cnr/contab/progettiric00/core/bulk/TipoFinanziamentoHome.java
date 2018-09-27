/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 27/09/2018
 */
package it.cnr.contab.progettiric00.core.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class TipoFinanziamentoHome extends BulkHome {
	public TipoFinanziamentoHome(Connection conn) {
		super(TipoFinanziamentoBulk.class, conn);
	}
	public TipoFinanziamentoHome(Connection conn, PersistentCache persistentCache) {
		super(TipoFinanziamentoBulk.class, conn, persistentCache);
	}
}