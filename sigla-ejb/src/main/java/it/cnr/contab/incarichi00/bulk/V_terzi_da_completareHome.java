/*
* Created by Generator 1.0
* Date 10/10/2005
*/
package it.cnr.contab.incarichi00.bulk;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_terzi_da_completareHome extends BulkHome {
	public V_terzi_da_completareHome(java.sql.Connection conn) {
		super(V_terzi_da_completareBulk.class, conn);
	}
	public V_terzi_da_completareHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_terzi_da_completareBulk.class, conn, persistentCache);
	}

	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		//sql.addSQLClause("AND","V_TERZI_DA_COMPLETARE.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
		//sql.addSQLClause("AND","V_TERZI_DA_CONGUAGLIARE.CD_CDS",sql.EQUALS,CNRUserContext.getCd_cds(usercontext));

		//	Se uo 999.000 in scrivania: visualizza tutto l'elenco
		//	Se uo ***.000 in scrivania: visualizza l'elenco di tutte le U.O. dello stesso CDS
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);

		if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa()))
		{
				sql.addSQLClause("AND","V_TERZI_DA_COMPLETARE.CD_CDS",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(usercontext));

				Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext)));
				if(!uoScrivania.isUoCds())
					  sql.addSQLClause("AND","V_TERZI_DA_COMPLETARE.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,CNRUserContext.getCd_unita_organizzativa(usercontext));  
		}	

		//if (compoundfindclause!= null)
		  // sql.addClause(compoundfindclause);
		return sql;
	}
}