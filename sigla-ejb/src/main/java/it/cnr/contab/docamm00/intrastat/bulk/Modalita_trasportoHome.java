package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Modalita_trasportoHome extends BulkHome {
public Modalita_trasportoHome(java.sql.Connection conn) {
	super(Modalita_trasportoBulk.class,conn);
}
public Modalita_trasportoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Modalita_trasportoBulk.class,conn,persistentCache);
}
public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
	SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
    sql.addClause("AND","esercizio",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext));
	return sql;
}	
}
