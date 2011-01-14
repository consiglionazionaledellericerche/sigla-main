package it.cnr.contab.config00.tabnum.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Numerazione_baseHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Numerazione_baseHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Numerazione_baseHome(java.sql.Connection conn) {
	super(Numerazione_baseBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Numerazione_baseHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Numerazione_baseHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Numerazione_baseBulk.class,conn,persistentCache);
}
}
