package it.cnr.contab.missioni00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (13/02/2003 10.08.41)
 * @author: Roberto Fantino
 */
public class Stampa_vpg_missioneHome extends MissioneHome {
/**
 * Stampa_vpg_missioneHome constructor comment.
 * @param conn java.sql.Connection
 */
public Stampa_vpg_missioneHome(Class aClass, java.sql.Connection conn) {
	super(aClass, conn);
}
/**
 * Stampa_vpg_missioneHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_vpg_missioneHome(Class aClass, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(aClass, conn, persistentCache);
}
/**
 * Stampa_vpg_missioneHome constructor comment.
 * @param conn java.sql.Connection
 */
public Stampa_vpg_missioneHome(java.sql.Connection conn) {
	super(Stampa_vpg_missioneBulk.class, conn);
}
/**
 * Stampa_vpg_missioneHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_vpg_missioneHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Stampa_vpg_missioneBulk.class, conn, persistentCache);
}
}
