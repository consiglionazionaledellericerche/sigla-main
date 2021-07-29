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

package it.cnr.contab.missioni00.actions;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk;
import it.cnr.contab.docamm00.actions.EconomicaAction;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoSpesaBP;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.missioni00.bp.CRUDMissioneBP;
import it.cnr.contab.missioni00.docs.bulk.AnticipoBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.missioni00.docs.bulk.Missione_dettaglioBulk;
import it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk;
import it.cnr.contab.missioni00.ejb.MissioneComponentSession;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_rimborso_kmBulk;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_pastoBulk;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_spesaBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashMap;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.FormBP;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;

import java.util.GregorianCalendar;
import java.util.Optional;

/**
 * Insert the type's description here.
 * Creation date: (07/02/2002 13.23.09)
 *
 * @author: Paola sala
 */
public class CRUDMissioneAction extends EconomicaAction {

    public static final String I_DETTAGLI_DELLA_MISSIONE_VERRANNO_CANCELLATI_PROSEGUIRE = "I dettagli della missione verranno cancellati. Proseguire ??";

    /**
     * CRUDMissioneAction constructor comment.
     */
    public CRUDMissioneAction() {
        super();
    }

    /**
     * Il metodo gestisce parte del rientro dall'aggiornamento manuale e dalla creazione della scadenza
     * associata alla missione.
     * (Validazione terzo, sincronizzazione delle scadenze e lock alla scadenza)
     */

    public Forward basicDoBringBackOpenObbligazioniWindow(ActionContext context, Obbligazione_scadenzarioBulk scadenza) throws BusinessProcessException {
        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
        MissioneBulk missione = (MissioneBulk) bp.getModel();

        try {
            try {
                TerzoBulk creditore = scadenza.getObbligazione().getCreditore();
                if (!missione.getTerzo().equalsByPrimaryKey(creditore) &&
                        !AnagraficoBulk.DIVERSI.equalsIgnoreCase(creditore.getAnagrafico().getTi_entita()))
                    throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata deve appartenere ad un'obbligazione che ha come creditore il terzo della missione!");

                it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession h = it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP.getVirtualComponentSession(context, true);
                h.lockScadenza(context.getUserContext(), scadenza);
            } catch (Exception e) {
                throw new BusinessProcessException(e);
            }
            missione.gestisciCambioSelezioneScadenza(scadenza);
        } catch (Throwable t) {
            it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi defSaldiBulk = bp.getDefferedUpdateSaldiParentBP().getDefferedUpdateSaldiBulk();
            if (scadenza.getObbligazione().getPg_ver_rec().equals((Long) scadenza.getObbligazione().getSaldiInfo().get("pg_ver_rec")))
                defSaldiBulk.removeFromDefferredSaldi(scadenza.getObbligazione());
            try {
                it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP.rollbackToSafePoint(context);
            } catch (Exception e) {
                throw new BusinessProcessException(e);
            }
            throw new BusinessProcessException(t);
        }
        return context.findDefaultForward();
    }

    /**
     * Metodo chiamato dal Fondo Economale per cercare le missioni eleggibili da associare
     * alle spese del fondo.
     * Tale metodo chiama il metodo "find" della missione
     */

