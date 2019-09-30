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

package it.cnr.contab.compensi00.tabrif.bulk;

import java.sql.*;

import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ScaglioneHome extends BulkHome {
public ScaglioneHome(java.sql.Connection conn) {
	super(ScaglioneBulk.class,conn);
}
public ScaglioneHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(ScaglioneBulk.class,conn,persistentCache);
}
public java.util.List caricaScaglioni(ScaglioneBulk obj) throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND", "cd_contributo_ritenuta", sql.EQUALS, obj.getCd_contributo_ritenuta());
	sql.addClause("AND", "cd_regione", sql.EQUALS, obj.getCd_regione());
	sql.addClause("AND", "cd_provincia", sql.EQUALS, obj.getCd_provincia());
	sql.addClause("AND", "pg_comune", sql.EQUALS, obj.getPg_comune());
	sql.addClause("AND", "ti_anagrafico", sql.EQUALS, obj.getTi_anagrafico());
	sql.addClause("AND", "ti_ente_percipiente", sql.EQUALS, obj.getTi_ente_percipiente());

//	sql.addBetweenClause("AND","dt_inizio_validita",obj.getDt_inizio_validita(), obj.getDt_fine_validita());
	

	CompoundFindClause clause1 = CompoundFindClause.and(
		new SimpleFindClause("dt_inizio_validita", sql.GREATER_EQUALS,obj.getDt_inizio_validita()),
		new SimpleFindClause("dt_inizio_validita", sql.LESS_EQUALS,obj.getDt_fine_validita()));
	CompoundFindClause clause2 = CompoundFindClause.and(
		new SimpleFindClause("dt_fine_validita", sql.GREATER_EQUALS,obj.getDt_inizio_validita()),
		new SimpleFindClause("dt_fine_validita", sql.LESS_EQUALS,obj.getDt_fine_validita()));
	sql.addClause(CompoundFindClause.or(clause1, clause2));

	return fetchAll(sql);
}
public ScaglioneBulk findScaglione(UserContext context,AddizionaliBulk addizionale,ComuneBulk comune) throws PersistencyException{
	java.util.List result=null;
	ScaglioneBulk scaglione = new ScaglioneBulk();
	
	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND", "cd_contributo_ritenuta", sql.EQUALS,"ADDCOM");
	sql.addClause("AND", "pg_comune", sql.EQUALS, comune.getPg_comune());
	sql.addClause("AND", "dt_fine_validita", sql.GREATER, getServerTimestamp());
	sql.addClause("AND", "dt_inizio_validita", sql.LESS_EQUALS, getServerTimestamp());
	SQLBuilder sql_succ = createSQLBuilder();
	sql_succ.addClause("AND", "cd_contributo_ritenuta", sql.EQUALS,"ADDCOM");
	sql_succ.addClause("AND", "pg_comune", sql.EQUALS, comune.getPg_comune());
	sql_succ.addClause("AND", "dt_inizio_validita", sql.GREATER, getServerTimestamp());
	
	sql.addSQLNotExistsClause("AND", sql_succ);
	Broker broker = createBroker(sql);
	if (broker.next()){
		scaglione = (ScaglioneBulk)fetch(broker);
		result = getHomeCache().getHome( ScaglioneBulk.class ).fetchAll( sql );
		getHomeCache().fetchAll(context);
		
	}
	broker.close();
	if (result !=null && result.size()==1)
		return scaglione;
	else
		return null;
}
}
