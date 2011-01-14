package it.cnr.contab.docamm00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_stm_paramin_ft_attivaHome extends BulkHome {
public V_stm_paramin_ft_attivaHome(java.sql.Connection conn) {
	super(V_stm_paramin_ft_attivaBulk.class,conn);
}
public V_stm_paramin_ft_attivaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_stm_paramin_ft_attivaBulk.class,conn,persistentCache);
}
}
