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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 28/09/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
import java.sql.Connection;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Stipendi_cofi_obb_scadHome extends BulkHome {
	public Stipendi_cofi_obb_scadHome(Connection conn) {
		super(Stipendi_cofi_obb_scadBulk.class, conn);
	}
	public Stipendi_cofi_obb_scadHome(Connection conn, PersistentCache persistentCache) {
		super(Stipendi_cofi_obb_scadBulk.class, conn, persistentCache);
	}

	public java.util.Collection findStipendiCofiObbScad(UserContext context, int esercizio, int mese) throws PersistencyException{
		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause(FindClause.AND,"ESERCIZIO",SQLBuilder.EQUALS,esercizio);
		sql.addSQLClause(FindClause.AND,"MESE",SQLBuilder.EQUALS,mese);
		return fetchAll(sql);
	}
}