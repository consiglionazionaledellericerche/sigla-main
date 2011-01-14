package it.cnr.contab.missioni00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Missione_dettaglioHome extends BulkHome {
public Missione_dettaglioHome(java.sql.Connection conn) {
	super(Missione_dettaglioBulk.class,conn);
}
public Missione_dettaglioHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Missione_dettaglioBulk.class,conn,persistentCache);
}
}
