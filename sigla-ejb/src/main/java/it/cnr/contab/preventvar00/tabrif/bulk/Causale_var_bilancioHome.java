package it.cnr.contab.preventvar00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Causale_var_bilancioHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Causale_var_bilancioHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Causale_var_bilancioHome(java.sql.Connection conn) {
	super(Causale_var_bilancioBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Causale_var_bilancioHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Causale_var_bilancioHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Causale_var_bilancioBulk.class,conn,persistentCache);
}
}
