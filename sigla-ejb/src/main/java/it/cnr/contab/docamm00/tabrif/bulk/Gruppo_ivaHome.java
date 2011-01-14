package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Gruppo_ivaHome extends BulkHome {
public Gruppo_ivaHome(java.sql.Connection conn) {
	super(Gruppo_ivaBulk.class,conn);
}
public Gruppo_ivaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Gruppo_ivaBulk.class,conn,persistentCache);
}
}
