/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 14/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Reversale_siopeHome extends BulkHome {
	public Reversale_siopeHome(Class clazz, java.sql.Connection conn) {
		super(clazz,conn);
	}
	public Reversale_siopeHome(Class clazz,java.sql.Connection conn,PersistentCache persistentCache) {
		super(clazz,conn,persistentCache);
	}
	public Reversale_siopeHome(Connection conn) {
		super(Reversale_siopeBulk.class, conn);
	}
	public Reversale_siopeHome(Connection conn, PersistentCache persistentCache) {
		super(Reversale_siopeBulk.class, conn, persistentCache);
	}
	public java.util.Collection findCodiciSiopeCupCollegati(UserContext usercontext,Reversale_siopeBulk riga) throws PersistencyException {
		PersistentHome reversaleSiopeCupHome = getHomeCache().getHome(ReversaleSiopeCupIBulk.class);
		SQLBuilder sql = reversaleSiopeCupHome.createSQLBuilder();
		sql.addClause("AND", "cdCds", SQLBuilder.EQUALS, riga.getCd_cds());
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, riga.getEsercizio());
		sql.addClause("AND", "pgReversale", SQLBuilder.EQUALS, riga.getPg_reversale());
		sql.addClause("AND", "esercizioAccertamento", SQLBuilder.EQUALS, riga.getEsercizio_accertamento());
		sql.addClause("AND", "esercizioOriAccertamento", SQLBuilder.EQUALS, riga.getEsercizio_ori_accertamento());
		sql.addClause("AND", "pgAccertamento", SQLBuilder.EQUALS, riga.getPg_accertamento());
		sql.addClause("AND", "pgAccertamentoScadenzario", SQLBuilder.EQUALS, riga.getPg_accertamento_scadenzario());
		sql.addClause("AND", "cdCdsDocAmm", SQLBuilder.EQUALS, riga.getCd_cds_doc_amm());
		sql.addClause("AND", "cdUoDocAmm", SQLBuilder.EQUALS, riga.getCd_uo_doc_amm());
		sql.addClause("AND", "esercizioDocAmm", SQLBuilder.EQUALS, riga.getEsercizio_doc_amm());
		sql.addClause("AND", "cdTipoDocumentoAmm", SQLBuilder.EQUALS, riga.getCd_tipo_documento_amm());
		sql.addClause("AND", "pgDocAmm", SQLBuilder.EQUALS, riga.getPg_doc_amm());
		sql.addClause("AND", "esercizioSiope", SQLBuilder.EQUALS, riga.getEsercizio_siope());
		sql.addClause("AND", "tiGestione", SQLBuilder.EQUALS, riga.getTi_gestione());
		sql.addClause("AND", "cdSiope", SQLBuilder.EQUALS, riga.getCd_siope());
		return reversaleSiopeCupHome.fetchAll(sql);
	}
}