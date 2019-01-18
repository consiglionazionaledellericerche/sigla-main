package it.cnr.contab.config00.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.List;

public class Configurazione_cnrHome extends BulkHome {
    /**
     * <!-- @TODO: da completare -->
     * Costruisce un Configurazione_cnrHome
     *
     * @param conn La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     */
    public Configurazione_cnrHome(java.sql.Connection conn) {
        super(Configurazione_cnrBulk.class, conn);
    }

    /**
     * <!-- @TODO: da completare -->
     * Costruisce un Configurazione_cnrHome
     *
     * @param conn            La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     * @param persistentCache La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
     */
    public Configurazione_cnrHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(Configurazione_cnrBulk.class, conn, persistentCache);
    }

    public java.util.List findTipoVariazioniPdg() throws PersistencyException {

        SQLBuilder sql = createSQLBuilder();

        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, new Integer(0));
        sql.addClause("AND", "cd_unita_funzionale", SQLBuilder.EQUALS, Configurazione_cnrBulk.PK_PDG_VARIAZIONE);
        sql.addClause("AND", "cd_chiave_primaria", SQLBuilder.EQUALS, Configurazione_cnrBulk.SK_TIPO_VAR_APPROVA_CDS);

        return fetchAll(sql);
    }

    public java.util.List findTipoVariazioniStanz_res() throws PersistencyException {

        SQLBuilder sql = createSQLBuilder();

        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, new Integer(0));
        sql.addClause("AND", "cd_unita_funzionale", SQLBuilder.EQUALS, Configurazione_cnrBulk.PK_VAR_STANZ_RES);
        sql.addClause("AND", "cd_chiave_primaria", SQLBuilder.EQUALS, Configurazione_cnrBulk.SK_TIPO_VAR_APPROVA_CDS);

        return fetchAll(sql);
    }

    public java.util.List findTipoVariazioniEnteStanz_res() throws PersistencyException {

        SQLBuilder sql = createSQLBuilder();

        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, new Integer(0));
        sql.addClause("AND", "cd_unita_funzionale", SQLBuilder.EQUALS, Configurazione_cnrBulk.PK_VAR_STANZ_RES);
        sql.addClause("AND", "cd_chiave_primaria", SQLBuilder.EQUALS, Configurazione_cnrBulk.SK_TIPO_VAR_APPROVA_CNR);

        return fetchAll(sql);
    }

    public boolean isUOSpecialeTuttaSAC(String cdUnitaOrganizzativa) throws PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, new Integer(0));
        sql.addClause("AND", "cd_unita_funzionale", SQLBuilder.EQUALS, "*");
        sql.addClause("AND", "cd_chiave_primaria", SQLBuilder.EQUALS, Configurazione_cnrBulk.PK_UO_SPECIALE);
        sql.addClause("AND", "cd_chiave_secondaria", SQLBuilder.EQUALS, "UO_DISTINTA_TUTTA_SAC");
        final List<Configurazione_cnrBulk> list = fetchAll(sql);
        return list.stream()
                    .filter(configurazione_cnrBulk -> configurazione_cnrBulk.getVal01().equals(cdUnitaOrganizzativa))
                    .findAny().isPresent();
    }
}
