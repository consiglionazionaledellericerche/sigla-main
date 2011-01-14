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