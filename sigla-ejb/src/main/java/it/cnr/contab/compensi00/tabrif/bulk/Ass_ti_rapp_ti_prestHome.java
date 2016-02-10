package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_ti_rapp_ti_prestHome extends BulkHome {
public Ass_ti_rapp_ti_prestHome(java.sql.Connection conn) {
	super(Ass_ti_rapp_ti_prestBulk.class,conn);
}
public Ass_ti_rapp_ti_prestHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ass_ti_rapp_ti_prestBulk.class,conn,persistentCache);
}
}
