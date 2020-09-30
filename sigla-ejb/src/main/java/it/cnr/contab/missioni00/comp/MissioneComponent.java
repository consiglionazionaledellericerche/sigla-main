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

package it.cnr.contab.missioni00.comp;

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoHome;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoHome;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneHome;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoHome;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoHome;
import it.cnr.contab.compensi00.tabrif.bulk.Filtro_trattamentoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoHome;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable;
import it.cnr.contab.docamm00.ejb.ProgressiviAmmComponentSession;
import it.cnr.contab.docamm00.ejb.RiportoDocAmmComponentSession;
import it.cnr.contab.docamm00.tabrif.bulk.CambioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.CambioHome;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaHome;
import it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession;
import it.cnr.contab.doccont00.core.DatiFinanziariScadenzeDTO;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession;
import it.cnr.contab.missioni00.docs.bulk.*;
import it.cnr.contab.missioni00.tabrif.bulk.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashMap;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.IPrintMgr;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;

public class MissioneComponent extends CRUDComponent implements IMissioneMgr, Cloneable, Serializable, IPrintMgr {
    private transient final static Logger logger = LoggerFactory.getLogger(MissioneComponent.class);

    /**
     * MissioneComponent constructor comment.
     */
    public MissioneComponent() {
        super();
    }

    private void addStaticClause(SQLBuilder sql, MissioneBulk missione) {
        sql.addSQLClause("AND", "v_terzo_per_compenso.TI_DIPENDENTE_ALTRO", sql.EQUALS, missione.getTi_anagrafico());
        sql.addSQLClause("AND", "v_terzo_per_compenso.DT_INI_VALIDITA", sql.LESS_EQUALS, missione.getDt_inizio_missione());
        sql.addSQLClause("AND", "v_terzo_per_compenso.DT_FIN_VALIDITA", sql.GREATER_EQUALS, missione.getDt_inizio_missione());
    }

    /**
     * Aggiorna anticipo
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Aggiorna anticipo
     * Pre:  L'utente ha richiesto l'inserimento/modifica di una missione
     * L'utente ha specificato un anticipo per la missione
     * Post: Il sistema aggiona il flag 'associato a missione' dell'anticipo
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    anticipo    l'AnticipoBulk da aggiornare
     * @param    flg valore con cui impostare il flag associato a missione
     */

    private void aggiornaAnticipo(it.cnr.jada.UserContext userContext, AnticipoBulk anticipo, Boolean flg) throws ComponentException {
        try {
            anticipo.setFl_associato_missione(flg);
            anticipo.setToBeUpdated();
            updateBulk(userContext, anticipo);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(anticipo, e);
        }
    }

    private void aggiornaCogeCoan(
            UserContext userContext,
            MissioneBulk missione,
            IDocumentoContabileBulk docCont)
            throws ComponentException {

        try {
            if (docCont != null && missione != null && missione.getDefferredSaldi() != null) {
                IDocumentoContabileBulk key = missione.getDefferredSaldoFor(docCont);
                if (key != null) {
                    java.util.Map values = (java.util.Map) missione.getDefferredSaldi().get(key);

                    //caso di creazione o di nessuna modifica sui doc cont
                    if (values == null) return;

                    //QUI chiamare component del documento contabile interessato
                    String jndiName = null;
                    Class clazz = null;
                    DocumentoContabileComponentSession session = null;
                    if (docCont instanceof ObbligazioneBulk) {
                        jndiName = "CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession";
                        clazz = ObbligazioneAbstractComponentSession.class;
                        session =
                                (ObbligazioneAbstractComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
                                        jndiName, clazz);
                    } else if (docCont instanceof AccertamentoBulk) {
                        jndiName = "CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession";
                        clazz = AccertamentoAbstractComponentSession.class;
                        session =
                                (AccertamentoAbstractComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
                                        jndiName, clazz);
                    }
                    if (session != null) {
                        session.aggiornaCogeCoanInDifferita(userContext, key, values);
                        missione.getDefferredSaldi().remove(key);
                    }
                }
            }
        } catch (javax.ejb.EJBException e) {
            throw handleException(missione, e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(missione, e);
        }
    }

    private void aggiornaCogeCoanDocAmm(
            UserContext userContext,
            MissioneBulk missione)
            throws ComponentException {

        if (missione == null || missione.isSalvataggioTemporaneo()) return;

        aggiornaCogeCoanObbligazioniDaCancellare(userContext, missione);
        aggiornaCogeCoanObbligazioni(userContext, missione);
    }

    private void aggiornaCogeCoanObbligazioni(
            UserContext userContext,
            MissioneBulk missione)
            throws ComponentException {

        if (missione != null) {
            ObbligazioniTable obbligazioniHash = missione.getObbligazioniHash();
            if (obbligazioniHash != null && !obbligazioniHash.isEmpty()) {

                //Aggiorna coge coan per le obbligazioni NON temporanee
                for (java.util.Enumeration e = getDocumentiContabiliNonTemporanei(userContext, ((ObbligazioniTable) obbligazioniHash.clone()).keys()).keys(); e.hasMoreElements(); )
                    aggiornaCogeCoan(
                            userContext,
                            missione,
                            (IDocumentoContabileBulk) e.nextElement());

            }
        }
    }

    private void aggiornaCogeCoanObbligazioniDaCancellare(
            UserContext userContext,
            MissioneBulk missione)
            throws ComponentException {

        if (missione != null) {
            if (missione.getDocumentiContabiliCancellati() != null &&
                    !missione.getDocumentiContabiliCancellati().isEmpty() &&
                    missione.getObbligazioniHash() != null) {

                for (java.util.Enumeration e = missione.getDocumentiContabiliCancellati().elements(); e.hasMoreElements(); ) {
                    OggettoBulk oggettoBulk = (OggettoBulk) e.nextElement();
                    if (oggettoBulk instanceof Obbligazione_scadenzarioBulk) {
                        Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) oggettoBulk;
                        if (!scadenza.getObbligazione().isTemporaneo()) {
                            PrimaryKeyHashtable obbligs = getDocumentiContabiliNonTemporanei(userContext, missione.getObbligazioniHash().keys());
                            if (!obbligs.containsKey(scadenza.getObbligazione()))
                                aggiornaCogeCoan(
                                        userContext,
                                        missione,
                                        scadenza.getObbligazione());
                        }
                    }
                }
            }
        }
    }

    /**
     * Aggiorna obbligazione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Aggiorna obbligazione temporanea
     * Pre:  L'utente ha richiesto l'inserimento/modifica di una missione
     * L'utente ha creato una nuova obbligazione nel contesto transazionale della missione
     * Post: Il sistema assegna un progressivo definitivo all'obbligazione (metodo 'aggiornaObbligazionetemporanea'),
     * aggiona l'importo associato a doc. amministrativi della scadenza di obbligazione, aggiorna il saldo dell'obbligazione
     * (metodo 'aggiornaSaldi')
     * <p>
     * Nome: Aggiorna obbligazione non temporanea
     * Pre:  L'utente ha richiesto l'inserimento/modifica di una missione
     * L'utente ha selezionato una scadenza di obbligazione già esistente
     * Post: Il sistema aggiorna l'importo associato a doc. amministrativi della scadenza di obbligazione e aggiorna il saldo dell'obbligazione
     * (metodo 'aggiornaSaldi')
     * <p>
     * Nome: Elimina associazione con obbligazione
     * Pre:  L'utente ha richiesto la modifica di una missione
     * L'utente ha eliminato un'associazione con una scadenza di obbligazione
     * Post: Il sistema azzera l'importo associato a doc. amministrativi della scadenza di obbligazione e aggiorna il saldo dell'obbligazione
     * (metodo 'aggiornaSaldi')
     * <p>
     * Nome: Aggiorna saldi per obbligazione del compenso
     * Pre:  Il sistema salva una missione legata ad un compenso con associato una obbligazione
     * Post: Il sistema aggiorna il saldo dell'obbligazione legata al compenso (metodo 'aggiornaSaldi')
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    missione    la MissioneBulk da creare/modificare
     * @param    status        serve per gestire l'eccezione lanciata dall'obbligazione
     * nel caso non ci sia disponibilita' di cassa
     */

    private void aggiornaObbligazione(UserContext userContext, MissioneBulk missione, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status) throws ComponentException {
        Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) missione.getObbligazione_scadenzario();

        // Ciclo sulle scadenze precedentementi associate alla missione.
        // Se nuove le inserisco comunque in tabella con "im_associato_doc_amm" nullo e ne aggiorno i saldi
        // Se non nuove le aggiorno azzerando il loro "im_associato_doc_amm"
        Obbligazione_scadenzarioBulk aScadenzaNonAssociata;
        if (missione.getDocumentiContabiliCancellati() != null) {
            for (Iterator i = missione.getDocumentiContabiliCancellati().iterator(); i.hasNext(); ) {
                aScadenzaNonAssociata = (Obbligazione_scadenzarioBulk) i.next();
                if (scadenza == null || !aScadenzaNonAssociata.getObbligazione().equalsByPrimaryKey(scadenza.getObbligazione())) {
                    // se la scadenza appartiene alla stessa obbligazione della scadenza associata alla missione
                    // non aggiornare i saldi altrimenti lo faresti due volte (l'aggiornamento dei saldi va per
                    // obbligazione e non per scadenze)
                    aggiornaSaldi(userContext, missione, aScadenzaNonAssociata.getObbligazione(), status);

                    if (aScadenzaNonAssociata.getObbligazione().isTemporaneo())
                        aggiornaObbligazioneTemporanea(userContext, aScadenzaNonAssociata.getObbligazione());
                }

                aScadenzaNonAssociata.setIm_associato_doc_amm((new BigDecimal(0)));
                updateImportoAssociatoDocAmm(userContext, aScadenzaNonAssociata);
            }
        }

        if (missione.isMissioneConObbligazione()) {
            // Se la scadenza era gia' stata creata e non e' stata modificata
            // il metodo non aggiornera' alcun saldo
            aggiornaSaldi(userContext, missione, scadenza.getObbligazione(), status);

            if (scadenza.getObbligazione().isTemporaneo())
                aggiornaObbligazioneTemporanea(userContext, scadenza.getObbligazione());

            scadenza.setIm_associato_doc_amm(missione.getImporto_scadenza_obbligazione());
            updateImportoAssociatoDocAmm(userContext, scadenza);
        }

