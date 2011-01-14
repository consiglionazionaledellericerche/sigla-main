package it.cnr.contab.cori00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Liquid_coriHome extends BulkHome {
public Liquid_coriHome(java.sql.Connection conn) {
	super(Liquid_coriBulk.class,conn);
}
public Liquid_coriHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Liquid_coriBulk.class,conn,persistentCache);
}
}
