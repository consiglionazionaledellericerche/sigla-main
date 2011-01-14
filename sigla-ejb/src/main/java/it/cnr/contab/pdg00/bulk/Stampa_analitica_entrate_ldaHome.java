package it.cnr.contab.pdg00.bulk;

/**
 * Insert the type's description here.
 * Creation date: (06/11/2003 15.52.44)
 * @author: Gennaro Borriello
 */
public class Stampa_analitica_entrate_ldaHome extends Stampa_spese_ldaHome {
/**
 * Stampa_analitica_entate_ldaHome constructor comment.
 * @param conn java.sql.Connection
 */
public Stampa_analitica_entrate_ldaHome(java.sql.Connection conn) {
	super(Stampa_analitica_entrate_ldaBulk.class, conn);
}
/**
 * Stampa_analitica_entate_ldaHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_analitica_entrate_ldaHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Stampa_analitica_entrate_ldaBulk.class, conn, persistentCache);
}
}
