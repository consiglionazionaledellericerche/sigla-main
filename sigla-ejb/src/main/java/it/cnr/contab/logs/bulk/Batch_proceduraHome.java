package it.cnr.contab.logs.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;

public class Batch_proceduraHome extends BulkHome
{

    public Batch_proceduraHome(Connection connection)
    {
        super(it.cnr.contab.logs.bulk.Batch_proceduraBulk.class, connection);
    }

    public Batch_proceduraHome(Connection connection, PersistentCache persistentcache)
    {
        super(it.cnr.contab.logs.bulk.Batch_proceduraBulk.class, connection, persistentcache);
    }
}