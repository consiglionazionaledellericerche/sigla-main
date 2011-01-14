package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class CapHome extends BulkHome {
public CapHome(java.sql.Connection conn) {
	super(CapBulk.class,conn);
}
public CapHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(CapBulk.class,conn,persistentCache);
}
}
