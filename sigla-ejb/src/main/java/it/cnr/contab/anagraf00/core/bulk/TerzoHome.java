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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.Optional;

public class TerzoHome extends BulkHome {
    protected TerzoHome(Class clazz, java.sql.Connection conn) {
        super(clazz, conn);
    }

    public TerzoHome(Class clazz, java.sql.Connection conn, PersistentCache persistentCache) {
        super(clazz, conn, persistentCache);
    }

    public TerzoHome(java.sql.Connection conn) {
        super(TerzoBulk.class, conn);
    }

    public TerzoHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(TerzoBulk.class, conn, persistentCache);
    }

    /**
     * Restituisce l'oggetto Anagrafoco del'Ente.
     *
     * @return AnagrafocoBulk del'Ente.
     */

    public AnagraficoBulk findAnagraficoEnte() throws PersistencyException {
        try {
            LoggableStatement ps = new LoggableStatement(getConnection(),
                    "SELECT ANAG.CD_ANAG FROM " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CONFIGURAZIONE_CNR CONF, " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "ANAGRAFICO ANAG " +
                            "WHERE " +
                            "CONF.CD_CHIAVE_PRIMARIA = ? AND " +
                            "CONF.CD_CHIAVE_SECONDARIA = ? AND " +
                            "CONF.ESERCIZIO = ? AND " +
                            "CONF.IM01 = ANAG.CD_ANAG", true, this.getClass());
            ps.setString(1, "COSTANTI");
            ps.setObject(2, "CODICE_ANAG_ENTE");
            ps.setObject(3, new Integer(0));

            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next())
                return (it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk) getHomeCache(
                ).getHome(
                        it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk.class
                ).findByPrimaryKey(
                        new it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk(
                                new Integer(
                                        rs.getInt(1)
                                )
                        )
                );
            else
                return null;
        } catch (java.sql.SQLException e) {
            throw new PersistencyException(e);
        }
    }

    /**
     * Restituisce l'oggetto Terzo dell'Ente.
     *
     * @return terzoBulk dell'Ente.
     */
    public TerzoBulk findTerzoEnte() throws PersistencyException {
        AnagraficoBulk anagraficoEnte = findAnagraficoEnte();
        Unita_organizzativa_enteBulk unitaEnte = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
        SQLBuilder sql = createSQLBuilder();
        sql.addClause("AND", "cd_anag", sql.EQUALS, anagraficoEnte.getCd_anag());
        sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, unitaEnte.getCd_unita_organizzativa());
        return (TerzoBulk) fetchAll(sql).get(0);
    }

    /**
     * Recupera tutti i dati nella tabella BANCA relativi all'anagrafica in uso.
     *
     * @param anagrafico L'anagrafica in uso.
     * @return java.util.Collection Collezione di oggetti <code>BancaBulk</code>
     */

    public java.util.Collection findBanca(TerzoBulk terzo) throws IntrospectionException, PersistencyException {
        PersistentHome bancaHome = getHomeCache().getHome(BancaBulk.class);
        SQLBuilder sql = bancaHome.createSQLBuilder();
        sql.addClause("AND", "cd_terzo", sql.EQUALS, Optional.ofNullable(terzo.getCd_terzo()).orElse(-1));
        return bancaHome.fetchAll(sql);
    }

    /**
     * Attiva la ricerca dei cap associati al comune selezionato.
     *
     * @param terzo   <code>TerzoBulk</code>
     * @param capHome <code>it.cnr.contab.anagraf00.tabter.bulk.CapHome</code>
     * @param clause  <code>it.cnr.contab.anagraf00.tabter.bulk.CapBulk</code>
     * @return La <code>java.util.Collection</code> contenente i cap.
     * @exeption IntrospectionException
     * @exeption PersistencyException
     */
    public java.util.Collection findCaps_comune(TerzoBulk terzo, it.cnr.contab.anagraf00.tabter.bulk.CapHome capHome, it.cnr.contab.anagraf00.tabter.bulk.CapBulk clause) throws IntrospectionException, PersistencyException {
        return ((it.cnr.contab.anagraf00.tabter.bulk.ComuneHome) getHomeCache().getHome(it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk.class)).findCaps(terzo.getComune_sede());
    }

    /**
     * Recupera tutti i dati nella tabella CONTATTO relativi all'anagrafica in uso.
     *
     * @param anagrafico L'anagrafica in uso.
     * @return java.util.Collection Collezione di oggetti <code>ContattoBulk</code>
     */

    public java.util.Collection findContatti(TerzoBulk terzo) throws IntrospectionException, PersistencyException {
        PersistentHome contattoHome = getHomeCache().getHome(ContattoBulk.class);
        SQLBuilder sql = contattoHome.createSQLBuilder();
        sql.addClause("AND", "cd_terzo", sql.EQUALS, Optional.ofNullable(terzo.getCd_terzo()).orElse(-1));
        return contattoHome.fetchAll(sql);
    }

    /**
     * Recupera tutti i dati nella tabella MODALITA_PAGAMENTO relativi all'anagrafica in uso.
     *
     * @param anagrafico L'anagrafica in uso.
     * @return java.util.Collection Collezione di oggetti <code>Modalita_pagamentoBulk</code>
     */

    public java.util.Collection findModalita_pagamento(TerzoBulk terzo) throws IntrospectionException, PersistencyException {
        PersistentHome modalita_pagamentoHome = getHomeCache().getHome(Modalita_pagamentoBulk.class);
        SQLBuilder sql = modalita_pagamentoHome.createSQLBuilder();
        sql.addClause("AND", "cd_terzo", sql.EQUALS, terzo.getCd_terzo());
        return modalita_pagamentoHome.fetchAll(sql);
    }

    public java.util.Collection findRif_modalita_pagamento(TerzoBulk terzo) throws PersistencyException, IntrospectionException {
        return findRif_modalita_pagamento(terzo, Boolean.FALSE);
    }

    public java.util.Collection findRif_modalita_pagamento(TerzoBulk terzo, Boolean flPerCessione) throws PersistencyException, IntrospectionException {
        return getHomeCache().getHome(Rif_modalita_pagamentoBulk.class).fetchAll(selectRif_modalita_pagamento(terzo, flPerCessione));
    }

    public java.util.Collection findRif_termini_pagamento(TerzoBulk terzo) throws PersistencyException, IntrospectionException {
        return getHomeCache().getHome(Rif_termini_pagamentoBulk.class).fetchAll(selectRif_termini_pagamento(terzo));
    }

    public java.util.Collection findRif_termini_pagamento_disponibili(TerzoBulk terzo) throws IntrospectionException, PersistencyException {
        PersistentHome termini_pagamentoHome = getHomeCache().getHome(Rif_termini_pagamentoBulk.class);
        SQLBuilder sql = termini_pagamentoHome.createSQLBuilder();
        sql.addClause("AND", "cd_terzo", sql.EQUALS, terzo.getCd_terzo());

        SQLBuilder sql2 = getHomeCache().getHome(Termini_pagamentoBulk.class).createSQLBuilder();
        sql2.addClause("AND", "cd_terzo", sql.EQUALS, terzo.getCd_terzo());
        sql2.addSQLJoin("TERMINI_PAGAMENTO.CD_TERMINI_PAG", "RIF_TERMINI_PAGAMENTO.CD_TERMINI_PAG");

        sql.addSQLNotExistsClause("AND", sql2);

        return termini_pagamentoHome.fetchAll(sql);

    }

    /**
     * Recupera tutti i dati nella tabella TELEFONO relativi all'anagrafica in uso.
     *
     * @param anagrafico   L'anagrafica in uso.
     * @param telefonoHome <code>TelefonoHome</code>.
     * @return java.util.Collection Collezione di oggetti <code>TelefonoBulk</code>
     */

    public java.util.Collection findTelefoni(AnagraficoBulk anagrafico, PersistentHome telefonoHome) throws IntrospectionException, PersistencyException {
        SQLBuilder sql = telefonoHome.createSQLBuilder();
        sql.addSQLClause("AND", "CD_ANAG", sql.EQUALS, anagrafico.getCd_anag());
        return telefonoHome.fetchAll(sql);
    }

    /**
     * Recupera tutti i dati nella tabella TELEFONO relativi all'anagrafica in uso.
     *
     * @param anagrafico   L'anagrafica in uso.
     * @param telefonoHome <code>TelefonoHome</code>.
     * @return java.util.Collection Collezione di oggetti <code>TelefonoBulk</code>
     */

    public java.util.Collection findTelefoni(TerzoBulk terzo, String ti_riferimento) throws IntrospectionException, PersistencyException {
        PersistentHome telefonoHome = getHomeCache().getHome(TelefonoBulk.class);
        SQLBuilder sql = telefonoHome.createSQLBuilder();
        sql.addClause("AND", "cd_terzo", sql.EQUALS, Optional.ofNullable(terzo.getCd_terzo()).orElse(-1));
        sql.addClause("AND", "ti_riferimento", sql.EQUALS, ti_riferimento);
        return telefonoHome.fetchAll(sql);
    }

    /**
     * Recupera tutti i dati nella tabella TERMINI_PAGAMENTO relativi all'anagrafica in uso.
     *
     * @param anagrafico L'anagrafica in uso.
     * @return java.util.Collection Collezione di oggetti <code>Termini_pagamentoBulk</code>
     */

    public java.util.Collection findTermini_pagamento(TerzoBulk terzo) throws IntrospectionException, PersistencyException {
        PersistentHome termini_pagamentoHome = getHomeCache().getHome(Termini_pagamentoBulk.class);
        SQLBuilder sql = termini_pagamentoHome.createSQLBuilder();
        sql.addClause("AND", "cd_terzo", sql.EQUALS, Optional.ofNullable(terzo.getCd_terzo()).orElse(-1));
        return termini_pagamentoHome.fetchAll(sql);
    }

    /**
     * Imposta il pg_banca di un oggetto <code>BancaBulk</code>.
     *
     * @param bank <code>BancaBulk</code>
     * @throws PersistencyException
     * @see getCd_terzo
     */

    public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk trz) throws PersistencyException, ApplicationException {
        try {
            it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession numerazione =
                    (it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession)
                            it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_TABNUM_EJB_Numerazione_baseComponentSession",
                                    it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession.class);
            ((TerzoBulk) trz).setCd_terzo(
                    new Integer(
                            (numerazione.creaNuovoProgressivo(userContext, new Integer(0), "TERZO", "CD_TERZO", ((TerzoBulk) trz).getAnagrafico().getUser())).toString()
                    )
            );
        } catch (it.cnr.jada.bulk.BusyResourceException e) {
            throw new ApplicationException(e);
        } catch (Throwable e) {
            throw new PersistencyException(e);
        }
    }

    public SQLBuilder selectRif_modalita_pagamento(TerzoBulk terzo, Boolean flPerCessione) throws PersistencyException, IntrospectionException {

        PersistentHome home = getHomeCache().getHome(Rif_modalita_pagamentoBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addTableToHeader("MODALITA_PAGAMENTO");
        sql.addSQLJoin("MODALITA_PAGAMENTO.CD_MODALITA_PAG", "RIF_MODALITA_PAGAMENTO.CD_MODALITA_PAG");
        sql.addSQLClause("AND", "MODALITA_PAGAMENTO.CD_TERZO", sql.EQUALS, Optional.ofNullable(terzo.getCd_terzo()).orElse(-1));

        sql.addClause("AND", "fl_per_cessione", sql.EQUALS, flPerCessione);
        return sql;
    }

    public SQLBuilder selectRif_termini_pagamento(TerzoBulk terzo) throws PersistencyException, IntrospectionException {
        PersistentHome home = getHomeCache().getHome(it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addTableToHeader("TERMINI_PAGAMENTO");
        sql.addSQLJoin("TERMINI_PAGAMENTO.CD_TERMINI_PAG", "RIF_TERMINI_PAGAMENTO.CD_TERMINI_PAG");
        sql.addSQLClause("AND", "TERMINI_PAGAMENTO.CD_TERZO", sql.EQUALS, Optional.ofNullable(terzo.getCd_terzo()).orElse(-1));
        return sql;
    }

    public SQLBuilder selectTerzoPerCompensi(Integer codiceTerzo, CompoundFindClause clauses) throws PersistencyException {

        SQLBuilder sql = createSQLBuilder();
        sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, codiceTerzo);
        SQLBuilder sql2 = getHomeCache().getHome(V_terzo_per_compensoBulk.class).createSQLBuilder();
        sql2.addClause("AND", "cd_terzo", sql.EQUALS, codiceTerzo);
        sql2.addSQLJoin("TERZO.CD_TERZO", "V_TERZO_PER_COMPENSO.CD_TERZO");

        sql.addSQLExistsClause("AND", sql2);

        sql.addClause(clauses);

        return sql;
    }

    public java.util.List<TerzoBulk> findTerzi(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        sql.addSQLClause("AND", "CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
        return fetchAll(sql);
    }


    public String findCodiceUnivocoUfficioIPA(String cdUnitaOrganizzativa) throws IntrospectionException, PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, cdUnitaOrganizzativa);
        final Optional<TerzoBulk> optional = fetchAll(sql).stream().findAny()
                .filter(TerzoBulk.class::isInstance)
                .map(TerzoBulk.class::cast);
        return optional
                .map(TerzoBulk::getCodiceUnivocoUfficioIpa)
                .orElse(null);
    }

    @Override
    public SQLBuilder selectByClause(CompoundFindClause compoundfindclause)
            throws PersistencyException {
        PersistentHome home = getHomeCache().getHome(TerzoBulk.class, "V_TERZO_CF_PI");
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause(compoundfindclause);
        return sql;
    }
}
