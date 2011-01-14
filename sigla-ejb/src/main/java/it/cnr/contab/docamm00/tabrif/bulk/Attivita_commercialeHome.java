package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Attivita_commercialeHome extends BulkHome {
public Attivita_commercialeHome(java.sql.Connection conn) {
	super(Attivita_commercialeBulk.class,conn);
}
public Attivita_commercialeHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Attivita_commercialeBulk.class,conn,persistentCache);
}
}
