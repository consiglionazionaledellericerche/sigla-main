package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.persistency.sql.SQLBuilder;
/**
 * Insert the type's description here.
 * Creation date: (26/09/2001 11.37.02)
 * @author: Simonetta Costa
 */
public class V_persona_fisicaHome extends V_anagrafico_terzoHome {
/**
 * V_persona_fisicaHome constructor comment.
 * @param conn java.sql.Connection
 */
public V_persona_fisicaHome(java.sql.Connection conn) {
	super(V_persona_fisicaBulk.class, conn);
}
/**
 * V_persona_fisicaHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public V_persona_fisicaHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(V_persona_fisicaBulk.class, conn, persistentCache);
}
/**
 * Ritorna un SQLBuilder con la columnMap del ricevente
 */
public SQLBuilder createSQLBuilder() {
	SQLBuilder sql = super.createSQLBuilder(); 
	sql.addClause( "AND" , "ti_entita", SQLBuilder.EQUALS, AnagraficoBulk.FISICA );
	sql.addClause( "AND" , "dt_fine_rapporto", SQLBuilder.ISNULL, null );
	return sql;
}
}
