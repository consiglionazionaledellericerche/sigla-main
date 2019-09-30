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

package it.cnr.contab.util;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;
import java.util.Optional;

public class IdHome<T extends IdKey> extends BulkHome {
    protected IdHome(Class class1, Connection connection) {
        super(class1, connection);
    }

    protected IdHome(Class class1, Connection connection, PersistentCache persistentcache) {
        super(class1, connection, persistentcache);
    }

    @Override
    public void initializePrimaryKeyForInsert(UserContext usercontext, OggettoBulk oggettobulk) throws PersistencyException, ComponentException {
        T key = (T) oggettobulk;
        Optional.ofNullable(key)
                .ifPresent(t -> {
                    try {
                        t.setId(
                                Optional.ofNullable(findAndLockMax(t, t.getKeyName(), new Long(0)))
                                        .filter(Long.class::isInstance)
                                        .map(Long.class::cast)
                                        .map(aLong -> aLong + 1)
                                        .orElse(new Long(0))
                        );
                    } catch (PersistencyException| BusyResourceException e) {
                        throw new DetailedRuntimeException(e);
                    }
                });
    }
}
