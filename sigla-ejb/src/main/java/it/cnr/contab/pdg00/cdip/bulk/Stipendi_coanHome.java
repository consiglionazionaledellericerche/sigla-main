package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Stipendi_coanHome extends BulkHome {
public Stipendi_coanHome(java.sql.Connection conn) {
	super(Stipendi_coanBulk.class,conn);
}
public Stipendi_coanHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Stipendi_coanBulk.class,conn,persistentCache);
}
}
