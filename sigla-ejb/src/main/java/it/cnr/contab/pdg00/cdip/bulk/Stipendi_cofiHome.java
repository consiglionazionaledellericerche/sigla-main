package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Stipendi_cofiHome extends BulkHome {
public Stipendi_cofiHome(java.sql.Connection conn) {
	super(Stipendi_cofiBulk.class,conn);
}
public Stipendi_cofiHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Stipendi_cofiBulk.class,conn,persistentCache);
}
public java.util.Collection findStipendiCofiAnno(UserContext context) throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,((it.cnr.contab.utenze00.bp.CNRUserContext)context).getEsercizio());
	sql.addOrderBy("MESE DESC");
	return fetchAll(sql);
}
}
