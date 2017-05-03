/*
* Created by Generator 1.0
* Date 19/09/2005
*/
package it.cnr.contab.prevent01.bulk;
import java.sql.SQLException;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_entrate_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Pdg_moduloHome extends BulkHome {
	public Pdg_moduloHome(java.sql.Connection conn) {
		super(Pdg_moduloBulk.class, conn);
	}
	public Pdg_moduloHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Pdg_moduloBulk.class, conn, persistentCache);
	}
	/**
	 * Recupera tutti le aree utilizzate per il modulo e presenti nelle tabelle PDG_MODULO_ENTRATE
	 * e PDG_MODULO_SPESE
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>CdsBulk</code>
	 */
	public java.util.Collection getAreeUtilizzate(Pdg_moduloBulk pdg) throws PersistencyException {
		PersistentHome cdsHome = getHomeCache().getHome(CdsBulk.class);
		SQLBuilder sql = cdsHome.createSQLBuilder();

		Pdg_Modulo_EntrateHome etrHome = (Pdg_Modulo_EntrateHome)getHomeCache().getHome(Pdg_Modulo_EntrateBulk.class);
		SQLBuilder sql1 = etrHome.createSQLBuilder();
		sql1.addClause("AND","esercizio",SQLBuilder.EQUALS,pdg.getEsercizio());
		sql1.addClause("AND","cd_centro_responsabilita",SQLBuilder.EQUALS,pdg.getCd_centro_responsabilita());
		sql1.addClause("AND","pg_progetto",SQLBuilder.EQUALS,pdg.getPg_progetto());
		sql1.addSQLJoin("PDG_MODULO_ENTRATE.CD_CDS_AREA", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
		
		Pdg_modulo_speseHome speHome = (Pdg_modulo_speseHome)getHomeCache().getHome(Pdg_modulo_speseBulk.class);
		SQLBuilder sql2 = speHome.createSQLBuilder();
		sql2.addClause("AND","esercizio",SQLBuilder.EQUALS,pdg.getEsercizio());
		sql2.addClause("AND","cd_centro_responsabilita",SQLBuilder.EQUALS,pdg.getCd_centro_responsabilita());
		sql2.addClause("AND","pg_progetto",SQLBuilder.EQUALS,pdg.getPg_progetto());
		sql2.addSQLJoin("PDG_MODULO_SPESE.CD_CDS_AREA", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");

		sql.openParenthesis("AND");
		sql.addSQLExistsClause("AND", sql1);
		sql.addSQLExistsClause("OR", sql2);
		sql.closeParenthesis();

		return cdsHome.fetchAll(sql);
	}	
	
	public boolean existsDecisionaleE(Pdg_moduloBulk modulo) throws PersistencyException, SQLException {
		PersistentHome etrHome = getHomeCache().getHome(Pdg_Modulo_EntrateBulk.class);
		SQLBuilder sql = etrHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,modulo.getEsercizio());
		sql.addClause(FindClause.AND,"cd_centro_responsabilita",SQLBuilder.EQUALS,modulo.getCd_centro_responsabilita());
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,modulo.getPg_progetto());
		return sql.executeExistsQuery(getConnection());
	}
	
	public boolean existsDecisionaleS(Pdg_moduloBulk modulo) throws PersistencyException, SQLException {
		PersistentHome speHome = getHomeCache().getHome(Pdg_modulo_speseBulk.class);
		SQLBuilder sql = speHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,modulo.getEsercizio());
		sql.addClause(FindClause.AND,"cd_centro_responsabilita",SQLBuilder.EQUALS,modulo.getCd_centro_responsabilita());
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,modulo.getPg_progetto());
		return sql.executeExistsQuery(getConnection());
	}
	
	public boolean existsGestionaleE(Pdg_moduloBulk modulo) throws PersistencyException, SQLException {
		PersistentHome etrHome = getHomeCache().getHome(Pdg_modulo_entrate_gestBulk.class);
		SQLBuilder sql = etrHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,modulo.getEsercizio());
		sql.addClause(FindClause.AND,"cd_centro_responsabilita",SQLBuilder.EQUALS,modulo.getCd_centro_responsabilita());
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,modulo.getPg_progetto());
		return sql.executeExistsQuery(getConnection());
	}
	
	public boolean existsGestionaleS(Pdg_moduloBulk modulo) throws PersistencyException, SQLException {
		PersistentHome speHome = getHomeCache().getHome(Pdg_modulo_spese_gestBulk.class);
		SQLBuilder sql = speHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,modulo.getEsercizio());
		sql.addClause(FindClause.AND,"cd_centro_responsabilita",SQLBuilder.EQUALS,modulo.getCd_centro_responsabilita());
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,modulo.getPg_progetto());
		return sql.executeExistsQuery(getConnection());
	}	
}