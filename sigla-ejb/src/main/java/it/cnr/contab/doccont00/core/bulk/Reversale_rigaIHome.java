package it.cnr.contab.doccont00.core.bulk;

public class Reversale_rigaIHome extends Reversale_rigaHome {
public Reversale_rigaIHome(Class clazz, java.sql.Connection conn) {
	super(clazz, conn);
}
public Reversale_rigaIHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(clazz, conn, persistentCache);
}
public Reversale_rigaIHome(java.sql.Connection conn) {
	super(Reversale_rigaIBulk.class,conn);
}
public Reversale_rigaIHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Reversale_rigaIBulk.class,conn, persistentCache);
}
}
