package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;

/**
 * @author aimprota
 */
public class Classificazione_entrateHome extends BulkHome
{

    /**
     * @param connection
     */
    public Classificazione_entrateHome(Connection connection)
    {
        super(Classificazione_entrateBulk.class, connection);
    }

    /**
     * @param connection
     * @param persistentcache
     */
    public Classificazione_entrateHome(Connection connection, PersistentCache persistentcache)
    {
        super(Classificazione_entrateBulk.class, connection, persistentcache);
    }

}
