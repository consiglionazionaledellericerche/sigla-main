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
 * Date 11/12/2015
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;
public class UtenteFirmaDettaglioHome extends BulkHome {
	public UtenteFirmaDettaglioHome(Connection conn) {
		super(UtenteFirmaDettaglioBulk.class, conn);
	}
	public UtenteFirmaDettaglioHome(Connection conn, PersistentCache persistentCache) {
		super(UtenteFirmaDettaglioBulk.class, conn, persistentCache);
	}

	public SQLBuilder selectUnitaOrganizzativaByClause(UtenteFirmaDettaglioBulk utenteFirmaDettaglioBulk, Unita_organizzativaHome unita_organizzativaHome,
			Unita_organizzativaBulk unita_organizzativaBulk, CompoundFindClause compoundfindclause) {
        SQLBuilder sqlbuilder = unita_organizzativaHome.createSQLBuilderEsteso();
        sqlbuilder.addClause(compoundfindclause);
        return sqlbuilder;		
	}
}