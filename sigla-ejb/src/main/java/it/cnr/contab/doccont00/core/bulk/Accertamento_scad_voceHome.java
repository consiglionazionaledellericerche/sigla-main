package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Accertamento_scad_voceHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Accertamento_scad_voceHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Accertamento_scad_voceHome(java.sql.Connection conn) {
	super(Accertamento_scad_voceBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Accertamento_scad_voceHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Accertamento_scad_voceHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Accertamento_scad_voceBulk.class,conn,persistentCache);
}
/**
 * Ricerco i dettagli di tutte le scadenze dell'accertamento dato
 * 
 *
 * @param accertamento	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public java.util.List findDettagli_scadenze( AccertamentoBulk accertamento ) throws IntrospectionException,PersistencyException 
{
	SQLBuilder sql = createSQLBuilder();
	
	sql.addClause("AND","cd_cds",sql.EQUALS, accertamento.getCd_cds());
	sql.addClause("AND","esercizio",sql.EQUALS, accertamento.getEsercizio());
	sql.addClause("AND","esercizio_originale",sql.EQUALS, accertamento.getEsercizio_originale());
	sql.addClause("AND","pg_accertamento",sql.EQUALS, accertamento.getPg_accertamento());
	sql.addOrderBy("PG_ACCERTAMENTO_SCADENZARIO");
	
	return fetchAll(sql);
}
}
