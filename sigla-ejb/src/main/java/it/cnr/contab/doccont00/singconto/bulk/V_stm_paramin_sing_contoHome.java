package it.cnr.contab.doccont00.singconto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_stm_paramin_sing_contoHome extends BulkHome {
public V_stm_paramin_sing_contoHome(java.sql.Connection conn) {
	super(V_stm_paramin_sing_contoBulk.class,conn);
}
public V_stm_paramin_sing_contoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_stm_paramin_sing_contoBulk.class,conn,persistentCache);
}
}
