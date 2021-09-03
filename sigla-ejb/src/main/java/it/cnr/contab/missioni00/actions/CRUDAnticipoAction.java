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
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.docamm00.actions.EconomicaAction;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoSpesaBP;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.missioni00.bp.CRUDAnticipoBP;
import it.cnr.contab.missioni00.docs.bulk.AnticipoBulk;
import it.cnr.contab.missioni00.ejb.AnticipoComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;

/**
 * Insert the type's description here.
 * Creation date: (21/05/2002 12.23.52)
 *
 * @author: Paola sala
 */
public class CRUDAnticipoAction extends EconomicaAction {
    /**
     * CRUDAnticipoAction constructor comment.
     */
    public CRUDAnticipoAction() {
        super();
    }

    /**
     * Gestisco parte del rientro dall'aggiornamento manuale e dalla creazione della scadenza associata all'anticipo.
     * (Validazione terzo e lock alla scadenza)
     */

    public Forward basicDoBringBackOpenObbligazioniWindow(ActionContext context, Obbligazione_scadenzarioBulk scadenza) throws BusinessProcessException {
        CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
        AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

        try {
            try {
                TerzoBulk creditore = scadenza.getObbligazione().getCreditore();
                if (!anticipo.getTerzo().equalsByPrimaryKey(creditore) &&
                        !AnagraficoBulk.DIVERSI.equalsIgnoreCase(creditore.getAnagrafico().getTi_entita()))
                    throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata deve appartenere ad un'obbligazione che ha come creditore il terzo dell'anticipo!");

                it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession h = it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP.getVirtualComponentSession(context, true);
                h.lockScadenza(context.getUserContext(), scadenza);
            } catch (Exception e) {
                throw new BusinessProcessException(e);
            }
            anticipo.gestisciCambioSelezioneScadenza(scadenza);
        } catch (Throwable t) {
            it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi defSaldiBulk = bp.getDefferedUpdateSaldiParentBP().getDefferedUpdateSaldiBulk();
            if (scadenza.getObbligazione().getPg_ver_rec().equals(scadenza.getObbligazione().getSaldiInfo().get("pg_ver_rec")))
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
     * Metodo chiamato dal Fondo Economale per cercare gli anticipi eleggibili da associare
     * alle spese del fondo.
     * Tale metodo chiama il metodo "find" dell'anticipo
     */

    protected Forward basicDoCerca(ActionContext context) throws java.rmi.RemoteException, InstantiationException, javax.ejb.RemoveException {
        try {
            fillModel(context);
            it.cnr.jada.util.action.CRUDBP bp = getBusinessProcess(context);
            OggettoBulk model = bp.getModel();
            it.cnr.jada.util.RemoteIterator ri = bp.find(context, null, model);
            if (ri == null || ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                bp.setMessage("La ricerca non ha fornito alcun risultato.");
                return context.findDefaultForward();
                //}
                //else if (ri.countElements() == 1)
                //{
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
     * Metodo chiamato dal Fonfo Economale per riportare l'Anticipo selezionato
     */

    protected Forward basicDoRiportaSelezione(ActionContext context, it.cnr.jada.bulk.OggettoBulk selezione) throws java.rmi.RemoteException {
        try {
            if (selezione != null) {
                CRUDAnticipoBP bp = (CRUDAnticipoBP) context.getBusinessProcess();
                bp.edit(context, selezione);
                selezione = bp.getModel();
                //if (!AnticipoBulk.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(((AnticipoBulk)selezione).getRiportata()))
                //throw new it.cnr.jada.comp.ApplicationException("Il documento amministrativo risulta riportato! Operazione annullata.");

                // Borriello: integrazione Err. CNR 775
                Integer esScriv = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());

                if ((((AnticipoBulk) selezione).getEsercizio().compareTo(esScriv) == 0) && ((AnticipoBulk) selezione).isRiportata()) {
                    throw new it.cnr.jada.comp.ApplicationException("Il documento amministrativo risulta (parzialmente) riportato! Operazione annullata.");
                }

                // Gennaro Borriello - (09/11/2004 18.08.57)
                //	Nuova gestione dello stato <code>getRiportata()</code>
                if ((((AnticipoBulk) selezione).getEsercizio().compareTo(esScriv) != 0) && (!AnticipoBulk.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(((AnticipoBulk) selezione).getRiportataInScrivania()))) {
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
     * Inizializzazione dell'attributo "Linea di Attivita"
     */

    public Forward doBlankSearchFind_latt(ActionContext context, AnticipoBulk anticipo) {
        it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt = new it.cnr.contab.config00.latt.bulk.WorkpackageBulk();
        latt.setCentro_responsabilita(new it.cnr.contab.config00.sto.bulk.CdrBulk());
        anticipo.setLattPerRimborso(latt);

        return context.findDefaultForward();
    }

    /**
     * Inizializzazione degli attributi relativi al terzo e ai riferimenti bancari del terzo
     */

    public Forward doBlankSearchFind_terzo(ActionContext context, AnticipoBulk anticipo) {
        CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
        anticipo.inizializzaTerzo();
        return context.findDefaultForward();
    }

    /**
     * Il metodo gestisce il rientro dall'aggiornamento manuale della scadenza associata all'anticipo.
     * (Validazione terzo e Lock alla scadenza)
     */

    public Forward doBringBackOpenObbligazioniWindow(ActionContext context) {
        try {
            HookForward caller = (HookForward) context.getCaller();
            Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) caller.getParameter("bringback");

            if (scadenza == null)
                return context.findDefaultForward();

            CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
            AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

            basicDoBringBackOpenObbligazioniWindow(context, scadenza);

            anticipo.setScadenza_obbligazione(scadenza);
            bp.setDirty(true);
            bp.setModel(context, anticipo);

            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
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

            CRUDAnticipoBP bp = (CRUDAnticipoBP) context.getBusinessProcess();
            AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

            basicDoBringBackOpenObbligazioniWindow(context, scadenza);

            anticipo.setScadenza_obbligazione(scadenza);

            //	verifico se la scadenza appena associata l'avevo precedentemente slegata
            if (anticipo.isScadenzaDaRimuovereDaiCancellati()) {
                //  la rimuovo dalla collection altrimenti l'aggiornamento del suo
                //	im_associato_doc_amm a zero mi cambierebbe il pg_ver_rec
                anticipo.removeFromDocumentiContabiliCancellati(scadenza);
            }
            bp.setDirty(true);
            bp.setModel(context, anticipo);

            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce l'inizializzazione di alcuni attributi dell'anticipo relativi al terzo e ai dati
     * bancari del terzo (nome, cognome, ragione sociale, codice fiscale, partita iva, modalita e termini di pagamento)
     */

    public Forward doBringBackSearchFind_terzo(ActionContext context, AnticipoBulk anticipo, V_terzo_per_compensoBulk aTerzo) {
        try {
            if (aTerzo == null)
                return context.findDefaultForward();

            CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
            AnticipoComponentSession component = (AnticipoComponentSession) bp.createComponentSession();
            anticipo.inizializzaTerzo();
            anticipo = component.completaTerzo(context.getUserContext(), anticipo, aTerzo);

            bp.setModel(context, anticipo);

            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Attiva la ricerca libera di un anticipo
     */
    public Forward doCerca(ActionContext context) throws java.rmi.RemoteException, InstantiationException, javax.ejb.RemoveException {
        CRUDAnticipoBP bp = (CRUDAnticipoBP) context.getBusinessProcess();

        //	Se chi invoca la ricerca degli anticipi e' il Fondo Economale
        if (bp instanceof IDocumentoAmministrativoSpesaBP && ((IDocumentoAmministrativoSpesaBP) bp).isSpesaBP())
            return basicDoCerca(context);
        return super.doCerca(context);
    }

    /**
     * Il metodo attiva la creazione del Documento di Rimborso dell'anticipo
     */
    public Forward doConfermaCreaRimborsoCompleto(ActionContext context, int option) {
        try {
            if (option == OptionBP.YES_BUTTON) {
                CRUDAnticipoBP bp = (CRUDAnticipoBP) context.getBusinessProcess();
                bp.creaRimborsoCompleto(context);
            }
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la modifica della data di registrazione nel caso il terzo non sia piu' valido
     * alla nuova data :
     * - inizializzazione del terzo
     * - modifica delle date di competenza
     * - validazione della nuova data di registrazione
     */

    public Forward doConfermaDataRegistrazioneChange(ActionContext context, OptionBP option) {
        CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
        AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

        java.sql.Timestamp dataIniziale = (java.sql.Timestamp) option.getAttribute("dataIniziale");

        if (option.getOption() == OptionBP.YES_BUTTON) {
            try {
                anticipo.inizializzaTerzo();
                bp.gestisciCambioDataRegistrazione(context, anticipo);

                return context.findDefaultForward();
            } catch (Throwable t) {
                try {
                    // In caso di errore ripropongo la data precedente
                    anticipo.setDt_registrazione(dataIniziale);
                    anticipo.setDt_a_competenza_coge(anticipo.getDt_registrazione());
                    anticipo.setDt_da_competenza_coge(anticipo.getDt_registrazione());

                    bp.setModel(context, anticipo);

                    return handleException(context, t);
                } catch (Throwable e) {
                    return handleException(context, e);
                }
            }
        }
        if (option.getOption() == OptionBP.NO_BUTTON) {
            try {
                anticipo.setDt_registrazione(dataIniziale);
                anticipo.setDt_a_competenza_coge(anticipo.getDt_registrazione());
                anticipo.setDt_da_competenza_coge(anticipo.getDt_registrazione());

                bp.setModel(context, anticipo);
            } catch (Throwable e) {
                return handleException(context, e);
            }
        }
        return context.findDefaultForward();
    }

    /**
     * Alla selezione del bottone di Ricerca il sistema visualizza il primo Tab
     */
    public Forward doConfermaNuovaRicerca(ActionContext context, int option) {
        CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
        bp.setTab("tab", "tabAnagrafico");

        return super.doConfermaNuovaRicerca(context, option);
    }

    /**
     * Il metodo gestisce la selezione del bottone di "Creazione del rimborso dell'anticipo"
     */
    public Forward doCreaRimborsoCompleto(ActionContext context) {
        try {
            fillModel(context);
            return doConfermaCreaRimborsoCompleto(context, OptionBP.YES_BUTTON);
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce un comando di cancellazione logica/fisica.
     * Metodo ridefinito per poter ricaricare l'anticipo annullato
     */
    public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {
        try {
            fillModel(context);

            CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
            AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

            if (!bp.isEditing()) {
                bp.setMessage("Non è possibile cancellare in questo momento");
            } else {
                if (AnticipoBulk.STATO_ANNULLATO.equals(anticipo.getStato_cofi()))
                    throw new it.cnr.jada.comp.ApplicationException("L'anticipo e' gia' stato annullato!");
                if (anticipo.isPagata())
                    throw new it.cnr.jada.comp.ApplicationException("Impossibile proseguire! L'anticipo risulta pagato.");
                //	Il bottone di cancellazione anticipo verra' disabilitato se l'anticipo risulta collegato a missione
                //	o rimborsato

                bp.delete(context);
                anticipo = (AnticipoBulk) bp.getModel();
                AnticipoComponentSession session = (AnticipoComponentSession) bp.createComponentSession();
                if (session.isAnticipoAnnullato(context.getUserContext(), anticipo)) {
                    bp.edit(context, anticipo);
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
     * Il metodo gestisce l'eliminazione dell'associazione anticipo-scadenza :
     * - l'aggiornamento/inserimento a db di tale scadenza con im_associato_doc_amm=0
     * - se l'anticipo usa il fondo economale devo eliminare questa associazione
     */

    public Forward doEliminaScadenzaObbligazione(ActionContext context) {
        try {
            fillModel(context);
            CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
            AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

            if ((anticipo.getPg_obbligazione_scadenzario() == null) || (anticipo.getPg_obbligazione() == null) || (anticipo.getEsercizio_ori_obbligazione() == null))
                throw new it.cnr.jada.comp.ApplicationException("Non esiste alcuna scadenza da eliminare!");

            anticipo.addToDocumentiContabiliCancellati(anticipo.getScadenza_obbligazione());
            anticipo.setScadenza_obbligazione(new Obbligazione_scadenzarioBulk());
            if (anticipo.getStato_pagamento_fondo_eco().compareTo(AnticipoBulk.STATO_ASSEGNATO_FONDO_ECO) == 0)
                anticipo.setStato_pagamento_fondo_eco(AnticipoBulk.STATO_LIBERO_FONDO_ECO);
            bp.setDirty(true);

            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo verifica che, prima di effettuare una ricerca libera di un terzo, sia stata
     * valorizzata la data di registrazione dell'anticipo. Tale data servira' per verificare
     * la validita' del terzo
     */

    public Forward doFreeSearchFind_terzo(ActionContext context) {
        try {
            CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
            bp.fillModel(context);

            AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

            if (bp.isEditable() && !bp.isSearching() && anticipo.getDt_registrazione() == null)
                throw new it.cnr.jada.comp.ApplicationException("Impostare la data di registrazione");

            return freeSearch(context, bp.getFormField("find_terzo"));
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il metodo gestisce la modifica automatica della scadenza associata all'anticipo.
     * Al termine dell'aggiornamento metto un lock alla scadenza
     */

    public Forward doModificaScadenzaInAutomatico(ActionContext context) {
        try {
            fillModel(context);
            CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
            AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

            boolean viewMode = bp.isViewing();

            if ((anticipo.getPg_obbligazione_scadenzario() == null) || (anticipo.getPg_obbligazione() == null) || (anticipo.getEsercizio_ori_obbligazione() == null))
                throw new it.cnr.jada.comp.ApplicationException("Selezionare la scadenza di obbligazione da modificare!");

            if ((anticipo.getIm_anticipo_divisa() == null) || (anticipo.getIm_anticipo_divisa().compareTo(new java.math.BigDecimal(0)) == 0))
                throw new it.cnr.jada.comp.ApplicationException("Inserire l'importo dell'anticipo!");

            Obbligazione_scadenzarioBulk scadenza = anticipo.getScadenza_obbligazione();
            it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession h = it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP.getVirtualComponentSession(context, true);

            try {
                scadenza = (Obbligazione_scadenzarioBulk) h.modificaScadenzaInAutomatico(
                        context.getUserContext(),
                        scadenza,
                        anticipo.getIm_anticipo_divisa(),
                        false);

                h.lockScadenza(context.getUserContext(), scadenza);
                anticipo.addToDefferredSaldi(scadenza.getObbligazione(), scadenza.getObbligazione().getSaldiInfo());
                anticipo.gestisciCambioSelezioneScadenza(scadenza);
                anticipo.setScadenza_obbligazione(scadenza);
                bp.setDirty(true);
                bp.setModel(context, anticipo);

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
     * Alla selezione del bottone "Nuovo" il sistema visualizza il primo Tab
     */

    public Forward doNuovo(ActionContext context) {
        super.doTab(context, "tab", "tabAnagrafico");
        return (super.doNuovo(context));
    }

    /**
     * Il metodo gestise la mancata disponibilita' di cassa in fase di associazione dell'anticipo ad
     * una scadenza.
     */

    public Forward doOnCheckDisponibilitaCassaFailed(ActionContext context, int option) {
        if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) {
            CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
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
            CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
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
     * Il metodo gestisce la modifica della data di registrazione facendo le opportune validazioni sulla
     * data e sul terzo
     */

    public Forward doOnDataRegistrazioneChange(ActionContext context) {
        CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
        AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

        if (anticipo.getFl_associato_missione() != null && anticipo.getFl_associato_missione().booleanValue()) {
            bp.setMessage("Anticipo collegato a missione. Data non modificabile");
            return context.findDefaultForward();
        }
        java.sql.Timestamp oldDate = null;
        if (anticipo.getDt_registrazione() != null)
            oldDate = (java.sql.Timestamp) anticipo.getDt_registrazione().clone();

        try {
            fillModel(context);
            anticipo = (AnticipoBulk) bp.getModel();

            if (bp.isSearching())
                return context.findDefaultForward();

            anticipo.validaDataRegistrazione(context);

            if (anticipo.getV_terzo() != null && anticipo.getV_terzo().getCd_terzo() != null &&
                    !anticipo.isTerzoValido()) {
                OptionBP option = openConfirm(context, "Il terzo selezionato verra' cancellato. Proseguire ?", OptionBP.CONFIRM_YES_NO, "doConfermaDataRegistrazioneChange");
                option.addAttribute("dataIniziale", oldDate);
                return option;
            }

            bp.gestisciCambioDataRegistrazione(context, anticipo);

            return context.findDefaultForward();
        } catch (Throwable t) {
            // In caso di errore ripropongo la data precedente
            anticipo.setDt_registrazione(oldDate);
            anticipo.setDt_a_competenza_coge(anticipo.getDt_registrazione());
            anticipo.setDt_da_competenza_coge(anticipo.getDt_registrazione());

            try {
                bp.setModel(context, anticipo);
                return handleException(context, t);
            } catch (Throwable e) {
                return handleException(context, e);
            }
        }
    }

    /**
     * Il metodo gestisce la modifica dell'importo dell'anticipo :
     * - se l'anticipo è legato alla missione non posso modificarne l'importo
     * - l'importo non deve essere negativo
     */

    public Forward doOnImportoAnticipoChange(ActionContext context) {
        CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
        AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

        if (anticipo.getFl_associato_missione() != null && anticipo.getFl_associato_missione().booleanValue()) {
            bp.setMessage("Anticipo collegato a missione. Importo non modificabile");
            return context.findDefaultForward();
        }
        try {
            fillModel(context);
            anticipo = (AnticipoBulk) bp.getModel();

            if (bp.isSearching())
                return context.findDefaultForward();


            if (anticipo.getIm_anticipo_divisa() == null)
                return context.findDefaultForward();

            if (anticipo.getIm_anticipo_divisa().compareTo(new java.math.BigDecimal(0)) == -1) {
                anticipo.setIm_anticipo_divisa(null);
                throw new it.cnr.jada.comp.ApplicationException("L'importo non può essere negativo !");
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la selezione della Modalita di Pagamento ricercando le opportune banche
     */

    public Forward doOnModalitaPagamentoChange(ActionContext context) {
        try {
            fillModel(context);
            CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
            AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();
            if (anticipo.getModalita_pagamento() != null) {
                AnticipoComponentSession component = (AnticipoComponentSession) bp.createComponentSession();
                java.util.Collection coll = component.findListabanche(context.getUserContext(), anticipo);

                //	Assegno di default la prima banca tra quelle selezionate
                if (coll == null || coll.isEmpty())
                    anticipo.setBanca(null);
                else
                    anticipo.setBanca((it.cnr.contab.anagraf00.core.bulk.BancaBulk) new java.util.Vector(coll).firstElement());
            } else {
                anticipo.setBanca(null);
            }
            bp.setModel(context, anticipo);
            bp.resyncChildren(context);
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * Il metodo gestisce la selezione del Tipo Anagrafico. Se l'utente ha gia' selezionato un terzo devo
     * re-inizializzare i dati relativi a tale terzo
     */

    public Forward doOnTipoAnagraficoChange(ActionContext context) {
        try {
            CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
            AnticipoBulk anticipoIniziale = (AnticipoBulk) bp.getModel().clone();

            fillModel(context);
            AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

            if (anticipoIniziale.getTi_anagrafico() == null ||
                    anticipoIniziale.getTi_anagrafico().compareTo(anticipo.getTi_anagrafico()) == 0)
                return context.findDefaultForward();

            if (bp.isSearching())
                return context.findDefaultForward();

            anticipo.inizializzaTerzo();

            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il metodo gestisce l'apertura dell'obbligazione per l' "Aggiornamento Manuale"
     */

    public Forward doOpenObbligazioniWindow(ActionContext context) {
        try {
            fillModel(context);
            CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
            AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

            boolean viewMode = bp.isViewing();

            // 	Se l'anticipo risulta pagato/legato a missione/rimborsato/
            //	non modificabile per chiusura contabile
            //	devo poter aprire l'obbligazione in visualizzazione
            if (!anticipo.isEditable() || anticipo.isROPerChiusura())
                viewMode = true;

            if ((anticipo.getPg_obbligazione_scadenzario() == null) || (anticipo.getPg_obbligazione() == null) || (anticipo.getEsercizio_ori_obbligazione() == null))
                throw new it.cnr.jada.comp.ApplicationException("Selezionare l'impegno da " + (viewMode ? "visualizzare" : "modificare") + " in manuale!");

            if (!viewMode && bp instanceof IDocumentoAmministrativoBP)
                viewMode = !((IDocumentoAmministrativoBP) bp).getDocumentoAmministrativoCorrente().isEditable();

            String status = viewMode ? "V" : "M";
            it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP nbp = it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP.getBusinessProcessFor(context, anticipo.getScadenza_obbligazione().getObbligazione(), status + "RSWTh");
            nbp.edit(context, anticipo.getScadenza_obbligazione().getObbligazione());
            nbp.selezionaScadenza(anticipo.getScadenza_obbligazione(), context);

            context.addHookForward("bringback", this, "doBringBackOpenObbligazioniWindow");
            HookForward hook = (HookForward) context.findForward("bringback");
            return context.addBusinessProcess(nbp);
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Alla selezione del bottone "Ricerca Libera" il sistema visualizza il primo Tab
     */

    public Forward doRicercaLibera(ActionContext context) {
        CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
        bp.setTab("tab", "tabAnagrafico");

        return super.doRicercaLibera(context);
    }

    /**
     * Il metodo gestisce l'apertura della finestra di ricerca con filtri o creazione di obbligazioni
     * inizializzandone gli opportuni campi
     */

    public Forward doRicercaScadenzaObbligazione(ActionContext context) {
        try {
            fillModel(context);
            CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
            AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

            if (anticipo.getTerzo() == null || anticipo.getCd_terzo() == null)
                throw new it.cnr.jada.comp.ApplicationException("Per eseguire questa operazione è necessario impostare un fornitore!");
            if (anticipo.getIm_anticipo_divisa() == null || anticipo.getIm_anticipo_divisa().compareTo(new java.math.BigDecimal(0)) == 0)
                throw new it.cnr.jada.comp.ApplicationException("Per eseguire questa operazione è necessario valorizzare l'importo!");

            Filtro_ricerca_obbligazioniVBulk filtro = new Filtro_ricerca_obbligazioniVBulk();
            filtro.setFornitore(anticipo.getTerzo());
            filtro.setIm_importo(anticipo.getIm_anticipo_divisa());
            filtro.setCd_unita_organizzativa(anticipo.getCd_unita_organizzativa());
            filtro.setFl_importo(Boolean.TRUE);
            filtro.setData_scadenziario(null);
            filtro.setFl_data_scadenziario(Boolean.FALSE);
            filtro.setDs_obbligazione("Impegno per anticipo");
            filtro.setDs_scadenza("Scadenza per anticipo");

            BulkBP robp = (BulkBP) context.getUserInfo().createBusinessProcess(context, "RicercaObbligazioniBP", new Object[]{"MRSWTh"});
            robp.setModel(context, filtro);
            context.addHookForward("bringback", this, "doBringBackRicercaObbligazioniWindow");

            return context.addBusinessProcess(robp);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la selezione del bottone "Riporta avanti". Ciò implica :
     * - se non ho rimborso : salvare l'anticipo, riportare avanti l'obbligazione e committare
     * - se ho rimborso : riportare avanti l'accertamento e committare
     */

    public Forward doRiportaAvanti(ActionContext context) throws java.rmi.RemoteException {
        CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);

        try {
            fillModel(context);
            AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

            if (!anticipo.hasRimborso())
                bp.salvaRiportandoAvanti(context);
            else
                bp.riportaRimborsoAvanti(context);

            return context.findDefaultForward();
        } catch (ValidationException e) {
            bp.setErrorMessage(e.getMessage());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la selezione del bottone "Riporta indietro". Ciò implica :
     * - se non ho rimborso : riportare indietro l'obbligazione senza committare
     * - se ho rimborso : riportare indietro l'accertamento e committare
     */

    public Forward doRiportaIndietro(ActionContext context) throws java.rmi.RemoteException {
        try {
            fillModel(context);
            CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);

            AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

            if (!anticipo.hasRimborso())
                bp.riportaIndietro(context);
            else
                bp.riportaRimborsoIndietro(context);

            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }

    }

    /**
     * Il metodo gestisce il riporto dell'anticipo selezionato.
     * Nel caso di apertura da spesa per fondo economale, viene riportato l'elemento nel fondo
     */

    public Forward doRiportaSelezione(ActionContext context) throws java.rmi.RemoteException {
        CRUDAnticipoBP bp = (CRUDAnticipoBP) context.getBusinessProcess();
        if (bp instanceof IDocumentoAmministrativoSpesaBP && ((IDocumentoAmministrativoSpesaBP) bp).isSpesaBP()) {
            HookForward caller = (HookForward) context.getCaller();
            it.cnr.jada.bulk.OggettoBulk selezione = (it.cnr.jada.bulk.OggettoBulk) caller.getParameter("focusedElement");
            return basicDoRiportaSelezione(context, selezione);
        }
        return super.doRiportaSelezione(context);
    }

    /**
     * Il metodo gestisce la ricerca di un terzo verificando che la data di registrazione
     * sia gia' stata valorizzata. Tale data servirà per validare il terzo
     */

    public Forward doSearchFind_terzo(ActionContext context) {
        try {
            CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
            bp.fillModel(context);

            AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

            if (bp.isEditable() && !bp.isSearching() && anticipo.getDt_registrazione() == null)
                throw new it.cnr.jada.comp.ApplicationException("Impostare la data di registrazione");

            return search(context, bp.getFormField("find_terzo"), null);
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il metodo gestisce la selezione del bottone di ricerca delle Banche
     */

    public Forward doSearchListabanche(ActionContext context) {
        try {
            fillModel(context);
            AnticipoBulk anticipo = (AnticipoBulk) getBusinessProcess(context).getModel();
            return search(context, getFormField(context, "main.listabanche"), anticipo.getModalita_pagamento().getTiPagamentoColumnSet());
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Il metodo gestisce la selezione dei Tab dell'interfaccia "Anticipo"
     */
    public Forward doTab(ActionContext context, String tabName, String pageName) {
        try {
            fillModel(context);
            CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
            AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

            if (anticipo == null)
                return super.doTab(context, tabName, pageName);

            // Da tab anagrafico
            if (bp.isEditable() && !bp.isSearching() && bp.getTab(tabName).equalsIgnoreCase("tabAnagrafico"))
                anticipo.validaTabAnagrafico();

            return super.doTab(context, tabName, pageName);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce l'apertura della missione in visualizzazione
     */

    public Forward doVisualizzaMissione(ActionContext context) {
        try {
            fillModel(context);
            CRUDAnticipoBP bp = (CRUDAnticipoBP) getBusinessProcess(context);
            AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

            if (!anticipo.isAnticipoConMissione())
                throw new it.cnr.jada.comp.ApplicationException("L' anticipo non ha associato alcuna missione !");

            it.cnr.contab.missioni00.bp.CRUDMissioneBP bpMissione = (it.cnr.contab.missioni00.bp.CRUDMissioneBP) context.getUserInfo().createBusinessProcess(context, "CRUDMissioneBP", new Object[]{"VRSW"});
            bpMissione.edit(context, anticipo.getMissione());

            return context.addBusinessProcess(bpMissione);
        } catch (Exception e) {
            return handleException(context, e);
        }
    }
}
