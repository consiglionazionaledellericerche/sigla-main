/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/11/2007
 */
package it.cnr.contab.inventario00.consultazioni.bulk;
import java.sql.Connection;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_inventario_utilizzatoriHome extends BulkHome {
	public V_inventario_utilizzatoriHome(Connection conn) {
		super(V_inventario_utilizzatoriBulk.class, conn);
	}
	public V_inventario_utilizzatoriHome(Connection conn, PersistentCache persistentCache) {
		super(V_inventario_utilizzatoriBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
	
		//	Se uo 999.000 in scrivania: visualizza tutto l'elenco
		if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
	
			sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(usercontext));
		}	
		return sql;
	}	
}