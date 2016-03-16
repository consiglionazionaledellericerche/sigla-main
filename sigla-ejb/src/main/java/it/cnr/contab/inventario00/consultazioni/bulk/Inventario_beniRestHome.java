package it.cnr.contab.inventario00.consultazioni.bulk;
import it.cnr.contab.rest.bulk.RestServicesHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Inventario_beniRestHome extends RestServicesHome {
	
public Inventario_beniRestHome(java.sql.Connection conn) {
	super(Inventario_beniRestBulk.class,conn);
}
public Inventario_beniRestHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Inventario_beniRestBulk.class,conn,persistentCache);
}
@Override
public SQLBuilder selectByClause(UserContext usercontext,
		CompoundFindClause compoundfindclause) throws PersistencyException {
	SQLBuilder builder = super.selectByClause(usercontext, compoundfindclause);
	return super.addConditionCds(usercontext, builder, "cd_cds");
}
}
