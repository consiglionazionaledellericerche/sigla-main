package it.cnr.contab.fondecon00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_ass_mandato_fondo_ecoHome extends BulkHome {
public V_ass_mandato_fondo_ecoHome(java.sql.Connection conn) {
	super(V_ass_mandato_fondo_ecoBulk.class,conn);
}
public V_ass_mandato_fondo_ecoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_ass_mandato_fondo_ecoBulk.class,conn,persistentCache);
}
}
