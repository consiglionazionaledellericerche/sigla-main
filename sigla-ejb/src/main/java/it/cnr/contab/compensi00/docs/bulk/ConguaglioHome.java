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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.config00.esercizio.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ConguaglioHome extends BulkHome {
	public ConguaglioHome(java.sql.Connection conn) {
		super(ConguaglioBulk.class,conn);
	}
	public ConguaglioHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(ConguaglioBulk.class,conn,persistentCache);
	}
	/**
	 * Ritorna il record ConguaglioBulk associato al compenso e presente sulla tabella CONGUAGLIO.
	 * Ritorna il conguaglio con le informazioni utilizzate per l'emissione del compenso.
	 */
	public ConguaglioBulk findConguaglio(CompensoBulk compenso) throws PersistencyException {

		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND", "CD_CDS_COMPENSO",sql.EQUALS,compenso.getCd_cds());
		sql.addSQLClause("AND", "CD_UO_COMPENSO",sql.EQUALS,compenso.getCd_unita_organizzativa());
		sql.addSQLClause("AND", "ESERCIZIO_COMPENSO",sql.EQUALS,compenso.getEsercizio());
		sql.addSQLClause("AND", "PG_COMPENSO",sql.EQUALS,compenso.getPg_compenso());

		ConguaglioBulk conguaglio = null;
		Broker broker = createBroker(sql);
		if (broker.next())
			conguaglio = (ConguaglioBulk)fetch(broker);
		broker.close();

		return conguaglio;
	}
	/**
	 * Ritorna il record ConguaglioBulk associato al compenso tramite la tabella di associazione ASS_COMPENSO_CONGUAGLIO.
	 * Ritorna il conguaglio che è stato emesso contemporaneamente al compenso e collegato allo stesso.
	 */
	public ConguaglioBulk findConguaglioAssociatoACompenso(CompensoBulk compenso) throws PersistencyException {

		SQLBuilder sql = createSQLBuilder();
		sql.addTableToHeader("ASS_COMPENSO_CONGUAGLIO");
		sql.addSQLJoin("ASS_COMPENSO_CONGUAGLIO.CD_CDS_CONGUAGLIO","CONGUAGLIO.CD_CDS");
		sql.addSQLJoin("ASS_COMPENSO_CONGUAGLIO.CD_UO_CONGUAGLIO","CONGUAGLIO.CD_UNITA_ORGANIZZATIVA");
		sql.addSQLJoin("ASS_COMPENSO_CONGUAGLIO.ESERCIZIO_CONGUAGLIO","CONGUAGLIO.ESERCIZIO");
		sql.addSQLJoin("ASS_COMPENSO_CONGUAGLIO.PG_CONGUAGLIO","CONGUAGLIO.PG_CONGUAGLIO");

		sql.addSQLClause("AND","ASS_COMPENSO_CONGUAGLIO.CD_CDS_COMPENSO",sql.EQUALS, compenso.getCd_cds());
		sql.addSQLClause("AND","ASS_COMPENSO_CONGUAGLIO.CD_UO_COMPENSO",sql.EQUALS, compenso.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ASS_COMPENSO_CONGUAGLIO.ESERCIZIO_COMPENSO",sql.EQUALS, compenso.getEsercizio());
		sql.addSQLClause("AND","ASS_COMPENSO_CONGUAGLIO.PG_COMPENSO",sql.EQUALS, compenso.getPg_compenso());

		ConguaglioBulk conguaglio = null;
		Broker broker = createBroker(sql);
		if (broker.next())
			conguaglio = (ConguaglioBulk)fetch(broker);
		broker.close();

		return conguaglio;
	}
}
