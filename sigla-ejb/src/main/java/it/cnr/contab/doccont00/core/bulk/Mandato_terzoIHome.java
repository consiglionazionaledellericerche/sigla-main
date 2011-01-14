package it.cnr.contab.doccont00.core.bulk;

public class Mandato_terzoIHome extends Mandato_terzoHome {
public Mandato_terzoIHome(java.sql.Connection conn) {
	super(Mandato_terzoIBulk.class, conn);
}
public Mandato_terzoIHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Mandato_terzoIBulk.class, conn, persistentCache);
}
}
