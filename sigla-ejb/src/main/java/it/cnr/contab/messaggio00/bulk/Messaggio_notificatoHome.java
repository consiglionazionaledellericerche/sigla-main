package it.cnr.contab.messaggio00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Messaggio_notificatoHome extends BulkHome {
public Messaggio_notificatoHome(java.sql.Connection conn) {
	super(Messaggio_notificatoBulk.class,conn);
}
public Messaggio_notificatoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Messaggio_notificatoBulk.class,conn,persistentCache);
}
}
