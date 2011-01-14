package it.cnr.contab.doccont00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_consegnaHome extends BulkHome {
public Tipo_consegnaHome(java.sql.Connection conn) {
	super(Tipo_consegnaBulk.class,conn);
}
public Tipo_consegnaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipo_consegnaBulk.class,conn,persistentCache);
}
}
