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

package it.cnr.contab.preventvar00.bulk;

import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Var_bilancioHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Var_bilancioHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Var_bilancioHome(java.sql.Connection conn) {
	super(Var_bilancioBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Var_bilancioHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Var_bilancioHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Var_bilancioBulk.class,conn,persistentCache);
}
	public Var_bilancioBulk findByVar_stanz_res(Var_stanz_resBulk var_stanz_res) throws PersistencyException{
		SQLBuilder sql = super.createSQLBuilder();
		sql.addClause("AND","esercizio_var_stanz_res",SQLBuilder.EQUALS,var_stanz_res.getEsercizio());
		sql.addClause("AND","pg_var_stanz_res",SQLBuilder.EQUALS,var_stanz_res.getPg_variazione());
		sql.addClause("AND","stato",SQLBuilder.EQUALS,Var_bilancioBulk.DEFINITIVA);
		Broker broker = createBroker(sql);
		if (broker.next())
		  return (Var_bilancioBulk)fetch(broker);
		return null;
	}
	public Var_bilancioBulk findByPdg_variazione(Pdg_variazioneBulk pdgVar) throws PersistencyException{
		SQLBuilder sql = super.createSQLBuilder();
		sql.addClause("AND","esercizio_pdg_variazione",SQLBuilder.EQUALS,pdgVar.getEsercizio());
		sql.addClause("AND","pg_variazione_pdg",SQLBuilder.EQUALS,pdgVar.getPg_variazione_pdg());
		sql.addClause("AND","stato",SQLBuilder.EQUALS,Var_bilancioBulk.DEFINITIVA);
		Broker broker = createBroker(sql);
		if (broker.next())
		  return (Var_bilancioBulk)fetch(broker);
		return null;
	}
	public Var_bilancioBulk findByMandato(MandatoBulk mandato) throws PersistencyException{
		SQLBuilder sql = super.createSQLBuilder();
		sql.addClause("AND","cd_cds_mandato",SQLBuilder.EQUALS,mandato.getCd_cds());
		sql.addClause("AND","esercizio_mandato",SQLBuilder.EQUALS,mandato.getEsercizio());
		sql.addClause("AND","pg_mandato",SQLBuilder.EQUALS,mandato.getPg_mandato());
		Broker broker = createBroker(sql);
		if (broker.next())
		  return (Var_bilancioBulk)fetch(broker);
		return null;
	}
}
