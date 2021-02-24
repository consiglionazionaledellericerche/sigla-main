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

package it.cnr.contab.missioni00.docs.bulk;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoHome;
import it.cnr.contab.anagraf00.core.bulk.RapportoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaHome;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.ejb.NumerazioneTempDocAmmComponentSession;
import it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.spring.service.LDAPService;
import it.cnr.contab.util.SIGLAGroups;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.StoreService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.UUID;

public class MissioneHome extends BulkHome implements
        IDocumentoAmministrativoSpesaHome {
    public MissioneHome(Class aClass, java.sql.Connection conn) {
        super(aClass, conn);
    }

    public MissioneHome(Class aClass, java.sql.Connection conn,
                        PersistentCache persistentCache) {
        super(aClass, conn, persistentCache);
    }

    public MissioneHome(java.sql.Connection conn) {
        super(MissioneBulk.class, conn);
    }

    public MissioneHome(java.sql.Connection conn,
                        PersistentCache persistentCache) {
        super(MissioneBulk.class, conn, persistentCache);
    }

    /**
     * Il metodo inserisco la missione, i dettagli, le tappe con numerazione
     * definitiva
     */
    public void confermaMissioneTemporanea(UserContext userContext,
                                           MissioneBulk missioneTemp, Long pg) throws IntrospectionException,
            PersistencyException {
        // Il progressivo della missione ricevuto come parametro è quello
        // definitivo
        LoggableStatement ps = null;
        java.io.StringWriter sql = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sql);
        String condition = " WHERE ESERCIZIO = ? AND CD_CDS = ? AND CD_UNITA_ORGANIZZATIVA = ? AND PG_MISSIONE = ?";

        /***************** CONFERMO LA TESTATA DELLA MISSIONE ***************************/
        try {
            pw.write("INSERT INTO " + EJBCommonServices.getDefaultSchema()
                    + getColumnMap().getTableName() + " (");
            pw.write(getPersistenColumnNamesReplacingWith(this, null)
                    .toString());
            pw.write(") SELECT ");
            pw.write(getPersistenColumnNamesReplacingWith(this,
                    new String[][]{{"PG_MISSIONE", "?"}}).toString());
            pw.write(" FROM " + EJBCommonServices.getDefaultSchema()
                    + getColumnMap().getTableName());
            pw.write(condition);
            pw.flush();

            ps = new LoggableStatement(getConnection(), sql.toString(), true,
                    this.getClass());
            pw.close();

            ps.setLong(1, pg.longValue());
            ps.setInt(2, missioneTemp.getEsercizio().intValue());
            ps.setString(3, missioneTemp.getCd_cds());
            ps.setString(4, missioneTemp.getCd_unita_organizzativa());
            ps.setLong(5, missioneTemp.getPg_missione().longValue());

            ps.execute();
        } catch (java.sql.SQLException e) {
            throw new PersistencyException(e);
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
            ;
        }
        /***************** CONFERMO LE TAPPE DELLA MISSIONE ***************************/
        try {
            sql = new java.io.StringWriter();
            pw = new java.io.PrintWriter(sql);
            Missione_tappaHome tappaHome = (Missione_tappaHome) getHomeCache()
                    .getHome(Missione_tappaBulk.class);
            pw.write("INSERT INTO " + EJBCommonServices.getDefaultSchema()
                    + tappaHome.getColumnMap().getTableName() + " (");
            pw.write(getPersistenColumnNamesReplacingWith(tappaHome, null)
                    .toString());
            pw.write(") SELECT ");
            pw.write(getPersistenColumnNamesReplacingWith(tappaHome,
                    new String[][]{{"PG_MISSIONE", "?"}}).toString());
            pw.write(" FROM " + EJBCommonServices.getDefaultSchema()
                    + tappaHome.getColumnMap().getTableName());
            pw.write(condition);
            pw.flush();
            ps = new LoggableStatement(getConnection(), sql.toString(), true,
                    this.getClass());
            pw.close();

            ps.setLong(1, pg.longValue());
            ps.setInt(2, missioneTemp.getEsercizio().intValue());
            ps.setString(3, missioneTemp.getCd_cds());
            ps.setString(4, missioneTemp.getCd_unita_organizzativa());
            ps.setLong(5, missioneTemp.getPg_missione().longValue());

            ps.execute();
        } catch (java.sql.SQLException e) {
            throw new PersistencyException(e);
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
            ;
        }
        /***************** CONFERMO LE SPESE DELLA MISSIONE ***************************/
        try {
            sql = new java.io.StringWriter();
            pw = new java.io.PrintWriter(sql);
            Missione_dettaglioHome dettaglioHome = (Missione_dettaglioHome) getHomeCache()
                    .getHome(Missione_dettaglioBulk.class);
            pw.write("INSERT INTO " + EJBCommonServices.getDefaultSchema()
                    + dettaglioHome.getColumnMap().getTableName() + " (");
            pw.write(getPersistenColumnNamesReplacingWith(dettaglioHome, null)
                    .toString());
            pw.write(") SELECT ");
            pw.write(getPersistenColumnNamesReplacingWith(dettaglioHome,
                    new String[][]{{"PG_MISSIONE", "?"}}).toString());
            pw.write(" FROM " + EJBCommonServices.getDefaultSchema()
                    + dettaglioHome.getColumnMap().getTableName());
            pw.write(condition);
            pw.flush();
            ps = new LoggableStatement(getConnection(), sql.toString(), true,
                    this.getClass());
            pw.close();
            ps.setLong(1, pg.longValue());
            ps.setInt(2, missioneTemp.getEsercizio().intValue());
            ps.setString(3, missioneTemp.getCd_cds());
            ps.setString(4, missioneTemp.getCd_unita_organizzativa());
            ps.setLong(5, missioneTemp.getPg_missione().longValue());

            ps.execute();
        } catch (java.sql.SQLException e) {
            throw new PersistencyException(e);
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
            ;
        }

        // Cancello la missione, spese e tappe con numerazione temporanea
        delete(missioneTemp, userContext);

        missioneTemp.setPg_missione(pg);
    }

    private StringBuffer getPersistenColumnNamesReplacingWith(BulkHome home,
                                                              String[][] fields) throws PersistencyException {
        java.io.StringWriter columns = new java.io.StringWriter();

        if (home == null)
            throw new PersistencyException(
                    "Impossibile ottenere la home per l'aggiornamento dei progressivi temporanei della missione!");

        java.io.PrintWriter pw = new java.io.PrintWriter(columns);
        String[] persistenColumns = home.getColumnMap().getColumnNames();
        for (int i = 0; i < persistenColumns.length; i++) {
            String columnName = persistenColumns[i];
            if (fields != null) {
                for (int j = 0; j < fields.length; j++) {
                    String[] field = fields[j];
                    if (columnName.equalsIgnoreCase(field[0])) {
                        columnName = field[1];
                        break;
                    }
                }
            }
            pw.print(columnName);
            pw.print((i == persistenColumns.length - 1) ? "" : ", ");
        }
        pw.close();
        return columns.getBuffer();
    }

    /**
     * Il metodo viene chiamato dal creaConBulk, cioe' in fase di salvataggio
     * temporaneo della missione
     */

    public void initializePrimaryKeyForInsert(
            it.cnr.jada.UserContext userContext, OggettoBulk bulk)
            throws it.cnr.jada.comp.ComponentException {
        MissioneBulk missione = (MissioneBulk) bulk;

        try {
            // Assegno un progressivo temporaneo alla missione

            NumerazioneTempDocAmmComponentSession session = (NumerazioneTempDocAmmComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
                    .createEJB(
                            "CNRDOCAMM00_EJB_NumerazioneTempDocAmmComponentSession",
                            NumerazioneTempDocAmmComponentSession.class);
            Numerazione_doc_ammBulk numerazione = new Numerazione_doc_ammBulk(
                    missione);
            missione.setPg_missione(session.getNextTempPG(userContext,
                    numerazione));
        } catch (Throwable e) {
            throw new it.cnr.jada.comp.ComponentException(e);
        }
    }

    /**
     * Il metodo carica la missione legata al compenso ricevuto come parametro
     */
    public MissioneBulk loadMissione(it.cnr.jada.UserContext userContext,
                                     it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso)
            throws PersistencyException {

        SQLBuilder sql = createSQLBuilder();
        sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, compenso
                .getCd_cds_missione());
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, compenso
                .getEsercizio_missione());
        sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, compenso
                .getCd_uo_missione());
        sql.addSQLClause("AND", "PG_MISSIONE", sql.EQUALS, compenso
                .getPg_missione());

        MissioneBulk missione = null;
        Broker broker = createBroker(sql);
        if (broker.next())
            missione = (MissioneBulk) fetch(broker);
        broker.close();
        getHomeCache().fetchAll(userContext);

        return missione;
    }

    /**
     * Il metodo carica la missione legata all'anticipo ricevuto come parametro
     */
    public MissioneBulk loadMissione(it.cnr.jada.UserContext userContext,
                                     AnticipoBulk anticipo) throws PersistencyException {
        SQLBuilder sql = createSQLBuilder();

        sql.addSQLClause("AND", "CD_CDS_ANTICIPO", sql.EQUALS, anticipo
                .getCd_cds());
        sql.addSQLClause("AND", "ESERCIZIO_ANTICIPO", sql.EQUALS, anticipo
                .getEsercizio());
        sql.addSQLClause("AND", "CD_UO_ANTICIPO", sql.EQUALS, anticipo
                .getCd_unita_organizzativa());
        sql.addSQLClause("AND", "PG_ANTICIPO", sql.EQUALS, anticipo
                .getPg_anticipo());
        sql.addSQLClause("AND", "STATO_COFI", sql.NOT_EQUALS, MissioneBulk.STATO_ANNULLATO);


        MissioneBulk missione = null;
        Broker broker = createBroker(sql);
        if (broker.next())
            missione = (MissioneBulk) fetch(broker);
        broker.close();
        getHomeCache().fetchAll(userContext);

        return missione;
    }

	/**
	 * Il metodo carica la missione legata all'ID di gemis
	 */
	public MissioneBulk loadMissione(it.cnr.jada.UserContext userContext,
			Long idRimborsoMissioneGemis)
			throws PersistencyException {

		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND", "ID_RIMBORSO_MISSIONE", sql.EQUALS, idRimborsoMissioneGemis);

		MissioneBulk missione = null;
		Broker broker = createBroker(sql);
		if (broker.next())
			missione = (MissioneBulk) fetch(broker);
		broker.close();
		getHomeCache().fetchAll(userContext);

		return missione;
	}


    /**
     * Metodo richiesto dall'interfaccia IDocumentoAmministrativoSpesaHome Il
     * metodo aggiorna la missione dopo che e' stata collegata ad una spesa del
     * Fondo Economale
     */

    public void updateFondoEconomale(Fondo_spesaBulk spesa)
            throws it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.bulk.OutdatedResourceException,
            it.cnr.jada.bulk.BusyResourceException {
        if (spesa == null)
            return;

        MissioneBulk missione = (MissioneBulk) spesa.getDocumento();

        lock(missione);

        StringBuffer stm = new StringBuffer("UPDATE ");
        stm.append(it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema());
        stm.append(getColumnMap().getTableName());
        stm
                .append(" SET STATO_PAGAMENTO_FONDO_ECO = ?, DT_PAGAMENTO_FONDO_ECO = ?, PG_VER_REC = PG_VER_REC+1, DUVA = ?, UTUV = ?");
        stm.append(" WHERE (");
        stm
                .append("CD_CDS = ? AND CD_UNITA_ORGANIZZATIVA = ? AND ESERCIZIO = ? AND PG_MISSIONE = ? )");

        try {
            LoggableStatement ps = new LoggableStatement(getConnection(), stm
                    .toString(), true, this.getClass());
            try {
                ps
                        .setString(
                                1,
                                (spesa.isToBeCreated() || spesa.isToBeUpdated()) ? missione.REGISTRATO_IN_FONDO_ECO
                                        : missione.FONDO_ECO);
                if (spesa.isToBeCreated() || spesa.isToBeUpdated())
                    ps.setTimestamp(2, spesa.getDt_spesa());
                else
                    ps.setNull(2, java.sql.Types.TIMESTAMP);

                ps.setTimestamp(3, getServerTimestamp());
                ps.setString(4, spesa.getUser());

                ps.setString(5, missione.getCd_cds());
                ps.setString(6, missione.getCd_unita_organizzativa());
                ps.setInt(7, missione.getEsercizio().intValue());
                ps.setLong(8, missione.getPg_missione().longValue());

                ps.executeUpdate();
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
                ;
            }
        } catch (java.sql.SQLException e) {
            throw it.cnr.jada.persistency.sql.SQLExceptionHandler.getInstance()
                    .handleSQLException(e, spesa);
        }
    }

    @SuppressWarnings("unchecked")
    public void archiviaStampa(UserContext userContext, MissioneBulk missione)
            throws IntrospectionException, PersistencyException {
        Boolean isDipendente = Boolean.FALSE;
        Integer matricola = null;
        String stato_pagamento_fondo_eco = missione.getStato_pagamento_fondo_eco();
        Timestamp dt_pagamento_fondo_eco = missione.getDt_pagamento_fondo_eco();
        if (missione.getTi_provvisorio_definitivo().equalsIgnoreCase("D")) {
            Collection<RapportoBulk> rapporti = ((AnagraficoHome) getHomeCache()
                    .getHome(AnagraficoBulk.class)).findRapporti(missione
                    .getTerzo().getAnagrafico());
            for (RapportoBulk rapporto : rapporti) {
                if (rapporto.getMatricola_dipendente() != null) {
                    isDipendente = Boolean.TRUE;
                    matricola = rapporto.getMatricola_dipendente();
                    break;
                }
            }
        }
        if (missione.getFl_associato_compenso()) {
            stato_pagamento_fondo_eco = missione.getCompenso().getStato_pagamento_fondo_eco();
            dt_pagamento_fondo_eco = missione.getCompenso().getDt_pagamento_fondo_eco();
        }
        if (isDipendente && missione.getTipo_rapporto().isDipendente()) {
            if ((missione.getAnticipo() != null
                    && missione.getAnticipo().getIm_anticipo().compareTo(
                    missione.getIm_netto_pecepiente()) > 0) ||
                    (stato_pagamento_fondo_eco.equals(MissioneBulk.REGISTRATO_IN_FONDO_ECO) &&
                            dt_pagamento_fondo_eco != null)) {
                Print_spoolerBulk print = new Print_spoolerBulk();
                print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
                print.setFlEmail(false);
                print.setReport("/docamm/docamm/vpg_missione.jasper");
                print.setNomeFile("Missione n. "
                        + missione.getPg_missione()
                        + " della UO "
                        + missione.getCd_unita_organizzativa()
                        + " del "
                        + new SimpleDateFormat("dd-MM-yyyy").format(missione
                        .getDt_inizio_missione()) + ".pdf");
                print.setUtcr(userContext.getUser());
                print.addParam("aCd_cds", missione.getCd_cds(), String.class);
                print.addParam("aCd_uo", missione.getCd_unita_organizzativa(),
                        String.class);
                print.addParam("aEs", missione.getEsercizio(), Integer.class);
                print.addParam("aPg_da", missione.getPg_missione(), Long.class);
                print.addParam("aPg_a", missione.getPg_missione(), Long.class);
                print.addParam("aCd_terzo", String.valueOf(missione
                        .getCd_terzo()), String.class);

                try {
                    missione
                            .setUnitaOrganizzativa((Unita_organizzativaBulk) getHomeCache()
                                    .getHome(Unita_organizzativaBulk.class)
                                    .findByPrimaryKey(
                                            new Unita_organizzativaBulk(
                                                    missione
                                                            .getCd_unita_organizzativa())));
                    String cmisPath;
                    if (missione.getFl_associato_compenso())
                        cmisPath = SpringUtil.getBean(StorePath.class).getPathConcorrentiFormazioneReddito();
                    else
                        cmisPath = SpringUtil.getBean(StorePath.class).getPathNonConcorrentiFormazioneReddito();

                    LDAPService ldapService = SpringUtil.getBean("ldapService",
                            LDAPService.class);
                    String uid = ldapService.getLdapUserFromMatricola(
                            userContext, matricola);

                    Report report = SpringUtil.getBean("printService",
                            PrintService.class).executeReport(userContext,
                            print);
                    SpringUtil.getBean("storeService", StoreService.class).storeSimpleDocument(
                            missione,
                            report.getInputStream(),
                            report.getContentType(),
                            report.getName(),
                            cmisPath,
                            StorageDriver.Permission.construct(uid, StorageDriver.ACLType.Consumer),
                            StorageDriver.Permission.construct(SIGLAGroups.GROUP_EMPPAY_GROUP.name(), StorageDriver.ACLType.Coordinator)
                    );
                } catch (Exception e) {
                    throw new PersistencyException(e);
                }
            }
        }
    }
}
