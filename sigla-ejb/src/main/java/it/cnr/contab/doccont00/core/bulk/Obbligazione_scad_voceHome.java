package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;

public class Obbligazione_scad_voceHome extends BulkHome {
public Obbligazione_scad_voceHome(Class clazz, java.sql.Connection conn) {
	super(clazz,conn);
}
public Obbligazione_scad_voceHome(Class clazz, java.sql.Connection conn,PersistentCache persistentCache) {
	super(clazz,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Obbligazione_scad_voceHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Obbligazione_scad_voceHome(java.sql.Connection conn) {
	super(Obbligazione_scad_voceBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Obbligazione_scad_voceHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Obbligazione_scad_voceHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Obbligazione_scad_voceBulk.class,conn,persistentCache);
}
}
