/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 14/03/2016
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.contab.rest.bulk.RestServicesHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;
public class CnrIfacDettObbligazioniHome extends RestServicesHome {
	public CnrIfacDettObbligazioniHome(Connection conn) {
		super(CnrIfacDettObbligazioniBulk.class, conn);
	}
	public CnrIfacDettObbligazioniHome(Connection conn, PersistentCache persistentCache) {
		super(CnrIfacDettObbligazioniBulk.class, conn, persistentCache);
	}
	@Override
	public SQLBuilder selectByClause(UserContext usercontext,
			CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder builder = super.selectByClause(usercontext, compoundfindclause);
		return super.addConditionCds(usercontext, builder, "cdCds");
	}
}