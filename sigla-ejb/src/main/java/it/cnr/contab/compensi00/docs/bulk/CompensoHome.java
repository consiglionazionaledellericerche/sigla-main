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
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.*;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.StoreService;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
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
        Ass_compenso_conguaglioHome homeAssCompCong = (Ass_compenso_conguaglioHome)getHomeCache().getHome(Ass_compenso_conguaglioBulk.class);
        Ass_compenso_conguaglioBulk assCompCong = homeAssCompCong.findAssCompensoConguaglio(compenso);

        if (assCompCong!=null) {
            //Cerco il conguaglio
            ConguaglioBulk conguaglio = (ConguaglioBulk)getHomeCache().getHome(ConguaglioBulk.class).findByPrimaryKey(assCompCong.getConguaglio());

            //Sul conguaglio c'è il compenso associato... quindi lo recupero
            CompensoBulk compensoConguaglio = (CompensoBulk)this.findByPrimaryKey(conguaglio.getCompenso());

            //Verifico se il compenso principale cui è associato il conguaglio è proprio il mio
            CompensoBulk compensoPrincipale = this.findCompensoPrincipaleAssociato(userContext, compensoConguaglio);
            if (compensoPrincipale!=null && compensoPrincipale.equalsByPrimaryKey(compenso))
                return compensoConguaglio;
        }
        return null;
    }

    /**
     * Ritorna il compenso principale del compenso indicato.
     * Se trattasi di compenso semplice ritorna se stesso.
     * Se trattasi di conguaglio individua il compenso cui è stato legato. Se non legato ad altro compenso ritorna se stesso
     */
    public CompensoBulk findCompensoPrincipaleAssociato(UserContext userContext, CompensoBulk compenso) throws PersistencyException {
        if (!compenso.getFl_compenso_conguaglio())
            return compenso;

        //Trattasi di CONGUAGLIO
        //Devo recuperare il compenso legato ai mandati di versamento/accantonamento cori
        Collection<V_doc_cont_compBulk> listCompensi = ((V_doc_cont_compHome)getHomeCache().getHome(V_doc_cont_compBulk.class)).loadAllDocCont(compenso);

        Collection<V_doc_cont_compBulk> listMandati = listCompensi.stream().filter(V_doc_cont_compBulk::isTipoDocMandato).collect(Collectors.toList());
        Collection<V_doc_cont_compBulk> listReversali = listCompensi.stream().filter(V_doc_cont_compBulk::isTipoDocReversale).collect(Collectors.toList());

        if (listMandati.size()>1)
            throw new ApplicationRuntimeException("Errore nell'individuazione del compenso principale a cui è collegato il compenso di conguaglio "+compenso.getEsercizio()+"/"+compenso.getCd_cds()+
                    "/"+compenso.getCd_unita_organizzativa()+"/"+compenso.getPg_compenso()+": il compenso di conguaglio risulta collegato a più di un mandato.");

//        if (!listMandati.isEmpty() && !listReversali.isEmpty())
//            throw new ApplicationRuntimeException("Errore nell'individuazione del compenso principale a cui è collegato il compenso di conguaglio "+compensoConguaglio.getEsercizio()+"/"+compensoConguaglio.getCd_cds()+
//                    "/"+compensoConguaglio.getCd_unita_organizzativa()+"/"+compensoConguaglio.getPg_compenso()+": il compenso di conguaglio risulta collegato sia a mandati che a reversali.");

        if (!listReversali.isEmpty()) {
            MandatoBulk mandato = null;
            //Tutte le reversali devono essere vicolate ad un unico mandato.... ed il mandato ad un unico compenso... cosi lo individuo
            for (java.util.Iterator<V_doc_cont_compBulk> i = listReversali.iterator(); i.hasNext(); ) {
                V_doc_cont_compBulk value = i.next();
                List<Ass_mandato_reversaleBulk> result = ((Ass_mandato_reversaleHome) getHomeCache().getHome(Ass_mandato_reversaleBulk.class)).findMandati(userContext, (ReversaleBulk) value.getManRev(), false);
                for (java.util.Iterator<Ass_mandato_reversaleBulk> y = result.iterator(); y.hasNext(); ) {
                    Ass_mandato_reversaleBulk assMandatoReversaleBulk = y.next();
                    if (mandato==null)
                        mandato = new MandatoIBulk(assMandatoReversaleBulk.getCd_cds_mandato(), assMandatoReversaleBulk.getEsercizio_mandato(), assMandatoReversaleBulk.getPg_mandato());
                    else if (mandato.equalsByPrimaryKey(assMandatoReversaleBulk.getMandato()))
                        throw new ApplicationRuntimeException("Errore nell'individuazione del compenso principale a cui è collegato il compenso di conguaglio "+compenso.getEsercizio()+"/"+compenso.getCd_cds()+
                                "/"+compenso.getCd_unita_organizzativa()+"/"+compenso.getPg_compenso()+
                                ": le reversali cui risulta essere collegato il compenso di conguaglio risultano collegate a diversi mandati.");
                }
            }

            if (mandato!=null) {
                Collection<V_doc_cont_compBulk> result = ((V_doc_cont_compHome) getHomeCache().getHome(V_doc_cont_compBulk.class)).findByDocumento(mandato.getEsercizio(), mandato.getCd_cds(), mandato.getPg_mandato(), V_doc_cont_compBulk.TIPO_DOC_CONT_MANDATO);

                if (!result.isEmpty()) {
                    if (result.size() > 1)
                        throw new ApplicationRuntimeException("Errore nell'individuazione del compenso principale a cui è collegato il compenso di conguaglio " + compenso.getEsercizio() + "/" + compenso.getCd_cds() +
                                "/" + compenso.getCd_unita_organizzativa() + "/" + compenso.getPg_compenso() + ": il mandato cui risultano essere collegate le reversali associate al compenso di conguaglio risulta collegato a più compensi.");
                    V_doc_cont_compBulk vDocCont = result.stream().findAny().get();

                    return (CompensoBulk) this.findByPrimaryKey(new CompensoBulk(vDocCont.getCd_cds_compenso(), vDocCont.getCd_uo_compenso(),
                            vDocCont.getEsercizio_compenso(), vDocCont.getPg_compenso()));
                }
            }
        }

        if (!listMandati.isEmpty()) {
            V_doc_cont_compBulk vDocCont = listMandati.stream().findAny().get();
            Collection<V_doc_cont_compBulk> result = ((V_doc_cont_compHome)getHomeCache().getHome(V_doc_cont_compBulk.class)).findByDocumento(vDocCont.getEsercizio_doc_cont(), vDocCont.getCd_cds_doc_cont(), vDocCont.getPg_doc_cont(), vDocCont.getTipo_doc_cont());

            if (result.size()>1)
                throw new ApplicationRuntimeException("Errore nell'individuazione del compenso principale a cui è collegato il compenso di conguaglio "+compenso.getEsercizio()+"/"+compenso.getCd_cds()+
                        "/"+compenso.getCd_unita_organizzativa()+"/"+compenso.getPg_compenso()+": il mandato cui risulta essere collegato il compenso di conguaglio risulta collegato anche ad altri compensi.");
            return (CompensoBulk) this.findByPrimaryKey(new CompensoBulk(vDocCont.getCd_cds_compenso(), vDocCont.getCd_uo_compenso(),
                    vDocCont.getEsercizio_compenso(), vDocCont.getPg_compenso()));
        }

        return compenso;
    }

    public IManRevBulk findMandatoPrincipaleAssociato(UserContext userContext, CompensoBulk compenso) throws PersistencyException {
        //Devo recuperare il compenso legato ai mandati di versamento/accantonamento cori
        Collection<V_doc_cont_compBulk> listCompensi = ((V_doc_cont_compHome)getHomeCache().getHome(V_doc_cont_compBulk.class)).loadAllDocCont(compenso);

        List<V_doc_cont_compBulk> listMandati = listCompensi.stream().filter(V_doc_cont_compBulk::isTipoDocMandato).filter(V_doc_cont_compBulk::isDocumentoPrincipale).collect(Collectors.toList());
        List<V_doc_cont_compBulk> listReversali = listCompensi.stream().filter(V_doc_cont_compBulk::isTipoDocReversale).collect(Collectors.toList());

        if (!compenso.getFl_compenso_conguaglio()) {
            if (!listMandati.isEmpty()) {
                if (listMandati.size() > 1)
                    throw new ApplicationRuntimeException("Errore nell'individuazione del mandato principale a cui è collegato il compenso  " + compenso.getEsercizio() + "/" + compenso.getCd_unita_organizzativa() + "/" + compenso.getPg_compenso()
                            + ": il compenso risulta associato a troppi mandati principali.");
                V_doc_cont_compBulk docContCompBulk = listMandati.get(0);
                return docContCompBulk.getManRev();
            }
        } else {
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
                return mandato;
            }
            if (!listMandati.isEmpty()) {
                if (listMandati.size() > 1)
                    throw new ApplicationRuntimeException("Errore nell'individuazione del mandato principale a cui è collegato il compenso  " + compenso.getEsercizio() + "/" + compenso.getCd_unita_organizzativa() + "/" + compenso.getPg_compenso()
                            + ": il compenso risulta associato a troppi mandati principali.");
                V_doc_cont_compBulk docContCompBulk = listMandati.get(0);
                return docContCompBulk.getManRev();
            }
        }
        return null;
    }
}
