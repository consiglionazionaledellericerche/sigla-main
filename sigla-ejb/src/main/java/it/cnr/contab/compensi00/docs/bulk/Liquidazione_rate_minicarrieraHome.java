package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;

/**
 * Insert the type's description here.
 * Creation date: (24/12/2002 11.51.51)
 * @author: Roberto Fantino
 */
public class Liquidazione_rate_minicarrieraHome extends Minicarriera_rataHome {
/**
 * Liquidazione_rate_minicarrieraHome constructor comment.
 * @param conn java.sql.Connection
 */
public Liquidazione_rate_minicarrieraHome(java.sql.Connection conn) {
	super(Liquidazione_rate_minicarrieraBulk.class, conn);
}
/**
 * Liquidazione_rate_minicarrieraHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Liquidazione_rate_minicarrieraHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Liquidazione_rate_minicarrieraBulk.class, conn, persistentCache);
}
}
