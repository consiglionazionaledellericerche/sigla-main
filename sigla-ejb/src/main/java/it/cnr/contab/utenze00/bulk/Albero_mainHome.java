package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Albero_mainHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Albero_mainHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Albero_mainHome(java.sql.Connection conn) {
	super(Albero_mainBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Albero_mainHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Albero_mainHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Albero_mainBulk.class,conn,persistentCache);
}
}
