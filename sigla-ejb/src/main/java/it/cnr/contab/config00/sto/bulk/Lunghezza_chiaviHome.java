package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Lunghezza_chiaviHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Lunghezza_chiaviHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Lunghezza_chiaviHome(java.sql.Connection conn) {
	super(Lunghezza_chiaviBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Lunghezza_chiaviHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Lunghezza_chiaviHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Lunghezza_chiaviBulk.class,conn,persistentCache);
}
}
