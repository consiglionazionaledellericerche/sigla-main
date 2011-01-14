package it.cnr.contab.reports.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Print_spooler_paramHome extends BulkHome {
public Print_spooler_paramHome(java.sql.Connection conn) {
	super(Print_spooler_paramBulk.class,conn);
}
public Print_spooler_paramHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Print_spooler_paramBulk.class,conn,persistentCache);
}
}