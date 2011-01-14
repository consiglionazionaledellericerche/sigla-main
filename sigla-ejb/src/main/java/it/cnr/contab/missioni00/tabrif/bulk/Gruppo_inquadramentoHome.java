package it.cnr.contab.missioni00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Gruppo_inquadramentoHome extends BulkHome {
public Gruppo_inquadramentoHome(java.sql.Connection conn) {
	super(Gruppo_inquadramentoBulk.class,conn);
}
public Gruppo_inquadramentoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Gruppo_inquadramentoBulk.class,conn,persistentCache);
}
public java.util.List findGruppiInquadramento() throws IntrospectionException, PersistencyException{

	SQLBuilder sql = createSQLBuilder();

	return fetchAll(sql);
}
}
