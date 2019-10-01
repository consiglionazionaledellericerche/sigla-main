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
 * Date 27/09/2018
 */
package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.contab.logs.bulk.Batch_procedura_parametroBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;
import java.util.Optional;

public class TipoFinanziamentoHome extends BulkHome {
    public TipoFinanziamentoHome(Connection conn) {
        super(TipoFinanziamentoBulk.class, conn);
    }

    public TipoFinanziamentoHome(Connection conn, PersistentCache persistentCache) {
        super(TipoFinanziamentoBulk.class, conn, persistentCache);
    }

    public void initializePrimaryKeyForInsert(UserContext usercontext, OggettoBulk oggettobulk)
            throws PersistencyException {
        try {
            Optional.ofNullable(oggettobulk)
                    .filter(TipoFinanziamentoBulk.class::isInstance)
                    .map(TipoFinanziamentoBulk.class::cast)
                    .ifPresent(tipoFinanziamentoBulk -> {
                        try {
                            tipoFinanziamentoBulk.setId(
                                    Optional.ofNullable(findAndLockMax(tipoFinanziamentoBulk, "id", 0))
                                        .filter(Long.class::isInstance)
                                        .map(Long.class::cast).map(aLong -> aLong + 1)
                                        .orElse(null)
                            );
                        } catch (PersistencyException|BusyResourceException e) {
                           throw new DetailedRuntimeException(e);
                        }
                    });
        } catch (DetailedRuntimeException detailedRuntimeException) {
            throw new PersistencyException(detailedRuntimeException);
        }
    }
}