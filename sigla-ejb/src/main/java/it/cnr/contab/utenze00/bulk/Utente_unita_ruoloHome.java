package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Utente_unita_ruoloHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Utente_unita_ruoloHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Utente_unita_ruoloHome(java.sql.Connection conn) {
	super(Utente_unita_ruoloBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Utente_unita_ruoloHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Utente_unita_ruoloHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Utente_unita_ruoloBulk.class,conn,persistentCache);
}
}
