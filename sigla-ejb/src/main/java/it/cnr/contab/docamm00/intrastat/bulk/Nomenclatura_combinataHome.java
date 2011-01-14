package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Nomenclatura_combinataHome extends BulkHome {
public Nomenclatura_combinataHome(java.sql.Connection conn) {
	super(Nomenclatura_combinataBulk.class,conn);
}
public Nomenclatura_combinataHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Nomenclatura_combinataBulk.class,conn,persistentCache);
}
public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException {
	try {
		((Nomenclatura_combinataBulk)bulk).setId_nomenclatura_combinata(
				new Long(
				((Long)findAndLockMax( bulk, "id_nomenclatura_combinata", new Long(0) )).longValue()+1
			)
		);
	} catch(it.cnr.jada.bulk.BusyResourceException e) {
		throw new PersistencyException(e);
	}
}

public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
	SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
    sql.addClause("AND","esercizio",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext));
	return sql;
}	
}
