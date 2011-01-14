package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.*;

public class V_disp_cassa_cdsHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_disp_cassa_cdsHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public V_disp_cassa_cdsHome(java.sql.Connection conn) {
	super(V_disp_cassa_cdsBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_disp_cassa_cdsHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public V_disp_cassa_cdsHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_disp_cassa_cdsBulk.class,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param esercizio	
 * @return 
 * @throws PersistencyException	
 */
public Collection findDisponibilitaCassa( Integer esercizio ) throws PersistencyException
{
	EnteBulk ente = (EnteBulk) getHomeCache().getHome(EnteBulk.class).findAll().get(0);
	SQLBuilder sql = createSQLBuilder();
	sql.addClause( "AND" , "esercizio", sql.EQUALS, esercizio );
	sql.addClause( "AND" , "cd_cds", sql.NOT_EQUALS, ente.getCd_unita_organizzativa() );
	return fetchAll( sql );
}
}
