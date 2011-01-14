package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_anagrafico_terzoHome extends BulkHome {
public V_anagrafico_terzoHome(Class clazz,java.sql.Connection conn) {
	super(clazz,conn);
}
public V_anagrafico_terzoHome(Class clazz,java.sql.Connection conn,PersistentCache persistentCache) {
	super( clazz,conn,persistentCache);
}
public V_anagrafico_terzoHome(java.sql.Connection conn) {
	super(V_anagrafico_terzoBulk.class,conn);
}
public V_anagrafico_terzoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_anagrafico_terzoBulk.class,conn,persistentCache);
}
}
