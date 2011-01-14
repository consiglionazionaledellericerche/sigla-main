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