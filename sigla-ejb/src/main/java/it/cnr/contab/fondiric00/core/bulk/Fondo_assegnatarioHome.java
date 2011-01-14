package it.cnr.contab.fondiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fondo_assegnatarioHome extends BulkHome {
public Fondo_assegnatarioHome(java.sql.Connection conn) {
	super(Fondo_assegnatarioBulk.class,conn);
}
public Fondo_assegnatarioHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Fondo_assegnatarioBulk.class,conn,persistentCache);
}
}
