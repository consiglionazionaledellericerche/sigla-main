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
 * Date 10/09/2007
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;

import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoHome;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
public class Incarichi_repertorio_annoHome extends BulkHome {
	public Incarichi_repertorio_annoHome(Connection conn) {
		super(Incarichi_repertorio_annoBulk.class, conn);
	}
	public Incarichi_repertorio_annoHome(Connection conn, PersistentCache persistentCache) {
		super(Incarichi_repertorio_annoBulk.class, conn, persistentCache);
	}

	@Override
	public Persistent completeBulkRowByRow(UserContext userContext, Persistent persistent) throws PersistencyException {
		persistent = super.completeBulkRowByRow(userContext, persistent);
		try {
			if (persistent instanceof Incarichi_repertorio_annoBulk) {
				Incarichi_repertorio_annoBulk increpanno = (Incarichi_repertorio_annoBulk) persistent;
				CompensoHome cHome = (CompensoHome) getHomeCache().getHome(CompensoBulk.class);
				increpanno.setCompensiColl(new BulkList(cHome.findCompensoIncaricoList(userContext, increpanno)));
			}
		} catch (IntrospectionException e) {
		}
		return persistent;
	}
}