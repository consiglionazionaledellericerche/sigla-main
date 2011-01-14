package it.cnr.contab.fondecon00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (06/03/2003 15.49.12)
 * @author: Gennaro Borriello
 */
public class Stampa_vpg_fondo_economaleHome extends Fondo_economaleHome {
public Stampa_vpg_fondo_economaleHome(Class aClass, java.sql.Connection conn) {
	super(aClass,conn);
}
public Stampa_vpg_fondo_economaleHome(Class aClass, java.sql.Connection conn,it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(aClass, conn, persistentCache);
}
public Stampa_vpg_fondo_economaleHome(java.sql.Connection conn) {
	super(Stampa_vpg_fondo_economaleBulk.class,conn);
}
public Stampa_vpg_fondo_economaleHome(java.sql.Connection conn,it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Stampa_vpg_fondo_economaleBulk.class,conn,persistentCache);
}
}
