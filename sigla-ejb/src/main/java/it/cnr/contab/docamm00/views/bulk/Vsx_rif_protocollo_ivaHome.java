package it.cnr.contab.docamm00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vsx_rif_protocollo_ivaHome extends BulkHome {
public Vsx_rif_protocollo_ivaHome(java.sql.Connection conn) {
	super(Vsx_rif_protocollo_ivaBulk.class,conn);
}
public Vsx_rif_protocollo_ivaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Vsx_rif_protocollo_ivaBulk.class,conn,persistentCache);
}
}
