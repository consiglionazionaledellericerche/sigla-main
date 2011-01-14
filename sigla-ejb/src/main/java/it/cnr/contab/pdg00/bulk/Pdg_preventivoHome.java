package it.cnr.contab.pdg00.bulk;

import it.cnr.contab.pdg00.comp.PdGPreventivoComponent;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Pdg_preventivoHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Pdg_preventivoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Pdg_preventivoHome(java.sql.Connection conn) {
	super(Pdg_preventivoBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Pdg_preventivoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Pdg_preventivoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Pdg_preventivoBulk.class,conn,persistentCache);
}
}
