package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk;
import it.cnr.contab.ordmag.ordini.bulk.*;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;


/**
 * Insert the type's description here.
 * Creation date: (10/24/2001 2:31:46 PM)
 *
 * @author: Roberto Peli
 */
public class Fattura_passiva_IHome
        extends Fattura_passivaHome
        implements IDocumentoAmministrativoSpesaHome {
    /**
     * Fattura_passiva_IHome constructor comment.
     *
     * @param conn java.sql.Connection
     */
    public Fattura_passiva_IHome(java.sql.Connection conn) {
        super(Fattura_passiva_IBulk.class, conn);
    }

    /**
     * Fattura_passiva_IHome constructor comment.
     *
     * @param conn            java.sql.Connection
     * @param persistentCache it.cnr.jada.persistency.PersistentCache
     */
    public Fattura_passiva_IHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
        super(Fattura_passiva_IBulk.class, conn, persistentCache);
    }

    /**
     * Ritorna un SQLBuilder con la columnMap del ricevente
     */
    private void addFatturaEsteraClauses(SQLBuilder sql, Fattura_passiva_IBulk fatturaPassiva) {

        sql.addClause("AND", "esercizio_fat_clgs", sql.EQUALS, fatturaPassiva.getEsercizio());
        sql.addClause("AND", "cd_cds_fat_clgs", sql.EQUALS, fatturaPassiva.getCd_cds());
        sql.addClause("AND", "cd_uo_fat_clgs", sql.EQUALS, fatturaPassiva.getCd_unita_organizzativa());
        sql.addClause("AND", "pg_fattura_passiva_fat_clgs", sql.EQUALS, fatturaPassiva.getPg_fattura_passiva());
    }

    /**
     * Ritorna un SQLBuilder con la columnMap del ricevente
     */
    public SQLBuilder createSQLBuilder() {

        SQLBuilder sql = super.createSQLBuilder();
        sql.addSQLClause("AND", "FATTURA_PASSIVA.TI_FATTURA", sql.EQUALS, Fattura_passiva_IBulk.TIPO_FATTURA_PASSIVA);
        return sql;
    }

    /**
     * Ritorna un SQLBuilder con la columnMap del ricevente
     */
    public SQLBuilder selectBolleDoganaliPer(Fattura_passiva_IBulk fatturaPassiva) {

        SQLBuilder sql = createSQLBuilder();

        sql.addClause("AND", "fl_bolla_doganale", sql.EQUALS, Boolean.TRUE);

        addFatturaEsteraClauses(sql, fatturaPassiva);

        return sql;
    }

    /**
     * Ritorna un SQLBuilder con la columnMap del ricevente
     */
    public SQLBuilder selectSpedizionieriPer(Fattura_passiva_IBulk fatturaPassiva) {

        SQLBuilder sql = createSQLBuilder();

        sql.addClause("AND", "fl_spedizioniere", sql.EQUALS, Boolean.TRUE);

        addFatturaEsteraClauses(sql, fatturaPassiva);

        return sql;
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/10/2002 3:27:22 PM)
     */
    public void updateFondoEconomale(
            Fondo_spesaBulk spesa)
            throws it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.bulk.OutdatedResourceException,
            it.cnr.jada.bulk.BusyResourceException {

        if (spesa == null) return;

        Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk) spesa.getDocumento();

        lock(fp);

        StringBuffer stm = new StringBuffer("UPDATE ");
        stm.append(it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema());
        stm.append(getColumnMap().getTableName());
        stm.append(" SET STATO_PAGAMENTO_FONDO_ECO = ?, DT_PAGAMENTO_FONDO_ECO = ?, PG_VER_REC = PG_VER_REC+1, DUVA = ?, UTUV = ?");
        stm.append(" WHERE (");
        stm.append("CD_CDS = ? AND CD_UNITA_ORGANIZZATIVA = ? AND ESERCIZIO = ? AND PG_FATTURA_PASSIVA = ? )");

        try {
            LoggableStatement ps = new LoggableStatement(getConnection(), stm.toString(), true, this.getClass());
            try {
                ps.setString(1, (spesa.isToBeCreated() || spesa.isToBeUpdated()) ? fp.REGISTRATO_IN_FONDO_ECO : fp.FONDO_ECO);
                if (spesa.isToBeCreated() || spesa.isToBeUpdated())
                    ps.setTimestamp(2, spesa.getDt_spesa());
                else
                    ps.setNull(2, java.sql.Types.TIMESTAMP);

                ps.setTimestamp(3, getServerTimestamp());
                ps.setString(4, spesa.getUser());


                ps.setString(5, fp.getCd_cds());
                ps.setString(6, fp.getCd_unita_organizzativa());
                ps.setInt(7, fp.getEsercizio().intValue());
                ps.setLong(8, fp.getPg_fattura_passiva().longValue());

                ps.executeUpdate();
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
                ;
            }
        } catch (java.sql.SQLException e) {
            throw it.cnr.jada.persistency.sql.SQLExceptionHandler.getInstance().handleSQLException(e, spesa);
        }
    }

    public SQLBuilder selectContabilizzaRigaByClause(Fattura_passiva_IBulk fatturaPassiva, EvasioneOrdineRigaHome home,
                                                     EvasioneOrdineRigaBulk evasioneOrdineRigaBulk, CompoundFindClause findclause) throws PersistencyException {
        home.setColumnMap("V_EVASIONE_ORDINE");
        SQLBuilder sqlBuilder = home.createSQLBuilder();
        sqlBuilder.setAutoJoins(true);

        sqlBuilder.generateJoin("evasioneOrdine", "EVASIONE_ORDINE");
        sqlBuilder.addSQLClause(FindClause.AND, "EVASIONE_ORDINE.STATO", SQLBuilder.EQUALS, OrdineAcqConsegnaBulk.STATO_INSERITA);
        sqlBuilder.addSQLClause(FindClause.AND, "EVASIONE_ORDINE.DATA_BOLLA", SQLBuilder.LESS_EQUALS, fatturaPassiva.getDt_fattura_fornitore());

        sqlBuilder.generateJoin("ordineAcqConsegna", "ORDINE_ACQ_CONSEGNA");
        sqlBuilder.addSQLClause(FindClause.AND, "ORDINE_ACQ_CONSEGNA.STATO_FATT", SQLBuilder.NOT_EQUALS, OrdineAcqConsegnaBulk.STATO_FATT_ASSOCIATA_TOTALMENTE);
        sqlBuilder.addSQLClause(FindClause.AND, "ORDINE_ACQ_CONSEGNA.STATO", SQLBuilder.NOT_EQUALS, OrdineAcqConsegnaBulk.STATO_EVASA);

        sqlBuilder.generateJoin(OrdineAcqConsegnaBulk.class, OrdineAcqRigaBulk.class, "ordineAcqRiga", "ORDINE_ACQ_RIGA");
        sqlBuilder.generateJoin(OrdineAcqRigaBulk.class, OrdineAcqBulk.class, "ordineAcq", "ORDINE_ACQ");

        sqlBuilder.addSQLClause(FindClause.AND, "ORDINE_ACQ.CD_TERZO", SQLBuilder.EQUALS, fatturaPassiva.getCd_terzo());
        sqlBuilder.addSQLClause(FindClause.AND, "ORDINE_ACQ.TI_ATTIVITA", SQLBuilder.EQUALS, fatturaPassiva.getTi_istituz_commerc());
        /*
        sqlBuilder.addTableToHeader("ORDINE_ACQ_RIGA");
        sqlBuilder.addSQLJoin("ORDINE_ACQ_RIGA.CD_CDS","ORDINE_ACQ_CONSEGNA.CD_CDS");
        sqlBuilder.addSQLJoin("ORDINE_ACQ_RIGA.CD_UNITA_OPERATIVA","ORDINE_ACQ_CONSEGNA.CD_UNITA_OPERATIVA");
        sqlBuilder.addSQLJoin("ORDINE_ACQ_RIGA.ESERCIZIO","ORDINE_ACQ_CONSEGNA.ESERCIZIO");
        sqlBuilder.addSQLJoin("ORDINE_ACQ_RIGA.CD_NUMERATORE","ORDINE_ACQ_CONSEGNA.CD_NUMERATORE");
        sqlBuilder.addSQLJoin("ORDINE_ACQ_RIGA.NUMERO","ORDINE_ACQ_CONSEGNA.NUMERO");
        sqlBuilder.addSQLJoin("ORDINE_ACQ_RIGA.RIGA","ORDINE_ACQ_CONSEGNA.RIGA");

        sqlBuilder.addTableToHeader("ORDINE_ACQ");
        sqlBuilder.addSQLJoin("ORDINE_ACQ_RIGA.CD_CDS","ORDINE_ACQ.CD_CDS");
        sqlBuilder.addSQLJoin("ORDINE_ACQ_RIGA.CD_UNITA_OPERATIVA","ORDINE_ACQ.CD_UNITA_OPERATIVA");
        sqlBuilder.addSQLJoin("ORDINE_ACQ_RIGA.ESERCIZIO","ORDINE_ACQ.ESERCIZIO");
        sqlBuilder.addSQLJoin("ORDINE_ACQ_RIGA.CD_NUMERATORE","ORDINE_ACQ.CD_NUMERATORE");
        sqlBuilder.addSQLJoin("ORDINE_ACQ_RIGA.NUMERO","ORDINE_ACQ.NUMERO");
        sqlBuilder.addSQLClause(FindClause.AND, "ORDINE_ACQ.CD_TERZO", SQLBuilder.EQUALS, fatturaPassiva.getCd_terzo());
        sqlBuilder.addSQLClause(FindClause.AND, "ORDINE_ACQ.TI_ATTIVITA", SQLBuilder.EQUALS, fatturaPassiva.getTi_istituz_commerc());
        */
        return sqlBuilder;
    }
}
