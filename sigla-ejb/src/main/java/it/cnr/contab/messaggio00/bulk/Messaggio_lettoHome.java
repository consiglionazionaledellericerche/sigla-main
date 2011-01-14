package it.cnr.contab.messaggio00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Messaggio_lettoHome extends BulkHome {
public Messaggio_lettoHome(java.sql.Connection conn) {
	super(Messaggio_lettoBulk.class,conn);
}
public Messaggio_lettoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Messaggio_lettoBulk.class,conn,persistentCache);
}
}
