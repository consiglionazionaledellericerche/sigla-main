package it.cnr.contab.missioni00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_missioneHome extends BulkHome {
public Tipo_missioneHome(java.sql.Connection conn) {
	super(Tipo_missioneBulk.class,conn);
}
public Tipo_missioneHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipo_missioneBulk.class,conn,persistentCache);
}
@Override
public SQLBuilder createSQLBuilder() {
 
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "fl_valido", sql.EQUALS,Boolean.TRUE);
	return sql;
}
}
