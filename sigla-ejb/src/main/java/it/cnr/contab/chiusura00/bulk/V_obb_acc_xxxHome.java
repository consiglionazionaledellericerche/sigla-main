package it.cnr.contab.chiusura00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_obb_acc_xxxHome extends BulkHome {
public V_obb_acc_xxxHome(java.sql.Connection conn) {
	super(V_obb_acc_xxxBulk.class,conn);
}
public V_obb_acc_xxxHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_obb_acc_xxxBulk.class,conn,persistentCache);
}
}
