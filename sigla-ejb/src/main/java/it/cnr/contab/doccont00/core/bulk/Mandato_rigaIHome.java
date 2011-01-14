package it.cnr.contab.doccont00.core.bulk;

public class Mandato_rigaIHome extends Mandato_rigaHome {
public Mandato_rigaIHome(java.sql.Connection conn) {
	super(Mandato_rigaIBulk.class,conn);
}
public Mandato_rigaIHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Mandato_rigaIBulk.class,conn, persistentCache);
}
}
