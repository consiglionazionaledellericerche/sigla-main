package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Pdg_aggregato_etr_detHome extends BulkHome {
protected Pdg_aggregato_etr_detHome(Class clazz,java.sql.Connection connection) {
	super(clazz,connection);
}
protected Pdg_aggregato_etr_detHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
	super(clazz,connection,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Pdg_aggregato_etr_detHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Pdg_aggregato_etr_detHome(java.sql.Connection conn) {
	super(Pdg_aggregato_etr_detBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Pdg_aggregato_etr_detHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Pdg_aggregato_etr_detHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Pdg_aggregato_etr_detBulk.class,conn,persistentCache);
}
}
