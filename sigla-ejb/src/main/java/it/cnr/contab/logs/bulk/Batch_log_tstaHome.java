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

package it.cnr.contab.logs.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;

public class Batch_log_tstaHome extends BulkHome
{

    public Batch_log_tstaHome(Connection connection)
    {
        super(it.cnr.contab.logs.bulk.Batch_log_tstaBulk.class, connection);
    }

    public Batch_log_tstaHome(Connection connection, PersistentCache persistentcache)
    {
        super(it.cnr.contab.logs.bulk.Batch_log_tstaBulk.class, connection, persistentcache);
    }
}