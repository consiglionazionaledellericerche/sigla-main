package it.cnr.contab.pdg00.bulk;

/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 16.03.39)
 * @author: Roberto Fantino
 */
public class Stampa_analitica_spese_ldaHome extends Stampa_spese_ldaHome {
/**
 * Stampa_obbligazioniHome constructor comment.
 * @param conn java.sql.Connection
 */
public Stampa_analitica_spese_ldaHome(java.sql.Connection conn) {
	super(Stampa_analitica_spese_ldaBulk.class, conn);
}
/**
 * Stampa_obbligazioniHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_analitica_spese_ldaHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Stampa_analitica_spese_ldaBulk.class, conn, persistentCache);
}
}
