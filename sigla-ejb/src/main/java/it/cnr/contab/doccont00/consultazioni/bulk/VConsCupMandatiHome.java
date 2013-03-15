/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 20/02/2013
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
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
public class VConsCupMandatiHome extends BulkHome {
	public VConsCupMandatiHome(Connection conn) {
		super(VConsCupMandatiBulk.class, conn);
	}
	public VConsCupMandatiHome(Connection conn, PersistentCache persistentCache) {
		super(VConsCupMandatiBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
			throws PersistencyException
		{
			SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
			Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
			//	Se uo 999.000 in scrivania: visualizza tutto l'elenco
			if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
				sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(usercontext));
				Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext)));
				if(!uoScrivania.isUoCds())
				  sql.addSQLClause("AND","UO",SQLBuilder.EQUALS,CNRUserContext.getCd_unita_organizzativa(usercontext));
			}	
			return sql;
		}
}