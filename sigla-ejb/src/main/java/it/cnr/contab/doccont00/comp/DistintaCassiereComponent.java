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

package it.cnr.contab.doccont00.comp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.anagraf00.ejb.AnagraficoComponentSession;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneHome;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoHome;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoHome;
import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.ejb.EsercizioComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession;
import it.cnr.contab.doccont00.ejb.SospesoRiscontroComponentSession;
import it.cnr.contab.doccont00.intcass.bulk.*;
import it.cnr.contab.doccont00.intcass.giornaliera.MovimentoContoEvidenzaBulk;
import it.cnr.contab.doccont00.intcass.giornaliera.MovimentoContoEvidenzaHome;
import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.contab.doccont00.tabrif.bulk.CupBulk;
import it.cnr.contab.doccont00.tabrif.bulk.CupKey;
import it.cnr.contab.logs.bulk.Batch_log_rigaBulk;
import it.cnr.contab.logs.bulk.Batch_log_tstaBulk;
import it.cnr.contab.logs.ejb.BatchControlComponentSession;
import it.cnr.contab.messaggio00.bulk.MessaggioBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaHome;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.ApplicationMessageFormatException;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.contab.util.enumeration.EsitoOperazione;
import it.cnr.contab.util.enumeration.StatoVariazioneSostituzione;
import it.cnr.contab.util.enumeration.TipoDebitoSIOPE;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.blobs.bulk.Bframe_blobBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDNotDeletableException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.si.service.dto.anagrafica.letture.PersonaEntitaOrganizzativaWebDto;
import it.cnr.si.spring.storage.MimeTypes;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.bulk.StorageFile;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.siopeplus.*;
import org.apache.commons.io.IOUtils;

