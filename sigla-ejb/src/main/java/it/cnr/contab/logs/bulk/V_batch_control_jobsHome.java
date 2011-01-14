package it.cnr.contab.logs.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;

public class V_batch_control_jobsHome extends BulkHome
{

    public V_batch_control_jobsHome(Connection connection)
    {
        super(it.cnr.contab.logs.bulk.V_batch_control_jobsBulk.class, connection);
    }

    public V_batch_control_jobsHome(Connection connection, PersistentCache persistentcache)
    {
        super(it.cnr.contab.logs.bulk.V_batch_control_jobsBulk.class, connection, persistentcache);
    }
}