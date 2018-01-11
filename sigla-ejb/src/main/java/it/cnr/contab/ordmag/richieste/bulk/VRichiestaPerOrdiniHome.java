/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/05/2017
 */
package it.cnr.contab.ordmag.richieste.bulk;
import java.sql.Connection;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class VRichiestaPerOrdiniHome extends BulkHome {
	public VRichiestaPerOrdiniHome(Connection conn) {
		super(VRichiestaPerOrdiniBulk.class, conn);
	}
	public VRichiestaPerOrdiniHome(Connection conn, PersistentCache persistentCache) {
		super(VRichiestaPerOrdiniBulk.class, conn, persistentCache);
	}
	@Override
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
			throws PersistencyException {
		// TODO Auto-generated method stub
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addOrderBy("cd_cds");
		sql.addOrderBy("cd_unita_operativa");
		sql.addOrderBy("esercizio");
		sql.addOrderBy("cd_numeratore");
		sql.addOrderBy("numero");
		sql.addOrderBy("riga");
		return sql;
	}
}