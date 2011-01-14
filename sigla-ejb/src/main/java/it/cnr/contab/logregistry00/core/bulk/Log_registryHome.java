package it.cnr.contab.logregistry00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Log_registryHome extends BulkHome {
public Log_registryHome(java.sql.Connection conn) {
	super(Log_registryBulk.class,conn);
}
public Log_registryHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Log_registryBulk.class,conn,persistentCache);
}
}
