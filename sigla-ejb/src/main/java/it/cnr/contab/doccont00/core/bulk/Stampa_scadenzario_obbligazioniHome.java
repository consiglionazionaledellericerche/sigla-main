package it.cnr.contab.doccont00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 17.05.04)
 * @author: Roberto Fantino
 */
public class Stampa_scadenzario_obbligazioniHome extends Obbligazione_scadenzarioHome {
/**
 * Stampa_scadenzario_obbligazioniHome constructor comment.
 * @param clazz java.lang.Class
 * @param conn java.sql.Connection
 */
public Stampa_scadenzario_obbligazioniHome(Class clazz, java.sql.Connection conn) {
	super(clazz, conn);
}
/**
 * Stampa_scadenzario_obbligazioniHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_scadenzario_obbligazioniHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(clazz, conn, persistentCache);
}
/**
 * Stampa_scadenzario_obbligazioniHome constructor comment.
 * @param conn java.sql.Connection
 */
public Stampa_scadenzario_obbligazioniHome(java.sql.Connection conn) {
	this(Stampa_scadenzario_obbligazioniBulk.class, conn);
}
/**
 * Stampa_scadenzario_obbligazioniHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_scadenzario_obbligazioniHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Stampa_scadenzario_obbligazioniBulk.class, conn, persistentCache);
}
}
