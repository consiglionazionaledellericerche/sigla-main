/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 14/03/2016
 */
package it.cnr.contab.docamm00.consultazioni.bulk;
import java.sql.Connection;

import it.cnr.contab.rest.bulk.RestServicesHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class CnrIfacFatturaPassivaRigaHome extends RestServicesHome {
	public CnrIfacFatturaPassivaRigaHome(Connection conn) {
		super(CnrIfacFatturaPassivaRigaBulk.class, conn);
	}
	public CnrIfacFatturaPassivaRigaHome(Connection conn, PersistentCache persistentCache) {
		super(CnrIfacFatturaPassivaRigaBulk.class, conn, persistentCache);
	}
	@Override
	public SQLBuilder selectByClause(UserContext usercontext,
			CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder builder = super.selectByClause(usercontext, compoundfindclause);
		return super.addConditionCds(usercontext, builder, "cdCds");
	}
}