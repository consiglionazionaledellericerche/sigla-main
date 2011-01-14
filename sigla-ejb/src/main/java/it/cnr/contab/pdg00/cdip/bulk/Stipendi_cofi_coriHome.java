package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Stipendi_cofi_coriHome extends BulkHome {
public Stipendi_cofi_coriHome(java.sql.Connection conn) {
	super(Stipendi_cofi_coriBulk.class,conn);
}
public Stipendi_cofi_coriHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Stipendi_cofi_coriBulk.class,conn,persistentCache);
}
}