import javax.ejb.EJBException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DistintaCassiereComponent extends
        it.cnr.jada.comp.CRUDDetailComponent implements IDistintaCassiereMgr,
        Cloneable, Serializable {

    public static final String SEPA_CREDIT_TRANSFER = "SEPA CREDIT TRANSFER";
    public static final String ACCREDITO_CONTO_CORRENTE_POSTALE = "ACCREDITO CONTO CORRENTE POSTALE";
    public static final String LIBERA = "LIBERA";
    public static final String NUMERO_CONTO_BANCA_ITALIA_ENTE_RICEVENTE = "0001777";
    public static final String TIPO_CONTABILITA_ENTE_RICEVENTE = "INFRUTTIFERA";
    public static final String STIPENDI = "STIPENDI";
    final private static String SEMAFORO_DISTINTA = "DISTINTA_CASSIERE00";
    public static final String CORRENTE = "CORRENTE";
    public static final String COMPENSAZIONE = "COMPENSAZIONE";
    public static final String REGOLARIZZAZIONE = "REGOLARIZZAZIONE";
    public static final String CASSA = "CASSA";
    public static final String INFRUTTIFERO = "INFRUTTIFERO";
    public static final String INSERIMENTO = "INSERIMENTO";
    public static final String ANNULLO = "ANNULLO";

    public static final int MAX_LENGTH_CAUSALE = 140;
    public static final String FATT_ANALOGICA = "FATT_ANALOGICA";
    public static final String DOC_EQUIVALENTE = "DOC_EQUIVALENTE";
    public static final String REGOLARIZZAZIONE_ACCREDITO_BANCA_D_ITALIA = "REGOLARIZZAZIONE ACCREDITO BANCA D'ITALIA";
    public static final String SCOSTAMENTO = "0.03";
    public static final String VARIAZIONE = "VARIAZIONE";
    public static final String SOSTITUZIONE = "SOSTITUZIONE";
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("ddMMyyyy");
    final String regexBic = "[A-Z|a-z||0-9]{11}|[A-Z|a-z||0-9]{8}";
    final Pattern patternBic = Pattern.compile(regexBic, Pattern.MULTILINE);

    public DistintaCassiereComponent() {
    }

    /**
     * Crea un semaforo per consentire l'immissione della prima distinta
     * <p>
     * Nome: crea semaforo - ok Pre: E' necessario inserire la prima distinta (
     * per esercizio e uo) e nessun altro utente sta eseguendo questa operazione
     * Post: Il semaforo viene acquisito in modo da impedire ad altri utenti
     * l'inserimento di un'altra distinta
     * <p>
     * Nome: crea semaforo - errore Pre: E' necessario inserire la prima
     * distinta ( per esercizio e uo) e un altro utente sta eseguendo la stessa
     * operazione Post: Una segnalazione di errore comunica all'utente
     * l'impossibilità a procedere
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinta    l'istanza di Distinta_cassiereBulk da inserire
     */

    protected void acquisisciSemaforo(UserContext userContext,
                                      Distinta_cassiereBulk distinta) throws ComponentException {

        try {
            LoggableStatement cs = new LoggableStatement(
                    getConnection(userContext), "{  call "
                    + EJBCommonServices
                    .getDefaultSchema()
                    + "CNRCTB800.acquisisciSemaforo(?, ?, ?, ?)}",
                    false, this.getClass());
            try {
                cs.setObject(1, distinta.getEsercizio());
                cs.setString(2, distinta.getCd_unita_organizzativa());
                cs.setString(3, SEMAFORO_DISTINTA);
                cs.setString(4, userContext.getUser());
                cs.executeQuery();
            } catch (SQLException e) {
                throw handleException(e);
            } finally {
                cs.close();
            }
        } catch (SQLException e) {
            throw handleException(e);
        }

    }

    /**
     * Assegna lo stato trasmissione di mandato/reversale
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Aggiorna stato mandato accreditamento Pre: Una richiesta di
     * aggiornare lo stato trasmissione di un documento contabile di tipo
     * mandato di accreditamento e' stata generata Post: E' stato aggiornato lo
     * stato del mandato di accreditamento (metodo
     * 'aggiornaStatoMandatoAccreditamento')
     * <p>
     * Nome: Aggiorna stato mandato non di accreditamento Pre: Una richiesta di
     * aggiornare lo stato trasmissione di un mandato con tipologia diversa da
     * accreditamento e' stata generata Post: E' stato aggiornato lo stato del
     * mandato (metodo 'aggiornaStatoMandato')
     * <p>
     * Nome: Aggiorna stato reversale Pre: Una richiesta di aggiornare lo stato
     * trasmissione di una reversale e' stata generata Post: E' stato aggiornato
     * lo stato della reversale (metodo 'aggiornaStatoReversale')
     *
     * @param userContext        lo UserContext che ha generato la richiesta
     * @param docContabile       il documento contabile V_mandato_reversaleBulk per cui
     *                           aggiornare lo stato
     * @param stato_trasmissione il nuovo stato che il doc. contabile dovrà assumere
     */

    protected void aggiornaStatoDocContabile(UserContext userContext,
                                             V_mandato_reversaleBulk docContabile, String stato_trasmissione)
            throws OutdatedResourceException,
            PersistencyException, ComponentException,
            BusyResourceException, EJBException {
        if (docContabile.isMandatoAccreditamento())
            aggiornaStatoMandatoAccreditamento(userContext, docContabile,
                    stato_trasmissione);
        else if (docContabile.isMandato())
            aggiornaStatoMandato(userContext, docContabile, stato_trasmissione);
        else if (docContabile.isReversale())
            aggiornaStatoReversale(userContext, docContabile,
                    stato_trasmissione);
    }

    /**
     * Assegna lo stato trasmissione di tutti i mandati/reversali inseriti in
     * distinta
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Aggiorna stato doc.contabili Pre: Una richiesta di aggiornare lo
     * stato trasmissione di tutti doc. contabili inseriti in una distinta e'
     * stata generata Post: Per ogni doc. contabile inserito in distinta e'
     * stato richiamato il metodo 'aggiornaStatoDocContabile' che ne ha
     * aggiornato lo stato trasmissione
     *
     * @param userContext        lo UserContext che ha generato la richiesta
     * @param distinta           la distinta i cui doc.contabili devono essere aggiornati
     * @param stato_trasmissione il nuovo stato che il doc. contabile dovrà assumere
     */

    protected void aggiornaStatoDocContabili(UserContext userContext,
                                             Distinta_cassiereBulk distinta, String stato_trasmissione)
            throws ComponentException {
        try {
            SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
                    userContext, distinta, V_mandato_reversaleBulk.class, null);
            SQLBroker broker = getHome(userContext,
                    V_mandato_reversaleBulk.class).createBroker(sql);
            V_mandato_reversaleBulk docContabile;
            while (broker.next()) {
                docContabile = (V_mandato_reversaleBulk) broker
                        .fetch(V_mandato_reversaleBulk.class);
                aggiornaStatoDocContabile(userContext, docContabile,
                        stato_trasmissione);
            }
            broker.close();
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * Assegna lo stato trasmissione di un mandato con tipologia diversa da
     * accreditamento
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Aggiorna stato mandato a INSERITO_IN_DISTINTA/NON INSERITO IN
     * DISTINTA Pre: Una richiesta di aggiornare lo stato trasmissione di un
     * mandato non di accreditamento e' stata generata e il nuovo stato e'
     * INSERITO_IN_DISTINTA o NON INSERITO IN DISTINTA Post: E' stato aggiornato
     * lo stato del mandato
     * <p>
     * Nome: Prima trasmissione Pre: Una richiesta di aggiornare lo stato
     * trasmissione di un mandato non di accreditamento e' stata generata e il
     * nuovo stato e' TRASMESSO e il mandato non e' ancora stato trasmesso Post:
     * E' stato aggiornato lo stato del mandato e la sua data di trasmissione
     * <p>
     * Nome: Ritrasmissione Pre: Una richiesta di aggiornare lo stato
     * trasmissione di un mandato non di accreditamento e' stata generata e il
     * nuovo stato e' TRASMESSO e il mandato era già stato trasmesso Post: E'
     * stato aggiornato lo stato del mandato e la sua data di ritrasmissione
     *
     * @param userContext        lo UserContext che ha generato la richiesta
     * @param docContabile       il mandato non di accredit. per cui aggiornare lo stato
     * @param stato_trasmissione il nuovo stato che il mandato dovrà assumere
     */

    protected void aggiornaStatoMandato(UserContext userContext,
                                        V_mandato_reversaleBulk docContabile, String stato_trasmissione)
            throws OutdatedResourceException,
            PersistencyException, ComponentException,
            BusyResourceException, EJBException {
        MandatoBulk mandato = (MandatoBulk) getHome(userContext,
                MandatoIBulk.class).findAndLock(
                new MandatoIBulk(docContabile.getCd_cds(), docContabile
                        .getEsercizio(), docContabile.getPg_documento_cont()));

        if (mandato.getPg_ver_rec().compareTo(docContabile.getPg_ver_rec())
                != 0)
            throw new ApplicationException("Attenzione! Il mandato " +
                    mandato.getPg_mandato() +
                    " non e' più valido perche' stato modificato.");
        if (mandato.isAnnullato())
            mandato.setStato_trasmissione_annullo(stato_trasmissione);
        else
            mandato.setStato_trasmissione(stato_trasmissione);

        // se si tratta di una prima trasmissione aggiorna la dt_trasmissione
        if (MandatoBulk.STATO_TRASMISSIONE_TRASMESSO.equals(mandato
                .getStato_trasmissione())) {
            if (mandato.getDt_trasmissione() == null) {
                // Se la data di annullamento NON E' NULLA, e siamo in esercizio
                // successivo, metto
                // la data di trasmissione = ad istante successivo a quella di
                // annullamento
                if (DateServices.isAnnoMaggEsScriv(userContext)) {
                    if (mandato.getDt_annullamento() != null) {
                        mandato.setDt_trasmissione(DateServices.getNextMinTs(
                                userContext, mandato.getDt_annullamento()));
                    } else {
                        mandato
                                .setDt_trasmissione(DateServices
                                        .getMidDayTs(DateServices
                                                .getTs_valido(userContext)));
                    }
                } else {
                    mandato.setDt_trasmissione(DateServices
                            .getTs_valido(userContext));
                }
                // se si tratta di una ritrasmissione aggiorna la dt_ritrasmissione
            } else if (mandato.getDt_trasmissione() != null) {
                if (MandatoBulk.STATO_TRASMISSIONE_TRASMESSO.equals(mandato
                        .getStato_trasmissione_annullo()) || mandato
                        .getStato_trasmissione_annullo() == null) {
                    if (DateServices.isAnnoMaggEsScriv(userContext)) {
                        if (mandato.getDt_trasmissione().after(
                                Optional.ofNullable(mandato.getDt_annullamento())
                                    .orElse(mandato.getDt_trasmissione())
                        ))
                            mandato.setDt_ritrasmissione(DateServices.getNextMinTs(
                                    userContext, mandato.getDt_trasmissione()));
                        else
                            mandato.setDt_ritrasmissione(DateServices.getNextMinTs(
                                    userContext, Optional.ofNullable(mandato.getDt_annullamento())
                                            .orElse(mandato.getDt_trasmissione())));
                    } else {
                        mandato.setDt_ritrasmissione(DateServices
                                .getTs_valido(userContext));
                    }
                    if (Optional.ofNullable(mandato.getStatoVarSos())
                                .map(statoVarSos -> statoVarSos.equals(StatoVariazioneSostituzione.VARIAZIONE_DEFINITIVA.value()))
                                .orElse(Boolean.FALSE)){
                        mandato.setStatoVarSos(StatoVariazioneSostituzione.VARIAZIONE_TRASMESSA.value());
                    }
                } else {
                    mandato.setDt_ritrasmissione(null);
                }
            }
        }
        mandato.setUser(userContext.getUser());
        updateBulk(userContext, mandato);

    }

    /**
     * Assegna lo stato trasmissione di un mandato con tipologia accreditamento
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Aggiorna stato mandato a INSERITO_IN_DISTINTA/NON INSERITO IN
     * DISTINTA Pre: Una richiesta di aggiornare lo stato trasmissione di un
     * mandato di accreditamento e' stata generata e il nuovo stato e' INSERITO
     * IN DISTINTA o NON INSERITO IN DISTINTA Post: E' stato aggiornato lo stato
     * del mandato
     * <p>
     * Nome: Prima trasmissione Pre: Una richiesta di aggiornare lo stato
     * trasmissione di un mandato di accreditamento e' stata generata e il nuovo
     * stato e' TRASMESSO e il mandato non e' ancora stato trasmesso Post: E'
     * stato aggiornato lo stato del mandato e la sua data di trasmissione
     * <p>
     * Nome: Ritrasmissione Pre: Una richiesta di aggiornare lo stato
     * trasmissione di un mandato di accreditamento e' stata generata e il nuovo
     * stato e' TRASMESSO e il mandato era già stato trasmesso Post: E' stato
     * aggiornato lo stato del mandato e la sua data di ritrasmissione
     *
     * @param userContext        lo UserContext che ha generato la richiesta
     * @param docContabile       il mandato di accredit. per cui aggiornare lo stato
     * @param stato_trasmissione il nuovo stato che il mandato dovrà assumere
     */

    protected void aggiornaStatoMandatoAccreditamento(UserContext userContext,
                                                      V_mandato_reversaleBulk docContabile, String stato_trasmissione)
            throws OutdatedResourceException,
            PersistencyException, ComponentException,
            BusyResourceException, EJBException {
        MandatoBulk mandato = (MandatoBulk) getHome(userContext,
                MandatoAccreditamentoBulk.class).findAndLock(
                new MandatoAccreditamentoBulk(docContabile.getCd_cds(),
                        docContabile.getEsercizio(), docContabile
                        .getPg_documento_cont()));
        if (mandato.getPg_ver_rec().compareTo(docContabile.getPg_ver_rec())
                != 0)
            throw new ApplicationException("Attenzione! Il mandato " +
                    mandato.getPg_mandato() +
                    " non e' più valido perche' stato modificato.");

        mandato.setStato_trasmissione(stato_trasmissione);

        // se si tratta di una prima trasmissione aggiorna la dt_trasmissione
        if (MandatoBulk.STATO_TRASMISSIONE_TRASMESSO.equals(mandato
                .getStato_trasmissione())) {
            if (mandato.getDt_trasmissione() == null) {
                // Se la data di annullamento NON E' NULLA, e siamo in esercizio
                // successivo, metto
                // la data di trasmissione = ad istante successivo a quella di
                // annullamento
                if (DateServices.isAnnoMaggEsScriv(userContext)) {
                    if (mandato.getDt_annullamento() != null) {
                        mandato.setDt_trasmissione(DateServices.getNextMinTs(
                                userContext, mandato.getDt_annullamento()));
                    } else {
                        mandato
                                .setDt_trasmissione(DateServices
                                        .getMidDayTs(DateServices
                                                .getTs_valido(userContext)));
                    }
                } else {
                    mandato.setDt_trasmissione(DateServices
                            .getTs_valido(userContext));
                }
                // se si tratta di una ritrasmissione aggiorna la dt_ritrasmissione
            } else if (mandato.getDt_trasmissione() != null) {
                if (MandatoBulk.STATO_TRASMISSIONE_TRASMESSO.equals(mandato
                        .getStato_trasmissione_annullo()) || mandato
                        .getStato_trasmissione_annullo() == null) {

                    if (DateServices.isAnnoMaggEsScriv(userContext)) {
                        if (mandato.getDt_annullamento() == null
                                || mandato.getDt_trasmissione().after(
                                mandato.getDt_annullamento()))
                            mandato.setDt_ritrasmissione(DateServices.getNextMinTs(
                                    userContext, mandato.getDt_trasmissione()));
                        else
                            mandato.setDt_ritrasmissione(DateServices.getNextMinTs(
                                    userContext, mandato.getDt_annullamento()));
                    } else {
                        mandato.setDt_ritrasmissione(DateServices
                                .getTs_valido(userContext));
                    }
                    if (Optional.ofNullable(mandato.getStatoVarSos())
                            .map(statoVarSos -> statoVarSos.equals(StatoVariazioneSostituzione.VARIAZIONE_DEFINITIVA.value()))
                            .orElse(Boolean.FALSE)){
                        mandato.setStatoVarSos(StatoVariazioneSostituzione.VARIAZIONE_TRASMESSA.value());
                    }
                } else {
                    mandato.setDt_ritrasmissione(null);
                }
            }
        }
        mandato.setUser(userContext.getUser());
        updateBulk(userContext, mandato);

    }

    /**
     * Assegna lo stato trasmissione di una reversale
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Aggiorna stato reversale a INSERITO IN DISTINTA/NON INSERITO IN
     * DISTINTA Pre: Una richiesta di aggiornare lo stato trasmissione di una
     * reversale e' stata generata e il nuovo stato e' INSERITO IN DISTINTA o
     * NON INSERITO IN DISTINTA Post: E' stato aggiornato lo stato della
     * reversale
     * <p>
     * Nome: Prima trasmissione Pre: Una richiesta di aggiornare lo stato
     * trasmissione di una reversale e' stata generata e il nuovo stato e'
     * TRASMESSO e la reversale non e' ancora stata trasmessa Post: E' stato
     * aggiornato lo stato della reversale e la sua data di trasmissione
     * <p>
     * Nome: Ritrasmissione Pre: Una richiesta di aggiornare lo stato
     * trasmissione di una reversale e' stata generata e il nuovo stato e'
     * TRASMESSO e la reversale era già stata trasmessa Post: E' stato
     * aggiornato lo stato della reversale e la sua data di ritrasmissione
     *
     * @param userContext        lo UserContext che ha generato la richiesta
     * @param docContabile       la reversale per cui aggiornare lo stato
     * @param stato_trasmissione il nuovo stato che la reversale dovrà assumere
     */

    protected void aggiornaStatoReversale(UserContext userContext,
                                          V_mandato_reversaleBulk docContabile, String stato_trasmissione)
            throws OutdatedResourceException,
            PersistencyException, ComponentException,
            BusyResourceException, EJBException {
        ReversaleBulk reversale = (ReversaleBulk) getHome(userContext,
                ReversaleIBulk.class).findAndLock(
                new ReversaleIBulk(docContabile.getCd_cds(), docContabile
                        .getEsercizio(), docContabile.getPg_documento_cont()));
        if (reversale.getPg_ver_rec().compareTo(
                docContabile.getPg_ver_rec()) != 0)
            throw new ApplicationException("Attenzione! La reversale " +
                    reversale.getPg_reversale() +
                    " non e' più valida perche' stata modificata.");
        if (reversale.isAnnullato())
            reversale.setStato_trasmissione_annullo(stato_trasmissione);
        else
            reversale.setStato_trasmissione(stato_trasmissione);

        // se si tratta di una prima trasmissione aggiorna la dt_trasmissione
        if (ReversaleBulk.STATO_TRASMISSIONE_TRASMESSO.equals(reversale
                .getStato_trasmissione())) {

            if (reversale.getDt_trasmissione() == null) {
                // Se la data di annullamento NON E' NULLA, e siamo in esercizio
                // successivo, metto
                // la data di trasmissione = ad istante successivo a quella di
                // annullamento
                if (DateServices.isAnnoMaggEsScriv(userContext)) {
                    if (reversale.getDt_annullamento() != null) {
                        reversale.setDt_trasmissione(DateServices.getNextMinTs(
                                userContext, reversale.getDt_annullamento()));
                    } else {
                        reversale
                                .setDt_trasmissione(DateServices
                                        .getMidDayTs(DateServices
                                                .getTs_valido(userContext)));
                    }
                } else {
                    reversale.setDt_trasmissione(DateServices
                            .getTs_valido(userContext));
                }
                // se si tratta di una ritrasmissione aggiorna la dt_ritrasmissione
            } else if (reversale.getDt_trasmissione() != null) {
                if (ReversaleBulk.STATO_TRASMISSIONE_TRASMESSO.equals(reversale
                        .getStato_trasmissione_annullo()) || reversale
                        .getStato_trasmissione_annullo() == null) {
                    if (DateServices.isAnnoMaggEsScriv(userContext)) {

                        if (reversale.getDt_annullamento() == null
                                || reversale.getDt_trasmissione().after(
                                reversale.getDt_annullamento()))
                            reversale.setDt_ritrasmissione(DateServices.getNextMinTs(
                                    userContext, reversale.getDt_trasmissione()));
                        else
                            reversale.setDt_ritrasmissione(DateServices.getNextMinTs(
                                    userContext, reversale.getDt_annullamento()));
                    } else {
                        reversale.setDt_ritrasmissione(DateServices
                                .getTs_valido(userContext));
                    }
                    if (Optional.ofNullable(reversale.getStatoVarSos())
                            .map(statoVarSos -> statoVarSos.equals(StatoVariazioneSostituzione.VARIAZIONE_DEFINITIVA.value()))
                            .orElse(Boolean.FALSE)){
                        reversale.setStatoVarSos(StatoVariazioneSostituzione.VARIAZIONE_TRASMESSA.value());
                    }
                } else {
                    reversale.setDt_ritrasmissione(null);
                }
            }
        }
        reversale.setUser(userContext.getUser());
        updateBulk(userContext, reversale);

    }

    /**
     * Calcola i totali storici dei doc.contabili trasmessi a cassiere
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Calcola totali storici Pre: Una richiesta di invio di una distinta
     * al cassiere e' stata generata ed e pertanto necessario aggiornare lo
     * storico dei totali Post: Nella distinta sono stati calcolati i totali
     * storici (suddivisi per tipologia) sommando gli importi dei doc. contabili
     * non annullati presenti in distinta
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinta    la distinta da inviare al cassiere
     * @return distinta la distinta coi totali storici aggiornati
     */

    protected Distinta_cassiereBulk aggiornaStoricoTrasmessi(
            UserContext userContext, Distinta_cassiereBulk distinta)
            throws ComponentException,
            PersistencyException {
        try {
            calcolaTotali(userContext, distinta);
            calcolaTotaliStorici(userContext, distinta);
            /* aggiorno lo storico dei trasmessi */
            distinta
                    .setIm_man_ini_pag(distinta
                            .getTotStoricoMandatiPagamentoTrasmessi()
                            .add(distinta.getTotMandatiPagamento())
                            .subtract(
                                    distinta
                                            .getTotMandatiPagamentoAnnullatiRitrasmessi()));
            distinta
                    .setIm_man_ini_sos(distinta
                            .getTotStoricoMandatiRegSospesoTrasmessi()
                            .add(distinta.getTotMandatiRegSospeso())
                            .subtract(
                                    distinta
                                            .getTotMandatiRegSospesoAnnullatiRitrasmessi()));
            distinta
                    .setIm_man_ini_acc(distinta
                            .getTotStoricoMandatiAccreditamentoTrasmessi()
                            .add(distinta.getTotMandatiAccreditamento())
                            .subtract(
                                    distinta
                                            .getTotMandatiAccreditamentoAnnullatiRitrasmessi()));
            distinta
                    .setIm_rev_ini_sos(distinta
                            .getTotStoricoReversaliRegSospesoTrasmesse()
                            .add(distinta.getTotReversaliRegSospesoBI())
                            .subtract(
                                    distinta
                                            .getTotReversaliRegSospesoBIAnnullateRitrasmesse())
                            .add(distinta.getTotReversaliRegSospesoCC())
                            .subtract(
                                    distinta
                                            .getTotReversaliRegSospesoCCAnnullateRitrasmesse()));
            distinta
                    .setIm_rev_ini_tra(distinta
                            .getTotStoricoReversaliTrasferimentoTrasmesse()
                            .add(distinta.getTotReversaliTrasferimento())
                            .subtract(
                                    distinta
                                            .getTotReversaliTrasferimentoAnnullateRitrasmesse()));
            distinta
                    .setIm_rev_ini_rit(distinta
                            .getTotStoricoReversaliRitenuteTrasmesse()
                            .add(distinta.getTotReversaliRitenute())
                            .subtract(
                                    distinta
                                            .getTotReversaliRitenuteAnnullateRitrasmesse()));

            return distinta;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Nome: Aggiungi mandati reversali collegati Pre: E' stata generata la
     * richiesta di inserire in distinta un mandato/reversale Post: Per ogni
     * mandato/reversale dipendente da quello da inserire in distinta viene
     * creato automaticamente un altro dettaglio distinta e lo stato di tale
     * doc.contabile viene aggiornato a INSERITO IN DISTINTA ( metodo
     * 'inserisciDettaglioDistinta')
     *
     * @param userContext       lo UserContext che ha generato la richiesta
     * @param distinta          la Distinta_cassiereBulk in cui inserire i doc. contabili
     *                          collegati
     * @param docContabilePadre il V_mandato_reversaleBulk per cui ricercare i doc. contabili
     *                          dièpendenti
     * @param last_pg_dettaglio , il progressivo dell'ultimo dettaglio inserito in distinta
     * @return il progressivo aggiornato dell'ultimo dettaglio inserito in
     * distinta
     */

    protected Long aggiungiMandatiEReversaliCollegati(UserContext userContext,
                                                      Distinta_cassiereBulk distinta,
                                                      V_mandato_reversaleBulk docContabilePadre, Long last_pg_dettaglio)
            throws PersistencyException, ComponentException {
        /*
         * aggiungo in automatico i mandati già trasmessi e successivamente
         * annullati
         */
        Collection docContabili = ((V_mandato_reversaleHome) getHome(
                userContext, V_mandato_reversaleBulk.class))
                .findDocContabiliCollegati(docContabilePadre);
        V_mandato_reversaleBulk docContabile;
        if (Optional.ofNullable(docContabilePadre.getStatoVarSos())
                .map(s -> !Arrays.asList(StatoVariazioneSostituzione.ANNULLATO_PER_SOSTITUZIONE.value(),
                        StatoVariazioneSostituzione.SOSTITUZIONE_DEFINITIVA.value()).contains(s))
                .orElse(Boolean.TRUE)) {
            for (Iterator i = docContabili.iterator(); i.hasNext(); ) {
                docContabile = (V_mandato_reversaleBulk) i.next();
                if (Optional.ofNullable(docContabile.getEsitoOperazione()).map(s -> s.equals(EsitoOperazione.NON_ACQUISITO.value())).orElse(Boolean.TRUE)) {
                    last_pg_dettaglio = inserisciDettaglioDistinta(userContext,
                            distinta, docContabile, last_pg_dettaglio);
                    inserisciDettaglioDistinteCollegate(userContext, distinta,
                            docContabile);
                }
            }
        }
        return last_pg_dettaglio;
    }

    /**
     * Nome: Aggiungi mandati reversali da ritrasmettere Pre: E' stata generata
     * la richiesta di inviare una distinta al cassiere ed e' pertanto
     * necessario inserire automaticamente in distinta tutti i mandati/reversali
     * che sono stati annullati dopo che erano già stati inviati a cassiere
     * Post: E' stato creato un nuovo dettaglio per ogni doc. contabile con data
     * di annullamento successiva alla data di trasmissione e data di
     * ritrasmissione non valorizzata.
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinta    la Distinta_cassiereBulk in cui inserire i doc. contabili da
     *                    ritrasmettere
     * @throws RemoteException
     * @throws EJBException
     */

    protected void aggiungiMandatiEReversaliDaRitrasmettere(
            UserContext userContext, Distinta_cassiereBulk distinta)
            throws SQLException, ComponentException, PersistencyException,
            EJBException, RemoteException {
        Long last_pg_dettaglio = ((Distinta_cassiere_detHome) getHome(
                userContext, Distinta_cassiere_detBulk.class))
                .getUltimoPg_Dettaglio(userContext, distinta);
        Collection docContabili = ((V_mandato_reversaleHome) getHome(
                userContext, V_mandato_reversaleBulk.class))
                .findDocContabiliAnnullatiDaRitrasmettere(distinta,
                        annulliTuttaSac(userContext, distinta), tesoreriaUnica(userContext, distinta));
        V_mandato_reversaleBulk docContabile;
        for (Iterator i = docContabili.iterator(); i.hasNext(); ) {
            docContabile = (V_mandato_reversaleBulk) i.next();

            last_pg_dettaglio = inserisciDettaglioDistinta(userContext,
                    distinta, docContabile, last_pg_dettaglio);
            inserisciDettaglioDistinteCollegate(userContext, distinta,
                    docContabile);

            /*
             * non e' necessario aggiungere i collegati perch' se e' stato
             * annullato il padre, automaticamente risultano annullati tutti i
             * collegati last_pg_dettaglio = aggiungiMandatiEReversaliCollegati(
             * userContext, distinta, docContabile, last_pg_dettaglio ) ;
             */
        }
    }

    /**
     * Nome: Annulla modifiche dettagli Pre: E' stata generata la richiesta di
     * annullare tutte le modifiche fatte dall'utente ai dettagli della distinta
     * Post: La transazione viene riportata all'ultimo savepoint impostato
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinta    la Distinta_cassiereBulk le cui modifiche ai dettagli devono
     *                    essere annullate
     */

    public void annullaModificaDettagliDistinta(
            UserContext userContext, Distinta_cassiereBulk distinta)
            throws ComponentException {
        try {
            rollbackToSavepoint(userContext, "DISTINTA_CASSIERE_DET");
        } catch (SQLException e) {
            throw handleException(e);
        }
    }

    /**
     * Assegna il progressivo
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Assegna progressivo Pre: Una richiesta di creazione di una distinta
     * e' stata generata ed e' pertanto necessario assegnarle il progressivo
     * Post: Il progressivo viene calcolato incrementando di 1 l'ultimo
     * progressivo presente nel db per l'esercizio e l'uo di scrivania;
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinta    la Distinta_cassiereBulk per cui generare il progressivo
     * @param distinta    la distinta con il progressivo assegnato
     */

    protected Distinta_cassiereBulk assegnaProgressivo(UserContext userContext,
                                                       Distinta_cassiereBulk distinta) throws ComponentException,
            PersistencyException {
        try {
            ((Distinta_cassiereHome) getHome(userContext, distinta.getClass()))
                    .inizializzaProgressivo(userContext, distinta);
            return distinta;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Assegna il progressivo cassiere
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Assegna progressivo cassiere Pre: Una richiesta di inviare una
     * distinta a cassiere e' stata generata ed e' pertanto necessario
     * assegnarle il progressivo cassiere Post: Il progressivo cassiere viene
     * calcolato incrementando di 1 l'ultimo progressivo cassiere presente per
     * l'esercizio e l'uo di scrivania;
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinta    la Distinta_cassiereBulk per cui generare il progressivo
     *                    cassiere
     * @param distinta    la distinta con il progressivo cassiere assegnato
     */

    protected Distinta_cassiereBulk assegnaProgressivoCassiere(
            UserContext userContext, Distinta_cassiereBulk distinta)
            throws ComponentException,
            PersistencyException {
        try {
            ((Distinta_cassiereHome) getHome(userContext, distinta.getClass()))
                    .inizializzaProgressivoCassiere(userContext, distinta);
            return distinta;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Inserisce in distinta tutti i mandati e tutte le reversali
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Inserisci tutti i documenti contabili Pre: Una richiesta di
     * inserire in distinta tutti i mandati e le reversali visualizzati
     * all'utente e' stata generata Post: Per ogni mandato/reversale
     * visualizzato all'utente viene generato un dettaglio della distinta e lo
     * stato trasmissione del mandato/reversale viene aggiornato a 'inserito in
     * distinta' (metodo 'inserisciDettaglioDistinta'); se tale
     * mandato/reversale ha associati altre reversali/mandati, vengono creati
     * automaticamente dei dettagli di distinta anche per questi ed il loro
     * stato trasmissione viene aggiornato (metodo
     * 'aggiungiMandatiEReversaliCollegati').
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinta    la Distinta_cassiereBulk per cui generare i dettagkli
     * @param docPassivo  un istanza di V_mandato_reversaleBulk contente i criteri di
     *                    ricerca specificati dall'utente nella selezione del
     *                    mandato/reversale
     */

    public void associaTuttiDocContabili(UserContext userContext,
                                         Distinta_cassiereBulk distinta, V_mandato_reversaleBulk docPassivo)
            throws ComponentException {

        try {
            SQLQuery sql = cercaMandatiEReversaliSQL(userContext, null,
                    docPassivo, distinta);
            V_mandato_reversaleHome home = (V_mandato_reversaleHome) getHome(
                    userContext, V_mandato_reversaleBulk.class);
            SQLBroker broker = home.createBroker(sql);

            Long last_pg_dettaglio = ((Distinta_cassiere_detHome) getHome(
                    userContext, Distinta_cassiere_detBulk.class))
                    .getUltimoPg_Dettaglio(userContext, distinta);
            V_mandato_reversaleBulk docContabile;
            while (broker.next()) {
                docContabile = (V_mandato_reversaleBulk) broker
                        .fetch(V_mandato_reversaleBulk.class);
                last_pg_dettaglio = inserisciDettaglioDistinta(userContext,
                        distinta, docContabile, last_pg_dettaglio);
                last_pg_dettaglio = aggiungiMandatiEReversaliCollegati(
                        userContext, distinta, docContabile, last_pg_dettaglio);
                inserisciDettaglioDistinteCollegate(userContext, distinta,
                        docContabile);
            }
            broker.close();

        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * Calcola i totali dei mandati/reversali inseriti in distinta
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Calcola totali Pre: Una richiesta di calcolare i totali dei mandati
     * e delle reversali inseriti in distinta e' stata generata Post: I totali,
     * distinti secondo le varie tipologie dei mandati e delle reversali, sono
     * stati calcolati
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinta    la Distinta_cassiereBulk per cui calcolare i totali
     * @return la Distinta_cassiereBulk con tutti i totali impostati
     */

    public Distinta_cassiereBulk calcolaTotali(UserContext userContext,
                                               Distinta_cassiereBulk distinta) throws ComponentException {
        try {
            Distinta_cassiereHome distHome = (Distinta_cassiereHome) getHome(
                    userContext, distinta.getClass());
            distinta.resetTotali();
            distinta.setTotMandatiAccreditamento(distHome
                    .calcolaTotMandatiAccreditamento(distinta));
            distinta.setTotMandatiAccreditamentoAnnullati(distHome
                    .calcolaTotMandatiAccreditamentoAnnullati(distinta));
            distinta
                    .setTotMandatiAccreditamentoAnnullatiRitrasmessi(distHome
                            .calcolaTotMandatiAccreditamentoAnnullatiRitrasmessi(distinta));
            distinta.setTotMandatiPagamento(distHome
                    .calcolaTotMandatiPagamento(distinta));
            distinta.setTotMandatiPagamentoAnnullati(distHome
                    .calcolaTotMandatiPagamentoAnnullati(distinta));
            distinta.setTotMandatiPagamentoAnnullatiRitrasmessi(distHome
                    .calcolaTotMandatiPagamentoAnnullatiRitrasmessi(distinta));
            distinta.setTotMandatiRegSospeso(distHome
                    .calcolaTotMandatiRegSospeso(distinta));
            distinta.setTotMandatiRegSospesoAnnullati(distHome
                    .calcolaTotMandatiRegSospesoAnnullati(distinta));
            distinta.setTotMandatiRegSospesoAnnullatiRitrasmessi(distHome
                    .calcolaTotMandatiRegSospesoAnnullatiRitrasmessi(distinta));

            distinta = distHome.calcolaTotReversaliRegSospeso(distinta);
            /*
             * distinta.setTotReversaliRegSospesoBI(
             * distHome.calcolaTotReversaliRegSospesoBI( distinta ));
             * distinta.setTotReversaliRegSospesoBIAnnullate(
             * distHome.calcolaTotReversaliRegSospesoBIAnnullate( distinta ));
             * distinta.setTotReversaliRegSospesoBIAnnullateRitrasmesse(
             * distHome.calcolaTotReversaliRegSospesoBIAnnullateRitrasmesse(
             * distinta )); distinta.setTotReversaliRegSospesoCC(
             * distHome.calcolaTotReversaliRegSospesoCC( distinta ));
             * distinta.setTotReversaliRegSospesoCCAnnullate(
             * distHome.calcolaTotReversaliRegSospesoCCAnnullate( distinta ));
             * distinta.setTotReversaliRegSospesoCCAnnullateRitrasmesse(
             * distHome.calcolaTotReversaliRegSospesoCCAnnullateRitrasmesse(
             * distinta ));
             */

            distinta.setTotReversaliTrasferimento(distHome
                    .calcolaTotReversaliTrasferimento(distinta));
            distinta.setTotReversaliTrasferimentoAnnullate(distHome
                    .calcolaTotReversaliTrasferimentoAnnullate(distinta));
            distinta
                    .setTotReversaliTrasferimentoAnnullateRitrasmesse(distHome
                            .calcolaTotReversaliTrasferimentoAnnullateRitrasmesse(distinta));
            distinta.setTotReversaliRitenute(distHome
                    .calcolaTotReversaliRitenute(distinta));
            distinta.setTotReversaliRitenuteAnnullate(distHome
                    .calcolaTotReversaliRitenuteAnnullate(distinta));
            distinta.setTotReversaliRitenuteAnnullateRitrasmesse(distHome
                    .calcolaTotReversaliRitenuteAnnullateRitrasmesse(distinta));

            return distinta;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Calcola i totali storici degli importi dei mandati/reversali trasmesse al
     * cassiere
     * <p>
     * Nome: storico senza distinte trasmesse Pre: E' stata generata la
     * richiesta di calcolare i totali storici degli importi dei
     * mandati/reversali trasmessi al cassiere e nessuna distinta e' ancora
     * stata trasmessa al cassiere Post: Tutti i totali vengono inizializzati al
     * valore 0.
     * <p>
     * Nome: storico con distinte trasmesse Pre: E' stata generata la richiesta
     * di calcolare i totali storici degli importi dei mandati/reversali
     * trasmessi al cassiere e esistono distinte già trasmessa al cassiere Post:
     * Tutti i totali di ogni singola tipologia di doc. contabile vengono
     * inizializzati col valore impostato nell'ultima distinta trasmessa. I
     * totali complessivi dei mandati trasmessi vengono calcolati come somma dei
     * totali dei mandati di tipo 'Accreditamento', 'Regolamento Sospeso',
     * 'Pagamento'. I totali complessivi delle reversali trasmesse vengono
     * calcolati come somma dei totali delle reversali di tipo 'Accreditamento',
     * 'Regolamento Sospeso'. Inoltre vengono ricercati se esistono
     * mandati/reversali annullati dopo il trasferimento a cassiere della
     * distinta in cui erano stati inseriti: gli importi di tali doc. contabili
     * vengono sommati nei tot. mandati/reversali da ritrasmettere.
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinta    l'istanza di Distinta_cassiereBulk per cui calolare lo storico
     * @return la distinta coi totali storici valorizzati;
     */

    protected Distinta_cassiereBulk calcolaTotaliStorici(
            UserContext userContext, Distinta_cassiereBulk distinta)
            throws ComponentException {
        try {
            Distinta_cassiereBulk ultimaDistinta = ((Distinta_cassiereHome) getHome(
                    userContext, distinta.getClass()))
                    .findUltimaDistintaTrasmessa(distinta);
            if (ultimaDistinta == null) {
                distinta.setTotStoricoMandatiPagamentoTrasmessi(new BigDecimal(
                        0));
                distinta
                        .setTotStoricoMandatiRegSospesoTrasmessi(new BigDecimal(
                                0));
                distinta
                        .setTotStoricoMandatiAccreditamentoTrasmessi(new BigDecimal(
                                0));
                distinta.setTotStoricoMandatiTrasmessi(new BigDecimal(0));
                distinta
                        .setTotStoricoReversaliRegSospesoTrasmesse(new BigDecimal(
                                0));
                distinta
                        .setTotStoricoReversaliTrasferimentoTrasmesse(new BigDecimal(
                                0));
                distinta
                        .setTotStoricoReversaliRitenuteTrasmesse(new BigDecimal(
                                0));
                distinta.setTotStoricoReversaliTrasmesse(new BigDecimal(0));
            } else {
                distinta.setTotStoricoMandatiPagamentoTrasmessi(ultimaDistinta
                        .getIm_man_ini_pag());
                distinta.setTotStoricoMandatiRegSospesoTrasmessi(ultimaDistinta
                        .getIm_man_ini_sos());
                distinta
                        .setTotStoricoMandatiAccreditamentoTrasmessi(ultimaDistinta
                                .getIm_man_ini_acc());
                distinta
                        .setTotStoricoMandatiTrasmessi(distinta
                                .getTotStoricoMandatiAccreditamentoTrasmessi()
                                .add(
                                        distinta
                                                .getTotStoricoMandatiRegSospesoTrasmessi())
                                .add(
                                        distinta
                                                .getTotStoricoMandatiPagamentoTrasmessi()));
                distinta
                        .setTotStoricoReversaliRegSospesoTrasmesse(ultimaDistinta
                                .getIm_rev_ini_sos());
                distinta
                        .setTotStoricoReversaliTrasferimentoTrasmesse(ultimaDistinta
                                .getIm_rev_ini_tra());
                distinta.setTotStoricoReversaliRitenuteTrasmesse(ultimaDistinta
                        .getIm_rev_ini_rit());
                distinta
                        .setTotStoricoReversaliTrasmesse(distinta
                                .getTotStoricoReversaliRegSospesoTrasmesse()
                                .add(
                                        distinta
                                                .getTotStoricoReversaliTrasferimentoTrasmesse())
                                .add(
                                        distinta
                                                .getTotStoricoReversaliRitenuteTrasmesse()));
            }

            // calcolo i totali dei mandati/reversali da ritrasmettere
            V_mandato_reversaleBulk docContabile;
            distinta
                    .setTotStoricoMandatiDaRitrasmettere(new BigDecimal(
                            0));
            distinta
                    .setTotStoricoReversaliDaRitrasmettere(new BigDecimal(
                            0));
            for (Iterator i = ((V_mandato_reversaleHome) getHome(userContext,
                    V_mandato_reversaleBulk.class))
                    .findDocContabiliAnnullatiDaRitrasmettere(distinta,
                            annulliTuttaSac(userContext, distinta), tesoreriaUnica(userContext, distinta)).iterator(); i
                         .hasNext(); ) {
                docContabile = (V_mandato_reversaleBulk) i.next();
                if (docContabile.isMandato())
                    distinta.setTotStoricoMandatiDaRitrasmettere(distinta
                            .getTotStoricoMandatiDaRitrasmettere().add(
                                    docContabile.getIm_documento_cont()));
                else if (docContabile.isReversale())
                    distinta.setTotStoricoReversaliDaRitrasmettere(distinta
                            .getTotStoricoReversaliDaRitrasmettere().add(
                                    docContabile.getIm_documento_cont()));

            }

            return distinta;
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * Richiama la procedura che processa il file selezionato dall'utente
     * PreCondition: E' stata generata la richiesta di processare un file
     * selezionato dall'utente. PostCondition: Viene richiamata la procedura di
     * Processo dei File
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
     * @param distinta    il <code>V_ext_cassiere00Bulk</code> file da processare.
     **/
    private void callCheckDocContForDistinta(UserContext userContext,
                                             Distinta_cassiereBulk distinta)
            throws ComponentException {

        LoggableStatement cs = null;
        try {
            cs = new LoggableStatement(getConnection(userContext), "{ call "
                    + EJBCommonServices.getDefaultSchema()
                    + "CNRCTB750.checkDocContForDistCas(?,?,?,?) }", false,
                    this.getClass());

            cs.setString(1, distinta.getCd_cds());
            cs.setInt(2, distinta.getEsercizio().intValue());
            cs.setString(3, distinta.getCd_unita_organizzativa());
            cs.setLong(4, distinta.getPg_distinta().longValue());

            cs.executeQuery();
        } catch (Throwable e) {
            throw handleException(e);
        } finally {
            try {
                if (cs != null)
                    cs.close();
            } catch (SQLException e) {
                throw handleException(e);
            }
        }
    }

    /**
     * Richiama la procedura che processa il file selezionato dall'utente
     * PreCondition: E' stata generata la richiesta di processare un file
     * selezionato dall'utente. PostCondition: Viene richiamata la procedura di
     * Processo dei File
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
     * @param file        il <code>V_ext_cassiere00Bulk</code> file da processare.
     **/

    private void callProcessaFile(UserContext userContext,
                                  V_ext_cassiere00Bulk file)
            throws ComponentException {
        Timestamp oggi = null;
        BatchControlComponentSession batchControlComponentSession = (BatchControlComponentSession) EJBCommonServices
                .createEJB("BLOGS_EJB_BatchControlComponentSession");
        String subjectError = "Errore Caricamento Giornaliera File: "+file.getNome_file();
        try {
            Date today = Calendar.getInstance().getTime();
            oggi = new Timestamp(today.getTime());
        } catch (EJBException e) {
            throw new DetailedRuntimeException(e);
        }

        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone( ZoneId.systemDefault() );
        Batch_log_tstaBulk log = new Batch_log_tstaBulk();
        log.setDs_log("CICF-"+file.getNome_file()+" Start: "+ formatterTime.format(oggi.toInstant()));
        log.setCd_log_tipo(Batch_log_tstaBulk.LOG_TIPO_INTERF_CASS00);
        log.setNote("Caricamento interfaccia ritorno cassiere. File:"+file.getNome_file()+" Utente: "+userContext.getUser());
        log.setToBeCreated();
        try {
            log = (Batch_log_tstaBulk)batchControlComponentSession.creaConBulkRequiresNew(userContext,log);
        } catch (ComponentException | RemoteException e) {
            SendMail.sendErrorMail(subjectError, "Errore durante l'inserimento in Batch_log_tsta "+e.getMessage());
            throw new ComponentException(e);
        }

        Ext_cassiere00_logsBulk cassiere00_logsBulk = new Ext_cassiere00_logsBulk();
        cassiere00_logsBulk.setNome_file(file.getNome_file());
        cassiere00_logsBulk.setEsercizio(file.getEsercizio());
        cassiere00_logsBulk.setPg_esecuzione(log.getPg_esecuzione());
        cassiere00_logsBulk.setToBeCreated();
        try {
            cassiere00_logsBulk = (Ext_cassiere00_logsBulk)batchControlComponentSession.creaConBulkRequiresNew(userContext,cassiere00_logsBulk);
        } catch (ComponentException | RemoteException e) {
            SendMail.sendErrorMail(subjectError, "Errore durante l'inserimento in Ext_cassiere00_logs "+e.getMessage());
            throw new ComponentException(e);
        }

        boolean tesoreriaUnica = false;
        String ccEnte = null;
        EnteBulk cdsEnte = null;
        try {
            tesoreriaUnica = Utility.createParametriCnrComponentSession().getParametriCnr(userContext, file.getEsercizio()).getFl_tesoreria_unica();
            ccEnte = Utility.createConfigurazioneCnrComponentSession().getContoCorrenteEnte(userContext, file.getEsercizio());
            EnteHome aEnteHome = (EnteHome)getHome(userContext,EnteBulk.class);
            List aL = aEnteHome.findAll();
            if(aL.size() > 0){
                cdsEnte = (EnteBulk) aL.get(0);
            } else {
                SendMail.sendErrorMail(subjectError, "Cds Ente non trovato!");
                throw new ComponentException("Cds Ente non trovato!");
            }
        } catch (RemoteException | PersistencyException e) {
            SendMail.sendErrorMail(subjectError, "Errore durante la ricerca della tesoreria unica "+e.getMessage());
            throw new ComponentException(e);
        }

        MovimentoContoEvidenzaHome home = (MovimentoContoEvidenzaHome)getHome(userContext, MovimentoContoEvidenzaBulk.class);
        SospesoRiscontroComponentSession sess = (SospesoRiscontroComponentSession) EJBCommonServices
                .createEJB("CNRDOCCONT00_EJB_SospesoRiscontroComponentSession");
        List righeFile = new ArrayList();
        try {
            righeFile = home.recuperoRigheFile(file.getNome_file(), file.getEsercizio(), MovimentoContoEvidenzaBulk.STATO_RECORD_INIZIALE);
        } catch (IntrospectionException | PersistencyException e) {
            SendMail.sendErrorMail(subjectError, "Errore durante il recupero delle righe del file "+e.getMessage());
            throw new ComponentException(e);
        }

        List listaRigheMovimentoContoEvidenza = (List) righeFile.stream().collect(Collectors.toList());

        int totaleRighe = 0;
        int contaErrori = 0;
        int righeProcessate = 0;
        for (Object bulk : listaRigheMovimentoContoEvidenza){
            MovimentoContoEvidenzaBulk riga = (MovimentoContoEvidenzaBulk)bulk;
            try {
                totaleRighe++;
                righeProcessate = righeProcessate + sess.caricamentoRigaGiornaleCassa(userContext, tesoreriaUnica, cdsEnte, riga);
            } catch (Exception e) {
                contaErrori++;
                Batch_log_rigaBulk log_riga = new Batch_log_rigaBulk();
                log_riga.setPg_esecuzione(log.getPg_esecuzione());
                log_riga.setPg_riga(BigDecimal.valueOf(contaErrori));
                log_riga.setTi_messaggio("E");
                log_riga.setMessaggio(cdsEnte.getCd_unita_organizzativa()+"-"+file.getNome_file()+"-Riga-"+riga.getProgressivo());
                log_riga.setTrace(
                        Optional.ofNullable(log_riga.getMessaggio())
                                .map(s -> s.substring(0, Math.min(s.length(), 4000)))
                                .orElse(null)
                );
                log_riga.setNote(
                        Optional.ofNullable(e.getMessage())
                                .map(s -> s.substring(0, Math.min(s.length(), 4000)))
                                .orElse(null)
                );
                log_riga.setToBeCreated();
                try {
                    log_riga = (Batch_log_rigaBulk)batchControlComponentSession.creaConBulkRequiresNew(userContext,log_riga);
                } catch (ComponentException | RemoteException ex) {
                    SendMail.sendErrorMail(subjectError, "Errore durante l'inserimento dell'errore in Batch_log_rigaBulk "+ex.getMessage());
                    throw new ComponentException(ex);
                }

                Ext_cassiere00_scartiBulk scarto = new Ext_cassiere00_scartiBulk();
                scarto.setEsercizio(file.getEsercizio());
                scarto.setNome_file(file.getNome_file());
                scarto.setPg_esecuzione(log.getPg_esecuzione().longValue());
                scarto.setPg_rec(riga.getProgressivo());
                scarto.setDt_giornaliera(riga.getDataMovimento());

                oggi = null;
                try {
                    Date today = Calendar.getInstance().getTime();
                    oggi = new Timestamp(today.getTime());
                } catch (EJBException ex) {
                    throw new ComponentException(ex);
                }

                scarto.setDt_elaborazione(oggi);
                scarto.setCd_cds(cdsEnte.getCd_unita_organizzativa());
                if (riga.isMandatoReversale()){
                    scarto.setTipo_mov(riga.getTipoMovimento().substring(0,1));
                    scarto.setCd_cds_manrev(scarto.getCd_cds());
                    scarto.setEsercizio_manrev(riga.getEsercizio());
                    scarto.setPg_manrev(riga.getNumeroDocumento().toString());
                } else {
                    scarto.setTipo_mov(Ext_cassiere00_scartiBulk.TIPO_MOVIMENTO_SOSPESO);
                    scarto.setCd_cds_sr(scarto.getCd_cds());
                    scarto.setEsercizio_sr(riga.getEsercizio());
                    scarto.setTi_entrata_spesa_sr(riga.recuperoTipoSospesoEntrataSpesa());
                    scarto.setTi_sospeso_riscontro_sr(riga.isTipoOperazioneStornato() ? SospesoBulk.TI_SOSPESO : SospesoBulk.TI_RISCONTRO);
                    scarto.setCd_sr(riga.recuperoNumeroSospeso());
                }
                scarto.setAnomalia(
                        Optional.ofNullable(e.getMessage())
                                .map(s -> s.substring(0, Math.min(s.length(), 1000)))
                                .orElse(null)
                );
                scarto.setToBeCreated();
                try {
                    scarto = (Ext_cassiere00_scartiBulk)batchControlComponentSession.creaConBulkRequiresNew(userContext,scarto);
                } catch (ComponentException | RemoteException ex) {
                    SendMail.sendErrorMail(subjectError, "Errore durante il recupero dell'errore in Ext_cassiere00_scarti "+ex.getMessage());
                    throw new ComponentException(ex);
                }
            }
        }
        try {
            Date today = Calendar.getInstance().getTime();
            oggi = new Timestamp(today.getTime());
        } catch (EJBException e) {
            throw new ComponentException(e);
        }
        if (contaErrori > 0) {
            log.setFl_errori(true);
            log.setToBeUpdated();
            try {
                super.updateBulk(userContext, log);
            } catch (PersistencyException e) {
                SendMail.sendErrorMail(subjectError, "Errore durante l'aggiornamento di Batch_log_tsta "+e.getMessage());
                throw new ComponentException(e);
            }
        }
        Batch_log_rigaBulk log_riga = new Batch_log_rigaBulk();
        log_riga.setPg_esecuzione(log.getPg_esecuzione());
        log_riga.setPg_riga(BigDecimal.valueOf(contaErrori + 1));
        log_riga.setTi_messaggio("I");
        log_riga.setMessaggio("Caricamento "+cdsEnte.getCd_unita_organizzativa()+"-"+file.getNome_file()+". Righe elaborate: "+totaleRighe+". Righe processate: "+righeProcessate+". Errori: "+contaErrori);
        log_riga.setNote("Termine operazione caricamento interfaccia ritorno cassiere."+ formatterTime.format(oggi.toInstant()));
        log_riga.setToBeCreated();
        try {
            log_riga = (Batch_log_rigaBulk)batchControlComponentSession.creaConBulkRequiresNew(userContext,log_riga);
        } catch (ComponentException | RemoteException ex) {
            SendMail.sendErrorMail(subjectError, "Errore durante l'inserimento della riga di chiusura di Batch_log_riga "+ex.getMessage());
            throw new ComponentException(ex);
        }

        MessaggioBulk messaggio = new MessaggioBulk();
        String ds = "Caricamento interfaccia ritorno cassiere: "+file.getNome_file()+" es."+file.getEsercizio();
        messaggio.setDs_messaggio("ATTN: "+ds);
        messaggio.setCd_utente(userContext.getUser());
        messaggio.setSoggetto(ds+" "+formatterTime.format(oggi.toInstant())+" (log esecuzione: "+log.getPg_esecuzione()+")");
        messaggio.setCorpo("Operazione Completata. Righe elaborate: "+totaleRighe+". Righe processate: "+righeProcessate+". Errori: "+contaErrori);
        messaggio.setPriorita(1);
        messaggio.setToBeCreated();
        try {
            messaggio = (MessaggioBulk)batchControlComponentSession.creaConBulkRequiresNew(userContext,messaggio);
        } catch (ComponentException | RemoteException ex) {
            SendMail.sendErrorMail(subjectError, "Errore durante l'inserimento del Messaggio "+ex.getMessage());
            throw new ComponentException(ex);
        }
    }

    /**
     * Nome: Carica i logs relativi al file selezionato. Pre: E' stata richiesto
     * di inizializzare un oggetto <code>V_ext_cassiere00Bulk</code> per la
     * visualizzazione dei Log relativi ai processi effettuati sul File
     * corrispondente. Post: Viene caricata la lista di processi relativi al
     * File specificato.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param file        <code>V_ext_cassiere00Bulk</code> l'oggetto da inizializzare
     * @return <code>V_ext_cassiere00Bulk<code> l'oggetto inizializzato
     */
    public V_ext_cassiere00Bulk caricaLogs(
            UserContext userContext,
            V_ext_cassiere00Bulk file)
            throws ComponentException {

        SQLBuilder sql = getHome(userContext, Ext_cassiere00_logsBulk.class)
                .createSQLBuilder();

        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, file.getEsercizio());
        sql.addSQLClause("AND", "NOME_FILE", SQLBuilder.EQUALS, file.getNome_file());

        try {
            List risultato = getHome(userContext, Ext_cassiere00_logsBulk.class)
                    .fetchAll(sql);
            if (risultato.size() > 0) {
                file.setLogs(new SimpleBulkList(risultato));
            }
        } catch (PersistencyException pe) {
            throw new ComponentException(pe);
        }

        return file;
    }

    /**
     * Nome: Cerca File Cassiere. Pre: E' stata richiesto di visualizzare tutti
     * i File Ccassiere caricati.
     * <p>
     * Post: Viene restituito un RemoteIterator con il risultato della ricerca.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @return il <code>RemoteIterator</code> della lista dei File Cassiere
     */
    public RemoteIterator cercaFile_Cassiere(
            UserContext userContext, CompoundFindClause user_clauses)
            throws ComponentException {

        SQLBuilder sql = getHome(userContext, V_ext_cassiere00Bulk.class)
                .createSQLBuilder();
        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getEsercizio());
        if (user_clauses != null)
            sql.addClause(user_clauses);

        return iterator(userContext, sql, V_ext_cassiere00Bulk.class, "default");

    }

    /**
     * Esegue una operazione di ricerca di Mandati/Reversali
     * <p>
     * Nome: Cerca mandati e reversali Pre: E' necessario ricercare i mandati e
     * le reversali da cui selezionare quelli da includere in distinta Post:
     * Viene creato un RemoteIterator passandogli le clausole presenti nel
     * SQLBuilder creato dal metodo 'cercaMandatiEReversaliSQL'
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param clausole    le clausole specificate dall'utente
     * @param docPassivo  l'istanza di V_mandato_reversaleBulk con le impostazioni
     *                    specificate dall'utente
     * @param distinta    l'istanza di Distinta_cassiereBulk per cui ricercare i
     *                    mandati/reversali
     * @return RemoteIterator con le istanze di V_mandato_reversaleBulk
     */
    public RemoteIterator cercaMandatiEReversali(
            UserContext userContext,
            CompoundFindClause clausole,
            V_mandato_reversaleBulk docPassivo, Distinta_cassiereBulk distinta)
            throws ComponentException {
        try {

            SQLQuery sql = cercaMandatiEReversaliSQL(userContext, clausole,
                    docPassivo, distinta);
            return iterator(userContext, sql, V_mandato_reversaleBulk.class,
                    null);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Esegue una operazione di creazione del SQLBuilder per ricercare
     * Mandati/Reversali
     * <p>
     * Nome: Cerca mandati e reversali Pre: E' necessario creare il SQLBuilder
     * per ricercare i mandati e le reversali da cui selezionare quelli da
     * includere in distinta Post: Viene generato il SQLBuilder con le clausole
     * specificate dall'utente ed inoltre le clausole che il cds di appartenza
     * sia uguale al cds di scrivania, lo stato di trasmissione sia NON INSERITO
     * IN DISTINTA, il tipo doc. contabile sia diverso da REGOLARIZZAZIONE, e il
     * doc. contabile non dipenda da altri doc. contabili.
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param clausole    le clausole specificate dall'utente
     * @param docPassivo  l'istanza di V_mandato_reversaleBulk con le impostazioni
     *                    specificate dall'utente
     * @param distinta    l'istanza di Distinta_cassiereBulk per cui ricercare i
     *                    mandati/reversali
     * @return SQLBuilder con tutte le clausole
     */
    private SQLQuery cercaMandatiEReversaliSQL(UserContext userContext,
                                               CompoundFindClause clausole,
                                               V_mandato_reversaleBulk docPassivo, Distinta_cassiereBulk distinta)
            throws ComponentException {
        try {
            if (distinta.getFl_flusso()) {

                SQLBuilder sql = getHome(userContext,
                        V_mandato_reversaleBulk.class,
                        "V_MANDATO_REVERSALE_DIST_XML").createSQLBuilder();
                sql.addClause(clausole);
                sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.esercizio", SQLBuilder.EQUALS,
                        ((CNRUserContext) userContext).getEsercizio());
                // Da condizionare 02/12/2015
                if (!tesoreriaUnica(userContext, distinta)) {
                    sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.cd_cds", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getCd_cds());
                    sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.stato_trasmissione", SQLBuilder.EQUALS,
                            MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
                } else {
                    sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.dt_firma", SQLBuilder.ISNOTNULL, null);
                    sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.stato_trasmissione", SQLBuilder.EQUALS,
                            MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
                }
                sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.ti_documento_cont", SQLBuilder.NOT_EQUALS,
                        MandatoBulk.TIPO_REGOLARIZZAZIONE);
                sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.stato", SQLBuilder.NOT_EQUALS,
                        MandatoBulk.STATO_MANDATO_ANNULLATO);
                sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.stato_var_sos", SQLBuilder.ISNULL, null);
                if (isInserisciMandatiVersamentoCori(userContext)) {
                    sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.versamento_cori = 'N'");
                }
                sql.addSQLJoin("V_MANDATO_REVERSALE_DIST_XML.CD_TIPO_DOCUMENTO_CONT_PADRE", "V_MANDATO_REVERSALE_DIST_XML.CD_TIPO_DOCUMENTO_CONT");
                sql.addSQLJoin("V_MANDATO_REVERSALE_DIST_XML.PG_DOCUMENTO_CONT_PADRE", "V_MANDATO_REVERSALE_DIST_XML.PG_DOCUMENTO_CONT");
                if (Utility.createParametriCnrComponentSession().getParametriCnr(
                        userContext, docPassivo.getEsercizio()).getFl_siope()
                        .booleanValue()) {
                    Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(
                            userContext, Unita_organizzativa_enteBulk.class)
                            .findAll().get(0);
                    if (!((CNRUserContext) userContext).getCd_cds().equals(
                            ente.getUnita_padre().getCd_unita_organizzativa()))
                        sql.addSQLClause("AND", "v_mandato_reversale_dist_xml.ti_documento_cont",
                                SQLBuilder.NOT_EQUALS,
                                MandatoBulk.TIPO_ACCREDITAMENTO);
                }

                if (docPassivo != null) // (1) clausole sull'esercizio,
                    // cd_unita_organizzativa + clausole
                    // dell'utente
                    sql.addClause(docPassivo.buildFindClauses(null));
                // sql.addOrderBy(
                // "cd_tipo_documento_cont, ti_documento_cont, pg_documento_cont" );

                SQLUnion union;

                // MARIO - condizione che aggiunge in ogni caso, a prescindere dalla
                // selezione effettuata
                // i mandati di versamento CORI/IVA, ma solo se i parametri sono
                // impostati in tal senso
                if (isInserisciMandatiVersamentoCori(userContext)) {
                    SQLBuilder sql2 = getHome(userContext,
                            V_mandato_reversaleBulk.class,
                            "V_MANDATO_REVERSALE_DIST_XML").createSQLBuilder();
                    sql2.addClause(clausole);
                    sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.esercizio", SQLBuilder.EQUALS,
                            ((CNRUserContext) userContext).getEsercizio());
                    sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.cd_unita_organizzativa",
                            SQLBuilder.EQUALS, docPassivo
                                    .getCd_unita_organizzativa());
                    sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.cd_tipo_documento_cont",
                            SQLBuilder.EQUALS, "MAN");

                    // Da condizionare 02/12/2015
                    if (!tesoreriaUnica(userContext, distinta)) {
                        sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.cd_cds", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getCd_cds());
                        sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.stato_trasmissione", SQLBuilder.EQUALS,
                                MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
                    } else {
                        sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.dt_firma", SQLBuilder.ISNOTNULL, null);
                        sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.stato_trasmissione", SQLBuilder.EQUALS, MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
                    }
                    sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.ti_documento_cont",
                            SQLBuilder.NOT_EQUALS,
                            MandatoBulk.TIPO_REGOLARIZZAZIONE);
                    sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.stato", SQLBuilder.NOT_EQUALS,
                            MandatoBulk.STATO_MANDATO_ANNULLATO);
                    sql2.addSQLClause("AND", "v_mandato_reversale_dist_xml.versamento_cori = 'S'");
                    sql2.addSQLJoin("V_MANDATO_REVERSALE_DIST_XML.CD_TIPO_DOCUMENTO_CONT_PADRE", "V_MANDATO_REVERSALE_DIST_XML.CD_TIPO_DOCUMENTO_CONT");
                    sql2.addSQLJoin("V_MANDATO_REVERSALE_DIST_XML.PG_DOCUMENTO_CONT_PADRE", "V_MANDATO_REVERSALE_DIST_XML.PG_DOCUMENTO_CONT");

                    union = sql2.union(sql, true);
                    return union;
                } else {
                    return sql;
                }
            } else if (distinta.getFl_sepa()) {  //no flusso
                SQLBuilder sql = getHome(userContext,
                        V_mandato_reversaleBulk.class,
                        "V_MANDATO_REVERSALE_DIST_SEPA").createSQLBuilder();
                sql.addClause(clausole);
                sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.esercizio", SQLBuilder.EQUALS,
                        ((CNRUserContext) userContext).getEsercizio());
                // Da condizionare 02/12/2015
                if (!tesoreriaUnica(userContext, distinta)) {
                    sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.cd_cds", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getCd_cds());
                    sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.stato_trasmissione", SQLBuilder.EQUALS,
                            MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
                } else {
                    sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.dt_firma", SQLBuilder.ISNOTNULL, null);
                    sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.stato_trasmissione", SQLBuilder.EQUALS,
                            MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
                }
                sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.ti_documento_cont", SQLBuilder.NOT_EQUALS,
                        MandatoBulk.TIPO_REGOLARIZZAZIONE);
                sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.stato", SQLBuilder.NOT_EQUALS,
                        MandatoBulk.STATO_MANDATO_ANNULLATO);
                sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.stato_var_sos", SQLBuilder.ISNULL, null);

                sql.addSQLJoin("v_mandato_reversale_dist_sepa.CD_TIPO_DOCUMENTO_CONT_PADRE", "v_mandato_reversale_dist_sepa.CD_TIPO_DOCUMENTO_CONT");
                sql.addSQLJoin("v_mandato_reversale_dist_sepa.PG_DOCUMENTO_CONT_PADRE", "v_mandato_reversale_dist_sepa.PG_DOCUMENTO_CONT");
                if (Utility.createParametriCnrComponentSession().getParametriCnr(
                        userContext, docPassivo.getEsercizio()).getFl_siope()
                        .booleanValue()) {
                    Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(
                            userContext, Unita_organizzativa_enteBulk.class)
                            .findAll().get(0);
                    if (!((CNRUserContext) userContext).getCd_cds().equals(
                            ente.getUnita_padre().getCd_unita_organizzativa()))
                        sql.addSQLClause("AND", "v_mandato_reversale_dist_sepa.ti_documento_cont",
                                SQLBuilder.NOT_EQUALS,
                                MandatoBulk.TIPO_ACCREDITAMENTO);
                }
                if (docPassivo != null) // (1) clausole sull'esercizio,
                    // cd_unita_organizzativa + clausole
                    // dell'utente
                    sql.addClause(docPassivo.buildFindClauses(null));
                return sql;
            } else if (distinta.getFl_annulli()) {  //annulli
                SQLBuilder sql = getHome(userContext, V_mandato_reversaleBulk.class,"V_MANDATO_REVERSALE_DIST_ANN").createSQLBuilder();
                sql.addClause(clausole);
                sql.addSQLClause("AND", "v_mandato_reversale_dist_ann.esercizio", SQLBuilder.EQUALS,
                        ((CNRUserContext) userContext).getEsercizio());
                // Da condizionare 02/12/2015
                if (!tesoreriaUnica(userContext, distinta)) {
                    sql.addSQLClause("AND", "v_mandato_reversale_dist_ann.cd_cds", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getCd_cds());
                    sql.addSQLClause("AND", "v_mandato_reversale_dist_ann.stato_trasmissione", SQLBuilder.EQUALS,
                            MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
                } else {
                    sql.addSQLClause("AND", "v_mandato_reversale_dist_ann.dt_firma", SQLBuilder.ISNOTNULL, null);
                    sql.openParenthesis(FindClause.AND);
                        sql.addSQLClause("AND", "v_mandato_reversale_dist_ann.stato_trasmissione", SQLBuilder.EQUALS,
                            MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
                        sql.addSQLClause(FindClause.OR, "v_mandato_reversale_dist_ann.esito_operazione", SQLBuilder.EQUALS, EsitoOperazione.NON_ACQUISITO.value());
                    sql.closeParenthesis();
                }
                sql.addSQLClause("AND", "v_mandato_reversale_dist_ann.ti_documento_cont", SQLBuilder.NOT_EQUALS,
                        MandatoBulk.TIPO_REGOLARIZZAZIONE);
                sql.openParenthesis(FindClause.AND);
                    sql.addSQLClause(FindClause.AND, "v_mandato_reversale_dist_ann.stato_var_sos", SQLBuilder.NOT_EQUALS,
                        StatoVariazioneSostituzione.ANNULLATO_PER_SOSTITUZIONE.value());
                    sql.addSQLClause(FindClause.OR, "v_mandato_reversale_dist_ann.stato_var_sos", SQLBuilder.ISNULL, null);
                sql.closeParenthesis();
                sql.addSQLJoin("v_mandato_reversale_dist_ann.CD_TIPO_DOCUMENTO_CONT_PADRE", "v_mandato_reversale_dist_ann.CD_TIPO_DOCUMENTO_CONT");
                sql.addSQLJoin("v_mandato_reversale_dist_ann.PG_DOCUMENTO_CONT_PADRE", "v_mandato_reversale_dist_ann.PG_DOCUMENTO_CONT");
                if (Utility.createParametriCnrComponentSession().getParametriCnr(
                        userContext, docPassivo.getEsercizio()).getFl_siope()
                        .booleanValue()) {
                    Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(
                            userContext, Unita_organizzativa_enteBulk.class)
                            .findAll().get(0);
                    if (!((CNRUserContext) userContext).getCd_cds().equals(
                            ente.getUnita_padre().getCd_unita_organizzativa()))
                        sql.addSQLClause("AND", "v_mandato_reversale_dist_ann.ti_documento_cont",
                                SQLBuilder.NOT_EQUALS,
                                MandatoBulk.TIPO_ACCREDITAMENTO);
                }
                if (docPassivo != null) // (1) clausole sull'esercizio,
                    // cd_unita_organizzativa + clausole
                    // dell'utente
                    sql.addClause(docPassivo.buildFindClauses(null));
                return sql;
            } else  //no flusso e no sepa e no annulli
            {
                SQLBuilder sql = getHome(userContext,
                        V_mandato_reversaleBulk.class,
                        "V_MANDATO_REVERSALE_DISTINTA").createSQLBuilder();
                sql.addClause(clausole);
                sql.addSQLClause("AND", "v_mandato_reversale_distinta.esercizio", SQLBuilder.EQUALS,
                        ((CNRUserContext) userContext).getEsercizio());
                // Da condizionare 02/12/2015
                if (!tesoreriaUnica(userContext, distinta)) {
                    sql.addSQLClause("AND", "v_mandato_reversale_distinta.cd_cds", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getCd_cds());
                    sql.addSQLClause("AND", "v_mandato_reversale_distinta.stato_trasmissione", SQLBuilder.EQUALS,
                            MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
                } else {
                    sql.addSQLClause("AND", "v_mandato_reversale_distinta.dt_firma", SQLBuilder.ISNOTNULL, null);
                    sql.addSQLClause("AND", "v_mandato_reversale_distinta.stato_trasmissione", SQLBuilder.EQUALS,
                            MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
                    if (isAttivoSiopeplus(userContext)) {
                        SQLBuilder sql2 = getHome(userContext, V_mandato_reversaleBulk.class,
                                "V_MANDATO_REVERSALE_DIST_SEPA").createSQLBuilder();
                        sql2.addSQLClause("AND", "V_MANDATO_REVERSALE_DIST_SEPA.esercizio", SQLBuilder.EQUALS,
                                ((CNRUserContext) userContext).getEsercizio());
                        sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO",
                                "V_MANDATO_REVERSALE_DIST_SEPA.ESERCIZIO");
                        sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_CDS",
                                "V_MANDATO_REVERSALE_DIST_SEPA.CD_CDS");
                        sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
                                "V_MANDATO_REVERSALE_DIST_SEPA.CD_TIPO_DOCUMENTO_CONT");
                        sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT",
                                "V_MANDATO_REVERSALE_DIST_SEPA.PG_DOCUMENTO_CONT");
                        sql.addSQLNotExistsClause("AND", sql2);

                        SQLBuilder sql3 = getHome(userContext, V_mandato_reversaleBulk.class,
                                "V_MANDATO_REVERSALE_DIST_XML").createSQLBuilder();
                        sql3.addSQLClause("AND", "V_MANDATO_REVERSALE_DIST_XML.esercizio", SQLBuilder.EQUALS,
                                ((CNRUserContext) userContext).getEsercizio());
                        sql3.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO",
                                "V_MANDATO_REVERSALE_DIST_XML.ESERCIZIO");
                        sql3.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_CDS",
                                "V_MANDATO_REVERSALE_DIST_XML.CD_CDS");
                        sql3.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
                                "V_MANDATO_REVERSALE_DIST_XML.CD_TIPO_DOCUMENTO_CONT");
                        sql3.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT",
                                "V_MANDATO_REVERSALE_DIST_XML.PG_DOCUMENTO_CONT");

                        // Aggiunto per query troppo lenta
                        if (!tesoreriaUnica(userContext, distinta)) {
                            sql3.addSQLClause("AND", "v_mandato_reversale_dist_xml.cd_cds", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getCd_cds());
                            sql3.addSQLClause("AND", "v_mandato_reversale_dist_xml.stato_trasmissione", SQLBuilder.EQUALS,
                                    MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
                        } else {
                            sql3.addSQLClause("AND", "v_mandato_reversale_dist_xml.dt_firma", SQLBuilder.ISNOTNULL, null);
                            sql3.addSQLClause("AND", "v_mandato_reversale_dist_xml.stato_trasmissione", SQLBuilder.EQUALS,
                                    MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
                        }
                        sql3.addSQLClause("AND", "v_mandato_reversale_dist_xml.ti_documento_cont", SQLBuilder.NOT_EQUALS,
                                MandatoBulk.TIPO_REGOLARIZZAZIONE);
                        sql3.addSQLClause("AND", "v_mandato_reversale_dist_xml.stato", SQLBuilder.NOT_EQUALS,
                                MandatoBulk.STATO_MANDATO_ANNULLATO);
                        sql3.addSQLJoin("V_MANDATO_REVERSALE_DIST_XML.CD_TIPO_DOCUMENTO_CONT_PADRE", "V_MANDATO_REVERSALE_DIST_XML.CD_TIPO_DOCUMENTO_CONT");
                        sql3.addSQLJoin("V_MANDATO_REVERSALE_DIST_XML.PG_DOCUMENTO_CONT_PADRE", "V_MANDATO_REVERSALE_DIST_XML.PG_DOCUMENTO_CONT");


                        sql.addSQLNotExistsClause("AND", sql3);

                        SQLBuilder sql4 = getHome(userContext, V_mandato_reversaleBulk.class,
                                "V_MANDATO_REVERSALE_DIST_ANN").createSQLBuilder();
                        sql4.addSQLClause("AND", "V_MANDATO_REVERSALE_DIST_ANN.esercizio", SQLBuilder.EQUALS,
                                ((CNRUserContext) userContext).getEsercizio());
                        sql4.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO",
                                "V_MANDATO_REVERSALE_DIST_ANN.ESERCIZIO");
                        sql4.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_CDS",
                                "V_MANDATO_REVERSALE_DIST_ANN.CD_CDS");
                        sql4.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
                                "V_MANDATO_REVERSALE_DIST_ANN.CD_TIPO_DOCUMENTO_CONT");
                        sql4.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT",
                                "V_MANDATO_REVERSALE_DIST_ANN.PG_DOCUMENTO_CONT");
                        sql.addSQLNotExistsClause("AND", sql4);
                    }
                }
                sql.addSQLClause("AND", "v_mandato_reversale_distinta.ti_documento_cont", SQLBuilder.NOT_EQUALS,
                        MandatoBulk.TIPO_REGOLARIZZAZIONE);
                sql.addSQLClause("AND", "v_mandato_reversale_distinta.stato", SQLBuilder.NOT_EQUALS,
                        MandatoBulk.STATO_MANDATO_ANNULLATO);
                sql.addSQLClause("AND", "v_mandato_reversale_distinta.stato_var_sos", SQLBuilder.ISNULL, null);

                if (isInserisciMandatiVersamentoCori(userContext)) {
                    sql.addSQLClause("AND", "v_mandato_reversale_distinta.versamento_cori = 'N'");
                }
                sql.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT_PADRE", "V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT");
                sql.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT_PADRE", "V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT");

                if (Utility.createParametriCnrComponentSession().getParametriCnr(
                        userContext, docPassivo.getEsercizio()).getFl_siope()
                        .booleanValue()) {
                    Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(
                            userContext, Unita_organizzativa_enteBulk.class)
                            .findAll().get(0);
                    if (!((CNRUserContext) userContext).getCd_cds().equals(
                            ente.getUnita_padre().getCd_unita_organizzativa()))
                        sql.addSQLClause("AND", "v_mandato_reversale_distinta.ti_documento_cont",
                                SQLBuilder.NOT_EQUALS,
                                MandatoBulk.TIPO_ACCREDITAMENTO);
                }

                if (docPassivo != null) // (1) clausole sull'esercizio,
                    // cd_unita_organizzativa + clausole
                    // dell'utente
                    sql.addClause(docPassivo.buildFindClauses(null));
                // sql.addOrderBy(
                // "cd_tipo_documento_cont, ti_documento_cont, pg_documento_cont" );

                SQLUnion union;

                // MARIO - condizione che aggiunge in ogni caso, a prescindere dalla
                // selezione effettuata
                // i mandati di versamento CORI/IVA, ma solo se i parametri sono
                // impostati in tal senso
                if (isInserisciMandatiVersamentoCori(userContext)) {
                    SQLBuilder sql2 = getHome(userContext,
                            V_mandato_reversaleBulk.class,
                            "V_MANDATO_REVERSALE_DISTINTA").createSQLBuilder();
                    sql2.addClause(clausole);
                    sql2.addSQLClause("AND", "v_mandato_reversale_distinta.esercizio", SQLBuilder.EQUALS,
                            ((CNRUserContext) userContext).getEsercizio());
                    sql2.addSQLClause("AND", "v_mandato_reversale_distinta.cd_unita_organizzativa",
                            SQLBuilder.EQUALS, docPassivo
                                    .getCd_unita_organizzativa());
                    sql2.addSQLClause("AND", "v_mandato_reversale_distinta.cd_tipo_documento_cont",
                            SQLBuilder.EQUALS, "MAN");

                    // Da condizionare 02/12/2015
                    if (!tesoreriaUnica(userContext, distinta)) {
                        sql2.addSQLClause("AND", "v_mandato_reversale_distinta.cd_cds", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getCd_cds());
                        sql2.addSQLClause("AND", "v_mandato_reversale_distinta.stato_trasmissione", SQLBuilder.EQUALS,
                                MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
                    } else {
                        sql2.addSQLClause("AND", "v_mandato_reversale_distinta.dt_firma", SQLBuilder.ISNOTNULL, null);
                        sql2.addSQLClause("AND", "v_mandato_reversale_distinta.stato_trasmissione", SQLBuilder.EQUALS,
                                MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
                    }
                    sql2.addSQLClause("AND", "v_mandato_reversale_distinta.ti_documento_cont",
                            SQLBuilder.NOT_EQUALS,
                            MandatoBulk.TIPO_REGOLARIZZAZIONE);
                    sql2.addSQLClause("AND", "v_mandato_reversale_distinta.stato", SQLBuilder.NOT_EQUALS,
                            MandatoBulk.STATO_MANDATO_ANNULLATO);
                    sql2.addSQLClause("AND", "v_mandato_reversale_distinta.versamento_cori = 'S'");
                    sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT_PADRE", "V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT");
                    sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT_PADRE", "V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT");
                    union = sql2.union(sql, true);
                    return union;
                } else {
                    return sql;
                }
            }
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Elimina un dettaglio di distinta
     * <p>
     * Nome: elimina dettaglio Pre: E' stata generata la richiesta di eliminare
     * un dettaglio di distinta relativo ad un documento contabile (mandato o
     * reversale) Post: Il dettaglio e' stato cancellato e lo stato_trasmissione
     * del doc. contabile associato a tale dettaglio viene aggiornato a NON
     * INSERITO IN DISTINTA
     *
     * @param userContext  lo UserContext che ha generato la richiesta
     * @param distinta     la Distinta_cassiereBulk per cui cancellare il dettaglio
     * @param docContabile il mandato/reversale da cancellare dalla distinta
     */
    public void eliminaDettaglioDistinta(UserContext userContext,
                                         Distinta_cassiereBulk distinta, V_mandato_reversaleBulk docContabile)
            throws ComponentException {
        try {
            if (tesoreriaUnica(userContext, distinta))
                aggiornaStatoDocContabile(userContext, docContabile,
                        MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
            else
                aggiornaStatoDocContabile(userContext, docContabile,
                        MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
            String schema = EJBCommonServices
                    .getDefaultSchema();
            LoggableStatement ps = null;
            if (docContabile.isMandato()) {
                ps = new LoggableStatement(getConnection(userContext),
                        "DELETE FROM " + schema + "DISTINTA_CASSIERE_DET "
                                + "WHERE ESERCIZIO = ? AND "
                                + "CD_CDS = ? AND "
                                + "CD_UNITA_ORGANIZZATIVA = ? AND "
                                + "PG_DISTINTA = ? AND " + "PG_MANDATO = ? ",
                        true, this.getClass());
            } else if (docContabile.isReversale()) {
                ps = new LoggableStatement(getConnection(userContext),
                        "DELETE FROM " + schema + "DISTINTA_CASSIERE_DET "
                                + "WHERE ESERCIZIO = ? AND "
                                + "CD_CDS = ? AND "
                                + "CD_UNITA_ORGANIZZATIVA = ? AND "
                                + "PG_DISTINTA = ? AND " + "PG_REVERSALE = ? ",
                        true, this.getClass());
            }
            try {
                ps.setObject(1, distinta.getEsercizio());
                ps.setString(2, distinta.getCd_cds());
                ps.setString(3, distinta.getCd_unita_organizzativa());
                ps.setObject(4, distinta.getPg_distinta());
                ps.setObject(5, docContabile.getPg_documento_cont());
                ps.execute();
            } finally {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Elimina un insieme di dettagli dalla distinta
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Elimina dettagli Pre: Una richiesta di eliminare alcuni dettagli da
     * una distinta non ancora trasmessa al cassiere e' stata generata Post:
     * Ogni dettaglio per cui e' stata richiesta la cancellazione viene
     * eliminato dalla distinta e lo stato trasmissione del documento contabile
     * ad esso associato viene aggiornato a NON INSERITO IN DISTINTA; se tale
     * documento aveva altri documenti contabili collegati anche questi
     * documenti vengono eliminati dalla distinta ed il loro stato trasmissione
     * aggiornato.
     *
     * @param userContext  lo UserContext che ha generato la richiesta
     * @param docContabili l'array di V_mandato_reversaleBulk da cancellare dalla
     *                     distinta
     * @param distinta     la Distinta_cassiereBulk per cui eliminare i dettagli
     */

    public void eliminaDistinta_cassiere_detCollConBulk(
            UserContext userContext,
            OggettoBulk[] docContabili,
            Distinta_cassiereBulk distinta) throws ComponentException {
        try {
            for (int i = 0; i < docContabili.length; i++) {
                V_mandato_reversaleBulk docContabile = (V_mandato_reversaleBulk) docContabili[i];
                // aggiornaStatoDocContabile( userContext, docContabile,
                // MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO );
                eliminaDettaglioDistinta(userContext, distinta, docContabile);
                eliminaMandatiEReversaliCollegati(userContext, distinta,
                        docContabile);
                eliminaDettaglioDistinteCollegate(userContext, distinta,
                        docContabile);
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Elimina tutti i dettagli di una distinta
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Elimina tutti dettagli Pre: Una richiesta di eliminare una distinta
     * non ancora trasmessa al cassiere e' stata generata ed e' pertanto
     * necessario eliminare tutti i suoi dettagli Post: Ogni dettaglio della
     * distinta viene eliminato e lo stato trasmissione del documento contabile
     * ad esso associato viene aggiornato a NON INSERITO IN DISTINTA
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinta    la Distinta_cassiereBulk per cui eliminare tutti i dettagli
     */
    public void eliminaDistinta_cassiere_detCollConBulk(
            UserContext userContext, Distinta_cassiereBulk distinta)
            throws ComponentException {
        try {
            eliminaTuttiDettagliDistinteCollegate(userContext, distinta);
            eliminaTuttiDettagliDistinta(userContext, distinta);
            distinta.resetTotali();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Nome: Elimina Doc.Contabile Padre Pre: E' stata richiesta la
     * cancellazione di un dettaglio di una distinta relativo ad un doc.
     * contabile da cui dipendono altri doc. contabili; Post: Per tutti i doc.
     * contabili figli di quello da cancellare viene richiamato il metodo
     * 'eliminaDettaglioDistinta' che procede all'eliminazione del dettaglio
     * dalla distinta e all'aggiornamento dello stato_trasmissione del doc.
     * contabile collegato
     * <p>
     * Nome: Elimina Doc.Contabile Figlio Pre: E' stata richiesta la
     * cancellazione di un dettaglio di una distinta relativo ad un doc.
     * contabile che dipende da un altro doc. contabile; Post: Viene recuperato
     * il doc. conatabile padre rispetto a quello da cancellare e tutti i suoi
     * figli e per ognuno di questi documenti viene richiamato il metodo
     * 'eliminaDettaglioDistinta' che procede all'eliminazione del dettaglio
     * dalla distinta e all'aggiornamento dello stato_trasmissione del doc.
     * contabile collegato
     *
     * @param userContext  lo <code>UserContext</code> che ha generato la richiesta
     * @param distinta     <code>OggettoBulk</code> la distinta i cui dettagli sono da
     *                     cancellare
     * @param docContabile <code>V_mandato_reversaleBulk</code> il doc.contabile per cui
     *                     ricercare i doc. contabili collegati
     */

    protected void eliminaMandatiEReversaliCollegati(UserContext userContext,
                                                     Distinta_cassiereBulk distinta, V_mandato_reversaleBulk docContabile)
            throws PersistencyException, ComponentException {
        Collection docContabili;
        V_mandato_reversaleHome home = (V_mandato_reversaleHome) getHome(
                userContext, V_mandato_reversaleBulk.class);
        V_mandato_reversaleBulk docContabilePadre;

        if (docContabile.getPg_documento_cont_padre().compareTo(
                docContabile.getPg_documento_cont()) == 0)
            // si tratta di un padre, seleziona tutti i figli
            docContabili = home.findDocContabiliCollegati(docContabile);
        else // si tratta di un figlio, seleziona il padre + tutti gli altri
        // figli
        {
            docContabilePadre = home.findDocContabilePadre(docContabile);
            docContabili = home.findDocContabiliCollegatiEccetto(docContabile);
            docContabili.add(docContabilePadre);
        }
        for (Iterator i = docContabili.iterator(); i.hasNext(); ) {
            V_mandato_reversaleBulk vManRev = (V_mandato_reversaleBulk) i
                    .next();
            eliminaDettaglioDistinta(userContext, distinta, vManRev);
            eliminaDettaglioDistinteCollegate(userContext, distinta, vManRev);
        }
    }

    /**
     * cancellazione dettagli distinta PreCondition: E' stata richiesta la
     * cancellazione di tutti i dettagli di una istanza di Distinta_cassiereBulk
     * PostCondition: Lo stato_trasmissione dei doc.contabili associati ai
     * dettagli della distinta viene aggiornato a NON INSERITO IN DISTINTA
     * (metodo 'aggiornaStatoDocContabili'). Tutti i dettagli della distinta
     * vengono fisicamente rimossi dal database
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param distinta    <code>OggettoBulk</code> la distinta i cui dettagli sono da
     *                    cancellare
     */
    public void eliminaTuttiDettagliDistinta(
            UserContext userContext, Distinta_cassiereBulk distinta)
            throws ComponentException {
        try {
            if (tesoreriaUnica(userContext, distinta))
                aggiornaStatoDocContabili(userContext, distinta,
                        MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
            else
                aggiornaStatoDocContabili(userContext, distinta,
                        MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
            String schema = EJBCommonServices
                    .getDefaultSchema();
            LoggableStatement ps = null;
            ps = new LoggableStatement(getConnection(userContext),
                    "DELETE FROM " + schema + "DISTINTA_CASSIERE_DET "
                            + "WHERE ESERCIZIO = ? AND " + "CD_CDS = ? AND "
                            + "CD_UNITA_ORGANIZZATIVA = ? AND "
                            + "PG_DISTINTA = ? ", true, this.getClass());
            try {
                ps.setObject(1, distinta.getEsercizio());
                ps.setString(2, distinta.getCd_cds());
                ps.setString(3, distinta.getCd_unita_organizzativa());
                ps.setObject(4, distinta.getPg_distinta());
                ps.executeUpdate();
            } finally {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Nome: Creazione di una Distinta_cassiereBulk Pre: E' stata richiesta la
     * creazione di una istanza di Distinta_cassiereBulk Post: La distinta, che
     * era già stata in precedenza inserita nel database ( metodo
     * 'inizializzaBulkPerInserimento'), viene aggiornata.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> la distinta da cancellare
     */

    protected OggettoBulk eseguiCreaConBulk(UserContext userContext,
                                            OggettoBulk bulk) throws ComponentException,
            PersistencyException {
        bulk.setCrudStatus(OggettoBulk.TO_BE_UPDATED);
        return modificaConBulk(userContext, bulk);
    }

    /**
     * Cancellazione di una istanza di Distinta_cassiereBulk
     * <p>
     * Nome: Esegui cancellazione Pre: E' stata richiesta la cancellazione di
     * una istanza di Distinta_cassiereBulk che non e' ancora stata inviata al
     * cassiere Post: Tutti i dettagli della distinta sono stati eliminati e lo
     * stato dei doc. contabili inseriti nella distinta viene riportato a NON
     * INSERITO IN DISTINTA (metodo 'eliminaTuttiDettagliDistinta'). La distinta
     * viene cancellata
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> la distinta da cancellare
     */

    protected void eseguiEliminaConBulk(UserContext userContext,
                                        OggettoBulk bulk) throws ComponentException, PersistencyException {
        try {
            Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) bulk;
            eliminaTuttiDettagliDistinteCollegate(userContext, distinta);
            eliminaTuttiDettagliDistinta(userContext, distinta);
            deleteBulk(userContext, distinta);
        } catch (NotDeletableException e) {
            if (e.getPersistent() != bulk)
                throw handleException(e);
            throw new CRUDNotDeletableException("Oggetto non eliminabile", e);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Modifica di una istanza di Distinta_cassiereBulk Nome: Esegui modifica
     * distinta Pre: E' stata richiesta la modifica di una istanza di
     * Distinta_cassiereBulk che ha superato la validazione Post: La distinta e'
     * stata modificata
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> la distinta da modificare
     * @return la distinta modificata
     */
    protected OggettoBulk eseguiModificaConBulk(UserContext userContext,
                                                OggettoBulk bulk) throws ComponentException,
            PersistencyException {
        Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) bulk;
        try {
            makeBulkPersistent(userContext, distinta);
            return distinta;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Inizializzazione di una istanza di Distinta_cassiereBulk per inserimento
     * <p>
     * Nome: Inizializzazione per inserimento Pre: E' stata richiesta
     * l'inizializzazione di una istanza di Distinta_cassiereBulk per
     * inserimento Post: Viene inizializzata la distinta, impostando come Cds
     * quello di scrivania e come data di emissione la data odierna; vengono
     * impostati a 0 tutti i totali dei mandati/reversali presenti in distinta;
     * viene assegnato il progressivo distinta ( metodo 'assegnaProgressivo');
     * vengono calcolati gli storici degli importi di mandati/reversali già
     * trasmessi al cassiere (metodo 'calcolaTotaliStorici'); viene recuperato
     * il codice del Cds Ente (999). La distinta viene inserita nel database.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> la distinta da inizializzare per
     *                    inserimento
     * @return la distinta inizializzata per l'inserimento
     */

    public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,
                                                     OggettoBulk bulk) throws ComponentException {
        try {
            verificaStatoEsercizio(userContext);

            // questa inizializzazione è necessaria per motivi prestazionali
            // serve per preimpostare il terzo per i versamenti CORI accentrati
            // in base all'anno di esercizio di scrivania
            callCercaTerzoVersCORI(userContext,
                    CNRUserContext
                            .getEsercizio(userContext));

            bulk = super.inizializzaBulkPerInserimento(userContext, bulk);
            if (bulk instanceof Distinta_cassiereBulk) {
                Distinta_cassiereBulk distinta = inizializzaDistintaPerInserimento(
                        userContext, (Distinta_cassiereBulk) bulk,
                        (CdsBulk) getHome(userContext, CdsBulk.class)
                                .findByPrimaryKey(
                                        new CdsBulk(
                                                ((CNRUserContext) userContext)
                                                        .getCd_cds())));
                /*
                 * Distinta_cassiereBulk distinta = (Distinta_cassiereBulk)
                 * bulk; distinta.setCds( (CdsBulk) getHome( userContext,
                 * CdsBulk.class ).findByPrimaryKey( new CdsBulk(
                 * ((CNRUserContext) userContext).getCd_cds())));
                 * lockUltimaDistinta( userContext, distinta ); //
                 * distinta.setDt_emissione( getHome( userContext,
                 * distinta.getClass()).getServerTimestamp()); // imposto la
                 * data di emissione in modo da averla nel seguente formato:
                 * gg/mm/aaaa distinta.setDt_emissione(
                 * DateServices.getDt_valida( userContext) ); // inizializzo i
                 * totali dei trasmessi distinta = calcolaTotaliStorici(
                 * userContext, distinta ); distinta.setIm_man_ini_pag( new
                 * BigDecimal(0) ); distinta.setIm_man_ini_sos( new
                 * BigDecimal(0) ); distinta.setIm_man_ini_acc( new
                 * BigDecimal(0) ); distinta.setIm_rev_ini_sos( new
                 * BigDecimal(0) ); distinta.setIm_rev_ini_tra( new
                 * BigDecimal(0) ); distinta.setIm_rev_ini_rit( new
                 * BigDecimal(0) );
                 *
                 * assegnaProgressivo( userContext, distinta );
                 *
                 * EnteBulk ente = (EnteBulk) getHome( userContext,
                 * EnteBulk.class).findAll().get(0); distinta.setCd_cds_ente(
                 * ente.getCd_unita_organizzativa());
                 */
                insertBulk(userContext, distinta);
            }
            return bulk;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Inizializzazione di una istanza di Distinta_cassiereBulk per modifica
     * <p>
     * Nome: Inizializzazione per modifica Pre: E' stata richiesta
     * l'inizializzazione di una istanza di Distinta_cassiereBulk per modifica
     * Post: Viene inizializzato la distinta, calcolati i totali dei
     * mandati/reversali presenti in distinta (suddivivisi per tipologia)
     * (metodo 'calcolaTotali'), vengono calcolati gli storici degli importi di
     * mandati/reversali già trasmessi al cassiere (metodo
     * 'calcolaTotaliStorici') e viene recuperato il codice del Cds Ente (999)
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> la distinta da inizializzare per la
     *                    modifica
     * @return la distinta inizializzata per la modifica
     */

    public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,
                                                  OggettoBulk bulk) throws ComponentException {
        try {
            // questa inizializzazione è necessaria per motivi prestazionali
            // serve per preimpostare il terzo per i versamenti CORI accentrati
            // in base all'anno di esercizio di scrivania
            callCercaTerzoVersCORI(userContext,
                    CNRUserContext
                            .getEsercizio(userContext));

            bulk = super.inizializzaBulkPerModifica(userContext, bulk);
            if (bulk instanceof Distinta_cassiereBulk) {
                Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) bulk;
                lockUltimaDistinta(userContext, distinta);
                distinta = calcolaTotali(userContext, distinta);
                distinta = calcolaTotaliStorici(userContext, distinta);

                EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class)
                        .findAll().get(0);
                distinta.setCd_cds_ente(ente.getCd_unita_organizzativa());
                distinta.setCreateByOtherUo(this.isCreateByOtherUo(userContext,
                        distinta));
            }
            return bulk;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Inizializzazione di una istanza di Distinta_cassiereBulk per ricerca
     * <p>
     * Nome: Inizializzazione per ricerca Pre: E' stata richiesta
     * l'inizializzazione di una istanza di Distinta_cassiereBulk per ricerca
     * Post: Viene inizializzato il Cds della distinta, viene recuperato il
     * codice del Cds ente (999) e vengono calcolati gli storici degli importi
     * di mandati/reversali già trasmessi al cassiere (metodo
     * 'calcolaTotaliStorici')
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> la distinta da inizializzare per la
     *                    ricerca
     * @return la distinta inizializzata per la ricerca
     */
    public OggettoBulk inizializzaBulkPerRicerca(UserContext userContext,
                                                 OggettoBulk bulk) throws ComponentException {
        try {
            // questa inizializzazione è necessaria per motivi prestazionali
            // serve per preimpostare il terzo per i versamenti CORI accentrati
            // in base all'anno di esercizio di scrivania
            callCercaTerzoVersCORI(userContext,
                    CNRUserContext
                            .getEsercizio(userContext));

            bulk = super.inizializzaBulkPerRicerca(userContext, bulk);
            if (bulk instanceof Distinta_cassiereBulk) {
                Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) bulk;
                distinta.setCds((CdsBulk) getHome(userContext, CdsBulk.class)
                        .findByPrimaryKey(
                                new CdsBulk(((CNRUserContext) userContext)
                                        .getCd_cds())));
                /* inizializzo i totali dei trasmessi */
                //distinta = calcolaTotaliStorici(userContext, distinta);

                EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class)
                        .findAll().get(0);
                distinta.setCd_cds_ente(ente.getCd_unita_organizzativa());
            }
            return bulk;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Nome: inizializza dettagli per modfica Pre: E' stata generata la
     * richiesta di modificare i dettagli di una distinta Post: E' stato
     * impostato un savepoint in modo da consentire all'utente l'annullamento
     * delle modifiche dei dettagli
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinta    la Distinta_cassiereBulk i cui dettagli devono essere
     *                    modificati
     */

    public void inizializzaDettagliDistintaPerModifica(UserContext userContext,
                                                       Distinta_cassiereBulk distinta) throws ComponentException {
        try {
            setSavepoint(userContext, "DISTINTA_CASSIERE_DET");
        } catch (SQLException e) {
            throw handleException(e);
        }
    }

    /**
     * Crea un dettaglio di distinta
     * <p>
     * Nome: creazione dettaglio Pre: E' stata generata la richiesta di
     * inserimento di un nuovo dettaglio di distinta relativo al documento
     * contabile (mandato o reversale) a partire dal progerssivo dettaglio
     * last_pg_dettaglio Post: Un nuovo dettaglio distinta e' stato creato e lo
     * stato_trasmissione del doc. contabile associato a tale dettaglio viene
     * aggiornato a INSERITO IN DISTINTA; il progressivo dettaglio viene
     * incrementato di 1.
     *
     * @param userContext       lo UserContext che ha generato la richiesta
     * @param distinta          la Distinta_cassiereBulk per cui creare il dettaglio
     * @param docContabile      il mandato/reversale da inserire in distinta
     * @param last_pg_dettaglio il numero che indica l'ultimo progressivo dettaglio utilizzato
     *                          per la distinta
     * @return last_pg_dettaglio + 1
     */
    public Long inserisciDettaglioDistinta(UserContext userContext,
                                           Distinta_cassiereBulk distinta,
                                           V_mandato_reversaleBulk docContabile, Long last_pg_dettaglio)
            throws ComponentException {
        try {
            Distinta_cassiere_detBulk dettaglio = new Distinta_cassiere_detBulk(
                    distinta.getCd_cds(), distinta.getCd_unita_organizzativa(),
                    distinta.getEsercizio(), last_pg_dettaglio, distinta
                    .getPg_distinta());
            if (docContabile.isMandato())
                dettaglio.setPg_mandato(docContabile.getPg_documento_cont());
            else if (docContabile.isReversale())
                dettaglio.setPg_reversale(docContabile.getPg_documento_cont());
            dettaglio.setCd_cds_origine(docContabile.getCd_cds());
            dettaglio.setUser(CNRUserContext
                    .getUser(userContext));
            insertBulk(userContext, dettaglio);

            aggiornaStatoDocContabile(userContext, docContabile,
                    MandatoBulk.STATO_TRASMISSIONE_INSERITO);

            last_pg_dettaglio = new Long(last_pg_dettaglio.longValue() + 1);

            return last_pg_dettaglio;

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Gestisce l'invio a cassiere di un insieme di distinte selezionate
     * dall'utente
     * <p>
     * Nome: gestione dell'invio di distinte al cassiere Pre: L'utente ha
     * selezionato le distinte da inviare al cassiere Post: Tutte le distinte
     * selezionate sono state inviate al cassiere ( metodo
     * 'inviaSingolaDistinta')
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinte    la collezione di oggetti V_distinta_cass_im_man_revBulk da
     *                    inviare
     */

    public void inviaDistinte(UserContext userContext, Collection distinte)
            throws ComponentException {
        try {
            V_distinta_cass_im_man_revBulk v_distinta;
            Distinta_cassiereBulk distinta;

            for (Iterator i = distinte.iterator(); i.hasNext(); ) {
                v_distinta = (V_distinta_cass_im_man_revBulk) i.next();
                distinta = (Distinta_cassiereBulk) getHome(userContext,
                        Distinta_cassiereBulk.class).findByPrimaryKey(
                        new Distinta_cassiereBulk(v_distinta.getCd_cds(),
                                v_distinta.getCd_unita_organizzativa(),
                                v_distinta.getEsercizio(), v_distinta
                                .getPg_distinta()));
                if (v_distinta.getPg_ver_rec().compareTo(
                        distinta.getPg_ver_rec()) != 0)
                    throw new ApplicationException("Risorsa non più valida");
                inviaSingolaDistinta(userContext, distinta);
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Nome: gestione dell'invio di una distinta al cassiere Pre: Una richiesta
     * di invio a cassiere di una distinta e' stata generata Post: Tutti i
     * mandati/reversali che erano già stati trasmessi al cassiere e che
     * successivamente sono stati annullati vengono aggiunti automaticamente in
     * distinta per essere ritrasmessi (metodo
     * 'aggiungiMandatiEReversaliDaRitrasmettere'); tutti i doc.contabili
     * inclusi in distinta vengono aggiornati per modificarne lo
     * stato_trasmissione a TRASMESSO e la data di trasmissione ( metodo
     * 'aggiornaStatoDocContabili'); alla distinta viene assegnato il
     * progressivo cassiere (metodo 'assegnaProgressivoCassiere'); lo storico
     * dei trasmessi viene aggiornato aggiungendo gli importi relativi ai
     * mandati/reversali non annullati presenti nella distinta inviata (metodo
     * 'aggiornaStoricoTrasmessi'); la data di invio della distinta viene
     * aggiornata con la data odierna.
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinta    la Distinta_cassiereBulk da inviare al cassiere
     */

    public void inviaSingolaDistinta(UserContext userContext,
                                     Distinta_cassiereBulk distinta)
            throws ComponentException {
        try {
            if (distinta == null)
                throw new ApplicationException(
                        "Attenzione! La distinta e' stata cancellata");

            boolean isDistintaCollegata = false;
            Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(
                    userContext, Unita_organizzativa_enteBulk.class).findAll()
                    .get(0);
            if (distinta.getCd_cds() != ente.getCd_unita_organizzativa()
                    && this.isCreateByOtherUo(userContext, distinta))
                isDistintaCollegata = true;

            if (isDistintaCollegata
                    && !CNRUserContext.getCd_unita_organizzativa(userContext)
                    .equals(ente.getCd_unita_organizzativa()))
                throw new ApplicationException(
                        "Attenzione! La distinta "
                                + distinta.getPg_distinta()
                                + " e' stata creata dalla Uo Ente. Non è possibile modificare lo stato.");

            if (!(isDistintaCollegata && CNRUserContext
                    .getCd_unita_organizzativa(userContext).equals(
                            ente.getCd_unita_organizzativa())))
                if (!tesoreriaUnica(userContext, distinta)) {
                    // aggiungo i mandati da ritrasmettere
                    aggiungiMandatiEReversaliDaRitrasmettere(userContext, distinta);
                }
            verificaDuplicazioniProgDocCont(userContext, distinta);

            // aggiorno lo stato trasmissione di mandati/reversali
            aggiornaStatoDocContabili(userContext, distinta,
                    MandatoBulk.STATO_TRASMISSIONE_TRASMESSO);

            assegnaProgressivoCassiere(userContext, distinta);

            aggiornaStoricoTrasmessi(userContext, distinta);

            // imposto la data di invio della distinta
            distinta.setDt_invio(DateServices.getDt_valida(userContext));
            distinta.setUser(userContext.getUser());
            distinta.setToBeUpdated();
            makeBulkPersistent(userContext, distinta);
            aggiornaDataDiffDocamm(userContext, distinta,
                    MandatoBulk.STATO_TRASMISSIONE_TRASMESSO);
            SQLBuilder sql = selectDistinte_cassiere_detCollegate(userContext,
                    distinta);
            List list = getHome(userContext, Distinta_cassiere_detBulk.class)
                    .fetchAll(sql);

            for (Iterator i = list.iterator(); i.hasNext(); )
                inviaSingolaDistinta(userContext,
                        (Distinta_cassiereBulk) getHome(userContext,
                                Distinta_cassiereBulk.class).findByPrimaryKey(
                                ((Distinta_cassiere_detBulk) i.next())
                                        .getDistinta()));

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private void aggiornaDataDiffDocamm(UserContext userContext,
                                        Distinta_cassiereBulk distinta, String statoTrasmissioneTrasmesso)
            throws ComponentException {
        try {
            V_mandato_reversaleHome home = (V_mandato_reversaleHome) getHome(
                    userContext, V_mandato_reversaleBulk.class);
            Fattura_passiva_IHome home_fat = (Fattura_passiva_IHome) getHome(
                    userContext, Fattura_passiva_IBulk.class);
            Fattura_passiva_rigaIHome home_righe = (Fattura_passiva_rigaIHome) getHome(
                    userContext, Fattura_passiva_rigaIBulk.class);
            Mandato_rigaIHome home_mandato = (Mandato_rigaIHome) getHome(
                    userContext, Mandato_rigaIBulk.class);
            Mandato_rigaIBulk mandato_riga = new Mandato_rigaIBulk();
            Reversale_rigaIHome home_reversale = (Reversale_rigaIHome) getHome(
                    userContext, Reversale_rigaIBulk.class);
            Reversale_rigaIBulk reversale_riga = new Reversale_rigaIBulk();
            SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
                    userContext, distinta, V_mandato_reversaleBulk.class, null);
            List list = home.fetchAll(sql);

            for (Iterator i = list.iterator(); i.hasNext(); ) {
                V_mandato_reversaleBulk bulk = (V_mandato_reversaleBulk) i
                        .next();
                if (bulk.isMandato()) {
                    mandato_riga = new Mandato_rigaIBulk();
                    mandato_riga.setMandatoI(new MandatoIBulk(bulk.getCd_cds(),
                            bulk.getEsercizio(), bulk.getPg_documento_cont()));
                    List l = home_mandato.find(mandato_riga);
                    for (Iterator iter = l.iterator(); iter.hasNext(); ) {
                        mandato_riga = (Mandato_rigaIBulk) iter.next();
                        if (mandato_riga.getCd_tipo_documento_amm() != null
                                && mandato_riga
                                .getCd_tipo_documento_amm()
                                .compareTo(
                                        Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA) == 0
                                || mandato_riga
                                .getCd_tipo_documento_amm()
                                .compareTo(
                                        Numerazione_doc_ammBulk.TIPO_COMPENSO) == 0) {
                            // Fattura_passiva_IBulk
                            // fattura=(Fattura_passiva_IBulk)home_fat.findByPrimaryKey(
                            // new
                            // Fattura_passiva_IBulk(mandato_riga.getCd_cds_doc_amm(),mandato_riga.getCd_uo_doc_amm(),mandato_riga.getEsercizio_doc_amm(),mandato_riga.getPg_doc_amm()));
                            // if(fattura!=null){
                            SQLBuilder sql_fat = home_righe.createSQLBuilder();
                            if (mandato_riga
                                    .getCd_tipo_documento_amm()
                                    .compareTo(
                                            Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA) == 0) {
                                sql_fat.addTableToHeader("FATTURA_PASSIVA");
                                sql_fat.addSQLJoin(
                                        "FATTURA_PASSIVA_RIGA.ESERCIZIO",
                                        "FATTURA_PASSIVA.ESERCIZIO");
                                sql_fat.addSQLJoin(
                                        "FATTURA_PASSIVA_RIGA.CD_CDS",
                                        "FATTURA_PASSIVA.CD_CDS");
                                sql_fat
                                        .addSQLJoin(
                                                "FATTURA_PASSIVA_RIGA.CD_UNITA_ORGANIZZATIVA",
                                                "FATTURA_PASSIVA.CD_UNITA_ORGANIZZATIVA");
                                sql_fat
                                        .addSQLJoin(
                                                "FATTURA_PASSIVA_RIGA.PG_FATTURA_PASSIVA",
                                                "FATTURA_PASSIVA.PG_FATTURA_PASSIVA");
                                sql_fat
                                        .addSQLClause(
                                                "AND",
                                                "FATTURA_PASSIVA_RIGA.CD_CDS_OBBLIGAZIONE",
                                                SQLBuilder.EQUALS, mandato_riga
                                                        .getCd_cds());
                                sql_fat
                                        .addSQLClause(
                                                "AND",
                                                "FATTURA_PASSIVA_RIGA.ESERCIZIO_OBBLIGAZIONE",
                                                SQLBuilder.EQUALS,
                                                mandato_riga
                                                        .getEsercizio_obbligazione());
                                sql_fat
                                        .addSQLClause(
                                                "AND",
                                                "FATTURA_PASSIVA_RIGA.ESERCIZIO_ORI_OBBLIGAZIONE",
                                                SQLBuilder.EQUALS,
                                                mandato_riga
                                                        .getEsercizio_ori_obbligazione());
                                sql_fat.addSQLClause("AND",
                                        "FATTURA_PASSIVA_RIGA.PG_OBBLIGAZIONE",
                                        SQLBuilder.EQUALS, mandato_riga
                                                .getPg_obbligazione());
                                sql_fat
                                        .addSQLClause(
                                                "AND",
                                                "FATTURA_PASSIVA_RIGA.PG_OBBLIGAZIONE_SCADENZARIO",
                                                SQLBuilder.EQUALS,
                                                mandato_riga
                                                        .getPg_obbligazione_scadenzario());
                                sql_fat
                                        .addSQLClause(
                                                "AND",
                                                "FATTURA_PASSIVA_RIGA.STATO_COFI",
                                                SQLBuilder.NOT_EQUALS,
                                                Fattura_passiva_rigaBulk.STATO_ANNULLATO);
                                sql_fat
                                        .addSQLClause(
                                                "AND",
                                                "FATTURA_PASSIVA.FL_LIQUIDAZIONE_DIFFERITA",
                                                SQLBuilder.EQUALS, "Y");
                            } else if (mandato_riga
                                    .getCd_tipo_documento_amm()
                                    .compareTo(
                                            Numerazione_doc_ammBulk.TIPO_COMPENSO) == 0) {
                                sql_fat
                                        .addTableToHeader("FATTURA_PASSIVA,COMPENSO");
                                sql_fat.addSQLJoin(
                                        "FATTURA_PASSIVA_RIGA.ESERCIZIO",
                                        "FATTURA_PASSIVA.ESERCIZIO");
                                sql_fat.addSQLJoin(
                                        "FATTURA_PASSIVA_RIGA.CD_CDS",
                                        "FATTURA_PASSIVA.CD_CDS");
                                sql_fat
                                        .addSQLJoin(
                                                "FATTURA_PASSIVA_RIGA.CD_UNITA_ORGANIZZATIVA",
                                                "FATTURA_PASSIVA.CD_UNITA_ORGANIZZATIVA");
                                sql_fat
                                        .addSQLJoin(
                                                "FATTURA_PASSIVA_RIGA.PG_FATTURA_PASSIVA",
                                                "FATTURA_PASSIVA.PG_FATTURA_PASSIVA");
                                sql_fat
                                        .addSQLJoin(
                                                "FATTURA_PASSIVA.ESERCIZIO_FATTURA_FORNITORE",
                                                SQLBuilder.EQUALS,
                                                "COMPENSO.ESERCIZIO_FATTURA_FORNITORE");
                                sql_fat.addSQLJoin(
                                        "FATTURA_PASSIVA.DT_FATTURA_FORNITORE",
                                        SQLBuilder.EQUALS,
                                        "COMPENSO.DT_FATTURA_FORNITORE");
                                sql_fat.addSQLJoin(
                                        "FATTURA_PASSIVA.NR_FATTURA_FORNITORE",
                                        SQLBuilder.EQUALS,
                                        "COMPENSO.NR_FATTURA_FORNITORE");
                                sql_fat
                                        .addSQLJoin(
                                                "FATTURA_PASSIVA.DT_REGISTRAZIONE",
                                                SQLBuilder.EQUALS,
                                                "COMPENSO.DT_REGISTRAZIONE");
                                sql_fat
                                        .addSQLClause(
                                                "AND",
                                                "FATTURA_PASSIVA.STATO_COFI",
                                                SQLBuilder.NOT_EQUALS,
                                                Fattura_passiva_rigaBulk.STATO_ANNULLATO);
                                sql_fat
                                        .addSQLClause(
                                                "AND",
                                                "FATTURA_PASSIVA.FL_LIQUIDAZIONE_DIFFERITA",
                                                SQLBuilder.EQUALS, "Y");

                                sql_fat.addSQLClause("AND",
                                        "COMPENSO.CD_CDS_OBBLIGAZIONE",
                                        SQLBuilder.EQUALS, mandato_riga.getCd_cds());
                                sql_fat.addSQLClause("AND",
                                        "COMPENSO.ESERCIZIO_OBBLIGAZIONE",
                                        SQLBuilder.EQUALS, mandato_riga
                                                .getEsercizio_obbligazione());
                                sql_fat
                                        .addSQLClause(
                                                "AND",
                                                "COMPENSO.ESERCIZIO_ORI_OBBLIGAZIONE",
                                                SQLBuilder.EQUALS,
                                                mandato_riga
                                                        .getEsercizio_ori_obbligazione());
                                sql_fat.addSQLClause("AND",
                                        "COMPENSO.PG_OBBLIGAZIONE", SQLBuilder.EQUALS,
                                        mandato_riga.getPg_obbligazione());
                                sql_fat
                                        .addSQLClause(
                                                "AND",
                                                "COMPENSO.PG_OBBLIGAZIONE_SCADENZARIO",
                                                SQLBuilder.EQUALS,
                                                mandato_riga
                                                        .getPg_obbligazione_scadenzario());
                                sql_fat
                                        .addSQLClause(
                                                "AND",
                                                "COMPENSO.STATO_COFI",
                                                SQLBuilder.NOT_EQUALS,
                                                Fattura_passiva_rigaBulk.STATO_ANNULLATO);
                                sql_fat.addSQLClause("AND",
                                        "COMPENSO.FL_LIQUIDAZIONE_DIFFERITA",
                                        SQLBuilder.EQUALS, "Y");
                            }
                            List list_righe = home_righe.fetchAll(sql_fat);
                            for (Iterator it = list_righe.iterator(); it
                                    .hasNext(); ) {
                                Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) it
                                        .next();
                                riga = (Fattura_passiva_rigaBulk) home_righe
                                        .findByPrimaryKey(riga);
                                if (riga.getData_esigibilita_iva() == null) {

                                    LoggableStatement cs = new LoggableStatement(
                                            getConnection(userContext),
                                            "{  call "
                                                    + EJBCommonServices
                                                    .getDefaultSchema()
                                                    + "CNRCTB100.aggiorna_data_differita(?, ?, ?, ?, ?)}",
                                            false, this.getClass());
                                    try {
                                        cs.setInt(1, riga.getEsercizio());
                                        cs.setString(2, riga.getCd_cds());
                                        cs.setString(3, riga
                                                .getCd_unita_organizzativa());
                                        cs.setLong(4, riga
                                                .getPg_fattura_passiva()
                                                .longValue());
                                        cs.setLong(5, riga
                                                .getProgressivo_riga()
                                                .longValue());
                                        cs.executeQuery();
                                    } catch (SQLException e) {
                                        throw handleException(e);
                                    } finally {
                                        cs.close();
                                    }
                                }

                            }
                        }
                    }

                }
                // Per le NC che generano una reversale
                else {

                    reversale_riga.setReversaleI(new ReversaleIBulk(bulk
                            .getCd_cds(), bulk.getEsercizio(), bulk
                            .getPg_documento_cont()));
                    List l = home_reversale.find(reversale_riga);
                    for (Iterator iter = l.iterator(); iter.hasNext(); ) {
                        reversale_riga = (Reversale_rigaIBulk) iter.next();
                        if (reversale_riga.getCd_tipo_documento_amm() != null
                                && reversale_riga
                                .getCd_tipo_documento_amm()
                                .compareTo(
                                        Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA) == 0) {
                            SQLBuilder sql_fat = home_righe.createSQLBuilder();
                            sql_fat.addTableToHeader("FATTURA_PASSIVA");
                            sql_fat.addSQLJoin(
                                    "FATTURA_PASSIVA_RIGA.ESERCIZIO",
                                    "FATTURA_PASSIVA.ESERCIZIO");
                            sql_fat.addSQLJoin("FATTURA_PASSIVA_RIGA.CD_CDS",
                                    "FATTURA_PASSIVA.CD_CDS");
                            sql_fat
                                    .addSQLJoin(
                                            "FATTURA_PASSIVA_RIGA.CD_UNITA_ORGANIZZATIVA",
                                            "FATTURA_PASSIVA.CD_UNITA_ORGANIZZATIVA");
                            sql_fat.addSQLJoin(
                                    "FATTURA_PASSIVA_RIGA.PG_FATTURA_PASSIVA",
                                    "FATTURA_PASSIVA.PG_FATTURA_PASSIVA");
                            sql_fat.addSQLClause("AND",
                                    "FATTURA_PASSIVA_RIGA.CD_CDS_ACCERTAMENTO",
                                    SQLBuilder.EQUALS, reversale_riga.getCd_cds());
                            sql_fat
                                    .addSQLClause(
                                            "AND",
                                            "FATTURA_PASSIVA_RIGA.ESERCIZIO_ACCERTAMENTO",
                                            SQLBuilder.EQUALS,
                                            reversale_riga
                                                    .getEsercizio_accertamento());
                            sql_fat
                                    .addSQLClause(
                                            "AND",
                                            "FATTURA_PASSIVA_RIGA.ESERCIZIO_ORI_ACCERTAMENTO",
                                            SQLBuilder.EQUALS,
                                            reversale_riga
                                                    .getEsercizio_ori_accertamento());
                            sql_fat.addSQLClause("AND",
                                    "FATTURA_PASSIVA_RIGA.PG_ACCERTAMENTO",
                                    SQLBuilder.EQUALS, reversale_riga
                                            .getPg_accertamento());
                            sql_fat
                                    .addSQLClause(
                                            "AND",
                                            "FATTURA_PASSIVA_RIGA.PG_ACCERTAMENTO_SCADENZARIO",
                                            SQLBuilder.EQUALS,
                                            reversale_riga
                                                    .getPg_accertamento_scadenzario());
                            sql_fat.addSQLClause("AND",
                                    "FATTURA_PASSIVA_RIGA.STATO_COFI",
                                    SQLBuilder.NOT_EQUALS,
                                    Fattura_passiva_rigaBulk.STATO_ANNULLATO);
                            sql_fat
                                    .addSQLClause(
                                            "AND",
                                            "FATTURA_PASSIVA.FL_LIQUIDAZIONE_DIFFERITA",
                                            SQLBuilder.EQUALS, "Y");
                            List list_righe = home_righe.fetchAll(sql_fat);
                            for (Iterator it = list_righe.iterator(); it
                                    .hasNext(); ) {
                                Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) it
                                        .next();
                                riga = (Fattura_passiva_rigaBulk) home_righe
                                        .findByPrimaryKey(riga);
                                if (riga.getData_esigibilita_iva() == null) {

                                    LoggableStatement cs = new LoggableStatement(
                                            getConnection(userContext),
                                            "{  call "
                                                    + EJBCommonServices
                                                    .getDefaultSchema()
                                                    + "CNRCTB100.aggiorna_data_differita(?, ?, ?, ?, ?)}",
                                            false, this.getClass());
                                    try {
                                        cs.setInt(1, riga.getEsercizio());
                                        cs.setString(2, riga.getCd_cds());
                                        cs.setString(3, riga
                                                .getCd_unita_organizzativa());
                                        cs.setLong(4, riga
                                                .getPg_fattura_passiva()
                                                .longValue());
                                        cs.setLong(5, riga
                                                .getProgressivo_riga()
                                                .longValue());
                                        cs.executeQuery();
                                    } catch (SQLException e) {
                                        throw handleException(e);
                                    } finally {
                                        cs.close();
                                    }
                                }

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    protected void lockUltimaDistinta(UserContext userContext,
                                      Distinta_cassiereBulk distinta) throws ComponentException {
        try {
            Distinta_cassiereBulk ultimaDistinta = ((Distinta_cassiereHome) getHome(
                    userContext, distinta.getClass()))
                    .findUltimaDistinta(distinta);
            if (ultimaDistinta == null)
                acquisisciSemaforo(userContext, distinta);

        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * Nome: modifica dettagli distinta Pre: L'utente ha richiesto la modifica
     * dei dettagli di una distinta, aggiungendo nuovi mandati/reversali e/o
     * richiedendo la cancellazione dalla distinta di mandati/reversali
     * precedentemente inseriti Post: Per ogni mandato/reversale per cui
     * l'utente ha richiesto l'inserimento in distinta viene generato un
     * dettaglio della distinta e lo stato trasmissione del mandato/reversale
     * viene aggiornato a 'inserito in distinta' (metodo
     * 'inserisciDettaglioDistinta'); se tale mandato/reversale ha associati
     * altre reversali/mandati, vengono creati automaticamente dei dettagli di
     * distinta anche per questi ed il loro stato trasmissione viene aggiornato
     * (metodo 'aggiungiMandatiEReversaliCollegati'); Per ogni mandato/reversale
     * per cui l'utente ha richiesto la cancellazione dalla distinta viene
     * eliminato il dettaglio distinta ad esso riferito e lo stato trasmissione
     * del mandato/reversale viene aggiornato a 'non inserito in distinta'
     * (metodo 'eliminaDettaglioDistinta'); se tale mandato/reversale ha
     * associati reversali/mandati, vengono eliminati automaticamente i loro
     * dettagli di distinta ed il loro stato trasmissione viene aggiornato
     * (metodo 'eliminaMandatiEReversaliCollegati');
     *
     * @param userContext     lo UserContext che ha generato la richiesta
     * @param distinta        la Distinta_cassiereBulk i cui dettagli sono stati modificati
     * @param docContabili    l'array di documenti contabili (V_mandato_reversaleBulk)
     *                        potenzialmente interessati da questa modifica
     * @param oldDocContabili il BitSet che specifica la precedente selezione nell'array
     *                        docContabili
     * @param newDocContabili il BitSet che specifica l'attuale selezione nell'array
     *                        docContabili
     */

    public void modificaDettagliDistinta(UserContext userContext,
                                         Distinta_cassiereBulk distinta, OggettoBulk[] docContabili,
                                         BitSet oldDocContabili, BitSet newDocContabili)
            throws ComponentException {
        try {
            Long last_pg_dettaglio = null;
            for (int i = 0; i < docContabili.length; i++) {
                V_mandato_reversaleBulk docContabile = (V_mandato_reversaleBulk) docContabili[i];
                if (oldDocContabili.get(i) != newDocContabili.get(i)) {
                    if (last_pg_dettaglio == null)
                        last_pg_dettaglio = ((Distinta_cassiere_detHome) getHome(
                                userContext, Distinta_cassiere_detBulk.class))
                                .getUltimoPg_Dettaglio(userContext, distinta);
                    if (newDocContabili.get(i)) {
                        last_pg_dettaglio = inserisciDettaglioDistinta(
                                userContext, distinta, docContabile,
                                last_pg_dettaglio);
                        last_pg_dettaglio = aggiungiMandatiEReversaliCollegati(
                                userContext, distinta, docContabile,
                                last_pg_dettaglio);
                        inserisciDettaglioDistinteCollegate(userContext,
                                distinta, docContabile);
                    } else {
                        eliminaDettaglioDistinta(userContext, distinta,
                                docContabile);
                        eliminaMandatiEReversaliCollegati(userContext,
                                distinta, docContabile);
                        eliminaDettaglioDistinteCollegate(userContext,
                                distinta, docContabile);
                    }
                }
            }
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Processa File PreCondition: E' stata generata la richiesta di processare
     * un file. Nessun errore rilevato. PostCondition: Viene richiamata la
     * procedura che processerà il file selezionato dall'utente, (metodo
     * callProcessaFile). Restituisce l'oggetto V_ext_cassiere00Bulk aggiornato.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
     * @param file        <code>V_ext_cassiere00Bulk</code> l'oggetto che contiene le
     *                    informazioni relative al file da processare.
     * @return <code>V_ext_cassiere00Bulk</code> l'oggetto aggiornato.
     **/
    public V_ext_cassiere00Bulk processaFile(
            UserContext userContext, V_ext_cassiere00Bulk file)
            throws ComponentException {

        callProcessaFile(userContext, file);

        return file;// aggiornaLiquidCori(userContext, liquidazione_cori);
    }

    /**
     * Crea il SQLBuilder per ricercare le distinte
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: cerca distinte Pre: Una richiesta di ricerca di una o più distinte
     * e' stata generata Post: Viene creato il SQLBuilder con impostati
     * l'esercizio, il cd_cds e il cd-unita_organizzativa di scrivania
     * <p>
     * Nome: cerca distinte da inviare Pre: Una richiesta di ricerca di una o
     * più distinte da inviare al cassiere e' stata generata Post: Viene creato
     * il SQLBuilder con impostati l'esercizio, il cd_cds e il
     * cd_unita_organizzativa di scrivania e con la data di invio non
     * valorizzata
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param clauses     le clausole specificate dall'utente
     * @param bulk        la Distinta_cassiereBulk oppure la
     *                    V_distinta_cass_im_man_revBulk da ricercare
     * @return sql Query con le clausole aggiuntive
     */

    protected Query select(UserContext userContext, CompoundFindClause clauses,
                           OggettoBulk bulk) throws ComponentException,
            PersistencyException {
        SQLBuilder sql = (SQLBuilder) super.select(userContext, clauses, bulk);
        sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS,
                ((CNRUserContext) userContext).getCd_unita_organizzativa());
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS,
                ((CNRUserContext) userContext).getEsercizio());
        sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS,
                ((CNRUserContext) userContext).getCd_cds());
        // visualizza solo le distinte che non sono state ancora inviate
        if (bulk instanceof V_distinta_cass_im_man_revBulk) {
            verificaStatoEsercizio(userContext);
            sql.addClause("AND", "dt_invio", SQLBuilder.ISNULL, null);
        }
        if (bulk instanceof Distinta_cassiereBulk && ((Distinta_cassiereBulk) bulk).getFl_flusso() != null)
            sql.addClause("AND", "fl_flusso", SQLBuilder.EQUALS, ((Distinta_cassiereBulk) bulk).getFl_flusso().booleanValue());
        if (bulk instanceof Distinta_cassiereBulk && ((Distinta_cassiereBulk) bulk).getFl_sepa() != null)
            sql.addClause("AND", "fl_sepa", SQLBuilder.EQUALS, ((Distinta_cassiereBulk) bulk).getFl_sepa().booleanValue());
        if (bulk instanceof Distinta_cassiereBulk && ((Distinta_cassiereBulk) bulk).getFl_annulli() != null)
            sql.addClause("AND", "fl_annulli", SQLBuilder.EQUALS, ((Distinta_cassiereBulk) bulk).getFl_annulli().booleanValue());

        sql.addOrderBy("pg_distinta");
        return sql;
    }

    /**
     * Crea il SQLBuilder per recuperare tutti dettagli del File Cassiere
     * selezionato
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: cerca i dettagli di un File Cassiere Pre: Una richiesta di ricerca
     * dei dettagli del File Cassiere e' stata generata Post: Viene creato il
     * SQLBuilder che consente di recuperare le istanze di Ext_cassiere00Bulk
     * che contengono tutte le informazioni richieste.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param file        il <code>V_ext_cassiere00Bulk</code> di cui ricercare i
     *                    dettagli
     * @param bulkClass   l'<code>OggettoBulk</code> da usare come prototipo della
     *                    ricerca; sul prototipo vengono costruite delle clausole
     *                    aggiuntive che vengono aggiunte in AND alle clausole
     *                    specificate.
     * @param clauses     <code>CompoundFindClause</code> L'albero logico delle clausole
     *                    da applicare alla ricerca
     * @return <code>SQLBuilder</code> Un'istanza di SQLBuilder contenente
     * l'istruzione SQL da eseguire e tutti i parametri della query.
     */
    public SQLBuilder selectDettagliFileCassiereByClause(
            UserContext userContext, V_ext_cassiere00Bulk file,
            Ext_cassiere00Bulk bulkClass, CompoundFindClause clauses)
            throws ComponentException {

        if (file == null)
            return null;

        SQLBuilder sql = getHome(userContext, Ext_cassiere00Bulk.class)
                .createSQLBuilder();

        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, file.getEsercizio());
        sql.addSQLClause("AND", "NOME_FILE", SQLBuilder.EQUALS, file.getNome_file());

        return sql;
    }

    /**
     * Crea il SQLBuilder per recuperare i documenti 1210 collegabili alla distinta
     *
     * @param userContext
     * @param distinta
     * @param bulkClass
     * @param clauses
     * @return
     * @throws ComponentException
     */
    public SQLBuilder selectDistintaCassiere1210LettereDaCollegareByClause(UserContext userContext, DistintaCassiere1210Bulk distinta, Class bulkClass, CompoundFindClause clauses) throws ComponentException {
        SQLBuilder sql = getHome(userContext, Lettera_pagam_esteroBulk.class).createSQLBuilder();
        if (clauses != null)
            sql.addClause(clauses);
        sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
        sql.addClause(FindClause.AND, "stato_trasmissione", SQLBuilder.EQUALS, MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA);
        sql.addClause(FindClause.AND, "distintaCassiere", SQLBuilder.ISNULL, null);
        return sql;
    }

    /**
     * Crea il SQLBuilder per recuperare i documenti 1210 collegati alla distinta
     *
     * @param userContext
     * @param distinta
     * @param bulkClass
     * @param clauses
     * @return
     * @throws ComponentException
     */
    public SQLBuilder selectDistintaCassiere1210LettereCollegateByClause(UserContext userContext, DistintaCassiere1210Bulk distinta, Class bulkClass, CompoundFindClause clauses) throws ComponentException {
        SQLBuilder sql = getHome(userContext, Lettera_pagam_esteroBulk.class).createSQLBuilder();
        if (clauses != null)
            sql.addClause(clauses);
        if (distinta.getEsercizio() == null)
            sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, 0);
        sql.addClause(FindClause.AND, "distintaCassiere", SQLBuilder.EQUALS, distinta);
        return sql;
    }

    /**
     * Crea il SQLBuilder per recuperare tutti dettagli della distinta
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: cerca i dettagli di una distinta Pre: Una richiesta di ricerca dei
     * dettagli della distinta e' stata generata Post: Viene creato il
     * SQLBuilder che consente di recuperare le istanze di
     * V_mandato_reversaleBulk che contengono tutte le informazioni dei mandati
     * e delle reversali inseriti in distinta.
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinta    la Distinta_cassiereBulk di cui ricercare i dettagli
     * @param bulkClass
     * @param clauses
     * @return SQLBuilder
     */

    public SQLBuilder selectDistinta_cassiere_detCollByClause(
            UserContext userContext, Distinta_cassiereBulk distinta,
            Class bulkClass, CompoundFindClause clauses)
            throws ComponentException {
        try {
            Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
            SQLBuilder sql = getHome(userContext, V_mandato_reversaleBulk.class,
                    "V_MANDATO_REVERSALE_DISTINTA").createSQLBuilder();

            if (distinta.getPg_distinta() == null)
                return null;
            long nrDettagli = ((Distinta_cassiere_detHome) getHome(userContext,
                    Distinta_cassiere_detBulk.class)).getNrDettagli(
                    userContext, distinta);
            if (nrDettagli == 0) {
                sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, -1);
                return sql;
            }

            if (tesoreriaUnica(userContext, distinta)) {
                SQLBuilder sqlIN = getHome(userContext, Distinta_cassiere_detBulk.class).createSQLBuilder();
                sqlIN.addSQLClause("AND", "DISTINTA_CASSIERE_DET.ESERCIZIO", SQLBuilder.EQUALS,
                        distinta.getEsercizio());
                sqlIN.addSQLClause("AND", "DISTINTA_CASSIERE_DET.CD_CDS", SQLBuilder.EQUALS,
                        distinta.getCd_cds());
                sqlIN.addSQLClause("AND",
                        "DISTINTA_CASSIERE_DET.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS,
                        distinta.getCd_unita_organizzativa());
                sqlIN.addSQLClause("AND", "DISTINTA_CASSIERE_DET.PG_DISTINTA",
                        SQLBuilder.EQUALS, distinta.getPg_distinta());
                //sqlIN.addOrderBy("CD_CDS_ORIGINE,PG_DETTAGLIO");
                List list = getHome(userContext, Distinta_cassiere_detBulk.class).fetchAll(sqlIN);
                String lista_mandati = null;
                String lista_reversali = null;
                //String cds_old=null;
                //String cds_old_rev=null;
                sql.openParenthesis("AND");
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Distinta_cassiere_detBulk det = (Distinta_cassiere_detBulk) i.next();
                    //if(det.getCd_cds_origine()!=cds_old && det.getPg_mandato()!=null){
                    if (det.getPg_mandato() != null) {
                        if (lista_mandati != null) {
                            sql.openParenthesis("OR");

                            sql.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_CDS", "V_MANDATO_REVERSALE_DISTINTA.CD_CDS");
                            sql.addSQLClause("AND", "V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO", SQLBuilder.EQUALS, distinta
                                    .getEsercizio());
                            sql.addSQLClause("AND",
                                    "V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
                                    SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);

                            sql.addSQLClause("AND", "(V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT) IN (" + lista_mandati + ")");

//							  sql.openParenthesis("AND");
//							  sql.addSQLClause("AND","V_MANDATO_REVERSALE_DISTINTA.CD_CDS_ORIGINE",sql.EQUALS,cds_old);
//							  sql.addSQLClause("OR","V_MANDATO_REVERSALE_DISTINTA.CD_CDS",sql.EQUALS,ente.getUnita_padre().getCd_unita_organizzativa());
//							  sql.closeParenthesis();
//
                            sql.closeParenthesis();
                        }
                        //cds_old=det.getCd_cds_origine();
                        lista_mandati = null;
                    }
                    if (det.getPg_mandato() != null) {
                        if (lista_mandati != null)
                            lista_mandati = lista_mandati + "," + det.getPg_mandato().toString();
                        else
                            lista_mandati = det.getPg_mandato().toString();
                    }
                    //if(det.getCd_cds_origine()!=cds_old_rev && det.getPg_reversale()!=null){
                    if (det.getPg_reversale() != null) {
                        if (lista_reversali != null) {
                            sql.openParenthesis("OR");

                            sql.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_CDS", "V_MANDATO_REVERSALE_DISTINTA.CD_CDS");
                            sql.addSQLClause("AND", "V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO", SQLBuilder.EQUALS, distinta
                                    .getEsercizio());
                            sql.addSQLClause("AND",
                                    "V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
                                    SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_REV);

                            sql.addSQLClause("AND", "(V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT) IN (" + lista_reversali + ")");


//							  sql.openParenthesis("AND");
//							  sql.addSQLClause("AND","V_MANDATO_REVERSALE_DISTINTA.CD_CDS_ORIGINE",sql.EQUALS,cds_old_rev);
//							  sql.addSQLClause("OR","V_MANDATO_REVERSALE_DISTINTA.CD_CDS",sql.EQUALS,ente.getUnita_padre().getCd_unita_organizzativa());
//							  sql.closeParenthesis();
//
                            sql.closeParenthesis();
                        }
                        //cds_old_rev=det.getCd_cds_origine();
                        lista_reversali = null;
                    }
                    if (det.getPg_reversale() != null) {
                        if (lista_reversali != null)
                            lista_reversali = lista_reversali + "," + det.getPg_reversale().toString();
                        else
                            lista_reversali = det.getPg_reversale().toString();
                    }
                }

                if (lista_mandati != null) {
                    sql.openParenthesis("OR");
                    sql.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_CDS", "V_MANDATO_REVERSALE_DISTINTA.CD_CDS");
                    sql.addSQLClause("AND", "V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO", SQLBuilder.EQUALS, distinta
                            .getEsercizio());
                    sql.addSQLClause("AND",
                            "V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
                            SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);

                    sql.addSQLClause("AND", "(V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT) IN (" + lista_mandati + ")");

//					  sql.openParenthesis("AND");
//					  sql.addSQLClause("AND","V_MANDATO_REVERSALE_DISTINTA.CD_CDS_ORIGINE",sql.EQUALS,cds_old);
//					  sql.addSQLClause("OR","V_MANDATO_REVERSALE_DISTINTA.CD_CDS",sql.EQUALS,ente.getUnita_padre().getCd_unita_organizzativa());
//					  sql.closeParenthesis();
                    sql.closeParenthesis();
                }
                if (lista_reversali != null) {
                    sql.openParenthesis("OR");
                    sql.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_CDS", "V_MANDATO_REVERSALE_DISTINTA.CD_CDS");
                    sql.addSQLClause("AND", "V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO", SQLBuilder.EQUALS, distinta
                            .getEsercizio());
                    sql.addSQLClause("AND",
                            "V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
                            SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_REV);

                    sql.addSQLClause("AND", "(V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT) IN (" + lista_reversali + ")");

//					  sql.openParenthesis("AND");
//					  sql.addSQLClause("AND","V_MANDATO_REVERSALE_DISTINTA.CD_CDS_ORIGINE",sql.EQUALS,cds_old_rev);
//					  sql.addSQLClause("OR","V_MANDATO_REVERSALE_DISTINTA.CD_CDS",sql.EQUALS,ente.getUnita_padre().getCd_unita_organizzativa());
//					  sql.closeParenthesis();
                    sql.closeParenthesis();
                }

                sql.closeParenthesis();
                //  sql.addOrderBy(" V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT_PADRE, V_MANDATO_REVERSALE_DISTINTA.TI_DOCUMENTO_CONT_PADRE, V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT_PADRE ");
                return sql;
            } else {
                SQLBuilder sql2 = getHome(userContext, Distinta_cassiere_detBulk.class)
                        .createSQLBuilder();
                sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO",
                        "DISTINTA_CASSIERE_DET.ESERCIZIO");
                sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.CD_CDS",
                        "DISTINTA_CASSIERE_DET.CD_CDS");
                // da committare
                sql2.openParenthesis("AND");
                sql2.openParenthesis("AND");
                sql2.addSQLClause("AND",
                        "V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
                        SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);
                sql2.addSQLJoin("	V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT",
                        " DISTINTA_CASSIERE_DET.PG_MANDATO");
                sql2.addSQLClause("OR",
                        "V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
                        SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_REV);
                sql2.addSQLJoin("V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT",
                        "DISTINTA_CASSIERE_DET.PG_REVERSALE");
                sql2.closeParenthesis();
                sql2.closeParenthesis();
                // da committare
                sql2.addSQLClause("AND", "DISTINTA_CASSIERE_DET.ESERCIZIO", SQLBuilder.EQUALS,
                        distinta.getEsercizio());
                sql2.addSQLClause("AND", "DISTINTA_CASSIERE_DET.CD_CDS", SQLBuilder.EQUALS,
                        distinta.getCd_cds());
                sql2.addSQLClause("AND",
                        "DISTINTA_CASSIERE_DET.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS,
                        distinta.getCd_unita_organizzativa());
                sql2.addSQLClause("AND", "DISTINTA_CASSIERE_DET.PG_DISTINTA",
                        SQLBuilder.EQUALS, distinta.getPg_distinta());

                sql.addSQLExistsClause("AND", sql2);
                sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, distinta
                        .getEsercizio());
                sql.addSQLClause("AND", "CD_CDS", SQLBuilder.EQUALS, distinta.getCd_cds());
                // sql.addSQLClause( "AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS,
                // distinta.getCd_unita_organizzativa() );

                sql.addOrderBy(" V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT_PADRE, V_MANDATO_REVERSALE_DISTINTA.TI_DOCUMENTO_CONT_PADRE, V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT_PADRE ");
                return sql;
            }
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Validazione di una DistintaBulk in caso di inserimento/maodifica
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Errore nr. dettagli Pre: E' stata richiesta la creazione/modifica
     * di una distinta senza dettagli Post: Viene restituita all'utente una
     * segnalazione di errore
     * <p>
     * Nome: validazione ok Pre: E' stata richiesta la creazione/modifica di una
     * distinta con almeno un dettaglio Post: La distinta ha superato la
     * validazione
     *
     * @param userContext <code>UserContext</code>
     * @param bulk        <code>Distinta_cassiereBulk</code> distinta da validare
     */

    protected void validaCreaModificaConBulk(UserContext userContext,
                                             OggettoBulk bulk) throws ComponentException {
        super.validaCreaModificaConBulk(userContext, bulk);
        if (bulk instanceof Distinta_cassiereBulk){
            Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) bulk;

            try {
                long nrDettagli = ((Distinta_cassiere_detHome) getHome(userContext,
                        Distinta_cassiere_detBulk.class)).getNrDettagli(
                        userContext, distinta);
                if (nrDettagli == 0)
                    throw new ApplicationException(
                            " La distinta deve avere almeno un dettaglio!");
                // Controlla i documenti inseriti nella distinta
                validaDocumentiContabiliAssociati(userContext, distinta);
                if (distinta.getFl_annulli().booleanValue())
                    callCheckDocContForDistintaAnn(userContext, distinta);
                callCheckDocContForDistinta(userContext, distinta);
            } catch (Exception e) {
                throw handleException(e);
            }
        }
    }

    /**
     * Verifica dello stato dell'esercizio
     *
     * @param userContext <code>UserContext</code>
     * @return FALSE se per il cds interessato non è stato inserito nessun
     * esercizio o se l'esercizio non è in stato di "aperto" TRUE in
     * tutti gli altri casi
     */
    protected void verificaStatoEsercizio(UserContext userContext)
            throws ComponentException {
        try {
            EsercizioBulk esercizio = (EsercizioBulk) getHome(userContext,
                    EsercizioBulk.class).findByPrimaryKey(
                    new EsercizioBulk(((CNRUserContext) userContext)
                            .getCd_cds(), ((CNRUserContext) userContext)
                            .getEsercizio()));
            if (esercizio == null)
                throw new ApplicationException("L'esercizio "
                        + ((CNRUserContext) userContext).getEsercizio()
                        + " non è ancora stato definito per il Cds "
                        + ((CNRUserContext) userContext).getCd_cds());
            if (!EsercizioBulk.STATO_APERTO.equals(esercizio
                    .getSt_apertura_chiusura()))
                throw new ApplicationException("L'esercizio "
                        + ((CNRUserContext) userContext).getEsercizio()
                        + " non è ancora stato aperto per il Cds "
                        + ((CNRUserContext) userContext).getCd_cds());
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Ricerca i parametri ente per l'anno di esercizio in scrivania
     *
     * @param aUC UserContext
     * @return Parametri_cnrBulk contenente i parametri ente
     * @throws ComponentException
     */
    public Parametri_cnrBulk parametriCnr(UserContext aUC)
            throws ComponentException {
        Parametri_cnrBulk param;
        try {
            param = (Parametri_cnrBulk) getHome(aUC, Parametri_cnrBulk.class)
                    .findByPrimaryKey(
                            new Parametri_cnrBulk(((CNRUserContext) aUC)
                                    .getEsercizio()));
        } catch (PersistencyException ex) {
            throw handleException(ex);
        } catch (ComponentException ex) {
            throw handleException(ex);
        }
        if (param == null) {
            throw new ApplicationException("Parametri CNR non trovati.");
            // se si vuole gestire un default
            // param = new Parametri_cnrBulk();
            // param.setFl_versamenti_cori(Boolean.FALSE);
        }
        return param;
    }

    /**
     * è vero se è stato impostato il flag nei parametri generali
     * FL_VERSAMENTO_CORI che indica se inserire i mandati di versamento
     * CORI/IVA in modo obbligatorio e automatico
     */
    private boolean isInserisciMandatiVersamentoCori(UserContext context)
            throws ComponentException {

        Parametri_cnrBulk parametriCnr = parametriCnr(context);

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return parametriCnr.getFl_versamenti_cori().booleanValue()
                && day >= parametriCnr.getVersamenti_cori_giorno().intValue();
    }

    private void callCercaTerzoVersCORI(UserContext userContext,
                                        Integer esercizio) throws ComponentException {

        LoggableStatement cs = null;
        try {
            cs = new LoggableStatement(getConnection(userContext), "{ call "
                    + EJBCommonServices.getDefaultSchema()
                    + "CNRUTIL.loadTerzoVersCori(?) }", false, this.getClass());
            cs.setInt(1, esercizio.intValue());
            cs.executeQuery();
        } catch (Throwable e) {
            throw handleException(e);
        } finally {
            try {
                if (cs != null)
                    cs.close();
            } catch (SQLException e) {
                throw handleException(e);
            }
        }
    }

    protected boolean annulliTuttaSac(UserContext userContext,
                                      Distinta_cassiereBulk distinta) throws SQLException,
            ComponentException, PersistencyException, EJBException,
            RemoteException {
        return ((Configurazione_cnrHome)getHome(userContext,Configurazione_cnrBulk.class))
                    .isUOSpecialeDistintaTuttaSAC(CNRUserContext.getEsercizio(userContext),distinta.getCd_unita_organizzativa());
    }

    /**
     * Verifica che i Documenti Associati alla distinta siano associabili
     *
     * @param userContext <code>UserContext</code>
     * @param distinta    <code>Distinta_cassiereBulk</code>
     */
    private void validaDocumentiContabiliAssociati(UserContext userContext,
                                                   Distinta_cassiereBulk distinta) throws ComponentException {
        try {
            // if
            // (Utility.createParametriCnrComponentSession().getParametriCnr(userContext,
            // distinta.getEsercizio()).getFl_siope().booleanValue()) {
            V_mandato_reversaleHome home = (V_mandato_reversaleHome) getHome(
                    userContext, V_mandato_reversaleBulk.class);
            SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
                    userContext, distinta, V_mandato_reversaleBulk.class, null);
            List list = home.fetchAll(sql);
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                V_mandato_reversaleBulk bulk = (V_mandato_reversaleBulk) i
                        .next();

                if (Utility.createParametriCnrComponentSession()
                        .getParametriCnr(userContext, distinta.getEsercizio())
                        .getFl_siope().booleanValue()) {
                    if (bulk.isMandato()
                            && !Utility
                            .createMandatoComponentSession()
                            .isCollegamentoSiopeCompleto(
                                    userContext,
                                    (MandatoBulk) getHome(userContext,
                                            MandatoIBulk.class)
                                            .findByPrimaryKey(
                                                    new MandatoIBulk(
                                                            bulk
                                                                    .getCd_cds(),
                                                            bulk
                                                                    .getEsercizio(),
                                                            bulk
                                                                    .getPg_documento_cont()))))
                        throw new ApplicationException(
                                "Il mandato "
                                        + bulk.getCd_cds() + "/"
                                        + bulk.getPg_documento_cont()
                                        + " non risulta associato completamente a codici Siope. Scollegarlo dalla distinta e ripetere l'operazione.");
                    else if (bulk.isReversale()
                            && !Utility
                            .createReversaleComponentSession()
                            .isCollegamentoSiopeCompleto(
                                    userContext,
                                    (ReversaleBulk) getHome(
                                            userContext,
                                            ReversaleIBulk.class)
                                            .findByPrimaryKey(
                                                    new ReversaleIBulk(
                                                            bulk
                                                                    .getCd_cds(),
                                                            bulk
                                                                    .getEsercizio(),
                                                            bulk
                                                                    .getPg_documento_cont()))))
                        throw new ApplicationException(
                                "La reversale " + bulk.getCd_cds() + "/"
                                        + bulk.getPg_documento_cont()
                                        + " non risulta associata completamente a codici Siope. Scollegarla dalla distinta e ripetere l'operazione.");
                }
                // Controllo valorizzazione Iban
                if (!distinta.isCheckIbanEseguito()) {
                    if (bulk.isMandato()) {
                        Mandato_rigaIHome home_mandato = (Mandato_rigaIHome) getHome(
                                userContext, Mandato_rigaIBulk.class);
                        Mandato_rigaIBulk mandato_riga = new Mandato_rigaIBulk();
                        mandato_riga.setMandatoI(new MandatoIBulk(bulk
                                .getCd_cds(), bulk.getEsercizio(), bulk
                                .getPg_documento_cont()));
                        List l = home_mandato.find(mandato_riga);
                        for (Iterator iter = l.iterator(); iter.hasNext(); ) {
                            mandato_riga = (Mandato_rigaIBulk) iter.next();
                            mandato_riga
                                    .setBanca((BancaBulk) getHome(userContext,
                                            BancaBulk.class)
                                            .findByPrimaryKey(
                                                    new BancaBulk(
                                                            mandato_riga
                                                                    .getCd_terzo(),
                                                            mandato_riga
                                                                    .getPg_banca())));
                            mandato_riga.getBanca().setTerzo(
                                    (TerzoBulk) getHome(userContext,
                                            TerzoBulk.class).findByPrimaryKey(
                                            new TerzoBulk(mandato_riga
                                                    .getCd_terzo())));
                            mandato_riga
                                    .getBanca()
                                    .getTerzo()
                                    .setAnagrafico(
                                            (AnagraficoBulk) getHome(
                                                    userContext,
                                                    AnagraficoBulk.class)
                                                    .findByPrimaryKey(
                                                            new AnagraficoBulk(
                                                                    mandato_riga
                                                                            .getBanca()
                                                                            .getTerzo()
                                                                            .getCd_anag())));

                            if (mandato_riga
                                    .getBanca()
                                    .getTi_pagamento()
                                    .compareTo(
                                            Rif_modalita_pagamentoBulk.BANCARIO) == 0
                                    && mandato_riga.getBanca().getTerzo()
                                    .getAnagrafico()
                                    .getTi_italiano_estero().compareTo(
                                            NazioneBulk.EXTRA_CEE) != 0
                                    && mandato_riga.getBanca().getCodice_iban() == null)
                                if (!(Utility
                                        .createParametriCdsComponentSession()
                                        .getParametriCds(userContext,
                                                distinta.getCd_cds(),
                                                distinta.getEsercizio())
                                        .getFl_blocco_iban().booleanValue()))
                                    throw new CheckIbanFailed(
                                            "Poiché a partire dal 1/7/2008 i pagamenti privi di codice IBAN verranno eseguiti dalla banca in ritardo e con una penale a carico dell'istituto, prima di continuare con il salvataggio della distinta (pulsante OK) si consiglia di verificare le coordinate bancarie del terzo "
                                                    + bulk.getCd_terzo()
                                                    + " - "
                                                    + mandato_riga
                                                    .getBanca()
                                                    .getTerzo()
                                                    .getAnagrafico()
                                                    .getDescrizioneAnagrafica()
                                                    + "!"
                                                    + "<BR><BR>"
                                                    + " Selezionando il pulsante 'Annulla' e uscendo dalla funzione di 'Distinta' è possibile eseguire l'aggiornamento dei dati IBAN in anagrafica, dalla funzione 'Terzo persona fisica/giuridica', senza dover ricorrere all'annullo del mandato emesso. Al salvataggio dell'aggiornamento del codice terzo, il codice IBAN verrà riportato automaticamente sul mandato, il quale dovrà essere solo nuovamente stampato ed inserito in distinta per l'invio in banca.<BR><BR>"
                                                    + "Si ricorda che dal 1/07/2008 non sarà più possibile emettere distinte contenenti mandati con beneficiari privi di IBAN.");

                                else
                                    throw new ApplicationException(
                                            "Non è possibile procedere al salvataggio della distinta emessa in quanto sono presenti pagamenti privi di codice IBAN. E' necessario verificare le coordinate bancarie del terzo "
                                                    + bulk.getCd_terzo()
                                                    + " - "
                                                    + mandato_riga
                                                    .getBanca()
                                                    .getTerzo()
                                                    .getAnagrafico()
                                                    .getDescrizioneAnagrafica()
                                                    + "!\n\n"
                                                    + "Uscendo dalla funzione di 'Distinta' è obbligatorio eseguire l'aggionamento dei dati IBAN in anagrafica, dalla funzione 'Terzo persona fisica/giuridica', senza dover ricorrere all'annullo del mandato emesso. Al salvataggio dell'aggiornamento del codice terzo, il codice IBAN verrà riportato automaticamente sul mandato,"
                                                    + " il quale dovrà essere solo nuovamente stampato ed inserito in distinta per l'invio in banca.");

                        }
                    }
                }
            }

            // }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private Distinta_cassiereBulk inizializzaDistintaPerInserimento(
            UserContext userContext, Distinta_cassiereBulk distinta, CdsBulk cds)
            throws ComponentException {
        try {
            distinta.setCds(cds);
            lockUltimaDistinta(userContext, distinta);
            // distinta.setDt_emissione( getHome( userContext,
            // distinta.getClass()).getServerTimestamp());
            // imposto la data di emissione in modo da averla nel seguente
            // formato: gg/mm/aaaa
            distinta.setDt_emissione(DateServices.getDt_valida(userContext));
            /* inizializzo i totali dei trasmessi */
            distinta = calcolaTotaliStorici(userContext, distinta);
            distinta.setIm_man_ini_pag(new BigDecimal(0));
            distinta.setIm_man_ini_sos(new BigDecimal(0));
            distinta.setIm_man_ini_acc(new BigDecimal(0));
            distinta.setIm_rev_ini_sos(new BigDecimal(0));
            distinta.setIm_rev_ini_tra(new BigDecimal(0));
            distinta.setIm_rev_ini_rit(new BigDecimal(0));
            distinta.setStato(Distinta_cassiereBulk.Stato.PROVVISORIA);
            assegnaProgressivo(userContext, distinta);

            EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class)
                    .findAll().get(0);
            distinta.setCd_cds_ente(ente.getCd_unita_organizzativa());
            return distinta;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Elimina dalle distinte dei CDS le reversali associate al mandato di
     * trasferimento che si sta scollegando
     * <p>
     * Nome: elimina dalle distinte le reversali associate al mandato di
     * trasferimento che si sta scollegando Pre: E' stata generata la richiesta
     * di eliminare un dettaglio di distinta relativo ad un documento contabile
     * (mandato o reversale) Post: Il dettaglio e' stato cancellato e lo
     * stato_trasmissione del doc. contabile associato a tale dettaglio viene
     * aggiornato a NON INSERITO IN DISTINTA
     *
     * @param userContext  lo UserContext che ha generato la richiesta
     * @param distinta     la Distinta_cassiereBulk per cui cancellare il dettaglio
     * @param docContabile il mandato/reversale da cancellare dalla distinta
     */
    public void eliminaDettaglioDistinteCollegate(
            UserContext userContext,
            Distinta_cassiereBulk distinta, V_mandato_reversaleBulk docContabile)
            throws ComponentException {
        try {
            if (Utility.createParametriCnrComponentSession().getParametriCnr(
                    userContext, distinta.getEsercizio()).getFl_siope()
                    .booleanValue()
                    && docContabile.isMandatoAccreditamento()) {
                Ass_mandato_reversaleHome assHome = (Ass_mandato_reversaleHome) getHome(
                        userContext, Ass_mandato_reversaleBulk.class);
                Collection listReversali = assHome.findReversali(userContext,
                        new MandatoBulk(docContabile.getCd_cds(), docContabile
                                .getEsercizio(), docContabile
                                .getPg_documento_cont()));

                for (Iterator j = listReversali.iterator(); j.hasNext(); ) {
                    Ass_mandato_reversaleBulk assBulk = (Ass_mandato_reversaleBulk) j
                            .next();
                    ReversaleBulk reversale = (ReversaleBulk) getHome(
                            userContext, ReversaleIBulk.class)
                            .findByPrimaryKey(
                                    new ReversaleBulk(assBulk
                                            .getCd_cds_reversale(), assBulk
                                            .getEsercizio_reversale(), assBulk
                                            .getPg_reversale()));
                    if (!reversale.getCd_tipo_documento_cont().equals(
                            Numerazione_doc_contBulk.TIPO_REV_PROVV)) {
                        V_mandato_reversaleBulk docContabileAssociato = (V_mandato_reversaleBulk) getHome(
                                userContext, V_mandato_reversaleBulk.class)
                                .findByPrimaryKey(
                                        new V_mandato_reversaleBulk(reversale
                                                .getEsercizio(), reversale
                                                .getCd_tipo_documento_cont(),
                                                reversale.getCd_cds(),
                                                reversale.getPg_reversale()));

                        Distinta_cassiere_detHome distHome = (Distinta_cassiere_detHome) getHome(
                                userContext, Distinta_cassiere_detBulk.class);
                        Collection listDistinteReversali = distHome
                                .getDettaglioDistinta(userContext, reversale);

                        for (Iterator x = listDistinteReversali.iterator(); x
                                .hasNext(); ) {
                            Distinta_cassiereBulk distintaReversale = ((Distinta_cassiere_detBulk) x
                                    .next()).getDistinta();

                            eliminaDettaglioDistinta(userContext,
                                    distintaReversale, docContabileAssociato);
                            eliminaMandatiEReversaliCollegati(userContext,
                                    distintaReversale, docContabileAssociato);

                            SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
                                    userContext, distintaReversale,
                                    V_mandato_reversaleBulk.class, null);
                            List list = getHome(userContext,
                                    V_mandato_reversaleBulk.class)
                                    .fetchAll(sql);
                            if (list.isEmpty()) {
                                distintaReversale.setToBeDeleted();
                                deleteBulk(userContext, distintaReversale);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Elimina dalle distinte dei CDS le reversali associate al mandato di
     * trasferimento che si sta scollegando
     * <p>
     * Nome: elimina dalle distinte le reversali associate al mandato di
     * trasferimento che si sta scollegando Pre: E' stata generata la richiesta
     * di eliminare un dettaglio di distinta relativo ad un documento contabile
     * (mandato o reversale) Post: Il dettaglio e' stato cancellato e lo
     * stato_trasmissione del doc. contabile associato a tale dettaglio viene
     * aggiornato a NON INSERITO IN DISTINTA
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinta    la Distinta_cassiereBulk per cui cancellare il dettaglio
     */
    public void eliminaTuttiDettagliDistinteCollegate(
            UserContext userContext, Distinta_cassiereBulk distinta)
            throws ComponentException {
        try {
            if (Utility.createParametriCnrComponentSession().getParametriCnr(
                    userContext, distinta.getEsercizio()).getFl_siope()
                    .booleanValue()) {
                V_mandato_reversaleHome home = (V_mandato_reversaleHome) getHome(
                        userContext, V_mandato_reversaleBulk.class);
                SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
                        userContext, distinta, V_mandato_reversaleBulk.class,
                        null);
                if (sql != null) {
                    List list = home.fetchAll(sql);

                    for (Iterator i = list.iterator(); i.hasNext(); )
                        eliminaDettaglioDistinteCollegate(userContext, distinta,
                                (V_mandato_reversaleBulk) i.next());
                }
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea un dettaglio di distinta
     * <p>
     * Nome: creazione dettaglio Pre: E' stata generata la richiesta di
     * inserimento di un nuovo dettaglio di distinta relativo al documento
     * contabile (mandato o reversale) a partire dal progerssivo dettaglio
     * last_pg_dettaglio Post: Un nuovo dettaglio distinta e' stato creato e lo
     * stato_trasmissione del doc. contabile associato a tale dettaglio viene
     * aggiornato a INSERITO IN DISTINTA; il progressivo dettaglio viene
     * incrementato di 1.
     *
     * @param userContext  lo UserContext che ha generato la richiesta
     * @param distinta     la Distinta_cassiereBulk per cui creare il dettaglio
     * @param docContabile il mandato/reversale da inserire in distinta
     * @return last_pg_dettaglio + 1
     */
    private void inserisciDettaglioDistinteCollegate(
            UserContext userContext,
            Distinta_cassiereBulk distinta, V_mandato_reversaleBulk docContabile)
            throws ComponentException {
        try {
            if (Utility.createParametriCnrComponentSession().getParametriCnr(
                    userContext, distinta.getEsercizio()).getFl_siope()
                    .booleanValue()
                    && docContabile.isMandatoAccreditamento()) {
                Ass_mandato_reversaleHome assHome = (Ass_mandato_reversaleHome) getHome(
                        userContext, Ass_mandato_reversaleBulk.class);
                Collection listReversali = assHome.findReversali(userContext,
                        new MandatoBulk(docContabile.getCd_cds(), docContabile
                                .getEsercizio(), docContabile
                                .getPg_documento_cont()));

                for (Iterator j = listReversali.iterator(); j.hasNext(); ) {
                    Ass_mandato_reversaleBulk assBulk = (Ass_mandato_reversaleBulk) j
                            .next();
                    ReversaleBulk reversale = (ReversaleBulk) getHome(
                            userContext, ReversaleIBulk.class)
                            .findByPrimaryKey(
                                    new ReversaleBulk(assBulk
                                            .getCd_cds_reversale(), assBulk
                                            .getEsercizio_reversale(), assBulk
                                            .getPg_reversale()));
                    if (!reversale.getCd_tipo_documento_cont().equals(
                            Numerazione_doc_contBulk.TIPO_REV_PROVV)) {
                        V_mandato_reversaleBulk docContabileAssociato = (V_mandato_reversaleBulk) getHome(
                                userContext, V_mandato_reversaleBulk.class)
                                .findByPrimaryKey(
                                        new V_mandato_reversaleBulk(reversale
                                                .getEsercizio(), reversale
                                                .getCd_tipo_documento_cont(),
                                                reversale.getCd_cds(),
                                                reversale.getPg_reversale()));

                        Distinta_cassiere_detHome distHome = (Distinta_cassiere_detHome) getHome(
                                userContext, Distinta_cassiere_detBulk.class);
                        Collection listDistinteReversali = distHome
                                .getDettaglioDistinta(userContext, reversale);

                        if (listDistinteReversali.isEmpty()) {
                            Distinta_cassiereBulk distintaRev = findDistintaCollegataCreata(
                                    userContext, distinta, reversale
                                            .getUnita_organizzativa());

                            if (distintaRev == null) {
                                try {
                                    distintaRev = new Distinta_cassiereBulk();
                                    distintaRev.setEsercizio(reversale
                                            .getEsercizio());
                                    distintaRev
                                            .setUnita_organizzativa(reversale
                                                    .getUnita_organizzativa());
                                    distintaRev.setToBeCreated();
                                    distintaRev = inizializzaDistintaPerInserimento(
                                            userContext,
                                            distintaRev,
                                            (CdsBulk) getHome(userContext,
                                                    CdsBulk.class)
                                                    .findByPrimaryKey(
                                                            new CdsBulk(
                                                                    reversale
                                                                            .getCd_cds())));
                                    distintaRev.setFl_flusso(Boolean.FALSE);
                                    distintaRev.setFl_sepa(Boolean.FALSE);
                                    insertBulk(userContext, distintaRev);
                                } catch (ApplicationException e) {
                                    if (e.getDetail() instanceof BusyRecordException)
                                        throw new ApplicationException(
                                                "Attenzione! L'unità organizzativa "
                                                        + reversale
                                                        .getUnita_organizzativa()
                                                        .getCd_unita_organizzativa()
                                                        + " sta effettuando operazioni sulle distinte. Impossibile creare la distinta per le reversali di regolarizzazione associate. Ripetere successivamente l'operazione o scollegare il mandato dalla distinta.");
                                    throw e;
                                }
                            }

                            Long last_pg_dettaglio = ((Distinta_cassiere_detHome) getHome(
                                    userContext,
                                    Distinta_cassiere_detBulk.class))
                                    .getUltimoPg_Dettaglio(userContext,
                                            distintaRev);
                            Distinta_cassiere_detBulk dettaglio = new Distinta_cassiere_detBulk(
                                    distintaRev.getCd_cds(), distintaRev
                                    .getCd_unita_organizzativa(),
                                    distintaRev.getEsercizio(),
                                    last_pg_dettaglio, distintaRev
                                    .getPg_distinta());
                            dettaglio.setPg_reversale(reversale
                                    .getPg_reversale());
                            dettaglio
                                    .setUser(CNRUserContext
                                            .getUser(userContext));
                            insertBulk(userContext, dettaglio);
                            aggiornaStatoDocContabile(userContext,
                                    docContabileAssociato,
                                    ReversaleBulk.STATO_TRASMISSIONE_INSERITO);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private Distinta_cassiereBulk findDistintaCollegataCreata(
            UserContext userContext,
            Distinta_cassiereBulk distinta,
            Unita_organizzativaBulk unitaOrganizzativa)
            throws ComponentException {
        try {
            SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
                    userContext, distinta, V_mandato_reversaleBulk.class, null);
            List list = getHome(userContext, V_mandato_reversaleBulk.class)
                    .fetchAll(sql);

            for (Iterator i = list.iterator(); i.hasNext(); ) {
                V_mandato_reversaleBulk docContabile = (V_mandato_reversaleBulk) i
                        .next();
                if (docContabile.isMandatoAccreditamento()) {
                    Ass_mandato_reversaleHome assHome = (Ass_mandato_reversaleHome) getHome(
                            userContext, Ass_mandato_reversaleBulk.class);
                    Collection listReversali = assHome.findReversali(
                            userContext, new MandatoBulk(docContabile
                                    .getCd_cds(), docContabile.getEsercizio(),
                                    docContabile.getPg_documento_cont()));

                    for (Iterator j = listReversali.iterator(); j.hasNext(); ) {
                        Ass_mandato_reversaleBulk assBulk = (Ass_mandato_reversaleBulk) j
                                .next();
                        ReversaleBulk reversale = (ReversaleBulk) getHome(
                                userContext, ReversaleIBulk.class)
                                .findByPrimaryKey(
                                        new ReversaleBulk(assBulk
                                                .getCd_cds_reversale(), assBulk
                                                .getEsercizio_reversale(),
                                                assBulk.getPg_reversale()));
                        if (reversale.getUnita_organizzativa()
                                .equalsByPrimaryKey(unitaOrganizzativa)
                                && !reversale
                                .getCd_tipo_documento_cont()
                                .equals(
                                        Numerazione_doc_contBulk.TIPO_REV_PROVV)
                                && reversale
                                .getStato_trasmissione()
                                .equals(
                                        ReversaleBulk.STATO_TRASMISSIONE_INSERITO)) {

                            Collection listDistinteReversali = ((Distinta_cassiere_detHome) getHome(
                                    userContext,
                                    Distinta_cassiere_detBulk.class))
                                    .getDettaglioDistinta(userContext,
                                            reversale);

                            for (Iterator x = listDistinteReversali.iterator(); x
                                    .hasNext(); ) {
                                Distinta_cassiereBulk distintaReversale = ((Distinta_cassiere_detBulk) x
                                        .next()).getDistinta();
                                if (distintaReversale.getDt_invio() == null)
                                    return distintaReversale;
                            }
                        }
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private Collection<Distinta_cassiereBulk> findDistinteCollegateCreate(
            UserContext userContext, Distinta_cassiereBulk distinta)
            throws ComponentException {
        try {
            Collection<Distinta_cassiereBulk> collection = new BulkList();

            SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
                    userContext, distinta, V_mandato_reversaleBulk.class, null);
            List list = getHome(userContext, V_mandato_reversaleBulk.class)
                    .fetchAll(sql);

            for (Iterator i = list.iterator(); i.hasNext(); ) {
                V_mandato_reversaleBulk docContabile = (V_mandato_reversaleBulk) i
                        .next();
                if (docContabile.isMandatoAccreditamento()) {
                    Ass_mandato_reversaleHome assHome = (Ass_mandato_reversaleHome) getHome(
                            userContext, Ass_mandato_reversaleBulk.class);
                    Collection listReversali = assHome.findReversali(
                            userContext, new MandatoBulk(docContabile
                                    .getCd_cds(), docContabile.getEsercizio(),
                                    docContabile.getPg_documento_cont()));

                    for (Iterator j = listReversali.iterator(); j.hasNext(); ) {
                        Ass_mandato_reversaleBulk assBulk = (Ass_mandato_reversaleBulk) j
                                .next();
                        ReversaleBulk reversale = (ReversaleBulk) getHome(
                                userContext, ReversaleIBulk.class)
                                .findByPrimaryKey(
                                        new ReversaleBulk(assBulk
                                                .getCd_cds_reversale(), assBulk
                                                .getEsercizio_reversale(),
                                                assBulk.getPg_reversale()));
                        if (!reversale.getCd_tipo_documento_cont().equals(
                                Numerazione_doc_contBulk.TIPO_REV_PROVV)
                                && reversale
                                .getStato_trasmissione()
                                .equals(
                                        ReversaleBulk.STATO_TRASMISSIONE_INSERITO)) {

                            Collection listDistinteReversali = ((Distinta_cassiere_detHome) getHome(
                                    userContext,
                                    Distinta_cassiere_detBulk.class))
                                    .getDettaglioDistinta(userContext,
                                            reversale);

                            for (Iterator x = listDistinteReversali.iterator(); x
                                    .hasNext(); )
                                collection.add(((Distinta_cassiere_detBulk) x
                                        .next()).getDistinta());
                        }
                    }
                }
            }
            return collection;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private boolean isCreateByOtherUo(UserContext userContext,
                                      Distinta_cassiereBulk distinta) throws ComponentException {
        try {
            SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
                    userContext, distinta, V_mandato_reversaleBulk.class, null);
            List list = getHome(userContext, V_mandato_reversaleBulk.class)
                    .fetchAll(sql);

            if (Utility.createParametriCnrComponentSession().getParametriCnr(
                    userContext,
                    CNRUserContext
                            .getEsercizio(userContext)).getFl_siope()
                    .booleanValue()) {
                for (Iterator i = list.iterator(); i.hasNext(); )
                    if (((V_mandato_reversaleBulk) i.next())
                            .isReversaleTrasferimento())
                        return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea il SQLBuilder per recuperare tutti dettagli delle distinte collegate
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: cerca i dettagli delle distinte collegate a quella indicata Pre:
     * Una richiesta di ricerca dei dettagli delle distinte collegate a quella
     * indicata e' stata generata Post: Viene creato il SQLBuilder che consente
     * di recuperare le istanze di V_mandato_reversaleBulk che contengono tutte
     * le informazioni dei mandati e delle reversali inseriti nelle distinte
     * collegate a quella indicata.
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinta    la Distinta_cassiereBulk di cui ricercare i dettagli
     * @param bulkClass
     * @param clauses
     * @return SQLBuilder
     */
    public SQLBuilder selectDistinte_cassiere_detCollegateCollByClause(
            UserContext userContext, Distinta_cassiereBulk distinta,
            Class bulkClass, CompoundFindClause clauses)
            throws ComponentException {
        try {
            if (distinta.getPg_distinta() == null)
                return null;

            SQLBuilder sql2 = selectDistinte_cassiere_detCollegate(userContext,
                    distinta);
            List list = getHome(userContext, Distinta_cassiere_detBulk.class)
                    .fetchAll(sql2);

            SQLBuilder sql = getHome(userContext,
                    V_mandato_reversaleBulk.class,
                    "V_MANDATO_REVERSALE_DISTINTA").createSQLBuilder();
            if (!list.isEmpty()) {
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Distinta_cassiere_detBulk det = (Distinta_cassiere_detBulk) i
                            .next();
                    if (det.getPg_reversale() != null) {
                        sql.openParenthesis("OR");
                        sql.addSQLClause("AND",
                                "V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO",
                                SQLBuilder.EQUALS, det.getEsercizio());
                        sql.addSQLClause("AND",
                                "V_MANDATO_REVERSALE_DISTINTA.CD_CDS",
                                SQLBuilder.EQUALS, det.getCd_cds());
                        sql
                                .addSQLClause(
                                        "AND",
                                        "V_MANDATO_REVERSALE_DISTINTA.CD_TIPO_DOCUMENTO_CONT",
                                        SQLBuilder.EQUALS,
                                        Numerazione_doc_contBulk.TIPO_REV);
                        sql
                                .addSQLClause(
                                        "AND",
                                        "V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT",
                                        SQLBuilder.EQUALS, det
                                                .getPg_reversale());
                        sql.closeParenthesis();
                    }
                }
                sql
                        .addOrderBy(" V_MANDATO_REVERSALE_DISTINTA.CD_CDS, V_MANDATO_REVERSALE_DISTINTA.PG_DOCUMENTO_CONT ");
            } else
                sql.addSQLClause("AND",
                        "V_MANDATO_REVERSALE_DISTINTA.ESERCIZIO IS NULL");
            return sql;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea il SQLBuilder per recuperare tutti dettagli delle distinte collegate
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: cerca i dettagli delle distinte collegate a quella indicata Pre:
     * Una richiesta di ricerca dei dettagli delle distinte collegate a quella
     * indicata e' stata generata Post: Viene creato il SQLBuilder che consente
     * di recuperare le istanze di V_mandato_reversaleBulk che contengono tutte
     * le informazioni dei mandati e delle reversali inseriti nelle distinte
     * collegate a quella indicata.
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param distinta    la Distinta_cassiereBulk di cui ricercare i dettagli
     * @return SQLBuilder
     */
    private SQLBuilder selectDistinte_cassiere_detCollegate(
            UserContext userContext, Distinta_cassiereBulk distinta)
            throws ComponentException {
        if (distinta.getPg_distinta() == null)
            return null;

        SQLBuilder sql = getHome(userContext, Distinta_cassiere_detBulk.class)
                .createSQLBuilder();
        sql.addTableToHeader("DISTINTA_CASSIERE_DET", "DCMAN");
        sql.addTableToHeader("ASS_MANDATO_REVERSALE", "ASS");
        sql.addTableToHeader("MANDATO", "MAN");

        sql.addSQLClause("AND", "DCMAN.ESERCIZIO", SQLBuilder.EQUALS, distinta
                .getEsercizio());
        sql.addSQLClause("AND", "DCMAN.CD_CDS", SQLBuilder.EQUALS, distinta
                .getCd_cds());
        sql.addSQLClause("AND", "DCMAN.CD_UNITA_ORGANIZZATIVA",
                SQLBuilder.EQUALS, distinta.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "DCMAN.PG_DISTINTA", SQLBuilder.EQUALS,
                distinta.getPg_distinta());

        sql.addSQLJoin("DCMAN.ESERCIZIO", "MAN.ESERCIZIO");
        sql.addSQLJoin("DCMAN.CD_CDS", "MAN.CD_CDS");
        sql.addSQLJoin("DCMAN.PG_MANDATO", "MAN.PG_MANDATO");

        sql.addSQLClause("AND", "MAN.TI_MANDATO", SQLBuilder.EQUALS,
                MandatoBulk.TIPO_ACCREDITAMENTO);

        sql.addSQLJoin("MAN.ESERCIZIO", "ASS.ESERCIZIO_MANDATO");
        sql.addSQLJoin("MAN.CD_CDS", "ASS.CD_CDS_MANDATO");
        sql.addSQLJoin("MAN.PG_MANDATO", "ASS.PG_MANDATO");

        sql.addSQLJoin("ASS.ESERCIZIO_REVERSALE",
                "DISTINTA_CASSIERE_DET.ESERCIZIO");
        sql.addSQLJoin("ASS.CD_CDS_REVERSALE", "DISTINTA_CASSIERE_DET.CD_CDS");
        sql
                .addSQLJoin("ASS.PG_REVERSALE",
                        "DISTINTA_CASSIERE_DET.PG_REVERSALE");

        return sql;
    }

    public RemoteIterator selectFileScarti(UserContext userContext,
                                           Ext_cassiere00_logsBulk logs) throws ComponentException,
            RemoteException {
        SQLBuilder sql = getHome(userContext, Ext_cassiere00_scartiBulk.class)
                .createSQLBuilder();
        // sql.setDistinctClause(true);
        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, logs.getEsercizio());
        sql.addSQLClause("AND", "NOME_FILE", SQLBuilder.EQUALS, logs.getNome_file());
        sql.addSQLClause("AND", "PG_ESECUZIONE", SQLBuilder.EQUALS, logs
                .getPg_esecuzione());
        RemoteIterator ri = iterator(userContext, sql,
                Ext_cassiere00_scartiBulk.class, null);
        return ri;
    }

    public void caricaFile(UserContext userContext, File file)
            throws ComponentException {
        try {

            if (((EsercizioComponentSession) EJBCommonServices
                    .createEJB("CNRCONFIG00_EJB_EsercizioComponentSession",
                            EsercizioComponentSession.class))
                    .isEsercizioChiuso(userContext))
                throw new ApplicationException(
                        "Funzione non disponibile ad esercizio chiuso.");

            String eserc_scirvania = CNRUserContext
                    .getEsercizio(userContext).toString();
            Bframe_blobBulk bframe_blob = new Bframe_blobBulk("INT_CASS00",
                    new File(parseFilename(file)).getName(), "ritorno/"
                    + eserc_scirvania + "/");
            BulkHome home = getHome(userContext, Bframe_blobBulk.class);
            bframe_blob.setUser(userContext.getUser());
            home.insert(bframe_blob, userContext);
            home.setSQLLob(bframe_blob, "CDATA", IOUtils.toString(new FileInputStream(file), "UTF-8"));
            home.update(bframe_blob, userContext);
        } catch (Throwable e) {
            throw handleException(e);
        } finally {
            file.delete();
        }
    }

    private String parseFilename(File file) {

        StringTokenizer fileName = new StringTokenizer(file.getName(), "\\",
                false);
        String newFileName = null;

        while (fileName.hasMoreTokens()) {
            if (newFileName == null) {
                newFileName = fileName.nextToken();
            } else
                newFileName = newFileName + "/" + fileName.nextToken();
        }

        if (newFileName != null)
            return newFileName;

        return file.getName();
    }

    public ExtCassiereCdsBulk recuperaCodiciCdsCassiere(UserContext userContext, Distinta_cassiereBulk distinta) throws ComponentException, PersistencyException, EJBException {
        try {
            ExtCassiereCdsBulk bulk = new ExtCassiereCdsBulk();
            bulk.setEsercizio(distinta.getEsercizio());
            if (!tesoreriaUnica(userContext, distinta))
                bulk.setCdCds(distinta.getCd_cds());
            else if (distinta.getCd_cds_ente() != null)
                bulk.setCdCds(distinta.getCd_cds_ente());
            List oggetti = null;
            oggetti = getHome(userContext, ExtCassiereCdsBulk.class).find(bulk);
            if (oggetti.size() == 0)
                throw new ApplicationException("Configurazione mancante dati cassiere");
            else if (oggetti.size() > 1)
                throw new ApplicationException("Configurazione errata dati cassiere");
            else
                return (ExtCassiereCdsBulk) oggetti.get(0);

        } catch (Exception e) {
            throw handleException(e);

        }
    }


    public BancaBulk recuperaIbanUo(UserContext userContext, Unita_organizzativaBulk uo) throws ComponentException,
            PersistencyException {
        SQLBuilder sql = getHome(userContext, BancaBulk.class).createSQLBuilder();
        sql.addTableToHeader("TERZO");
        sql.addSQLClause("AND", "TERZO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uo.getCd_unita_organizzativa());
        sql.addSQLJoin("BANCA.CD_TERZO", "TERZO.CD_TERZO");
        sql.addSQLClause("AND", "TERZO.DT_FINE_RAPPORTO", SQLBuilder.ISNULL, null);
        try {
            if (!Utility.createParametriCnrComponentSession().getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext)).getFl_tesoreria_unica().booleanValue())
                sql.addSQLClause("AND", "BANCA.FL_CC_CDS", SQLBuilder.EQUALS, "Y");
            else {
                Configurazione_cnrBulk config = new Configurazione_cnrBulk(
                        "CONTO_CORRENTE_SPECIALE",
                        "ENTE",
                        "*",
                        new Integer(0));
                Configurazione_cnrHome home = (Configurazione_cnrHome) getHome(userContext, config);
                List configurazioni = home.find(config);
                if ((configurazioni != null) && (configurazioni.size() == 1)) {
                    Configurazione_cnrBulk configBanca = (Configurazione_cnrBulk) configurazioni.get(0);
                    sql.addSQLClause("AND", "BANCA.ABI", SQLBuilder.EQUALS, configBanca.getVal01());
                    sql.addSQLClause("AND", "BANCA.CAB", SQLBuilder.EQUALS, configBanca.getVal02());
                    sql.addSQLClause("AND", "BANCA.NUMERO_CONTO", SQLBuilder.CONTAINS, configBanca.getVal03());
                }
            }
        } catch (Exception e) {
            throw handleException(e);
        }
        sql.addSQLClause("AND", "BANCA.FL_CANCELLATO", SQLBuilder.EQUALS, "N");
        sql.addSQLClause("AND", "CODICE_IBAN", SQLBuilder.ISNOTNULL, null);
        if (getHome(userContext, BancaBulk.class).fetchAll(sql).size() == 0)
            throw new ApplicationException("Configurazione iban uo mancante");
        else
            return
                    (BancaBulk) getHome(userContext, BancaBulk.class).fetchAll(sql).get(0);
    }

    public List dettagliDistinta(UserContext usercontext, Distinta_cassiereBulk distinta, String tipo) throws PersistencyException, ComponentException {
        try {
            SQLBuilder sql = getHome(usercontext, V_mandato_reversaleBulk.class).createSQLBuilder();
            sql.addTableToHeader("DISTINTA_CASSIERE_DET");
            sql.addSQLClause("AND", "DISTINTA_CASSIERE_DET.ESERCIZIO", SQLBuilder.EQUALS, distinta.getEsercizio());
            sql.addSQLClause("AND", "DISTINTA_CASSIERE_DET.CD_CDS", SQLBuilder.EQUALS, distinta.getCd_cds());
            sql.addSQLClause("AND", "DISTINTA_CASSIERE_DET.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, distinta.getCd_unita_organizzativa());
            sql.addSQLClause("AND", "DISTINTA_CASSIERE_DET.PG_DISTINTA", SQLBuilder.EQUALS, distinta.getPg_distinta());

            sql.addSQLJoin("V_MANDATO_REVERSALE.ESERCIZIO", "DISTINTA_CASSIERE_DET.ESERCIZIO");

            if (!Utility.createParametriCnrComponentSession().getParametriCnr(usercontext, CNRUserContext.getEsercizio(usercontext)).getFl_tesoreria_unica().booleanValue())
                sql.addSQLJoin("V_MANDATO_REVERSALE.CD_CDS", "DISTINTA_CASSIERE_DET.CD_CDS");
            else
                sql.addSQLJoin("V_MANDATO_REVERSALE.CD_CDS", "DISTINTA_CASSIERE_DET.CD_CDS_ORIGINE");

            // da committare
            //sql.addSQLJoin("V_MANDATO_REVERSALE.CD_UNITA_ORGANIZZATIVA","DISTINTA_CASSIERE_DET.CD_UNITA_ORGANIZZATIVA");

            if (tipo.compareTo(Numerazione_doc_contBulk.TIPO_MAN) == 0) {
                sql.addSQLClause("AND", "V_MANDATO_REVERSALE.CD_TIPO_DOCUMENTO_CONT", SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);
                sql.addSQLJoin("DISTINTA_CASSIERE_DET.PG_MANDATO", SQLBuilder.EQUALS, "V_MANDATO_REVERSALE.PG_DOCUMENTO_CONT");
            } else {
                sql.addSQLClause("AND", "V_MANDATO_REVERSALE.CD_TIPO_DOCUMENTO_CONT", SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_REV);
                sql.addSQLJoin("DISTINTA_CASSIERE_DET.PG_REVERSALE", SQLBuilder.EQUALS, "V_MANDATO_REVERSALE.PG_DOCUMENTO_CONT");
            }
            return getHome(usercontext, V_mandato_reversaleBulk.class).fetchAll(sql);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    protected boolean tesoreriaUnica(UserContext userContext,
                                     Distinta_cassiereBulk distinta) throws SQLException,
            ComponentException, PersistencyException, EJBException,
            RemoteException {
        return (Utility.createParametriCnrComponentSession().getParametriCnr(userContext, distinta.getEsercizio()).getFl_tesoreria_unica().booleanValue());
    }

    private boolean isAttivoSiopeplus(UserContext userContext) throws RemoteException, ComponentException {
        return Optional.ofNullable(((Configurazione_cnrComponentSession) EJBCommonServices
                .createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession")).getVal01(
                userContext,
                CNRUserContext.getEsercizio(userContext),
                null,
                Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                Configurazione_cnrBulk.SK_ATTIVO_SIOPEPLUS))
                .map(s -> Boolean.valueOf(s))
                .orElse(Boolean.FALSE);
    }

    public Distinta_cassiereBulk inviaDistinta(UserContext userContext,
                                               Distinta_cassiereBulk distinta)
            throws ComponentException {
        try {
            if (distinta == null)
                throw new ApplicationException(
                        "Attenzione! La distinta e' stata cancellata");
            validaCreaModificaConBulk(userContext, distinta);
            verificaDuplicazioniProgDocCont(userContext, distinta);
            // aggiungo i mandati da ritrasmettere
            if (!tesoreriaUnica(userContext, distinta)) {
                aggiungiMandatiEReversaliDaRitrasmettere(userContext, distinta);
            }
            assegnaProgressivoCassiere(userContext, distinta);

            if (isAttivoSiopeplus(userContext)) {
                generaFlussoSiopeplus(userContext, distinta);
            } else {
                // aggiorno lo stato trasmissione di mandati/reversali
                aggiornaStatoDocContabili(userContext, distinta, MandatoBulk.STATO_TRASMISSIONE_TRASMESSO);

                aggiornaStoricoTrasmessi(userContext, distinta);

                distinta.setDt_invio(DateServices.getDt_valida(userContext));
            }
            distinta.setStato(Distinta_cassiereBulk.Stato.DEFINITIVA);
            distinta.setUser(userContext.getUser());
            distinta.setToBeUpdated();
            makeBulkPersistent(userContext, distinta);
            if (!isAttivoSiopeplus(userContext))
                aggiornaDataDiffDocamm(userContext, distinta, MandatoBulk.STATO_TRASMISSIONE_TRASMESSO);
            return distinta;

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public Distinta_cassiereBulk inviaDistintaSiopePlus(UserContext userContext, Distinta_cassiereBulk distinta, Integer progFlusso) throws ComponentException {
        try {
            // aggiorno lo stato trasmissione di mandati/reversali
            aggiornaStatoDocContabili(userContext, distinta, MandatoBulk.STATO_TRASMISSIONE_TRASMESSO);

            aggiornaStoricoTrasmessi(userContext, distinta);

            distinta.setProgFlusso(progFlusso);
            distinta.setDt_invio(DateServices.getDt_valida(userContext));
            distinta.setStato(Distinta_cassiereBulk.Stato.TRASMESSA);
            distinta.setToBeUpdated();
            makeBulkPersistent(userContext, distinta);
            aggiornaDataDiffDocamm(userContext, distinta, MandatoBulk.STATO_TRASMISSIONE_TRASMESSO);
            return distinta;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public List<V_mandato_reversaleBulk> findMandatiCollegati(UserContext usercontext, V_mandato_reversaleBulk v_mandato_reversaleBulk) throws ComponentException {
        try {
            return ((V_mandato_reversaleHome) getHome(usercontext, V_mandato_reversaleBulk.class)).findMandatiCollegati(v_mandato_reversaleBulk);
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public List<V_mandato_reversaleBulk> findReversaliCollegate(UserContext usercontext, V_mandato_reversaleBulk v_mandato_reversaleBulk) throws ComponentException {
        try {
            return ((V_mandato_reversaleHome) getHome(usercontext, V_mandato_reversaleBulk.class)).findReversaliCollegate(v_mandato_reversaleBulk);
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }


    public List findDocumentiFlusso(UserContext usercontext, V_mandato_reversaleBulk bulk) throws ComponentException {
        VDocumentiFlussoHome home = (VDocumentiFlussoHome) getHome(usercontext, VDocumentiFlussoBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.setDistinctClause(true);
        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, bulk.getEsercizio());
        sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, bulk.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "PG_DOCUMENTO", SQLBuilder.EQUALS, bulk.getPg_documento_cont());
        sql.addSQLClause("AND", "CD_TIPO_DOCUMENTO_CONT", SQLBuilder.EQUALS, bulk.getCd_tipo_documento_cont());
        try {
            return home.fetchAll(sql);
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public List findDocumentiFlussoClass(UserContext usercontext, V_mandato_reversaleBulk bulk) throws ComponentException {
        VDocumentiFlussoHome homeClass = (VDocumentiFlussoHome)
                getHome(usercontext, VDocumentiFlussoBulk.class, "CLASSIFICAZIONE");
        SQLBuilder sqlClass = homeClass.createSQLBuilder();
        sqlClass.resetColumns();
        sqlClass.addColumn("ESERCIZIO");
        sqlClass.addColumn("CD_UNITA_ORGANIZZATIVA");
        sqlClass.addColumn("PG_DOCUMENTO");
        sqlClass.addColumn("CD_SIOPE");
        sqlClass.addColumn("CD_CUP");
        sqlClass.addColumn("IM_DOCUMENTO");
        sqlClass.addColumn("CD_TIPO_DOCUMENTO_AMM");
        sqlClass.addColumn("PG_DOC_AMM");
        sqlClass.addColumn("IMPORTO_CGE", "IMPORTO_CGE");
        sqlClass.addColumn("IMPORTO_CUP", "IMPORTO_CUP");
        sqlClass.setDistinctClause(true);
        sqlClass.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, bulk.getEsercizio());
        sqlClass.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, bulk.getCd_unita_organizzativa());
        sqlClass.addSQLClause("AND", "PG_DOCUMENTO", SQLBuilder.EQUALS, bulk.getPg_documento_cont());
        sqlClass.addSQLClause("AND", "CD_TIPO_DOCUMENTO_CONT", SQLBuilder.EQUALS, bulk.getCd_tipo_documento_cont());
        sqlClass.setOrderBy("cdSiope", it.cnr.jada.util.OrderConstants.ORDER_ASC);
        sqlClass.setOrderBy("cdCup", it.cnr.jada.util.OrderConstants.ORDER_ASC);
        try {
            return homeClass.fetchAll(sqlClass);
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public List findDocumentiFlussoSospeso(UserContext usercontext, V_mandato_reversaleBulk bulk) throws ComponentException {
        VDocumentiFlussoHome homeSosp = (VDocumentiFlussoHome) getHome(usercontext, VDocumentiFlussoBulk.class, "SOSPESO");
        SQLBuilder sqlSosp = homeSosp.createSQLBuilder();
        sqlSosp.resetColumns();
        sqlSosp.addColumn("ESERCIZIO");
        sqlSosp.addColumn("CD_UNITA_ORGANIZZATIVA");
        sqlSosp.addColumn("PG_DOCUMENTO");
        sqlSosp.addColumn("DT_REGISTRAZIONE_SOSP");
        sqlSosp.addColumn("TI_ENTRATA_SPESA");
        sqlSosp.addColumn("CD_SOSPESO");
        sqlSosp.addColumn("IM_SOSPESO");
        sqlSosp.addColumn("IM_ASSOCIATO");
        sqlSosp.setDistinctClause(true);
        sqlSosp.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, bulk.getEsercizio());
        sqlSosp.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, bulk.getCd_unita_organizzativa());
        sqlSosp.addSQLClause("AND", "PG_DOCUMENTO", SQLBuilder.EQUALS, bulk.getPg_documento_cont());
        sqlSosp.addSQLClause("AND", "CD_TIPO_DOCUMENTO_CONT", SQLBuilder.EQUALS, bulk.getCd_tipo_documento_cont());
        sqlSosp.setOrderBy("cdSospeso", it.cnr.jada.util.OrderConstants.ORDER_ASC);
        try {
            return homeSosp.fetchAll(sqlSosp);
        } catch (PersistencyException e) {
            throw handleException(e);
        }

    }

    public List findReversali(UserContext usercontext, V_mandato_reversaleBulk bulk) throws ComponentException {
        SQLBuilder sql2 = getHome(usercontext, V_mandato_reversaleBulk.class).createSQLBuilder();
        sql2.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, bulk.getEsercizio());
        sql2.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, bulk.getCd_unita_organizzativa());
        sql2.addSQLClause("AND", "PG_DOCUMENTO_CONT_PADRE", SQLBuilder.EQUALS, bulk.getPg_documento_cont());
        sql2.addSQLClause("AND", "CD_TIPO_DOCUMENTO_CONT", SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_REV);
        sql2.addSQLClause("AND", "CD_TIPO_DOCUMENTO_CONT_PADRE", SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);
        try {
            return getHome(usercontext, V_mandato_reversaleBulk.class).fetchAll(sql2);
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public List findDocumentiFlussoClassReversali(UserContext usercontext, V_mandato_reversaleBulk bulk) throws ComponentException {
        VDocumentiFlussoHome homeClass = (VDocumentiFlussoHome) getHome(usercontext, VDocumentiFlussoBulk.class, "CLASSIFICAZIONE");
        SQLBuilder sqlClass = homeClass.createSQLBuilder();
        sqlClass.resetColumns();
        sqlClass.addColumn("ESERCIZIO");
        sqlClass.addColumn("CD_UNITA_ORGANIZZATIVA");
        sqlClass.addColumn("PG_DOCUMENTO");
        sqlClass.addColumn("CD_SIOPE");
        sqlClass.addColumn("CD_CUP");// sempre null per le Revesali, non gestito dal flusso
        sqlClass.addColumn("CD_TIPO_DOCUMENTO_AMM");
        sqlClass.addColumn("PG_DOC_AMM");
        sqlClass.addColumn("IM_DOCUMENTO");
        sqlClass.addColumn("IMPORTO_CGE");
        sqlClass.addColumn("IMPORTO_CUP"); //sempre 0  per le Revesali, non gestito dal flusso
        sqlClass.setDistinctClause(true);
        sqlClass.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, bulk.getEsercizio());
        sqlClass.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, bulk.getCd_unita_organizzativa());
        sqlClass.addSQLClause("AND", "PG_DOCUMENTO", SQLBuilder.EQUALS, bulk.getPg_documento_cont());
        sqlClass.addSQLClause("AND", "CD_TIPO_DOCUMENTO_CONT", SQLBuilder.EQUALS, bulk.getCd_tipo_documento_cont());
        sqlClass.setOrderBy("cdSiope", it.cnr.jada.util.OrderConstants.ORDER_ASC);
        try {
            return homeClass.fetchAll(sqlClass);
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    private void callCheckDocContForDistintaAnn(UserContext userContext,
                                                Distinta_cassiereBulk distinta)
            throws ComponentException {

        LoggableStatement cs = null;
        try {
            cs = new LoggableStatement(getConnection(userContext), "{ call "
                    + EJBCommonServices.getDefaultSchema()
                    + "CNRCTB750.checkDocContForDistCasAnn(?,?,?,?) }", false,
                    this.getClass());

            cs.setString(1, distinta.getCd_cds());
            cs.setInt(2, distinta.getEsercizio().intValue());
            cs.setString(3, distinta.getCd_unita_organizzativa());
            cs.setLong(4, distinta.getPg_distinta().longValue());

            cs.executeQuery();
        } catch (Throwable e) {
            throw handleException(e);
        } finally {
            try {
                if (cs != null)
                    cs.close();
            } catch (SQLException e) {
                throw handleException(e);
            }
        }
    }

    private void verificaDuplicazioniProgDocCont(UserContext userContext,
                                                 Distinta_cassiereBulk distinta) throws ComponentException {
        try {
            V_mandato_reversaleHome home = (V_mandato_reversaleHome) getHome(
                    userContext, V_mandato_reversaleBulk.class);
            SQLBuilder sql = selectDistinta_cassiere_detCollByClause(
                    userContext, distinta, V_mandato_reversaleBulk.class, null);
            List list = home.fetchAll(sql);
            List lista = home.fetchAll(sql);
            String duplicati = null;
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                V_mandato_reversaleBulk bulk = (V_mandato_reversaleBulk) i.next();
                if (tesoreriaUnica(userContext, distinta)) {
                    Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) EJBCommonServices
                            .createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
                    if (sess.getVal01(userContext, new Integer(0), null, "COSTANTI", "BLOCCO_UNICITA_PG_MANREV") != null &&
                            sess.getVal01(userContext, new Integer(0), null, "COSTANTI", "BLOCCO_UNICITA_PG_MANREV").compareTo("S") == 0) {
                        if (distinta.getFl_flusso().booleanValue()) {
                            for (Iterator iter = lista.iterator(); iter.hasNext(); ) {
                                V_mandato_reversaleBulk copia = (V_mandato_reversaleBulk) iter.next();
                                if (copia.getPg_documento_cont().compareTo(bulk.getPg_documento_cont()) == 0 && copia.getCd_tipo_documento_cont().compareTo(bulk.getCd_tipo_documento_cont()) != 0)
                                    if (duplicati != null) {
                                        if (!duplicati.contains(" " + bulk.getPg_documento_cont()))
                                            duplicati = duplicati + " " + bulk.getPg_documento_cont();
                                    } else
                                        duplicati = "Risultano presenti in distinta sia il mandato che la reversale n. " + bulk.getPg_documento_cont();
                            }
                        }
                    }
                }
            }
            if (duplicati != null)
                throw new ApplicationException(duplicati);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public Rif_modalita_pagamentoBulk findModPag(UserContext userContext, V_mandato_reversaleBulk mandato_reversaleBulk) throws ComponentException {
        SQLBuilder sql = getHome(userContext, Rif_modalita_pagamentoBulk.class).createSQLBuilder();
        if (mandato_reversaleBulk.isMandato()) {
            sql.addTableToHeader("MANDATO_RIGA");
            sql.addSQLJoin("MANDATO_RIGA.CD_MODALITA_PAG", "RIF_MODALITA_PAGAMENTO.CD_MODALITA_PAG");
            sql.addSQLClause(FindClause.AND, "MANDATO_RIGA.CD_CDS", SQLBuilder.EQUALS, mandato_reversaleBulk.getCd_cds());
            sql.addSQLClause(FindClause.AND, "MANDATO_RIGA.ESERCIZIO", SQLBuilder.EQUALS, mandato_reversaleBulk.getEsercizio());
            sql.addSQLClause(FindClause.AND, "MANDATO_RIGA.PG_MANDATO", SQLBuilder.EQUALS, mandato_reversaleBulk.getPg_documento_cont());
        } else {
            sql.addTableToHeader("REVERSALE_RIGA");
            sql.addSQLJoin("REVERSALE_RIGA.CD_MODALITA_PAG", "RIF_MODALITA_PAGAMENTO.CD_MODALITA_PAG");
            sql.addSQLClause(FindClause.AND, "REVERSALE_RIGA.CD_CDS", SQLBuilder.EQUALS, mandato_reversaleBulk.getCd_cds());
            sql.addSQLClause(FindClause.AND, "REVERSALE_RIGA.ESERCIZIO", SQLBuilder.EQUALS, mandato_reversaleBulk.getEsercizio());
            sql.addSQLClause(FindClause.AND, "REVERSALE_RIGA.PG_REVERSALE", SQLBuilder.EQUALS, mandato_reversaleBulk.getPg_documento_cont());
        }
        try {
            final List<Rif_modalita_pagamentoBulk> list = getHome(userContext, Rif_modalita_pagamentoBulk.class).fetchAll(sql);
            return list.stream().distinct().findAny().get();
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }


    public void unlockMessaggiSIOPEPlus(UserContext userContext) throws ComponentException {
        try {
            Configurazione_cnrBulk configurazione_cnrBulk = new Configurazione_cnrBulk(
                    Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                    Configurazione_cnrBulk.SK_ATTIVO_SIOPEPLUS,
                    "*",
                    CNRUserContext.getEsercizio(userContext)
            );
            Configurazione_cnrHome configurazione_cnrHome = (Configurazione_cnrHome) getHome(userContext, Configurazione_cnrBulk.class);
            configurazione_cnrBulk = (Configurazione_cnrBulk) configurazione_cnrHome.findAndLock(configurazione_cnrBulk);
            configurazione_cnrBulk.setVal04("N");
            configurazione_cnrBulk.setToBeUpdated();
            configurazione_cnrHome.update(configurazione_cnrBulk, userContext);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public Configurazione_cnrBulk lockMessaggiSIOPEPlus(UserContext userContext) throws ComponentException {
        try {
            Configurazione_cnrBulk configurazione_cnrBulk = new Configurazione_cnrBulk(
                    Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                    Configurazione_cnrBulk.SK_ATTIVO_SIOPEPLUS,
                    "*",
                    CNRUserContext.getEsercizio(userContext)
            );
            Configurazione_cnrHome configurazione_cnrHome = (Configurazione_cnrHome) getHome(userContext, Configurazione_cnrBulk.class);
            configurazione_cnrBulk = (Configurazione_cnrBulk) configurazione_cnrHome.findAndLock(configurazione_cnrBulk);
            if ("Y".equalsIgnoreCase(configurazione_cnrBulk.getVal04()))
                return null;
            configurazione_cnrBulk.setVal04("Y");
            configurazione_cnrBulk.setToBeUpdated();
            configurazione_cnrHome.update(configurazione_cnrBulk, userContext);
            return configurazione_cnrBulk;
        } catch (BusyResourceException _ex) {
            return null;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public StorageObject generaFlussoSiopeplus(UserContext userContext, Distinta_cassiereBulk distinta) throws ComponentException,
            RemoteException {
        try {
            final DocumentiContabiliService documentiContabiliService = SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class);
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            JAXBContext jc = JAXBContext.newInstance("it.siopeplus");
            // creo i file del flusso
            // Testata
            final ObjectFactory objectFactory = new ObjectFactory();
            FlussoOrdinativi currentFlusso = objectFactory.createFlussoOrdinativi();
            Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) EJBCommonServices
                    .createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");

            String codiceAbi = Optional.ofNullable(
                    sess.getVal01(
                            userContext,
                            CNRUserContext.getEsercizio(userContext),
                            null, Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                            Configurazione_cnrBulk.SK_CODICE_ABI_BT
                    )).orElseThrow(() -> new ApplicationException("Configurazione mancante per flusso Ordinativo [CODICE_ABI_BT]"));
            String codiceA2A = Optional.ofNullable(
                    sess.getVal01(
                            userContext,
                            CNRUserContext.getEsercizio(userContext),
                            null, Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                            Configurazione_cnrBulk.SK_CODICE_A2A
                    )).orElseThrow(() -> new ApplicationException("Configurazione mancante per flusso Ordinativo [CODICE_A2A]"));

            String codiceEnte = Optional.ofNullable(
                    sess.getVal01(
                            userContext,
                            CNRUserContext.getEsercizio(userContext),
                            null, Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                            Configurazione_cnrBulk.SK_CODICE_ENTE
                    )).orElseThrow(() -> new ApplicationException("Configurazione mancante per flusso Ordinativo [CODICE_ENTE]"));

            String codiceEnteBT = Optional.ofNullable(
                    sess.getVal01(
                            userContext,
                            CNRUserContext.getEsercizio(userContext),
                            null, Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                            Configurazione_cnrBulk.SK_CODICE_ENTE_BT
                    )).orElseThrow(() -> new ApplicationException("Configurazione mancante per flusso Ordinativo [CODICE_ENTE_BT]"));

            String codiceTramiteBT = Optional.ofNullable(
                    sess.getVal01(
                            userContext,
                            CNRUserContext.getEsercizio(userContext),
                            null, Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                            Configurazione_cnrBulk.SK_CODICE_TRAMITE_BT
                    )).orElseThrow(() -> new ApplicationException("Configurazione mancante per flusso Ordinativo [CODICE_TRAMITE_BT]"));

            String codiceIstatEnte = Optional.ofNullable(
                    sess.getVal01(
                            userContext,
                            CNRUserContext.getEsercizio(userContext),
                            null, Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                            Configurazione_cnrBulk.SK_CODICE_ISTAT_ENTE
                    )).orElseThrow(() -> new ApplicationException("Configurazione mancante per flusso Ordinativo [CODICE_ISTAT_ENTE]"));

            final CtTestataFlusso testataFlusso = objectFactory.createCtTestataFlusso();
            testataFlusso.setCodiceABIBT(codiceAbi);
            testataFlusso.setRiferimentoEnte(codiceA2A);
            testataFlusso.setIdentificativoFlusso(distinta.getIdentificativoFlusso());
            testataFlusso.setDataOraCreazioneFlusso(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    formatterTime.format(EJBCommonServices
                            .getServerTimestamp().toLocalDateTime()))
            );
            testataFlusso.setCodiceEnte(codiceEnte);
            testataFlusso.setCodiceEnteBT(codiceEnteBT);
            testataFlusso.setCodiceTramiteEnte(codiceA2A);
            testataFlusso.setCodiceTramiteBT(codiceTramiteBT);
            AnagraficoComponentSession component = (AnagraficoComponentSession)
                    EJBCommonServices
                            .createEJB("CNRANAGRAF00_EJB_AnagraficoComponentSession");

            AnagraficoBulk uoEnte = component.getAnagraficoEnte(userContext);
            testataFlusso.setDescrizioneEnte(uoEnte.getRagione_sociale());

            testataFlusso.setCodiceIstatEnte(codiceIstatEnte);
            testataFlusso.setCodiceFiscaleEnte(uoEnte.getCodice_fiscale());
            currentFlusso.getContent().add(objectFactory.createTestataFlusso(testataFlusso));

            currentFlusso.getContent().add(objectFactory.createEsercizio(CNRUserContext.getEsercizio(userContext)));


            List dettagliRev = dettagliDistinta(
                    userContext,
                    distinta,
                    Numerazione_doc_contBulk.TIPO_REV);
            // Elaboriamo prima le reversali
            Reversale currentReversale = null;
            for (Iterator i = dettagliRev.iterator(); i.hasNext(); ) {
                V_mandato_reversaleBulk bulk = (V_mandato_reversaleBulk) i.next();
                currentFlusso.getContent().add(objectFactory.createReversale(creaReversaleFlussoSiopeplus(userContext, bulk)));
            }
            List dettagliMan = dettagliDistinta(
                    userContext,
                    distinta,
                    Numerazione_doc_contBulk.TIPO_MAN);
            // Mandati
            Mandato currentMandato = null;
            for (Iterator i = dettagliMan.iterator(); i.hasNext(); ) {
                V_mandato_reversaleBulk bulk = (V_mandato_reversaleBulk) i.next();
                currentFlusso.getContent().add(objectFactory.createMandato(creaMandatoFlussoSiopeplus(userContext, bulk)));
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Marshaller jaxbMarshaller = jc.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
            jaxbMarshaller.marshal(currentFlusso, byteArrayOutputStream);

            //FIX per firma xml
            String out = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
            out = out.replace("</flusso_ordinativi>", "\n</flusso_ordinativi>");

            StorageFile storageFile = new StorageFile(out.getBytes(StandardCharsets.UTF_8), MimeTypes.XML.mimetype(), distinta.getFileNameXML());
            final StorageObject storageObject = documentiContabiliService.getStorageObjectBykey(
                    documentiContabiliService.restoreSimpleDocument(
                            storageFile,
                            new ByteArrayInputStream(storageFile.getBytes()),
                            storageFile.getContentType(),
                            storageFile.getFileName(),
                            distinta.getStorePath(),
                            true).getKey());
            final BigInteger dimension = storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).divide(BigInteger.valueOf(1024));
            if (dimension.add(BigInteger.valueOf(7)).compareTo(DistintaCassiereComponentSession.MAX_OPI_DIMENSION) > 0) {
                throw new ApplicationMessageFormatException("La dimensione del flusso {0}kbytes supera la dimensione massima consentita {1}kbytes!",
                        dimension.add(BigInteger.valueOf(7)),
                        DistintaCassiereComponentSession.MAX_OPI_DIMENSION);
            }
            return storageObject;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private String getTipoOperazioneVariazione(UserContext userContext, V_mandato_reversaleBulk bulk, String stato) throws ComponentException, PersistencyException {
        /**
         * Cerco il Mandato associato alla reversale
         *
         */
        Boolean isVariazioneDefinitiva = Optional.ofNullable(bulk.getStatoVarSos())
                .map(statoVarSos -> statoVarSos.equals(StatoVariazioneSostituzione.VARIAZIONE_DEFINITIVA.value()))
                .orElse(Boolean.FALSE);
        if (bulk.isReversale() && !isVariazioneDefinitiva) {
            isVariazioneDefinitiva = Optional.ofNullable(getHome(userContext, V_mandato_reversaleBulk.class)
                .findByPrimaryKey(
                        new V_mandato_reversaleBulk(
                                bulk.getEsercizio(),
                                bulk.getCd_tipo_documento_cont_padre(),
                                bulk.getCd_cds(),
                                bulk.getPg_documento_cont_padre())
                ))
                .filter(V_mandato_reversaleBulk.class::isInstance)
                .map(V_mandato_reversaleBulk.class::cast)
                .filter(V_mandato_reversaleBulk::isMandato)
                .flatMap(v_mandato_reversaleBulk -> Optional.ofNullable(v_mandato_reversaleBulk.getStatoVarSos()))
                .map(statoVarSos -> statoVarSos.equals(StatoVariazioneSostituzione.VARIAZIONE_DEFINITIVA.value()))
                .orElse(Boolean.FALSE);
        }
        if (isVariazioneDefinitiva) {
            return VARIAZIONE;
        } else {
            if (Optional.ofNullable(bulk.getStatoVarSos())
                    .map(statoVarSos -> statoVarSos.equals(StatoVariazioneSostituzione.SOSTITUZIONE_DEFINITIVA.value()))
                    .orElse(Boolean.FALSE)) {
                return SOSTITUZIONE;
            }
            return stato;
        }

    }

    private String getTipoOperazione(UserContext userContext, V_mandato_reversaleBulk bulk) throws ComponentException, PersistencyException {
        switch (bulk.getStato()) {
            case MandatoBulk.STATO_MANDATO_ANNULLATO : {
                return getTipoOperazioneVariazione(userContext, bulk, ANNULLO);
            }
            case MandatoBulk.STATO_MANDATO_EMESSO : {
                return getTipoOperazioneVariazione(userContext, bulk, INSERIMENTO);
            }

            default: {
                return getTipoOperazioneVariazione(userContext, bulk, INSERIMENTO);
            }
        }
    }

    private Reversale creaReversaleFlussoSiopeplus(UserContext userContext,
                                                                V_mandato_reversaleBulk bulk) throws ComponentException,
            RemoteException, BusinessProcessException {
        try {
            final ObjectFactory objectFactory = new ObjectFactory();
            Reversale reversale = objectFactory.createReversale();
            List list = findDocumentiFlusso(userContext, bulk);
            reversale.setTipoOperazione(getTipoOperazione(userContext, bulk));

            GregorianCalendar gcdi = new GregorianCalendar();
            VDocumentiFlussoBulk docContabile = null;
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                Reversale.InformazioniVersante infover = objectFactory.createReversaleInformazioniVersante();
                docContabile = (VDocumentiFlussoBulk) i
                        .next();
                reversale.setNumeroReversale(docContabile.getPgDocumento().intValue());
                gcdi.setTime(docContabile.getDtEmissione());
                XMLGregorianCalendar xgc = DatatypeFactory.newInstance()
                        .newXMLGregorianCalendar(gcdi);
                xgc.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
                xgc.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
                xgc.setSecond(DatatypeConstants.FIELD_UNDEFINED);
                xgc.setMinute(DatatypeConstants.FIELD_UNDEFINED);
                xgc.setHour(DatatypeConstants.FIELD_UNDEFINED);
                reversale.setDataReversale(xgc);
                reversale.setImportoReversale(docContabile.getImDocumento().setScale(2, BigDecimal.ROUND_HALF_UP));

                infover.setProgressivoVersante(1);// Dovrebbe essere sempre 1 ?
                infover.setImportoVersante(docContabile.getImDocumento().setScale(2, BigDecimal.ROUND_HALF_UP));

                final String modalitaPagamento = docContabile.getModalitaPagamento();

                final Rif_modalita_pagamentoBulk rif_modalita_pagamentoBulk =
                        Optional.ofNullable(findByPrimaryKey(userContext, new Rif_modalita_pagamentoBulk(modalitaPagamento)))
                                .filter(Rif_modalita_pagamentoBulk.class::isInstance)
                                .map(Rif_modalita_pagamentoBulk.class::cast)
                                .orElseThrow(() -> new ApplicationMessageFormatException("Modalità di pagamento non trovata: {0}", modalitaPagamento));

                if (docContabile.getTiDocumento().compareTo(ReversaleBulk.TIPO_REGOLAM_SOSPESO) == 0) {
                    if (Optional.ofNullable(bulk.getTi_cc_bi()).filter(s -> s.equals("B")).isPresent() &&
                            Optional.ofNullable(rif_modalita_pagamentoBulk.getTi_pagamento()).filter(s -> s.equals(Rif_modalita_pagamentoBulk.BANCA_ITALIA)).isPresent() ) {
                        infover.setTipoRiscossione(REGOLARIZZAZIONE_ACCREDITO_BANCA_D_ITALIA);
                    } else {
                        infover.setTipoRiscossione(REGOLARIZZAZIONE);
                    }
                } else if (docContabile.getTiDocumento().compareTo(ReversaleBulk.TIPO_INCASSO) == 0) {
                    if(!bulk.getPg_documento_cont_padre().equals(bulk.getPg_documento_cont())) {
                        infover.setTipoRiscossione(COMPENSAZIONE);
                    } else {
                        infover.setTipoRiscossione(CASSA);
                    }
                } else if(!bulk.getPg_documento_cont_padre().equals(bulk.getPg_documento_cont())) {
                    infover.setTipoRiscossione(COMPENSAZIONE);
                }
                // Classificazioni
                infover.setTipoEntrata(INFRUTTIFERO);
                infover.setDestinazione(LIBERA);

                List listClass = findDocumentiFlussoClassReversali(userContext, bulk);
                VDocumentiFlussoBulk oldDoc = null;
                for (Iterator c = listClass.iterator(); c.hasNext(); ) {
                    VDocumentiFlussoBulk doc = (VDocumentiFlussoBulk) c.next();
                    if (doc.getCdSiope() != null && oldDoc != null && oldDoc.getCdSiope() != null) {
                        if ((oldDoc.getCdSiope().compareTo(doc.getCdSiope()) != 0)
                                || (oldDoc.getCdTipoDocumentoAmm() != null && oldDoc
                                .getCdTipoDocumentoAmm().compareTo(
                                        doc.getCdTipoDocumentoAmm()) != 0)
                                || (oldDoc.getPgDocAmm() != null && oldDoc
                                .getPgDocAmm().compareTo(
                                        doc.getPgDocAmm()) != 0)) {
                            Reversale.InformazioniVersante.Classificazione clas = objectFactory.createReversaleInformazioniVersanteClassificazione();
                            clas.setCodiceCge(doc.getCdSiope());
                            clas.setImporto(doc.getImportoCge().setScale(2, BigDecimal.ROUND_HALF_UP));
                            CtClassificazioneDatiSiopeEntrate ctClassificazioneDatiSiopeEntrate = objectFactory.createCtClassificazioneDatiSiopeEntrate();
                                ctClassificazioneDatiSiopeEntrate.getContent().add(
                                        objectFactory.createCtClassificazioneDatiSiopeEntrateTipoDebitoSiopeNc(StTipoDebitoNonCommerciale.NON_COMMERCIALE)
                                );
                            clas.setClassificazioneDatiSiopeEntrate(ctClassificazioneDatiSiopeEntrate);
                            infover.getClassificazione().add(clas);
                            oldDoc = doc;
                        }
                    } else if (doc.getCdSiope() != null) {
                        Reversale.InformazioniVersante.Classificazione clas = objectFactory.createReversaleInformazioniVersanteClassificazione();
                        clas.setCodiceCge(doc.getCdSiope());
                        clas.setImporto(doc.getImportoCge().setScale(2, BigDecimal.ROUND_HALF_UP));
                        CtClassificazioneDatiSiopeEntrate ctClassificazioneDatiSiopeEntrate = objectFactory.createCtClassificazioneDatiSiopeEntrate();
                        ctClassificazioneDatiSiopeEntrate.getContent().add(
                                objectFactory.createCtClassificazioneDatiSiopeEntrateTipoDebitoSiopeNc(StTipoDebitoNonCommerciale.NON_COMMERCIALE)
                        );
                        clas.setClassificazioneDatiSiopeEntrate(ctClassificazioneDatiSiopeEntrate);
                        infover.getClassificazione().add(clas);
                        oldDoc = doc;
                    }
                    if (infover.getCausale() != null && doc.getCdCup() != null) {
                        if (!infover.getCausale().contains(doc.getCdCup()))
                            infover.setCausale(infover.getCausale() + "-"
                                    + doc.getCdCup());
                    } else if (doc.getCdCup() != null) {
                        infover.setCausale("CUP " + doc.getCdCup());
                    }
                }
                // Fine classificazioni
                Reversale.InformazioniVersante.Bollo bollo = objectFactory.createReversaleInformazioniVersanteBollo();
                bollo.setAssoggettamentoBollo(docContabile.getAssoggettamentoBollo());
                bollo.setCausaleEsenzioneBollo(docContabile.getCausaleBollo());

                infover.setBollo(bollo);
                Versante versante = objectFactory.createVersante();
                versante.setAnagraficaVersante(RemoveAccent
                        .convert(docContabile.getDenominazioneSede())
                        .replace('"', ' ').replace('\u00b0', ' '));
                infover.setVersante(versante);

                // gestito inserimento cup nella CAUSALE
                if (infover.getCausale() != null && (infover.getCausale() + docContabile.getDsDocumento()).length() > MAX_LENGTH_CAUSALE)
                    infover.setCausale((infover.getCausale() + " " + docContabile
                            .getDsDocumento()).substring(0, MAX_LENGTH_CAUSALE - 1));
                else if (infover.getCausale() != null)
                    infover.setCausale(infover.getCausale() + " "
                            + docContabile.getDsDocumento());
                else if (docContabile.getDsDocumento().length() > MAX_LENGTH_CAUSALE)
                    infover.setCausale(docContabile.getDsDocumento().substring(
                            0, MAX_LENGTH_CAUSALE -1 ));
                else
                    infover.setCausale(docContabile.getDsDocumento());
                infover.setCausale(RemoveAccent.convert(infover.getCausale())
                        .replace('"', ' ').replace('\u00b0', ' '));
                // SOSPESO
                if (docContabile.getTiDocumento().compareTo(
                        ReversaleBulk.TIPO_REGOLAM_SOSPESO) == 0) {
                    List listSosp = findDocumentiFlussoSospeso(userContext, bulk);

                    for (Iterator c = listSosp.iterator(); c.hasNext(); ) {
                        boolean sospesoTrovato = false;
                        VDocumentiFlussoBulk doc = (VDocumentiFlussoBulk) c.next();
                        if (doc.getCdSospeso() != null) {
                            if (Optional.ofNullable(doc.getCdSospeso()).isPresent() && isSospesoFromAccreditamento(userContext, doc)) {
                                final V_mandato_reversaleBulk mandatoReversale = findMandatoReversale(userContext, findSospeso(userContext, doc).get().getMandatoRiaccredito());
                                final CtClassificazioneDatiSiopeUscite classificazioneDatiSiope = getClassificazioneDatiSiope(userContext, objectFactory, mandatoReversale, null);
                                final Optional<Reversale.InformazioniVersante.Classificazione> any = infover.getClassificazione().stream().findAny();
                                final Optional<StTipoDebitoCommerciale> stTipoDebitoCommerciale = classificazioneDatiSiope.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().stream()
                                        .filter(StTipoDebitoCommerciale.class::isInstance)
                                        .map(StTipoDebitoCommerciale.class::cast)
                                        .findAny();
                                final Optional<StTipoDebitoNonCommerciale> stTipoDebitoNonCommerciale = classificazioneDatiSiope.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().stream()
                                        .filter(StTipoDebitoNonCommerciale.class::isInstance)
                                        .map(StTipoDebitoNonCommerciale.class::cast)
                                        .findAny();
                                final Optional<CtFatturaSiope> ctFatturaSiope = classificazioneDatiSiope.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().stream()
                                        .filter(CtFatturaSiope.class::isInstance)
                                        .map(CtFatturaSiope.class::cast)
                                        .findAny();
                                if (any.isPresent()) {
                                    final Reversale.InformazioniVersante.Classificazione classificazione = any.get();
                                    classificazione.getClassificazioneDatiSiopeEntrate().getContent().clear();
                                    if (stTipoDebitoCommerciale.isPresent())
                                        classificazione.getClassificazioneDatiSiopeEntrate().getContent().add(objectFactory.createCtClassificazioneDatiSiopeEntrateTipoDebitoSiopeC(stTipoDebitoCommerciale.get()));
                                    if (stTipoDebitoNonCommerciale.isPresent())
                                        classificazione.getClassificazioneDatiSiopeEntrate().getContent().add(objectFactory.createCtClassificazioneDatiSiopeEntrateTipoDebitoSiopeNc(stTipoDebitoNonCommerciale.get()));
                                    if (ctFatturaSiope.isPresent()) {
                                        CtFatturaSiope ctFatturaSiope1 = ctFatturaSiope.get();
                                        ctFatturaSiope1.getDatiFatturaSiope().setImportoSiope(classificazione.getImporto());
                                        classificazione.getClassificazioneDatiSiopeEntrate().getContent().add(objectFactory.createCtClassificazioneDatiSiopeEntrateFatturaSiope(ctFatturaSiope1));
                                    }
                                }
                            }
                            for (Iterator it = infover.getSospeso().iterator(); it.hasNext(); ) {
                                Reversale.InformazioniVersante.Sospeso presente = (Reversale.InformazioniVersante.Sospeso) it.next();
                                Long l = new Long(doc.getCdSospeso().substring(0, doc.getCdSospeso().indexOf(".")).replace(" ", "")).longValue();
                                if (l.compareTo(presente.getNumeroProvvisorio()) == 0) {
                                    presente.setImportoProvvisorio(presente.getImportoProvvisorio().add(doc.getImAssociato()));
                                    sospesoTrovato = true;
                                    break;
                                }
                            }
                            if (!sospesoTrovato) {
                                Reversale.InformazioniVersante.Sospeso sosp = objectFactory.createReversaleInformazioniVersanteSospeso();
                                try {
                                    sosp.setNumeroProvvisorio(new Long(
                                            doc.getCdSospeso()
                                                    .substring(
                                                            0,
                                                            doc.getCdSospeso()
                                                                    .indexOf(".")).replace(" ", ""))
                                            .longValue());
                                } catch (NumberFormatException e) {
                                    throw new ApplicationException(
                                            "Formato del codice del sospeso non compatibile.");
                                }
                                sosp.setImportoProvvisorio(doc.getImAssociato()
                                        .setScale(2, BigDecimal.ROUND_HALF_UP));
                                infover.getSospeso().add(sosp);
                            }
                        }
                    }
                }
                reversale.getInformazioniVersante().add(infover);
            }
            return reversale;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private V_mandato_reversaleBulk findMandatoReversale(UserContext userContext, MandatoBulk mandatoBulk) throws ComponentException, PersistencyException {
        final BulkHome home = getHome(userContext, V_mandato_reversaleBulk.class);
        return Optional.ofNullable(home.findByPrimaryKey(new V_mandato_reversaleBulk(mandatoBulk.getEsercizio(), Numerazione_doc_contBulk.TIPO_MAN, mandatoBulk.getCd_cds(), mandatoBulk.getPg_mandato())))
                .filter(V_mandato_reversaleBulk.class::isInstance)
                .map(V_mandato_reversaleBulk.class::cast)
                .orElseThrow(() -> new ComponentException("Mandato associato al sospeso non trovato!"));
    }

    private Optional<SospesoBulk> findSospeso(UserContext userContext, VDocumentiFlussoBulk doc) throws ComponentException, PersistencyException {
        final BulkHome home = getHome(userContext, SospesoBulk.class);
        final SQLBuilder sqlBuilder = home.createSQLBuilder();
        sqlBuilder.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, doc.getEsercizio());
        sqlBuilder.addClause(FindClause.AND, "cd_cds_origine", SQLBuilder.EQUALS, doc.getCdCdsOrigine());
        sqlBuilder.addClause(FindClause.AND, "ti_entrata_spesa", SQLBuilder.EQUALS, SospesoBulk.TIPO_ENTRATA);
        sqlBuilder.addClause(FindClause.AND, "ti_sospeso_riscontro", SQLBuilder.EQUALS, SospesoBulk.TI_SOSPESO);
        sqlBuilder.addClause(FindClause.AND, "cd_sospeso", SQLBuilder.EQUALS, doc.getCdSospeso());
        return home.fetchAll(sqlBuilder).stream()
                .filter(SospesoBulk.class::isInstance)
                .map(SospesoBulk.class::cast)
                .findAny();
    }

    private boolean isSospesoFromAccreditamento(UserContext userContext, VDocumentiFlussoBulk doc) throws ComponentException, PersistencyException {
        final Optional<SospesoBulk> sospeso = findSospeso(userContext, doc);
        return sospeso.isPresent() && sospeso.filter(sospesoBulk -> Optional.ofNullable(sospesoBulk.getMandatoRiaccredito()).isPresent()).isPresent();
    }

    private MandatoBulk cercaMandatoRiemissione(UserContext userContext, V_mandato_reversaleBulk bulk) throws ComponentException, PersistencyException {
        final BulkHome home = getHome(userContext, MandatoIBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
        sql.addClause(FindClause.AND, "cd_cds_origine", SQLBuilder.EQUALS, bulk.getCd_cds_origine());
        sql.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS, MandatoBulk.STATO_MANDATO_ANNULLATO);
        sql.addSQLClause(FindClause.AND, "pg_mandato_riemissione", SQLBuilder.EQUALS, bulk.getPg_documento_cont());
        sql.addClause(FindClause.AND, "fl_riemissione", SQLBuilder.EQUALS, true);
        final Optional<MandatoBulk> any = home.fetchAll(sql).stream()
                .filter(MandatoBulk.class::isInstance)
                .map(MandatoBulk.class::cast)
                .findAny();
        if (!any.isPresent()) {
            throw new ApplicationMessageFormatException("Mandato di riemmissione non trovato per {1}/{2}/{3}", bulk.getEsercizio(), bulk.getCd_cds_origine(), bulk.getPg_documento_cont());
        }
        return any.get();
    }

    public Mandato creaMandatoFlussoSiopeplus(UserContext userContext, V_mandato_reversaleBulk bulk) throws ComponentException, RemoteException {
        try {
            Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) EJBCommonServices
                    .createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            final ObjectFactory objectFactory = new ObjectFactory();
            BancaBulk bancauo = recuperaIbanUo(userContext, bulk.getUo());
            Mandato mandato = objectFactory.createMandato();
            List list = findDocumentiFlusso(userContext, bulk);
            mandato.setTipoOperazione(getTipoOperazione(userContext, bulk));
            GregorianCalendar gcdi = new GregorianCalendar();

            Mandato.InformazioniBeneficiario infoben = objectFactory.createMandatoInformazioniBeneficiario();
            Mandato.InformazioniBeneficiario.Classificazione clas = objectFactory.createMandatoInformazioniBeneficiarioClassificazione();
            Mandato.InformazioniBeneficiario.Bollo bollo = objectFactory.createMandatoInformazioniBeneficiarioBollo();
            SepaCreditTransfer sepa = objectFactory.createSepaCreditTransfer();
            Piazzatura piazzatura = objectFactory.createPiazzatura();
            Beneficiario benef = objectFactory.createBeneficiario();
            Mandato.InformazioniBeneficiario.Sospeso sosp = objectFactory.createMandatoInformazioniBeneficiarioSospeso();
            Ritenute riten = objectFactory.createRitenute();
            Mandato.InformazioniBeneficiario.InformazioniAggiuntive aggiuntive = objectFactory.createMandatoInformazioniBeneficiarioInformazioniAggiuntive();
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                final VDocumentiFlussoBulk  docContabile = (VDocumentiFlussoBulk) i.next();
                final String modalitaPagamento = docContabile.getModalitaPagamento();

                final Rif_modalita_pagamentoBulk rif_modalita_pagamentoBulk =
                        Optional.ofNullable(findByPrimaryKey(userContext, new Rif_modalita_pagamentoBulk(modalitaPagamento)))
                                .filter(Rif_modalita_pagamentoBulk.class::isInstance)
                                .map(Rif_modalita_pagamentoBulk.class::cast)
                                .orElseThrow(() -> new ApplicationMessageFormatException("Modalità di pagamento non trovata: {0}", modalitaPagamento));

                if (Rif_modalita_pagamentoBulk.IBAN.equals(rif_modalita_pagamentoBulk.getTi_pagamento())) {
                    final Optional<String> codiceNazione = Optional.ofNullable(docContabile.getCdIso());
                    if (codiceNazione.isPresent()) {
                        NazioneHome nazioneHome = (NazioneHome) getHome(userContext,NazioneBulk.class);
                        SQLBuilder sqlExists = nazioneHome.createSQLBuilder();
                        sqlExists.addSQLClause("AND","NAZIONE.CD_ISO",SQLBuilder.EQUALS,codiceNazione.get());
                        sqlExists.addSQLClause("AND","NAZIONE.FL_SEPA",SQLBuilder.EQUALS,"Y");
                        if (sqlExists.executeCountQuery(getConnection(userContext))!=0 )
                            throw new ApplicationMessageFormatException("Attenzione la modalità di pagamento {0} presente sul mandato {1}/{2}/{3} non è " +
                                    "coerente con la nazione {4} del beneficiario!",
                                    rif_modalita_pagamentoBulk.getCd_modalita_pag(),
                                    String.valueOf(bulk.getEsercizio()),
                                    String.valueOf(bulk.getCd_cds()),
                                    String.valueOf(bulk.getPg_documento_cont()),
                                    codiceNazione.get());
                    }
                }
                final Boolean isSostituzione = Optional.ofNullable(bulk.getStatoVarSos())
                        .map(statoVarSos -> statoVarSos.equals(StatoVariazioneSostituzione.SOSTITUZIONE_DEFINITIVA.value()))
                        .orElse(Boolean.FALSE);
                final Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus tipoPagamentoSiopePlus = isSostituzione ?
                        Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.SOSTITUZIONE :
                        Optional.ofNullable(rif_modalita_pagamentoBulk.getTipo_pagamento_siope())
                                .map(s -> Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.getValueFrom(s))
                                .orElseGet(() -> Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.REGOLARIZZAZIONE);
                if (isSostituzione) {
                    final MandatoBulk mandatoRiemissione = cercaMandatoRiemissione(userContext, bulk);
                    SostituzioneMandato sostituzioneMandato = objectFactory.createSostituzioneMandato();
                    sostituzioneMandato.setEsercizioMandatoDaSostituire(mandatoRiemissione.getEsercizio());
                    sostituzioneMandato.setNumeroMandatoDaSostituire(mandatoRiemissione.getPg_mandato().intValue());
                    // TODO Valore fisso a 1
                    sostituzioneMandato.setProgressivoBeneficiarioDaSostituire(BigInteger.ONE.intValue());
                    infoben.setSostituzioneMandato(sostituzioneMandato);
                }

                // deve esserci IBAN
                boolean obb_iban = Arrays.asList(
                        Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.SEPACREDITTRANSFER,
                        Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ACCREDITOTESORERIAPROVINCIALESTATOPERTABB,
                        Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.BONIFICOESTEROEURO
                ).contains(tipoPagamentoSiopePlus);
                // deve esserci il Conto
                boolean obb_conto = Arrays.asList(
                        Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ACCREDITOCONTOCORRENTEPOSTALE
                ).contains(tipoPagamentoSiopePlus);

                boolean obb_dati_beneficiario = Arrays.asList(
                        Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ASSEGNOBANCARIOEPOSTALE,
                        Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ASSEGNOCIRCOLARE,
                        Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.CASSA,
                        Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.DISPOSIZIONEDOCUMENTOESTERNO,
                        Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.COMPENSAZIONE
                ).contains(tipoPagamentoSiopePlus) &&
                        //TODO da sostituire
                        !rif_modalita_pagamentoBulk.getCd_modalita_pag().equals(STIPENDI);

                mandato.setNumeroMandato(docContabile.getPgDocumento().intValue());
                gcdi.setTime(docContabile.getDtEmissione());
                XMLGregorianCalendar xgc = DatatypeFactory.newInstance()
                        .newXMLGregorianCalendar(gcdi);
                xgc.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
                xgc.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
                xgc.setSecond(DatatypeConstants.FIELD_UNDEFINED);
                xgc.setMinute(DatatypeConstants.FIELD_UNDEFINED);
                xgc.setHour(DatatypeConstants.FIELD_UNDEFINED);
                mandato.setDataMandato(xgc);
                mandato.setImportoMandato(docContabile.getImDocumento().setScale(2, BigDecimal.ROUND_HALF_UP));

                BigDecimal coef = BigDecimal.ZERO;
                BigDecimal totSiope = BigDecimal.ZERO;

                boolean multibeneficiario = Optional.ofNullable(bulk)
                        .filter(v_mandato_reversaleBulk ->
                                Optional.ofNullable(v_mandato_reversaleBulk.getIm_ritenute())
                                        .filter(imRitenute -> imRitenute.compareTo(BigDecimal.ZERO) != 0).isPresent())
                        .filter(v_mandato_reversaleBulk -> v_mandato_reversaleBulk.getIm_documento_cont().compareTo(v_mandato_reversaleBulk.getIm_ritenute()) != 0)
                        .filter(v_mandato_reversaleBulk -> (!Arrays.asList(
                                Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ASSEGNOBANCARIOEPOSTALE,
                                Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ASSEGNOCIRCOLARE,
                                Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.CASSA,
                                Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.SEPACREDITTRANSFER,
                                Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.COMPENSAZIONE,
                                Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.SOSTITUZIONE
                        ).contains(tipoPagamentoSiopePlus))
                                //TODO da sostituire
                                ||rif_modalita_pagamentoBulk.getCd_modalita_pag().equals(STIPENDI)
                                ||docContabile.getTiDocumento().compareTo(MandatoBulk.TIPO_REGOLAM_SOSPESO) == 0).isPresent();

                if (multibeneficiario) {
                    bollo = objectFactory.createMandatoInformazioniBeneficiarioBollo();
                    benef = objectFactory.createBeneficiario();
                    sosp = objectFactory.createMandatoInformazioniBeneficiarioSospeso();
                    riten = objectFactory.createRitenute();
                    aggiuntive = objectFactory.createMandatoInformazioniBeneficiarioInformazioniAggiuntive();

                    infoben = objectFactory.createMandatoInformazioniBeneficiario();
                    infoben.setProgressivoBeneficiario(1);// Dovrebbe essere sempre
                    // 1 ?
                    infoben.setImportoBeneficiario(docContabile.getImDocumento().subtract(bulk.getIm_ritenute())
                            .setScale(2, BigDecimal.ROUND_HALF_UP));
                    if (docContabile.getTiDocumento().compareTo(MandatoBulk.TIPO_REGOLAM_SOSPESO) == 0) {
                        if (tipoPagamentoSiopePlus.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ACCREDITOTESORERIAPROVINCIALESTATOPERTABA)) {
                            infoben.setTipoPagamento(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.REGOLARIZZAZIONEACCREDITOTESORERIAPROVINCIALESTATOPERTABA.value());
                        } else if (tipoPagamentoSiopePlus.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ACCREDITOTESORERIAPROVINCIALESTATOPERTABB)) {
                            infoben.setTipoPagamento(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.REGOLARIZZAZIONEACCREDITOTESORERIAPROVINCIALESTATOPERTABB.value());
                        } else {
                            infoben.setTipoPagamento(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.REGOLARIZZAZIONE.value());
                        }
                    } else if (tipoPagamentoSiopePlus.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.F24EP)
                            && docContabile.getDtPagamentoRichiesta() == null) {
                        throw new ApplicationMessageFormatException(
                                "Impossibile generare il flusso, indicare data richiesta pagamento nel mandato cds {0} n. {1}",
                                docContabile.getCdCds(), docContabile.getPgDocumento());
                    } else if (tipoPagamentoSiopePlus.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.F24EP)
                            && docContabile.getDtPagamentoRichiesta() != null &&
                            (EJBCommonServices.getServerTimestamp().after(docContabile.getDtPagamentoRichiesta()))) {
                        throw new ApplicationMessageFormatException(
                                "Impossibile generare il flusso, indicare data richiesta pagamento nel mandato cds {0} mandato {1} superiore alla data odierna!",
                                docContabile.getCdCds(), docContabile.getPgDocumento());
                    } else if (tipoPagamentoSiopePlus.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.F24EP) &&
                            docContabile.getDtPagamentoRichiesta() != null &&
                            (EJBCommonServices.getServerTimestamp().before(docContabile.getDtPagamentoRichiesta()))) {
                        infoben.setTipoPagamento(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.F24EP.value());
                        final XMLGregorianCalendar xmlGregorianCalendar =
                                DatatypeFactory.newInstance()
                                        .newXMLGregorianCalendar(
                                                formatterDate.format(
                                                        docContabile.getDtPagamentoRichiesta().toLocalDateTime()
                                                )
                                        );
                        infoben.setDataEsecuzionePagamento(xmlGregorianCalendar);
                        infoben.setDestinazione(LIBERA);
                        infoben.setNumeroContoBancaItaliaEnteRicevente(NUMERO_CONTO_BANCA_ITALIA_ENTE_RICEVENTE);
                        infoben.setTipoContabilitaEnteRicevente(TIPO_CONTABILITA_ENTE_RICEVENTE);
                    } else {
                        infoben.setTipoPagamento(tipoPagamentoSiopePlus.value());
                    }
                    if (tipoPagamentoSiopePlus.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ACCREDITOTESORERIAPROVINCIALESTATOPERTABA)) {
                        infoben.setNumeroContoBancaItaliaEnteRicevente(
                                Optional.ofNullable(docContabile.getNumeroConto())
                                        .filter(s -> s.length() == 7)
                                        .orElseThrow(() -> new ApplicationMessageFormatException("Impossibile generare il flusso, manca il numero conto " +
                                                "sul Mandato {0}/{1}/{2}, oppure il numero conto {3} non ha la lunghezza corretta!",
                                                String.valueOf(bulk.getEsercizio()),
                                                String.valueOf(bulk.getCd_cds()),
                                                String.valueOf(bulk.getPg_documento_cont()),
                                                docContabile.getNumeroConto()
                                        ))
                        );
                        infoben.setTipoContabilitaEnteRicevente(TIPO_CONTABILITA_ENTE_RICEVENTE);
                    }

                    infoben.setDestinazione(LIBERA);
                    caricaInformazioniAggiuntive(infoben, bulk, aggiuntive, tipoPagamentoSiopePlus);
                    caricaTipoPostalizzazione(infoben, docContabile, tipoPagamentoSiopePlus);
                    if (obb_dati_beneficiario) {
                        benef.setIndirizzoBeneficiario(RemoveAccent
                                .convert(docContabile.getViaSede())
                                .replace('"', ' ').replace('\u00b0', ' '));
                        if (docContabile.getCapComuneSede() == null)
                            throw new ApplicationException(
                                    "Impossibile generare il flusso, Cap beneficiario non valorizzato per il terzo "
                                            + docContabile.getCdTerzo()
                                            + " cds "
                                            + docContabile.getCdCds()
                                            + " mandato "
                                            + docContabile.getPgDocumento());
                        benef.setCapBeneficiario(docContabile.getCapComuneSede());
                        benef.setLocalitaBeneficiario(RemoveAccent.convert(docContabile.getDsComune())
                                .replace('"', ' ').replace('\u00b0', ' '));
                        benef.setProvinciaBeneficiario(docContabile.getCdProvincia());
                        benef.setStatoBeneficiario(docContabile.getCdIso());
                        benef.setCodiceFiscaleBeneficiario(Optional.ofNullable(docContabile.getCodiceFiscale()).orElse(null));
                        benef.setPartitaIvaBeneficiario(Optional.ofNullable(docContabile.getPartitaIva()).orElse(null));
                    }
                    infoben.setBeneficiario(benef);
                    if (obb_conto && !infoben.getTipoPagamento().equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.REGOLARIZZAZIONE.value())) {
                        piazzatura.setNumeroContoCorrenteBeneficiario(
                                Optional.ofNullable(docContabile.getNumeroConto())
                                    .orElseThrow(() -> new ApplicationMessageFormatException("Impossibile generare il flusso, manca il numero conto " +
                                            "sul Mandato {0}/{1}/{2}",
                                            String.valueOf(bulk.getEsercizio()),
                                            String.valueOf(bulk.getCd_cds()),
                                            String.valueOf(bulk.getPg_documento_cont())
                                            ))
                        );
                        infoben.setPiazzatura(piazzatura);
                    }
                    if (obb_iban && !infoben.getTipoPagamento().equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.REGOLARIZZAZIONE.value())) {
                        sepa.setIban( Optional.ofNullable(docContabile.getCodiceIban())
                                .orElseThrow(() -> new ApplicationMessageFormatException("Impossibile generare il flusso, manca il codice iban " +
                                        "sul Mandato {0}/{1}/{2}",
                                        String.valueOf(bulk.getEsercizio()),
                                        String.valueOf(bulk.getCd_cds()),
                                        String.valueOf(bulk.getPg_documento_cont())
                                )));

                        if (tipoPagamentoSiopePlus.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.BONIFICOESTEROEURO)) {
                            sepa.setBic(Optional.ofNullable(docContabile.getBic())
                                    .filter(s -> Optional.ofNullable(docContabile.getCodiceIban()).isPresent())
                                    .filter(s -> patternBic.matcher(s).find())
                                    .orElseThrow(() -> new ApplicationMessageFormatException("Impossibile generare il flusso, codice BIC: {0} non valido " +
                                            "sul Mandato {1}/{2}/{3}",
                                            docContabile.getBic(),
                                            String.valueOf(bulk.getEsercizio()),
                                            String.valueOf(bulk.getCd_cds()),
                                            String.valueOf(bulk.getPg_documento_cont())
                                    )));
                        }
                        if (tipoPagamentoSiopePlus.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.SEPACREDITTRANSFER)
                                && !infoben.getTipoPagamento().equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.COMPENSAZIONE.value()))
                            sepa.setIdentificativoEndToEnd(docContabile.getEsercizio()
                                    .toString()
                                    + "-"
                                    + docContabile.getCdUoOrigine()
                                    + "-" + docContabile.getPgDocumento().toString());
                        infoben.setSepaCreditTransfer(sepa);
                    }
                    List listClass = findDocumentiFlussoClass(userContext, bulk);
                    VDocumentiFlussoBulk oldDoc = null;
                    for (Iterator c = listClass.iterator(); c.hasNext(); ) {
                        VDocumentiFlussoBulk doc = (VDocumentiFlussoBulk) c.next();
                        boolean salta = false;
                        if (multibeneficiario) {
                            if (infoben.getClassificazione() != null && infoben.getClassificazione().size() != 0) {
                                for (Iterator it = infoben.getClassificazione().iterator(); it.hasNext(); ) {
                                    Mandato.InformazioniBeneficiario.Classificazione presente = (Mandato.InformazioniBeneficiario.Classificazione) it.next();
                                    if (doc.getCdSiope().compareTo(presente.getCodiceCgu()) == 0) {
                                        salta = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (!salta) {
                            // 17/01/2018 per non indicare cup nel tag - fine aggiunta
                            if (doc.getCdSiope() != null) {
                                clas = objectFactory.createMandatoInformazioniBeneficiarioClassificazione();
                                clas.setCodiceCgu(doc.getCdSiope());
                                if (doc.getImDocumento().compareTo(doc.getImportoCge()) == 0)
                                    clas.setImporto(infoben.getImportoBeneficiario());
                                else {
                                    coef = infoben.getImportoBeneficiario().divide(doc.getImDocumento(), 10, BigDecimal.ROUND_HALF_UP);
                                    clas.setImporto((doc.getImportoCge().multiply(coef)).setScale(2,
                                            BigDecimal.ROUND_HALF_UP));
                                }
                                caricaClassificazione(userContext, clas, objectFactory, bulk, doc.getCdSiope());
                                totSiope = totSiope.add(clas.getImporto());
                                infoben.getClassificazione().add(clas);
                            }
                            oldDoc = doc;
                        }
                    }

                    if (totSiope.compareTo(infoben.getImportoBeneficiario()) != 0) {
                        if (totSiope.subtract(infoben.getImportoBeneficiario()).abs().compareTo(new BigDecimal(SCOSTAMENTO)) <= 0) {
                            clas = infoben.getClassificazione().get(0);
                            if (totSiope.subtract(infoben.getImportoBeneficiario()).compareTo(BigDecimal.ZERO) > 0)
                                clas.setImporto(clas.getImporto().subtract(totSiope.subtract(infoben.getImportoBeneficiario()).abs()));
                            else
                                clas.setImporto(clas.getImporto().add(totSiope.subtract(infoben.getImportoBeneficiario()).abs()));
                        } else {
                            throw new ApplicationMessageFormatException("Impossibile generare il flusso, ripartizione per siope errata " +
                                    "sul Mandato {0}/{1}/{2}",
                                    String.valueOf(bulk.getEsercizio()),
                                    String.valueOf(bulk.getCd_cds()),
                                    String.valueOf(bulk.getPg_documento_cont()));
                        }
                    }

                    bollo.setAssoggettamentoBollo(docContabile
                            .getAssoggettamentoBollo());
                    bollo.setCausaleEsenzioneBollo(docContabile.getCausaleBollo());
                    infoben.setBollo(bollo);
                    benef.setAnagraficaBeneficiario(RemoveAccent
                            .convert(docContabile.getDenominazioneSede())
                            .replace('"', ' ').replace('\u00b0', ' '));
                    // benef.setStatoBeneficiario(docContabile.getCdIso());
                    infoben.setBeneficiario(benef);
                    infoben.setCausale(Optional.ofNullable(Optional.ofNullable(docContabile.getDsDocumento())
                            .filter(s -> s.length() > MAX_LENGTH_CAUSALE)
                            .map(s -> s.substring(0, MAX_LENGTH_CAUSALE -1))
                            .orElseGet(() -> docContabile.getDsDocumento()))
                            .map(s -> RemoveAccent.convert(s).replace('"', ' ').replace('\u00b0', ' '))
                            .orElseThrow(() -> new ApplicationMessageFormatException("Impossibile generare il flusso, causale non presente " +
                                    "sul Mandato {0}/{1}/{2}",
                                    String.valueOf(bulk.getEsercizio()),
                                    String.valueOf(bulk.getCd_cds()),
                                    String.valueOf(bulk.getPg_documento_cont()))));
                    // SOSPESO
                    if (docContabile.getTiDocumento().compareTo(
                            MandatoBulk.TIPO_REGOLAM_SOSPESO) == 0) {
                        List listSosp = findDocumentiFlussoSospeso(userContext, bulk);
                        for (Iterator c = listSosp.iterator(); c.hasNext(); ) {
                            boolean sospesoTrovato = false;
                            VDocumentiFlussoBulk doc = (VDocumentiFlussoBulk) c
                                    .next();
                            if (doc.getCdSospeso() != null) {
                                for (Iterator it = infoben.getSospeso().iterator(); it.hasNext(); ) {
                                    Mandato.InformazioniBeneficiario.Sospeso presente =
                                            (Mandato.InformazioniBeneficiario.Sospeso) it.next();
                                    Long l = new Long(doc.getCdSospeso().substring(0, doc.getCdSospeso().indexOf(".")).replace(" ", "")).longValue();
                                    if (l.compareTo(presente.getNumeroProvvisorio()) == 0) {
                                        presente.setImportoProvvisorio(presente.getImportoProvvisorio().add(doc.getImAssociato()));
                                        sospesoTrovato = true;
                                        break;
                                    }
                                }
                                if (!sospesoTrovato) {
                                    sosp = objectFactory.createMandatoInformazioniBeneficiarioSospeso();
                                    try {
                                        sosp.setNumeroProvvisorio(new Long(
                                                doc.getCdSospeso()
                                                        .substring(
                                                                0,
                                                                doc.getCdSospeso()
                                                                        .indexOf(".")).replace(" ", ""))
                                                .longValue());
                                    } catch (NumberFormatException e) {
                                        throw new ApplicationException(
                                                "Formato del codice del sospeso non compatibile.");
                                    }
                                    sosp.setImportoProvvisorio(doc.getImAssociato()
                                            .setScale(2, BigDecimal.ROUND_HALF_UP));
                                    infoben.getSospeso().add(sosp);
                                }
                            }
                        }
                    }
                    // Fine sospeso
                    mandato.getInformazioniBeneficiario().add(infoben);

                    bollo = objectFactory.createMandatoInformazioniBeneficiarioBollo();
                    benef = objectFactory.createBeneficiario();
                    sosp = objectFactory.createMandatoInformazioniBeneficiarioSospeso();
                    riten = objectFactory.createRitenute();
                    aggiuntive = objectFactory.createMandatoInformazioniBeneficiarioInformazioniAggiuntive();
                    infoben = objectFactory.createMandatoInformazioniBeneficiario();
                    totSiope = BigDecimal.ZERO;
                    infoben.setProgressivoBeneficiario(2);
                    infoben.setImportoBeneficiario(bulk.getIm_ritenute()
                            .setScale(2, BigDecimal.ROUND_HALF_UP));
                    infoben.setTipoPagamento(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.COMPENSAZIONE.value());
                    infoben.setDestinazione(LIBERA);
                    listClass = findDocumentiFlussoClass(userContext, bulk);
                    oldDoc = null;
                    for (Iterator c = listClass.iterator(); c.hasNext(); ) {
                        VDocumentiFlussoBulk doc = (VDocumentiFlussoBulk) c.next();
                        // 17/01/2018 per non indicare cup nel tag ma solo nella causale e ripartire gli importi solo per siope
                        boolean salta = false;
                        //nel caso di multibeneficiario non considero il cup comunque
                        if (multibeneficiario) {
                            if (infoben.getClassificazione() != null && infoben.getClassificazione().size() != 0) {
                                for (Iterator it = infoben.getClassificazione().iterator(); it.hasNext(); ) {
                                    Mandato.InformazioniBeneficiario.Classificazione presente = (Mandato.InformazioniBeneficiario.Classificazione) it.next();
                                    if (doc.getCdSiope().compareTo(presente.getCodiceCgu()) == 0) {
                                        salta = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (!salta) {
                            // 17/01/2018 per non indicare cup nel tag - fine aggiunta
                            if (doc.getCdSiope() != null) {
                                clas = objectFactory.createMandatoInformazioniBeneficiarioClassificazione();
                                clas.setCodiceCgu(doc.getCdSiope());
                                if (doc.getImDocumento().compareTo(doc.getImportoCge()) == 0)
                                    clas.setImporto(infoben.getImportoBeneficiario());
                                else {
                                    coef = infoben.getImportoBeneficiario().divide(doc.getImDocumento(), 10, BigDecimal.ROUND_HALF_UP);
                                    clas.setImporto((doc.getImportoCge().multiply(coef)).setScale(2,
                                            BigDecimal.ROUND_HALF_UP));
                                }
                                caricaClassificazione(userContext, clas, objectFactory, bulk, doc.getCdSiope());

                                totSiope = totSiope.add(clas.getImporto());
                                infoben.getClassificazione().add(clas);
                            }
                            oldDoc = doc;
                        }
                    }
                    if (totSiope.compareTo(infoben.getImportoBeneficiario()) != 0) {
                        if (totSiope.subtract(infoben.getImportoBeneficiario()).abs().compareTo(new BigDecimal(SCOSTAMENTO)) <= 0) {
                            clas = infoben.getClassificazione().get(0);
                            if (totSiope.subtract(infoben.getImportoBeneficiario()).compareTo(BigDecimal.ZERO) > 0)
                                clas.setImporto(clas.getImporto().subtract(totSiope.subtract(infoben.getImportoBeneficiario()).abs()));
                            else
                                clas.setImporto(clas.getImporto().add(totSiope.subtract(infoben.getImportoBeneficiario()).abs()));
                        } else {
                            throw new ApplicationMessageFormatException("Impossibile generare il flusso, " +
                                    "ripartizione per siope errata " +
                                    "sul Mandato {0}/{1}/{2}",
                                    String.valueOf(bulk.getEsercizio()),
                                    String.valueOf(bulk.getCd_cds()),
                                    String.valueOf(bulk.getPg_documento_cont()));
                        }
                    }

                    bollo.setAssoggettamentoBollo(docContabile
                            .getAssoggettamentoBollo());
                    bollo.setCausaleEsenzioneBollo(docContabile.getCausaleBollo());
                    infoben.setBollo(bollo);
                    benef.setAnagraficaBeneficiario(RemoveAccent
                            .convert(docContabile.getDenominazioneSede())
                            .replace('"', ' ').replace('\u00b0', ' '));
                    // benef.setStatoBeneficiario(docContabile.getCdIso());
                    infoben.setBeneficiario(benef);
                    infoben.setCausale(Optional.ofNullable(Optional.ofNullable(docContabile.getDsDocumento())
                            .filter(s -> s.length() > MAX_LENGTH_CAUSALE)
                            .map(s -> s.substring(0, MAX_LENGTH_CAUSALE -1))
                            .orElseGet(() -> docContabile.getDsDocumento()))
                            .map(s -> RemoveAccent.convert(s).replace('"', ' ').replace('\u00b0', ' '))
                            .orElseThrow(() -> new ApplicationMessageFormatException("Impossibile generare il flusso, causale non presente " +
                                    "sul Mandato {0}/{1}/{2}",
                                    String.valueOf(bulk.getEsercizio()),
                                    String.valueOf(bulk.getCd_cds()),
                                    String.valueOf(bulk.getPg_documento_cont()))));
                    if (bulk.getIm_ritenute().compareTo(BigDecimal.ZERO) != 0) {
                        List list_rev = findReversali(userContext, bulk);
                        for (Iterator iRev = list_rev.iterator(); iRev.hasNext(); ) {
                            riten = objectFactory.createRitenute();
                            V_mandato_reversaleBulk rev = (V_mandato_reversaleBulk) iRev
                                    .next();
                            riten.setImportoRitenute(rev.getIm_documento_cont()
                                    .setScale(2, BigDecimal.ROUND_HALF_UP));
                            riten.setNumeroReversale(rev.getPg_documento_cont()
                                    .intValue());
                            riten.setProgressivoVersante(1);// ???
                            infoben.getRitenute().add(riten);
                        }
                    }
                    mandato.getInformazioniBeneficiario().add(infoben);
                } // if multibeneficiario
                else {
                    infoben.setProgressivoBeneficiario(1);// Dovrebbe essere sempre 1 ?
                    infoben.setImportoBeneficiario(docContabile.getImDocumento().setScale(2, BigDecimal.ROUND_HALF_UP));
                    if (bulk.getIm_documento_cont().compareTo(bulk.getIm_ritenute()) == 0) {
                        infoben.setTipoPagamento(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.COMPENSAZIONE.value());
                    } else if (docContabile.getTiDocumento().compareTo(MandatoBulk.TIPO_REGOLAM_SOSPESO) == 0) {
                        infoben.setTipoPagamento(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.REGOLARIZZAZIONE.value());
                    } else if (tipoPagamentoSiopePlus.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.F24EP)
                            && docContabile.getDtPagamentoRichiesta() == null) {
                        throw new ApplicationMessageFormatException(
                                "Impossibile generare il flusso, indicare data richiesta pagamento nel mandato cds {0} n. {1}",
                                docContabile.getCdCds(), docContabile.getPgDocumento());
                    } else if (tipoPagamentoSiopePlus.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.F24EP)
                            && docContabile.getDtPagamentoRichiesta() != null &&
                            (EJBCommonServices.getServerTimestamp().after(docContabile.getDtPagamentoRichiesta()))) {
                        throw new ApplicationMessageFormatException(
                                "Impossibile generare il flusso, indicare data richiesta pagamento nel mandato cds {0} mandato {1} superiore alla data odierna!",
                                docContabile.getCdCds(), docContabile.getPgDocumento());
                    } else if (tipoPagamentoSiopePlus.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.F24EP) &&
                            docContabile.getDtPagamentoRichiesta() != null &&
                            (EJBCommonServices.getServerTimestamp().before(docContabile.getDtPagamentoRichiesta()))) {
                        infoben.setTipoPagamento(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.F24EP.value());
                        final XMLGregorianCalendar xmlGregorianCalendar =
                                DatatypeFactory.newInstance()
                                        .newXMLGregorianCalendar(
                                                formatterDate.format(
                                                        docContabile.getDtPagamentoRichiesta().toLocalDateTime()
                                                )
                                        );
                        infoben.setDataEsecuzionePagamento(xmlGregorianCalendar);
                        infoben.setDestinazione(LIBERA);
                        infoben.setNumeroContoBancaItaliaEnteRicevente(NUMERO_CONTO_BANCA_ITALIA_ENTE_RICEVENTE);
                        infoben.setTipoContabilitaEnteRicevente(TIPO_CONTABILITA_ENTE_RICEVENTE);
                    } else {
                        infoben.setTipoPagamento(tipoPagamentoSiopePlus.value());
                    }
                    if (tipoPagamentoSiopePlus.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ACCREDITOTESORERIAPROVINCIALESTATOPERTABA)) {
                        infoben.setNumeroContoBancaItaliaEnteRicevente(
                                Optional.ofNullable(docContabile.getNumeroConto())
                                        .filter(s -> s.length() == 7)
                                        .orElseThrow(() -> new ApplicationMessageFormatException("Impossibile generare il flusso, manca il numero conto " +
                                                "sul Mandato {0}/{1}/{2}, oppure il numero conto {3} non ha la lunghezza corretta!",
                                                String.valueOf(bulk.getEsercizio()),
                                                String.valueOf(bulk.getCd_cds()),
                                                String.valueOf(bulk.getPg_documento_cont()),
                                                docContabile.getNumeroConto()
                                        ))
                        );
                        infoben.setTipoContabilitaEnteRicevente(TIPO_CONTABILITA_ENTE_RICEVENTE);
                    }
                    caricaInformazioniAggiuntive(infoben, bulk, aggiuntive, tipoPagamentoSiopePlus);
                    caricaTipoPostalizzazione(infoben, docContabile, tipoPagamentoSiopePlus);
                    infoben.setDestinazione(LIBERA);
                    List listClass = findDocumentiFlussoClass(userContext, bulk);
                    BigDecimal totAssSiope = BigDecimal.ZERO;
                    BigDecimal totAssCup = BigDecimal.ZERO;
                    VDocumentiFlussoBulk oldDoc = null;
                    for (Iterator c = listClass.iterator(); c.hasNext(); ) {
                        VDocumentiFlussoBulk doc = (VDocumentiFlussoBulk) c.next();
                        // 17/01/2018 per non indicare cup nel tag ma solo nella causale e ripartire gli importi solo per siope
                        boolean salta = false;
                        if (infoben.getClassificazione() != null && infoben.getClassificazione().size() != 0) {
                            for (Iterator<Mandato.InformazioniBeneficiario.Classificazione> it = infoben.getClassificazione().iterator(); it.hasNext(); ) {
                                Mandato.InformazioniBeneficiario.Classificazione presente = it.next();
                                if (doc.getCdSiope().compareTo(presente.getCodiceCgu()) == 0 &&
                                        Optional.ofNullable(doc.getCdCup()).equals(Optional.ofNullable(presente.getCodiceCup()))) {
                                    salta = true;
                                    break;
                                }
                            }
                        }
                        if (!salta) {
                            // 17/01/2018 per non indicare cup nel tag - fine aggiunta
                            if (doc.getCdSiope() != null) {
                                // cambio codice siope senza cup dovrebbe essere il resto
                                if (oldDoc != null
                                        && oldDoc.getCdSiope().compareTo(doc.getCdSiope()) != 0
                                        && doc.getCdCup() == null
                                        && totAssSiope.compareTo(totAssCup) != 0
                                        && totAssCup.compareTo(BigDecimal.ZERO) != 0) {
                                    clas = objectFactory.createMandatoInformazioniBeneficiarioClassificazione();
                                    clas.setCodiceCgu(oldDoc.getCdSiope());
                                    clas.setImporto((totAssSiope.subtract(totAssCup)).setScale(2, BigDecimal.ROUND_HALF_UP));
                                    caricaClassificazione(userContext, clas, objectFactory, bulk, oldDoc.getCdSiope());

                                    totAssCup = BigDecimal.ZERO;
                                    totAssSiope = BigDecimal.ZERO;
                                    infoben.getClassificazione().add(clas);

                                    // Carico l'attuale codice SIOPE
                                    clas = objectFactory.createMandatoInformazioniBeneficiarioClassificazione();
                                    clas.setCodiceCgu(doc.getCdSiope());
                                    clas.setImporto(doc.getImportoCge().setScale(2, BigDecimal.ROUND_HALF_UP));
                                    caricaClassificazione(userContext, clas, objectFactory, bulk, doc.getCdSiope());
                                    infoben.getClassificazione().add(clas);

                                } else {
                                    // stesso codice siope senza cup resto
                                    if (oldDoc != null
                                            && oldDoc.getCdSiope().compareTo(
                                            doc.getCdSiope()) == 0
                                            && doc.getCdCup() == null
                                            && totAssSiope.compareTo(totAssCup) != 0
                                            && totAssCup.compareTo(BigDecimal.ZERO) != 0) {
                                        clas = objectFactory.createMandatoInformazioniBeneficiarioClassificazione();
                                        clas.setCodiceCgu(oldDoc.getCdSiope());
                                        clas.setImporto(totAssSiope.subtract(totAssCup)
                                                .setScale(2, BigDecimal.ROUND_HALF_UP));

                                        caricaClassificazione(userContext, clas, objectFactory, bulk, oldDoc.getCdSiope());

                                        totAssCup = BigDecimal.ZERO;
                                        totAssSiope = BigDecimal.ZERO;
                                        infoben.getClassificazione().add(clas);
                                    } else // stesso codice siope con cup
                                        if (oldDoc != null
                                                && oldDoc.getCdSiope().compareTo(
                                                doc.getCdSiope()) == 0
                                                && doc.getCdCup() != null
                                                && totAssSiope.compareTo(totAssCup) != 0
                                                && totAssCup.compareTo(BigDecimal.ZERO) != 0) {
                                            clas = objectFactory.createMandatoInformazioniBeneficiarioClassificazione();
                                            clas.setCodiceCgu(doc.getCdSiope());
                                            clas.setCodiceCup(doc.getCdCup());
                                            clas.setImporto(doc.getImportoCup().setScale(2,
                                                    BigDecimal.ROUND_HALF_UP));
                                            totAssCup = totAssCup.add(doc.getImportoCup());
                                            // Causale Cup
                                            if (infoben.getCausale() != null) {
                                                if (!infoben.getCausale().contains(
                                                        doc.getCdCup()))
                                                    infoben.setCausale(infoben.getCausale()
                                                            + "-" + doc.getCdCup());
                                            } else
                                                infoben.setCausale("CUP " + doc.getCdCup());
                                            caricaClassificazione(userContext, clas, objectFactory, bulk, doc.getCdSiope());

                                            infoben.getClassificazione().add(clas);
                                        } else // stesso siope con cup null precedente
                                            // completamente associato a cup
                                            if (oldDoc != null
                                                    && oldDoc.getCdSiope().compareTo(
                                                    doc.getCdSiope()) == 0
                                                    && doc.getCdCup() == null
                                                    && totAssSiope.compareTo(totAssCup) == 0
                                                    && totAssCup.compareTo(BigDecimal.ZERO) != 0) {
                                                clas = objectFactory.createMandatoInformazioniBeneficiarioClassificazione();
                                                clas.setCodiceCgu(doc.getCdSiope());
                                                clas.setImporto(doc.getImportoCge().setScale(2,
                                                        BigDecimal.ROUND_HALF_UP));
                                                totAssSiope = BigDecimal.ZERO;
                                                totAssCup = BigDecimal.ZERO;
                                                caricaClassificazione(userContext, clas, objectFactory, bulk, doc.getCdSiope());

                                                infoben.getClassificazione().add(clas);
                                            } else // diverso siope con cup null e precedente completamente associato a cup
                                                if (oldDoc != null
                                                        && oldDoc.getCdSiope().compareTo(
                                                        doc.getCdSiope()) != 0
                                                        && doc.getCdCup() == null
                                                        && totAssSiope.compareTo(totAssCup) == 0
                                                        && totAssCup.compareTo(BigDecimal.ZERO) != 0) {
                                                    clas = objectFactory.createMandatoInformazioniBeneficiarioClassificazione();
                                                    clas.setCodiceCgu(doc.getCdSiope());
                                                    clas.setImporto(doc.getImportoCge().setScale(2,
                                                            BigDecimal.ROUND_HALF_UP));
                                                    totAssSiope = BigDecimal.ZERO;
                                                    totAssCup = BigDecimal.ZERO;
                                                    caricaClassificazione(userContext, clas, objectFactory, bulk, doc.getCdSiope());
                                                    infoben.getClassificazione().add(clas);
                                                } else
                                                    // primo inserimento
                                                    if (doc.getCdCup() != null && doc.getImportoCup().compareTo(BigDecimal.ZERO) != 0) {
                                                        clas = objectFactory.createMandatoInformazioniBeneficiarioClassificazione();
                                                        clas.setCodiceCgu(doc.getCdSiope());
                                                        clas.setCodiceCup(doc.getCdCup());
                                                        clas.setImporto(doc.getImportoCup().setScale(2, BigDecimal.ROUND_HALF_UP));
                                                        totAssCup = doc.getImportoCup();
                                                        totAssSiope = doc.getImportoCge();
                                                        // Causale Cup
                                                        if (infoben.getCausale() != null) {
                                                            if (!infoben.getCausale().contains(doc.getCdCup())) {
                                                                infoben.setCausale(infoben.getCausale() + "-" + doc.getCdCup());
                                                            }
                                                        } else {
                                                            infoben.setCausale("CUP " + doc.getCdCup());
                                                        }
                                                        caricaClassificazione(userContext, clas, objectFactory, bulk, doc.getCdSiope());

                                                        infoben.getClassificazione().add(clas);
                                                    } else {
                                                        clas = objectFactory.createMandatoInformazioniBeneficiarioClassificazione();
                                                        clas.setCodiceCgu(doc.getCdSiope());
                                                        clas.setImporto(doc.getImportoCge().setScale(2,
                                                                BigDecimal.ROUND_HALF_UP));
                                                        totAssSiope = doc.getImportoCge();
                                                        caricaClassificazione(userContext, clas, objectFactory, bulk, doc.getCdSiope());
                                                        infoben.getClassificazione().add(clas);
                                                    }
                                }
                                oldDoc = doc;
                            }
                        } // if(!salta){ -- 17/01/2018 per non indicare cup nel tag
                    }
                    // differenza ultimo
                    if (totAssSiope.subtract(totAssCup).compareTo(BigDecimal.ZERO) > 0) {
                        if (totAssCup.compareTo(BigDecimal.ZERO) != 0
                                && totAssCup.compareTo(totAssSiope) != 0) {
                            clas = objectFactory.createMandatoInformazioniBeneficiarioClassificazione();
                            clas.setCodiceCgu(oldDoc.getCdSiope());
                            clas.setImporto((totAssSiope.subtract(totAssCup))
                                    .setScale(2, BigDecimal.ROUND_HALF_UP));
                            //TODO
                            CtClassificazioneDatiSiopeUscite ctClassificazioneDatiSiopeUscite = objectFactory.createCtClassificazioneDatiSiopeUscite();
                            ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(StTipoDebitoNonCommerciale.NON_COMMERCIALE);
                            clas.setClassificazioneDatiSiopeUscite(ctClassificazioneDatiSiopeUscite);

                            infoben.getClassificazione().add(clas);
                        }
                    }
                    bollo.setAssoggettamentoBollo(docContabile.getAssoggettamentoBollo());
                    bollo.setCausaleEsenzioneBollo(docContabile.getCausaleBollo());
                    infoben.setBollo(bollo);

                    final Mandato.InformazioniBeneficiario.Spese mandatoInformazioniBeneficiarioSpese = objectFactory.createMandatoInformazioniBeneficiarioSpese();
                    mandatoInformazioniBeneficiarioSpese.setSoggettoDestinatarioDelleSpese("ESENTE");
                    mandatoInformazioniBeneficiarioSpese.setCausaleEsenzioneSpese("ESENTE");
                    infoben.setSpese(mandatoInformazioniBeneficiarioSpese);

                    benef.setAnagraficaBeneficiario(RemoveAccent
                            .convert(docContabile.getDenominazioneSede())
                            .replace('"', ' ').replace('\u00b0', ' '));
                    // benef.setStatoBeneficiario(docContabile.getCdIso());
                    if (obb_dati_beneficiario) {
                        benef.setIndirizzoBeneficiario(RemoveAccent
                                .convert(docContabile.getViaSede())
                                .replace('"', ' ').replace('\u00b0', ' '));
                        if (docContabile.getCapComuneSede() == null)
                            throw new ApplicationException(
                                    "Impossibile generare il flusso, Cap benificiario non valorizzato per il terzo "
                                            + docContabile.getCdTerzo()
                                            + " cds "
                                            + docContabile.getCdCds()
                                            + " mandato "
                                            + docContabile.getPgDocumento());
                        benef.setCapBeneficiario(docContabile.getCapComuneSede());
                        benef.setLocalitaBeneficiario(RemoveAccent.convert(docContabile.getDsComune())
                                .replace('"', ' ').replace('\u00b0', ' '));
                        benef.setProvinciaBeneficiario(docContabile.getCdProvincia());
                        benef.setStatoBeneficiario(docContabile.getCdIso());
                        benef.setCodiceFiscaleBeneficiario(Optional.ofNullable(docContabile.getCodiceFiscale()).orElse(null));
                        benef.setPartitaIvaBeneficiario(Optional.ofNullable(docContabile.getPartitaIva()).orElse(null));
                    }
                    infoben.setBeneficiario(benef);
                    if (obb_conto  && !infoben.getTipoPagamento().equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.REGOLARIZZAZIONE.value())) {
                        piazzatura.setNumeroContoCorrenteBeneficiario(
                                Optional.ofNullable(docContabile.getNumeroConto())
                                        .orElseThrow(() -> new ApplicationMessageFormatException("Impossibile generare il flusso, manca il numero conto " +
                                                "sul Mandato {0}/{1}/{2}",
                                                String.valueOf(bulk.getEsercizio()),
                                                String.valueOf(bulk.getCd_cds()),
                                                String.valueOf(bulk.getPg_documento_cont())
                                        ))
                        );
                        infoben.setPiazzatura(piazzatura);
                    }
                    if (obb_iban && !infoben.getTipoPagamento().equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.REGOLARIZZAZIONE.value())) {
                        sepa.setIban(
                                Optional.ofNullable(docContabile.getCodiceIban())
                                        .orElseThrow(() -> new ApplicationMessageFormatException("Impossibile generare il flusso, manca il codice iban " +
                                                "sul Mandato {0}/{1}/{2}",
                                                String.valueOf(bulk.getEsercizio()),
                                                String.valueOf(bulk.getCd_cds()),
                                                String.valueOf(bulk.getPg_documento_cont())
                                        ))
                        );
                        if (tipoPagamentoSiopePlus.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.BONIFICOESTEROEURO)) {
                            sepa.setBic(Optional.ofNullable(docContabile.getBic())
                                    .filter(s -> Optional.ofNullable(docContabile.getCodiceIban()).isPresent())
                                    .filter(s -> patternBic.matcher(s).find())
                                    .orElseThrow(() -> new ApplicationMessageFormatException("Impossibile generare il flusso, codice BIC: {0} non valido " +
                                            "sul Mandato {1}/{2}/{3}",
                                            docContabile.getBic(),
                                            String.valueOf(bulk.getEsercizio()),
                                            String.valueOf(bulk.getCd_cds()),
                                            String.valueOf(bulk.getPg_documento_cont())
                                    )));
                        }
                        if ((tipoPagamentoSiopePlus.equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.SEPACREDITTRANSFER))
                                && !infoben.getTipoPagamento().equals(Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.COMPENSAZIONE.value()))
                            sepa.setIdentificativoEndToEnd(docContabile.getEsercizio()
                                    .toString()
                                    + "-"
                                    + docContabile.getCdUoOrigine()
                                    + "-" + docContabile.getPgDocumento().toString());
                        infoben.setSepaCreditTransfer(sepa);
                    }
                    infoben.setCausale(Optional.ofNullable(Optional.ofNullable(docContabile.getDsDocumento())
                            .filter(s -> s.length() > MAX_LENGTH_CAUSALE)
                            .map(s -> s.substring(0, MAX_LENGTH_CAUSALE -1))
                            .orElseGet(() -> docContabile.getDsDocumento()))
                            .map(s -> RemoveAccent.convert(s).replace('"', ' ').replace('\u00b0', ' '))
                            .orElseThrow(() -> new ApplicationMessageFormatException("Impossibile generare il flusso, causale non presente " +
                                    "sul Mandato {0}/{1}/{2}",
                                    String.valueOf(bulk.getEsercizio()),
                                    String.valueOf(bulk.getCd_cds()),
                                    String.valueOf(bulk.getPg_documento_cont()))));
                    // SOSPESO
                    if (docContabile.getTiDocumento().compareTo(
                            MandatoBulk.TIPO_REGOLAM_SOSPESO) == 0) {

                        List listSosp = findDocumentiFlussoSospeso(userContext, bulk);
                        for (Iterator c = listSosp.iterator(); c.hasNext(); ) {
                            boolean sospesoTrovato = false;
                            VDocumentiFlussoBulk doc = (VDocumentiFlussoBulk) c
                                    .next();
                            if (doc.getCdSospeso() != null) {
                                for (Iterator it = infoben.getSospeso().iterator(); it.hasNext(); ) {
                                    Mandato.InformazioniBeneficiario.Sospeso presente = (Mandato.InformazioniBeneficiario.Sospeso) it.next();
                                    Long l = new Long(doc.getCdSospeso().substring(0, doc.getCdSospeso().indexOf(".")).replace(" ", "")).longValue();
                                    if (l.compareTo(presente.getNumeroProvvisorio()) == 0) {
                                        presente.setImportoProvvisorio(presente.getImportoProvvisorio().add(doc.getImAssociato()));
                                        sospesoTrovato = true;
                                        break;
                                    }
                                }
                                if (!sospesoTrovato) {
                                    sosp = objectFactory.createMandatoInformazioniBeneficiarioSospeso();
                                    try {
                                        sosp.setNumeroProvvisorio(new Long(
                                                doc.getCdSospeso()
                                                        .substring(
                                                                0,
                                                                doc.getCdSospeso()
                                                                        .indexOf(".")).replace(" ", ""))
                                                .longValue());
                                    } catch (NumberFormatException e) {
                                        throw new ApplicationException(
                                                "Formato del codice del sospeso non compatibile.");
                                    }
                                    sosp.setImportoProvvisorio(doc.getImAssociato()
                                            .setScale(2, BigDecimal.ROUND_HALF_UP));
                                    infoben.getSospeso().add(sosp);
                                }
                            }
                        }
                    }
                    // Fine sospeso

                    if (bulk.getIm_ritenute().compareTo(BigDecimal.ZERO) != 0) {
                        List list_rev = findReversali(userContext, bulk);
                        for (Iterator iRev = list_rev.iterator(); iRev.hasNext(); ) {
                            riten = objectFactory.createRitenute();
                            V_mandato_reversaleBulk rev = (V_mandato_reversaleBulk) iRev
                                    .next();
                            riten.setImportoRitenute(rev.getIm_documento_cont()
                                    .setScale(2, BigDecimal.ROUND_HALF_UP));
                            riten.setNumeroReversale(rev.getPg_documento_cont()
                                    .intValue());
                            riten.setProgressivoVersante(1);// ???
                            infoben.getRitenute().add(riten);
                        }
                    }
                    mandato.getInformazioniBeneficiario().add(infoben);
                } // end else di multibeneficiario
            }
            return mandato;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private void caricaTipoPostalizzazione(Mandato.InformazioniBeneficiario infoben, VDocumentiFlussoBulk docContabile, Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus tipoPagamentoSiopePlus) throws ApplicationMessageFormatException {
        if (Arrays.asList(
                Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ASSEGNOBANCARIOEPOSTALE,
                Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ASSEGNOCIRCOLARE
        ).contains(tipoPagamentoSiopePlus)) {
            infoben.setTipoPostalizzazione(
                    Optional.ofNullable(docContabile.getTipoPostalizzazione())
                            .orElseThrow(() -> new ApplicationMessageFormatException(
                                    "Impossibile generare il flusso, indicare il tipo postalizzazione sulla modalità di pagamento {0} del mandato {1}/{2}/{3} riferito al terzo {4}!",
                                    docContabile.getModalitaPagamento(),
                                    String.valueOf(docContabile.getEsercizio()),
                                    docContabile.getCdCds(),
                                    String.valueOf(docContabile.getPgDocumento()),
                                    String.valueOf(docContabile.getCdTerzo()))
                            )
            );
        }
    }

    private void caricaInformazioniAggiuntive(Mandato.InformazioniBeneficiario infoben,
                                              V_mandato_reversaleBulk bulk,
                                              Mandato.InformazioniBeneficiario.InformazioniAggiuntive aggiuntive,
                                              Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus tipoPagamentoSiopePlus) {
        if (Arrays.asList(
                Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.DISPOSIZIONEDOCUMENTOESTERNO,
                Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ACCREDITOCONTOCORRENTEPOSTALE
        ).contains(tipoPagamentoSiopePlus)) {
            aggiuntive.setRiferimentoDocumentoEsterno(bulk.getCMISName());
            infoben.setInformazioniAggiuntive(aggiuntive);
        }
    }

    private void caricaClassificazione(UserContext userContext,
                                       Mandato.InformazioniBeneficiario.Classificazione clas,
                                       ObjectFactory objectFactory,
                                       V_mandato_reversaleBulk bulk,
                                       String codiceSiope) throws ComponentException, PersistencyException, IntrospectionException, DatatypeConfigurationException {
        clas.setClassificazioneDatiSiopeUscite(getClassificazioneDatiSiope(userContext, objectFactory, bulk, codiceSiope));
    }

    private CtClassificazioneDatiSiopeUscite getClassificazioneDatiSiope(UserContext userContext,
                                       ObjectFactory objectFactory,
                                       V_mandato_reversaleBulk bulk,
                                       String codiceSiope) throws ComponentException, PersistencyException, IntrospectionException, DatatypeConfigurationException {
        CtClassificazioneDatiSiopeUscite ctClassificazioneDatiSiopeUscite = objectFactory.createCtClassificazioneDatiSiopeUscite();

        Optional<TipoDebitoSIOPE> tipoDebitoSIOPE = Optional.ofNullable(bulk.getTipo_debito_siope())
                .map(s -> TipoDebitoSIOPE.getValueFrom(s));
        if (!tipoDebitoSIOPE.isPresent() || (tipoDebitoSIOPE.isPresent() && tipoDebitoSIOPE.get().equals(TipoDebitoSIOPE.COMMERCIALE))) {
            MandatoIHome mandatoHome = Optional.ofNullable(getHome(userContext, MandatoIBulk.class))
                    .filter(MandatoIHome.class::isInstance)
                    .map(MandatoIHome.class::cast)
                    .orElseThrow(() -> new ComponentException("Home del mandato non trovata!"));
            MandatoBulk mandatoBulk = Optional.ofNullable(
                    mandatoHome.findByPrimaryKey(
                            new MandatoIBulk(bulk.getCd_cds(), bulk.getEsercizio(), bulk.getPg_documento_cont()
                            )
                    )).filter(MandatoBulk.class::isInstance)
                    .map(MandatoBulk.class::cast)
                    .orElseThrow(() -> new ComponentException("Mandato non trovato!"));
            final List<Mandato_siopeBulk> siopeBulks = mandatoHome.findMandato_siope(userContext, mandatoBulk)
                    .stream()
                    .filter(Mandato_siopeBulk.class::isInstance)
                    .map(Mandato_siopeBulk.class::cast)
                    .filter(o -> o.getCd_siope().equals(Optional.ofNullable(codiceSiope).orElse(o.getCd_siope())))
                    .collect(Collectors.toList());

            final Optional<Mandato_siopeBulk> mandatoDaFattura = siopeBulks
                    .stream()
                    .filter(mandato_siopeBulk ->
                            mandato_siopeBulk.getCd_tipo_documento_amm().equals(Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA)
                     )
                    .findAny();

            final Optional<Mandato_siopeBulk> mandatoDaCompenso = siopeBulks
                    .stream()
                    .filter(mandato_siopeBulk -> mandato_siopeBulk.getCd_tipo_documento_amm().equals(Numerazione_doc_ammBulk.TIPO_COMPENSO))
                    .findAny();
            Fattura_passivaHome fattura_passivaHome = Optional.ofNullable(getHome(userContext, Fattura_passiva_IBulk.class))
                    .filter(Fattura_passivaHome.class::isInstance)
                    .map(Fattura_passivaHome.class::cast)
                    .orElseThrow(() -> new ComponentException("Home della fattura non trovata!"));
            TerzoHome terzoHome = Optional.ofNullable(getHome(userContext, TerzoBulk.class))
                    .filter(TerzoHome.class::isInstance)
                    .map(TerzoHome.class::cast)
                    .orElseThrow(() -> new ComponentException("Home della terzo non trovato!"));

            if (mandatoDaFattura.isPresent()) {
                final Map<Fattura_passivaBulk, Double> collect = siopeBulks
                        .stream()
                        .map(mandato_siopeBulk -> {
                            try {
                                return new AbstractMap.SimpleEntry<Fattura_passivaBulk, BigDecimal>(Optional.ofNullable(
                                        fattura_passivaHome.findByPrimaryKey(
                                                new Fattura_passiva_IBulk(
                                                        mandato_siopeBulk.getCd_cds_doc_amm(),
                                                        mandato_siopeBulk.getCd_uo_doc_amm(),
                                                        mandato_siopeBulk.getEsercizio_doc_amm(),
                                                        mandato_siopeBulk.getPg_doc_amm()
                                                )
                                        )
                                ).filter(Fattura_passivaBulk.class::isInstance)
                                        .map(Fattura_passivaBulk.class::cast)
                                        .orElseThrow(() -> new ApplicationMessageFormatException(
                                                "Generazione flusso interrotta in quanto non è stata trovata la fattura {0}/{1}/{2} asscociata al mandato {3}/{4}/{5}",
                                                String.valueOf(mandato_siopeBulk.getEsercizio_doc_amm()),
                                                String.valueOf(mandato_siopeBulk.getCd_cds_doc_amm()),
                                                String.valueOf(mandato_siopeBulk.getPg_doc_amm()),
                                                String.valueOf(bulk.getEsercizio()),
                                                String.valueOf(bulk.getCd_cds()),
                                                String.valueOf(bulk.getPg_documento_cont())
                                        )), mandato_siopeBulk.getImporto());
                            } catch (ComponentException | PersistencyException e) {
                                throw new DetailedRuntimeException(e);
                            }
                        })
                        .collect(Collectors.groupingBy(o -> o.getKey(), Collectors.summingDouble(value -> value.getValue().doubleValue())));

                getHomeCache(userContext).fetchAll(userContext);
                final boolean isCommerciale = collect
                        .keySet()
                        .stream()
                        .filter(fattura_passivaBulk -> !(fattura_passivaBulk.isEstera() ||
                                fattura_passivaBulk.isSanMarinoConIVA() ||
                                fattura_passivaBulk.isSanMarinoSenzaIVA()))
                        .findAny().isPresent();
                if (!isCommerciale) {
                    ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(StTipoDebitoNonCommerciale.NON_COMMERCIALE);
                } else {
                    ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(StTipoDebitoCommerciale.COMMERCIALE);
                    Fattura_passiva_rigaHome fattura_passivaRigaHome = Optional.ofNullable(getHome(userContext, Fattura_passiva_rigaIBulk.class))
                            .filter(Fattura_passiva_rigaHome.class::isInstance)
                            .map(Fattura_passiva_rigaHome.class::cast)
                            .orElseThrow(() -> new ComponentException("Home della fattura non trovata!"));

                    List<String> codiciCIG = new ArrayList<String>();
                    List<String> motiviAssenzaCIG = new ArrayList<String>();
                    boolean isExistsFatturaEstera = false;
                    for(Mandato_siopeBulk siopeBulk : siopeBulks) {
                        if(siopeBulk.getCd_tipo_documento_amm().equals(Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA)) {
                            final Fattura_passivaBulk fattura_passivaBulk = Optional.ofNullable(
                                    fattura_passivaHome.findByPrimaryKey(
                                            new Fattura_passiva_IBulk(
                                                    siopeBulk.getCd_cds_doc_amm(),
                                                    siopeBulk.getCd_uo_doc_amm(),
                                                    siopeBulk.getEsercizio_doc_amm(),
                                                    siopeBulk.getPg_doc_amm()
                                            )
                                    )
                            ).filter(Fattura_passivaBulk.class::isInstance)
                                    .map(Fattura_passivaBulk.class::cast)
                                    .orElseThrow(() -> new ComponentException("Fattura non trovata!"));
                            if (!(fattura_passivaBulk.isEstera() ||
                                    fattura_passivaBulk.isSanMarinoConIVA() ||
                                    fattura_passivaBulk.isSanMarinoSenzaIVA())) {
                                isExistsFatturaEstera = true;
                                codiciCIG.addAll(fattura_passivaRigaHome.findCodiciCIG(fattura_passivaBulk, mandatoBulk, siopeBulk));
                                motiviAssenzaCIG.addAll(fattura_passivaRigaHome.findMotiviEsclusioneCIG(fattura_passivaBulk, mandatoBulk, siopeBulk));
                            }
                        }
                    }
                    codiciCIG = codiciCIG.stream().distinct().collect(Collectors.toList());
                    motiviAssenzaCIG = motiviAssenzaCIG.stream().distinct().collect(Collectors.toList());

                    if (codiciCIG.isEmpty() && motiviAssenzaCIG.isEmpty()) {
                        throw new ApplicationMessageFormatException("Generazione flusso interrotta in quanto al mandato {0}/{1}/{2} sono associate fatture " +
                                "su cui non è posssibile determinare il CIG!",
                                String.valueOf(bulk.getEsercizio()),
                                String.valueOf(bulk.getCd_cds()),
                                String.valueOf(bulk.getPg_documento_cont())
                        );
                    }
                    if (codiciCIG.size() > 0 && motiviAssenzaCIG.size() > 0) {
                        throw new ApplicationMessageFormatException("Generazione flusso interrotta in quanto al mandato {0}/{1}/{2} sono associate fatture " +
                                "sia con CIG che con motivo di assenza CIG!",
                                String.valueOf(bulk.getEsercizio()),
                                String.valueOf(bulk.getCd_cds()),
                                String.valueOf(bulk.getPg_documento_cont())
                        );
                    }
                    if (codiciCIG.size() > 1) {
                        throw new ApplicationMessageFormatException("Generazione flusso interrotta in quanto al mandato {0}/{1}/{2} sono associate fatture " +
                                "con CIG diversi : {3}!",
                                String.valueOf(bulk.getEsercizio()),
                                String.valueOf(bulk.getCd_cds()),
                                String.valueOf(bulk.getPg_documento_cont()),
                                String.join(" - ", codiciCIG.stream().map(o -> ((Object) o).toString()).collect(Collectors.toList()))
                        );
                    }
                    if (motiviAssenzaCIG.size() > 1) {
                        throw new ApplicationMessageFormatException("Generazione flusso interrotta in quanto al mandato {0}/{1}/{2} sono associate fatture " +
                                "con motivi di assenza CIG diversi : {3}!",
                                String.valueOf(bulk.getEsercizio()),
                                String.valueOf(bulk.getCd_cds()),
                                String.valueOf(bulk.getPg_documento_cont()),
                                String.join(" - ", motiviAssenzaCIG.stream().map(o -> ((Object) o).toString()).collect(Collectors.toList()))
                        );
                    }
                    if (codiciCIG.stream().findAny().isPresent()) {
                        ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(codiciCIG.stream().findAny().get());
                    }
                    if (motiviAssenzaCIG.stream().findAny().isPresent()) {
                        ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(
                                StMotivoEsclusioneCigSiope.valueOf(motiviAssenzaCIG.stream().findAny().get().toString())
                        );
                    }
                    collect.entrySet()
                            .stream()
                            .forEach(fattura_passivaBulkDoubleEntry -> {
                                Fattura_passivaBulk fattura_passivaBulk = fattura_passivaBulkDoubleEntry.getKey();
                                BigDecimal importo = BigDecimal.valueOf(fattura_passivaBulkDoubleEntry.getValue());
                                CtFatturaSiope ctFatturaSiope = objectFactory.createCtFatturaSiope();
                                if (Optional.ofNullable(fattura_passivaBulk.getDocumentoEleTestata()).isPresent()) {
                                    ctFatturaSiope.setCodiceIpaEnteSiope(fattura_passivaBulk.getDocumentoEleTestata().getDocumentoEleTrasmissione().getCodiceDestinatario());
                                    ctFatturaSiope.setTipoDocumentoSiopeE(StTipoDocumentoElettronico.ELETTRONICO);
                                    ctFatturaSiope.setIdentificativoLottoSdiSiope(
                                            fattura_passivaBulk.getDocumentoEleTestata().getDocumentoEleTrasmissione().getIdentificativoSdi() +
                                                    fattura_passivaBulk.getDocumentoEleTestata().getProgressivo()
                                    );
                                } else {
                                    try {
                                        ctFatturaSiope.setCodiceIpaEnteSiope(terzoHome.findCodiceUnivocoUfficioIPA(fattura_passivaBulk.getCd_unita_organizzativa()));
                                    } catch (IntrospectionException|PersistencyException e) {
                                        throw new DetailedRuntimeException(e);
                                    }
                                    ctFatturaSiope.setTipoDocumentoSiopeA(StTipoDocumentoAnalogico.ANALOGICO);
                                    ctFatturaSiope.setAnnoEmissioneFatturaSiope(fattura_passivaBulk.getEsercizio());
                                    ctFatturaSiope.setCodiceFiscaleEmittenteSiope(fattura_passivaBulk.getCodice_fiscale());
                                    ctFatturaSiope.setTipoDocumentoAnalogicoSiope(FATT_ANALOGICA);
                                }
                                CtDatiFatturaSiope ctDatiFatturaSiope = objectFactory.createCtDatiFatturaSiope();
                                ctDatiFatturaSiope.setNumeroFatturaSiope(fattura_passivaBulk.getNr_fattura_fornitore());
                                ctDatiFatturaSiope.setNaturaSpesaSiope(CORRENTE);
                                ctDatiFatturaSiope.setDataScadenzaPagamSiope(convertToXMLGregorianCalendar(fattura_passivaBulk.getDt_scadenza()));
                                //TODO CONTROLLARE SE NOTA
                                ctDatiFatturaSiope.setImportoSiope(importo.setScale(2, BigDecimal.ROUND_HALF_UP));

                                ctFatturaSiope.setDatiFatturaSiope(ctDatiFatturaSiope);
                                ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(ctFatturaSiope);
                            });

                }
            } else if (mandatoDaCompenso.isPresent()){
                CompensoHome compensoHome = Optional.ofNullable(getHome(userContext, CompensoBulk.class))
                        .filter(CompensoHome.class::isInstance)
                        .map(CompensoHome.class::cast)
                        .orElseThrow(() -> new ComponentException("Home del compenso non trovato!"));
                Tipo_trattamentoHome tipo_trattamentoHome = Optional.ofNullable(getHome(userContext, Tipo_trattamentoBulk.class))
                        .filter(Tipo_trattamentoHome.class::isInstance)
                        .map(Tipo_trattamentoHome.class::cast)
                        .orElseThrow(() -> new ComponentException("Home del trattamento non trovato!"));

                final CompensoBulk compensoBulk = Optional.ofNullable(
                        compensoHome.findByPrimaryKey(
                                new CompensoBulk(
                                        mandatoDaCompenso.get().getCd_cds_doc_amm(),
                                        mandatoDaCompenso.get().getCd_uo_doc_amm(),
                                        mandatoDaCompenso.get().getEsercizio_doc_amm(),
                                        mandatoDaCompenso.get().getPg_doc_amm()
                                )
                        )
                ).filter(CompensoBulk.class::isInstance)
                        .map(CompensoBulk.class::cast)
                        .orElseThrow(() -> new ComponentException("Compenso non trovato!"));
                getHomeCache(userContext).fetchAll(userContext);
                final TipoDebitoSIOPE tipoDebitoSIOPETrattamento = Optional.ofNullable(compensoBulk.getTipoTrattamento())
                        .map(tipo_trattamentoBulk -> {
                            try {
                                return (Tipo_trattamentoBulk)tipo_trattamentoHome.findByPrimaryKey(tipo_trattamentoBulk);
                            } catch (PersistencyException e) {
                                throw new DetailedRuntimeException(e);
                            }
                        })
                        .flatMap(tipo_trattamentoBulk -> Optional.ofNullable(tipo_trattamentoBulk.getTipoDebitoSiope()))
                        .map(s -> TipoDebitoSIOPE.getValueFrom(s))
                        .orElse(TipoDebitoSIOPE.NON_COMMERCIALE);
                switch (tipoDebitoSIOPETrattamento) {
                    case IVA: {
                        ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(StTipoDebitoNonCommerciale.IVA);
                        break;
                    }
                    case NON_COMMERCIALE: {
                        ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(StTipoDebitoNonCommerciale.NON_COMMERCIALE);
                        break;
                    }
                    case COMMERCIALE: {
                        ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(StTipoDebitoCommerciale.COMMERCIALE);
                        final Optional<String> cigCompenso = Optional.ofNullable(compensoBulk.getCig()).map(CigBulk::getCdCig).filter(s -> Optional.ofNullable(s).isPresent());
                        final Optional<String> motivoAssenzaCigCompenso = Optional.ofNullable(compensoBulk.getMotivo_assenza_cig()).filter(s -> Optional.ofNullable(s).isPresent());
                        if (cigCompenso.isPresent() && motivoAssenzaCigCompenso.isPresent()) {
                            throw new ApplicationMessageFormatException("Generazione flusso interrotta in quanto al mandato {0}/{1}/{2} è associato un compenso " +
                                    "sia con CIG che con motivo di assenza CIG!",
                                    String.valueOf(bulk.getEsercizio()),
                                    String.valueOf(bulk.getCd_cds()),
                                    String.valueOf(bulk.getPg_documento_cont())
                            );
                        }
                        if (!cigCompenso.isPresent() && !motivoAssenzaCigCompenso.isPresent()){
                            throw new ApplicationMessageFormatException("Generazione flusso interrotta in quanto al mandato {0}/{1}/{2} è associato un compenso " +
                                    "su cui non è posssibile determinare il CIG!",
                                    String.valueOf(bulk.getEsercizio()),
                                    String.valueOf(bulk.getCd_cds()),
                                    String.valueOf(bulk.getPg_documento_cont())
                            );
                        }
                        if (cigCompenso.isPresent())
                            ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(cigCompenso.get());
                        if (motivoAssenzaCigCompenso.isPresent())
                            ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(StMotivoEsclusioneCigSiope.valueOf(motivoAssenzaCigCompenso.get()));

                        final Optional<Fattura_passivaBulk> fattura_passivaBulk = Optional.ofNullable(compensoBulk.getFatturaPassiva());
                        if (fattura_passivaBulk.isPresent()) {
                            CtFatturaSiope ctFatturaSiope = objectFactory.createCtFatturaSiope();
                            if (Optional.ofNullable(fattura_passivaBulk.get().getDocumentoEleTestata()).isPresent()) {
                                ctFatturaSiope.setCodiceIpaEnteSiope(fattura_passivaBulk.get().getDocumentoEleTestata().getDocumentoEleTrasmissione().getCodiceDestinatario());
                                ctFatturaSiope.setTipoDocumentoSiopeE(StTipoDocumentoElettronico.ELETTRONICO);
                                ctFatturaSiope.setIdentificativoLottoSdiSiope(
                                        fattura_passivaBulk.get().getDocumentoEleTestata().getDocumentoEleTrasmissione().getIdentificativoSdi() +
                                                fattura_passivaBulk.get().getDocumentoEleTestata().getProgressivo()
                                );
                            } else {
                                try {
                                    ctFatturaSiope.setCodiceIpaEnteSiope(terzoHome.findCodiceUnivocoUfficioIPA(fattura_passivaBulk.get().getCd_unita_organizzativa()));
                                } catch (IntrospectionException|PersistencyException e) {
                                    throw new DetailedRuntimeException(e);
                                }
                                ctFatturaSiope.setTipoDocumentoSiopeA(StTipoDocumentoAnalogico.ANALOGICO);
                                ctFatturaSiope.setAnnoEmissioneFatturaSiope(fattura_passivaBulk.get().getEsercizio());
                                ctFatturaSiope.setCodiceFiscaleEmittenteSiope(fattura_passivaBulk.get().getCodice_fiscale());
                                ctFatturaSiope.setTipoDocumentoAnalogicoSiope(FATT_ANALOGICA);
                            }
                            CtDatiFatturaSiope ctDatiFatturaSiope = objectFactory.createCtDatiFatturaSiope();
                            ctDatiFatturaSiope.setNumeroFatturaSiope(fattura_passivaBulk.get().getNr_fattura_fornitore());
                            ctDatiFatturaSiope.setNaturaSpesaSiope(CORRENTE);
                            ctDatiFatturaSiope.setDataScadenzaPagamSiope(convertToXMLGregorianCalendar(fattura_passivaBulk.get().getDt_scadenza()));
                            //TODO CONTROLLARE SE NOTA
                            ctDatiFatturaSiope.setImportoSiope(fattura_passivaBulk.get().getIm_totale_fattura().setScale(2, BigDecimal.ROUND_HALF_UP));
                            ctFatturaSiope.setDatiFatturaSiope(ctDatiFatturaSiope);
                            ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(ctFatturaSiope);
                        } else {
                            CtFatturaSiope ctFatturaSiope = objectFactory.createCtFatturaSiope();
                            ctFatturaSiope.setTipoDocumentoSiopeA(StTipoDocumentoAnalogico.ANALOGICO);
                            ctFatturaSiope.setAnnoEmissioneFatturaSiope(compensoBulk.getEsercizio());
                            ctFatturaSiope.setCodiceFiscaleEmittenteSiope(compensoBulk.getCodice_fiscale());
                            ctFatturaSiope.setTipoDocumentoAnalogicoSiope(DOC_EQUIVALENTE);
                            ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(ctFatturaSiope);
                        }
                        break;
                    }
                }
            } else {
                ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(StTipoDebitoNonCommerciale.NON_COMMERCIALE);
            }

        } else {
            switch (tipoDebitoSIOPE.get()) {
                case IVA: {
                    ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(StTipoDebitoNonCommerciale.IVA);
                    break;
                }
                case NON_COMMERCIALE: {
                    ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(StTipoDebitoNonCommerciale.NON_COMMERCIALE);
                    break;
                }
            }
        }
        return ctClassificazioneDatiSiopeUscite;
    }

    private XMLGregorianCalendar convertToXMLGregorianCalendar(Timestamp timestamp) {
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(timestamp.toLocalDateTime().toLocalDate().toString());
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException("Cannot convert timpestamp to XMLGregorianCalendar " + timestamp.toInstant().toString());
        }
    }

    public Long findMaxMovimentoContoEvidenza(UserContext userContext, MovimentoContoEvidenzaBulk movimentoContoEvidenzaBulk) throws ComponentException {
        MovimentoContoEvidenzaHome movimentoContoEvidenzaHome = Optional.ofNullable(getHome(userContext, MovimentoContoEvidenzaBulk.class))
                .filter(MovimentoContoEvidenzaHome.class::isInstance)
                .map(MovimentoContoEvidenzaHome.class::cast)
                .orElseThrow(() -> new ComponentException("Home MovimentoContoEvidenzaHome non trovata!"));
        try {
            return Optional.ofNullable(movimentoContoEvidenzaHome.findMax(movimentoContoEvidenzaBulk, "progressivo"))
                    .filter(Long.class::isInstance)
                    .map(Long.class::cast)
                    .orElse(new Long(0));
        } catch (PersistencyException e) {
           throw handleException(e);
        }
    }

}