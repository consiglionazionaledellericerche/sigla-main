package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_impegnoHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_impegnoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public V_impegnoHome(java.sql.Connection conn) {
	super(V_impegnoBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_impegnoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public V_impegnoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_impegnoBulk.class,conn,persistentCache);
}
/**
 * Ritorna un SQLBuilder con la columnMap del ricevente
 */
public SQLBuilder createSQLBuilder() 
{
	SQLBuilder sql = super.createSQLBuilder();
	//join su OBBLIGAZIONE per recuperare il TIPO DOC CONTABILE
	sql.addSQLClause( "AND", "V_IMPEGNO.FL_PGIRO", sql.EQUALS, "N");	
	return sql;
}
}
