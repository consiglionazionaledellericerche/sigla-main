/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.rmi.RemoteException;
import java.sql.Connection;

import javax.ejb.EJBException;

import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneHome;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class LuogoConsegnaMagHome extends BulkHome {
	public LuogoConsegnaMagHome(Connection conn) {
		super(LuogoConsegnaMagBulk.class, conn);
	}
	public LuogoConsegnaMagHome(Connection conn, PersistentCache persistentCache) {
		super(LuogoConsegnaMagBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectComuneItalianoByClause(it.cnr.jada.UserContext userContext, LuogoConsegnaMagBulk luogoConsegnaMagBulk, ComuneHome comuneHome,ComuneBulk comune,CompoundFindClause clause)  throws ComponentException, EJBException, RemoteException {
		SQLBuilder sql = comuneHome.createSQLBuilder();
		sql.addSQLClause("AND","PG_NAZIONE",SQLBuilder.EQUALS, new Long(1));
		return sql;
	}
}