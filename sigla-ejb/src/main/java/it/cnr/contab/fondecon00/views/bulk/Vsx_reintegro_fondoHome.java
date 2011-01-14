package it.cnr.contab.fondecon00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vsx_reintegro_fondoHome extends BulkHome {
public Vsx_reintegro_fondoHome(java.sql.Connection conn) {
	super(Vsx_reintegro_fondoBulk.class,conn);
}
public Vsx_reintegro_fondoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Vsx_reintegro_fondoBulk.class,conn,persistentCache);
}
}
