/*
* Creted by Generator 1.0
* Date 13/04/2005
*/
package it.cnr.contab.pdg00.consultazioni.bulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_dpdg_aggregato_spe_det_dHome extends BulkHome {
	public V_dpdg_aggregato_spe_det_dHome(java.sql.Connection conn) {
		super(V_dpdg_aggregato_spe_det_dBulk.class, conn);
	}
	public V_dpdg_aggregato_spe_det_dHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_dpdg_aggregato_spe_det_dBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addTableToHeader("UTENTE");
		sql.addSQLClause("AND","V_DPDG_AGGREGATO_SPE_DET_D.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
		sql.addSQLClause("AND","UTENTE.CD_UTENTE",sql.EQUALS,CNRUserContext.getUser(usercontext));
		sql.addSQLJoin("UTENTE.CD_CDR","V_DPDG_AGGREGATO_SPE_DET_D.CD_CENTRO_RESPONSABILITA");
		return sql;
	}	
}