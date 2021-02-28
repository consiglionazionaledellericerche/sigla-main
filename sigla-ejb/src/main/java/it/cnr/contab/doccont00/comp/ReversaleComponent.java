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
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
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
import it.cnr.contab.doccont00.ejb.AccertamentoComponentSession;
import it.cnr.contab.doccont00.ejb.AccertamentoPGiroComponentSession;
import it.cnr.contab.doccont00.ejb.MandatoComponentSession;
import it.cnr.contab.doccont00.ejb.SaldoComponentSession;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.doccont00.tabrif.bulk.CupBulk;
import it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashMap;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReversaleComponent extends it.cnr.jada.comp.CRUDComponent implements IReversaleMgr, ICRUDMgr, IPrintMgr, Cloneable, Serializable {
    public final static String INSERIMENTO_REVERSALE_ACTION = "I";
    public final static String ANNULLAMENTO_REVERSALE_ACTION = "A";
    public final static String MODIFICA_REVERSALE_ACTION = "M";
    private static final DateFormat PDF_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    //@@<< CONSTRUCTORCST
    public ReversaleComponent() {
//>>

//<< CONSTRUCTORCSTL
        /*Default constructor*/
//>>

//<< CONSTRUCTORCSTT

    }

    public static java.sql.Timestamp getLastDayOfYear(int year) {

        java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 31);
        calendar.set(java.util.Calendar.MONTH, 11);
        calendar.set(java.util.Calendar.YEAR, year);
        calendar.set(java.util.Calendar.HOUR, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        calendar.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
        return new java.sql.Timestamp(calendar.getTime().getTime());
    }

    /**
     * creazione riga
     * PreCondition:
     * E' stata creata una nuova riga di reversale
     * PostCondition:
     * Vengono recuperati dai dettagli della scadenza dell'accertamento, associata alla riga della reversale,
     * le voci del piano i cui saldi devono essere incrementati e viene richiesto alla component che gestisce i saldi
     * di effettuare l'aggiornamento delle voci e di verificare la disponibilità di cassa per ogni voce
     * (metodo aggiornaMandatiReversali)
     * modifica riga - annullamento
     * PreCondition:
     * E' stata modificata una riga di reversale e lo stato della reversale e' annullato
     * PostCondition:
     * Vengono recuperati dai dettagli della scadenza dell'accertamento, associata alla riga della reversale,
     * le voci del piano i cui saldi devono essere decrementati e viene richiesto alla component che gestisce i saldi
     * di effettuare l'aggiornamento delle voci (metodo aggiornaMandatiReversali)
     * modifica riga - cancellazione
     * PreCondition:
     * E' stata cancellata una riga di reversale provvisoria
     * PostCondition:
     * Vengono recuperati dai dettagli della scadenza dell'accertamento, associata alla riga della reversale,
     * le voci del piano i cui saldi devono essere decrementati e viene richiesto alla component che gestisce i saldi
     * di effettuare l'aggiornamento delle voci (metodo aggiornaMandatiReversali)
     *
     * @param aUC     lo <code>UserContext</code> che ha generato la richiesta
     * @param riga    <code>Reversale_rigaBulk</code> la riga della reversale da modificare
     * @param session <code>SaldoComponentSession</code>
     */
/* status riga:
- TO_BE_CREATED alla creazione
- TO_BE_UPDATED quando la riga e' stata annullata oppure e' stato modificata la mod. pagamento
- TO_BE_DELETED alla cancellazione di una reversale provvisoria
Attenzione: l'importo della riga non viene mai modificato
  *
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param riga <code>Reversale_rigaBulk</code> la riga della reversale da modificare
  * @param session <code>SaldoComponentSession</code>
  *
*/
    private void aggiornaCapitoloSaldoRiga(UserContext aUC, Reversale_rigaBulk riga, SaldoComponentSession session) throws ComponentException {
        try {
            Reversale_rigaBulk rigaDaDB = null;
            Voce_fBulk voce;
            boolean flConsumo;
            java.math.BigDecimal importo;

            if (!riga.isToBeCreated() && !riga.isToBeUpdated() && !riga.isToBeDeleted())
                return;

            /* ricerco l'accertamento */
            AccertamentoBulk acr = new AccertamentoBulk(riga.getCd_cds(), riga.getEsercizio_accertamento(), riga.getEsercizio_ori_accertamento(), riga.getPg_accertamento());
            acr = (AccertamentoBulk) getHome(aUC, AccertamentoBulk.class).findByPrimaryKey(acr);

            /* ricerco lo sacdenzario */
            Accertamento_scadenzarioBulk scadenzario = (Accertamento_scadenzarioBulk)
                    getHome(aUC, Accertamento_scadenzarioBulk.class).findByPrimaryKey(
                            new Accertamento_scadenzarioBulk(riga.getCd_cds(), riga.getEsercizio_accertamento(), riga.getEsercizio_ori_accertamento(), riga.getPg_accertamento(), riga.getPg_accertamento_scadenzario()));
            /* verifico se sono a consumo o a copertura completa */
            flConsumo = scadenzario.getIm_scadenza().compareTo(riga.getIm_reversale_riga()) != 0;


            voce = new Voce_fBulk(acr.getCd_voce(), acr.getEsercizio(), acr.getTi_appartenenza(), acr.getTi_gestione());

            /* ricerco le scad_voce */
            Accertamento_scad_voceBulk asv = new Accertamento_scad_voceBulk();
            asv.setEsercizio(riga.getEsercizio_accertamento());
            asv.setCd_cds(riga.getCd_cds());
            asv.setEsercizio_originale(riga.getEsercizio_ori_accertamento());
            asv.setPg_accertamento(riga.getPg_accertamento());
            asv.setPg_accertamento_scadenzario(riga.getPg_accertamento_scadenzario());
            List result = getHome(aUC, Accertamento_scad_voceBulk.class).find(asv);

            //per ogni scad_voce recupero il capitolo
            for (Iterator i = result.iterator(); i.hasNext(); ) {
                asv = (Accertamento_scad_voceBulk) i.next();
                if (flConsumo)
                    importo = riga.getIm_reversale_riga();
                else
                    importo = asv.getIm_voce();

                /*
                 * Aggiorno i Saldi per CDR/Linea
                 */
                if (riga.isToBeCreated()) {
                    session.aggiornaMandatiReversali(aUC, asv.getCd_centro_responsabilita(), asv.getCd_linea_attivita(), voce, acr.getEsercizio_originale(), importo, acr.isAccertamentoResiduo() ? Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO : Voce_f_saldi_cdr_lineaBulk.TIPO_COMPETENZA);
                } else if (riga.isToBeUpdated() && riga.isFl_aggiorna_saldi_per_annullamento())   //e' sempre un annullamento
                {
                    session.aggiornaMandatiReversali(aUC, asv.getCd_centro_responsabilita(), asv.getCd_linea_attivita(), voce, acr.getEsercizio_originale(), importo.negate(), acr.isAccertamentoResiduo() ? Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO : Voce_f_saldi_cdr_lineaBulk.TIPO_COMPETENZA);
                } else if (riga.isToBeDeleted()) { // cancellazione di una reversale provvisoria
                    session.aggiornaMandatiReversali(aUC, asv.getCd_centro_responsabilita(), asv.getCd_linea_attivita(), voce, acr.getEsercizio_originale(), importo.negate(), acr.isAccertamentoResiduo() ? Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO : Voce_f_saldi_cdr_lineaBulk.TIPO_COMPETENZA);
                }
            }

            if (riga.isToBeCreated()) {
                session.aggiornaMandatiReversali(aUC, voce, acr.getCd_cds(), riga.getIm_reversale_riga(), riga.getReversale().getTi_competenza_residuo());
            } else if (riga.isToBeUpdated() && riga.isFl_aggiorna_saldi_per_annullamento())   //e' sempre un annullamento
            {
                session.aggiornaMandatiReversali(aUC, voce, acr.getCd_cds(), riga.getIm_reversale_riga().negate(), riga.getReversale().getTi_competenza_residuo());
                riga.setFl_aggiorna_saldi_per_annullamento(false);
            } else if (riga.isToBeDeleted()) { // cancellazione di una reversale provvisoria
                session.aggiornaMandatiReversali(aUC, voce, acr.getCd_cds(), riga.getIm_reversale_riga().negate(), riga.getReversale().getTi_competenza_residuo());
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * creazione riga
     * PreCondition:
     * E' stata creata una nuova riga di reversale
     * PostCondition:
     * Viene incrementato l'importo associato ai documenti contabili della scadenza di accertamento
     * pagata con la riga della reversale dell'importo della riga della reversale
     * (scadenza.im_associato_doc_contabile = scadenza.im_associato_doc_contabile + reversale_riga.im_reversale_riga)
     * (metodo aggiornaImportoAccertamentoPerRiga)
     * modifica riga - annullamento
     * PreCondition:
     * E' stata modificata una riga di reversale e lo stato della reversale e' annullato
     * PostCondition:
     * Viene decrementato l'importo associato ai documenti contabili della scadenza di accertamento
     * pagata con la riga della reversale dell'importo della riga della reversale
     * (scadenza.im_associato_doc_contabile = scadenza.im_associato_doc_contabile - reversale_riga.im_reversale_riga)
     * (metodo aggiornaImportoAccertamentoPerRiga)
     * modifica riga - modifica modalità pagamento
     * PreCondition:
     * E' stata modificata una riga di reversale, ma il suo importo non e' stato modificato
     * PostCondition:
     * Nessun aggiornamento all'importo della scadenza viene effettuato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale   <code>ReversaleBulk</code> la reversale da modificare
     */
    private void aggiornaImportoAccertamenti(UserContext userContext, ReversaleBulk reversale) throws ComponentException {

        try {
            PrimaryKeyHashMap accertamentiTable = new PrimaryKeyHashMap();
            PrimaryKeyHashMap accertScadTable = new PrimaryKeyHashMap();
            Reversale_rigaBulk riga;
            Accertamento_scadenzarioBulk scadenza;
            AccertamentoBulk accertamento;

            for (Iterator i = reversale.getReversale_rigaColl().iterator(); i.hasNext(); ) {
                riga = (Reversale_rigaBulk) i.next();
                if (!riga.isToBeCreated() && !riga.isToBeUpdated() && !riga.isToBeDeleted())
                    continue;
                //scadenza
                scadenza = new Accertamento_scadenzarioBulk(riga.getCd_cds(), riga.getEsercizio_accertamento(), riga.getEsercizio_ori_accertamento(), riga.getPg_accertamento(), riga.getPg_accertamento_scadenzario());
                if (accertScadTable.get(scadenza) == null) {
                    //leggo la scadenza da db
                    scadenza = (Accertamento_scadenzarioBulk) getHome(userContext, Accertamento_scadenzarioBulk.class).findAndLock(scadenza);
                    accertScadTable.put(scadenza, scadenza);
                } else
                    // scadenza già letto da db
                    scadenza = (Accertamento_scadenzarioBulk) accertScadTable.get(scadenza);
                if (scadenza == null)
                    throw new ApplicationException("Non esiste la scadenza");
                //accertamento
                accertamento = scadenza.getAccertamento();
                if (accertamentiTable.get(accertamento) == null) {
                    //leggo l'accertamento da db
                    accertamento = (AccertamentoBulk) getHome(userContext, AccertamentoBulk.class).findAndLock(accertamento);
                    accertamentiTable.put(accertamento, accertamento);
                } else
                    // scadenza già letta da db
                    accertamento = (AccertamentoBulk) accertamentiTable.get(accertamento);

                aggiornaImportoAccertamentoPerRiga(userContext, riga, scadenza);
            }
            for (Iterator i = accertamentiTable.values().iterator(); i.hasNext(); ) {
                accertamento = (AccertamentoBulk) i.next();
                //accertamento.setUser( userContext.getUser());
                //updateBulk(( userContext, accertamento );
                lockBulk(userContext, accertamento);
            }
            for (Iterator i = accertScadTable.values().iterator(); i.hasNext(); ) {
                scadenza = (Accertamento_scadenzarioBulk) i.next();
                scadenza.setUser(userContext.getUser());
                if (scadenza.getIm_associato_doc_contabile().compareTo(scadenza.getIm_associato_doc_amm()) > 0 ||
                        scadenza.getIm_associato_doc_contabile().compareTo(scadenza.getIm_scadenza()) > 0)
                    throw new ApplicationException("La scadenza " + " con esercizio: " + scadenza.getEsercizio() +
                            " Cds: " + scadenza.getCd_cds() +
                            " Esercizio accertamento: " + scadenza.getEsercizio_originale() +
                            " Pg accertamento: " + scadenza.getPg_accertamento() +
                            " Pg scadenza: " + scadenza.getPg_accertamento_scadenzario() +
                            " ha importo associato ai doc. contabili maggiore dell'importo associato a doc.amm o dell'importo della scadenza.");

                updateBulk(userContext, scadenza);
            }
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * creazione riga
     * PreCondition:
     * E' stata creata una nuova riga di reversale
     * PostCondition:
     * Viene incrementato l'importo associato ai documenti contabili della scadenza di accertamento
     * pagata con la riga della reversale dell'importo della riga della reversale
     * (scadenza.im_associato_doc_contabile = scadenza.im_associato_doc_contabile + reversale_riga.im_reversale_riga)
     * modifica riga - annullamento
     * PreCondition:
     * E' stata modificata una riga di reversale e lo stato della reversale e' annullato
     * PostCondition:
     * Viene decrementato l'importo associato ai documenti contabili della scadenza di accertamento
     * pagata con la riga della reversale dell'importo della riga della reversale
     * (scadenza.im_associato_doc_contabile = scadenza.im_associato_doc_contabile - reversale_riga.im_reversale_riga)
     * modifica riga - modifica modalità pagamento
     * PreCondition:
     * E' stata modificata una riga di reversale, ma il suo importo non e' stato modificato
     * PostCondition:
     * Nessun aggiornamento all'importo della scadenza viene effettuato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riga        <code>Reversale_rigaBulk</code> la riga della reversale da modificare
     * @param scadenza    <code>Accertamento_scadenzarioBulk</code> la scadenza dell'accertamento associato alla reversale
     *                    da modificare
     */
    private void aggiornaImportoAccertamentoPerRiga(UserContext userContext, Reversale_rigaBulk riga, Accertamento_scadenzarioBulk scadenza) throws it.cnr.jada.persistency.PersistencyException, ComponentException {

        if (riga.isToBeCreated())
            scadenza.setIm_associato_doc_contabile(scadenza.getIm_associato_doc_contabile().add(riga.getIm_reversale_riga()));
        else if (riga.isToBeDeleted()) {
            Reversale_rigaBulk rigaDaDB = (Reversale_rigaBulk) getHome(userContext, riga.getClass()).findByPrimaryKey(riga);
            scadenza.setIm_associato_doc_contabile(scadenza.getIm_associato_doc_contabile().subtract(rigaDaDB.getIm_reversale_riga()));
        } else if (riga.isToBeUpdated()) {
            java.math.BigDecimal importo = null;
            // (29/10/2003 12.40.38) Giorgio Massussi
            // Sostituito getHome() con getTempHome() perchè se arrivo da annullaMandato può essere che la
            // riga del mandato sia stata appena caricata da db e successivamente modificata; se uso
            // la stessa HomeCache la rilettura mi seppellisce le modifiche!
            Reversale_rigaBulk rigaDaDB = (Reversale_rigaBulk) getTempHome(userContext, riga.getClass()).findByPrimaryKey(riga);
            if (riga.getReversale().getStato().equals(ReversaleBulk.STATO_REVERSALE_ANNULLATO)) //caso 2 - annullamento
                importo = rigaDaDB.getIm_reversale_riga().negate();
            else if (riga.getIm_reversale_riga().compareTo(rigaDaDB.getIm_reversale_riga()) == 0) //caso 3 - no modifica importo
                return;
	/*	else //caso 1 - modifica importo
		importo = riga.getIm_reversale_riga().subtract(rigaDaDB.getIm_reversale_riga());
	*/
            scadenza.setIm_associato_doc_contabile(scadenza.getIm_associato_doc_contabile().add(importo));
        }
    }

    /**
     * creazione sospeso
     * PreCondition:
     * E' stata generata la richiesta di creazione di una nuova associazione Reversale-Sospeso (Sospeso_det_etrBulk) e
     * l'importo specificato dall'utente e' inferiore o uguale all'importo disponibile del sospeso (importo disponibile =
     * sospeso.im_sospeso - sospeso.im_associati)
     * PostCondition:
     * Viene creata una nuova istanza di Sospeso_det_etrBulk e viene incrementato l'importo associato del sospeso
     * (sospeso.im_associato) con l'importo che e' stato associato alla reversale (Sospeso_det_etrBulk.im_associato)
     * creazione sospeso - errore
     * PreCondition:
     * E' stata generata la richiesta di creazione di una nuova associazione Reversale-Sospeso (Sospeso_det_etrBulk) e
     * l'importo specificato dall'utente e' superiore all'importo disponibile del sospeso (importo disponibile =
     * sospeso.im_sospeso - sospeso.im_associati)
     * PostCondition:
     * Viene segnalato all'utente l'impossibilità di creare l'associazione Reversale-Sospeso
     * modifica sospeso
     * PreCondition:
     * E' stata generata la richiesta di modifica dell'importo di una associazione Reversale-Sospeso (Sospeso_det_etrBulk) e
     * la differenza fra l'importo specificato ora dall'utente per il Sospeso_det_etrBulk e l'importo che aveva in precedenza e'
     * inferiore o uguale all'importo disponibile del sospeso (importo disponibile =
     * sospeso.im_sospeso - sospeso.im_associati)
     * PostCondition:
     * Viene aggiornato l'importo di Sospeso_det_etrBulk con il nuovo importo specificato dall'utente e viene
     * aggiornato l'importo associato del SospesoBulk
     * con la differenza fra l'importo specificato ora dall'utente e l'importo che
     * aveva in precedenza (sospeso.im_associato = sospeso_det_etr.im_associato(ora) - sospeso_det_etr.im_associato(in precedenza)
     * modifica sospeso - errore
     * PreCondition:
     * E' stata generata la richiesta di modifica dell'importo di una associazione Reversale-Sospeso (Sospeso_det_etrBulk) e
     * la differenza fra l'importo specificato ora dall'utente per il Sospeso_det_etrBulk e l'importo che aveva in precedenza e'
     * superiore all'importo disponibile del sospeso (importo disponibile =
     * sospeso.im_sospeso - sospeso.im_associati)
     * PostCondition:
     * Viene segnalato all'utente l'impossibilità di aggiornare l'associazione Reversale-Sospeso
     * cancellazione sospeso
     * PreCondition:
     * E' stata generata la richiesta di cancellazione di una associazione Reversale-Sospeso (Sospeso_det_etrBulk)
     * PostCondition:
     * L'istanza di Sospeso_det_etrBulk viene cancellata e viene decrementato l'importo associato del sospeso
     * (sospeso.im_associato) con l'importo che era stato associato alla reversale (Sospeso_det_etrBulk.im_associato)
     *
     * @param aUC       lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale <code>ReversaleBulk</code> la reversale da aggiornare
     * @return reversale <code>ReversaleBulk</code> la Reversale aggiornata
     */
    private ReversaleBulk aggiornaImportoSospesi(UserContext aUC, ReversaleBulk reversale) throws ComponentException {
        try {
            Sospeso_det_etrBulk sde, sdeFromDb;
            SospesoBulk sospeso;
            BigDecimal totSospesi = new BigDecimal(0);
            for (Iterator i = reversale.getSospeso_det_etrColl().iterator(); i.hasNext(); ) {
                sde = (Sospeso_det_etrBulk) i.next();
                sospeso = sde.getSospeso();
                lockBulk(aUC, sospeso);
                totSospesi = totSospesi.add(sde.getIm_associato());
                if (sde.isToBeCreated()) {
                    if (sde.getIm_associato().compareTo(sospeso.getIm_disponibile()) > 0)
                        throw new ApplicationException("L'Importo specificato per il sospeso deve essere inferiore all'Ulteriore disponibilità su sospeso");
                    sospeso.setIm_associato(sospeso.getIm_associato().add(sde.getIm_associato()));
                } else if (sde.isToBeDeleted()) {
                    sdeFromDb = (Sospeso_det_etrBulk) getHome(aUC, Sospeso_det_etrBulk.class).findByPrimaryKey(sde);
                    sospeso.setIm_associato(sospeso.getIm_associato().subtract(sdeFromDb.getIm_associato()));
                } else if (sde.isToBeUpdated()) {
                    sdeFromDb = (Sospeso_det_etrBulk) getHome(aUC, Sospeso_det_etrBulk.class).findByPrimaryKey(sde);
                    if (sde.getIm_associato().subtract(sdeFromDb.getIm_associato()).compareTo(sospeso.getIm_disponibile()) > 0)
                        throw new ApplicationException("L'importo disponibile del sospeso e' stato esaurito");
                    sospeso.setIm_associato(sospeso.getIm_associato().add(sde.getIm_associato().subtract(sdeFromDb.getIm_associato())));
                } else
                    continue;

                if (SospesoBulk.STATO_SOSP_IN_SOSPESO.equals(sospeso.getStato_sospeso()) && sospeso.getCd_cds_origine() == null)
                    sospeso.setCds_origine(new CdsBulk(reversale.getCd_cds_origine()));

                sospeso.setToBeUpdated();
                sospeso.setUser(aUC.getUser());
            }
/*  24/09/2002
	Commentata la chiamata al metodo per l'impostazione dell'importo incassato della
	Reversale, in quanto adesso non si imposta più a INCASSATO lo stato di una Reversale,
	quando viene associata ad un sospeso */
//		reversale.setIm_incassato( totSospesi );
//		reversale.setToBeUpdated();

            //itero anche fra i sospesi che sono stati cancellati
            for (Iterator i = reversale.getSospeso_det_etrColl().deleteIterator(); i.hasNext(); ) {
                sde = (Sospeso_det_etrBulk) i.next();
                sospeso = sde.getSospeso();
                lockBulk(aUC, sospeso);
			/*
			if ( sde.isToBeCreated() )
			{
				if ( sde.getIm_associato().compareTo( sospeso.getIm_disponibile() ) > 0 )
					throw new ApplicationException( "L'importo specificato per il sospeso deve essere inferiore all'importo disponibile del sospeso");
				sospeso.setIm_associato( sospeso.getIm_associato().add( sde.getIm_associato()));
			}
			else */
                if (sde.isToBeDeleted()) {
                    sdeFromDb = (Sospeso_det_etrBulk) getHome(aUC, Sospeso_det_etrBulk.class).findByPrimaryKey(sde);
                    sospeso.setIm_associato(sospeso.getIm_associato().subtract(sdeFromDb.getIm_associato()));
                } else if (sde.isToBeUpdated()) {
                    sdeFromDb = (Sospeso_det_etrBulk) getHome(aUC, Sospeso_det_etrBulk.class).findByPrimaryKey(sde);
                    // if ( sde.getIm_associato().subtract( sdeFromDb.getIm_associato()).compareTo( sospeso.getIm_disponibile() ) > 0 )
                    //	throw new ApplicationException( "L'importo disponibile del sospeso e' stato esaurito");
                    sde.setIm_associato(sdeFromDb.getIm_associato());
                    sospeso.setIm_associato(sospeso.getIm_associato().subtract(sdeFromDb.getIm_associato()));
                } else
                    continue;

                if (SospesoBulk.STATO_SOSP_IN_SOSPESO.equals(sospeso.getStato_sospeso()) && sospeso.getCd_cds_origine() == null)
                    sospeso.setCd_cds_origine(reversale.getCd_cds_origine());

                sospeso.setToBeUpdated();
                sospeso.setUser(aUC.getUser());
            }
            return reversale;
        } catch (Exception e) {
            throw handleException(e);

        }
    }

    /**
     * creazione reversale
     * PreCondition:
     * E' stata creata una reversale e sono stati associati dei sospesi per un importo pari all'importo della reversale
     * PostCondition:
     * Viene richiamata una stored procedure (aggDaSospesoRiscontro) che aggiorna lo stato della reversale a INCASSATO,
     * la data di incasso alla data odierna e aggiorna i saldi relativi all'incassato delle voci del piano presenti nella
     * reversale
     * annullamento
     * PreCondition:
     * E' stata annullata una reversale con stato INCASSATO
     * PostCondition:
     * Viene richiamata una stored procedure (aggDaSospesoRiscontro) che aggiorna lo stato della reversale a EMESSO,
     * la data di incasso viene resettata e vengono aggiornati i saldi relativi all'incassato delle voci del piano presenti
     * nella reversale
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale   <code>ReversaleBulk</code> reversale per cui aggiornare lo stato e i saldi incassati
     * @param action      <code>String</code> azione effettuata sulla reversale. ( I - inserimento, M - modifica, A annullamento)
     */
    private void aggiornaSaldoIncassato(UserContext userContext, ReversaleBulk reversale, String action) throws ComponentException {
        try {
            LoggableStatement cs = new LoggableStatement(getConnection(userContext),
                    "{  call " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CNRCTB037.riscontroReversale(?, ?, ?, ?, ?)}", false, this.getClass());
            try {
                cs.setObject(1, reversale.getEsercizio());
                cs.setString(2, reversale.getCd_cds());
                cs.setObject(3, reversale.getPg_reversale());
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
    }

    /**
     * creazione reversale
     * PreCondition:
     * E' stata creata una nuova reversale
     * PostCondition:
     * Viene richiesta alla component che gestisce i documenti amministrativi l'aggiornamento dello stato COFI dei
     * documenti pagati con la reversale
     * annullamento reversale
     * PreCondition:
     * E' stata annullata una nuova reversale
     * PostCondition:
     * Viene richiesta alla component che gestisce i documenti amministrativi l'aggiornamento dello stato COFI dei
     * documenti pagati con la reversale
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale   <code>ReversaleBulk</code> la reversale
     * @param action      <code>String</code> azione che può assumere valori inserimento/annullamento
     */
    private void aggiornaStatoFattura(UserContext userContext, ReversaleBulk reversale, String action) throws ComponentException {
        try {
            createFatturaPassivaComponentSession().aggiornaStatoDocumentiAmministrativi(
                    userContext,
                    reversale.getCd_cds(),
                    reversale.getCd_unita_organizzativa(),
                    reversale.getCd_tipo_documento_cont(),
                    reversale.getEsercizio(),
                    reversale.getPg_reversale(),
                    action);
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * aggiungiDocAttivi
     * PreCondition:
     * E' stata generata la richiesta di aggiungere ad una reversale nuovi documenti amministrativi attivi ( fatture
     * attive o domenti generici attivi). Tali documenti hanno lo stesso terzo e la stessa classe di pagamento.
     * PostCondition:
     * Per ogni documento attivo viene creata una o piu' righe di reversale (metodo creaReversaleRiga)
     * secondo la seguente regola:
     * - per ogni documento generico viene creata una sola riga di reversale
     * - per ogni fattura attiva viene creata una riga di reversale ed eventualmente righe aggiuntive se
     * tale fattura e' associata a note di debito e/o note di credito
     * Viene creata una istanza di ReversaleTerzoBulk (metodo creaReversaleTerzo) coi dati del terzo presente
     * nei documenti amministrativi
     * errore - debitori diversi
     * PreCondition:
     * Il codice terzo dei documenti amministrativi attivi da aggiungere alla reversale non e'
     * lo stesso per tutti i documenti
     * PostCondition:
     * Un messaggio di errore segnala all'utente l'impossibilità di aggiungere i documenti alla reversale
     * errore - classe di pagamento
     * PreCondition:
     * La classe di pagamento (Bancario,Postale,etc.) dei documenti amministrativi attivi da aggiungere alla reversale
     * non e' lo stesso per tutti i documenti.
     * PostCondition:
     * Un messaggio di errore segnala all'utente l'impossibilità di aggiungere i documenti alla reversale
     * errore - reversale di regolarizzazione
     * PreCondition:
     * I documenti amministrativi attivi selezionati per essere aggiunti ad una reversale di regolarizzazione sono stati
     * contabilizzati in parte su accertamenti relativi a capitoli di bilancio e in parte su accertamenti
     * relativi a partite di giro.
     * PostCondition:
     * Un messaggio di errore segnala all'utente l'impossibilità di aggiungere i documenti alla reversale
     * errore - Tipo competenza/residuo
     * PreCondition:
     * Il tipo (Competenza,Residuo) dei documenti amministrativi attivi da aggiungere alla reversale
     * non e' lo stesso per tutti i documenti.
     * PostCondition:
     * Un messaggio di errore segnala all'utente l'impossibilità di aggiungere i documenti alla reversale
     *
     * @param aUC       lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale <code>ReversaleBulk</code> la reversale da aggiornare
     * @param docAttivi <code>List</code> la lista dei documenti attivi
     * @return reversale <code>ReversaleBulk</code> la Reversale aggiornata
     */
    public ReversaleBulk aggiungiDocAttivi(UserContext aUC, ReversaleBulk reversale, List docAttivi) throws ComponentException {

        Integer cd_terzo;
        String ti_pagamento;
        Boolean pGiro;
        V_doc_attivo_accertamentoBulk docAttivo;
        Reversale_rigaBulk riga;
        Collection docAttiviCollegati;
        String ti_competenza_residuo;

        try {
            if (reversale.getReversale_rigaColl().size() == 0)
                reversale.setReversale_terzo(null);

            if (reversale.getReversale_terzo() == null) {
                cd_terzo = ((V_doc_attivo_accertamentoBulk) docAttivi.get(0)).getCd_terzo();
                ti_pagamento = ((V_doc_attivo_accertamentoBulk) docAttivi.get(0)).getTi_pagamento();
                pGiro = ((V_doc_attivo_accertamentoBulk) docAttivi.get(0)).getFl_pgiro();
                ti_competenza_residuo = ((V_doc_attivo_accertamentoBulk) docAttivi.get(0)).getTi_competenza_residuo();
            } else {
                cd_terzo = reversale.getReversale_terzo().getCd_terzo();
                ti_pagamento = ((Reversale_rigaIBulk) reversale.getReversale_rigaColl().get(0)).getBanca().getTi_pagamento();
                pGiro = ((Reversale_rigaIBulk) reversale.getReversale_rigaColl().get(0)).getFl_pgiro();
                ti_competenza_residuo = reversale.getTi_competenza_residuo();
            }

            for (Iterator i = docAttivi.iterator(); i.hasNext(); ) {
                docAttivo = (V_doc_attivo_accertamentoBulk) i.next();
                if (!cd_terzo.equals(docAttivo.getCd_terzo()))
                    throw new ApplicationException("E' possibile selezionare solo doc attivi relativi ad un unico beneficiario");
                if (!ti_pagamento.equals(docAttivo.getTi_pagamento()))
                    throw new ApplicationException("E' possibile selezionare solo doc attivi relativi ad una stessa classe di pagamento");
                if (reversale.getTi_reversale().equals(ReversaleBulk.TIPO_REGOLARIZZAZIONE) && !docAttivo.getFl_pgiro().equals(pGiro))
                    throw new ApplicationException("Per la reversale di regolarizzazione non e' possibile selezionare doc attivi su partite di giro e doc attivi su capitoli di bilancio");
                if (!ti_competenza_residuo.equals(docAttivo.getTi_competenza_residuo()))
                    throw new ApplicationException("E' possibile selezionare solo doc attivi dello stesso tipo COMPETENZA/RESIDUO.");


                //creo reversale_riga
                riga = creaReversaleRigaConModalitaPag(aUC, reversale, docAttivo);

                //estrae le eventuali note di credito/debito
                docAttiviCollegati = ((ReversaleIHome) getHome(aUC, reversale.getClass())).findDocAttiviCollegati(docAttivo);
                for (Iterator j = docAttiviCollegati.iterator(); j.hasNext(); )
                    riga = creaReversaleRigaConModalitaPag(aUC, reversale, (V_doc_attivo_accertamentoBulk) j.next());
            }
            //creo reversale terzo
            if (reversale.getReversale_terzo() == null) {
                Reversale_terzoBulk rTerzo = creaReversaleTerzo(aUC, reversale, cd_terzo);
                reversale.setReversale_terzo(rTerzo);
            }

            reversale.setTi_competenza_residuo(ti_competenza_residuo);
            reversale.refreshImporto();


            return reversale;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public boolean isChiudibileReversaleProvvisoria(UserContext aUC, ReversaleBulk reversale) throws PersistencyException, IntrospectionException, ComponentException {
        Collection reversaleRighe;
        boolean pgiroLiqIvaCentroAperte = false;
        reversaleRighe = ((ReversaleIHome) getHome(aUC, reversale.getClass())).findReversale_riga(aUC, reversale);
        for (Iterator j = reversaleRighe.iterator(); j.hasNext(); ) {
            try {
                Reversale_rigaBulk reversaleRigaCorrente = (Reversale_rigaBulk) j.next();
                pgiroLiqIvaCentroAperte = ((Ass_obb_acr_pgiroHome) getHome(aUC, Ass_obb_acr_pgiroBulk.class)).findPgiroLiqIvaCentroAperte(aUC, reversaleRigaCorrente);
            } catch (SQLException e) {
                throw new ComponentException(e);
            }
            if (pgiroLiqIvaCentroAperte) {
                return false;
            }
        }
        return true;
    }

    public boolean isRevProvvLiquidCoriCentroAperta(UserContext aUC, ReversaleBulk reversale) throws PersistencyException, IntrospectionException, ComponentException {
        Collection reversaleRighe;
        boolean pgiroLiqIvaCentroAperte = false;
        reversaleRighe = ((ReversaleIHome) getHome(aUC, reversale.getClass())).findReversale_riga(aUC, reversale);
        for (Iterator j = reversaleRighe.iterator(); j.hasNext(); ) {
            try {
                Reversale_rigaBulk reversaleRigaCorrente = (Reversale_rigaBulk) j.next();
                pgiroLiqIvaCentroAperte = ((Ass_obb_acr_pgiroHome) getHome(aUC, Ass_obb_acr_pgiroBulk.class)).findPgiroLiqIvaCentroAperte(aUC, reversaleRigaCorrente);
            } catch (SQLException e) {
                throw new ComponentException(e);
            }
            if (pgiroLiqIvaCentroAperte) {
                return true;
            }
        }
        return false;
    }

    /**
     * annullamento
     * PreCondition:
     * E' stata generata la richiesta di annullare una Reversale
     * La Reversale ha tipo diverso da regolarizzazione e accreditamento
     * La reversale ha originato delle reversali o dei mandati
     * PostCondition:
     * Tutte le reversali originate dalla reversale da annnullare sono state annullate
     * E' stata richiesta alla Component che gestisce il Mandato l'annullamento di tutti i mandati originati dalla reversale da annullare
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale   <code>ReversaleBulk</code> la reversale  da annullare
     */
    private void annullaDocContabiliCollegati(UserContext userContext, ReversaleBulk reversale) throws ComponentException {
        try {
            V_ass_doc_contabiliBulk ass;
            MandatoComponentSession manSession = null;
            MandatoBulk mandato;
            ReversaleBulk revColl;

            if (reversale.getDoc_contabili_collColl().size() > 0)
                manSession = createMandatoComponentSession();
            for (Iterator i = reversale.getDoc_contabili_collColl().iterator(); i.hasNext(); ) {
                ass = (V_ass_doc_contabiliBulk) i.next();
                if (ass.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_REV) &&
                        ass.getCd_cds().equals(reversale.getCd_cds()) &&
                        ass.getEsercizio().equals(reversale.getEsercizio()) &&
                        ass.getPg_documento_cont().equals(reversale.getPg_reversale()) &&
                        ass.getCd_tipo_documento_cont_coll().equals(Numerazione_doc_contBulk.TIPO_MAN)) {   //la reversale ha un mandato associato
                    mandato = (MandatoBulk) manSession.inizializzaBulkPerModifica(userContext, new MandatoIBulk(ass.getCd_cds_coll(), ass.getEsercizio_coll(), ass.getPg_documento_cont_coll()));
                    manSession.annullaMandato(userContext, mandato, null, false);
                } else if (ass.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_REV) &&
                        ass.getCd_cds().equals(reversale.getCd_cds()) &&
                        ass.getEsercizio().equals(reversale.getEsercizio()) &&
                        ass.getPg_documento_cont().equals(reversale.getPg_reversale()) &&
                        ass.getCd_tipo_documento_cont_coll().equals(Numerazione_doc_contBulk.TIPO_REV)) {   //la reversale ha una reversale associata --- non si dovrebbe mai verificare
                    revColl = (ReversaleBulk) inizializzaBulkPerModifica(userContext, new ReversaleIBulk(ass.getCd_cds_coll(), ass.getEsercizio_coll(), ass.getPg_documento_cont_coll()));
                    annullaReversale(userContext, revColl, false);
                }
            }
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * annulla importo sospesi
     * PreCondition:
     * E' stata generata la richiesta di annullare una reversale ed e' pertanto necessario aggiornare l'importo
     * di tutti i sospesi che erano sono stati associati a questa reversale
     * PostCondition:
     * Per ogni istanza di Sospeso_det_etrBulk presente nel db, viene aggiornato l'importo del sospeso associato
     * nel modo seguente:
     * sospeso.im_associato = sospeso.im_associato - sospeso_det_etr.im_associato
     *
     * @param aUC       lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale <code>ReversaleBulk</code> la reversale da annullare
     * @return reversale <code>ReversaleBulk</code> la Reversale da annullare
     */

    private ReversaleBulk annullaImportoSospesi(UserContext aUC, ReversaleBulk reversale) throws ComponentException {
        try {
            Sospeso_det_etrBulk sde, sdeFromDb;
            SospesoBulk sospeso;
            for (Iterator i = reversale.getSospeso_det_etrColl().iterator(); i.hasNext(); ) {
                sde = (Sospeso_det_etrBulk) i.next();
                sospeso = sde.getSospeso();
                lockBulk(aUC, sospeso);
                sdeFromDb = (Sospeso_det_etrBulk) getHome(aUC, Sospeso_det_etrBulk.class).findByPrimaryKey(sde);
                if (sdeFromDb != null) {
                    sospeso.setIm_associato(sospeso.getIm_associato().subtract(sdeFromDb.getIm_associato()));
                    sospeso.setToBeUpdated();
                    sospeso.setUser(aUC.getUser());

                }
            }
            //itero anche fra i sospesi che sono stati cancellati
		/*
		for ( Iterator i = reversale.getSospeso_det_etrColl().deleteIterator(); i.hasNext(); )
		{
			sde = (Sospeso_det_etrBulk) i.next();
			sospeso = sde.getSospeso();
			lockBulk( aUC, sospeso );
			sdeFromDb = (Sospeso_det_etrBulk) getHome( aUC, Sospeso_det_etrBulk.class ).findByPrimaryKey( sde );
			if ( sdeFromDb != null )
			{
				sospeso.setIm_associato( sospeso.getIm_associato().subtract( sdeFromDb.getIm_associato()));
				sospeso.setToBeUpdated();
				sospeso.setUser( ((it.cnr.contab.utenze00.bp.CNRUserContext) aUC).getUser());

			}
		}
		*/
            return reversale;
        } catch (Exception e) {
            throw handleException(e);

        }
    }

    /**
     * annullamento
     * PreCondition:
     * E' stata generata la richiesta di annullare una Reversale
     * PostCondition:
     * Viene annullata la Reversale (metodo annullaReversale) specificando che e' necessario procedere
     * anche all'annullamento dei mandati/reversali collegate
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale   <code>ReversaleBulk</code> la reversale da annullare
     * @return reversale <code>ReversaleBulk</code> la Reversale annullata
     */

    public ReversaleBulk annullaReversale(UserContext userContext, ReversaleBulk reversale) throws ComponentException {
        try {
            return annullaReversale(userContext, reversale, true);
        } catch (Exception e) {
            throw handleException(reversale, e);
        }

    }

    /**
     * annullamento reversale e doc. contabili collegati
     * PreCondition:
     * E' stata generata la richiesta di annullare una Reversale
     * La reversale non ha riscontri associati
     * E' stato richiesta di annullare anche i doc. contabili collegati alla reversale
     * PostCondition:
     * Viene impostata la data di annullamento della reversale con la data odierna e lo stato della reversale
     * diventa ANNULLATO. Viene impostato lo stato ANNULLATO su tutte le righe della reversale.
     * Per ogni riga inoltre viene aggiornato l'importo associato a doc.contabili della scadenza di
     * accertamento legata alla riga (metodo aggiornaImportoAccertamenti), viene aggiornato lo stato
     * del documento amministrativo legato alla riga (metodo aggiornaStatoFattura). Vengono aggiornati i
     * saldi dei capitoli (metodo aggiornaCapitoloSaldoRiga). Per ogni associazione sospeso-reversale, viene
     * aggiornato l'importo associato del sospeso (metodo annullaImportoSospesi). Se la reversale ha associati mandati
     * o altre reversali viene eseguito il loro annullamento (metodo 'annullaDocContabiliCollegati').
     * <p>
     * annullamento reversale
     * PreCondition:
     * E' stata generata la richiesta di annullare una Reversale
     * La reversale non ha riscontri associati
     * E' stato richiesta di non annullare anche i doc. contabili collegati alla reversale
     * PostCondition:
     * Viene impostata la data di annullamento della reversale con la data odierna e lo stato della reversale
     * diventa ANNULLATO. Viene impostato lo stato ANNULLATO su tutte le righe della reversale.
     * Per ogni riga inoltre viene aggiornato l'importo associato a doc.contabili della scadenza di
     * accertamento legata alla riga (metodo aggiornaImportoAccertamenti), viene aggiornato lo stato
     * del documento amministrativo legato alla riga (metodo aggiornaStatoFattura). Vengono aggiornati i
     * saldi dei capitoli (metodo aggiornaCapitoloSaldoRiga). Per ogni associazione sospeso-reversale, viene
     * aggiornato l'importo associato del sospeso (metodo annullaImportoSospesi). Se la reversale ha associati mandati
     * o altre reversali NON viene eseguito il loro annullamento (metodo 'annullaDocContabiliCollegati').
     * <p>
     * errore riscontri associati
     * PreCondition:
     * E' stata generata la richiesta di annullare una Reversale che ha riscontri associati
     * PostCondition:
     * Una segnalazione di errore comunica all'utente l'impossibilità di eseguire l'annullamento
     *
     * @param userContext      lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale        <code>ReversaleBulk</code> la reversale da annullare
     * @param annullaCollegati valore booleano che indica se procedere o meno all'annullamento dei doc. contabili associati alla reversale
     * @return reversale <code>ReversaleBulk</code> la Reversale annullata
     */

    public ReversaleBulk annullaReversale(UserContext userContext, ReversaleBulk reversale, boolean annullaCollegati, boolean riemissione) throws ComponentException {
        try {
            if (reversale.isAnnullato())
                throw handleException(new ApplicationException("La reversale e' già stata annullata"));
            verificaStatoEsercizio(userContext, reversale.getEsercizio(), reversale.getCd_cds());
            BigDecimal totdettagli = ((Sospeso_det_etrHome) getHome(userContext, Sospeso_det_etrBulk.class)).calcolaTotDettagli(new V_mandato_reversaleBulk(reversale.getEsercizio(), reversale.getCd_tipo_documento_cont(), reversale.getCd_cds(), reversale.getPg_reversale()));
            if (totdettagli.compareTo(new BigDecimal(0)) > 0)
                throw new ApplicationException("Annullamento impossibile! La reversale e' già stata associata ad un riscontro");

//		verificaReversale( userContext, reversale, true );
            Sospeso_det_etrBulk sde;
            for (Iterator i = reversale.getSospeso_det_etrColl().iterator(); i.hasNext(); ) {
                sde = (Sospeso_det_etrBulk) i.next();
                if (sde.isToBeCreated() || sde.isToBeUpdated() || sde.isToBeDeleted())
                    throw new ApplicationException("Annullamento impossibile! Sono state fatte delle modifiche ai sospesi che devono essere ancora salvate");
            }

            checkAnnullabilita(userContext, reversale);
            if (isReversaleCollegataAnnullodaRiemettere(userContext, reversale).booleanValue())
                throw new ApplicationException(
                        "Annullamento impossibile! Esiste una reversale annullata associata alla reversale.");

            if (isAnnullabile(userContext, reversale).compareTo("N") == 0)
                throw new ApplicationException(
                        "Verificare lo stato di trasmissione della reversale. Annullamento impossibile!");

            lockBulk(userContext, reversale);

//		if(DateServices.isAnnoMaggEsScriv(userContext)) {
//		// Se la data di annullamento NON E' NULLA, e siamo in esercizio successivo, metto
//		// la data di trasmissione = ad istante successivo a quella di annullamento
//		 if(reversale.getDt_trasmissione() != null) {
//		  reversale.setDt_annullamento( DateServices.getNextMinTs( userContext,reversale.getDt_trasmissione()));
//		 } else {
//		  reversale.setDt_annullamento( DateServices.getMidDayTs( DateServices.getTs_valido( userContext)));
//		 }
//		} else {
//	 reversale.setDt_annullamento( DateServices.getTs_valido( userContext));
//		}

            reversale.setDt_annullamento(DateServices.getTs_valido(userContext));

            if (reversale.getStato_coge().equals(MandatoBulk.STATO_COGE_C))
                reversale.setStato_coge(MandatoBulk.STATO_COGE_R);
            if (!ReversaleBulk.TIPO_REGOLARIZZAZIONE.equals(reversale.getTi_reversale())) {
                reversale.setFl_riemissione(riemissione);
                reversale.setStato_trasmissione_annullo(MandatoIBulk.STATO_TRASMISSIONE_NON_INSERITO);
            }
            reversale.annulla();
            annullaImportoSospesi(userContext, reversale);

            Reversale_rigaBulk riga;
            SaldoComponentSession session = createSaldoComponentSession();
            for (Iterator i = reversale.getReversale_rigaColl().iterator(); i.hasNext(); ) {
                riga = (Reversale_rigaBulk) i.next();
                riga.annulla();
                aggiornaCapitoloSaldoRiga(userContext, riga, session);
            }

            aggiornaImportoAccertamenti(userContext, reversale);
            makeBulkPersistent(userContext, reversale);
            aggiornaStatoFattura(userContext, reversale, ANNULLAMENTO_REVERSALE_ACTION);
            if (annullaCollegati)
                annullaDocContabiliCollegati(userContext, reversale);
/*  24/09/2002
	Commentata la chiamata alla stored procedure per l'aggiornamento dei saldi,
	in quanto adesso non si imposta più a INCASSATO lo stato di una Reversale,
	quando viene associata ad un sospeso */
            // if ( reversale.getIm_reversale().compareTo( reversale.getIm_incassato()) == 0 )
            //	aggiornaSaldoPagato( userContext, reversale, ANNULLAMENTO_REVERSALE_ACTION );

            return reversale;
        } catch (Exception e) {
            throw handleException(reversale, e);
        }

    }

    public void annullaReversaleDiIncassoIVA(it.cnr.jada.UserContext userContext, it.cnr.contab.doccont00.core.bulk.ReversaleBulk reversale) throws it.cnr.jada.comp.ComponentException {
        try {
            /* REVERSALE */
            annullaReversale(userContext, reversale, false);
            /* DOC GENERICO */
            Reversale_rigaBulk rRiga = (Reversale_rigaBulk) reversale.getReversale_rigaColl().get(0);
            Documento_genericoBulk docGenerico = new Documento_genericoBulk(rRiga.getCd_cds(), rRiga.getCd_tipo_documento_amm(), rRiga.getCd_uo_doc_amm(), rRiga.getEsercizio_doc_amm(), rRiga.getPg_doc_amm());
            DocumentoGenericoComponentSession docSession = createDocumentoGenericoComponentSession();
            docGenerico = (Documento_genericoBulk) docSession.inizializzaBulkPerModifica(userContext, docGenerico);
            /*????? annulla documento contabile */
//		docSession.eliminaConBulk( userContext, docGenerico );
            docGenerico_annullaDocumentoGenerico(userContext, docGenerico);
            /* ACR_PGIRO */
            Documento_generico_rigaBulk dRiga;
            AccertamentoPGiroBulk accertamento;
            AccertamentoPGiroComponentSession acrSession = createAccertamentoPGiroComponentSession();
            for (Iterator i = docGenerico.getDocumento_generico_dettColl().iterator(); i.hasNext(); ) {
                dRiga = (Documento_generico_rigaBulk) i.next();
                accertamento = new AccertamentoPGiroBulk(dRiga.getAccertamento_scadenziario().getCd_cds(), dRiga.getAccertamento_scadenziario().getEsercizio(), dRiga.getAccertamento_scadenziario().getEsercizio_originale(), dRiga.getAccertamento_scadenziario().getPg_accertamento());
                accertamento = (AccertamentoPGiroBulk) acrSession.inizializzaBulkPerModifica(userContext, accertamento);
                acrSession.eliminaConBulk(userContext, accertamento);
            }

        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * annullamento
     * PreCondition:
     * E' stata generata la richiesta di annullare una Reversale di Regolarizzazione
     * PostCondition:
     * Viene richiesta alla Component che gestisce il Documento Generico l'annullamento della
     * reversale di regolarizzazione (metodo annullaReversale) e di eventuali documenti
     * generici associati alla reversale (metodo docGenerico_annullaDocumentoGenerico).
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale   <code>ReversaleBulk</code> la reversale di regolarizzazione da annullare
     */
    public void annullaReversaleDiRegolarizzazione(UserContext userContext, ReversaleBulk reversale) throws ComponentException {
        try {
            /* REVERSALE */
            annullaReversale(userContext, reversale);
            /* DOC GENERICO */
		/*
		Reversale_rigaBulk rRiga = (Reversale_rigaBulk) reversale.getReversale_rigaColl().get(0);
		Documento_genericoBulk docGenerico = new Documento_genericoBulk( rRiga.getCd_cds(), rRiga.getCd_tipo_documento_amm(), rRiga.getCd_uo_doc_amm(), rRiga.getEsercizio_doc_amm(), rRiga.getPg_doc_amm());
		DocumentoGenericoComponentSession docSession = createDocumentoGenericoComponentSession();
		docGenerico = (Documento_genericoBulk) docSession.inizializzaBulkPerModifica( userContext, docGenerico );
		//????? annulla documento contabile
//		docSession.eliminaConBulk( userContext, docGenerico );
		docGenerico_annullaDocumentoGenerico( userContext, docGenerico );
		*/
            aggiornaSaldoIncassato(userContext, reversale, ANNULLAMENTO_REVERSALE_ACTION);
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * annullamento
     * PreCondition:
     * E' stata generata la richiesta di annullare una Reversale di Trasferimento
     * PostCondition:
     * Viene richiesta alla Component che gestisce il Documento Generico l'annullamento della
     * reversale di Trasferimento (metodo annullaReversale) e di eventuali documenti
     * generici associati alla reversale (metodo docGenerico_annullaDocumentoGenerico).
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale   <code>ReversaleBulk</code> la reversale di trasferimento da annullare
     */
    public void annullaReversaleDiTrasferimento(UserContext userContext, ReversaleBulk reversale) throws ComponentException {
        try {
            /* REVERSALE */
            annullaReversale(userContext, reversale);
            /* DOC GENERICO */
            Reversale_rigaBulk rRiga = (Reversale_rigaBulk) reversale.getReversale_rigaColl().get(0);
            Documento_genericoBulk docGenerico = new Documento_genericoBulk(rRiga.getCd_cds(), rRiga.getCd_tipo_documento_amm(), rRiga.getCd_uo_doc_amm(), rRiga.getEsercizio_doc_amm(), rRiga.getPg_doc_amm());
            DocumentoGenericoComponentSession docSession = createDocumentoGenericoComponentSession();
            docGenerico = (Documento_genericoBulk) docSession.inizializzaBulkPerModifica(userContext, docGenerico);
            /*????? annulla documento contabile */
//		docSession.eliminaConBulk( userContext, docGenerico );
            docGenerico_annullaDocumentoGenerico(userContext, docGenerico);
            /* ACR_SISTEMA */
            Documento_generico_rigaBulk dRiga;
            AccertamentoCdsBulk accertamento;
            AccertamentoComponentSession acrSession = createAccertamentoComponentSession();
            for (Iterator i = docGenerico.getDocumento_generico_dettColl().iterator(); i.hasNext(); ) {
                dRiga = (Documento_generico_rigaBulk) i.next();
                accertamento = new AccertamentoCdsBulk(dRiga.getAccertamento_scadenziario().getCd_cds(), dRiga.getAccertamento_scadenziario().getEsercizio(), dRiga.getAccertamento_scadenziario().getEsercizio_originale(), dRiga.getAccertamento_scadenziario().getPg_accertamento());
                accertamento = (AccertamentoCdsBulk) acrSession.inizializzaBulkPerModifica(userContext, accertamento);
                acrSession.annullaAccertamento(userContext, accertamento);
                //acrSession.eliminaConBulk( userContext, accertamento );
            }

        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * ricerca sospesi
     * PreCondition:
     * E' stata richiesta la ricerca dei sospesi di entrata da associare ad una reversale
     * PostCondition:
     * Vengono ricercati tutti i sospesi di entrata che non sono ancora stati associati alla reversale
     * con cds appartenza uguale al cds appartenenza della reversale,
     * uo origine uguale all'uo di scrivania, importo disponibile (importo disponibile = importo iniziale del sospeso -
     * importo già associato a reversali) maggiore di zero (metodo findSospesiDiEntrata)
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param clausole    le clausole specificate dall'utente
     * @param reversale   <code>ReversaleBulk</code> la reversale
     * @return il RemoteIterator della lista dei sospesi
     */

    public it.cnr.jada.util.RemoteIterator cercaSospesi(UserContext userContext, it.cnr.jada.persistency.sql.CompoundFindClause clausole, ReversaleBulk reversale) throws it.cnr.jada.comp.ComponentException {
        try {
            return iterator(
                    userContext,
                    ((SospesoHome) getHome(userContext, SospesoBulk.class)).selectSospesiDiEntrata(reversale, clausole, Utility.createParametriCnrComponentSession().getParametriCnr(userContext, reversale.getEsercizio()).getFl_tesoreria_unica().booleanValue()),
                    SospesoBulk.class,
                    getFetchPolicyName("find"));

        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    private void checkAnnullabilita(UserContext userContext, ReversaleBulk reversale) throws ComponentException {
        try {
            LoggableStatement cs = new LoggableStatement(getConnection(userContext),
                    "{  call " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CNRCTB039.checkAnnullabilita(?, ?, ?, ?)}", false, this.getClass());
            try {
                cs.setString(1, "R"); //reversale
                cs.setObject(2, reversale.getEsercizio());
                cs.setString(3, reversale.getCd_cds());
                cs.setObject(4, reversale.getPg_reversale());
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

    private void checkDocAmmCambiato(UserContext userContext, Reversale_rigaBulk riga) throws ComponentException {
        try {
            LoggableStatement cs = new LoggableStatement(getConnection(userContext),
                    "{  call " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CNRCTB300.checkDocAmmCambiato(?, ?, ?, ?, ?, ? )}", false, this.getClass());
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

    /**
     * creazione
     * PreCondition:
     * E' stata generata la richiesta di creazione una Reversale Definitiva di Trasferimento
     * a partire da una reversale provvisoria di trasferimento
     * PostCondition:
     * Viene creata una istanza di Ass_mandato_reversaleBulk con il progressivo della reversale definitiva
     * e del mandato di trasferimento associato
     *
     * @param uc            lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale     <code>ReversaleBulk</code> la reversale definitiva
     * @param cd_cds_man    Il centro di spesa del mandato
     * @param esercizio_man L'esercizio del mandato
     * @param pg_man        Il numero del mandato
     * @return ass_man_rev <code>Ass_mandato_reversaleBulk</code> L'associazione mandato-reversale da creare
     */
    public Ass_mandato_reversaleBulk creaAss_mandato_reversale(UserContext uc, ReversaleBulk reversale, String cd_cds_man, Integer esercizio_man, Long pg_man) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        Ass_mandato_reversaleBulk ass_man_rev = new Ass_mandato_reversaleBulk();
        ass_man_rev.setUser(uc.getUser());
        ass_man_rev.setToBeCreated();

        // campi chiave dell'Associazione mandato_reversale
        ass_man_rev.setReversale(reversale);
        ass_man_rev.setCd_cds_mandato(cd_cds_man);
        ass_man_rev.setEsercizio_mandato(esercizio_man);
        ass_man_rev.setPg_mandato(pg_man);

        // altri campi
        ass_man_rev.setTi_origine(Tipo_bolloBulk.TIPO_SPESA);

        makeBulkPersistent(uc, ass_man_rev);
        return ass_man_rev;
    }

    /**
     * creazione reversale
     * PreCondition:
     * E' stata generata la richiesta di creazione di una Reversale e deve essere verificata la sua
     * data di emissione
     * PostCondition:
     * Viene richiamato il metodo creaConBulk specificando di verificare la data di emissione della reversale
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> la reversale da creare
     * @return OggettoBulk la Reversale creata
     */
/*
REVERSALE
1 - verifica reversale
2 - inserisce record reversale, terzo, riga
3 - aggiorna accertamento_scadenzario per im_associato_doc_contabili
4 - aggiorna stato cofi della fattura
5 - aggiorna saldi per im_incassato

*/
    public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        return creaConBulk(userContext, bulk, true);
    }

    /**
     * creazione reversale
     * PreCondition:
     * E' stata generata la richiesta di creazione di una Reversale e la reversale supera la validazione
     * (metodo verificaReversale)
     * PostCondition:
     * Vengono aggiornati gli importi dei sospesi eventualmente associati alla reversale (metodo aggiornaImportoSospesi),
     * vengono aggiornati gli importi associati a documenti contabili di tutte le scadenze di accertamenti specificate
     * nelle righe della reversale (metodo aggiornaImportoAccertamenti), vengono aggiornati i saldi relativi ai capitoli di entrata
     * (metodo aggiornaCapitoloSaldoRiga), vengono aggiornati gli stati delle fatture specificate nelle righe delle reversali
     * (metodo aggiornaStatoFattura).
     *
     * @param userContext          lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk                 <code>OggettoBulk</code> la reversale da creare
     * @param verificaDt_emissione booleano che indica se è necessario o meno verificare la dt_emissione della reversale
     * @return OggettoBulk la Reversale creata
     */
/*
REVERSALE
1 - verifica reversale
2 - inserisce record reversale, terzo, riga
3 - aggiorna accertamento_scadenzario per im_associato_doc_contabili
4 - aggiorna stato cofi della fattura
5 - aggiorna saldi per im_incassato

*/
    public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk, boolean verificaDt_emissione) throws ComponentException {
        ReversaleBulk reversale = (ReversaleBulk) bulk;
        //check and lock i doc.amm.
        for (Iterator i = reversale.getReversale_rigaColl().iterator(); i.hasNext(); )
            checkDocAmmCambiato(userContext, (Reversale_rigaBulk) i.next());

        verificaReversale(userContext, reversale, verificaDt_emissione);
        aggiornaImportoSospesi(userContext, reversale);
        Reversale_rigaBulk riga;
        SaldoComponentSession session = createSaldoComponentSession();
        for (Iterator i = reversale.getReversale_rigaColl().iterator(); i.hasNext(); ) {
            riga = (Reversale_rigaBulk) i.next();
            aggiornaCapitoloSaldoRiga(userContext, riga, session);
        }
        aggiornaImportoAccertamenti(userContext, reversale);
        reversale = (ReversaleBulk) super.creaConBulk(userContext, bulk);
        aggiornaStatoFattura(userContext, reversale, INSERIMENTO_REVERSALE_ACTION);

/*  24/09/2002
	Commentata la chiamata alla stored procedure per l'aggiornamento dei saldi,
	in quanto adesso non si imposta più a INCASSATO lo stato di una Reversale,
	quando viene associata ad un sospeso */
        // if ( reversale.getIm_incassato().compareTo( reversale.getIm_reversale()) == 0 )
        //	aggiornaSaldoPagato( userContext, reversale, INSERIMENTO_REVERSALE_ACTION );

        return reversale;

    }

    /*
     *  creazione reversale definitiva
     *    PreCondition:
     *      E' stata generata la richiesta di creazione di una Reversale definitiva
     *    PostCondition:
     *      Viene creata una reversale definitiva nel momento in cui le vengono associati
     *		 dei sospesi.
     *      Vengono create tante righe di reversale quante sono quelle della reversale
     *		 provvisoria di partenza.
     *      Viene creata una reversaleTerzo con i dati relativi alla reversaleTerzo di
     *		 partenza.
     *      Vengono create tante righe di sospesi quanti sono i sospesi selezionati dall'utente.
     *
     * @param uc lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale <code>ReversaleBulk</code> la reversale provvisoria
     *
     * @return reversaleDef <code>ReversaleBulk</code> la reversale definitiva creata
     */
    public ReversaleBulk creaReversaleDefinitiva(UserContext uc, ReversaleBulk reversale) throws ComponentException {
        try {
            ReversaleIBulk reversaleDef = new ReversaleIBulk();
            reversaleDef.setUser(reversale.getUser());
            reversaleDef.setToBeCreated();

            // campi chiave
            reversaleDef.setEsercizio(reversale.getEsercizio());
            reversaleDef.setCds(reversale.getCds());

            // altri campi...
            reversaleDef.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_REV);
            reversaleDef.setUnita_organizzativa(reversale.getUnita_organizzativa());
            reversaleDef.setCd_cds_origine(reversale.getCd_cds_origine());
            reversaleDef.setCd_uo_origine(reversale.getCd_uo_origine());
            reversaleDef.setTi_reversale(reversale.getTi_reversale());
            reversaleDef.setTi_competenza_residuo(reversale.getTi_competenza_residuo());
            reversaleDef.setDs_reversale(reversale.getDs_reversale());
            reversaleDef.setStato(reversale.getStato());
            reversaleDef.setIm_reversale(reversale.getIm_reversale());
            reversaleDef.setIm_incassato(reversale.getIm_incassato());
            /* segnalazione 539 */
//		reversaleDef.setDt_emissione( reversale.getDt_emissione() );
            reversaleDef.setDt_emissione(DateServices.getDt_valida(uc));
            reversaleDef.setStato_trasmissione(ReversaleBulk.STATO_TRASMISSIONE_NON_INSERITO);
            reversaleDef.setStato_coge(reversale.getStato_coge());

            Reversale_rigaBulk riga;
            for (Iterator i = reversale.getReversale_rigaColl().iterator(); i.hasNext(); ) {
                riga = (Reversale_rigaBulk) i.next();
                riga = creaReversaleRiga(uc, reversaleDef, riga);
                reversaleDef.addToReversale_rigaColl(riga);
            }

            Reversale_terzoBulk rTerzo = creaReversaleTerzo(uc, reversaleDef, reversale.getReversale_terzo().getCd_terzo());
            reversaleDef.setReversale_terzo(rTerzo);

            Sospeso_det_etrBulk sde;
            for (Iterator i = reversale.getSospeso_det_etrColl().iterator(); i.hasNext(); ) {
                sde = (Sospeso_det_etrBulk) i.next();
                sde.setReversale(reversaleDef);
                sde.setToBeCreated();
                reversaleDef.getSospeso_det_etrColl().add(sde);
            }
            reversaleDef = (ReversaleIBulk) creaConBulk(uc, reversaleDef, false);
            return reversaleDef;
        } catch (Exception e) {
            throw handleException(e);

        }
    }

    public ReversaleBulk creaReversaleDiIncassoIVA(UserContext userContext, MandatoBulk mandato) throws ComponentException {
        try {
            AccertamentoPGiroBulk accertamento;
            Documento_generico_rigaBulk dRiga;
            BigDecimal importo = ((MandatoIBulk) mandato).getImReversaleDiIncassoIVA();

            BigDecimal importoSplit = new java.math.BigDecimal(0);

            Mandato_rigaIBulk riga;
            for (Iterator i = mandato.getMandato_rigaColl().iterator(); i.hasNext(); ) {
                riga = (Mandato_rigaIBulk) i.next();

                if (it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA.equals(riga.getCd_tipo_documento_amm())
                        && riga.getIm_ritenute_riga().compareTo(new BigDecimal(0)) > 0)
                    if (mandato.getMandato_terzo().getTerzo().getAnagrafico() != null)
                        mandato.getMandato_terzo().getTerzo().setAnagrafico((AnagraficoBulk) getHome(userContext, AnagraficoBulk.class).findByPrimaryKey(new AnagraficoBulk(mandato.getMandato_terzo().getTerzo().getAnagrafico().getCd_anag())));
                if (mandato.getMandato_terzo().getTerzo().getAnagrafico().isItaliano())
                    importoSplit = importoSplit.add(riga.getIm_ritenute_riga());
            }

            //REVERSALE
            ReversaleIBulk reversale = new ReversaleIBulk();

            reversale.setToBeCreated();
            reversale.setUser(mandato.getUser());
            reversale.setEsercizio(mandato.getEsercizio());
            reversale.setCds(mandato.getCds());
            reversale.setUnita_organizzativa(mandato.getUnita_organizzativa());
            reversale.setCd_cds_origine(mandato.getCd_cds());
            reversale.setCd_uo_origine(mandato.getCd_unita_organizzativa());
            reversale.setTi_reversale(ReversaleBulk.TIPO_INCASSO);
            reversale.setTi_competenza_residuo(mandato.getTi_competenza_residuo());
            reversale.setDs_reversale("REVERSALE IVA");
            reversale.setStato(ReversaleBulk.STATO_REVERSALE_EMESSO);
            reversale.setIm_reversale(importo);
            reversale.setIm_incassato(new BigDecimal(0));
            reversale.setDt_emissione(mandato.getDt_emissione());
            reversale.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_REV);
            reversale.setStato_trasmissione(ReversaleBulk.STATO_TRASMISSIONE_NON_INSERITO);
            reversale.setStato_coge(MandatoBulk.STATO_COGE_N);

            //REVERSALE_TERZO
            Reversale_terzoBulk rTerzo = creaReversaleTerzo(userContext, reversale, mandato.getMandato_terzo());

            //DOCUMENTO_GENERICO
            Documento_genericoBulk documento = docGenerico_creaDocumentoGenerico(userContext, reversale, Numerazione_doc_ammBulk.TIPO_GEN_IVA_E);


            //CREA UN ACCERTAMENTO con una scadenza
            accertamento = createAccertamentoPGiroComponentSession().creaAccertamentoDiIncassoIVA(userContext, reversale, importoSplit.compareTo(new java.math.BigDecimal(0)) != 0);
            //CREA UNA RIGA DEL DOCUMENTO CONTABILIZZATA SULLA SCADENZA DELL'ACCERTAMENTO
            docGenerico_creaDocumentoGenericoRiga(userContext, documento, accertamento.getAccertamento_scadenzarioColl().get(0), ((MandatoIBulk) mandato).getImReversaleDiIncassoIVA(), mandato.getMandato_rigaColl().get(0));

            documento = (Documento_genericoBulk) createDocumentoGenericoComponentSession().creaConBulk(userContext, documento);

            for (Iterator i = documento.getDocumento_generico_dettColl().iterator(); i.hasNext(); )
                creaReversaleRiga(userContext, reversale, (Documento_generico_rigaBulk) i.next());
            reversale = (ReversaleIBulk) creaConBulk(userContext, reversale);
            return reversale;
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /*
     *  creazione reversale di Regolarizzazione
     *    PreCondition:
     *      E' stata generata la richiesta di creazione di una Reversale di regolarizzazione
     *    PostCondition:
     *      Viene creata una reversale di regolarizzazione a partire dal mandato di
     *		 regolarizzazione associato.
     *      Viene creata una reversaleTerzo (metodo creaReversaleTerzo).
     *      Viene creato un documento generico attivo (metodo docGenerico_creaDocumentoGenerico). Viene creata una
     *		 riga di documento generico (metodo docGenerico_creaDocumentoGenericoRiga) per ogni scadenza dell'accertamento
     *		 (specificato dall'utente) non ancora associata a documenti amministrativi.
     *      Vengono create tante righe di reversale quante sono quelle del documento generico (metodo creaReversaleRiga).
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato <code>MandatoBulk</code> il mandato
     *
     * @return reversale <code>ReversaleBulk</code> la reversale di regolarizzazione creata
     */
    public ReversaleBulk creaReversaleDiRegolarizzazione(UserContext userContext, MandatoBulk mandato) throws ComponentException {
        try {
            AccertamentoBulk accertamento = null;
            Accertamento_scadenzarioBulk scadenza;
            Mandato_rigaBulk mRiga;
            Documento_generico_rigaBulk dRiga;
            Boolean fl_pgiro;

            //REVERSALE
            ReversaleIBulk reversale = new ReversaleIBulk();

            reversale.setToBeCreated();
            reversale.setUser(mandato.getUser());
            reversale.setEsercizio(mandato.getEsercizio());
            //La reversale viene creata sulla Uo e Cds di appartenenza dell'accertamento
            //usato per la regolarizzazione
            reversale.setCds(((MandatoIBulk) mandato).getAccertamentoPerRegolarizzazione().getCds());
            reversale.setUnita_organizzativa(((MandatoIBulk) mandato).getAccertamentoPerRegolarizzazione().getUnita_organizzativa());
            reversale.setCd_cds_origine(mandato.getCd_cds_origine());
            reversale.setCd_uo_origine(mandato.getCd_uo_origine());
            reversale.setTi_reversale(ReversaleBulk.TIPO_REGOLARIZZAZIONE);
            reversale.setTi_competenza_residuo(((MandatoIBulk) mandato).getAccertamentoPerRegolarizzazione().getTi_competenza_residuo());
            reversale.setDs_reversale("REVERSALE DI REGOLARIZZAZIONE");
            reversale.setStato(ReversaleBulk.STATO_REVERSALE_EMESSO);
            reversale.setIm_reversale(mandato.getIm_mandato());
            reversale.setIm_incassato(mandato.getIm_mandato());
            reversale.setDt_emissione(mandato.getDt_emissione());
            reversale.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_REV);
            reversale.setStato_trasmissione(ReversaleBulk.STATO_TRASMISSIONE_NON_INSERITO);
            List result = getHome(userContext, Unita_organizzativa_enteBulk.class).findAll();
            reversale.setCd_uo_ente(((Unita_organizzativaBulk) result.get(0)).getCd_unita_organizzativa());
            reversale.setStato_coge(MandatoBulk.STATO_COGE_N);

            //REVERSALE_TERZO
            Reversale_terzoBulk rTerzo = creaReversaleTerzo(userContext, reversale, mandato.getMandato_terzo());

            if (!((MandatoIBulk) mandato).isGeneraReversaleDaDocAmm()) {
                Documento_genericoBulk documento = null;

                for (Iterator i = ((MandatoIBulk) mandato).getScadenzeAccertamentoSelezionatePerRegolarizzazione().iterator(); i.hasNext(); ) {
                    scadenza = (Accertamento_scadenzarioBulk) i.next();
                    //sui dettagli senza documento creo la riga di documento e successivamente la riga di reversale
                    if (scadenza.getIm_scadenza().subtract(scadenza.getIm_associato_doc_amm()).compareTo(new java.math.BigDecimal(0)) > 0) {
                        if (documento == null)
                            documento = docGenerico_creaDocumentoGenerico(userContext, reversale, Numerazione_doc_ammBulk.TIPO_REGOLA_E);
                        dRiga = docGenerico_creaDocumentoGenericoRiga(userContext, documento, scadenza, scadenza.getIm_scadenza().subtract(scadenza.getIm_associato_doc_amm()));
                    } else if (scadenza.getIm_scadenza().subtract(scadenza.getIm_associato_doc_contabile()).compareTo(new java.math.BigDecimal(0)) > 0) {
                        //sui dettagli con documento generico creo direttamente la riga di reversale
                        V_doc_attivo_accertamentoBulk dr = new V_doc_attivo_accertamentoBulk();
                        dr.setCd_cds_accertamento(scadenza.getCd_cds());
                        dr.setEsercizio_accertamento(scadenza.getEsercizio());
                        dr.setEsercizio_ori_accertamento(scadenza.getEsercizio_originale());
                        dr.setPg_accertamento(scadenza.getPg_accertamento());
                        dr.setPg_accertamento_scadenzario(scadenza.getPg_accertamento_scadenzario());

                        for (Iterator y = getHome(userContext, V_doc_attivo_accertamentoBulk.class).find(dr).iterator(); y.hasNext(); ) {
                            V_doc_attivo_accertamentoBulk drBulk = (V_doc_attivo_accertamentoBulk) y.next();
                            if (drBulk != null)
                                creaReversaleRigaSenzaModalitaPag(userContext, reversale, drBulk);
                        }
                    }
                }

                if (documento != null) {
                    documento = (Documento_genericoBulk) createDocumentoGenericoComponentSession().creaConBulk(userContext, documento);
                    // creo una reversale riga per ogni riga di doc. generico
                    for (Iterator i = documento.getDocumento_generico_dettColl().iterator(); i.hasNext(); )
                        creaReversaleRiga(userContext, reversale, (Documento_generico_rigaBulk) i.next());
                }
            } else {
                V_doc_attivo_accertamentoBulk dr;
                for (Iterator i = ((MandatoIBulk) mandato).getDocGenericiSelezionatiPerRegolarizzazione().iterator(); i.hasNext(); ) {
                    dr = (V_doc_attivo_accertamentoBulk) i.next();
                    creaReversaleRigaSenzaModalitaPag(userContext, reversale, dr);
                }
            }

            reversale = (ReversaleIBulk) creaConBulk(userContext, reversale);
            aggiornaSaldoIncassato(userContext, reversale, INSERIMENTO_REVERSALE_ACTION);


/* GESTIONE DISTINTA PER PARTITA DI GIRO E NON
		fl_pgiro = ((Mandato_rigaBulk) mandato.getMandato_rigaColl().get(0)).getFl_pgiro();

		//SU PGIRO: creo una riga di documento generico per ogni riga di mandato
		if ( fl_pgiro.booleanValue() )
		{
			for ( Iterator i = mandato.getMandato_rigaColl().iterator(); i.hasNext(); )
			{
				mRiga = (Mandato_rigaBulk)i.next();
				scadenza = ((ImpegnoPGiroHome)getHome( userContext, ImpegnoPGiroBulk.class )).findAccertamentoScadenzarioPGiro( mRiga.getEsercizio_obbligazione(), mRiga.getCd_cds(), mRiga.getEsercizio_ori_obbligazione(), mRiga.getPg_obbligazione());
				//CREA UNA RIGA DEL DOCUMENTO CONTABILIZZATA SULLA SCADENZA DELL'ACCERTAMENTO
				dRiga = docGenerico_creaDocumentoGenericoRiga( userContext, documento, scadenza, mRiga.getIm_mandato_riga()  );
			}
		}
		else //NON SU PGIRO: creo una riga di documento generico per ogni scadenza della obbligazione
		{
			for ( Iterator i = ((MandatoIBulk)mandato).getAccertamentoPerRegolarizzazione().getAccertamento_scadenzarioColl().iterator(); i.hasNext(); )
			{
				scadenza = (Accertamento_scadenzarioBulk) i.next();
				if ( scadenza.getIm_associato_doc_amm().compareTo( new java.math.BigDecimal(0)) == 0 )
					dRiga = docGenerico_creaDocumentoGenericoRiga( userContext, documento, scadenza, scadenza.getIm_scadenza()  );
			}

		}
		documento = (Documento_genericoBulk) createDocumentoGenericoComponentSession().creaConBulk( userContext, documento );


		// creo una reversale riga per ogni riga di doc. generico
		for ( Iterator i = documento.getDocumento_generico_dettColl().iterator(); i.hasNext(); )
			creaReversaleRiga( userContext, reversale, (Documento_generico_rigaBulk) i.next(), fl_pgiro );
		reversale = (ReversaleIBulk) creaConBulk( userContext, reversale);
*/
            return reversale;
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /*
     *  creazione reversale di Trasferimento
     *    PreCondition:
     *      E' stata generata la richiesta di creazione di una Reversale di trasferimento
     *    PostCondition:
     *      Viene creata una reversale di trasferimento a partire dal mandato di
     *		 accreditamento associato.
     *      Viene creata una reversaleTerzo (metodo creaReversaleTerzo).
     *      Viene creato un documento generico con le relative righe (metodo docGenerico_creaDocumentoGenerico).
     *      Vengono create tante righe di reversale quante sono quelle del documento generico (metodo creaReversaleRiga).
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato <code>MandatoAccreditamentoBulk</code> il mandato di accreditamento
     *
     * @return reversale <code>ReversaleBulk</code> la reversale di trasferimento creata
     */
    public ReversaleBulk creaReversaleDiTrasferimento(UserContext userContext, MandatoAccreditamentoBulk mandato) throws ComponentException {
        try {
            AccertamentoBulk accertamento;
            MandatoAccreditamento_rigaBulk mRiga;
            Documento_generico_rigaBulk dRiga;

            //REVERSALE
            ReversaleIBulk reversale = new ReversaleIBulk();

            reversale.setToBeCreated();
            reversale.setUser(mandato.getUser());
            reversale.setEsercizio(mandato.getEsercizio());
            CdsBulk cds = (CdsBulk) getHome(userContext, CdsBulk.class).findByPrimaryKey(new CdsBulk(mandato.getCodice_cds()));
            reversale.setCds(cds);
            Unita_organizzativaBulk uo = findUnita_organizzativa(userContext, mandato);
            reversale.setUnita_organizzativa(uo);
//		reversale.setCd_cds_origine( mandato.getCd_cds_origine());
//		reversale.setCd_uo_origine(mandato.getCd_uo_origine());
            reversale.setCd_cds_origine(reversale.getCd_cds());
            reversale.setCd_uo_origine(reversale.getCd_unita_organizzativa());
            reversale.setTi_reversale(ReversaleBulk.TIPO_TRASFERIMENTO);
            reversale.setTi_competenza_residuo(ReversaleBulk.TIPO_COMPETENZA);
            reversale.setDs_reversale("REVERSALE DI TRASFERIMENTO DA CNR");
            reversale.setStato(ReversaleBulk.STATO_REVERSALE_EMESSO);
            reversale.setIm_reversale(mandato.getIm_mandato());
            reversale.setIm_incassato(new BigDecimal(0));
            reversale.setDt_emissione(mandato.getDt_emissione());

            if (Utility.createParametriCnrComponentSession().getParametriCnr(userContext, mandato.getEsercizio()).getFl_siope().equals(Boolean.TRUE))
                reversale.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_REV);
            else
                reversale.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_REV_PROVV);

            reversale.setStato_trasmissione(ReversaleBulk.STATO_TRASMISSIONE_NON_INSERITO);
            reversale.setStato_coge(MandatoBulk.STATO_COGE_X);

            //REVERSALE_TERZO
            Reversale_terzoBulk rTerzo = creaReversaleTerzo(userContext, reversale, mandato.getCd_unita_organizzativa());

            //DOCUMENTO_GENERICO
            Documento_genericoBulk documento = docGenerico_creaDocumentoGenerico(userContext, reversale, Numerazione_doc_ammBulk.TIPO_TRASF_E);

            for (Iterator i = mandato.getMandato_rigaColl().iterator(); i.hasNext(); ) {
                mRiga = (MandatoAccreditamento_rigaBulk) i.next();
                //CREA UN ACCERTAMENTO con una scadenza
                accertamento = createAccertamentoComponentSession().creaAccertamentoDiSistema(userContext, mRiga, uo);
//			accertamento = accertamento_creaAccertamentoDiSistema( userContext, mRiga, uo );
                //CREA UNA RIGA DEL DOCUMENTO CONTABILIZZATA SULLA SCADENZA DELL'ACCERTAMENTO
                dRiga = docGenerico_creaDocumentoGenericoRiga(userContext, documento, accertamento.getAccertamento_scadenzarioColl().get(0), mRiga);
            }
            documento = (Documento_genericoBulk) createDocumentoGenericoComponentSession().creaConBulk(userContext, documento);
            for (Iterator i = documento.getDocumento_generico_dettColl().iterator(); i.hasNext(); )
                creaReversaleRiga(userContext, reversale, (Documento_generico_rigaBulk) i.next(), new Boolean(false));
            reversale = (ReversaleIBulk) creaConBulk(userContext, reversale);
            return reversale;
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /*
     *  creazione riga di reversale
     *    PreCondition:
     *      E' stata generata la richiesta di creazione di una riga di Reversale di regolarizzazione
     *    PostCondition:
     *      Viene creata una riga di reversale richiamando il metodo 'creaReversaleRiga' e passando come
     *      parametro del fl_pgiro il valore che tale flag assume nell'accertamento su cui e' stata contabilizzata la riga
     *      del documento amministrativo
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale <code>ReversaleBulk</code> la reversale
     * @param dRiga <code>Documento_generico_rigaBulk</code> la riga del documento generico
     *
     * @return riga <code>Reversale_rigaBulk</code> la riga della reversale creata
     */
    private Reversale_rigaBulk creaReversaleRiga(UserContext userContext, ReversaleBulk reversale, Documento_generico_rigaBulk dRiga) throws ComponentException {
        return creaReversaleRiga(userContext, reversale, dRiga, dRiga.getAccertamento_scadenziario().getAccertamento().getFl_pgiro());
    }

    /*
     *  creazione riga di reversale
     *    PreCondition:
     *      E' stata generata la richiesta di creazione di una riga di Reversale di regolarizzazione o di trasferimento
     *    PostCondition:
     *      Viene creata una riga di reversale coi dati relativi al documento generico di entrata
     *		 creato in automatico alla creazione della reversale
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale <code>ReversaleBulk</code> la reversale
     * @param dRiga <code>Documento_generico_rigaBulk</code> la riga del documento generico
     * @param fl_pgiro il flag che determina se si tratta di partita di giro
     *
     * @return riga <code>Reversale_rigaBulk</code> la riga della reversale creata
     */
    private Reversale_rigaBulk creaReversaleRiga(UserContext userContext, ReversaleBulk reversale, Documento_generico_rigaBulk dRiga, Boolean fl_pgiro) throws ComponentException {
        try {
            if (!dRiga.getStato_cofi().equals(Documento_generico_rigaBulk.STATO_CONTABILIZZATO))
                return null;
            Reversale_rigaBulk riga = new Reversale_rigaIBulk();
            riga.setToBeCreated();
            riga.setUser(reversale.getUser());
            riga.setStato(Reversale_rigaBulk.STATO_INIZIALE);
            riga.setIm_reversale_riga(dRiga.getIm_riga());
            riga.setReversale(reversale);
            //accertamento
            riga.setEsercizio_accertamento(dRiga.getAccertamento_scadenziario().getEsercizio());
            riga.setEsercizio_ori_accertamento(dRiga.getAccertamento_scadenziario().getEsercizio_originale());
            riga.setPg_accertamento(dRiga.getAccertamento_scadenziario().getPg_accertamento());
            riga.setPg_accertamento_scadenzario(dRiga.getAccertamento_scadenziario().getPg_accertamento_scadenzario());
            riga.setFl_pgiro(fl_pgiro);

            //doc amm
            riga.setEsercizio_doc_amm(dRiga.getEsercizio());
            riga.setCd_cds_doc_amm(dRiga.getCd_cds());
            riga.setCd_uo_doc_amm(dRiga.getCd_unita_organizzativa());
            riga.setCd_tipo_documento_amm(dRiga.getCd_tipo_documento_amm());
            riga.setPg_doc_amm(dRiga.getPg_documento_generico());
            riga.setPg_ver_rec_doc_amm(dRiga.getDocumento_generico().getPg_ver_rec());

            //imposto il terzo
            riga.setCd_terzo(dRiga.getTerzo().getCd_terzo()); //CNR

            BancaBulk banca = new BancaBulk(dRiga.getCd_terzo_uo_cds(), dRiga.getPg_banca_uo_cds());
            riga.setBanca(banca);

            //imposto le modalità di pagamento del terzo uo
            Modalita_pagamentoBulk mod_pagamento = new Modalita_pagamentoBulk(dRiga.getCd_modalita_pag_uo_cds(), dRiga.getCd_terzo_uo_cds());
            riga.setModalita_pagamento(mod_pagamento);

            reversale.getReversale_rigaColl().add(riga);

            //Carico automaticamente i codici SIOPE e visualizzo quelli ancora collegabili se la gestione è attiva
            if (Utility.createParametriCnrComponentSession().getParametriCnr(userContext, reversale.getEsercizio()).getFl_siope().booleanValue()) {
                riga = aggiornaLegameSIOPE(userContext, riga);
                riga = setCodiciSIOPECollegabili(userContext, riga);
            }

            return riga;

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /*
     *  creazione riga di reversale (definitiva)
     *    PreCondition:
     *      E' stata generata la richiesta di creazione di una riga di Reversale definitiva a partire da una Provvisoria
     *    PostCondition:
     *      Viene creata una riga di reversale (definitiva) a partire dalle righe di quella
     *		 provvisoria
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversaleDef <code>ReversaleBulk</code> la reversale definitiva
     * @param rigaProvv <code>Reversale_rigaBulk</code> la riga della reversale provvisoria
     *
     * @return riga <code>Reversale_rigaBulk</code> la riga della reversale creata
     */
    private Reversale_rigaBulk creaReversaleRiga(UserContext userContext, ReversaleBulk reversaleDef, Reversale_rigaBulk rigaProvv) throws ComponentException {
        try {
            Reversale_rigaIBulk riga = new Reversale_rigaIBulk();
            riga.setToBeCreated();
            riga.setUser(userContext.getUser());
            riga.setStato(Reversale_rigaBulk.STATO_INIZIALE);
            riga.setIm_reversale_riga(rigaProvv.getIm_reversale_riga());
            riga.setReversale(reversaleDef);
            riga.setEsercizio_accertamento(rigaProvv.getEsercizio_accertamento());
            riga.setEsercizio_ori_accertamento(rigaProvv.getEsercizio_ori_accertamento());
            riga.setPg_accertamento(rigaProvv.getPg_accertamento());
            riga.setPg_accertamento_scadenzario(rigaProvv.getPg_accertamento_scadenzario());
            riga.setFl_pgiro(rigaProvv.getFl_pgiro());
            riga.setCd_uo_doc_amm(rigaProvv.getCd_uo_doc_amm());
            riga.setCd_cds_doc_amm(rigaProvv.getCd_cds_doc_amm());
            riga.setEsercizio_doc_amm(rigaProvv.getEsercizio_doc_amm());
            riga.setCd_tipo_documento_amm(rigaProvv.getCd_tipo_documento_amm());
            riga.setPg_doc_amm(rigaProvv.getPg_doc_amm());


            V_doc_attivo_accertamentoBulk docAttivo = new V_doc_attivo_accertamentoBulk();
            docAttivo.setCd_cds(riga.getCd_cds_doc_amm());
            docAttivo.setCd_unita_organizzativa(riga.getCd_uo_doc_amm());
            docAttivo.setCd_tipo_documento_amm(riga.getCd_tipo_documento_amm());
            docAttivo.setEsercizio(riga.getEsercizio_doc_amm());
            docAttivo.setPg_documento_amm(riga.getPg_doc_amm());
            docAttivo.setCd_cds_accertamento(riga.getCd_cds());
            docAttivo.setEsercizio_accertamento(riga.getEsercizio_accertamento());
            docAttivo.setEsercizio_ori_accertamento(riga.getEsercizio_ori_accertamento());
            docAttivo.setPg_accertamento(riga.getPg_accertamento());
            docAttivo.setPg_accertamento_scadenzario(riga.getPg_accertamento_scadenzario());
            List result = getHome(userContext, V_doc_attivo_accertamentoBulk.class).find(docAttivo);
            if (result == null || result.size() == 0)
                throw new ApplicationException("Non trovato doc.amm." + docAttivo.getCd_tipo_documento_amm() + ": " + docAttivo.getPg_documento_amm());
            riga.setPg_ver_rec_doc_amm(((V_doc_attivo_accertamentoBulk) result.get(0)).getPg_ver_rec());

            //imposto il terzo
            riga.setCd_terzo(rigaProvv.getCd_terzo());
            riga.setBanca(rigaProvv.getBanca());
            riga.setBancaOptions(rigaProvv.getBancaOptions());
            riga.setModalita_pagamento(rigaProvv.getModalita_pagamento());
            riga.setModalita_pagamentoOptions(rigaProvv.getModalita_pagamentoOptions());

            for (Iterator i = rigaProvv.getReversale_siopeColl().iterator(); i.hasNext(); ) {
                Reversale_siopeBulk siopeProvv = (Reversale_siopeIBulk) i.next();

                Reversale_siopeBulk reversale_siope = new Reversale_siopeIBulk();
                reversale_siope.setCodice_siope(siopeProvv.getCodice_siope());
                reversale_siope.setImporto(siopeProvv.getImporto());
                reversale_siope.setToBeCreated();

                riga.addToReversale_siopeColl(reversale_siope);
            }

            return riga;
        } catch (Exception e) {
            throw handleException(e);
        }


    }
    /*
     *  creazione reversale terzo per Reversale di trasferimento
     *    PreCondition:
     *      E' stata generata la richiesta di creazione di una Reversale_terzoBulk per una Reversale di trasferimento
     *    PostCondition:
     *      Viene creata una istanza di Reversale_terzoBulk coi dati del Cds beneficiario del mandato di accreditamento e viene impostato
     *      il tipo bollo di default.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale <code>ReversaleBulk</code> la reversale
     * @param mTerzo  l'istanza di <code>Mandato_terzoBulk</code>
     *
     * @return rTerzo l'istanza di <code>Reversale_terzoBulk</code> creata
     */

    /*
     *  creazione riga di reversale
     *    PreCondition:
     *      E' stata generata la richiesta di creazione di una riga di Reversale dall'on-line
     *    PostCondition:
     *      Viene creata una riga di reversale (metodo 'creaReversaleRigaSenzaModalitaPag')
     *		Vengono inoltre impostati come dati relativi
     *      alla banca e alla modalità di pagamento quelli presenti nel documento amministrativo
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale <code>ReversaleBulk</code> la reversale
     * @param docAttivo <code>V_doc_attivo_accertamentoBulk</code> il documento attivo
     * @param cd_terzo Il codice del terzo della reversale
     *
     * @return riga <code>Reversale_rigaBulk</code> la riga della reversale creata
     */
    private Reversale_rigaBulk creaReversaleRigaConModalitaPag(UserContext userContext, ReversaleBulk reversale, V_doc_attivo_accertamentoBulk docAttivo) throws ComponentException {
        try {
            Reversale_rigaIBulk riga = (Reversale_rigaIBulk) creaReversaleRigaSenzaModalitaPag(userContext, reversale, docAttivo);

            //imposto le coordinate bancarie del terzo uo
/*		SQLBuilder sql = getHome( userContext, TerzoBulk.class ).createSQLBuilder();
		sql.addClause( "AND", "cd_unita_organizzativa", sql.LIKE, reversale.getCd_cds()+".%");
		List result = getHome( userContext, TerzoBulk.class ).fetchAll( sql );
		if ( result.size() == 0 )
			throw handleException( new ApplicationException(" Impossibile emettere la reversale: il Cds non e' stato codificato in anagrafica"));
*/

            TerzoBulk terzo_uo = new TerzoBulk(docAttivo.getCd_terzo_uo_cds());
            BancaBulk banca = new BancaBulk();
            banca.setTerzo(terzo_uo);
            banca.setPg_banca(docAttivo.getPg_banca());
            banca = (BancaBulk) getHome(userContext, BancaBulk.class).findByPrimaryKey(banca);
            riga.setBanca(banca);
            riga.setBancaOptions(findBancaOptions(userContext, riga));

            //imposto le modalità di pagamento del terzo uo
            Modalita_pagamentoBulk mod_pagamento = new Modalita_pagamentoBulk();
            mod_pagamento.setTerzo(terzo_uo);
            Rif_modalita_pagamentoBulk rif_modalita_pagamento = new Rif_modalita_pagamentoBulk(docAttivo.getCd_modalita_pag());
            mod_pagamento.setRif_modalita_pagamento(rif_modalita_pagamento);
            mod_pagamento = (Modalita_pagamentoBulk) getHome(userContext, Modalita_pagamentoBulk.class).findByPrimaryKey(mod_pagamento);
            riga.setModalita_pagamento(mod_pagamento);
            riga.setModalita_pagamentoOptions(findModalita_pagamentoOptions(userContext, riga));

            return riga;
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /*
     *  creazione riga di reversale
     *    PreCondition:
     *      E' stata generata la richiesta di creazione di una riga di Reversale dall'on-line o dall'automatismo
     *      che genera la reversale di regolarizzazione (con doc.amm.attivi selezionati)
     *    PostCondition:
     *      Viene creata una riga di reversale coi dati relativi al documento ammninistrativo di spesa
     *		 selezionato dall'utente.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale <code>ReversaleBulk</code> la reversale
     * @param docAttivo <code>V_doc_attivo_accertamentoBulk</code> il documento attivo
     * @param cd_terzo Il codice del terzo della reversale
     *
     * @return riga <code>Reversale_rigaBulk</code> la riga della reversale creata
     */
    private Reversale_rigaBulk creaReversaleRigaSenzaModalitaPag(UserContext userContext, ReversaleBulk reversale, V_doc_attivo_accertamentoBulk docAttivo) throws ComponentException {
        try {
            Reversale_rigaIBulk riga = new Reversale_rigaIBulk();
            riga.setToBeCreated();
            riga.setUser(reversale.getUser());
            riga.setStato(Reversale_rigaBulk.STATO_INIZIALE);
            riga.setIm_reversale_riga(docAttivo.getIm_totale_doc_amm());
            riga.setReversale(reversale);
            riga.setEsercizio_accertamento(docAttivo.getEsercizio_accertamento());
            riga.setEsercizio_ori_accertamento(docAttivo.getEsercizio_ori_accertamento());
            riga.setPg_accertamento(docAttivo.getPg_accertamento());
            riga.setPg_accertamento_scadenzario(docAttivo.getPg_accertamento_scadenzario());
            riga.setFl_pgiro(docAttivo.getFl_pgiro());
            riga.setCd_uo_doc_amm(docAttivo.getCd_unita_organizzativa());
            riga.setCd_cds_doc_amm(docAttivo.getCd_cds());
            riga.setEsercizio_doc_amm(docAttivo.getEsercizio());
            riga.setCd_tipo_documento_amm(docAttivo.getCd_tipo_documento_amm());
            riga.setPg_doc_amm(docAttivo.getPg_documento_amm());
            riga.setPg_ver_rec_doc_amm(docAttivo.getPg_ver_rec());
            riga.setFlCancellazione(docAttivo.getFl_selezione().equalsIgnoreCase("Y"));
            riga.setTi_fattura(docAttivo.getTi_fattura());
            riga.setCd_terzo(docAttivo.getCd_terzo());

            ((ReversaleIBulk) reversale).addToReversale_rigaColl(riga, docAttivo);

            //Carico automaticamente i codici SIOPE e visualizzo quelli ancora collegabili se la gestione è attiva
            if (Utility.createParametriCnrComponentSession().getParametriCnr(userContext, reversale.getEsercizio()).getFl_siope().booleanValue()) {
                riga = (Reversale_rigaIBulk) aggiornaLegameSIOPE(userContext, riga);
                riga = (Reversale_rigaIBulk) setCodiciSIOPECollegabili(userContext, riga);
            }

            return riga;
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    private Reversale_terzoBulk creaReversaleTerzo(UserContext userContext, ReversaleBulk reversale, Mandato_terzoBulk mTerzo) throws ComponentException {
        try {
            Reversale_terzoBulk rTerzo = new Reversale_terzoBulk();
            rTerzo.setToBeCreated();
            rTerzo.setUser(reversale.getUser());
            rTerzo.setReversaleI((ReversaleIBulk) reversale);

            rTerzo.setTerzo(mTerzo.getTerzo());

            //imposto il tipo bollo esente
            rTerzo.setTipoBollo(mTerzo.getTipoBollo());

            reversale.setReversale_terzo(rTerzo);
            return rTerzo;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /*
     *  creazione reversale terzo
     *    PreCondition:
     *      E' stata generata la richiesta di creazione di una Reversale_terzoBulk
     *    PostCondition:
     *      Viene creata una istanza di Reversale_terzoBulk coi dati del terzo della reversale e viene impostato
     *      il tipo bollo di default.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale <code>ReversaleBulk</code> la reversale
     * @param cd_terzo Il codice del terzo della reversale
     *
     * @return rTerzo l'istanza di <code>Reversale_terzoBulk</code> creata
     */
    private Reversale_terzoBulk creaReversaleTerzo(UserContext userContext, ReversaleBulk reversale, Integer cd_terzo) throws ComponentException {
        try {
            Reversale_terzoBulk rTerzo = new Reversale_terzoBulk();
            rTerzo.setToBeCreated();
            rTerzo.setUser(reversale.getUser());
            rTerzo.setReversaleI((ReversaleIBulk) reversale);

            //imposto il terzo
            TerzoBulk terzo = (TerzoBulk) getHome(userContext, TerzoBulk.class).findByPrimaryKey(new TerzoKey(cd_terzo));
            rTerzo.setTerzo(terzo);
            //imposto il tipo bollo di default
            rTerzo.setTipoBollo(((it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloHome) getHome(userContext, Tipo_bolloBulk.class)).findTipoBolloDefault(Tipo_bolloBulk.TIPO_ENTRATA));
            return rTerzo;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /*
     *  creazione reversale terzo per Reversale
     *    PreCondition:
     *      E' stata generata la richiesta di creazione di una Reversale_terzoBulk per una Reversale
     *    PostCondition:
     *      Viene creata una istanza di Reversale_terzoBulk con unità organizzativa del Cnr e viene impostato
     *      il tipo bollo di default.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale <code>ReversaleBulk</code> la reversale
     * @param cd_unita_organizzativa Il codice dell'unità organizzativa della reversale
     *
     * @return rTerzo l'istanza di <code>Reversale_terzoBulk</code> creata
     */
    private Reversale_terzoBulk creaReversaleTerzo(UserContext userContext, ReversaleBulk reversale, String cd_unita_organizzativa) throws ComponentException {
        try {
            Reversale_terzoBulk rTerzo = new Reversale_terzoBulk();
            rTerzo.setToBeCreated();
            rTerzo.setUser(reversale.getUser());
            rTerzo.setReversaleI((ReversaleIBulk) reversale);

            //imposto il terzo
            SQLBuilder sql = getHome(userContext, TerzoBulk.class).createSQLBuilder();
            sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, cd_unita_organizzativa); //CNR
            List result = getHome(userContext, TerzoBulk.class).fetchAll(sql);
            if (result.size() == 0)
                throw new ApplicationException("Non e' stato codificato il CNR come terzo in anagrafica");
            rTerzo.setTerzo((TerzoBulk) result.get(0));

            //imposto il tipo bollo esente
            rTerzo.setTipoBollo(((it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloHome) getHome(userContext, Tipo_bolloBulk.class)).findTipoBolloEsente());

            reversale.setReversale_terzo(rTerzo);
            return rTerzo;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative agli Accertamenti
     *
     * @return AccertamentoComponentSession l'istanza di <code>AccertamentoComponentSession</code> che serve per gestire un accertamento
     */
    private it.cnr.contab.doccont00.ejb.AccertamentoComponentSession createAccertamentoComponentSession() throws ComponentException {
        try {
            return (AccertamentoComponentSession) EJBCommonServices.createEJB("CNRDOCCONT00_EJB_AccertamentoComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative agli Accertamenti
     *
     * @return AccertamentoComponentSession l'istanza di <code>AccertamentoComponentSession</code> che serve per gestire un accertamento
     */
    private it.cnr.contab.doccont00.ejb.AccertamentoPGiroComponentSession createAccertamentoPGiroComponentSession() throws ComponentException {
        try {
            return (AccertamentoPGiroComponentSession) EJBCommonServices.createEJB("CNRDOCCONT00_EJB_AccertamentoPGiroComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni di lettura della Configurazione CNR
     *
     * @return Configurazione_cnrComponentSession l'istanza di <code>Configurazione_cnrComponentSession</code> che serve per leggere i parametri di configurazione del CNR
     */
    private Configurazione_cnrComponentSession createConfigurazioneCnrComponentSession() throws ComponentException {
        try {
            return (Configurazione_cnrComponentSession) EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative ai Documenti Amministrativi Generici
     *
     * @return DocumentoGenericoComponentSession l'istanza di <code>DocumentoGenericoComponentSession</code> che serve per gestire un documento generico
     */
    private DocumentoGenericoComponentSession createDocumentoGenericoComponentSession() throws ComponentException {
        try {
            return (DocumentoGenericoComponentSession) EJBCommonServices.createEJB("CNRDOCAMM00_EJB_DocumentoGenericoComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative alle Fatture Passive
     */
    private FatturaPassivaComponentSession createFatturaPassivaComponentSession() throws ComponentException {
        try {
            return (FatturaPassivaComponentSession) EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaPassivaComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative ai Mandati
     *
     * @return MandatoComponentSession l'istanza di <code>MandatoComponentSession</code> che serve per gestire un mandato
     */
    private it.cnr.contab.doccont00.ejb.MandatoComponentSession createMandatoComponentSession() throws ComponentException {
        try {
            return (MandatoComponentSession) EJBCommonServices.createEJB("CNRDOCCONT00_EJB_MandatoComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative ai Saldi sui capitoli del Piano dei Conti
     *
     * @return SaldoComponentSession l'istanza di <code>SaldoComponentSession</code> che serve per aggiornare un saldo
     */
    private it.cnr.contab.doccont00.ejb.SaldoComponentSession createSaldoComponentSession() throws ComponentException {
        try {
            return (SaldoComponentSession) EJBCommonServices.createEJB("CNRDOCCONT00_EJB_SaldoComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * annullamento documento amm.generico
     * PreCondition:
     * E' stata generata la richiesta di annullamento di un documento generico
     * PostCondition:
     * Viene impostata la data di annullamento del documento generico e vengono aggiornati gli importi associati ai documenti
     * amministrativi delle scadenze di obbligazione e/o accertamenti che erano stati utilizzati nella contabilizzazione
     * del documento annullato
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param documento   <code>Documento_genericoBulk</code> il documento generico da annullare
     * @return documento <code>Documento_genericoBulk</code> il documento generico annullato
     */

    /* ATTENZIONE E' LO STESSO METODO DEL MANDATO */
    /* ATTENZIONE le modififiche al documento generico vengono fatte dalla stored procedure che aggiorna lo stato */
    public Documento_genericoBulk docGenerico_annullaDocumentoGenerico(UserContext userContext, Documento_genericoBulk documento) throws ComponentException {
        try {
//		documento.setDt_annullamento( getHome(userContext, Documento_genericoBulk.class).getServerTimestamp());
//		documento.setToBeUpdated();
/*
		Documento_generico_rigaBulk riga;
		Obbligazione_scadenzarioBulk os;
		Accertamento_scadenzarioBulk as;
		for ( Iterator i = documento.getDocumento_generico_dettColl().iterator(); i.hasNext(); )
		{
			riga = (Documento_generico_rigaBulk) i.next();
			if ( riga.getAccertamento_scadenziario() != null )
			{
//				as = riga.getAccertamento_scadenziario();
				as = (Accertamento_scadenzarioBulk)getHome(
							userContext,
							Accertamento_scadenzarioBulk.class ).findAndLock(
								new Accertamento_scadenzarioBulk(
									riga.getAccertamento_scadenziario().getCd_cds(),
									riga.getAccertamento_scadenziario().getEsercizio(),
									riga.getAccertamento_scadenziario().getEsercizio_originale(),
									riga.getAccertamento_scadenziario().getPg_accertamento(),
									riga.getAccertamento_scadenziario().getPg_accertamento_scadenzario()));
				AccertamentoBulk accertamento = (AccertamentoBulk) getHome( userContext, AccertamentoBulk.class ).findAndLock( as.getAccertamento());
//				accertamento.setUser( userContext.getUser());
//				updateBulk( userContext, accertamento );
				lockBulk( userContext, accertamento );
				as.setIm_associato_doc_amm( as.getIm_associato_doc_amm().subtract(riga.getIm_riga()));
				as.setUser( userContext.getUser());
				updateBulk( userContext, as );

			}
			else if ( riga.getObbligazione_scadenziario() != null )
			{
//				os = riga.getObbligazione_scadenziario();
				os = (Obbligazione_scadenzarioBulk)getHome(
							userContext,
							Obbligazione_scadenzarioBulk.class ).findAndLock(
								new Obbligazione_scadenzarioBulk(
									riga.getObbligazione_scadenziario().getCd_cds(),
									riga.getObbligazione_scadenziario().getEsercizio(),
									riga.getObbligazione_scadenziario().getEsercizio_originale(),
									riga.getObbligazione_scadenziario().getPg_obbligazione(),
									riga.getObbligazione_scadenziario().getPg_obbligazione_scadenzario()));

				ObbligazioneBulk obbligazione = (ObbligazioneBulk) getHome( userContext, ObbligazioneBulk.class ).findAndLock( os.getObbligazione());
//				obbligazione.setUser( userContext.getUser());
//				updateBulk( userContext, obbligazione);
				lockBulk( userContext, obbligazione);
				os.setIm_associato_doc_amm( os.getIm_associato_doc_amm().subtract(riga.getIm_riga()));
				os.setUser( userContext.getUser());
				updateBulk( userContext, os );
			}

		}
//		makeBulkPersistent( userContext, documento );
*/
            return documento;
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * creazione documento amm.generico
     * PreCondition:
     * E' stata generata la richiesta di creazione di un documento generico di entrata a partire
     * da una reversale di regolarizzazione o di trasferimento
     * PostCondition:
     * Un documento amm.generico viene creato
     *
     * @param userContext           lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale             <code>ReversaleBulk</code> la reversale
     * @param cd_tipo_documento_amm Il codice del tipo di documento amministrativo
     * @return documento <code>Documento_genericoBulk</code> il documento generico creato
     */
    public Documento_genericoBulk docGenerico_creaDocumentoGenerico(UserContext userContext, ReversaleBulk reversale, String cd_tipo_documento_amm) throws ComponentException {
        try {
            Documento_genericoBulk documento = new Documento_genericoBulk();
            documento.setToBeCreated();
            documento.setUser(reversale.getUser());
            documento.setTi_entrate_spese(Documento_genericoBulk.ENTRATE);
            documento.setEsercizio(reversale.getEsercizio());
            documento.setCd_cds(reversale.getCd_cds());
            documento.setCd_unita_organizzativa(reversale.getCd_unita_organizzativa());
            documento.setCd_cds_origine(reversale.getCd_cds_origine());
            documento.setCd_uo_origine(reversale.getCd_uo_origine());
            documento.setTipo_documento(new Tipo_documento_ammBulk(cd_tipo_documento_amm));
            documento.setTi_istituz_commerc(Documento_genericoBulk.ISTITUZIONALE);
            documento.setStato_cofi(Documento_genericoBulk.STATO_CONTABILIZZATO);
            documento.setStato_coge(Documento_genericoBulk.NON_REGISTRATO_IN_COGE);
            documento.setData_registrazione(reversale.getDt_emissione());
            documento.setDt_a_competenza_coge(reversale.getDt_emissione());
            documento.setDt_da_competenza_coge(reversale.getDt_emissione());
            documento.setDs_documento_generico("DOCUMENTO CREATO IN AUTOMATICO ASSOCIATO A REVERSALE DI " +
                    ((String) reversale.getTipoReversaleKeys().get(reversale.getTi_reversale())).toUpperCase());
            documento.setIm_totale(reversale.getIm_reversale());
            DivisaBulk divisa = new DivisaBulk(docGenerico_createConfigurazioneCnrComponentSession().getVal01(userContext, new Integer(0), "*", Configurazione_cnrBulk.PK_CD_DIVISA, Configurazione_cnrBulk.SK_EURO));
            documento.setValuta(divisa);
            documento.setCambio(new BigDecimal(1));
//		documento.setFl_modifica_coge(new Boolean( false));
            documento.setStato_coan(Documento_genericoBulk.NON_CONTABILIZZATO_IN_COAN);
            documento.setStato_pagamento_fondo_eco("N");
            documento.setTi_associato_manrev("T");

            return documento;
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * creazione riga di documento amm.generico di entrata
     * PreCondition:
     * E' stata generata la richiesta di creazione di una riga di documento generico di entrata
     * a partire da una reversale di trasferimento
     * PostCondition:
     * Un riga di documento amm.generico viene creata.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param documento   <code>Documento_genericoBulk</code> il documento generico
     * @param scadenza    <code>Accertamento_scadenzarioBulk</code> la scadenza dell'accertamento
     * @param mRiga       <code>Mandato_rigaBulk</code> la riga del mandato
     * @return riga <code>Documento_generico_rigaBulk</code> la riga del documento generico creata
     */

//documento generico di entrata creato alla creazione della reversale di trasferimento */
    public Documento_generico_rigaBulk docGenerico_creaDocumentoGenericoRiga(UserContext userContext, Documento_genericoBulk documento, Accertamento_scadenzarioBulk scadenza, Mandato_rigaBulk mRiga) throws ComponentException {
        try {
            Documento_generico_rigaBulk riga = new Documento_generico_rigaBulk();
            riga.setToBeCreated();
            riga.setUser(documento.getUser());
            riga.setCd_cds(documento.getCd_cds());
            riga.setCd_unita_organizzativa(documento.getCd_unita_organizzativa());
            riga.setCd_tipo_documento_amm(documento.getCd_tipo_documento_amm());
            riga.setStato_cofi(Documento_generico_rigaBulk.STATO_CONTABILIZZATO);
            riga.setDt_a_competenza_coge(documento.getData_registrazione());
            riga.setDt_da_competenza_coge(documento.getData_registrazione());
            riga.setTerzo(scadenza.getAccertamento().getDebitore());      //CNR
            AnagraficoBulk anagrafico = (AnagraficoBulk) getHome(userContext, AnagraficoBulk.class).findByPrimaryKey(riga.getTerzo().getAnagrafico());
            riga.getTerzo().setAnagrafico(anagrafico);
            riga.setRagione_sociale(anagrafico.getRagione_sociale());
            riga.setNome(anagrafico.getNome());
            riga.setCognome(anagrafico.getCognome());
            riga.setCodice_fiscale(anagrafico.getCodice_fiscale());
            riga.setPartita_iva(anagrafico.getPartita_iva());
            /* tutti duplicati perchè altrimenti non funziona */
            riga.setCd_terzo_uo_cds(mRiga.getCd_terzo());   //CDS
            riga.setTerzo_uo_cds(new TerzoBulk(mRiga.getCd_terzo()));
            riga.setCd_modalita_pag_uo_cds(mRiga.getCd_modalita_pag());
            riga.setModalita_pagamento_uo_cds(new Rif_modalita_pagamentoBulk(mRiga.getCd_modalita_pag()));
            riga.setPg_banca_uo_cds(mRiga.getPg_banca());
            riga.setBanca_uo_cds(new BancaBulk(mRiga.getCd_terzo(), mRiga.getPg_banca()));

            riga.setIm_riga(mRiga.getIm_mandato_riga());
            riga.setIm_riga_divisa(mRiga.getIm_mandato_riga());
            //ridondati altrimenti non funziona
            riga.setAccertamento_scadenziario(scadenza);
            riga.setEsercizio_accertamento(scadenza.getEsercizio());
            riga.setCd_cds_accertamento(scadenza.getCd_cds());
            riga.setEsercizio_ori_accertamento(scadenza.getEsercizio_originale());
            riga.setPg_accertamento(scadenza.getPg_accertamento());
            riga.setPg_accertamento_scadenzario(scadenza.getPg_accertamento_scadenzario());
            riga.setTi_associato_manrev("T");
            //
            riga.setDocumento_generico(documento);

            documento.getDocumento_generico_dettColl().add(riga);

            return riga;
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * creazione riga di documento amm.generico di entrata
     * PreCondition:
     * E' stata generata la richiesta di creazione di una riga di documento generico di entrata
     * a partire da una reversale di regolarizzazione
     * PostCondition:
     * Un riga di documento amm.generico viene creata.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param documento   <code>Documento_genericoBulk</code> il documento generico
     * @param scadenza    <code>Accertamento_scadenzarioBulk</code> la scadenza dell'accertamento
     * @param importo     L'importo della reversale di regolarizzazione
     * @return riga <code>Documento_generico_rigaBulk</code> la riga del documento generico creata
     */

//documento generico di entrata creato alla creazione della reversale di regolarizzazione */
    public Documento_generico_rigaBulk docGenerico_creaDocumentoGenericoRiga(UserContext userContext, Documento_genericoBulk documento, Accertamento_scadenzarioBulk scadenza, BigDecimal importo) throws ComponentException {
        try {
            Documento_generico_rigaBulk riga = new Documento_generico_rigaBulk();
            riga.setToBeCreated();
            riga.setUser(documento.getUser());
            riga.setCd_cds(documento.getCd_cds());
            riga.setCd_unita_organizzativa(documento.getCd_unita_organizzativa());
            riga.setCd_tipo_documento_amm(documento.getCd_tipo_documento_amm());
            riga.setStato_cofi(Documento_generico_rigaBulk.STATO_CONTABILIZZATO);
            riga.setDt_a_competenza_coge(documento.getData_registrazione());
            riga.setDt_da_competenza_coge(documento.getData_registrazione());
            riga.setTerzo(scadenza.getAccertamento().getDebitore());      //CNR
            AnagraficoBulk anagrafico = (AnagraficoBulk) getHome(userContext, AnagraficoBulk.class).findByPrimaryKey(riga.getTerzo().getAnagrafico());
            riga.setRagione_sociale(anagrafico.getRagione_sociale());
            riga.setNome(anagrafico.getNome());
            riga.setCognome(anagrafico.getCognome());
            riga.setCodice_fiscale(anagrafico.getCodice_fiscale());
            riga.setPartita_iva(anagrafico.getPartita_iva());
            riga.setIm_riga(importo);
            riga.setIm_riga_divisa(importo);
            //ridondati altrimenti non funziona
            riga.setAccertamento_scadenziario(scadenza);
            riga.setEsercizio_accertamento(scadenza.getEsercizio());
            riga.setCd_cds_accertamento(scadenza.getCd_cds());
            riga.setEsercizio_ori_accertamento(scadenza.getEsercizio_originale());
            riga.setPg_accertamento(scadenza.getPg_accertamento());
            riga.setPg_accertamento_scadenzario(scadenza.getPg_accertamento_scadenzario());
            riga.setTi_associato_manrev("T");

            riga.setDocumento_generico(documento);
            documento.getDocumento_generico_dettColl().add(riga);

            //aggiorno im_assciato_doc_amm della scadenza
            AccertamentoBulk accertamento = (AccertamentoBulk) getHome(userContext, AccertamentoBulk.class).findAndLock(scadenza.getAccertamento());
//		accertamento.setUser( userContext.getUser());
//		updateBulk( userContext, accertamento );
            lockBulk(userContext, accertamento);
            scadenza.setIm_associato_doc_amm(scadenza.getIm_associato_doc_amm().add(riga.getIm_riga()));
            scadenza.setUser(userContext.getUser());
            updateBulk(userContext, scadenza);
            return riga;
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * creazione riga di documento di entrata x incasso IVA da fatture estere
     * PreCondition:
     * E' stata generata la richiesta di creazione di una riga di documento amministrativo di entrata
     * a partire da una reversale di incasso IVA x fattura estera
     * PostCondition:
     * Un riga di documento amministrativo viene creata.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param documento   <code>Documento_genericoBulk</code> il documento generico
     * @param scadenza    <code>Accertamento_scadenzarioBulk</code> la scadenza dell'accertamento
     * @param importo     L'importo della reversale di regolarizzazione
     * @param mRiga       Mandato_rigaBulk da cui dedurre le modalita di pagamento
     * @return riga <code>Documento_generico_rigaBulk</code> la riga del documento generico creata
     */


    public Documento_generico_rigaBulk docGenerico_creaDocumentoGenericoRiga(UserContext userContext, Documento_genericoBulk documento, Accertamento_scadenzarioBulk scadenza, BigDecimal importo, Mandato_rigaBulk mRiga) throws ComponentException {
        try {
            Documento_generico_rigaBulk riga = docGenerico_creaDocumentoGenericoRiga(userContext, documento, scadenza, importo);
            //terzo uo
            SQLBuilder sql = getHome(userContext, TerzoBulk.class).createSQLBuilder();
            sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, documento.getCd_unita_organizzativa());
            sql.addClause("AND", "dt_canc", SQLBuilder.ISNULL, null);
            List result = getHome(userContext, TerzoBulk.class).fetchAll(sql);
            if (result == null || result.size() == 0)
                throw handleException(new ApplicationException(" Impossibile emettere la reversale: l'unità organizzativa " + documento.getCd_unita_organizzativa() + " non e' stata codificata in anagrafica"));
            TerzoBulk terzo_uo = (TerzoBulk) result.get(0);
            riga.setCd_terzo_uo_cds(terzo_uo.getCd_terzo());
            riga.setTerzo_uo_cds(terzo_uo);
            //modalità pagamento
            sql = getHome(userContext, Modalita_pagamentoBulk.class).createSQLBuilder();
            sql.addClause("AND", "cd_terzo", SQLBuilder.EQUALS, terzo_uo.getCd_terzo());
            sql.addClause("AND", "cd_terzo_delegato", SQLBuilder.ISNULL, null);
            sql.addTableToHeader("RIF_MODALITA_PAGAMENTO");
            sql.addSQLJoin("RIF_MODALITA_PAGAMENTO.CD_MODALITA_PAG", "MODALITA_PAGAMENTO.CD_MODALITA_PAG");
            sql.addSQLClause("AND", "TI_PAGAMENTO", SQLBuilder.EQUALS, Rif_modalita_pagamentoBulk.BANCARIO);
            result = getHome(userContext, Modalita_pagamentoBulk.class).fetchAll(sql);
            if (result == null || result.size() == 0)
                throw handleException(new ApplicationException(" Impossibile emettere la reversale: l'unità organizzativa " + documento.getCd_unita_organizzativa() + " non ha modalità di pagamento associate"));
            Modalita_pagamentoBulk mp = (Modalita_pagamentoBulk) result.get(0);

            riga.setCd_modalita_pag_uo_cds(mp.getCd_modalita_pag());
            riga.setModalita_pagamento_uo_cds(new Rif_modalita_pagamentoBulk(mp.getCd_modalita_pag()));

            sql = getHome(userContext, BancaBulk.class).createSQLBuilder();
            sql.addClause("AND", "cd_terzo", SQLBuilder.EQUALS, terzo_uo.getCd_terzo());
            sql.addClause("AND", "ti_pagamento", SQLBuilder.EQUALS, Rif_modalita_pagamentoBulk.BANCARIO);
            sql.addClause("AND", "fl_cancellato", SQLBuilder.EQUALS, Boolean.FALSE);
            sql.addSQLClause("AND", "CODICE_IBAN", SQLBuilder.ISNOTNULL, null);
            sql.addSQLClause("AND", "cd_terzo_delegato", SQLBuilder.ISNULL, null);

            try {
                if (!Utility.createParametriCnrComponentSession().getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext)).getFl_tesoreria_unica().booleanValue())
                    sql.addSQLClause("AND", "BANCA.FL_CC_CDS", SQLBuilder.EQUALS, "Y");
                else {
                    Configurazione_cnrBulk config = new Configurazione_cnrBulk(
                            "CONTO_CORRENTE_SPECIALE",
                            "ENTE",
                            "*",
                            new Integer(0));
                    it.cnr.contab.config00.bulk.Configurazione_cnrHome home = (it.cnr.contab.config00.bulk.Configurazione_cnrHome) getHome(userContext, config);
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
            result = getHome(userContext, BancaBulk.class).fetchAll(sql);
            if (result == null || result.size() == 0)
                throw handleException(new ApplicationException(" Impossibile emettere la reversale: l'unità organizzativa " + documento.getCd_unita_organizzativa() + " non ha coordinate bancarie associate"));
            BancaBulk banca = (BancaBulk) result.get(0);

            riga.setPg_banca_uo_cds(banca.getPg_banca());
            riga.setBanca_uo_cds(banca);

            return riga;
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * Crea la ComponentSession da usare per effettuare le operazioni relative alla Configurazione CNR
     *
     * @return Configurazione_cnrComponentSession l'istanza di <code>Configurazione_cnrComponentSession</code> che serve per leggere i parametri di configurazione del CNR
     */
    private Configurazione_cnrComponentSession docGenerico_createConfigurazioneCnrComponentSession() throws ComponentException {
        try {
            return (Configurazione_cnrComponentSession) EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * eliminazione
     * PreCondition:
     * E' stata generata la richiesta di eliminazione dell'associazione Mandato-Reversale
     * PostCondition:
     * Viene eliminato l'oggetto Ass_mandato_reversaleBulk con i dati del mandato e della reversale associata
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param ass_man_rev <code>Ass_mandato_reversaleBulk</code> l'associazione mandato-reversale da cancellare
     */
// serve x eliminare un oggetto bulk
    public void eliminaAss_mandato_reversale(UserContext userContext, Ass_mandato_reversaleBulk ass_man_rev) throws it.cnr.jada.persistency.PersistencyException, ComponentException {
        ass_man_rev.setToBeDeleted();
        makeBulkPersistent(userContext, ass_man_rev);
    }

    /**
     * eliminazione reversale provvisoria
     * PreCondition:
     * E' stata generata la richiesta di cancellare fisicamente una reversale provvisoria
     * PostCondition:
     * La reversale provvisoria coi suoi dettagli e' stata eliminata.
     * I saldi dei capitoli di entrata dell'accertamento di sistema a cui si riferisce sono stati aggiornati
     * (metodo aggiornaCapitoloSaldoRiga).
     * Vengono aggiornati lo stato della fattura (metodo aggiornaStatoFattura) e l'importo degli
     * accertamenti associati alla reversale (metodo aggiornaImportoAccertamenti).
     * eliminazione reversale definitiva
     * PreCondition:
     * E' stata generata la richiesta di eliminare una reversale definitiva
     * PostCondition:
     * La reversale viene annullata in quanto ha dei sospesi associati (metodo annullaReversale).
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> la reversale da cancellare
     */

    public void eliminaConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

        try {
            ReversaleBulk reversale = (ReversaleBulk) bulk;
            verificaStatoEsercizio(userContext, ((ReversaleBulk) bulk).getEsercizio(), ((CNRUserContext) userContext).getCd_cds());
            lockBulk(userContext, reversale);
            //REVERSALE DEFINITIVA
            if (reversale.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_REV)) {
                annullaReversale(userContext, reversale);
                return;
            }
            //REVERSALE PROVVISORIA
            reversale.setToBeDeleted();
            Reversale_rigaBulk riga;
            SaldoComponentSession session = createSaldoComponentSession();
            for (Iterator i = reversale.getReversale_rigaColl().iterator(); i.hasNext(); ) {
                riga = (Reversale_rigaBulk) i.next();
//			riga.annulla();
//			updateBulk( userContext, riga);
                aggiornaCapitoloSaldoRiga(userContext, riga, session);
            }
		/* simona 9.12.2002 commentato perchè altrimenti il doc. generico TRASF_E veniva annullato
		   e successivamente non si riusciva più ad annullare il mandato di accreditamento collegato alla reversale
		aggiornaStatoFattura( userContext, reversale, ANNULLAMENTO_REVERSALE_ACTION );
		*/
            aggiornaImportoAccertamenti(userContext, reversale);
            super.eliminaConBulk(userContext, reversale);
        } catch (Exception e) {
            throw handleException(bulk, e);

        }
    }

    /**
     * lista le coordinate bancarie
     * PreCondition:
     * E' stato creata una riga di reversale con tipologia diversa da quella di regolarizzazione e di trasferimento
     * PostCondition:
     * La lista delle coordinate bancarie del terzo debitore della reversale viene estratta
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riga        <code>Reversale_rigaIBulk</code> la riga della reversale
     * @return List la lista delle banche definite per il terzo della reversale
     */
    public List findBancaOptions(UserContext userContext, Reversale_rigaIBulk riga) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException {
        if (riga.getReversale() != null && ReversaleBulk.TIPO_REGOLARIZZAZIONE.equals(riga.getReversale().getTi_reversale()))
            return null;
        SQLBuilder sql = getHome(userContext, BancaBulk.class).createSQLBuilder();
        sql.addClause("AND", "cd_terzo", SQLBuilder.EQUALS, riga.getCd_terzo_uo());
        sql.addSQLClause("AND", "BANCA.CD_TERZO_DELEGATO", SQLBuilder.ISNULL, null);
        sql.addClause("AND", "ti_pagamento", SQLBuilder.EQUALS, riga.getBanca().getTi_pagamento());
        sql.addSQLClause("AND", "BANCA.FL_CANCELLATO", SQLBuilder.EQUALS, "N");
        return getHome(userContext, BancaBulk.class).fetchAll(sql);
    }

    /**
     * lista le modalità di pagamento
     * PreCondition:
     * E' stato creata una riga di reversale con tipologia diversa da quella di regolarizzazione e di trasferimento
     * PostCondition:
     * La lista delle modalità di pagamento del terzo debitore, tutte appartenenti alla stessa classe (Bancario/Postale/..)
     * per cui si sta emettendo la reversale, viene estratta
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riga        <code>Reversale_rigaIBulk</code> la riga della reversale
     * @return List la lista delle modalità di pagamento definite per il terzo della reversale
     */
    public List findModalita_pagamentoOptions(UserContext userContext, Reversale_rigaIBulk riga) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException {
        if (riga.getReversale() != null && ReversaleBulk.TIPO_REGOLARIZZAZIONE.equals(riga.getReversale().getTi_reversale()))
            return null;
        SQLBuilder sql = getHome(userContext, Modalita_pagamentoBulk.class).createSQLBuilder();
        sql.addTableToHeader("RIF_MODALITA_PAGAMENTO");
        sql.addSQLJoin("MODALITA_PAGAMENTO.CD_MODALITA_PAG", "RIF_MODALITA_PAGAMENTO.CD_MODALITA_PAG");
        sql.addClause("AND", "cd_terzo", SQLBuilder.EQUALS, riga.getCd_terzo_uo());
        sql.addSQLClause("AND", "MODALITA_PAGAMENTO.CD_TERZO_DELEGATO", SQLBuilder.ISNULL, null);
        if (riga.getBanca() == null)
            throw new ApplicationException("Reversale: " + riga.getPg_reversale() + " Esercizio: " + riga.getEsercizio() + " Cds: " + riga.getCd_cds() + " - Non esistono le coordinate bancarie! ");
        sql.addSQLClause("AND", "RIF_MODALITA_PAGAMENTO.TI_PAGAMENTO", SQLBuilder.EQUALS, riga.getBanca().getTi_pagamento());
        return getHome(userContext, Modalita_pagamentoBulk.class).fetchAll(sql);
    }

    /**
     * lista le tipologie di bollo
     * PreCondition:
     * E' stato creata una riga di reversale  di trasferimento
     * PostCondition:
     * La lista delle tipologie di bollo di tipo entrata viene estratta
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale   <code>ReversaleBulk</code> la reversale
     * @return List la lista dei tipi di bollo definiti per la reversale
     */
    public List findTipoBolloOptions(UserContext userContext, ReversaleBulk reversale) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException {
        SQLBuilder sql = getHome(userContext, Tipo_bolloBulk.class).createSQLBuilder();
        sql.addClause("AND", "ti_entrata_spesa", SQLBuilder.NOT_EQUALS, Tipo_bolloBulk.TIPO_SPESA);
        sql.addOrderBy("cd_tipo_bollo");
        return getHome(userContext, Tipo_bolloBulk.class).fetchAll(sql);
    }

    /**
     * ricerca uo a cui assegnare la reversale per cds diverso da SAC
     * PreCondition:
     * E' stato generata la richiesta di creazione di una reversale di trasferimento
     * Il Cds beneficiario dell'accreditamento è diverso dal SAC
     * PostCondition:
     * L'UO-cds del Cds beneficiario dell'accreditamento viene restituito
     * <p>
     * ricerca uo a cui assegnare la reversale per cds uguale a SAC
     * PreCondition:
     * E' stato generata la richiesta di creazione di una reversale di trasferimento
     * Il Cds beneficiario dell'accreditamento è uguale al SAC
     * PostCondition:
     * Viene letta da Configurazione CNR il codice dell'uo per l'accreditamento SAC e vengono resituiti
     * i dati di tale UO
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param mandato     <code>MandatoAccreditamentoBulk</code> il mandato di accreditamento da generare
     * @return Unita_organizzativaBulk l'uo a cui assegnare la reversale di trasferimento
     */
    private Unita_organizzativaBulk findUnita_organizzativa(UserContext userContext, MandatoAccreditamentoBulk mandato) throws ComponentException {
        try {
            //verifico se è il cds SAC
            boolean isSac = false;
            SQLBuilder sql = getHome(userContext, CdsBulk.class).createSQLBuilder();
            sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, mandato.getCodice_cds());
            sql.addSQLClause("AND", "CD_TIPO_UNITA", SQLBuilder.EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_SAC);
            List result = getHome(userContext, CdsBulk.class).fetchAll(sql);
            if (result.size() != 0)
                isSac = true;

            if (!isSac) {
                //cerco l'uo-cds
                sql = getHome(userContext, Unita_organizzativaBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "CD_UNITA_PADRE", SQLBuilder.EQUALS, mandato.getCodice_cds());
                sql.addSQLClause("AND", "FL_UO_CDS", SQLBuilder.EQUALS, "Y");
                result = getHome(userContext, Unita_organizzativaBulk.class).fetchAll(sql);
                if (result.size() != 1)
                    throw new ApplicationException("Non è possibile identificare l'uo-cds per il Cds " + mandato.getCodice_cds());
                return ((Unita_organizzativaBulk) result.get(0));
            } else {
                //cerco l'uo in configurazione CNR
                String cdUo = Optional.ofNullable(((Configurazione_cnrHome) getHome(userContext, Configurazione_cnrBulk.class)).getUoAccreditamentoSac(CNRUserContext.getEsercizio(userContext)))
                        .orElseThrow(() -> new ApplicationException("Configurazione CNR: manca la definizione dell'UO_SPECIALE per ACCREDITAMENTO SAC per l'esercizio " + CNRUserContext.getEsercizio(userContext) + "."));
                return (Unita_organizzativaBulk) getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdUo));
            }

        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * lista le unità organizzative - scrivania = Ente
     * PreCondition:
     * E' stata richiesta una lista delle unità organizzative per cui è possibile emettere una reversale e
     * l'unità organizzativa di scrivania e' l'UO Ente
     * PostCondition:
     * Una lista comprendente solo l'UO Ente viene restituita
     * lista le unità organizzative - scrivania diversa da UO Ente
     * PreCondition:
     * E' stata richiesta una lista delle unità organizzative per cui è possibile emettere una reversale e
     * l'unità organizzativa di scrivania e' diversa dall'UO Ente
     * PostCondition:
     * Una lista comprendente l'UO Ente e l'UO di scrivania viene restituita
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale   <code>ReversaleBulk</code> la reversale
     * @return List la lista delle unità organizzative definite per la reversale
     */
    public List findUnita_organizzativaOptions(UserContext userContext, ReversaleBulk reversale) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException {
        SQLBuilder sql = getHome(userContext, Unita_organizzativa_enteBulk.class).createSQLBuilder();
        List result = getHome(userContext, Unita_organizzativa_enteBulk.class).fetchAll(sql);
        reversale.setCd_uo_ente(((Unita_organizzativaBulk) result.get(0)).getCd_unita_organizzativa());
        if (!reversale.getCd_uo_ente().equals(((CNRUserContext) userContext).getCd_unita_organizzativa())) {
            Unita_organizzativaBulk uo = (Unita_organizzativaBulk) getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(reversale.getCd_uo_origine()));
            result.add(uo);
        }
        return result;
    }

    /**
     * Validazione dell'oggetto in fase di stampa
     */
    private Timestamp getDataOdierna(it.cnr.jada.UserContext userContext) throws ComponentException {

        try {
            return getHome(userContext, MandatoIBulk.class).getServerDate();
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    /**
     * inizializzazione di una istanza di ReversaleBulk
     * PreCondition:
     * E' stata richiesta l'inizializzazione di una istanza di ReversaleBulk
     * PostCondition:
     * Viene impostata la data di emissione della reversale con la data del Server
     *
     * @param aUC  lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk <code>OggettoBulk</code> la reversale da inizializzare per l'inserimento
     * @return bulk <code>OggettoBulk</code> la Reversale inizializzata per l'inserimento
     */
    public OggettoBulk inizializzaBulkPerInserimento(UserContext aUC, OggettoBulk bulk) throws ComponentException {

        try {
            ReversaleBulk reversale = (ReversaleBulk) bulk;
            Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(aUC, Unita_organizzativa_enteBulk.class).findAll().get(0);
            reversale.setUnita_organizzativa(ente);
            reversale.setCd_cds(ente.getCd_unita_padre());
            verificaStatoEsercizio(aUC, reversale.getEsercizio(), reversale.getCd_cds_origine());
            reversale.setDt_emissione(DateServices.getDt_valida(aUC));
            return super.inizializzaBulkPerInserimento(aUC, reversale);
        } catch (Exception e) {
            throw handleException(bulk, e);
        }
    }

    /**
     * inizializzazione di una istanza di ReversaleBulk per modifica
     * PreCondition:
     * E' stata richiesta l'inizializzazione di una istanza di ReversaleBulk
     * PostCondition:
     * Viene caricata la collezione delle righe di reversale (Reversale_rigaBulk), dei sospesi associati alla reversale (Sospeso_det_etrBulk),
     * delle associazioni mandato-reversale( Ass_mandato_reversaleBulk). Vengono caricati i dati del terzo della reversale (Reversale_terzoBulk)
     * e viene verificato il tipo bollo (metodo verificaTipoBollo)
     *
     * @param aUC  lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk <code>OggettoBulk</code> la reversale da inizializzare per la modifica
     * @return reversale la Reversale inizializzata per la modifica
     */
/*
1 - carica le reversali riga
2 - carica la reversale terzo
*/
    public OggettoBulk inizializzaBulkPerModifica(UserContext aUC, OggettoBulk bulk) throws ComponentException {
        ReversaleBulk reversale = (ReversaleBulk) super.inizializzaBulkPerModifica(aUC, bulk);
        try {
            //carico le reversali riga
            Reversale_rigaBulk riga;
            reversale.setReversale_rigaColl(new BulkList(((ReversaleHome) getHome(aUC, reversale.getClass())).findReversale_riga(aUC, reversale)));
            for (Iterator i = reversale.getReversale_rigaColl().iterator(); i.hasNext(); ) {
                riga = (Reversale_rigaBulk) super.inizializzaBulkPerModifica(aUC, (Reversale_rigaBulk) i.next());
                //Carico automaticamente i codici SIOPE e visualizzo quelli ancora collegabili se la gestione è attiva
                if (Utility.createParametriCnrComponentSession().getParametriCnr(aUC, reversale.getEsercizio()).getFl_siope().booleanValue()) {
                    riga.setReversale_siopeColl(new BulkList(((Reversale_rigaHome) getHome(aUC, Reversale_rigaBulk.class)).findCodiciCollegatiSIOPE(aUC, riga)));
                    setCodiciSIOPECollegabili(aUC, riga);
                }


//			if (Utility.createParametriCnrComponentSession().getParametriCnr(aUC, reversale.getEsercizio()).getFl_cup().booleanValue() &&
//					Utility.createParametriCnrComponentSession().getParametriCnr(aUC, reversale.getEsercizio()).getFl_siope_cup().booleanValue()){
//				Timestamp dataLimite=Utility.createConfigurazioneCnrComponentSession().getDt01(aUC, "DATA_LIMITE_CUP_SIOPE_CUP");
//				if(reversale.getDt_emissione().after(dataLimite)){
//					for (Iterator j=riga.getReversale_siopeColl().iterator();j.hasNext();){
//						Reversale_siopeIBulk rigaSiope = (Reversale_siopeIBulk)j.next();
//						rigaSiope.setReversaleSiopeCupColl(new BulkList(((Reversale_siopeHome) getHome( aUC,Reversale_siopeBulk.class)).findCodiciSiopeCupCollegati(aUC, rigaSiope)));
//					}
//				}else
//				{
//					riga.setReversaleCupColl(new BulkList(((Reversale_rigaHome) getHome( aUC, Reversale_rigaBulk.class)).findCodiciCupCollegati(aUC, riga)));
//				}
//
//			}else{
                if (Utility.createParametriCnrComponentSession().getParametriCnr(aUC, reversale.getEsercizio()).getFl_cup().booleanValue()) {
                    riga.setReversaleCupColl(new BulkList(((Reversale_rigaHome) getHome(aUC, Reversale_rigaBulk.class)).findCodiciCupCollegati(aUC, riga)));
                } else {
                    if (Utility.createParametriCnrComponentSession().getParametriCnr(aUC, reversale.getEsercizio()).getFl_siope_cup().booleanValue()) {
                        for (Iterator j = riga.getReversale_siopeColl().iterator(); j.hasNext(); ) {
                            Reversale_siopeIBulk rigaSiope = (Reversale_siopeIBulk) j.next();
                            rigaSiope.setReversaleSiopeCupColl(new BulkList(((Reversale_siopeHome) getHome(aUC, Reversale_siopeBulk.class)).findCodiciSiopeCupCollegati(aUC, rigaSiope)));
                        }
                    }
                }
//			}

                inizializzaTi_fattura(aUC, riga);
                ((Reversale_rigaHome) getHome(aUC, riga.getClass())).initializeElemento_voce(aUC, riga);
            }

            //carico la reversale terzo
            reversale.setReversale_terzo(((ReversaleHome) getHome(aUC, reversale.getClass())).findReversale_terzo(aUC, reversale));
            verificaTipoBollo(aUC, reversale);

            //carico i sospeso_det_etr
            Sospeso_det_etrBulk sde;
            reversale.setSospeso_det_etrColl(new BulkList(((ReversaleHome) getHome(aUC, reversale.getClass())).findSospeso_det_etr(aUC, reversale)));

            //aggiungo nella deleteList i sospesi annullati
            for (Iterator i = reversale.getSospeso_det_etrColl().iterator(); i.hasNext(); ) {
                sde = (Sospeso_det_etrBulk) i.next();
                sde.setReversale(reversale);
                if (sde.getStato().equals(Sospeso_det_etrBulk.STATO_ANNULLATO))
                    i.remove();
            }

            // carico il cd uo ente
            SQLBuilder sql = getHome(aUC, Unita_organizzativa_enteBulk.class).createSQLBuilder();
            List result = getHome(aUC, Unita_organizzativa_enteBulk.class).fetchAll(sql);
            reversale.setCd_uo_ente(((Unita_organizzativa_enteBulk) result.get(0)).getCd_unita_organizzativa());

            // carico i mandati associati alla reversale
/*		sql = getHome( aUC, Ass_mandato_reversaleBulk.class ).createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS, reversale.getEsercizio() );
		sql.addClause("AND","cd_cds",sql.EQUALS, reversale.getCds().getCd_unita_organizzativa() );
		sql.addClause("AND","pg_reversale",sql.EQUALS, reversale.getPg_reversale() );
		result = getHome( aUC, Ass_mandato_reversaleBulk.class ).fetchAll( sql );
		if ( result.size() == 0 )
			throw new ApplicationException("Non esiste associazione fra mandati e reversali");
		reversale.setMandatiColl( new BulkList(result) );
*/
            reversale.setMandatiColl(new BulkList(((Ass_mandato_reversaleHome) getHome(aUC, Ass_mandato_reversaleBulk.class)).findMandati(aUC, reversale)));

            // carico i doc. contabili (mandati/reversali) associati alla reversale
            reversale.setDoc_contabili_collColl(((V_ass_doc_contabiliHome) getHome(aUC, V_ass_doc_contabiliBulk.class)).findDoc_contabili_coll(reversale));

            if (reversale.getPg_reversale_riemissione() != null) {
                V_mandato_reversaleBulk man_rev = (V_mandato_reversaleBulk) getHome(aUC, V_mandato_reversaleBulk.class).findByPrimaryKey(new V_mandato_reversaleBulk(reversale.getEsercizio(), Numerazione_doc_contBulk.TIPO_REV, reversale.getCd_cds_origine(), reversale.getPg_reversale_riemissione()));
                if (man_rev != null)
                    reversale.setV_man_rev(man_rev);
                else
                    man_rev = (V_mandato_reversaleBulk) getHome(aUC, V_mandato_reversaleBulk.class).findByPrimaryKey(new V_mandato_reversaleBulk(reversale.getEsercizio(), Numerazione_doc_contBulk.TIPO_REV, reversale.getCd_cds(), reversale.getPg_reversale_riemissione()));
                if (man_rev != null)
                    reversale.setV_man_rev(man_rev);
            }
        } catch (Exception e) {
            throw handleException(reversale, e);
        }
        return reversale;

    }

    /**
     * inizializzazione di una istanza di ReversaleBulk per ricerca
     * PreCondition:
     * E' stata richiesta l'inizializzazione di una istanza di ReversaleBulk per ricerca
     * PostCondition:
     * Viene inizializzata l'istanza di ReversaleBulk
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> la reversale da inizializzare per la ricerca
     * @return reversale la Reversale inizializzata per la ricerca
     */
    public OggettoBulk inizializzaBulkPerRicerca(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            ReversaleBulk reversale = (ReversaleBulk) bulk;
            Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
            if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals(ente.getCd_unita_organizzativa()))
                //se in scrivania ho uo diversa da ente
                reversale.setCd_uo_origine(((CNRUserContext) userContext).getCd_unita_organizzativa());
            reversale.setUnita_organizzativa(ente);
            reversale = (ReversaleBulk) super.inizializzaBulkPerRicerca(userContext, bulk);
            return reversale;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * inizializzazione di una istanza di ReversaleBulk per ricerca libera
     * PreCondition:
     * E' stata richiesta l'inizializzazione di una istanza di ReversaleBulk per ricerca libera
     * PostCondition:
     * Viene inizializzata l'istanza di ReversaleBulk
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> la reversale da inizializzare per la ricerca
     * @return reversale la Reversale inizializzata per la ricerca
     */
    public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        return inizializzaBulkPerRicerca(userContext, bulk);
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    private void inizializzaBulkPerStampa(UserContext userContext, Stampa_giornale_reversaliBulk stampa) throws it.cnr.jada.comp.ComponentException {

        stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));
        stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));

        stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
        stampa.setDataFine(getDataOdierna(userContext));
        stampa.setPgInizio(new Long(0));
        stampa.setPgFine(new Long("9999999999"));

        stampa.setUnita_organizzativa(new Unita_organizzativaBulk());

        String cd_uo = CNRUserContext.getCd_unita_organizzativa(userContext);

        try {
            Unita_organizzativaHome uoHome = (Unita_organizzativaHome) getHome(userContext, Unita_organizzativaBulk.class);
            Unita_organizzativaBulk uo = (Unita_organizzativaBulk) uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo));

            if (!uo.isUoCds()) {
                stampa.setUoEmittenteForPrint(uo);
                stampa.setFindUOForPrintEnabled(false);
            } else {
                stampa.setUoEmittenteForPrint(new Unita_organizzativaBulk());
                stampa.setFindUOForPrintEnabled(true);
            }

        } catch (it.cnr.jada.persistency.PersistencyException pe) {
            throw new ComponentException(pe);
        }

        stampa.setTi_reversale(Stampa_giornale_reversaliBulk.TIPO_TUTTI);
        stampa.setStato(Stampa_giornale_reversaliBulk.STATO_REVERSALE_TUTTI);
        stampa.setStato_trasmissione(Stampa_giornale_reversaliBulk.STATO_TRASMISSIONE_TUTTI);
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    private void inizializzaBulkPerStampa(UserContext userContext, Stampa_vpg_reversaleBulk stampa) throws it.cnr.jada.comp.ComponentException {

        stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));
        stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));

        stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
        stampa.setDataFine(getDataOdierna(userContext));
        stampa.setPgInizio(new Long(0));
        stampa.setPgFine(new Long("9999999999"));

        stampa.setTerzoForPrint(new TerzoBulk());
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

        if (bulk instanceof Stampa_giornale_reversaliBulk)
            inizializzaBulkPerStampa(userContext, (Stampa_giornale_reversaliBulk) bulk);
        else if (bulk instanceof Stampa_vpg_reversaleBulk)
            inizializzaBulkPerStampa(userContext, (Stampa_vpg_reversaleBulk) bulk);

        return bulk;
    }

    /**
     * inizializzazione dell'attributo ti_fattura di una istanza di Reversale_rigaBulk
     * PreCondition:
     * E' stata richiesta l'inizializzazione di una istanza di Reversale_rigaBulk
     * La riga della reversale e' relativa ad un documento amministrativo di tipo fattura (attiva o passiva)
     * PostCondition:
     * Il tipo fattura ( fattura, nota a credito, nota a debito) e' stato caricato per la riga della reversale
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riga        <code>Reversale_rigaBulk</code> la riga della reversale da inizializzare
     */
    private void inizializzaTi_fattura(UserContext userContext, Reversale_rigaBulk riga) throws SQLException, ComponentException {
        if (riga.getCd_tipo_documento_amm() != null &&
                (riga.getCd_tipo_documento_amm().equals(it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA)))
            ((Reversale_rigaHome) getHome(userContext, riga.getClass())).initializeTi_fatturaPerFattura(riga, "FATTURA_ATTIVA");
        else if (riga.getCd_tipo_documento_amm() != null &&
                (riga.getCd_tipo_documento_amm().equals(it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA)))
            ((Reversale_rigaHome) getHome(userContext, riga.getClass())).initializeTi_fatturaPerFattura(riga, "FATTURA_PASSIVA");

    }
/* per le date salvate nel database come timestamp bisogna ridefinire la query nel modo seguente:
		TRUNC( dt_nel_db) operator 'GG/MM/YYYY'
*/

    /**
     * ricerca documenti attivi
     * PreCondition:
     * E' stata richiesta la ricerca dei documenti attivi per cui e' possibile emettere una reversale
     * PostCondition:
     * Vengono ricercati tutti i documenti attivi che verificano le seguenti condizioni:
     * - cds e uo origine uguali a cds e uo di scrivania
     * - cds di appartenenza uguale al cds per cui si vuole emettere la reversale
     * - (im_scadenza-im_associato_doc_contabile) della scadenza di accertamento su cui il documento amm.
     * e' stato contabilizzato maggiore di zero
     * Fra tutti i documenti individuati vengono esclusi quelli che eventualmente sono già stati selezionati
     * per questa reversale
     *
     * @param aUC       lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale <code>ReversaleBulk</code> la reversale
     * @return reversale la Reversale aggiornata dopo la ricerca dei documenti attivi
     */
    public ReversaleBulk listaDocAttivi(UserContext aUC, ReversaleBulk reversale) throws ComponentException {

        Reversale_rigaIBulk riga;
        V_doc_attivo_accertamentoBulk docAttivo;
        try {
            Collection result = ((ReversaleIHome) getHome(aUC, reversale.getClass())).findDocAttivi((ReversaleIBulk) reversale, (it.cnr.contab.utenze00.bp.CNRUserContext) aUC);
            if (result.size() == 0)
                throw new ApplicationException("La ricerca non ha fornito alcun risultato.");

            //elimino dal risultato i doc attivi già selezionati per questa reversale

            for (Iterator j = result.iterator(); j.hasNext(); ) {
                docAttivo = (V_doc_attivo_accertamentoBulk) j.next();
                docAttivo.setTipoDocumentoKeys((Hashtable) reversale.getTipoDocumentoKeys());
                for (Iterator i = reversale.getReversale_rigaColl().iterator(); i.hasNext(); ) {
                    riga = (Reversale_rigaIBulk) i.next();

                    if (docAttivo.getEsercizio_ori_accertamento().intValue() == riga.getEsercizio_ori_accertamento().intValue() &&
                            docAttivo.getPg_accertamento().longValue() == riga.getPg_accertamento().longValue() &&
                            docAttivo.getPg_accertamento_scadenzario().longValue() == riga.getPg_accertamento_scadenzario().longValue() &&
                            docAttivo.getEsercizio_accertamento().intValue() == riga.getEsercizio_accertamento().intValue() &&
                            docAttivo.getCd_cds_accertamento().equals(riga.getCd_cds()) &&
                            docAttivo.getEsercizio().intValue() == riga.getEsercizio_doc_amm().intValue() &&
                            docAttivo.getCd_unita_organizzativa().equals(riga.getCd_uo_doc_amm()) &&
                            docAttivo.getCd_cds().equals(riga.getCd_cds_doc_amm()) &&
                            docAttivo.getCd_tipo_documento_amm().equals(riga.getCd_tipo_documento_amm()) &&
                            docAttivo.getPg_documento_amm().longValue() == riga.getPg_doc_amm().longValue())
                        j.remove();
                }
            }

            ((ReversaleIBulk) reversale).setDocAttiviColl(result);
            return reversale;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(reversale, e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(reversale, e);
        }
    }

    /**
     * modifica reversale
     * PreCondition:
     * E' stata generata la richiesta di modifica di una Reversale (TIPO_REV) e la reversale supera la validazione
     * (metodo verificaReversale)
     * PostCondition:
     * Vengono aggiornati gli importi dei sospesi eventualmente associati alla reversale (metodo aggiornaImportoSospesi)
     * e vengono aggiornate le eventuali modifiche alle modalità di pagamento e al tipo bollo della reversale
     * modifica reversale provvisoria
     * PreCondition:
     * E' stata generata la richiesta di modifica di una Reversale provvisoria (TIPO_REV_PROVV) e la reversale supera la
     * validazione (metodo verificaReversale)
     * PostCondition:
     * Viene eliminata la precedente associazione tra reversale e mandati (metodo eliminaAss_mandato_reversale),
     * viene eliminata la reversale provvisoria. Vengono quindi create una nuova reversale definitiva coi relativi
     * sospesi associati (metodo creaReversaleDefinitiva), e la nuova associazione reversale/mandato
     * (metodo creaAss_mandato_reversale).
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> la reversale da modificare
     * @return bulk <code>OggettoBulk</code> la Reversale modificata
     */
/*
1 - verifica reversale
2 - inserisce record reversale, terzo, riga
3 - per le righe aggiunte o cancellate aggiorna accertamento_scadenzario per im_associato_doc_contabili
4 - per le righe aggiunte o cancellate aggiorna stato cofi della fattura
5 - aggiorna saldi per delta im_incassato
*/
    public OggettoBulk modificaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {

            ReversaleBulk reversale = (ReversaleBulk) bulk;
            verificaStatoEsercizio(userContext, ((ReversaleBulk) bulk).getEsercizio(), ((CNRUserContext) userContext).getCd_cds());
            lockBulk(userContext, reversale);
            if (!reversale.isAnnullato()) {
                reversale.refreshImporto();
                verificaReversale(userContext, reversale, false);
            }
            // verifico se si tratta di una reversale provvisoria o definitiva
            if (reversale.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_REV_PROVV)) {
                boolean hasAssociazione = true;
                String cd_cds_man = null;
                Integer esercizio_man = null;
                Long pg_man = null;

                if (reversale.getSospeso_det_etrColl() != null && !reversale.getSospeso_det_etrColl().isEmpty()) {
                    if (reversale.getMandatiColl().size() == 0)
                        hasAssociazione = false;
                    if (hasAssociazione) {
                        cd_cds_man = ((Ass_mandato_reversaleBulk) reversale.getMandatiColl().get(0)).getCd_cds_mandato();
                        esercizio_man = ((Ass_mandato_reversaleBulk) reversale.getMandatiColl().get(0)).getEsercizio_mandato();
                        pg_man = ((Ass_mandato_reversaleBulk) reversale.getMandatiColl().get(0)).getPg_mandato();
                        eliminaAss_mandato_reversale(userContext, (Ass_mandato_reversaleBulk) reversale.getMandatiColl().get(0));
                    }
                    eliminaConBulk(userContext, reversale);
                    ReversaleBulk reversaleDef = creaReversaleDefinitiva(userContext, reversale);
                    if (hasAssociazione) {
                        Ass_mandato_reversaleBulk ass_man_rev = creaAss_mandato_reversale(userContext, reversaleDef, cd_cds_man, esercizio_man, pg_man);
                        reversaleDef.getMandatiColl().add(ass_man_rev);
                    }
                    return reversaleDef;
                } else
                    throw handleException(new ApplicationException("Non e' possibile salvare una reversale provvisoria senza aver specificato i sospesi"));
            }

            aggiornaImportoSospesi(userContext, reversale);
		/*
		Reversale_rigaBulk riga;
		SaldoComponentSession session = createSaldoComponentSession();
		//itera su tutte le righe
		for ( Iterator i = reversale.getReversale_rigaColl().iterator(); i.hasNext(); )
		{
			riga = (Reversale_rigaBulk) i.next();
			aggiornaImportoAccertamento(userContext, riga );

			aggiornaCapitoloSaldoRiga( userContext, riga, session );
		}
		aggiornaStatoFattura( userContext, mandato );
		*/
            reversale = (ReversaleBulk) super.modificaConBulk(userContext, reversale);
            return bulk;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    protected void ridefinisciClausoleConTimestamp(UserContext userContext, CompoundFindClause clauses) {
        SimpleFindClause clause;
        for (Iterator i = clauses.iterator(); i.hasNext(); ) {
            clause = (SimpleFindClause) i.next();
            if (clause.getPropertyName().equalsIgnoreCase("dt_trasmissione") ||
                    clause.getPropertyName().equalsIgnoreCase("dt_annullamento") ||
                    clause.getPropertyName().equalsIgnoreCase("dt_ritrasmissione"))
                if (clause.getOperator() == SQLBuilder.ISNOTNULL || clause.getOperator() == SQLBuilder.ISNULL)
                    clause.setSqlClause("TRUNC( " + clause.getPropertyName() + ") " + SQLBuilder.getSQLOperator(clause.getOperator()));
                else
                    clause.setSqlClause("TRUNC( " + clause.getPropertyName() + ") " + SQLBuilder.getSQLOperator(clause.getOperator()) + " ? ");
        }


    }

    /**
     * ricerca di una reversale
     * PreCondition:
     * E' stata richiesta la ricerca di una reversale
     * PostCondition:
     * E' stato creato il SQLBuilder con le clausole implicite (presenti nell'istanza di ReversaleBulk) su esercizio e uo origine
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param clauses     <code>CompoundFindClause</code> le clausole della selezione
     * @param bulk        <code>OggettoBulk</code> la reversale
     * @return sql <code>Query</code> Risultato della selezione.
     */
    protected Query select(UserContext userContext, CompoundFindClause clauses, OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        /* COMPORTAMENTO DI DEFAULT - INIZIO */
        if (clauses == null) {
            if (bulk != null)
                clauses = bulk.buildFindClauses(null);
        } else
            clauses = it.cnr.jada.persistency.sql.CompoundFindClause.and(clauses, bulk.buildFindClauses(Boolean.FALSE));
        /* COMPORTAMENTO DI DEFAULT - FINE */

        if (clauses != null)
            ridefinisciClausoleConTimestamp(userContext, clauses);


        SQLBuilder sql = getHome(userContext, bulk).selectByClause(clauses);
        if (((CNRUserContext) userContext).getCd_unita_organizzativa().equals(((ReversaleBulk) bulk).getCd_uo_ente()))
            sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, ((ReversaleBulk) bulk).getCd_uo_ente());
//	verificaStatoEsercizio( userContext, ((ReversaleBulk)bulk).getEsercizio(), ((CNRUserContext)userContext).getCd_cds() );
        return sql;
    }

    /**
     * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
     * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
     * <p>
     * Nome: Richiesta di ricerca di una Unita Organizzativa
     * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
     * Post: Viene restituito l'SQLBuilder per filtrare le UO
     * in base al cds di scrivania
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk        l'OggettoBulk che rappresenta il contesto della ricerca.
     * @param uo          l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
     *                    costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
     * @param clauses     L'albero logico delle clausole da applicare alla ricerca
     * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
     * della query.
     **/
    public SQLBuilder selectUoEmittenteForPrintByClause(UserContext userContext, Stampa_giornale_reversaliBulk bulk, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {

        Unita_organizzativaHome home = (Unita_organizzativaHome) getHome(userContext, Unita_organizzativaBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "cd_unita_padre", SQLBuilder.EQUALS, bulk.getCd_cds());
        sql.addClause(clauses);
        return sql;
    }

    /**
     * stampaConBulk method comment.
     */
    public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

        if (bulk instanceof Stampa_giornale_reversaliBulk)
            validateBulkForPrint(aUC, (Stampa_giornale_reversaliBulk) bulk);
        else if (bulk instanceof Stampa_vpg_reversaleBulk)
            validateBulkForPrint(aUC, (Stampa_vpg_reversaleBulk) bulk);

        return bulk;
    }

    /**
     * Validazione dell'oggetto in fase di stampa
     */
    private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_giornale_reversaliBulk stampa) throws ComponentException {

        try {
            Timestamp dataOdierna = getDataOdierna(userContext);

            if (stampa.getEsercizio() == null)
                throw new ValidationException("Il campo ESERCIZIO e' obbligatorio");
            if (stampa.getCd_cds() == null)
                throw new ValidationException("Il campo CDS e' obbligatorio");

            if (stampa.getDataInizio() == null)
                throw new ValidationException("Il campo DATA INIZIO PERIODO è obbligatorio");
            if (stampa.getDataFine() == null)
                throw new ValidationException("Il campo DATA FINE PERIODO è obbligatorio");

            java.sql.Timestamp firstDayOfYear = DateServices.getFirstDayOfYear(stampa.getEsercizio().intValue());
            if (stampa.getDataInizio().compareTo(stampa.getDataFine()) > 0)
                throw new ValidationException("La DATA di INIZIO PERIODO non può essere superiore alla DATA di FINE PERIODO");
            if (stampa.getDataInizio().compareTo(firstDayOfYear) < 0) {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
                throw new ValidationException("La DATA di INIZIO PERIODO non può essere inferiore a " + formatter.format(firstDayOfYear));
            }
            if (stampa.getDataFine().compareTo(dataOdierna) > 0) {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
                throw new ValidationException("La DATA di FINE PERIODO non può essere superiore a " + formatter.format(dataOdierna));
            }

            if (stampa.getPgInizio() == null)
                throw new ValidationException("Il campo NUMERO INIZIO REVERSALE è obbligatorio");
            if (stampa.getPgFine() == null)
                throw new ValidationException("Il campo NUMERO FINE REVERSALE è obbligatorio");
            if (stampa.getPgInizio().compareTo(stampa.getPgFine()) > 0)
                throw new ValidationException("Il NUMERO INIZIO REVERSALE non può essere superiore al NUMERO FINE REVERSALE");

            //if (stampa.getCdUOEmittenteForPrint()==null)
            //throw new ValidationException("Il campo UO EMITTENTE è obbligatorio");

        } catch (ValidationException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Validazione dell'oggetto in fase di stampa
     */
    private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_vpg_reversaleBulk stampa) throws ComponentException {

        try {
            Timestamp dataOdierna = getDataOdierna(userContext);

            if (stampa.getEsercizio() == null)
                throw new ValidationException("Il campo ESERCIZIO e' obbligatorio");
            if (stampa.getCd_cds() == null)
                throw new ValidationException("Il campo CDS e' obbligatorio");

            if (stampa.getDataInizio() == null)
                throw new ValidationException("Il campo DATA INIZIO PERIODO è obbligatorio");
            if (stampa.getDataFine() == null)
                throw new ValidationException("Il campo DATA FINE PERIODO è obbligatorio");

            java.sql.Timestamp firstDayOfYear = DateServices.getFirstDayOfYear(stampa.getEsercizio().intValue());
            if (stampa.getDataInizio().compareTo(stampa.getDataFine()) > 0)
                throw new ValidationException("La DATA di INIZIO PERIODO non può essere superiore alla DATA di FINE PERIODO");
            if (stampa.getDataInizio().compareTo(firstDayOfYear) < 0) {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
                throw new ValidationException("La DATA di INIZIO PERIODO non può essere inferiore a " + formatter.format(firstDayOfYear));
            }
            if (stampa.getDataFine().compareTo(dataOdierna) > 0) {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
                throw new ValidationException("La DATA di FINE PERIODO non può essere superiore a " + formatter.format(dataOdierna));
            }

            if (stampa.getPgInizio() == null)
                throw new ValidationException("Il campo NUMERO INIZIO MANDATO è obbligatorio");
            if (stampa.getPgFine() == null)
                throw new ValidationException("Il campo NUMERO FINE MANDATO è obbligatorio");
            if (stampa.getPgInizio().compareTo(stampa.getPgFine()) > 0)
                throw new ValidationException("Il NUMERO INIZIO MANDATO non può essere superiore al NUMERO FINE MANDATO");

        } catch (ValidationException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * verifica reversale - errore modalità di pagamento
     * PreCondition:
     * E' stata richiesta la creazione/modifica di una reversale
     * Le righe della reversale hanno come tipo di pagamento BANCARIO o POSTALE o QUIETANZA o ALTRO
     * Le righe della reversale hanno modalità di pagamento differenti
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare la reversale
     * verifica reversale - errore mod. pagamento bancario
     * PreCondition:
     * E' stata richiesta la creazione/modifica di una reversale
     * Le righe della reversale hanno come tipo di pagamento BANCARIO
     * Le righe della reversale hanno coordinate bancarie (abi, cab, nr conto) differenti
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare la reversale
     * verifica reversale - errore mod. pagamento postale
     * PreCondition:
     * E' stata richiesta la creazione/modifica di una reversale
     * Le righe della reversale hanno come tipo di pagamento POSTALE
     * Le righe della reversale hanno coordinate postali ( nr conto ) differenti
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare la reversale
     * verifica reversale - errore mod. pagamento quietanza
     * PreCondition:
     * E' stata richiesta la creazione/modifica di una reversale
     * Le righe della reversale hanno come tipo di pagamento QUIETANZA
     * Le righe della reversale hanno quietanze differenti
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare la reversale
     * verifica reversale - errore mod. pagamento quietanza
     * PreCondition:
     * E' stata richiesta la creazione/modifica di una reversale
     * Le righe della reversale hanno come tipo di pagamento ALTRO
     * Le righe della reversale hanno intestazioni differenti
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare la reversale
     * verifica reversale - ok
     * PreCondition:
     * E' stata richiesta la creazione/modifica di una reversale
     * Le righe della reversale hanno la stesse coordinate di pagamento
     * PostCondition:
     * La reversale ha superato la validazione e può pertanto essere salvata
     *
     * @param aUC       lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale <code>ReversaleBulk</code> la reversale di cui si verifica la correttezza
     */

    private void verificaModalitaPagamento(UserContext aUC, ReversaleBulk reversale) throws ComponentException {
        try {
            if (reversale.getReversale_rigaColl().size() == 0)
                return;
            Reversale_rigaBulk riga = (Reversale_rigaBulk) reversale.getReversale_rigaColl().get(0);

/*		if ( riga.getBanca() == null ||
			  riga.getBanca().getNumero_conto() == null) //reversale di regolarizzazione
			return;

		if ( Rif_modalita_pagamentoBulk.ALTRO.equals( riga.getBanca().getTi_pagamento()) ||
		     Rif_modalita_pagamentoBulk.IBAN.equals( riga.getBanca().getTi_pagamento()) ||
			Rif_modalita_pagamentoBulk.QUIETANZA.equals( riga.getBanca().getTi_pagamento()))
			return;
*/
            if (riga.getBanca() == null ||
                    ReversaleBulk.TIPO_REGOLARIZZAZIONE.equals(reversale.getTi_reversale())) //reversale di regolarizzazione
                return;


            String abi = riga.getBanca().getAbi();
            String cab = riga.getBanca().getCab();
            String nrConto = riga.getBanca().getNumero_conto();
            String quietanza = riga.getBanca().getQuietanza();
            String intestazione = riga.getBanca().getIntestazione();
            String cd_modalita_pag = riga.getModalita_pagamento().getCd_modalita_pag();

            /* verifico che ogni riga abbia le modalità di pagamento e gli attributi della banca uguali */
            /* vengono escluse dal test le note di debito e le note di credito */

            for (Iterator i = reversale.getReversale_rigaColl().iterator(); i.hasNext(); ) {
                riga = (Reversale_rigaBulk) i.next();

//			if ( Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA.equals( riga.getCd_tipo_documento_amm() ) &&
//				  riga.getTi_fattura().equals( Fattura_passiva_IBulk.TIPO_NOTA_DI_CREDITO ) )
//			// si tratta di una nota di credito - non deve essere effettuato la verifica delle modalità di pagamento
//				continue;
//
                //modalità di pagamento
                if (!riga.getModalita_pagamento().getCd_modalita_pag().equals(cd_modalita_pag))
                    throw new ApplicationException("Attenzione le righe della reversale devono avere la stessa modalità di pagamento");

                //conto bancario
                if (Rif_modalita_pagamentoBulk.BANCARIO.equals(riga.getBanca().getTi_pagamento()) &&
                        (!abi.equals(riga.getBanca().getAbi()) ||
                                !cab.equals(riga.getBanca().getCab()) ||
                                !nrConto.equals(riga.getBanca().getNumero_conto())))
                    throw new ApplicationException("Attenzione le righe della reversale devono avere la stessa modalità di pagamento bancario");
                else
                    //postale
                    if (Rif_modalita_pagamentoBulk.POSTALE.equals(riga.getBanca().getTi_pagamento()) &&
                            !nrConto.equals(riga.getBanca().getNumero_conto()))
                        throw new ApplicationException("Attenzione le righe della reversale devono avere la stessa modalità di pagamento postale");
                    else
                        //quietanza
                        if (Rif_modalita_pagamentoBulk.QUIETANZA.equals(riga.getBanca().getTi_pagamento()) &&
                                !quietanza.equals(riga.getBanca().getQuietanza()))
                            throw new ApplicationException("Attenzione le righe della reversale devono avere la stessa quietanza");
                        else
                            //altro
                            if ((Rif_modalita_pagamentoBulk.ALTRO.equals(riga.getBanca().getTi_pagamento()) ||
                                    Rif_modalita_pagamentoBulk.IBAN.equals(riga.getBanca().getTi_pagamento())) &&
                                    !intestazione.equals(riga.getBanca().getIntestazione()))
                                throw new ApplicationException("Attenzione le righe della reversale devono avere la stessa modalità di pagamento");

            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * verifica reversale - errore dettaglio
     * PreCondition:
     * E' stata richiesta la creazione/modifica di una reversale e la reversale non ha dettagli (Reversale_rigaBulk)
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare la reversale
     * verifica reversale - errore data di emissione futura
     * PreCondition:
     * E' stato richiesto di verificare la data di emissione della reversale
     * La reversale ha una data di emissione futura
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare la reversale
     * verifica reversale - errore data di emissione superiore alla data ultima reversale
     * PreCondition:
     * E' stato richiesto di verificare la data di emissione della reversale
     * La reversale ha una data di emissione maggiore della data di emissione dell'ultima reversale emessa
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare la reversale
     * verifica reversale di regolarizzazione/Incasso - errore sospesi
     * PreCondition:
     * E' stata richiesta la creazione/modifica di una reversale di regolarizzazione o di incasso
     * e dei sospesi di entrata sono stati associati alla reversale
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare una reversale di regolarizzazione
     * o di incasso con sospesi di entrata
     * verifica reversale a regolamento sospeso/accreditamento definitiva - errore sospesi
     * PreCondition:
     * E' stata richiesta la creazione/modifica di una reversale a regolamento sospeso o di accreditamento definitiva
     * e nessun sospeso e' stato associato alla reversale
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare una reversale a regolamento
     * o una reversale definitiva di accreditamento senza sospesi di entrata
     * verifica reversale a regolamento sospeso/accreditamento definitiva - errore importo sospesi
     * PreCondition:
     * E' stata richiesta la creazione/modifica di una reversale a regolamento sospeso o di accreditamento definitiva
     * e la somma degli importi dei sospesi associati e' diverso dall'importo della reversale
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare una reversale a regolamento
     * o una reversale definitiva di accreditamento con sospesi di entrata che non coprono completamente l'importo della reversale
     * stessa
     * verifica reversale a regolamento sospeso/accreditamento definitiva - errore modalità di pagamento
     * PreCondition:
     * E' stata richiesta la creazione/modifica di una reversale a regolamento sospeso o di accreditamento definitiva
     * e le modalità di pagamento dei sospesi d'entrata CNR associati sono diverse dalle modalità di pagamento della reversale
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare una reversale a regolamento
     * o una reversale definitiva di accreditamento con sospesi di entrata CNR che provengono da Banca d'Italia mentre la reversale
     * ha modalità di pagamento diverse dai sospesi o viceversa
     * verifica reversale - ok
     * PreCondition:
     * E' stata richiesta la creazione/modifica di una reversale e tutti i controlli sono stati superati
     * PostCondition:
     * La reversale ha superato la validazione e può pertanto essere salvata
     *
     * @param aUC       lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale <code>ReversaleBulk</code> la reversale di cui si verifica la correttezza
     */
    private void verificaReversale(UserContext aUC, ReversaleBulk reversale, boolean verificaDt_emissione) throws ComponentException {
        ReversaleHome rh = (ReversaleHome) getHome(aUC, reversale.getClass());

        //la reversale deve avere almeno un dettaglio
        if (reversale.getReversale_rigaColl().size() == 0)
            throw handleException(new ApplicationException("E' necessario selezionare almeno un documento attivo"));

        //le reverali di regoalarizz/incasso non possono avere sospesi associati
        if ((ReversaleBulk.TIPO_REGOLARIZZAZIONE.equals(reversale.getTi_reversale()) ||
                ReversaleBulk.TIPO_INCASSO.equals(reversale.getTi_reversale())) &&
                reversale.getSospeso_det_etrColl().size() > 0)
            throw handleException(new ApplicationException("Non e' consentito associare sospesi ad una reversale di regolarizzazione"));

        boolean isSiopeAttivo = false;

        try {
            isSiopeAttivo = Utility.createParametriCnrComponentSession().getParametriCnr(aUC, reversale.getEsercizio()).getFl_siope().booleanValue();
        } catch (Exception e) {
            throw handleException(e);
        }

        //la reversale a regolamento sospeso deve avere dei sospesi associati
        if (reversale.getTi_reversale().equals(ReversaleBulk.TIPO_REGOLAM_SOSPESO) ||
                (reversale.getTi_reversale().equals(ReversaleBulk.TIPO_TRASFERIMENTO) &&
                        reversale.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_REV) &&
                        !isSiopeAttivo)) {
            if (reversale.getSospeso_det_etrColl().size() == 0)
                throw handleException(new ApplicationException("E' necessario selezionare almeno un sospeso"));
            //verifica sull'importo sospesi
            if (reversale.getImTotaleSospesi().compareTo(reversale.getIm_reversale()) != 0)
                throw new ApplicationException("La somma degli importi dei sospesi deve essere uguale all'importo della reversale.");

            //verifico mod. pagamento sospesi/riscontri associati
            if (reversale.getCd_unita_organizzativa().equals(reversale.getCd_uo_ente())) {
                if (reversale.getSospeso_det_etrColl() != null && reversale.getSospeso_det_etrColl().size() > 0) {
                    Sospeso_det_etrBulk sospeso = (Sospeso_det_etrBulk) reversale.getSospeso_det_etrColl().get(0);
                    if (sospeso.getSospeso().getTi_cc_bi().equals(SospesoBulk.TIPO_CC) &&
                            reversale.isBanca_italia())
                        throw new ApplicationException("Attenzione! Il sospeso non proviene da Banca d'Italia mentre le modalità di pagamento della reversale sono di Banca d'Italia");

                    else if (sospeso.getSospeso().getTi_cc_bi().equals(SospesoBulk.TIPO_BANCA_ITALIA) &&
                            !reversale.isBanca_italia())
                        throw new ApplicationException("Attenzione! Il sospeso proviene da Banca d'Italia mentre le modalità di pagamento della reversale sono diverse da Banca d'Italia");
                }
            }

        }


        //verifica la data di contabilizzazione
        try {
            if (verificaDt_emissione) {
                Timestamp lastDayOfTheYear = DateServices.getLastDayOfYear(reversale.getEsercizio().intValue());

                if (getDataOdierna(aUC).after(lastDayOfTheYear) &&
                        reversale.getDt_emissione().compareTo(lastDayOfTheYear) != 0)
                    throw new ApplicationException("La data di registrazione deve essere " +
                            java.text.DateFormat.getDateInstance().format(lastDayOfTheYear));

                if (reversale.getDt_emissione().compareTo(rh.getServerTimestamp()) > 0)
                    throw new ApplicationException("Non è possibile inserire una reversale con data futura");
                Timestamp dataUltReversale = ((ReversaleHome) getHome(aUC, reversale.getClass())).findDataUltimaReversalePerCds(reversale);
                if (dataUltReversale != null && dataUltReversale.after(reversale.getDt_emissione()))
                    throw new ApplicationException("Non è possibile inserire una reversale con data anteriore a " +
                            java.text.DateFormat.getDateTimeInstance().format(dataUltReversale));
            }
            verificaModalitaPagamento(aUC, reversale);
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * Verifica dello stato dell'esercizio
     *
     * @param userContext <code>UserContext</code>
     * @return FALSE se per il cds interessato non è stato inserito nessun esercizio o se l'esercizio non è in stato di "aperto"
     * TRUE in tutti gli altri casi
     */

    protected void verificaStatoEsercizio(UserContext userContext, Integer es, String cd_cds) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        EsercizioBulk esercizio = (EsercizioBulk) getHome(userContext, EsercizioBulk.class).findByPrimaryKey(
                new EsercizioBulk(cd_cds, es));
        if (esercizio == null)
            throw handleException(new ApplicationException("Operazione impossibile: esercizio inesistente!"));
        if (!EsercizioBulk.STATO_APERTO.equals(esercizio.getSt_apertura_chiusura()))
            throw handleException(new ApplicationException("Operazione impossibile: esercizio non aperto!"));
    }

    /**
     * verifica tipo bollo
     * PreCondition:
     * E' stata richiesta l'inizializzazione di una istanza di ReversaleBulk e il tipo bollo
     * selezionato è corretto
     * PostCondition:
     * La reversale è valida. E' consentito procedere alla sua inizializzazione.
     * verifica tipo bollo - errore
     * PreCondition:
     * E' stata richiesta l'inizializzazione di una istanza di ReversaleBulk e il tipo bollo
     * selezionato è stato cancellato fisicamente dal database.
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare che il tipo bollo è
     * inesistente
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale   <code>ReversaleBulk</code> la reversale di cui si verifica la correttezza
     */

    private void verificaTipoBollo(UserContext userContext, ReversaleBulk reversale) throws it.cnr.jada.persistency.PersistencyException, ComponentException {
        if (reversale instanceof ReversaleIBulk) {
            Tipo_bolloBulk bollo;
            boolean found = false;
            for (Iterator i = ((ReversaleIBulk) reversale).getTipoBolloOptions().iterator(); i.hasNext(); ) {
                bollo = (Tipo_bolloBulk) i.next();
                if (bollo.getCd_tipo_bollo().equals(reversale.getReversale_terzo().getCd_tipo_bollo())) {
                    found = true;
                    return;
                }

            }
            if (!found) //probabilmente il tipo bollo ha il flag cancellato
            {
                bollo = (Tipo_bolloBulk) getHome(userContext, Tipo_bolloBulk.class).findByPrimaryKey(reversale.getReversale_terzo().getTipoBollo());
                if (bollo == null)
                    throw new ApplicationException(" Tipo bollo inesistente");
                else
                    ((ReversaleIBulk) reversale).getTipoBolloOptions().add(bollo);
            }
        }
    }

    /**
     * creazione legame Riga Reversale/Codici SIOPE
     * PreCondition:
     * E' stata generata la richiesta di caricare in automatico i codici SIOPE alla riga della reversale
     * per l'importo complessivo
     * PostCondition:
     * Se esiste un unico Codice SIOPE associabile alla riga della reversale viene creata una nuova
     * istanza di Reversale_siopeBulk per l'importo complessivo della riga della reversale
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riga        <code>Reversale_rigaBulk</code> la riga reversale da aggiornare
     * @return riga <code>Reversale_rigaBulk</code> la riga reversale aggiornata
     */

    private Reversale_rigaBulk aggiornaLegameSIOPE(UserContext userContext, Reversale_rigaBulk riga) throws ComponentException {
        try {
            if (riga.getReversale().isRequiredSiope()) {
                Reversale_rigaHome reversale_rigaHome = (Reversale_rigaHome) getHome(userContext, Reversale_rigaBulk.class);
                if (riga.isToBeCreated() || reversale_rigaHome.findCodiciCollegatiSIOPE(userContext, riga).isEmpty()) {
                    BulkList list = new BulkList(reversale_rigaHome.findCodiciCollegabiliSIOPE(userContext, riga));
                    if (list.size() == 1) {
                        Reversale_siopeIBulk reversale_siope = new Reversale_siopeIBulk();
                        reversale_siope.setCodice_siope((Codici_siopeBulk) list.get(0));
                        reversale_siope.setImporto(riga.getIm_reversale_riga());
                        reversale_siope.setToBeCreated();
                        riga.addToReversale_siopeColl(reversale_siope);
                    }
                }
            }
            return riga;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * caricamento codici SIOPE collegabili alla riga della reversale
     * PreCondition:
     * E' stata generata la richiesta di caricare in automatico i codici SIOPE da proporre per l'associazione
     * alla riga della reversale
     * PostCondition:
     * Vengono caricati i codici SIOPE disponibili per l'associazione della riga della reversale
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riga        <code>Reversale_rigaBulk</code> la riga reversale da aggiornare
     * @return riga <code>Reversale_rigaBulk</code> la riga reversale aggiornata
     */
    private Reversale_rigaBulk setCodiciSIOPECollegabili(UserContext userContext, Reversale_rigaBulk riga) throws ComponentException {
        try {
            Reversale_rigaHome reversale_rigaHome = (Reversale_rigaHome) getHome(userContext, Reversale_rigaBulk.class);

            Collection codiciCollegatiSIOPE;
            if (riga.getReversale_siopeColl().isEmpty() && !riga.isToBeCreated())
                codiciCollegatiSIOPE = reversale_rigaHome.findCodiciCollegatiSIOPE(userContext, riga);
            else
                codiciCollegatiSIOPE = riga.getReversale_siopeColl();

            boolean trovato = false;
            riga.setCodici_siopeColl(new BulkList());

            for (java.util.Iterator collegabile = reversale_rigaHome.findCodiciCollegabiliSIOPE(userContext, riga).iterator(); collegabile.hasNext(); ) {
                Codici_siopeBulk codiceCollegabile = (Codici_siopeBulk) collegabile.next();
                trovato = false;

                for (java.util.Iterator collegati = codiciCollegatiSIOPE.iterator(); collegati.hasNext(); ) {
                    Reversale_siopeBulk codiceCollegato = (Reversale_siopeBulk) collegati.next();

                    if (codiceCollegato.getCodice_siope().equalsByPrimaryKey(codiceCollegabile)) {
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
     * Ritorna true se il totale SIOPE associato alla riga reversale non corrisponde con l'importo della riga stessa
     * PreCondition:
     * E' stata generata la richiesta di verificare che la riga della reversale sia associata completamente a codici SIOPE
     * PostCondition:
     * Ritorna TRUE se la riga della reversale è associata completamente a codici SIOPE
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param riga        <code>Reversale_rigaBulk</code> la riga reversale da controllare
     * @return Boolean
     */
    private java.lang.Boolean isCollegamentoSiopeCompleto(UserContext userContext, Reversale_rigaBulk riga) throws ComponentException {
        try {
            Reversale_rigaHome reversale_rigaHome = (Reversale_rigaHome) getHome(userContext, riga);
            BigDecimal totaleSiope = Utility.ZERO;

            for (java.util.Iterator collegati = reversale_rigaHome.findCodiciCollegatiSIOPE(userContext, riga).iterator(); collegati.hasNext(); )
                totaleSiope = totaleSiope.add(((Reversale_siopeBulk) collegati.next()).getImporto());

            return new Boolean(totaleSiope.compareTo(riga.getIm_reversale_riga()) == 0);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Ritorna true se il totale SIOPE associato alla reversale non corrisponde con l'importo della reversale stessa
     * PreCondition:
     * E' stata generata la richiesta di verificare che la reversale sia associata completamente a codici SIOPE
     * PostCondition:
     * Viene verificato che tutte le righe della reversale siano associate a codici SIOPE.
     * Ritorna TRUE se tutte le righe della reversale sono associate completamente a codici SIOPE
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale   <code>ReversaleBulk</code> la reversale da controllare
     * @return Boolean
     */
    public java.lang.Boolean isCollegamentoSiopeCompleto(UserContext userContext, ReversaleBulk reversale) throws ComponentException {
        try {
            ReversaleHome reversaleHome = (ReversaleHome) getHome(userContext, reversale.getClass());
            reversale = (ReversaleBulk) reversaleHome.findByPrimaryKey(reversale);
            if (reversale.isRequiredSiope()) {

                for (Iterator i = reversaleHome.findReversale_riga(userContext, reversale).iterator(); i.hasNext(); ) {
                    if (!this.isCollegamentoSiopeCompleto(userContext, (Reversale_rigaBulk) i.next()))
                        return Boolean.FALSE;
                }
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * caricamento codici SIOPE collegabili a tutte le righe della reversale
     * PreCondition:
     * E' stata generata la richiesta di caricare in automatico i codici SIOPE da proporre per l'associazione
     * alle righe della reversale
     * PostCondition:
     * Vengono caricati i codici SIOPE disponibili per l'associazione sulla righe della reversale
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param reversale   <code>ReversaleBulk</code> la reversale da aggiornare
     * @return riga <code>ReversaleBulk</code> la reversale aggiornata
     */
    public ReversaleBulk setCodiciSIOPECollegabili(UserContext userContext, ReversaleBulk reversale) throws ComponentException {
        for (Iterator i = reversale.getReversale_rigaColl().iterator(); i.hasNext(); )
            setCodiciSIOPECollegabili(userContext, (Reversale_rigaBulk) i.next());
        return reversale;
    }

    public SQLBuilder selectCupByClause(UserContext userContext, ReversaleCupIBulk reversale, CupBulk cup, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, CupBulk.class).createSQLBuilder();
        sql.openParenthesis("AND");
        sql.addClause("AND", "dt_canc", SQLBuilder.ISNULL, null);
        sql.addClause("OR", "dt_canc", SQLBuilder.GREATER, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
        sql.closeParenthesis();
        sql.addClause(clauses);
        return sql;
    }

    private java.sql.Timestamp getFirstDayOfYear(int year) {

        java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
        calendar.set(java.util.Calendar.MONTH, 0);
        calendar.set(java.util.Calendar.YEAR, year);
        calendar.set(java.util.Calendar.HOUR, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        calendar.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
        return new java.sql.Timestamp(calendar.getTime().getTime());
    }

    public SQLBuilder selectCupByClause(UserContext userContext, ReversaleSiopeCupIBulk bulk, CupBulk cup, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, CupBulk.class).createSQLBuilder();
        sql.openParenthesis("AND");
        sql.addClause("AND", "dt_canc", SQLBuilder.ISNULL, null);
        sql.addClause("OR", "dt_canc", SQLBuilder.GREATER, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
        sql.closeParenthesis();
        sql.addClause(clauses);
        return sql;
    }

    public byte[] lanciaStampa(UserContext userContext, String cds, Integer esercizio, Long pgReversale)
            throws PersistencyException, ComponentException, RemoteException, javax.ejb.EJBException, ParseException {

        ReversaleIHome home = (ReversaleIHome) getHome(userContext, ReversaleIBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        String cdsEnte = "999";
        String tipoReversaleSospeso = "S"; // S = Sospeso. Reversale a regolamento di sospeso
        String statoIncassato = "P"; // P = Incassata. La reversale risulta incassata
        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, esercizio);
        sql.addSQLClause("AND", "CD_CDS", SQLBuilder.EQUALS, cdsEnte);
        sql.addSQLClause("AND", "PG_REVERSALE", SQLBuilder.EQUALS, pgReversale);
        sql.addSQLClause("AND", "CD_CDS_ORIGINE", SQLBuilder.EQUALS, cds);
        sql.addSQLClause("AND", "TI_REVERSALE", SQLBuilder.EQUALS, tipoReversaleSospeso);
        sql.addSQLClause("AND", "STATO", SQLBuilder.EQUALS, statoIncassato);

        java.util.List list = home.fetchAll(sql);
        if (list.isEmpty())
            throw new FatturaNonTrovataException("Reversale non trovata");

        try {

            java.sql.Timestamp dataInizio = getFirstDayOfYear(esercizio);
            Timestamp dataFine = getLastDayOfYear(esercizio);

            File output = new File(System.getProperty("tmp.dir.SIGLAWeb") + "/tmp/", File.separator + getOutputFileName("vpg_reversale.jasper", cds, esercizio, pgReversale));

            Print_spoolerBulk print = new Print_spoolerBulk();
            Print_spooler_paramBulk printparam = new Print_spooler_paramBulk();
            print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
            print.setFlEmail(false);
            print.setReport("/doccont/doccont/vpg_reversale.jasper");
            print.setNomeFile(getOutputFileName("vpg_reversale.jasper", cds, esercizio, pgReversale));
            print.setUtcr(userContext.getUser());

            print.addParam("aCd_cds", cdsEnte, String.class);
            print.addParam("aEs", esercizio, Integer.class);
            print.addParam("aPg_da", pgReversale, Long.class);
            print.addParam("aPg_a", pgReversale, Long.class);

            printparam = new Print_spooler_paramBulk();
            printparam.setNomeParam("aDt_da");
            printparam.setValoreParam(new SimpleDateFormat("yyyy/MM/dd").format(dataInizio));
            printparam.setParamType("java.util.Date");
            print.addParam(printparam);

            printparam = new Print_spooler_paramBulk();
            printparam.setNomeParam("aDt_a");
            printparam.setValoreParam(new SimpleDateFormat("yyyy/MM/dd").format(dataFine));
            printparam.setParamType("java.util.Date");
            print.addParam(printparam);
            print.addParam("aCd_terzo", "%", String.class);

            Report report = SpringUtil.getBean("printService", PrintService.class).executeReport(userContext, print);

            FileOutputStream f = new FileOutputStream(output);
            f.write(report.getBytes());
            f.flush();
            f.close();

            return report.getBytes();
        } catch (IOException e) {
            throw new GenerazioneReportException("Generazione Stampa non riuscita", e);
        }
    }

    private String getOutputFileName(String reportName, String cds, Integer esercizio, Long pgReversale) {
        String fileName = reportName;
        fileName = fileName.replace('/', '_');
        fileName = fileName.replace('\\', '_');
        if (fileName.startsWith("_"))
            fileName = fileName.substring(1);
        if (fileName.endsWith(".jasper"))
            fileName = fileName.substring(0, fileName.length() - 7);
        fileName = fileName + ".pdf";
        fileName = PDF_DATE_FORMAT.format(new java.util.Date()) + '_' + fileName + '_' + esercizio + '_' + cds + '_' + pgReversale;
        return fileName;
    }

    public String isAnnullabile(
            UserContext userContext, ReversaleBulk reversale)
            throws ComponentException {
        try {
            Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk) getHome(userContext, Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(reversale.getEsercizio()));
            if (parametriCnr.getFl_tesoreria_unica()) {
                UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));
                if (reversale.getStato_trasmissione().compareTo(ReversaleBulk.STATO_TRASMISSIONE_NON_INSERITO) == 0)
                    return "S";
                else if (reversale.getStato_trasmissione().compareTo(ReversaleBulk.STATO_TRASMISSIONE_TRASMESSO) == 0 &&
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

    @Override
    public ReversaleBulk annullaReversale(UserContext userContext,
                                          ReversaleBulk reversale, boolean annullaCollegati)
            throws ComponentException {
        try {
            return annullaReversale(userContext, reversale, annullaCollegati, false);
        } catch (Exception e) {
            throw handleException(reversale, e);
        }
    }

    public SQLBuilder selectV_man_revByClause(UserContext userContext, ReversaleBulk bulk, V_mandato_reversaleBulk v_man_rev, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, V_mandato_reversaleBulk.class).createSQLBuilder();
        sql.addClause("AND", "cd_tipo_documento_cont", SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_REV);
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
        //sql.addClause( "AND", "cd_cds", sql.EQUALS, bulk.getCd_cds() );
        sql.addClause("AND", "cd_uo_origine", SQLBuilder.EQUALS, ((CNRUserContext) userContext).getCd_unita_organizzativa());
        sql.addClause("AND", "ti_documento_cont", SQLBuilder.NOT_EQUALS, ReversaleBulk.TIPO_REGOLARIZZAZIONE);
        sql.addClause("AND", "stato", SQLBuilder.EQUALS, ReversaleBulk.STATO_REVERSALE_EMESSO);
        sql.addSQLClause("AND", "dt_trasmissione", SQLBuilder.ISNULL, null);
        sql.addClause(clauses);
        return sql;
    }

    public Boolean esisteAnnullodaRiemettereNonCollegato(UserContext userContext,
                                                         Integer esercizio, String cds) throws ComponentException {
        try {
            SQLBuilder sql = getHome(userContext, ReversaleIBulk.class).createSQLBuilder();
            sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
            sql.addClause("AND", "cd_cds_origine", SQLBuilder.EQUALS, cds);
            sql.addClause("AND", "stato", SQLBuilder.EQUALS, ReversaleBulk.STATO_REVERSALE_ANNULLATO);
            sql.addSQLClause("AND", "pg_reversale_riemissione", SQLBuilder.ISNULL, null);
            sql.addClause(FindClause.AND, "fl_riemissione", SQLBuilder.EQUALS, true);
            if (sql.executeCountQuery(getConnection(userContext)) > 0)
                return Boolean.TRUE;
            else
                return Boolean.FALSE;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public Boolean isReversaleCollegataAnnullodaRiemettere(UserContext userContext,
                                                           ReversaleBulk reversale) throws ComponentException {
        try {
            SQLBuilder sql = getHome(userContext, ReversaleIBulk.class).createSQLBuilder();
            sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, reversale.getEsercizio());
            sql.addClause("AND", "cd_cds_origine", SQLBuilder.EQUALS, reversale.getCd_cds_origine());
            sql.addClause("AND", "stato", SQLBuilder.EQUALS, ReversaleBulk.STATO_REVERSALE_ANNULLATO);
            sql.addSQLClause("AND", "pg_reversale_riemissione", SQLBuilder.EQUALS, reversale.getPg_reversale());
            sql.addClause(FindClause.AND, "fl_riemissione", SQLBuilder.EQUALS, true);
            sql.addClause("AND", "stato_trasmissione_annullo", SQLBuilder.NOT_EQUALS, ReversaleBulk.STATO_TRASMISSIONE_TRASMESSO);
            if (sql.executeCountQuery(getConnection(userContext)) > 0)
                return Boolean.TRUE;
            else
                return Boolean.FALSE;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public Boolean isReversaleCORINonAssociataMandato (UserContext userContext, ReversaleBulk reversale) throws ComponentException {
        try {
            SQLBuilder sql = getHome(userContext, ReversaleIBulk.class).createSQLBuilder();
            sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, reversale.getEsercizio());
            sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, reversale.getCd_cds());
            sql.addClause("AND", "pg_reversale", SQLBuilder.EQUALS, reversale.getPg_reversale());
            sql.addTableToHeader("REVERSALE_RIGA");
            sql.addSQLJoin( "REVERSALE_RIGA.ESERCIZIO", "REVERSALE.ESERCIZIO");
            sql.addSQLJoin( "REVERSALE_RIGA.CD_CDS", "REVERSALE.CD_CDS");
            sql.addSQLJoin( "REVERSALE_RIGA.PG_REVERSALE", "REVERSALE.PG_REVERSALE");
            sql.openParenthesis(FindClause.AND);
                sql.addSQLClause(FindClause.AND, "REVERSALE_RIGA.CD_TIPO_DOCUMENTO_AMM", SQLBuilder.EQUALS, IDocumentoAmministrativoRigaBulk.tipo.GEN_CORA_E.name());
                sql.addSQLClause(FindClause.OR, "REVERSALE_RIGA.CD_TIPO_DOCUMENTO_AMM", SQLBuilder.EQUALS, IDocumentoAmministrativoRigaBulk.tipo.GEN_CORV_E.name());
            sql.closeParenthesis();
            SQLBuilder sqlNotExists = getHome(userContext, Ass_mandato_reversaleBulk.class).createSQLBuilder();
            sqlNotExists.addSQLClause("AND", "esercizio_reversale", SQLBuilder.EQUALS, reversale.getEsercizio());
            sqlNotExists.addSQLClause("AND", "cd_cds_reversale", SQLBuilder.EQUALS, reversale.getCd_cds());
            sqlNotExists.addSQLClause("AND", "pg_reversale", SQLBuilder.EQUALS, reversale.getPg_reversale());
            sql.addSQLNotExistsClause(FindClause.AND, sqlNotExists);

            return sql.executeCountQuery(getConnection(userContext)) > 0;
        } catch (Exception e) {
            throw handleException(e);
        }
    }
}
