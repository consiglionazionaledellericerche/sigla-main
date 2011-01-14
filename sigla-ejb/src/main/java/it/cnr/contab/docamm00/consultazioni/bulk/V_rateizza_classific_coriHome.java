/*
* Created by Generator 1.0
* Date 10/10/2005
*/
package it.cnr.contab.docamm00.consultazioni.bulk;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_rateizza_classific_coriHome extends BulkHome {
	public V_rateizza_classific_coriHome(java.sql.Connection conn) {
		super(V_rateizza_classific_coriBulk.class, conn);
	}
	public V_rateizza_classific_coriHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_rateizza_classific_coriBulk.class, conn, persistentCache);
	}

	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addSQLClause("AND","V_RATEIZZA_CLASSIFIC_CORI.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
		return sql;
	}
}