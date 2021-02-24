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

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBase;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneHome;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk;
import it.cnr.contab.compensi00.docs.bulk.ConguaglioHome;
import it.cnr.contab.config00.bulk.Codici_siopeBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.DocumentoGenericoComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession;
import it.cnr.contab.doccont00.ejb.AccertamentoComponentSession;
import it.cnr.contab.doccont00.ejb.ReversaleComponentSession;
import it.cnr.contab.doccont00.ejb.SaldoComponentSession;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.doccont00.tabrif.bulk.CupBulk;
import it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk;
import it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloHome;
import it.cnr.contab.missioni00.docs.bulk.AnticipoBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.preventvar00.bulk.Var_bilancioBulk;
import it.cnr.contab.preventvar00.bulk.Var_bilancioHome;
import it.cnr.contab.preventvar00.ejb.VarBilancioComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.Utente_indirizzi_mailBulk;
import it.cnr.contab.utenze00.bulk.Utente_indirizzi_mailHome;
import it.cnr.contab.util.ApplicationMessageFormatException;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util.enumeration.EsitoOperazione;
import it.cnr.contab.util.enumeration.StatoVariazioneSostituzione;
import it.cnr.contab.util00.ejb.ProcedureComponentSession;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.comp.IPrintMgr;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.ejb.EJBCommonServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MandatoComponent extends it.cnr.jada.comp.CRUDComponent implements
        IMandatoMgr, ICRUDMgr, IPrintMgr, Cloneable, Serializable {

    public final static String INSERIMENTO_MANDATO_ACTION = "I";
    public final static String ANNULLAMENTO_MANDATO_ACTION = "A";
    public final static String MODIFICA_MANDATO_ACTION = "M";

    public final static String VSX_PROC_NAME = "CNRCTB037.vsx_man_acc";
    private transient static final Logger logger = LoggerFactory.getLogger(MandatoComponent.class);
    public static final int DS_MANDATO_MAX_LENGTH = 300;

    // @@<< CONSTRUCTORCST
    public MandatoComponent() {
        // >>

        // << CONSTRUCTORCSTL
        /* Default constructor */
        // >>

        // << CONSTRUCTORCSTT

    }

    /**
     * creazione riga PreCondition: E' stata creata una nuova riga di mandato
     * PostCondition: Vengono recuperati dai dettagli della scadenza
     * dell'obbligazione, associata alla riga del mandato, le voci del piano i
     * cui saldi devono essere incrementati e viene richiesto alla component che
     * gestisce i saldi di effettuare l'aggiornamento delle voci e di verificare
     * la disponibilità di cassa per ogni voce modifica riga - annullamento
     * PreCondition: E' stata modificata una riga di mandato e lo stato del
     * mandato e' annullato PostCondition: Vengono recuperati dai dettagli della
     * scadenza dell'obbligazione, associata alla riga del mandato, le voci del
     * piano i cui saldi devono essere decrementati e viene richiesto alla
     * component che gestisce i saldi di effettuare l'aggiornamento delle voci
     * modifica riga - modifica modalità pagamento PreCondition: E' stata
     * modificata una riga di mandato, ma il suo importo non e' stato modificato
     * PostCondition: Nessun aggiornamento all'importo dei saldi viene
     * effettuato
     *
     * @param aUC     lo <code>UserContext</code> che ha generato la richiesta
     * @param riga    <code>Mandato_rigaBulk</code> la riga del mandato da
     *                modificare
     * @param session <code>SaldoComponentSession</code>
     */

    /*
     * esegue il check di disponibilita di cassa e aggiorna il capitolo saldo
     * **Mandato Accreditamento ***** riga.crudStatus = - TO_BE_CREATED alla
     * creazione - TO_BE_UPDATED all' annullamento (2) o alla modifica delle
     * modalità di pagamento (3) - TO_BE_DELETED mai**Mandato Pagamento *****
     * riga.crudStatus = - TO_BE_CREATED alla creazione - TO_BE_UPDATED
     * all'annullamento (2) o alla modifica delle modalità di pagamento (3) -
     * TO_BE_DELETED mai
     */
    protected void aggiornaCapitoloSaldoRiga(UserContext aUC,
                                             Mandato_rigaBulk riga, SaldoComponentSession session)
            throws ComponentException {
        try {
            Mandato_rigaBulk rigaDaDB = null;
            Voce_fBulk voce;
            java.math.BigDecimal importo, prc;
            boolean flConsumo;
            if (!riga.isToBeCreated() && !riga.isToBeUpdated()
                    && !riga.isToBeDeleted())
                return;
            if (riga.isToBeUpdated() || riga.isToBeDeleted()) { // rileggo la
                // riga dal db
                // per vedere se
                // e' stato
                // modificato
                // l'importo
                // (29/10/2003 12.40.38) Giorgio Massussi
                // Sostituito getHome() con getTempHome() perchè se arrivo da
                // annullaMandato può essere che la
                // riga del mandato sia stata appena caricata da db e
                // successivamente modificata; se uso
                // la stessa HomeCache la rilettura mi seppellisce le modifiche!
                rigaDaDB = (Mandato_rigaBulk) getTempHome(aUC, riga.getClass())
                        .findByPrimaryKey(riga);
                // non e' stato modificato l'importo della riga e non si tratta
                // di un annullamento --> non aggiorno i saldi
                if (riga.isToBeUpdated()
                        && !riga.isFl_aggiorna_saldi_per_annullamento() && // caso
                        // 3
                        rigaDaDB.getIm_mandato_riga().compareTo(
                                riga.getIm_mandato_riga()) == 0)
                    return;
            }
            /* ricerco l'obbligazione */
            ObbligazioneBulk obbligazione = (ObbligazioneBulk) getHome(aUC,
                    ObbligazioneBulk.class).findByPrimaryKey(
                    new ObbligazioneBulk(riga.getCd_cds(), riga
                            .getEsercizio_obbligazione(), riga
                            .getEsercizio_ori_obbligazione(), riga
                            .getPg_obbligazione()));
            /* ricerco la scadenza */
            Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) getHome(
                    aUC, Obbligazione_scadenzarioBulk.class).findByPrimaryKey(
                    new Obbligazione_scadenzarioBulk(riga.getCd_cds(), riga
                            .getEsercizio_obbligazione(), riga
                            .getEsercizio_ori_obbligazione(), riga
                            .getPg_obbligazione(), riga
                            .getPg_obbligazione_scadenzario()));
            /*
             * /* calcolo la percentuale di ripartizione if (
             * scadenza.getIm_scadenza().compareTo( new BigDecimal(0)) != 0 )
             * prc = riga.getIm_mandato_riga().divide(
             * scadenza.getIm_scadenza(), 8,BigDecimal.ROUND_HALF_UP); else
             * prc = new java.math.BigDecimal(0);
             */

            /* verifico se sono a consumo o a copertura completa */
            flConsumo = scadenza.getIm_scadenza().compareTo(riga.getIm_mandato_riga()) != 0;
            /* ricerco le scad_voce */
            Obbligazione_scad_voceBulk osv = new Obbligazione_scad_voceBulk();
            osv.setEsercizio(riga.getEsercizio_obbligazione());
            osv.setCd_cds(riga.getCd_cds());
            osv.setEsercizio_originale(riga.getEsercizio_ori_obbligazione());
            osv.setPg_obbligazione(riga.getPg_obbligazione());
            osv.setPg_obbligazione_scadenzario(riga
                    .getPg_obbligazione_scadenzario());
            List result = getHome(aUC, Obbligazione_scad_voceBulk.class).find(
                    osv);
            // per ogni scad_voce recupero il capitolo
            for (Iterator i = result.iterator(); i.hasNext(); ) {
                osv = (Obbligazione_scad_voceBulk) i.next();
                if (flConsumo)
                    importo = riga.getIm_mandato_riga();
                else
                    importo = osv.getIm_voce();
                /*
                 * importo = osv.getIm_voce().multiply( prc );
                 */
                voce = new Voce_fBulk(osv.getCd_voce(), osv.getEsercizio(), osv
                        .getTi_appartenenza(), osv.getTi_gestione());
                if (riga.isToBeDeleted()) {
                    session.aggiornaMandatiReversali(aUC, voce,
                            osv.getCd_cds(), importo.negate(), riga
                                    .getMandato().getTi_competenza_residuo());
                    /*
                     * Aggiorno i Saldi per CDR/Linea
                     */
                    session
                            .aggiornaMandatiReversali(
                                    aUC,
                                    osv.getCd_centro_responsabilita(),
                                    osv.getCd_linea_attivita(),
                                    voce,
                                    obbligazione.getEsercizio_originale(),
                                    importo.negate(),
                                    obbligazione
                                            .isObbligazioneResiduoImproprio() ? Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO
                                            : Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO);
                } else if (riga.isToBeUpdated()) {
                    if (riga.isFl_aggiorna_saldi_per_annullamento()) { // caso 2
                        session.aggiornaMandatiReversali(aUC, voce, osv
                                .getCd_cds(), importo.negate(), riga
                                .getMandato().getTi_competenza_residuo());
                        /*
                         * Aggiorno i Saldi per CDR/Linea
                         */
                        session
                                .aggiornaMandatiReversali(
                                        aUC,
                                        osv.getCd_centro_responsabilita(),
                                        osv.getCd_linea_attivita(),
                                        voce,
                                        obbligazione.getEsercizio_originale(),
                                        importo.negate(),
                                        obbligazione
                                                .isObbligazioneResiduoImproprio() ? Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO
                                                : Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO);
                    }
                } else if (riga.isToBeCreated()) {
                    session.aggiornaMandatiReversali(aUC, voce,
                            osv.getCd_cds(), importo, riga.getMandato()
                                    .getTi_competenza_residuo(), !riga
                                    .getFl_pgiro().booleanValue());
                    /*
                     * Aggiorno i Saldi per CDR/Linea
                     */
                    session
                            .aggiornaMandatiReversali(
                                    aUC,
                                    osv.getCd_centro_responsabilita(),
                                    osv.getCd_linea_attivita(),
                                    voce,
                                    obbligazione.getEsercizio_originale(),
                                    importo,
                                    obbligazione
                                            .isObbligazioneResiduoImproprio() ? Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO
                                            : Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO,
                                    !riga.getFl_pgiro().booleanValue());
                }
            }
            riga.setFl_aggiorna_saldi_per_annullamento(false);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * creazione riga PreCondition: E' stata creata una nuova riga di mandato
     * PostCondition: Viene incrementato l'importo associato ai documenti
     * contabili della scadenza di obbligazione pagata con la riga del mandato
     * dell'importo della riga del mandato (scadenza.im_associato_doc_contabile
     * = scadenza.im_associato_doc_contabile + mandato_riga.im_mandato_riga)
     * modifica riga - annullamento PreCondition: E' stata modificata una riga
     * di mandato e lo stato del mandato e' annullato PostCondition: Viene
     * decrementato l'importo associato ai documenti contabili della scadenza di
     * obbligazione pagata con la riga del mandato dell'importo della riga del
     * mandato (scadenza.im_associato_doc_contabile =
     * scadenza.im_associato_doc_contabile - mandato_riga.im_mandato_riga)
     * modifica riga - modifica modalità pagamento PreCondition: E' stata
     * modificata una riga di mandato, ma il suo importo non e' stato modificato
     * PostCondition: Nessun aggiornamento all'importo della scadenza viene
     * effettuato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riga        <code>Mandato_rigaBulk</code> la riga del mandato da
     *                    modificare
     * @param scadenza    <code>Obbligazione_scadenzarioBulk</code> la scadenza
     *                    dell'obbligazione pagata dalla riga del mandato
     */
    private void aggiornaImportoObbligazionePerRiga(UserContext userContext,
                                                    Mandato_rigaBulk riga, Obbligazione_scadenzarioBulk scadenza)
            throws it.cnr.jada.persistency.PersistencyException,
            ComponentException {
        /*
         * **Mandato Accreditamento ***** riga.crudStatus = - TO_BE_CREATED alla
         * creazione //- TO_BE_UPDATED alla modifica dell'importo (1) o
         * all'annullamento (2) o alla modifica delle modalità di pagamento (3)
         * - TO_BE_UPDATED all'annullamento (2) o alla modifica delle modalità
         * di pagamento (3) - TO_BE_DELETED mai**Mandato Pagamento *****
         * riga.crudStatus = - TO_BE_CREATED alla creazione - TO_BE_UPDATED
         * all'annullamento (2) o alla modifica delle modalità di pagamento (3)
         * - TO_BE_DELETED mai
         */

        if (riga.isToBeCreated())
            scadenza.setIm_associato_doc_contabile(scadenza
                    .getIm_associato_doc_contabile().add(
                            riga.getIm_mandato_riga()));
        else if (riga.isToBeDeleted()) {
            // (29/10/2003 12.40.38) Giorgio Massussi
            // Sostituito getHome() con getTempHome() perchè se arrivo da
            // annullaMandato può essere che la
            // riga del mandato sia stata appena caricata da db e
            // successivamente modificata; se uso
            // la stessa HomeCache la rilettura mi seppellisce le modifiche!
            Mandato_rigaBulk rigaDaDB = (Mandato_rigaBulk) getTempHome(
                    userContext, riga.getClass()).findByPrimaryKey(riga);
            scadenza.setIm_associato_doc_contabile(scadenza
                    .getIm_associato_doc_contabile().subtract(
                            rigaDaDB.getIm_mandato_riga()));
        } else if (riga.isToBeUpdated()) {
            java.math.BigDecimal importo = null;
            // (29/10/2003 12.40.38) Giorgio Massussi
            // Sostituito getHome() con getTempHome() perchè se arrivo da
            // annullaMandato può essere che la
            // riga del mandato sia stata appena caricata da db e
            // successivamente modificata; se uso
            // la stessa HomeCache la rilettura mi seppellisce le modifiche!
            Mandato_rigaBulk rigaDaDB = (Mandato_rigaBulk) getTempHome(
                    userContext, riga.getClass()).findByPrimaryKey(riga);
            if (riga.getMandato().getStato().equals(
                    MandatoBulk.STATO_MANDATO_ANNULLATO)) // caso 2 -
                // annullamento
                importo = rigaDaDB.getIm_mandato_riga().negate();
            else if (riga.getIm_mandato_riga().compareTo(
                    rigaDaDB.getIm_mandato_riga()) == 0) // caso 3 - no modifica
                // importo
                return;
            /*
             * else //caso 1 - modifica importo importo =
             * riga.getIm_mandato_riga
             * ().subtract(rigaDaDB.getIm_mandato_riga());
             */
            scadenza.setIm_associato_doc_contabile(scadenza
                    .getIm_associato_doc_contabile().add(importo));
        }
    }

    /**
     * aggiorno importo scadenze obbligazione PreCondition: E' stata
     * creato/annullato un mandato ed e' pertanto necessario modificare
     * l'importo associtao a doc. contabili di tutte le scadenze di obbligazione
     * pagate dal mandato PostCondition: Per ogni riga del mandato viene
     * recuperata la scadenza di obbligazione collegata e viene calcolato
     * l'importo dell'aggiornamento ( metodo
     * aggiornaImportoObbligazionePerRiga); per ogni scadenza di obbligazione
     * presente nel mandato, dopo aver messo un lock sulla testata
     * dell'obbligazione, ne viene aggiornato l'importo associato a
     * doc.contabile
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> il Mandato per cui aggiornare le
     *                    scadenze dell'obbligazione
     */
    protected void aggiornaImportoObbligazioni(UserContext userContext,
                                               MandatoBulk mandato) throws ComponentException {

        try {
            PrimaryKeyHashMap obbligazioniTable = new PrimaryKeyHashMap();
            PrimaryKeyHashMap obblScadTable = new PrimaryKeyHashMap();
            Mandato_rigaBulk riga;
            Obbligazione_scadenzarioBulk scadenza;
            ObbligazioneBulk obbligazione;

            for (Iterator i = mandato.getMandato_rigaColl().iterator(); i
                    .hasNext(); ) {
                riga = (Mandato_rigaBulk) i.next();
                if (!riga.isToBeCreated() && !riga.isToBeUpdated()
                        && !riga.isToBeDeleted())
                    continue;
                // scadenza
                scadenza = new Obbligazione_scadenzarioBulk(riga.getCd_cds(),
                        riga.getEsercizio_obbligazione(), riga
                        .getEsercizio_ori_obbligazione(), riga
                        .getPg_obbligazione(), riga
                        .getPg_obbligazione_scadenzario());
                if (obblScadTable.get(scadenza) == null) {
                    // leggo la scadenza da db
                    scadenza = (Obbligazione_scadenzarioBulk) getHome(
                            userContext, Obbligazione_scadenzarioBulk.class)
                            .findAndLock(scadenza);
                    obblScadTable.put(scadenza, scadenza);
                } else
                    // scadenza già letto da db
                    scadenza = (Obbligazione_scadenzarioBulk) obblScadTable
                            .get(scadenza);
                if (scadenza == null)
                    throw new ApplicationException("Non esiste la scadenza");
                // obbligazione
                obbligazione = scadenza.getObbligazione();
                if (obbligazioniTable.get(obbligazione) == null) {
                    // leggo l'obbligazione da db
                    obbligazione = (ObbligazioneBulk) getHome(userContext,
                            ObbligazioneBulk.class).findAndLock(obbligazione);
                    obbligazioniTable.put(obbligazione, obbligazione);
                } else
                    // scadenza già letto da db
                    obbligazione = (ObbligazioneBulk) obbligazioniTable
                            .get(obbligazione);

                aggiornaImportoObbligazionePerRiga(userContext, riga, scadenza);
            }
            for (Iterator i = obbligazioniTable.values().iterator(); i
                    .hasNext(); ) {
                obbligazione = (ObbligazioneBulk) i.next();
                // obbligazione.setUser( userContext.getUser());
                // updateBulk( userContext, obbligazione);
                lockBulk(userContext, obbligazione);
            }
            for (Iterator i = obblScadTable.values().iterator(); i.hasNext(); ) {
                scadenza = (Obbligazione_scadenzarioBulk) i.next();
                scadenza.setUser(userContext.getUser());
                if (scadenza.getIm_associato_doc_contabile().compareTo(
                        scadenza.getIm_associato_doc_amm()) > 0
                        || scadenza.getIm_associato_doc_contabile().compareTo(
                        scadenza.getIm_scadenza()) > 0)
                    throw new ApplicationException(
                            "La scadenza "
                                    + " con esercizio: "
                                    + scadenza.getEsercizio()
                                    + " Cds: "
                                    + scadenza.getCd_cds()
                                    + " Esercizio impegno: "
                                    + scadenza.getEsercizio_originale()
                                    + " Pg impegno: "
                                    + scadenza.getPg_obbligazione()
                                    + " Pg scadenza: "
                                    + scadenza.getPg_obbligazione_scadenzario()
                                    + " ha importo associato ai doc. contabili maggiore dell'importo associato a doc.amm o dell'importo della scadenza.");

                updateBulk(userContext, scadenza);
            }
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * creazione sospeso PreCondition: E' stata generata la richiesta di
     * creazione una nuova associazione Mandato-Sospeso (Sospeso_det_uscBulk) e
     * l'importo specificato dall'utente e' inferiore o uguale all'importo
     * disponibile del sospeso (importo disponibile = sospeso.im_sospeso -
     * sospeso.im_associati) PostCondition: Viene creata una nuova istanza di
     * Sospeso_det_uscBulk e viene incrementato l'importo associato del sospeso
     * (sospeso.im_associato) con l'importo che e' stato associato al mandato
     * (Sospeso_det_uscBulk.im_associato) creazione sospeso - errore
     * PreCondition: E' stata generata la richiesta di creazione una nuova
     * associazione Mandato-Sospeso (Sospeso_det_uscBulk) e l'importo
     * specificato dall'utente e' superiore all'importo disponibile del sospeso
     * (importo disponibile = sospeso.im_sospeso - sospeso.im_associati)
     * PostCondition: Viene segnalato all'utente l'impossibilità di creare
     * l'associazione Mandato-Sospeso modifica sospeso PreCondition: E' stata
     * generata la richiesta di modifica dell'importo di una associazione
     * Mandato-Sospeso (Sospeso_det_uscBulk) e la differenza fra l'importo
     * specificato ora dall'utente per il Sospeso_det_uscBulk e l'importo che
     * aveva in precedenza e' inferiore o uguale all'importo disponibile del
     * sospeso (importo disponibile = sospeso.im_sospeso - sospeso.im_associati)
     * PostCondition: Viene aggiornato l'importo di Sospeso_det_uscBulk con il
     * nuovo importo specificato dall'utente e viene aggiornato l'importo
     * associato del SospesoBulk con la differenza fra l'importo specificato ora
     * dall'utente e l'importo che aveva in precedenza (sospeso.im_associato =
     * sospeso_det_usc.im_associato(ora) - sospeso_det_usc.im_associato(in
     * precedenza) modifica sospeso - errore PreCondition: E' stata generata la
     * richiesta di modifica dell'importo di una associazione Mandato-Sospeso
     * (Sospeso_det_uscBulk) e la differenza fra l'importo specificato ora
     * dall'utente per il Sospeso_det_uscBulk e l'importo che aveva in
     * precedenza e' superiore all'importo disponibile del sospeso (importo
     * disponibile = sospeso.im_sospeso - sospeso.im_associati) PostCondition:
     * Viene segnalato all'utente l'impossibilità di aggiornare l'associazione
     * Mandato-Sospeso cancellazione sospeso PreCondition: E' stata generata la
     * richiesta di cancellazione di una associazione Mandato-Sospeso
     * (Sospeso_det_uscBulk) PostCondition: L'istanza di Sospeso_det_uscBulk
     * viene cancellata e viene decrementato l'importo associato del sospeso
     * (sospeso.im_associato) con l'importo che era stato associato al mandato
     * (Sospeso_det_uscBulk.im_associato)
     *
     * @param aUC     lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato <code>MandatoBulk</code> il mandato da aggiornare
     * @return mandato <code>MandatoBulk</code> il Mandato aggiornato
     */

    private MandatoBulk aggiornaImportoSospesi(UserContext aUC,
                                               MandatoBulk mandato) throws ComponentException {
        try {
            Sospeso_det_uscBulk sdu, sduFromDb;
            SospesoBulk sospeso;
            BigDecimal totSospesi = new BigDecimal(0);
            for (Iterator i = mandato.getSospeso_det_uscColl().iterator(); i
                    .hasNext(); ) {
                sdu = (Sospeso_det_uscBulk) i.next();
                totSospesi = totSospesi.add(sdu.getIm_associato());
                sospeso = sdu.getSospeso();
                lockBulk(aUC, sospeso);
                if (sdu.isToBeCreated()) {
                    if (sdu.getIm_associato().compareTo(
                            sospeso.getIm_disponibile()) > 0)
                        throw new ApplicationException(
                                "L'Importo specificato per il sospeso deve essere inferiore all'Ulteriore disponibilità su sospeso");
                    sospeso.setIm_associato(sospeso.getIm_associato().add(
                            sdu.getIm_associato()));
                } else if (sdu.isToBeDeleted()) {
                    sduFromDb = (Sospeso_det_uscBulk) getHome(aUC,
                            Sospeso_det_uscBulk.class).findByPrimaryKey(sdu);
                    sospeso.setIm_associato(sospeso.getIm_associato().subtract(
                            sduFromDb.getIm_associato()));
                    totSospesi = totSospesi.add(sdu.getIm_associato());
                } else if (sdu.isToBeUpdated()) {
                    sduFromDb = (Sospeso_det_uscBulk) getHome(aUC,
                            Sospeso_det_uscBulk.class).findByPrimaryKey(sdu);
                    if (sdu.getIm_associato().subtract(
                            sduFromDb.getIm_associato()).compareTo(
                            sospeso.getIm_disponibile()) > 0)
                        throw new ApplicationException(
                                "L'importo disponibile del sospeso e' stato esaurito");
                    sospeso.setIm_associato(sospeso.getIm_associato().add(
                            sdu.getIm_associato().subtract(
                                    sduFromDb.getIm_associato())));
                } else
                    continue;

                if (SospesoBulk.STATO_SOSP_IN_SOSPESO.equals(sospeso
                        .getStato_sospeso())
                        && sospeso.getCd_cds_origine() == null)
                    sospeso.setCds_origine(new CdsBulk(mandato
                            .getCd_cds_origine()));

                sospeso.setToBeUpdated();
                sospeso
                        .setUser(aUC
                                .getUser());
            }
            /*
             * 24/09/2002 Commentata la chiamata al metodo per l'impostazione
             * dell'importo pagato del Mandato, in quanto adesso non si imposta
             * più a PAGATO lo stato di un Mandato, quando viene associato ad un
             * sospeso
             */
            // mandato.setIm_pagato( totSospesi );
            // mandato.setToBeUpdated();
            // itero anche fra i sospesi che sono stati cancellati
            for (Iterator i = mandato.getSospeso_det_uscColl().deleteIterator(); i
                    .hasNext(); ) {
                sdu = (Sospeso_det_uscBulk) i.next();
                sospeso = sdu.getSospeso();
                lockBulk(aUC, sospeso);
                /*
                 * if ( sdu.isToBeCreated() ) { if (
                 * sdu.getIm_associato().compareTo( sospeso.getIm_disponibile()
                 * ) > 0 ) throw new ApplicationException(
                 * "L'importo specificato per il sospeso deve essere inferiore all'importo disponibile del sospeso"
                 * ); sospeso.setIm_associato( sospeso.getIm_associato().add(
                 * sdu.getIm_associato())); } else
                 */
                if (sdu.isToBeDeleted()) {
                    sduFromDb = (Sospeso_det_uscBulk) getHome(aUC,
                            Sospeso_det_uscBulk.class).findByPrimaryKey(sdu);
                    sospeso.setIm_associato(sospeso.getIm_associato().subtract(
                            sduFromDb.getIm_associato()));
                } else if (sdu.isToBeUpdated()) {
                    sduFromDb = (Sospeso_det_uscBulk) getHome(aUC,
                            Sospeso_det_uscBulk.class).findByPrimaryKey(sdu);
                    // if ( sdu.getIm_associato().subtract(
                    // sduFromDb.getIm_associato()).compareTo(
                    // sospeso.getIm_disponibile() ) > 0 )
                    // throw new ApplicationException(
                    // "L'importo disponibile del sospeso e' stato esaurito");
                    sdu.setIm_associato(sduFromDb.getIm_associato());
                    sospeso.setIm_associato(sospeso.getIm_associato().subtract(
                            sduFromDb.getIm_associato()));
                } else
                    continue;
                sospeso.setToBeUpdated();
                sospeso
                        .setUser(aUC
                                .getUser());
            }
            return mandato;
        } catch (Exception e) {
            throw handleException(e);

        }
    }

    /**
     * creazione mandato PreCondition: E' stata creata un mandato e sono stati
     * associati dei sospesi per un importo pari all'importo del mandato
     * PostCondition: Viene richiamata una stored proceduere che aggiorna lo
     * stato del mandato a PAGATO, la data di pagamento alla data odierna e
     * aggiorna i saldi relativi al pagato delle voci del piano presenti nel
     * mandato annullamento PreCondition: E' stato annullato un mandato con
     * stato PAGATO PostCondition: Viene richiamata una stored proceduere che
     * aggiorna lo stato del mandato a EMESSO, la data di pagamento viene
     * resettata e vengono aggiornati i saldi relativi al pagato delle voci del
     * piano presenti nel mandato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> mandato per cui aggiornare lo stato e
     *                    i saldi pagati
     * @param action      <code>String</code> azione effettuata sul mandato. ( I -
     *                    inserimento, M - modifica, A annullamento)
     */

    private void aggiornaSaldoPagato(UserContext userContext,
                                     MandatoBulk mandato, String action) throws ComponentException {
        try {
            LoggableStatement cs = new LoggableStatement(
                    getConnection(userContext), "{  call "
                    + it.cnr.jada.util.ejb.EJBCommonServices
                    .getDefaultSchema()
                    + "CNRCTB037.riscontroMandato(?, ?, ?, ?, ?)}",
                    false, this.getClass());
            try {
                cs.setObject(1, mandato.getEsercizio());
                cs.setString(2, mandato.getCd_cds());
                cs.setObject(3, mandato.getPg_mandato());
                cs.setString(4, action);
                cs.setString(5, userContext.getUser());
                cs.executeQuery();
            } catch (SQLException e) {
                throw handleException(e);
            } finally {
                cs.close();
            }
        } catch (SQLException e) {
            throw handleException(e);
        }

        /*
         * try { CallableStatement cs = getConnection( userContext
         * ).prepareCall( "{  call " +
         * it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
         * "CNRCTB300.aggDaSospesoRiscontro(?, ?, ?, ?, ?)}"); try {
         * cs.setString( 1, mandato.getCd_cds() ); cs.setObject( 2,
         * mandato.getEsercizio() ); cs.setObject( 3, mandato.getPg_mandato() );
         * cs.setString( 4, mandato.getCd_tipo_documento_cont() ); cs.setString(
         * 5, userContext.getUser()); LoggableStatement.executeQuery(cs); }
         * catch ( SQLException e ) { throw handleException(e); } finally {
         * cs.close(); } } catch ( SQLException e ) { throw handleException(e);
         * }
         */
    }

    /**
     * creazione mandato PreCondition: E' stata creato un nuovo mandato
     * PostCondition: Viene richiesta alla component che gestisce i documenti
     * amministrativi l'aggiornamento dello stato COFI dei documenti pagati con
     * il mandato annullamento mandato PreCondition: E' stata annullato un nuovo
     * mandato PostCondition: Viene richiesta alla component che gestisce i
     * documenti amministrativi l'aggiornamento dello stato COFI dei documenti
     * pagati con il mandato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> il mandato
     * @param action      <code>String</code> azione che può assumere valori
     *                    inserimento/annullamento
     */

    protected void aggiornaStatoFattura(UserContext userContext,
                                        MandatoBulk mandato, String action) throws ComponentException {
        try {
            createFatturaPassivaComponentSession()
                    .aggiornaStatoDocumentiAmministrativi(userContext,
                            mandato.getCd_cds(),
                            mandato.getCd_unita_organizzativa(),
                            mandato.getCd_tipo_documento_cont(),
                            mandato.getEsercizio(), mandato.getPg_mandato(),
                            action);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * aggiungiDocPassivi PreCondition: E' stata generata la richiesta di
     * aggiungere ad un mandato nuovi documenti amministrativi passivi ( fatture
     * passive o domenti generici passivi). Tali documenti hanno lo stesso terzo
     * e la stessa classe di pagamento. PostCondition: Per ogni documento
     * passivo viene creata una o piu' righe di mandato (metodo creaMandatoRiga)
     * secondo la seguente regola: - per ogni documento generico viene creata
     * una sola riga di mandato - per ogni fattura passiva viene creata una riga
     * di mandato ed eventualmente righe aggiuntive se tale fattura e' associata
     * a note di debito e/o note di credito Viene creata una istanza di
     * MandatoTerzoBulk (metodo creaMandatoTerzo) coi dati del terzo presente
     * nei documenti amministrativi errore - beneficiari diversi PreCondition:
     * Il codice terzo dei documenti amministrativi passivi da aggiungere al
     * mandato non e' lo stesso per tutti i documenti PostCondition: Un
     * messaggio di errore segnala all'utente l'impossibilità di aggiungere i
     * documenti al mandato errore - classe di pagamento PreCondition: La classe
     * di pagamento (Bancario,Postale,etc.) dei documenti amministrativi passivi
     * da aggiungere al mandato non e' lo stesso per tutti i documenti.
     * PostCondition: Un messaggio di errore segnala all'utente l'impossibilità
     * di aggiungere i documenti al mandato errore - mandato di regolarizzazione
     * PreCondition: I documenti amministrativi passivi selezionati per essere
     * aggiunti ad un mandato di regolarizzazione sono stati contabilizzati in
     * parte su obbligazioni relative a capitoli di bilancio e in parte su
     * obbligazioni relative a partite di giro. PostCondition: Un messaggio di
     * errore segnala all'utente l'impossibilità di aggiungere i documenti al
     * mandato
     * <p>
     * errore - mandato competenza/residuo PreCondition: I documenti
     * amministrativi passivi selezionati per essere aggiunti ad un mandato sono
     * stati contabilizzati in parte su impegni residui e in parte su impegni di
     * competenza PostCondition: Un messaggio di errore segnala all'utente
     * l'impossibilità di aggiungere i documenti al mandato
     *
     * @param aUC        lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato    <code>MandatoBulk</code> il mandato da aggiornare
     * @param docPassivi <code>List</code> la lista dei documenti passivi selezionati
     *                   dall'utente
     * @return mandato <code>MandatoBulk</code> il Mandato aggiornato
     */

    public MandatoBulk aggiungiDocPassivi(UserContext aUC, MandatoBulk mandato,
                                          List docPassivi) throws ComponentException {

        Integer cd_terzo;
        String ti_pagamento, ti_competenza_residuo;
        Boolean pGiro;
        V_doc_passivo_obbligazioneBulk docPassivo;
        Mandato_rigaBulk riga;
        Collection docPassiviCollegati;

        try {
            if (mandato.getMandato_rigaColl().size() == 0)
                mandato.setMandato_terzo(null);

            if (mandato.getMandato_terzo() == null) {
                cd_terzo = ((V_doc_passivo_obbligazioneBulk) docPassivi.get(0))
                        .getCodice_terzo_o_cessionario();
                ti_pagamento = ((V_doc_passivo_obbligazioneBulk) docPassivi
                        .get(0)).getTi_pagamento();
                pGiro = ((V_doc_passivo_obbligazioneBulk) docPassivi.get(0))
                        .getFl_pgiro();
                ti_competenza_residuo = ((V_doc_passivo_obbligazioneBulk) docPassivi
                        .get(0)).getTi_competenza_residuo();

            } else {
                cd_terzo = mandato.getMandato_terzo().getCd_terzo();
                ti_pagamento = ((Mandato_rigaIBulk) mandato
                        .getMandato_rigaColl().get(0)).getBanca()
                        .getTi_pagamento();
                pGiro = ((Mandato_rigaIBulk) mandato.getMandato_rigaColl().get(
                        0)).getFl_pgiro();
                ti_competenza_residuo = mandato.getTi_competenza_residuo();
            }
            for (Iterator i = docPassivi.iterator(); i.hasNext(); ) {
                docPassivo = (V_doc_passivo_obbligazioneBulk) i.next();
                if (!cd_terzo
                        .equals(docPassivo.getCodice_terzo_o_cessionario()))
                    throw new ApplicationException(
                            "E' possibile selezionare solo doc passivi relativi ad un unico beneficiario");
                if (!MandatoBulk.TIPO_REGOLARIZZAZIONE.equals(mandato
                        .getTi_mandato())
                        && !ti_pagamento.equals(docPassivo.getTi_pagamento()))
                    throw new ApplicationException(
                            "E' possibile selezionare solo doc passivi relativi ad una stessa classe di pagamento");
                /*
                 * simona 9.10.02 if ( mandato.getTi_mandato().equals(
                 * mandato.TIPO_REGOLARIZZAZIONE ) &&
                 * !docPassivo.getFl_pgiro().equals( pGiro )) throw new
                 * ApplicationException(
                 * "Per il mandato di regolarizzaione non e' possibile selezionare doc passivi su partite di giro e doc passivi su capitoli di bilancio"
                 * );
                 */
                if (!ti_competenza_residuo.equals(docPassivo
                        .getTi_competenza_residuo()))
                    throw new ApplicationException(
                            "E' possibile selezionare solo doc passivi dello stesso tipo COMPETENZA/RESIDUO.");
                // creo mandato_riga
                riga = creaMandatoRiga(aUC, mandato, docPassivo);
                //controllo cap /swift
                if (riga.getBanca() != null &&
                        ((Rif_modalita_pagamentoBulk.BANCARIO.equals(riga.getBanca().getTi_pagamento())
                                || (Rif_modalita_pagamentoBulk.IBAN.equals(riga.getBanca().getTi_pagamento()))))) {

                    BancaBulk banca = (BancaBulk) getHome(aUC,
                            BancaBulk.class).findByPrimaryKey(
                            new BancaBulk(riga.getCd_terzo(), riga.getPg_banca()));

                    if (banca != null && banca.getCodice_iban() != null && riga.getBanca().getAbi() != null && banca.getCodice_iban().startsWith("IT")) {
                        if (riga.getCd_terzo() != null) {
                            TerzoBulk terzo = (TerzoBulk) getHome(aUC,
                                    TerzoBulk.class).findByPrimaryKey(
                                    new TerzoBulk(riga.getCd_terzo()));
                            if (terzo.getPg_comune_sede() != null) {
                                ComuneBulk comune = (ComuneBulk) getHome(aUC,
                                        ComuneBulk.class).findByPrimaryKey(
                                        new ComuneBulk(terzo.getPg_comune_sede()));
                                if (comune.getTi_italiano_estero().equals(NazioneBulk.ITALIA) && terzo.getCap_comune_sede() == null)
                                    throw new ApplicationException(
                                            "Attenzione per la modalità di pagamento presente sul documento è necessario indicare il cap sul terzo.");
                            }
                        }
                    } else if (banca != null && banca.getCodice_iban() != null && riga.getBanca().getAbi() == null) {
                        NazioneHome nazioneHome = (NazioneHome) getHome(aUC,
                                NazioneBulk.class);
                        SQLBuilder sqlExists = nazioneHome.createSQLBuilder();
                        sqlExists.addSQLClause("AND", "NAZIONE.CD_ISO", SQLBuilder.EQUALS, banca.getCodice_iban().substring(0, 2));
                        sqlExists.addSQLClause("AND", "NAZIONE.FL_SEPA", SQLBuilder.EQUALS, "Y");
                        if (sqlExists.executeCountQuery(getConnection(aUC)) != 0 && riga.getBanca().getCodice_swift() == null)
                            throw new ApplicationException(
                                    "Attenzione per la modalità di pagamento presente sul documento è necessario indicare il codice swift/bic.");
                    }
                }
                // estrae le eventuali note di credito/debito
                docPassiviCollegati = ((MandatoIHome) getHome(aUC, mandato
                        .getClass())).findDocPassiviCollegati(docPassivo);
                for (Iterator j = docPassiviCollegati.iterator(); j.hasNext(); )
                    creaMandatoRigaCollegato(aUC, mandato,
                            (V_doc_passivo_obbligazioneBulk) j.next(), riga);
            }
            // creo mandato terzo
            if (mandato.getMandato_terzo() == null) {
                Mandato_terzoBulk mTerzo = creaMandatoTerzo(aUC, mandato,
                        cd_terzo);
                mandato.setMandato_terzo(mTerzo);
            }
            mandato.setTi_competenza_residuo(ti_competenza_residuo);
            mandato.refreshImporto();
            return mandato;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /*
     * ---- non usato ---- Consente di modificare un mandato di accreditamento
     * CNR - Cds consentendo la creazione di nuove righe di mandato
     */
    public MandatoAccreditamentoBulk aggiungiImpegni(UserContext aUC,
                                                     MandatoAccreditamentoBulk mandato, List impegni)
            throws ComponentException {
        // se nella lista di impegni ci sono sia residuo che competenza e'
        // necessario creare un nuovo mandato

        V_impegnoBulk impegno;
        Mandato_rigaBulk riga = null;

        // creo mandato_riga
        for (Iterator i = impegni.iterator(); i.hasNext(); ) {
            impegno = (V_impegnoBulk) i.next();
            // riga = creaMandatoRiga( aUC, mandato, impegno );
            mandato.addToMandato_rigaColl(riga, impegno);
        }

        // creo mandato terzo
        if (mandato.getMandato_terzo() == null) {
            Mandato_terzoBulk mTerzo = creaMandatoTerzo(aUC, mandato, mandato
                    .getMandato_terzo().getCd_terzo());
            mandato.setMandato_terzo(mTerzo);
        }

        mandato.refreshImporto();
        return mandato;
    }

    /**
     * annullamento PreCondition: E' stata generata la richiesta di annullare un
     * Mandato Il Mandato ha tipo diverso da regolarizzazione e accreditamento
     * Il Mandato ha originato delle reversali o dei mandati PostCondition:
     * Tutti i mandati originati dal mandato da annullare sono stati annullati
     * E' stata richiesta alla Component che gestisce la Reversale
     * l'annullamento di tutte le reversali originate dal mandato da annullare
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> il mandato da annullare
     */
    private void annullaDocContabiliCollegati(UserContext userContext, MandatoBulk mandato) throws ComponentException {
        try {
            V_ass_doc_contabiliBulk ass;
            ReversaleComponentSession revSession = null;
            ReversaleBulk reversale;
            MandatoBulk manColl;

            if (mandato.getDoc_contabili_collColl().size() > 0)
                revSession = createReversaleComponentSession();
            for (Iterator i = mandato.getDoc_contabili_collColl().iterator(); i.hasNext(); ) {
                ass = (V_ass_doc_contabiliBulk) i.next();
                if (ass.getCd_tipo_documento_cont().equals(
                        Numerazione_doc_contBulk.TIPO_MAN)
                        && ass.getCd_cds().equals(mandato.getCd_cds())
                        && ass.getEsercizio().equals(mandato.getEsercizio())
                        && ass.getPg_documento_cont().equals(
                        mandato.getPg_mandato())
                        && ass.getCd_tipo_documento_cont_coll().equals(
                        Numerazione_doc_contBulk.TIPO_MAN)) {
                    /** il mandato ha un mandato associato **/
                    manColl = (MandatoBulk) inizializzaBulkPerModifica(
                            userContext, new MandatoIBulk(ass.getCd_cds_coll(),
                                    ass.getEsercizio_coll(), ass
                                    .getPg_documento_cont_coll()));
                    annullaMandato(userContext, manColl, null, false);
                } else if (ass.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_MAN)
                        && ass.getCd_cds().equals(mandato.getCd_cds())
                        && ass.getEsercizio().equals(mandato.getEsercizio())
                        && ass.getPg_documento_cont().equals(mandato.getPg_mandato())
                        && ass.getCd_tipo_documento_cont_coll().equals(Numerazione_doc_contBulk.TIPO_REV)) {
                    /** il mandato ha una reversale associata **/
                    reversale = (ReversaleBulk) revSession.inizializzaBulkPerModifica(userContext,
                            new ReversaleIBulk(ass.getCd_cds_coll(),
                                    ass.getEsercizio_coll(),
                                    ass.getPg_documento_cont_coll()));
                    revSession.annullaReversale(userContext, reversale, false);
                    rimuoviVincoliAlMandato(userContext, mandato);
                }
            }
        } catch (Exception e) {
            throw handleException(mandato, e);
        }
    }

    private void rimuoviVincoliAlMandato(UserContext userContext, MandatoBulk mandatoBulk) throws ComponentException, PersistencyException, IntrospectionException {
        /**
         * Nel caso di Mandato NON ACQUISITO e reversali NON_ESEGUITE ma ACQUISITA
         * rimuovo i vincoli
         */
        if (Optional.ofNullable(mandatoBulk.getEsitoOperazione())
                .filter(s -> s.equalsIgnoreCase(EsitoOperazione.NON_ACQUISITO.value()))
                .isPresent()) {
            Ass_mandato_reversaleHome ass_mandato_reversaleHome =
                    Optional.ofNullable(getHome(userContext, Ass_mandato_reversaleBulk.class))
                            .filter(Ass_mandato_reversaleHome.class::isInstance)
                            .map(Ass_mandato_reversaleHome.class::cast)
                            .orElseThrow(() -> new ComponentException(("Ass_mandato_reversaleHome not found")));

            final Collection<Ass_mandato_reversaleBulk> ass = Optional.ofNullable(ass_mandato_reversaleHome.findReversali(userContext, mandatoBulk))
                    .filter(collection -> !collection.isEmpty())
                    .orElse(Collections.emptyList());
            for (Ass_mandato_reversaleBulk ass_mandato_reversaleBulk : ass) {
                ReversaleBulk reversale =
                        Optional.ofNullable(super.findByPrimaryKey(userContext,
                                new ReversaleIBulk(ass_mandato_reversaleBulk.getCd_cds_reversale(),
                                        ass_mandato_reversaleBulk.getEsercizio_reversale(),
                                        ass_mandato_reversaleBulk.getPg_reversale())))
                                .filter(ReversaleBulk.class::isInstance)
                                .map(ReversaleBulk.class::cast)
                                .orElse(null);
                if (Optional.ofNullable(reversale)
                        .flatMap(reversaleBulk -> Optional.ofNullable(reversaleBulk.getEsitoOperazione()))
                        .filter(s -> s.equalsIgnoreCase(EsitoOperazione.ACQUISITO.value()) ||
                                s.equalsIgnoreCase(EsitoOperazione.NON_ESEGUIBILE.value()))
                        .isPresent()
                ) {
                    ass_mandato_reversaleBulk.setToBeDeleted();
                    try {
                        ass_mandato_reversaleHome.delete(ass_mandato_reversaleBulk, userContext);
                        logger.info("SIOPE+ annulato vincolo al Mandato {}/{} Reversale {}/{}",
                                mandatoBulk.getEsercizio(),
                                mandatoBulk.getPg_mandato(),
                                ass_mandato_reversaleBulk.getEsercizio_reversale(),
                                ass_mandato_reversaleBulk.getPg_reversale());
                    } catch (PersistencyException e) {
                        throw new DetailedRuntimeException(e);
                    }
                }
            }
        }
    }

    /**
     * annulla documento generico E' stata generata la richiesta di annullare un
     * mandato di accreditamento CNR-Cds ed e' pertanto necessario annullare
     * anche il documento generico creato in automatico alla creazione del
     * mandato. Il servizio viene richiesto alla Component che gestisce i
     * documenti generici amministrativi PostCondition: Il documento generico di
     * spesa di tipo TRASF_S associato al mandato di accreditamento e' stato
     * annullato e le scadenze degli impegni su cui era stato contabilizzato
     * sono state aggiornate
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> il mandato associato al documento
     *                    generico di spesa
     */
    public void annullaDocumentoGenerico(UserContext userContext,
                                         MandatoBulk mandato) throws ComponentException {
        try {
            Mandato_rigaBulk riga = (Mandato_rigaBulk) mandato
                    .getMandato_rigaColl().get(0);
            DocumentoGenericoComponentSession docSession = createDocumentoGenericoComponentSession();
            Documento_genericoBulk docGenerico = (Documento_genericoBulk) docSession
                    .inizializzaBulkPerModifica(userContext,
                            new Documento_genericoBulk(
                                    riga.getCd_cds_doc_amm(), riga
                                    .getCd_tipo_documento_amm(), riga
                                    .getCd_uo_doc_amm(), riga
                                    .getEsercizio_doc_amm(), riga
                                    .getPg_doc_amm()));
            docGenerico_annullaDocumentoGenerico(userContext, docGenerico);
        } catch (Exception e) {
            throw handleException(mandato, e);
        }

    }

    /**
     * annulla importo sospesi PreCondition: E' stata generata la richiesta di
     * annullare un mandato ed e' pertanto necessario aggiornare l'importo di
     * tutti i sospesi che erano sono stati associati a questo mandato
     * PostCondition: Per ogni istanza di Sospeso_det_uscBulk presente nel db,
     * viene aggiornato l'importo del sospeso associato nel modo seguente:
     * sospeso.im_associato = sospeso.im_associato -
     * sospeso_det_usc.im_associato
     *
     * @param aUC     lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato <code>MandatoBulk</code> il mandato da annullare
     * @return mandato <code>MandatoBulk</code> il Mandato da annullare
     */

    private MandatoBulk annullaImportoSospesi(UserContext aUC,
                                              MandatoBulk mandato) throws ComponentException {
        try {
            Sospeso_det_uscBulk sdu, sduFromDb;
            SospesoBulk sospeso;
            for (Iterator i = mandato.getSospeso_det_uscColl().iterator(); i
                    .hasNext(); ) {
                sdu = (Sospeso_det_uscBulk) i.next();
                sospeso = sdu.getSospeso();
                lockBulk(aUC, sospeso);
                sduFromDb = (Sospeso_det_uscBulk) getHome(aUC,
                        Sospeso_det_uscBulk.class).findByPrimaryKey(sdu);
                if (sduFromDb != null) {
                    sospeso.setIm_associato(sospeso.getIm_associato().subtract(
                            sduFromDb.getIm_associato()));
                    sospeso.setToBeUpdated();
                    sospeso
                            .setUser(aUC
                                    .getUser());

                }
            }
            /*
             * //itero anche fra i sospesi che sono stati cancellati for (
             * Iterator i = mandato.getSospeso_det_uscColl().deleteIterator();
             * i.hasNext(); ) { sdu = (Sospeso_det_uscBulk) i.next(); sospeso =
             * sdu.getSospeso(); lockBulk( aUC, sospeso ); sduFromDb =
             * (Sospeso_det_uscBulk) getHome( aUC, Sospeso_det_uscBulk.class
             * ).findByPrimaryKey( sdu ); if ( sduFromDb != null ) {
             * sospeso.setIm_associato( sospeso.getIm_associato().subtract(
             * sduFromDb.getIm_associato())); sospeso.setToBeUpdated();
             * sospeso.setUser( ((it.cnr.contab.utenze00.bp.CNRUserContext)
             * aUC).getUser());
             *
             * } }
             */
            return mandato;
        } catch (Exception e) {
            throw handleException(e);

        }
    }

    /**
     * annullamento PreCondition: E' stata generata la richiesta di annullare un
     * Mandato PostCondition: Viene annullato il Mandato (metodo annullaMandato)
     * specificando che non e' stata effettuata la verifica sui compensi e che
     * e' necessario procedere anche all'annullamento dei mandati/reversali
     * collegate
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> il mandato da annullare
     * @return mandato <code>MandatoBulk</code> il Mandato annullato
     */

    public MandatoBulk annullaMandato(UserContext userContext,
                                      MandatoBulk mandato) throws ComponentException {
        try {
            return annullaMandato(userContext, mandato, null, true);
        } catch (Exception e) {
            throw handleException(mandato, e);
        }

    }

    /**
     * annullamento PreCondition: E' stata generata la richiesta di annullare un
     * Mandato PostCondition: Viene annullato il Mandato (metodo annullaMandato)
     * passando l'informazione se effettuare o meno la verifica sui compensi e
     * passando l'informazione che e' necessario procedere anche
     * all'annullamento dei mandati/reversali collegate
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> il mandato da annullare
     * @param param       il parametro che indica se il controllo sul compenso e'
     *                    necessario ( se param = null e' necessario effettuare il
     *                    controllo, altrimenti no)
     * @return mandato <code>MandatoBulk</code> il Mandato annullato
     */

    public MandatoBulk annullaMandato(UserContext userContext,
                                      MandatoBulk mandato, CompensoOptionRequestParameter param)
            throws ComponentException {
        try {
            return annullaMandato(userContext, mandato, param, true);
        } catch (Exception e) {
            throw handleException(mandato, e);
        }

    }

    /**
     * annullamento mandato e collegati PreCondition: E' stata generata la
     * richiesta di annullare un Mandato E' stato richiesto l'annullamento dei
     * doc. contabili collegati PostCondition: Viene impostata la data di
     * annullamento del mandato con la data odierna e lo stato del mandato
     * diventa ANNULLATO. Viene impostato lo stato ANNULLATO su tutte le righe
     * del mandato. Per ogni riga inoltre viene aggiornato l'importo associato a
     * doc.contabili della scadenza di obbligazione legata alla riga (metodo
     * aggiornaImportoObbligazione), viene aggiornato lo stato del documento
     * amministrativo legato alla riga (metodo aggiornaStatoFattura). Vengono
     * aggiornati i saldi dei capitoli (metodo aggiornaCapitoloSaldoRiga). Per
     * ogni associzione sospeso-mandato, viene aggiornato l'importo associato
     * del sospeso (metodo annullaImportoSospesi). Se il mandato ha associate
     * reversali o altri mandati viene eseguito il loro annullamento (metodo
     * 'annullaDocContabiliCollegati') annullamento mandato PreCondition: E'
     * stata generata la richiesta di annullare un Mandato Non e' necessario
     * procedere all'annullamento dei doc. contabili collegati PostCondition:
     * Viene impostata la data di annullamento del mandato con la data odierna e
     * lo stato del mandato diventa ANNULLATO. Viene impostato lo stato
     * ANNULLATO su tutte le righe del mandato. Per ogni riga inoltre viene
     * aggiornato l'importo associato a doc.contabili della scadenza di
     * obbligazione legata alla riga (metodo aggiornaImportoObbligazione), viene
     * aggiornato lo stato del documento amministrativo legato alla riga (metodo
     * aggiornaStatoFattura). Vengono aggiornati i saldi dei capitoli (metodo
     * aggiornaCapitoloSaldoRiga). Per ogni associzione sospeso-mandato, viene
     * aggiornato l'importo associato del sospeso (metodo
     * annullaImportoSospesi). Se il mandato ha associate reversali o altri
     * mandati NON viene eseguito il loro annullamento annullamento mandato di
     * regolarizzazione PreCondition: E' stata generata la richiesta di
     * annullare un Mandato di regolarizzazione PostCondition: Oltre alle
     * PostCondition dell'annullamento di un mandato normale, viene anche
     * annullati sia la reversale di regolarizzazione associata al mandato che
     * il documento amministrativo generico di entrata creato dal sistema
     * (metodo annullaReversaleRegolarizzazione) annullamento mandato di
     * trasferimento PreCondition: E' stata generata la richiesta di annullare
     * un Mandato di trasferimento PostCondition: Oltre alle PostCondition
     * dell'annullamento di un mandato normale, viene anche annullato il
     * documento amm. generico creato in automatico alla creazione del mandato
     * (metodo annullaDocumentoGenerico) e viene annullata sia la reversale di
     * trasferimento associata al mandato che il relativo documento generico di
     * entrata (metodo annullaReversaleTrasferimento) errore riscontri associati
     * PreCondition: E' stata generata la richiesta di annullare un Mandato che
     * ha riscontri associati PostCondition: Una segnalazione di errore comunica
     * all'utente l'impossibilità di eseguire l'annullamento annullamento
     * mandato su anticipo associato a missione PreCondition: E' stata generata
     * la richiesta di annullare un Mandato Il mandato non supera la validazione
     * effettuata dal metodo 'verificaMandatoSuAnticipo' in quanto include un
     * anticipo associato a missione PostCondition: Una segnalazione di errore
     * comunica all'utente l'impossibilità di eseguire l'annullamento
     * annullamento mandato di compenso su riscontro PreCondition: E' stata
     * generata la richiesta di annullare un Mandato Il mandato si riferisce a
     * compensi inclusi in conguagli PostCondition: Una segnalazione richiede
     * all'utente se intende comunque proseguire all'annullamento del mandato
     *
     * @param userContext      lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato          <code>MandatoBulk</code> il mandato da annullare
     * @param p                il parametro che indica se il controllo sul compenso e'
     *                         necessario
     * @param annullaCollegati valore booleano che indica se procedere o meno con
     *                         l'annullamento dei doc. contabili collegati al mandato
     * @return mandato <code>MandatoBulk</code> il Mandato annullato
     */

    public MandatoBulk annullaMandato(UserContext userContext,
                                      MandatoBulk mandato, CompensoOptionRequestParameter p,
                                      boolean annullaCollegati, boolean riemissione) throws ComponentException {
        try {
            if (mandato.isAnnullato())
                throw new ApplicationException(
                        "Il mandato e' già stato annullato");

            verificaStatoEsercizio(userContext, mandato.getEsercizio(), mandato
                    .getCd_cds());
            BigDecimal totdettagli = ((Sospeso_det_uscHome) getHome(
                    userContext, Sospeso_det_uscBulk.class))
                    .calcolaTotDettagli(new V_mandato_reversaleBulk(mandato
                            .getEsercizio(), mandato
                            .getCd_tipo_documento_cont(), mandato.getCd_cds(),
                            mandato.getPg_mandato()));
            if (totdettagli.compareTo(new BigDecimal(0)) > 0 &&
                    Optional.ofNullable(mandato.getStatoVarSos())
                            .map(s -> !s.equalsIgnoreCase(StatoVariazioneSostituzione.ANNULLATO_PER_SOSTITUZIONE.value()))
                            .orElse(Boolean.TRUE))
                throw new ApplicationException(
                        "Annullamento impossibile! Il mandato e' già stato associato ad un riscontro");

            Sospeso_det_uscBulk sdu;
            for (Iterator i = mandato.getSospeso_det_uscColl().iterator(); i
                    .hasNext(); ) {
                sdu = (Sospeso_det_uscBulk) i.next();
                if (sdu.isToBeCreated() || sdu.isToBeUpdated()
                        || sdu.isToBeDeleted())
                    throw new ApplicationException(
                            "Annullamento impossibile! Sono state fatte delle modifiche ai sospesi che devono essere ancora salvate");
            }

            if (MandatoBulk.TIPO_REGOLARIZZAZIONE.equals(mandato
                    .getTi_mandato())) {
                if (mandato instanceof MandatoIBulk) {
                    Var_bilancioBulk varBilancio = ((Var_bilancioHome) getHome(
                            userContext, Var_bilancioBulk.class))
                            .findByMandato(mandato);
                    if (varBilancio != null)
                        throw new ApplicationException(
                                "Annullamento impossibile! Esiste una variazione di bilancio Ente associata al mandato di regolarizzazione");
                }
            }

            verificaMandatoSuAnticipo(userContext, mandato);

            checkAnnullabilita(userContext, mandato);
            if (isMandatoCollegatoAnnullodaRiemettere(userContext, mandato).booleanValue())
                throw new ApplicationException(
                        "Annullamento impossibile! Esiste un mandato annullato associato al mandato.");

            if (isAnnullabile(userContext, mandato).compareTo("N") == 0)
                throw new ApplicationException(
                        "Verificare lo stato di trasmissione del mandato. Annullamento impossibile!");

            lockBulk(userContext, mandato);

            // modifica 16/01/2017 messa sempre o sysdate o 31/12
            mandato.setDt_annullamento(DateServices
                    .getTs_valido(userContext));

            if (mandato.getStato_coge().equals(MandatoBulk.STATO_COGE_C))
                mandato.setStato_coge(MandatoBulk.STATO_COGE_R);
            if (!MandatoBulk.TIPO_REGOLARIZZAZIONE.equals(mandato
                    .getTi_mandato())) {
                mandato.setFl_riemissione(riemissione);
                mandato.setStato_trasmissione_annullo(MandatoIBulk.STATO_TRASMISSIONE_NON_INSERITO);
            }
            mandato.annulla();
            annullaImportoSospesi(userContext, mandato);

            Mandato_rigaBulk riga;
            SaldoComponentSession session = createSaldoComponentSession();
            for (Iterator i = mandato.getMandato_rigaColl().iterator(); i
                    .hasNext(); ) {
                riga = (Mandato_rigaBulk) i.next();
                riga.annulla();
                aggiornaCapitoloSaldoRiga(userContext, riga, session);
            }
            aggiornaImportoObbligazioni(userContext, mandato);
            makeBulkPersistent(userContext, mandato);
            aggiornaStatoFattura(userContext, mandato,
                    ANNULLAMENTO_MANDATO_ACTION);
            /*
             * 24/09/2002 Commentata la chiamata alla stored procedure per
             * l'aggiornamento dei saldi, in quanto adesso non si imposta più a
             * PAGATO lo stato di un Mandato, quando viene associato ad un
             * sospeso
             */
            // if ( mandato.getIm_mandato().compareTo( mandato.getIm_pagato())
            // == 0 )
            // aggiornaSaldoPagato( userContext, mandato,
            // ANNULLAMENTO_MANDATO_ACTION );

            if (mandato.getTi_mandato().equals(MandatoBulk.TIPO_ACCREDITAMENTO)
                    && mandato.getReversaliColl().size() > 0) {
                annullaDocumentoGenerico(userContext, mandato);
                annullaReversaleDiTrasferimento(userContext, mandato);
            } else if (mandato.getTi_mandato().equals(
                    MandatoBulk.TIPO_REGOLARIZZAZIONE)
                    && mandato.getReversaliColl().size() > 0) {
                aggiornaSaldoPagato(userContext, mandato,
                        ANNULLAMENTO_MANDATO_ACTION);
                annullaReversaleDiRegolarizzazione(userContext, mandato);
            } else if (mandato.getDoc_contabili_collColl().size() > 0
                    && ((MandatoIBulk) mandato).hasFattura_passiva()) {
                /**
                 * Nel caso di annullamento per sostituzione non annullo le reversali collegate
                 */
                if (Optional.ofNullable(mandato.getStatoVarSos())
                        .map(s -> !s.equalsIgnoreCase(StatoVariazioneSostituzione.ANNULLATO_PER_SOSTITUZIONE.value()))
                        .orElse(Boolean.TRUE)) {
                    annullaReversaleDiIncassoIVA(userContext,
                            (MandatoIBulk) mandato);
                }
            } else if (annullaCollegati) {
                annullaDocContabiliCollegati(userContext, mandato);
            }
            return mandato;
        } catch (Exception e) {
            throw handleException(mandato, e);
        }

    }

    private void annullaReversaleDiIncassoIVA(UserContext userContext,
                                              MandatoIBulk mandato) throws ComponentException {
        try {
            /* REVERSALE */
            Ass_mandato_reversaleBulk ass = (Ass_mandato_reversaleBulk) mandato
                    .getReversaliColl().get(0);
            ReversaleComponentSession revSession = createReversaleComponentSession();
            ReversaleIBulk reversale = (ReversaleIBulk) revSession
                    .inizializzaBulkPerModifica(userContext,
                            new ReversaleIBulk(ass.getCd_cds_reversale(), ass
                                    .getEsercizio_reversale(), ass
                                    .getPg_reversale()));
            revSession.annullaReversaleDiIncassoIVA(userContext, reversale);
            rimuoviVincoliAlMandato(userContext, mandato);
        } catch (Exception e) {
            throw handleException(mandato, e);
        }

    }

    /**
     * annullamento PreCondition: E' stata generata la richiesta di annullare un
     * Mandato di Regolarizzazione PostCondition: Viene richiesta alla Component
     * che gestisce la Reversale l'annullamento della reversale di
     * regolarizzazione associata al mandato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> il mandato di regolarizzazione
     *                    (associato alla reversale) da annullare
     */
    private void annullaReversaleDiRegolarizzazione(UserContext userContext,
                                                    MandatoBulk mandato) throws ComponentException {
        try {
            /* REVERSALE */
            Ass_mandato_reversaleBulk ass = (Ass_mandato_reversaleBulk) mandato
                    .getReversaliColl().get(0);
            ReversaleComponentSession revSession = createReversaleComponentSession();
            ReversaleBulk reversale = (ReversaleBulk) revSession
                    .inizializzaBulkPerModifica(userContext,
                            new ReversaleIBulk(ass.getCd_cds_reversale(), ass
                                    .getEsercizio_reversale(), ass
                                    .getPg_reversale()));
            revSession.annullaReversaleDiRegolarizzazione(userContext,
                    reversale);
            rimuoviVincoliAlMandato(userContext, mandato);
        } catch (Exception e) {
            throw handleException(mandato, e);
        }

    }

    /**
     * annullamento PreCondition: E' stata generata la richiesta di annullare un
     * Mandato di Trasferimento PostCondition: Viene richiesta alla Component
     * che gestisce la Reversale l'annullamento della reversale di Trasferimento
     * associata al mandato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> il mandato di trasferimento
     *                    (associato alla reversale) da annullare
     */

    private void annullaReversaleDiTrasferimento(UserContext userContext,
                                                 MandatoBulk mandato) throws ComponentException {
        try {
            /* REVERSALE */
            Ass_mandato_reversaleBulk ass = (Ass_mandato_reversaleBulk) mandato
                    .getReversaliColl().get(0);
            ReversaleComponentSession revSession = createReversaleComponentSession();
            ReversaleIBulk reversale = (ReversaleIBulk) revSession
                    .inizializzaBulkPerModifica(userContext,
                            new ReversaleIBulk(ass.getCd_cds_reversale(), ass
                                    .getEsercizio_reversale(), ass
                                    .getPg_reversale()));
            revSession.annullaReversaleDiTrasferimento(userContext, reversale);
            rimuoviVincoliAlMandato(userContext, mandato);
        } catch (Exception e) {
            throw handleException(mandato, e);
        }

    }

    protected void callVsx_man_acc(UserContext userContext, Long pg_call)
            throws ComponentException {
        LoggableStatement cs = null;
        try {
            try {
                cs = new LoggableStatement(getConnection(userContext),
                        "{ call "
                                + it.cnr.jada.util.ejb.EJBCommonServices
                                .getDefaultSchema() + VSX_PROC_NAME
                                + "( ? ) }", false, this.getClass());
                cs.setObject(1, pg_call);
                // cs.setObject( 2, ((CNRUserContext)userContext).getUser());
                // cs.setObject( 2,
                // ((CNRUserContext)userContext).getEsercizio());
                // cs.setObject( 4,
                // it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp());
                cs.executeQuery();
            } catch (Throwable e) {
                throw handleException(e);
            } finally {
                if (cs != null)
                    cs.close();
            }
        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        }
    }

    public it.cnr.jada.util.RemoteIterator cercaImpegni(
            UserContext userContext,
            it.cnr.jada.persistency.sql.CompoundFindClause clausole,
            MandatoBulk mandato) throws it.cnr.jada.comp.ComponentException {
        try {
            return iterator(userContext, ((MandatoAccreditamentoHome) getHome(
                    userContext, MandatoAccreditamentoBulk.class))
                            .selectImpegno(mandato), V_impegnoBulk.class,
                    getFetchPolicyName("find"));
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * cerca sospesi PreCondition: E' stata richiesta la ricerca dei sospesi di
     * spesa da associare ad un mandato PostCondition: Vengono ricercati tutti i
     * sospesi di spesa non annullati che non sono ancora stati associati al
     * mandato con cds appartenza uguale al cds appartenenza del mandato,
     * esercizio uguale all'esercizio di scrivania, importo disponibile (importo
     * disponibile = importo iniziale del sospeso - importo già associato a
     * mandati) maggiore di zero, stato uguale a ASSOCIATO A CDS
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param clausole    le clausole specificate dall'utente
     * @param mandato     <code>MandatoBulk</code> il mandato
     * @return il RemoteIterator della lista dei sospesi di spesa
     */

    public it.cnr.jada.util.RemoteIterator cercaSospesi(
            UserContext userContext,
            it.cnr.jada.persistency.sql.CompoundFindClause clausole,
            MandatoBulk mandato) throws it.cnr.jada.comp.ComponentException {
        try {

            return iterator(userContext, ((SospesoHome) getHome(userContext,
                    SospesoBulk.class)).selectSospesiDiSpesa(mandato, clausole, Utility.createParametriCnrComponentSession().getParametriCnr(userContext, mandato.getEsercizio()).getFl_tesoreria_unica().booleanValue()),
                    SospesoBulk.class, getFetchPolicyName("find"));
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    private void checkAnnullabilita(UserContext userContext, MandatoBulk mandato)
            throws ComponentException {
        try {
            LoggableStatement cs = new LoggableStatement(
                    getConnection(userContext), "{  call "
                    + it.cnr.jada.util.ejb.EJBCommonServices
                    .getDefaultSchema()
                    + "CNRCTB039.checkAnnullabilita(?, ?, ?, ?)}",
                    false, this.getClass());
            try {
                cs.setString(1, "M"); // mandato
                cs.setObject(2, mandato.getEsercizio());
                cs.setString(3, mandato.getCd_cds());
                cs.setObject(4, mandato.getPg_mandato());
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

    private void checkDocAmmCambiato(UserContext userContext,
                                     Mandato_rigaBulk riga) throws ComponentException {
        try {
            LoggableStatement cs = new LoggableStatement(
                    getConnection(userContext),
                    "{  call "
                            + it.cnr.jada.util.ejb.EJBCommonServices
                            .getDefaultSchema()
                            + "CNRCTB300.checkDocAmmCambiato(?, ?, ?, ?, ?, ? )}",
                    false, this.getClass());
            try {
                cs.setString(1, riga.getCd_tipo_documento_amm());
                cs.setString(2, riga.getCd_cds());
                cs.setObject(3, riga.getEsercizio_doc_amm());
                cs.setString(4, riga.getCd_uo_doc_amm());
                cs.setObject(5, riga.getPg_doc_amm());
                cs.setObject(6, riga.getPg_ver_rec_doc_amm());

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

    protected void chiamaVsx(UserContext userContext,
                             MandatoAccreditamentoBulk mandato) throws ComponentException {
        try {
            Integer last_par_num = new Integer(1);
            Long pg_call = getPg_call(userContext);
            V_impegnoBulk impegno;
            for (Iterator i = mandato.getImpegniSelezionatiColl().iterator(); i
                    .hasNext(); ) {
                impegno = (V_impegnoBulk) i.next();
                last_par_num = inserisciVsx(userContext, pg_call, mandato,
                        impegno, last_par_num);
            }
            callVsx_man_acc(userContext, pg_call);
            eliminaVsx(userContext, pg_call);

        } catch (Throwable e) {
            throw handleException(e);
        }

    }

    /**
     * creazione PreCondition: E' stata generata la richiesta di creazione un
     * Mandato di Trasferimento o di un Mandato di Regolarizzazione
     * PostCondition: Viene creata una istanza di Ass_mandato_reversaleBulk con
     * i dati del mandato e della reversale associata
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> il mandato
     * @param reversale   <code>ReversaleBulk</code> la reversale associata al mandato
     * @return ass <code>Ass_mandato_reversaleBulk</code> L'associazione
     * Mandato-Reversale da creare
     */

    public Ass_mandato_reversaleBulk creaAss_mandato_reversale(
            UserContext userContext, MandatoBulk mandato,
            ReversaleBulk reversale) throws ComponentException {
        try {
            Ass_mandato_reversaleBulk ass = new Ass_mandato_reversaleBulk();
            ass.setToBeCreated();
            ass.setUser(mandato.getUser());
            ass.setMandato(mandato);
            ass.setReversale(reversale);
            ass.setTi_origine(Ass_mandato_reversaleBulk.TIPO_ORIGINE_SPESA);
            insertBulk(userContext, ass);
            mandato.getReversaliColl().add(ass);
            reversale.getMandatiColl().add(ass);
            return ass;
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * creazione mandato PreCondition: E' stata generata la richiesta di
     * creazione un Mandato e il mandato supera la validazione (metodo
     * verificaMandato) PostCondition: Vengono aggiornati gli importi dei
     * sospesi eventualmente associati al mandato (metodo
     * aggiornaImportoSospesi), vengono aggiornati gli importi associati a
     * documenti contabili di tutte le scadenze di obbligazioni specificate
     * nelle righe del mandato (metodo aggiornaImportoObbligazione), vengo
     * aggiornati i saldi relativi ai capitoli di spesa (metodo
     * aggiornaStatoFattura), vengono aggiornati gli stati delle fatture
     * specificate nelle righe dei mandati (metodo aggiornaCapitoloSaldoRiga)
     * creazione mandato di regolarizzazione PreCondition: E' stata generata la
     * richiesta di creazione un Mandato di regolarizzazione e il mandato supera
     * la validazione (metodo verificaMandato) PostCondition: Oltre alle
     * PostCondition presenti in 'creazione mandato', il mandato viene
     * riscontrato (metodo 'aggiornaSaldoPagato') e viene generata in automatico
     * una reversale di regolarizzazione (metodo
     * creaReversaleDiregolarizzazione) creazione mandato di trasferimento di
     * competenza PreCondition: E' stata generata la richiesta di creazione un
     * Mandato di trasferimento e l'utente ha selezionato solo impegni di
     * competenza PostCondition: Viene richiesto alla Component che gestisce i
     * documenti amministrativi generici di creare un documento generico di
     * spesa (di tipo TRASF_S) con tante righe quanti sono gli impegni
     * selezionati dall'utente, viene creato un mandato di regolarizzazione di
     * tipo competenza (metodo creaMandatoRegolarizzazione) con tante righe
     * (metodo creaMandatoRiga) quanti sono gli impegni selezionati dall'utente.
     * Con il metodo 'aggiornaImportoObbligazione' vengono incrementati gli
     * importi (im_associato_doc_contabili) degli impegni selezionati con
     * l'importo trasferito nel mandato. Con il metodo
     * 'aggiornaCapitoloSaldoRiga' vengono aggiornati i saldi relativi ai
     * capitoli di competenza degli impegni selezionati. Viene creata una
     * reversale provvisoria di trasferimento per il Cds che beneficia del
     * trasferimento (metodo 'creaReversaleDiRegolarizzazione') creazione
     * mandato di trasferimento residuo PreCondition: E' stata generata la
     * richiesta di creazione un Mandato di trasferimento e l'utente ha
     * selezionato solo impegni residui PostCondition: Viene richiesto alla
     * Component che gestisce i documenti amministrativi generici di creare un
     * documento generico di spesa (di tipo TRASF_S) con tante righe quanti sono
     * gli impegni selezionati dall'utente, viene creato un mandato di
     * regolarizzazione di tipo residuo (metodo creaMandatoRegolarizzazione) con
     * tante righe (metodo creaMandatoRiga) quanti sono gli impegni selezionati
     * dall'utente. Con il metodo 'aggiornaImportoObbligazione' vengono
     * incrementati gli importi (im_associato_doc_contabili) degli impegni
     * selezionati con l'importo trasferito nel mandato. Con il metodo
     * 'aggiornaCapitoloSaldoRiga' vengono aggiornati i saldi relativi ai
     * capitoli residui degli impegni selezionati. Viene creata una reversale
     * provvisoria di trasferimento per il Cds che beneficia del trasferimento
     * (metodo 'creaReversaleDiRegolarizzazione') creazione di 2 mandati di
     * trasferimento residuo+competenza PreCondition: E' stata generata la
     * richiesta di creazione un Mandato di trasferimento e l'utente ha
     * selezionato sia impegni residui che di competenza PostCondition: Vengono
     * creati 2 mandati uno di competenza e uno residuo e sono da considerarsi
     * valide entrambe le postconditions: 'creazione mandato di trasferimento
     * residuo' e 'creazione mandato di trasferimento competenza'
     * <p>
     * creazione di mandati di pagamento fatture estere PreCondition: E' stata
     * generata la richiesta di creazione un Mandato Le fatture selezionate sono
     * fatture istituzionali di beni dalla Repubblica di san marino e da paesi
     * intra UE PostCondition: Oltre alle PostCondition presenti in 'creazione
     * mandato', viene generata in automatico una reversale di incasso (metodo
     * creaReversaleDiIncassoIVA) dell'iVA
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> il mandato da creare
     * @return wizard il Mandato di Accreditamento creato bulk il Mandato
     * (ordinario) creato
     */
    public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk)
            throws ComponentException {
        try {
            if (this instanceof MandatoAutomaticoComponent)
                return super.creaConBulk(userContext, bulk);

            if (bulk instanceof MandatoAccreditamentoWizardBulk) {
                MandatoAccreditamentoWizardBulk wizard = (MandatoAccreditamentoWizardBulk) bulk;
                MandatoAccreditamentoBulk mandatoCompetenza = null, mandatoResiduo = null;
                Documento_genericoBulk docCompetenza = null, docResiduo = null;
                Mandato_rigaBulk mRiga;
                V_impegnoBulk impegno;
                ReversaleBulk reversale;
                SaldoComponentSession session = createSaldoComponentSession();
                for (Iterator i = wizard.getImpegniSelezionatiColl().iterator(); i
                        .hasNext(); ) {
                    impegno = (V_impegnoBulk) i.next();
                    if (impegno.isCompetenza()) {
                        if (mandatoCompetenza == null) {
                            mandatoCompetenza = creaMandatoAccreditamento(
                                    userContext, wizard,
                                    MandatoBulk.TIPO_COMPETENZA);
                            docCompetenza = docGenerico_creaDocumentoGenerico(
                                    userContext, mandatoCompetenza, wizard
                                            .getImpegniSelezionatiColl());
                        }
                        mRiga = creaMandatoRiga(userContext, wizard,
                                mandatoCompetenza, impegno, docCompetenza);
                    } else // residuo
                    {
                        if (mandatoResiduo == null) {
                            mandatoResiduo = creaMandatoAccreditamento(
                                    userContext, wizard,
                                    MandatoBulk.TIPO_RESIDUO);
                            docResiduo = docGenerico_creaDocumentoGenerico(
                                    userContext, mandatoResiduo, wizard
                                            .getImpegniSelezionatiColl());
                        }
                        mRiga = creaMandatoRiga(userContext, wizard,
                                mandatoResiduo, impegno, docResiduo);
                    }
                    aggiornaCapitoloSaldoRiga(userContext, mRiga, session);
                }
                if (mandatoCompetenza != null) {
                    mandatoCompetenza.refreshImporto();
                    verificaMandato(userContext, mandatoCompetenza);
                    aggiornaImportoObbligazioni(userContext, mandatoCompetenza);
                    super.creaConBulk(userContext, mandatoCompetenza);
                    aggiornaStatoFattura(userContext, mandatoCompetenza,
                            INSERIMENTO_MANDATO_ACTION);
                    wizard.getMandatiColl().add(mandatoCompetenza);
                    reversale = creaReversaleDiTrasferimento(userContext,
                            mandatoCompetenza);
                }
                if (mandatoResiduo != null) {
                    mandatoResiduo.refreshImporto();
                    verificaMandato(userContext, mandatoResiduo);
                    aggiornaImportoObbligazioni(userContext, mandatoResiduo);
                    super.creaConBulk(userContext, mandatoResiduo);
                    aggiornaStatoFattura(userContext, mandatoResiduo,
                            INSERIMENTO_MANDATO_ACTION);
                    wizard.getMandatiColl().add(mandatoResiduo);
                    reversale = creaReversaleDiTrasferimento(userContext,
                            mandatoResiduo);
                }
                return wizard;
            }
            /* MANDATO DI ACCREDITAMENTO END */
            else {
                /* MANDATO NON DI ACCREDITAMENTO BEGIN */
                MandatoBulk mandato = (MandatoBulk) bulk;

                // check and lock i doc.amm.
                for (Iterator i = mandato.getMandato_rigaColl().iterator(); i
                        .hasNext(); )
                    checkDocAmmCambiato(userContext, (Mandato_rigaBulk) i
                            .next());
                aggiornaCausale(userContext, mandato);
                verificaMandato(userContext, mandato);
                aggiornaImportoSospesi(userContext, mandato);
                Mandato_rigaBulk riga;
                SaldoComponentSession session = createSaldoComponentSession();
                for (Iterator i = mandato.getMandato_rigaColl().iterator(); i
                        .hasNext(); ) {
                    riga = (Mandato_rigaBulk) i.next();
                    aggiornaCapitoloSaldoRiga(userContext, riga, session);
                }
                aggiornaImportoObbligazioni(userContext, mandato);

                if (mandato.getTi_mandato().equals(
                        MandatoBulk.TIPO_REGOLARIZZAZIONE))
                    mandato.setIm_pagato(mandato.getIm_mandato());

                mandato = (MandatoBulk) super.creaConBulk(userContext, bulk);
                final MandatoBulk mandatoAnnPerSostituzione = getMandatoAnnPerSostituzione(userContext, mandato);
                if (Optional.ofNullable(mandatoAnnPerSostituzione).isPresent()) {
                    if (Optional.ofNullable(mandatoAnnPerSostituzione.getPg_mandato_riemissione()).isPresent()) {
                        Optional<MandatoBulk> mandatoBulk = Optional.ofNullable(super.findByPrimaryKey(userContext,
                                new MandatoIBulk(mandatoAnnPerSostituzione.getCd_cds(), mandatoAnnPerSostituzione.getEsercizio(), mandatoAnnPerSostituzione.getPg_mandato_riemissione())))
                                .filter(MandatoBulk.class::isInstance)
                                .map(MandatoBulk.class::cast);
                        if (mandatoBulk.isPresent()) {
                            mandatoBulk.get().setFl_riemissione(Boolean.TRUE);
                            mandatoBulk.get().setPg_mandato_riemissione(mandato.getPg_mandato());
                            mandatoBulk.get().setToBeUpdated();
                            super.modificaConBulk(userContext, mandatoBulk.get());
                        }
                    }
                }
                aggiornaStatoFattura(userContext, mandato,
                        INSERIMENTO_MANDATO_ACTION);
                /*
                 * 24/09/2002 Commentata la chiamata alla stored procedure per
                 * l'aggiornamento dei saldi, in quanto adesso non si imposta
                 * più a PAGATO lo stato di un Mandato, quando viene associato
                 * ad un sospeso
                 */
                // if ( mandato.getIm_pagato().compareTo(
                // mandato.getIm_mandato()) == 0 )
                // aggiornaSaldoPagato( userContext, mandato,
                // INSERIMENTO_MANDATO_ACTION );
                if (mandato.getTi_mandato().equals(
                        MandatoBulk.TIPO_REGOLARIZZAZIONE)) {
                    aggiornaSaldoPagato(userContext, mandato,
                            INSERIMENTO_MANDATO_ACTION);

                    /*
                     * REVERSALE DI REGOLARIZZAZIONE
                     */
                    ReversaleBulk reversale = creaReversaleDiRegolarizzazione(
                            userContext, mandato);

                    /*
                     * VARIAZIONE DI BILANCIO Se il CDS ementte un mandato di
                     * regolarizzazione su propri capitoli di spesa deve: 1.
                     * generare sempre la variazione di bilancio se la reversale
                     * di regolarizzazione emessa è su accertamenti o partite di
                     * giro dell'Ente 2. mai in tutti gli altri casi
                     */
                    Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(
                            userContext, Unita_organizzativa_enteBulk.class)
                            .findAll().get(0);
                    if (!((CNRUserContext) userContext)
                            .getCd_unita_organizzativa().equals(
                                    ente.getCd_unita_organizzativa()))
                        if (!((Mandato_rigaIBulk) mandato.getMandato_rigaColl()
                                .get(0)).getFl_pgiro().booleanValue())
                            // Il mandato è stato emesso dal CDS su capitoli di
                            // spesa propri
                            if (reversale.getCd_unita_organizzativa().equals(
                                    ente.getCd_unita_organizzativa())) {
                                // Ricarico il mandato che potrebbe essere stato
                                // aggiornato dalla creazione reversale
                                bulk = inizializzaBulkPerModifica(
                                        userContext, mandato);
                                if (Utility.createParametriCnrComponentSession().getParametriCnr(userContext, mandato.getEsercizio()).getFl_tesoreria_unica().booleanValue()) {
                                    ((MandatoIBulk) bulk)
                                            .setStato_coge(MandatoIBulk.STATO_COGE_X);
                                    bulk.setToBeUpdated();
                                    bulk = super.modificaConBulk(
                                            userContext, bulk);
                                    mandato = (MandatoIBulk) bulk;
                                } else {
                                    // La reversale è stata emessa sull'ente
                                    Var_bilancioBulk varBilancio = creaVariazioneBilancioDiRegolarizzazione(
                                            userContext, (MandatoIBulk) bulk);

                                    ((MandatoIBulk) bulk)
                                            .setStato_coge(MandatoIBulk.STATO_COGE_X);
                                    bulk.setToBeUpdated();
                                    bulk = super.modificaConBulk(
                                            userContext, bulk);

                                    ((MandatoIBulk) bulk)
                                            .setVar_bilancio(varBilancio);
                                    mandato = (MandatoIBulk) bulk;
                                }
                            }
                }
                if (((MandatoIBulk) mandato).getImReversaleDiIncassoIVA().compareTo(new BigDecimal(0)) > 0 && !Optional.ofNullable(mandatoAnnPerSostituzione).isPresent()) {
                    creaReversaleDiIncassoIVA(userContext, (MandatoIBulk) mandato);
                }
            }
            /* MANDATO NON DI ACCREDITAMENTO END */
            /**
             * Verifica CIG su fatture
             */
            if (bulk instanceof MandatoIBulk) {
                verificaCIGSUFatture(userContext, (MandatoIBulk) bulk);
            }
            return bulk;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private void aggiornaCausale(UserContext userContext, MandatoBulk mandato) {
        final String dsCUP = mandato.getMandato_rigaColl()
                .stream()
                .flatMap(mandato_rigaBulk -> mandato_rigaBulk.getMandato_siopeColl().stream())
                .flatMap(mandato_siopeBulk -> mandato_siopeBulk.getMandatoSiopeCupColl().stream())
                .map(MandatoSiopeCupBulk::getCdCup)
                .distinct()
                .filter(s -> !Optional.ofNullable(mandato.getDs_mandato()).orElse(" ").contains(s))
                .map(s -> "CUP ".concat(s))
                .collect(Collectors.joining(" "));
        if (Optional.ofNullable(dsCUP).filter(s -> s.length() > 0).isPresent()) {
            mandato.setDs_mandato(
                    Optional.ofNullable(mandato.getDs_mandato())
                            .map(s -> {
                                final String concat = dsCUP.concat(" ").concat(s);
                                return Optional.ofNullable(concat)
                                        .filter(s1 -> s1.length() > DS_MANDATO_MAX_LENGTH)
                                        .map(s1 -> s1.substring(0, DS_MANDATO_MAX_LENGTH))
                                        .orElse(concat);
                            })
                            .orElseGet(() -> mandato.getDs_mandato())
            );
        }
    }

    private MandatoBulk getMandatoAnnPerSostituzione(UserContext userContext, MandatoBulk mandato) throws ComponentException {
        final BulkList<Mandato_rigaBulk> mandato_rigaColl = mandato.getMandato_rigaColl();
        Mandato_rigaHome mandato_rigaHome = (Mandato_rigaHome) getHome(userContext, Mandato_rigaBulk.class);
        final Stream<?> mandatiAnnullatiPerSostituzione = mandato_rigaColl.stream()
                .map(mandato_rigaBulk -> {
                    try {
                        return getDocumentoAmministrativoSpesaBulk(userContext, mandato_rigaBulk);
                    } catch (ComponentException e) {
                        return null;
                    }
                })
                .map(iDocumentoAmministrativoSpesaBulk -> {
                    if (Optional.ofNullable(iDocumentoAmministrativoSpesaBulk).isPresent()) {
                        try {
                            final List<Mandato_rigaIBulk> righe = mandato_rigaHome.findRighe(userContext, iDocumentoAmministrativoSpesaBulk);
                            getHomeCache(userContext).fetchAll(userContext);
                            return righe.stream()
                                    .map(Mandato_rigaBulk::getMandato)
                                    .filter(mandatoBulk -> Optional.ofNullable(mandatoBulk.getStatoVarSos()).isPresent())
                                    .filter(mandatoBulk -> mandatoBulk.getStatoVarSos().equals(StatoVariazioneSostituzione.ANNULLATO_PER_SOSTITUZIONE.value()))
                                    .collect(Collectors.toList());
                        } catch (PersistencyException | ComponentException e) {
                            return Collections.emptyList();
                        }
                    }
                    return Collections.emptyList();
                }).flatMap(List::stream);
        return mandatiAnnullatiPerSostituzione
                .distinct()
                .filter(MandatoBulk.class::isInstance)
                .map(MandatoBulk.class::cast)
                .distinct()
                .findAny()
                .orElse(null);

    }
    /*
     * creazione mandato di accreditamento PreCondition: E' stata generata la
     * richiesta di creazione un Mandato di accreditamento PostCondition: Viene
     * creato un mandato di tipo Accreditamento con CDS/UO = Ente e CDS/UO
     * origine = CDS/UO di scrivania. Viene creato un mandatoTerzo coi dati
     * relativi al terzo CDS verso cui il mandato e' emesso
     *
     * @param userContext lo <code>UserContext</code> che ha generato la
     * richiesta
     *
     * @param wizard <code>MandatoAccreditamentoWizardBulk</code> il mandato di
     * accreditamento
     *
     * @param ti_competenza_residuo tipologia (competenza/residuo) del mandato
     * da creare
     *
     * @return mandato <code>MandatoAccreditamentoBulk</code> il mandato di
     * regolarizzazione creato
     */

    private MandatoAccreditamentoBulk creaMandatoAccreditamento(
            UserContext userContext, MandatoAccreditamentoWizardBulk wizard,
            String ti_competenza_residuo) throws ComponentException {
        try {
            MandatoAccreditamentoBulk mandato = new MandatoAccreditamentoBulk();
            mandato.setToBeCreated();
            mandato.setUser(wizard.getUser());
            mandato.setEsercizio(wizard.getEsercizio());
            mandato.setCodice_cds(wizard.getCodice_cds());
            mandato.setCds(wizard.getCds());
            mandato.setUnita_organizzativa(wizard.getUnita_organizzativa());
            mandato
                    .setCd_cds_origine(((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
                            .getCd_cds());
            mandato
                    .setCd_uo_origine(((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
                            .getCd_unita_organizzativa());
            mandato.setStato(MandatoBulk.STATO_MANDATO_EMESSO);
            mandato.setDt_emissione(wizard.getDt_emissione());
            mandato.setIm_mandato(new BigDecimal(0));
            mandato.setIm_pagato(new BigDecimal(0));
            mandato.setDs_mandato(MandatoAccreditamentoBulk.DS_MANDATO_ACCREDITAMENTO
                    + wizard.getCodice_cds());
            mandato.setTi_mandato(MandatoBulk.TIPO_ACCREDITAMENTO);
            mandato
                    .setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_MAN);
            mandato.setTi_competenza_residuo(ti_competenza_residuo);
            mandato.setBanca(wizard.getBanca());
            mandato.setModalita_pagamento(wizard.getModalita_pagamento());
            mandato
                    .setStato_trasmissione(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
            mandato.setStato_coge(MandatoBulk.STATO_COGE_N);
            mandato.setIm_ritenute(new java.math.BigDecimal(0));

            mandato.setMandato_terzo(new MandatoAccreditamento_terzoBulk(
                    mandato, wizard.getMandato_terzo()));
            return mandato;
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /*
     * creazione riga di mandato di accreditamento PreCondition: E' stata
     * generata la richiesta di creazione di una riga di Mandato di
     * accreditamento PostCondition: Viene creata una riga di mandato coi dati
     * relativi all'impegno selezionato dall'utente e al documento generico di
     * spesa (TRASF_S) creato in automatico alla crezione del mandato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la
     * richiesta
     *
     * @param wizard <code>MandatoAccreditamentoBulk</code> il mandato di
     * accreditamento
     *
     * @param mandato <code>MandatoBulk</code> il mandato
     *
     * @param impegno <code>V_impegnoBulk</code> l'impegno selezionato
     * dall'utente
     *
     * @param documento <code>Documento_genericoBulk</code> il documento
     * generico di spesa
     *
     * @return riga <code>Mandato_rigaBulk</code> la riga di mandato creata
     */

    private Mandato_rigaBulk creaMandatoRiga(UserContext userContext,
                                             MandatoAccreditamentoBulk wizard, MandatoBulk mandato,
                                             V_impegnoBulk impegno, Documento_genericoBulk documento)
            throws ComponentException {
        try {
            MandatoAccreditamento_rigaBulk riga = new MandatoAccreditamento_rigaBulk();
            riga.setToBeCreated();
            riga.setUser(mandato.getUser());
            riga.setStato(Mandato_rigaBulk.STATO_INIZIALE);
            riga.setIm_mandato_riga(impegno.getIm_da_trasferire());
            riga.setIm_ritenute_riga(new java.math.BigDecimal(0));
            riga.setMandato(mandato);
            riga.setImpegno(impegno);

            riga.setFl_pgiro(new Boolean(false));
            riga.setEsercizio_obbligazione(impegno.getEsercizio());
            riga
                    .setEsercizio_ori_obbligazione(impegno
                            .getEsercizio_originale());
            riga.setPg_obbligazione(impegno.getPg_obbligazione());
            riga.setPg_obbligazione_scadenzario(impegno
                    .getPg_obbligazione_scadenzario());

            riga.setEsercizio_doc_amm(documento.getEsercizio());
            riga.setCd_cds_doc_amm(documento.getCd_cds());
            riga.setCd_uo_doc_amm(documento.getCd_unita_organizzativa());
            riga.setPg_doc_amm(documento.getPg_documento_generico());
            riga.setCd_tipo_documento_amm(documento.getCd_tipo_documento_amm());
            riga.setPg_ver_rec_doc_amm(documento.getPg_ver_rec());

            riga.setBancaOptions(wizard.getBancaOptions());
            riga.setBanca(wizard.getBanca());

            riga.setModalita_pagamentoOptions(wizard
                    .getModalita_pagamentoOptions());
            riga.setModalita_pagamento(wizard.getModalita_pagamento());

            ((MandatoAccreditamentoBulk) mandato).addToMandato_rigaColl(riga,
                    impegno);

            // Carico automaticamente i codici SIOPE e visualizzo quelli ancora
            // collegabili se la gestione è attiva
            if (Utility.createParametriCnrComponentSession().getParametriCnr(
                    userContext, mandato.getEsercizio()).getFl_siope()
                    .booleanValue()) {
                riga = (MandatoAccreditamento_rigaBulk) aggiornaLegameSIOPE(
                        userContext, riga);
                riga = (MandatoAccreditamento_rigaBulk) setCodiciSIOPECollegabili(
                        userContext, riga);
            }

            return riga;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /*
     * creazione riga di mandato PreCondition: E' stata generata la richiesta di
     * creazione di una riga di Mandato PostCondition: Viene creata una riga di
     * mandato coi dati relativi alla scadenza di obbligazione selezionata
     * dall'utente e al documento ammninistrativo di spesa selezionato
     * dall'utente. Vengono inoltre impostati come dati relativi alla banca e
     * alla modalità di pagamento quelli presenti nel documento amministrativo
     *
     * @param userContext lo <code>UserContext</code> che ha generato la
     * richiesta
     *
     * @param mandato <code>MandatoBulk</code> il mandato
     *
     * @param docPassivo <code>V_doc_passivo_obbligazioneBulk</code> il
     * documento ammninistrativo di spesa selezionato dall'utente
     *
     * @return riga <code>Mandato_rigaBulk</code> la riga di mandato creata
     */

    protected Mandato_rigaBulk creaMandatoRiga(UserContext userContext,
                                               MandatoBulk mandato, V_doc_passivo_obbligazioneBulk docPassivo)
            throws ComponentException {
        try {
            Mandato_rigaIBulk riga = new Mandato_rigaIBulk();
            riga.setToBeCreated();
            riga.setUser(mandato.getUser());
            riga.setStato(Mandato_rigaBulk.STATO_INIZIALE);
            riga.setIm_mandato_riga(docPassivo.getIm_mandato_riga());
            if (docPassivo.getFl_fai_reversale().booleanValue()) {
                riga.setIm_ritenute_riga(docPassivo.getIm_iva_doc_amm());
                ((MandatoIBulk) mandato).setFaiReversale(true);
            } else
                riga.setIm_ritenute_riga(new BigDecimal(0));
            riga.setMandato(mandato);
            riga.setEsercizio_obbligazione(docPassivo
                    .getEsercizio_obbligazione());
            riga.setEsercizio_ori_obbligazione(docPassivo
                    .getEsercizio_ori_obbligazione());
            riga.setPg_obbligazione(docPassivo.getPg_obbligazione());
            riga.setPg_obbligazione_scadenzario(docPassivo
                    .getPg_obbligazione_scadenzario());
            riga.setFl_pgiro(docPassivo.getFl_pgiro());
            riga.setCd_uo_doc_amm(docPassivo.getCd_unita_organizzativa());
            riga.setCd_cds_doc_amm(docPassivo.getCd_cds());
            riga.setEsercizio_doc_amm(docPassivo.getEsercizio());
            riga
                    .setCd_tipo_documento_amm(docPassivo
                            .getCd_tipo_documento_amm());
            riga.setPg_doc_amm(docPassivo.getPg_documento_amm());
            riga.setPg_ver_rec_doc_amm(docPassivo.getPg_ver_rec());
            riga.setFlCancellazione(docPassivo.getFl_selezione()
                    .equalsIgnoreCase("Y"));
            riga.setTi_fattura(docPassivo.getTi_fattura());
            riga.setPg_lettera(docPassivo.getPg_lettera());
            riga.setCd_sospeso(docPassivo.getCd_sospeso());
            ((Mandato_rigaHome) getHome(userContext, riga.getClass()))
                    .initializeElemento_voce(userContext, riga);

            // imposto il terzo
            if (docPassivo.getCodice_terzo_cedente() != null) {
                TerzoBulk cedente = (TerzoBulk) getHome(userContext,
                        TerzoBulk.class).findByPrimaryKey(
                        new TerzoBulk(docPassivo.getCodice_terzo_cedente()));
                riga.setTerzo_cedente(cedente);
            }
            BancaBulk banca = new BancaBulk();
            TerzoBulk terzo = new TerzoBulk(docPassivo
                    .getCodice_terzo_o_cessionario());
            banca.setTerzo(terzo);
            riga.setBanca(banca);

            if (!MandatoBulk.TIPO_REGOLARIZZAZIONE.equals(mandato
                    .getTi_mandato())) {
                banca.setPg_banca(docPassivo.getPg_banca());
                banca = (BancaBulk) getHome(userContext, BancaBulk.class)
                        .findByPrimaryKey(banca);
                if (banca == null)
                    throw new ApplicationException(
                            "Attenzione! Le coordinate bancarie specificate nel doc. amministrativo per il terzo "
                                    + docPassivo
                                    .getCodice_terzo_o_cessionario()
                                    + " non sono valide");
                riga.setBanca(banca);
                riga.setBancaOptions(findBancaOptions(userContext, riga));

                Modalita_pagamentoBulk mod_pagamento = new Modalita_pagamentoBulk();
                mod_pagamento.setTerzo(terzo);
                Rif_modalita_pagamentoBulk rif_modalita_pagamento = new Rif_modalita_pagamentoBulk(
                        docPassivo.getCd_modalita_pag());
                mod_pagamento.setRif_modalita_pagamento(rif_modalita_pagamento);
                mod_pagamento = (Modalita_pagamentoBulk) getHome(userContext,
                        Modalita_pagamentoBulk.class).findByPrimaryKey(
                        mod_pagamento);
                riga.setModalita_pagamento(mod_pagamento);
                riga
                        .setModalita_pagamentoOptions(findModalita_pagamentoOptions(
                                userContext, riga));
            }

            ((MandatoIBulk) mandato).addToMandato_rigaColl(riga, docPassivo);

            if (docPassivo.getCd_sospeso() != null)
                ((MandatoIBulk) mandato).getSospesiDa1210List().add(
                        docPassivo.getCd_sospeso());

            // Carico automaticamente i codici SIOPE e visualizzo quelli ancora
            // collegabili se la gestione è attiva
            if (Utility.createParametriCnrComponentSession().getParametriCnr(
                    userContext, mandato.getEsercizio()).getFl_siope()
                    .booleanValue()) {
                riga = (Mandato_rigaIBulk) aggiornaLegameSIOPE(userContext,
                        riga);
                riga = (Mandato_rigaIBulk) setCodiciSIOPECollegabili(
                        userContext, riga);
            }

            return riga;
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /*
     * creazione riga collegata di mandato PreCondition: E' stata generata la
     * richiesta di creazione di una riga di Mandato relativa ad un documento
     * (nota credito/debito) collegato ad una fattura (riga principale già
     * creata) PostCondition: Per le fatture passive per le quali è necessario
     * generare la reversale di incasso IVA, l'importo relativo alle ritenute
     * della riga collegata a fattura (rigaPrincipale) deve essere
     * incrementato/decrementato con l'importo delle eventuali note di
     * credito/debito collegate a quella fattura. Tutte le righe di mandato
     * relative a note di credito/debito hanno gli importi (sia im_riga che
     * im_ritenute) a 0 e il loro valore viene sommato sulla riga principale
     * relativa alla fattura
     *
     * @param userContext lo <code>UserContext</code> che ha generato la
     * richiesta
     *
     * @param mandato <code>MandatoBulk</code> il mandato
     *
     * @param docPassivo <code>V_doc_passivo_obbligazioneBulk</code> il
     * documento ammninistrativo di tipo nota credito/debito
     *
     * @param rigaPrincipale <code>Mandato_rigaBulk</code> riga di mandato già
     * creata relativa alla fattura passiva da cui il docPassivo dipende
     *
     * @return riga <code>Mandato_rigaBulk</code> la riga di mandato creata
     */

    private Mandato_rigaBulk creaMandatoRigaCollegato(UserContext userContext,
                                                      MandatoBulk mandato, V_doc_passivo_obbligazioneBulk docPassivo,
                                                      Mandato_rigaBulk rigaPrincipale) throws ComponentException {
        try {
            if (rigaPrincipale.getIm_ritenute_riga().compareTo(
                    new BigDecimal(0)) != 0)
            // se la riga principale è una fattura passiva con ritenute,
            // aggiungo/tolgo le ritenute delle note di credito/debito
            {
                if (docPassivo.getIm_iva_doc_amm().compareTo(new BigDecimal(0)) != 0)
                    rigaPrincipale.setIm_ritenute_riga(rigaPrincipale
                            .getIm_ritenute_riga().add(
                                    docPassivo.getIm_iva_doc_amm()));
            }

            return creaMandatoRiga(userContext, mandato, docPassivo);
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /*
     * creazione mandato terzo PreCondition: E' stata generata la richiesta di
     * creazione di un Mandato_terzoBulk PostCondition: Viene creata una istanza
     * di Mandato_terzoBulk coi dati del beneficiario del mandato e viene
     * impostato il tipo bollo di default.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la
     * richiesta
     *
     * @param mandato <code>MandatoBulk</code> il mandato
     *
     * @param cd_terzo Il codice del beneficiario del mandato
     *
     * @return mTerzo l'istanza di <code>Mandato_terzoBulk</code> creata
     */

    private Mandato_terzoBulk creaMandatoTerzo(UserContext userContext,
                                               MandatoBulk mandato, Integer cd_terzo) throws ComponentException {
        try {
            Mandato_terzoIBulk mTerzo = new Mandato_terzoIBulk();
            mTerzo.setToBeCreated();
            mTerzo.setUser(mandato.getUser());
            mTerzo.setMandato(mandato);

            // imposto il terzo
            TerzoBulk terzo = (TerzoBulk) getHome(userContext, TerzoBulk.class)
                    .findByPrimaryKey(new TerzoKey(cd_terzo));
            mTerzo.setTerzo(terzo);
            // imposto il tipo bollo di default
            mTerzo.setTipoBollo(((Tipo_bolloHome) getHome(userContext,
                    Tipo_bolloBulk.class))
                    .findTipoBolloDefault(Tipo_bolloBulk.TIPO_SPESA));
            return mTerzo;
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /*
     * creazione mandato terzo per Mandato di accreditamento non SAC
     * PreCondition: E' stata generata la richiesta di creazione di un
     * Mandato_terzoBulk per un Mandato di accreditamento Il Cds beneficiario
     * del mandato è un cds con tipo diverso da SAC PostCondition: Viene creata
     * una istanza di Mandato_terzoBulk coi dati dell'uo-cds del Cds
     * beneficiario del mandato di accreditamento e viene impostato il tipo
     * bollo di default.
     *
     * creazione mandato terzo per Mandato di accreditamento SAC PreCondition:
     * E' stata generata la richiesta di creazione di un Mandato_terzoBulk per
     * un Mandato di accreditamento Il Cds beneficiario del mandato è un cds con
     * tipo uguale a SAC PostCondition: Viene creata una istanza di
     * Mandato_terzoBulk coi dati dell'uo specificata in Configurazione CNR per
     * il CDS SAC e viene impostato il tipo bollo di default.
     *
     *
     * @param userContext lo <code>UserContext</code> che ha generato la
     * richiesta
     *
     * @param mandato <code>MandatoAccreditamentoWizardBulk</code> il mandato di
     * accreditamento
     *
     * @return mTerzo l'istanza di <code>Mandato_terzoBulk</code> creata per un
     * Mandato di accreditamento
     */

    private Mandato_terzoBulk creaMandatoTerzoPerCds(UserContext userContext,
                                                     MandatoAccreditamentoWizardBulk mandato) throws ComponentException {
        try {
            MandatoAccreditamento_terzoBulk mTerzo = new MandatoAccreditamento_terzoBulk();
            mTerzo.setToBeCreated();
            mTerzo.setUser(mandato.getUser());
            mTerzo.setMandato(mandato);

            // verifico se è il cds SAC
            boolean isSac = false;
            SQLBuilder sql = getHome(userContext, CdsBulk.class)
                    .createSQLBuilder();
            sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS,
                    mandato.getCodice_cds());
            sql.addSQLClause("AND", "CD_TIPO_UNITA", SQLBuilder.EQUALS,
                    Tipo_unita_organizzativaHome.TIPO_UO_SAC);
            List result = getHome(userContext, CdsBulk.class).fetchAll(sql);
            if (result.size() != 0)
                isSac = true;

            // cerco l'unità organizzativa
            String cd_uo = null;

            if (!isSac) {
                sql = getHome(userContext, Unita_organizzativaBulk.class)
                        .createSQLBuilder();
                sql.addSQLClause("AND", "CD_UNITA_PADRE", SQLBuilder.EQUALS, mandato
                        .getCodice_cds());
                sql.addSQLClause("AND", "FL_UO_CDS", SQLBuilder.EQUALS, "Y");
                result = getHome(userContext, Unita_organizzativaBulk.class)
                        .fetchAll(sql);
                if (result.size() != 1)
                    throw new ApplicationException(
                            "Non è possibile identificare l'uo-cds per il Cds "
                                    + mandato.getCodice_cds());
                cd_uo = ((Unita_organizzativaBulk) result.get(0))
                        .getCd_unita_organizzativa();
            } else {
                cd_uo = Optional.ofNullable(((Configurazione_cnrHome)getHome(userContext,Configurazione_cnrBulk.class)).getUoAccreditamentoSac(CNRUserContext.getEsercizio(userContext)))
                        .orElseThrow(()->new ApplicationException("Configurazione CNR: manca la definizione dell'UO_SPECIALE per ACCREDITAMENTO SAC per l'esercizio "+CNRUserContext.getEsercizio(userContext)+"."));
            }

            // imposto il terzo

            sql = getHome(userContext, TerzoBulk.class).createSQLBuilder();
            sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, cd_uo);
            result = getHome(userContext, TerzoBulk.class).fetchAll(sql);
            if (result.size() == 0)
                throw handleException(new ApplicationException(
                        " Impossibile emettere il mandato: l'UO " + cd_uo
                                + " non e' stata codificata in anagrafica"));
            mTerzo.setTerzo((TerzoBulk) result.get(0));

            // imposto il tipo bollo di default
            mTerzo.setTipoBollo(((Tipo_bolloHome) getHome(userContext,
                    Tipo_bolloBulk.class))
                    .findTipoBolloDefault(Tipo_bolloBulk.TIPO_SPESA));
            return mTerzo;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private ReversaleBulk creaReversaleDiIncassoIVA(UserContext userContext,
                                                    MandatoIBulk mandato) throws ComponentException {
        try {
            ReversaleComponentSession revSession = createReversaleComponentSession();
            ReversaleBulk reversale = revSession.creaReversaleDiIncassoIVA(
                    userContext, mandato);
            creaAss_mandato_reversale(userContext, mandato, reversale);
            return reversale;
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /*
     * creazione reversale di regolarizzazione PreCondition: E' stata generata
     * la richiesta di creazione di una reversale di regolarizzazione
     * PostCondition: Viene richiesta alla Component che gestisce le Reversali
     * la creazione di una reversale di regolarizzazione a partire dal mandato
     * di regolarizzazione. Viene creata una istanza dell'associazione fra
     * Mandato-Reversale (metodo creaAss_mandato_reversale)
     *
     * @param userContext lo <code>UserContext</code> che ha generato la
     * richiesta
     *
     * @param mandato <code>MandatoBulk</code> il mandato di regolarizzazione
     *
     * @return reversale <code>ReversaleBulk</code> la reversale di
     * regolarizzazione creata
     */

    private ReversaleBulk creaReversaleDiRegolarizzazione(
            UserContext userContext, MandatoBulk mandato)
            throws ComponentException {
        try {
            ReversaleBulk reversale = null;
            ReversaleComponentSession revSession = createReversaleComponentSession();
            reversale = revSession.creaReversaleDiRegolarizzazione(userContext,
                    mandato);
            creaAss_mandato_reversale(userContext, mandato, reversale);
            return reversale;

        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /*
     * creazione reversale di trasferimento PreCondition: E' stata generata la
     * richiesta di creazione di una reversale di trasferimento PostCondition:
     * Viene richiesta alla Component che gestisce le Reversali la creazione di
     * una reversale di trasferimento a partire dal mandato di trasferimento.
     * Viene creata una istanza dell'associazione fra Mandato-Reversale (metodo
     * creaAss_mandato_reversale)
     *
     * @param userContext lo <code>UserContext</code> che ha generato la
     * richiesta
     *
     * @param mandato <code>MandatoAccreditamentoBulk</code> il mandato di
     * trasferimento
     *
     * @return reversale <code>ReversaleBulk</code> la reversale di
     * trasferimento creata
     */

    private ReversaleBulk creaReversaleDiTrasferimento(UserContext userContext,
                                                       MandatoAccreditamentoBulk mandato) throws ComponentException {
        try {
            ReversaleComponentSession revSession = createReversaleComponentSession();
            ReversaleBulk reversale = revSession.creaReversaleDiTrasferimento(
                    userContext, mandato);
            creaAss_mandato_reversale(userContext, mandato, reversale);
            return reversale;
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative
     * agli Accertamenti sia su partite di giro che ordinari
     *
     * @return AccertamentoAbstractComponentSession l'istanza di
     * <code>AccertamentoAbstractComponentSession</code> che serve per
     * gestire un accertamento
     */
    private AccertamentoAbstractComponentSession createAccertamentoAbstractComponentSession()
            throws ComponentException {
        try {
            return (AccertamentoAbstractComponentSession) EJBCommonServices
                    .createEJB("CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative
     * agli Accertamenti
     *
     * @return AccertamentoComponentSession l'istanza di
     * <code>AccertamentoComponentSession</code> che serve per gestire
     * un accertamento
     */
    private AccertamentoComponentSession createAccertamentoComponentSession()
            throws ComponentException {
        try {
            return (AccertamentoComponentSession) EJBCommonServices
                    .createEJB("CNRDOCCONT00_EJB_AccertamentoComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni di lettura
     * della Configurazione CNR
     *
     * @return Configurazione_cnrComponentSession l'istanza di
     * <code>Configurazione_cnrComponentSession</code> che serve per
     * leggere i parametri di configurazione del CNR
     */
    private Configurazione_cnrComponentSession createConfigurazioneCnrComponentSession()
            throws ComponentException {
        try {
            return (Configurazione_cnrComponentSession) EJBCommonServices
                    .createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative
     * ai Documenti Amministrativi Generici
     *
     * @return DocumentoGenericoComponentSession l'istanza di
     * <code>DocumentoGenericoComponentSession</code> che serve per
     * gestire un documento generico
     */
    protected DocumentoGenericoComponentSession createDocumentoGenericoComponentSession()
            throws ComponentException {
        try {
            return (DocumentoGenericoComponentSession) EJBCommonServices
                    .createEJB("CNRDOCAMM00_EJB_DocumentoGenericoComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative
     * alle Fatture Passive
     *
     * @return FatturaPassivaComponentSession l'istanza di
     * <code>FatturaPassivaComponentSession</code> che serve per gestire
     * una fattura passiva
     */
    private FatturaPassivaComponentSession createFatturaPassivaComponentSession()
            throws ComponentException {
        try {
            return (FatturaPassivaComponentSession) EJBCommonServices
                    .createEJB("CNRDOCAMM00_EJB_FatturaPassivaComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative
     * alle Reversali
     *
     * @return ReversaleComponentSession l'istanza di
     * <code>ReversaleComponentSession</code> che serve per gestire una
     * reversale
     */
    private ReversaleComponentSession createReversaleComponentSession()
            throws ComponentException {
        try {
            return (ReversaleComponentSession) EJBCommonServices
                    .createEJB("CNRDOCCONT00_EJB_ReversaleComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative
     * ai Saldi sui capitoli del Piano dei Conti
     *
     * @return SaldoComponentSession l'istanza di
     * <code>SaldoComponentSession</code> che serve per aggiornare un
     * saldo
     */
    protected it.cnr.contab.doccont00.ejb.SaldoComponentSession createSaldoComponentSession()
            throws ComponentException {
        try {
            return (SaldoComponentSession) EJBCommonServices
                    .createEJB("CNRDOCCONT00_EJB_SaldoComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * annullamento documento amm.generico PreCondition: E' stata generata la
     * richiesta di annullamento di un documento generico PostCondition: Vengono
     * aggiornati gli importi associati ai documenti amministrativi delle
     * scadenze di obbligazione e/o accertamenti che erano stati utilizzati
     * nella contabilizzazione del documento annullato. N.B. L'impostazione
     * della data di annullamento del documento generico viene effettuata da una
     * stored proceduere
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param documento   <code>Documento_genericoBulk</code> il documento generico da
     *                    annullare
     * @return documento <code>Documento_genericoBulk</code> il documento
     * generico annullato
     */

    /* ATTENZIONE E' LO STESSO METODO DELLA REVERSALE */
    /*
     * ATTENZIONE le modififiche al documento generico vengono fatte dalla
     * stored procedure che aggiorna lo stato
     */
    public Documento_genericoBulk docGenerico_annullaDocumentoGenerico(
            UserContext userContext, Documento_genericoBulk documento)
            throws ComponentException {
        try {
            // documento.setDt_annullamento( getHome(userContext,
            // Documento_genericoBulk.class).getServerTimestamp());
            // documento.setToBeUpdated();
            /*
             * Documento_generico_rigaBulk riga; Obbligazione_scadenzarioBulk
             * os; Accertamento_scadenzarioBulk as; for ( Iterator i =
             * documento.getDocumento_generico_dettColl().iterator();
             * i.hasNext(); ) { riga = (Documento_generico_rigaBulk) i.next();
             * if ( riga.getAccertamento_scadenziario() != null ) { // as =
             * riga.getAccertamento_scadenziario(); as =
             * (Accertamento_scadenzarioBulk)getHome( userContext,
             * Accertamento_scadenzarioBulk.class ).findAndLock( new
             * Accertamento_scadenzarioBulk(
             * riga.getAccertamento_scadenziario().getCd_cds(),
             * riga.getAccertamento_scadenziario().getEsercizio(),
             * riga.getAccertamento_scadenziario().getEsercizio_originale(),
             * riga.getAccertamento_scadenziario().getPg_accertamento(),
             * riga.getAccertamento_scadenziario
             * ().getPg_accertamento_scadenzario())); AccertamentoBulk
             * accertamento = (AccertamentoBulk) getHome( userContext,
             * AccertamentoBulk.class ).findAndLock( as.getAccertamento()); //
             * accertamento.setUser( userContext.getUser()); // updateBulk(
             * userContext, accertamento ); lockBulk( userContext, accertamento
             * ); as.setIm_associato_doc_amm(
             * as.getIm_associato_doc_amm().subtract(riga.getIm_riga()));
             * as.setUser( userContext.getUser()); updateBulk( userContext, as
             * );
             *
             * } else if ( riga.getObbligazione_scadenziario() != null ) { os =
             * (Obbligazione_scadenzarioBulk)getHome( userContext,
             * Obbligazione_scadenzarioBulk.class ).findAndLock( new
             * Obbligazione_scadenzarioBulk(
             * riga.getObbligazione_scadenziario().getCd_cds(),
             * riga.getObbligazione_scadenziario().getEsercizio(),
             * riga.getObbligazione_scadenziario().getEsercizio_originale(),
             * riga.getObbligazione_scadenziario().getPg_obbligazione(),
             * riga.getObbligazione_scadenziario
             * ().getPg_obbligazione_scadenzario())); // os =
             * riga.getObbligazione_scadenziario(); ObbligazioneBulk
             * obbligazione = (ObbligazioneBulk) getHome( userContext,
             * ObbligazioneBulk.class ).findAndLock( os.getObbligazione()); //
             * obbligazione.setUser( userContext.getUser()); // updateBulk(
             * userContext, obbligazione); lockBulk( userContext, obbligazione);
             * os.setIm_associato_doc_amm(
             * os.getIm_associato_doc_amm().subtract(riga.getIm_riga()));
             * os.setUser( userContext.getUser()); updateBulk( userContext, os
             * ); }
             *
             * } // makeBulkPersistent( userContext, documento );
             */
            return documento;
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * creazione documento amm.generico PreCondition: E' stata generata la
     * richiesta di creazione di un documento generico di spesa di tipo TRASF_S
     * a partire da un mandato di accreditamento PostCondition: Un documento
     * viene creato con un numero di righe pari al numero di impegni selezionati
     * dall'utente di tipo uguale al tipo del mandato(competenza o residuo)
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoAccreditamentoBulk</code> il mandato di
     *                    accreditamento
     * @param impegni     La collezione di impegni selezionati dall'utente
     * @return documento <code>Documento_genericoBulk</code> il documento
     * generico di spesa creato
     */
    public Documento_genericoBulk docGenerico_creaDocumentoGenerico(
            UserContext userContext, MandatoAccreditamentoBulk mandato,
            Collection impegni) throws ComponentException {
        try {
            V_impegnoBulk impegno;
            Documento_generico_rigaBulk dRiga;
            Documento_genericoBulk documento = new Documento_genericoBulk();
            documento.setToBeCreated();
            documento.setUser(mandato.getUser());
            documento.setTi_entrate_spese(Documento_genericoBulk.SPESE);
            documento.setEsercizio(mandato.getEsercizio());
            documento.setCd_cds(mandato.getCd_cds());
            documento.setCd_unita_organizzativa(mandato
                    .getCd_unita_organizzativa());
            documento.setCd_cds_origine(mandato.getCd_cds_origine());
            documento.setCd_uo_origine(mandato.getCd_uo_origine());
            documento.setTipo_documento(new Tipo_documento_ammBulk(
                    Numerazione_doc_ammBulk.TIPO_TRASF_S));
            documento.setTi_istituz_commerc(Documento_genericoBulk.ISTITUZIONALE);
            documento.setStato_cofi(Documento_genericoBulk.STATO_CONTABILIZZATO);
            documento.setStato_coge(Documento_genericoBulk.NON_REGISTRATO_IN_COGE);
            // documento.setFl_modifica_coge(new Boolean( false));
            documento.setStato_coan(Documento_genericoBulk.NON_CONTABILIZZATO_IN_COAN);
            documento.setStato_pagamento_fondo_eco("N");
            documento.setTi_associato_manrev("T");
            documento.setData_registrazione(mandato.getDt_emissione());
            documento.setDt_a_competenza_coge(mandato.getDt_emissione());
            documento.setDt_da_competenza_coge(mandato.getDt_emissione());
            documento
                    .setDs_documento_generico("DOCUMENTO ASSOCIATO A MANDATO DI TRASFERIMENTO");
            documento.setIm_totale(mandato.getIm_mandato());
            DivisaBulk divisa = new DivisaBulk(
                    docGenerico_createConfigurazioneCnrComponentSession()
                            .getVal01(userContext, new Integer(0), "*",
                                    Configurazione_cnrBulk.PK_CD_DIVISA,
                                    Configurazione_cnrBulk.SK_EURO));
            documento.setValuta(divisa);
            documento.setCambio(new BigDecimal(1));
            for (Iterator i = impegni.iterator(); i.hasNext(); ) {
                impegno = (V_impegnoBulk) i.next();
                if (impegno.isCompetenza()
                        && mandato.getTi_competenza_residuo().equals(
                        MandatoBulk.TIPO_COMPETENZA))
                    dRiga = docGenerico_creaDocumentoGenericoRiga(userContext,
                            documento, impegno, mandato);
                else if (!impegno.isCompetenza()
                        && mandato.getTi_competenza_residuo().equals(
                        MandatoBulk.TIPO_RESIDUO))
                    dRiga = docGenerico_creaDocumentoGenericoRiga(userContext,
                            documento, impegno, mandato);
            }

            documento = (Documento_genericoBulk) createDocumentoGenericoComponentSession()
                    .creaConBulk(userContext, documento);
            return documento;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * creazione riga di documento amm.generico di spesa PreCondition: E' stata
     * generata la richiesta di creazione di una riga di documento generico di
     * spesa di tipo TRASF_S a partire da un impegno selezionato dall'utente nel
     * mandato di accreditamento PostCondition: Un riga di documento viene
     * creata con i dati relativi al terzo (codice terzo, coordinate bancarie,
     * modalità di pagamento) derivati da quelli che l'utente ha specificato nel
     * mandato e i dati relativi all'importo derivati dall'impegno selezionato
     * dall'utente nel mandato; viene inoltre aggiornato l'importo associato ai
     * documenti amministrativi della scadenza di obbligazione che rappresenta
     * l'impegno( scadenza.im_associato_doc_amm = scadenza.im_associato_doc_amm
     * + documento_riga.im_riga)
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param documento   <code>Documento_genericoBulk</code> il documento generico di
     *                    spesa
     * @param impegno     <code>V_impegnoBulk</code> l'impegno selezionato dall'utente
     *                    nel mandato di accreditamento
     * @param mandato     <code>MandatoAccreditamentoBulk</code> il mandato di
     *                    accreditamento
     * @return riga <code>Documento_generico_rigaBulk</code> la riga del
     * documento generico di spesa creata
     */

    public Documento_generico_rigaBulk docGenerico_creaDocumentoGenericoRiga(
            UserContext userContext, Documento_genericoBulk documento,
            V_impegnoBulk impegno, MandatoAccreditamentoBulk mandato)
            throws ComponentException {
        try {
            Documento_generico_rigaBulk riga = new Documento_generico_rigaBulk();
            riga.setToBeCreated();
            riga.setUser(documento.getUser());
            riga.setCd_cds(documento.getCd_cds());
            riga.setCd_unita_organizzativa(documento
                    .getCd_unita_organizzativa());
            riga.setCd_tipo_documento_amm(documento.getCd_tipo_documento_amm());
            riga.setStato_cofi(Documento_generico_rigaBulk.STATO_CONTABILIZZATO);
            riga.setDt_a_competenza_coge(documento.getData_registrazione());
            riga.setDt_da_competenza_coge(documento.getData_registrazione());
            riga.setTerzo(mandato.getMandato_terzo().getTerzo()); // CNR
            AnagraficoBulk anagrafico = (AnagraficoBulk) getHome(userContext,
                    AnagraficoBulk.class).findByPrimaryKey(
                    riga.getTerzo().getAnagrafico());
            riga.getTerzo().setAnagrafico(anagrafico);
            riga.setRagione_sociale(anagrafico.getRagione_sociale());
            riga.setNome(anagrafico.getNome());
            riga.setCognome(anagrafico.getCognome());
            riga.setCodice_fiscale(anagrafico.getCodice_fiscale());
            riga.setPartita_iva(anagrafico.getPartita_iva());
            riga.setModalita_pagamento(mandato.getModalita_pagamento()
                    .getRif_modalita_pagamento());
            riga.setBanca(mandato.getBanca());
            riga.setIm_riga(impegno.getIm_da_trasferire());
            riga.setIm_riga_divisa(impegno.getIm_da_trasferire());
            riga.setTi_associato_manrev("T");
            Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) getHome(
                    userContext, Obbligazione_scadenzarioBulk.class)
                    .findByPrimaryKey(
                            new Obbligazione_scadenzarioBulk(impegno
                                    .getCd_cds(), impegno.getEsercizio(),
                                    impegno.getEsercizio_originale(), impegno
                                    .getPg_obbligazione(), impegno
                                    .getPg_obbligazione_scadenzario()));
            getHomeCache(userContext).fetchAll(userContext);
            riga.setObbligazione_scadenziario(scadenza);
            riga.setDocumento_generico(documento);
            documento.getDocumento_generico_dettColl().add(riga);

            // aggiorno im_assciato_doc_amm della scadenza
            lockBulk(userContext, scadenza.getObbligazione());
            // scadenza.getObbligazione().setUser(userContext.getUser());
            // updateBulk( userContext, scadenza.getObbligazione());
            scadenza.setIm_associato_doc_amm(scadenza.getIm_associato_doc_amm()
                    .add(riga.getIm_riga()));
            updateBulk(userContext, scadenza);

            return riga;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative
     * alla Configurazione CNR
     *
     * @return Configurazione_cnrComponentSession l'istanza di
     * <code>Configurazione_cnrComponentSession</code> che serve per
     * leggere i parametri di configurazione del CNR
     */
    protected Configurazione_cnrComponentSession docGenerico_createConfigurazioneCnrComponentSession()
            throws ComponentException {
        try {
            return (Configurazione_cnrComponentSession) EJBCommonServices
                    .createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /* non usato */
    public void eliminaConBulk(UserContext userContext, OggettoBulk bulk)
            throws it.cnr.jada.comp.ComponentException {

    }

    protected void eliminaVsx(UserContext userContext, Long pg_call)
            throws ComponentException {
        try {
            LoggableStatement ps = new LoggableStatement(
                    getConnection(userContext), "DELETE FROM "
                    + it.cnr.jada.util.ejb.EJBCommonServices
                    .getDefaultSchema() + "VSX_CHIUSURA "
                    + "WHERE PG_CALL = ?  ", true, this.getClass());

            try {
                ps.setObject(1, pg_call);
                ps.executeUpdate();
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
            }

        } catch (SQLException e) {
            throw handleException(e);
        }
    }

    /**
     * cerca accertamento pgiro PreCondition: E' stato caricato/selezionato un
     * impegno su partita di giro PostCondition: La controparte in parte entrate
     * viene ricercata e inizializzata
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param impegno     <code>Reversale_rigaIBulk</code> la riga della reversale
     * @return List la lista delle banche definite per il terzo della reversale
     */

    private AccertamentoPGiroBulk findAccertamentoPGiro(
            UserContext userContext, ImpegnoPGiroBulk impegno)
            throws it.cnr.jada.persistency.PersistencyException,
            ComponentException {
        SQLBuilder sql = getHome(userContext, AccertamentoPGiroBulk.class)
                .createSQLBuilder();
        sql.addTableToHeader("ASS_OBB_ACR_PGIRO");
        sql.addJoin("ASS_OBB_ACR_PGIRO.CD_CDS", "ACCERTAMENTO.CD_CDS");
        sql.addJoin("ASS_OBB_ACR_PGIRO.ESERCIZIO", "ACCERTAMENTO.ESERCIZIO");
        sql.addJoin("ASS_OBB_ACR_PGIRO.ESERCIZIO_ORI_ACCERTAMENTO",
                "ACCERTAMENTO.ESERCIZIO_ORIGINALE");
        sql.addJoin("ASS_OBB_ACR_PGIRO.PG_ACCERTAMENTO",
                "ACCERTAMENTO.ACCERTAMENTO");
        sql.addClause("AND", "ASS_OBB_ACR_PGIRO.CD_CDS", SQLBuilder.EQUALS, impegno
                .getCd_cds());
        sql.addSQLClause("AND", "ASS_OBB_ACR_PGIRO.ESERCIZIO", SQLBuilder.EQUALS,
                impegno.getEsercizio());
        sql.addClause("AND", "ASS_OBB_ACR_PGIRO.ESERCIZIO_ORI_OBBLIGAZIONE",
                SQLBuilder.EQUALS, impegno.getEsercizio_originale());
        sql.addClause("AND", "ASS_OBB_ACR_PGIRO.PG_OBBLIGAZIONE", SQLBuilder.EQUALS,
                impegno.getPg_obbligazione());
        List result = getHome(userContext, AccertamentoPGiroBulk.class)
                .fetchAll(sql);
        if (result == null || result.size() == 0)
            throw new ApplicationException(
                    "Attenzione! Non esiste la controparte di entrata per l'annotazione di spesa su partita di giro "
                            + impegno.getEsercizio_originale()
                            + "/"
                            + impegno.getPg_obbligazione());
        else if (result.size() > 1)
            throw new ApplicationException(
                    "Attenzione! Esiste più di una controparte di entrata per l'annotazione di spesa su partita di giro "
                            + impegno.getEsercizio_originale()
                            + "/"
                            + impegno.getPg_obbligazione());
        return (AccertamentoPGiroBulk) result.get(0);
    }

    /**
     * lista le coordinate bancarie PreCondition: E' stato creata una riga di
     * mandato con tipologia diversa da quella di regolarizzazione e di
     * trasferimento PostCondition: La lista delle coordinate bancarie del terzo
     * beneficiario del mandato viene estratta
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riga        <code>Mandato_rigaBulk</code> la riga di mandato
     * @return List la lista delle banche definite per il terzo beneficiario del
     * mandato
     */

    public List findBancaOptions(UserContext userContext, Mandato_rigaBulk riga)
            throws it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.persistency.IntrospectionException, ComponentException {
        if (riga.getMandato() != null
                && MandatoBulk.TIPO_REGOLARIZZAZIONE.equals(riga.getMandato()
                .getTi_mandato()))
            return null;
        SQLBuilder sql = getHome(userContext, BancaBulk.class)
                .createSQLBuilder();
        sql.addClause("AND", "cd_terzo", SQLBuilder.EQUALS, riga.getCd_terzo());
        sql.addSQLClause("AND", "BANCA.CD_TERZO_DELEGATO", SQLBuilder.ISNULL, null);
        sql.addSQLClause("AND", "BANCA.FL_CANCELLATO", SQLBuilder.EQUALS, "N");
        /* if ( riga instanceof Mandato_rigaIBulk) */
        if (riga.getBanca() != null
                && riga.getBanca().getTi_pagamento() != null)
            sql.addClause("AND", "ti_pagamento", SQLBuilder.EQUALS, riga.getBanca()
                    .getTi_pagamento());
        return getHome(userContext, BancaBulk.class).fetchAll(sql);
    }

    /**
     * lista le coordinate bancarie PreCondition: E' stato creata una riga di
     * mandato di trasferimento PostCondition: La lista delle coordinate
     * bancarie del terzo beneficiario del mandato viene estratta
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoAccreditamentoBulk</code> il mandato di
     *                    trasferimento
     * @return result la lista delle banche definite per il terzo beneficiario
     * del mandato null non è stata definita nessuna banca per il terzo
     * beneficiario del mandato
     */

    public List findBancaOptions(UserContext userContext,
                                 MandatoAccreditamentoBulk mandato)
            throws it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.persistency.IntrospectionException, ComponentException {
        if (mandato.getMandato_terzo() != null
                && !mandato.isMandatoAccreditamentoBulk()) {
            if (mandato.getModalita_pagamentoOptions() != null
                    && mandato.getModalita_pagamento().getCd_modalita_pag() == null)
                mandato.setModalita_pagamento((Modalita_pagamentoBulk) mandato
                        .getModalita_pagamentoOptions().get(0));

            SQLBuilder sql = getHome(userContext, BancaBulk.class)
                    .createSQLBuilder();
            sql.addClause("AND", "cd_terzo", SQLBuilder.EQUALS, mandato
                    .getMandato_terzo().getCd_terzo());
            sql
                    .addSQLClause("AND", "BANCA.CD_TERZO_DELEGATO", SQLBuilder.ISNULL,
                            null);
            sql.addSQLClause("AND", "BANCA.FL_CANCELLATO", SQLBuilder.EQUALS, "N");
            sql.addOrderBy("FL_CC_CDS DESC");
            if (mandato.getModalita_pagamento() != null
                    && mandato.getModalita_pagamento().getCd_modalita_pag() != null) {
                SQLBuilder sql2 = getHome(userContext,
                        Modalita_pagamentoBulk.class).createSQLBuilder();
                sql2.setHeader("SELECT TI_PAGAMENTO ");
                sql2.addTableToHeader("rif_modalita_pagamento");
                sql2.addSQLClause("AND", "modalita_pagamento.cd_terzo",
                        SQLBuilder.EQUALS, mandato.getMandato_terzo().getCd_terzo());
                sql2.addSQLClause("AND", "modalita_pagamento.cd_modalita_pag",
                        SQLBuilder.EQUALS, mandato.getModalita_pagamento()
                                .getCd_modalita_pag());
                sql2.addSQLJoin("modalita_pagamento.cd_modalita_pag",
                        "rif_modalita_pagamento.cd_modalita_pag");
                sql2.addSQLClause("AND",
                        "MODALITA_PAGAMENTO.CD_TERZO_DELEGATO", SQLBuilder.ISNULL,
                        null);

                sql.addSQLClause("AND", "TI_PAGAMENTO", SQLBuilder.EQUALS, sql2);
            }

            List result = getHome(userContext, BancaBulk.class).fetchAll(sql);
            if (result.size() == 0)
                throw new ApplicationException(
                        "Non esistono coordinate bancarie per il terzo "
                                + mandato.getMandato_terzo().getCd_terzo());
            return result;
        }
        if (mandato.getMandato_terzo() != null
                && mandato.isMandatoAccreditamentoBulk()) {
            mandato.setModalita_pagamento(new Modalita_pagamentoBulk("BO",
                    mandato.getMandato_terzo().getCd_terzo()));
            SQLBuilder sql = getHome(userContext, BancaBulk.class)
                    .createSQLBuilder();
            sql.addClause("AND", "cd_terzo", SQLBuilder.EQUALS, mandato
                    .getMandato_terzo().getCd_terzo());
            sql
                    .addSQLClause("AND", "BANCA.CD_TERZO_DELEGATO", SQLBuilder.ISNULL,
                            null);
            sql.addSQLClause("AND", "BANCA.FL_CANCELLATO", SQLBuilder.EQUALS, "N");
            sql.addSQLClause("AND", "BANCA.FL_CC_CDS", SQLBuilder.EQUALS, "Y");

            if (mandato.getModalita_pagamento() != null
                    && mandato.getModalita_pagamento().getCd_modalita_pag() != null) {
                SQLBuilder sql2 = getHome(userContext,
                        Modalita_pagamentoBulk.class).createSQLBuilder();
                sql2.setHeader("SELECT TI_PAGAMENTO ");
                sql2.addTableToHeader("rif_modalita_pagamento");
                sql2.addSQLClause("AND", "modalita_pagamento.cd_terzo",
                        SQLBuilder.EQUALS, mandato.getMandato_terzo().getCd_terzo());
                sql2.addSQLClause("AND", "modalita_pagamento.cd_modalita_pag",
                        SQLBuilder.EQUALS, mandato.getModalita_pagamento()
                                .getCd_modalita_pag());
                sql2.addSQLJoin("modalita_pagamento.cd_modalita_pag",
                        "rif_modalita_pagamento.cd_modalita_pag");
                sql2.addSQLClause("AND",
                        "MODALITA_PAGAMENTO.CD_TERZO_DELEGATO", SQLBuilder.ISNULL,
                        null);

                sql.addSQLClause("AND", "TI_PAGAMENTO", SQLBuilder.EQUALS, sql2);
            }

            List result = getHome(userContext, BancaBulk.class).fetchAll(sql);
            if (result.size() == 0)
                throw new ApplicationException(
                        "Non esistono coordinate bancarie per il terzo "
                                + mandato.getMandato_terzo().getCd_terzo());
            return result;
        } else
            return null;
    }

    /**
     * find disponibilità di cassa capitolo PreCondition: E' stata richiesta la
     * disponibilita di cassa per ogni capitolo della scadenza di obbligazione
     * presente nella riga del mandato PostCondition: Viene restituita la
     * disponibilità di cassa di ogni capitolo presente nel dettaglio delle
     * scadenze dell'obbligazione pagate dalla riga del mandato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riga        <code>Mandato_rigaBulk</code> la riga del mandato di cui si
     *                    verifica disponibilità sui capitoli
     */

    private List findDisponibilitaDiCassaPerCapitolo(UserContext userContext,
                                                     Mandato_rigaBulk riga) throws ComponentException {
        try {
            BulkHome home = null;

            if (riga.getMandato().TIPO_COMPETENZA.equals(riga.getMandato()
                    .getTi_competenza_residuo()))
                home = getHome(userContext,
                        it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk.class);
            else
                home = getHome(
                        userContext,
                        it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmp_resBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            sql.setDistinctClause(true);
            sql.addTableToHeader("obbligazione_scad_voce", "b");
            sql.addSQLClause("AND", "b.cd_cds", SQLBuilder.EQUALS, riga.getCd_cds());
            sql.addSQLClause("AND", "b.esercizio", SQLBuilder.EQUALS, riga
                    .getEsercizio_obbligazione());
            sql.addSQLClause("AND", "b.esercizio_originale", SQLBuilder.EQUALS, riga
                    .getEsercizio_ori_obbligazione());
            sql.addSQLClause("AND", "b.pg_obbligazione", SQLBuilder.EQUALS, riga
                    .getPg_obbligazione());
            sql.addSQLClause("AND", "b.pg_obbligazione_scadenzario",
                    SQLBuilder.EQUALS, riga.getPg_obbligazione_scadenzario());
            sql.addSQLJoin("b.esercizio", "voce_f_saldi_cmp.esercizio");
            sql.addSQLJoin("b.CD_VOCE", "voce_f_saldi_cmp.cd_voce");
            sql.addSQLJoin("b.ti_gestione", "voce_f_saldi_cmp.ti_gestione");
            sql.addSQLJoin("b.ti_appartenenza",
                    "voce_f_saldi_cmp.ti_appartenenza");
            sql.addSQLJoin("b.cd_cds", "voce_f_saldi_cmp.cd_cds");
            // //sql.addClause( "AND", "ti_competenza_residuo", sql.EQUALS,
            // riga.getMandato().getTi_competenza_residuo());
            return home.fetchAll(sql);

        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * find disponibilità di cassa capitolo PreCondition: E' stata richiesta la
     * disponibilita di cassa per ogni capitolo di ogni obbligazione pagata del
     * mandato PostCondition: Viene restituita la disponibilità di cassa di ogni
     * capitolo presente nel dettaglio delle scadenze delle obbligazioni pagate
     * da ogni riga del mandato (metodo
     * 'findDisponibilitaDiCassaPerCapitolo(Mandato_rigaBulk)')
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoIBulk</code> il mandato di cui si verifica
     *                    disponibilità di cassa sui capitoli
     */

    public List findDisponibilitaDiCassaPerCapitolo(UserContext userContext,
                                                    MandatoBulk mandato) throws ComponentException {
        try {
            List result = new LinkedList();
            Mandato_rigaBulk riga;
            for (Iterator i = mandato.getMandato_rigaColl().iterator(); i
                    .hasNext(); ) {
                riga = (Mandato_rigaBulk) i.next();
                if (!riga.getFl_pgiro().booleanValue())
                    result.addAll(findDisponibilitaDiCassaPerCapitolo(
                            userContext, riga));
            }

            // accorpa quelli uguali
            HashMap hm = new HashMap();
            it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpKey voce;
            for (Iterator i = result.iterator(); i.hasNext(); ) {
                voce = (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpKey) i
                        .next();
                hm.put(voce.getKey(), voce);

            }
            result = new LinkedList();
            for (Iterator i = hm.values().iterator(); i.hasNext(); ) {
                voce = (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpKey) i
                        .next();
                result.add(voce);
            }
            return result;

        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * find disponibilità di cassa Cds PreCondition: E' stata richiesta la
     * disponibilita di cassa di un cds diverso da ente ( disp cassa CDS = fondo
     * di cassa iniziale del Cds per l'esercizio di scrivania + mandati di
     * accreditamento del CNR emessi a favore di questo Cds - mandati emessi dal
     * Cds - modello 1210 emessi dal Cds) PostCondition: Viene restituita la
     * disponibilità di cassa del cds
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoIBulk</code> il mandato emesso dal Cds di cui si
     *                    verifica disponibilità di cassa
     */

    private BigDecimal findDisponibilitaDiCassaPerCDS(UserContext userContext,
                                                      MandatoBulk mandato) throws ComponentException {
        try {
            SQLBuilder sql = getHome(userContext, V_disp_cassa_cdsBulk.class)
                    .createSQLBuilder();
            sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, mandato
                    .getEsercizio());
            sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS,
                    ((CNRUserContext) userContext).getCd_cds());
            List result = getHome(userContext, V_disp_cassa_cdsBulk.class)
                    .fetchAll(sql);
            if (result.size() == 0)
                throw new ApplicationException(
                        "Non esiste il record per la disponibilità di cassa del CDS: "
                                + mandato.getCd_cds() + " - esercizio: "
                                + mandato.getEsercizio());
            V_disp_cassa_cdsBulk cassa = (V_disp_cassa_cdsBulk) result.get(0);
            return cassa.getIm_disponibilita_cassa();

        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * find disponibilità di cassa CNR PreCondition: E' stata richiesta la
     * disponibilita di cassa dell'ente ( disp cassa CNR = fondo di cassa
     * iniziale del CNR per l'esercizio di scrivania + saldo delle reversali
     * emesse dal CNR + sospesi di entrata non ancora associati alle reversali -
     * saldo dei mandati emessi dal CNR - sospesi di spesa non ancora associati
     * ai mandati) PostCondition: Viene restituita la disponibilità di cassa
     * dell'ente
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoIBulk</code> il mandato emesso dal Cds di cui si
     *                    verifica disponibilità di cassa
     */

    private BigDecimal findDisponibilitaDiCassaPerCNR(UserContext userContext,
                                                      MandatoBulk mandato) throws ComponentException {
        try {
            EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class)
                    .findAll().get(0);
            SQLBuilder sql = getHome(userContext, V_disp_cassa_cnrBulk.class)
                    .createSQLBuilder();
            sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, mandato
                    .getEsercizio());
            sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, ente
                    .getCd_unita_organizzativa());
            List result = getHome(userContext, V_disp_cassa_cnrBulk.class)
                    .fetchAll(sql);
            if (result.size() == 0)
                throw new ApplicationException(
                        "Non esiste il record per la disponibilità di cassa del CNR: "
                                + mandato.getCd_cds() + " - esercizio: "
                                + mandato.getEsercizio());
            V_disp_cassa_cnrBulk cassa = (V_disp_cassa_cnrBulk) result.get(0);
            return cassa.getIm_disponibilta_cassa();

        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * find disponibilità di cassa Cds PreCondition: E' stata richiesta la
     * disponibilita di cassa di un cds diverso da ente ( disp cassa CDS = fondo
     * di cassa iniziale del Cds per l'esercizio di scrivania + mandati di
     * accreditamento del CNR emessi a favore di questo Cds - mandati emessi dal
     * Cds - modello 1210 emessi dal Cds) PostCondition: Viene restituita la
     * disponibilità di cassa del cds
     * <p>
     * find disponibilità di cassa CNR PreCondition: E' stata richiesta la
     * disponibilita di cassa dell'ente ( disp cassa CNR = fondo di cassa
     * iniziale del CNR per l'esercizio di scrivania + saldo delle reversali
     * emesse dal CNR + sospesi di entrata non ancora associati alle reversali -
     * saldo dei mandati emessi dal CNR - sospesi di spesa non ancora associati
     * ai mandati) PostCondition: Viene restituita la disponibilità di cassa
     * dell'ente
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoIBulk</code> il mandato emesso dal Cds di cui si
     *                    verifica disponibilità di cassa
     */

    private BigDecimal findDisponibilitaDiCassaPerContoCorrente(
            UserContext userContext, MandatoBulk mandato)
            throws ComponentException {
        try {
            EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class)
                    .findAll().get(0);
            if (!mandato.getCd_cds().equals(ente.getCd_unita_organizzativa())) // mandato
                // CDS
                return findDisponibilitaDiCassaPerCDS(userContext, mandato);
            else
                // mandato CNR
                return findDisponibilitaDiCassaPerCNR(userContext, mandato);

        } catch (Exception e) {
            throw handleException(e);
        }

    }

    public List findImpegni(UserContext userContext,
                            MandatoAccreditamentoBulk mandato)
            throws it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.persistency.IntrospectionException, ComponentException {
        SQLBuilder sql = getHome(userContext, V_impegnoBulk.class)
                .createSQLBuilder();
        sql.addTableToHeader("MANDATO_RIGA");
        sql.addSQLJoin("MANDATO_RIGA.CD_CDS", "V_IMPEGNO.CD_CDS");
        sql.addSQLJoin("MANDATO_RIGA.ESERCIZIO", "V_IMPEGNO.ESERCIZIO");
        sql.addSQLJoin("MANDATO_RIGA.ESERCIZIO_ORI_OBBLIGAZIONE",
                "V_IMPEGNO.ESERCIZIO_ORIGINALE");
        sql.addSQLJoin("MANDATO_RIGA.PG_OBBLIGAZIONE",
                "V_IMPEGNO.PG_OBBLIGAZIONE");
        sql.addSQLJoin("MANDATO_RIGA.PG_OBBLIGAZIONE_SCADENZARIO",
                "V_IMPEGNO.PG_OBBLIGAZIONE_SCADENZARIO");
        sql.addSQLClause("AND", "MANDATO_RIGA.ESERCIZIO", SQLBuilder.EQUALS, mandato
                .getEsercizio());
        sql.addSQLClause("AND", "MANDATO_RIGA.CD_CDS", SQLBuilder.EQUALS, mandato
                .getCd_cds());
        sql.addSQLClause("AND", "MANDATO_RIGA.PG_MANDATO", SQLBuilder.EQUALS, mandato
                .getPg_mandato());
        List result = getHome(userContext, V_impegnoBulk.class).fetchAll(sql);
        // imposto l'importo da trasferire per ogni impegno
        V_impegnoBulk impegno;
        Mandato_rigaBulk riga;
        for (Iterator i = mandato.getMandato_rigaColl().iterator(); i.hasNext(); ) {
            riga = (Mandato_rigaBulk) i.next();
            for (Iterator j = result.iterator(); j.hasNext(); ) {
                impegno = (V_impegnoBulk) j.next();
                if (impegno.getEsercizio().compareTo(riga.getEsercizio()) == 0
                        && impegno.getEsercizio_originale().compareTo(
                        riga.getEsercizio_ori_obbligazione()) == 0
                        && impegno.getPg_obbligazione().compareTo(
                        riga.getPg_obbligazione()) == 0
                        && impegno.getPg_obbligazione_scadenzario().compareTo(
                        riga.getPg_obbligazione_scadenzario()) == 0
                        && impegno.getCd_cds().compareTo(riga.getCd_cds()) == 0) {
                    impegno.setIm_da_trasferire(riga.getIm_mandato_riga());
                    break;
                }
            }
        }
        return result;
    }

    /**
     * lista le modalità di pagamento PreCondition: E' stato creata una riga di
     * mandato con tipologia diversa da quella di regolarizzazione e di
     * trasferimento PostCondition: La lista delle modalità di pagamento del
     * terzo beneficiario, tutte appartenenti alla stessa classe
     * (Bancario/Postale/..) per cui si sta emettendo il mandato, viene
     * estratta. Vengono escluse le modalità di pagamento riferite a terzi
     * cessionari
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riga        <code>Mandato_rigaBulk</code> la riga di mandato
     * @return List la lista delle modalità di pagamento definite per il terzo
     * beneficiario del mandato
     */

    public List findModalita_pagamentoOptions(UserContext userContext,
                                              Mandato_rigaBulk riga)
            throws it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.persistency.IntrospectionException, ComponentException {
        if (riga.getMandato() != null
                && MandatoBulk.TIPO_REGOLARIZZAZIONE.equals(riga.getMandato()
                .getTi_mandato()))
            return null;
        SQLBuilder sql = getHome(userContext, Modalita_pagamentoBulk.class)
                .createSQLBuilder();
        sql.addTableToHeader("RIF_MODALITA_PAGAMENTO");
        sql.addSQLJoin("MODALITA_PAGAMENTO.CD_MODALITA_PAG",
                "RIF_MODALITA_PAGAMENTO.CD_MODALITA_PAG");
        sql.addClause("AND", "cd_terzo", SQLBuilder.EQUALS, riga.getCd_terzo());
        sql.addClause("AND", "cd_terzo_delegato", SQLBuilder.ISNULL, null);
        /* if ( riga instanceof Mandato_rigaIBulk) */
        if (riga.getBanca() != null
                && riga.getBanca().getTi_pagamento() != null)
            sql.addSQLClause("AND", "RIF_MODALITA_PAGAMENTO.TI_PAGAMENTO",
                    SQLBuilder.EQUALS, riga.getBanca().getTi_pagamento());
        return getHome(userContext, Modalita_pagamentoBulk.class).fetchAll(sql);
    }

    /**
     * lista le modalità di pagamento PreCondition: E' stato creata una riga di
     * mandato di trasferimento PostCondition: La lista delle modalità di
     * pagamento del terzo beneficiario, tutte appartenenti alla stessa classe
     * (Bancario/Postale/..) per cui si sta emettendo il mandato, viene
     * estratta.Vengono escluse le modalità di pagamento riferite a terzi
     * cessionari
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoAccreditamentoBulk</code> il mandato di
     *                    trasferimento
     * @return result la lista delle modalità di pagamento definite per il terzo
     * beneficiario del mandato null non è stata definita nessuna
     * modalità di pagamento per il terzo beneficiario del mandato
     */

    public List findModalita_pagamentoOptions(UserContext userContext,
                                              MandatoAccreditamentoBulk mandato)
            throws it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.persistency.IntrospectionException, ComponentException {
        if (mandato.getMandato_terzo() != null) {
            SQLBuilder sql = getHome(userContext, Modalita_pagamentoBulk.class)
                    .createSQLBuilder();
            sql.addTableToHeader("RIF_MODALITA_PAGAMENTO");
            sql.addSQLJoin("MODALITA_PAGAMENTO.CD_MODALITA_PAG",
                    "RIF_MODALITA_PAGAMENTO.CD_MODALITA_PAG");
            sql.addClause("AND", "cd_terzo", SQLBuilder.EQUALS, mandato
                    .getMandato_terzo().getCd_terzo());
            sql.addClause("AND", "cd_terzo_delegato", SQLBuilder.ISNULL, null);

            if (mandato.isMandatoAccreditamentoBulk())
                sql.addSQLClause("AND", "MODALITA_PAGAMENTO.CD_MODALITA_PAG",
                        SQLBuilder.EQUALS, "BO");
            // if ( riga instanceof Mandato_rigaIBulk)
            // sql.addSQLClause( "AND", "RIF_MODALITA_PAGAMENTO.TI_PAGAMENTO",
            // sql.EQUALS,
            // ((Mandato_rigaIBulk)riga).getDoc_passivo().getTi_pagamento() );
            List result = getHome(userContext, Modalita_pagamentoBulk.class)
                    .fetchAll(sql);
            if (result.size() == 0)
                throw new ApplicationException(
                        "Non esistono modalità di pagamento per il terzo "
                                + mandato.getMandato_terzo().getCd_terzo());
            return result;

        } else
            return null;
    }

    /**
     * lista le tipologie di bollo PreCondition: E' stato creata una riga di
     * mandato di trasferimento PostCondition: La lista delle tipologie di bollo
     * di tipo spesa viene estratta
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> il mandato di trasferimento
     * @return List la lista dei tipi di bollo definiti per il mandato di
     * trasferimento
     */

    public List findTipoBolloOptions(UserContext userContext,
                                     MandatoBulk mandato)
            throws it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.persistency.IntrospectionException, ComponentException {
        SQLBuilder sql = getHome(userContext, Tipo_bolloBulk.class)
                .createSQLBuilder();
        sql.addClause("AND", "ti_entrata_spesa", SQLBuilder.NOT_EQUALS,
                Tipo_bolloBulk.TIPO_ENTRATA);
        sql.addOrderBy("cd_tipo_bollo");
        return getHome(userContext, Tipo_bolloBulk.class).fetchAll(sql);
    }

    /**
     * lista le unità organizzative - scrivania = Ente PreCondition: E' stata
     * richiesta una lista delle unità organizzative per cui è possibile
     * emettere un mandato e l'unità organizzativa di scrivania e' l'UO Ente
     * PostCondition: Una lista comprendente solo l'UO Ente viene restituita
     * lista le unità organizzative - scrivania diversa da UO Ente PreCondition:
     * E' stata richiesta una lista delle unità organizzative per cui è
     * possibile emettere un mandato e l'unità organizzativa di scrivania e'
     * diversa dall'UO Ente PostCondition: Una lista comprendente l'UO Ente e
     * l'UO di scrivania viene restituita
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> il mandato
     * @return result la lista delle unità organizzative definite per il mandato
     */

    public List findUnita_organizzativaOptions(UserContext userContext,
                                               MandatoBulk mandato)
            throws it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.persistency.IntrospectionException, ComponentException {
        SQLBuilder sql = getHome(userContext,
                Unita_organizzativa_enteBulk.class).createSQLBuilder();
        List result = getHome(userContext, Unita_organizzativa_enteBulk.class)
                .fetchAll(sql);
        mandato.setCd_uo_ente(((Unita_organizzativaBulk) result.get(0))
                .getCd_unita_organizzativa());
        if (mandato.getUnita_organizzativa() != null
                && mandato.getUnita_organizzativa().getCd_unita_organizzativa() != null
                && !((Unita_organizzativaBulk) result.get(0))
                .getCd_unita_organizzativa().equals(
                        mandato.getUnita_organizzativa()
                                .getCd_unita_organizzativa()))
            result.add(mandato.getUnita_organizzativa());
        return result;
    }

    /**
     * Validazione dell'oggetto in fase di stampa
     */
    private Timestamp getDataOdierna(it.cnr.jada.UserContext userContext)
            throws ComponentException {

        try {
            return getHome(userContext, MandatoIBulk.class).getServerDate();
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    protected Long getPg_call(UserContext userContext)
            throws ComponentException {
        LoggableStatement cs = null;
        Long pg = null;
        try {
            try {
                cs = new LoggableStatement(getConnection(userContext),
                        "{ ? = call "
                                + it.cnr.jada.util.ejb.EJBCommonServices
                                .getDefaultSchema()
                                + "IBMUTL020.vsx_get_pg_call() }", false, this
                        .getClass());
                cs.registerOutParameter(1, java.sql.Types.NUMERIC);
                cs.executeQuery();
                pg = new Long(cs.getLong(1));
            } catch (Throwable e) {
                throw handleException(e);
            } finally {
                if (cs != null)
                    cs.close();
            }
        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        }
        if (pg == null)
            throw new it.cnr.jada.comp.ApplicationException(
                    "Impossibile ottenere un progressivo valido per la vista VSX_CHIUSURA!");
        return pg;
    }

    /**
     * inizializzazione di una istanza di MandatoBulk PreCondition: E' stata
     * richiesta l'inizializzazione di una istanza di MandatoBulk PostCondition:
     * Viene impostata la data di emissione del mandato con la data del Server
     * inizializzazione di una istanza di MandatoAccreditamentoWizardBulk
     * PreCondition: E' stata richiesta l'inizializzazione di una istanza di
     * MandatoAccreditamentoWizardBulk, l'oggetto bulk utilizzato come wizard
     * per la generazione dei mandati di accreditamento PostCondition: Viene
     * impostata la data di emissione del wizard con la data del Server, il Cds
     * e l'UO di appartenenza con il Cds e l'UO dell'Ente, il mandato terzo con
     * il codice terzo che corrisponde al Cds beneficiario del mandato di
     * accreditamento (metodo creaMandatoTerzoPerCds), viene impostata la lista
     * degli impegni (metodo listaImpegniCNR) del CNR inizializzazione di una
     * istanza di RicercaMandatoAccreditamentoBulk PreCondition: E' stata
     * richiesta l'inizializzazione di una istanza di
     * RicercaMandatoAccreditamentoBulk, l'oggetto bulk utilizzato per
     * visualizzare i dati di tutti i Cds verso cui emettere i mandati di
     * accreditamento PostCondition: Viene impostata la disponibilità di cassa
     * del CNR e viene inizializzata la lista dei Cds con la loro disponibilità
     * di cassa
     *
     * @param aUC  lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk <code>OggettoBulk</code> il mandato da inizializzare per
     *             l'inserimento
     * @return bulk <code>OggettoBulk</code> il Mandato inizializzato per
     * l'inserimento
     */
    public OggettoBulk inizializzaBulkPerInserimento(UserContext aUC,
                                                     OggettoBulk bulk) throws ComponentException {

        try {
            Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome(
                    aUC, Unita_organizzativa_enteBulk.class).findAll().get(0);
            if (bulk instanceof MandatoBulk) {
                ((MandatoBulk) bulk).setDt_emissione(DateServices
                        .getDt_valida(aUC));
//Eliminato in quanto sono campi valorizzati ma non utilizzati
//				((MandatoBulk) bulk)
//				.setIm_disp_cassa_cds(findDisponibilitaDiCassaPerCDS(
//						aUC, (MandatoBulk) bulk));
//				((MandatoBulk) bulk)
//				.setIm_disp_cassa_CNR(findDisponibilitaDiCassaPerCNR(
//						aUC, (MandatoBulk) bulk));
                // accreditamento
                if (bulk instanceof MandatoAccreditamentoWizardBulk) {
                    MandatoAccreditamentoWizardBulk mandato = (MandatoAccreditamentoWizardBulk) bulk;
                    mandato.setCds((CdsBulk) getHome(aUC, EnteBulk.class)
                            .findAll().get(0));
                    mandato.setUnita_organizzativa(uoEnte);
                    mandato.setMandato_terzo(creaMandatoTerzoPerCds(aUC,
                            mandato));
                    bulk = listaImpegniCNR(aUC, mandato);
                }
                verificaStatoEsercizio(aUC,
                        ((MandatoBulk) bulk).getEsercizio(),
                        ((MandatoBulk) bulk).getCd_cds());
                /*
                 * simona 9/10/02 - inserita gestione mandato accreditamento
                 * cds-cds //regolarizzazione else if (
                 * MandatoBulk.TIPO_REGOLARIZZAZIONE.equals( ((MandatoBulk)
                 * bulk).getTi_mandato())) { ((MandatoBulk) bulk).setCds(
                 * (CdsBulk)getHome( aUC, EnteBulk.class).findAll().get(0));
                 * ((MandatoBulk) bulk).setUnita_organizzativa( uoEnte ); if
                 * (!((CNRUserContext) aUC).getCd_unita_organizzativa().equals(
                 * uoEnte.getCd_unita_organizzativa())) throw new
                 * ApplicationException
                 * ("Funzione non consentita per utente non abilitato a " +
                 * uoEnte.getCd_unita_organizzativa() ); }
                 */
                // pagamento
                //devono essere visualizzati anche i mandati di pagamento fatti dalla 999 (F24EP)
				/*
				if (!MandatoBulk.TIPO_REGOLARIZZAZIONE
						.equals(((MandatoBulk) bulk).getTi_mandato())
						&& !MandatoBulk.TIPO_ACCREDITAMENTO
								.equals(((MandatoBulk) bulk).getTi_mandato())
						&& ((CNRUserContext) aUC).getCd_unita_organizzativa()
								.equals(uoEnte.getCd_unita_organizzativa()))
					throw new ApplicationException(
							"Funzione non consentita per utente abilitato a "
									+ uoEnte.getCd_unita_organizzativa());
				 */
                bulk = super.inizializzaBulkPerInserimento(aUC, bulk);

                return bulk;
            } else if (bulk instanceof RicercaMandatoAccreditamentoBulk) {
                if (!((CNRUserContext) aUC).getCd_unita_organizzativa().equals(
                        uoEnte.getCd_unita_organizzativa()))
                    throw new ApplicationException(
                            "Funzione non consentita per utente non abilitato a "
                                    + uoEnte.getCd_unita_organizzativa());
                verificaStatoEsercizio(aUC, ((CNRUserContext) aUC)
                        .getEsercizio(), ((CNRUserContext) aUC).getCd_cds());
                RicercaMandatoAccreditamentoBulk ricerca = (RicercaMandatoAccreditamentoBulk) bulk;
                ricerca
                        .setIm_disp_cassa_CNR(((V_disp_cassa_cnrHome) getHome(
                                aUC, V_disp_cassa_cnrBulk.class))
                                .findIm_disponibilita_cassaCNR(((it.cnr.contab.utenze00.bp.CNRUserContext) aUC)
                                        .getEsercizio()));
                ricerca.setDt_scadenza_obbligazioni(getHome(aUC,
                        MandatoAccreditamentoBulk.class).getServerTimestamp());
                ricerca
                        .setCentriDiSpesaColl(((V_disp_cassa_cdsHome) getHome(
                                aUC, V_disp_cassa_cdsBulk.class))
                                .findDisponibilitaCassa(((it.cnr.contab.utenze00.bp.CNRUserContext) aUC)
                                        .getEsercizio()));
                return ricerca;
            }

            return bulk;
        } catch (Exception e) {
            throw handleException(bulk, e);
        }
    }

    /**
     * inizializzazione di una istanza di MandatoBulk per modifica PreCondition:
     * E' stata richiesta l'inizializzazione di una istanza di MandatoBulk
     * PostCondition: Viene caricata la collezione delle righe di mandato
     * (Mandato_rigaBulk), dei sospesi associati al mandato
     * (Sospeso_det_uscBulk), delle associazioni mandato-reversale(
     * Ass_mandato_reversaleBulk). Viene caricato i dati del beneficiario del
     * mandato (Mandato_terzoBulk)
     *
     * @param aUC  lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk <code>OggettoBulk</code> il mandato da inizializzare per la
     *             modifica
     * @return mandato il Mandato inizializzato per la modifica
     */
    public OggettoBulk inizializzaBulkPerModifica(UserContext aUC,
                                                  OggettoBulk bulk) throws ComponentException {
        MandatoBulk mandato = (MandatoBulk) super.inizializzaBulkPerModifica(
                aUC, bulk);
        try {
            //Eliminato in quanto sono campi valorizzati ma non utilizzati
//			mandato.setIm_disp_cassa_cds(findDisponibilitaDiCassaPerCDS(aUC,
//					mandato));
//			mandato.setIm_disp_cassa_CNR(findDisponibilitaDiCassaPerCNR(aUC,
//					mandato));

            // carico i mandati riga
            mandato.setMandato_rigaColl(new BulkList(((MandatoHome) getHome(
                    aUC, mandato.getClass())).findMandato_riga(aUC, mandato)));
            Mandato_rigaBulk riga;
            for (Iterator i = mandato.getMandato_rigaColl().iterator(); i
                    .hasNext(); ) {
                riga = (Mandato_rigaBulk) super.inizializzaBulkPerModifica(aUC,
                        (Mandato_rigaBulk) i.next());
                // Carico automaticamente i codici SIOPE e visualizzo quelli
                // ancora collegabili se la gestione è attiva
                if (Utility.createParametriCnrComponentSession()
                        .getParametriCnr(aUC, mandato.getEsercizio())
                        .getFl_siope().booleanValue()) {
                    riga.setMandato_siopeColl(new BulkList(
                            ((Mandato_rigaHome) getHome(aUC,
                                    Mandato_rigaBulk.class))
                                    .findCodiciCollegatiSIOPE(aUC, riga)));
                    setCodiciSIOPECollegabili(aUC, riga);
                }

                //			if (Utility.createParametriCnrComponentSession().getParametriCnr(aUC, mandato.getEsercizio()).getFl_cup().booleanValue() &&
                //					Utility.createParametriCnrComponentSession().getParametriCnr(aUC, mandato.getEsercizio()).getFl_siope_cup().booleanValue()){
                //				Timestamp dataLimite=Utility.createConfigurazioneCnrComponentSession().getDt01(aUC, "DATA_LIMITE_CUP_SIOPE_CUP");
                //				if(mandato.getDt_emissione().after(dataLimite)){
                //					for (Iterator j=riga.getMandato_siopeColl().iterator();j.hasNext();){
                //						Mandato_siopeIBulk rigaSiope = (Mandato_siopeIBulk)j.next();
                //						rigaSiope.setMandatoSiopeCupColl(new BulkList(((Mandato_siopeHome) getHome( aUC, Mandato_siopeBulk.class)).findCodiciSiopeCupCollegati(aUC, rigaSiope)));
                //					}
                //				}else
                //				{
                //					riga.setMandatoCupColl(new BulkList(((Mandato_rigaHome) getHome( aUC, Mandato_rigaBulk.class)).findCodiciCupCollegati(aUC, riga)));
                //				}
                //
                //			}else{
                if (Utility.createParametriCnrComponentSession().getParametriCnr(aUC, mandato.getEsercizio()).getFl_cup().booleanValue()) {
                    riga.setMandatoCupColl(new BulkList(((Mandato_rigaHome) getHome(aUC, Mandato_rigaBulk.class)).findCodiciCupCollegati(aUC, riga)));
                } else {
                    if (Utility.createParametriCnrComponentSession().getParametriCnr(aUC, mandato.getEsercizio()).getFl_siope_cup().booleanValue()) {
                        for (Iterator j = riga.getMandato_siopeColl().iterator(); j.hasNext(); ) {
                            Mandato_siopeIBulk rigaSiope = (Mandato_siopeIBulk) j.next();
                            rigaSiope.setMandatoSiopeCupColl(new BulkList(((Mandato_siopeHome) getHome(aUC, Mandato_siopeBulk.class)).findCodiciSiopeCupCollegati(aUC, rigaSiope)));
                        }
                    }
                }
                //			}
                inizializzaTi_fattura(aUC, riga);
                ((Mandato_rigaHome) getHome(aUC, riga.getClass()))
                        .initializeElemento_voce(aUC, riga);
            }

            if (mandato instanceof MandatoAccreditamentoBulk) {
                ((MandatoAccreditamentoBulk) mandato)
                        .setModalita_pagamento(((Mandato_rigaBulk) mandato
                                .getMandato_rigaColl().get(0))
                                .getModalita_pagamento());
                ((MandatoAccreditamentoBulk) mandato)
                        .setModalita_pagamentoOptions(((Mandato_rigaBulk) mandato
                                .getMandato_rigaColl().get(0))
                                .getModalita_pagamentoOptions());
                ((MandatoAccreditamentoBulk) mandato)
                        .setBanca(((Mandato_rigaBulk) mandato
                                .getMandato_rigaColl().get(0)).getBanca());
                ((MandatoAccreditamentoBulk) mandato)
                        .setBancaOptions(((Mandato_rigaBulk) mandato
                                .getMandato_rigaColl().get(0))
                                .getBancaOptions());
                ((MandatoAccreditamentoBulk) mandato)
                        .setImpegniSelezionatiColl(findImpegni(aUC,
                                (MandatoAccreditamentoBulk) mandato));
            }
            if (mandato instanceof MandatoIBulk)
                mandato = inizializzaSospesiDa1210(aUC, (MandatoIBulk) mandato);
            // carico il mandato terzo
            mandato.setMandato_terzo(((MandatoHome) getHome(aUC, mandato
                    .getClass())).findMandato_terzo(aUC, mandato));
            initializeKeysAndOptionsInto(aUC, mandato);
            verificaTipoBollo(aUC, mandato);

            if (MandatoBulk.TIPO_REGOLARIZZAZIONE.equals(mandato
                    .getTi_mandato())) {
                BancaBulk banca = new BancaBulk();
                banca.setTerzo(mandato.getMandato_terzo().getTerzo());
                for (Iterator i = mandato.getMandato_rigaColl().iterator(); i
                        .hasNext(); )
                    ((Mandato_rigaBulk) i.next()).setBanca(banca);

                if (mandato instanceof MandatoIBulk) {
                    Var_bilancioBulk varBilancio = ((Var_bilancioHome) getHome(
                            aUC, Var_bilancioBulk.class))
                            .findByMandato(mandato);
                    if (varBilancio != null)
                        ((MandatoIBulk) mandato).setVar_bilancio(varBilancio);
                }
            }

            // carico i sospeso_det_usc
            Sospeso_det_uscBulk sdu;
            mandato
                    .setSospeso_det_uscColl(new BulkList(
                            ((MandatoHome) getHome(aUC, mandato.getClass()))
                                    .findSospeso_det_usc(aUC, mandato)));
            // aggiungo nella deleteList i sospesi annullati
            for (Iterator i = mandato.getSospeso_det_uscColl().iterator(); i
                    .hasNext(); ) {
                sdu = (Sospeso_det_uscBulk) i.next();
                sdu.setMandato(mandato);
                if (sdu.getStato().equals(Sospeso_det_uscBulk.STATO_ANNULLATO))
                    i.remove();
            }

            // carico il cd uo ente
            SQLBuilder sql = getHome(aUC, Unita_organizzativa_enteBulk.class)
                    .createSQLBuilder();
            List result = getHome(aUC, Unita_organizzativa_enteBulk.class)
                    .fetchAll(sql);
            mandato
                    .setCd_uo_ente(((Unita_organizzativa_enteBulk) result
                            .get(0)).getCd_unita_organizzativa());

            // carico le reversali associate al mandato di
            // regolarizzazione/accreditamento
            mandato.setReversaliColl(new BulkList(
                    ((Ass_mandato_reversaleHome) getHome(aUC,
                            Ass_mandato_reversaleBulk.class)).findReversali(
                            aUC, mandato)));

            // carico i doc. contabili (mandati/reversali) associati al mandato
            mandato
                    .setDoc_contabili_collColl(((V_ass_doc_contabiliHome) getHome(
                            aUC, V_ass_doc_contabiliBulk.class))
                            .findDoc_contabili_coll(aUC, mandato));

            // per mandato di accreditamento inizializzo il codice cds
            if (mandato instanceof MandatoAccreditamentoBulk) {
                String cd_uo = mandato.getMandato_terzo().getTerzo()
                        .getUnita_organizzativa().getCd_unita_organizzativa();
                String cd_cds = cd_uo.substring(0, cd_uo.indexOf("."));
                ((MandatoAccreditamentoBulk) mandato).setCodice_cds(cd_cds);
            }
            // per mandati a regolamento sospeso con ritenute e reversale
            // associate verifico se la reversale è stata generata
            // perchè fattura estera istituzuionale di beni intraue o san marino
            if (MandatoBulk.TIPO_REGOLAM_SOSPESO.equals(mandato.getTi_mandato())
                    && mandato.getIm_ritenute().compareTo(new BigDecimal(0)) > 0)
                mandato = inizializzaFlagFaiReversale(aUC,
                        (MandatoIBulk) mandato);
            if (mandato.getPg_mandato_riemissione() != null) {
                V_mandato_reversaleBulk man_rev = (V_mandato_reversaleBulk) getHome(aUC, V_mandato_reversaleBulk.class).findByPrimaryKey(new V_mandato_reversaleBulk(mandato.getEsercizio(), Numerazione_doc_contBulk.TIPO_MAN, mandato.getCd_cds_origine(), mandato.getPg_mandato_riemissione()));
                if (man_rev != null)
                    mandato.setV_man_rev(man_rev);
                else
                    man_rev = (V_mandato_reversaleBulk) getHome(aUC, V_mandato_reversaleBulk.class).findByPrimaryKey(new V_mandato_reversaleBulk(mandato.getEsercizio(), Numerazione_doc_contBulk.TIPO_MAN, mandato.getCd_cds(), mandato.getPg_mandato_riemissione()));
                if (man_rev != null)
                    mandato.setV_man_rev(man_rev);
            }
        } catch (Exception e) {
            throw handleException(mandato, e);
        }
        return mandato;

    }

    /**
     * inizializzazione di una istanza di MandatoAccreditamentoBulk per ricerca
     * PreCondition: E' stata richiesta l'inizializzazione di una istanza di
     * MandatoAccreditamentoBulk per ricerca PostCondition: Viene inizializzato
     * il Cds e l'UO di appartenenza con il Cds e l'UO del mandato
     * inizializzazione di una istanza di CdsBilancioBulk per ricerca
     * PreCondition: E' stata richiesta l'inizializzazione di una istanza di
     * CdsBilancioBulk, l'oggetto bulk che consente la visualizzazione del
     * Bilancio entrate/spese del CdS PostCondition: Viene inizializzata la
     * collezione delle voci di bilancio di un Cds
     *
     * @param aUC  lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk <code>OggettoBulk</code> il mandato da inizializzare per la
     *             ricerca
     * @return bulk il Mandato inizializzato per la ricerca bilancio istanza di
     * <code>CdsBilancioBulk</code> che ha impostate le voci di bilancio
     * di quel Cds
     */
    public OggettoBulk inizializzaBulkPerRicerca(UserContext aUC,
                                                 OggettoBulk bulk) throws ComponentException {
        try {
            if (bulk instanceof MandatoBulk) {
                bulk = super.inizializzaBulkPerRicerca(aUC, bulk);
                MandatoBulk mandato = (MandatoBulk) bulk;

                // Se uo 999.000 in scrivania: visualizza mandati fatti da Cds
                // differenti per l'Ente
                Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(
                        aUC, Unita_organizzativa_enteBulk.class).findAll().get(
                        0);
                if (!((CNRUserContext) aUC).getCd_unita_organizzativa().equals(
                        ente.getCd_unita_organizzativa()))
                    // se in scrivania ho uo diversa da ente
                    mandato.setCd_uo_origine(((CNRUserContext) aUC)
                            .getCd_unita_organizzativa());

                //Eliminato in quanto sono campi valorizzati ma non utilizzati
//				mandato.setIm_disp_cassa_cds(findDisponibilitaDiCassaPerCDS(
//						aUC, mandato));
//				mandato.setIm_disp_cassa_CNR(findDisponibilitaDiCassaPerCNR(
//						aUC, mandato));

                if (mandato instanceof MandatoAccreditamentoBulk
                    // || MandatoBulk.TIPO_REGOLARIZZAZIONE.equals( ((MandatoBulk)
                    // bulk).getTi_mandato())
                ) {
                    mandato.setCds((CdsBulk) getHome(aUC, EnteBulk.class)
                            .findAll().get(0));
                    mandato
                            .setUnita_organizzativa((Unita_organizzativaBulk) getHome(
                                    aUC, Unita_organizzativa_enteBulk.class)
                                    .findAll().get(0));
                }
            } else if (bulk instanceof CdsBilancioBulk) {
                CdsBilancioBulk bilancio = (CdsBilancioBulk) bulk;
                bilancio.setVociBilancioColl(((V_sit_bil_cds_cnrHome) getHome(
                        aUC, V_sit_bil_cds_cnrBulk.class)).findBilancioCds(
                        ((it.cnr.contab.utenze00.bp.CNRUserContext) aUC)
                                .getEsercizio(), bilancio.getCd_cds(), bilancio
                                .getTipoGestione()));
                return bilancio;
            }

        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
        return bulk;
    }

    /**
     * inizializzazione di una istanza di MandatoAccreditamentoBulk per ricerca
     * PreCondition: E' stata richiesta l'inizializzazione di una istanza di
     * MandatoAccreditamentoBulk per ricerca PostCondition: Viene inizializzato
     * il Cds e l'UO di appartenenza con il Cds e l'UO del mandato
     * inizializzazione di una istanza di CdsBilancioBulk per ricerca
     * PreCondition: E' stata richiesta l'inizializzazione di una istanza di
     * CdsBilancioBulk, l'oggetto bulk che consente la visualizzazione del
     * Bilancio entrate/spese del CdS PostCondition: Viene inizializzata la
     * collezione delle voci di bilancio di un Cds
     *
     * @param aUC  lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk <code>OggettoBulk</code> il mandato da inizializzare per la
     *             ricerca
     * @return bulk il Mandato inizializzato per la ricerca bilancio istanza di
     * <code>CdsBilancioBulk</code> che ha impostate le voci di bilancio
     * di quel Cds
     */
    public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext aUC,
                                                       OggettoBulk bulk) throws ComponentException {
        try {
            if (bulk instanceof MandatoBulk) {
                bulk = super.inizializzaBulkPerRicercaLibera(aUC, bulk);

                MandatoBulk mandato = (MandatoBulk) bulk;

                // Se uo 999.000 in scrivania: visualizza mandati fatti da Cds
                // differenti per l'Ente
                Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(
                        aUC, Unita_organizzativa_enteBulk.class).findAll().get(
                        0);
                if (!((CNRUserContext) aUC).getCd_unita_organizzativa().equals(
                        ente.getCd_unita_organizzativa()))
                    // se in scrivania ho uo diversa da ente
                    mandato.setCd_uo_origine(((CNRUserContext) aUC)
                            .getCd_unita_organizzativa());

                //Eliminato in quanto sono campi valorizzati ma non utilizzati
//				mandato.setIm_disp_cassa_cds(findDisponibilitaDiCassaPerCDS(
//						aUC, mandato));
//				mandato.setIm_disp_cassa_CNR(findDisponibilitaDiCassaPerCNR(
//						aUC, mandato));

                /*
                 * if ( mandato instanceof MandatoAccreditamentoBulk ||
                 * MandatoBulk.TIPO_REGOLARIZZAZIONE.equals( ((MandatoBulk)
                 * bulk).getTi_mandato())) { mandato.setCds( (CdsBulk) getHome(
                 * aUC, EnteBulk.class).findAll().get(0));
                 * mandato.setUnita_organizzativa( (Unita_organizzativaBulk)
                 * getHome( aUC,
                 * Unita_organizzativa_enteBulk.class).findAll().get(0)); }
                 */
                if (mandato instanceof MandatoAccreditamentoBulk) {
                    mandato.setCds((CdsBulk) getHome(aUC, EnteBulk.class)
                            .findAll().get(0));
                    mandato
                            .setUnita_organizzativa((Unita_organizzativaBulk) getHome(
                                    aUC, Unita_organizzativa_enteBulk.class)
                                    .findAll().get(0));
                }
            }
            /*
             * else if ( bulk instanceof CdsBilancioBulk ) { CdsBilancioBulk
             * bilancio = (CdsBilancioBulk) bulk;
             * bilancio.setVociBilancioColl(((V_sit_bil_cds_cnrHome)getHome(
             * aUC, V_sit_bil_cds_cnrBulk.class)).findBilancioCds(
             * ((it.cnr.contab.utenze00.bp.CNRUserContext)aUC).getEsercizio(),
             * bilancio.getCd_cds(), bilancio.getTipoGestione())); return
             * bilancio; }
             */

        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
        return bulk;
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    private void inizializzaBulkPerStampa(UserContext userContext,
                                          Stampa_avviso_pag_mandBulk stampa) throws ComponentException {

        stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));
        stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));

        stampa.setPgInizioMand(new Long(0));
        stampa.setPgFineMand(new Long("9999999999"));

        stampa.setPgInizioDist(new Long(0));
        stampa.setPgFineDist(new Long("9999999999"));

        String cd_uo = CNRUserContext.getCd_unita_organizzativa(userContext);

        try {
            Unita_organizzativaHome uoHome = (Unita_organizzativaHome) getHome(
                    userContext, Unita_organizzativaBulk.class);
            Unita_organizzativaBulk uo = (Unita_organizzativaBulk) uoHome
                    .findByPrimaryKey(new Unita_organizzativaBulk(cd_uo));

            Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
                    .createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
            if (uo.isUoCds() ||
                    ((Configurazione_cnrHome)getHome(userContext,Configurazione_cnrBulk.class)).isUOSpecialeDistintaTuttaSAC(CNRUserContext.getEsercizio(userContext),cd_uo)) {
                stampa.setUoEmittenteForPrint(new Unita_organizzativaBulk());
                stampa.setFindUOForPrintEnabled(true);
            } else {
                stampa.setUoEmittenteForPrint(uo);
                stampa.setFindUOForPrintEnabled(false);
            }

        } catch (it.cnr.jada.persistency.PersistencyException pe) {
            throw new ComponentException(pe);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    private void inizializzaBulkPerStampa(UserContext userContext,
                                          Stampa_giornale_mandatiBulk stampa) throws ComponentException {

        stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));
        stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));

        stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext
                .getEsercizio(userContext).intValue()));
        stampa.setDataFine(getDataOdierna(userContext));
        stampa.setPgInizio(new Long(0));
        stampa.setPgFine(new Long("9999999999"));

        // stampa.setUnita_organizzativa(new Unita_organizzativaBulk());

        stampa.setTi_mandato(Stampa_giornale_mandatiBulk.TIPO_TUTTI);
        stampa.setStato(Stampa_giornale_mandatiBulk.STATO_MANDATO_TUTTI);
        stampa.setStato_trasmissione(Stampa_giornale_mandatiBulk.STATO_TRASMISSIONE_TUTTI);

        try {
            CdsHome cds_home = (CdsHome) getHome(userContext, CdsBulk.class);
            CdsBulk cds_scrivania = (CdsBulk) cds_home
                    .findByPrimaryKey(new CdsBulk(CNRUserContext
                            .getCd_cds(userContext)));

            if (cds_scrivania.getCd_tipo_unita().equals(
                    Tipo_unita_organizzativaHome.TIPO_UO_ENTE)) {
                stampa.setUoEmittenteForPrint(new Unita_organizzativaBulk());
                stampa.setIsUOForPrintEnabled(false);
            } else {
                Unita_organizzativaHome uoHome = (Unita_organizzativaHome) getHome(
                        userContext, Unita_organizzativaBulk.class);
                Unita_organizzativaBulk uo = (Unita_organizzativaBulk) uoHome
                        .findByPrimaryKey(new Unita_organizzativaBulk(
                                CNRUserContext
                                        .getCd_unita_organizzativa(userContext)));

                if (!uo.isUoCds()) {
                    stampa.setUoEmittenteForPrint(uo);
                    stampa.setIsUOForPrintEnabled(false);
                } else {
                    stampa
                            .setUoEmittenteForPrint(new Unita_organizzativaBulk());
                    stampa.setIsUOForPrintEnabled(true);
                }
            }

        } catch (it.cnr.jada.persistency.PersistencyException pe) {
            throw new ComponentException(pe);
        }
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    private void inizializzaBulkPerStampa(UserContext userContext,
                                          Stampa_vpg_mandatoBulk stampa) throws ComponentException {
        try {
            CdsHome cds_home = (CdsHome) getHome(userContext, CdsBulk.class);
            CdsBulk cds_scrivania = (CdsBulk) cds_home
                    .findByPrimaryKey(new CdsBulk(CNRUserContext
                            .getCd_cds(userContext)));

            if (cds_scrivania.getCd_tipo_unita().equals(
                    Tipo_unita_organizzativaHome.TIPO_UO_ENTE)) {
                stampa.setUoEmittenteForPrint(new Unita_organizzativaBulk());
                stampa.setIsUOForPrintEnabled(false);
            } else {
                Unita_organizzativaHome uoHome = (Unita_organizzativaHome) getHome(
                        userContext, Unita_organizzativaBulk.class);
                Unita_organizzativaBulk uo = (Unita_organizzativaBulk) uoHome
                        .findByPrimaryKey(new Unita_organizzativaBulk(
                                CNRUserContext
                                        .getCd_unita_organizzativa(userContext)));

                if (!uo.isUoCds()) {
                    stampa.setUoEmittenteForPrint(uo);
                    stampa.setIsUOForPrintEnabled(false);
                } else {
                    stampa
                            .setUoEmittenteForPrint(new Unita_organizzativaBulk());
                    stampa.setIsUOForPrintEnabled(true);
                }
            }
            stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));
            stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));
            stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext
                    .getEsercizio(userContext).intValue()));
            stampa.setDataFine(getDataOdierna(userContext));
            //stampa.setPgInizio(new Long(0));
            //stampa.setPgFine(new Long("9999999999"));

            stampa.setTerzoForPrint(new TerzoBulk());
        } catch (it.cnr.jada.persistency.PersistencyException pe) {
            throw new ComponentException(pe);
        }


    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(
            it.cnr.jada.UserContext userContext,
            it.cnr.jada.bulk.OggettoBulk bulk)
            throws it.cnr.jada.comp.ComponentException {

        if (bulk instanceof Stampa_giornale_mandatiBulk)
            inizializzaBulkPerStampa(userContext,
                    (Stampa_giornale_mandatiBulk) bulk);
        else if (bulk instanceof Stampa_vpg_mandatoBulk)
            inizializzaBulkPerStampa(userContext, (Stampa_vpg_mandatoBulk) bulk);
        else if (bulk instanceof Stampa_avviso_pag_mandBulk)
            inizializzaBulkPerStampa(userContext,
                    (Stampa_avviso_pag_mandBulk) bulk);

        return bulk;
    }

    /**
     * verifica se il mandato contiene righe relative a fatture passive
     * istituzionali di beni intra ue o san marino (in tal caso alla creazione
     * del mandato è stata creata una reversale associata). Questa informazione
     * serve ai fini della quadratura dei PreCondition: E' stata richiesta
     * l'inizializzazione di una istanza di MandatoBulk di tipo A REGOLAMENTO
     * SOSPESO e con importo ritenute > 0 PostCondition: Il flag fai_reversale è
     * inizializzato a true nel caso in cui il mandato contenga tali tipi di
     * fatture e a false altrimenti
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoIBulk</code> il mandato da inizializzare
     * @return MandatoIBulk il mandato inizializzato
     */
    private MandatoIBulk inizializzaFlagFaiReversale(UserContext userContext,
                                                     MandatoIBulk mandato) throws ComponentException {

        try {
            String statement = "SELECT A.FL_FAI_REVERSALE FROM "
                    + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                    +
                    /*
                     * 13.3.2003 tuning della query
                     * "V_DOC_PASSIVO_OBBLIGAZIONE A,  " +
                     */
                    "V_DOC_PASSIVO A,  "
                    + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                    + "MANDATO_RIGA B  "
                    + "WHERE "
                    + "A.FL_FAI_REVERSALE = ? AND "
                    + "B.CD_CDS = ? AND "
                    + "B.PG_MANDATO = ? AND "
                    + "B.ESERCIZIO = ? AND "
                    + "B.CD_CDS = A.CD_CDS_OBBLIGAZIONE AND "
                    + "B.ESERCIZIO_OBBLIGAZIONE = A.ESERCIZIO_OBBLIGAZIONE AND "
                    + "B.ESERCIZIO_ORI_OBBLIGAZIONE = A.ESERCIZIO_ORI_OBBLIGAZIONE AND "
                    + "B.PG_OBBLIGAZIONE = A.PG_OBBLIGAZIONE AND "
                    + "B.PG_OBBLIGAZIONE_SCADENZARIO = A.PG_OBBLIGAZIONE_SCADENZARIO AND "
                    + "B.CD_CDS_DOC_AMM = A.CD_CDS AND "
                    + "B.CD_UO_DOC_AMM = A.CD_UNITA_ORGANIZZATIVA AND "
                    + "B.ESERCIZIO_DOC_AMM = A.ESERCIZIO AND "
                    + "B.CD_TIPO_DOCUMENTO_AMM = A.CD_TIPO_DOCUMENTO_AMM AND "
                    + "B.PG_DOC_AMM = A.PG_DOCUMENTO_AMM ";

            LoggableStatement ps = new LoggableStatement(getHomeCache(
                    userContext).getConnection(), statement, true, this
                    .getClass());
            try {
                ps.setString(1, "Y");
                ps.setString(2, mandato.getCd_cds());
                ps.setObject(3, mandato.getPg_mandato());
                ps.setObject(4, mandato.getEsercizio());

                ResultSet rs = ps.executeQuery();
                try {
                    while (rs.next()) {
                        mandato.setFaiReversale(true);
                        break;
                    }
                } catch (Exception e) {
                    throw handleException(e);
                } finally {
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                    }
                }
            } catch (SQLException e) {
                throw handleException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
            }
        } catch (SQLException e) {
            throw handleException(e);
        }

        return mandato;

    }

    /**
     * inizializzazione della lista dei sospesi associati ai dcoumenti amm. con
     * 1210 PreCondition: E' stata richiesta l'inizializzazione di una istanza
     * di MandatoBulk PostCondition: La lista dei codici dei sospesi definiti
     * per i modelli 1210 inclusi nel mandato e' stata valorizzata
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoIBulk</code> il mandato da inizializzare
     * @return MandatoIBulk il mandato inizializzato
     */
    private MandatoIBulk inizializzaSospesiDa1210(UserContext userContext,
                                                  MandatoIBulk mandato) throws ComponentException {

        try {
            String statement = "SELECT DISTINCT cd_sospeso FROM "
                    + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                    +
                    /*
                     * 13.3.2003 tuning della query
                     * "V_DOC_PASSIVO_OBBLIGAZIONE A,  " +
                     */
                    "V_MANDATO_RIGA_DP_SOSP " + "WHERE " + "CD_CDS = ? AND "
                    + "PG_MANDATO = ? AND " + "ESERCIZIO = ? ";

            LoggableStatement ps = new LoggableStatement(getHomeCache(
                    userContext).getConnection(), statement, true, this
                    .getClass());
            try {

                ps.setString(1, mandato.getCd_cds());
                ps.setObject(2, mandato.getPg_mandato());
                ps.setObject(3, mandato.getEsercizio());

                ResultSet rs = ps.executeQuery();
                String cd_sospeso;
                try {
                    while (rs.next()) {
                        cd_sospeso = rs.getString(1);
                        if (cd_sospeso != null)
                            mandato.getSospesiDa1210List().add(cd_sospeso);
                    }
                } catch (Exception e) {
                    throw handleException(e);
                } finally {
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                    }
                }
            } catch (SQLException e) {
                throw handleException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
            }
        } catch (SQLException e) {
            throw handleException(e);
        }

        return mandato;

    }

    /**
     * inizializzazione dell'attributo ti_fattura di una istanza di
     * Mandato_rigaBulk PreCondition: E' stata richiesta l'inizializzazione di
     * una istanza di Mandato_rigaBulk La riga del mandato e' relativa ad un
     * documento amministrativo di tipo fattura (attiva o passiva)
     * PostCondition: Il tipo fattura ( fattura, nota a credito, nota a debito)
     * e' stato caricato per la riga del mandato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riga        <code>Mandato_rigaBulk</code> la riga del mandato da
     *                    inizializzare
     */
    public Mandato_rigaBulk inizializzaTi_fattura(UserContext userContext,
                                                  Mandato_rigaBulk riga) throws ComponentException {
        try {
            if (riga.getCd_tipo_documento_amm() != null
                    && (riga.getCd_tipo_documento_amm()
                    .equals(it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA)))
                return ((Mandato_rigaHome) getHome(userContext, riga.getClass()))
                        .initializeTi_fatturaPerFattura(riga, "FATTURA_ATTIVA");
            else if (riga.getCd_tipo_documento_amm() != null
                    && (riga.getCd_tipo_documento_amm()
                    .equals(it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA)))
                return ((Mandato_rigaHome) getHome(userContext, riga.getClass()))
                        .initializeTi_fatturaPerFattura(riga, "FATTURA_PASSIVA");
            return riga;
        } catch (SQLException _ex) {
            throw handleException(_ex);
        }

    }

    protected Integer inserisciVsx(UserContext userContext, Long pg_call,
                                   MandatoAccreditamentoBulk mandato, V_impegnoBulk impegno,
                                   Integer last_par_num) throws ComponentException {
        try {
            LoggableStatement ps = new LoggableStatement(
                    getConnection(userContext),
                    "INSERT  INTO "
                            + it.cnr.jada.util.ejb.EJBCommonServices
                            .getDefaultSchema()
                            + "VSX_MAN_ACC ( "
                            + "PG_CALL, "
                            + "PAR_NUM, "
                            + "ESERCIZIO, "
                            + "CD_CDS, "
                            + "PG_MANDATO, "
                            + "ESERCIZIO_ORI_OBBLIGAZIONE, "
                            + "PG_OBBLIGAZIONE, "
                            + "PG_OBBLIGAZIONE_SCADENZARIO, "
                            + "PG_VER_REC_OBB_SCAD, "
                            + "IM_RIGA, "
                            + "PROC_NAME, "
                            + "PG_VER_REC, "
                            + "UTUV, "
                            + "UTCR, "
                            + "DACR, "
                            + "DUVA, "
                            + "MESSAGETOUSER ) "
                            + "VALUES (	?, ?,	?, ?,	?,	?,	?,	?,	?,	?,	?,	?,	?,	?,	?, ?, ? )",
                    true, this.getClass());

            try {
                ps.setObject(1, pg_call);
                ps.setObject(2, last_par_num);
                ps.setObject(3, mandato.getEsercizio());
                ps.setString(4, mandato.getCd_cds());
                ps.setObject(5, mandato.getPg_mandato());
                ps.setObject(6, impegno.getEsercizio_originale());
                ps.setObject(7, impegno.getPg_obbligazione());
                ps.setObject(8, impegno.getPg_obbligazione_scadenzario());
                ps.setObject(9, impegno.getPg_ver_rec_scadenza());
                ps.setObject(10, impegno.getIm_da_trasferire());
                ps.setString(11, VSX_PROC_NAME);
                ps.setObject(12, new Integer(1));
                ps.setString(13, userContext.getUser());
                ps.setString(14, userContext.getUser());
                Timestamp now = it.cnr.jada.util.ejb.EJBCommonServices
                        .getServerTimestamp();
                ps.setTimestamp(15, now);
                ps.setTimestamp(16, now);
                ps.setString(17, null);
                ps.executeUpdate();

                last_par_num = new Integer(last_par_num.intValue() + 1);
                return last_par_num;

            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
            }

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * ricerca documenti attivi per regolarizzazione PreCondition: E' stata
     * richiesta la creazione di un mandato di regolarizzazione CNR L'utente ha
     * selezionato l'accertamento su cui creare in automatico la reversale di
     * regolarizzazione PostCondition: Vengono ricercati tutti i documenti
     * attivi che sono stati contabilizzati sulle scadenze dell'accertamento
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoIBulk</code> il mandato
     * @return mandato il Mandato dopo la ricerca dei documenti attivi
     */

    public MandatoIBulk listaDocAttiviPerRegolarizzazione(
            UserContext userContext, MandatoIBulk mandato)
            throws ComponentException {
        try {
            Collection result = ((MandatoIHome) getHome(userContext, mandato
                    .getClass())).findDocAttiviPerRegolarizzazione(
                    (it.cnr.contab.utenze00.bp.CNRUserContext) userContext,
                    mandato);
            Hashtable ht = ((V_doc_attivo_accertamentoHome) getHome(
                    userContext, V_doc_attivo_accertamentoBulk.class))
                    .loadTipoDocumentoKeys(new V_doc_attivo_accertamentoBulk());
            for (Iterator j = result.iterator(); j.hasNext(); )
                ((V_doc_attivo_accertamentoBulk) j.next())
                        .setTipoDocumentoKeys(ht);

            mandato.setDocGenericiPerRegolarizzazione(result);
            return mandato;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * ricerca documenti passivi PreCondition: E' stata richiesta la ricerca dei
     * documenti passivi per cui e' possibile emettere un mandato PostCondition:
     * Vengono ricercati tutti i documenti passivi che verificano le seguenti
     * condizioni: - cds e uo origine uguali a cds e uo di scrivania - cds di
     * appartenenza uguale al cds per cui si vuole emettere il mandato -
     * (im_scadenza-im_associato_doc_contabile) della scadenza di obbligazione
     * su cui il documento amm. e' stato contabilizzato maggiore di zero Fra
     * tutti i documenti individuati vengono esclusi quelli che eventualmente
     * sono già stati selezionati per questo mandato
     *
     * @param aUC     lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato <code>MandatoBulk</code> il mandato
     * @return mandato il Mandato emesso dopo la ricerca dei documenti passivi
     */

    public MandatoBulk listaDocPassivi(UserContext aUC, MandatoBulk mandato)
            throws ComponentException {
        Mandato_rigaIBulk riga;
        V_doc_passivo_obbligazioneBulk docPassivo;
        try {
            Collection result = ((MandatoIHome) getHome(aUC, mandato.getClass()))
                    .findDocPassivi((MandatoIBulk) mandato,
                            (it.cnr.contab.utenze00.bp.CNRUserContext) aUC);
            if (result.size() == 0)
                throw new ApplicationException(
                        "La ricerca non ha fornito alcun risultato.");

            // elimino dal risultato i doc passivi già selezionati per questo
            // mandato
            for (Iterator j = result.iterator(); j.hasNext(); ) {
                docPassivo = (V_doc_passivo_obbligazioneBulk) j.next();
                docPassivo.setTipoDocumentoKeys((Hashtable) mandato
                        .getTipoDocumentoKeys());
                for (Iterator i = mandato.getMandato_rigaColl().iterator(); i
                        .hasNext(); ) {
                    riga = (Mandato_rigaIBulk) i.next();
                    if (docPassivo.getEsercizio_ori_obbligazione().intValue() == riga
                            .getEsercizio_ori_obbligazione().intValue()
                            && docPassivo.getPg_obbligazione().longValue() == riga
                            .getPg_obbligazione().longValue()
                            && docPassivo.getPg_obbligazione_scadenzario()
                            .longValue() == riga
                            .getPg_obbligazione_scadenzario()
                            .longValue()
                            && docPassivo.getEsercizio_obbligazione()
                            .intValue() == riga
                            .getEsercizio_obbligazione().intValue()
                            && docPassivo.getCd_cds_obbligazione().equals(
                            riga.getCd_cds())
                            && docPassivo.getEsercizio().intValue() == riga
                            .getEsercizio_doc_amm().intValue()
                            && docPassivo.getCd_unita_organizzativa().equals(
                            riga.getCd_uo_doc_amm())
                            && docPassivo.getCd_cds().equals(
                            riga.getCd_cds_doc_amm())
                            && docPassivo.getCd_tipo_documento_amm().equals(
                            riga.getCd_tipo_documento_amm())
                            && docPassivo.getPg_documento_amm().longValue() == riga
                            .getPg_doc_amm().longValue())
                        j.remove();
                }
            }

            ((MandatoIBulk) mandato).setDocPassiviColl(result);
            return mandato;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(mandato, e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(mandato, e);
        }
    }

    /**
     * ricerca impegni CNR PreCondition: E' stata richiesta la ricerca degli
     * impegni del CNR per emettere un mandato di accreditamento verso un Cds
     * PostCondition: Vengono ricercati tutti gli impegni che hanno un importo
     * disponibile ( importo disponibile = importo iniziale dell'impegno -
     * importo già associato ai documenti contabili) e la cui voce del piano
     * abbia come cd_proprio il codice del cds beneficiario del mandato di
     * accreditamento e appartenga alla parte 1 del piano dei conti CNR parte
     * spese
     *
     * @param aUC     lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato <code>MandatoBulk</code> il mandato di accreditamento
     * @return mandato il Mandato di accreditamento emesso dopo la ricerca degli
     * impegni del CNR
     */

    public MandatoAccreditamentoBulk listaImpegniCNR(UserContext aUC,
                                                     MandatoAccreditamentoBulk mandato) throws ComponentException {
        try {
            Collection result = ((MandatoAccreditamentoHome) getHome(aUC,
                    mandato.getClass())).findImpegni(mandato);
            ((MandatoAccreditamentoWizardBulk) mandato).setImpegniColl(result);
            int size = ((MandatoAccreditamentoWizardBulk) mandato)
                    .getImpegniColl().size();
            if (size == 0)
                throw new ApplicationException("Non esistono impegni");
            for (Iterator i = ((MandatoAccreditamentoWizardBulk) mandato)
                    .getImpegniColl().iterator(); i.hasNext(); )
                ((V_impegnoBulk) i.next()).setNrImpegni(size);
            return mandato;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(mandato, e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(mandato, e);
        }

    }

    /**
     * lista CdS - disp. cassa PreCondition: E' stata richiesta la disponibilità
     * di cassa di tutti i Cds PostCondition: Vengono estratte le disponibilità
     * di cassa di tutti i Cds per l'esercizio di scrivania calcolate nella
     * vista V_DISP_CASSA_CDS
     * <p>
     * lista CdS - obbligazione PreCondition: E' stata richiesta la situazione
     * delle obbligazione non pagate per alcuni Cds selezionati dall'utente
     * PostCondition: Vengono sommati gli importi relative a scadenza di
     * obbligazioni definitive (sia su partite di giro che non) non ancora
     * pagate per una certa data per tutti i cds selezionati dall'utente
     *
     * @param aUC     lo <code>UserContext</code> che ha generato la richiesta
     * @param ricerca <code>RicercaMandatoAccreditamentoBulk</code> il mandato di
     *                accreditamento
     * @return ricerca il Mandato di accreditamento aggiornato in base alla
     * situazione cassa del Cds
     */

    public RicercaMandatoAccreditamentoBulk listaSituazioneCassaCds(
            UserContext aUC, RicercaMandatoAccreditamentoBulk ricerca)
            throws ComponentException {
        try {
            if (!ricerca.isFlTuttiCdsCaricati()) // ricerca le obbligazioni solo
            // per i Cds selezionati
            {
                ((MandatoAccreditamentoHome) getHome(aUC,
                        MandatoAccreditamentoBulk.class))
                        .findSituazioneCassaCds(aUC, ricerca);
                ricerca.setCentriDiSpesaColl(ricerca
                        .getCentriDiSpesaSelezionatiColl());
            } else // carica tutti i Cds con la disponibilità di cassa
            {
                ricerca
                        .setCentriDiSpesaColl(((V_disp_cassa_cdsHome) getHome(
                                aUC, V_disp_cassa_cdsBulk.class))
                                .findDisponibilitaCassa(((it.cnr.contab.utenze00.bp.CNRUserContext) aUC)
                                        .getEsercizio()));
            }

            return ricerca;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(ricerca, e);
        }
    }

    /**
     * modifica mandato PreCondition: E' stata generata la richiesta di modifica
     * di un Mandato e il mandato supera la validazione (metodo verificaMandato)
     * PostCondition: Vengono aggiornati gli importi dei sospesi eventualmente
     * associati al mandato (metodo aggiornaImportoSospesi), e vengono
     * aggiornate le eventuali modifiche alle modalità di pagamento e al tipo
     * bollo del mandato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> il mandato da modificare
     * @return mandato <code>OggettoBulk</code> il mandato modificato
     */

    public OggettoBulk modificaConBulk(UserContext userContext, OggettoBulk bulk)
            throws ComponentException {

        try {
            MandatoBulk mandato = (MandatoBulk) bulk;
            verificaStatoEsercizio(userContext, mandato.getEsercizio(), mandato
                    .getCd_cds());
            lockBulk(userContext, mandato);
            // mandato.refreshImporto();
            if (!mandato.isAnnullato())
                verificaMandato(userContext, mandato);
            if (mandato instanceof MandatoAccreditamentoBulk) {
                // verifica importo = somma delle righe
                // verifica che tutti gli impegni selezioanti siano a competenza
                // o a residuo
                // aggiorna la testata del mandato
                // aggiorna le modalità di pagamento se modificate
                // chiama la stored procedure che aggiorna le righe del mandato,
                // i saldi (anche del pagato), il doc.amm TRASF_S
                // inoltre modifica il mandato competenza/residuo

                MandatoAccreditamentoBulk manAcc = (MandatoAccreditamentoBulk) mandato;
                verificaMandatoAccreditamento(userContext, manAcc);
                // copio le info sulle mod.pag. e banca dalla testata alle righe
                if (manAcc.getModalita_pagamento() == null
                        || manAcc.getModalita_pagamento().getCd_modalita_pag() == null
                        || manAcc.getBanca() == null
                        || manAcc.getBanca().getPg_banca() == null)
                    throw new ApplicationException(
                            "Deve essere selezionata una modalità di pagamento e una coordinata bancaria ");
                Mandato_rigaBulk riga = (Mandato_rigaBulk) mandato
                        .getMandato_rigaColl().get(0);
                if (!riga.getCd_modalita_pag().equals(
                        manAcc.getModalita_pagamento().getCd_modalita_pag())
                        || !riga.getPg_banca().equals(
                        manAcc.getBanca().getPg_banca())) {
                    for (Iterator i = mandato.getMandato_rigaColl().iterator(); i
                            .hasNext(); ) {
                        riga = (Mandato_rigaBulk) i.next();
                        riga.setBanca(manAcc.getBanca());
                        riga.setModalita_pagamento(manAcc
                                .getModalita_pagamento());
                        riga.setToBeUpdated();
                    }
                }

                // Carico automaticamente i codici SIOPE e visualizzo quelli
                // ancora collegabili se la gestione è attiva
                if (Utility.createParametriCnrComponentSession()
                        .getParametriCnr(userContext, mandato.getEsercizio())
                        .getFl_siope().booleanValue()) {
                    for (Iterator i = mandato.getMandato_rigaColl().iterator(); i
                            .hasNext(); ) {
                        riga = (Mandato_rigaBulk) i.next();
                        // if (riga.isToBeCreated())
                        riga = aggiornaLegameSIOPE(
                                userContext, riga);
                    }
                }

                makeBulkPersistent(userContext, mandato);
                chiamaVsx(userContext, manAcc);

            } else {
                aggiornaImportoSospesi(userContext, mandato);
                if (Optional.ofNullable(mandato.getStatoVarSos())
                        .map(s -> s.equals(StatoVariazioneSostituzione.VARIAZIONE_DEFINITIVA.value()))
                        .orElse(Boolean.FALSE)) {
                    aggiornaImportoObbligazioni(userContext, mandato);
                    SaldoComponentSession session = createSaldoComponentSession(); //itera su tutte le righe
                    for (Iterator i = mandato.getMandato_rigaColl().iterator(); i.hasNext(); ) {
                        Mandato_rigaBulk riga = (Mandato_rigaBulk) i.next();
                        aggiornaCapitoloSaldoRiga(userContext, riga, session);
                    }
                    mandato = (MandatoBulk) super.modificaConBulk(userContext, bulk);
                } else {
                    mandato = (MandatoBulk) super.modificaConBulk(userContext, bulk);
                }
            }
            return mandato;
        } catch (Exception e) {
            throw handleException(bulk, e);
        }
    }

    /*
     * per le date salvate nel database come timestamp bisogna ridefinire la
     * query nel modo seguente: TRUNC( dt_nel_db) operator 'GG/MM/YYYY'
     */

    protected void ridefinisciClausoleConTimestamp(UserContext userContext,
                                                   CompoundFindClause clauses) {
        SimpleFindClause clause;
        for (Iterator i = clauses.iterator(); i.hasNext(); ) {
            clause = (SimpleFindClause) i.next();
            if (clause.getPropertyName().equalsIgnoreCase("dt_trasmissione")
                    || clause.getPropertyName().equalsIgnoreCase(
                    "dt_annullamento")
                    || clause.getPropertyName().equalsIgnoreCase(
                    "dt_ritrasmissione"))
                if (clause.getOperator() == SQLBuilder.ISNOTNULL
                        || clause.getOperator() == SQLBuilder.ISNULL)
                    clause.setSqlClause("TRUNC( " + clause.getPropertyName()
                            + ") "
                            + SQLBuilder.getSQLOperator(clause.getOperator()));
                else
                    clause.setSqlClause("TRUNC( " + clause.getPropertyName()
                            + ") "
                            + SQLBuilder.getSQLOperator(clause.getOperator())
                            + " ? ");

        }

    }

    /**
     * ricerca di un mandato di accreditamento PreCondition: E' stata richiesta
     * la ricerca di un mandato di accreditamento PostCondition: E' stato creato
     * il SQLBuilder che oltre alle clausole implicite (presenti nell'istanza di
     * MandatoBulk) su esercizio e uo origine, ha anche le clausole sul tipo
     * mandato = ACCREDITAMENTO
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param clauses     <code>CompoundFindClause</code> le clausole della selezione
     * @param bulk        <code>OggettoBulk</code> il mandato di accreditamento
     * @return sql <code>Query</code> Risultato della selezione
     */

    /* in automatico esercizio + cd_uo_origine */

    /*
     * per le date salvate nel database come timestamp bisogna ridefinire la
     * query nel modo seguente: TRUNC( dt_nel_db) operator 'GG/MM/YYYY'
     */
    protected Query select(UserContext userContext, CompoundFindClause clauses,
                           OggettoBulk bulk) throws ComponentException,
            it.cnr.jada.persistency.PersistencyException {
        /* COMPORTAMENTO DI DEFAULT - INIZIO */
        final Optional<V_mandato_terzoBulk> v_mandato_terzoBulk1 =
                Optional.ofNullable(bulk)
                        .filter(V_mandato_terzoBulk.class::isInstance)
                        .map(V_mandato_terzoBulk.class::cast);
        v_mandato_terzoBulk1
                .flatMap(v_mandato_terzoBulk -> Optional.ofNullable(v_mandato_terzoBulk.getMandato_terzo()))
                .flatMap(mandato_terzoBulk -> Optional.ofNullable(mandato_terzoBulk.getCd_terzo()))
                .ifPresent(cdTerzo -> v_mandato_terzoBulk1.get().setCd_terzo(cdTerzo));
        if (clauses == null) {
            if (bulk != null)
                clauses = bulk.buildFindClauses(null);
        } else
            clauses = it.cnr.jada.persistency.sql.CompoundFindClause.and(
                    clauses, bulk.buildFindClauses(Boolean.FALSE));
        /* COMPORTAMENTO DI DEFAULT - FINE */

        if (clauses != null)
            ridefinisciClausoleConTimestamp(userContext, clauses);

        SQLBuilder sql = getHome(userContext, bulk).selectByClause(clauses);

        if (!(bulk instanceof MandatoAccreditamentoBulk))
            sql.addClause("AND", "ti_mandato", SQLBuilder.NOT_EQUALS,
                    MandatoBulk.TIPO_ACCREDITAMENTO);
        // else if ( bulk instanceof MandatoBulk &&
        // MandatoBulk((MandatoBulk)bulk).getTi_mandato()

        // sql.addClause( "AND", "cd_cds", sql.EQUALS, ((MandatoBulk)
        // bulk).getUnita_organizzativa().getUnita_padre().getCd_unita_organizzativa());
        // sql.addClause( "AND", "cd_uo_origine", sql.EQUALS, ((MandatoBulk)
        // bulk).getUnita_organizzativa().getCd_unita_organizzativa());

        if (((CNRUserContext) userContext).getCd_unita_organizzativa().equals(
                ((MandatoBulk) bulk).getCd_uo_ente()))
            sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS,
                    ((MandatoBulk) bulk).getCd_uo_ente());
        return sql;
    }

    /**
     * ricerca di un accertamento da parte del CNR PreCondition: E' stata
     * richiesta la ricerca di un accertamento da utilizzare nel mandato di
     * regolarizzazione emesso dal CNR per creare in automatico la reversale di
     * regolarizzazione PostCondition: E' stato creato il SQLBuilder per
     * selezionare un accertamento con cds di appartenenza uguale a quello del
     * CDS del mandato, cds e uo origine qualsiasi, con importo accertamento -
     * importo associato a a documenti contabili maggiore di 0
     * <p>
     * ricerca di un accertamento da parte del CDS PreCondition: E' stata
     * richiesta la ricerca di un accertamento da utilizzare nel mandato di
     * regolarizzazione emesso da un CDS, diverso dall'Ente, per creare in
     * automatico la reversale di regolarizzazione PostCondition: E' stato
     * creato il SQLBuilder per selezionare un accertamento con cds di
     * appartenenza uguale a quello del CDS del mandato, cds e uo origine uguali
     * a quelli di scrivania, non su partita di giro, con importo accertamento -
     * importo associato a a documenti contabili maggiore di 0
     *
     * @param userContext  lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato      lo <code>MandatoIBulk</code> il mandato di regolarizzazione
     *                     emesso dal CNR
     * @param accertamento lo <code>AccertamentoBulk</code> accertamento da utilizzare
     *                     nel mandato di regolarizzazione emesso dal CNR
     * @param clauses      <code>CompoundFindClause</code> le clausole della selezione
     * @return sql <code>SQLBuilder</code> Risultato della selezione
     */

    public SQLBuilder selectAccertamentoPerRegolarizzazioneByClause(
            UserContext userContext, MandatoIBulk mandato,
            AccertamentoBulk accertamento, CompoundFindClause clauses)
            throws ComponentException,
            it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, AccertamentoBulk.class,
                "V_ACCERTAMENTO_IM_REVERSALE").createSQLBuilder();
        EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class)
                .findAll().get(0);
        if (!mandato.getCd_cds().equals(ente.getCd_unita_organizzativa())) // mandato
        // CDS
        {
            sql.openParenthesis("AND");
            sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, ente
                    .getCd_unita_organizzativa());
            sql.addClause("OR", "cd_cds", SQLBuilder.EQUALS, mandato.getCd_cds());
            sql.closeParenthesis();
        } else
            // mandato CNR
            sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, mandato.getCd_cds());

        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, mandato.getEsercizio());
        sql.addClause("AND", "cd_tipo_documento_cont", SQLBuilder.NOT_EQUALS,
                Numerazione_doc_contBulk.TIPO_ACR_SIST);
        sql.addClause("AND", "cd_tipo_documento_cont", SQLBuilder.NOT_EQUALS,
                Numerazione_doc_contBulk.TIPO_ACR_PLUR);
        sql.addClause("AND", "dt_cancellazione", SQLBuilder.ISNULL, null);
        sql.addSQLClause("AND", "riportato", SQLBuilder.EQUALS, "N");
        if (!mandato.getCd_cds().equals(ente.getCd_unita_organizzativa())) // mandato
        // CDS
        {
            sql.addClause("AND", "cd_cds_origine", SQLBuilder.EQUALS, mandato
                    .getCd_cds_origine());
            sql.addClause("AND", "cd_uo_origine", SQLBuilder.EQUALS, mandato
                    .getCd_uo_origine());
            sql.addSQLClause("AND", "IM_ACCERTAMENTO - IM_REVERSALE",
                    SQLBuilder.GREATER, new BigDecimal(0));
        } else
            // mandato CNR
            sql.addSQLClause("AND", "IM_ACCERTAMENTO - IM_REVERSALE",
                    SQLBuilder.GREATER, new BigDecimal(0));

        sql.addClause(clauses);
        return sql;
    }

    /**
     * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole
     * specificate. Aggiunge una clausola a tutte le operazioni di ricerca
     * eseguite sulla Unita Organizzativa
     * <p>
     * Nome: Richiesta di ricerca di una Unita Organizzativa Pre: E' stata
     * generata la richiesta di ricerca delle UO associate al Cds di scrivania
     * Post: Viene restituito l'SQLBuilder per filtrare le UO in base al cds di
     * scrivania
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk        l'OggettoBulk che rappresenta il contesto della ricerca.
     * @param uo          l'OggettoBulk da usare come prototipo della ricerca; sul
     *                    prototipo vengono costruite delle clausole aggiuntive che
     *                    vengono aggiunte in AND alle clausole specificate.
     * @param clauses     L'albero logico delle clausole da applicare alla ricerca
     * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire
     * e tutti i parametri della query.
     **/
    public SQLBuilder selectUoEmittenteForPrintByClause(
            UserContext userContext, Stampa_avviso_pag_mandBulk bulk,
            Unita_organizzativaBulk uo, CompoundFindClause clauses)
            throws ComponentException {

        Unita_organizzativaHome home = (Unita_organizzativaHome) getHome(
                userContext, Unita_organizzativaBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "cd_unita_padre", SQLBuilder.EQUALS, bulk.getCd_cds());
        sql.addClause(clauses);
        return sql;
    }

    /**
     * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole
     * specificate. Aggiunge una clausola a tutte le operazioni di ricerca
     * eseguite sulla Unita Organizzativa
     * <p>
     * Nome: Richiesta di ricerca di una Unita Organizzativa Pre: E' stata
     * generata la richiesta di ricerca delle UO associate al Cds di scrivania
     * Post: Viene restituito l'SQLBuilder per filtrare le UO in base al cds di
     * scrivania
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk        l'OggettoBulk che rappresenta il contesto della ricerca.
     * @param uo          l'OggettoBulk da usare come prototipo della ricerca; sul
     *                    prototipo vengono costruite delle clausole aggiuntive che
     *                    vengono aggiunte in AND alle clausole specificate.
     * @param clauses     L'albero logico delle clausole da applicare alla ricerca
     * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire
     * e tutti i parametri della query.
     **/
    public SQLBuilder selectUoEmittenteForPrintByClause(
            UserContext userContext, Stampa_giornale_mandatiBulk bulk,
            Unita_organizzativaBulk uo, CompoundFindClause clauses)
            throws ComponentException {

        Unita_organizzativaHome home = (Unita_organizzativaHome) getHome(
                userContext, Unita_organizzativaBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "cd_unita_padre", SQLBuilder.EQUALS, bulk.getCd_cds());
        sql.addClause(clauses);
        return sql;
    }

    public SQLBuilder selectUoEmittenteForPrintByClause(
            UserContext userContext, Stampa_vpg_mandatoBulk bulk,
            Unita_organizzativaBulk uo, CompoundFindClause clauses)
            throws ComponentException {

        Unita_organizzativaHome home = (Unita_organizzativaHome) getHome(
                userContext, Unita_organizzativaBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "cd_unita_padre", SQLBuilder.EQUALS, bulk.getCd_cds());
        sql.addClause(clauses);
        return sql;
    }

    /**
     * stampaConBulk method comment.
     */
    public it.cnr.jada.bulk.OggettoBulk stampaConBulk(
            it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk)
            throws it.cnr.jada.comp.ComponentException {

        if (bulk instanceof Stampa_giornale_mandatiBulk)
            validateBulkForPrint(aUC, (Stampa_giornale_mandatiBulk) bulk);
        else if (bulk instanceof Stampa_vpg_mandatoBulk)
            validateBulkForPrint(aUC, (Stampa_vpg_mandatoBulk) bulk);
        else if (bulk instanceof Stampa_avviso_pag_mandBulk)
            validateBulkForPrint(aUC, (Stampa_avviso_pag_mandBulk) bulk);

        return bulk;
    }

    /**
     * Validazione dell'oggetto in fase di stampa
     */
    private void validateBulkForPrint(it.cnr.jada.UserContext userContext,
                                      Stampa_avviso_pag_mandBulk stampa) throws ComponentException {

        try {

            if (stampa.getPgInizioMand() == null)
                throw new ValidationException(
                        "Attenzione: il campo Numero Inizio Mandato è obbligatorio");
            if (stampa.getPgFineMand() == null)
                throw new ValidationException(
                        "Attenzione: il campo Numero Fine Mandato è obbligatorio");

            if (stampa.getPgInizioDist() == null)
                throw new ValidationException(
                        "Attenzione: il campo Numero Inizio Distinta è obbligatorio");
            if (stampa.getPgFineDist() == null)
                throw new ValidationException(
                        "Attenzione: il campo Numero Fine Distinta è obbligatorio");

            if (stampa.getPgInizioMand().compareTo(stampa.getPgFineMand()) > 0)
                throw new ValidationException(
                        "Attenzione: il Numero Inizio Mandato non può essere superiore al Numero Fine Mandato");

            if (stampa.getPgInizioDist().compareTo(stampa.getPgFineDist()) > 0)
                throw new ValidationException(
                        "Attenzione: il Numero Inizio Distinta non può essere superiore al Numero Fine Distinta");

        } catch (ValidationException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Validazione dell'oggetto in fase di stampa
     */
    private void validateBulkForPrint(it.cnr.jada.UserContext userContext,
                                      Stampa_giornale_mandatiBulk stampa) throws ComponentException {

        try {
            Timestamp dataOdierna = getDataOdierna(userContext);

            if (stampa.getEsercizio() == null)
                throw new ValidationException(
                        "Il campo ESERCIZIO e' obbligatorio");
            if (stampa.getCd_cds() == null)
                throw new ValidationException("Il campo CDS e' obbligatorio");

            if (stampa.getDataInizio() == null)
                throw new ValidationException(
                        "Il campo DATA INIZIO PERIODO è obbligatorio");
            if (stampa.getDataFine() == null)
                throw new ValidationException(
                        "Il campo DATA FINE PERIODO è obbligatorio");

            java.sql.Timestamp firstDayOfYear = DateServices
                    .getFirstDayOfYear(stampa.getEsercizio().intValue());
            if (stampa.getDataInizio().compareTo(stampa.getDataFine()) > 0)
                throw new ValidationException(
                        "La DATA di INIZIO PERIODO non può essere superiore alla DATA di FINE PERIODO");
            if (stampa.getDataInizio().compareTo(firstDayOfYear) < 0) {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
                        "dd/MM/yyyy");
                throw new ValidationException(
                        "La DATA di INIZIO PERIODO non può essere inferiore a "
                                + formatter.format(firstDayOfYear));
            }
            if (stampa.getDataFine().compareTo(dataOdierna) > 0) {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
                        "dd/MM/yyyy");
                throw new ValidationException(
                        "La DATA di FINE PERIODO non può essere superiore a "
                                + formatter.format(dataOdierna));
            }

            if (stampa.getPgInizio() == null)
                throw new ValidationException(
                        "Il campo NUMERO INIZIO MANDATO è obbligatorio");
            if (stampa.getPgFine() == null)
                throw new ValidationException(
                        "Il campo NUMERO FINE MANDATO è obbligatorio");
            if (stampa.getPgInizio().compareTo(stampa.getPgFine()) > 0)
                throw new ValidationException(
                        "Il NUMERO INIZIO MANDATO non può essere superiore al NUMERO FINE MANDATO");

            // if (stampa.getCdUOEmittenteForPrint()==null)
            // throw new
            // ValidationException("Il campo UO EMITTENTE è obbligatorio");

        } catch (ValidationException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Validazione dell'oggetto in fase di stampa
     */
    private void validateBulkForPrint(it.cnr.jada.UserContext userContext,
                                      Stampa_vpg_mandatoBulk stampa) throws ComponentException {

        try {
            Timestamp dataOdierna = getDataOdierna(userContext);

            if (stampa.getEsercizio() == null)
                throw new ValidationException(
                        "Il campo ESERCIZIO e' obbligatorio");
            if (stampa.getCd_cds() == null)
                throw new ValidationException("Il campo CDS e' obbligatorio");

            if (stampa.getDataInizio() == null)
                throw new ValidationException(
                        "Il campo DATA INIZIO PERIODO è obbligatorio");
            if (stampa.getDataFine() == null)
                throw new ValidationException(
                        "Il campo DATA FINE PERIODO è obbligatorio");

            java.sql.Timestamp firstDayOfYear = DateServices
                    .getFirstDayOfYear(stampa.getEsercizio().intValue());
            if (stampa.getDataInizio().compareTo(stampa.getDataFine()) > 0)
                throw new ValidationException(
                        "La DATA di INIZIO PERIODO non può essere superiore alla DATA di FINE PERIODO");
            if (stampa.getDataInizio().compareTo(firstDayOfYear) < 0) {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
                        "dd/MM/yyyy");
                throw new ValidationException(
                        "La DATA di INIZIO PERIODO non può essere inferiore a "
                                + formatter.format(firstDayOfYear));
            }
            if (stampa.getDataFine().compareTo(dataOdierna) > 0) {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
                        "dd/MM/yyyy");
                throw new ValidationException(
                        "La DATA di FINE PERIODO non può essere superiore a "
                                + formatter.format(dataOdierna));
            }

            if (stampa.getPgInizio() == null)
                throw new ValidationException(
                        "Il campo NUMERO INIZIO MANDATO è obbligatorio");
            if (stampa.getPgFine() == null)
                throw new ValidationException(
                        "Il campo NUMERO FINE MANDATO è obbligatorio");
            if (stampa.getPgInizio().compareTo(stampa.getPgFine()) > 0)
                throw new ValidationException(
                        "Il NUMERO INIZIO MANDATO non può essere superiore al NUMERO FINE MANDATO");
            MandatoIHome home = (MandatoIHome) getHome(userContext,
                    MandatoIBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, stampa
                    .getCd_cds());
            sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, stampa
                    .getEsercizio());
            sql.addBetweenClause("AND", "pg_mandato", stampa.getPgInizio(),
                    stampa.getPgFine());
            sql.addBetweenClause("AND", "dt_emissione", stampa.getDataInizio(),
                    stampa.getDataFine());
            Mandato_rigaHome rigaHome = (Mandato_rigaHome) getHome(userContext,
                    Mandato_rigaBulk.class);
            SQLBuilder sqlRiga = rigaHome.createSQLBuilder();
            sqlRiga.addSQLJoin("MANDATO.CD_CDS", "MANDATO_RIGA.CD_CDS");
            sqlRiga.addSQLJoin("MANDATO.ESERCIZIO", "MANDATO_RIGA.ESERCIZIO");
            sqlRiga.addSQLJoin("MANDATO.PG_MANDATO", "MANDATO_RIGA.PG_MANDATO");
            sqlRiga.addClause("AND", "cd_terzo", SQLBuilder.EQUALS, stampa
                    .getTerzoForPrint().getCd_terzo());
            sql.addSQLExistsClause("AND", sqlRiga);
            Iterator i = home.fetchAll(sql).iterator();
            for (; i.hasNext(); ) {
                MandatoIBulk mandato = (MandatoIBulk) i.next();
                if (contaModalitaPagamento(userContext, mandato) > 1)
                    throw new ApplicationException(
                            "Impossibile stampare il Mandato n° "
                                    + mandato.getPg_mandato()
                                    + " in quanto le modalità di pagamento dei dettagli sono diverse.");
            }
        } catch (ValidationException ex) {
            throw new ApplicationException(ex);
        } catch (PersistencyException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * verifica 1210 - errore PreCondition: E' stata richiesta la
     * creazione/modifica di un mandato che include dei pagamenti 1210 e non
     * tutte le scadenze di obbligazione associate al 1210 sono state incluse
     * PostCondition: Un messaggio di errore viene visualizzato all'utente per
     * segnalare l'impossibilità di salvare il mandato verifica1210 - ok
     * PreCondition: E' stata richiesta la creazione/modifica di un mandato e
     * tutti i precedenti controlli sono stati superati PostCondition: Il
     * mandato ha superato la validazione e può pertanto essere salvato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> il mandato di cui si verifica la
     *                    correttezza
     */

    private void verifica1210(UserContext userContext, MandatoIBulk mandato)
            throws ComponentException {
        Mandato_rigaIBulk riga;
        Collection docPassivi;
        V_doc_passivo_obbligazioneBulk docPassivo;

        try {
            for (Iterator i = mandato.getMandato_rigaColl().iterator(); i
                    .hasNext(); ) {
                riga = (Mandato_rigaIBulk) i.next();
                if (riga.getPg_lettera() != null) {
                    docPassivi = ((V_doc_passivo_obbligazioneHome) getHome(
                            userContext, V_doc_passivo_obbligazioneBulk.class))
                            .find1210Collegati(riga);
                    for (Iterator j = docPassivi.iterator(); j.hasNext(); ) {
                        docPassivo = (V_doc_passivo_obbligazioneBulk) j.next();
                        if (!mandato.isDocPassivoIncluso(docPassivo))
                            throw new ApplicationException(
                                    "Lettera di pagamento "
                                            + riga.getPg_lettera()
                                            + ": e' necessario includere nel mandato anche la scadenza "
                                            + new java.text.SimpleDateFormat(
                                            "dd.MM.yyyyy")
                                            .format(docPassivo
                                                    .getDt_scadenza())
                                            + " dell'impegno "
                                            + docPassivo
                                            .getEsercizio_ori_obbligazione()
                                            + "/"
                                            + docPassivo.getPg_obbligazione()
                                            + ".");
                    }
                }
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * verifica disponibilità di cassa- errore PreCondition: E' stata richiesta
     * la creazione di un mandato con tipologia diversa da regolarizzazione e il
     * Cds di appartenenza non ha una disponibilità di cassa sufficiente (metodo
     * 'findDisponibilitaDiCassaPerContoCorrente') PostCondition: Un messaggio
     * di errore viene visualizzato all'utente per segnalare l'impossibilità di
     * salvare il mandato
     * <p>
     * verifica disponibilità di cassa - ok PreCondition: E' stata richiesta la
     * creazione di un mandato con tipologia diversa da regolarizzazione e il
     * Cds di apparteneza ha una disponibilità di cassa sufficiente
     * PostCondition: Il mandato supera la validazione sulla disponibilità di
     * cassa del conto corrente ed e' pertanto possibile proseguire con il suo
     * salvataggio
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoIBulk</code> il mandato emesso dal Cds di cui si
     *                    verifica disponibilità di cassa
     */

    private void verificaDisponibilitaDiCassaPerContoCorrente(
            UserContext userContext, MandatoBulk mandato)
            throws ComponentException {
        try {
            java.math.BigDecimal imDispCassa = findDisponibilitaDiCassaPerContoCorrente(
                    userContext, mandato);
            /*
             * IMPORTANTE: I mandati di accreditamento aggiornano i saldi sui
             * capitoli prima di effettuare questa verifica, pertanto la
             * disponibilità di cassa ritonata dalla vista V_DISP_CASSA_CNR
             * include l'importo del mandato che si sta emettendo.In questo caso
             * la verifica deve essere im_disp_cassa > 0.
             *
             * In tutti gli altri tipi di mandato sia per cds che per cnr la
             * verifica deve essere im_disp_cassa - im_mandato > 0
             */
            if (!Utility.createParametriCnrComponentSession().getParametriCnr(userContext, mandato.getEsercizio()).getFl_tesoreria_unica().booleanValue()) {
                if (MandatoBulk.TIPO_ACCREDITAMENTO.equals(mandato.getTi_mandato())) {
                    if (imDispCassa.compareTo(new BigDecimal(0)) < 0)
                        throw new ApplicationException(
                                "Mandato superiore alla disponibilità di cassa");

                } else {
                    if (imDispCassa.subtract(mandato.getIm_mandato()).compareTo(
                            new BigDecimal(0)) < 0)
                        throw new ApplicationException(
                                "Mandato superiore alla disponibilità di cassa");
                }
            }
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * verifica mandato - errore dettaglio PreCondition: E' stata richiesta la
     * creazione/modifica di un mandato e il mandato non ha dettagli
     * (Mandato_rigaBulk) PostCondition: Un messaggio di errore viene
     * visualizzato all'utente per segnalare l'impossibilità di salvare il
     * mandato verifica mandato - errore regolamento sospeso PreCondition: E'
     * stata richiesta la creazione/modifica di un mandato a regolamento di
     * sospeso e il mandato non e' stato associato a sospesi PostCondition: Un
     * messaggio di errore viene visualizzato all'utente per segnalare
     * l'impossibilità di salvare il mandato verifica mandato - errore importo
     * regolamento sospeso PreCondition: E' stata richiesta la
     * creazione/modifica di un mandato a regolamento di sospeso e la somma dei
     * sospesi associati al mandato e' diverso dall'importo del mandato
     * PostCondition: Un messaggio di errore viene visualizzato all'utente per
     * segnalare l'impossibilità di salvare il mandato verifica mandato - errore
     * data di emissione futura PreCondition: E' stata richiesta la
     * creazione/modifica di un mandato con data di emissione futura
     * PostCondition: Un messaggio di errore viene visualizzato all'utente per
     * segnalare l'impossibilità di salvare il mandato verifica mandato - errore
     * data di emissione superiore alla data ultimo mandato PreCondition: E'
     * stata richiesta la creazione/modifica di un mandato con data di emissione
     * maggiore della data di emissione dell'ultimo mandato emesso
     * PostCondition: Un messaggio di errore viene visualizzato all'utente per
     * segnalare l'impossibilità di salvare il mandato verifica mandato - errore
     * disponibilità di cassa PreCondition: E' stata richiesta la
     * creazione/modifica di un mandato ma il Cds di appartenenza non ha una
     * disponibilità di cassa sufficiente (metodo
     * verificaDisponibilitaDiCassaPerContoCorrente) PostCondition: Un messaggio
     * di errore viene visualizzato all'utente per segnalare l'impossibilità di
     * salvare il mandato verifica mandato - errore modello 1210 PreCondition:
     * E' stata richiesta la creazione/modifica di un mandato Il mandato include
     * dei pagamenti 1210 Il mandato non include i sospesi definiti per il
     * modello 1210 (metodo verificaSospesiDa1210) PostCondition: Un messaggio
     * di errore viene visualizzato all'utente per segnalare l'impossibilità di
     * salvare il mandato verifica mandato - errore mod. pagamento PreCondition:
     * E' stata richiesta la creazione/modifica di un mandato Le righe del
     * mandato hanno come tipo di pagamento BANCARIO o POSTALE Le righe del
     * mandato hanno coordinate bancarie o postali differenti (metodo
     * 'verificaModalitaPagamento') PostCondition: Un messaggio di errore viene
     * visualizzato all'utente per segnalare l'impossibilità di salvare il
     * mandato verifica mandato - ok PreCondition: E' stata richiesta la
     * creazione/modifica di un mandato e tutti i controlli sono stati superati
     * PostCondition: Il mandato ha superato la validazione e può pertanto
     * essere salvato
     *
     * @param aUC     lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato <code>MandatoBulk</code> il mandato di cui si verifica la
     *                correttezza
     */

    protected void verificaMandato(UserContext aUC, MandatoBulk mandato)
            throws ComponentException {
        MandatoHome mh = (MandatoHome) getHome(aUC, mandato.getClass());

        // il mandato deve avere almeno un dettaglio
        if (mandato.getMandato_rigaColl().size() == 0)
            throw handleException(new ApplicationException(
                    "E' necessario selezionare almeno un documento passivo"));

        // il mandato a regolamento sospeso deve avere dei sospesi associati
        if (mandato.getTi_mandato().equals(MandatoBulk.TIPO_REGOLAM_SOSPESO)) {
            if (mandato.getSospeso_det_uscColl().size() == 0)
                throw handleException(new ApplicationException(
                        "E' necessario selezionare almeno un sospeso"));
            /*
             * simona 31.10.03 nel caso di mandato che ha generato una reversale
             * di incasso la quadratura della somma dei sospesi deve essere
             * fatto con im_mandato-im_reversale
             */

            BigDecimal imTotaleSospesi;
            if (((MandatoIBulk) mandato).isFaiReversale())
                imTotaleSospesi = mandato.getIm_mandato().subtract(
                        ((MandatoIBulk) mandato).getImReversaleDiIncassoIVA());
            else
                imTotaleSospesi = mandato.getIm_mandato();
            if (mandato.getImTotaleSospesi().compareTo(imTotaleSospesi) != 0)
                throw handleException(new ApplicationException(
                        "La somma degli importi dei sospesi deve essere uguale a "
                                + imTotaleSospesi));
        }

        // mandato di regolarizzazione
        if (mandato.getTi_mandato().equals(MandatoBulk.TIPO_REGOLARIZZAZIONE))
            verificaMandatoDiRegolarizzazione(aUC, (MandatoIBulk) mandato);

        try {
            // in caso di INSERT: verifica la data di contabilizzazione
            if (mandato.isToBeCreated()) {
                Timestamp lastDayOfTheYear = DateServices
                        .getLastDayOfYear(mandato.getEsercizio().intValue());

                if (getDataOdierna(aUC).after(lastDayOfTheYear)
                        && mandato.getDt_emissione()
                        .compareTo(lastDayOfTheYear) != 0)
                    throw new ApplicationException(
                            "La data di registrazione deve essere "
                                    + java.text.DateFormat.getDateInstance()
                                    .format(lastDayOfTheYear));

                if (mandato.getDt_emissione()
                        .compareTo(mh.getServerTimestamp()) > 0)
                    throw new ApplicationException(
                            "Non è possibile inserire un mandato con data futura");
                Timestamp dataUltMandato = ((MandatoHome) getHome(aUC, mandato
                        .getClass())).findDataUltimoMandatoPerCds(mandato);
                if (dataUltMandato != null
                        && dataUltMandato.after(mandato.getDt_emissione()))
                    throw new ApplicationException(
                            "Non è possibile inserire un mandato con data anteriore a "
                                    + java.text.DateFormat
                                    .getDateTimeInstance().format(
                                            dataUltMandato));
                // verifica disponibilità su CC
                if (!mandato.getTi_mandato().equals(
                        MandatoBulk.TIPO_REGOLARIZZAZIONE))
                    verificaDisponibilitaDiCassaPerContoCorrente(aUC, mandato);

            }
            if (mandato instanceof MandatoIBulk) {
                if (mandato.isToBeCreated())
                    verifica1210(aUC, (MandatoIBulk) mandato);
                verificaSospesiDa1210(aUC, (MandatoIBulk) mandato);
            }
            verificaModalitaPagamento(aUC, mandato);
            if (mandato.getTi_mandato().equals(MandatoBulk.TIPO_PAGAMENTO))
                verificaTracciabilitaPagamenti(aUC, mandato);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private void verificaCIGSUFatture(UserContext userContext, MandatoBulk mandatoBulk) throws ComponentException, IntrospectionException, PersistencyException {
        MandatoIHome mandatoHome = Optional.ofNullable(getHome(userContext, MandatoIBulk.class))
                .filter(MandatoIHome.class::isInstance)
                .map(MandatoIHome.class::cast)
                .orElseThrow(() -> new ComponentException("Home del mandato non trovata!"));
        final List<Mandato_siopeBulk> siopeBulks = mandatoHome.findMandato_siope(userContext, mandatoBulk)
                .stream()
                .filter(Mandato_siopeBulk.class::isInstance)
                .map(Mandato_siopeBulk.class::cast)
                .collect(Collectors.toList());
        Fattura_passivaHome fattura_passivaHome = Optional.ofNullable(getHome(userContext, Fattura_passiva_IBulk.class))
                .filter(Fattura_passivaHome.class::isInstance)
                .map(Fattura_passivaHome.class::cast)
                .orElseThrow(() -> new ComponentException("Home della fattura non trovata!"));
        Fattura_passiva_rigaHome fattura_passivaRigaHome = Optional.ofNullable(getHome(userContext, Fattura_passiva_rigaIBulk.class))
                .filter(Fattura_passiva_rigaHome.class::isInstance)
                .map(Fattura_passiva_rigaHome.class::cast)
                .orElseThrow(() -> new ComponentException("Home della fattura non trovata!"));

        final Map<String, List<Mandato_siopeBulk>> codiciSiope = siopeBulks
                .stream()
                .collect(Collectors.groupingBy(Mandato_siopeBulk::getCd_siope));

        for (String codiceSiope : codiciSiope.keySet()) {
            List<String> codiciCIG = new ArrayList<String>();
            List<String> motiviAssenzaCIG = new ArrayList<String>();
            boolean isExistsFatturaEstera = false;
            for (Mandato_siopeBulk siopeBulk : codiciSiope.get(codiceSiope)) {
                if (siopeBulk.getCd_tipo_documento_amm().equals(Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA)) {
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
            if (isExistsFatturaEstera) {
                if (codiciCIG.isEmpty() && motiviAssenzaCIG.isEmpty()) {
                    throw new ApplicationMessageFormatException("Al mandato e per il codice SIOPE {0} sono associate fatture " +
                            "commerciali su cui non è posssibile determinare il CIG!", codiceSiope);
                }
                if (codiciCIG.size() > 0 && motiviAssenzaCIG.size() > 0) {
                    throw new ApplicationMessageFormatException("Al mandato e per il codice SIOPE {0} sono associate fatture " +
                            "commerciali sia con CIG che con motivo di assenza CIG!", codiceSiope);
                }
                if (codiciCIG.size() > 1) {
                    throw new ApplicationMessageFormatException("Al mandato e per il codice SIOPE {0} sono associate fatture " +
                            "commerciali con CIG diversi : {1}!",
                            codiceSiope,
                            String.join(" - ", codiciCIG.stream().collect(Collectors.toList()))
                    );
                }
                if (motiviAssenzaCIG.size() > 1) {
                    throw new ApplicationMessageFormatException("Al mandato e per il codice SIOPE {0} sono associate fatture " +
                            "commerciali con motivi di assenza CIG diversi : {1}!",
                            codiceSiope,
                            String.join(" - ", motiviAssenzaCIG.stream().collect(Collectors.toList()))
                    );
                }
            }
        }
    }

    /**
     * verifica mandato di accreditamento PreCondition: E' stata richiesta la
     * modifica di un mandato di accreditamento (modifica dei capitoli
     * finanziari quindi degli impegni) La somma degli importi associati ai
     * nuovi capitoli è uguale all'importo originario di testata del mandato I
     * nuovi impegni selezionati sono o tutti di competenza o tutti a residuo
     * PostCondition: Il mandato ha superato la validazione e può pertanto
     * essere salvato errore importo PreCondition: E' stata richiesta la
     * modifica di un mandato di accreditamento (modifica dei capitoli
     * finanziari quindi degli impegni) La somma degli importi associati ai
     * nuovi capitoli è diverso dall'importo originario di testata del mandato
     * PostCondition: Una segnalazione di errore viene emessa per comunicare
     * l'impossibilità a salvare il mandato. errore competenza/residuo
     * PreCondition: E' stata richiesta la modifica di un mandato di
     * accreditamento (modifica dei capitoli finanziari quindi degli impegni) I
     * nuovi capitoli selezionati dall'utenet sono in parte di competenza e in
     * parte a residuo PostCondition: Una segnalazione di errore viene emessa
     * per comunicare l'impossibilità a salvare il mandato.
     *
     * @param aUC     lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato <code>MandatoAccreditamentoBulk</code> il mandato di
     *                accreditamento di cui si verifica la correttezza
     */

    private void verificaMandatoAccreditamento(UserContext aUC,
                                               MandatoAccreditamentoBulk mandato) throws ComponentException {

        BigDecimal nuovo_imp = new BigDecimal(0);
        V_impegnoBulk impegno;
        String ti_competenza_residuo = null;
        for (Iterator i = mandato.getImpegniSelezionatiColl().iterator(); i
                .hasNext(); ) {
            impegno = (V_impegnoBulk) i.next();

            if (ti_competenza_residuo == null)
                ti_competenza_residuo = impegno.getTi_competenza_residuo();
            else if (!ti_competenza_residuo.equals(impegno
                    .getTi_competenza_residuo()))
                throw new ApplicationException(
                        "Sono stati selezionati sia capitoli di competenza che capitoli a residuo");

            if (impegno.getIm_da_trasferire() == null)
                throw new ApplicationException("L'impegno "
                        + impegno.getEsercizio_originale() + "/"
                        + impegno.getPg_obbligazione()
                        + " ha importo da trasferire nullo");
            if (impegno.getIm_da_trasferire().compareTo(new BigDecimal(0)) <= 0)
                throw new ApplicationException("L'impegno "
                        + impegno.getEsercizio_originale() + "/"
                        + impegno.getPg_obbligazione()
                        + " ha importo da trasferire minore o uguale a 0");
            nuovo_imp = nuovo_imp.add(impegno.getIm_da_trasferire());
        }
        if (nuovo_imp.compareTo(mandato.getIm_mandato()) != 0)
            throw new ApplicationException(
                    "La somma degli importi da trasferire sui singoli impegni differisce dall'importo del mandato");

    }

    /**
     * verifica mandato di regolarizzazione PreCondition: E' stata richiesta la
     * creazione di un mandato di regolarizzazione e l'accertamento selezionato
     * dall'utente ha un importo disponibile (somma degli importi delle scadenze
     * non associate a doc. amministrativi) da associare ai documenti
     * amministrativi/contabili uguale all'importo del mandato PostCondition: Il
     * mandato ha superato la validazione e può pertanto essere salvato verifica
     * mandato di regolarizzazione - errore PreCondition: E' stata richiesta la
     * creazione di un mandato di regolarizzazione e l'accertamento selezionato
     * dall'utente ha un importo disponibile (somma degli importi delle scadenze
     * non associate a doc. amministrativi) da associare ai documenti
     * amministrativi/contabili diverso dall'importo del mandato PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare
     * l'impossibilità di salvare il mandato verifica mandato di
     * regolarizzazione - errore sospesi PreCondition: E' stata richiesta la
     * creazione/modifica di un mandato di regolarizzazione e sono stati
     * associati dei sospesi al mandato PostCondition: Un messaggio di errore
     * viene visualizzato all'utente per segnalare l'impossibilità di salvare un
     * mandato di regolarizzazione con sospesi associati verifica mandato di
     * regolarizzazione - errore partite di giro PreCondition: E' stata
     * richiesta la creazione/modifica di un mandato di regolarizzazione e sono
     * stati associati al mandato sia impegni su partita di giro che impegni
     * generici PostCondition: Un messaggio di errore viene visualizzato
     * all'utente per segnalare l'impossibilità di salvare un mandato di
     * regolarizzazione con sia impegni su partita di giro che impegni generici
     *
     * @param aUC     lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato <code>MandatoIBulk</code> il mandato di regolarizzazione di
     *                cui si verifica la correttezza
     */

    private void verificaMandatoDiRegolarizzazione(UserContext aUC,
                                                   MandatoIBulk mandato) throws ComponentException {

        try {
            if (mandato.isToBeCreated() && !mandato.isGeneraReversaleDaDocAmm()) {
                // Locko l'accertamento selezionato dall'utente: in tal modo
                // garantisco che nessuno lo modifichi mentre
                // vengono effettuati i controlli e, nello stesso tempo, posso
                // controllare che il PG_VER_REC dell'accertamento
                // non sia cambiato da quando è stato selezionato.
                lockBulk(aUC, mandato.getAccertamentoPerRegolarizzazione());

                // verifica l'importo dell'accertamento
                BigDecimal im_accertamento_disponibile = new BigDecimal(0);
                AccertamentoBulk accertamento = (AccertamentoBulk) createAccertamentoAbstractComponentSession()
                        .inizializzaBulkPerModifica(aUC,
                                mandato.getAccertamentoPerRegolarizzazione());
                mandato.setAccertamentoPerRegolarizzazione(accertamento);
                Set<Accertamento_scadenzarioBulk> newSet = new HashSet<Accertamento_scadenzarioBulk>();

                for (Iterator i = mandato
                        .getScadenzeAccertamentoSelezionatePerRegolarizzazione()
                        .iterator(); i.hasNext(); ) {
                    Accertamento_scadenzarioBulk asMandato = (Accertamento_scadenzarioBulk) i
                            .next();
                    Accertamento_scadenzarioBulk asDB = accertamento
                            .getAccertamento_scadenzarioColl().get(
                                    accertamento
                                            .getAccertamento_scadenzarioColl()
                                            .indexOfByPrimaryKey(asMandato));
                    if (asDB != null) {
                        newSet.add(asDB);
                        if (asDB.getIm_scadenza().subtract(
                                asDB.getIm_associato_doc_contabile())
                                .compareTo(new BigDecimal(0)) > 0)
                            im_accertamento_disponibile = im_accertamento_disponibile
                                    .add(asDB
                                            .getIm_scadenza()
                                            .subtract(
                                                    asDB
                                                            .getIm_associato_doc_contabile()));
                    }
                }

                if (mandato.getIm_mandato().compareTo(
                        im_accertamento_disponibile) != 0)
                    throw new ApplicationException(
                            "L'importo del mandato e l'importo disponibile dell'accertamento non corrispondono");

                mandato
                        .setScadenzeAccertamentoSelezionatePerRegolarizzazione(newSet);
            } else if (mandato.isToBeCreated()
                    && mandato.isGeneraReversaleDaDocAmm()) {
                // verifica l'importo dei doc amm selezioanti e il terzo
                BigDecimal im_documento_disponibile = new BigDecimal(0);
                Integer cd_terzo = ((V_doc_attivo_accertamentoBulk) mandato
                        .getDocGenericiSelezionatiPerRegolarizzazione()
                        .iterator().next()).getCd_terzo();
                for (Iterator i = mandato
                        .getDocGenericiSelezionatiPerRegolarizzazione()
                        .iterator(); i.hasNext(); ) {
                    V_doc_attivo_accertamentoBulk docAttivo = (V_doc_attivo_accertamentoBulk) i
                            .next();
                    im_documento_disponibile = im_documento_disponibile
                            .add(docAttivo.getIm_totale_doc_amm());
                    if (!(cd_terzo.intValue() == docAttivo.getCd_terzo()
                            .intValue()))
                        throw new ApplicationException(
                                "Sono stati selezionati documenti amministrativi intestati a terzi diversi!");
                }
                if (mandato.getIm_mandato().compareTo(im_documento_disponibile) != 0)
                    throw new ApplicationException(
                            "L'importo del mandato e la somma degli importi dei documenti amministrativi selezionati non corrispondono");
                /*
                 * //verifica competenza/residuo String ti_competenza_residuo =
                 * mandato.getDocGenericiSelezionatiPerRegolarizzazione()(
                 * Documento_generico_rigaBulk
                 * )documento.getDocumento_generico_dettColl
                 * ().get(0)).getAccertamento_scadenziario
                 * ().getAccertamento().getTi_competenza_residuo(); for (
                 * Iterator i =
                 * documento.getDocumento_generico_dettColl().iterator();
                 * i.hasNext(); ) { Documento_generico_rigaBulk dr =
                 * (Documento_generico_rigaBulk) i.next(); if (
                 * !dr.getAccertamento_scadenziario
                 * ().getAccertamento().getTi_competenza_residuo
                 * ().equals(ti_competenza_residuo)) throw new
                 * ApplicationException(
                 * "Non e' possibile creare la reversale perchè il documento amministrativo selezionato e' stato contabilizzato in parte su accertamenti di competenza e in parte su accertamenti a residuo"
                 * ); }
                 */

            }

            // non deve essere possibile collegare contemporaneamente impegni su
            // partita di giro e impegni normali
            Boolean PGiro = null;
            for (Iterator i = mandato.getMandato_rigaColl().iterator(); i
                    .hasNext(); ) {
                Mandato_rigaBulk riga = (Mandato_rigaBulk) i.next();
                ObbligazioneBulk obbligazione = (ObbligazioneBulk) getHome(aUC,
                        ObbligazioneBulk.class).findByPrimaryKey(
                        new ObbligazioneBulk(riga.getCd_cds(), riga
                                .getEsercizio_obbligazione(), riga
                                .getEsercizio_ori_obbligazione(), riga
                                .getPg_obbligazione()));
                if (PGiro == null)
                    PGiro = obbligazione.getFl_pgiro();
                if (!PGiro.equals(obbligazione.getFl_pgiro()))
                    throw new ApplicationException(
                            "Non e' possibile associare ad un mandato di regolarizzazione sia impegni su partita di giro che impegni generici.");
            }

            if (mandato.getSospeso_det_uscColl().size() > 0)
                throw new ApplicationException(
                        "Il mandato di regolarizzazione non può avere sospesi associati");
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * annullamento mandato su anticipo associato a missione PreCondition: E'
     * stata generata la richiesta di annullare un Mandato Il mandato include un
     * anticipo L'anticipo e' associato a missione PostCondition: Una
     * segnalazione di errore comunica all'utente l'impossibilità di eseguire
     * l'annullamento
     * <p>
     * annullamento mandato su anticipo non associato a missione PreCondition:
     * E' stata generata la richiesta di annullare un Mandato Il mandato include
     * un anticipo L'anticipo non e' associato a missione PostCondition: La
     * validazione del mandato su anticipo e' stata superata ed e' pertanto
     * possibile proseguire con l'annullamento del mandato
     * <p>
     * annullamento mandato non su anticipo PreCondition: E' stata generata la
     * richiesta di annullare un Mandato Il mandato non include un anticipo
     * PostCondition: La validazione del mandato su anticipo e' stata superata
     * ed e' pertanto possibile proseguire con l'annullamento del mandato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> il mandato da annullare
     * @return mandato <code>MandatoBulk</code> il Mandato annullato
     */

    private void verificaMandatoSuAnticipo(UserContext userContext,
                                           MandatoBulk mandato) throws ComponentException {
        try {
            Mandato_rigaBulk riga;
            for (Iterator i = mandato.getMandato_rigaColl().iterator(); i
                    .hasNext(); ) {
                riga = (Mandato_rigaBulk) i.next();
                if (Numerazione_doc_ammBulk.TIPO_ANTICIPO.equals(riga
                        .getCd_tipo_documento_amm())) {
                    AnticipoBulk anticipo = (AnticipoBulk) getHome(userContext,
                            AnticipoBulk.class).findByPrimaryKey(
                            new AnticipoBulk(riga.getCd_cds_doc_amm(), riga
                                    .getCd_uo_doc_amm(), riga
                                    .getEsercizio_doc_amm(), riga
                                    .getPg_doc_amm()));
                    if (anticipo == null)
                        throw new ApplicationException(
                                "Attenzione anticipo non trovato");
                    if (anticipo.getFl_associato_missione() != null
                            && anticipo.getFl_associato_missione()
                            .booleanValue()
                            && anticipo
                            .getStato_pagamento_fondo_eco()
                            .compareTo(
                                    AnticipoBulk.STATO_REGISTRATO_FONDO_ECO) != 0)
                        throw new ApplicationException(
                                "Annullamento impossibile! L'anticipo "
                                        + anticipo.getPg_anticipo()
                                        + " presente nel mandato e' associato a missione.");
                }
            }
        } catch (Exception e) {
            throw handleException(mandato, e);
        }

    }

    /**
     * verifica mandato - errore mod. pagamento bancario PreCondition: E' stata
     * richiesta la creazione/modifica di un mandato Le righe del mandato hanno
     * come tipo di pagamento BANCARIO Le righe del mandato hanno coordinate
     * bancarie (abi, cab, nr conto) differenti PostCondition: Un messaggio di
     * errore viene visualizzato all'utente per segnalare l'impossibilità di
     * salvare il mandato verifica mandato - errore mod. pagamento postale
     * PreCondition: E' stata richiesta la creazione/modifica di un mandato Le
     * righe del mandato hanno come tipo di pagamento POSTALE Le righe del
     * mandato hanno coordinate postali ( nr conto ) differenti PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare
     * l'impossibilità di salvare il mandato verifica mandato - errore mod.
     * pagamento quietanza PreCondition: E' stata richiesta la
     * creazione/modifica di un mandato Le righe del mandato hanno come tipo di
     * pagamento QUIETANZA Le righe del mandato hanno quietanze differenti
     * PostCondition: Un messaggio di errore viene visualizzato all'utente per
     * segnalare l'impossibilità di salvare il mandato verifica mandato - errore
     * mod. pagamento quietanza PreCondition: E' stata richiesta la
     * creazione/modifica di un mandato Le righe del mandato hanno come tipo di
     * pagamento ALTRO Le righe del mandato hanno intestazioni differenti
     * PostCondition: Un messaggio di errore viene visualizzato all'utente per
     * segnalare l'impossibilità di salvare il mandato verifica mandato - ok
     * PreCondition: E' stata richiesta la creazione/modifica di un mandato Le
     * righe del mandato hanno la stesse coordinate di pagamento PostCondition:
     * Il mandato ha superato la validazione e può pertanto essere salvato
     *
     * @param aUC     lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato <code>MandatoBulk</code> il mandato di cui si verifica la
     *                correttezza
     */

    private void verificaModalitaPagamento(UserContext aUC, MandatoBulk mandato)
            throws ComponentException {
        try {
            if (mandato.getMandato_rigaColl().size() == 0)
                return;
            Mandato_rigaBulk riga = (Mandato_rigaBulk) mandato
                    .getMandato_rigaColl().get(0);

            /*
             * if ( riga.getBanca() == null || riga.getBanca().getNumero_conto()
             * == null) //mandato di regolarizzazione return;
             */
            if (riga.getBanca() == null
                    || MandatoBulk.TIPO_REGOLARIZZAZIONE.equals(mandato
                    .getTi_mandato())) // mandato di regolarizzazione
                return;
            if (riga.getModalita_pagamento() != null) {
                Rif_modalita_pagamentoBulk rifModPag = (Rif_modalita_pagamentoBulk) getHome(aUC,
                        Rif_modalita_pagamentoBulk.class).findByPrimaryKey(
                        new Rif_modalita_pagamentoBulk(riga.getModalita_pagamento().getCd_modalita_pag()));
                if (rifModPag.isMandatoRegSospeso() && !mandato.isRegolamentoSospeso())
                    throw new ApplicationException(
                            "Attenzione per la modalità di pagamento indicata il mandato deve essere a regolamento sospeso.");
                if (rifModPag.getCd_modalita_pag().compareTo("F24EP") == 0 && mandato.getDt_pagamento_richiesta() == null)
                    throw new ApplicationException(
                            "Attenzione per la modalità di pagamento indicata il mandato deve avere la data pagamento richiesta.");
                if (rifModPag.getCd_modalita_pag().compareTo("F24EP") != 0 && mandato.getDt_pagamento_richiesta() != null)
                    throw new ApplicationException(
                            "Attenzione per la modalità di pagamento " + rifModPag.getCd_modalita_pag() + " la data pagamento richiesta non deve essere indicata.");
                if (rifModPag.getCd_modalita_pag().compareTo("F24EP") == 0 && mandato.getDt_pagamento_richiesta() != null &&
                        mandato.getDt_emissione() != null && mandato.getDt_pagamento_richiesta().before(mandato.getDt_emissione()))
                    throw new ApplicationException(
                            "Attenzione per la modalità di pagamento " + rifModPag.getCd_modalita_pag() + " la data pagamento richiesta non può essere inferiore alla data contabilizzazione.");

                if (Optional.ofNullable(rifModPag)
                        .map(Rif_modalita_pagamentoBase::getTi_pagamento)
                        .filter(s -> s.equalsIgnoreCase(Rif_modalita_pagamentoBulk.BANCA_ITALIA))
                        .isPresent() &&
                        !Arrays.asList(
                                Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ACCREDITOTESORERIAPROVINCIALESTATOPERTABA.value(),
                                Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ACCREDITOTESORERIAPROVINCIALESTATOPERTABB.value())
                                .contains(Optional.ofNullable(rifModPag)
                                        .map(Rif_modalita_pagamentoBase::getTipo_pagamento_siope)
                                        .orElse(null)
                                )
                ) {
                    throw new ApplicationMessageFormatException(
                            "Attenzione la modalità di pagamento {0} non può essere utilizzata per l''emissione del Mandato!", rifModPag.getCd_modalita_pag());
                }
            }


            if (riga.getBanca() == null
                    || Rif_modalita_pagamentoBulk.ALTRO.equals(riga.getBanca()
                    .getTi_pagamento()))
                return;

            BancaBulk banca = riga.getBanca();

            String abi = riga.getBanca().getAbi();
            String cab = riga.getBanca().getCab();
            String nrConto = riga.getBanca().getNumero_conto();
            String quietanza = riga.getBanca().getQuietanza();
            String intestazione = riga.getBanca().getIntestazione();
            String cd_modalita_pag = riga.getModalita_pagamento().getCd_modalita_pag();

            if (Rif_modalita_pagamentoBulk.IBAN.equals(riga.getBanca().getTi_pagamento())) {
                final Optional<String> codiceNazione = Optional.ofNullable(riga.getBanca())
                        .flatMap(bancaBulk -> Optional.ofNullable(bancaBulk.getCodice_iban()))
                        .map(s -> s.substring(0, 2));
                if (codiceNazione.isPresent()) {
                    NazioneHome nazioneHome = (NazioneHome) getHome(aUC, NazioneBulk.class);
                    SQLBuilder sqlExists = nazioneHome.createSQLBuilder();
                    sqlExists.addSQLClause("AND", "NAZIONE.CD_ISO", SQLBuilder.EQUALS, codiceNazione.get());
                    sqlExists.addSQLClause("AND", "NAZIONE.FL_SEPA", SQLBuilder.EQUALS, "Y");
                    if (sqlExists.executeCountQuery(getConnection(aUC)) != 0)
                        throw new ApplicationMessageFormatException("Attenzione la modalità di pagamento {0} presente sul documento non è " +
                                "coerente con la nazione {1} del beneficiario!", cd_modalita_pag, codiceNazione.get());
                }
                return;
            }

            /*
             * verifico che ogni riga abbia le modalità di pagamento e gli
             * attributi della banca uguali
             */
            /* vengono escluse dal test le note di debito e le note di credito */

            for (Iterator i = mandato.getMandato_rigaColl().iterator(); i
                    .hasNext(); ) {
                riga = (Mandato_rigaBulk) i.next();

                if (Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA.equals(riga
                        .getCd_tipo_documento_amm())
                        && riga.getTi_fattura().equals(
                        Fattura_passiva_IBulk.TIPO_NOTA_DI_CREDITO)
                        && riga.getIm_mandato_riga().compareTo(
                        new BigDecimal(0)) == 0)
                    // si tratta di una nota di credito - non deve essere
                    // effettuato la verifica delle modalità di pagamento
                    continue;

                if (!riga.getModalita_pagamento().getCd_modalita_pag().equals(
                        cd_modalita_pag))
                    throw new ApplicationException(
                            "Attenzione le righe del mandato devono avere la stessa modalità di pagamento");

                // conto bancario
                if (Rif_modalita_pagamentoBulk.BANCARIO.equals(riga.getBanca()
                        .getTi_pagamento())
                        && !banca.equalsByPrimaryKey(riga.getBanca()))
                    throw new ApplicationException(
                            "Attenzione le righe del mandato devono avere la stessa modalità di pagamento bancario");
                else
                    // postale
                    if (Rif_modalita_pagamentoBulk.POSTALE.equals(riga.getBanca()
                            .getTi_pagamento())
                            && !banca.equalsByPrimaryKey(riga.getBanca()))
                        throw new ApplicationException(
                                "Attenzione le righe del mandato devono avere la stessa modalità di pagamento postale");
                    else
                        // quietanza
                        if (Rif_modalita_pagamentoBulk.QUIETANZA.equals(riga.getBanca()
                                .getTi_pagamento())
                                && !quietanza.equals(riga.getBanca().getQuietanza()))
                            throw new ApplicationException(
                                    "Attenzione le righe del mandato devono avere la stessa quietanza");
                        else
                            // altro
                            if ((Rif_modalita_pagamentoBulk.ALTRO.equals(riga.getBanca()
                                    .getTi_pagamento()) || Rif_modalita_pagamentoBulk.IBAN
                                    .equals(riga.getBanca().getTi_pagamento()))
                                    && !intestazione.equals(riga.getBanca()
                                    .getIntestazione()))
                                throw new ApplicationException(
                                        "Attenzione le righe del mandato devono avere la stessa modalità di pagamento");
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * verifica sospesi da 1210 - errore mandato non a regolamento sospeso
     * PreCondition: E' stata richiesta la creazione/modifica di un mandato che
     * include dei pagamenti 1210 e il mandato non e' a regolamento sospeso
     * PostCondition: Un messaggio di errore viene visualizzato all'utente per
     * segnalare l'impossibilità di salvare il mandato verifica sospesi da 1210
     * - errore sospeso PreCondition: E' stata richiesta la creazione/modifica
     * di un mandato che include dei pagamenti 1210 e il mandato a regolamento
     * sospeso non include il sospeso definito nel modello 1210 PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare
     * l'impossibilità di salvare il mandato verifica sospesi da 1210 - ok
     * PreCondition: E' stata richiesta la creazione/modifica di un mandato e
     * tutti i precedenti controlli sono stati superati PostCondition: Il
     * mandato ha superato la validazione e può pertanto essere salvato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> il mandato di cui si verifica la
     *                    correttezza
     */

    private void verificaSospesiDa1210(UserContext userContext,
                                       MandatoIBulk mandato) throws ComponentException {
        if (mandato.getSospesiDa1210List() != null) {
            String cd_sospeso;
            Sospeso_det_uscBulk sospesoDet;
            boolean found;
            for (Iterator i = mandato.getSospesiDa1210List().iterator(); i
                    .hasNext(); ) {
                cd_sospeso = (String) i.next();
                found = false;
                for (Iterator j = mandato.getSospeso_det_uscColl().iterator(); j
                        .hasNext(); ) {
                    sospesoDet = (Sospeso_det_uscBulk) j.next();
                    if (sospesoDet.getCd_sospeso().equals(cd_sospeso)) {
                        found = true;
                        break;
                    }
                }
                if (!found)
                    if (MandatoBulk.TIPO_REGOLAM_SOSPESO.equals(mandato
                            .getTi_mandato()))
                        throw new ApplicationException(
                                "Attenzione! Il mandato deve essere associato al sospeso "
                                        + cd_sospeso
                                        + " definito nella lettera di pagamento 1210.");
                    else
                        throw new ApplicationException(
                                "Attenzione! Il mandato deve essere a regolamento sospeso perchè include lettera di pagamento 1210.");

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

    void verificaStatoEsercizio(UserContext userContext, Integer es,
                                String cd_cds) throws ComponentException,
            it.cnr.jada.persistency.PersistencyException {
        EsercizioBulk esercizio = (EsercizioBulk) getHome(userContext,
                EsercizioBulk.class).findByPrimaryKey(
                new EsercizioBulk(cd_cds, es));
        if (esercizio == null)
            throw handleException(new ApplicationException(
                    "Inserimento impossibile: esercizio inesistente!"));
        if (!EsercizioBulk.STATO_APERTO.equals(esercizio.getSt_apertura_chiusura()))
            throw handleException(new ApplicationException(
                    "Inserimento impossibile: esercizio non aperto!"));
    }

    /**
     * verifica tipo bollo PreCondition: PostCondition:
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> il mandato di cui si verifica la
     *                    correttezza
     */

    private void verificaTipoBollo(UserContext userContext, MandatoBulk mandato)
            throws it.cnr.jada.persistency.PersistencyException,
            ComponentException {
        if (mandato instanceof MandatoIBulk) {
            Tipo_bolloBulk bollo;
            boolean found = false;
            for (Iterator i = ((MandatoIBulk) mandato).getTipoBolloOptions()
                    .iterator(); i.hasNext(); ) {
                bollo = (Tipo_bolloBulk) i.next();
                if (bollo.getCd_tipo_bollo().equals(
                        mandato.getMandato_terzo().getCd_tipo_bollo())) {
                    found = true;
                    return;
                }

            }
            if (!found) // probabilmente il tipo bollo ha il flag cancellato
            {
                bollo = (Tipo_bolloBulk) getHome(userContext,
                        Tipo_bolloBulk.class).findByPrimaryKey(
                        mandato.getMandato_terzo().getTipoBollo());
                if (bollo == null)
                    throw new ApplicationException(" Tipo bollo inesistente");
                else
                    ((MandatoIBulk) mandato).getTipoBolloOptions().add(bollo);
            }
        }
    }

    private int contaModalitaPagamento(it.cnr.jada.UserContext userContext,
                                       it.cnr.contab.doccont00.core.bulk.MandatoBulk mandato)
            throws it.cnr.jada.comp.ComponentException {
        Mandato_rigaHome home = (Mandato_rigaHome) getHome(userContext,
                Mandato_rigaBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.setDistinctClause(true);
        sql.resetColumns();
        sql.addColumn("CD_TERZO");
        sql.addColumn("CD_MODALITA_PAG");
        sql.addColumn("PG_BANCA");
        sql.addColumn("CD_TERZO_CEDENTE");
        sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, mandato.getCd_cds());
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, mandato
                .getEsercizio());
        sql.addClause("AND", "pg_mandato", SQLBuilder.EQUALS, mandato
                .getPg_mandato());
        sql.openParenthesis("AND");
        sql.openParenthesis("AND");
        sql.addClause("AND", "cd_tipo_documento_amm", SQLBuilder.EQUALS,
                Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA);
        sql.addClause("AND", "im_mandato_riga", SQLBuilder.NOT_EQUALS,
                new BigDecimal(0));
        sql.closeParenthesis();
        sql.addClause("OR", "cd_tipo_documento_amm", SQLBuilder.NOT_EQUALS,
                Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA);
        sql.closeParenthesis();
        try {
            return sql.executeCountQuery(getConnection(userContext));
        } catch (SQLException e) {
            throw new ComponentException(e);
        }
    }

    public void esistonoPiuModalitaPagamento(
            it.cnr.jada.UserContext userContext,
            it.cnr.contab.doccont00.core.bulk.MandatoBulk mandato)
            throws it.cnr.jada.comp.ComponentException {
        if (contaModalitaPagamento(userContext, mandato) > 1)
            throw new ApplicationException(
                    "Impossibile stampare. Le modalità di pagamento dei dettagli del mandato sono diverse.");
    }

    /*
     * creazione variazione di bilancio di regolarizzazione PreCondition: E'
     * stata generata la richiesta di creazione di una variazione di bilancio
     * Ente per ridurre le disponibilità di importi nel bilancio di servizio da
     * assegnare a seguito di emissione di una reversale di regolarizzazione
     * PostCondition: Viene richiesta alla Component che gestisce le Variazioni
     * di Bilancio la creazione di una variazione di regolarizzazione a partire
     * dal mandato di regolarizzazione.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la
     * richiesta
     *
     * @param mandato <code>MandatoBulk</code> il mandato di regolarizzazione
     *
     * @return reversale <code>Var_bilancioBulk</code> la variazione di bilancio
     * di regolarizzazione creata
     */
    private Var_bilancioBulk creaVariazioneBilancioDiRegolarizzazione(
            UserContext userContext, MandatoBulk mandato)
            throws ComponentException {
        try {
            Var_bilancioBulk varBilancio = null;
            VarBilancioComponentSession varSession = createVariazioneBilancioComponentSession();
            varBilancio = varSession.creaVariazioneBilancioDiRegolarizzazione(
                    userContext, mandato);
            return varBilancio;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public MandatoIBulk esitaVariazioneBilancioDiRegolarizzazione(
            UserContext userContext, MandatoIBulk mandato)
            throws ComponentException {
        try {
            if (mandato.getVar_bilancio() != null) {
                ProcedureComponentSession varSession = createProcedureComponentSession();
                mandato.setVar_bilancio(varSession.salvaDefinitivo(userContext,
                        mandato.getVar_bilancio()));
            }
        } catch (Exception e) {
            try {
                String soggetto = "Si è verificato un errore durante l'approvazione della variazione sul bilancio dell'ente "
                        + mandato.getVar_bilancio().getEsercizio()
                        + "/"
                        + mandato.getVar_bilancio().getPg_variazione();

                String preText = "Si è verificato il seguente errore durante l'approvazione della variazione sul bilancio dell'ente "
                        + mandato.getVar_bilancio().getEsercizio()
                        + "/"
                        + mandato.getVar_bilancio().getPg_variazione()
                        + "<BR>"
                        + "generata in automatico a seguito del Mandato di Regolarizzazione "
                        + mandato.getEsercizio()
                        + "/"
                        + mandato.getPg_mandato()
                        + " del CDS "
                        + mandato.getCd_cds()
                        + ".<BR><BR>"
                        + "<b>"
                        + e.getMessage()
                        + "</b><BR><BR>"
                        + "La Variazione al bilancio dell'Ente rimarrà pertanto PROVVISORIA.<BR>";

                generaEMAIL(userContext, mandato, soggetto, preText, null,
                        "ERR");
                mandato.setErroreEsitaVariazioneBilancio(true);
            } catch (IntrospectionException e1) {
                throw handleException(e1);
            } catch (PersistencyException e1) {
                throw handleException(e1);
            }
        }
        return mandato;
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative
     * alle Variazioni di Bilancio
     *
     * @return VarBilancioComponentSession l'istanza di
     * <code>VarBilancioComponentSession</code> che serve per gestire
     * una variazione
     */
    private VarBilancioComponentSession createVariazioneBilancioComponentSession()
            throws ComponentException {
        try {
            return (VarBilancioComponentSession) EJBCommonServices
                    .createEJB("CNRPREVENTVAR00_EJB_VarBilancioComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative
     * alle Variazioni di Bilancio
     *
     * @return ProcedureComponentSession l'istanza di
     * <code>ProcedureComponentSession</code> che serve per gestire una
     * variazione con una nuova connessione DB
     */
    private ProcedureComponentSession createProcedureComponentSession()
            throws ComponentException {
        try {
            return (ProcedureComponentSession) EJBCommonServices
                    .createEJB("CNRUTIL00_EJB_ProcedureComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private void generaEMAIL(UserContext userContext, MandatoIBulk mandato,
                             String soggetto, String preText, String postText, String tipo)
            throws ComponentException, IntrospectionException,
            PersistencyException {
        String formDate = "dd/MM/yyyy";
        SimpleDateFormat formatterDate = new SimpleDateFormat(formDate, Config
                .getHandler().getLocale());
        String text = "";
        Utente_indirizzi_mailHome utente_indirizzi_mailHome = (Utente_indirizzi_mailHome) getHome(
                userContext, Utente_indirizzi_mailBulk.class);
        if (preText != null)
            text += preText + "<BR>";
        text = text + "CdS proponente: " + mandato.getCds().getCd_ds_cds()
                + "<BR>";
        text = text + "Tipologia: Mandato di Regolarizzazione Contabile<BR>";
        text = text + "Data di approvazione: "
                + formatterDate.format(mandato.getDt_emissione()) + "<BR>";
        text = text + "<BR>";
        text = text + "CdR abilitati a concorrervi:<BR>";
        String addressTO = null;
        if (tipo.equalsIgnoreCase("ERR")) {
            for (java.util.Iterator i = utente_indirizzi_mailHome
                    .findUtenteMancataApprovazioneVariazioniBilancioEnteComp()
                    .iterator(); i.hasNext(); ) {
                Utente_indirizzi_mailBulk utente_indirizzi = (Utente_indirizzi_mailBulk) i
                        .next();
                if (addressTO == null)
                    addressTO = "";
                else
                    addressTO = addressTO + ",";
                addressTO = addressTO + utente_indirizzi.getIndirizzo_mail();
            }
        }
        if (postText != null)
            text += "<BR>" + postText + "<BR>";
        if (addressTO != null) {
            try {
                SendMail.sendMail(soggetto, text, InternetAddress
                        .parse(addressTO));
            } catch (AddressException e) {
            }
        }
    }

    /**
     * creazione legame Riga Mandato/Codici SIOPE PreCondition: E' stata
     * generata la richiesta di caricare in automatico i codici SIOPE alla riga
     * del mandato per l'importo complessivo PostCondition: Se esiste un unico
     * Codice SIOPE associabile alla riga del mandato viene creata una nuova
     * istanza di Mandato_siopeBulk per l'importo complessivo della riga del
     * mandato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riga        <code>Mandato_rigaBulk</code> la riga mandato da aggiornare
     * @return riga <code>Mandato_rigaBulk</code> la riga mandato aggiornato
     */

    private Mandato_rigaBulk aggiornaLegameSIOPE(UserContext userContext,
                                                 Mandato_rigaBulk riga) throws ComponentException {
        try {
            if (riga.getMandato().isRequiredSiope()) {
                Mandato_rigaHome mandato_rigaHome = (Mandato_rigaHome) getHome(
                        userContext, Mandato_rigaBulk.class);
                if (riga.isToBeCreated()
                        || mandato_rigaHome.findCodiciCollegatiSIOPE(
                        userContext, riga).isEmpty()) {
                    BulkList list = new BulkList(mandato_rigaHome
                            .findCodiciCollegabiliSIOPE(userContext, riga));
                    if (list.size() == 1) {
                        Mandato_siopeBulk mandato_siope = null;
                        if (riga instanceof MandatoAccreditamento_rigaBulk)
                            mandato_siope = new MandatoAccreditamento_siopeBulk();
                        else
                            mandato_siope = new Mandato_siopeIBulk();
                        mandato_siope.setCodice_siope((Codici_siopeBulk) list
                                .get(0));
                        mandato_siope.setImporto(riga.getIm_mandato_riga());
                        mandato_siope.setToBeCreated();
                        riga.addToMandato_siopeColl(mandato_siope);
                    }
                }
            }
            return riga;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * caricamento codici SIOPE collegabili alla riga del mandato PreCondition:
     * E' stata generata la richiesta di caricare in automatico i codici SIOPE
     * da proporre per l'associazione alla riga del mandato PostCondition:
     * Vengono caricati i codici SIOPE disponibili per l'associazione della riga
     * del mandato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riga        <code>Mandato_rigaBulk</code> la riga mandato da aggiornare
     * @return riga <code>Mandato_rigaBulk</code> la riga mandato aggiornato
     */
    public Mandato_rigaBulk setCodiciSIOPECollegabili(UserContext userContext,
                                                      Mandato_rigaBulk riga) throws ComponentException {
        try {

            Mandato_rigaHome mandato_rigaHome = (Mandato_rigaHome) getHome(
                    userContext, Mandato_rigaBulk.class);

            Collection codiciCollegatiSIOPE;
            if (riga.getMandato_siopeColl().isEmpty() && !riga.isToBeCreated())
                codiciCollegatiSIOPE = mandato_rigaHome
                        .findCodiciCollegatiSIOPE(userContext, riga);
            else
                codiciCollegatiSIOPE = riga.getMandato_siopeColl();

            boolean trovato = false;
            riga.setCodici_siopeColl(new BulkList());

            for (java.util.Iterator collegabile = mandato_rigaHome
                    .findCodiciCollegabiliSIOPE(userContext, riga).iterator(); collegabile
                         .hasNext(); ) {
                Codici_siopeBulk codiceCollegabile = (Codici_siopeBulk) collegabile
                        .next();
                trovato = false;

                for (java.util.Iterator collegati = codiciCollegatiSIOPE
                        .iterator(); collegati.hasNext(); ) {
                    Mandato_siopeBulk codiceCollegato = (Mandato_siopeBulk) collegati
                            .next();

                    if (codiceCollegato.getCodice_siope().equalsByPrimaryKey(
                            codiceCollegabile)) {
                        trovato = true;
                        break;
                    }
                }
                if (!trovato)
                    riga.addToCodici_siopeColl(codiceCollegabile);
            }
            return riga;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Ritorna true se il totale SIOPE associato alla riga mandato non
     * corrisponde con l'importo della riga stessa PreCondition: E' stata
     * generata la richiesta di verificare che la riga del mandato sia associata
     * completamente a codici SIOPE PostCondition: Ritorna TRUE se la riga del
     * mandato è associata completamente a codici SIOPE
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riga        <code>Mandato_rigaBulk</code> la riga mandato da controllare
     * @return Boolean
     */
    private java.lang.Boolean isCollegamentoSiopeCompleto(
            UserContext userContext, Mandato_rigaBulk riga)
            throws ComponentException {
        try {
            Mandato_rigaHome mandato_rigaHome = (Mandato_rigaHome) getHome(
                    userContext, riga);
            BigDecimal totaleSiope = Utility.ZERO;

            for (java.util.Iterator collegati = mandato_rigaHome
                    .findCodiciCollegatiSIOPE(userContext, riga).iterator(); collegati
                         .hasNext(); )
                totaleSiope = totaleSiope.add(((Mandato_siopeBulk) collegati
                        .next()).getImporto());

            return new Boolean(
                    totaleSiope.compareTo(riga.getIm_mandato_riga()) == 0);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Ritorna true se il totale SIOPE associato al mandato non corrisponde con
     * l'importo del mandato stesso PreCondition: E' stata generata la richiesta
     * di verificare che il mandato sia associato completamente a codici SIOPE
     * PostCondition: Viene verificato che tutte le righe del mandato siano
     * associate a codici SIOPE. Ritorna TRUE se tutte le righe del mandato sono
     * associate completamente a codici SIOPE
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoBulk</code> il mandato da controllare
     * @return Boolean
     */
    public java.lang.Boolean isCollegamentoSiopeCompleto(
            UserContext userContext, MandatoBulk mandato)
            throws ComponentException {
        try {
            MandatoHome mandatoHome = (MandatoHome) getHome(userContext,
                    mandato.getClass());
            mandato = (MandatoBulk) mandatoHome.findByPrimaryKey(mandato);
            if (mandato.isRequiredSiope()) {
                for (Iterator i = mandatoHome.findMandato_riga(userContext,
                        mandato).iterator(); i.hasNext(); ) {
                    if (!this.isCollegamentoSiopeCompleto(userContext,
                            (Mandato_rigaBulk) i.next()))
                        return Boolean.FALSE;
                }
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * ricerca scadenze accertamento per regolarizzazione PreCondition: E' stata
     * richiesta la creazione di un mandato di regolarizzazione CNR L'utente ha
     * selezionato l'accertamento su cui creare in automatico la reversale di
     * regolarizzazione PostCondition: Vengono ricercate tutte le scadenze
     * dell'accertamento con importo disponibile positivo
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoIBulk</code> il mandato
     * @return mandato il Mandato dopo la ricerca delle scadenze
     */

    public MandatoIBulk listaScadenzeAccertamentoPerRegolarizzazione(
            UserContext userContext, MandatoIBulk mandato)
            throws ComponentException {
        try {
            Collection result = ((MandatoIHome) getHome(userContext, mandato
                    .getClass())).findScadenzeAccertamentoPerRegolarizzazione(
                    (it.cnr.contab.utenze00.bp.CNRUserContext) userContext,
                    mandato);
            mandato.setScadenzeAccertamentoPerRegolarizzazione(result);
            return mandato;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public SQLBuilder selectCupByClause(UserContext userContext, MandatoCupIBulk mandato, CupBulk cup, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, CupBulk.class).createSQLBuilder();
        sql.openParenthesis("AND");
        sql.addClause("AND", "dt_canc", SQLBuilder.ISNULL, null);
        sql.addClause("OR", "dt_canc", SQLBuilder.GREATER, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
        sql.closeParenthesis();
        sql.addClause(clauses);
        return sql;
    }

    public java.lang.Boolean isDipendenteDaConguaglio(
            UserContext userContext, MandatoBulk mandato)
            throws ComponentException {
        try {
            for (Iterator i = mandato.getMandato_rigaColl().iterator(); i.hasNext(); ) {
                Mandato_rigaBulk riga = (Mandato_rigaBulk) i.next();
                if (riga.getCd_tipo_documento_amm().equals(Numerazione_doc_ammBulk.TIPO_COMPENSO)) {
                    CompensoBulk compenso = new CompensoBulk(
                            riga.getCd_cds_doc_amm(),
                            riga.getCd_uo_doc_amm(),
                            riga.getEsercizio_doc_amm(),
                            riga.getPg_doc_amm());

                    ConguaglioHome conguaglioHome = (ConguaglioHome) getHome(userContext, ConguaglioBulk.class);
                    if (conguaglioHome.findConguaglioAssociatoACompenso(compenso) != null)
                        return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }

            return Boolean.FALSE;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public SQLBuilder selectCupByClause(UserContext userContext, MandatoSiopeCupIBulk bulk, CupBulk cup, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, CupBulk.class).createSQLBuilder();
        sql.openParenthesis("AND");
        sql.addClause("AND", "dt_canc", SQLBuilder.ISNULL, null);
        sql.addClause("OR", "dt_canc", SQLBuilder.GREATER, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
        sql.closeParenthesis();
        sql.addClause(clauses);
        return sql;
    }

    private void verificaTracciabilitaPagamenti(UserContext userContext,
                                                MandatoBulk mandato) throws ComponentException {
        try {
            if (mandato.getMandato_rigaColl().size() == 0)
                return;

            for (Iterator i = mandato.getMandato_rigaColl().iterator(); i
                    .hasNext(); ) {
                Mandato_rigaBulk riga = (Mandato_rigaBulk) i.next();


                if (riga.getCd_modalita_pag() == null || riga.getCd_tipo_documento_amm() == null)
                    throw new ApplicationException(
                            "Attenzione! esistono righe del mandato per cui non risulta valorizzata la modalità di pagamento oppure il tipo di documento amministrativo");
                try {
                    LoggableStatement cs = new LoggableStatement(
                            getConnection(userContext), "{  call "
                            + it.cnr.jada.util.ejb.EJBCommonServices
                            .getDefaultSchema()
                            + "CNRCTB037.verificaTracciabilitaPag(?, ?, ?,?,?)}",
                            false, this.getClass());
                    try {
                        cs.setObject(1, mandato.getEsercizio());
                        cs.setObject(2, mandato.getDt_emissione());
                        cs.setString(3, riga.getCd_modalita_pag());
                        cs.setString(4, riga.getCd_tipo_documento_amm());
                        cs.setObject(5, mandato.getIm_netto());
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
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public List<Rif_modalita_pagamentoBulk> findModPagObbligatorieAssociateAlMandato(UserContext userContext, V_mandato_reversaleBulk mandato_reversaleBulk) throws ComponentException {
        SQLBuilder sql = getHome(userContext, Rif_modalita_pagamentoBulk.class).createSQLBuilder();
        sql.addClause(FindClause.AND, "fl_all_obbl_mandato", SQLBuilder.EQUALS, true);
        sql.addTableToHeader("MANDATO_RIGA");
        sql.addSQLJoin("MANDATO_RIGA.CD_MODALITA_PAG", "RIF_MODALITA_PAGAMENTO.CD_MODALITA_PAG");
        sql.addSQLClause(FindClause.AND, "MANDATO_RIGA.CD_CDS", SQLBuilder.EQUALS, mandato_reversaleBulk.getCd_cds());
        sql.addSQLClause(FindClause.AND, "MANDATO_RIGA.ESERCIZIO", SQLBuilder.EQUALS, mandato_reversaleBulk.getEsercizio());
        sql.addSQLClause(FindClause.AND, "MANDATO_RIGA.PG_MANDATO", SQLBuilder.EQUALS, mandato_reversaleBulk.getPg_documento_cont());
        try {
            return getHome(userContext, Rif_modalita_pagamentoBulk.class).fetchAll(sql);
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public java.lang.String isAnnullabile(
            UserContext userContext, MandatoBulk mandato)
            throws ComponentException {
        try {
            Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk) getHome(userContext, Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(mandato.getEsercizio()));
            if (parametriCnr.getFl_tesoreria_unica()) {
                UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));
                if (mandato.getStato_trasmissione().compareTo(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO) == 0)
                    return "S";
                else if (mandato.getStato_trasmissione().compareTo(MandatoBulk.STATO_TRASMISSIONE_TRASMESSO) == 0 &&
                        utente.isSupervisore()) {
                    return "F";
                } else
                    return "N";
            }
            return "S";
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public Boolean isCollegamentoSospesoCompleto(UserContext userContext,
                                                 MandatoBulk mandato) throws ComponentException {
        try {
            MandatoHome mandatoHome = (MandatoHome) getHome(userContext, mandato.getClass());
            mandato = (MandatoBulk) mandatoHome.findByPrimaryKey(mandato);
            if (mandato.isRequiredSospeso()) {
                mandato.setMandato_rigaColl(new BulkList(((MandatoHome) getHome(
                        userContext, mandato.getClass())).findMandato_riga(userContext, mandato)));
                Sospeso_det_uscBulk sdu;
                mandato.setSospeso_det_uscColl(new BulkList(
                        ((MandatoHome) getHome(userContext, mandato.getClass()))
                                .findSospeso_det_usc(userContext, mandato)));
                mandato = inizializzaFlagFaiReversale(userContext,
                        (MandatoIBulk) mandato);
                for (Iterator i = mandato.getSospeso_det_uscColl().iterator(); i.hasNext(); ) {
                    sdu = (Sospeso_det_uscBulk) i.next();
                    sdu.setMandato(mandato);
                    if (sdu.getStato().equals(Sospeso_det_uscBulk.STATO_ANNULLATO))
                        i.remove();
                }
                return mandato.isSospesoTotalmenteAssociato();
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public MandatoBulk annullaMandato(UserContext userContext,
                                      MandatoBulk mandato,
                                      boolean riemissione) throws ComponentException {
        try {
            return annullaMandato(userContext, mandato, null, true, riemissione);
        } catch (Exception e) {
            throw handleException(mandato, e);
        }
    }

    @Override
    public MandatoBulk annullaMandato(UserContext userContext, MandatoBulk mandato,
                                      CompensoOptionRequestParameter param, boolean annullaCollegati)
            throws ComponentException {
        try {
            return annullaMandato(userContext, mandato, param, true, false);
        } catch (Exception e) {
            throw handleException(mandato, e);
        }
    }

    public SQLBuilder selectV_man_revByClause(UserContext userContext, MandatoBulk bulk, V_mandato_reversaleBulk v_man_rev, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, V_mandato_reversaleBulk.class).createSQLBuilder();
        sql.addClause("AND", "cd_tipo_documento_cont", SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
        //sql.addClause( "AND", "cd_cds", sql.EQUALS, bulk.getCd_cds() );
        sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getCd_unita_organizzativa());
        sql.addClause("AND", "ti_documento_cont", SQLBuilder.NOT_EQUALS, MandatoBulk.TIPO_REGOLARIZZAZIONE);
        sql.addClause("AND", "stato", SQLBuilder.EQUALS, MandatoBulk.STATO_MANDATO_EMESSO);
        sql.addSQLClause("AND", "dt_trasmissione", SQLBuilder.ISNULL, null);
        sql.addClause(clauses);
        return sql;
    }

    public Boolean esisteAnnullodaRiemettereNonCollegato(UserContext userContext,
                                                         Integer esercizio, String cds) throws ComponentException {
        try {
            SQLBuilder sql = getHome(userContext, MandatoIBulk.class).createSQLBuilder();
            sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
            sql.addClause("AND", "cd_cds_origine", SQLBuilder.EQUALS, cds);
            sql.addClause("AND", "stato", SQLBuilder.EQUALS, MandatoBulk.STATO_MANDATO_ANNULLATO);
            sql.addSQLClause("AND", "pg_mandato_riemissione", SQLBuilder.ISNULL, null);
            sql.addClause(FindClause.AND, "fl_riemissione", SQLBuilder.EQUALS, true);
            if (sql.executeCountQuery(getConnection(userContext)) > 0)
                return Boolean.TRUE;
            else
                return Boolean.FALSE;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public Boolean isMandatoCollegatoAnnullodaRiemettere(UserContext userContext,
                                                         MandatoBulk mandato) throws ComponentException {
        try {
            SQLBuilder sql = getHome(userContext, MandatoIBulk.class).createSQLBuilder();
            sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, mandato.getEsercizio());
            sql.addClause("AND", "cd_cds_origine", SQLBuilder.EQUALS, mandato.getCd_cds_origine());
            sql.addClause("AND", "stato", SQLBuilder.EQUALS, MandatoBulk.STATO_MANDATO_ANNULLATO);
            sql.addSQLClause("AND", "pg_mandato_riemissione", SQLBuilder.EQUALS, mandato.getPg_mandato());
            sql.addClause(FindClause.AND, "fl_riemissione", SQLBuilder.EQUALS, true);
            sql.addClause("AND", "stato_trasmissione_annullo", SQLBuilder.NOT_EQUALS, MandatoBulk.STATO_TRASMISSIONE_TRASMESSO);
            if (sql.executeCountQuery(getConnection(userContext)) > 0)
                return Boolean.TRUE;
            else
                return Boolean.FALSE;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public Boolean isVerificataModPagMandato(UserContext userContext,
                                             V_mandato_reversaleBulk mandato_reversaleBulk) throws ComponentException {
        try {
            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));
            SQLBuilder sql = getHome(userContext, Rif_modalita_pagamentoBulk.class).createSQLBuilder();
            sql.addClause(FindClause.AND, "fl_conto_bi", SQLBuilder.EQUALS, true);
            sql.addTableToHeader("MANDATO_RIGA");
            sql.addSQLJoin("MANDATO_RIGA.CD_MODALITA_PAG", "RIF_MODALITA_PAGAMENTO.CD_MODALITA_PAG");
            sql.addSQLClause(FindClause.AND, "MANDATO_RIGA.CD_CDS", SQLBuilder.EQUALS, mandato_reversaleBulk.getCd_cds());
            sql.addSQLClause(FindClause.AND, "MANDATO_RIGA.ESERCIZIO", SQLBuilder.EQUALS, mandato_reversaleBulk.getEsercizio());
            sql.addSQLClause(FindClause.AND, "MANDATO_RIGA.PG_MANDATO", SQLBuilder.EQUALS, mandato_reversaleBulk.getPg_documento_cont());
            if (sql.executeCountQuery(getConnection(userContext)) > 0 && utente.isSupervisore())
                return Boolean.TRUE;
            else if (sql.executeCountQuery(getConnection(userContext)) == 0)
                return Boolean.TRUE;
            else
                return Boolean.FALSE;
        } catch (SQLException e) {
            throw handleException(e);
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public IDocumentoAmministrativoSpesaBulk getDocumentoAmministrativoSpesaBulk(UserContext userContext, Mandato_rigaBulk mandatoRiga) throws ComponentException {
        return Optional.ofNullable(getHome(userContext, mandatoRiga.getClass()))
                .filter(Mandato_rigaHome.class::isInstance)
                .map(Mandato_rigaHome.class::cast)
                .map(mandatoRigaHome -> mandatoRigaHome.getDocumentoAmministrativoSpesaBulk(userContext, mandatoRiga))
                .orElse(null);
    }
}