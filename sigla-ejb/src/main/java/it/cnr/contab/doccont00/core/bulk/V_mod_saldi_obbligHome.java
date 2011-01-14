package it.cnr.contab.doccont00.core.bulk;

import java.util.*;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_mod_saldi_obbligHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_mod_saldi_obbligHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public V_mod_saldi_obbligHome(java.sql.Connection conn) {
	super(V_mod_saldi_obbligBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_mod_saldi_obbligHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public V_mod_saldi_obbligHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_mod_saldi_obbligBulk.class,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param obbligazione	
 * @param pg_ver_rec	
 * @return 
 * @throws PersistencyException	
 */
public List findModificheSaldiFor(ObbligazioneBulk obbligazione, Long pg_ver_rec) throws PersistencyException
{
	SQLBuilder sql = createSQLBuilder();
	sql.addClause( "AND", "cd_cds", sql.EQUALS, obbligazione.getCd_cds());
	sql.addClause( "AND", "esercizio", sql.EQUALS, obbligazione.getEsercizio());
	sql.addClause( "AND", "esercizio_originale", sql.EQUALS, obbligazione.getEsercizio_originale());
	sql.addClause( "AND", "pg_obbligazione", sql.EQUALS, obbligazione.getPg_obbligazione());
	sql.addClause( "AND", "pg_old", sql.EQUALS, pg_ver_rec);
//	sql.addClause( "AND", "im_delta_voce", sql.NOT_EQUALS, new java.math.BigDecimal(0));	
	return fetchAll( sql );

}
}
