/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 19/02/2009
 */
package it.cnr.contab.compensi00.docs.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Bonus_nucleo_famHome extends BulkHome {
	public Bonus_nucleo_famHome(Connection conn) {
		super(Bonus_nucleo_famBulk.class, conn);
	}
	public Bonus_nucleo_famHome(Connection conn, PersistentCache persistentCache) {
		super(Bonus_nucleo_famBulk.class, conn, persistentCache);
	}
	public java.util.List findDetailsFor(BonusBulk bonus) throws PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Bonus_nucleo_famBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		
		if (bonus != null) {
			sql.addSQLClause("AND", "BONUS_NUCLEO_FAM.ESERCIZIO", sql.EQUALS,bonus.getEsercizio());
			sql.addSQLClause("AND", "BONUS_NUCLEO_FAM.PG_BONUS", sql.EQUALS,bonus.getPg_bonus());
		}
		sql.addOrderBy("esercizio,pg_bonus,decode(TIPO_COMPONENTE_NUCLEO,'C',1,'F',2,'A',3)");
		return fetchAll(sql);
	}
}