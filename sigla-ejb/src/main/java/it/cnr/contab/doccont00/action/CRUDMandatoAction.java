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

package it.cnr.contab.doccont00.action;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk;
import it.cnr.contab.doccont00.bp.CRUDAbstractMandatoBP;
import it.cnr.contab.doccont00.bp.CRUDMandatoBP;
import it.cnr.contab.doccont00.bp.DispCassaCapitoloBP;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.util.enumeration.StatoVariazioneSostituzione;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.*;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Azione che gestisce le richieste relative alla Gestione Documenti Contabili
 * (Mandato)
 */
public class CRUDMandatoAction extends CRUDAbstractMandatoAction {
    public CRUDMandatoAction() {
        super();
    }

    /**
     * Metodo utilizzato per gestire la conferma dei documenti passivi
     * disponibili selezionati.
     */

    public Forward doAggiungiDocPassivi(ActionContext context) {
        try {
            CRUDMandatoBP bp = (CRUDMandatoBP) getBusinessProcess(context);
            fillModel(context);
            bp.aggiungiDocPassivi(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il caricamento dei doc.amm.attivi collegati all'accertamento
     *
     * @param context      <code>ActionContext</code> in uso.
     * @param mandato      Oggetto di tipo <code>MandatoIBulk</code>
     * @return <code>Forward</code>
     */
    public Forward doBlankSearchFind_accertamento(ActionContext context, MandatoIBulk mandato) {
        try {
            mandato.setDocGenericiPerRegolarizzazione(null);
            mandato.setDocGenericiSelezionatiPerRegolarizzazione(null);
            mandato.setAccertamentoPerRegolarizzazione(new AccertamentoBulk());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce l'annullamento del terzo
     */
    public Forward doBlankSearchFind_doc_passivi(ActionContext context, OggettoBulk mandato) {
        try {
            ((MandatoIBulk) mandato).setDocPassiviColl(new java.util.ArrayList());
            ((MandatoIBulk) mandato).getFind_doc_passivi().setTerzoAnag(new it.cnr.contab.anagraf00.core.bulk.V_all_anagrafico_terzoBulk());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il caricamento dei doc.amm.attivi collegati all'accertamento
     *
     * @param context      <code>ActionContext</code> in uso.
     * @param mandato      Oggetto di tipo <code>MandatoIBulk</code>
     * @param accertamento Oggetto di tipo <code>AccertamentoBulk</code>
     * @return <code>Forward</code>
     */
    public Forward doBringBackSearchFind_accertamento(ActionContext context, MandatoIBulk mandato, AccertamentoBulk accertamento) {
        try {
            CRUDMandatoBP bp = (CRUDMandatoBP) getBusinessProcess(context);
            if (accertamento != null) {
                mandato.setAccertamentoPerRegolarizzazione(accertamento);
                if (mandato.getCd_unita_organizzativa() == null || mandato.getCd_uo_ente() == null ||
                        !mandato.getCd_unita_organizzativa().equals(mandato.getCd_uo_ente()))
                    bp.caricaScadenzeAccertamentoPerRegolarizzazione(context);
                else
                    bp.caricaDocAttiviPerRegolarizzazione(context);
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la selezione di un tipo bollo
     */
    public Forward doCambiaTipoBollo(ActionContext context) {
        try {
            fillModel(context);
            CRUDMandatoBP bp = (CRUDMandatoBP) getBusinessProcess(context);
            MandatoBulk mandato = (MandatoBulk) bp.getModel();
            mandato.getMandato_terzo().setToBeUpdated();
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la selezione dell'unità organizzativa
     */
    public Forward doCambiaTipoMandato(ActionContext context) {
        try {
            fillModel(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la selezione dell'unità organizzativa
     */
    public Forward doCambiaUnitaOrganizzativa(ActionContext context) {
        try {
            fillModel(context);
            SimpleCRUDBP bp = (SimpleCRUDBP) getBusinessProcess(context);
            MandatoIBulk mandato = (MandatoIBulk) bp.getModel();
            mandato.setDocPassiviColl(new java.util.ArrayList());
//		mandato.setSospesiColl( new ArrayList() );
            mandato.setCd_cds(mandato.getUnita_organizzativa().getCd_unita_padre());
            mandato.setAccertamentoPerRegolarizzazione(null);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il caricamento dei documenti passivi
     */
    public Forward doCercaDocPassivi(ActionContext context) {
        try {
            CRUDMandatoBP bp = (CRUDMandatoBP) getBusinessProcess(context);
            fillModel(context);
            MandatoIBulk mandato = (MandatoIBulk) bp.getModel();
            if (mandato.getFind_doc_passivi().getCd_terzo() == null &&
                    mandato.getFind_doc_passivi().getCd_precedente() == null &&
                    mandato.getFind_doc_passivi().getCognome() == null &&
                    mandato.getFind_doc_passivi().getRagione_sociale() == null &&
                    mandato.getFind_doc_passivi().getNome() == null &&
                    mandato.getFind_doc_passivi().getPartita_iva() == null &&
                    mandato.getFind_doc_passivi().getCodice_fiscale() == null
            )
                throw new it.cnr.jada.comp.ApplicationException("Attenzione! Deve essere specificato almeno un campo dell'anagrafica.");

            if (mandato.getFind_doc_passivi().getTerzoAnag().getCrudStatus() == bp.getModel().UNDEFINED) {
                //doSearchFind_doc_passiviInAutomatico( context );
                it.cnr.jada.util.RemoteIterator ri = bp.find(context, null, mandato.getFind_doc_passivi().getTerzoAnag(), mandato, "find_doc_passivi.terzoAnag");
                if (ri == null || ri.countElements() == 0) {
                    it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                    bp.setMessage("Il terzo non e' presente nell'anagrafico.");
                    return context.findDefaultForward();
                } else if (ri.countElements() == 1) {
                    FormField field = getFormField(context, "main.find_doc_passivi");
                    doBringBackSearchResult(context, field, (OggettoBulk) ri.nextElement());
                    it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                } else {
                    it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                    bp.setMessage("Esite piu' di un terzo che soddisfa i criteri di ricerca.");
                    return context.findDefaultForward();
                }
            }
            bp.cercaDocPassivi(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Metodo utilizzato per ricercare i documenti passivi in automatico.
     */
    public Forward doSearchFind_doc_passiviInAutomatico(ActionContext context) {
        try {
            CRUDMandatoBP bp = (CRUDMandatoBP) getBusinessProcess(context);
            MandatoIBulk mandato = (MandatoIBulk) bp.getModel();
            it.cnr.jada.util.RemoteIterator ri = bp.find(context, null, mandato.getFind_doc_passivi().getTerzoAnag(), mandato, "find_doc_passivi.terzoAnag");
            if (ri == null || ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                bp.setMessage("Il terzo non e' presente nell'anagrafico.");
                return context.findDefaultForward();
            } else if (ri.countElements() == 1) {
                FormField field = getFormField(context, "main.find_doc_passivi");
                doBringBackSearchResult(context, field, (OggettoBulk) ri.nextElement());
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                return context.findDefaultForward();
            } else {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                bp.setMessage("Esite piu' di un terzo che soddisfa i criteri di ricerca.");
                return context.findDefaultForward();
            }
        } catch (Exception e) {
            return handleException(context, e);
        }

    }

    /**
     * Metodo utilizzato per visualizzare la disponibilità di cassa sul capitolo
     */

    public Forward doVisualizzaDispCassaCapitolo(ActionContext context) {
        try {
            CRUDMandatoBP bp = (CRUDMandatoBP) getBusinessProcess(context);
            fillModel(context);
            context.addHookForward("close", this, "doDefault");
            DispCassaCapitoloBP view = (DispCassaCapitoloBP) context.createBusinessProcess("DispCassaCapitoloBP");
            DispCassaCapitoloBulk bulk = new DispCassaCapitoloBulk();
            bulk.setMandato((MandatoBulk) bp.getModel());
            view.setModel(context, bulk);
            view.refreshDispCassa(context);
            return context.addBusinessProcess(view);

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doPrint(ActionContext actioncontext) {
        try {
            CRUDAbstractMandatoBP bp = (CRUDAbstractMandatoBP) getBusinessProcess(actioncontext);
            fillModel(actioncontext);
            bp.esistonoPiuModalitaPagamento(actioncontext);
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
        return super.doPrint(actioncontext);
    }

    /**
     * Associa un codice SIOPE (Mandato_siopeBulk), ad una riga di mandato (Mandato_rigaBulk).
     *
     * @param context {@link ActionContext } in uso.
     * @return Forward
     */

    public Forward doAggiungiCodiceSiope(ActionContext context) {
        try {
            fillModel(context);
            CRUDMandatoBP bp = (CRUDMandatoBP) context.getBusinessProcess();
            bp.getCodiciSiopeCollegabili().remove(context);
            bp.getCodiciSiopeCollegati().reset(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return super.handleException(context, e);
        }
    }

    /**
     * Rimuove i codici SIOPE selezionati.
     *
     * @param context {@link ActionContext } in uso.
     * @return Forward
     */

    public Forward doRimuoviCodiceSiope(ActionContext context) {
        try {
            fillModel(context);
            CRUDMandatoBP bp = (CRUDMandatoBP) context.getBusinessProcess();
            bp.getCodiciSiopeCollegati().remove(context);
            bp.getCodiciSiopeCollegabili().reset(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return super.handleException(context, e);
        }
    }

    public Forward doSalva(ActionContext context) throws RemoteException {
        try {
            fillModel(context);
            CRUDMandatoBP bp = (CRUDMandatoBP) context.getBusinessProcess();
            MandatoBulk mandato = (MandatoBulk) bp.getModel();

            if (bp.isSiope_attiva() && mandato.isRequiredSiope() && !mandato.isSiopeTotalmenteAssociato()) {
                if (bp.isSiopeBloccante(context)) {
                    bp.setMessage("Attenzione! Alcune o tutte le righe mandato non risultano associate completamente a codici SIOPE. Impossibile continuare.");
                    return doSelezionaRigaSiopeDaCompletare(context);
                }
                return openConfirm(context, "Attenzione! Alcune o tutte le righe mandato non risultano associate completamente a codici SIOPE. Vuoi continuare?", OptionBP.CONFIRM_YES_NO, "doConfirmSalvaCup");
            }

            return doConfirmSalvaCup(context, OptionBP.YES_BUTTON);
        } catch (Throwable e) {
            return super.handleException(context, e);
        }
    }

    public Forward doConfirmSalva(ActionContext actioncontext, int option) {
        try {
            if (option == OptionBP.YES_BUTTON) {
                return super.doSalva(actioncontext);
            }
            return doSelezionaRigaSiopeDaCompletare(actioncontext);
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
    }

    public Forward doConfirmSalvaCup(ActionContext actioncontext, int option) {
        try {
            if (option == OptionBP.YES_BUTTON) {
                CRUDMandatoBP bp = (CRUDMandatoBP) actioncontext.getBusinessProcess();
                MandatoBulk mandato = (MandatoBulk) bp.getModel();
                // mandato.isRequiredSiope() controlla che non sia un mandato di regolarizzazione
                if (bp.isCup_attivo() && mandato.isRequiredSiope()) {
                    boolean trovato = false;
                    if (mandato instanceof MandatoIBulk) {
                        bp.getCupCollegati().validate(actioncontext);
                        for (Iterator i = mandato.getMandato_rigaColl().iterator(); i.hasNext() && !trovato; ) {
                            Mandato_rigaBulk riga = (Mandato_rigaBulk) i.next();
                            if (riga.getMandatoCupColl().isEmpty() || riga.getTipoAssociazioneCup().compareTo(Mandato_rigaBulk.SIOPE_TOTALMENTE_ASSOCIATO) != 0)
                                trovato = true;
                        }
                        if (trovato)
                            return openConfirm(actioncontext, "Attenzione! Alcune o tutte le righe mandato non risultano associate completamente al CUP. Vuoi continuare?", OptionBP.CONFIRM_YES_NO, "doConfirmSalva");
                    }
                }
                if (!mandato.isAnnullato()) {
                    if (bp.isSiope_cup_attivo() && mandato.isRequiredSiope()) {
                        boolean trovato = false;
                        if (mandato instanceof MandatoIBulk) {
                            bp.getSiopeCupCollegati().validate(actioncontext);
                            for (Iterator i = mandato.getMandato_rigaColl().iterator(); i.hasNext() && !trovato; ) {
                                Mandato_rigaBulk riga = (Mandato_rigaBulk) i.next();
                                for (Iterator j = riga.getMandato_siopeColl().iterator(); j.hasNext() && !trovato; ) {
                                    Mandato_siopeBulk rigaSiope = (Mandato_siopeBulk) j.next();

                                    if (rigaSiope.getMandatoSiopeCupColl().isEmpty() || rigaSiope.getTipoAssociazioneCup().compareTo(Mandato_rigaBulk.SIOPE_TOTALMENTE_ASSOCIATO) != 0)
                                        trovato = true;
                                }
                                if (trovato)
                                    return openConfirm(actioncontext, "Attenzione! Alcune o tutte le righe siope non risultano associate completamente al CUP. Vuoi continuare?", OptionBP.CONFIRM_YES_NO, "doConfirmSalva");
                            }
                        }
                    }
                }
                return doConfirmSalva(actioncontext, OptionBP.YES_BUTTON);
            }
            return doConfirmSalva(actioncontext, OptionBP.NO_BUTTON);
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
    }

    public Forward doTab(ActionContext actioncontext, String s, String s1) {
        try {
            fillModel(actioncontext);
            Forward forward = super.doTab(actioncontext, s, s1);
            if (s.equals("tab") && s1.equals("tabDettaglioMandato")) {
                CRUDMandatoBP bp = (CRUDMandatoBP) actioncontext.getBusinessProcess();
                CRUDController crudcontroller = getController(actioncontext, "main.DocumentiPassiviSelezionati");
                MandatoBulk mandato = (MandatoBulk) bp.getModel();
                if (mandato != null &&
                        crudcontroller != null &&
                        crudcontroller.getElements().hasMoreElements() &&
                        crudcontroller.getSelection().isEmpty() &&
                        crudcontroller.getSelection().getFocus() == -1) {
                    crudcontroller.getSelection().setFocus(0);
                    return doSelection(actioncontext, "main.DocumentiPassiviSelezionati");
                }
            }
            return forward;
        } catch (Exception exception) {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doSelezionaRigaSiopeDaCompletare(ActionContext actioncontext) {
        try {
            fillModel(actioncontext);
            CRUDMandatoBP bp = (CRUDMandatoBP) actioncontext.getBusinessProcess();
            super.doTab(actioncontext, "tab", "tabDettaglioMandato");
            bp.selezionaRigaSiopeDaCompletare(actioncontext);
            return actioncontext.findDefaultForward();
        } catch (Exception exception) {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doCambiaStatoDaVariare(ActionContext actioncontext) {
        try {
            fillModel(actioncontext);
            CRUDMandatoBP bp = (CRUDMandatoBP) actioncontext.getBusinessProcess();
            bp.impostaMandatoDaVariare(actioncontext);
            return actioncontext.findDefaultForward();
        } catch (Exception exception) {
            return handleException(actioncontext, exception);
        }
    }

    /**
     * Gestisce la ricerca delle obbligazioni
     *
     *
     * @param actionContext	L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doRicercaObbligazione(ActionContext actionContext) {
        CRUDMandatoBP bp = Optional.ofNullable(getBusinessProcess(actionContext))
                .filter(CRUDMandatoBP.class::isInstance)
                .map(CRUDMandatoBP.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("CRUDMandatoBP non trovato!"));
        try {
            fillModel(actionContext);
            List<Mandato_rigaBulk> models = bp.getDocumentiPassiviSelezionati().getSelectedModels(actionContext);
            Mandato_rigaBulk mandato_rigaBulk =
                    Optional.ofNullable(models)
                            .orElse(Collections.emptyList())
                            .stream()
                            .reduce((a, b) -> {
                                throw new IllegalStateException();
                            })
                            .get();
            return basicDoRicercaObbligazione(actionContext, mandato_rigaBulk);
        } catch (IllegalStateException| NoSuchElementException _ex) {
            bp.setErrorMessage("Per procedere, selezionare un unico dettaglio su cui cambiare l'impegno!");
            return actionContext.findDefaultForward();
        } catch (Throwable e) {
            return handleException(actionContext, e);
        }
    }

    private Forward basicDoRicercaObbligazione(ActionContext actionContext, Mandato_rigaBulk mandato_rigaBulk) {
        try {
            //imposta il filtro per la ricerca
            Filtro_ricerca_obbligazioniVBulk filtro = new Filtro_ricerca_obbligazioniVBulk();

            CRUDMandatoBP bp = Optional.ofNullable(getBusinessProcess(actionContext))
                    .filter(CRUDMandatoBP.class::isInstance)
                    .map(CRUDMandatoBP.class::cast)
                    .orElseThrow(() -> new DetailedRuntimeException("CRUDMandatoBP non trovato!"));

            filtro.setFornitore(mandato_rigaBulk.getModalita_pagamento().getTerzo());
            filtro.setIm_importo(mandato_rigaBulk.getIm_mandato_riga());
            filtro.setCd_unita_organizzativa(mandato_rigaBulk.getCd_uo_doc_amm());
            filtro.setFl_importo(Boolean.TRUE);
            filtro.setData_scadenziario(null);
            filtro.setFl_data_scadenziario(Boolean.FALSE);
            filtro.setDs_obbligazione("Impegno per variazione");
            filtro.setDs_scadenza("Scadenza per variazione");


            //richiama il filtro RicercaAccertamentiBP
            it.cnr.jada.util.action.BulkBP robp= (it.cnr.jada.util.action.BulkBP) actionContext.getUserInfo()
                    .createBusinessProcess(actionContext, "RicercaObbligazioniBP", new Object[]{"MRSWTh"});
            robp.setModel(actionContext, filtro);
            //imposta il bringback su doContabilizzaAccertamenti
            actionContext.addHookForward("bringback", this, "doContabilizzaObbligazioni");
            HookForward hook= (HookForward) actionContext.findForward("bringback");
            return actionContext.addBusinessProcess(robp);
        } catch (Throwable e) {
            return handleException(actionContext, e);
        }
    }

    public Forward doContabilizzaObbligazioni(ActionContext context) throws BusinessProcessException, ApplicationException {
        HookForward caller = (HookForward) context.getCaller();
        CRUDMandatoBP bp = Optional.ofNullable(getBusinessProcess(context))
                .filter(CRUDMandatoBP.class::isInstance)
                .map(CRUDMandatoBP.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("CRUDMandatoBP non trovato!"));
        Obbligazione_scadenzarioBulk scadenza = Optional.ofNullable(caller.getParameter("obbligazioneSelezionata"))
                .filter(Obbligazione_scadenzarioBulk.class::isInstance)
                .map(Obbligazione_scadenzarioBulk.class::cast)
                .orElseThrow(() -> new ApplicationException("Selezionare un impegno da associare!"));
        try {
            List<Mandato_rigaBulk> models = bp.getDocumentiPassiviSelezionati().getSelectedModels(context);
            Mandato_rigaBulk mandato_rigaBulk =
                    Optional.ofNullable(models)
                            .orElse(Collections.emptyList())
                            .stream()
                            .reduce((a, b) -> {
                                throw new IllegalStateException();
                            })
                            .get();
            bp.cambiaObbligazioneScadenzario(context, mandato_rigaBulk, scadenza);
        } catch (Throwable e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }

    public Forward doSalvaVariazioneSostituzione (ActionContext context) throws RemoteException {
        return doSalva(context);
    }
}
