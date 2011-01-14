package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_dipendenteHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_dipendenteHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public V_dipendenteHome(java.sql.Connection conn) {
	super(V_dipendenteBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_dipendenteHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public V_dipendenteHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_dipendenteBulk.class,conn,persistentCache);
}

}
