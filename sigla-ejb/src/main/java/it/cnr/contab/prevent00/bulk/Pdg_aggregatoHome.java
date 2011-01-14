package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Pdg_aggregatoHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Pdg_aggregatoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Pdg_aggregatoHome(java.sql.Connection conn) {
	super(Pdg_aggregatoBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Pdg_aggregatoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Pdg_aggregatoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Pdg_aggregatoBulk.class,conn,persistentCache);
}
}
