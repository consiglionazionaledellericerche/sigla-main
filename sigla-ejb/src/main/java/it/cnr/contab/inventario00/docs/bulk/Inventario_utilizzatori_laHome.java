package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Inventario_utilizzatori_laHome extends BulkHome {
public Inventario_utilizzatori_laHome(java.sql.Connection conn) {
	super(Inventario_utilizzatori_laBulk.class,conn);
}
public Inventario_utilizzatori_laHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Inventario_utilizzatori_laBulk.class,conn,persistentCache);
}

}
