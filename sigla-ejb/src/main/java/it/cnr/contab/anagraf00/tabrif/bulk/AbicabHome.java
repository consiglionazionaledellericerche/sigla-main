package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class AbicabHome extends BulkHome {
public AbicabHome(java.sql.Connection conn) {
	super(AbicabBulk.class,conn);
}
public AbicabHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(AbicabBulk.class,conn,persistentCache);
}
public SQLBuilder createSQLBuilder() {
 
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "fl_cancellato", sql.EQUALS,Boolean.FALSE);
	return sql;
}

}
