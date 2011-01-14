package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ruolo_accessoHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Ruolo_accessoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Ruolo_accessoHome(java.sql.Connection conn) {
	super(Ruolo_accessoBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Ruolo_accessoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Ruolo_accessoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ruolo_accessoBulk.class,conn,persistentCache);
}
}
