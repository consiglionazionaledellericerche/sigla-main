/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 17/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Gruppo_cr_detHome extends BulkHome {
	public Gruppo_cr_detHome(Connection conn) {
		super(Gruppo_cr_detBulk.class, conn);
	}
	public Gruppo_cr_detHome(Connection conn, PersistentCache persistentCache) {
		super(Gruppo_cr_detBulk.class, conn, persistentCache);
	}
	public java.util.List getDetailsFor(Gruppo_crBulk gruppo) throws it.cnr.jada.persistency.PersistencyException{

		it.cnr.jada.persistency.sql.SQLBuilder sql = createSQLBuilder();

		sql.addClause("AND","esercizio",sql.EQUALS,gruppo.getEsercizio());
		sql.addClause("AND","cd_gruppo_cr",sql.EQUALS,gruppo.getCd_gruppo_cr());
		
		return fetchAll(sql);
	}
}