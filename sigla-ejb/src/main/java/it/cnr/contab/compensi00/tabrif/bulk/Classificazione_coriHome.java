package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Classificazione_coriHome extends BulkHome {
public Classificazione_coriHome(java.sql.Connection conn) {
	super(Classificazione_coriBulk.class,conn);
}
public Classificazione_coriHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Classificazione_coriBulk.class,conn,persistentCache);
}
}
