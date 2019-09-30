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
* Created by Generator 1.0
* Date 22/09/2005
*/
package it.cnr.contab.logregistry00.logs.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class L_ass_comp_doc_cont_nmpHome extends BulkHome {
	public L_ass_comp_doc_cont_nmpHome(java.sql.Connection conn) {
		super(L_ass_comp_doc_cont_nmpBulk.class, conn);
	}
	public L_ass_comp_doc_cont_nmpHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(L_ass_comp_doc_cont_nmpBulk.class, conn, persistentCache);
	}
}