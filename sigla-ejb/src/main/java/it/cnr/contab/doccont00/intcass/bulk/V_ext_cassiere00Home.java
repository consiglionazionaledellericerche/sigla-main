package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_ext_cassiere00Home extends BulkHome {
public V_ext_cassiere00Home(java.sql.Connection conn) {
	super(V_ext_cassiere00Bulk.class,conn);
}
public V_ext_cassiere00Home(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_ext_cassiere00Bulk.class,conn,persistentCache);
}
}
