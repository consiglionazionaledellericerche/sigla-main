package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Progetto_uoHome extends BulkHome {
public Progetto_uoHome(java.sql.Connection conn) {
	super(Progetto_uoBulk.class,conn);
}
public Progetto_uoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Progetto_uoBulk.class,conn,persistentCache);
}
}