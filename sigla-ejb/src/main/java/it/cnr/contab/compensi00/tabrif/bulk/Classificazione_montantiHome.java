package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Classificazione_montantiHome extends BulkHome {
public Classificazione_montantiHome(java.sql.Connection conn) {
	super(Classificazione_montantiBulk.class,conn);
}
public Classificazione_montantiHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Classificazione_montantiBulk.class,conn,persistentCache);
}
}
