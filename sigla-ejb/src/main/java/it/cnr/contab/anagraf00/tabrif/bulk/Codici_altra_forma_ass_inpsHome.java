package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Codici_altra_forma_ass_inpsHome extends BulkHome {
public Codici_altra_forma_ass_inpsHome(java.sql.Connection conn) {
	super(Codici_altra_forma_ass_inpsBulk.class,conn);
}
public Codici_altra_forma_ass_inpsHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Codici_altra_forma_ass_inpsBulk.class,conn,persistentCache);
}
@Override
public SQLBuilder selectByClause(CompoundFindClause compoundfindclause) throws PersistencyException {
	SQLBuilder sql = super.selectByClause(compoundfindclause);
	sql.addSQLClause("AND","FL_CANCELLATO",sql.EQUALS, "N");
	return sql;
}
}
