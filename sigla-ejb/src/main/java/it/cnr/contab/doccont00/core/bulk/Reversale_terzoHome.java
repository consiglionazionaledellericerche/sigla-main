package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Reversale_terzoHome extends BulkHome {
public Reversale_terzoHome(Class clazz, java.sql.Connection conn) {
	super(clazz,conn);
}
public Reversale_terzoHome(Class clazz, java.sql.Connection conn,PersistentCache persistentCache) {
	super(clazz,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Reversale_terzoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Reversale_terzoHome(java.sql.Connection conn) {
	super(Reversale_terzoBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Reversale_terzoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Reversale_terzoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Reversale_terzoBulk.class,conn,persistentCache);
}
}
