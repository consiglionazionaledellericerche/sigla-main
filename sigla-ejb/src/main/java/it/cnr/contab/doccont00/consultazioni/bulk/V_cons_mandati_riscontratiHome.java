/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 01/10/2007
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.sql.Connection;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_cons_mandati_riscontratiHome extends BulkHome {
	public V_cons_mandati_riscontratiHome(Connection conn) {
		super(V_cons_mandati_riscontratiBulk.class, conn);
	}
	public V_cons_mandati_riscontratiHome(Connection conn, PersistentCache persistentCache) {
		super(V_cons_mandati_riscontratiBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
	throws PersistencyException
{
	SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
	
	
	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
	if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
	  sql.addSQLClause("AND","V_CONS_MANDATI_RISCONTRATI.CD_CDS",sql.EQUALS,CNRUserContext.getCd_cds(usercontext));
	}
	sql.addSQLClause("AND","V_CONS_MANDATI_RISCONTRATI.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
	return sql;
}	
}