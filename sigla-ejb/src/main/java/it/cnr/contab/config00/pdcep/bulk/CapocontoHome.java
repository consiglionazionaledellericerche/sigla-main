package it.cnr.contab.config00.pdcep.bulk;

import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Home che gestisce i capoconti.
 */
public class CapocontoHome extends Voce_epHome {
public CapocontoHome(java.sql.Connection conn) {
	super(CapocontoBulk.class,conn);
}
public CapocontoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(CapocontoBulk.class,conn,persistentCache);
}
/**
 * Restituisce il SQLBuilder per selezionare fra tutte le Voci Economico Patrimoniali quelle relative
 * ai Capoconti.
 * @return SQLBuilder 
 */
public SQLBuilder createSQLBuilder( )	
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "ti_voce_ep", SQLBuilder.EQUALS, TIPO_CAPOCONTO);
	return sql; 
}
}
