package it.cnr.contab.logregistry00.logs.bulk;

import it.cnr.contab.logregistry00.core.bulk.OggettoLogBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class L_abicabHome extends BulkHome {
public L_abicabHome(java.sql.Connection conn) {
	super(L_abicabBulk.class,conn);
}
public L_abicabHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(L_abicabBulk.class,conn,persistentCache);
}
}
