package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_sospeso_rev_accertHome extends BulkHome {
public V_sospeso_rev_accertHome(java.sql.Connection conn) {
	super(V_sospeso_rev_accertBulk.class,conn);
}
public V_sospeso_rev_accertHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_sospeso_rev_accertBulk.class,conn,persistentCache);
}
}
