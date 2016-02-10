/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 29/08/2013
 */
package it.cnr.contab.consultazioni.bulk;
import java.sql.Connection;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class VGaeTotaleStoricoSpeHome extends BulkHome {
	public VGaeTotaleStoricoSpeHome(Connection conn) {
		super(VGaeTotaleStoricoSpeBulk.class, conn);
	}
	public VGaeTotaleStoricoSpeHome(Connection conn, PersistentCache persistentCache) {
		super(VGaeTotaleStoricoSpeBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
			throws PersistencyException
		{  
			SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
			Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
			//	Se uo 999.000 in scrivania: visualizza tutto l'elenco
			if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
				sql.addSQLClause("AND","CDR",sql.EQUALS,CNRUserContext.getCd_cdr(usercontext));	
			}
			return sql;
		}
}