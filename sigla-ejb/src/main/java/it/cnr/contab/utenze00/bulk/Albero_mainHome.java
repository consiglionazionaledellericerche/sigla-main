package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.List;

public class Albero_mainHome extends BulkHome {
    /**
     * <!-- @TODO: da completare -->
     * Costruisce un Albero_mainHome
     *
     * @param conn La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     */
    public Albero_mainHome(java.sql.Connection conn) {
        super(Albero_mainBulk.class, conn);
    }

    /**
     * <!-- @TODO: da completare -->
     * Costruisce un Albero_mainHome
     *
     * @param conn            La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     * @param persistentCache La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
     */
    public Albero_mainHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(Albero_mainBulk.class, conn, persistentCache);
    }

    public List<Albero_mainBulk> findNodo(UserContext userContext, String businessProcess, String cdAccesso) throws PersistencyException {
        SQLBuilder sqlBuilder = this.createSQLBuilder();
        sqlBuilder.addClause(FindClause.AND, "business_process", SQLBuilder.EQUALS, businessProcess);
        sqlBuilder.addClause(FindClause.AND, "cd_accesso", SQLBuilder.EQUALS, cdAccesso);
        sqlBuilder.addClause(FindClause.AND, "fl_terminale", SQLBuilder.EQUALS, Boolean.TRUE);
        return this.fetchAll(sqlBuilder);
    }
}
