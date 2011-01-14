package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Classificazione_anagHome extends BulkHome {
public Classificazione_anagHome(java.sql.Connection conn) {
	super(Classificazione_anagBulk.class,conn);
}
public Classificazione_anagHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Classificazione_anagBulk.class,conn,persistentCache);
}
}
