package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * Insert the type's description here.
 * Creation date: (10/24/2001 2:31:46 PM)
 * @author: Roberto Peli
 */
public class Fattura_attiva_IHome extends Fattura_attivaHome {
/**
 * Fattura_passiva_IHome constructor comment.
 * @param conn java.sql.Connection
 */
public Fattura_attiva_IHome(java.sql.Connection conn) {
	super(Fattura_attiva_IBulk.class, conn);
}
/**
 * Fattura_passiva_IHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Fattura_attiva_IHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Fattura_attiva_IBulk.class, conn, persistentCache);
}
/**
 * Ritorna un SQLBuilder con la columnMap del ricevente
 */
public SQLBuilder createSQLBuilder() {

	SQLBuilder sql = super.createSQLBuilder();
	sql.addSQLClause("AND", "FATTURA_ATTIVA.TI_FATTURA", sql.EQUALS, Fattura_attiva_IBulk.TIPO_FATTURA_ATTIVA);
	return sql;
}
}
