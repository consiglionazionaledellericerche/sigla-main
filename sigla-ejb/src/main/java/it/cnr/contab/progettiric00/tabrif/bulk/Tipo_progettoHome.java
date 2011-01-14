package it.cnr.contab.progettiric00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_progettoHome extends BulkHome {
public Tipo_progettoHome(java.sql.Connection conn) {
	super(Tipo_progettoBulk.class,conn);
}
public Tipo_progettoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipo_progettoBulk.class,conn,persistentCache);
}
}
