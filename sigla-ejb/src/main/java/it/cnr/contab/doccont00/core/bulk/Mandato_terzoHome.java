package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Mandato_terzoHome extends BulkHome {
public Mandato_terzoHome(Class clazz, java.sql.Connection conn) {
	super(clazz,conn);
}
public Mandato_terzoHome(Class clazz, java.sql.Connection conn,PersistentCache persistentCache) {
	super(clazz,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Mandato_terzoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Mandato_terzoHome(java.sql.Connection conn) {
	super(Mandato_terzoBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Mandato_terzoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Mandato_terzoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Mandato_terzoBulk.class,conn,persistentCache);
}
}
