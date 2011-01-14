/*
* Creted by Generator 1.0
* Date 01/03/2005
*/
package it.cnr.contab.inventario00.consultazioni.bulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class ElencoinventariobeniHome extends BulkHome {
	public ElencoinventariobeniHome(java.sql.Connection conn) {
		super(ElencoinventariobeniBulk.class, conn);
	}
	public ElencoinventariobeniHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(ElencoinventariobeniBulk.class, conn, persistentCache);
	}
	
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
        //	Se uo 999.000 in scrivania: visualizza tutto l'elenco
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
		if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
		  sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA = ?");
		  sql.addParameter(((CNRUserContext) usercontext).getCd_unita_organizzativa() ,java.sql.Types.VARCHAR,0);
		}
		
		return sql;
	}
	
}