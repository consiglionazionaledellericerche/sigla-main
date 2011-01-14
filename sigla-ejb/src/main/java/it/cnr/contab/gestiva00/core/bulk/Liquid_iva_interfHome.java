package it.cnr.contab.gestiva00.core.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Liquid_iva_interfHome extends BulkHome {
public Liquid_iva_interfHome(java.sql.Connection conn) {
	super(Liquid_iva_interfBulk.class,conn);
}

public Liquid_iva_interfHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Liquid_iva_interfBulk.class,conn,persistentCache);
}

}
