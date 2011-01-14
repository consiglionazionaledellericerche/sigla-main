package it.cnr.contab.coepcoan00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Saldo_cogeHome extends BulkHome {
public Saldo_cogeHome(java.sql.Connection conn) {
	super(Saldo_cogeBulk.class,conn);
}
public Saldo_cogeHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Saldo_cogeBulk.class,conn,persistentCache);
}
}
