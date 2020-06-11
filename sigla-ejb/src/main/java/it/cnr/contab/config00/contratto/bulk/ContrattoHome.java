/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Creted by Generator 1.0
 * Date 09/04/2005
 */
package it.cnr.contab.config00.contratto.bulk;

import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.config00.consultazioni.bulk.VContrattiTotaliDetBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SQLUnion;

import java.sql.SQLException;
import java.util.List;

public class ContrattoHome extends BulkHome {
    public ContrattoHome(java.sql.Connection conn) {
        super(ContrattoBulk.class, conn);
    }

    public ContrattoHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(ContrattoBulk.class, conn, persistentCache);
    }

    public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk contratto) throws PersistencyException, ApplicationException {
        if (((ContrattoBulk) contratto).getPg_contratto() == null && ((ContrattoBulk) contratto).getStato().equals(ContrattoBulk.STATO_PROVVISORIO)) {
            try {
                it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession numerazione =
                        (it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession)
                                it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_TABNUM_EJB_Numerazione_baseComponentSession",
                                        it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession.class);
                ((ContrattoBulk) contratto).setPg_contratto(
                        numerazione.creaNuovoProgressivo(userContext, CNRUserContext.getEsercizio(userContext), "CONTRATTO", "PG_CONTRATTO_PROVVISORIO", CNRUserContext.getUser(userContext))
                );
            } catch (it.cnr.jada.bulk.BusyResourceException e) {
                throw new ApplicationException(e);
            } catch (Throwable e) {
                throw new PersistencyException(e);
            }
        }
    }

    /**
     * Recupera tutti i dati nella tabella ASS_CONTRATTO_UO relativi alla testata in uso.
     *
     * @param testata La testata in uso.
     * @return java.util.Collection Collezione di oggetti <code>Ass_contratto_uoBulk</code>
     */

    public java.util.Collection findAssociazioneUO(ContrattoBulk testata) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(Ass_contratto_uoBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, testata.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO", sql.EQUALS, testata.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO", sql.EQUALS, testata.getPg_contratto());
        sql.addOrderBy("CD_UNITA_ORGANIZZATIVA");
        return dettHome.fetchAll(sql);
    }

    /**
     * Recupera tutte le Uo disponibili ad essere associate
     *
     * @param testata La testata in uso.
     * @return java.util.Collection Collezione di oggetti <code>Ass_contratto_uoBulk</code>
     */

    public java.util.Collection findAssociazioneUODisponibili(ContrattoBulk testata) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(Unita_organizzativaBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        if (!testata.getAssociazioneUO().isEmpty()) {
            SQLBuilder sqlEx = getHomeCache().getHome(Ass_contratto_uoBulk.class).createSQLBuilder();
            sqlEx.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, testata.getEsercizio());
            sqlEx.addSQLClause("AND", "STATO_CONTRATTO", sql.EQUALS, testata.getStato());
            sqlEx.addSQLClause("AND", "PG_CONTRATTO", sql.EQUALS, testata.getPg_contratto());
            sqlEx.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA", "ASS_CONTRATTO_UO.CD_UNITA_ORGANIZZATIVA");
            sql.addSQLNotExistsClause("AND", sqlEx);
        }
        sql.addOrderBy("CD_UNITA_ORGANIZZATIVA");
        return dettHome.fetchAll(sql);
    }

    /**
     * @param tipo_contratto
     * @return true se esiste un contratto valido per il tipo passato, false altrimenti
     * @throws IntrospectionException
     * @throws PersistencyException
     * @throws SQLException
     * @author mspasiano
     */
    public boolean existsContrattoValidoForTipo(Tipo_contrattoBulk tipo_contratto) throws IntrospectionException, PersistencyException, SQLException {
        SQLBuilder sql = createSQLBuilder();
        sql.addSQLClause("AND", "STATO", sql.EQUALS, ContrattoBulk.STATO_DEFINITIVO);
        sql.addSQLClause("AND", "CD_TIPO_CONTRATTO", sql.EQUALS, tipo_contratto.getCd_tipo_contratto());
        return sql.executeExistsQuery(getConnection());
    }

    /**
     * @param tipo_atto_amministrativo
     * @return true se esiste un contratto valido per il tipo_atto passato, false altrimenti
     * @throws IntrospectionException
     * @throws PersistencyException
     * @throws SQLException
     * @author mspasiano
     */
    public boolean existsContrattoValidoForProvvedimento(Tipo_atto_amministrativoBulk tipo_atto_amministrativo) throws IntrospectionException, PersistencyException, SQLException {
        SQLBuilder sql = createSQLBuilder();
        sql.addSQLClause("AND", "STATO", sql.EQUALS, ContrattoBulk.STATO_DEFINITIVO);
        sql.openParenthesis("AND");
        sql.addSQLClause("AND", "CD_TIPO_ATTO", sql.EQUALS, tipo_atto_amministrativo.getCd_tipo_atto());
        sql.addSQLClause("OR", "CD_TIPO_ATTO_ANN", sql.EQUALS, tipo_atto_amministrativo.getCd_tipo_atto());
        sql.closeParenthesis();
        return sql.executeExistsQuery(getConnection());
    }

    /**
     * @param tipo_organo
     * @return true se esiste un contratto valido per il tipo_organo passato, false altrimenti
     * @throws IntrospectionException
     * @throws PersistencyException
     * @throws SQLException
     * @author mspasiano
     */
    public boolean existsContrattoValidoForOrgano(OrganoBulk tipo_organo) throws IntrospectionException, PersistencyException, SQLException {
        SQLBuilder sql = createSQLBuilder();
        sql.addSQLClause("AND", "STATO", sql.EQUALS, ContrattoBulk.STATO_DEFINITIVO);
        sql.openParenthesis("AND");
        sql.addSQLClause("AND", "CD_ORGANO", sql.EQUALS, tipo_organo.getCd_organo());
        sql.addSQLClause("OR", "CD_ORGANO_ANN", sql.EQUALS, tipo_organo.getCd_organo());
        sql.closeParenthesis();
        return sql.executeExistsQuery(getConnection());
    }

    /**
     * @param proc_amm
     * @return
     * @throws IntrospectionException
     * @throws PersistencyException
     * @throws SQLException
     * @author mspasiano
     */
    public boolean existsContrattoValidoForProcedure_amministrative(Procedure_amministrativeBulk proc_amm) throws IntrospectionException, PersistencyException, SQLException {
        SQLBuilder sql = createSQLBuilder();
        sql.addSQLClause("AND", "STATO", sql.EQUALS, ContrattoBulk.STATO_DEFINITIVO);
        sql.addSQLClause("AND", "CD_PROC_AMM", sql.EQUALS, proc_amm.getCd_proc_amm());
        return sql.executeExistsQuery(getConnection());
    }

    /**
     * @param contratto
     * @return true se esiste un documento contabile associato, false altrimenti
     * @throws IntrospectionException
     * @throws PersistencyException
     * @throws SQLException
     * @author mspasiano
     */
    public boolean existsDocContForContratto(ContrattoBulk contratto) throws IntrospectionException, PersistencyException, SQLException {
        PersistentHome dettHomeAcc = getHomeCache().getHome(AccertamentoBulk.class);
        SQLBuilder sqlAcc = dettHomeAcc.createSQLBuilder();
        sqlAcc.resetColumns();
        sqlAcc.addColumn("PG_CONTRATTO");
        sqlAcc.addSQLClause("AND", "ESERCIZIO_CONTRATTO", SQLBuilder.EQUALS, contratto.getEsercizio());
        sqlAcc.addSQLClause("AND", "STATO_CONTRATTO", SQLBuilder.EQUALS, contratto.getStato());
        sqlAcc.addSQLClause("AND", "PG_CONTRATTO", SQLBuilder.EQUALS, contratto.getPg_contratto());

        PersistentHome dettHomeObb = getHomeCache().getHome(ObbligazioneBulk.class);
        SQLBuilder sqlObb = dettHomeObb.createSQLBuilder();
        sqlObb.resetColumns();
        sqlObb.addColumn("PG_CONTRATTO");
        sqlObb.addSQLClause("AND", "ESERCIZIO_CONTRATTO", SQLBuilder.EQUALS, contratto.getEsercizio());
        sqlObb.addSQLClause("AND", "STATO_CONTRATTO", SQLBuilder.EQUALS, contratto.getStato());
        sqlObb.addSQLClause("AND", "PG_CONTRATTO", SQLBuilder.EQUALS, contratto.getPg_contratto());

        SQLUnion union = sqlObb.union(sqlAcc, true);
        return union.executeExistsQuery(getConnection());
    }

    /**
     * Recupera tutti gli accertamenti legati al contratto
     */
    public SQLBuilder findAccertamenti(it.cnr.jada.UserContext userContext, ContrattoBulk contratto) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(AccertamentoBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO", sql.EQUALS, contratto.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO", SQLBuilder.EQUALS, contratto.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO", sql.EQUALS, contratto.getPg_contratto());
        return sql;
    }

    /**
     * Recupera tutte le obbligazioni legate al contratto
     */
    public SQLBuilder findObbligazioni(it.cnr.jada.UserContext userContext, ContrattoBulk contratto) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(ObbligazioneBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO", sql.EQUALS, contratto.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO", SQLBuilder.EQUALS, contratto.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO", sql.EQUALS, contratto.getPg_contratto());
        return sql;
    }

    public SQLBuilder findCompensi(it.cnr.jada.UserContext userContext, ContrattoBulk contratto) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(CompensoBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO", sql.EQUALS, contratto.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO", SQLBuilder.EQUALS, contratto.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO", sql.EQUALS, contratto.getPg_contratto());
        return sql;
    }

    /**
     * Recupera il totale degli accertamenti legati al contratto
     *
     * @param esercizio   del contratto
     * @param progressivo del contratto
     * @return java.math.BigDecimal
     */

    public SQLBuilder calcolaTotAccertamenti(it.cnr.jada.UserContext userContext, ContrattoBulk contratto) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(TOTALE_ENTRATE) TOTALE ");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO", sql.EQUALS, contratto.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO", SQLBuilder.EQUALS, contratto.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO", sql.EQUALS, contratto.getPg_contratto());
        return sql;
    }

    /**
     * Recupera il totale degli accertamenti legati ai contratti del contratto di riferimento
     *
     * @param contratto_padre
     * @return java.math.BigDecimal
     */

    public SQLBuilder calcolaTotAccertamentiPadre(it.cnr.jada.UserContext userContext, ContrattoBulk contratto_padre) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(TOTALE_ENTRATE) TOTALE");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO_PADRE", SQLBuilder.EQUALS, contratto_padre.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getPg_contratto());
        return sql;
    }

    /**
     * Recupera il totale deglle Obbligazioni legati al contratto
     *
     * @param esercizio   del contratto
     * @param progressivo del contratto
     * @return java.math.BigDecimal
     */

    public SQLBuilder calcolaTotObbligazioni(it.cnr.jada.UserContext userContext, ContrattoBulk contratto) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(TOTALE_SPESE) TOTALE ");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO", sql.EQUALS, contratto.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO", SQLBuilder.EQUALS, contratto.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO", sql.EQUALS, contratto.getPg_contratto());
        return sql;
    }

    /**
     * Recupera il totale deglle Obbligazioni legate ai contratti del contratto di riferimento
     *
     * @param contratto_padre
     * @return java.math.BigDecimal
     */

    public SQLBuilder calcolaTotObbligazioniPadre(it.cnr.jada.UserContext userContext, ContrattoBulk contratto_padre) throws IntrospectionException, PersistencyException {

        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(TOTALE_SPESE) TOTALE");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO_PADRE", SQLBuilder.EQUALS, contratto_padre.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getPg_contratto());
        return sql;
    }

    public SQLBuilder calcolaTotDocumentiAtt(it.cnr.jada.UserContext userContext, ContrattoBulk contratto) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(LIQUIDATO_ENTRATE) TOTALE");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO", sql.EQUALS, contratto.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO", SQLBuilder.EQUALS, contratto.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO", sql.EQUALS, contratto.getPg_contratto());
        return sql;
    }

    public SQLBuilder calcolaTotDocumentiPas(it.cnr.jada.UserContext userContext, ContrattoBulk contratto) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(LIQUIDATO_SPESE) TOTALE");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO", sql.EQUALS, contratto.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO", SQLBuilder.EQUALS, contratto.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO", sql.EQUALS, contratto.getPg_contratto());
        return sql;
    }

    public SQLBuilder calcolaTotDocumentiPasNetto(it.cnr.jada.UserContext userContext, ContrattoBulk contratto) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(LIQUIDATO_SPESE_NETTO) TOTALE");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO", sql.EQUALS, contratto.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO", SQLBuilder.EQUALS, contratto.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO", sql.EQUALS, contratto.getPg_contratto());
        return sql;
    }

    public SQLBuilder calcolaTotReversali(it.cnr.jada.UserContext userContext, ContrattoBulk contratto) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(TOTALE_REVERSALI) TOTALE");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO", sql.EQUALS, contratto.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO", SQLBuilder.EQUALS, contratto.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO", sql.EQUALS, contratto.getPg_contratto());
        return sql;
    }

    public SQLBuilder calcolaTotMandati(it.cnr.jada.UserContext userContext, ContrattoBulk contratto) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(TOTALE_MANDATI) TOTALE");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO", sql.EQUALS, contratto.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO", SQLBuilder.EQUALS, contratto.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO", sql.EQUALS, contratto.getPg_contratto());
        return sql;
    }

    public SQLBuilder calcolaTotMandatiNetto(it.cnr.jada.UserContext userContext, ContrattoBulk contratto) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(TOTALE_MANDATI_NETTO) TOTALE");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO", sql.EQUALS, contratto.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO", SQLBuilder.EQUALS, contratto.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO", sql.EQUALS, contratto.getPg_contratto());
        return sql;
    }

    public SQLBuilder calcolaTotDocumentiAttPadre(it.cnr.jada.UserContext userContext, ContrattoBulk contratto_padre) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(LIQUIDATO_ENTRATE) TOTALE");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO_PADRE", SQLBuilder.EQUALS, contratto_padre.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getPg_contratto());
        return sql;
    }

    public SQLBuilder calcolaTotDocumentiPasPadre(it.cnr.jada.UserContext userContext, ContrattoBulk contratto_padre) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(LIQUIDATO_SPESE) TOTALE");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO_PADRE", SQLBuilder.EQUALS, contratto_padre.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getPg_contratto());
        return sql;
    }

    public SQLBuilder calcolaTotDocumentiPasNettoPadre(it.cnr.jada.UserContext userContext, ContrattoBulk contratto_padre) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(LIQUIDATO_SPESE_NETTO) TOTALE");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO_PADRE", SQLBuilder.EQUALS, contratto_padre.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getPg_contratto());
        return sql;
    }

    public SQLBuilder calcolaTotReversaliPadre(it.cnr.jada.UserContext userContext, ContrattoBulk contratto_padre) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(TOTALE_REVERSALI) TOTALE");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO_PADRE", SQLBuilder.EQUALS, contratto_padre.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getPg_contratto());
        return sql;
    }

    public SQLBuilder calcolaTotMandatiPadreNetto(it.cnr.jada.UserContext userContext, ContrattoBulk contratto_padre) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(TOTALE_MANDATI_NETTO) TOTALE");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO_PADRE", SQLBuilder.EQUALS, contratto_padre.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getPg_contratto());
        return sql;
    }

    public SQLBuilder calcolaTotMandatiPadre(it.cnr.jada.UserContext userContext, ContrattoBulk contratto_padre) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(TOTALE_MANDATI) TOTALE");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO_PADRE", SQLBuilder.EQUALS, contratto_padre.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getPg_contratto());
        return sql;
    }

    public java.util.Collection findDitteAssociate(it.cnr.jada.UserContext userContext, ContrattoBulk contratto, String tipologia) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(Ass_contratto_ditteBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, contratto.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO", SQLBuilder.EQUALS, contratto.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO", sql.EQUALS, contratto.getPg_contratto());
        sql.addSQLClause("AND", "TIPOLOGIA", sql.EQUALS, tipologia);
        sql.setOrderBy("denominazione_rti", it.cnr.jada.util.OrderConstants.ORDER_ASC);
        return dettHome.fetchAll(sql);
    }

    public SQLBuilder calcolaTotOrdini(it.cnr.jada.UserContext userContext, ContrattoBulk contratto) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(TOTALE_ORDINI) TOTALE ");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO", sql.EQUALS, contratto.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO", SQLBuilder.EQUALS, contratto.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO", sql.EQUALS, contratto.getPg_contratto());
        return sql;
    }

    /**
     * Recupera il totale deglle Obbligazioni legate ai contratti del contratto di riferimento
     *
     * @param contratto_padre
     * @return java.math.BigDecimal
     */

    public SQLBuilder calcolaTotOrdiniPadre(it.cnr.jada.UserContext userContext, ContrattoBulk contratto_padre) throws IntrospectionException, PersistencyException {

        PersistentHome dettHome = getHomeCache().getHome(VContrattiTotaliDetBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(TOTALE_ORDINI) TOTALE");
        sql.addSQLClause("AND", "ESERCIZIO_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getEsercizio());
        sql.addSQLClause("AND", "STATO_CONTRATTO_PADRE", SQLBuilder.EQUALS, contratto_padre.getStato());
        sql.addSQLClause("AND", "PG_CONTRATTO_PADRE", sql.EQUALS, contratto_padre.getPg_contratto());
        return sql;
    }

    public List<ContrattoBulk> findByCIG(UserContext userContext, String cig) throws PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        sql.addSQLClause("AND", "CD_CIG", SQLBuilder.EQUALS, cig);
        final List all = fetchAll(sql);
        getHomeCache().fetchAll(userContext);
        return all;
    }
}