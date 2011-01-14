/*
* Created by Generator 1.0
* Date 07/08/2006
*/
package it.cnr.contab.pdg00.cdip.bulk;
import java.sql.Timestamp;
import java.util.Iterator;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SimpleFindClause;
public class V_cnr_estrazione_coriHome extends BulkHome {
	public V_cnr_estrazione_coriHome(java.sql.Connection conn) {
		super(V_cnr_estrazione_coriBulk.class, conn);
	}
	public V_cnr_estrazione_coriHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cnr_estrazione_coriBulk.class, conn, persistentCache);
	}
	public java.util.List findDistinct(it.cnr.jada.UserContext userContext) throws IntrospectionException, PersistencyException 
	{
		Integer esercizio = ((CNRUserContext) userContext).getEsercizio();
		setColumnMap("DISTINCT_MESE");
		SQLBuilder sql = createSQLBuilder();
		sql.resetColumns();
		sql.setDistinctClause(true);

		sql.addColumn("V_CNR_ESTRAZIONE_CORI.ESERCIZIO");
		sql.addColumn("V_CNR_ESTRAZIONE_CORI.MESE");
		sql.addColumn("V_CNR_ESTRAZIONE_CORI.DT_ESTRAZIONE");
		sql.addSQLClause("AND","V_CNR_ESTRAZIONE_CORI.ESERCIZIO",sql.EQUALS,esercizio);
		sql.addOrderBy("V_CNR_ESTRAZIONE_CORI.MESE");
		return fetchAll(sql);
	}	
	/*
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
	throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		if (compoundfindclause!= null)
		   sql.addClause(compoundfindclause);
		return sql;
	}*/
}