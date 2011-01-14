package it.cnr.contab.doccont00.core.bulk;

import java.util.*;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_mod_saldi_accertHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_mod_saldi_accertHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public V_mod_saldi_accertHome(java.sql.Connection conn) {
	super(V_mod_saldi_accertBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_mod_saldi_accertHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public V_mod_saldi_accertHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_mod_saldi_accertBulk.class,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param accertamento	
 * @param pg_ver_rec	
 * @return 
 * @throws PersistencyException	
 */
public List findModificheSaldiFor(AccertamentoBulk accertamento, Long pg_ver_rec) throws PersistencyException
{
	SQLBuilder sql = createSQLBuilder();
	sql.addClause( "AND", "cd_cds", sql.EQUALS, accertamento.getCd_cds());
	sql.addClause( "AND", "esercizio", sql.EQUALS, accertamento.getEsercizio());
	sql.addClause( "AND", "esercizio_originale", sql.EQUALS, accertamento.getEsercizio_originale());
	sql.addClause( "AND", "pg_accertamento", sql.EQUALS, accertamento.getPg_accertamento());
	sql.addClause( "AND", "pg_old", sql.EQUALS, pg_ver_rec);
//	sql.addClause( "AND", "im_delta_voce", sql.NOT_EQUALS, new java.math.BigDecimal(0));	
	return fetchAll( sql );

}
}
