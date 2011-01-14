/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/02/2010
 */
package it.cnr.contab.docamm00.intrastat.bulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.CRUDBP;

import java.sql.Connection;
public class Modalita_erogazioneHome extends BulkHome {
	public Modalita_erogazioneHome(Connection conn) {
		super(Modalita_erogazioneBulk.class, conn);
	}
	public Modalita_erogazioneHome(Connection conn, PersistentCache persistentCache) {
		super(Modalita_erogazioneBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
	    sql.addClause("AND","esercizio",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext));
		return sql;
	}
}