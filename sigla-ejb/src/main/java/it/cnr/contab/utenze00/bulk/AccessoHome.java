package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class AccessoHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un AccessoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public AccessoHome(java.sql.Connection conn) {
	super(AccessoBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un AccessoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public AccessoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(AccessoBulk.class,conn,persistentCache);
}
}
