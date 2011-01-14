package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Capoconto_finHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Capoconto_finHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Capoconto_finHome(java.sql.Connection conn) {
	super(Capoconto_finBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Capoconto_finHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Capoconto_finHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Capoconto_finBulk.class,conn,persistentCache);
}
}
