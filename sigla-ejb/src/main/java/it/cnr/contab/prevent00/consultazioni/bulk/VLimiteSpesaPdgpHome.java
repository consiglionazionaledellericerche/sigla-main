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
 * Date 20/09/2011
 */
package it.cnr.contab.prevent00.consultazioni.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VLimiteSpesaPdgpHome extends BulkHome {
	public VLimiteSpesaPdgpHome(Connection conn) {
		super(VLimiteSpesaPdgpBulk.class, conn);
	}
	public VLimiteSpesaPdgpHome(Connection conn, PersistentCache persistentCache) {
		super(VLimiteSpesaPdgpBulk.class, conn, persistentCache);
	}
}