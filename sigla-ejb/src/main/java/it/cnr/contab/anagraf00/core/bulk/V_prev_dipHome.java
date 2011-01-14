package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_prev_dipHome extends BulkHome {
public V_prev_dipHome(java.sql.Connection conn) {
	super(V_prev_dipBulk.class,conn);
}
public V_prev_dipHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_prev_dipBulk.class,conn,persistentCache);
}
}
