package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Progetto_partner_esternoHome extends BulkHome {
public Progetto_partner_esternoHome(java.sql.Connection conn) {
	super(Progetto_partner_esternoBulk.class,conn);
}
public Progetto_partner_esternoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Progetto_partner_esternoBulk.class,conn,persistentCache);
}
}