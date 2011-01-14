/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 11/01/2010
 */
package it.cnr.contab.segnalazioni00.bulk;
import java.sql.Connection;
import java.util.Collection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class ConfrontoSiglaDwhHome extends BulkHome {
	
//	private java.util.Collection dt_elaborazioni;
	
	public ConfrontoSiglaDwhHome(Connection conn) {
		super(ConfrontoSiglaDwhBulk.class, conn);
	}
	public ConfrontoSiglaDwhHome(Connection conn, PersistentCache persistentCache) {
		super(ConfrontoSiglaDwhBulk.class, conn, persistentCache);
	}
	
	public SQLBuilder selectDt_elaborazioniByClause(Stampa_confronto_sigla_dwhBulk bulk) throws PersistencyException
		{
		PersistentHome pHome = getHomeCache().getHome(ConfrontoSiglaDwhBulk.class,"STAMPA");
		SQLBuilder sql = pHome.createSQLBuilder();
		sql.setDistinctClause(true);
		
		return sql;
		}

	public Collection findDt_elaborazioni(Stampa_confronto_sigla_dwhBulk bulk) throws IntrospectionException, PersistencyException {
		SQLBuilder sql = this.selectDt_elaborazioniByClause(bulk);			
			return  fetchAll(sql);
		}
}