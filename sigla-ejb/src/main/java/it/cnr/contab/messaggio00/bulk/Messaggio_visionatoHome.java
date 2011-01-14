package it.cnr.contab.messaggio00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Messaggio_visionatoHome extends BulkHome {
public Messaggio_visionatoHome(java.sql.Connection conn) {
	super(Messaggio_visionatoBulk.class,conn);
}
public Messaggio_visionatoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Messaggio_visionatoBulk.class,conn,persistentCache);
}
}
