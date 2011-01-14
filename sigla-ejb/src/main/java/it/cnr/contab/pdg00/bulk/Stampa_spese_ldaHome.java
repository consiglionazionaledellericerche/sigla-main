package it.cnr.contab.pdg00.bulk;

import it.cnr.jada.persistency.PersistentCache;

/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 16.03.39)
 * @author: Roberto Fantino
 */
public abstract class Stampa_spese_ldaHome extends Pdg_preventivo_spe_detHome {
protected Stampa_spese_ldaHome(Class clazz,java.sql.Connection connection) {
	super(clazz,connection);
}
protected Stampa_spese_ldaHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
	super(clazz,connection,persistentCache);
}
public Stampa_spese_ldaHome(java.sql.Connection conn) {
	super(Stampa_spese_ldaBulk.class, conn);
}
public Stampa_spese_ldaHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Stampa_spese_ldaBulk.class, conn, persistentCache);
}
}
