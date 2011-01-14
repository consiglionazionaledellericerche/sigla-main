package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Area_scientificaHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Area_scientificaHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Area_scientificaHome(java.sql.Connection conn) {
	super(Area_scientificaBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Area_scientificaHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Area_scientificaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Area_scientificaBulk.class,conn,persistentCache);
}
}