    protected Forward basicDoCerca(ActionContext context) throws java.rmi.RemoteException, InstantiationException, javax.ejb.RemoveException {
        try {
            fillModel(context);
            it.cnr.jada.util.action.CRUDBP bp = getBusinessProcess(context);
            OggettoBulk model = (OggettoBulk) bp.getModel();
            it.cnr.jada.util.RemoteIterator ri = bp.find(context, null, model);
            if (ri == null || ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                bp.setMessage("La ricerca non ha fornito alcun risultato.");
                return context.findDefaultForward();
                //}
                //else if (ri.countElements() == 1) {
                //OggettoBulk bulk = (OggettoBulk)ri.nextElement();
                //return basicDoRiportaSelezione(context, bulk);
            } else {
                bp.setModel(context, model);
                SelezionatoreListaBP nbp = (SelezionatoreListaBP) context.createBusinessProcess("Selezionatore");
                nbp.setIterator(context, ri);
                nbp.setBulkInfo(bp.getBulkInfo());
                nbp.setColumns(getBusinessProcess(context).getSearchResultColumns());
                context.addHookForward("seleziona", this, "doRiportaSelezione");
                return context.addBusinessProcess(nbp);
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Metodo chiamato dal Fonfo Economale per riportare la Missione selezionata
     */

    protected Forward basicDoRiportaSelezione(ActionContext context, it.cnr.jada.bulk.OggettoBulk selezione) throws java.rmi.RemoteException {

        try {
            if (selezione != null) {
                CRUDMissioneBP bp = (CRUDMissioneBP) context.getBusinessProcess();
                bp.edit(context, selezione);
                selezione = bp.getModel();
                //if (!MissioneBulk.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(((MissioneBulk)selezione).getRiportata()))
                //throw new it.cnr.jada.comp.ApplicationException("Il documento amministrativo risulta riportato! Operazione annullata.");

                // Borriello: integrazione Err. CNR 775
                Integer esScriv = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());

                if ((((MissioneBulk) selezione).getEsercizio().compareTo(esScriv) == 0) && ((MissioneBulk) selezione).isRiportata()) {
                    throw new it.cnr.jada.comp.ApplicationException("Il documento amministrativo risulta (parzialmente) riportato! Operazione annullata.");
                }

                // Gennaro Borriello - (09/11/2004 18.08.57)
                //	Nuova gestione dello stato <code>getRiportata()</code>
                if ((((MissioneBulk) selezione).getEsercizio().compareTo(esScriv) != 0) && (!MissioneBulk.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(((MissioneBulk) selezione).getRiportataInScrivania()))) {
                    throw new it.cnr.jada.comp.ApplicationException("Il documento amministrativo non risulta completamente riportato! Operazione annullata.");
                }

                context.closeBusinessProcess();
                HookForward forward = (HookForward) context.findForward("bringback");
                forward.addParameter("documentoAmministrativoSelezionato", selezione);
                return forward;
            }
        } catch (Exception e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }

    /**
     * Il metodo gestisce la fine della modalità "modifica/inserimento dettaglio di spesa".
     */

    private void basicDoUndoSpesa(ActionContext context) throws BusinessProcessException {
        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
        bp.undoSpesa(context);
    }

    /**
     * Il metodo gestisce la fine della modalità "modifica/inserimento di una tappa"
     */

    private void basicDoUndoTappa(ActionContext context) throws BusinessProcessException {

        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
        bp.undoTappa(context);
    }

    /**
     * Il metodo gestisce la creazione di una nuova spesa e le relative inizializzazioni
     */

    public Forward doAddToCRUDMain_Spesa(ActionContext context) {
        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
        try {
            fillModel(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            missione.isInserimentoSpeseModificabile();

            // 	Se ho la diaria ne propongo la cancellazione automatica
            //	per poter proseguire con la creazione della spesa
            if ((missione.getDiariaMissioneColl() != null) && (!missione.getDiariaMissioneColl().isEmpty()))
                return openConfirm(context, "La Diaria verra' cancellata. Proseguire ?", OptionBP.CONFIRM_YES_NO, "doCancellaDiariaPerCreazioneSpesa");
            else {
                // Chiamo il metodo "addDetail" di CRUDMissione_spesaController
                bp.getSpesaController().add(context);
                missione.setSpeseInserite(false);
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }

    /**
     * Il metodo gestisce l'inizializzazione di una nuova tappa (cds, unita organizzativa, esercizio, comune proprio)
     */

    public Forward doAddToCRUDMain_Tappa(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);

            bp.addTappa(context);

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce l' aggiornamento della missione (importi calcolati) dopo il rientro dalla creazione/modifica
     * del compenso
     * Se il compenso ha impostato la linea di attivita' e se la missione ha associato un anticipo
     * devo inizializzare la linea di attività dell'anticipo con quella del compenso. Cio' servirà per
     * creare il Rimborso.
     */

    private void doAggiornaMissionePerCompenso(ActionContext context, MissioneBulk missione) throws BusinessProcessException {
        CRUDMissioneBP bp = (CRUDMissioneBP) context.getBusinessProcess();

        if (missione.getCompenso().getCd_linea_attivita_genrc() != null && missione.getAnticipo().getCd_linea_attivita() == null)
            bp.updateAnticipo(context, missione);

        missione.setFl_associato_compenso(new Boolean(true));
        missione.setIm_netto_pecepiente(missione.getCompenso().getIm_netto_percipiente());
        missione.setIm_lordo_percepiente(missione.getCompenso().getIm_lordo_percipiente());
        missione.setIm_totale_missione(missione.getCompenso().getIm_totale_compenso());

        missione.setStato_coge(missione.STATO_COGE_NON_PROCESSARE);
        missione.setStato_coan(missione.STATO_COAN_NON_PROCESSARE);

        bp.update(context);

        bp.ricaricaMissioneInModifica(context);
    }

    /**
     * Il metodo gestisce l'eliminazione dell'associazione missione-anticipo.
     * Il metodo effettua le opportune validazioni sull'importo della scadenza eventualmente associata alla missione e
     * ricalcola gli importi della missione
     */

    public Forward doBlankSearchFind_anticipo(ActionContext context, MissioneBulk missione) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) context.getBusinessProcess();

            if (!missione.isMissioneConAnticipo()) {
                missione.setAnticipo(new AnticipoBulk());
                return context.findDefaultForward();
            }

            missione.setAnticipo(new AnticipoBulk());

            if (missione.isMissioneConObbligazione()) {
                missione.calcolaConsuntivi();

                if (missione.getImporto_scadenza_obbligazione().compareTo(missione.getObbligazione_scadenzario().getIm_scadenza()) != 0)
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "L'importo della scadenza deve essere " + missione.getImporto_scadenza_obbligazione().toString());
            }

            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il metodo gestisce l'inizializzazione del terzo della missione.
     * Se la missione possiede dei dettagli di spesa/diaria il metodo ne propone la cancellazione prima di proseguire.
     * Se l'utente non conferma tale cancellazione l'inizializzazione del terzo verrà annullata.
     */

    public Forward doBlankSearchFind_terzo(ActionContext context, MissioneBulk missione) {
        try {
            if (missione.getDettagliMissioneColl().isEmpty()) {
                missione.inizializzaTerzo();

                return context.findDefaultForward();
            } else {
                OptionBP option = openConfirm(context, "I dettagli della missione verranno cancellati. Proseguire ??", OptionBP.CONFIRM_YES_NO, "doConfermaTerzoBlankSearchChange");
                return option;
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il metodo inizializza gli attributi di un dettaglio di spesa e relativi al Tipo Auto
     */

    public Forward doBlankSearchFind_tipo_auto(ActionContext context, Missione_dettaglioBulk aSpesa) {
        aSpesa.setTipo_auto(new Missione_rimborso_kmBulk());
        aSpesa.setTi_auto(null);
        aSpesa.setIm_spesa_divisa(new java.math.BigDecimal(0));
        return context.findDefaultForward();
    }

    /**
     * Il metodo inizializza gli attributi di un dettaglio di spesa e relativi al Tipo Pasto
     */

    public Forward doBlankSearchFind_tipo_pasto(ActionContext context, Missione_dettaglioBulk aSpesa) {
        aSpesa.setTipo_pasto(new Missione_tipo_pastoBulk());
        aSpesa.setCd_ti_pasto(null);

        return context.findDefaultForward();
    }

    /**
     * Il metodo inizializza gli attributi di un dettaglio di spesa e relativi al Tipo Spesa
     */

    public Forward doBlankSearchFind_tipo_spesa(ActionContext context, Missione_dettaglioBulk aSpesa) {
        aSpesa.setTipo_spesa(new Missione_tipo_spesaBulk());
        aSpesa.setCd_ti_spesa(null);
        aSpesa.setPercentuale_maggiorazione(new java.math.BigDecimal(0));
        aSpesa.setDs_ti_spesa(null);
        aSpesa.setTi_cd_ti_spesa(null);

        return context.findDefaultForward();
    }

    /**
     * Il metodo gestisce il rientro dalla creazione/modifica di un compenso :
     * - se la missione è provvisoria e la creazione del compenso è stata annullata riporto
     * la missione in stato provvisorio
     * - sincronizzo l'obbligazione del compenso con quella presente nella proprietà della missione
     * "deferredSaldi" (per l'aggiornamento dei saldi)
     * - aggiorno la missione con gli importi calcolati dal compenso
     */

    public Forward doBringBackCompenso(ActionContext context) {
        try {
            HookForward caller = (HookForward) context.getCaller();
            CompensoBulk compenso = (CompensoBulk) caller.getParameter("bringback");

            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (compenso == null) {
                if (missione.getMissioneIniziale() != null &&
                        missione.getMissioneIniziale().getTi_provvisorio_definitivo() != null)
                    missione.setTi_provvisorio_definitivo(missione.getMissioneIniziale().getTi_provvisorio_definitivo());
                else
                    missione.setTi_provvisorio_definitivo(MissioneBulk.SALVA_TEMPORANEO);

                missione.setDeferredSaldi(new PrimaryKeyHashMap());
                missione = (MissioneBulk) bp.getModel();
                return context.findDefaultForward();
            }

            missione.setCompenso(compenso);
            //	Sincronizzo l' obbligazione del compenso (numero positivo)
            //	con l'obbligazione del compenso salvata nel deferredSaldi della missione
            //	(numero negativo)
            //	Stessa cosa per le scadenze cancellate
            missione.sincronizzaObbligazioneCompenso(context.getUserContext());
            missione.sincronizzaObbligazioniCancellateCompenso(context.getUserContext());

            doAggiornaMissionePerCompenso(context, missione);    // reinizializza 'missioneIniziale'

            bp.commitUserTransaction();
            bp.setMessage("Salvataggio terminato con successo.");

            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * Il metodo gestisce il rientro dalla creazione di un nuovo anagrafico.
     * Vengono inizializzati attributi quali : nome, cognome, ragione sociale, codice fiscale, partita iva,
     * modalita e termini di pagamento dell'anangrafico associato al Terzo selezionato
     */

    public Forward doBringBackCRUDCrea_terzo(ActionContext context, MissioneBulk missione, TerzoBulk aTerzo) throws java.rmi.RemoteException {
        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
        if (Optional.ofNullable(aTerzo).isPresent()) {
            try {
                MissioneComponentSession component = (MissioneComponentSession) bp.createComponentSession();
                missione.setCd_terzo(aTerzo.getCd_terzo());
                missione = component.caricaTerzoInModificaMissione(context.getUserContext(), missione);
            } catch (BusinessProcessException|ComponentException e) {
                return handleException(context, e);
            }
        }
        return doBringBackSearchFind_terzo(context, missione, missione.getV_terzo());
    }

    /**
     * Il metodo gestisce il rientro dall'aggiornamento manuale della scadenza associata alla missione.
     */

    public Forward doBringBackOpenObbligazioniWindow(ActionContext context) {
        try {
            HookForward caller = (HookForward) context.getCaller();
            Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) caller.getParameter("bringback");

            if (scadenza == null)
                return context.findDefaultForward();

            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            basicDoBringBackOpenObbligazioniWindow(context, scadenza);

            missione.setObbligazione_scadenzario(scadenza);
            bp.setDirty(true);
            bp.setModel(context, missione);

            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * Il metodo gestisce il rientro dalla finestra di ricerca con filtri o creazione di obbligazioni.
     */

    public Forward doBringBackRicercaObbligazioniWindow(ActionContext context) {
        try {
            HookForward caller = (HookForward) context.getCaller();
            Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) caller.getParameter("obbligazioneSelezionata");

            if (scadenza == null)
                return context.findDefaultForward();

            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            basicDoBringBackOpenObbligazioniWindow(context, scadenza);

            missione.setObbligazione_scadenzario(scadenza);

            //	verifico se la scadenza appena associata l'avevo precedentemente slegata
            if (missione.isScadenzaDaRimuovereDaiCancellati()) {
                //  la rimuovo dalla collection altrimenti l'aggiornamento del suo
                //	im_associato_doc_amm a zero mi cambierebbe il pg_ver_rec
                missione.removeFromDocumentiContabiliCancellati(scadenza);
            }
            bp.setDirty(true);
            bp.setModel(context, missione);

            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce il rientro dalla ricerca di un anticipo da associare alla missione.
     * Se la missione e' associata ad una scadenza si effettuano i seguenti controlli :
     * -	se importo anticipo > importo missione --> sgancio la scadenza
     * - 	se importo missione - importo anticipo != importo scadenza --> mando msg
     */

    public Forward doBringBackSearchFind_anticipo(ActionContext context, MissioneBulk missione, AnticipoBulk anticipo) {
        try {
            if (anticipo == null)
                return context.findDefaultForward();

            missione.setAnticipo(anticipo);
            missione.calcolaConsuntivi();

            if (missione.isMissioneConObbligazione()) {
                if (missione.isImportoAnticipoMaggioreDiMissione()) {
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Associazione con la scadenza di impegno rimossa! L'anticipo ha importo maggiore della missione");
                    missione.addToDocumentiContabiliCancellati(missione.getObbligazione_scadenzario());
                    missione.setObbligazione_scadenzario(new Obbligazione_scadenzarioBulk());
                    if (missione.getStato_pagamento_fondo_eco().compareTo(MissioneBulk.FONDO_ECO) == 0)
                        missione.setStato_pagamento_fondo_eco(MissioneBulk.NO_FONDO_ECO);
                } else if (missione.getImporto_scadenza_obbligazione().compareTo(missione.getObbligazione_scadenzario().getIm_scadenza()) != 0)
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "L'importo della scadenza deve essere " + missione.getImporto_scadenza_obbligazione().toString());
            }

            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la selezione della divisa di un dettaglio di spesa.
     * Se tale divisa risulta uguale a quella inserita nella configurazione della tappa per lo stesso giorno
     * propongo di default il cambio di tale tappa
     */

    public Forward doBringBackSearchFind_divisa_spesa(ActionContext context, Missione_dettaglioBulk spesa, DivisaBulk aDivisa) {
        try {
            if (aDivisa == null)
                return context.findDefaultForward();

            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);

            spesa.setDivisa_spesa(aDivisa);

            bp.setCambioSpesaDefault(context, spesa);

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la selezione di una nazione estera di una tappa.
     * Vengono inizializzate le seguenti proprietà della tappa :
     * - la divisa viene inizializzata con quella letta da tabella MISSIONE_DIARIA per la stessa nazione,
     * per l'inquadramento della missione e valida alla data di inizio missione.
     * Tale divisa non sara' modificabile.
     * - il cambio della divisa trovata valido alla data inizio missione
     * Tale cambio può essere modificato.
     */

    public Forward doBringBackSearchFind_nazione(ActionContext context, Missione_tappaBulk tappa, NazioneBulk aNazione) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);

            tappa.setNazione(aNazione);
            tappa.setDivisa_tappa(new DivisaBulk());
            tappa.setCambio_tappa(new java.math.BigDecimal(0));

            if (aNazione != null && aNazione.getCd_area_estera() == null)
                throw new it.cnr.jada.comp.ApplicationException("Area Estera non difinita per la nazione !");
            //bp.setMessage("Area Estera non difinita per la nazione !");
            if (tappa.getNazione() != null)
                bp.setDivisaCambioTappaEstera(context, tappa);

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce l'inizializzazione di alcuni attributi dell'anticipo relativi al terzo e ai dati
     * bancari del terzo (nome, cognome, ragione sociale, codice fiscale, partita iva, modalita e termini di pagamento).
     * Vengono inizializzati anche i "Tipi rapporto"; se non presenti l'utente non può proseguire
     */

    public Forward doBringBackSearchFind_terzo(ActionContext context, MissioneBulk missione, V_terzo_per_compensoBulk aTerzo) {
        try {
            if (aTerzo == null)
                return context.findDefaultForward();

            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneComponentSession component = (MissioneComponentSession) bp.createComponentSession();
            missione.inizializzaTerzo();
            missione = component.completaTerzo(context.getUserContext(), missione, aTerzo);

            if ((missione.getTipi_rapporto() == null) || (missione.getTipi_rapporto().isEmpty()))
                bp.setMessage("Impossibile proseguire. Tipi Rapporto non disponibili !");

            bp.setModel(context, missione);

            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Inizializzo l'attributo Tipo Auto del dettaglio di spesa
     */
    public Forward doBringBackSearchFind_tipo_auto(ActionContext context, Missione_dettaglioBulk aSpesa, Missione_rimborso_kmBulk tipoAuto) {
        try {
            if (tipoAuto == null)        // Nessuna selezione
                return context.findDefaultForward();

            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            aSpesa.setTi_auto(tipoAuto.getTi_auto());
            aSpesa.setTipo_auto(tipoAuto);

            bp.setModel(context, missione);

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Inizializzo l'attributo Tipo Pasto del dettaglio di spesa
     */

    public Forward doBringBackSearchFind_tipo_pasto(ActionContext context, Missione_dettaglioBulk aSpesa, Missione_tipo_pastoBulk tipoPasto) {
        try {
            if (tipoPasto == null)        // Nessuna selezione
                return context.findDefaultForward();

            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            aSpesa.setTipo_pasto(tipoPasto);
            aSpesa.setCd_ti_pasto(aSpesa.getTipo_pasto().getCd_ti_pasto());

            bp.setModel(context, missione);

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce il rientro dalla selezione di un "Tipo Spesa".
     * Inizializzo alcuni attributi del dettaglio con quelli del Tipo Spesa.
     * Se il tipo spesa selezionato e' un rimborsoKm inizializzo la divisa e il cambio della spesa
     * con quelli di default (EURO)
     */

    public Forward doBringBackSearchFind_tipo_spesa(ActionContext context, Missione_dettaglioBulk aSpesa, Missione_tipo_spesaBulk tipoSpesa) {
        try {
            if (tipoSpesa == null)        // Nessuna selezione
                return context.findDefaultForward();

            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            aSpesa.setTipo_spesa(tipoSpesa);
            aSpesa.setCd_ti_spesa(aSpesa.getTipo_spesa().getCd_ti_spesa());
            aSpesa.setPercentuale_maggiorazione(tipoSpesa.getPercentuale_maggiorazione());
            aSpesa.setDs_ti_spesa(tipoSpesa.getDs_ti_spesa());
            aSpesa.impostaTipologiaSpesa();

            if (aSpesa.isRimborsoKm())
                missione = bp.inizializzaDivisaCambioPerRimborsoKm(context, aSpesa);

            bp.setModel(context, missione);

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo effettua una cancellazione della diaria prima di poter proseguire con la cancellazione
     * di un dettaglio di spesa
     */

    public Forward doCancellaDiariaPerCancellazioneSpesa(ActionContext context, OptionBP option) throws BusinessProcessException, ValidationException {
        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
        MissioneBulk missione = (MissioneBulk) bp.getModel();

        if (option.getOption() == OptionBP.YES_BUTTON) {
            // Cancella Diaria
            bp.cancellaDiaria(context);
            missione = (MissioneBulk) bp.getModel();
            missione.setSpeseInserite(false);

            // Chiama il metodo "removeFromSpeseMissioneColl" di MissioneBulk
            bp.getSpesaController().remove(context);
        }

        return context.findDefaultForward();
    }

    /**
     * Il metodo effettua una cancellazione della diaria prima di poter proseguire con la creazione di
     * un nuovo dettaglio di spesa
     */

    public Forward doCancellaDiariaPerCreazioneSpesa(ActionContext context, OptionBP option) throws BusinessProcessException {
        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
        MissioneBulk missione = (MissioneBulk) bp.getModel();

        if (option.getOption() == OptionBP.YES_BUTTON) {
            // Cancella Diaria
            bp.cancellaDiaria(context);
            missione = (MissioneBulk) bp.getModel();
            missione.setSpeseInserite(false);

            // Chiamo il metodo "addDetail" di CRUDMissione_spesaController
            bp.getSpesaController().add(context);
        }

        return context.findDefaultForward();
    }

    /**
     * Il metodo effettua una cancellazione della diaria prima di poter proseguire con la modifica di
     * un dettaglio di spesa
     */
    public Forward doCancellaDiariaPerModificaSpesa(ActionContext context, OptionBP option) throws BusinessProcessException {
        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
        MissioneBulk missione = (MissioneBulk) bp.getModel();

        if (option.getOption() == OptionBP.YES_BUTTON) {
            // Cancella Diaria
            bp.cancellaDiaria(context);
            missione = (MissioneBulk) bp.getModel();
            missione.setSpeseInserite(false);

            bp.editaSpesa(context);
        }

        return context.findDefaultForward();
    }

    /**
     * Attiva la ricerca libera di una missione
     */

    public Forward doCerca(ActionContext context) throws java.rmi.RemoteException, InstantiationException, javax.ejb.RemoveException {
        CRUDMissioneBP bp = (CRUDMissioneBP) context.getBusinessProcess();

        //	Se chi invoca la ricerca delle missioni e' il Fondo Economale
        if (bp instanceof IDocumentoAmministrativoSpesaBP && ((IDocumentoAmministrativoSpesaBP) bp).isSpesaBP())
            return basicDoCerca(context);
        return super.doCerca(context);
    }

    /**
     * A fronte della modifica della data di registrazione vengono validati il terzo e i tipi trattamento.
     * Se i controlli di validazione non hanno successo l'utente deve confermare o meno se proseguire con
     * la modifica della data e quindi con la re-inizializzazione dei dati non piu' validi
     * Il metodo gestisce la risposta dell'utente.
     */

    public Forward doConfermaDataRegistrazioneChange(ActionContext context, OptionBP optionBP) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            java.sql.Timestamp oldDataRegistrazione = (java.sql.Timestamp) optionBP.getAttribute("dataReg");
            int errorCodeTerzo = ((Integer) optionBP.getAttribute("errorCodeTerzo")).intValue();

            if (optionBP.getOption() == OptionBP.YES_BUTTON) {
                switch (errorCodeTerzo) {
                    case 2: {
                        missione.inizializzaTerzo();
                        break;
                    }
                    case 10: {
                        bp.findTipiTrattamento(context);
                        break;
                    }
                }
            } else
                missione.setDt_registrazione(oldDataRegistrazione);

            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * A fronte di una modifica in diminuzione della data di fine di una missione con dettagli di spesa/diaria o tappe
     * viene chiesto all'utente se proseguire con la cancellazione di questi ultimi o meno.
     * Il metodo gestisce la risposta dell'utente e quindi :
     * - SI : cancello i dettagli e le tappe e modifico la data di fine
     * - NO : annullo l amodifica della data
     */

    public Forward doConfermaFineMissioneChange(ActionContext context, OptionBP option) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (option.getOption() == OptionBP.YES_BUTTON) {
                // Cancello la configurazione delle tappe
                bp.cancellaTappeMissione(context);

                // Cancello i dettagli di spesa/diaria
                bp.cancellaDettagliMissione(context);
                ;
                missione = (MissioneBulk) bp.getModel();

                //	Resetto la collection dei giorni in modo da ricostruirla
                missione.setCollectionGiorni(null);
            }

            // Annullo la modifica
            if (option.getOption() == OptionBP.NO_BUTTON)
                missione.setDt_fine_missione((java.sql.Timestamp) option.getAttribute("oldDataFine"));

            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * A fronte di una modifica in aumento della data di fine di una missione con dettagli di spesa/diaria o tappe
     * viene chiesto all'utente se proseguire con la cancellazione di questi ultimi o meno.
     * Il metodo gestisce la risposta dell'utente e quindi :
     * - SI : cancello i dettagli di spesa/diaria, forzo la riconfigurazione delle tappe e modifico la data di fine
     * - NO : annullo la modifica della data
     */

    public Forward doConfermaFineMissioneInAumentoChange(ActionContext context, OptionBP option) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();
            java.sql.Timestamp oldDataFine = (java.sql.Timestamp) option.getAttribute("oldDataFine");

            if (option.getOption() == OptionBP.YES_BUTTON) {
                // Se allungo la durata della missione annulla la data fine validita dell'ultima tappa
                Missione_tappaBulk ultimaTappa = (Missione_tappaBulk) missione.getTappeMissioneColl().get(missione.getTappeMissioneColl().size() - 1);
                if (ultimaTappa != null) {
                    ultimaTappa.setDt_fine_tappa(null);
                    missione.setTappeConfigurate(false);
                }

                //	Resetto la collection dei giorni in modo da ricostruirla
                missione.setCollectionGiorni(null);

                // Cancello i dettagli di spesa/diaria
                bp.cancellaDettagliMissione(context);
            }
            // Annullo la modifica
            if (option.getOption() == OptionBP.NO_BUTTON)
                missione.setDt_fine_missione(oldDataFine);

            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * A fronte di una modifica della data di inizio di una missione occorre :
     * - ricare i Tipi Rapporto in modo che siano validi alla nuova data inizio missione
     * - cancellare le tappe e/o dettagli se presenti
     * All'utente viene chiesto se proseguire con queste operazioni o meno.
     * Il metodo gestisce la risposta dell'utente e quindi :
     */

    public Forward doConfermaInizioMissioneChange(ActionContext context, OptionBP option) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (option.getOption() == OptionBP.YES_BUTTON) {
                // Carico i Tipi Rapporto validi alla data inizio missione
                missione.setInquadramenti(null);
                missione.setRif_inquadramento(null);
                missione.setTipi_trattamento(null);
                missione.setCd_trattamento(null);
                missione.setTipo_trattamento(null);
                bp.findTipiRapporto(context);

                // Cancello la configurazione delle tappe
                bp.cancellaTappeMissione(context);

                // Cancello i dettagli di spesa/diaria
                bp.cancellaDettagliMissione(context);
                missione = (MissioneBulk) bp.getModel();

                missione.setCollectionGiorni(null);
            }
            // Annullo la modifica
            if (option.getOption() == OptionBP.NO_BUTTON)
                missione.setDt_inizio_missione((java.sql.Timestamp) option.getAttribute("oldDataIniziale"));

            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * A fronte di una nuova selezione dell'inquadramento del terzo viene chiesto all'utente se proseguire con la
     * cancellazione dei dettagli di spesa/diaria della missione.
     * Il metodo gestisce la risposta dell'utente e quindi :
     * - SI : cancello i dettagli di spesa/diaria e modifico l'inquadramento
     * - NO : annullo la modifica dell'inquadramento
     */

    public Forward doConfermaInquadramentoChange(ActionContext context, OptionBP option) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (option.getOption() == OptionBP.YES_BUTTON) {
                // Cancello i dettagli di spesa/diaria
                bp.cancellaDettagliMissione(context);
            }
            // Annullo la modifica
            if (option.getOption() == OptionBP.NO_BUTTON)
                missione.setRif_inquadramento((Rif_inquadramentoBulk) option.getAttribute("oldInquadramento"));

            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * Alla selezione del bottone "Ricerca" il sistema visualizza il primo Tab
     */

    public Forward doConfermaNuovaRicerca(ActionContext context, int option) {
        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
        bp.setTab("tab", "tabTestata");

        return super.doConfermaNuovaRicerca(context, option);
    }

    /**
     * Il metodo gestisce la fine della modalita modifica/inserimento dettaglio spesa
     */

    public Forward doConfermaSpesa(ActionContext context) {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);

            bp.confermaSpesa(context);

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la fine della modalita modifica/inserimento tappa
     */

    public Forward doConfermaTappa(ActionContext context) {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);

            bp.confermaTappa(context);

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * A fronte di una inizializzazione del terzo viene chiesto all'utente se proseguire con la cancellazione dei
     * dettagli di spesa/diaria della missione.
     * Il metodo gestisce la risposta dell'utente e quindi :
     * - SI : cancello i dettagli di spesa/diaria e inizializzo il terzo
     * - NO : annullo l'inizializzazione del terzo
     */

    public Forward doConfermaTerzoBlankSearchChange(ActionContext context, OptionBP option) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);

            if (option.getOption() == OptionBP.YES_BUTTON) {
                // Cancello i dettagli di spesa/diaria
                bp.cancellaDettagliMissione(context);

                // Inizializzo i campi del Terzo
                MissioneBulk missione = (MissioneBulk) bp.getModel();
                missione.inizializzaTerzo();
            }

            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * A fronte di una nuova selezione del terzo viene chiesto all'utente se proseguire con la cancellazione dei
     * dettagli di spesa/diaria della missione.
     * Il metodo gestisce la risposta dell'utente e quindi :
     * - SI : cancello i dettagli di spesa/diaria e modifico il terzo
     * - NO : annullo la modifica del terzo
     */

    public Forward doConfermaTerzoFreeSearchChange(ActionContext context, OptionBP option) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);

            if (option.getOption() == OptionBP.YES_BUTTON) {
                // Cancello i dettagli di spesa/diaria
                bp.cancellaDettagliMissione(context);

                return freeSearch(context, bp.getFormField("find_terzo"));
            }

            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * A fronte di una nuova selezione del terzo viene chiesto all'utente se proseguire con la cancellazione dei
     * dettagli di spesa/diaria della missione.
     * Il metodo gestisce la risposta dell'utente e quindi :
     * - SI : cancello i dettagli di spesa/diaria e modifico il terzo
     * - NO : annullo la modifica del terzo
     */

    public Forward doConfermaTerzoSearchChange(ActionContext context, OptionBP option) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);

            if (option.getOption() == OptionBP.YES_BUTTON) {
                // Cancello i dettagli di spesa/diaria
                bp.cancellaDettagliMissione(context);

                return search(context, bp.getFormField("find_terzo"), null);
            }

            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doConfermaTerzoSearchChangeForCRUD(ActionContext context, OptionBP option) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);

            if (option.getOption() == OptionBP.YES_BUTTON) {
                // Cancello i dettagli di spesa/diaria
                bp.cancellaDettagliMissione(context);

                return doCRUD(context, "main.crea_terzo");
            }

            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * A fronte di una nuova selezione del Tipo Anagrafico viene chiesto all'utente se reinizializzare in terzo ed
     * cancellare i dettagli di spesa/diaria della missione.
     * Il metodo gestisce la risposta dell'utente e quindi :
     * - SI : cancello i dettagli di spesa/diaria, inizializzo il terzo e modifico il Tipo Anagrafico
     * - NO : annullo la modifica del Tipo Anagrafico
     */

    public Forward doConfermaTipoAnagraficoChange(ActionContext context, OptionBP option) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (option.getOption() == OptionBP.YES_BUTTON) {
                // Cancello i dettagli di spesa/diaria
                bp.cancellaDettagliMissione(context);
                missione = (MissioneBulk) bp.getModel();

                // Inizializzo i campi del Terzo
                missione.inizializzaTerzo();
            }
            // Annullo la modifica
            if (option.getOption() == OptionBP.NO_BUTTON)
                missione.setTi_anagrafico((String) option.getAttribute("oldTipoAnagrafico"));

            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * A fronte di una nuova selezione del Tipo Rapporto viene chiesto all'utente se cancellare i dettagli di spesa/diaria
     * e ricaricare gli Inquadramenti e i Tipi Trattamento.
     * Il metodo gestisce la risposta dell'utente e quindi :
     * - SI : cancello i dettagli di spesa/diaria, inizializzo Inquadramenti e Tipi Trattamento e modifico il
     * Tipo Rapporto
     * - NO : annullo la modifica del Tipo Rapporto
     */
    public Forward doConfermaTipoRapportoChange(ActionContext context, OptionBP option) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (option.getOption() == OptionBP.YES_BUTTON) {
                // Cancello i dettagli di spesa/diaria
                bp.cancellaDettagliMissione(context);

                // Carico gli Inquadramenti e i Tipi trattamento del nuovo Tipo rapporto
                bp.findInquadramentiETipiTrattamento(context);
            }

            // Annullo la modifica
            if (option.getOption() == OptionBP.NO_BUTTON)
                missione.setTipo_rapporto((Tipo_rapportoBulk) option.getAttribute("oldTipoRapporto"));

            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * A seguito della configurazione delle tappe il sistema verifica se mantenere o meno il compenso o la scadenza
     * eventualmente associati alla missione :
     * - se le tappe sono state configurate in modo da rendere necessaria la creazione
     * di un compenso devo cancellare la scadenza di obbligazione eventualmente associata
     * alla missione.
     * - se invece le tappe sono state configurate in modo da non richiedere l'associazione
     * della missione con un compenso devo annullare o cancellare fisicamente il compenso
     * eventualmente associato. Sara' la procedura a decidere che tipo cancellazione fare e
     * ad eseguirla
     */

    public void doControlliPerCompenso(ActionContext context, MissioneBulk missione) throws BusinessProcessException {
        if (missione.isCompensoObbligatorio() && missione.isMissioneConObbligazione()) {
            setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "L'associazione della missione con la scadenza di impegno e' stata rimossa!");
            missione.addToDocumentiContabiliCancellati(missione.getObbligazione_scadenzario());
            missione.setObbligazione_scadenzario(new Obbligazione_scadenzarioBulk());
        }
        if (!missione.isCompensoObbligatorio() && missione.isMissioneConCompenso()) {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            bp.cancellaCompensoPhisically(context);
        }
    }

    /**
     * Pre-condition : missione con anticipo ed scadenza. Seleziono il bottone di fine inserimento
     * spese che ricalcola il totale della missione
     * Post-condition :	se il nuovo importo della missione e' minore a quello dell'anticipo
     * devo scollegare la scadenza
     */

    public void doControlliPerFineSpese(ActionContext context, MissioneBulk missione) throws BusinessProcessException {
        if (missione.isMissioneConObbligazione() && missione.isImportoAnticipoMaggioreDiMissione()) {
            setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "L'associazione della missione con la scadenza di impegno e' stata rimossa!");
            missione.addToDocumentiContabiliCancellati(missione.getObbligazione_scadenzario());
            missione.setObbligazione_scadenzario(new Obbligazione_scadenzarioBulk());
        }
    }

    /**
     * Il metodo gestisce l'apertura della finestra di creazione del compenso ed effettua le opportune
     * inizializzazioni del compenso
     */

    private Forward doCreaCompenso(ActionContext context) throws BusinessProcessException {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            context.addHookForward("bringback", this, "doBringBackCompenso");

            it.cnr.contab.compensi00.bp.CRUDCompensoBP compensoBP = (it.cnr.contab.compensi00.bp.CRUDCompensoBP) context.getUserInfo().createBusinessProcess(context, "CRUDCompensoBP", new Object[]{"MRSWTh"});
            compensoBP.reset(context);
            CompensoBulk compenso = (CompensoBulk) compensoBP.getModel();

            it.cnr.contab.compensi00.ejb.CompensoComponentSession component = (it.cnr.contab.compensi00.ejb.CompensoComponentSession) bp.createComponentSession("CNRCOMPENSI00_EJB_CompensoComponentSession", it.cnr.contab.compensi00.ejb.CompensoComponentSession.class);
            compenso = component.inizializzaCompensoPerMissione(context.getUserContext(), compenso, missione);
            compensoBP.setModel(context, compenso);

            return context.addBusinessProcess(compensoBP);
        } catch (Throwable ex) {
            throw new BusinessProcessException(ex);
        }
    }

    /**
     * Il metodo gestisce l'inizio della modalita modifica dettaglio di spesa verificando
     * la fattibilità dell'operazione
     */

    public Forward doEditaSpesa(ActionContext context) {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            missione.isInserimentoSpeseModificabile();

            // 	Se ho la diaria ne propongo la cancellazione automatica
            //	per poter proseguire con la modifica della spesa
            if ((missione.getDiariaMissioneColl() != null) && (!missione.getDiariaMissioneColl().isEmpty()))
                return openConfirm(context, "La Diaria verra' cancellata. Proseguire ?", OptionBP.CONFIRM_YES_NO, "doCancellaDiariaPerModificaSpesa");
            else {
                bp.editaSpesa(context);
                missione.setSpeseInserite(false);
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }

    /**
     * Il metodo gestisce l'inizio modalita modifica tappa
     */

    public Forward doEditaTappa(ActionContext context) {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            bp.editaTappa(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la modalita alla data della tappa
     */

    public Forward doCambiaDataTappa(ActionContext context) {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            bp.setDiariaSiNo(context);
            bp.isDiariaEditable(context.getUserContext());
            bp.isRimborsoEditable(context.getUserContext());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce un comando di cancellazione logica/fisica della missione
     */
    public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {
        try {
            fillModel(context);

            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            if (!bp.isEditing()) {
                bp.setMessage("Non è possibile cancellare in questo momento");
            } else {
                bp.delete(context);
                MissioneBulk missione = (MissioneBulk) bp.getModel();
                MissioneComponentSession session = (MissioneComponentSession) bp.createComponentSession();
                if (session.isMissioneAnnullata(context.getUserContext(), missione)) {
                    bp.edit(context, missione);
                    bp.setMessage("Annullamento effettuato");
                } else {
                    bp.reset(context);
                    bp.setMessage("Cancellazione effettuata");
                }
            }
            return context.findDefaultForward();

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce l'eliminazione dell'associazione missione-scadenza.
     * Se tolgo la relazione tra la missione e una scadenza devo prevedere
     * l'aggiornamento/inserimento a db di tale scadenza con im_associato_doc_amm=0
     * e se la missione usa il fondo economale devo eliminare questa associazione
     */

    public Forward doEliminaScadenzaObbligazione(ActionContext context) {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (!missione.isMissioneConObbligazione()) {
                missione.setObbligazione_scadenzario(new Obbligazione_scadenzarioBulk());
                throw new it.cnr.jada.comp.ApplicationException("Non esiste alcuna scadenza da eliminare!");
            }

            missione.addToDocumentiContabiliCancellati(missione.getObbligazione_scadenzario());
            missione.setObbligazione_scadenzario(new Obbligazione_scadenzarioBulk());
            if (missione.getStato_pagamento_fondo_eco().compareTo(MissioneBulk.FONDO_ECO) == 0)
                missione.setStato_pagamento_fondo_eco(MissioneBulk.NO_FONDO_ECO);
            bp.setDirty(true);

            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la selezione del bottone di fine configurazione tappe.
     * In particolare è gestita :
     * - la cancellazione fisica delle tappe se queste sono gia' state salvate in tabella
     * (essendo dt_inizio_tappa parte della chiave non mi e' possibile :
     * - modificare dt_inizio_tappa (ora) di una tappa gia' salvata
     * - cancellare una tappa gia' presente in tabella la cui data di inizio e' cambiata (o resettata)
     * - la creazione delle tappe che l'utente non ha configurato facendole ereditare dalla precedente,
     * - l'inizializzazione corretta della data/ora di inizio/fine tappa di ogni tappa
     * - la predisposizione in base alla nuova configurazione :
     * - della cancellazione fisica del compenso se non piu' richiesto
     * - della cancellazione dell'obbligazione se e' richiesto il compenso
     */

    public Forward doFineConfigurazioneTappa(ActionContext context) {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (!bp.isEditable() || bp.isSearching())
                return context.findDefaultForward();

            if (bp.isInputReadonly())
                throw new it.cnr.jada.comp.ApplicationException("Missione non modificabile!");

            if (missione.getTappeMissioneColl() == null || missione.getTappeMissioneColl().isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Inserire almeno la prima tappa !");

            if (missione.isTappeConfigurate()) {
                setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Tappe gia' configurate!");
                return context.findDefaultForward();
            }

            if (!missione.isTappeEstereCoerenti()) {
                setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Le tappe estere devono avere tutte la stessa tipologia di Trattamento di missione!");
                return context.findDefaultForward();
            }

            if (missione.getCrudStatus() == OggettoBulk.NORMAL || missione.getCrudStatus() == OggettoBulk.TO_BE_UPDATED) {
                bp.cancellaTappePhisically(context);
                missione = (MissioneBulk) bp.getModel();
            }
            doControlliPerCompenso(context, missione);
            missione = (MissioneBulk) bp.getModel();

            bp.fineConfigurazioneTappa(context);

            if (missione.isMissioneConRimborso() && !bp.isRimborsoValidoPerDurataTappeEstere(context)) {
                missione.setTappeConfigurate(false);
                setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "La durata delle tappe estere è inferiore alle 24 ore: Trattamento alternativo di missione non consentito!");
                return context.findDefaultForward();
            }

            // Riseleziono la tappa gia' selezionata (refresh)
            bp.getTappaController().setSelection(context, bp.getTappaController().getSelection());
            String str = bp.getMessage();
            if (str != null)
                setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, str + "\nConfigurazione terminata con successo.");
            else
                setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Configurazione terminata con successo.");

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la selezione del bottone di fine inserimento spese.
     * Viene eseguito un salvataggio temporaneo della missione,viene lanciata la procedura per la
     * gestione abbattimenti (spese eleggibili) e per la generazione della diaria
     */

    public Forward doFineInserimentoSpese(ActionContext context) {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            MissioneBulk oldMissione = null;
            if (missione.getMissioneIniziale() != null)
                oldMissione = (MissioneBulk) missione.getMissioneIniziale().clone();

            if ((bp.isEditable()) && (!bp.isSearching())) {
                if (bp.isInputReadonly())
                    throw new it.cnr.jada.comp.ApplicationException("Missione non modificabile!");

                if (missione.isSpeseInserite()) {
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Spese gia' confermate!");
                    return context.findDefaultForward();
                }
                //	Salvo temporaneamente la missione per potere lanciare le
                //  procedure di generazione Diaria + Abbattimenti
                missione = bp.generaDiaria(context, missione);

                // A differenza della diaria, il rimborso viene generato solo se previsto
                // Controllo che non sia già inserito (poichè non viene sempre cancellato come la diaria)
                if (missione.isMissioneConRimborso() && (missione.getRimborsoMissioneColl() == null || missione.getRimborsoMissioneColl().isEmpty()))
                    missione = bp.generaRimborso(context, missione);

                //	Verifico se devo scollegare la scadenza
                doControlliPerFineSpese(context, missione);

                missione.setSpeseInserite(true);
                missione.setMissioneIniziale(oldMissione);

                if (missione.isMissioneConRimborso())
                    doTab(context, "tab", "tabDettaglioRimborso");
                else
                    doTab(context, "tab", "tabDettaglioDiaria");
                bp.setModel(context, missione);
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la selezione di un terzo.
     * Se la missione possiede dei dettagli di spesa/diaria viene chiesto all'utente se proseguire con la loro cancellazione.
     */

    public Forward doFreeSearchFind_terzo(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            bp.fillModel(context);

            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (bp.isEditable() && !bp.isSearching() && missione.getDt_inizio_missione() == null)
                throw new it.cnr.jada.comp.ApplicationException("Impostare la data di inizio missione");

            if (missione.getDettagliMissioneColl().isEmpty())
                return freeSearch(context, bp.getFormField("find_terzo"));
            else {
                OptionBP option = openConfirm(context, "I dettagli della missione verranno cancellati. Proseguire ??", OptionBP.CONFIRM_YES_NO, "doConfermaTerzoFreeSearchChange");
                return option;
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il metodo gestisce la ricerca del tipo spesa assicurandosi che l'utente abbia gia' selezionato
     * un giorno.
     * Il giorno serve per recuperare la configurazione della relativa tappa, che, nel caso di selezione
     * multipla dei giorni, avra' area gografica e nazione uguale alle tappe degli altri giorni
     */

    public Forward doFreeSearchFind_tipo_spesa(ActionContext context) {
        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
        MissioneBulk missione = (MissioneBulk) bp.getModel();

        java.sql.Timestamp primoGG = missione.getPrimoGiornoSpesaSelezionato();
        if (primoGG == null)
            throw new it.cnr.jada.action.MessageToUser("Selezionare il giorno !");

        return freeSearch(context, bp.getSpesaController().getFormField("find_tipo_spesa"));
    }

    /**
     * Il metodo gestisce l'apertura della finestra del compenso in compenso
     */

    private Forward doModificaCompenso(ActionContext context) throws BusinessProcessException {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            it.cnr.contab.compensi00.bp.CRUDCompensoBP nbp = it.cnr.contab.compensi00.bp.CRUDCompensoBP.getBusinessProcessFor(context, missione.getCompenso(), "MRSWTh");
            nbp.edit(context, missione.getCompenso());
            missione.setCompenso((CompensoBulk) nbp.getModel());
            missione.getCompenso().setStatoCompensoToEseguiCalcolo();
            it.cnr.contab.compensi00.ejb.CompensoComponentSession component = (it.cnr.contab.compensi00.ejb.CompensoComponentSession) bp.createComponentSession("CNRCOMPENSI00_EJB_CompensoComponentSession", it.cnr.contab.compensi00.ejb.CompensoComponentSession.class);
            CompensoBulk compenso = component.inizializzaCompensoPerMissione(context.getUserContext(), missione.getCompenso(), missione);
            nbp.setModel(context, compenso);

            context.addHookForward("bringback", this, "doBringBackCompenso");
            HookForward hook = (HookForward) context.findForward("bringback");

            return context.addBusinessProcess(nbp);
        } catch (Throwable ex) {
            throw new BusinessProcessException(ex);
        }
    }

    /**
     * Il metodo gestisce l'aggiornamento automatico della scadenza associata alla missione.
     * Al termine dell'aggiornamento viene messo un lock alla scadenza e vengono sincronizzate le scadenze
     * eventualmente elaborate dalla missione corrente
     */

    public Forward doModificaScadenzaInAutomatico(ActionContext context) {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();
            missione.calcolaConsuntivi();

            boolean viewMode = bp.isViewing();

            if (!missione.isMissioneConObbligazione())
                throw new it.cnr.jada.comp.ApplicationException("Selezionare la scadenza di impegno da modificare!");

            if ((missione.getImporto_scadenza_obbligazione() == null) || (missione.getImporto_scadenza_obbligazione().compareTo(new java.math.BigDecimal(0)) == 0))
                throw new it.cnr.jada.comp.ApplicationException("L'mporto della missione e' nullo!");

            Obbligazione_scadenzarioBulk scadenza = missione.getObbligazione_scadenzario();
            it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession h = it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP.getVirtualComponentSession(context, true);

            try {
                scadenza = (Obbligazione_scadenzarioBulk) h.modificaScadenzaInAutomatico(
                        context.getUserContext(),
                        scadenza,
                        missione.getImporto_scadenza_obbligazione(),
                        false);

                h.lockScadenza(context.getUserContext(), scadenza);
                missione.addToDefferredSaldi(scadenza.getObbligazione(), scadenza.getObbligazione().getSaldiInfo());
                missione.gestisciCambioSelezioneScadenza(scadenza);
                missione.setObbligazione_scadenzario(scadenza);
                bp.setDirty(true);
                bp.setModel(context, missione);

                return context.findDefaultForward();
            } catch (it.cnr.jada.comp.ComponentException e) {
                if (e.getDetail() instanceof it.cnr.contab.doccont00.comp.CheckDisponibilitaCassaFailed)
                    throw new it.cnr.jada.comp.ApplicationException(e.getDetail().getMessage());
                if (e.getDetail() instanceof it.cnr.contab.doccont00.comp.SfondamentoPdGException)
                    throw new it.cnr.jada.comp.ApplicationException(e.getDetail().getMessage());
                throw e;
            }
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Alla selezione del bottone di Ricerca il sistema abilita i campi della spesa o della tappa
     * nell'eventualità che l'utente si trovasse in stato di Edit su una Spesa o Tappa
     */

    public Forward doNuovaRicerca(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);

            if (bp.getSpesaController().isEditingSpesa())
                basicDoUndoSpesa(context);

            if (bp.isEditingTappa())
                basicDoUndoTappa(context);

            return super.doNuovaRicerca(context);

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * Alla selezione del bottone "Nuovo" il sistema visualizza il primo Tab
     */

    public Forward doNuovo(ActionContext context) {
        super.doTab(context, "tab", "tabTestata");
        return (super.doNuovo(context));
    }

    /**
     * Il metodo verifica che alla selezione di una banca il compenso eventualmente associato alla missione
     * sia modificabile. In caso contrario la selezione viene annulata
     */

    public Forward doOnBancaChange(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            BancaBulk oldBanca = ((MissioneBulk) bp.getModel()).getBanca();

            fillModel(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            try {
                missione.isCompensoModificabile();
            } catch (it.cnr.jada.comp.ApplicationException e) {
                missione.setBanca(oldBanca);
                return handleException(context, e);
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * Il metodo gestise la mancata disponibilita' di cassa in fase di associazione della missione ad
     * una scadenza.
     */

    public Forward doOnCheckDisponibilitaCassaFailed(ActionContext context, int option) {
        if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            try {
                boolean modified = fillModel(context);
                OptionRequestParameter userConfirmation = new OptionRequestParameter();
                userConfirmation.setCheckDisponibilitaDiCassaRequired(Boolean.FALSE);
                bp.setUserConfirm(userConfirmation);
                if (bp.isBringBack())
                    doConfermaRiporta(context, it.cnr.jada.util.action.OptionBP.OK_BUTTON);
                else
                    doSalva(context);
            } catch (Throwable e) {
                return handleException(context, e);
            }
        }
        return context.findDefaultForward();
    }

    /**
     * Metodo utilizzato per gestire la conferma dell'inserimento/modifica di una obbligazione che ha sfondato
     * la disponibilità per il contratto
     *
     * @param context <code>ActionContext</code> in uso.
     * @param option  Esito della risposta alla richiesta di sfondamento
     * @return <code>Forward</code>
     * @throws <code>RemoteException</code>
     */
    public Forward doOnCheckDisponibilitaContrattoFailed(ActionContext context, int option) {
        if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            try {
                boolean modified = fillModel(context);
                OptionRequestParameter userConfirmation = new OptionRequestParameter();
                userConfirmation.setCheckDisponibilitaContrattoRequired(Boolean.FALSE);
                bp.setUserConfirm(userConfirmation);
                if (bp.isBringBack())
                    doConfermaRiporta(context, it.cnr.jada.util.action.OptionBP.OK_BUTTON);
                else
                    doSalva(context);
            } catch (Throwable e) {
                return handleException(context, e);
            }
        }
        return context.findDefaultForward();
    }

    /**
     * Il metodo gestisce la valorizzazione della data di registrazione.
     * Nel caso si stia modificando la data di registrazione, il metodo :
     * - verifica se la modifica puo' essere effettuata
     * - valida la nuova data di registrazione
     * - verifica che il terzo e i tipi trattamento siano ancora validi.
     * Se la validazione non ha successo e ho associato un compenso non posso proseguire
     * Se la validazione non ha successo e non ho un compenso chiedo all'utente se proseguire e
     * quindi reinizializzare il terzo e ricaricarire i tipi trattamento
     */

    public Forward doOnDataRegistrazioneChange(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            java.sql.Timestamp oldData = ((MissioneBulk) bp.getModel()).getDt_registrazione();
            Rif_inquadramentoBulk aInquadramento = ((MissioneBulk) bp.getModel()).getRif_inquadramento();
            it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk aTrattamento = ((MissioneBulk) bp.getModel()).getTipo_trattamento();

            fillModel(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (bp.isSearching())
                return context.findDefaultForward();

            try {
                missione.isDataRegistrazioneModificabile();
                bp.validaDataRegistrazione(context);
            } catch (Throwable ex) {
                missione.setDt_registrazione(oldData);
                return handleException(context, ex);
            }

            int errorCodeTerzo = bp.validaTerzo(context, false);
            if (errorCodeTerzo == 2 || errorCodeTerzo == 8 || errorCodeTerzo == 10) {
                String msg = null;
                switch (errorCodeTerzo) {
                    case 2: {
                        msg = "Il terzo selezionato non è più valido!";
                        break;
                    }
                    case 10: {
                        msg = "Il tipo trattamento selezionato non è più valido!";
                        break;
                    }
                }
                if (missione.isMissioneConCompenso()) {
                    //	Missione legata a compensi. Il messaggio deve essere bloccante
                    missione.setDt_registrazione(oldData);
                    throw new it.cnr.jada.comp.ApplicationException(msg + " Operazione non consentita.");
                } else {
                    //	Se la missione non e' legata a compenso
                    //	(fase di inserimento) il messaggio deve solo essere informativo
                    msg = msg + " I dati verranno persi. Vuoi continuare?";
                }

                OptionBP option = openConfirm(context, msg, OptionBP.CONFIRM_YES_NO, "doConfermaDataRegistrazioneChange");
                option.addAttribute("dataReg", oldData);
                option.addAttribute("errorCodeTerzo", new Integer(errorCodeTerzo));
                return option;
            }

            bp.findTipiTrattamento(context);
            bp.ripristinaSelezioneTipoTrattamento(aTrattamento);

            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * Il metodo gestisce la modifica degli importi della diaria.
     * La modifica non e' fattibile se ho associato un compenso pagato o un anticipo rimborsato
     * La modifica deve rispettare alcune regole :
     * - la quota esente non deve superare l'importo netto
     * - l'importo netto non deve superare l'importo lordo
     * Se l'utente ha modificato l'importo netto o la quota esente di un dettaglio di diaria devo
     * impostare la flag "fl_diaria_manuale" a Y (cio' significa che il dettaglio e' stato modificato
     * manualmente)
     */

    public Forward doOnDiariaManuale(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);

            Missione_dettaglioBulk aVecchiaDiaria = (Missione_dettaglioBulk) bp.getDiariaController().getModel().clone();

            aVecchiaDiaria.getMissione().isInserimentoSpeseModificabile();

            fillModel(context);
            Missione_dettaglioBulk aDiaria = (Missione_dettaglioBulk) bp.getDiariaController().getModel();

            // Controlli sui valori nulli
            if (aDiaria.getIm_diaria_netto() == null)
                aDiaria.setIm_diaria_netto(new java.math.BigDecimal(0));
            if (aDiaria.getIm_quota_esente() == null)
                aDiaria.setIm_quota_esente(new java.math.BigDecimal(0));

            // Errore : quota esente > netto
            if (aDiaria.getIm_quota_esente().compareTo(aDiaria.getIm_diaria_netto()) > 0) {
                aDiaria.setIm_diaria_netto(aVecchiaDiaria.getIm_diaria_netto());
                aDiaria.setIm_quota_esente(aVecchiaDiaria.getIm_quota_esente());
                throw new it.cnr.jada.comp.ApplicationException("La quota esente non puo' essere superiore all'importo netto della diaria!");
            }

            // Errore : netto > lordo
            if (aDiaria.getIm_diaria_netto().compareTo(aDiaria.getIm_diaria_lorda()) > 0) {
                aDiaria.setIm_diaria_netto(aVecchiaDiaria.getIm_diaria_netto());
                aDiaria.setIm_quota_esente(aVecchiaDiaria.getIm_quota_esente());
                throw new it.cnr.jada.comp.ApplicationException("L'importo netto della diaria non puo' essere superiore all'importo lordo della diaria!");
            }

            if ((aVecchiaDiaria.getFl_diaria_manuale() != null) &&
                    (aVecchiaDiaria.getFl_diaria_manuale().booleanValue())) {
                return context.findDefaultForward();
            }

            if (aVecchiaDiaria.getIm_diaria_netto() == null)
                aVecchiaDiaria.setIm_diaria_netto(new java.math.BigDecimal(0));
            if (aVecchiaDiaria.getIm_quota_esente() == null)
                aVecchiaDiaria.setIm_quota_esente(new java.math.BigDecimal(0));

            if ((!aDiaria.getIm_diaria_netto().equals(aVecchiaDiaria.getIm_diaria_netto())) ||
                    (!aDiaria.getIm_quota_esente().equals(aVecchiaDiaria.getIm_quota_esente()))) {
                aDiaria.setFl_diaria_manuale(new Boolean(true));
            }

            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il metodo gestisce la modifica della data di fine missione, verifica se tale data puo' essere modificata
     * e verifica la validità del nuovo periodo di inizio/fine della missione.
     * Vengono ricostruite le collection dei giorni della missione.
     * Se cambio la data di fine missione :
     * - se la modidica e' in aumento l'applicazione propone la cancellazione di eventuali dettagli di
     * spesa/diaria e forza la riconfigurazione delle tappe
     * - se la modifica e' in diminuzione (mi comporto come se modificassi la data di inizio missione) l'applicazione
     * propone la cancellazione di eventuali tappe e dettagli dispesa/diaria
     */

    public Forward doOnFineMissioneChange(ActionContext context) {
        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
        java.sql.Timestamp oldDataFine = ((MissioneBulk) bp.getModel()).getDt_fine_missione();

        try {
            fillModel(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (bp.isSearching())
                return context.findDefaultForward();
            if (((MissioneBulk) bp.getModel()).getDt_fine_missione() == null) {
                // cancello la collection dei giorni
                if (missione.getCollectionGiorni() != null && !missione.getCollectionGiorni().isEmpty())
                    missione.setCollectionGiorni(null);
                return context.findDefaultForward();
            }
            try {
                missione.isDataFineModificabile();
            } catch (it.cnr.jada.comp.ApplicationException e) {
                missione.setDt_fine_missione(oldDataFine);
                return handleException(context, e);
            }

            try {
                bp.checkValiditaInizioFineMissione(context);
            } catch (MessageToUser e) {
                missione.setDt_fine_missione(oldDataFine);
                return handleException(context, e);
            }

            GregorianCalendar data_a = (GregorianCalendar) GregorianCalendar.getInstance();
            GregorianCalendar old_data_a = (GregorianCalendar) GregorianCalendar.getInstance();
            data_a.setTime(((MissioneBulk) bp.getModel()).getDt_fine_missione());
            if (oldDataFine != null)
                old_data_a.setTime(oldDataFine);

            int annoCompetenzaA = data_a.get(java.util.GregorianCalendar.YEAR);
            int annoOldCompetenzaA = 0;
            if (oldDataFine != null)
                annoOldCompetenzaA = old_data_a.get(java.util.GregorianCalendar.YEAR);

            if (bp.isTerzoCervellone(context.getUserContext(), (MissioneBulk) bp.getModel()) &&
                    annoOldCompetenzaA != 0 &&
                    annoCompetenzaA != annoOldCompetenzaA) {
                ((MissioneBulk) bp.getModel()).setDt_fine_missione(oldDataFine);
                throw new it.cnr.jada.comp.ApplicationException("Operazione non consentita. Non è possibile cambiare l'anno di Fine missione poichè il Terzo scelto potrebbe essere soggetto ad Agevolazioni per 'Rientro dei Cervelli'.");
            }

            if (oldDataFine == null)
                return context.findDefaultForward();

            GregorianCalendar gcUltimoGiorno = (GregorianCalendar) missione.getGregorianCalendar(oldDataFine).clone();
            GregorianCalendar gcFineMissione = missione.getGregorianCalendar(missione.getDt_fine_missione());
            if (gcFineMissione != null)
                gcFineMissione = (GregorianCalendar) gcFineMissione.clone();

            if (gcUltimoGiorno.equals(gcFineMissione))
                return context.findDefaultForward();

            if ((gcFineMissione != null) && (gcFineMissione.after(gcUltimoGiorno))) {
                // Se la data di fine missione è cambiata in aumento e ho dei dettagli di spesa/diaria
                // ne propongo la cancellazione
                if (missione.getDettagliMissioneColl().size() > 0) {
                    OptionBP option = openConfirm(context, "La configurazione delle tappe verra' modificata e i dettagli della missione verranno cancellati. Proseguire ?", OptionBP.CONFIRM_YES_NO, "doConfermaFineMissioneInAumentoChange");
                    option.addAttribute("oldDataFine", oldDataFine);
                    return option;
                } else if ((missione.getTappeMissioneColl() != null) && (missione.getTappeMissioneColl().size() > 0)) {
                    // Se allungo la durata della missione annulla la data fine validita dell'ultima tappa
                    Missione_tappaBulk ultimaTappa = (Missione_tappaBulk) missione.getTappeMissioneColl().get(missione.getTappeMissioneColl().size() - 1);
                    if (ultimaTappa != null) {
                        ultimaTappa.setDt_fine_tappa(null);
                        missione.setTappeConfigurate(false);
                    }
                }
                //	Resetto la collection dei giorni in modo da ricostruirla
                missione.setCollectionGiorni(null);
            } else {
                // Se la data di fine missione è cambiata e in diminuzione e ho delle tappe configurate e dei dettagli di spesa/diaria
                // ne propongo la cancellazione
                if (missione.getDettagliMissioneColl().size() > 0) {
                    OptionBP option = openConfirm(context, "La configurazione delle tappe e i dettagli della missione verranno cancellati. Proseguire ?", OptionBP.CONFIRM_YES_NO, "doConfermaFineMissioneChange");
                    option.addAttribute("oldDataFine", oldDataFine);
                    return option;
                }
                // Se la data di fine missione è cambiata in diminuzione e ho delle tappe configurate ne
                // propongo la cancellazione
                else if ((missione.getTappeMissioneColl() != null) && (missione.getTappeMissioneColl().size() > 0)) {
                    OptionBP option = openConfirm(context, "La configurazione delle tappe della missione verra' cancellata. Proseguire ?", OptionBP.CONFIRM_YES_NO, "doConfermaFineMissioneChange");
                    option.addAttribute("oldDataFine", oldDataFine);
                    return option;
                }

                //	Resetto la collection dei giorni in modo da ricostruirla
                if (gcFineMissione != null)
                    missione.setCollectionGiorni(null);
            }
            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }

    }

    /**
     * Il metodo gestisce la selezione di un giorno nel Tab dei consuntivi.
     * A fronte della selezione del giorno, l'applicazione evidenzia (seleziona)
     * le spese del giorno selezionato e calcola i vari importi di consuntivo
     * della missione per quel giorno
     */

    public Forward doOnGiornoConsuntivoChange(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            boolean isDirty = bp.isDirty();

            fillModel(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            bp.selezionaDettagliConsuntivo(context);
            missione.calcolaConsuntiviSpeseDelGiorno();
            missione.calcolaConsuntiviDiariaDelGiorno();
            missione.calcolaConsuntiviRimborsoDelGiorno();

            bp.setDirty(isDirty);

            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il metodo gestisce la selezione del giorno relativo al dettaglio di spesa.
     * Se l'utente seleziona dei giorni di spesa diversi devo inizializzare
     * tutti i campi della spesa perche' il tipo spesa, il tipo pasto e
     * il rimborso km sono ricercati in base alla tappa del giorno selezionato.
     * <p>
     * Se l'utente ha selezionato piu' di un giorno di spesa mi assicuro che
     * tutte le relative tappe abbiano stessa area geografica e progressivo nazione
     * (Questo serve affinche' i tipi spesa, pasto, rimborso km... siano
     * coerenti, cioe' abbiano ad esempio gli stessi massimali per ogni giorno)
     * <p>
     * Se ho impostato la valuta ma non il cambio alla selezione dei giorni
     * propongo il cambio valido alla data del primo giorno selezionato
     */
    public Forward doOnGiornoSpesaChange(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            fillModel(context);
            Missione_dettaglioBulk aSpesa = (Missione_dettaglioBulk) bp.getSpesaController().getModel();
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            //	Ripulisco i campi
            doBlankSearchFind_tipo_spesa(context, aSpesa);
            aSpesa.setCambio_spesa(null);

            //	Verifico coerenza tappe dei giorni selezionati
            if (missione.isSelezioneGiorniSpesaMultipla() && !missione.isSelezioneGiorniSpesaValida()) {
                missione.setCollectionGiorniSpeseSelezionati(null);
                throw new it.cnr.jada.comp.ApplicationException("Selezione non valida! Le relative tappe non hanno stessa area geografica e/o nazione.");
            }

            java.sql.Timestamp primoGiorno = missione.getPrimoGiornoSpesaSelezionato();

            //	Inizializzo il cambio valido al primo giorno della selezione
            if (primoGiorno != null && aSpesa.getCd_divisa_spesa() != null &&
                    (aSpesa.getCambio_spesa() == null || (aSpesa.getCambio_spesa().compareTo(new java.math.BigDecimal(0)) < 1)))
                bp.setCambioSpesaDefault(context, aSpesa);

            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il metodo gestisce la valorizzazione dell'importo della spesa.
     * Se si tratta di una spesa di tipo TRASPORTO devo porre di default l'importo
     * base maggiorazione uguale all'importo inserito dall'utente
     */
    public Forward doOnImSpesaDivisaChange(ActionContext context) {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (bp.isSearching())
                return context.findDefaultForward();

            Missione_dettaglioBulk spesa = (Missione_dettaglioBulk) bp.getSpesaController().getModel();

            if (spesa != null && spesa.isTrasporto()) {
                if (spesa.getIm_spesa_divisa() == null)
                    spesa.setIm_spesa_divisa(new java.math.BigDecimal(0));
                spesa.setIm_base_maggiorazione(spesa.getIm_spesa_divisa());
            }
            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * Il metodo gestisce la valorizzazione della data di inizio missione.
     * Il metodo verifica se la data puo' essere modificata e verifica se il periodo inizio/fine
     * missione e' ancora valido.
     * Per poter modificare la data di inizio missione devo proporre all'utente :
     * - di ricaricare i Tipi rapporto la cui validita' dipende dalla data inizio missione
     * - la cancellazione delle tappe e/o dettagli se presenti
     */

    public Forward doOnInizioMissioneChange(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            java.sql.Timestamp oldDataIniziale = ((MissioneBulk) bp.getModel()).getDt_inizio_missione();

            fillModel(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (bp.isSearching())
                return context.findDefaultForward();

            if (missione.getDt_inizio_missione() == null) {
                // cancello la collection dei giorni
                if (missione.getCollectionGiorni() != null && !missione.getCollectionGiorni().isEmpty())
                    missione.setCollectionGiorni(null);
                return context.findDefaultForward();
            }
            if (oldDataIniziale == null)
                return context.findDefaultForward();

            try {
                missione.isDataInizioModificabile();
            } catch (it.cnr.jada.comp.ApplicationException e) {
                missione.setDt_inizio_missione(oldDataIniziale);
                return handleException(context, e);
            }

            try {
                bp.checkValiditaInizioFineMissione(context);
            } catch (MessageToUser e) {
                missione.setDt_inizio_missione(oldDataIniziale);
                return handleException(context, e);
            }

            if (!missione.isInizioMissioneCambiato(missione.getDt_inizio_missione(), oldDataIniziale))
                return context.findDefaultForward();

            GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
            GregorianCalendar old_data_da = (GregorianCalendar) GregorianCalendar.getInstance();
            data_da.setTime(((MissioneBulk) bp.getModel()).getDt_inizio_missione());
            if (oldDataIniziale != null)
                old_data_da.setTime(oldDataIniziale);

            int annoCompetenzaDa = data_da.get(java.util.GregorianCalendar.YEAR);
            int annoOldCompetenzaDa = 0;
            if (oldDataIniziale != null)
                annoOldCompetenzaDa = old_data_da.get(java.util.GregorianCalendar.YEAR);

            if (bp.isTerzoCervellone(context.getUserContext(), (MissioneBulk) bp.getModel()) &&
                    annoOldCompetenzaDa != 0 &&
                    annoCompetenzaDa != annoOldCompetenzaDa) {
                ((MissioneBulk) bp.getModel()).setDt_inizio_missione(oldDataIniziale);
                throw new it.cnr.jada.comp.ApplicationException("Operazione non consentita. Non è possibile cambiare l'anno di Inizio missione poichè il Terzo scelto potrebbe essere soggetto ad Agevolazioni per 'Rientro dei Cervelli'.");
            }

            // Se ho dei dettagli di spesa/diaria (avro' sicuramente anche le tappe) ne propongo la cancellazione
            if (!missione.getDettagliMissioneColl().isEmpty()) {
                OptionBP option;
                option = openConfirm(context, "La configurazione delle tappe e i dettagli della missione verranno cancellati. Proseguire ?", OptionBP.CONFIRM_YES_NO, "doConfermaInizioMissioneChange");
                option.addAttribute("oldDataIniziale", oldDataIniziale);

                return option;
            }
            // Se ho delle tappe (ma non i dettagli) ne  propongo la cancellazione
            else if (missione.getTappeMissioneColl() != null && !missione.getTappeMissioneColl().isEmpty()) {
                OptionBP option;
                option = openConfirm(context, "La configurazione delle tappe della missione verra' cancellata. Proseguire ?", OptionBP.CONFIRM_YES_NO, "doConfermaInizioMissioneChange");
                option.addAttribute("oldDataIniziale", oldDataIniziale);
                return option;
            }
            // 	Se ho gia' caricato i tipi rapporto li ricarico in base alla nuova data di inizio missione (validita')
            // 	Se i tipi rapporto non sono stati caricati ma il terzo e' gia valorizzato li ricarico (caso in cui
            //	non erano stati trovati)
            if ((missione.getTipi_rapporto() != null && !missione.getTipi_rapporto().isEmpty()) ||
                    ((missione.getTipi_rapporto() == null || missione.getTipi_rapporto().isEmpty()) && missione.areCampiPerRicercaTipiRapportoValorizzati())) {
                missione.setInquadramenti(null);
                missione.setRif_inquadramento(null);
                missione.setTipi_trattamento(null);
                missione.setCd_trattamento(null);
                missione.setTipo_trattamento(null);

                bp.findTipiRapporto(context);
            }
            // Altrimenti cancello la collection dei giorni
            if (missione.getCollectionGiorni() != null && !missione.getCollectionGiorni().isEmpty())
                missione.setCollectionGiorni(null);

            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il  metodo gestisce la selezione dell'inquadramento.
     * Se la missione possiede dei dettagli di spesa/diaria devo proporre all'utente la loro cancellazione
     * per poeter procedere con la modifica
     */

    public Forward doOnInquadramentoChange(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            Rif_inquadramentoBulk oldInquadramento = ((MissioneBulk) bp.getModel()).getRif_inquadramento();

            fillModel(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (!missione.getDettagliMissioneColl().isEmpty() &&
                    oldInquadramento != null && oldInquadramento.getPg_rif_inquadramento() != null &&
                    !oldInquadramento.getPg_rif_inquadramento().equals(missione.getPg_rif_inquadramento())) {
                OptionBP option = openConfirm(context, "I dettagli della missione verranno cancellati. Proseguire ??", OptionBP.CONFIRM_YES_NO, "doConfermaInquadramentoChange");
                option.addAttribute("oldInquadramento", oldInquadramento);
                return option;
            }
            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il metodo gestisce la selezione della flag "commerciale/istituzionale"
     * L'utente non può modificare la missione da Istituzionale a Commerciale o viceversa se l'eventuale
     * compenso associato alla missione non e' piu' modificabile o se la missione è collegata ad
     * anticipo rimborsato.
     * Alla modifica del tipo da istituzionale a commerciale vanno ricaricati i tipi trattamento. Se
     * l'applicazione non ne trova di eleggibili annullo la modifica
     */

    public Forward doOnIstituzionaleCommercialeChange(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            String oldTipo = ((MissioneBulk) bp.getModel()).getTi_istituz_commerc();
            java.util.Collection oldTipiTrattamento = ((MissioneBulk) bp.getModel()).getTipi_trattamento();
            Tipo_trattamentoBulk oldTipoTrattamento = ((MissioneBulk) bp.getModel()).getTipo_trattamento();

            fillModel(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (bp.isSearching())
                return context.findDefaultForward();

            try {
                missione.isIstituzionaleCommercialeModificabile();
            } catch (it.cnr.jada.comp.ApplicationException e) {
                missione.setTi_istituz_commerc(oldTipo);
                return handleException(context, e);
            }
            missione.setTipi_trattamento(null);
            missione.setTipo_trattamento(null);
            //	Ricerco i tipi trattamento solo se ho tutte le condizioni di ricerca
            if (missione.areCampiPerRicercaTipiTrattamentoValorizzati())
                bp.findTipiTrattamento(context);

            //  Se la missione ha collegato un compenso e se la nuova selezione non fornisce
            //	alcun tipo di trattamento allora ripristino la selezione precedente
            if (missione.isMissioneConCompenso() && (missione.getTipi_trattamento() == null || missione.getTipi_trattamento().isEmpty())) {
                missione.setTi_istituz_commerc(oldTipo);
                missione.setTipo_trattamento(oldTipoTrattamento);
                missione.setTipi_trattamento(oldTipiTrattamento);
            } else
                missione.setCd_trattamento(null);

            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il metodo gestisce la selezione di una modalità di pagamento.
     * L'utente non può modificare le modalita se ho associato un compenso pagato
     * Alla selezione della Modalita di Pagamento l'applicazione ricerca le banche eleggibili
     */

    public Forward doOnModalitaPagamentoChange(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            Rif_modalita_pagamentoBulk oldModalita = ((MissioneBulk) bp.getModel()).getModalita_pagamento();

            fillModel(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            try {
                missione.isCompensoModificabile();
            } catch (it.cnr.jada.comp.ApplicationException e) {
                missione.setModalita_pagamento(oldModalita);
                return handleException(context, e);
            }

            if (missione.getModalita_pagamento() != null) {
                MissioneComponentSession component = (MissioneComponentSession) bp.createComponentSession();
                java.util.Collection coll = component.findListabanche(context.getUserContext(), missione);

                //	Assegno di default la prima banca tra quelle selezionate
                if (coll == null || coll.isEmpty())
                    missione.setBanca(null);
                else
                    missione.setBanca((it.cnr.contab.anagraf00.core.bulk.BancaBulk) new java.util.Vector(coll).firstElement());
            } else {
                missione.setBanca(null);
            }
            bp.setModel(context, missione);
            bp.resyncChildren(context);
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * Il metodo gestisce il salvataggio della missione e l' eventuale salvataggio del compenso
     */

    private Forward doOnSalvataggio(ActionContext context, MissioneBulk missione) {
        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
        MissioneBulk oldMissioneIniziale = null;

        try {
            if (missione.getMissioneIniziale() == null)
                oldMissioneIniziale = new MissioneBulk();
            else
                oldMissioneIniziale = (MissioneBulk) missione.getMissioneIniziale().clone();

            // 	Salvo la missione.
            doSalvaMissione(context, missione);        // reinizializza 'missioneIniziale'

            //	Nel metodo 'doSalvaMissione' e' stato fatto un setModel() ma
            //	non reinizializzo la variabile 'missione' con un getModel()
            //	altrimenti mi perderei il valore precedente delle variabili
            //	che mi servono per capire se salvare o meno il compenso.
            //	Se facessi un getModel caricherei la missione salvata a DB

            // Se il compenso e' obbligatorio e la missione e' definitiva procedo con il suo salvataggio
            if (missione.isCompensoObbligatorio() && missione.isMissioneDefinitiva()) {
                //	Finche' non termina il salvataggio del compenso
                //	devo tenere in memoria la missione iniziale prima del precente
                //	salvataggio
                ((MissioneBulk) bp.getModel()).setMissioneIniziale((MissioneBulk) oldMissioneIniziale.clone());

                if (missione.getTipo_trattamento() == null || missione.getTipo_trattamento().getCd_trattamento() == null)
                    throw new it.cnr.jada.comp.ApplicationException("Selezionare un Tipo Trattamento per poter creare un compenso !");

                if (!missione.isMissioneConCompenso())
                    return doCreaCompenso(context);
                else if (missione.isCompensoDaModificareInManuale())
                    return doModificaCompenso(context);
                else if (missione.isCompensoDaModificareInAutomatico())
                    bp.updateCompenso(context, missione);
            }
            // Fine salvataggio missione nel caso il compenso non sia previsto
            bp.commitUserTransaction();
            bp.setCarryingThrough(false);
            bp.setMessage("Salvataggio terminato con successo.");

            return context.findDefaultForward();
        } catch (Throwable e) {
            try {
                missione = (MissioneBulk) bp.getModel();
                if (oldMissioneIniziale.getPg_missione() != null && oldMissioneIniziale.getPg_missione().compareTo(new Long(0)) > 0) {
                    missione.setMissioneIniziale((MissioneBulk) oldMissioneIniziale.clone());
                    missione.setTi_provvisorio_definitivo(missione.getMissioneIniziale().getTi_provvisorio_definitivo());
                } else {
                    //	Sto creando una missione, quindi non ho quella iniziale
                    missione.setMissioneIniziale(null);
                    missione.setTi_provvisorio_definitivo(MissioneBulk.SALVA_TEMPORANEO);
                }

                bp.setModel(context, missione);
            } catch (Throwable a) {
                return handleException(context, a);
            }

            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la selezione dei termini di pagamento.
     * La modifica e' ammessa solo se l'eventuale compenso associato e' modificabile (es.non pagato)
     */

    public Forward doOnTerminiPagamentoChange(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            Rif_termini_pagamentoBulk oldTermini = ((MissioneBulk) bp.getModel()).getTermini_pagamento();

            fillModel(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            try {
                missione.isCompensoModificabile();
            } catch (it.cnr.jada.comp.ApplicationException e) {
                missione.setTermini_pagamento(oldTermini);
                return handleException(context, e);
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * Il metodo gestisce la selezione del Tipo Anagrafico (dipendente/altro).
     * Se ho dei dettagli di spesa/diaria il sistema propone all'utente la loro cancellazione.
     * Se modifico il Tipo Anagrafico dovro' per forza inizializzare il Terzo
     */

    public Forward doOnTipoAnagraficoChange(ActionContext context) {
        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
        String oldTipoAnagrafico = ((MissioneBulk) bp.getModel()).getTi_anagrafico();

        try {
            fillModel(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (oldTipoAnagrafico == null || oldTipoAnagrafico.compareTo(missione.getTi_anagrafico()) == 0)
                return context.findDefaultForward();

            if (bp.isSearching())
                return context.findDefaultForward();

            if (missione.getDettagliMissioneColl().isEmpty()) {
                missione.inizializzaTerzo();
                return context.findDefaultForward();
            } else {

                OptionBP option = openConfirm(context, "I dettagli della missione verranno cancellati. Proseguire ??", OptionBP.CONFIRM_YES_NO, "doConfermaTipoAnagraficoChange");
                option.addAttribute("oldTipoAnagrafico", oldTipoAnagrafico);
                return option;
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    public OptionBP openConfirm(ActionContext actioncontext, String message, int type, String action) throws BusinessProcessException {
        OptionBP optionbp = createOptionBP(actioncontext, message, FormBP.QUESTION_MESSAGE, type, action);
        return (OptionBP) actioncontext.addBusinessProcess(optionbp);
    }

    public OptionBP createOptionBP(ActionContext actioncontext, String message, int icon, int type, String action) {
        try {
            OptionBP optionbp = (OptionBP) actioncontext.createBusinessProcess("OptionBP");
            optionbp.setMessage(icon, message);
            optionbp.setType(type);
            HookForward hookforward = actioncontext.addHookForward("option", this, "doOption");
            hookforward.addParameter("bp", optionbp);
            if (action != null)
                hookforward.addParameter("action", action);
            return optionbp;
        } catch (BusinessProcessException businessprocessexception) {
            throw new ActionPerformingError(businessprocessexception);
        }
    }

    /**
     * Il metodo gestisce la selezione del Tipo Rapporto.
     * La modifica è consentita se l'evetuale compenso associato e' modificabile (es. non pagato)
     * Per proseguire con la modifica l'applicazione propone la cancellazione degli eventuali dettagli
     * di spesa/diaria.
     * Alla selezione del Tipo rapporto l'applicazione ricarica i relativi inquadramenti e tipi trattamento.
     */

    public Forward doOnTipoRapportoChange(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);

            Tipo_rapportoBulk oldTipoRapporto = ((MissioneBulk) bp.getModel()).getTipo_rapporto();

            fillModel(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            try {
                missione.isCompensoModificabile();
            } catch (it.cnr.jada.comp.ApplicationException e) {
                missione.setTipo_rapporto(oldTipoRapporto);
                return handleException(context, e);
            }

            // La prima volta che seleziono il tipo rapporto carico gli inquadramenti
            if ((oldTipoRapporto == null || oldTipoRapporto.getCd_tipo_rapporto() == null) && missione.getTipo_rapporto() != null) {
                bp.findInquadramentiETipiTrattamento(context);
                return context.findDefaultForward();
            }
            // Se il tipo rapporto e' cambiato
            if (!oldTipoRapporto.getCd_tipo_rapporto().equals(missione.getCd_tipo_rapporto())) {
                if (missione.getDettagliMissioneColl().size() > 0) {
                    // Se ho dei dettagli di spesa/diaria ne  propongo la cancellazione
                    OptionBP option = openConfirm(context, "I dettagli della missione verranno cancellati. Proseguire ??", OptionBP.CONFIRM_YES_NO, "doConfermaTipoRapportoChange");
                    option.addAttribute("oldTipoRapporto", oldTipoRapporto);
                    return option;
                }
                // Altrimenti carico gli inquadramenti e i tipi trattamento del nuovo tipo rapporto
                else {
                    bp.findInquadramentiETipiTrattamento(context);
                    return context.findDefaultForward();
                }
            }
            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il metodo gestisce la selezione del Tipo Trattamento.
     * La modifica è consentita se l'eventuale compenso associato alla missione è modificabile (es. non pagato).
     */

    public Forward doOnTipoTrattamentoChange(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            Tipo_trattamentoBulk oldTipoTrattamento = ((MissioneBulk) bp.getModel()).getTipo_trattamento();

            fillModel(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            try {
                missione.isCompensoModificabile();
            } catch (it.cnr.jada.comp.ApplicationException e) {
                missione.setTipo_trattamento(oldTipoTrattamento);
                return handleException(context, e);
            }

            if (missione.getTipo_trattamento() != null && !missione.getTipo_trattamento().getFl_visibile_a_tutti() && !UtenteBulk.isAbilitatoAllTrattamenti(context.getUserContext())) {
                missione.setTipo_trattamento(oldTipoTrattamento);
                throw new it.cnr.jada.comp.ApplicationException(
                        "Utente non abilitato all'utilizzo del trattamento selezionato!");
            }

            if (missione.getTipo_trattamento() == null)
                missione.setCd_trattamento(null);
            else
                missione.setCd_trattamento(missione.getTipo_trattamento().getCd_trattamento());

            // A differenza della diaria, il rimborso viene generato solo se previsto
            // Poichè cambia il trattamento devo ricalcolarlo (può cambiare la quota esente)
            if (missione.isMissioneConRimborso() && missione.getPg_missione() != null)// && (missione.getRimborsoMissioneColl() == null || missione.getRimborsoMissioneColl().isEmpty()))
            {
                bp.cancellaRimborso(context);
                missione = bp.generaRimborso(context, missione);
            }

            bp.setModel(context, missione);
            bp.resyncChildren(context);

            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il metodo gestisce l'apertura dell'obbligazione per un aggiornamento manuale.
     * Se la missione non e' modificabile il metodo aprirà la obbligazione in visualizzazione
     */

    public Forward doOpenObbligazioniWindow(ActionContext context) {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();
            missione.calcolaConsuntivi();

            boolean viewMode = bp.isViewing();

            // 	Se la missione risulta pagata
            //	devo poter aprire l'obbligazione in visualizzazione
            if (!missione.isEditable())
                viewMode = true;

            if (!missione.isMissioneConObbligazione())
                throw new it.cnr.jada.comp.ApplicationException("Selezionare l'impegno da " + (viewMode ? "visualizzare" : "modificare") + " in manuale!");

            if (!viewMode && bp instanceof IDocumentoAmministrativoBP)
                viewMode = !((IDocumentoAmministrativoBP) bp).getDocumentoAmministrativoCorrente().isEditable();

            String status = viewMode ? "V" : "M";
            it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP nbp = it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP.getBusinessProcessFor(context, missione.getObbligazione_scadenzario().getObbligazione(), status + "RSWTh");
            nbp.edit(context, missione.getObbligazione_scadenzario().getObbligazione());
            nbp.selezionaScadenza(missione.getObbligazione_scadenzario(), context);

            context.addHookForward("bringback", this, "doBringBackOpenObbligazioniWindow");
            HookForward hook = (HookForward) context.findForward("bringback");
            return context.addBusinessProcess(nbp);
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la cancellazione di un dettaglio di spesa e verifica che l'operazione
     * sia consentita.
     * Per proseguire con tale cancellazione l'applicazione propone all'utente la cancellazione
     * della diaria se gia' creata.
     */

    public Forward doRemoveFromCRUDMain_Spesa(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);

            it.cnr.jada.util.action.Selection selection = bp.getSpesaController().getSelection();
            if (selection.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Selezionare la spesa che si desidera eliminare!");

            MissioneBulk missione = (MissioneBulk) bp.getModel();

            missione.isInserimentoSpeseModificabile();

            // 	Se ho la diaria ne propongo la cancellazione automatica
            //	per poter proseguire con la cancellazione della spesa
            if ((missione.getDiariaMissioneColl() != null) && (!missione.getDiariaMissioneColl().isEmpty()))
                return openConfirm(context, "La Diaria verra' cancellata. Proseguire ?", OptionBP.CONFIRM_YES_NO, "doCancellaDiariaPerCancellazioneSpesa");
            else {
                // Chiama il metodo "removeFromSpeseMissioneColl" di MissioneBulk
                bp.getSpesaController().remove(context);
                missione.setSpeseInserite(false);
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la cancellazione di una tappa.
     * Quando l'utente cancella una tappa l'applicazione deve reinizializzare le date di inizio/fine di tutte le tappe
     * per forzarne la riconfigurazione
     */

    public Forward doRemoveFromCRUDMain_Tappa(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            it.cnr.jada.util.action.Selection selection = bp.getTappaController().getSelection();

            if (selection.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Selezionare la tappa che si desidera eliminare!");

            bp.removeTappa(context);

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la selezione del bottone "Ricerca Libera".
     * Il metodo visualizza il primo Tab e abilita i campi della spesa o della tappa nel caso
     * l'utente si trovi in modalità di edit su una di esse
     */

    public Forward doRicercaLibera(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            bp.setTab("tab", "tabTestata");

            if (bp.getSpesaController().isEditingSpesa())
                basicDoUndoSpesa(context);

            if (bp.isEditingTappa())
                basicDoUndoTappa(context);

            return super.doRicercaLibera(context);

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * Il metodo gestisce l'apertura della finestra di ricerca con filtri o creazione
     * di obbligazioni
     */

    public Forward doRicercaScadenzaObbligazione(ActionContext context) {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();
            missione.calcolaConsuntivi();

            if (missione.getTerzo() == null || missione.getCd_terzo() == null)
                throw new it.cnr.jada.comp.ApplicationException("Per eseguire questa operazione è necessario impostare un fornitore!");
            if (missione.getImporto_scadenza_obbligazione() == null || missione.getImporto_scadenza_obbligazione().compareTo(new java.math.BigDecimal(0)) == 0)
                throw new it.cnr.jada.comp.ApplicationException("Per eseguire questa operazione è necessario valorizzare l'importo!");

            Filtro_ricerca_obbligazioniVBulk filtro = new Filtro_ricerca_obbligazioniVBulk();
            filtro.setFornitore(missione.getTerzo());
            filtro.setIm_importo(missione.getImporto_scadenza_obbligazione());
            filtro.setCd_unita_organizzativa(missione.getCd_unita_organizzativa());
            filtro.setFl_importo(Boolean.TRUE);
            filtro.setData_scadenziario(null);
            filtro.setFl_data_scadenziario(Boolean.FALSE);
            filtro.setDs_obbligazione("Impegno per missione");
            filtro.setDs_scadenza("Scadenza per missione");

            BulkBP robp = (BulkBP) context.getUserInfo().createBusinessProcess(context, "RicercaObbligazioniBP", new Object[]{"MRSWTh"});
            robp.setModel(context, filtro);
            context.addHookForward("bringback", this, "doBringBackRicercaObbligazioniWindow");

            return context.addBusinessProcess(robp);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la selezione del bottone "Riporta avanti". Ciò implica salvare la missione,
     * riportare avanti l'obbligazione e committare
     */

    public Forward doRiportaAvanti(ActionContext context) throws java.rmi.RemoteException {
        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
        MissioneBulk missione = null;

        try {
            fillModel(context);
            missione = (MissioneBulk) bp.getModel();

            bp.setSavePoint(context, "RIPORTA_AVANTI");
            // Salva missione
            doSalvaMissione(context, missione);

            bp.riportaAvanti(context);
            if (bp.getMessage() == null)
                bp.setMessage("Salvataggio e riporto all'esercizio successivo eseguito in modo corretto.");

            bp.commitUserTransaction();
            bp.setCarryingThrough(false);

            bp.ricaricaMissioneInModifica(context);

            return context.findDefaultForward();
        } catch (Throwable e) {
            try {
                bp.rollbackToSavePoint(context, "RIPORTA_AVANTI");
                bp.setModel(context, missione);
            } catch (BusinessProcessException v) {
                return handleException(context, v);
            }

            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la selezione del bottone "Riporta indietro". Ciò implica riportare
     * indietro l'obbligazione senza committare
     */

    public Forward doRiportaIndietro(ActionContext context) throws java.rmi.RemoteException {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);

            bp.riportaIndietro(context);

            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }

    }

    /**
     * Il metodo gestisce il riporto della missione selezionata.
     * Nel caso di apertura da spesa per fondo economale, viene riportato l'elemento nel fondo
     */

    public Forward doRiportaSelezione(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
        BusinessProcess bp = context.getBusinessProcess();

        if (bp instanceof IDocumentoAmministrativoSpesaBP && ((IDocumentoAmministrativoSpesaBP) bp).isSpesaBP()) {
            HookForward caller = (HookForward) context.getCaller();
            it.cnr.jada.bulk.OggettoBulk selezione = (it.cnr.jada.bulk.OggettoBulk) caller.getParameter("focusedElement");
            return basicDoRiportaSelezione(context, selezione);
        }

        super.doRiportaSelezione(context, bulk);

        return context.findDefaultForward();
    }

    /**
     * Il metodo gestisce la selezione del bottone Salva.
     * Quando l'utente sta creando una missione puo' salvarla in modo provvisorio o definitivo.
     * Il salvataggio provvisorio consente :
     * - di salvare una missione in comune altro senza l'obbligo di associarle una scadenza
     * - di salvare una missione in comune proprio o estero senza l'obbligo di creare un compenso
     * Una missione DEFINITIVA non puo' essere resa PROVVISORIA
     * Se la missione e' in fase di creazione o in stato PROVVISORIO :
     * - se l'obbligazione e' obbligatoria ma l'utente non l'ha associata chiedo se vuole
     * proseguire con un salvataggio definitivo o provvisorio.
     * Se definitivo --> l'utente deve associare una obbligazione
     * Se provvisorio --> proseguo con il salvataggio
     * - se il compenso e' obbligatorio chiedo all'utente se vuole
     * proseguire con un salvataggio definitivo o provvisorio.
     * Se definitivo --> salvo la missione e creo un compenso
     * Se provvisorio --> salvo la missione e non creo un compenso.
     * - se ne il compenso ne l'obbligazione sono obbligatori
     * (missione in comune altro con anticipo>=missione)
     * salvo direttamente la missione in modo definitivo
     */

    public Forward doSalva(ActionContext context) throws java.rmi.RemoteException {
        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);

        try {
            fillModel(context);

            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (missione.isObbligazioneObbligatoria() && !missione.isMissioneConObbligazione())
                throw new it.cnr.jada.comp.ApplicationException("Associare una scadenza di impegno alla missione !");
            if (!missione.isMissioneDefinitiva()) {
                //	- La missione e' in stato provvisorio ma l'utente ha finalmente agganciato l'obbligazione
                //	  che risulta essere obbligatoria --> la missione diventa definitiva
                //	- La missione non prevede ne' compenso ne' obbligazione quindi procedo direttamente con
                //	  un salvataggio definitivo	(caso comune altro con anticipo>missione)
                missione.setMissioneDefinitiva();
                return doOnSalvataggio(context, missione);
            }

            //	La missione e' gia definitiva
            return doOnSalvataggio(context, missione);
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    public Forward doSalvaProvvisorio(ActionContext context) throws java.rmi.RemoteException {
        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);

        try {
            fillModel(context);

            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (!missione.isMissioneDefinitiva()) {
                //	La missione e' in fase di creazione o in stato provvisorio
                if (missione.isCompensoObbligatorio() || (missione.isObbligazioneObbligatoria() && !missione.isMissioneConObbligazione())) {
                    missione.setMissioneProvvisoria();
                } else {
                    throw new it.cnr.jada.comp.ApplicationException("Non è possibile salvare una missione in provvisorio quando non sono obbligatori il compenso e l'obbligazione");
                }
            } else {
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile salvare una missione in provvisorio quando è già definitiva");
            }
            return doOnSalvataggio(context, missione);
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il metodo salva la missione e la ricarica in modifica
     */

    private void doSalvaMissione(ActionContext context, MissioneBulk missione) throws BusinessProcessException {
        CRUDMissioneBP bp = (CRUDMissioneBP) context.getBusinessProcess();

        try {
            bp.completeSearchTools(context, bp);
            bp.validate(context);
            bp.saveChildren(context);
        } catch (ValidationException ex) {
            throw new BusinessProcessException(ex);
        }

        if (bp.isInserting() || (missione.getPg_missione().compareTo(new Long(0)) == -1))
            bp.create(context);        // creo la missione
        else if (bp.isEditing())
            bp.update(context);        // modifico la missione

        bp.ricaricaMissioneInModifica(context);
    }

    /**
     * Il metodo gestisce la ricerca di un Terzo.
     * Se l'utente cambia il terzo e la missione ha dei dettagli di spesa/diaria l'applicazione ne propone
     * la cancellazione.
     */
    public Forward doCRUDFind_terzo(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            bp.fillModel(context);

            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (bp.isEditable() && !bp.isSearching() && missione.getDt_inizio_missione() == null)
                throw new it.cnr.jada.comp.ApplicationException("Impostare la data di inizio missione");

            if (missione.getDettagliMissioneColl().isEmpty())
                return doCRUD(context, "main.crea_terzo");
            else {
                OptionBP option = openConfirm(context, I_DETTAGLI_DELLA_MISSIONE_VERRANNO_CANCELLATI_PROSEGUIRE, OptionBP.CONFIRM_YES_NO, "doConfermaTerzoSearchChangeForCRUD");
                return option;
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    public Forward doSearchFind_terzo(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            bp.fillModel(context);

            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (bp.isEditable() && !bp.isSearching() && missione.getDt_inizio_missione() == null)
                throw new it.cnr.jada.comp.ApplicationException("Impostare la data di inizio missione");

            if (missione.getDettagliMissioneColl().isEmpty())
                return search(context, bp.getFormField("find_terzo"), null);
            else {
                OptionBP option = openConfirm(context, I_DETTAGLI_DELLA_MISSIONE_VERRANNO_CANCELLATI_PROSEGUIRE, OptionBP.CONFIRM_YES_NO, "doConfermaTerzoSearchChange");
                return option;
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il metodo gestisce la ricerca del Tipo Spesa.
     * L'utente puo' ricercare un Tipo Spesa solo se ha gia' selezionato almeno un giorno.
     * Il giorno serve per recuperare la configurazione della relativa tappa, che,
     * nel caso di selezione multimpla dei giorni, avra' area gografica e nazione
     * uguale alle tappe degli altri giorni
     */

    public Forward doSearchFind_tipo_spesa(ActionContext context) {
        CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
        MissioneBulk missione = (MissioneBulk) bp.getModel();

        java.sql.Timestamp primoGG = missione.getPrimoGiornoSpesaSelezionato();
        if (primoGG == null)
            throw new it.cnr.jada.action.MessageToUser("Selezionare il giorno !");

        return search(context, bp.getSpesaController().getFormField("find_tipo_spesa"), null);
    }

    /**
     * Il metodo gestisce la selezione del bottone di ricerca delle Banche
     */

    public Forward doSearchListabanche(ActionContext context) {
        MissioneBulk missione = (MissioneBulk) getBusinessProcess(context).getModel();
        return search(context, getFormField(context, "main.listabanche"), missione.getModalita_pagamento().getTiPagamentoColumnSet());
    }

    /**
     * Il metodo gestisce l'inizializzazione della nazione, divisa e cambio di una tappa in comune altro
     * o proprio con i valori di default (Italia, Euro).
     */

    public Forward doSetNazioneDivisaCambioItalia(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            Missione_tappaBulk missione_tappa = (Missione_tappaBulk) bp.getTappaController().getModel();
            fillModel(context);
            if (missione_tappa.getComune().equals(Missione_tappaBulk.COMUNE_PROPRIO)) {
                missione_tappa.setFl_comune_proprio(new Boolean(true));
                missione_tappa.setFl_comune_altro(new Boolean(false));
                missione_tappa.setFl_comune_estero(new Boolean(false));
                missione_tappa.setFl_no_diaria(new Boolean(true));
                //if (bp.isRimborsoEditable(context.getUserContext()))
                missione_tappa.setFl_rimborso(new Boolean(false));
            }
            if (missione_tappa.getComune().equals(Missione_tappaBulk.COMUNE_ALTRO)) {
                missione_tappa.setFl_comune_proprio(new Boolean(false));
                missione_tappa.setFl_comune_altro(new Boolean(true));
                missione_tappa.setFl_comune_estero(new Boolean(false));
                missione_tappa.setFl_no_diaria(new Boolean(true));
                //if (bp.isRimborsoEditable(context.getUserContext()))
                missione_tappa.setFl_rimborso(new Boolean(false));
            }
            if (missione_tappa.getComune().equals(Missione_tappaBulk.COMUNE_ESTERO)) {
                missione_tappa.setFl_comune_proprio(new Boolean(false));
                missione_tappa.setFl_comune_altro(new Boolean(false));
                missione_tappa.setFl_comune_estero(new Boolean(true));
                //missione_tappa.setFl_no_diaria(new Boolean(false));
                missione_tappa.setFl_no_diaria(new Boolean(true));
                //if (bp.isRimborsoEditable(context.getUserContext()))
                missione_tappa.setFl_rimborso(new Boolean(false));
            }

            bp.setDiariaSiNo(context);
            bp.isDiariaEditable(context.getUserContext());
            bp.isRimborsoEditable(context.getUserContext());
            bp.setNazioneDivisaCambioItalia(context);

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la selezione dei Tab dell'interfaccia "Missione", verificando che i valori inseriti
     * nel Tab origine siano corretti
     */

    public Forward doTab(ActionContext context, String tabName, String pageName) {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (missione == null)
                return super.doTab(context, tabName, pageName);

            // Da tab Testata
            if (bp.isEditable() && !bp.isSearching() && bp.getTab(tabName).equalsIgnoreCase("tabTestata")) {
                missione.validaTabTestata();
                if (!pageName.equals("tabAnagrafico"))
                    missione.validaTabAnagrafico();
            }
            if ("tabAllegati".equalsIgnoreCase(bp.getTab(tabName))) {
                fillModel(context);
            }
            // Da tab Anagrafico
            if (bp.isEditable() && !bp.isSearching() && bp.getTab(tabName).equalsIgnoreCase("tabAnagrafico") &&
                    !pageName.equals("tabTestata"))
                missione.validaTabAnagrafico();

            // Da tab Configurazione tappe
            if ((bp.isEditable()) && (!bp.isSearching()) && (bp.getTab(tabName).equalsIgnoreCase("tabConfigurazioneTappe"))) {
                if ((missione.getTappeMissioneColl() != null) &&
                        (missione.getTappeMissioneColl().size() > 0) &&
                        (!missione.isTappeConfigurate()))
                    throw new it.cnr.jada.action.MessageToUser("Confermare la fine della configurazione delle tappe !");
            }

            // A tab Configurazione tappe
            if (bp.isEditable() && !bp.isSearching() && pageName.equals("tabConfigurazioneTappe")) {
                if (missione.getTipo_rapporto() == null && missione.getCd_terzo() != null) {
                    // Se modifico la data inizio missione ripulisco i rapporti
                    bp.setTab("tab", "tabAnagrafico");
                    throw new it.cnr.jada.action.MessageToUser("Selezionare il Tipo Rapporto !");
                }
            }

            // A tab Dettaglio Spese
            if (bp.isEditable() && !bp.isSearching() && pageName.equals("tabDettaglioSpese")) {
                if (missione.getTipo_rapporto() == null && missione.getCd_terzo() != null) {
                    // Se modifico la data inizio missione ripulisco i rapporti
                    bp.setTab("tab", "tabAnagrafico");
                    throw new it.cnr.jada.action.MessageToUser("Selezionare il Tipo Rapporto !");
                }
                if (missione.getTappeMissioneColl() == null || missione.getTappeMissioneColl().size() == 0)
                    throw new it.cnr.jada.action.MessageToUser("Occorre prima configurare le tappe della missione !");

                if (missione.getTappeMissioneColl() != null && !missione.getTappeMissioneColl().isEmpty() &&
                        !missione.isTappeConfigurate())
                    throw new it.cnr.jada.action.MessageToUser("Occorre prima terminare la configurazione delle tappe della missione !");
            }


            // A tab Documenti Associati
            if (bp.isEditable() && !bp.isSearching() && pageName.equals("tabObbligazione")) {
                if (!missione.isSpeseInserite())
                    throw new it.cnr.jada.action.MessageToUser("Selezionare il bottone di Fine Inserimento Spese !");
            }

            // A tab Consuntivo
            if (pageName.equals("tabConsuntivo")) {
                missione.setGiorno_consuntivo(null);
                missione.inizializzaGiornoConsuntivo();    // Azzero i consuntivi del giorno
                bp.getConsuntivoController().reset(context); // Elimino selezioni dalla table del consuntivo
                missione.calcolaConsuntivi();
            }

            return super.doTab(context, tabName, pageName);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la fine della modalita modifica/inserimento dettaglio di spesa
     */

    public Forward doUndoSpesa(ActionContext context) {
        try {
            basicDoUndoSpesa(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la fine della modalita modifica/inserimento tappa
     */


    public Forward doUndoTappa(ActionContext context) {

        try {

            basicDoUndoTappa(context);
            return context.findDefaultForward();

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce l'apertura dell'anticipo in visualizzazione
     */

    public Forward doVisualizzaAnticipo(ActionContext context) {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if (!missione.isMissioneConAnticipo())
                throw new it.cnr.jada.comp.ApplicationException("La missione non ha associato alcun anticipo !");

            it.cnr.contab.missioni00.bp.CRUDAnticipoBP bpAnticipo = (it.cnr.contab.missioni00.bp.CRUDAnticipoBP) context.getUserInfo().createBusinessProcess(context, "CRUDAnticipoBP", new Object[]{"VRSW"});
            bpAnticipo.edit(context, missione.getAnticipo());

            return context.addBusinessProcess(bpAnticipo);
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce l'apertura del compenso in visualizzazione
     */

    public Forward doVisualizzaCompenso(ActionContext context) {
        try {
            fillModel(context);
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            MissioneBulk missione = (MissioneBulk) bp.getModel();

            if ((!missione.isMissioneConCompenso()) || (missione.getCompenso() == null))
                throw new it.cnr.jada.comp.ApplicationException("La missione non ha associato alcun compenso !");

            it.cnr.contab.compensi00.bp.CRUDCompensoBP bpCompenso = (it.cnr.contab.compensi00.bp.CRUDCompensoBP) context.getUserInfo().createBusinessProcess(context, "CRUDCompensoBP", new Object[]{"VRSW"});
            bpCompenso.edit(context, missione.getCompenso());

            return context.addBusinessProcess(bpCompenso);
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doOnFlDiariaChange(ActionContext context) {
        try {
            CRUDMissioneBP bp = (CRUDMissioneBP) getBusinessProcess(context);
            Missione_tappaBulk missione_tappa = (Missione_tappaBulk) bp.getTappaController().getModel();
            fillModel(context);

            if (missione_tappa.getComune().equals(Missione_tappaBulk.COMUNE_ESTERO)) {
                missione_tappa.setFl_rimborso(new Boolean(false));
                if (missione_tappa.getFl_no_diaria()) {
                    missione_tappa.setFl_no_diaria(new Boolean(true));
                } else {
                    missione_tappa.setFl_no_diaria(new Boolean(false));
                }
            }

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doSelezionaStatoLiquidazione(ActionContext context) {
        try {
            fillModel(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

}