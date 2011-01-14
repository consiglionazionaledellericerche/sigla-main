package it.cnr.contab.missioni00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class RimborsoHome extends BulkHome {
public RimborsoHome(java.sql.Connection conn) {
	super(RimborsoBulk.class,conn);
}
public RimborsoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(RimborsoBulk.class,conn,persistentCache);
}
}
