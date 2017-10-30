package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Bene_servizioHome extends BulkHome {
public Bene_servizioHome(java.sql.Connection conn) {
	super(Bene_servizioBulk.class,conn);
}
public Bene_servizioHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Bene_servizioBulk.class,conn,persistentCache);
}
@Override
public SQLBuilder createSQLBuilder() {
	SQLBuilder sql=super.createSQLBuilder();
//		sql.addClause("AND", "fl_valido", SQLBuilder.EQUALS, true);
	return sql;
}
}
