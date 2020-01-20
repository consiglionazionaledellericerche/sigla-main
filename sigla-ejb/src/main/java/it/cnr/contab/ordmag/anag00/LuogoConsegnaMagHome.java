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
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneHome;
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
		Long pg_nazione=new Long(1);
		if (luogoConsegnaMagBulk.getNazione()!=null && luogoConsegnaMagBulk.getNazione().getPg_nazione()!=null && luogoConsegnaMagBulk.getNazione().getPg_nazione()>0)
			pg_nazione=luogoConsegnaMagBulk.getNazione().getPg_nazione();

		if( luogoConsegnaMagBulk.getCap()!=null && (!luogoConsegnaMagBulk.getCap().trim().isEmpty()))
			sql.addSQLClause("AND","CD_CAP",SQLBuilder.EQUALS, luogoConsegnaMagBulk.getCap());

		sql.addSQLClause("AND","PG_NAZIONE",SQLBuilder.EQUALS, pg_nazione);
		return sql;
	}

	public SQLBuilder selectNazioneByClause(it.cnr.jada.UserContext userContext, LuogoConsegnaMagBulk luogoConsegnaMagBulk, NazioneHome nazioneHome, NazioneBulk nazione, CompoundFindClause clause)  throws ComponentException, EJBException, RemoteException {
		SQLBuilder sql = nazioneHome.createSQLBuilder();
		sql.addSQLClause("AND","TI_NAZIONE",SQLBuilder.EQUALS, "I");
		return sql;
	}
}