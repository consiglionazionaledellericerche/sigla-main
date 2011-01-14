package it.cnr.contab.logs.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;

public class Batch_log_rigaHome extends BulkHome
{

    public Batch_log_rigaHome(Connection connection)
    {
        super(it.cnr.contab.logs.bulk.Batch_log_rigaBulk.class, connection);
    }

    public Batch_log_rigaHome(Connection connection, PersistentCache persistentcache)
    {
        super(it.cnr.contab.logs.bulk.Batch_log_rigaBulk.class, connection, persistentcache);
    }
}