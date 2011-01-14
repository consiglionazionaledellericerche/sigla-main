package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ext_cassiere00Home extends BulkHome {
public Ext_cassiere00Home(java.sql.Connection conn) {
	super(Ext_cassiere00Bulk.class,conn);
}
public Ext_cassiere00Home(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ext_cassiere00Bulk.class,conn,persistentCache);
}
}
