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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoHome;
import it.cnr.contab.anagraf00.core.bulk.RapportoBulk;
import it.cnr.contab.coepcoan00.core.bulk.Movimento_cogeBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.spring.service.LDAPService;
import it.cnr.contab.util.SIGLAGroups;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.*;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.StoreService;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CompensoHome extends BulkHome implements
        it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaHome {

    public CompensoHome(java.sql.Connection conn) {
        super(CompensoBulk.class, conn);
    }

    public CompensoHome(java.sql.Connection conn,
                        PersistentCache persistentCache) {
        super(CompensoBulk.class, conn, persistentCache);
    }

    public CompensoBulk loadCompenso(it.cnr.jada.UserContext userContext,
                                     MissioneBulk missione) throws PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        sql.addSQLClause(FindClause.AND, "CD_CDS_MISSIONE", SQLBuilder.EQUALS, missione
                .getCd_cds());
        sql.addSQLClause(FindClause.AND, "ESERCIZIO_MISSIONE", SQLBuilder.EQUALS, missione
                .getEsercizio());
        sql.addSQLClause(FindClause.AND, "CD_UO_MISSIONE", SQLBuilder.EQUALS, missione
                .getCd_unita_organizzativa());
        sql.addSQLClause(FindClause.AND, "PG_MISSIONE", SQLBuilder.EQUALS, missione
                .getPg_missione());
        sql.addSQLClause(FindClause.AND, "STATO_COFI", SQLBuilder.NOT_EQUALS,
                CompensoBulk.STATO_ANNULLATO);
        sql.addSQLClause(FindClause.AND, "DT_CANCELLAZIONE", SQLBuilder.ISNULL, null);

        CompensoBulk compenso = null;
        Broker broker = createBroker(sql);
        if (broker.next())
            compenso = (CompensoBulk) fetch(broker);
        broker.close();
        getHomeCache().fetchAll(userContext);

        return compenso;
    }

    public java.util.Collection loadContributiRitentute(CompensoBulk compenso)
            throws PersistencyException {

        Contributo_ritenutaHome contributoHome = (Contributo_ritenutaHome) getHomeCache()
                .getHome(Contributo_ritenutaBulk.class);
        return contributoHome.loadContributiRitenute(compenso);

    }

    public SQLBuilder selectLineeAttivita(CompensoBulk compenso,
                                          String capitolo, CompoundFindClause clauses)
            throws PersistencyException {

        PersistentHome laHome = getHomeCache().getHome(WorkpackageBulk.class,
                "V_LINEA_ATTIVITA_VALIDA");
        SQLBuilder sql = laHome.createSQLBuilder();

        sql.addTableToHeader("CDR");
        sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA",
                "CDR.CD_CENTRO_RESPONSABILITA");
        sql.addClause(FindClause.AND,"ti_gestione",SQLBuilder.EQUALS,
                        it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.GESTIONE_ENTRATE);

        sql.addSQLClause("AND", "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",
                SQLBuilder.EQUALS, compenso.getEsercizio());
        sql.addSQLClause("AND", "CDR.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS,
                compenso.getCd_uo_origine());

        String subQuery = "SELECT CD_NATURA FROM "
                + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                + "ASS_EV_EV C " + "WHERE C.esercizio = ? "
                + "AND C.cd_elemento_voce = ? " + "AND C.ti_appartenenza = ? "
                + "AND C.ti_gestione = ?";
        sql.addSQLClause("AND", "V_LINEA_ATTIVITA_VALIDA.CD_NATURA IN ( "
                + subQuery + " )");
        sql.addParameter(compenso.getEsercizio(), Types.NUMERIC, 0);
        sql.addParameter(capitolo, Types.VARCHAR, 0);
        sql
                .addParameter(
                        it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.APPARTENENZA_CNR,
                        Types.VARCHAR, 0);
        sql
                .addParameter(
                        it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.GESTIONE_ENTRATE,
                        Types.VARCHAR, 0);

        sql.addClause(clauses);

        return sql;
    }

    /**
     * Insert the method's description here. Creation date: (5/10/2002 3:27:22
     * PM)
     */
    public void updateFondoEconomale(
            it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk spesa)
            throws it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.bulk.OutdatedResourceException,
            it.cnr.jada.bulk.BusyResourceException {

        if (spesa == null)
            return;

        CompensoBulk compenso = (CompensoBulk) spesa.getDocumento();

        lock(compenso);

        StringBuffer stm = new StringBuffer("UPDATE ");
        stm.append(it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema());
        stm.append(getColumnMap().getTableName());
        stm
                .append(" SET STATO_PAGAMENTO_FONDO_ECO = ?, DT_PAGAMENTO_FONDO_ECO = ?, PG_VER_REC = PG_VER_REC+1, DUVA = ?, UTUV = ?");
        stm.append(" WHERE (");
        stm
                .append("CD_CDS = ? AND CD_UNITA_ORGANIZZATIVA = ? AND ESERCIZIO = ? AND PG_COMPENSO = ? )");

        try {
            LoggableStatement ps = new LoggableStatement(getConnection(), stm
                    .toString(), true, this.getClass());
            try {
                ps
                        .setString(
                                1,
                                (spesa.isToBeCreated() || spesa.isToBeUpdated()) ? CompensoBulk.REGISTRATO_FONDO_ECO
                                        : CompensoBulk.ASSEGNATO_FONDO_ECO);
                if (spesa.isToBeCreated() || spesa.isToBeUpdated())
                    ps.setTimestamp(2, spesa.getDt_spesa());
                else
                    ps.setNull(2, java.sql.Types.TIMESTAMP);

                ps.setTimestamp(3, getServerTimestamp());
                ps.setString(4, spesa.getUser());

                ps.setString(5, compenso.getCd_cds());
                ps.setString(6, compenso.getCd_unita_organizzativa());
                ps.setInt(7, compenso.getEsercizio().intValue());
                ps.setLong(8, compenso.getPg_compenso().longValue());

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

    public Boolean findCompensoConDataSuperiore(Integer esercizio,
                                                Integer cd_anag, java.sql.Timestamp data_pagamento_esterno)
            throws PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        sql.addTableToHeader("TERZO");
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, esercizio);
        sql.addSQLClause("AND", "DT_REGISTRAZIONE", sql.GREATER_EQUALS,
                data_pagamento_esterno);
        sql.addSQLClause("AND", "DT_CANCELLAZIONE", sql.ISNULL, null);
        sql.addSQLJoin("TERZO.CD_TERZO", "COMPENSO.CD_TERZO");
        sql.addSQLClause("AND", "TERZO.CD_ANAG", sql.EQUALS, cd_anag);

        Broker broker = createBroker(sql);
        if (broker.next())
            return new Boolean(true);
        broker.close();

        return new Boolean(false);
    }

    public Boolean findCompensoConDataInferiore(Integer esercizio,
                                                Integer cd_anag, java.sql.Timestamp data_pagamento_esterno)
            throws PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        sql.addTableToHeader("TERZO");
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, esercizio);
        sql.addSQLClause("AND", "DT_REGISTRAZIONE", sql.LESS_EQUALS,
                data_pagamento_esterno);
        sql.addSQLClause("AND", "DT_CANCELLAZIONE", sql.ISNULL, null);
        sql.addSQLJoin("TERZO.CD_TERZO", "COMPENSO.CD_TERZO");
        sql.addSQLClause("AND", "TERZO.CD_ANAG", sql.EQUALS, cd_anag);

        Broker broker = createBroker(sql);
        if (broker.next())
            return new Boolean(true);
        broker.close();

        return new Boolean(false);
    }

    public java.util.List findCompensoIncaricoList(
            it.cnr.jada.UserContext userContext,
            Incarichi_repertorio_annoBulk incarico_anno)
            throws IntrospectionException, PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        sql.addSQLClause("AND", "ESERCIZIO_REP", sql.EQUALS, incarico_anno
                .getEsercizio());
        sql.addSQLClause("AND", "PG_REPERTORIO", sql.EQUALS, incarico_anno
                .getPg_repertorio());
        sql.addSQLClause("AND", "ESERCIZIO_LIMITE_REP", sql.EQUALS,
                incarico_anno.getEsercizio_limite());
        sql.addSQLClause("AND", "PG_COMPENSO", sql.GREATER, 0);
        sql.addSQLClause("AND", "STATO_COFI", sql.NOT_EQUALS,
                CompensoBulk.STATO_ANNULLATO);
        List l = fetchAll(sql);
        getHomeCache().fetchAll(userContext);
        return l;
    }

    public java.util.List findCompensoIncaricoList(
            it.cnr.jada.UserContext userContext,
            Incarichi_repertorioBulk incarico) throws IntrospectionException,
            PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        sql.addSQLClause("AND", "ESERCIZIO_REP", SQLBuilder.EQUALS, incarico
                .getEsercizio());
        sql.addSQLClause("AND", "PG_REPERTORIO", SQLBuilder.EQUALS, incarico
                .getPg_repertorio());
        sql.addSQLClause("AND", "PG_COMPENSO", SQLBuilder.GREATER, 0);
        sql.addSQLClause("AND", "STATO_COFI", SQLBuilder.NOT_EQUALS,
                CompensoBulk.STATO_ANNULLATO);
        List l = fetchAll(sql);
        getHomeCache().fetchAll(userContext);
        return l;
    }

    @SuppressWarnings("unchecked")
    public void archiviaStampa(UserContext userContext, CompensoBulk compenso)
            throws IntrospectionException, PersistencyException {
        Boolean isDipendente = Boolean.FALSE;
        Integer matricola = null;
        if (compenso.getTipoRapporto().isDipendente()) {
            Collection<RapportoBulk> rapporti = ((AnagraficoHome) getHomeCache()
                    .getHome(AnagraficoBulk.class)).findRapporti(compenso
                    .getTerzo().getAnagrafico());
            for (RapportoBulk rapporto : rapporti) {
                if (rapporto.getMatricola_dipendente() != null) {
                    isDipendente = Boolean.TRUE;
                    matricola = rapporto.getMatricola_dipendente();
                    break;
                }
            }
        }
        if (isDipendente && compenso.getTipoRapporto().isDipendente()) {
            if (compenso.getMissione() == null && (
                    (compenso.getStato_pagamento_fondo_eco().equals(MissioneBulk.REGISTRATO_IN_FONDO_ECO) &&
                            compenso.getDt_pagamento_fondo_eco() != null))) {
                Print_spoolerBulk print = new Print_spoolerBulk();
                print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
                print.setFlEmail(false);
                print.setReport("/docamm/docamm/compenso.jasper");
                print.setNomeFile("Compenso n. "
                        + compenso.getPg_compenso()
                        + " della UO "
                        + compenso.getCd_unita_organizzativa()
                        + " del "
                        + new SimpleDateFormat("dd-MM-yyyy").format(compenso
                        .getDt_registrazione()) + ".pdf");
                print.setUtcr(userContext.getUser());
                print.addParam("Esercizio", compenso.getEsercizio(), Integer.class);
                print.addParam("CDS", compenso.getCd_cds(), String.class);
                print.addParam("UO", compenso.getCd_unita_organizzativa(),
                        String.class);
                print.addParam("Pg_compenso", compenso.getPg_compenso().intValue(), Integer.class);

                try {
                    compenso
                            .setUnitaOrganizzativa((Unita_organizzativaBulk) getHomeCache()
                                    .getHome(Unita_organizzativaBulk.class)
                                    .findByPrimaryKey(
                                            new Unita_organizzativaBulk(
                                                    compenso
                                                            .getCd_unita_organizzativa())));
                    String path = SpringUtil.getBean(StorePath.class).getPathConcorrentiFormazioneReddito();
                    LDAPService ldapService = SpringUtil.getBean("ldapService",
                            LDAPService.class);
                    String uid = ldapService.getLdapUserFromMatricola(
                            userContext, matricola);

                    Report report = SpringUtil.getBean("printService",
                            PrintService.class).executeReport(userContext,
                            print);
                    SpringUtil.getBean("storeService", StoreService.class).storeSimpleDocument(
                            compenso,
                            report.getInputStream(),
                            report.getContentType(),
                            report.getName(),
                            path,
                            StorageDriver.Permission.construct(uid, StorageDriver.ACLType.Consumer),
                            StorageDriver.Permission.construct(SIGLAGroups.GROUP_EMPPAY_GROUP.name(), StorageDriver.ACLType.Coordinator)
                    );
                } catch (Exception e) {
                    throw new PersistencyException(e);
                }
            }
        }
    }

    /**
     * Ritorna il compenso associato al conguaglio (presente sulla tabella CONGUAGLIO) emesso contemporaneamente al compenso principale
     */
    public CompensoBulk findCompensoConguaglioAssociato(UserContext userContext, CompensoBulk compenso) throws PersistencyException {
        //Trattasi di COMPENSO NORMALE
        //Devo recuperare il compenso legato ai mandati di versamento/accantonamento cori
        Collection<V_doc_cont_compBulk> listDocContComp = ((V_doc_cont_compHome) getHomeCache().getHome(V_doc_cont_compBulk.class)).loadAllDocCont(compenso);

        Collection<V_doc_cont_compBulk> listMandati = listDocContComp.stream().filter(V_doc_cont_compBulk::isTipoDocMandato).collect(Collectors.toList());
        Collection<V_doc_cont_compBulk> listReversali = listDocContComp.stream().filter(V_doc_cont_compBulk::isTipoDocReversale).collect(Collectors.toList());

        Collection<V_doc_cont_compBulk> listDocPrincipale = listDocContComp.stream().filter(V_doc_cont_compBulk::isDocumentoPrincipale).collect(Collectors.toList());
        if (listDocPrincipale.size()>1)
            throw new ApplicationRuntimeException("Errore nell'individuazione del compenso di conguaglio collegato al compenso principale " + compenso.getEsercizio() + "/" + compenso.getCd_cds() +
                    "/" + compenso.getCd_unita_organizzativa() + "/" + compenso.getPg_compenso() + ": il compenso principale risulta collegato a più di un documento definito come principale.");

        Optional<V_doc_cont_compBulk> docPrincipale = listDocPrincipale.stream().findFirst();

        if (!listMandati.isEmpty() && docPrincipale.map(V_doc_cont_compBulk::isTipoDocMandato).orElse(Boolean.TRUE)) {
            //Sul compenso deve esserci un solo mandato..... se ce ne sono di più deve esistere un solo mandato principale
            if (listMandati.size() > 1) {
                if (listMandati.stream().filter(V_doc_cont_compBulk::isDocumentoPrincipale).collect(Collectors.toList()).size()>1)
                    throw new ApplicationRuntimeException("Errore nell'individuazione del compenso di conguaglio collegato al compenso principale " + compenso.getEsercizio() + "/" + compenso.getCd_cds() +
                            "/" + compenso.getCd_unita_organizzativa() + "/" + compenso.getPg_compenso() + ": il compenso principale risulta collegato a più di un mandato principale.");
            }

            List<CompensoBulk> compensiSelected = new ArrayList<>();
            boolean existReversaleCompensoPrincipale = Boolean.FALSE;

            for (java.util.Iterator<V_doc_cont_compBulk> x = listMandati.iterator(); x.hasNext(); ) {
                V_doc_cont_compBulk vDocContComp = x.next();
                IManRevBulk mandatoPrincipale = vDocContComp.getManRev();
                List<Ass_mandato_reversaleBulk> result = ((Ass_mandato_reversaleHome) getHomeCache().getHome(Ass_mandato_reversaleBulk.class)).findReversali(userContext, (MandatoBulk) mandatoPrincipale, false);

                for (java.util.Iterator<Ass_mandato_reversaleBulk> y = result.iterator(); y.hasNext(); ) {
                    Ass_mandato_reversaleBulk assMandatoReversaleBulk = y.next();

                    if (!listReversali.stream()
                            .filter(el->el.getManRev().getEsercizio().equals(assMandatoReversaleBulk.getEsercizio_reversale()))
                            .filter(el->el.getManRev().getCd_cds().equals(assMandatoReversaleBulk.getCd_cds_reversale()))
                            .filter(el->el.getManRev().getPg_doc().equals(assMandatoReversaleBulk.getPg_reversale()))
                            .findAny().isPresent()) {
                        Collection<V_doc_cont_compBulk> result2 = ((V_doc_cont_compHome) getHomeCache().getHome(V_doc_cont_compBulk.class)).findByDocumento(assMandatoReversaleBulk.getEsercizio_reversale(), assMandatoReversaleBulk.getCd_cds_reversale(), assMandatoReversaleBulk.getPg_reversale(), V_doc_cont_compBulk.TIPO_DOC_CONT_REVERSALE);
                        if (result2.isEmpty())
                            throw new ApplicationRuntimeException("Errore nell'individuazione del compenso di conguaglio collegato al compenso principale " + compenso.getEsercizio() + "/" + compenso.getCd_cds() +
                                    "/" + compenso.getCd_unita_organizzativa() + "/" + compenso.getPg_compenso() + ": la reversale "+assMandatoReversaleBulk.getEsercizio_reversale()+"/"+assMandatoReversaleBulk.getCd_cds_reversale()+"/"+assMandatoReversaleBulk.getPg_reversale()+
                                    ", collegata al mandato del compenso principale, non risulta collegata a nessun compenso.");
                        if (result2.size()>1)
                            throw new ApplicationRuntimeException("Errore nell'individuazione del compenso di conguaglio collegato al compenso principale " + compenso.getEsercizio() + "/" + compenso.getCd_cds() +
                                    "/" + compenso.getCd_unita_organizzativa() + "/" + compenso.getPg_compenso() + ": la reversale "+assMandatoReversaleBulk.getEsercizio_reversale()+"/"+assMandatoReversaleBulk.getCd_cds_reversale()+"/"+assMandatoReversaleBulk.getPg_reversale()+
                                    ", collegata al mandato del compenso principale, risulta collegata a troppi compensi.");

                        V_doc_cont_compBulk docContCompBulk = result2.stream().findAny().get();
                        final CompensoBulk compensoConguaglio = new CompensoBulk(docContCompBulk.getCd_cds_compenso(), docContCompBulk.getCd_uo_compenso(), docContCompBulk.getEsercizio_compenso(), docContCompBulk.getPg_compenso());
                        if (compensiSelected.stream().noneMatch(el->el.equalsByPrimaryKey(compensoConguaglio)))
                            compensiSelected.add(compensoConguaglio);
                    } else
                        existReversaleCompensoPrincipale = Boolean.TRUE;
                }
            }

            CompensoBulk compensoConguaglio = null;

            //Dalla lista deve essere presente un solo compenso... se non ne esistono ma ci sono reversali collegate appartenenti al compenso principale, allora restituisco quello
            if (compensiSelected.size()>1)
                throw new ApplicationRuntimeException("Errore nell'individuazione del compenso di conguaglio collegato al compenso principale "+compenso.getEsercizio()+"/"+compenso.getCd_cds()+
                        "/"+compenso.getCd_unita_organizzativa()+"/"+compenso.getPg_compenso()+
                        ": le reversali, collegate al mandato del compenso principale, risultano collegate a compensi diversi.");

            if (compensiSelected.size()==1)
                compensoConguaglio = compensiSelected.get(0);
            else if (existReversaleCompensoPrincipale)
                compensoConguaglio = compenso;

            if (compensoConguaglio != null)
                return (CompensoBulk) this.findByPrimaryKey(compensoConguaglio);
        }
        return null;
    }

    /**
     * Ritorna la lista di tutti i mandati associati al compenso.
     * Sia quelli direttamente associati al compenso sia quelli associati tramite reversale generata dal compenso stesso
     */
    public List<MandatoBulk> findMandatiAssociati(UserContext userContext, CompensoBulk compenso) throws PersistencyException {
        List<MandatoBulk> resultMandati = new ArrayList<>();

        //Devo recuperare il compenso legato ai mandati di versamento/accantonamento cori
        Collection<V_doc_cont_compBulk> listCompensi = ((V_doc_cont_compHome)getHomeCache().getHome(V_doc_cont_compBulk.class)).loadAllDocCont(compenso);

        List<V_doc_cont_compBulk> listMandati = listCompensi.stream().filter(V_doc_cont_compBulk::isTipoDocMandato).collect(Collectors.toList());
        List<V_doc_cont_compBulk> listReversali = listCompensi.stream().filter(V_doc_cont_compBulk::isTipoDocReversale).collect(Collectors.toList());

        if (compenso.getFl_compenso_conguaglio() || compenso.getFl_senza_calcoli()) {
            if (!listReversali.isEmpty()) {
                MandatoBulk mandato = null;
                //Tutte le reversali devono essere vicolate ad un unico mandato.... ed il mandato ad un unico compenso... cosi lo individuo
                for (java.util.Iterator<V_doc_cont_compBulk> i = listReversali.iterator(); i.hasNext(); ) {
                    V_doc_cont_compBulk value = i.next();
                    List<Ass_mandato_reversaleBulk> result = ((Ass_mandato_reversaleHome) getHomeCache().getHome(Ass_mandato_reversaleBulk.class)).findMandati(userContext, (ReversaleBulk) value.getManRev(), false);
                    for (java.util.Iterator<Ass_mandato_reversaleBulk> y = result.iterator(); y.hasNext(); ) {
                        Ass_mandato_reversaleBulk assMandatoReversaleBulk = y.next();
                        if (mandato == null)
                            mandato = new MandatoIBulk(assMandatoReversaleBulk.getCd_cds_mandato(), assMandatoReversaleBulk.getEsercizio_mandato(), assMandatoReversaleBulk.getPg_mandato());
                        else if (mandato.equalsByPrimaryKey(assMandatoReversaleBulk.getMandato()))
                            throw new ApplicationRuntimeException("Errore nell'individuazione del compenso principale a cui è collegato il compenso di conguaglio " + compenso.getEsercizio() + "/" + compenso.getCd_cds() +
                                    "/" + compenso.getCd_unita_organizzativa() + "/" + compenso.getPg_compenso() +
                                    ": le reversali cui risulta essere collegato il compenso di conguaglio risultano collegate a diversi mandati.");
                    }
                }
                final MandatoBulk mandatoBulk = mandato;
                if (mandato!=null && resultMandati.stream().noneMatch(el->el.equalsByPrimaryKey(mandatoBulk)))
                    resultMandati.add((MandatoBulk)getHomeCache().getHome(MandatoIBulk.class).findByPrimaryKey(mandatoBulk));
            }
        }

        if (!listMandati.isEmpty())
            listMandati.stream().filter(el->resultMandati.stream().noneMatch(el2->el2.equalsByPrimaryKey(el.getManRev()))).forEach(el->resultMandati.add((MandatoBulk)el.getManRev()));

        return resultMandati;
    }
}
