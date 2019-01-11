/*
* Created by Generator 1.0
* Date 15/02/2006
*/
package it.cnr.contab.varstanz00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Ass_var_stanz_res_cdrHome extends BulkHome {
	public Ass_var_stanz_res_cdrHome(java.sql.Connection conn) {
		super(Ass_var_stanz_res_cdrBulk.class, conn);
	}
	public Ass_var_stanz_res_cdrHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Ass_var_stanz_res_cdrBulk.class, conn, persistentCache);
	}
	public java.util.Collection findDettagliSpesa(Ass_var_stanz_res_cdrBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Var_stanz_res_rigaBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PG_VARIAZIONE",SQLBuilder.EQUALS,testata.getPg_variazione());
		sql.addSQLClause("AND","CD_CDR",SQLBuilder.EQUALS,testata.getCd_centro_responsabilita());
		return dettHome.fetchAll(sql);
	}
	public java.util.Collection findDettagliSpesa(Var_stanz_resBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Var_stanz_res_rigaBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PG_VARIAZIONE",SQLBuilder.EQUALS,testata.getPg_variazione());
		return dettHome.fetchAll(sql);
	}	
}