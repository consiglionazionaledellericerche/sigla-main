/*
* Creted by Generator 1.0
* Date 28/01/2005
*/
package it.cnr.contab.consultazioni.bulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class ConsObbligazioniHome extends BulkHome {
	public ConsObbligazioniHome(java.sql.Connection conn) {
		super(ConsObbligazioniBulk.class, conn);
	}
	public ConsObbligazioniHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(ConsObbligazioniBulk.class, conn, persistentCache);
	}
	public ConsObbligazioniHome(Class clazz,java.sql.Connection conn) {
		super(clazz,conn);
	}
	public ConsObbligazioniHome(Class clazz,java.sql.Connection conn,PersistentCache persistentCache) {
		super( clazz,conn,persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
			throws PersistencyException
		{  
			SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
			Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
			//	Se uo 999.000 in scrivania: visualizza tutto l'elenco
			if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
				sql.addSQLClause("AND","CDS",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(usercontext));
				Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext)));
				if(!uoScrivania.isUoCds())
				  sql.addSQLClause("AND","UO",SQLBuilder.EQUALS,CNRUserContext.getCd_unita_organizzativa(usercontext));
			}	
			return sql; 
		}
}