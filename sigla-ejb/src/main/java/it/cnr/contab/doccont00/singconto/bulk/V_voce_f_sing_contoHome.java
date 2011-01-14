package it.cnr.contab.doccont00.singconto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_voce_f_sing_contoHome extends BulkHome {
public V_voce_f_sing_contoHome(java.sql.Connection conn) {
	super(V_voce_f_sing_contoBulk.class,conn);
}
public V_voce_f_sing_contoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_voce_f_sing_contoBulk.class,conn,persistentCache);
}
}
