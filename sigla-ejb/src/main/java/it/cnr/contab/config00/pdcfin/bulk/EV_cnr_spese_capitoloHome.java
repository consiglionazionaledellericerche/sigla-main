package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Home che gestisce i capitoli di spesa del CNR
 */


public class EV_cnr_spese_capitoloHome extends Elemento_voceHome {
	public EV_cnr_spese_capitoloHome(java.sql.Connection conn) {
	super(EV_cnr_spese_capitoloBulk.class,conn);
}
public EV_cnr_spese_capitoloHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(EV_cnr_spese_capitoloBulk.class,conn,persistentCache);
}
/**
 * Restituisce il SQLBuilder per selezionare fra tutti gli elementi voce quelli relativi ai capitoli di spesa
 * del CNR
 * @return SQLBuilder 
 */

public SQLBuilder createSQLBuilder() 
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, APPARTENENZA_CNR );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, GESTIONE_SPESE );
	sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, TIPO_CAPITOLO );
	sql.addClause("AND", "cd_parte", SQLBuilder.EQUALS, PARTE_1 );					
	return sql;
}
/**
 * Restituisce il SQLBuilder per selezionare i Titoli di Spesa del Cnr Parte 1 per l'esercizio di scrivania
 * @param bulk bulk ricevente
 * @param home home del bulk su cui si cerca
 * @param bulkClause è l'istanza di bulk che ha indotto le clauses 
 * @param clause clause che arrivano dalle properties (form collegata al search tool) 
 * @return it.cnr.jada.persistency.sql.SQLBuilder
 */

public SQLBuilder selectTitolo_padreByClause(EV_cnr_spese_capitoloBulk bulk,Elemento_voceHome home,it.cnr.jada.bulk.OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
	SQLBuilder sql = home.createSQLBuilder();
	
	// Attenzione: cerca il titolo non la categoria padre
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, APPARTENENZA_CNR );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, GESTIONE_SPESE );
	sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, TIPO_TITOLO);
	sql.addClause("AND", "cd_parte", SQLBuilder.EQUALS, PARTE_1 );
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
	sql.addClause( clause);
	return sql;
}
}
