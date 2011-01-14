package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;

/**
 * @author aimprota
 */
public class Classificazione_speseHome extends BulkHome
{

    /**
     * @param connection
     */
    public Classificazione_speseHome(Connection connection)
    {
        super(Classificazione_speseBulk.class, connection);
    }

    /**
     * @param connection
     * @param persistentcache
     */
    public Classificazione_speseHome(Connection connection, PersistentCache persistentcache)
    {
        super(Classificazione_speseBulk.class, connection, persistentcache);
    }

}
