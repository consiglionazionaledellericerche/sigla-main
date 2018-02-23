package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Gruppo_linea_attivitaHome extends BulkHome {
/**
 * Costrutture gruppo linea di attività Home
 *
 * @param conn connessione db
 */
public Gruppo_linea_attivitaHome(java.sql.Connection conn) {
	super(Gruppo_linea_attivitaBulk.class,conn);
}
/**
 * Costrutture gruppo linea di attività Home
 *
 * @param conn connessione db
 * @param persistentCache cache modelli
 */
public Gruppo_linea_attivitaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Gruppo_linea_attivitaBulk.class,conn,persistentCache);
}
}
