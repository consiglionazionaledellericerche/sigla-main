package it.cnr.contab.cori00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vsx_liquidazione_coriHome extends BulkHome {
public Vsx_liquidazione_coriHome(java.sql.Connection conn) {
	super(Vsx_liquidazione_coriBulk.class,conn);
}
public Vsx_liquidazione_coriHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Vsx_liquidazione_coriBulk.class,conn,persistentCache);
}
}
