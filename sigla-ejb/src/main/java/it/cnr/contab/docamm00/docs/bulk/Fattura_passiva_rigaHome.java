package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.List;

public class Fattura_passiva_rigaHome extends BulkHome {
    public Fattura_passiva_rigaHome(Class classe, java.sql.Connection conn) {
        super(classe, conn);
    }

    public Fattura_passiva_rigaHome(Class classe, java.sql.Connection conn, PersistentCache persistentCache) {
        super(classe, conn, persistentCache);
    }

    public Fattura_passiva_rigaHome(java.sql.Connection conn) {
        super(Fattura_passiva_rigaBulk.class, conn);
    }

    public Fattura_passiva_rigaHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(Fattura_passiva_rigaBulk.class, conn, persistentCache);
    }

    public java.util.List findAddebitiForObbligazioneExceptFor(
            it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza,
            Fattura_passivaBulk fattura)
            throws PersistencyException {

        return fetchAll(selectForObbligazioneExceptFor(scadenza, fattura));
    }

    public java.util.List findStorniForObbligazioneExceptFor(
            it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza,
            Fattura_passivaBulk fattura)
            throws PersistencyException {

        return fetchAll(selectForObbligazioneExceptFor(scadenza, fattura));
    }

    public List<String> findCodiciCIG(Fattura_passivaBulk fattura, MandatoBulk mandato) throws PersistencyException {
        return findCIG(fattura, mandato, "CD_CIG");
    }

    public List<String> findMotiviEsclusioneCIG(Fattura_passivaBulk fattura, MandatoBulk mandato) throws PersistencyException {
        return findCIG(fattura, mandato, "CD_CIG");
    }

    private List<String> findCIG(Fattura_passivaBulk fattura, MandatoBulk mandato, String column) throws PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("DISTINCT " + column);
        sql.addTableToHeader("MANDATO");
        sql.addTableToHeader("MANDATO_RIGA");

        sql.addSQLJoin("MANDATO.CD_CDS", "MANDATO_RIGA.CD_CDS");
        sql.addSQLJoin("MANDATO.ESERCIZIO", "MANDATO_RIGA.ESERCIZIO");
        sql.addSQLJoin("MANDATO.PG_MANDATO", "MANDATO_RIGA.PG_MANDATO");

        sql.addSQLJoin("FATTURA_PASSIVA_RIGA.ESERCIZIO", "MANDATO_RIGA.ESERCIZIO_DOC_AMM");
        sql.addSQLJoin("FATTURA_PASSIVA_RIGA.CD_CDS", "MANDATO_RIGA.CD_CDS_DOC_AMM");
        sql.addSQLJoin("FATTURA_PASSIVA_RIGA.CD_UNITA_ORGANIZZATIVA", "MANDATO_RIGA.CD_UO_DOC_AMM");
        sql.addSQLJoin("FATTURA_PASSIVA_RIGA.PG_FATTURA_PASSIVA", "MANDATO_RIGA.PG_DOC_AMM");

        sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.ESERCIZIO", SQLBuilder.EQUALS, fattura.getEsercizio());
        sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.CD_CDS", SQLBuilder.EQUALS, fattura.getCd_cds());
        sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, fattura.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.PG_FATTURA_PASSIVA", SQLBuilder.EQUALS, fattura.getPg_fattura_passiva());

        sql.addSQLClause("AND", "MANDATO.ESERCIZIO", SQLBuilder.EQUALS, mandato.getEsercizio());
        sql.addSQLClause("AND", "MANDATO.CD_CDS", SQLBuilder.EQUALS, mandato.getCd_cds());
        sql.addSQLClause("AND", "MANDATO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, mandato.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "MANDATO.PG_MANDATO", SQLBuilder.EQUALS, mandato.getPg_mandato());


        return fetchAll(sql);
    }

    protected SQLBuilder selectForObbligazioneExceptFor(
            it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza,
            Fattura_passivaBulk fattura) {

        SQLBuilder sql = createSQLBuilder();

        sql.addTableToHeader("FATTURA_PASSIVA");
        sql.addSQLJoin("FATTURA_PASSIVA_RIGA.ESERCIZIO", "FATTURA_PASSIVA.ESERCIZIO");
        sql.addSQLJoin("FATTURA_PASSIVA_RIGA.CD_CDS", "FATTURA_PASSIVA.CD_CDS");
        sql.addSQLJoin("FATTURA_PASSIVA_RIGA.CD_UNITA_ORGANIZZATIVA", "FATTURA_PASSIVA.CD_UNITA_ORGANIZZATIVA");
        sql.addSQLJoin("FATTURA_PASSIVA_RIGA.PG_FATTURA_PASSIVA", "FATTURA_PASSIVA.PG_FATTURA_PASSIVA");
        sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.CD_CDS_OBBLIGAZIONE", SQLBuilder.EQUALS, scadenza.getCd_cds());
        sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.ESERCIZIO_OBBLIGAZIONE", SQLBuilder.EQUALS, scadenza.getEsercizio());
        sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.ESERCIZIO_ORI_OBBLIGAZIONE", SQLBuilder.EQUALS, scadenza.getEsercizio_originale());
        sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.PG_OBBLIGAZIONE", SQLBuilder.EQUALS, scadenza.getPg_obbligazione());
        sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.PG_OBBLIGAZIONE_SCADENZARIO", SQLBuilder.EQUALS, scadenza.getPg_obbligazione_scadenzario());
        sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.STATO_COFI", SQLBuilder.NOT_EQUALS, Fattura_passiva_rigaBulk.STATO_ANNULLATO);
//FL_BOLLA_DOGANALE, FL_SPEDIZIONIERE
        //sql.addSQLClause("AND","FATTURA_PASSIVA.FL_BOLLA_DOGANALE",sql.EQUALS, Boolean.FALSE);
        //sql.addSQLClause("AND","FATTURA_PASSIVA.FL_SPEDIZIONIERE",sql.EQUALS, Boolean.FALSE);

        if (fattura != null) {
            sql.addSQLClause("AND", "FATTURA_PASSIVA.PG_FATTURA_PASSIVA", SQLBuilder.NOT_EQUALS, fattura.getPg_fattura_passiva());
            sql.addSQLClause("AND", "FATTURA_PASSIVA.CD_CDS", SQLBuilder.EQUALS, fattura.getCd_cds_origine());
            sql.addSQLClause("AND", "FATTURA_PASSIVA.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, fattura.getCd_uo_origine());
            sql.addSQLClause("AND", "FATTURA_PASSIVA.PG_LETTERA", SQLBuilder.ISNULL, null);
        }

        return sql;
    }

    /**
     * Inizializza la chiave primaria di un OggettoBulk per un
     * inserimento. Da usare principalmente per riempire i progressivi
     * automatici.
     *
     * @param bulk l'OggettoBulk da inizializzare
     */
    public SQLBuilder selectObbligazioniPer(
            it.cnr.jada.UserContext userContext,
            Fattura_passivaBulk fatturaPassiva,
            java.math.BigDecimal minIm_scadenza)
            throws PersistencyException {

        if (fatturaPassiva == null) return null;

        TerzoBulk fornitore = fatturaPassiva.getFornitore();
        if (fornitore != null) {
            SQLBuilder sql = createSQLBuilder();
            sql.addTableToHeader("FATTURA_PASSIVA");
            sql.addTableToHeader("OBBLIGAZIONE_SCADENZARIO");
            sql.addSQLJoin("FATTURA_PASSIVA_RIGA.ESERCIZIO", "FATTURA_PASSIVA.ESERCIZIO");
            sql.addSQLJoin("FATTURA_PASSIVA_RIGA.CD_CDS", "FATTURA_PASSIVA.CD_CDS");
            sql.addSQLJoin("FATTURA_PASSIVA_RIGA.CD_UNITA_ORGANIZZATIVA", "FATTURA_PASSIVA.CD_UNITA_ORGANIZZATIVA");
            sql.addSQLJoin("FATTURA_PASSIVA_RIGA.PG_FATTURA_PASSIVA", "FATTURA_PASSIVA.PG_FATTURA_PASSIVA");

            sql.addSQLJoin("FATTURA_PASSIVA_RIGA.CD_CDS_OBBLIGAZIONE", "OBBLIGAZIONE_SCADENZARIO.CD_CDS");
            sql.addSQLJoin("FATTURA_PASSIVA_RIGA.ESERCIZIO_OBBLIGAZIONE", "OBBLIGAZIONE_SCADENZARIO.ESERCIZIO");
            sql.addSQLJoin("FATTURA_PASSIVA_RIGA.ESERCIZIO_ORI_OBBLIGAZIONE", "OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE");
            sql.addSQLJoin("FATTURA_PASSIVA_RIGA.PG_OBBLIGAZIONE", "OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE");
            sql.addSQLJoin("FATTURA_PASSIVA_RIGA.PG_OBBLIGAZIONE_SCADENZARIO", "OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE_SCADENZARIO");

            sql.addSQLClause("AND", "FATTURA_PASSIVA.TI_FATTURA", SQLBuilder.EQUALS, Fattura_passiva_IBulk.TIPO_FATTURA_PASSIVA);
            sql.addSQLClause("AND", "FATTURA_PASSIVA.CD_TERZO", SQLBuilder.EQUALS, fornitore.getCd_terzo());
            //sql.addSQLClause("AND","FATTURA_PASSIVA.PG_LETTERA",sql.ISNULL, null);
            //sql.addSQLClause("AND","FATTURA_PASSIVA.PROTOCOLLO_IVA",sql.ISNULL, null);
            //sql.addSQLClause("AND","FATTURA_PASSIVA.PROTOCOLLO_IVA_GENERALE",sql.ISNULL, null);
            sql.addSQLClause("AND", "FATTURA_PASSIVA.STATO_PAGAMENTO_FONDO_ECO", SQLBuilder.EQUALS, Fattura_passiva_IBulk.NO_FONDO_ECO);
            sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.STATO_COFI", SQLBuilder.EQUALS, Fattura_passiva_rigaBulk.STATO_CONTABILIZZATO);
            sql.addSQLClause("AND", "FATTURA_PASSIVA.CD_CDS", SQLBuilder.EQUALS, fatturaPassiva.getCd_cds_origine());
            sql.addSQLClause("AND", "FATTURA_PASSIVA.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, fatturaPassiva.getCd_uo_origine());
            sql.addSQLClause("AND", "FATTURA_PASSIVA.ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

            //sql.addSQLClause("AND","FATTURA_PASSIVA.FL_BOLLA_DOGANALE",sql.EQUALS, Boolean.FALSE);
            //sql.addSQLClause("AND","FATTURA_PASSIVA.FL_SPEDIZIONIERE",sql.EQUALS, Boolean.FALSE);

            sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
            sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_SCADENZA", SQLBuilder.GREATER_EQUALS, minIm_scadenza);

            return sql;
        }
        return null;
    }

    private SQLBuilder selectRigaFor(Fattura_passiva_rigaIBulk rigaFattura) {

        SQLBuilder sql = createSQLBuilder();

        if (rigaFattura != null) {
            sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.CD_CDS_ASSNCNA_FIN", SQLBuilder.EQUALS, rigaFattura.getCd_cds());
            sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.CD_UO_ASSNCNA_FIN", SQLBuilder.EQUALS, rigaFattura.getCd_unita_organizzativa());
            sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.ESERCIZIO_RIGA_ASSNCNA_FIN", SQLBuilder.EQUALS, rigaFattura.getEsercizio());
            sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.PG_FATTURA_ASSNCNA_FIN", SQLBuilder.EQUALS, rigaFattura.getPg_fattura_passiva());
            sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.PG_RIGA_ASSNCNA_FIN", SQLBuilder.EQUALS, rigaFattura.getProgressivo_riga());
        }
        return sql;
    }

    public SQLBuilder selectModalita(Fattura_passiva_rigaBulk rigaFattura, it.cnr.contab.docamm00.tabrif.bulk.DivisaHome divisaHome, it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk clause) {

        return divisaHome.createSQLBuilder();
    }
}
