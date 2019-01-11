package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Timestamp;
import java.util.List;

public class Fattura_passivaHome extends BulkHome {
    protected Fattura_passivaHome(Class clazz, java.sql.Connection connection) {
        super(clazz, connection);
    }

    protected Fattura_passivaHome(Class clazz, java.sql.Connection connection, PersistentCache persistentCache) {
        super(clazz, connection, persistentCache);
    }

    public Fattura_passivaHome(java.sql.Connection conn) {
        super(Fattura_passivaBulk.class, conn);
    }

    public Fattura_passivaHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(Fattura_passivaBulk.class, conn, persistentCache);
    }

    public java.util.List findDuplicateFatturaFornitore(Persistent clause) throws PersistencyException {
        return fetchAll(selectDuplicateFatturaFornitore((Fattura_passivaBulk) clause));
    }

    /**
     * Viene ricercata la Data di Registrazione del documento immediatamente precedente
     * a quello che si sta registrando/modificando.
     * Viene restituita la data trovata, NULL altrimenti
     *
     * @param FatturaPassivaBulk
     * @return Timestamp
     * @throws PersistencyException, IntrospectionException
     */
    public Timestamp findDataRegFatturaPrecedente(Fattura_passivaBulk fatturaPassiva) throws PersistencyException, IntrospectionException {
        SQLBuilder sql = createSQLBuilder();
        sql.setHeader("SELECT TRUNC(MAX(DT_REGISTRAZIONE)) AS DT_REGISTRAZIONE");
        sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, fatturaPassiva.getCd_cds());
        sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, fatturaPassiva.getCd_unita_organizzativa());
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, fatturaPassiva.getEsercizio());
        sql.addClause("AND", "pg_fattura_passiva", SQLBuilder.LESS, fatturaPassiva.getPg_fattura_passiva());

        Broker broker = createBroker(sql);
        Object value = null;
        if (broker.next()) {
            value = broker.fetchPropertyValue("dt_registrazione", getIntrospector().getPropertyType(getPersistentClass(), "dt_registrazione"));
            broker.close();
        }
        return (Timestamp) value;
    }

    /**
     * Viene ricercata la Data di Registrazione del documento immediatamente successivo
     * a quello che si sta registrando/modificando.
     * Viene restituita la data trovata, NULL altrimenti
     *
     * @param FatturaPassivaBulk
     * @return Timestamp
     * @throws PersistencyException, IntrospectionException
     */
    public Timestamp findDataRegFatturaSuccessiva(Fattura_passivaBulk fatturaPassiva) throws PersistencyException, IntrospectionException {
        SQLBuilder sql = createSQLBuilder();
        sql.setHeader("SELECT TRUNC(MIN(DT_REGISTRAZIONE)) AS DT_REGISTRAZIONE");
        sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, fatturaPassiva.getCd_cds());
        sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, fatturaPassiva.getCd_unita_organizzativa());
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, fatturaPassiva.getEsercizio());
        sql.addClause("AND", "pg_fattura_passiva", SQLBuilder.GREATER, fatturaPassiva.getPg_fattura_passiva());

        Broker broker = createBroker(sql);
        Object value = null;
        if (broker.next()) {
            value = broker.fetchPropertyValue("dt_registrazione", getIntrospector().getPropertyType(getPersistentClass(), "dt_registrazione"));
            broker.close();
        }
        return (Timestamp) value;
    }

    public SQLBuilder selectDuplicateFatturaFornitore(Fattura_passivaBulk clause) {

        clause.setTi_fattura(null);
        SQLBuilder sql = createSQLBuilder();
        sql.addClausesUsing(clause, false);
        return sql;
    }

    public SQLBuilder selectModalita(Fattura_passivaBulk fatturaPassivaBulk, it.cnr.contab.docamm00.tabrif.bulk.DivisaHome divisaHome, it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk clause) {

        return divisaHome.createSQLBuilder();
    }

    public SQLBuilder selectTermini(Fattura_passivaBulk fatturaPassivaBulk, it.cnr.contab.docamm00.tabrif.bulk.DivisaHome divisaHome, it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk clause) {

        return divisaHome.createSQLBuilder();
    }

    public SQLBuilder selectValuta(Fattura_passivaBulk fatturaPassivaBulk, it.cnr.contab.docamm00.tabrif.bulk.DivisaHome divisaHome, it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk clause) {

        SQLBuilder sql = divisaHome.createSQLBuilder();

        sql.addTableToHeader("CAMBIO");
        sql.addSQLJoin("DIVISA.CD_DIVISA", "CAMBIO.CD_DIVISA");

        return sql;
    }
}
