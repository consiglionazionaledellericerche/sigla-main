package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_movimento_inventarioHome extends BulkHome {
public Tipo_movimento_inventarioHome(java.sql.Connection conn) {
	super(Tipo_movimento_inventarioBulk.class,conn);
}
public Tipo_movimento_inventarioHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipo_movimento_inventarioBulk.class,conn,persistentCache);
}
}
