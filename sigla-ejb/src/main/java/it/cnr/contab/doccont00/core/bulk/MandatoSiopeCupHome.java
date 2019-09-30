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
 * Date 20/06/2013
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class MandatoSiopeCupHome extends BulkHome {
	public MandatoSiopeCupHome(Connection conn) {
		super(MandatoSiopeCupBulk.class, conn);
	}
	public MandatoSiopeCupHome(Connection conn, PersistentCache persistentCache) {
		super(MandatoSiopeCupBulk.class, conn, persistentCache);
	}
	public MandatoSiopeCupHome(Class clazz, java.sql.Connection conn) {
		super(clazz,conn);
	}
	public MandatoSiopeCupHome(Class clazz,java.sql.Connection conn,PersistentCache persistentCache) {
		super(clazz,conn,persistentCache);
	}
}