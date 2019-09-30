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
 * Date 20/01/2007
 */
package it.cnr.contab.prevent01.bulk;

import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

public class Ass_pdg_missione_tipo_uoHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public Ass_pdg_missione_tipo_uoHome(Connection conn) {
		super(Ass_pdg_missione_tipo_uoBulk.class, conn);
	}
	
	public Ass_pdg_missione_tipo_uoHome(Connection conn, PersistentCache persistentCache) {
		super(Ass_pdg_missione_tipo_uoBulk.class, conn, persistentCache);
	}
}