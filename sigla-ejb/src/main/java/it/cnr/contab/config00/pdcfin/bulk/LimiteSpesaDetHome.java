/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 15/12/2010
 */
package it.cnr.contab.config00.pdcfin.bulk;

import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class LimiteSpesaDetHome extends BulkHome {
	public LimiteSpesaDetHome(Connection conn) {
		super(LimiteSpesaDetBulk.class, conn);
	}
	public LimiteSpesaDetHome(Connection conn, PersistentCache persistentCache) {
		super(LimiteSpesaDetBulk.class, conn, persistentCache);
	}
	public java.util.List getDetailsFor(LimiteSpesaBulk bulk) throws it.cnr.jada.persistency.PersistencyException{
		it.cnr.jada.persistency.sql.SQLBuilder sql = createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,bulk.getEsercizio());
		sql.addClause("AND","cd_elemento_voce",sql.EQUALS,bulk.getCd_elemento_voce());
		sql.addClause("AND","ti_gestione",sql.EQUALS,bulk.getTi_gestione());
		sql.addClause("AND","ti_appartenza",sql.EQUALS,bulk.getTi_appartenenza());
		sql.addClause("AND","fonte",sql.EQUALS,bulk.getFonte());
		return fetchAll(sql);
	}
}