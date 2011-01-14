package it.cnr.contab.cori00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Liquid_gruppo_cori_detHome extends BulkHome {
/**
  *  Costruttore utilizzato dalla sottoclasse <code>Liquid_gruppo_cori_detIHome</code>.
  *
**/  
public Liquid_gruppo_cori_detHome(Class clazz, java.sql.Connection conn) {
	super(clazz,conn);
}
/**
  *  Costruttore utilizzato dalla sottoclasse <code>Liquid_gruppo_cori_detIHome</code>.
  *
**/ 
public Liquid_gruppo_cori_detHome(Class clazz, java.sql.Connection conn,PersistentCache persistentCache) {
	super(clazz,conn,persistentCache);
}
public Liquid_gruppo_cori_detHome(java.sql.Connection conn) {
	super(Liquid_gruppo_cori_detBulk.class,conn);
}
public Liquid_gruppo_cori_detHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Liquid_gruppo_cori_detBulk.class,conn,persistentCache);
}
}
