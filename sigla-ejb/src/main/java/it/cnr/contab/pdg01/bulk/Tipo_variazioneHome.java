/*
* Created by Generator 1.0
* Date 14/06/2006
*/
package it.cnr.contab.pdg01.bulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Tipo_variazioneHome extends BulkHome {
	public Tipo_variazioneHome(java.sql.Connection conn) {
		super(Tipo_variazioneBulk.class, conn);
	}
	public Tipo_variazioneHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Tipo_variazioneBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
	    sql.addClause("AND","esercizio",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext));
		return sql;
	}	
}