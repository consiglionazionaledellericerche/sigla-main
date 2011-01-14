package it.cnr.contab.doccont00.core.bulk;

import java.util.*;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_sit_bil_cds_cnrHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_sit_bil_cds_cnrHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public V_sit_bil_cds_cnrHome(java.sql.Connection conn) {
	super(V_sit_bil_cds_cnrBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_sit_bil_cds_cnrHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public V_sit_bil_cds_cnrHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_sit_bil_cds_cnrBulk.class,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param esercizio	
 * @param cd_cds	
 * @param tipoGestione	
 * @return 
 * @throws PersistencyException	
 */
public Collection findBilancioCds( Integer esercizio, String cd_cds, String tipoGestione ) throws PersistencyException
{
	SQLBuilder sql = createSQLBuilder();
	sql.addClause( "AND" , "esercizio", sql.EQUALS, esercizio );
	sql.addClause( "AND" , "cd_cds", sql.EQUALS, cd_cds );
	sql.addClause( "AND" , "ti_gestione", sql.EQUALS, tipoGestione );	
	return fetchAll( sql );
}
}
