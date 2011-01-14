package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_cdp_stato_mensilitaHome extends BulkHome {
public V_cdp_stato_mensilitaHome(java.sql.Connection conn) {
	super(V_cdp_stato_mensilitaBulk.class,conn);
}
public V_cdp_stato_mensilitaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_cdp_stato_mensilitaBulk.class,conn,persistentCache);
}
}
