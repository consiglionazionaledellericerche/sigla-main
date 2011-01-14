package it.cnr.contab.cori00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_liquid_capitoli_coriHome extends BulkHome {
public V_liquid_capitoli_coriHome(java.sql.Connection conn) {
	super(V_liquid_capitoli_coriBulk.class,conn);
}
public V_liquid_capitoli_coriHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_liquid_capitoli_coriBulk.class,conn,persistentCache);
}
}
