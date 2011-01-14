package it.cnr.contab.logs.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;

public class Batch_controlHome extends BulkHome
{

    public Batch_controlHome(Connection connection)
    {
        super(it.cnr.contab.logs.bulk.Batch_controlBulk.class, connection);
    }

    public Batch_controlHome(Connection connection, PersistentCache persistentcache)
    {
        super(it.cnr.contab.logs.bulk.Batch_controlBulk.class, connection, persistentcache);
    }
}