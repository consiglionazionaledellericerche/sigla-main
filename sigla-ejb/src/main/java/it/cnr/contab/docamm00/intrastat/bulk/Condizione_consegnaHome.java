package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Condizione_consegnaHome extends BulkHome {
public Condizione_consegnaHome(java.sql.Connection conn) {
	super(Condizione_consegnaBulk.class,conn);
}
public Condizione_consegnaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Condizione_consegnaBulk.class,conn,persistentCache);
}
public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
	SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
    sql.addClause("AND","esercizio",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext));
	return sql;
}	
}
