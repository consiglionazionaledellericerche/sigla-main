package it.cnr.contab.cori00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Liquid_gruppo_coriHome extends BulkHome {
/**
  *  Costruttore utilizzato dalla sottoclasse <code>Liquid_gruppo_coriIHome</code>.
  *
**/  
public Liquid_gruppo_coriHome(Class clazz, java.sql.Connection conn) {
	super(clazz,conn);
}
/**
  *  Costruttore utilizzato dalla sottoclasse <code>Liquid_gruppo_coriIHome</code>.
  *
**/
public Liquid_gruppo_coriHome(Class clazz, java.sql.Connection conn,PersistentCache persistentCache) {
	super(clazz,conn,persistentCache);
}
public Liquid_gruppo_coriHome(java.sql.Connection conn) {
	super(Liquid_gruppo_coriBulk.class,conn);
}
public Liquid_gruppo_coriHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Liquid_gruppo_coriBulk.class,conn,persistentCache);
}
}
