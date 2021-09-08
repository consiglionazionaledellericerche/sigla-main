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
 * Date 02/10/2018
 */
package it.cnr.contab.doccont00.intcass.giornaliera;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class MovimentoContoEvidenzaHome extends BulkHome {
	public MovimentoContoEvidenzaHome(Connection conn) {
		super(MovimentoContoEvidenzaBulk.class, conn);
	}
	public MovimentoContoEvidenzaHome(Connection conn, PersistentCache persistentCache) {
		super(MovimentoContoEvidenzaBulk.class, conn, persistentCache);
	}

	public java.util.List recuperoRigheFile(String nomeFile, Integer esercizio, String stato) throws IntrospectionException, PersistencyException {
		SQLBuilder sql = createSQLBuilder();

		sql.addClause("AND","esercizio",SQLBuilder.EQUALS, esercizio);
		sql.addClause("AND","identificativo_flusso",SQLBuilder.EQUALS, nomeFile);
		sql.addClause("AND","stato",SQLBuilder.EQUALS, stato);
		sql.addOrderBy("progressivo");
		return fetchAll(sql);

	}

}