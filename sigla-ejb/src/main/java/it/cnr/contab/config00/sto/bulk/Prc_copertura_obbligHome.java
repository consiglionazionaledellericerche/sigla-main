package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Prc_copertura_obbligHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Prc_copertura_obbligHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Prc_copertura_obbligHome(java.sql.Connection conn) {
	super(Prc_copertura_obbligBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Prc_copertura_obbligHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Prc_copertura_obbligHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Prc_copertura_obbligBulk.class,conn,persistentCache);
}
}
