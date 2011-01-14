package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Categoria_beneHome extends BulkHome {
public Categoria_beneHome(java.sql.Connection conn) {
	super(Categoria_beneBulk.class,conn);
}
public Categoria_beneHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Categoria_beneBulk.class,conn,persistentCache);
}
}
