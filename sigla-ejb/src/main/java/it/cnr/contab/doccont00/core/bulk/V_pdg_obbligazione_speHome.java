package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_pdg_obbligazione_speHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_pdg_obbligazione_speHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public V_pdg_obbligazione_speHome(java.sql.Connection conn) {
	super(V_pdg_obbligazione_speBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_pdg_obbligazione_speHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public V_pdg_obbligazione_speHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_pdg_obbligazione_speBulk.class,conn,persistentCache);
}
}
