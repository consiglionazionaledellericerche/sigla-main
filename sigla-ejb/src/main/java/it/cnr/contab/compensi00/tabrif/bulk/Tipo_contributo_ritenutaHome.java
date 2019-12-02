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
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_contributo_ritenutaHome extends BulkHome {
public Tipo_contributo_ritenutaHome(java.sql.Connection conn) {
	super(Tipo_contributo_ritenutaBulk.class,conn);
}
public Tipo_contributo_ritenutaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipo_contributo_ritenutaBulk.class,conn,persistentCache);
}
/**
 * Inserisce la clausola di validità.
 * I record vengono filtrati secondo la seguente clausola
 *	DATA_INIZIO_VALIDITA <= data <= DATA_FINE_VALIDITA
 *
 * @param sql SQL statement a cui vengono aggiunte le clausole di validita
 * @param data Data di validita del tipo trattamento
 *
**/
public void addClauseValidita(SQLBuilder sql, Timestamp data) throws PersistencyException{

	sql.addClause("AND","dt_ini_validita", sql.LESS_EQUALS, data);
	sql.addClause("AND","dt_fin_validita", sql.GREATER_EQUALS, data);
}
public java.util.List caricaIntervalli(Tipo_contributo_ritenutaBulk tipoCori) throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND", "cd_contributo_ritenuta",sql.EQUALS,tipoCori.getCd_contributo_ritenuta());
	sql.addOrderBy("dt_ini_validita");
	return fetchAll(sql);
}
public Tipo_contributo_ritenutaBulk findIntervallo(Tipo_contributo_ritenutaBulk tipoCori) throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND","cd_contributo_ritenuta",sql.EQUALS,tipoCori.getCd_contributo_ritenuta());
	sql.addClause("AND","dt_ini_validita", sql.LESS_EQUALS, tipoCori.getDt_ini_validita());
	sql.addClause("AND","dt_fin_validita", sql.GREATER_EQUALS, tipoCori.getDt_ini_validita());

	Tipo_contributo_ritenutaBulk corrente = null;
	Broker broker = createBroker(sql);
	if (broker.next())
		corrente = (Tipo_contributo_ritenutaBulk)fetch(broker);
	broker.close();

	return corrente;
}
/**
 * Ritorna il Tipo Contributo Ritenuta con codice <cdTipoCORI>
 * e valido in data odierna
 *
 * @param	cdTipoCori	Codice del Tipo Contributo Ritenuta
 * @return	Il Tipo Contrinuto Ritenuta valido
 *
*/
public Tipo_contributo_ritenutaBulk findTipoCORIValido(String cdTipoCORI) throws PersistencyException{

	return findTipoCORIValido(cdTipoCORI, CompensoBulk.getDataOdierna(getServerTimestamp()));
}
/**
 * Ritorna il Tipo Contributo Ritenuta con codice <cdTipoCORI>
 * e valido in data <data>
 *
 * @param	cdTipoCori	Codice del Tipo Contributo Ritenuta
 * @param	data		Data di validita del Tipo Contributo Ritenuta
 * @return	Il Tipo Contrinuto Ritenuta valido
 *
*/
public Tipo_contributo_ritenutaBulk findTipoCORIValido(String cdTipoCORI, Timestamp data) throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();

	sql.addSQLClause("AND","CD_CONTRIBUTO_RITENUTA",sql.EQUALS,cdTipoCORI);
	addClauseValidita(sql, data);

	Tipo_contributo_ritenutaBulk cori = null;
	Broker broker = createBroker(sql);
	if (broker.next())
		cori = (Tipo_contributo_ritenutaBulk)fetch(broker);
	broker.close();
		
	return cori;
}
}
