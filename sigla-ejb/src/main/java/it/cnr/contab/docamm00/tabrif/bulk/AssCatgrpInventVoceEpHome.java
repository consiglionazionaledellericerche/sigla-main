/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneHome;
import it.cnr.contab.config00.pdcep.bulk.ContoBulk;
import it.cnr.contab.config00.pdcep.bulk.ContoHome;
import it.cnr.contab.config00.pdcep.bulk.Voce_epBulk;
import it.cnr.contab.ordmag.anag00.LuogoConsegnaMagBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import javax.ejb.EJBException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Home che gestisce i capoconti e i conti.
 */
public class AssCatgrpInventVoceEpHome extends BulkHome {

	private static it.cnr.jada.util.OrderedHashtable gruppiKeys;
protected AssCatgrpInventVoceEpHome(Class clazz, Connection connection) {
	super(clazz,connection);
}
protected AssCatgrpInventVoceEpHome(Class clazz, Connection connection, PersistentCache persistentCache) {
	super(clazz,connection,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 *
 *
 * @param conn
 */
public AssCatgrpInventVoceEpHome(Connection conn) {
	super(Voce_epBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 *
 *
 * @param conn
 * @param persistentCache
 */
public AssCatgrpInventVoceEpHome(Connection conn, PersistentCache persistentCache) {
	super(AssCatgrpInventVoceEpBulk.class,conn,persistentCache);
}

public SQLBuilder selectContoByClause(it.cnr.jada.UserContext userContext, AssCatgrpInventVoceEpBulk assCatgrpInventVoceEpBulk, ContoHome contoHome, ContoBulk conto, CompoundFindClause clause)  throws ComponentException, EJBException, RemoteException {
		SQLBuilder sql = contoHome.createSQLBuilder();
		sql.addClause(clause);
		sql.addSQLClause("AND","esercizio",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
		sql.addOrderBy("cd_voce_ep");
		return sql;
	}
}
