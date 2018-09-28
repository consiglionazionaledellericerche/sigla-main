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