        //	Aggiorno i saldi dell'obbligazione del compenso
        if (missione.isMissioneConCompenso())
            aggiornaSaldiPerCompenso(userContext, missione, status);
    }

    /**
     * Aggiorna obbligazione temporanea
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Aggiorna obbligazione temporanea
     * Pre:  L'utente ha richiesto l'inserimento/modifica di una missione
     * L'utente ha creato una nuova obbligazione nel contesto transazionale della missione e pertanto
     * questa obbligazione ha un progressivo temporaneo
     * Post: Il sistema assegna la numerazione definitiva all'obbligazione creata nel contesto della missione
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    obbligazioneTemporanea    l'ObbligazioneBulk con numerazione temporanea da rendere definitiva
     */

    private void aggiornaObbligazioneTemporanea(UserContext userContext, ObbligazioneBulk obbligazioneTemporanea) throws ComponentException {
        try {
            Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache(userContext).getHome(Numerazione_doc_contBulk.class);
            Long pg = null;
            pg = numHome.getNextPg(userContext,
                    obbligazioneTemporanea.getEsercizio(),
                    obbligazioneTemporanea.getCd_cds(),
                    obbligazioneTemporanea.getCd_tipo_documento_cont(),
                    obbligazioneTemporanea.getUser());
            ObbligazioneHome home = (ObbligazioneHome) getHome(userContext, obbligazioneTemporanea);
            home.confirmObbligazioneTemporanea(userContext, obbligazioneTemporanea, pg);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(obbligazioneTemporanea, e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(obbligazioneTemporanea, e);
        }
    }

    /**
     * Aggiorna saldi obbligazione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Aggiorna saldi obbligazione
     * Pre:  L'utente ha richiesto l'inserimento/modifica di una missione
     * L'utente ha associato una obbligazione alla missione
     * Post: Il sistema richiede alla Component che gestisce l'Obbligazione l'aggiornamento in differita dei saldi
     *
     * @param docCont il documento contabile di tipo ObbligazioneBulk per cui chiedere l'aggiornamento dei saldi
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    missione        la MissioneBulk a cui e' stata associata una scadenza di obbligazione
     * @param    status        serve per gestire l'eccezione lanciata dall'obbligazione
     * nel caso non ci sia disponibilita' di cassa
     */

    private void aggiornaSaldi(it.cnr.jada.UserContext userContext, MissioneBulk missione, IDocumentoContabileBulk docCont, OptionRequestParameter status) throws ComponentException {
        try {
            if (docCont != null && missione != null && missione.getDefferredSaldi() != null) {
                IDocumentoContabileBulk key = missione.getDefferredSaldoFor(docCont);
                if (key != null) {
                    java.util.Map values = (java.util.Map) missione.getDefferredSaldi().get(key);

                    if (values != null) {
                        //QUI chiamare component del documento contabile interessato
                        String jndiName = null;
                        Class clazz = null;
                        it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession session = null;
                        if (docCont instanceof ObbligazioneBulk) {
                            jndiName = "CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession";
                            clazz = it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession.class;
                            session = (it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB(jndiName, clazz);
                        }
                        if (session != null) {
                            session.aggiornaSaldiInDifferita(userContext, key, values, status);
                            //NON Differibile: si rischia di riprocessare i saldi impropriamente
                            missione.getDefferredSaldi().remove(key);
                        }
                    }
                }
            }
        } catch (javax.ejb.EJBException e) {
            throw handleException(missione, e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(missione, e);
        }
    }

    /**
     * Aggiorna saldi obbligazione compenso
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Aggiorna saldi obbligazione compenso
     * Pre:  Il sistema sta salvando una missione aasociata a compenso
     * Post: Il sistema aggiorna i saldi per le scadenze di obbligazione elaborate dal compeso associato alla
     * missione
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk        la MissioneBulk legata al compenso
     * @param    status        serve per gestire l'eccezione lanciata dall'obbligazione
     * nel caso non ci sia disponibilita' di cassa
     */

    private void aggiornaSaldiPerCompenso(it.cnr.jada.UserContext userContext, MissioneBulk missione, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status) throws ComponentException {
        Obbligazione_scadenzarioBulk aScadenzaNonAssociataCompenso;
        Obbligazione_scadenzarioBulk aScadenzaAssociataCompenso = missione.getCompenso().getObbligazioneScadenzario();

        if (missione.getCompenso().getDocumentiContabiliCancellati() != null &&
                !missione.getCompenso().getDocumentiContabiliCancellati().isEmpty()) {
            for (Iterator i = missione.getCompenso().getDocumentiContabiliCancellati().iterator(); i.hasNext(); ) {
                aScadenzaNonAssociataCompenso = (Obbligazione_scadenzarioBulk) i.next();
                if (aScadenzaAssociataCompenso == null || !aScadenzaNonAssociataCompenso.getObbligazione().equalsByPrimaryKey(aScadenzaAssociataCompenso.getObbligazione())) {
                    //	Aggiorno i saldi per le scadenze di obbligazione elaborate dal compenso
                    //	ma non piu' associate
                    aggiornaSaldi(userContext, missione, aScadenzaNonAssociataCompenso.getObbligazione(), status);
                }
            }
        }

        //	Aggiorno i saldi per la scadenza di obbligazione attualmente associata al compenso
        if (aScadenzaAssociataCompenso != null)
            aggiornaSaldi(userContext, missione, aScadenzaAssociataCompenso.getObbligazione(), status);
    }

    /**
     * Cancellazione fisica del compenso
     * <p>
     * Pre-post-conditions:
     * <p>
     * Cancella fisicamente/logicamente Compenso
     * Pre:  Cancellazione fisica o logica del compenso validata ed effettuata dalla procedura
     * Post: Il sistema cancella fisicamente il compenso e inizializza alcuni
     * campi della missione (stato_coge, stato_coan, fl_associato_compenso)
     * <p>
     * Condizione di errore
     * Pre:  Cancellazione fisica o logica del compenso NON validata dalla procedura
     * Post: Il sistema non procede con la cancellazione
     *
     * @return la MissioneBulk il cui compenso e' stato cancellato
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    missione    la MissioneBulk per cui cancellare il compenso
     */

    public MissioneBulk cancellaCompensoPhisically(it.cnr.jada.UserContext userContext, MissioneBulk missione) throws ComponentException {
        try {

            //	Chiamo la procedura che cancella logicamente o fisicamente
            //	il compenso
            getTipoCancellazione(userContext, missione, 0);

            //	Se esiste, scollego la scadenza dal compenso.
            // 	Ricorda che tale scadenza puo' non esistere se la missione ha associato
            //  un anticipo di importo maggiore
            Obbligazione_scadenzarioBulk scadenza = missione.getCompenso().getObbligazioneScadenzario();
            if (scadenza != null) {
                scadenza.setIm_associato_doc_amm(new BigDecimal(0));
                updateImportoAssociatoDocAmm(userContext, scadenza);
            }
            //	Inizializzo i campi della missione relativi al compenso
            missione.setCompenso(new CompensoBulk());
            missione.setFl_associato_compenso(new Boolean(false));
            missione.setStato_coge(MissioneBulk.STATO_INIZIALE_COGE);
            missione.setStato_coan(MissioneBulk.STATO_INIZIALE_COAN);

            return missione;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Cancellazione fisica Dettagli Diaria
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Cancella fisicamente Diaria
     * Pre:  L'utente ha fatto delle modifiche che comportano la cancellazione fisica
     * dei dettagli did iaria
     * Post: Il sistema richiede alla Component di cancellare i dettagli di diaria
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    missione    la MissioneBulk per cui cancellare i dettagli di diaria
     */

    public MissioneBulk cancellaDiariaPhisically(it.cnr.jada.UserContext userContext, MissioneBulk missione) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        try {
            LoggableStatement ps = new LoggableStatement(getConnection(userContext),
                    "DELETE FROM " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "Missione_dettaglio " +
                            " where pg_missione = ?" +
                            " and  esercizio = ?" +
                            " and  cd_cds = ?" +
                            " and  cd_unita_organizzativa = ?" +
                            " and  ti_spesa_diaria = 'D'", true, this.getClass());

            ps.setObject(1, missione.getPg_missione());
            ps.setObject(2, missione.getEsercizio());
            ps.setString(3, missione.getCd_cds());
            ps.setString(4, missione.getCd_unita_organizzativa());

            ps.executeQuery();

            return missione;
        } catch (java.sql.SQLException e) {
            throw new it.cnr.jada.persistency.PersistencyException(e);
        }
    }

    public MissioneBulk cancellaRimborsoPhisically(it.cnr.jada.UserContext userContext, MissioneBulk missione) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        try {
            LoggableStatement ps = new LoggableStatement(getConnection(userContext),
                    "DELETE FROM " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "Missione_dettaglio " +
                            " where pg_missione = ?" +
                            " and  esercizio = ?" +
                            " and  cd_cds = ?" +
                            " and  cd_unita_organizzativa = ?" +
                            " and  ti_spesa_diaria = 'R'", true, this.getClass());

            ps.setObject(1, missione.getPg_missione());
            ps.setObject(2, missione.getEsercizio());
            ps.setString(3, missione.getCd_cds());
            ps.setString(4, missione.getCd_unita_organizzativa());

            ps.executeQuery();

            return missione;
        } catch (java.sql.SQLException e) {
            throw new it.cnr.jada.persistency.PersistencyException(e);
        }
    }

    /**
     * Cancellazione fisica Tappe
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Cancella fisicamente Tappe
     * Pre:  L'utente ha modificato la configurazione delle tappe della missione
     * Post: Il sistema richiede alla Component di cancellare le tappe della missione
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    missione    la MissioneBulk per cui cancellare le tappe
     */

    public MissioneBulk cancellaTappePhisically(it.cnr.jada.UserContext userContext, MissioneBulk missione) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        try {
            LoggableStatement ps = new LoggableStatement(getConnection(userContext),
                    "DELETE FROM " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "Missione_tappa " +
                            " where pg_missione = ?" +
                            " and  esercizio = ?" +
                            " and  cd_cds = ?" +
                            " and  cd_unita_organizzativa = ?", true, this.getClass());

            ps.setObject(1, missione.getPg_missione());
            ps.setObject(2, missione.getEsercizio());
            ps.setString(3, missione.getCd_cds());
            ps.setString(4, missione.getCd_unita_organizzativa());

            ps.executeQuery();

            return missione;
        } catch (java.sql.SQLException e) {
            throw new it.cnr.jada.persistency.PersistencyException(e);
        }
    }

    /**
     * Lettura da Tabella Missione_dettaglioBulk dei dettagli della missione in modifica
     * PreCondition:
     * Dettagli trovati
     * PostCondition:
     * Inizializza ogni dettaglio letto da tabella con i relativi attributi esterni e non
     **/

    private void caricaDettagliMissione(UserContext aUC, MissioneBulk missione) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.comp.ApplicationException {
        Missione_dettaglioHome dettaglioHome = (Missione_dettaglioHome) getHome(aUC, Missione_dettaglioBulk.class);
        SQLBuilder sql = dettaglioHome.createSQLBuilder();

        sql.addSQLClause("AND", "cd_cds", sql.EQUALS, missione.getCd_cds());
        sql.addSQLClause("AND", "cd_unita_organizzativa", sql.EQUALS, missione.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "esercizio", sql.EQUALS, missione.getEsercizio());
        sql.addSQLClause("AND", "pg_missione", sql.EQUALS, missione.getPg_missione());

        it.cnr.jada.bulk.BulkList dettagli = new it.cnr.jada.bulk.BulkList(dettaglioHome.fetchAll(sql));

        Missione_dettaglioBulk aDettaglio = null;

        for (Iterator i = dettagli.iterator(); i.hasNext(); ) {
            aDettaglio = (Missione_dettaglioBulk) i.next();
            aDettaglio.setMissione(missione);

            if (aDettaglio.getTi_spesa_diaria().equals(Missione_dettaglioBulk.TIPO_DIARIA))
                missione.getDiariaMissioneColl().add(aDettaglio);
            if (aDettaglio.getTi_spesa_diaria().equals(Missione_dettaglioBulk.TIPO_RIMBORSO))
                missione.getRimborsoMissioneColl().add(aDettaglio);
            if (aDettaglio.getTi_spesa_diaria().equals(Missione_dettaglioBulk.TIPO_SPESA)) {
                // Carico gli attributi esterni
                aDettaglio.setTipo_spesa(findTipo_spesa(aUC, aDettaglio));
                if (aDettaglio.isPasto())
                    aDettaglio.setTipo_pasto(findTipo_pasto(aUC, aDettaglio));
                if (aDettaglio.isRimborsoKm())
                    aDettaglio.setTipo_auto(findTipo_auto(aUC, aDettaglio));
                else
                    aDettaglio.setTipo_auto(null);
                missione.getSpeseMissioneColl().add(aDettaglio);
                aDettaglio.setStatus(Missione_dettaglioBulk.STATUS_CONFIRMED);
            }
        }

        missione.setSpeseInserite(true);
        return;
    }

    /**
     * Inizializzazione rimborso
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: inizializzazione
     * Pre:  Il sistema carica in modifica una missione con anticipo
     * Post: Il sistema restituisce l'anticipo con l'eventuale rimborso inizializzato
     *
     * @return l'AnticipoBulk inizializzato con l'eventuale rimborso
     * @param    aUC        lo UserContext che ha generato la richiesta
     * @param    bulk l' AnticipoBulk per cui ricercare un eventuale rimborso
     */

    private AnticipoBulk caricaRimborsoAnticipo(UserContext userContext, AnticipoBulk anticipo) throws ComponentException {
        try {
            SQLBuilder sql = getHome(userContext, RimborsoBulk.class).createSQLBuilder();
            sql.addClause("AND", "esercizio_anticipo", sql.EQUALS, anticipo.getEsercizio());
            sql.addClause("AND", "cd_cds_anticipo", sql.EQUALS, anticipo.getCd_cds());
            sql.addClause("AND", "cd_uo_anticipo", sql.EQUALS, anticipo.getCd_unita_organizzativa());
            sql.addClause("AND", "pg_anticipo", sql.EQUALS, anticipo.getPg_anticipo());
            List result = getHome(userContext, RimborsoBulk.class).fetchAll(sql);
            if (result.size() > 1)
                throw new it.cnr.jada.comp.ApplicationException("Attenzione esiste piu' di un rimborso associato all'anticipo");
            if (result.size() == 1)
                anticipo.setRimborso((RimborsoBulk) result.get(0));
            return anticipo;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Lettura da Tabella Missione_tappaBulk delle tappe della missione in modifica
     * PreCondition:
     * Tappe trovate
     * PostCondition:
     * Riordina le tappe lette da tabella per dataInizioTappa, inizializza gli opportuni attributi delle tappe lette
     * e crea una HashTable contenente le tappe di ogni giorno.
     * <p>
     * Condizioni di errore
     * PreCondition:
     * Nessuna tappa trovata
     * PostCondition:
     * Emette errore con messaggio:"Tappe della missione non disponibili !".
     **/

    private void caricaTappeMissione(UserContext aUC, MissioneBulk missione) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.comp.ApplicationException {
        Missione_tappaHome tappaHome = (Missione_tappaHome) getHome(aUC, Missione_tappaBulk.class);
        SQLBuilder sql = tappaHome.createSQLBuilder();

        sql.addSQLClause("AND", "cd_cds", sql.EQUALS, missione.getCd_cds());
        sql.addSQLClause("AND", "cd_unita_organizzativa", sql.EQUALS, missione.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "esercizio", sql.EQUALS, missione.getEsercizio());
        sql.addSQLClause("AND", "pg_missione", sql.EQUALS, missione.getPg_missione());

        missione.setTappeMissioneColl(new it.cnr.jada.bulk.BulkList(tappaHome.fetchAll(sql)));
        if (missione.getTappeMissioneColl().isEmpty())
            throw new it.cnr.jada.comp.ApplicationException("Tappe della missione non disponibili !");

        missione.ordinaTappePerDataInizioTappa();
        missione.setTappeConfigurate(true);
        for (Iterator i = missione.getTappeMissioneColl().iterator(); i.hasNext(); ) {
            Missione_tappaBulk aTappa = (Missione_tappaBulk) i.next();
            aTappa.setMissione(missione);
            if (aTappa.getFl_comune_altro().booleanValue())
                aTappa.setComune(aTappa.COMUNE_ALTRO);
            if (aTappa.getFl_comune_proprio().booleanValue())
                aTappa.setComune(aTappa.COMUNE_PROPRIO);
            if (aTappa.getFl_comune_estero().booleanValue())
                aTappa.setComune(aTappa.COMUNE_ESTERO);
            aTappa.setStatus(Missione_tappaBulk.STATUS_CONFIRMED);
            missione.putInHashtableTappe(aTappa);
        }

        return;
    }

    /**
     * Lettura del terzo della missione e inizializzazione dei relativi attributi :
     * la collection di modalita e termini di pagamento e la collection dei tipi rapporto
     */

    public MissioneBulk caricaTerzoInModificaMissione(UserContext userContext, MissioneBulk missione) throws ComponentException {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.sql.Timestamp tsDtValidita = new java.sql.Timestamp(sdf.parse(sdf.format(missione.getDt_inizio_missione())).getTime());

            V_terzo_per_compensoHome home = (V_terzo_per_compensoHome) getHome(userContext, V_terzo_per_compensoBulk.class, "DISTINCT_TERZO");
            V_terzo_per_compensoBulk terzo = home.loadVTerzo(userContext, missione.getTi_anagrafico(), missione.getCd_terzo(), tsDtValidita, tsDtValidita);

            if (terzo == null || terzo.getCd_terzo() == null)
                throw new it.cnr.jada.comp.ApplicationException("Attenzione, il terzo associato alla missione non esiste !");

            missione.setV_terzo(terzo);
            missione.setTermini(findTermini(userContext, missione));
            missione.setModalita(findModalita(userContext, missione));
            missione.setTipi_rapporto(findTipi_rapporto(userContext, missione));
            return missione;
        } catch (Throwable e) {
            throw handleException(e);
        }

    }

    /**
     * Valorizzazione l'attributo esterno "tipo_trattamento" della missione in modifica
     * PreCondition:
     * La collezione degli oggetti Tipo Trattamento eleggibili alla missione e' vuota
     * PostCondition:
     * Esce dal metodo
     * <p>
     * PreCondition:
     * Trovata la corrispondenza tra il codice trattamento della missione e il codice trattamento
     * di uno degli oggetti Tipo Trattamento eleggibili
     * PostCondition:
     * Inizializzo l'attributo esterno "tipo_trattamento" della missione
     * <p>
     * PreCondition:
     * Non trovata la corrispondenza tra il codice trattamento della missione e il codice trattamento
     * di uno degli oggetti Tipo Trattamento eleggibili
     * PostCondition:
     * Esce dal metodo
     **/

    private void caricaTipoTrattamento(MissioneBulk missione) {
        if ((missione.getCd_trattamento() == null) ||
                (missione.getTipi_trattamento() == null) || (missione.getTipi_trattamento().isEmpty()))
            return;

        Tipo_trattamentoBulk aTrattamento = null;

        for (Iterator i = missione.getTipi_trattamento().iterator(); i.hasNext(); ) {
            aTrattamento = (Tipo_trattamentoBulk) i.next();
            if ((aTrattamento.getCd_trattamento() != null) && (aTrattamento.getCd_trattamento().equals(missione.getCd_trattamento()))) {
                missione.setTipo_trattamento(aTrattamento);
                break;
            }
        }
        return;
    }

    /**
     * Tutti i controlli  superati.
     * PreCondition:
     * Ricerca la lista delle scadenze di obbligazioni congruenti con la missione che si sta creando/modificando.
     * PostCondition:
     * Le scadenze vengono aggiunte alla lista delle scadenze congruenti.
     * Validazione lista delle obbligazioni per le missioni
     * PreCondition:
     * Si è verificato un errore nel caricamento delle scadenze delle obbligazioni.
     * PostCondition:
     * Viene inviato il messaggio corrispondente all'errore segnalato.
     * Obbligazione definitiva
     * PreCondition:
     * La scadenza non appartiene ad un'obbligazione definitiva
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Obbligazioni non cancellate
     * PreCondition:
     * La scadenza appartiene ad un'obbligazione cancellata
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Obbligazioni associate ad altri documenti amministrativi
     * PreCondition:
     * La scadenza appartiene ad un'obbligazione associata ad altri documenti amministrativi
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Obbligazioni della stessa UO
     * PreCondition:
     * La scadenza dell'obbligazione non appartiene alla stessa UO di generazione missione
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Abilitatazione filtro di selezione sul debitore dell'obbligazione
     * PreCondition:
     * La scadenza dell'obbligazione ha un debitore diverso da quello della missione
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Disabilitazione filtro di selezione sul debitore dell'obbligazione
     * PreCondition:
     * La scadenza dell'obbligazione ha un debitore diverso da quello della missione e non è di tipo "diversi"
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Abilitazione filtro di selezione sulla data di scadenza
     * PreCondition:
     * La scadenza dell'obbligazione ha una data scadenza precedente alla data di filtro
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Abilitazione filtro importo scadenza
     * PreCondition:
     * La scadenza dell'obbligazione ha un importo di scadenza inferiore a quella di filtro
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Abilitazione filtro sul progressivo dell'obbligazione
     * PreCondition:
     * La scadenza dell'obbligazione non ha progressivo specificato
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     */

    public RemoteIterator cercaObbligazioni(UserContext context, Filtro_ricerca_obbligazioniVBulk filtro) throws ComponentException {

        SQLBuilder sql = prepareQueryObbligazioni(context, filtro);

        return iterator(context, sql, Obbligazione_scadenzarioBulk.class, "default");
    }

    private SQLBuilder prepareQueryObbligazioni(UserContext context, Filtro_ricerca_obbligazioniVBulk filtro) throws ComponentException {
        Obbligazione_scadenzarioHome home = (Obbligazione_scadenzarioHome) getHome(context, Obbligazione_scadenzarioBulk.class);
        it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
        sql.setDistinctClause(true);

        sql.addTableToHeader("OBBLIGAZIONE");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.CD_CDS", "OBBLIGAZIONE.CD_CDS");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.ESERCIZIO", "OBBLIGAZIONE.ESERCIZIO");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE", "OBBLIGAZIONE.ESERCIZIO_ORIGINALE");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE", "OBBLIGAZIONE.PG_OBBLIGAZIONE");

        sql.addSQLClause("AND", "OBBLIGAZIONE.ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context));
        sql.addSQLClause("AND", "OBBLIGAZIONE.STATO_OBBLIGAZIONE", sql.EQUALS, "D");
        sql.addSQLClause("AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", sql.ISNULL, null);
        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_SCADENZA", sql.NOT_EQUALS, new java.math.BigDecimal(0));
        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_AMM = ? OR OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_AMM IS NULL");
        sql.addParameter(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP), java.sql.Types.DECIMAL, 2);
        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_CONTABILE = ? OR OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_CONTABILE IS NULL");
        sql.addParameter(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP), java.sql.Types.DECIMAL, 2);
        sql.addSQLClause("AND", "OBBLIGAZIONE.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, filtro.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "OBBLIGAZIONE.RIPORTATO", sql.EQUALS, "N");

        if (filtro.getElemento_voce() != null) {
            sql.addSQLClause("AND", "OBBLIGAZIONE.CD_ELEMENTO_VOCE", sql.STARTSWITH, filtro.getElemento_voce().getCd_elemento_voce());
            sql.addSQLClause("AND", "OBBLIGAZIONE.TI_APPARTENENZA", sql.EQUALS, filtro.getElemento_voce().getTi_appartenenza());
            sql.addSQLClause("AND", "OBBLIGAZIONE.TI_GESTIONE", sql.EQUALS, filtro.getElemento_voce().getTi_gestione());
            sql.addSQLClause("AND", "OBBLIGAZIONE.ESERCIZIO", sql.EQUALS, filtro.getElemento_voce().getEsercizio());
        }

        if (!filtro.getFl_fornitore().booleanValue()) {
            sql.addTableToHeader("TERZO");
            sql.addTableToHeader("ANAGRAFICO");
            sql.addSQLJoin("OBBLIGAZIONE.CD_TERZO", "TERZO.CD_TERZO");
            sql.addSQLJoin("TERZO.CD_ANAG", "ANAGRAFICO.CD_ANAG");
            sql.addSQLClause("AND", "(OBBLIGAZIONE.CD_TERZO = ? OR ANAGRAFICO.TI_ENTITA = ?)");
            sql.addParameter(filtro.getFornitore().getCd_terzo(), java.sql.Types.INTEGER, 0);
            sql.addParameter(AnagraficoBulk.DIVERSI, java.sql.Types.VARCHAR, 0);
        } else {
            sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TERZO", sql.EQUALS, filtro.getFornitore().getCd_terzo());
        }

        if (filtro.getFl_data_scadenziario().booleanValue() && filtro.getData_scadenziario() != null)
            sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.DT_SCADENZA", sql.EQUALS, filtro.getData_scadenziario());
        if (filtro.getFl_importo().booleanValue() && filtro.getIm_importo() != null)
            sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_SCADENZA", sql.GREATER_EQUALS, filtro.getIm_importo());

        //filtro su Tipo obbligazione
        if (filtro.getFl_nr_obbligazione().booleanValue() && filtro.getTipo_obbligazione() != null) {
            if (ObbligazioneBulk.TIPO_COMPETENZA.equals(filtro.getTipo_obbligazione()))
                sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT", sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB);
            else if (ObbligazioneBulk.TIPO_RESIDUO_PROPRIO.equals(filtro.getTipo_obbligazione()))
                sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT", sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB_RES);
            else if (ObbligazioneBulk.TIPO_RESIDUO_IMPROPRIO.equals(filtro.getTipo_obbligazione()))
                sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT", sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA);
        }

        //filtro su Anno Residuo obbligazione
        if (filtro.getFl_nr_obbligazione().booleanValue() && filtro.getEsercizio_ori_obbligazione() != null)
            sql.addSQLClause("AND", "OBBLIGAZIONE.ESERCIZIO_ORIGINALE", sql.EQUALS, filtro.getEsercizio_ori_obbligazione());

        //filtro su Numero obbligazione
        if (filtro.getFl_nr_obbligazione().booleanValue() && filtro.getNr_obbligazione() != null)
            sql.addSQLClause("AND", "OBBLIGAZIONE.PG_OBBLIGAZIONE", sql.EQUALS, filtro.getNr_obbligazione());

        return sql;
    }

    private boolean checkEleggibilitaAnticipo(UserContext aUC, MissioneBulk missione) throws ComponentException {
        AnticipoHome anticipoHome = (AnticipoHome) getHome(aUC, AnticipoBulk.class);
        AnticipoBulk aAnticipo = null;

        try {
            SQLBuilder sql = selectAnticipoByClause(aUC, missione, new AnticipoBulk(), null);

            sql.addClause("and", "pg_anticipo", sql.EQUALS, missione.getAnticipo().getPg_anticipo());
            sql.addClause("and", "cd_cds", sql.EQUALS, missione.getAnticipo().getCd_cds());
            sql.addClause("and", "cd_unita_organizzativa", sql.EQUALS, missione.getAnticipo().getCd_unita_organizzativa());
            sql.addClause("and", "esercizio", sql.EQUALS, missione.getAnticipo().getEsercizio());

            SQLBroker broker = anticipoHome.createBroker(sql);
            if (broker.next())
                aAnticipo = (AnticipoBulk) anticipoHome.fetch(broker);

            broker.close();

            if (aAnticipo == null)
                return false;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(missione, e);
        }
        return true;
    }

    /**
     * Completa i dati relativi al terzo
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Completa terzo
     * Pre:  L'utente ha selezionato un nuovo terzo per la missione
     * Post: Il sistema ha valorizzato tutti i dati relativi all'anagrafico associato al terzo selezionato, in particolare
     * codice, nome, cognome, ragione sociale, codice fiscale, partita iva,
     * modalita, termini di pagamento e tipi rapporto
     *
     * @return la MissioneBulk coi dati relativi al terzo inizializzati
     * @param    uc            lo UserContext che ha generato la richiesta
     * @param    missione la MissioneBulk per cui e' stato selezionato un nuovo terzo
     * @param    aTerzo        il terzo di tipo V_terzo_per_compensoBulk selezioanto dall'utente
     */

    public MissioneBulk completaTerzo(UserContext uc, MissioneBulk missione, V_terzo_per_compensoBulk aTerzo) throws ComponentException {
        if (missione != null) {
            missione.setV_terzo(aTerzo);
            missione.setCd_terzo(aTerzo.getCd_terzo());
            missione.setNome(aTerzo.getNome());
            missione.setCognome(aTerzo.getCognome());
            missione.setRagione_sociale(aTerzo.getRagione_sociale());
            missione.setCodice_fiscale(aTerzo.getCodice_fiscale());
            missione.setPartita_iva(aTerzo.getPartita_iva());
            missione.setTermini(findTermini(uc, missione));
            missione.setModalita(findModalita(uc, missione));
            missione.setTipi_rapporto(findTipi_rapporto(uc, missione));
        }

        return missione;
    }

    /**
     * Creazione missione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: creazione temporanea di missione
     * Pre:  L'utente ha selezionato fine inserimento spese
     * Post: Il sistema salva la missione temporaneamente
     * <p>
     * Nome: creazione di missione
     * Pre:  L'utente ha selezionato il bottone di salvataggio della missione
     * Post: Aggiornamento dell'anticipo associato alla missione,
     * aggiornamento dell'obbligazione associata alla missione ( metodo 'aggiornaObbligazione')
     * aggiornamento della missione con progressivo definitivo e stato cofi = contabilizzato
     *
     * @return la MissioneBulk creata
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk    la MissioneBulk da creare
     */

    public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        return creaConBulk(userContext, bulk, null);
    }

    /**
     * Creazione missione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: creazione temporanea di missione
     * Pre:  L'utente ha selezionato fine inserimento spese
     * Post: Il sistema salva la missione temporaneamente
     * <p>
     * Nome: creazione di missione
     * Pre:  Validazione della scadenza di obbligazione associata alla missione
     * Post: Il sistema prosegue con il salvataggio della missione
     * <p>
     * Nome: creazione di missione
     * Pre:  L'utente ha selezionato il bottone di salvataggio della missione
     * Post: Aggiornamento dell'anticipo associato alla missione,
     * aggiornamento dell'obbligazione associata alla missione ( metodo 'aggiornaObbligazione')
     * aggiornamento della missione con progressivo definitivo e stato cofi = contabilizzato
     *
     * @return la MissioneBulk creata
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk    la MissioneBulk da creare
     * @param    status        serve per gestire l'eccezione lanciata dall'obbligazione
     * nel caso non ci sia disponibilita' di cassa
     */

    public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status) throws ComponentException {
        MissioneBulk missioneTemp = (MissioneBulk) bulk;

        if (missioneTemp == null)
            return null;

        try {
            // Salvo temporaneamente l'hash map dei saldi
            PrimaryKeyHashMap aTempDiffSaldi = new PrimaryKeyHashMap();
            if (missioneTemp.getDefferredSaldi() != null)
                aTempDiffSaldi = (PrimaryKeyHashMap) missioneTemp.getDefferredSaldi().clone();

            impostaDatiRimborsoDaCompletare(missioneTemp);
            controlloDateTappeConDateMissioni(missioneTemp);

            if (missioneTemp.getPg_missione() == null) {
                // Salvo missione, le spese, le tappe in modo temporaneo
                missioneTemp = (MissioneBulk) super.creaConBulk(userContext, missioneTemp);
            } else {
                // Validazione dell'eventuale obbligazione collegata alla missione
                if (!missioneTemp.isSalvataggioTemporaneo() && missioneTemp.isMissioneDefinitiva() &&
                        missioneTemp.isObbligazioneObbligatoria()) {
                    if (!missioneTemp.isMissioneConObbligazione())
                        throw new it.cnr.jada.comp.ApplicationException("Associare una Obbligazione !");
                    validaObbligazione(userContext, missioneTemp.getObbligazione_scadenzario(), missioneTemp);
                }

                //	Verifico che l'anticipo associato alla missione sia ancora eleggibile
                //	(Qualche altro utente potrebbe avere ad esempio annullato il mandato)
                if (missioneTemp.isMissioneConAnticipo()) {
                    if (!checkEleggibilitaAnticipo(userContext, missioneTemp))
                        throw new ApplicationException("Anticipo non eleggibile alla missione");
                }

                if (missioneTemp.isMissioneConAnticipo())
                    aggiornaAnticipo(userContext, missioneTemp.getAnticipo(), new Boolean(true));

                aggiornaObbligazione(userContext, missioneTemp, status);
                missioneTemp.setStato_cofi(missioneTemp.STATO_CONTABILIZZATO_COFI);

                // Salvo missione/tappe/spese in modo definitivo
                // Cerco un progressivo definitivo per la missione
                Long pg = null;
                if (missioneTemp.isMissioneFromGemis() && missioneTemp.getPgMissioneFromGeMis() != null) {
                    pg = missioneTemp.getPgMissioneFromGeMis();
                } else {
                    ProgressiviAmmComponentSession progressiviSession = (ProgressiviAmmComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_ProgressiviAmmComponentSession", ProgressiviAmmComponentSession.class);
                    Numerazione_doc_ammBulk numerazione = new Numerazione_doc_ammBulk(missioneTemp);
                    pg = progressiviSession.getNextPG(userContext, numerazione);
                }

                // Inserisco la missione, le spese, le tappe in modo definitivo
                MissioneHome home = (MissioneHome) getHome(userContext, missioneTemp);

                // 	Aggiorno la missione temporanea perche' dal salvataggio temporaneo a
                //	quello definitivo potrei avere associato/cambiato scadenza, selezionato
                //	il fondo, modificato gli importi della diaria .....
                missioneTemp.setToBeUpdated();
                makeBulkPersistent(userContext, missioneTemp);

                // Rendo definitiva la missione
                home.confermaMissioneTemporanea(userContext, missioneTemp, pg);
            }

            // Restore dell'hash map dei saldi
            if (missioneTemp.getDefferredSaldi() != null)
                missioneTemp.getDefferredSaldi().putAll(aTempDiffSaldi);
            aggiornaCogeCoanDocAmm(userContext, missioneTemp);

            if (!verificaStatoEsercizio(userContext, new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(missioneTemp.getCd_cds(), ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio())))
                throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare un documento per un esercizio non aperto!");

            return missioneTemp;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    private void impostaDatiRimborsoDaCompletare(MissioneBulk missioneTemp) {
        if (missioneTemp.isMissioneProvvisoria() && missioneTemp.isMissioneFromGemis()) {
            missioneTemp.setDaRimborsoDaCompletare(true);
        } else {
            missioneTemp.setDaRimborsoDaCompletare(false);
        }
    }

    private void controlloTrovato(UserContext aUC,
                                  Obbligazione_scadenzarioBulk scadenza) throws ComponentException,
            ApplicationException {
        Elemento_voceHome evHome = (Elemento_voceHome) getHome(aUC, Elemento_voceBulk.class);
        SQLBuilder sql = evHome.createSQLBuilder();

        sql.addSQLClause("AND", "esercizio", SQLBuilder.EQUALS, scadenza.getObbligazione().getEsercizio());
        sql.addSQLClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, scadenza.getObbligazione().getTi_appartenenza());
        sql.addSQLClause("AND", "ti_gestione", SQLBuilder.EQUALS, scadenza.getObbligazione().getTi_gestione());
        sql.addSQLClause("AND", "cd_elemento_voce", SQLBuilder.EQUALS, scadenza.getObbligazione().getCd_elemento_voce());

        try {
            List voce = evHome.fetchAll(sql);
            if (!voce.isEmpty()) {
                Elemento_voceBulk elementoVoce = (Elemento_voceBulk) voce.get(0);
                if (elementoVoce.isVocePerTrovati()) {
                    throw new it.cnr.jada.comp.ApplicationException(
                            "Non è possibile selezionare per missioni obbligazioni su capitoli collegati a Brevetti/Trovati.");
                }
                if (!elementoVoce.getFl_missioni()) {
                    throw new it.cnr.jada.comp.ApplicationException(
                            "Non è possibile selezionare per missioni obbligazioni su capitoli non utilizzabili per le missioni.");
                }
            }

        } catch (PersistencyException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Cancellazione logica missione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: cancellazione logica missione
     * Pre:  L'utente ha selezionato elimina missione
     * Post: Il sistema cancella logicamnete la missione
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk        la MissioneBulk da cancellare
     */
    private void deleteLogically(UserContext userContext, MissioneBulk missione) throws ComponentException {
        try {
        	missione.setCrudStatus(OggettoBulk.TO_BE_UPDATED);
            // ************
            //	Se l'esercizio di scrivania e' diverso da quello solare
            //	inizializzo la data di cancellazione al 31/12/esercizio missione
            java.sql.Timestamp tsOdierno = ((MissioneHome) getHome(userContext, missione)).getServerDate();
            GregorianCalendar tsOdiernoGregorian = (GregorianCalendar) missione.getGregorianCalendar(tsOdierno);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

            if (tsOdiernoGregorian.get(GregorianCalendar.YEAR) != it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue())
                missione.setDt_cancellazione(new java.sql.Timestamp(sdf.parse("31/12/" + missione.getEsercizio().intValue()).getTime()));
            else {
                String currentDate = Integer.toString(tsOdiernoGregorian.get(GregorianCalendar.DAY_OF_MONTH)) + "/" +
                        Integer.toString(tsOdiernoGregorian.get(GregorianCalendar.MONTH)) + "/" +
                        Integer.toString(tsOdiernoGregorian.get(GregorianCalendar.YEAR));
                missione.setDt_cancellazione(new java.sql.Timestamp(sdf.parse(currentDate).getTime()));
            }
            // ************

            missione.setStato_cofi(MissioneBulk.STATO_ANNULLATO);

            //	Nel caso di cancellazione logica della missione + compenso
            //	reinizializzo i campi della missione nella tabella del compenso
            //	e quindi metto sempre e comunque la flag associata a compenso = N.
            missione.setFl_associato_compenso(new Boolean(false));

            if (missione.getStato_coan().compareTo(MissioneBulk.STATO_CONTABILIZZATO_COAN) == 0)
                missione.setStato_coan(MissioneBulk.STATO_RICONTABILIZZARE_COAN);
            if (missione.getStato_coge().compareTo(MissioneBulk.STATO_CONTABILIZZATO_COGE) == 0)
                missione.setStato_coge(MissioneBulk.STATO_RICONTABILIZZARE_COGE);

            updateBulk(userContext, missione);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Cancellazione missione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: cancellazione missione
     * Pre:  Validazione cancellazione superata
     * Post: Il sistema procede con :
     * - una cancellazione logica se la procedura ritorna il valore 1
     * - una cancellazione fisica se la procedura ritorna il valore 2
     * e scollega l'eventuale scadenza associata alla missione o al compenso
     * <p>
     * Pre:  Validazione cancellazione NON superata
     * Post: Il sistema non procede con la cancellazione della missione
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk        la MissioneBulk da cancellare
     */

    public void eliminaConBulk(UserContext aUC, OggettoBulk bulk) throws ComponentException {
        MissioneBulk missione = (MissioneBulk) bulk;

        try {

            // Controllo dello stato dell'es COEP prec. per compensi riportati Da ese. prec. (isRiportataInScrivania())
            validateEsercizioCOEP(aUC, missione);

            int rc = getTipoCancellazione(aUC, missione, 1);

            if (rc == 0)
                throw new it.cnr.jada.comp.ApplicationException("Impossibile eliminare la missione !");

            // Salvo temporaneamente l'hash map dei saldi
            PrimaryKeyHashMap aTempDiffSaldi = new PrimaryKeyHashMap();
            if (missione.getDefferredSaldi() != null)
                aTempDiffSaldi = (PrimaryKeyHashMap) missione.getDefferredSaldi().clone();

            Obbligazione_scadenzarioBulk scadenza = null;

            //	Scollego l'eventuale scadenza dalla missione o del compenso
            if (missione.isMissioneConObbligazione())
                scadenza = missione.getObbligazione_scadenzario();
            if (missione.isMissioneConCompenso())
                scadenza = missione.getCompenso().getObbligazioneScadenzario();
            if (scadenza != null) {
                scadenza.setIm_associato_doc_amm(new BigDecimal(0));
                updateImportoAssociatoDocAmm(aUC, scadenza);
            }
            if (rc == missione.CANCELLAZIONE_FISICA){
                logger.info("Cancellazione Fisica Missione "+missione.getPg_missione());
                super.eliminaConBulk(aUC, missione);
            } else if (rc == missione.CANCELLAZIONE_LOGICA){
                logger.info("Cancellazione Logica Missione "+missione.getPg_missione());
                deleteLogically(aUC, missione);
            }

            // Restore dell'hash map dei saldi
            if (missione.getDefferredSaldi() != null)
                missione.getDefferredSaldi().putAll(aTempDiffSaldi);

            aggiornaCogeCoanDocAmm(aUC, missione);

            if (!verificaStatoEsercizio(aUC, new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(missione.getCd_cds(), ((it.cnr.contab.utenze00.bp.CNRUserContext) aUC).getEsercizio())))
                throw new it.cnr.jada.comp.ApplicationException("Impossibile eliminare un documento per un esercizio non aperto!");
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Carica i dati relativi al cambio
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica cambio
     * Pre:  E' stata generata una richiesta di caricamento di un cambio associato ad una certa divisa e valido
     * in una certa data
     * Post: Il sistema restituisce il cambio valido per la divisa data
     *
     * @return il CambioBulk trovato oppure null se non esiste nessun cambio valido per quella divisa in quella data
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    divisa    la DivisaBulk per cui ricercare il cambio
     * @param    dataCambio    la data per cui il cambio deve essere valido
     */

    public CambioBulk findCambio(UserContext uc, DivisaBulk divisa, java.sql.Timestamp dataCambio) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        CambioHome cambioHome = (CambioHome) getHome(uc, CambioBulk.class);
        CambioBulk aCambio = null;
        if (divisa != null) {
            return findCambio(uc, divisa.getCd_divisa(), dataCambio);
        } else {
            aCambio = cambioHome.getCambio(divisa, dataCambio);
        }
        if (aCambio == null)
            return null;

        return aCambio;
    }

    public CambioBulk findCambio(UserContext uc, String divisa, java.sql.Timestamp dataCambio) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        CambioHome cambioHome = (CambioHome) getHome(uc, CambioBulk.class);
        CambioBulk aCambio = null;
        aCambio = cambioHome.getCambio(divisa, dataCambio);

        if (aCambio == null)
            return null;

        return aCambio;
    }

    /**
     * Carica i dati relativi alla divisa
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica divisa
     * Pre:  E' stata generata una richiesta di caricamento di una divisa dato il suo codice
     * Post: Il sistema restituisce la divisa
     *
     * @return la DivisaBulk oppure null se non esiste nessuna divisa per il codice specificato
     * @param    uc    lo UserContext che ha generato la richiesta
     */

    private DivisaBulk findDivisa(UserContext userContext, String cdDivisa) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        if (cdDivisa == null)
            return new DivisaBulk();

        DivisaHome divisaHome = (DivisaHome) getHome(userContext, DivisaBulk.class);
        DivisaBulk aDivisa = new DivisaBulk();

        SQLBuilder sql = divisaHome.createSQLBuilder();
        sql.addSQLClause("AND", "CD_DIVISA", sql.EQUALS, cdDivisa);
        sql.addSQLClause("AND", "DT_CANCELLAZIONE", sql.ISNULL, null);

        SQLBroker broker = divisaHome.createBroker(sql);
        if (broker.next())
            aDivisa = (DivisaBulk) divisaHome.fetch(broker);

        broker.close();

        return aDivisa;
    }

    /**
     * Carica i dati relativi agli Inquadramenti
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica inquadramenti
     * Pre:  E' stata generata una richiesta di caricamento degli inquadramenti disponibili per il terzo specificato per una
     * missione
     * Post: Il sistema restituisce l'elenco degli inquadramenti validi relativi al terzo della missione, con tipo
     * rapporto uguale a quello della missione e data inizio missione inclusa nell'intervallo di validità
     * dell'inquadramento
     *
     * @return la collezione di Rif_inquadramentoBulk valida per la missione
     * @param    uc            lo UserContext che ha generato la richiesta
     * @param    missione la MissioneBulk per cui selezionare gli inquadramenti
     */

    public Collection findInquadramenti(UserContext aUC, MissioneBulk missione) throws ComponentException {
        try {
            if (missione.getV_terzo() == null || missione.getV_terzo().getCd_terzo() == null ||
                    missione.getCd_tipo_rapporto() == null || missione.getDt_inizio_missione() == null)
                return null;

            Rif_inquadramentoHome rifHome = (Rif_inquadramentoHome) getHome(aUC, Rif_inquadramentoBulk.class);

            java.sql.Timestamp aData = it.cnr.jada.util.DateUtils.truncate(missione.getDt_inizio_missione());
            SQLBuilder sql = rifHome.selectInquadramenti(null, missione.getTipo_rapporto(), missione.getV_terzo(), aData);

            return rifHome.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(missione, ex);
        }
    }

    /**
     * Carica i dati relativi agli Inquadramenti e ai Tipi di Trattamento
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica Inquadramenti e Tipi Trattamento
     * Pre:  E' stata generata una richiesta di caricamento degli inquadramenti disponibili per il terzo specificato per una
     * missione e dei relativi tipi di trattamento
     * Post: Il sistema restituisce la missione con gli inquadramenti (metodo findInquadramenti)
     * e i Tipi Trattamento ( metodo 'findTipi_trattamento') validi relativi al terzo della missione
     *
     * @return la MissioneBulk con gli inquadramenti e i tipi di trattamento inizializzati
     * @param    uc            lo UserContext che ha generato la richiesta
     * @param    missione la MissioneBulk per cui selezionare gli inquadramenti e i tipi di trattamento
     */

    public MissioneBulk findInquadramentiETipiTrattamento(UserContext aUC, MissioneBulk missione) throws ComponentException {
        missione.setInquadramenti(findInquadramenti(aUC, missione));
        missione.setTipi_trattamento(findTipi_trattamento(aUC, missione));

        return missione;
    }

    /**
     * Carica i dati relativi alle coordinate bancarie
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica banche
     * Pre:  E' stata generata una richiesta di caricamento delle coordinate bancarie relative al terzo della missione
     * Post: Il sistema restituisce la lista delle coordinate bancarie relative al terzo della missione
     *
     * @return la collezione di istanze di tipo BancaBulk
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    missione la MissioneBulk da cui ricavare il terzo per cui selezionare le coordinate bancarie
     */

    public java.util.Collection findListabanche(UserContext aUC, MissioneBulk missione) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        if (missione.getCd_terzo() == null || missione.getModalita_pagamento() == null)
            return null;

        return getHome(aUC, BancaBulk.class).fetchAll(selectBancaByClause(aUC, missione, null, null));
    }

    /**
     * Carica i dati relativi alle modalità di pagamento
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica modalita
     * Pre:  E' stata generata una richiesta di caricamento delle modalità di pagamento relative al terzo della missione
     * Post: Il sistema restituisce la lista delle modalità di pagamento relative al terzo della missione
     *
     * @return la collezione di istanze di tipo Rif_modalita_pagamentoBulk
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk la MissioneBulk da cui ricavare il terzo per cui selezionare le modalità di pagamento
     */

    public java.util.Collection findModalita(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            MissioneBulk missione = (MissioneBulk) bulk;
            if ((missione.getTerzo() == null))
                return null;
            TerzoHome terzoHome = (TerzoHome) getHome(userContext, TerzoBulk.class);

            return terzoHome.findRif_modalita_pagamento(missione.getTerzo());
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(bulk, ex);
        } catch (it.cnr.jada.persistency.IntrospectionException ex) {
            throw handleException(bulk, ex);
        }
    }

    /**
     * Carica i dati relativi alla Nazione Italia
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica nazione Italia
     * Pre:  E' stata generata una richiesta di caricamento dei dati relativi alla nazione di tipo Italia
     * Post: Il sistema restituisce la nazione di tipo Italia
     *
     * @return la NazioneBulk di tipo Italia
     * @param    uc    lo UserContext che ha generato la richiesta
     */

    private NazioneBulk findNazioneItalia(UserContext userContext) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        NazioneHome nazHome = (NazioneHome) getHome(userContext, NazioneBulk.class);
        NazioneBulk nazione = null;

        SQLBuilder sql = nazHome.createSQLBuilder();
        sql.addSQLClause("AND", "TI_NAZIONE", sql.EQUALS, NazioneBulk.ITALIA);
        sql.addOrderBy("PG_NAZIONE");

        SQLBroker broker = nazHome.createBroker(sql);
        if (broker.next())
            nazione = (NazioneBulk) nazHome.fetch(broker);

        broker.close();

        return nazione;
    }

    /**
     * Carica i dati relativi ai termini di pagamento
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica termini
     * Pre:  E' stata generata una richiesta di caricamento dei termini di pagamento relativi al terzo della missione
     * Post: Il sistema restituisce la lista dei termini di pagamento relativi al terzo della missione
     *
     * @return la collezione di istanze di tipo Rif_termini_pagamentoBulk
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    bulk la MissioneBulk da cui ricavare il terzo per cui selezionare i termini di pagamento
     */
    public java.util.Collection findTermini(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            MissioneBulk missione = (MissioneBulk) bulk;
            if ((missione.getTerzo() == null))
                return null;
            TerzoHome terzoHome = (TerzoHome) getHome(userContext, TerzoBulk.class);

            return terzoHome.findRif_termini_pagamento(missione.getTerzo());
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(bulk, ex);
        } catch (it.cnr.jada.persistency.IntrospectionException ex) {
            throw handleException(bulk, ex);
        }

    }

    /**
     * Carica i dati relativi ai tipi di missione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica tipi missione
     * Pre:  E' stata generata una richiesta di caricamento dei tipi di missione
     * Post: Il sistema restituisce la lista di tutti i tipi di missione
     *
     * @return la collezione di istanze di tipo Tipo_missioneBulk
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    bulk la MissioneBulk
     */

    public java.util.Collection findTipi_missione(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            Tipo_missioneHome tipoMissioneHome = (Tipo_missioneHome) getHome(userContext, Tipo_missioneBulk.class);

            SQLBuilder sql = tipoMissioneHome.createSQLBuilder();

            return tipoMissioneHome.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(bulk, ex);
        }

    }

    /**
     * Viene richiesta la lista dei Tipi di rapporto associati ad un Terzo e validi
     * in data inizio missione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome:	Terzo NON selezionato o data inizio missione non valorizzata
     * Pre: 	Non è stato selezionato un Terzo per la missione oppure non e'
     * stata inserita la data inizio missione
     * Post: 	Non vengono caricati i Tipi di rapporto
     * <p>
     * Nome: 	Terzo selezionato e data inizio missione valorizzata
     * Pre: 	E' stato selezionato un Terzo valido per la missione ed e' stata
     * inserita la data inizio missione
     * Post: 	Viene restituita la lista dei Tipi di rapporto associati al Terzo e
     * validi in data inizio missione
     *
     * @param    userContext        lo UserContext che ha generato la richiesta
     * @param    bulk l'OggettoBulk da completare
     * @return La lista dei Tipi di rapporto associati al terzo e validi in data inizio
     * missione
     **/
    public java.util.Collection findTipi_rapporto(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            MissioneBulk missione = (MissioneBulk) bulk;
            if (!missione.areCampiPerRicercaTipiRapportoValorizzati())
                return null;

            Tipo_rapportoHome rifHome = (Tipo_rapportoHome) getHome(userContext, Tipo_rapportoBulk.class);
            java.sql.Timestamp aData = it.cnr.jada.util.DateUtils.truncate(missione.getDt_inizio_missione());

            List listaTipiRapporto = rifHome.findTipiRapporto(missione.getV_terzo(), aData);

            return listaTipiRapporto;
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(bulk, ex);
        }
    }

    /**
     * Viene richiesta la lista dei Tipi di Trattamento legati
     * al Tipo di Rapporto selezionato
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: 	Tipo di Rapporto o Data registrazione NON valorizzati
     * Pre: 	Non sono stati valorizzati il tipo di rapporto o la data di registrazione
     * Post: 	Non vengono caricati i Tipi Trattamento
     * <p>
     * Nome: 	Tipo rapporto e Data registrazione valorizzati
     * Pre: 	E' stato selezionato un tipo di rapporto valido ed e' stata
     * valorizza la data di registrazione della missione
     * Post: 	Viene restituita la lista dei Tipi di Trattamento
     * legati al Tipo di rapporto selezionato, al tipo anagrafico della missione
     * e validita alla data di registrazione della missione
     *
     * @param    userContext        lo UserContext che ha generato la richiesta
     * @param    bulk l'OggettoBulk da completare
     * @return La lista dei Tipi di Trattamento associati al Tipo Rapporto e al tipo anagrafico
     * della missione e validi alla data di registrazione della missione
     **/

    public java.util.Collection findTipi_trattamento(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            MissioneBulk missione = (MissioneBulk) bulk;
            if (missione == null || !missione.areCampiPerRicercaTipiTrattamentoValorizzati())
                return null;

            Tipo_trattamentoHome trattamentoHome = (Tipo_trattamentoHome) getHome(userContext, Tipo_trattamentoBulk.class);
            Filtro_trattamentoBulk filtro = new Filtro_trattamentoBulk();
            filtro.setCdTipoRapporto(missione.getCd_tipo_rapporto());
            filtro.setTipoAnagrafico(missione.getTi_anagrafico());
            filtro.setDataValidita(missione.getDt_registrazione());
            filtro.setFlSenzaCalcoli(Boolean.FALSE);
            filtro.setFlDiaria(Boolean.TRUE);
            filtro.setFlDefaultCongualio(Boolean.FALSE);
            filtro.setTiIstituzionaleCommerciale(missione.getTi_istituz_commerc());
            if (filtro.getCdTipoRapporto() != null && filtro.getCdTipoRapporto().equals("DIP")) {
                try {
                    TerzoHome tHome = (TerzoHome) getHomeCache(userContext).getHome(TerzoBulk.class);
                    TerzoBulk tKey = new TerzoBulk(missione.getCd_terzo());
                    TerzoBulk t = (TerzoBulk) tHome.findByPrimaryKey(tKey);

                    RapportoHome rHome = (RapportoHome) getHomeCache(userContext).getHome(RapportoBulk.class);
                    java.util.Collection collRapp = rHome.findByCdAnagCdTipoRapporto(t.getCd_anag(), filtro.getCdTipoRapporto());
                    boolean exit = false;
                    for (java.util.Iterator i = collRapp.iterator(); i.hasNext() && !exit; ) {
                        RapportoBulk r = (RapportoBulk) i.next();
                        exit = true;
                        if (r.getCd_ente_prev_sti() == null)
                            //throw new it.cnr.jada.comp.ApplicationException("Non è stato possibile recuperare l''Ente Previdenziale del dipendente selezionato.");
                            //non blocco perchè potrebbero esserci trattamenti che non prevedono contributi previdenziali
                            //quindi passo il codice fittizio 'XX'
                            filtro.setEntePrev("XX");
                        else
                            filtro.setEntePrev(r.getCd_ente_prev_sti());
                        if (r.getCd_rapp_impiego_sti() == null)
                            throw new it.cnr.jada.comp.ApplicationException("Per il dipendente in esame non è definito un Rapporto di Impiego!");
                        else {
                            Ass_rapp_impiegoHome assHome = (Ass_rapp_impiegoHome) getHome(userContext, Ass_rapp_impiegoBulk.class);
                            Ass_rapp_impiegoBulk assKey = new Ass_rapp_impiegoBulk(r.getCd_rapp_impiego_sti());
                            Ass_rapp_impiegoBulk ass = (Ass_rapp_impiegoBulk) assHome.findByPrimaryKey(assKey);
                            filtro.setTipoRappImpiego(ass.getTipo_rapp_impiego());
                        }
                    }
                } catch (IntrospectionException e) {
                    throw handleException(e);
                }
                //solo per il rapporto DIP aggiungo il filtro "Anno prec" a seconda della data di inizio competenza
                GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
                data_da.setTime(missione.getDt_inizio_missione());
                if (data_da.get(GregorianCalendar.YEAR) == (missione.getEsercizio() - 1))
                    filtro.setFlAnnoPrec(new Boolean(true));
                else
                    filtro.setFlAnnoPrec(new Boolean(false));
            }
            if (missione.getDt_inizio_missione() != null && missione.getDt_fine_missione() != null) {
                GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
                GregorianCalendar data_a = (GregorianCalendar) GregorianCalendar.getInstance();

                data_da.setTime(missione.getDt_inizio_missione());
                data_a.setTime(missione.getDt_fine_missione());

                if (data_da.get(GregorianCalendar.YEAR) == data_a.get(GregorianCalendar.YEAR)) {

                    TerzoHome tHome = (TerzoHome) getHomeCache(userContext).getHome(TerzoBulk.class);
                    TerzoBulk tKey = new TerzoBulk(missione.getCd_terzo());
                    TerzoBulk t = (TerzoBulk) tHome.findByPrimaryKey(tKey);

                    AnagraficoHome aHome = (AnagraficoHome) getHomeCache(userContext).getHome(AnagraficoBulk.class);
                    AnagraficoBulk aKey = new AnagraficoBulk(t.getCd_anag());
                    AnagraficoBulk a = (AnagraficoBulk) aHome.findByPrimaryKey(aKey);

                    if (a.getFl_cervellone() &&
                            !(new Integer(data_da.get(GregorianCalendar.YEAR)).compareTo(a.getAnno_inizio_res_fis().intValue()) < 0) &&
                            !(new Integer(data_da.get(GregorianCalendar.YEAR)).compareTo(a.getAnno_fine_agevolazioni().intValue()) > 0)) {
                        filtro.setFlAgevolazioniCervelli(new Boolean(a.getFl_cervellone()));
                    } else
                        filtro.setFlAgevolazioniCervelli(new Boolean(false));
                } else
                    filtro.setFlAgevolazioniCervelli(new Boolean(false));
            }
            return trattamentoHome.findTipiTrattamento(filtro);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(bulk, ex);
        }
    }

    /**
     * Pre-post-conditions:
     * <p>
     * Nome: inizializza tipo auto
     * Pre:  L'utente ha richiesto l'inizializzzaione dei dettagli di spesa di una missione
     * La spesa e' di tipo rimborso km
     * Post: Il sistema carica il tipo auto del dettaglio della missione
     *
     * @return la Missione_rimborso_kmBulk trovata
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    aSpesa    il Missione_dettaglioBulk per cui cercare il tipo auto
     */
    private Missione_rimborso_kmBulk findTipo_auto(UserContext aUC, Missione_dettaglioBulk aSpesa) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        Missione_rimborso_kmHome tipoAutoHome = (Missione_rimborso_kmHome) getHome(aUC, Missione_rimborso_kmBulk.class);
        Missione_rimborso_kmBulk tipoAuto = null;

        SQLBuilder sql = selectTipo_autoByClause(aUC, aSpesa, new Missione_rimborso_kmBulk(), null);

        SQLBroker broker = tipoAutoHome.createBroker(sql);
        if (broker.next())
            tipoAuto = (Missione_rimborso_kmBulk) tipoAutoHome.fetch(broker);

        broker.close();

        return tipoAuto;
    }

    /**
     * Pre-post-conditions:
     * <p>
     * Nome: inizializza tipo spesa
     * Pre:  L'utente ha richiesto l'inizializzzaione dei dettagli di spesa di una missione
     * La spesa e' di tipo pasto
     * Post: Il sistema carica il tipo pasto del dettaglio della missione
     *
     * @return la Missione_tipo_pastoBulk trovata
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    aSpesa    il Missione_dettaglioBulk per cui cercare il tipo pasto
     */
    private Missione_tipo_pastoBulk findTipo_pasto(UserContext aUC, Missione_dettaglioBulk aSpesa) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        Missione_tipo_pastoHome tipoPastoHome = (Missione_tipo_pastoHome) getHome(aUC, Missione_tipo_pastoBulk.class);
        Missione_tipo_pastoBulk tipoPasto = null;

        SQLBuilder sql = selectTipo_pastoByClause(aUC, aSpesa, new Missione_tipo_pastoBulk(), null);

        SQLBroker broker = tipoPastoHome.createBroker(sql);
        if (broker.next())
            tipoPasto = (Missione_tipo_pastoBulk) tipoPastoHome.fetch(broker);

        broker.close();

        return tipoPasto;
    }

    /**
     * Pre-post-conditions:
     * <p>
     * Nome: inizializza tipo spesa
     * Pre:  L'utente ha richiesto l'inizializzzaione dei dettagli di spesa di una missione
     * Post: Il sistema carica il tipo spesa del dettaglio della missione
     *
     * @return la Missione_tipo_spesaBulk trovata
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    aSpesa    il Missione_dettaglioBulk per cui cercare il tipo spesa
     */
    private Missione_tipo_spesaBulk findTipo_spesa(UserContext aUC, Missione_dettaglioBulk aSpesa) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        Missione_tipo_spesaHome tipoSpesaHome = (Missione_tipo_spesaHome) getHome(aUC, Missione_tipo_spesaBulk.class);
        Missione_tipo_spesaBulk tipoSpesa = null;

        SQLBuilder sql = selectTipo_spesaByClause(aUC, aSpesa, new Missione_tipo_spesaBulk(), null);

        SQLBroker broker = tipoSpesaHome.createBroker(sql);
        if (broker.next())
            tipoSpesa = (Missione_tipo_spesaBulk) tipoSpesaHome.fetch(broker);

        broker.close();

        return tipoSpesa;
    }

    /**
     * Genera diaria
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Genera diaria
     * Pre:  L'utente ha richiesto la generazione della diaria per una missione
     * Post: Il sistema richiama la stored procedure che genera la diaria; il sistema carica i dettagli della diaria
     * (metodo 'ritornaDiariaGenerata')
     *
     * @return la MissioneBulk con la diaria generata
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    missione    la MissioneBulk per cui generare la diaria
     */

    public MissioneBulk generaDiaria(UserContext aUC, MissioneBulk missione) throws ComponentException {
        LoggableStatement cs = null;
        try {
            missione.calcolaConsuntivi();
            try {
                cs = new LoggableStatement(getConnection(aUC), "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                        + "CNRCTB505.elaboraMissioneDiaria(?,?,?,?)}", false, this.getClass());
                cs.setObject(1, missione.getCd_cds());
                cs.setObject(2, missione.getCd_unita_organizzativa());
                cs.setObject(3, missione.getEsercizio());
                cs.setObject(4, missione.getPg_missione());
                cs.executeQuery();
            } finally {
                cs.close();
            }
            missione = ritornaDiariaGenerata(aUC, missione);
            return missione;
        } catch (java.sql.SQLException e) {
            throw handleException(missione, e);
        }
    }

    public MissioneBulk generaRimborso(UserContext aUC, MissioneBulk missione) throws ComponentException {
        LoggableStatement cs = null;
        try {
            //missione.calcolaConsuntivi();
            try {
                if (missione.getCd_trattamento() != null) {
                    cs = new LoggableStatement(getConnection(aUC), "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                            + "CNRCTB505.elaboraMissioneRimborso(?,?,?,?,?)}", false, this.getClass());
                    cs.setObject(1, missione.getCd_cds());
                    cs.setObject(2, missione.getCd_unita_organizzativa());
                    cs.setObject(3, missione.getEsercizio());
                    cs.setObject(4, missione.getPg_missione());
                    cs.setObject(5, missione.getCd_trattamento());
                    cs.executeQuery();
                } else {
                    cs = new LoggableStatement(getConnection(aUC), "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                            + "CNRCTB505.elaboraMissioneRimborso(?,?,?,?)}", false, this.getClass());
                    cs.setObject(1, missione.getCd_cds());
                    cs.setObject(2, missione.getCd_unita_organizzativa());
                    cs.setObject(3, missione.getEsercizio());
                    cs.setObject(4, missione.getPg_missione());
                    cs.executeQuery();
                }
            } finally {
                cs.close();
            }
            missione = ritornaRimborsoGenerato(aUC, missione);
            return missione;
        } catch (java.sql.SQLException e) {
            throw handleException(missione, e);
        }
    }

    /**
     * Carica i dati relativi alla divisa di default
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica divisa
     * Pre:  E' stata generata una richiesta di caricamento della divisa di default
     * Post: Il sistema restituisce la divisa di default
     *
     * @return la DivisaBulk di default oppure null se non esiste nessuna divisa di default
     * @param    uc    lo UserContext che ha generato la richiesta
     */

    public DivisaBulk getDivisaDefault(UserContext aUC) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException, it.cnr.jada.persistency.PersistencyException {
        DivisaHome divisaHome = (DivisaHome) getHome(aUC, DivisaBulk.class);
        DivisaBulk divisaDefault = divisaHome.getDivisaDefault(aUC);

        return divisaDefault;
    }

    /**
     * Carica i dati relativi alla divisa
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica divisa
     * Pre:  E' stata generata una richiesta di caricamento di una divisa dato il suo codice
     * Post: Il sistema restituisce la divisa
     *
     * @return la DivisaBulk oppure null se non esiste nessuna divisa per il codice specificato
     * @param    uc    lo UserContext che ha generato la richiesta
     */

    private String getDivisaTappaDaDiaria(UserContext userContext, Long nazione, String gruppoInqudramento, Timestamp data) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        Missione_diariaHome diariaHome = (Missione_diariaHome) getHome(userContext, Missione_diariaBulk.class);
        Missione_diariaBulk aDiaria = new Missione_diariaBulk();

        SQLBuilder sql = diariaHome.createSQLBuilder();
        sql.addSQLClause("AND", "PG_NAZIONE", sql.EQUALS, nazione);
        sql.addSQLClause("AND", "CD_GRUPPO_INQUADRAMENTO", sql.EQUALS, gruppoInqudramento);
        sql.addSQLClause("AND", "DT_INIZIO_VALIDITA", sql.LESS_EQUALS, data);
        sql.addSQLClause("AND", "DT_FINE_VALIDITA", sql.GREATER_EQUALS, data);

        SQLBroker broker = diariaHome.createBroker(sql);
        if (broker.next())
            aDiaria = (Missione_diariaBulk) diariaHome.fetch(broker);
        broker.close();

        return aDiaria.getCd_divisa();
    }

    private it.cnr.jada.bulk.PrimaryKeyHashtable getDocumentiContabiliNonTemporanei(
            UserContext userContext,
            java.util.Enumeration scadenze) throws ComponentException {

        it.cnr.jada.bulk.PrimaryKeyHashtable documentiContabiliNonTemporanei = new it.cnr.jada.bulk.PrimaryKeyHashtable();
        if (scadenze != null)
            while (scadenze.hasMoreElements()) {
                IScadenzaDocumentoContabileBulk scadenza = (IScadenzaDocumentoContabileBulk) scadenze.nextElement();
                if (!scadenza.getFather().isTemporaneo()) {
                    if (!documentiContabiliNonTemporanei.containsKey(scadenza.getFather())) {
                        Vector allInstances = new java.util.Vector();
                        allInstances.addElement(scadenza.getFather());
                        documentiContabiliNonTemporanei.put(scadenza.getFather(), allInstances);
                    } else {
                        ((Vector) documentiContabiliNonTemporanei.get(scadenza.getFather())).add(scadenza.getFather());
                    }
                }
            }
        return documentiContabiliNonTemporanei;
    }

    private BigDecimal getMassimaleEuro(UserContext aUC, Missione_dettaglioBulk spesa, DivisaBulk divisaDefault, String cdDivisaMassimale, BigDecimal importoMassimale, boolean flgSpesa) throws ComponentException, javax.ejb.EJBException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.bulk.ValidationException {
        CambioBulk cambioMassimale = new CambioBulk();
        DivisaBulk divisaMassimale = null;
        BigDecimal importoMassimaleEuro = new BigDecimal(0);

        // Se il massimale non e' espresso in EURO lo converto
        if (!cdDivisaMassimale.equals(divisaDefault.getCd_divisa())) {
            // Recupero l'oggetto divisa del massimale
            divisaMassimale = findDivisa(aUC, cdDivisaMassimale);
            if (divisaMassimale == null) {
                if (flgSpesa)
                    throw new it.cnr.jada.bulk.ValidationException("Impossibile trovare la divisa di riferimento del massimale della spesa !");
                else
                    throw new it.cnr.jada.bulk.ValidationException("Impossibile trovare la divisa di riferimento del massimale del pasto !");
            }
            // Cerco il Cambio da usare
            Missione_tappaBulk tappa = (Missione_tappaBulk) spesa.getMissione().getTappeMissioneHash().get(spesa.getDt_inizio_tappa());
            if ((tappa != null) && (tappa.getCd_divisa_tappa() != null) && (tappa.getCd_divisa_tappa().equals(cdDivisaMassimale))) {
                // Propongo il cambio della tappa dello stesso giorno
                cambioMassimale.setCambio(tappa.getCambio_tappa());
            } else {
                // Cambio letto da db
                cambioMassimale = findCambio(aUC, divisaMassimale, spesa.getDt_inizio_tappa());
                if (cambioMassimale == null) {
                    if (flgSpesa)
                        throw new it.cnr.jada.bulk.ValidationException("Impossibile trovare il cambio per convertire il massimale della spesa !");
                    else
                        throw new it.cnr.jada.bulk.ValidationException("Impossibile trovare il cambio per convertire il massimale del pasto !");
                }
            }

            // Converto massimale in Euro
            importoMassimale = importoMassimale.setScale(2, BigDecimal.ROUND_HALF_UP);

            if (divisaMassimale.getFl_calcola_con_diviso().booleanValue())
                importoMassimaleEuro = importoMassimale.divide(cambioMassimale.getCambio(), BigDecimal.ROUND_HALF_UP);
            else
                importoMassimaleEuro = importoMassimale.multiply(cambioMassimale.getCambio());


        } else
            importoMassimaleEuro = importoMassimale;

        return importoMassimaleEuro;
    }

    private String getStatoRiporto(UserContext context, MissioneBulk missione) throws ComponentException {
        try {
            RiportoDocAmmComponentSession session = (RiportoDocAmmComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_RiportoDocAmmComponentSession", RiportoDocAmmComponentSession.class);
            return session.getStatoRiporto(context, missione);
        } catch (Throwable t) {
            throw handleException(missione, t);
        }
    }

    /**
     * Gennaro Borriello - (02/11/2004 15.04.39)
     * Aggiunta gestione dell Stato Riportato all'esercizio di scrivania.
     */
    private String getStatoRiportoInScrivania(UserContext context, MissioneBulk missione) throws ComponentException {
        try {
            RiportoDocAmmComponentSession session = (RiportoDocAmmComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_RiportoDocAmmComponentSession", RiportoDocAmmComponentSession.class);
            return session.getStatoRiportoInScrivania(context, missione);
        } catch (Throwable t) {
            throw handleException(missione, t);
        }
    }

    /**
     * Tipo di Cancellazione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Tipo di Cancellazione
     * Pre:  L'utente ha richiesto la cancellazione della missione
     * Post: Il sistema richiama la stored procedure che stabilisce se la cancellazione
     * della missione deve essere puo' avvenire e se deve essere logica o fisica.
     * La procedura cancella anche l'eventuale compenso collegato
     *
     * @return il tipo di cancellazione (NULL = non cancellabile; F = cancellazione
     * fisica; L = cancellazione logica)
     * @param    userContext            lo UserContext che ha generato la richiesta
     * @param    missione            la MissioneBulk da cancellare
     * @param    cancellaAnticipo    0 = non cancellare anticipo
     * 1 = cancella anticipo
     */

    private int getTipoCancellazione(UserContext aUC, OggettoBulk bulk, int cancellaAnticipo) throws ComponentException {
        MissioneBulk missione = (MissioneBulk) bulk;
        int rc = 0;
        LoggableStatement cs = null;

        try {
            try {
                cs = new LoggableStatement(getConnection(aUC), "{call 	" + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                        + "CNRCTB505.eseguiDelMissione(?,?,?,?,?)}", false, this.getClass());

                cs.setObject(1, missione.getCd_cds());
                cs.setObject(2, missione.getCd_unita_organizzativa());
                cs.setObject(3, missione.getEsercizio());
                cs.setObject(4, missione.getPg_missione());
                cs.setInt(5, cancellaAnticipo);

                cs.registerOutParameter(5, java.sql.Types.INTEGER);

                cs.executeQuery();
                rc = ((Integer) (cs.getObject(5))).intValue();

                //	Se la missione e' eleggibile alla cancellazione fisica
                //	ma l'obbligazione ad essa associata risulta essere stata
                //	riportata --> forzo la cancellazione logica della missione

                //Gennaro Borriello - (03/11/2004 19.04.48)
                // Fix sul controllo dello "Stato Riportato"
                if (rc == missione.CANCELLAZIONE_FISICA && missione.isRiportata() && missione.isRiportataInScrivania())
                    rc = missione.CANCELLAZIONE_LOGICA;
            } finally {
                cs.close();
            }
            return rc;
        } catch (java.sql.SQLException e) {
            throw handleException(missione, e);
        }
    }
/**
 * Gestione della validazione del terzo selezionato
 *
 * Pre-post-conditions:
 *
 * Pre:  Il sistema ha effettuato i controlli del terzo della missione
 * Post: Il sistema a seconda dell'errore che si e' verificato durante la validazione del terzo
 *		 manda una ComponentException
 *
 * @param    error    il codice dell'errore che si e' verificato in fase di validazione del terzo
 */

    /**
     * ComponentException :
     * <p>
     * errorCode		Significato
     * =========		===========
     * 0			Tutto bene
     * 1			Terzo assente
     * 2			Terzo non valido alla data registrazione
     * 3			Controllo se ho inserito le modalità di pagamento
     * 4			Controllo se la modalità di pagamento è valida (ha una banca associata)
     * 5			Tipo rapporto assente
     * 6			Tipo di rapporto non valido in data inizio missione
     * 7			Tipo trattamento assente
     * 8			Tipo trattamento non valido alla data registrazione
     **/
    private void handleExceptionsTerzo(int error) throws ComponentException {
        switch (error) {
            case 1:
                throw new it.cnr.jada.comp.ApplicationException("Inserire il terzo");
            case 2:
                throw new it.cnr.jada.comp.ApplicationException("Il Terzo selezionato non è valido in Data Registrazione");
            case 3:
                throw new it.cnr.jada.comp.ApplicationException("Selezionare la Modalità di pagamento");
            case 4:
                throw new it.cnr.jada.comp.ApplicationException("Selezionare una Modalità di Pagamento valida");
            case 5:
                throw new it.cnr.jada.comp.ApplicationException("Selezionare il Tipo Rapporto");
            case 6:
                throw new it.cnr.jada.comp.ApplicationException("Il Tipo Rapporto selezionato non è valido alla Data Inizio Missione");
            case 7:
                throw new it.cnr.jada.comp.ApplicationException("Selezionare il Tipo Trattamento");
            case 8:
                throw new it.cnr.jada.comp.ApplicationException("Il Tipo Trattamento selezionato non è valido alla Data Registrazione");
        }
    }

    /**
     * Esercizio non aperto
     * PreCondition:
     * L'esercizio di scrivania e' in uno stato diverso da APERTO
     * PostCondition:
     * Il metodo utilizza un Throw Exception per comunicare che non e' possibile creare missioni.
     * Esercizio aperto
     * PreCondition:
     * L'esercizio di scrivania e' in stato APERTO
     * PostCondition:
     * e' possibile procedere con la creazione della missione
     * <p>
     * Inizializzazione data di registrazione
     * PreCondition:
     * se l'esercizio di scrivania (quello della missione) e' uguale all'esercizio corrente
     * PostCondition:
     * inizializzo la data di registrazione con la data odierna
     * <p>
     * Inizializzazione data di registrazione
     * PreCondition:
     * Se l'esercizio di scrivania (quello della missione) e' antecedente all'esercizio corrente
     * PostCondition:
     * inizializzo la data di registrazione con la data 31/12/esercizio di scrivania
     *
     * @param aUC  lo user context
     * @param bulk l'istanza di  MissioneBulk che si sta creando
     * @return l'istanza di  MissioneBulk inizializzata
     */
    public OggettoBulk inizializzaBulkPerInserimento(UserContext aUC, OggettoBulk bulk) throws ComponentException {
        MissioneBulk missione = (MissioneBulk) bulk;
        try {
            //	Verifico che l'esercizio della missione (scrivania) sia aperto
            if (!verificaStatoEsercizio(aUC, new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(missione.getCd_cds(), ((it.cnr.contab.utenze00.bp.CNRUserContext) aUC).getEsercizio())))
                throw new it.cnr.jada.comp.ApplicationException("Impossibile inserire una missione missione per un esercizio non aperto!");

            missione.setDt_registrazione(getDataRegistrazione(aUC, missione));

            return super.inizializzaBulkPerInserimento(aUC, missione);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(missione, e);
        }
    }

    private Timestamp getDataRegistrazione(UserContext aUC, MissioneBulk missione)
            throws PersistencyException, ComponentException, ApplicationException {
        java.sql.Timestamp tsOdierno = ((MissioneHome) getHome(aUC, missione)).getServerDate();
        GregorianCalendar tsOdiernoGregorian = (GregorianCalendar) missione.getGregorianCalendar(tsOdierno);

        //	Se l'esercizio della missione (scrivania) e' antecedente a quello corrente
        //	inizializzo la data di registrazione a 31/12/esercizio missione
        Timestamp dataRegistrazione = null;
        if (tsOdiernoGregorian.get(GregorianCalendar.YEAR) > missione.getEsercizio().intValue()) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                dataRegistrazione = new java.sql.Timestamp(sdf.parse("31/12/" + missione.getEsercizio().intValue()).getTime());
            } catch (java.text.ParseException e) {
                throw new it.cnr.jada.comp.ApplicationException("Impossibile inizializzare la data di registrazione!");
            }
        } else
            dataRegistrazione = tsOdierno;
        return dataRegistrazione;
    }

    /**
     * Pre-post-conditions:
     * <p>
     * Nome: inizializza Missione
     * Pre:  L'utente ha richiesto l'inizializzzaione dei dati di una missione già inserita per una eventuale modifica
     * Post: Il sistema carica la missione, il terzo della missione, gli inquadramenti e i tipi di rapporto,
     * le tappe della missione (metodo 'caricaTappeMissione'), i dettagli di spesa della missione (metodo 'caricaDettagliMissione'),
     * gli eventuali anticipi, obbligazioni o compensi (metodo 'loadCompenso')
     * su cui la missione e' stata contabilizzata
     *
     * @return la MissioneBulk inizializzata
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk    la MissioneBulk da inizializzare
     */

    public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        MissioneBulk missione = (MissioneBulk) bulk;

        if (missione.getEsercizio() == null)
            throw new it.cnr.jada.comp.ApplicationException("L'esercizio del documento non è valorizzato! Impossibile proseguire.");

        if (missione.getEsercizio().intValue() > it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue())
            throw new it.cnr.jada.comp.ApplicationException("Il documento deve appartenere o all'esercizio di scrivania o ad esercizi precedenti per essere aperto in modifica!");

        missione = (MissioneBulk) super.inizializzaBulkPerModifica(userContext, missione);
        try {
            lockBulk(userContext, missione);
            if (missione == null)
                return null;

            caricaTerzoInModificaMissione(userContext, missione);

            // Inizializzo gli attributi del terzo, la collection di modalita e termini
            // di pagamento e la collection dei tipi rapporto
            completaTerzo(userContext, missione, missione.getV_terzo());

            // Inizializzo la collection degli inquadramenti e dei tipi rapporto eleggibili
            findInquadramentiETipiTrattamento(userContext, missione);
            // Carico il Tipo rapporto selezionato
            caricaTipoTrattamento(missione);

            // Lettura delle tappe della missione e inizializzazioni della relativa collection e dictionary
            caricaTappeMissione(userContext, missione);

            // Lettura dei dettagli della missione e inizializzazioni della collection delle spese e della diaria
            caricaDettagliMissione(userContext, missione);

            // Inizializzo la collection dei giorni
            missione.riempiElencoGiorniCollPerModifica();
            missione.ordinaCollectionGiorni();

            // Riempi gli oggetti complessi persistenti
            getHomeCache(userContext).fetchAll(userContext);

            // Se la missione ha un anticipo associato carico l'eventuale rimborso
            if (missione.isMissioneConAnticipo())
                missione.setAnticipo(caricaRimborsoAnticipo(userContext, missione.getAnticipo()));

            if (missione.getAnticipo() != null)
                missione.setAnticipoClone((AnticipoBulk) missione.getAnticipo().clone());

            // 	Serve nella lettura delle scadenze eleggibili (per poter riselezionare la scadenza
            //	associata alla missione che si sta modificando)
            if (missione.getObbligazione_scadenzario() != null) {
                missione.setObbligazione_scadenzarioClone(new Obbligazione_scadenzarioBulk());
                missione.getObbligazione_scadenzarioClone().setEsercizio_originale(missione.getEsercizio_ori_obbligazione());
                missione.getObbligazione_scadenzarioClone().setPg_obbligazione(missione.getPg_obbligazione());
                missione.getObbligazione_scadenzarioClone().setPg_obbligazione_scadenzario(missione.getPg_obbligazione_scadenzario());
                missione.getObbligazione_scadenzarioClone().setEsercizio(missione.getObbligazione_scadenzario().getEsercizio());
                missione.getObbligazione_scadenzarioClone().setCd_cds(missione.getObbligazione_scadenzario().getCd_cds());

            }

            //	In base allo stato di riporto dell'obbligazione della missione (o del relativo compenso)
            //	inizializzo la variabile 'riportata' della missione
            missione.setRiportata(getStatoRiporto(userContext, missione));

            /**
             * Gennaro Borriello - (02/11/2004 15.04.39)
             *	Aggiunta gestione dell Stato Riportato all'esercizio di scrivania.
             */
            missione.setRiportataInScrivania(getStatoRiportoInScrivania(userContext, missione));


            // carico Compenso
            if (missione.isMissioneConCompenso())
                loadCompenso(userContext, missione);

            missione.setMissioneIniziale((MissioneBulk) missione.clone());
        } catch (Throwable e) {
            throw handleException(e);
        }

        return missione;
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    public OggettoBulk inizializzaBulkPerStampa(UserContext userContext, OggettoBulk bulk) throws ComponentException {

        try {

            Stampa_vpg_missioneBulk stampa = (Stampa_vpg_missioneBulk) bulk;

            Unita_organizzativaHome uoHome = (Unita_organizzativaHome) getHome(userContext, Unita_organizzativaBulk.class);
            Unita_organizzativaBulk uo = (Unita_organizzativaBulk) uoHome.findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));
            stampa.setUoForPrint(uo);

            stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));
            stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));

            stampa.setPgInizio(new Long(0));
            stampa.setPgFine(new Long(999999999));

            stampa.setTerzoForPrint(new TerzoBulk());

            return stampa;

        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Pre-post-conditions:
     * <p>
     * Nome: inizializza Divisa e Cambio Per spesa di tipo RimborsoKm
     * Pre:  Ad una spesa di una missione e' stato associato il tipo RimborsoKm
     * Post: Il sistema imposta la divisa della spesa con la divisa di default e
     * il cambio della spesa con il cambio valido per la divisa e per la data
     * della spesa (se piu' giorni ci si riferisce al primo della selezione)
     *
     * @return la MissioneBulk con la spesa inizializzata
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    aSpesa    la Missione_dettaglioBulk per tipo rimborso km
     */

    public MissioneBulk inizializzaDivisaCambioPerRimborsoKm(UserContext userContext, Missione_dettaglioBulk aSpesa) throws ComponentException, it.cnr.jada.persistency.PersistencyException, javax.ejb.EJBException, it.cnr.jada.bulk.ValidationException {
        aSpesa.setDivisa_spesa(getDivisaDefault(userContext));
        if ((aSpesa.getDivisa_spesa() == null) || (aSpesa.getDivisa_spesa().getCd_divisa() == null))
            throw new it.cnr.jada.bulk.ValidationException("Divisa non disponibile !");

        CambioBulk cambio = null;
        if (aSpesa.getDt_inizio_tappa() != null)        //	Si e' in fase di conferma della spesa
            cambio = findCambio(userContext, aSpesa.getDivisa_spesa(), aSpesa.getDt_inizio_tappa());
        else                                        //	Al bring back di una spesa di tipo rimborso km
            cambio = findCambio(userContext, aSpesa.getDivisa_spesa(), aSpesa.getMissione().getPrimoGiornoSpesaSelezionato());

        if (cambio == null)
            throw new it.cnr.jada.bulk.ValidationException("Cambio non disponibile !");
        aSpesa.setCambio_spesa(cambio.getCambio());

        return aSpesa.getMissione();
    }

    /**
     * Pre-post-conditions:
     * <p>
     * Nome: verifica validita' inquadramento
     * Pre:  Il sistema richiede la validita' dell'inquadramento del terzo associato alla missione
     * Post: Il sistema valida l'inquadramento selezionato
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param missione    La missione il cui inquadramento e' da controllare
     * @return true se l'inquadramento e' ancora valido
     **/

    private boolean isInquadramentoValido(UserContext userContext, MissioneBulk missione) throws ComponentException {
        try {
            Rif_inquadramentoHome home = (Rif_inquadramentoHome) getHome(userContext, Rif_inquadramentoBulk.class);
            return home.isInquadramentoValido(missione.getPg_rif_inquadramento(), missione.getTipo_rapporto(), missione.getV_terzo(), missione.getDt_inizio_missione());
        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Viene richiesto lo stato cofi della missione
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: Missione ANNULLATA - Stato COFI uguale ad 'A'
     * Pre: La missione è annullata
     * Post: Ritorna <true>. La missione è annullata
     * <p>
     * Nome: Missione NON ANNULLATA - Stato COFI diverso da 'A'
     * Pre: La missione non è annullata
     * Post: Ritorna <false>. La missione non è annullata
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param missione    La missione da controllare
     * @return vero se la missione è anullata
     * falso altrimenti
     **/
    public boolean isMissioneAnnullata(UserContext userContext, MissioneBulk missione) throws ComponentException {
        try {
            java.sql.ResultSet rs;
            String stato = null;
            String str = "SELECT STATO_COFI FROM " +
                    it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                    "MISSIONE " +
                    "WHERE " +
                    "CD_CDS = ? AND " +
                    "CD_UNITA_ORGANIZZATIVA = ? AND " +
                    "ESERCIZIO = ? AND " +
                    "PG_MISSIONE = ?";

            LoggableStatement ps = new LoggableStatement(getConnection(userContext), str,
                    true, this.getClass());
            try {
                ps.setObject(1, missione.getCd_cds());
                ps.setObject(2, missione.getCd_unita_organizzativa());
                ps.setObject(3, missione.getEsercizio());
                ps.setObject(4, missione.getPg_missione());

                rs = ps.executeQuery();
                try {
                    if (rs.next())
                        stato = rs.getString(1);
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

            return (missione.STATO_ANNULLATO.equals(stato));

        } catch (java.sql.SQLException ex) {
            throw handleException(missione, ex);
        }
    }

    /**
     * Pre-post-conditions:
     * <p>
     * Nome: verifica validita' tipo rapporto
     * Pre:  Il sistema richiede la validita' del Tipo Rapporto del terzo associato alla missione
     * Post: Il sistema valida il tipo rapporto selezionato
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param missione    La missione il cui Tipo Rapporto e' da controllare
     * @return true se il Tipo Rapporto e' ancora valido
     **/

    private boolean isTipoRapportoValido(UserContext userContext, MissioneBulk missione) throws ComponentException {
        try {
            Tipo_rapportoHome home = (Tipo_rapportoHome) getHome(userContext, Tipo_rapportoBulk.class);
            return home.isTipoRapportoValido(missione.getV_terzo(), missione.getCd_tipo_rapporto(), missione.getDt_inizio_missione());
        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Pre-post-conditions:
     * <p>
     * Nome: verifica validita' Tipo Trattamento
     * Pre:  Il sistema richiede la validita' del Tipo Trattamento del terzo associato alla missione
     * Post: Il sistema valida il Tipo Trattamento selezionato
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param missione    La missione il cui Tipo Trattamento e' da controllare
     * @return true se il Tipo Trattamento e' ancora valido
     **/

    private boolean isTipoTrattamentoValido(UserContext userContext, MissioneBulk missione) throws ComponentException {
        try {
            Tipo_trattamentoHome home = (Tipo_trattamentoHome) getHome(userContext, Tipo_trattamentoBulk.class);
            Filtro_trattamentoBulk filtro = new Filtro_trattamentoBulk();
            filtro.setCdTipoRapporto(missione.getCd_tipo_rapporto());
            filtro.setCdTipoTrattamento(missione.getCd_trattamento());
            filtro.setTipoAnagrafico(missione.getTi_anagrafico());
            filtro.setDataValidita(missione.getDt_registrazione());
            filtro.setFlSenzaCalcoli(Boolean.FALSE);
            filtro.setFlDiaria(Boolean.TRUE);
            filtro.setFlDefaultCongualio(Boolean.FALSE);
            filtro.setTiIstituzionaleCommerciale(missione.getTi_istituz_commerc());
            if (filtro.getCdTipoRapporto() != null && filtro.getCdTipoRapporto().equals("DIP")) {
                try {
                    TerzoHome tHome = (TerzoHome) getHomeCache(userContext).getHome(TerzoBulk.class);
                    TerzoBulk tKey = new TerzoBulk(missione.getCd_terzo());
                    TerzoBulk t = (TerzoBulk) tHome.findByPrimaryKey(tKey);

                    RapportoHome rHome = (RapportoHome) getHomeCache(userContext).getHome(RapportoBulk.class);
                    java.util.Collection collRapp = rHome.findByCdAnagCdTipoRapporto(t.getCd_anag(), filtro.getCdTipoRapporto());
                    boolean exit = false;
                    for (java.util.Iterator i = collRapp.iterator(); i.hasNext() && !exit; ) {
                        RapportoBulk r = (RapportoBulk) i.next();
                        exit = true;
                        if (r.getCd_ente_prev_sti() == null)
                            //throw new it.cnr.jada.comp.ApplicationException("Non è stato possibile recuperare l''Ente Previdenziale del dipendente selezionato.");
                            //non blocco perchè potrebbero esserci trattamenti che non prevedono contributi previdenziali
                            //quindi passo il codice fittizio 'XX'
                            filtro.setEntePrev("XX");
                        else
                            filtro.setEntePrev(r.getCd_ente_prev_sti());
                        if (r.getCd_rapp_impiego_sti() == null)
                            throw new it.cnr.jada.comp.ApplicationException("Per il dipendente in esame non è definito un Rapporto di Impiego!");
                        else {
                            Ass_rapp_impiegoHome assHome = (Ass_rapp_impiegoHome) getHome(userContext, Ass_rapp_impiegoBulk.class);
                            Ass_rapp_impiegoBulk assKey = new Ass_rapp_impiegoBulk(r.getCd_rapp_impiego_sti());
                            Ass_rapp_impiegoBulk ass = (Ass_rapp_impiegoBulk) assHome.findByPrimaryKey(assKey);
                            filtro.setTipoRappImpiego(ass.getTipo_rapp_impiego());
                        }
                    }
                } catch (IntrospectionException e) {
                    throw handleException(e);
                }
                //solo per il rapporto DIP aggiungo il filtro "Anno prec" a seconda della data di inizio competenza
                GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
                data_da.setTime(missione.getDt_inizio_missione());
                if (data_da.get(GregorianCalendar.YEAR) == (missione.getEsercizio() - 1))
                    filtro.setFlAnnoPrec(new Boolean(true));
                else
                    filtro.setFlAnnoPrec(new Boolean(false));

            }
            if (missione.getDt_inizio_missione() != null && missione.getDt_fine_missione() != null) {
                GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
                GregorianCalendar data_a = (GregorianCalendar) GregorianCalendar.getInstance();

                data_da.setTime(missione.getDt_inizio_missione());
                data_a.setTime(missione.getDt_fine_missione());

                if (data_da.get(GregorianCalendar.YEAR) == data_a.get(GregorianCalendar.YEAR)) {

                    TerzoHome tHome = (TerzoHome) getHomeCache(userContext).getHome(TerzoBulk.class);
                    TerzoBulk tKey = new TerzoBulk(missione.getCd_terzo());
                    TerzoBulk t = (TerzoBulk) tHome.findByPrimaryKey(tKey);

                    AnagraficoHome aHome = (AnagraficoHome) getHomeCache(userContext).getHome(AnagraficoBulk.class);
                    AnagraficoBulk aKey = new AnagraficoBulk(t.getCd_anag());
                    AnagraficoBulk a = (AnagraficoBulk) aHome.findByPrimaryKey(aKey);

                    if (a.getFl_cervellone() &&
                            !(new Integer(data_da.get(GregorianCalendar.YEAR)).compareTo(a.getAnno_inizio_res_fis().intValue()) < 0) &&
                            !(new Integer(data_da.get(GregorianCalendar.YEAR)).compareTo(a.getAnno_fine_agevolazioni().intValue()) > 0)) {
                        filtro.setFlAgevolazioniCervelli(new Boolean(a.getFl_cervellone()));
                    } else
                        filtro.setFlAgevolazioniCervelli(new Boolean(false));
                } else
                    filtro.setFlAgevolazioniCervelli(new Boolean(false));
            }

            return home.isTipoTrattamentoValido(filtro);
        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Carica compenso
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica compenso
     * Pre:  Una missione deve essere inizializzata per modifica
     * La missione ha un compenso
     * Post: Il sistema richiede alla Component che gestisce il compenso il suo caricamento
     *
     * @return la MissioneBulk con il compenso caricato
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    missione    la MissioneBulk per cui caricare il compenso
     */

    public MissioneBulk loadCompenso(UserContext userContext, MissioneBulk missione) throws ComponentException {
        try {
            CompensoHome compensoHome = (CompensoHome) getHome(userContext, CompensoBulk.class);
            CompensoBulk compenso = compensoHome.loadCompenso(userContext, missione);

            //	Se la missione e' annullata non troverei piu' il suo compenso
            //	perche' ho azzerato i campi della missione nella tabella
            //  dei compensi.
            if (compenso == null && missione.isAnnullato())
                return missione;

            if (compenso == null)
                throw new it.cnr.jada.comp.ApplicationException("Impossibile trovare il compenso associato alla missione !");

            it.cnr.contab.compensi00.ejb.CompensoComponentSession component = (it.cnr.contab.compensi00.ejb.CompensoComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCOMPENSI00_EJB_CompensoComponentSession", it.cnr.contab.compensi00.ejb.CompensoComponentSession.class);
            missione.setCompenso(component.reloadCompenso(userContext, compenso));
            if (missione.getCompenso() == null)
                throw new it.cnr.jada.comp.ApplicationException("Impossibile trovare il compenso associato alla missione !");
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(missione, e);
        } catch (javax.ejb.EJBException e) {
            throw handleException(missione, e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(missione, e);
        }
        return missione;
    }

    /**
     * Modifica missione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: modifica
     * Pre:  Una missione e' stata modificata
     * Post: Il sistema aggiorna lo stato coan/coge della missione e aggiorna l'obbligazione associata alla missione
     * ( metodo 'aggiornaObbligazione')
     * <p>
     * Nome: elimina compenso
     * Pre:  Una missione e' stata modificata
     * L'utente ha scollegato il compenso dalla missione
     * Post: Il sistema elimina il compenso ( metodo 'cancellaCompenso')
     * <p>
     * Nome: elimina anticipo
     * Pre:  Una missione e' stata modificata
     * L'utente ha scollegato l'anticipo dalla missione
     * Post: Il sistema agggiorna lo stato dell'anticipo ( metodo 'aggiornaAnticipo')
     * <p>
     * Nome: collega anticipo
     * Pre:  Una missione e' stata modificata
     * L'utente ha collegato un anticipo alla missione
     * Post: Il sistema agggiorna lo stato dell'anticipo ( metodo 'aggiornaAnticipo')
     *
     * @return la MissioneBulk con il compenso caricato
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    missione    la MissioneBulk per cui caricare il compenso
     */

    public OggettoBulk modificaConBulk(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        return modificaConBulk(userContext, bulk, null);
    }

    /**
     * Modifica missione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: modifica missione
     * Pre:  Validazione dell' eventuale obbligazione collegata alla missione andata a buon fine
     * Post: Il sistema prosegue con l'aggiornamento della missione
     * <p>
     * Nome: modifica
     * Pre:  Una missione e' stata modificata
     * Post: Il sistema aggiorna lo stato coan/coge della missione e aggiorna l'obbligazione associata alla missione
     * ( metodo 'aggiornaObbligazione')
     * <p>
     * Nome: elimina anticipo
     * Pre:  Una missione e' stata modificata
     * L'utente ha scollegato l'anticipo dalla missione
     * Post: Il sistema aggiorna lo stato dell'anticipo ( metodo 'aggiornaAnticipo')
     * <p>
     * Nome: collega anticipo
     * Pre:  Una missione e' stata modificata
     * L'utente ha collegato un anticipo alla missione
     * Post: Il sistema agggiorna lo stato dell'anticipo ( metodo 'aggiornaAnticipo')
     *
     * @return la MissioneBulk con il compenso caricato
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    missione    la MissioneBulk per cui caricare il compenso
     * @param    status        serve per gestire l'eccezione lanciata dall'obbligazione
     * nel caso non ci sia disponibilita' di cassa
     */

    public OggettoBulk modificaConBulk(it.cnr.jada.UserContext userContext, OggettoBulk bulk, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status) throws it.cnr.jada.comp.ComponentException {
        MissioneBulk missione = (MissioneBulk) bulk;

        // Salvo temporaneamente l'hash map dei saldi
        PrimaryKeyHashMap aTempDiffSaldi = new PrimaryKeyHashMap();
        if (missione.getDefferredSaldi() != null)
            aTempDiffSaldi = (PrimaryKeyHashMap) missione.getDefferredSaldi().clone();

        controlloDateTappeConDateMissioni(missione);

        if (!missione.isSalvataggioTemporaneo()) {
            // Validazione dell'eventuale obbligazione collegata alla missione
            if (!missione.isSalvataggioTemporaneo() && missione.isMissioneDefinitiva() &&
                    missione.isObbligazioneObbligatoria()) {
                if (!missione.isMissioneConObbligazione())
                    throw new it.cnr.jada.comp.ApplicationException("Associare una Obbligazione !");
                validaObbligazione(userContext, missione.getObbligazione_scadenzario(), missione);
            }

            //	Verifico che l'anticipo associato alla missione sia ancora eleggibile
            //	(Qualche altro utente potrebbe avere ad esempio annullato il mandato)
            if (missione.isMissioneConAnticipo()) {
                if (!checkEleggibilitaAnticipo(userContext, missione))
                    throw new ApplicationException("Anticipo non eleggibile alla missione");
            }

            // Aggiornamento dell'anticipo che ho slegato dalla missione
            if (missione.getAnticipoClone() != null && missione.getAnticipoClone().getPg_anticipo() != null &&
                    !missione.getAnticipoClone().equalsByPrimaryKey(missione.getAnticipo()))
                aggiornaAnticipo(userContext, missione.getAnticipoClone(), new Boolean(false));

            // Aggiornamento dell'anticipo che ho legato alla missione
            if (missione.getPg_anticipo() != null /*&& !missione.getAnticipo().equalsByPrimaryKey(missione.getAnticipoClone())*/)
                aggiornaAnticipo(userContext, missione.getAnticipo(), new Boolean(true));

            // Se ho modificato una missione gia' contabilizzata in Coge o Coan
            // devo predisporre la ricontabilizzazione
            //Aggiornamenti degli stati COGE e COAN
            boolean aggiornaStatoCoge = false;
            try {
                MissioneBulk missioneDB = (MissioneBulk) getTempHome(userContext, MissioneBulk.class).findByPrimaryKey(missione);
                if (!Utility.equalsNull(missione.getFl_associato_compenso(), missioneDB.getFl_associato_compenso()) ||
                        !Utility.equalsNull(missione.getStato_pagamento_fondo_eco(), missioneDB.getStato_pagamento_fondo_eco()) ||
                        !Utility.equalsNull(missione.getCd_terzo(), missioneDB.getCd_terzo()) ||
                        !Utility.equalsNull(missione.getDt_inizio_missione(), missioneDB.getDt_inizio_missione()) ||
                        !Utility.equalsNull(missione.getDt_fine_missione(), missioneDB.getDt_fine_missione()) ||
                        !Utility.equalsNull(missione.getIm_totale_missione(), missioneDB.getIm_totale_missione().setScale(2))
                        )
                    aggiornaStatoCoge = true;
            } catch (PersistencyException e) {
                throw new ComponentException(e);
            }
            if (aggiornaStatoCoge) {
                if (missione.getStato_coan() != null && missione.getStato_coan().compareTo(MissioneBulk.STATO_CONTABILIZZATO_COAN) == 0)
                    missione.setStato_coan(MissioneBulk.STATO_RICONTABILIZZARE_COAN);
                if (missione.getStato_coge() != null && missione.getStato_coge().compareTo(MissioneBulk.STATO_CONTABILIZZATO_COGE) == 0)
                    missione.setStato_coge(MissioneBulk.STATO_RICONTABILIZZARE_COGE);
            }
        }

        aggiornaObbligazione(userContext, missione, status);

        impostaDatiRimborsoDaCompletare(missione);

        missione = (MissioneBulk) super.modificaConBulk(userContext, missione);


        // Restore dell'hash map dei saldi
        if (missione.getDefferredSaldi() != null)
            missione.getDefferredSaldi().putAll(aTempDiffSaldi);
        aggiornaCogeCoanDocAmm(userContext, missione);

        if (!verificaStatoEsercizio(userContext, new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(missione.getCd_cds(), ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio())))
            throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare un documento per un esercizio non aperto!");

        return missione;
    }

    private void controlloDateTappeConDateMissioni(MissioneBulk missione) throws ApplicationException {
        if (!missione.getTappeMissioneColl().isEmpty()) {
            for (Iterator i = missione.getTappeMissioneColl().iterator(); i.hasNext(); ) {
                Missione_tappaBulk aTappa = (Missione_tappaBulk) i.next();
                if (aTappa.getDt_inizio_tappa() != null && missione.getDt_inizio_missione() != null &&
                        aTappa.getDt_inizio_tappa().before(missione.getDt_inizio_missione())) {
                    throw new it.cnr.jada.comp.ApplicationException("Esiste una tappa con data inizio precedente alla data di inizio della missione.");
                }
                if (aTappa.getDt_fine_tappa() != null && missione.getDt_fine_missione() != null &&
                        aTappa.getDt_fine_tappa().after(missione.getDt_fine_missione())) {
                    throw new it.cnr.jada.comp.ApplicationException("Esiste una tappa con data fine successiva alla data di fine della missione.");
                }
            }
        }
    }

    /**
     * Carica diaria
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Carica diaria
     * Pre:  Il sistema ha generato, tramite stored procedure, una diaria per la missione
     * Post: Il sistema carica i dettagli della diaria della missione
     *
     * @return la MissioneBulk con la diaria caricata
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    missione    la MissioneBulk per cui caricare la diaria
     */

    private MissioneBulk ritornaDiariaGenerata(UserContext aUC, MissioneBulk missione) throws ComponentException {
        try {
            Missione_dettaglioHome dettaglioHome = (Missione_dettaglioHome) getHome(aUC, Missione_dettaglioBulk.class);
            SQLBuilder sql = dettaglioHome.createSQLBuilder();

            sql.addSQLClause("AND", "cd_cds", sql.EQUALS, missione.getCd_cds());
            sql.addSQLClause("AND", "cd_unita_organizzativa", sql.EQUALS, missione.getCd_unita_organizzativa());
            sql.addSQLClause("AND", "esercizio", sql.EQUALS, missione.getEsercizio());
            sql.addSQLClause("AND", "pg_missione", sql.EQUALS, missione.getPg_missione());
            sql.addSQLClause("AND", "ti_spesa_diaria", sql.EQUALS, Missione_dettaglioBulk.TIPO_DIARIA);

            it.cnr.jada.bulk.BulkList dettagliDiaria = new it.cnr.jada.bulk.BulkList(dettaglioHome.fetchAll(sql));

            if ((dettagliDiaria == null) || (dettagliDiaria.isEmpty()))
                throw new it.cnr.jada.comp.ApplicationException("Problemi nella creazione della diaria !");

            missione.setDiariaMissioneColl(dettagliDiaria);

            for (Iterator i = missione.getDiariaMissioneColl().iterator(); i.hasNext(); ) {
                Missione_dettaglioBulk aDiaria = (Missione_dettaglioBulk) i.next();
                aDiaria.setMissione(missione);
            }

            return missione;
        } catch (Throwable e) {
            throw handleException(missione, e);
        }
    }

    private MissioneBulk ritornaRimborsoGenerato(UserContext aUC, MissioneBulk missione) throws ComponentException {
        try {
            Missione_dettaglioHome dettaglioHome = (Missione_dettaglioHome) getHome(aUC, Missione_dettaglioBulk.class);
            SQLBuilder sql = dettaglioHome.createSQLBuilder();

            sql.addSQLClause("AND", "cd_cds", sql.EQUALS, missione.getCd_cds());
            sql.addSQLClause("AND", "cd_unita_organizzativa", sql.EQUALS, missione.getCd_unita_organizzativa());
            sql.addSQLClause("AND", "esercizio", sql.EQUALS, missione.getEsercizio());
            sql.addSQLClause("AND", "pg_missione", sql.EQUALS, missione.getPg_missione());
            sql.addSQLClause("AND", "ti_spesa_diaria", sql.EQUALS, Missione_dettaglioBulk.TIPO_RIMBORSO);

            it.cnr.jada.bulk.BulkList dettagliRimborso = new it.cnr.jada.bulk.BulkList(dettaglioHome.fetchAll(sql));

            if ((dettagliRimborso == null) || (dettagliRimborso.isEmpty()))
                throw new it.cnr.jada.comp.ApplicationException("Problemi nella creazione del rimborso !");

            missione.setRimborsoMissioneColl(dettagliRimborso);

            for (Iterator i = missione.getRimborsoMissioneColl().iterator(); i.hasNext(); ) {
                Missione_dettaglioBulk aRimborso = (Missione_dettaglioBulk) i.next();
                aRimborso.setMissione(missione);
            }

            return missione;
        } catch (Throwable e) {
            throw handleException(missione, e);
        }
    }

    public Obbligazione_scadenzarioBulk recuperoObbligazioneDaGemis(UserContext aUC, MissioneBulk missione) throws ComponentException {
        Obbligazione_scadenzarioBulk obblScad = null;
        try {
            if (missione.getEsercizioObblGeMis() != null && missione.getEsercizioOriObblGeMis() != null && missione.getCdsObblGeMis() != null && missione.getPgObblGeMis() != null) {

                if (missione.getGaeGeMis() != null) {
                    Obbligazione_scad_voceHome scadenzaHome = (Obbligazione_scad_voceHome) getHome(aUC, Obbligazione_scad_voceBulk.class);
                    SQLBuilder sql = scadenzaHome.createSQLBuilder();

                    sql.addSQLClause("AND", "OBBLIGAZIONE_SCAD_VOCE.CD_LINEA_ATTIVITA", sql.EQUALS, missione.getGaeGeMis());
                    sql.addSQLClause("AND", "OBBLIGAZIONE_SCAD_VOCE.IM_VOCE", sql.GREATER_EQUALS, missione.getImportoDaRimborsare());

                    SQLBuilder sqlExists = impostaFiltroQueryObbligazioniFromGemis(aUC, missione);

                    sqlExists.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.CD_CDS", "OBBLIGAZIONE_SCAD_VOCE.CD_CDS");
                    sqlExists.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.ESERCIZIO", "OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO");
                    sqlExists.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE", "OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO_ORIGINALE");
                    sqlExists.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE", "OBBLIGAZIONE_SCAD_VOCE.PG_OBBLIGAZIONE");
                    sqlExists.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE_SCADENZARIO", "OBBLIGAZIONE_SCAD_VOCE.PG_OBBLIGAZIONE_SCADENZARIO");
                    sql.addSQLExistsClause("AND", sqlExists);

                    sql.addOrderBy("OBBLIGAZIONE_SCAD_VOCE.PG_OBBLIGAZIONE_SCADENZARIO");

                    it.cnr.jada.bulk.BulkList scadVoces = new it.cnr.jada.bulk.BulkList(scadenzaHome.fetchAll(sql));
                    if ((scadVoces != null) && (!scadVoces.isEmpty())) {
                        Obbligazione_scad_voceBulk scadVoce = (Obbligazione_scad_voceBulk) scadVoces.get(0);
                        Obbligazione_scadenzarioBulk scad = scadVoce.getObbligazione_scadenzario();
                        Obbligazione_scadenzarioHome scadHome = (Obbligazione_scadenzarioHome) getHome(aUC, Obbligazione_scadenzarioBulk.class);
                        scad = ((Obbligazione_scadenzarioBulk) scadHome.findByPrimaryKey(scad));
                        ObbligazioneHome obblHome = (ObbligazioneHome) getHome(aUC, ObbligazioneBulk.class);
                        scad.setObbligazione((ObbligazioneBulk) obblHome.findByPrimaryKey(scad.getObbligazione()));
                        obblScad = scad;
                    }
                } else {
                    Obbligazione_scadenzarioHome scadenzaHome = (Obbligazione_scadenzarioHome) getHome(aUC, Obbligazione_scadenzarioBulk.class);
                    SQLBuilder sql = impostaFiltroQueryObbligazioniFromGemis(aUC, missione);
                    sql.addOrderBy("OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE_SCADENZARIO");

                    it.cnr.jada.bulk.BulkList scadenzario = new it.cnr.jada.bulk.BulkList(scadenzaHome.fetchAll(sql));
                    if ((scadenzario != null) && (!scadenzario.isEmpty())) {
                        Obbligazione_scadenzarioBulk scad = (Obbligazione_scadenzarioBulk) scadenzario.get(0);
                        ObbligazioneHome obblHome = (ObbligazioneHome) getHome(aUC, ObbligazioneBulk.class);
                        scad.setObbligazione((ObbligazioneBulk) obblHome.findByPrimaryKey(scad.getObbligazione()));
                        obblScad = scad;
                    }
                }
            } else {
                return null;
            }
            if (obblScad != null) {
                return gestioneScadenzaObbligazioneDaGemis(aUC, missione, obblScad);
            }
        } catch (Throwable e) {
            throw handleException(missione, e);
        }
        return obblScad;
    }

    private Obbligazione_scadenzarioBulk gestioneScadenzaObbligazioneDaGemis(UserContext aUC, MissioneBulk missione, Obbligazione_scadenzarioBulk obblScad)
            throws ComponentException {
        try {
            Obbligazione_scadenzarioBulk scadenzaNuova = null;
            BigDecimal importoResiduo = obblScad.getImportoDisponibile().subtract(missione.getImportoDaRimborsare());
            if (obblScad != null && importoResiduo.compareTo(BigDecimal.ZERO) > 0) {
                return sdoppiaObbligazioneScadenzario(aUC, missione, obblScad, importoResiduo);
            } else if (obblScad != null && importoResiduo.compareTo(BigDecimal.ZERO) == 0) {
                return obblScad;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private Obbligazione_scadenzarioBulk sdoppiaObbligazioneScadenzario(UserContext aUC, MissioneBulk missione,
                                                                        Obbligazione_scadenzarioBulk obblScad, BigDecimal importoResiduo)
            throws ComponentException, RemoteException, PersistencyException, ValidationException {
        Obbligazione_scadenzarioBulk scadenzaNuova;
        ObbligazioneAbstractComponentSession sess = (ObbligazioneAbstractComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession");
        java.sql.Timestamp ts = getDataRegistrazione(aUC, missione);
        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        ts = new Timestamp(cal.getTime().getTime());
        DatiFinanziariScadenzeDTO datiScadenze = new DatiFinanziariScadenzeDTO();
        datiScadenze.setCdCentroResponsabilita(missione.getCdrGeMis());
        datiScadenze.setCdLineaAttivita(missione.getGaeGeMis());
        datiScadenze.setCdVoce(missione.getVoceGeMis());
        datiScadenze.setNuovaDescrizione(missione.getDs_missione());
        datiScadenze.setNuovaScadenza(ts);
        datiScadenze.setNuovoImportoScadenzaVecchia(importoResiduo);
        scadenzaNuova = (Obbligazione_scadenzarioBulk) sess
                .sdoppiaScadenzaInAutomatico(
                        aUC,
                        obblScad,
                        datiScadenze);

        // ricarico obbligazione e recupero i riferimenti alle scadenze
        ObbligazioneBulk obbligazione = (ObbligazioneBulk) sess.inizializzaBulkPerModifica(aUC, scadenzaNuova.getObbligazione());

        if (!obbligazione.getObbligazione_scadenzarioColl()
                .containsByPrimaryKey(obblScad)
                || !obbligazione.getObbligazione_scadenzarioColl()
                .containsByPrimaryKey(scadenzaNuova))
            throw new ValidationException(
                    "Errore nello sdoppiamento della scadenza dell'impegno.");

        obblScad = (Obbligazione_scadenzarioBulk) obbligazione
                .getObbligazione_scadenzarioColl().get(
                        obbligazione.getObbligazione_scadenzarioColl()
                                .indexOfByPrimaryKey(obblScad));
        scadenzaNuova = (Obbligazione_scadenzarioBulk) obbligazione
                .getObbligazione_scadenzarioColl().get(
                        obbligazione.getObbligazione_scadenzarioColl()
                                .indexOfByPrimaryKey(scadenzaNuova));
        return scadenzaNuova;
    }

    private SQLBuilder impostaFiltroQueryObbligazioniFromGemis(UserContext aUC, MissioneBulk missione) throws PersistencyException, ComponentException, ApplicationException {
        Filtro_ricerca_obbligazioniVBulk filtro = new Filtro_ricerca_obbligazioniVBulk();

        filtro.setCd_unita_organizzativa(missione.getCd_unita_organizzativa());
        filtro.setFl_data_scadenziario(false);
        filtro.setIm_importo(missione.getImportoDaRimborsare());
        filtro.setFl_fornitore(false);
        filtro.setFl_importo(true);
        filtro.setFl_nr_obbligazione(true);
        TerzoBulk terzo = new TerzoBulk();
        terzo.setCd_terzo(missione.getCd_terzo());
        filtro.setFornitore(terzo);
        filtro.setEsercizio_ori_obbligazione(missione.getEsercizioOriObblGeMis());
        filtro.setNr_obbligazione(missione.getPgObblGeMis());

        SQLBuilder sql = prepareQueryObbligazioni(aUC, filtro);

        return sql;
    }

    public AnticipoBulk recuperoAnticipoDaGemis(UserContext aUC, MissioneBulk missione) throws ComponentException {
        try {
            if (missione.getEsercizioAnticipoGeMis() != null && missione.getCdsAnticipoGeMis() != null && missione.getPgAnticipoGeMis() != null && missione.getCd_terzo() != null) {

                Mandato_rigaHome mandatoHome = (Mandato_rigaHome) getHome(aUC, Mandato_rigaIBulk.class);
                SQLBuilder sql = mandatoHome.createSQLBuilder();

                sql.addSQLClause("AND", "cd_cds", sql.EQUALS, missione.getCdsAnticipoGeMis());
                sql.addSQLClause("AND", "esercizio", sql.EQUALS, missione.getEsercizioAnticipoGeMis());
                sql.addSQLClause("AND", "pg_mandato", sql.EQUALS, missione.getPgAnticipoGeMis());
                sql.addSQLClause("AND", "cd_terzo", sql.EQUALS, missione.getCd_terzo());
                it.cnr.jada.bulk.BulkList mandati = new it.cnr.jada.bulk.BulkList(mandatoHome.fetchAll(sql));

                if ((mandati != null) && (!mandati.isEmpty())) {
                    for (Object object : mandati) {
                        Mandato_rigaIBulk mandato = (Mandato_rigaIBulk) object;
                        if (mandato.getCd_tipo_documento_amm() != null && mandato.getCd_tipo_documento_amm().equals("ANTICIPO")) {
                            AnticipoHome anticipoHome = (AnticipoHome) getHome(aUC, AnticipoBulk.class);
                            AnticipoKey key = new AnticipoKey(mandato.getCd_cds_doc_amm(), mandato.getCd_uo_doc_amm(), mandato.getEsercizio_doc_amm(), mandato.getPg_doc_amm());
                            AnticipoBulk anticipo = ((AnticipoBulk) anticipoHome.findByPrimaryKey(key));
                            if (anticipo != null && !anticipo.isAnticipoConMissione()) {
                                if (checkEleggibilitaAnticipo(aUC, missione))
                                    return anticipo;
                            }
                        }
                    }
                }
            } else {
                return null;
            }
        } catch (Throwable e) {
            throw handleException(missione, e);
        }
        return null;
    }

    /**
     * Annulla le modifiche apportate alla missione e ritorna al savepoint impostato in precedenza
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: 	Rollback to savePoint
     * Pre:  	Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata
     * Post: 	Tutte le modifiche effettuate sulla missione da quando si e' impostato il savepoint vengono annullate
     *
     * @param    uc    lo UserContext che ha generato la richiesta
     */
    public void rollbackToSavePoint(UserContext userContext, String savePointName) throws ComponentException {
        try {
            rollbackToSavepoint(userContext, savePointName);
        } catch (java.sql.SQLException e) {
            if (e.getErrorCode() != 1086)
                throw handleException(e);
        }
    }

    /**
     * Ricerca missione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Ricerca
     * Pre:  L'utente ha richiesto una ricerca di missioni
     * Post: Il sistema restitusce il SQLBuilder con le clausole specificate dall'utente e in aggiunta con le clausole che
     * la missione abbia cds, unità organizzativa uguali a quello di scrivania
     *
     * @param clauses le clausole speicificate dall'utene
     * @return il SQLBuilder con tutte le clausole
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk    la MissioneBulk da ricercare
     */

    public Query select(UserContext userContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = (SQLBuilder) super.select(userContext, clauses, bulk);
        MissioneBulk missione = (MissioneBulk) bulk;
        sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS,
                Optional.ofNullable(missione)
                    .flatMap(missioneBulk -> Optional.ofNullable(missioneBulk.getCd_cds()))
                    .orElseGet(() -> CNRUserContext.getCd_cds(userContext))
        );
        sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS,
                Optional.ofNullable(missione)
                        .flatMap(missioneBulk -> Optional.ofNullable(missioneBulk.getCd_unita_organizzativa()))
                        .orElseGet(() -> CNRUserContext.getCd_unita_organizzativa(userContext))
        );
        sql.addTableToHeader("TERZO");
        sql.addSQLJoin("MISSIONE.CD_TERZO", "TERZO.CD_TERZO");
        sql.addSQLClause("AND", "TERZO.CD_PRECEDENTE", SQLBuilder.EQUALS,
                Optional.ofNullable(missione)
                    .flatMap(missioneBulk -> Optional.ofNullable(missioneBulk.getV_terzo()))
                    .flatMap(v_terzo_per_compensoBulk -> Optional.ofNullable(v_terzo_per_compensoBulk.getCd_terzo_precedente()))
                    .orElse(null)
        );
        return sql;
    }

    /**
     * Ricerca anticipo
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Ricerca
     * Pre:  L'utente ha richiesto una ricerca degli anticipi associabili alla missione
     * Post: Il sistema restitusce il SQLBuilder con le clausole specificate dall'utente e in aggiunta le seguenti clausole clausole:
     * - anticipo non associato ad altra missione e non rimborsato
     * - anticipo non cancellato
     * - anticipo pagato e con relativo mandato riscontrato oppure
     * anticipo registrato sul fondo
     * - anticipo definito per lo stesso terzo della missione
     *
     * @return il SQLBuilder con tutte le clausole
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    missione    la MissioneBulk da cui ricavare il terzo e il tipo di pagamento
     * @param    anticipo    l'AnticipoBulk da ricercare
     * @param    clauses    le clausole specificate dall'utente
     */

    public SQLBuilder selectAnticipoByClause(UserContext aUC, MissioneBulk missione, AnticipoBulk anticipo, CompoundFindClause clauses) throws ComponentException {
        AnticipoHome anticipoHome = (AnticipoHome) getHome(aUC, AnticipoBulk.class);
        SQLBuilder sql = anticipoHome.createSQLBuilder();

        sql.addSQLClause("AND", "Anticipo.cd_terzo = ?" +
                " and Anticipo.ti_anagrafico = ?" +
                " and Anticipo.esercizio <= ?" +
                " and Anticipo.cd_cds = ?" +
                " and Anticipo.cd_unita_organizzativa = ?" +
                " and Anticipo.dt_cancellazione IS NULL " +

                " and ((exists (SELECT 1 FROM " +
                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "mandato man, " +
                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "mandato_riga manr " +
                " where man.stato = ?" +
                " and   manr.pg_mandato = man.pg_mandato" +
                " and   manr.cd_cds = man.cd_cds" +
                " and   manr.esercizio = man.esercizio" +
                " and   manr.cd_tipo_documento_amm = ?" +
                " and   manr.esercizio_doc_amm = Anticipo.esercizio" +
                " and   manr.cd_uo_doc_amm = Anticipo.cd_unita_organizzativa" +
                " and   manr.cd_cds_doc_amm = Anticipo.cd_cds" +
                " and   manr.pg_doc_amm = Anticipo.pg_anticipo))" +
                " or (Anticipo.stato_pagamento_fondo_eco = ? ))" +

                " and not exists (SELECT 1 FROM " +
                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "rimborso B " +
                " where B.cd_cds_anticipo = Anticipo.cd_cds" +
                " and B.cd_uo_anticipo = Anticipo.cd_unita_organizzativa" +
                " and B.esercizio_anticipo = Anticipo.esercizio" +
                " and B.pg_anticipo = Anticipo.pg_anticipo)" +

                " and (" +
                "not exists (SELECT 1 FROM " +
                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "missione C " +
                " where C.cd_cds_anticipo = Anticipo.cd_cds" +
                " and C.cd_uo_anticipo = Anticipo.cd_unita_organizzativa" +
                " and C.esercizio_anticipo = Anticipo.esercizio" +
                " and C.pg_anticipo = Anticipo.pg_anticipo " +
                " and C.dt_cancellazione IS NULL " +
                " and C.stato_cofi <> 'A' )" +
                " or " +
                " exists (SELECT 1 FROM " +
                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "missione C " +
                " where C.cd_cds_anticipo = Anticipo.cd_cds" +
                " and C.cd_uo_anticipo = Anticipo.cd_unita_organizzativa" +
                " and C.esercizio_anticipo = Anticipo.esercizio" +
                " and C.pg_anticipo = Anticipo.pg_anticipo" +
                " and C.pg_missione = ?" +
                " and C.esercizio = ?" +
                " and C.cd_cds = ?" +
                " and C.cd_unita_organizzativa = ?))");


        sql.addParameter(missione.getCd_terzo(), java.sql.Types.NUMERIC, 0);
        sql.addParameter(missione.getTi_anagrafico(), java.sql.Types.CHAR, 0);
        sql.addParameter(missione.getEsercizio(), java.sql.Types.NUMERIC, 0);
        sql.addParameter(missione.getCd_cds(), java.sql.Types.CHAR, 0);
        sql.addParameter(missione.getCd_unita_organizzativa(), java.sql.Types.CHAR, 0);

        sql.addParameter(MandatoBulk.STATO_MANDATO_PAGATO, java.sql.Types.CHAR, 0);
        sql.addParameter(Numerazione_doc_ammBulk.TIPO_ANTICIPO, java.sql.Types.CHAR, 0);
        sql.addParameter(AnticipoBulk.STATO_REGISTRATO_FONDO_ECO, java.sql.Types.CHAR, 0);

        sql.addParameter(missione.getPg_missione(), java.sql.Types.NUMERIC, 0);
        sql.addParameter(missione.getEsercizio(), java.sql.Types.NUMERIC, 0);
        sql.addParameter(missione.getCd_cds(), java.sql.Types.CHAR, 0);
        sql.addParameter(missione.getCd_unita_organizzativa(), java.sql.Types.CHAR, 0);

        sql.addClause(clauses);

        return sql;
    }

    /**
     * Ricerca banca
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Ricerca
     * Pre:  L'utente ha richiesto una ricerca delle coordinate bancarie del terzo associato alla missione
     * Post: Il sistema restitusce il SQLBuilder con le clausole specificate dall'utente e in aggiunta con le clausole che
     * le coordinate bancarie siano valide (non cancellate), siano associate al terzo della missione a abbiano un tipo uguale
     * a quello selezionato dall'utente (bancario, postale, etc.)
     *
     * @return il SQLBuilder con tutte le clausole
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    missione    la MissioneBulk da cui ricavare il terzo e il tipo di pagamento
     * @param    banca    la BancaBulk da ricercare
     * @param    clauses    le clausole specificate dall'utente
     */

    public SQLBuilder selectBancaByClause(UserContext aUC, MissioneBulk missione, BancaBulk banca, CompoundFindClause clauses) throws ComponentException {
        BancaHome bancaHome = (BancaHome) getHome(aUC, BancaBulk.class);
        return bancaHome.selectBancaFor(
                missione.getModalita_pagamento(),
                missione.getCd_terzo());
    }

    /**
     * Ricerca nazione
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Ricerca estero
     * Pre:  L'utente ha richiesto una ricerca delle nazioni per una tappa relativa ad un comune estero
     * Post: Il sistema restitusce il SQLBuilder con le clausole specificate dall'utente e in aggiunta la clausole
     * che il tipo nazione sia diverso da Italia
     * <p>
     * Nome: Ricerca italia
     * Pre:  L'utente ha richiesto una ricerca della nazione per una tappa relativa ad un comune non estero
     * Post: Il sistema restitusce il SQLBuilder con le clausole specificate dall'utente e in aggiunta la clausole
     * che il tipo nazione sia uguale ad Italia
     *
     * @return il SQLBuilder con tutte le clausole
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    tappa    la Missione_tappaBulk da cui ricercare la nazione
     * @param    nazione    la NazioneBulk da ricercare
     * @param    clauses    le clausole specificate dall'utente
     */


    public SQLBuilder selectNazioneByClause(UserContext aUC, Missione_tappaBulk tappa, NazioneBulk nazione, CompoundFindClause clauses) throws ComponentException {
        NazioneHome nazioneHome = (NazioneHome) getHome(aUC, NazioneBulk.class);
        SQLBuilder sql = nazioneHome.createSQLBuilder();

        if ((tappa.getFl_comune_estero() != null) && (tappa.getFl_comune_estero().booleanValue()))
            sql.addClause("AND", "ti_nazione", sql.NOT_EQUALS, NazioneBulk.ITALIA);
        else
            sql.addClause("AND", "ti_nazione", sql.EQUALS, NazioneBulk.ITALIA);

        if (clauses != null)
            sql.addClause(clauses);
        return sql;
    }

    /**
     * Pre-post-conditions:
     * <p>
     * Nome: Seleziona scadenza di obbligazione
     * Pre:  Una richiesta di listare le scadenze di obbligazione da cui selezionare quella da utilizzare
     * per la contabilizzazione della missione e' stata generata
     * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente e le seguenti clausole aggiuntive:
     * - obbligazione definitiva non annullata
     * - obbligazione con cds e esercizio uguali a quelli di scrivania
     * - obbligazione con terzo uguale a quello specificato per la missione
     * - scadenza con data maggiore o uguale a quella di registrazione della missione
     * - scadenza con importo associato a doc. amministrativo uguale a 0
     *
     * @return il SQLBuilder con tutte le clausole
     * @param    aUC    lo UserContext che ha generato la richiesta
     * @param    missione la MissioneBulk per cui selezionare la scadenza di obbligazione
     * @param    scadenza l'Obbligazione_scadenzarioBulk da ricercare
     * @param    clauses le clausole specificate dall'utente
     */

    public SQLBuilder selectObbligazione_scadenzarioByClause(UserContext aUC, MissioneBulk missione, Obbligazione_scadenzarioBulk scadenza, CompoundFindClause clauses) throws ComponentException {
        Obbligazione_scadenzarioHome scadenzaHome = (Obbligazione_scadenzarioHome) getHome(aUC, Obbligazione_scadenzarioBulk.class);
        SQLBuilder sql = scadenzaHome.createSQLBuilder();

        GregorianCalendar ggRegistrazione = (GregorianCalendar) missione.getGregorianCalendar(missione.getDt_registrazione()).clone();
        ggRegistrazione.set(GregorianCalendar.HOUR_OF_DAY, 0);
        ggRegistrazione.set(GregorianCalendar.MINUTE, 0);
        ggRegistrazione.set(GregorianCalendar.SECOND, 0);
        java.sql.Timestamp dataRegistrazione = new java.sql.Timestamp(ggRegistrazione.getTime().getTime());

        sql.setDistinctClause(true);
        sql.addTableToHeader("OBBLIGAZIONE");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.CD_CDS", "OBBLIGAZIONE.CD_CDS");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.ESERCIZIO", "OBBLIGAZIONE.ESERCIZIO");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE", "OBBLIGAZIONE.ESERCIZIO_ORIGINALE");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE", "OBBLIGAZIONE.PG_OBBLIGAZIONE");

        sql.addSQLClause("AND", "OBBLIGAZIONE.ESERCIZIO", sql.EQUALS, missione.getEsercizio());
        sql.addSQLClause("AND", "OBBLIGAZIONE.CD_CDS", sql.EQUALS, missione.getCd_cds());
        sql.addSQLClause("AND", "OBBLIGAZIONE.STATO_OBBLIGAZIONE", sql.EQUALS, "D");
        sql.addSQLClause("AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", sql.ISNULL, null);
        sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TERZO", sql.EQUALS, missione.getCd_terzo());
        sql.addSQLClause("AND", "OBBLIGAZIONE.RIPORTATO", sql.EQUALS, "N");

        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.DT_SCADENZA", sql.GREATER_EQUALS, dataRegistrazione);
        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_AMM = ? OR OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_AMM IS NULL");
        sql.addParameter(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP), java.sql.Types.DECIMAL, 2);
        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_CONTABILE = ? OR OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_CONTABILE IS NULL");
        sql.addParameter(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP), java.sql.Types.DECIMAL, 2);

        if (clauses != null)
            sql.addClause(clauses);

        return sql;
    }

    /**
     * Ricerca tipo auto
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Ricerca
     * Pre:  L'utente ha richiesto una ricerca dei tipi di auto per una spesa di una missione
     * Post: Il sistema restitusce il SQLBuilder con le clausole specificate dall'utente e in aggiunta la clausole
     * che il tipo auto abbia un intervallo di validità comprendente la data di inizio della missione e
     * un'area geografica  e una nazione comaptibili con quelli della tappa della spesa
     *
     * @return il SQLBuilder con tutte le clausole
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    dettaglioSpesa    la dettaglioSpesa per cui ricercare il tipo auto
     * @param    tipoAuto    la Missione_rimborso_kmBulk da ricercare
     * @param    clauses    le clausole specificate dall'utente
     */

    public SQLBuilder selectTipo_autoByClause(UserContext aUC, Missione_dettaglioBulk dettaglioSpesa, Missione_rimborso_kmBulk tipoAuto, CompoundFindClause clauses) throws ComponentException {
        MissioneBulk missione = dettaglioSpesa.getMissione();

        if (dettaglioSpesa.getTi_spesa_diaria().equals(Missione_dettaglioBulk.TIPO_DIARIA))
            return null;

        if (!dettaglioSpesa.isRimborsoKm())
            return null;

        // 	Se sto recuperando un tipo auto di un dettaglio appena caricato da db
        //	uso dt_inizio_tappa del dettaglio per recuperare la relativa tappa
        //	perche' non ho selezionato alcun giorno.
        //	Se sto cercando il tipo auto per un dettaglio che sto creando/modificando
        //	considero il giorno selezionato
        java.sql.Timestamp primoGG = null;
        if (dettaglioSpesa.getCrudStatus() == OggettoBulk.NORMAL)
            primoGG = dettaglioSpesa.getDt_inizio_tappa();
        else
            primoGG = missione.getPrimoGiornoSpesaSelezionato();

        Missione_tappaBulk tappa = (Missione_tappaBulk) missione.getTappeMissioneHash().get(primoGG);

        return selectTipo_autoByClause(aUC, missione.getDt_inizio_missione(), tappa.getNazione(), dettaglioSpesa.getTi_auto(), clauses);

    }

    public SQLBuilder selectTipo_autoByClause(UserContext aUC, Timestamp dataTappa, NazioneBulk nazione, String tipoAuto, CompoundFindClause clauses) throws ComponentException {
        Missione_rimborso_kmHome aTipoAutoHome = (Missione_rimborso_kmHome) getHome(aUC, Missione_rimborso_kmBulk.class);
        SQLBuilder sql = aTipoAutoHome.createSQLBuilder();

        sql.addClause("AND", "dt_inizio_validita", sql.LESS_EQUALS, dataTappa);
        sql.addClause("AND", "dt_fine_validita", sql.GREATER_EQUALS, dataTappa);

        sql.openParenthesis("AND");
        if ((nazione != null) && ((NazioneBulk.ITALIA).equals(nazione.getTi_nazione())))
            sql.addClause("AND", "ti_area_geografica", sql.EQUALS, "I");
        else
            sql.addClause("AND", "ti_area_geografica", sql.EQUALS, "E");
        sql.addClause("OR", "ti_area_geografica", sql.EQUALS, "*");
        sql.closeParenthesis();

        sql.openParenthesis("AND");
        sql.addClause("AND", "pg_nazione", sql.EQUALS, nazione.getPg_nazione());
        sql.addClause("OR", "pg_nazione", sql.EQUALS, new Long(0));
        sql.closeParenthesis();

        sql.addClause("AND", "ti_auto", sql.EQUALS, tipoAuto);

        sql.addSQLClause("AND", "(ti_auto || ti_area_geografica || pg_nazione || TO_CHAR(dt_inizio_validita, 'DDMMYYYY') || TO_CHAR(dt_fine_validita, 'DDMMYYYY'))  = " +
                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + " CNRCTB500.getFirstTabMissione('03', ti_auto, ?, ?, null, ?)");


        if ((nazione != null) && ((NazioneBulk.ITALIA).equals(nazione.getTi_nazione())))
            sql.addParameter("I", java.sql.Types.CHAR, 5);
        else
            sql.addParameter("E", java.sql.Types.CHAR, 5);
        sql.addParameter(nazione.getPg_nazione(), java.sql.Types.NUMERIC, 6);
        sql.addParameter(dataTappa, java.sql.Types.TIMESTAMP, 7);

        sql.addPreOrderBy(" ti_auto, pg_nazione desc, ti_area_geografica");

        return sql;
    }

    /**
     * Ricerca tipo pasto
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Ricerca
     * Pre:  L'utente ha richiesto una ricerca dei tipi di pasto per una spesa di una missione
     * Post: Il sistema restitusce il SQLBuilder con le clausole specificate dall'utente e in aggiunta la clausole
     * che il tipo pasto abbia un intervallo di validità comprendente la data di inizio della missione,
     * un'area geografica e una nazione compatibili con quelli della tappa della spesa e un inqudramento uguale a
     * quello selezionato per la missione
     *
     * @return il SQLBuilder con tutte le clausole
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    dettaglioSpesa    la dettaglioSpesa per cui ricercare il tipo pasto
     * @param    tipoPasto    la Missione_tipo_pastoBulk da ricercare
     * @param    clauses    le clausole specificate dall'utente
     */

    public SQLBuilder selectTipo_pastoByClause(UserContext aUC, Missione_dettaglioBulk dettaglioSpesa, Missione_tipo_pastoBulk tipoPasto, CompoundFindClause clauses) throws ComponentException, PersistencyException {
        MissioneBulk missione = dettaglioSpesa.getMissione();

        // 	Se sto recuperando un tipo pasto di un dettaglio appena caricato da db
        //	uso dt_inizio_tappa del dettaglio per recuperare la relativa tappa
        //	perche' non ho selezionato alcun giorno.
        //	Se sto cercando il tipo pasto per un dettaglio che sto creando/modificando
        //	considero il giorno selezionato
        java.sql.Timestamp primoGG = null;
        if (dettaglioSpesa.getCrudStatus() == OggettoBulk.NORMAL)
            primoGG = dettaglioSpesa.getDt_inizio_tappa();
        else
            primoGG = missione.getPrimoGiornoSpesaSelezionato();

        Missione_tappaBulk tappa = (Missione_tappaBulk) missione.getTappeMissioneHash().get(primoGG);

        NazioneHome nazionehome = (NazioneHome) getHome(aUC, NazioneBulk.class);

        tappa.setNazione((NazioneBulk) nazionehome.findByPrimaryKey(tappa.getNazione()));

        return selectTipo_pastoByClause(aUC, tappa.getDt_inizio_tappa(), missione.getPg_rif_inquadramento(), tappa.getNazione(), dettaglioSpesa.getCd_ti_pasto(), clauses);
    }

    public List recuperoTipi_pasto(UserContext aUC, Timestamp dataTappa, Long inquadramento, NazioneBulk nazione, String tipoPasto, CompoundFindClause clauses) throws ComponentException, PersistencyException {
        Missione_tipo_pastoHome tipoPastoHome = (Missione_tipo_pastoHome) getHome(aUC, Missione_tipo_pastoBulk.class);
        return tipoPastoHome.fetchAll(selectTipo_pastoByClause(aUC, dataTappa, inquadramento, nazione, tipoPasto, clauses));
    }

    public SQLBuilder selectTipo_pastoByClause(UserContext aUC, Timestamp dataTappa, Long inquadramento, NazioneBulk nazione, String tipoPasto, CompoundFindClause clauses) throws ComponentException {
        Missione_tipo_pastoHome tipoPastoHome = (Missione_tipo_pastoHome) getHome(aUC, Missione_tipo_pastoBulk.class);
        SQLBuilder sql = tipoPastoHome.createSQLBuilder();

        //sql.addClause("AND","dt_inizio_validita",sql.LESS_EQUALS,missione.getDt_inizio_missione());
        //sql.addClause("AND","dt_fine_validita",sql.GREATER_EQUALS,missione.getDt_inizio_missione());
        sql.addClause("AND", "dt_inizio_validita", sql.LESS_EQUALS, dataTappa);
        sql.addClause("AND", "dt_fine_validita", sql.GREATER_EQUALS, dataTappa);

        sql.openParenthesis("AND");
        if ((nazione != null) && ((NazioneBulk.ITALIA).equals(nazione.getTi_nazione())))
            sql.addClause("AND", "ti_area_geografica", sql.EQUALS, "I");
        else
            sql.addClause("AND", "ti_area_geografica", sql.EQUALS, "E");
        sql.addClause("OR", "ti_area_geografica", sql.EQUALS, "*");
        sql.closeParenthesis();

        sql.openParenthesis("AND");
        sql.addClause("AND", "pg_nazione", sql.EQUALS, nazione.getPg_nazione());
        sql.addClause("OR", "pg_nazione", sql.EQUALS, new Long(0));
        sql.closeParenthesis();

        sql.openParenthesis("AND");
        sql.addClause("AND", "pg_rif_inquadramento", sql.EQUALS, inquadramento);
        sql.addClause("OR", "pg_rif_inquadramento", sql.EQUALS, new Long(0));
        sql.closeParenthesis();

        sql.addClause("AND", "cd_ti_pasto", sql.EQUALS, tipoPasto);

        sql.addClause("AND", "cd_area_estera", sql.EQUALS, nazione.getCd_area_estera());

        sql.addSQLClause("AND", "(cd_ti_pasto || ti_area_geografica || pg_nazione || pg_rif_inquadramento || TO_CHAR(dt_inizio_validita, 'DDMMYYYY') || TO_CHAR(dt_fine_validita, 'DDMMYYYY'))  = " +
                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + " CNRCTB500.getFirstTabMissione('02', cd_ti_pasto, ?, ?, ?, ?)");

        if ((nazione != null) && ((NazioneBulk.ITALIA).equals(nazione.getTi_nazione())))
            sql.addParameter("I", java.sql.Types.CHAR, 6);
        else
            sql.addParameter("E", java.sql.Types.CHAR, 6);
        sql.addParameter(nazione.getPg_nazione(), java.sql.Types.NUMERIC, 7);
        sql.addParameter(inquadramento, java.sql.Types.NUMERIC, 8);
        sql.addParameter(dataTappa, java.sql.Types.TIMESTAMP, 9);

        sql.addPreOrderBy(" cd_ti_pasto, pg_rif_inquadramento desc, pg_nazione desc, ti_area_geografica");

        return sql;
    }

    /**
     * Ricerca tipo spesa
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Ricerca
     * Pre:  L'utente ha richiesto una ricerca dei tipi di spesa per una spesa di una missione
     * Post: Il sistema restitusce il SQLBuilder con le clausole specificate dall'utente e in aggiunta la clausole
     * che il tipo spesa abbia un intervallo di validità comprendente la data di inizio della missione,
     * un'area geografica e una nazione compatibili con quelli della tappa della spesa e un inqudramento uguale a
     * quello selezionato per la missione
     *
     * @return il SQLBuilder con tutte le clausole
     * @throws PersistencyException
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    dettaglioSpesa    la dettaglioSpesa per cui ricercare il tipo pasto
     * @param    tipoSpesa    la Missione_tipo_spesaBulk da ricercare
     * @param    clauses    le clausole specificate dall'utente
     */

    public SQLBuilder selectTipo_spesaByClause(UserContext aUC, Missione_dettaglioBulk dettaglioSpesa, Missione_tipo_spesaBulk tipoSpesa, CompoundFindClause clauses) throws ComponentException, PersistencyException {
        MissioneBulk missione = dettaglioSpesa.getMissione();

        // 	Se sto recuperando un tipo spesa di un dettaglio appena caricato da db
        //	uso dt_inizio_tappa del dettaglio per recuperare la relativa tappa
        //	perche' non ho selezionato alcun giorno.
        //	Se sto cercando la spesa per un dettaglio che sto creando/modificando
        //	considero il giorno selezionato
        java.sql.Timestamp primoGG = null;
        if (dettaglioSpesa.getCrudStatus() == OggettoBulk.NORMAL)
            primoGG = dettaglioSpesa.getDt_inizio_tappa();
        else
            primoGG = missione.getPrimoGiornoSpesaSelezionato();


        Missione_tappaBulk tappa = (Missione_tappaBulk) missione.getTappeMissioneHash().get(primoGG);
        NazioneHome nazionehome = (NazioneHome) getHome(aUC, NazioneBulk.class);

        tappa.setNazione((NazioneBulk) nazionehome.findByPrimaryKey(tappa.getNazione()));

        SQLBuilder sql = selectTipo_spesaByClause(aUC, tappa.getDt_inizio_tappa(), missione.getPg_rif_inquadramento(), tappa.getNazione(), tappa.getFl_rimborso(), dettaglioSpesa.getCd_ti_spesa(), clauses);
        sql.addPreOrderBy(" cd_ti_spesa, pg_rif_inquadramento desc, pg_nazione desc, ti_area_geografica");
        return sql;
    }

    public java.util.List recuperoTipiSpesa(UserContext aUC, Timestamp dataInizioTappa, Long nazione, Long inquadramento, Boolean rimborsoAmmissibile, String cdTipoSpesa) throws ComponentException, PersistencyException {
        NazioneHome nazionehome = (NazioneHome) getHome(aUC, NazioneBulk.class);
        NazioneBulk nazioneBulk = new NazioneBulk(nazione);
        nazioneBulk = (NazioneBulk) nazionehome.findByPrimaryKey(nazioneBulk);

        SQLBuilder sql = selectTipo_spesaByClause(aUC, dataInizioTappa, inquadramento, nazioneBulk, rimborsoAmmissibile, cdTipoSpesa, new CompoundFindClause());
        Missione_tipo_spesaHome tipoSpesaHome = (Missione_tipo_spesaHome) getHome(aUC, Missione_tipo_spesaBulk.class);
        return tipoSpesaHome.fetchAll(sql);
    }

    public SQLBuilder selectTipo_spesaByClause(UserContext aUC, Timestamp dataTappa, Long inquadramento, NazioneBulk nazione, Boolean ammissibileConRimborso, String tipoSpesa, CompoundFindClause clauses) throws ComponentException, PersistencyException {

        Missione_tipo_spesaHome tipoSpesaHome = (Missione_tipo_spesaHome) getHome(aUC, Missione_tipo_spesaBulk.class);
        SQLBuilder sql = tipoSpesaHome.createSQLBuilder();

        sql.addClause(clauses);

        sql.addClause("AND", "dt_inizio_validita", sql.LESS_EQUALS, dataTappa);
        sql.addClause("AND", "dt_fine_validita", sql.GREATER_EQUALS, dataTappa);


        sql.openParenthesis("AND");
        if ((nazione != null) && ((NazioneBulk.ITALIA).equals(nazione.getTi_nazione())))
            sql.addClause("AND", "ti_area_geografica", sql.EQUALS, "I");
        else
            sql.addClause("AND", "ti_area_geografica", sql.EQUALS, "E");
        sql.addClause("OR", "ti_area_geografica", sql.EQUALS, "*");
        sql.closeParenthesis();

        sql.openParenthesis("AND");
        sql.addClause("AND", "pg_nazione", sql.EQUALS, nazione.getPg_nazione());
        sql.addClause("OR", "pg_nazione", sql.EQUALS, new Long(0));
        sql.closeParenthesis();

        sql.openParenthesis("AND");
        sql.addClause("AND", "pg_rif_inquadramento", sql.EQUALS, inquadramento);
        sql.addClause("OR", "pg_rif_inquadramento", sql.EQUALS, new Long(0));
        sql.closeParenthesis();

        if (ammissibileConRimborso)
            sql.addClause("AND", "fl_ammissibile_con_rimborso", sql.EQUALS, ammissibileConRimborso);

        sql.addClause("AND", "cd_ti_spesa", sql.EQUALS, tipoSpesa);

        sql.addSQLClause("AND", "(cd_ti_spesa || ti_area_geografica || pg_nazione || pg_rif_inquadramento || TO_CHAR(dt_inizio_validita, 'DDMMYYYY') || TO_CHAR(dt_fine_validita, 'DDMMYYYY'))  = " +
                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + " CNRCTB500.getFirstTabMissione('01', cd_ti_spesa, ?, ?, ?, ?)");

        if ((nazione != null) && ((NazioneBulk.ITALIA).equals(nazione.getTi_nazione())))
            sql.addParameter("I", java.sql.Types.CHAR, 7);
        else
            sql.addParameter("E", java.sql.Types.CHAR, 7);
        sql.addParameter(nazione.getPg_nazione(), java.sql.Types.NUMERIC, 8);
        sql.addParameter(inquadramento, java.sql.Types.NUMERIC, 9);
        //sql.addParameter(missione.getDt_inizio_missione(), java.sql.Types.TIMESTAMP, 10);
        sql.addParameter(dataTappa, java.sql.Types.TIMESTAMP, 10);

        return sql;
    }

    /**
     * Pre-post-conditions:
     * <p>
     * Nome: Seleziona terzo
     * Pre:  Una richiesta di listare i terzi da cui selezionare quello da utilizzare
     * per la creazione della missione e' stata generata
     * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente e la clausola aggiuntiva che il
     * tipo anagrafico (dipendente/altro) sia uguale a quello specificato per la missione
     *
     * @return il SQLBuilder con tutte le clausole
     * @param    aUC    lo UserContext che ha generato la richiesta
     * @param    missione la MissioneBulk per cui selezionare il terzo
     * @param    aTerzo il V_terzo_per_compensoBulk da ricercare
     * @param    clauses le clausole specificate dall'utente
     */

    public SQLBuilder selectV_terzoByClause(UserContext aUC, MissioneBulk missione, V_terzo_per_compensoBulk aTerzo, CompoundFindClause clauses) throws ComponentException {
        try {
            V_terzo_per_compensoHome home = (V_terzo_per_compensoHome) getHome(aUC, V_terzo_per_compensoBulk.class, "DISTINCT_TERZO");

            java.sql.Timestamp aData = it.cnr.jada.util.DateUtils.truncate(missione.getDt_fine_missione());

            return home.selectVTerzo(missione.getTi_anagrafico(), missione.getCd_terzo(), missione.getDt_registrazione(), aData, clauses);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Inzializzazione divisa e cambio
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: inizializzazione
     * Pre:  L'utente ha richiesto la modifica della nazione di una tappa
     * Post: Il sistema inizializza la tappa impostando come divisa la divisa definita per la nuova nazione
     * e come cambio il cambio della divisa valido per la data di inizio tappa
     *
     * @return MissioneBulk con la tappa inizializzata
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    missione    la MissioneBulk a cui appartiene la tappa
     * @param    tappa    la Missione_tappaBulk da inizializzare
     */

    public MissioneBulk setDivisaCambio(UserContext userContext, MissioneBulk missione, Missione_tappaBulk tappa) throws ComponentException {
        try {

            tappa.setDivisa_tappa(recuperoDivisa(userContext, tappa.getPg_nazione(), missione.getRif_inquadramento().getCd_gruppo_inquadramento(), missione.getDt_inizio_missione()));
            CambioBulk cambio = null;
            if (tappa.getDivisa_tappa() != null) {
                cambio = getCambioDivisa(userContext, tappa.getDivisa_tappa().getCd_divisa(), missione.getDt_inizio_missione());
            }

            if (cambio == null)
                tappa.setCambio_tappa(null);
            else
                tappa.setCambio_tappa(cambio.getCambio());

            return missione;
        } catch (Throwable ex) {
            throw handleException(ex);
        }
    }

    public DivisaBulk recuperoDivisa(UserContext userContext, Long nazione, String gruppoInquadramento, Timestamp dataInizioMissione) throws ComponentException {
        try {
            String cdDivisaDiaria = getDivisaTappaDaDiaria(userContext, nazione, gruppoInquadramento, dataInizioMissione);
            if (cdDivisaDiaria == null)
                throw new ApplicationException("Non trovata la divisa per la tappa !");

            DivisaBulk divisaDiaria = findDivisa(userContext, cdDivisaDiaria);
            if (divisaDiaria == null)
                throw new ApplicationException("Non trovata la divisa per la tappa !");

            return divisaDiaria;
        } catch (Throwable ex) {
            throw handleException(ex);
        }
    }

    public BigDecimal recuperoCambio(UserContext userContext, String divisa, Timestamp dataInizioMissione) throws ComponentException {
        try {
            CambioBulk cambio = getCambioDivisa(userContext, divisa, dataInizioMissione);

            if (cambio == null)
                return null;
            else
                return cambio.getCambio();
        } catch (Throwable ex) {
            throw handleException(ex);
        }
    }

    private CambioBulk getCambioDivisa(UserContext userContext, String divisa,
                                       Timestamp dataInizioMissione) throws ComponentException,
            PersistencyException {
        CambioBulk cambio = null;
        if (divisa == null) {
            return null;
        }
        try {
            cambio = findCambio(userContext, divisa, dataInizioMissione);
        } catch (ApplicationException e) {
        }
        return cambio;
    }

    /**
     * Inzializzazione tappa
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: inizializzazione
     * Pre:  L'utente ha richiesto la creazione di una nuova tappa
     * Post: Il sistema inizializza la nuova tappa impostando la nazione a Italia, la divisa alla divisa definita per l'Italia
     * e il cambio al cambio della divisa valido per la data di inizio tappa
     *
     * @return MissioneBulk con la nuova tappa inizializzata
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    missione    la MissioneBulk a cui appartiene la nuova tappa
     * @param    tappa    la Missione_tappaBulk da inizializzare
     */

    public MissioneBulk setNazioneDivisaCambioItalia(UserContext userContext, MissioneBulk missione, Missione_tappaBulk tappa) throws ComponentException {
        try {
            tappa.setNazione(null);
            tappa.setDivisa_tappa(null);
            tappa.setCambio_tappa(null);

            // Inizializzo nazione ITALIA
            tappa.setNazione(findNazioneItalia(userContext));

            // Inizializzo divisa ITALIA
            if (tappa.getNazione() == null)
                tappa.setDivisa_tappa(null);
            else
                tappa.setDivisa_tappa(getDivisaDefault(userContext));

            // Inizializzo cambio ITALIA
            CambioBulk cambio = findCambio(userContext, tappa.getDivisa_tappa(), missione.getDt_inizio_missione());
            if (cambio == null)
                tappa.setCambio_tappa(null);
            else
                tappa.setCambio_tappa(cambio.getCambio());

            return missione;
        } catch (Throwable ex) {
            throw handleException(ex);
        }
    }

    /**
     * Imposta un savepoint che consente di salvare le modifiche apportate al doc. amministrativo
     * fino a quel momento
     * Pre-post-conditions:
     * <p>
     * Nome: Imposta savePoint
     * Pre:  Una richiesta di impostare un savepoint e' stata generata
     * Post: Un savepoint e' stato impostato in modo che le modifiche apportate al doc. amministrativo vengono consolidate
     *
     * @param    uc    lo UserContext che ha generato la richiesta
     */
    public void setSavePoint(UserContext userContext, String savePointName) throws ComponentException {
        try {
            setSavepoint(userContext, savePointName);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }
    }

    /**
     * stampaConBulk method comment.
     */
    public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

        validateBulkForPrint(aUC, (Stampa_vpg_missioneBulk) bulk);
        return bulk;
    }

    /**
     * Aggiornamento anticipo
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: salvataggio missione + anticio di importo maggiore + compenso
     * Pre:  Il sistema salva una missione collegata ad un anticipo di importo maggiore e
     * ad un compenso
     * Post: Il sistema aggiorna la linea di attivita' dell'anticipo collegato con quella
     * selezionata nel compenso
     *
     * @return
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    missione    la missione il cui anticipo e' da aggiornare
     */

    public void updateAnticipo(UserContext aUC, MissioneBulk missione) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException {
        super.updateBulk(aUC, missione.getAnticipo());
        return;
    }

    /**
     * Aggiornamento compenso
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: aggiornamento compenso in automatico
     * Pre:  Il sistema salva una missione per aggiornarne alcuni campi che
     * comportano l'aggiornamento dei relativi campi del compenso collegato.
     * Post: Il sistema aggiorna il compenso collegato alla missione
     * <p>
     * Nome: Rilettura del compenso modificato
     * Pre:  Il sistema ha aggiornato il compenso
     * Post: Il sistema rilegge la versione del compenso aggiornata
     *
     * @return la missione il cui compenso e' stato aggiornato
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    missione    la missione il cui compenso e' da aggiornare
     */

    public MissioneBulk updateCompenso(UserContext userContext, MissioneBulk missione) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        CompensoHome compensoHome = (CompensoHome) getHome(userContext, CompensoBulk.class);
        missione.getCompenso().setToBeUpdated();
        compensoHome.update(missione.getCompenso(), userContext);

        missione.setCompenso((CompensoBulk) compensoHome.findByPrimaryKey(missione.getCompenso()));
        return missione;
    }

    /**
     * Aggiorna importo associato a doc.amm. della scadenza
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: aggiorna
     * Pre:  L'utente ha selezionato una scadenza di obbligazione per contabilizzare la missione
     * Post: Il sistema aggiorna l'importo associato a documenti amministrativi della scadenza dell'obbligazione su cui
     * la missione e' stata contabilizzata
     *
     * @return l'Obbligazione_scadenzarioBulk con l'importo aggiornato
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    scadenza    l'Obbligazione_scadenzarioBulk per cui modificare l'importo associato a doc.amm.
     */

    private IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(UserContext userContext, IScadenzaDocumentoContabileBulk scadenza) throws ComponentException {
        try {
            ((IScadenzaDocumentoContabileHome) getHome(userContext, scadenza.getClass())).aggiornaImportoAssociatoADocAmm(userContext, scadenza);
        } catch (it.cnr.jada.persistency.PersistencyException exc) {
            throw handleException((OggettoBulk) scadenza, exc);
        } catch (it.cnr.jada.bulk.BusyResourceException exc) {
            throw handleException((OggettoBulk) scadenza, exc);
        } catch (it.cnr.jada.bulk.OutdatedResourceException exc) {
            throw handleException((OggettoBulk) scadenza, exc);
        }

        return scadenza;
    }

    /**
     * Validazione esercizio data registrazione
     * PreCondition:
     * L'esercizio della data registrazione e' antecedente a quello di scrivania
     * (quello della missione) ed e' APERTO
     * PostCondition:
     * La data di registrazione viene validata
     * <p>
     * Validazione esercizio data registrazione
     * PreCondition:
     * L'esercizio della data registrazione e' antecedente a quello di scrivania
     * (quello della missione) e NON e' APERTO
     * PostCondition:
     * La data di registrazione non viene validata
     *
     * @param aUC  lo user context
     * @param bulk l'istanza di  MissioneBulk di cui e' variata la data di registrazione
     */
    public void validaEsercizioDataRegistrazione(UserContext aUC, MissioneBulk missione) throws ComponentException {
        try {
            GregorianCalendar registrazioneGreg = (GregorianCalendar) missione.getGregorianCalendar(missione.getDt_registrazione());

            //	Verifico che l'esercizio della data di registrazione valorizzata dall'utente
            //	sia aperto
            if (!verificaStatoEsercizio(aUC, new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(missione.getCd_cds(), new Integer(registrazioneGreg.get(Calendar.YEAR)))))
                throw new it.cnr.jada.comp.ApplicationException("La data di registrazione non e' in un esercizio aperto !");
        } catch (ApplicationException e) {
            throw handleException(missione, e);
        }
    }

    /**
     * Valida massimale per tipo pasto
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: valida massimale
     * Pre:  E' stata richiesta la validazione di una spesa di tipo pasto
     * Il massimale del tipo pasto e' inferiore o uguale all'importo della spesa
     * Post: La spesa supera la validazione
     * <p>
     * Nome: valida massimale - errore
     * Pre:  E' stata richiesta la validazione di una spesa di tipo pasto
     * Il massimale del tipo pasto della spesa e' superiore all'importo della spesa
     * Post: La spesa non supera la validazione e l'errore viene segnalato all'utente
     *
     * @return MissioneBulk validata
     * @param    aUC    lo UserContext che ha generato la richiesta
     * @param    spesa    la Missione_dettaglioBulk per cui effettuare la validazione
     * @param    divisaDefault    la DivisaBulk di default
     */

    private Missione_dettaglioBulk validaMassimaleTipoPasto(UserContext aUC, Missione_dettaglioBulk spesa, DivisaBulk divisaDefault) throws it.cnr.jada.persistency.PersistencyException, ComponentException, it.cnr.jada.bulk.ValidationException, javax.ejb.EJBException {
        BigDecimal massimalePastoEuro = new BigDecimal(0);

        // MASSIMALE MISSIONE_TIPO_PASTO
        massimalePastoEuro = getMassimaleEuro(
                aUC,
                spesa,
                divisaDefault,
                Optional.ofNullable(spesa)
                    .flatMap(missione_dettaglioBulk -> Optional.ofNullable(missione_dettaglioBulk.getTipo_pasto()))
                    .flatMap(missione_tipo_pastoBulk -> Optional.ofNullable(missione_tipo_pastoBulk.getDivisa()))
                    .flatMap(divisaBulk -> Optional.ofNullable(divisaBulk.getCd_divisa()))
                    .orElseThrow(() -> new ValidationException("Tipo pasto non valorizzato!")),
                Optional.ofNullable(spesa)
                        .flatMap(missione_dettaglioBulk -> Optional.ofNullable(missione_dettaglioBulk.getTipo_pasto()))
                        .flatMap(missione_tipo_pastoBulk -> Optional.ofNullable(missione_tipo_pastoBulk.getLimite_max_pasto()))
                        .orElseThrow(() -> new ValidationException("Tipo pasto non valorizzato!")),
                false);

        // CONFRONTO IMPORTO SPESA CON MASSIMALE TIPO PASTO
        if (massimalePastoEuro.compareTo(spesa.getIm_spesa_euro()) == -1)
            throw new it.cnr.jada.bulk.ValidationException("L'importo della spesa supera il massimale del Tipo Pasto selezionato (€" + massimalePastoEuro + ")");

        return spesa;
    }

    /**
     * Valida massimale per tipo spesa
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: valida massimale
     * Pre:  E' stata richiesta la validazione di una spesa
     * Il massimale del tipo della spesa e' inferiore o uguale all'importo della spesa
     * Post: La spesa supera la validazione
     * <p>
     * Nome: valida massimale - errore
     * Pre:  E' stata richiesta la validazione di una spesa
     * Il massimale del tipo della spesa e' superiore all'importo della spesa
     * Post: La spesa non supera la validazione e l'errore viene segnalato all'utente
     *
     * @return MissioneBulk validata
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    spesa    la Missione_dettaglioBulk per cui effettuare la validazione
     * @param    divisaDefault    la DivisaBulk di default
     */

    private Missione_dettaglioBulk validaMassimaleTipoSpesa(UserContext aUC, Missione_dettaglioBulk spesa, DivisaBulk divisaDefault) throws it.cnr.jada.persistency.PersistencyException, ComponentException, it.cnr.jada.bulk.ValidationException, javax.ejb.EJBException {
        BigDecimal massimaleSpesaEuro = new BigDecimal(0);

        // MASSIMALE MISSIONE_TIPO_SPESA
        massimaleSpesaEuro = getMassimaleEuro(aUC, spesa, divisaDefault, spesa.getTipo_spesa().getCd_divisa(), spesa.getTipo_spesa().getLimite_max_spesa(), true);

        // CONFRONTO IMPORTO SPESA CON MASSIMALE TIPO SPESA
        if (massimaleSpesaEuro.compareTo(spesa.getIm_spesa_euro()) == -1)
            throw new it.cnr.jada.bulk.ValidationException("L'importo della spesa supera il massimale del Tipo Spesa selezionato (€" + massimaleSpesaEuro + ")");

        spesa.setIm_spesa_max(massimaleSpesaEuro);
        spesa.setIm_spesa_max(spesa.getIm_spesa_max().setScale(2, BigDecimal.ROUND_HALF_UP));
        spesa.setIm_spesa_max_divisa(spesa.getTipo_spesa().getLimite_max_spesa());
        spesa.setIm_spesa_max_divisa(spesa.getIm_spesa_max_divisa().setScale(2, BigDecimal.ROUND_HALF_UP));

        return spesa;
    }

    /**
     * Calcolo importi e validazione massimali importi
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: calcola importi spesa per rimborso km
     * Pre:  E' stata richiesto il calcolo degli importi di una spesa per rimborso km
     * Post: Il sistema calcola "im_spesa_euro" moltiplicando i km per l'indennità chilometrica (sempre in EURO).
     * Il sistema inizializza pone : "im_spesa_euro", "im_spesa_divisa" e "im_totale_spesa" tutti uguali.
     * <p>
     * Nome: conversione importo spesa con tipologia diversa da rimborso km
     * Pre:  E' stata richiesta la conversione dell'importo inserito dall'utente di una spesa diversa da rimborso km
     * Post: Se l'utente ha selezionato una divisa straniera, il sistema converte "im_spesa_divisa" nella divisa di default
     * (metodo 'getImportoSpesaEuro')
     * <p>
     * Nome: validazione massimale spesa
     * Pre:  E' stata richiesta la validazione dell'importo della spesa
     * Post: Il sistema verifica che "im_spesa_euro" non sia maggiore del massimale della spesa
     * (limite_max_spesa eventualmente convertito se tipo_spesa.cd_divisa!=EURO), metodo 'validaMassimaleTipoSpesa'
     * <p>
     * Nome: valida spesa di tipo pasto
     * Pre:  E' stata richiesta la validazione dell'importo di una spesa di tipo pasto
     * Post: Il sistema verifica che "im_spesa_euro" non sia maggiore del massimale del pasto.
     * (limite_max_pasto eventualmente convertito se tipo_pasto!=EURO), metodo 'validaMassimaleTipoPasto'
     * <p>
     * Nome: calcolo importo totale spesa
     * Pre:  E' stato richiesto il calcolo del totale della spesa (EURO)
     * Post: Se la spesa e' un trasporto l'"im_totale_spesa" e' uguale alla somma di "im_spesa_euro" con
     * "im_maggiorazione" (eventualmente convertita se l'utente ha selezionato una divisa straniera).
     * Se la spesa non e' un trasporto l'"im_totale_spesa" e' uguale a "im_spesa_euro".
     *
     * @return MissioneBulk validata
     * @param    uc            lo UserContext che ha generato la richiesta
     * @param    missione    la MissioneBulk a cui appartiene la spesa
     * @param    spesa        la Missione_dettaglioBulk per cui effettuare i cacloli e la validazione degli importi
     */

    public MissioneBulk validaMassimaliSpesa(UserContext aUC, MissioneBulk missione, Missione_dettaglioBulk spesa) throws ComponentException, javax.ejb.EJBException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.bulk.ValidationException {
        DivisaBulk divisaDefault = getDivisaDefault(aUC);

        if (divisaDefault == null)
            throw new it.cnr.jada.bulk.ValidationException("Impossibile trovare la divisa di riferimento !");

        // - Se la spesa e' un rimborso_km l'importo e' sempre in EURO (divisa disabilitata)
        //	 e viene calcolato moltiplicando i km per l'indennita_chilometrica (sempre in EURO)
        // - Altrimenti per le altre tipologie di spesa se l'utente ha selezionato una divisa
        //	 diversa da EURO converto im_spesa_divisa in EURO (im_spesa_euro)
        if (spesa.isRimborsoKm())
            spesa.calcolaImportoRimborsoKm();
        else
            spesa.setIm_spesa_euro(spesa.getImportoSpesaEuro(divisaDefault));

        // Verifico che l'importo inserito non superi il massimale
        // del tipo spesa selezionato (massimale della tabella Missione_tipo_spesa)
        spesa = validaMassimaleTipoSpesa(aUC, spesa, divisaDefault);

        if (spesa.isPasto()) {
            // Verifico che l'importo inserito non superi il massimale
            // del tipo pasto selezionato (massimale della tabella Missione_tipo_pasto)
            spesa = validaMassimaleTipoPasto(aUC, spesa, divisaDefault);
        }
        //	Se la spesa e' un trasporto l'im_totale_spesa verra' calcolato sommando a im_spesa_euro
        //	la maggiorazione (eventualmente convertita).
        //	Per le altre tipologie di spesa l'im_totale_spesa e' uguale all'im_spesa_euro
        if (spesa.isTrasporto()) {
            spesa.calcolaMaggiorazioneTrasporto();
            spesa.convertiMaggiorazioneInEuro(divisaDefault);
            spesa.setIm_totale_spesa(spesa.getIm_maggiorazione_euro().add(spesa.getIm_spesa_euro()));
            spesa.setIm_totale_spesa(spesa.getIm_totale_spesa().setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        } else {
            spesa.setIm_totale_spesa(spesa.getIm_spesa_euro());
            spesa.setIm_totale_spesa(spesa.getIm_totale_spesa().setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        }
        return missione;
    }

    /**
     * Viene richiesta la validazione dell'obbligazione associata alla missione
     * <p>
     * Pre-post-conditions
     * <p>
     * Pre: 	La missione e' collegata a compenso o ad anticipo di importo maggiore
     * Post: 	Non esiste alcuna scadenza legata alla missione (non faccio controlli)
     * <p>
     * Nome: 	Scadenza non selezionata
     * Pre: 	Non e' stata selezionata la scadenza da associare alla missione
     * Post: 	Non viene consentita l'associazione della scadenza con la missione
     * Generata una ApplicationException con il messaggio:	"Nessuna obbligazione associata!"
     * <p>
     * Nome: 	Importi obbligazione/scadenza NULLI
     * Pre: 	L'importo della obbligazione e/o della scadenza è nullo
     * Post: 	Non viene consentita l'associazione della scadenza con la missione
     * Generata una ApplicationException con il messaggio:
     * "L'importo dell'obbligazione/scadenza è un dato obbligatorio"
     * <p>
     * Nome: 	Importo scadenza diverso da quello della missione
     * Pre: 	L'importo della scadenza è diverso da quello della missione
     * Post: 	Non viene consentita l'associazione della scadenza con la missione
     * Generata una ApplicationException con il messaggio:
     * "La scadenza di obbligazione associata ha un importo diverso da quello della missione!"
     * <p>
     * Nome: 	Data scadenza NON valida
     * Pre: 	La scadenza selezionata ha una data minore della data di registrazione della missione
     * Post: 	Non viene consentita l'associazione della scadenza con la missione
     * Generata una ApplicationException con il messaggio:
     * "La data della scadenza dell'obbligazione deve essere successiva alla data di registrazione della missione!"
     * <p>
     * Nome: 	Terzo selezionato NON valido
     * Pre: 	Il terzo selezionato è diverso dal terzo della missione oppure il tipo entità NON è DIVERSI
     * Post: 	Non viene consentita l'associazione della scadenza con la missione
     * Generata una ApplicationException con il messaggio:
     * "L'obbligazione deve avere un creditore valido!"
     * <p>
     * Nome: 	Tutte le validazioni precedenti superate
     * Pre: 	L'obbligazione supera tutte le validazioni precedenti
     * Post: 	Viene validata l'associazione della missione con la scadenza di obbligazione
     *
     * @param    userContext        lo UserContext che genera la richiesta
     * @param    scadenza        la scadenza da validare
     **/
    public void validaObbligazione(UserContext userContext, Obbligazione_scadenzarioBulk scadenza, OggettoBulk bulk) throws ComponentException {
        MissioneBulk missione = (MissioneBulk) bulk;

        ObbligazioneBulk obbligazione = scadenza.getObbligazione();

        if (obbligazione.getIm_obbligazione() == null)
            throw new it.cnr.jada.comp.ApplicationException("L'importo dell'impegno è un dato obbligatorio");

        if (scadenza.getIm_scadenza() == null)
            throw new it.cnr.jada.comp.ApplicationException("L'importo della scadenza è un dato obbligatorio");

        if (scadenza.getIm_scadenza().compareTo(missione.getImporto_scadenza_obbligazione()) != 0)
            throw new it.cnr.jada.comp.ApplicationException("L'importo della scadenza di impegno deve essere " + missione.getImporto_scadenza_obbligazione().toString());

        GregorianCalendar gcRegistrazione = (GregorianCalendar) missione.getGregorianCalendar(missione.getDt_registrazione()).clone();
        gcRegistrazione.set(java.util.GregorianCalendar.HOUR_OF_DAY, 0);
        gcRegistrazione.set(java.util.GregorianCalendar.MINUTE, 0);
        gcRegistrazione.set(java.util.GregorianCalendar.SECOND, 0);
        if (gcRegistrazione.getTime().compareTo(scadenza.getDt_scadenza()) >= 0)
            throw new it.cnr.jada.comp.ApplicationException("La data della scadenza dell'impegno deve essere successiva alla data di registrazione della missione!");

        validaTerzoObbligazione(userContext, missione, obbligazione);
        controlloTrovato(userContext, scadenza);
    }

    /**
     * stampaConBulk method comment.
     */
    private void validateBulkForPrint(UserContext userContext, Stampa_vpg_missioneBulk stampa) throws ComponentException {

        try {

            if (stampa.getEsercizio() == null)
                throw new it.cnr.jada.bulk.ValidationException("Il campo ESERCIZIO e' obbligatorio");
            if (stampa.getCd_cds() == null)
                throw new it.cnr.jada.bulk.ValidationException("Il campo CDS e' obbligatorio");

            if (stampa.getPgInizio() == null)
                throw new it.cnr.jada.bulk.ValidationException("Il campo NUMERO INIZIO MISSIONE è obbligatorio");
            if (stampa.getPgFine() == null)
                throw new it.cnr.jada.bulk.ValidationException("Il campo NUMERO FINE MISSIONE è obbligatorio");
            if (stampa.getPgInizio().compareTo(stampa.getPgFine()) > 0)
                throw new it.cnr.jada.bulk.ValidationException("Il NUMERO INIZIO MISSIONE non può essere superiore al NUMERO FINE MISSIONE");

        } catch (it.cnr.jada.bulk.ValidationException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Validazione della Missione per Esercizio COEP precedente
     * <p>
     * Gennaro Borriello/Luisa Farinella - (05/11/2004 18.11.25)
     * Aggiunto controllo per i documenti RIPORTATI, in base all'esercizio COEP precedente
     * all'es. di scrivania.
     */
    private void validateEsercizioCOEP(it.cnr.jada.UserContext userContext, MissioneBulk missione) throws ComponentException {

        LoggableStatement cs = null;
        String status = null;

        try {
            if (missione.isRiportataInScrivania()) {
                Integer es_prec = new Integer(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue() - 1);

                cs = new LoggableStatement(getConnection(userContext), "{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                        + "CNRCTB200.isChiusuraCoepDef(?,?)}", false, this.getClass());
                cs.registerOutParameter(1, java.sql.Types.VARCHAR);
                cs.setObject(2, es_prec);
                cs.setObject(3, missione.getCd_cds());

                cs.executeQuery();

                status = new String(cs.getString(1));

                if (status.compareTo("Y") != 0) {
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile eliminare il documento, poichè l'esercizio economico precedente non è chiuso.");
                }
            }

        } catch (java.sql.SQLException ex) {
            throw new it.cnr.jada.comp.ComponentException(ex);
        } finally {
            try {
                if (cs != null)
                    cs.close();
            } catch (Throwable t) {
                throw new it.cnr.jada.comp.ComponentException(t);
            }
        }
    }

    /**
     * Validazione del Terzo
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: 	Terzo assente
     * Pre: 	Non è stato selezionato un terzo
     * Post: 	Ritorna una ApplicationException con la descrizione dell'errore
     * "Inserire il terzo"
     * <p>
     * Nome: 	Terzo non valido alla data registrazione
     * Pre: 	Il terzo selezionato non è valido alla data registrazione
     * Post: 	Ritorna una ApplicationException con la descrizione dell'errore
     * "Il Terzo selezionato non è valido in Data Registrazione"
     * <p>
     * Nome: 	Modalita di pagamento assente
     * Pre: 	Non è stato selezionata una modalita di pagamento
     * Post: 	Ritorna una ApplicationException con la descrizione dell'errore
     * "Selezionare la Modalità di pagamento"
     * <p>
     * Nome: 	Modalita di pagamento non valida
     * Pre: 	La modalita di pagamento non e' valida (con banca)
     * Post: 	Ritorna una ApplicationException con la descrizione dell'errore
     * "Selezionare una Modalità di pagamento valida"
     * <p>
     * Nome: 	Tipo rapporto assente
     * Pre: 	Non è stato selezionato un tipo rapporto
     * Post: 	Ritorna una ApplicationException con la descrizione dell'errore
     * "Selezionare il Tipo Rapporto"
     * <p>
     * Nome: 	Tipo rapporto non valido alla data inizio missione
     * Pre: 	Il tipo rapporto selezionato non è valido in data inizio missione
     * Post: 	Ritorna una ApplicationException con la descrizione dell'errore
     * "Il Tipo Rapporto selezionato non è valido alla Data Inizio Missione"
     * <p>
     * Nome: 	Tipo trattamento assente
     * Pre: 	Non è stato selezionato un tipo trattamento
     * Post: 	Ritorna una ApplicationException con la descrizione dell'errore
     * "Selezionare il Tipo Trattamento"
     * <p>
     * Nome: 	Tipo trattamento non valido alla data registrazione
     * Pre: 	Il tipo trattamento non e' valido alla data di registrazione
     * Post: 	Ritorna una ApplicationException con la descrizione dell'errore
     * "Il Tipo Trattamento selezionato non è valido alla Data Registrazione"
     * <p>
     * Nome: 	Inquadramento assente
     * Pre: 	Non è stato selezionato un inquadramento
     * Post: 	Ritorna una ApplicationException con la descrizione dell'errore
     * "Selezionare l'Inquadramento"
     * <p>
     * Nome: 	Inquadramento non valido alla data registrazione
     * Pre: 	L'inquadramento non e' valido alla data di registrazione
     * Post: 	Ritorna una ApplicationException con la descrizione dell'errore
     * "Il Tipo Trattamento selezionato non è valido alla Data Registrazione"
     * <p>
     * Nome: 	Terzo valido
     * Pre: 	Il terzo selezionato non ha errori
     * Post: 	Il terzo è valido e prosegue con l'operazione
     *
     * @param    userContext        lo UserContext che genera la richiesta
     * @param    missione        la missione di cui validare il terzo
     **/

// Viene richiesta la validazione del terzo selezionato
// Ritorna una ApplicationException con la descrizione
// dell'errore relativo
//
//	errorCode		Significato
//	=========		===========
//		0			Tutto bene
//		1			Terzo assente
//		2			Terzo non valido alla data registrazione
//		3			Controllo se ho inserito le modalità di pagamento
//		4			Banca non inserita
//		5			Tipo rapporto assente
//		6			Tipo di rapporto non valido in data inizio inizio missione
//		7			Inquadramento assente
//		8			Inquadramento non valido alla data registrazione
//		9			Tipo trattamento assente
//		10			Tipo trattamento non valido alla data registrazione
    public void validaTerzo(UserContext userContext, MissioneBulk missione) throws ComponentException {
        int error = validaTerzo(userContext, missione, true);
        handleExceptionsTerzo(error);
    }

    /**
     * Validazione del Terzo
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: 	Terzo assente
     * Pre: 	Non è stato selezionato un terzo
     * Post:	Ritorna il valore 1
     * <p>
     * Nome: 	Terzo non valido alla data registrazione
     * Pre: 	Il terzo selezionato non è valido alla data registrazione
     * Post: 	Ritorna il valore 2
     * <p>
     * Nome: 	Modalita di pagamento assente
     * Pre: 	Non è stato selezionata una modalita di pagamento
     * Post: 	Ritorna il valore 3
     * <p>
     * Nome: 	Banca non inserita
     * Pre: 	Non è stato selezionato un conto corretto
     * Post: 	Ritorna il valore 4
     * <p>
     * Nome: 	Tipo rapporto assente
     * Pre: 	Non è stato selezionato un tipo rapporto
     * Post: 	Ritorna il valore 5
     * <p>
     * Nome: 	Tipo rapporto non valido alla data inizio missione
     * Pre: 	Il tipo rapporto selezionato non è valido in data inizio missione
     * Post: 	Ritorna il valore 6
     * <p>
     * Nome: 	Inquadramento assente
     * Pre: 	Non è stato selezionato un inquadramento
     * Post: 	Ritorna il valore 7
     * <p>
     * Nome: 	Inquadramento non valido alla data inizio missione
     * Pre: 	L'inquadramento selezionato non è valido in data inizio missione
     * Post: 	Ritorna il valore 8
     * <p>
     * Nome: 	Tipo trattamento assente
     * Pre: 	Non è stato selezionato un tipo trattamento
     * Post: 	Ritorna il valore 9
     * <p>
     * Nome: 	Tipo trattamento non valido alla data registrazione
     * Pre: 	Il tipo trattamento selezionato non è valido in data registrazione
     * Post: 	Ritorna il valore 10
     * <p>
     * Nome: 	Terzo valido
     * Pre: 	Il terzo selezionato non ha errori
     * Post: 	Ritorna il valore 0
     *
     * @param    userContext        lo UserContext che genera la richiesta
     * @param    missione        la missione di cui validare il terzo
     * @param    checkModPag        Flag che stabilisce se occorre validare anche le modalita
     * di pagamento e la banca
     * @return il codice di errore relativo
     **/

//
//	Viene richiesta la validazione del terzo selezionato
//	Ritorna il codice di Errore relativo alla validzione
//
//	errorCode		Significato
//	=========		===========
//		0			Tutto bene
//		1			Terzo assente
//		2			Terzo non valido alla data registrazione
//		3			Controllo se ho inserito le modalità di pagamento
//		4			Banca non inserita
//		5			Tipo rapporto assente
//		6			Tipo di rapporto non valido in data inizio missione
//		7			Inquadramento assente
//		8			Inquadramento non valido alla data registrazione
//		9			Tipo trattamento assente
//		10			Tipo trattamento non valido alla data registrazione
//
    public int validaTerzo(UserContext userContext, MissioneBulk missione, boolean checkModPag) throws ComponentException {
        TerzoBulk terzo = missione.getTerzo();

        // terzo assente
        if (terzo == null)
            return 1;

        // terzo non valido alla data registrazione
        if (terzo.getDt_fine_rapporto() != null && terzo.getDt_fine_rapporto().compareTo(missione.getDt_registrazione()) < 0)
            return 2;

        // Controllo se ho inserito le modalità di pagamento
        if (checkModPag && missione.getModalita_pagamento() == null)
            return 3;

        // banca assente
        if (checkModPag && missione.getBanca() == null)
            return 4;

        // tipo rapporto assente
        if (missione.getTipo_rapporto() == null)
            return 5;

        // rapporto non valido in data inizio missione
        if (!isTipoRapportoValido(userContext, missione))
            return 6;

        // inquadramento assente
        if (missione.getPg_rif_inquadramento() == null)
            return 7;

        // inquadramento non valido alla data inizio missione
        if (!isInquadramentoValido(userContext, missione))
            return 8;

        // tipo trattamento assente
        if (missione.getTipo_trattamento() == null)
            return 9;

        // tipo trattamento non valido alla data registrazione
        if (!isTipoTrattamentoValido(userContext, missione))
            return 10;

        return (0);
    }

    /**
     * Viene controllato che il terzo selezionato nella missione corrisponda con al terzo selezionato
     * nell'obbligazione
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome:	Terzo obbligazione non valido
     * Pre: 	Non è stato selezionato il terzo nell'obbligazione
     * Post: 	Ritorna un ApplicationException con la descrizione dell'errore
     * <p>
     * Nome: 	Terzo obbligazione uguale al terzo della missione
     * Pre: 	Il terzo selezionato nell'obbligazione corrisponde al terzo della missione
     * Post: 	Viene validato il terzo
     * <p>
     * Nome: 	L'anagrafica dell'obbligazione ha tipo entita DIVERSI
     * Pre: 	L'anagafica associata al terzo dell'obbligazione ha come tipo entita DIVERSI
     * Post: 	Viene validato il terzo
     * <p>
     * Nome: 	Nessuna delle due condizioni precedenti è verificata
     * Pre: 	Il terzo selezionato NON corrisponde al terzo della missione e
     * l'anagrafica associata NON ha tipo entita DIVERSI
     * Post: 	Ritorna un ApplicationException con la descrizione dell'errore
     *
     * @param    userContext        lo UserContext che genera la richiesta
     * @param    missione        la missione di cui validare il terzo
     * @param    obblig            l'obbligazione di cui validare il terzo
     **/
    private void validaTerzoObbligazione(UserContext userContext, MissioneBulk missione, ObbligazioneBulk obbligazione) throws ComponentException {
        try {
            TerzoBulk creditore = obbligazione.getCreditore();
            TerzoHome terzoHome = (TerzoHome) getHome(userContext, TerzoBulk.class);

            creditore = (TerzoBulk) terzoHome.findByPrimaryKey(obbligazione.getCreditore());
            if (creditore == null || creditore.getCd_terzo() == null)
                throw new it.cnr.jada.comp.ApplicationException("L'impegno deve avere un creditore valido!");
            getHomeCache(userContext).fetchAll(userContext);

            AnagraficoHome anaHome = (AnagraficoHome) getHome(userContext, AnagraficoBulk.class);
            AnagraficoBulk anagrafico = (AnagraficoBulk) anaHome.findByPrimaryKey(creditore.getAnagrafico());
            if (!missione.getTerzo().equalsByPrimaryKey(creditore) && !AnagraficoBulk.DIVERSI.equalsIgnoreCase(anagrafico.getTi_entita()))
                throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata deve appartenere ad un'obbligazione che ha come creditore quello della missione!");
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public boolean verificaStatoEsercizio(UserContext userContext, it.cnr.contab.config00.esercizio.bulk.EsercizioBulk anEsercizio) throws ComponentException {
        try {
            it.cnr.contab.config00.esercizio.bulk.EsercizioHome eHome = (it.cnr.contab.config00.esercizio.bulk.EsercizioHome) getHome(userContext, it.cnr.contab.config00.esercizio.bulk.EsercizioBulk.class);

            return !eHome.isEsercizioChiuso(userContext, anEsercizio.getEsercizio(), anEsercizio.getCd_cds());
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    public boolean isDiariaEditable(UserContext context, Missione_tappaBulk tappa) throws it.cnr.jada.comp.ComponentException {

        Parametri_cnrBulk parametriCnr = parametriCnr(context);

        //Calendar cal = Calendar.getInstance();
        //cal.setTime(tappa.getDt_inizio_tappa());
        //int year = cal.get(Calendar.YEAR);

        java.sql.Timestamp data_fine_diaria_miss_estero;
        try {
            Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
            data_fine_diaria_miss_estero = sess.getDt01(context, new Integer(0), "*", "DIARIA_MISS_ESTERO", "DATA_FINE");
        } catch (RemoteException e) {
            throw handleException(e);
        }
        if (tappa.getFl_comune_estero().booleanValue()) {
            if ((!(tappa.getDt_inizio_tappa().compareTo(data_fine_diaria_miss_estero) > 0)) ||
                    (tappa.getMissione().getTerzo().getAnagrafico().getFl_abilita_diaria_miss_est().booleanValue()
                            &&
                            !(tappa.getMissione().getDt_inizio_missione().compareTo(tappa.getMissione().getTerzo().getAnagrafico().getDt_inizio_diaria_miss_est()) < 0)
                            &&
                            !(tappa.getMissione().getDt_fine_missione().compareTo(tappa.getMissione().getTerzo().getAnagrafico().getDt_fine_diaria_miss_est()) > 0)
                    )
                    ) {
                return true;
            } else {
                return (false);
            }
        } else {
            if (!parametriCnr.getFl_diaria_miss_italia().booleanValue())
			/*
			&&
			year == it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context).intValue())
			*/ {
                return (false);
            } else
                return (true);
        }
    }

    /**
     * Ricerca i parametri ente per l'anno di esercizio in scrivania
     *
     * @param aUC UserContext
     * @return Parametri_cnrBulk contenente i parametri ente
     * @throws it.cnr.jada.comp.ComponentException
     */
    public Parametri_cnrBulk parametriCnr(UserContext aUC) throws it.cnr.jada.comp.ComponentException {
        Parametri_cnrBulk param;
        try {
            param = (Parametri_cnrBulk) getHome(aUC, Parametri_cnrBulk.class).findByPrimaryKey(
                    new Parametri_cnrBulk(
                            ((CNRUserContext) aUC).getEsercizio()));
        } catch (PersistencyException ex) {
            throw handleException(ex);
        } catch (ComponentException ex) {
            throw handleException(ex);
        }
        if (param == null) {
            throw new ApplicationException("Parametri CNR non trovati.");
        }
        return param;
    }

    public boolean isTerzoCervellone(UserContext userContext, MissioneBulk missione) throws ComponentException {
        if (missione.getCd_terzo() != null) {
            try {
                TerzoHome tHome = (TerzoHome) getHome(userContext, TerzoBulk.class);
                TerzoBulk tKey = new TerzoBulk(missione.getCd_terzo());
                TerzoBulk t = (TerzoBulk) tHome.findByPrimaryKey(tKey);
                if (t == null) {
                    return false;
                } else {
                    AnagraficoHome aHome = (AnagraficoHome) getHome(userContext, AnagraficoBulk.class);
                    AnagraficoBulk aKey = new AnagraficoBulk(t.getCd_anag());
                    AnagraficoBulk a = (AnagraficoBulk) aHome.findByPrimaryKey(aKey);

                    if (a.getFl_cervellone())
                        return true;
                    else
                        return false;
                }
            } catch (it.cnr.jada.persistency.PersistencyException ex) {
                throw handleException(missione, ex);
            }
        } else
            return false;

    }

    public java.util.List findListaMissioniSIP(UserContext userContext, String query, String dominio, String uo,
                                               String terzo, String voce, String cdr, String gae,
                                               String tipoRicerca, Timestamp data_inizio, Timestamp data_fine) throws ComponentException {
        try {

            VMissioneSIPHome home = (VMissioneSIPHome) getHome(userContext, VMissioneSIPBulk.class, "VMISSIONESIP_RID");
            SQLBuilder sql = home.createSQLBuilder();
            sql.setDistinctClause(true);
            if (data_inizio != null && data_fine != null) {
                sql.addSQLClause("AND", "DT_PAGAMENTO", SQLBuilder.GREATER_EQUALS, data_inizio);
                sql.addSQLClause("AND", "DT_PAGAMENTO", SQLBuilder.LESS_EQUALS, data_fine);
            }
            if (uo != null)
                sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uo);
            if (terzo != null)
                sql.addSQLClause("AND", "CD_TERZO", SQLBuilder.EQUALS, terzo);
            if (voce != null)
                sql.addSQLClause("AND", "CD_ELEMENTO_VOCE", SQLBuilder.EQUALS, voce);
            if (cdr != null)
                sql.addSQLClause("AND", "CD_CENTRO_RESPONSABILITA", SQLBuilder.EQUALS, cdr);
            if (gae != null)
                sql.addSQLClause("AND", "GAE", SQLBuilder.EQUALS, gae);
            if (dominio.equalsIgnoreCase("pg_missione"))
                sql.addSQLClause("AND", "PG_MISSIONE", SQLBuilder.EQUALS, query);
            else if (dominio.equalsIgnoreCase("descrizione")) {
                for (StringTokenizer stringtokenizer = new StringTokenizer(query, " "); stringtokenizer.hasMoreElements(); ) {
                    String queryDetail = stringtokenizer.nextToken();
                    if ((tipoRicerca != null && tipoRicerca.equalsIgnoreCase("selettiva")) || tipoRicerca == null) {
                        if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail)))
                            sql.addSQLClause("AND", "DS_MISSIONE", SQLBuilder.CONTAINS, queryDetail);
                        else {
                            sql.openParenthesis("AND");
                            sql.addSQLClause("OR", "DS_MISSIONE", SQLBuilder.CONTAINS, queryDetail);
                            sql.addSQLClause("OR", "DS_MISSIONE", SQLBuilder.CONTAINS, RemoveAccent.convert(queryDetail));
                            sql.closeParenthesis();
                        }
                    } else if (tipoRicerca.equalsIgnoreCase("puntuale")) {
                        if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail))) {
                            sql.openParenthesis("AND");
                            sql.addSQLClause("AND", "UPPER(DS_MISSIONE)", SQLBuilder.EQUALS, queryDetail.toUpperCase());
                            sql.addSQLClause("OR", "DS_MISSIONE", SQLBuilder.STARTSWITH, queryDetail + " ");
                            sql.addSQLClause("OR", "DS_MISSIONE", SQLBuilder.ENDSWITH, " " + queryDetail);
                            sql.closeParenthesis();
                        } else {
                            sql.openParenthesis("AND");
                            sql.openParenthesis("AND");
                            sql.addSQLClause("OR", "UPPER(DS_MISSIONE)", SQLBuilder.EQUALS, queryDetail.toUpperCase());
                            sql.addSQLClause("OR", "UPPER(DS_MISSIONE)", SQLBuilder.EQUALS, RemoveAccent.convert(queryDetail).toUpperCase());
                            sql.closeParenthesis();
                            sql.openParenthesis("OR");
                            sql.addSQLClause("OR", "DS_MISSIONE", SQLBuilder.STARTSWITH, queryDetail + " ");
                            sql.addSQLClause("OR", "DS_MISSIONE", SQLBuilder.STARTSWITH, RemoveAccent.convert(queryDetail) + " ");
                            sql.closeParenthesis();
                            sql.openParenthesis("OR");
                            sql.addSQLClause("OR", "DS_MISSIONE", SQLBuilder.ENDSWITH, " " + queryDetail);
                            sql.addSQLClause("OR", "DS_MISSIONE", SQLBuilder.ENDSWITH, " " + RemoveAccent.convert(queryDetail));
                            sql.closeParenthesis();
                            sql.closeParenthesis();
                        }
                    }
                }
                sql.addOrderBy("DS_MISSIONE");
            }
            return home.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public void archiviaStampa(UserContext userContext, Date fromDate, Date untilDate, MissioneBulk missioneBulk, Integer... years) throws ComponentException {
        MissioneHome missioneHome = (MissioneHome) getHome(userContext, MissioneBulk.class);
        CompoundFindClause clauses = new CompoundFindClause();
        CompoundFindClause clausesYear = new CompoundFindClause();
        if (fromDate != null)
            clauses.addClause(FindClause.AND, "dt_inizio_missione", SQLBuilder.GREATER_EQUALS, new Timestamp(fromDate.getTime()));
        if (untilDate != null)
            clauses.addClause(FindClause.AND, "dt_fine_missione", SQLBuilder.LESS_EQUALS, new Timestamp(untilDate.getTime()));
        clauses.addClause(FindClause.AND, "ti_provvisorio_definitivo", SQLBuilder.EQUALS, "D");
        for (Integer year : years) {
            clausesYear.addClause(FindClause.OR, "esercizio", SQLBuilder.EQUALS, year);
        }
        clauses = CompoundFindClause.and(clauses, clausesYear);
        try {
            RemoteIterator missioni = cerca(userContext, clauses, missioneBulk);
            while (missioni.hasMoreElements()) {
                MissioneBulk missione = (MissioneBulk) missioni.nextElement();
                try {
                    missione = (MissioneBulk) inizializzaBulkPerModifica(userContext, missione);
                    missioneHome.archiviaStampa(userContext, missione);
                } catch (Exception ex) {
                    System.err.println("Missione:" + missione + " - " + ex);
                    continue;
                }
            }
        } catch (RemoteException e) {
            throw handleException(e);
        }
    }

    public BigDecimal calcolaMinutiTappa(UserContext aUC, Missione_tappaBulk tappa) throws ComponentException {
        LoggableStatement cs = null;
        java.sql.ResultSet rs;
        BigDecimal ore = new BigDecimal("0");
        try {
            try {
                cs = new LoggableStatement(getConnection(aUC), "{?=call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                        + "CNRCTB500.calcolaMinutiTappa(?,?)}", false, this.getClass());
                cs.registerOutParameter(1, java.sql.Types.DECIMAL);
                cs.setObject(2, tappa.getDt_inizio_tappa());
                cs.setObject(3, tappa.getDt_fine_tappa());
                cs.executeQuery();
                ore = cs.getBigDecimal(1);

            } finally {
                cs.close();
            }
            return ore;
        } catch (java.sql.SQLException e) {
            throw handleException(tappa, e);
        }
    }
    public void cancellazioneMissioneDaGemis(UserContext userContext, Long idRimborsoMissioneGemis) throws ComponentException {
        try {
    		MissioneHome home = (MissioneHome) getHome(userContext, MissioneBulk.class);
    		MissioneBulk missione = home.loadMissione(userContext, idRimborsoMissioneGemis);
    		if (missione != null){
                logger.info("Missione Recuperata "+missione.getPg_missione());
                missione = (MissioneBulk) inizializzaBulkPerModifica(userContext, missione);
                logger.info("Missione Inizializzata "+missione.getPg_missione());
                missione.setToBeDeleted();
                eliminaConBulk(userContext, missione);
                logger.info("Missione Eliminata "+missione.getPg_missione());
    		}
        } catch (Throwable e) {
            throw handleException(e);
        }

    }


}
