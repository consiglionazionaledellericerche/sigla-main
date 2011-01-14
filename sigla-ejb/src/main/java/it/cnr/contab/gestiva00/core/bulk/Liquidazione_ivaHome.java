package it.cnr.contab.gestiva00.core.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Liquidazione_ivaHome extends BulkHome {
public Liquidazione_ivaHome(java.sql.Connection conn) {
	super(Liquidazione_ivaBulk.class,conn);
}
public Liquidazione_ivaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Liquidazione_ivaBulk.class,conn,persistentCache);
}

}
