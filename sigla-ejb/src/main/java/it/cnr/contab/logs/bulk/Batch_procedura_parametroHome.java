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

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;

// Referenced classes of package it.cnr.contab.logs.bulk:
//            Batch_procedura_parametroBulk, Batch_procedura_parametroKey

public class Batch_procedura_parametroHome extends BulkHome
{

    public Batch_procedura_parametroHome(Connection connection)
    {
        super(it.cnr.contab.logs.bulk.Batch_procedura_parametroBulk.class, connection);
    }

    public Batch_procedura_parametroHome(Connection connection, PersistentCache persistentcache)
    {
        super(it.cnr.contab.logs.bulk.Batch_procedura_parametroBulk.class, connection, persistentcache);
    }

    public void initializePrimaryKeyForInsert(UserContext usercontext, OggettoBulk oggettobulk)
        throws PersistencyException
    {
        try
        {
            ((Batch_procedura_parametroBulk)oggettobulk).setPg_valore_parametro(new Integer(((Integer)findAndLockMax(oggettobulk, "pg_valore_parametro", new Integer(0))).intValue() + 1));
        }
        catch(BusyResourceException busyresourceexception)
        {
            throw new PersistencyException(busyresourceexception);
        }
    }
}