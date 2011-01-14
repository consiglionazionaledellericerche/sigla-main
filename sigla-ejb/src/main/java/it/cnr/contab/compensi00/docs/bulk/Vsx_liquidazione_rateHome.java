package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vsx_liquidazione_rateHome extends BulkHome {
public Vsx_liquidazione_rateHome(java.sql.Connection conn) {
	super(Vsx_liquidazione_rateBulk.class,conn);
}
public Vsx_liquidazione_rateHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Vsx_liquidazione_rateBulk.class,conn,persistentCache);
}
}
