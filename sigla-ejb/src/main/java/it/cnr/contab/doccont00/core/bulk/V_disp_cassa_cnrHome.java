package it.cnr.contab.doccont00.core.bulk;

import java.util.*;
import java.math.*;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_disp_cassa_cnrHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_disp_cassa_cnrHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public V_disp_cassa_cnrHome(java.sql.Connection conn) {
	super(V_disp_cassa_cnrBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_disp_cassa_cnrHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public V_disp_cassa_cnrHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_disp_cassa_cnrBulk.class,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param esercizio	
 * @return 
 * @throws PersistencyException	
 */
public BigDecimal findIm_disponibilita_cassaCNR( Integer esercizio ) throws PersistencyException
{
	SQLBuilder sql = createSQLBuilder();
	sql.addClause( "AND" , "esercizio", sql.EQUALS, esercizio );
	List result = fetchAll( sql );
	if ( result.isEmpty() )
		return null;
	return ((V_disp_cassa_cnrBulk)result.get(0)).getIm_disponibilta_cassa();
}
}
