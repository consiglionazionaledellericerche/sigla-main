package it.cnr.contab.logregistry00.logs.bulk;

import it.cnr.contab.logregistry00.core.bulk.OggettoLogBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class L_bancaHome extends BulkHome {
public L_bancaHome(java.sql.Connection conn) {
	super(L_bancaBulk.class,conn);
}
public L_bancaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(L_bancaBulk.class,conn,persistentCache);
}
}
