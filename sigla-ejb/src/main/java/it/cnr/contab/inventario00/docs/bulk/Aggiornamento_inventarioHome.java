package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoHome;
/**
 * Insert the type's description here.
 * @author: Rosangela Pucciarelli
 */
public class Aggiornamento_inventarioHome extends Buono_carico_scaricoHome {
/**
 * Trasferimento_inventarioHome constructor comment.
 * @param conn java.sql.Connection
 */
public Aggiornamento_inventarioHome(java.sql.Connection conn) {
	super(conn);
}
/**
 * Trasferimento_inventarioHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Aggiornamento_inventarioHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(conn, persistentCache);
}

}
