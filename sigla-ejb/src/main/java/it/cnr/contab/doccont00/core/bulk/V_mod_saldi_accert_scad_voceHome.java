/*
 * Created on Mar 22, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.core.bulk;

import java.util.*;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class V_mod_saldi_accert_scad_voceHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_mod_saldi_accert_scad_voceHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public V_mod_saldi_accert_scad_voceHome(java.sql.Connection conn) {
	super(V_mod_saldi_accert_scad_voceBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_mod_saldi_accert_scad_voceHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public V_mod_saldi_accert_scad_voceHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_mod_saldi_accert_scad_voceBulk.class,conn,persistentCache);
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
	sql.addOrderBy("PROGRESSIVO ASC");	
	return fetchAll( sql );

}
}
