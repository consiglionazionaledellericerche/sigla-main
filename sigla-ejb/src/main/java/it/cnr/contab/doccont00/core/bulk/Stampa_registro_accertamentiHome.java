package it.cnr.contab.doccont00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (27/01/2003 17.04.20)
 * @author: Roberto Fantino
 */
public class Stampa_registro_accertamentiHome extends AccertamentoHome {
/**
 * Stampa_registro_accertamentiHome constructor comment.
 * @param clazz java.lang.Class
 * @param conn java.sql.Connection
 */
public Stampa_registro_accertamentiHome(Class clazz, java.sql.Connection conn) {
	super(clazz, conn);
}
/**
 * Stampa_registro_accertamentiHome constructor comment.
 * @param clazz java.lang.Class
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_registro_accertamentiHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(clazz, conn, persistentCache);
}
/**
 * Stampa_registro_accertamentiHome constructor comment.
 * @param conn java.sql.Connection
 */
public Stampa_registro_accertamentiHome(java.sql.Connection conn) {
	super(Stampa_registro_accertamentiBulk.class, conn);
}
/**
 * Stampa_registro_accertamentiHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_registro_accertamentiHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Stampa_registro_accertamentiBulk.class, conn, persistentCache);
}
}
