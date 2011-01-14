package it.cnr.contab.doccont00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 16.03.39)
 * @author: Roberto Fantino
 */
public class Stampa_registro_obbligazioniHome extends ObbligazioneHome {
/**
 * Stampa_obbligazioniHome constructor comment.
 * @param clazz java.lang.Class
 * @param conn java.sql.Connection
 */
public Stampa_registro_obbligazioniHome(Class clazz, java.sql.Connection conn) {
	super(clazz, conn);
}
/**
 * Stampa_obbligazioniHome constructor comment.
 * @param clazz java.lang.Class
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_registro_obbligazioniHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(clazz, conn, persistentCache);
}
/**
 * Stampa_obbligazioniHome constructor comment.
 * @param conn java.sql.Connection
 */
public Stampa_registro_obbligazioniHome(java.sql.Connection conn) {
	super(Stampa_registro_obbligazioniBulk.class, conn);
}
/**
 * Stampa_obbligazioniHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_registro_obbligazioniHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Stampa_registro_obbligazioniBulk.class, conn, persistentCache);
}
}
