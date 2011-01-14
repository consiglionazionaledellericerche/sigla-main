package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ProvinciaHome extends BulkHome {
public ProvinciaHome(java.sql.Connection conn) {
	super(ProvinciaBulk.class,conn);
}
public ProvinciaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(ProvinciaBulk.class,conn,persistentCache);
}
}
