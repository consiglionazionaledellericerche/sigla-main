/*
* Creted by Generator 1.0
* Date 20/04/2005
*/
package it.cnr.contab.pdg00.consultazioni.bulk;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_pdg_speHome extends BulkHome {
	public V_pdg_speHome(java.sql.Connection conn) {
		super(V_pdg_speBulk.class, conn);
	}
	public V_pdg_speHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_pdg_speBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addSQLClause("AND","esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(usercontext));
		//	Se uo 999.000 in scrivania: visualizza tutto l'elenco
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
		Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext)));
		if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
			if(uoScrivania.isUoCds())
			  sql.addSQLClause("AND","cds",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(usercontext));
			else
			  sql.addSQLClause("AND","uo",SQLBuilder.EQUALS,CNRUserContext.getCd_unita_organizzativa(usercontext));  
		}	
		return sql;
	}	
}