package it.cnr.contab.coepcoan00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Saldo_coanHome extends BulkHome {
public Saldo_coanHome(java.sql.Connection conn) {
	super(Saldo_coanBulk.class,conn);
}
public Saldo_coanHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Saldo_coanBulk.class,conn,persistentCache);
}
}
