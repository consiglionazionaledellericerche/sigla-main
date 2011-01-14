package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Condizione_beneHome extends BulkHome {
public Condizione_beneHome(java.sql.Connection conn) {
	super(Condizione_beneBulk.class,conn);
}
public Condizione_beneHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Condizione_beneBulk.class,conn,persistentCache);
}
}
