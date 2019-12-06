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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.config00.bulk.Codici_siopeBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.missioni00.docs.bulk.AnticipoBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Mandato_rigaHome extends BulkHome {
    public Mandato_rigaHome(Class clazz, java.sql.Connection conn) {
        super(clazz, conn);
    }

    public Mandato_rigaHome(Class clazz, java.sql.Connection conn, PersistentCache persistentCache) {
        super(clazz, conn, persistentCache);
    }

    /**
     * <!-- @TODO: da completare -->
     * Costruisce un Mandato_rigaHome
     *
     * @param conn La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     */
    public Mandato_rigaHome(java.sql.Connection conn) {
        super(Mandato_rigaBulk.class, conn);
    }

    /**
     * <!-- @TODO: da completare -->
     * Costruisce un Mandato_rigaHome
     *
     * @param conn            La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     * @param persistentCache La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
     */
    public Mandato_rigaHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(Mandato_rigaBulk.class, conn, persistentCache);
    }

    /**
     * Metodo per inizializzare il campo ti_fattura di una riga di un mandato legato ad una fattura passiva o attiva
     *
     * @param riga      <code>Mandato_rigaBulk</code> la riga del mandato
     * @param tableName <code>String</code> il nome della tabella in cui effettuare la ricerca; può assumere i
     *                  valori "FATTURA_PASSIVA" o "FATTTURA_ATTIVA"
     */
    public Mandato_rigaBulk initializeTi_fatturaPerFattura(Mandato_rigaBulk riga, String tableName) throws SQLException {
        LoggableStatement ps = new LoggableStatement(getConnection(), "SELECT TI_FATTURA FROM " +
                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                tableName +
                " WHERE CD_CDS = ? AND " +
                "CD_UNITA_ORGANIZZATIVA = ? AND " +
                "ESERCIZIO = ? AND " +
                "PG_" + tableName + " = ? ", true, this.getClass());
        try {
            ps.setString(1, riga.getCd_cds_doc_amm());
            ps.setString(2, riga.getCd_uo_doc_amm());
            ps.setObject(3, riga.getEsercizio_doc_amm());
            ps.setObject(4, riga.getPg_doc_amm());
            ResultSet rs = ps.executeQuery();
            try {
                if (rs.next())
                    riga.setTi_fattura(rs.getString(1));
            } finally {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                }
                ;
            }
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
            ;
        }
        return riga;
    }

    /**
     * Recupera tutti i dati nella tabella MANDATO_SIOPE relativi alla riga mandato in uso.
     *
     * @param riga La riga di mandato in uso.
     * @return java.util.Collection Collezione di oggetti <code>Mandato_siopeBulk</code>
     */
    public java.util.Collection findCodiciCollegatiSIOPE(UserContext usercontext, Mandato_rigaBulk riga) throws PersistencyException {
        PersistentHome mandato_siopeHome = getHomeCache().getHome(Mandato_siopeIBulk.class);
        SQLBuilder sql = mandato_siopeHome.createSQLBuilder();
        sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, riga.getCd_cds());
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, riga.getEsercizio());
        sql.addClause("AND", "pg_mandato", SQLBuilder.EQUALS, riga.getPg_mandato());
        sql.addClause("AND", "esercizio_obbligazione", SQLBuilder.EQUALS, riga.getEsercizio_obbligazione());
        sql.addClause("AND", "esercizio_ori_obbligazione", SQLBuilder.EQUALS, riga.getEsercizio_ori_obbligazione());
        sql.addClause("AND", "pg_obbligazione", SQLBuilder.EQUALS, riga.getPg_obbligazione());
        sql.addClause("AND", "pg_obbligazione_scadenzario", SQLBuilder.EQUALS, riga.getPg_obbligazione_scadenzario());
        sql.addClause("AND", "cd_cds_doc_amm", SQLBuilder.EQUALS, riga.getCd_cds_doc_amm());
        sql.addClause("AND", "cd_uo_doc_amm", SQLBuilder.EQUALS, riga.getCd_uo_doc_amm());
        sql.addClause("AND", "esercizio_doc_amm", SQLBuilder.EQUALS, riga.getEsercizio_doc_amm());
        sql.addClause("AND", "cd_tipo_documento_amm", SQLBuilder.EQUALS, riga.getCd_tipo_documento_amm());
        sql.addClause("AND", "pg_doc_amm", SQLBuilder.EQUALS, riga.getPg_doc_amm());
        return mandato_siopeHome.fetchAll(sql);
    }

    /**
     * Recupera tutti i dati nella tabella CODICI_SIOPE associabili alla riga mandato in uso.
     *
     * @param riga La riga di mandato in uso.
     * @return java.util.Collection Collezione di oggetti <code>Codici_siopeBulk</code>
     */

    public java.util.Collection findCodiciCollegabiliSIOPE(UserContext userContext, Mandato_rigaBulk riga) throws PersistencyException {
        String uoEnte = riga.getMandato().getCd_uo_ente();
        if (uoEnte == null) {
            PersistentHome uoEnteHome = getHomeCache().getHome(Unita_organizzativa_enteBulk.class);
            List result = uoEnteHome.fetchAll(uoEnteHome.createSQLBuilder());
            uoEnte = ((Unita_organizzativaBulk) result.get(0)).getCd_unita_organizzativa();
        }

        if (uoEnte != null &&
                riga.getMandato().getCd_unita_organizzativa() != null &&
                uoEnte.equals(riga.getMandato().getCd_unita_organizzativa()) &&
                riga.getMandato().isMandatoAccreditamento()) {
            try {
                it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione(userContext, riga.getEsercizio(), null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_CODICE_SIOPE_DEFAULT, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_MANDATO_ACCREDITAMENTO);
                if (config.getVal01() == null)
                    return null;
                else {
                    PersistentHome codice_siopeHome = getHomeCache().getHome(Codici_siopeBulk.class);
                    SQLBuilder sql = codice_siopeHome.createSQLBuilder();
                    sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, riga.getEsercizio());
                    sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
                    sql.addClause("AND", "cd_siope", SQLBuilder.EQUALS, config.getVal01());
                    return codice_siopeHome.fetchAll(sql);
                }
            } catch (Exception e) {
                return null;
            }
        } else {
            initializeElemento_voce(userContext, riga);

            Elemento_voceHome elemento_voceHome = (Elemento_voceHome) getHomeCache().getHome(Elemento_voceBulk.class);
            if (riga.getElemento_voce().getFl_check_terzo_siope().booleanValue()) {
                TerzoBulk terzo = (TerzoBulk) getHomeCache().getHome(TerzoBulk.class).findByPrimaryKey(new TerzoBulk(riga.getCd_terzo()));
                AnagraficoBulk anagrafico = (AnagraficoBulk) getHomeCache().getHome(AnagraficoBulk.class).findByPrimaryKey(new AnagraficoBulk(terzo.getCd_anag()));

                return elemento_voceHome.findCodiciCollegatiSIOPE(userContext, riga.getElemento_voce(), anagrafico.getTipologia_istat());
            } else
                return elemento_voceHome.findCodiciCollegatiSIOPE(userContext, riga.getElemento_voce(), null);
        }
    }

    /**
     * Metodo per inizializzare il campo Elemento_voce di una riga di un mandato
     *
     * @param riga <code>Mandato_rigaBulk</code> la riga del mandato
     */
    public void initializeElemento_voce(UserContext userContext, Mandato_rigaBulk riga) throws PersistencyException {
        if (riga.getElemento_voce() != null) return;
        ObbligazioneBulk obbl = (ObbligazioneBulk) getHomeCache().getHome(ObbligazioneBulk.class).findByPrimaryKey(new ObbligazioneBulk(riga.getCd_cds(), riga.getEsercizio_obbligazione(), riga.getEsercizio_ori_obbligazione(), riga.getPg_obbligazione()));
        Elemento_voceBulk elemento_voce = (Elemento_voceBulk) getHomeCache().getHome(Elemento_voceBulk.class).findByPrimaryKey(new Elemento_voceBulk(obbl.getCd_elemento_voce(), obbl.getEsercizio(), obbl.getTi_appartenenza(), obbl.getTi_gestione()));
        riga.setElemento_voce(elemento_voce);
    }

    public Persistent completeBulkRowByRow(UserContext userContext, Persistent persistent) throws PersistencyException {
        if (persistent instanceof Mandato_rigaBulk)
            initializeElemento_voce(userContext, (Mandato_rigaBulk) persistent);
        return super.completeBulkRowByRow(userContext, persistent);
    }

    /**
     * Recupera tutti i dati nella tabella MANDATO_SIOPE relativi alla riga mandato in uso.
     *
     * @param riga La riga di mandato in uso.
     * @return java.util.Collection Collezione di oggetti <code>Mandato_siopeBulk</code>
     */
    public java.util.Collection findCodiciCupCollegati(UserContext usercontext, Mandato_rigaBulk riga) throws PersistencyException {
        PersistentHome mandatoCupHome = getHomeCache().getHome(MandatoCupIBulk.class);
        SQLBuilder sql = mandatoCupHome.createSQLBuilder();
        sql.addClause("AND", "cdCds", SQLBuilder.EQUALS, riga.getCd_cds());
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, riga.getEsercizio());
        sql.addClause("AND", "pgMandato", SQLBuilder.EQUALS, riga.getPg_mandato());
        sql.addClause("AND", "esercizioObbligazione", SQLBuilder.EQUALS, riga.getEsercizio_obbligazione());
        sql.addClause("AND", "esercizioOriObbligazione", SQLBuilder.EQUALS, riga.getEsercizio_ori_obbligazione());
        sql.addClause("AND", "pgObbligazione", SQLBuilder.EQUALS, riga.getPg_obbligazione());
        sql.addClause("AND", "pgObbligazioneScadenzario", SQLBuilder.EQUALS, riga.getPg_obbligazione_scadenzario());
        sql.addClause("AND", "cdCdsDocAmm", SQLBuilder.EQUALS, riga.getCd_cds_doc_amm());
        sql.addClause("AND", "cdUoDocAmm", SQLBuilder.EQUALS, riga.getCd_uo_doc_amm());
        sql.addClause("AND", "esercizioDocAmm", SQLBuilder.EQUALS, riga.getEsercizio_doc_amm());
        sql.addClause("AND", "cdTipoDocumentoAmm", SQLBuilder.EQUALS, riga.getCd_tipo_documento_amm());
        sql.addClause("AND", "pgDocAmm", SQLBuilder.EQUALS, riga.getPg_doc_amm());
        return mandatoCupHome.fetchAll(sql);
    }

    /**
     *
     * @param mandatoRiga
     * @return
     */
    private Class<?> getClassDocumentoAmministrativoSpesaBulk(Mandato_rigaBulk mandatoRiga) {
        switch (mandatoRiga.getCd_tipo_documento_amm()) {
            case Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA : {
                switch (mandatoRiga.getTi_fattura()) {
                    case Fattura_passivaBulk.TIPO_FATTURA_PASSIVA : {
                        return Fattura_passiva_IBulk.class;
                    }
                    case Fattura_passivaBulk.TIPO_NOTA_DI_CREDITO : {
                        return Nota_di_creditoBulk.class;
                    }
                    case Fattura_passivaBulk.TIPO_NOTA_DI_DEBITO : {
                        return Nota_di_debitoBulk.class;
                    }
                }
            }
            case Numerazione_doc_ammBulk.TIPO_MISSIONE : {
                return MissioneBulk.class;
            }
            case Numerazione_doc_ammBulk.TIPO_ANTICIPO : {
                return AnticipoBulk.class;
            }
            case Numerazione_doc_ammBulk.TIPO_COMPENSO : {
                return CompensoBulk.class;
            }
            default:{
                return Documento_generico_passivoBulk.class;
            }
        }
    }

    public List<Mandato_rigaIBulk> findRighe(UserContext userContext, IDocumentoAmministrativoSpesaBulk iDocumentoAmministrativoSpesaBulk) throws PersistencyException {
        PersistentHome home = getHomeCache().getHome(Mandato_rigaIBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause(FindClause.AND, "cd_cds_doc_amm", SQLBuilder.EQUALS, iDocumentoAmministrativoSpesaBulk.getCd_cds());
        sql.addClause(FindClause.AND, "cd_uo_doc_amm", SQLBuilder.EQUALS, iDocumentoAmministrativoSpesaBulk.getCd_uo());
        sql.addClause(FindClause.AND, "esercizio_doc_amm", SQLBuilder.EQUALS, iDocumentoAmministrativoSpesaBulk.getEsercizio());
        sql.addClause(FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.EQUALS, iDocumentoAmministrativoSpesaBulk.getCd_tipo_doc_amm());
        sql.addClause(FindClause.AND, "pg_doc_amm", SQLBuilder.EQUALS, iDocumentoAmministrativoSpesaBulk.getPg_doc_amm());
        return home.fetchAll(sql);
    }

    /**
     *
     * @param userContext
     * @param mandatoRiga
     * @return Il Documento amministrativo legato alla riga di mandato
     */
    public IDocumentoAmministrativoSpesaBulk getDocumentoAmministrativoSpesaBulk(UserContext userContext, Mandato_rigaBulk mandatoRiga) {
        return Optional.ofNullable(getClassDocumentoAmministrativoSpesaBulk(mandatoRiga))
                .map(aClass -> {
                    try {
                        return Optional.ofNullable(aClass.newInstance())
                                .filter(IDocumentoAmministrativoSpesaBulk.class::isInstance)
                                .map(IDocumentoAmministrativoSpesaBulk.class::cast)
                                .map(documentoAmministrativoSpesaBulk -> {
                                    documentoAmministrativoSpesaBulk.setCd_cds(mandatoRiga.getCd_cds_doc_amm());
                                    documentoAmministrativoSpesaBulk.setCd_uo(mandatoRiga.getCd_uo_doc_amm());
                                    documentoAmministrativoSpesaBulk.setEsercizio(mandatoRiga.getEsercizio_doc_amm());
                                    documentoAmministrativoSpesaBulk.setPg_doc_amm(mandatoRiga.getPg_doc_amm());
                                    documentoAmministrativoSpesaBulk.setCd_tipo_doc_amm(mandatoRiga.getCd_tipo_documento_amm());
                                    return documentoAmministrativoSpesaBulk;
                                })
                                .map(OggettoBulk.class::cast)
                                .map(oggettoBulk -> {
                                    try {
                                        Persistent persistent = getHomeCache().getHome(oggettoBulk.getClass()).findByPrimaryKey(oggettoBulk);
                                        getHomeCache().fetchAll(userContext);
                                        return persistent;
                                    } catch (PersistencyException e) {
                                        return null;
                                    }
                                })
                                .filter(IDocumentoAmministrativoSpesaBulk.class::isInstance)
                                .map(IDocumentoAmministrativoSpesaBulk.class::cast)
                                .orElse(null);
                    } catch (InstantiationException|IllegalAccessException e) {
                        return null;
                    }
                }).orElse(null);
    }

}
