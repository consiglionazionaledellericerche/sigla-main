/*
* Creted by Generator 1.0
* Date 04/04/2005
*/
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_stampa_costi_personaleHome extends BulkHome {
	public V_stampa_costi_personaleHome(java.sql.Connection conn) {
		super(V_stampa_costi_personaleBulk.class, conn);
	}
	public V_stampa_costi_personaleHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_stampa_costi_personaleBulk.class, conn, persistentCache);
	}
	/*public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,((CNRUserContext) usercontext).getEsercizio());			
		//	Se uo 999.000 in scrivania: visualizza tutto l'elenco
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
		if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa()))
			sql.addSQLClause("AND", "UO", sql.EQUALS, ((CNRUserContext) usercontext).getCd_unita_organizzativa());
		return sql;	
	}*/

}

