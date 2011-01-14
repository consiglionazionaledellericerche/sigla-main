package it.cnr.contab.missioni00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Missione_tappaHome extends BulkHome {
public Missione_tappaHome(java.sql.Connection conn) {
	super(Missione_tappaBulk.class,conn);
}
public Missione_tappaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Missione_tappaBulk.class,conn,persistentCache);
}
}
