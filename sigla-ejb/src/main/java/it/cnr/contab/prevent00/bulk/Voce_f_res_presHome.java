package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Voce_f_res_presHome extends BulkHome {
public Voce_f_res_presHome(java.sql.Connection conn) {
	super(Voce_f_res_presBulk.class,conn);
}
public Voce_f_res_presHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Voce_f_res_presBulk.class,conn,persistentCache);
}
}
