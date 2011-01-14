package it.cnr.contab.fondiric00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_fondoHome extends BulkHome {
public Tipo_fondoHome(java.sql.Connection conn) {
	super(Tipo_fondoBulk.class,conn);
}
public Tipo_fondoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipo_fondoBulk.class,conn,persistentCache);
}
}
