/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/06/2007
 */
package it.cnr.contab.config00.pdcfin.bulk;
import java.sql.Connection;

import it.cnr.contab.config00.bulk.Codici_siopeBulk;
import it.cnr.contab.config00.bulk.Codici_siopeHome;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Ass_tipologia_istat_siopeHome extends BulkHome {
	public Ass_tipologia_istat_siopeHome(Connection conn) {
		super(Ass_tipologia_istat_siopeBulk.class, conn);
	}
	public Ass_tipologia_istat_siopeHome(Connection conn, PersistentCache persistentCache) {
		super(Ass_tipologia_istat_siopeBulk.class, conn, persistentCache);
	}
	
	public SQLBuilder selectCodice_siopeByClause( Ass_tipologia_istat_siopeBulk bulk, Codici_siopeHome home,OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND", "cd_siope", SQLBuilder.EQUALS,bulk.getCd_siope());
		sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, bulk.getTi_gestione_siope());
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio_siope() );		
		sql.addClause( clause );
		return sql;
	}
	
	public boolean IsUtilizzato(Ass_tipologia_istat_siopeBulk ass) throws java.sql.SQLException{

		SQLBuilder sql = createSQLBuilder();
		SQLBuilder sql2 = createSQLBuilder();
		SQLBuilder sql3 = createSQLBuilder();
		sql2.resetColumns();
		sql2.addColumn("1");
		sql3.resetColumns();
		sql3.addColumn("1");
		sql2.addTableToHeader("MANDATO_SIOPE M");
		sql3.addTableToHeader("REVERSALE_SIOPE R");
		sql2.addSQLJoin("M.ESERCIZIO_SIOPE","ASS_TIPOLOGIA_ISTAT_SIOPE.ESERCIZIO_SIOPE");
		sql2.addSQLJoin("M.TI_GESTIONE","ASS_TIPOLOGIA_ISTAT_SIOPE.TI_GESTIONE_SIOPE");
		sql2.addSQLJoin("M.CD_SIOPE","ASS_TIPOLOGIA_ISTAT_SIOPE.CD_SIOPE");
		sql2.addSQLClause("AND","ASS_TIPOLOGIA_ISTAT_SIOPE.ESERCIZIO_SIOPE",sql2.EQUALS,ass.getEsercizio_siope());
		sql2.addSQLClause("AND","ASS_TIPOLOGIA_ISTAT_SIOPE.TI_GESTIONE_SIOPE",sql2.EQUALS,ass.getTi_gestione_siope());
		sql2.addSQLClause("AND","ASS_TIPOLOGIA_ISTAT_SIOPE.CD_SIOPE",sql2.EQUALS,ass.getCd_siope());
		sql2.addSQLClause("AND","ASS_TIPOLOGIA_ISTAT_SIOPE.PG_TIPOLOGIA",sql2.EQUALS,ass.getPg_tipologia());
		sql3.addSQLJoin("R.ESERCIZIO_SIOPE","ASS_TIPOLOGIA_ISTAT_SIOPE.ESERCIZIO_SIOPE");
		sql3.addSQLJoin("R.TI_GESTIONE","ASS_TIPOLOGIA_ISTAT_SIOPE.TI_GESTIONE_SIOPE");
		sql3.addSQLJoin("R.CD_SIOPE","ASS_TIPOLOGIA_ISTAT_SIOPE.CD_SIOPE");
		sql3.addSQLClause("AND","ASS_TIPOLOGIA_ISTAT_SIOPE.ESERCIZIO_SIOPE",sql3.EQUALS,ass.getEsercizio_siope());
		sql3.addSQLClause("AND","ASS_TIPOLOGIA_ISTAT_SIOPE.TI_GESTIONE_SIOPE",sql3.EQUALS,ass.getTi_gestione_siope());
		sql3.addSQLClause("AND","ASS_TIPOLOGIA_ISTAT_SIOPE.CD_SIOPE",sql3.EQUALS,ass.getCd_siope());
		sql3.addSQLClause("AND","ASS_TIPOLOGIA_ISTAT_SIOPE.PG_TIPOLOGIA",sql3.EQUALS,ass.getPg_tipologia());
		sql.addSQLExistsClause("AND",sql2);
		sql.addSQLExistsClause("OR", sql3);
		
		return sql.executeExistsQuery(getConnection());
	}
	
}