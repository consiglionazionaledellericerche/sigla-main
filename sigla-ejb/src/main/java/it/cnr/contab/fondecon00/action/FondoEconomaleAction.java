package it.cnr.contab.fondecon00.action;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.fondecon00.bp.*;
import it.cnr.contab.fondecon00.core.bulk.*;
import it.cnr.contab.fondecon00.ejb.FondoEconomaleComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.*;

import java.math.BigDecimal;
import java.rmi.RemoteException;

public class FondoEconomaleAction extends it.cnr.jada.util.action.CRUDAction {
    private Fondo_economaleBulk fondoSospeso;

    public FondoEconomaleAction() {
        super();
    }

    /**
     * Chiede conferma all'utente per la chisura del fondo economale
     *
     * @param context L'ActionContext della richiesta
     * @param option
     * @return Il Forward alla pagina di risposta
     */
    private it.cnr.jada.action.Forward basicDoConfermaChiudiFondo(
            ActionContext context,
            SospesoBulk sospesoDiChiusura) {

        FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
        Fondo_economaleBulk fondo = (Fondo_economaleBulk) bp.getModel();
        try {
            fondo.setSospeso_di_chiusura(sospesoDiChiusura);
            fondo.setToBeUpdated();
            setErrorMessage(context, "Per confermare la chiusura del fondo è necessario effettuare il salvataggio!");
//		FondoEconomaleComponentSession session = (FondoEconomaleComponentSession)bp.createComponentSession();
//		fondo = session.chiudeFondo(context.getUserContext(), fondo);
//		bp.commitUserTransaction();
//		bp.edit(context, fondo);
            return context.findDefaultForward();
        } catch (Throwable e) {
            try {
                //fondo.setSospeso_di_chiusura(null);
                bp.rollbackUserTransaction();
                bp.edit(context, fondo);
            } catch (it.cnr.jada.action.BusinessProcessException ex) {
                return handleException(context, ex);
            }
            return handleException(context, e);
        }
    }

    /**
     * Calcola il totale delle spese del fondo economale contenute nella lista passata
     */

    private java.math.BigDecimal calcolaTotaleSpesePer(
            it.cnr.jada.action.ActionContext context,
            java.util.List spese) {

        java.math.BigDecimal somma = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        if (spese != null && !spese.isEmpty())
            for (java.util.Iterator i = spese.iterator(); i.hasNext(); )
                somma = somma.add(((Fondo_spesaBulk) i.next()).getIm_ammontare_spesa());

        return somma;
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doAddToCRUDMain_AssociazioniMandati(ActionContext context) {

        try {
            FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
            bp.getAssociazioniMandati().getSelection().clearSelection();
            fillModel(context);
            it.cnr.jada.util.RemoteIterator ri = ((FondoEconomaleComponentSession) bp.createComponentSession()).cercaMandatiPerIntegrazioni(context.getUserContext(), (Fondo_economaleBulk) bp.getModel());
            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
            if (ri == null || ri.countElements() == 0) {
                bp.setMessage("Non esistono mandati disponibili per l'integrazione del fondo!");
                return context.findDefaultForward();
            }
            return select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(MandatoIBulk.class), "default", "doIntegraFondo");
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Crea, prepara e apre il filtro di ricerca delle scadenze di obbligazioni
     * valide per l'associazione a spesa del fondo
     */

    public it.cnr.jada.action.Forward doApriRicercaObbScad(it.cnr.jada.action.ActionContext context) {

        try {

            RicercaObbScadBP bp = (RicercaObbScadBP) context.createBusinessProcess(
                    "RicercaObbScadBP",
                    new Object[]{"MRSWTh"});
            //Nel caso in cui sto rientrando dopo aver eseguito la ricerca delle
            //spese da associare visualizzo il msg del bp associazione
            bp.setMessage(getBusinessProcess(context).getMessage());
            getBusinessProcess(context).setMessage(null);
            Filtro_ricerca_obbligazioniVBulk filtro = new Filtro_ricerca_obbligazioniVBulk();
            filtro.initializeForSearch(bp, context);
            filtro.setFondo((Fondo_economaleBulk) getBusinessProcess(context).getModel());
            bp.setModel(context, filtro);
            context.addHookForward("bringback", this, "doAssociaSpeseObbligazione");
            return context.addBusinessProcess(bp);

        } catch (Throwable e) {
            try {
                context.getBusinessProcess().rollbackUserTransaction();
            } catch (it.cnr.jada.action.BusinessProcessException ex) {
                return handleException(context, ex);
            }
            return handleException(context, e);
        }
    }

    /**
     * Apre in VIEW la spesa del fondo selezionata dopo la ricerca delle spese associate
     * alla scadenza
     */

    public it.cnr.jada.action.Forward doApriSpesaSelezionata(it.cnr.jada.action.ActionContext context) {

        try {

            it.cnr.jada.action.HookForward caller = (it.cnr.jada.action.HookForward) context.getCaller();
            Fondo_spesaBulk spesaSelezionata = (Fondo_spesaBulk) caller.getParameter("focusedElement");

            if (spesaSelezionata != null) {
                FondoEconomaleBP bp = (FondoEconomaleBP) context.getBusinessProcess();
                FondoSpesaBP spesaBp = (FondoSpesaBP) context.createBusinessProcess("FondoSpesaBP", new Object[]{"VRSTh"});
                spesaBp.setFondoEconomaleCorrente((Fondo_economaleBulk) bp.getModel());
                spesaBp.edit(context, spesaSelezionata);
                return context.addBusinessProcess(spesaBp);
            }
            return context.findDefaultForward();

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Apre il pannello di associazione delle spese per associare o disassociare
     * alla/dalla scadenza scelta le spese selezionate
     */

    public it.cnr.jada.action.Forward doAssociaSpeseObbligazione(it.cnr.jada.action.ActionContext context) {
        try {

            it.cnr.jada.action.HookForward caller = (it.cnr.jada.action.HookForward) context.getCaller();
            it.cnr.jada.bulk.OggettoBulk selezione = (it.cnr.jada.bulk.OggettoBulk) caller.getParameter("scadenzaSelezionata");
            Filtro_ricerca_obbligazioniVBulk filtroUtilizzato = (Filtro_ricerca_obbligazioniVBulk) caller.getParameter("filtroUtilizzato");
            if (selezione != null) {
                Fondo_economaleBulk fondo = (Fondo_economaleBulk) ((BulkBP) context.getBusinessProcess()).getModel();
                if (filtroUtilizzato != null)
                    fondo.setFl_associata_for_search(filtroUtilizzato.getFl_associate());
                AssociaSpeseObbligazioneBP bp =
                        (AssociaSpeseObbligazioneBP) context.getUserInfo(
                        ).createBusinessProcess(
                                context,
                                "AssociaSpeseObbligazioneBP",
                                new Object[]{
                                        "MRSWTr",
                                        fondo,
                                        (Obbligazione_scadenzarioBulk) selezione
                                }
                        );

                it.cnr.jada.util.RemoteIterator ri = bp.createComponentSession().cercaSpeseAssociabili(
                        context.getUserContext(),
                        (Fondo_economaleBulk) ((BulkBP) context.getBusinessProcess()).getModel(),
                        (Obbligazione_scadenzarioBulk) selezione);
                ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
                if (ri != null && ri.countElements() == 0) {
                    it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                    if (filtroUtilizzato.getFl_associate() != null && filtroUtilizzato.getFl_associate().booleanValue())
                        getBusinessProcess(context).setMessage("Non esiste alcuna spesa non documentata associata all'impegno selezionato!");
                    else
                        getBusinessProcess(context).setMessage("Non esiste alcuna spesa non documentata da associare!");
                    return doApriRicercaObbScad(context);
                } else {
                    bp.setIterator(context, ri);
                    bp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Fondo_spesaBulk.class));
                    context.addHookForward("bringback", this, "doBringBackAssociaSpeseObbligazione");
                    return context.addBusinessProcess(bp);
                }
            }
            return context.findDefaultForward();

        } catch (Throwable e) {
            try {
                context.getBusinessProcess().rollbackUserTransaction();
            } catch (it.cnr.jada.action.BusinessProcessException ex) {
                return handleException(context, ex);
            }
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di azzeramento del searchtool "economo"
     *
     * @param context L'ActionContext della richiesta
     * @param fondo   L'OggettoBulk padre del searchtool
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBlankSearchCreditore_scadenza(
            ActionContext context,
            Fondo_economaleBulk fondo)
            throws java.rmi.RemoteException {

        try {
            if (fondo.getScadenza_ricerca() == null)
                return doBlankSearchScadenza_ricerca(context, fondo);
            TerzoBulk tb = new TerzoBulk();
            tb.setAnagrafico(new AnagraficoBulk());
            fondo.setCreditore_scadenza(tb);
            fondo.getScadenza_ricerca().getObbligazione().setCreditore(tb);

            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di azzeramento del searchtool "economo"
     *
     * @param context L'ActionContext della richiesta
     * @param fondo   L'OggettoBulk padre del searchtool
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBlankSearchEconomo(ActionContext context,
                                        Fondo_economaleBulk fondo)
            throws java.rmi.RemoteException {

        try {
            TerzoBulk tb = new TerzoBulk();
            tb.setAnagrafico(new AnagraficoBulk());
            fondo.setEconomo(tb);
            fondo.setModalita(null);
            fondo.setModalita_pagamento(null);
            fondo.setBanca(null);

            return doBlankSearchMandato(context, fondo);

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di azzeramento del searchtool "mandato"
     *
     * @param context L'ActionContext della richiesta
     * @param fondo   L'OggettoBulk padre del searchtool
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBlankSearchMandato(ActionContext context,
                                        Fondo_economaleBulk fondo)
            throws java.rmi.RemoteException {

        try {
            if (hasSpese(context, fondo))
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile cambiare il mandato ad un fondo economale per il quale sono già state registrate delle spese!");

            FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
            MandatoIBulk mandato = new MandatoIBulk();
            mandato.initializeForSearch(bp, context);
            mandato.setIm_mandato(null);
            mandato.setIm_pagato(null);
            mandato.setIm_ritenute(null);
            fondo.setMandato(mandato);

            if (!bp.isSearching()) {
                fondo.resetImporti();
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di azzeramento del searchtool "scadenza_ricerca"
     *
     * @param context L'ActionContext della richiesta
     * @param fondo   L'OggettoBulk padre del searchtool
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBlankSearchScadenza_ricerca(
            ActionContext context,
            Fondo_economaleBulk fondo)
            throws java.rmi.RemoteException {

        try {
            Obbligazione_scadenzarioBulk scadenza = new Obbligazione_scadenzarioBulk();
            ObbligazioneBulk obbl = new ObbligazioneBulk();
            //TerzoBulk tb = new TerzoBulk();
            //tb.setAnagrafico(new AnagraficoBulk());
            obbl.setCreditore(null);
            obbl.setCd_cds(fondo.getCd_cds());
            //obbl.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_OBB);
            obbl.setEsercizio(fondo.getEsercizio());
            scadenza.setObbligazione(obbl);
            fondo.setScadenza_ricerca(scadenza);

            resetRicercaSpese(context, fondo);

            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il temine della procedura di associazione/disassociazione
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public it.cnr.jada.action.Forward doBringBackAssociaSpeseObbligazione(it.cnr.jada.action.ActionContext context) {

        BulkBP bp = (BulkBP) context.getBusinessProcess();
        try {
            bp.setMessage("Operazione eseguita correttamente!");
            bp.setDirty(false);
            return context.findDefaultForward();
        } catch (Throwable e) {
            try {
                bp.rollbackUserTransaction();
                bp.setErrorMessage("Operazione annullata per un errore!");
            } catch (it.cnr.jada.action.BusinessProcessException ex) {
                return handleException(context, ex);
            }
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di assegnamento al crudtool "crea_economo"
     *
     * @param context        L'ActionContext della richiesta
     * @param fondo
     * @param economoTrovato
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBringBackCRUDEconomo(ActionContext context,
                                          Fondo_economaleBulk fondo,
                                          TerzoBulk economoTrovato)
            throws java.rmi.RemoteException {

        return doBringBackSearchEconomo(
                context,
                fondo,
                economoTrovato);
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di assegnamento al crudtool "crea_economo"
     *
     * @param context        L'ActionContext della richiesta
     * @param fondo
     * @param economoTrovato
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBringBackCRUDCrea_economo(ActionContext context,
                                               Fondo_economaleBulk fondo,
                                               TerzoBulk economoTrovato)
            throws java.rmi.RemoteException {

        return doBringBackSearchEconomo(
                context,
                fondo,
                economoTrovato);
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di azzeramento del searchtool "economo"
     *
     * @param context L'ActionContext della richiesta
     * @param fondo   L'OggettoBulk padre del searchtool
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBringBackSearchCreditore_scadenza(
            ActionContext context,
            Fondo_economaleBulk fondo,
            TerzoBulk creditoreTrovato)
            throws java.rmi.RemoteException {

        if (creditoreTrovato != null &&
                fondo.getScadenza_ricerca() != null) {
            fondo.getScadenza_ricerca().getObbligazione().setCreditore(creditoreTrovato);
            fondo.setCreditore_scadenza(creditoreTrovato);
        }
        return context.findDefaultForward();
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di ricerca del searchtool "economo"
     *
     * @param context        L'ActionContext della richiesta
     * @param fondo          L'OggettoBulk padre del searchtool
     * @param economoTrovato L'OggettoBulk selezionato dall'utente
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBringBackSearchEconomo(ActionContext context,
                                            Fondo_economaleBulk fondo,
                                            TerzoBulk economoTrovato)
            throws java.rmi.RemoteException {

        try {

            if (economoTrovato != null) {
                FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
                fondo.setEconomo(economoTrovato);
                fondo.setBanca(null);
                FondoEconomaleComponentSession fecs = (FondoEconomaleComponentSession) bp.createComponentSession();
                java.util.Collection coll = fecs.findModalita(context.getUserContext(), fondo);
                fondo.setModalita(coll);
            }

            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di ricerca del searchtool "mandato"
     *
     * @param context        L'ActionContext della richiesta
     * @param fondo          L'OggettoBulk padre del searchtool
     * @param mandatoTrovato L'OggettoBulk selezionato dall'utente
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBringBackSearchMandato(ActionContext context,
                                            Fondo_economaleBulk fondo,
                                            MandatoIBulk mandatoTrovato)
            throws java.rmi.RemoteException {

        try {

            if (mandatoTrovato != null) {

                if (hasSpese(context, fondo))
                    throw new it.cnr.jada.comp.ApplicationException("Non è possibile cambiare il mandato ad un fondo economale per il quale sono già state registrate delle spese!");

                if (!((CRUDBP) getBusinessProcess(context)).isSearching()) {
                    java.math.BigDecimal importo = mandatoTrovato.getIm_pagato();
                    if (importo == null || importo.compareTo(mandatoTrovato.getIm_mandato()) != 0)
                        throw new it.cnr.jada.comp.ApplicationException("Il mandato selezionato non è completamente riscontrato! Operazione annullata.");
                    fondo.setMandato(mandatoTrovato);
                    if (fondo.getDs_fondo() == null || fondo.getDs_fondo().equals("")) {
                        String ds = "Fondo economale generato dal mandato \"";
                        ds += ((mandatoTrovato.getDs_mandato() == null) ?
                                String.valueOf(mandatoTrovato.getPg_mandato().longValue()) :
                                mandatoTrovato.getDs_mandato());
                        ds += "\"";
                        fondo.setDs_fondo(ds);
                    }
                    fondo.setIm_ammontare_fondo(importo);
                    fondo.setIm_ammontare_iniziale(importo);
                    fondo.setIm_residuo_fondo(importo);
                    fondo.setIm_totale_reintegri(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                    fondo.setIm_totale_spese(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                    fondo.setIm_totale_netto_spese(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                } else
                    fondo.setMandato(mandatoTrovato);
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di ricerca del searchtool "scadenza_ricerca"
     *
     * @param context         L'ActionContext della richiesta
     * @param fondo           L'OggettoBulk padre del searchtool
     * @param scadenzaTrovata L'OggettoBulk selezionato dall'utente
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBringBackSearchScadenza_ricerca(ActionContext context,
                                                     Fondo_economaleBulk fondo,
                                                     Obbligazione_scadenzarioBulk scadenzaTrovata)
            throws java.rmi.RemoteException {

        try {
            if (scadenzaTrovata != null) {
                fondo.setScadenza_ricerca(scadenzaTrovata);
            }
            resetRicercaSpese(context, fondo);
            ((CRUDBP) context.getBusinessProcess()).setDirty(false);
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Calcola il totale delle spese associate alla scadenza dell'obbligazione
     * ricercata nel pannello ricerca del fondo
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public it.cnr.jada.action.Forward doCalcolaTotaleSpesePerObblig(it.cnr.jada.action.ActionContext context) {

        FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
        Fondo_economaleBulk fondo = (Fondo_economaleBulk) bp.getModel();
        try {
            if (fondo.getScadenza_ricerca() == null ||
                    fondo.getScadenza_ricerca().getCrudStatus() != it.cnr.jada.bulk.OggettoBulk.NORMAL)
                throw new it.cnr.jada.comp.ApplicationException("Specificare una scadenza per la ricerca!");

            FondoEconomaleComponentSession session = (FondoEconomaleComponentSession) bp.createComponentSession();
            java.math.BigDecimal tot = session.calcolaTotaleSpese(
                    context.getUserContext(),
                    fondo,
                    fondo.getScadenza_ricerca());
            if (new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP).compareTo(tot) != 0) {
                if (fondo.getScadenza_ricerca().getIm_scadenza().compareTo(tot) != 0)
                    fondo.setSquared(Boolean.FALSE);
                else
                    fondo.setSquared(Boolean.TRUE);
            } else
                fondo.setSquared(null);

            fondo.setImporto_totale_scadenze_non_doc(tot);
            return context.findDefaultForward();

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Esegue la chiusura del fondo economale
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public it.cnr.jada.action.Forward doChiudiFondo(it.cnr.jada.action.ActionContext context) {

        try {
            return openConfirm(
                    context,
                    "Sei sicuro di voler chiudere il fondo economale?",
                    it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO,
                    "doSelezionaSospesoDiChiusuraFondo");
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Esegue la chiusura del fondo economale
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public it.cnr.jada.action.Forward doChiudiSpese(it.cnr.jada.action.ActionContext context) {

        try {
            FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
            Fondo_economaleBulk fondo = (Fondo_economaleBulk) bp.getModel();

            if (((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getVal01(context.getUserContext(), it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()), "*", "FONDO_ECONOMALE", "REINTEGRO_OBBLIGATORIO") != null &&
                    (((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getVal01(context.getUserContext(), it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()), "*", "FONDO_ECONOMALE", "REINTEGRO_OBBLIGATORIO").compareTo("S") == 0)) {
                if (fondo.getIm_totale_spese() != null && fondo.getIm_totale_spese().compareTo(BigDecimal.ZERO) != 0)
                    throw new it.cnr.jada.comp.ApplicationException("Prima di procedere è necessario reintegrare tutte le spese. Operazione annullata!");
                else
                    return doConfermaChiudiSpese(context, OptionBP.YES_BUTTON);
            } else {
                if (fondo.getIm_totale_spese() != null && fondo.getIm_totale_spese().compareTo(BigDecimal.ZERO) != 0)
                    return openConfirm(
                            context,
                            "Sei sicuro di voler chiudere tutte le spese del fondo economale non ancora reintegrate?",
                            it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO,
                            "doConfermaChiudiSpese");
                else
                    return doConfermaChiudiSpese(context, OptionBP.YES_BUTTON);

            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Chiede conferma all'utente per la chisura del fondo economale
     *
     * @param context L'ActionContext della richiesta
     * @param option
     * @return Il Forward alla pagina di risposta
     */
    public it.cnr.jada.action.Forward doConfermaChiudiFondo(ActionContext context) {

        HookForward fwd = (HookForward) context.getCaller();
        SospesoBulk sospesoSelezionato = (SospesoBulk) fwd.getParameter("focusedElement");
        FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
        if (sospesoSelezionato != null) {
            return basicDoConfermaChiudiFondo(context, sospesoSelezionato);
        } else {
            try {
                bp.rollbackUserTransaction();
            } catch (it.cnr.jada.action.BusinessProcessException e) {
                return handleException(context, e);
            }
            return context.findDefaultForward();
        }
    }

    /**
     * Chiede conferma all'utente per la chisura del fondo economale
     *
     * @param context L'ActionContext della richiesta
     * @param option
     * @return Il Forward alla pagina di risposta
     */
    public it.cnr.jada.action.Forward doConfermaChiudiSpese(
            it.cnr.jada.action.ActionContext context,
            int option) {

        if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
            FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
            Fondo_economaleBulk fondo = (Fondo_economaleBulk) bp.getModel();
            try {
                fondo.setToBeUpdated();
                FondoEconomaleComponentSession session = (FondoEconomaleComponentSession) bp.createComponentSession();
                fondo = session.chiudeSpese(context.getUserContext(), fondo);
                bp.commitUserTransaction();
                bp.edit(context, fondo);
                bp.setMessage("Operazione Completata");
            } catch (Throwable e) {
                try {
                    bp.rollbackUserTransaction();
                    bp.edit(context, fondo);
                } catch (it.cnr.jada.action.BusinessProcessException ex) {
                    return handleException(context, ex);
                }
                return handleException(context, e);
            }
        }
        return context.findDefaultForward();
    }

    /**
     * Integra il fondo con l'importo del mandato selezionato
     */
    public Forward doIntegraFondo(ActionContext context) {

        it.cnr.jada.action.HookForward caller = (it.cnr.jada.action.HookForward) context.getCaller();
        MandatoIBulk mandatoSelezionato = (MandatoIBulk) caller.getParameter("focusedElement");
        try {
            if (mandatoSelezionato != null) {
                java.math.BigDecimal importo = mandatoSelezionato.getIm_pagato();
                if (importo == null || importo.compareTo(mandatoSelezionato.getIm_mandato()) != 0)
                    throw new it.cnr.jada.comp.ApplicationException("Il mandato selezionato non è completamente riscontrato! Operazione annullata.");

                FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
                Fondo_economaleBulk fondo = (Fondo_economaleBulk) bp.getModel();

                if (fondo.getAssociazioni_mandati() != null)
                    for (java.util.Iterator i = fondo.getAssociazioni_mandati().iterator(); i.hasNext(); ) {
                        Ass_fondo_eco_mandatoBulk ass = (Ass_fondo_eco_mandatoBulk) i.next();
                        if (mandatoSelezionato.equalsByPrimaryKey(ass.getMandato()))
                            throw new it.cnr.jada.comp.ApplicationException("Il mandato selezionato è già stato inserito. Operazione annullata!");

                    }

                Ass_fondo_eco_mandatoBulk associazione = new Ass_fondo_eco_mandatoBulk(
                        fondo.getCd_cds(),
                        fondo.getCd_codice_fondo(),
                        fondo.getCd_unita_organizzativa(),
                        fondo.getEsercizio());
                associazione.setMandato(mandatoSelezionato);

                fondo.setIm_ammontare_fondo(fondo.getIm_ammontare_fondo().add(importo));
                fondo.setIm_residuo_fondo(fondo.getIm_residuo_fondo().add(importo));

                try {
                    bp.getAssociazioniMandati().add(context, associazione);
                    fondo.setToBeUpdated();
                    bp.setDirty(true);
                } catch (Throwable e) {
                    return handleException(context, e);
                }
            }
            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Ricerca le banche associate alla modalità di pagamento dell'economo
     */

    public Forward doOnModalitaPagamentoChange(ActionContext context) {

        try {
            fillModel(context);
            FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
            Fondo_economaleBulk fondo = (Fondo_economaleBulk) bp.getModel();
            if (fondo.getModalita_pagamento() != null) {
                FondoEconomaleComponentSession fecs = (FondoEconomaleComponentSession) bp.createComponentSession();
                java.util.Collection coll = fecs.findListabanche(context.getUserContext(), fondo);
                fondo.setBanca((coll == null || coll.isEmpty()) ? null : (BancaBulk) new java.util.Vector(coll).firstElement());
                bp.setModel(context, fondo);
            } else
                fondo.setBanca(null);
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * Reintegra le spese del fondo economale selezionate controllando il valore minimo
     * di reintegro specificato.
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public it.cnr.jada.action.Forward doReintegraSpeseSelezionate(it.cnr.jada.action.ActionContext context) {

        FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);

        try {
            it.cnr.jada.action.HookForward caller = (it.cnr.jada.action.HookForward) context.getCaller();
            java.util.List speseSelezionate = (java.util.List) caller.getParameter("speseSelezionate");

            if (speseSelezionate != null && !speseSelezionate.isEmpty()) {
                Fondo_economaleBulk fondo = (Fondo_economaleBulk) bp.getModel();
                java.math.BigDecimal totaleSpese = calcolaTotaleSpesePer(context, speseSelezionate);
                if (fondo.getIm_limite_min_reintegro().compareTo(totaleSpese) > 0)
                    throw new it.cnr.jada.comp.ApplicationException("L'importo delle spese di \"" + totaleSpese.toString() + "\" è inferiore al limite minimo di reintegro specificato nel fondo!");
                fondo = ((FondoEconomaleComponentSession) bp.createComponentSession()).
                        reintegraSpese(
                                context.getUserContext(),
                                fondo,
                                speseSelezionate);
                bp.commitUserTransaction();
                bp.edit(context, fondo);
                bp.setDirty(false);
                bp.setTab("subtab", "tabFondoEconomaleImporti");
                bp.setMessage("Operazione completata.");
            }
            return context.findDefaultForward();

        } catch (Throwable e) {
            try {
                bp.rollbackUserTransaction();
            } catch (it.cnr.jada.action.BusinessProcessException ex) {
                return handleException(context, ex);
            }
            return handleException(context, e);
        }
    }

    /**
     * Crea prepara e apre il filtro di ricerca delle spese reintegrabili
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public it.cnr.jada.action.Forward doReintegro(it.cnr.jada.action.ActionContext context) {

        try {

            RicercaSpeseBP bp = (RicercaSpeseBP) context.createBusinessProcess(
                    "RicercaSpeseBP",
                    new Object[]{"MRSWTh"});
            Filtro_ricerca_speseVBulk filtro = new Filtro_ricerca_speseVBulk();
            filtro.initializeForSearch(bp, context);
            filtro.setFondo_economale((Fondo_economaleBulk) ((CRUDBP) context.getBusinessProcess()).getModel());
            filtro.setMultiSelection(true);
            bp.setModel(context, filtro);
            context.addHookForward("bringback", this, "doReintegraSpeseSelezionate");
            return context.addBusinessProcess(bp);

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il comando di eliminazione di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doRemoveFromCRUDMain_AssociazioniMandati(ActionContext context) {

        FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
        Fondo_economaleBulk fondo = (Fondo_economaleBulk) bp.getModel();
        java.math.BigDecimal imAmmFondoOriginale = fondo.getIm_ammontare_fondo();
        java.math.BigDecimal imResiduoFondoOriginale = fondo.getIm_residuo_fondo();

        try {
            fillModel(context);

            if (!bp.getAssociazioniMandati().getSelection().isEmpty()) {
                for (SelectionIterator i = bp.getAssociazioniMandati().getSelection().iterator(); i.hasNext(); ) {
                    Ass_fondo_eco_mandatoBulk ass = (Ass_fondo_eco_mandatoBulk) bp.getAssociazioniMandati().getDetails().get(i.nextIndex());
                    MandatoIBulk mandatoSelezionato = ass.getMandato();

                    fondo.setIm_ammontare_fondo(fondo.getIm_ammontare_fondo().subtract(mandatoSelezionato.getIm_pagato()));
                    fondo.setIm_residuo_fondo(fondo.getIm_residuo_fondo().subtract(mandatoSelezionato.getIm_pagato()));

                    //Controllo che l'eliminazione non comporti un importo residuo del
                    //fondo minore del totale delle spese registrate
                    if (fondo.getIm_residuo_fondo().compareTo(fondo.getIm_totale_spese()) < 0)
                        throw new it.cnr.jada.comp.ApplicationException("Non è possibile scollegare il mandato \"" + mandatoSelezionato.getDs_mandato() + "\" perché l'importo delle spese registrate\n supererebbe l'importo residuo del fondo stesso!");
                }

                bp.getAssociazioniMandati().remove(context);
                fondo.setToBeUpdated();
                bp.setDirty(true);
            }

            return context.findDefaultForward();
        } catch (Throwable e) {
            fondo.setIm_ammontare_fondo(imAmmFondoOriginale);
            fondo.setIm_residuo_fondo(imResiduoFondoOriginale);
            return handleException(context, e);
        }
    }

    /**
     * Ricerca le spese associate alla scadenza della obbligazione ricercata
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public it.cnr.jada.action.Forward doRicercaSpeseAssociate(it.cnr.jada.action.ActionContext context) {

        FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
        try {
            fillModel(context);
            Fondo_economaleBulk fondo = (Fondo_economaleBulk) bp.getModel();
            if (fondo.getScadenza_ricerca() == null ||
                    fondo.getScadenza_ricerca().getCrudStatus() != it.cnr.jada.bulk.OggettoBulk.NORMAL)
                throw new it.cnr.jada.comp.ApplicationException("Specificare una scadenza per la ricerca!");

            FondoEconomaleComponentSession session = (FondoEconomaleComponentSession) bp.createComponentSession();
            it.cnr.jada.util.RemoteIterator ri = session.cercaSpeseDelFondo(
                    context.getUserContext(),
                    fondo,
                    fondo.getScadenza_ricerca());
            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
            if (ri == null || ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                bp.setMessage("La ricerca non ha fornito alcun risultato.");
                return context.findDefaultForward();
            }
            return select(
                    context,
                    ri,
                    it.cnr.jada.bulk.BulkInfo.getBulkInfo(Fondo_spesaBulk.class),
                    null,
                    "doApriSpesaSelezionata");
        } catch (Throwable e) {
            return handleException(context, e);
        }

    }

    /**
     * Ricerca le banche valide
     */

    public Forward doSearchListabanche(ActionContext context) {

        Fondo_economaleBulk fondo = (Fondo_economaleBulk) getBusinessProcess(context).getModel();
        return search(context, getFormField(context, "main.listabanche"), fondo.getModalita_pagamento().getTiPagamentoColumnSet());
    }

    /**
     * Ricerca il mandato di apertura del fondo economale
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doSearchMandato(ActionContext context)
            throws java.rmi.RemoteException {

        try {
            FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
            bp.fillModel(context);
            if (!bp.isSearching()) {
                Fondo_economaleBulk fondo = (Fondo_economaleBulk) bp.getModel();
                if (fondo.getEconomo() == null || fondo.getEconomo().getCrudStatus() != it.cnr.jada.bulk.OggettoBulk.NORMAL)
                    throw new it.cnr.jada.comp.ApplicationException("Prima di cercare il mandato specificare l'economo!");
            }
            FormField field = getFormField(context, "main.mandato");
            return search(context, field, null);
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Ricerca il mandato di apertura del fondo economale
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doSearchScadenza_ricerca(ActionContext context)
            throws java.rmi.RemoteException {

        try {
            FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
            bp.fillModel(context);
            Fondo_economaleBulk fondo = (Fondo_economaleBulk) bp.getModel();
            if (fondo.getScadenza_ricerca() != null) {
                TerzoBulk creditore = fondo.getScadenza_ricerca().getObbligazione().getCreditore();
                if (creditore != null &&
                        creditore.getCrudStatus() != it.cnr.jada.bulk.OggettoBulk.NORMAL)
                    throw new it.cnr.jada.comp.ApplicationException("Prima di cercare la scadenza, ricercare il creditore!");
            }
            FormField field = getFormField(context, "main.scadenza_ricerca");
            return search(context, field, null);
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Chiede conferma all'utente per la chisura del fondo economale
     *
     * @param context L'ActionContext della richiesta
     * @param option
     * @return Il Forward alla pagina di risposta
     */
    public it.cnr.jada.action.Forward doSelezionaSospesoDiChiusuraFondo(
            it.cnr.jada.action.ActionContext context,
            int option) {

        if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
            FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
            Fondo_economaleBulk fondo = (Fondo_economaleBulk) bp.getModel();
            if (fondo.isReversaleNecessaria())
                try {
                    FondoEconomaleComponentSession session = (FondoEconomaleComponentSession) bp.createComponentSession();
                    it.cnr.jada.util.RemoteIterator ri = session.cercaSospesiDiChiusuraFondo(context.getUserContext(), fondo);
                    ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
                    if (ri == null || ri.countElements() == 0) {
                        it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                        bp.setMessage("Non è stato trovato alcun sospeso valido per la chiusura del fondo economale! Operazione annullata.");
                        bp.rollbackUserTransaction();
                        return context.findDefaultForward();
                    } else {
                        SelezionatoreListaBP nbp = (SelezionatoreListaBP) context.createBusinessProcess("Selezionatore");
                        nbp.setIterator(context, ri);
                        it.cnr.jada.bulk.BulkInfo bulkInfo = it.cnr.jada.bulk.BulkInfo.getBulkInfo(SospesoBulk.class);
                        nbp.setFormField(bp.getFormField("cd_sospeso_di_chiusura"));
                        nbp.setBulkInfo(bulkInfo);
                        nbp.setColumns(bulkInfo.getColumnFieldPropertyDictionary());
                        context.addHookForward("seleziona", this, "doConfermaChiudiFondo");
                        it.cnr.jada.action.HookForward hook = (it.cnr.jada.action.HookForward) context.findForward("seleziona");
                        return context.addBusinessProcess(nbp);
                    }
                } catch (Throwable e) {
                    try {
                        bp.rollbackUserTransaction();
                    } catch (it.cnr.jada.action.BusinessProcessException ex) {
                        return handleException(context, ex);
                    }
                    return handleException(context, e);
                }
        }
        return context.findDefaultForward();
    }

    /**
     * Metodo utilizzato per gestire dell'eccezione generata dalla squadratura
     * del fondo economale. Pone quesito all'utente per la continuazione e se la
     * risposta è affermativa salva il fondo
     *
     * @param context {@link ActionContext } in uso.
     * @param ex      Eccezione da gestire.
     * @return Forward
     * @throws RemoteException
     * @see #
     */

    public it.cnr.jada.action.Forward handleException(it.cnr.jada.action.ActionContext context, Throwable ex) {
        try {
            throw ex;
        } catch (it.cnr.contab.fondecon00.comp.ErroreSquadraturaFondo e) {

            FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
            fondoSospeso = (Fondo_economaleBulk) e.getSospeso();
            try {
                return openConfirm(context, e.getMessage(), it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doSalva");
            } catch (Throwable twb) {
                bp.setErrorMessage(e.getMessage());
                return context.findDefaultForward();
            }

        } catch (Throwable e) {
            return super.handleException(context, e);
        }
    }

    private boolean hasSpese(
            ActionContext context,
            Fondo_economaleBulk fondo)
            throws it.cnr.jada.action.BusinessProcessException,
            java.rmi.RemoteException,
            it.cnr.jada.comp.ComponentException {

        FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
        if (!bp.isSearching() &&
                (fondo.getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL ||
                        fondo.getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.TO_BE_UPDATED)) {
            FondoEconomaleComponentSession session = (FondoEconomaleComponentSession) ((FondoEconomaleBP) context.getBusinessProcess()).createComponentSession();
            Filtro_ricerca_speseVBulk filtro = new Filtro_ricerca_speseVBulk();
            filtro.setFondo_economale(fondo);
            it.cnr.jada.util.RemoteIterator ri = session.cercaSpese(
                    context.getUserContext(),
                    filtro);
            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
            try {
                return (ri != null && ri.countElements() != 0);
            } finally {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
            }
        }
        return false;
    }

    /**
     * Prepara il fondo economale per l'esecuzione di una nuova ricerca delle spese
     * associate all'obbligazione ricercata
     */
    private void resetRicercaSpese(
            ActionContext context,
            Fondo_economaleBulk fondo) {

        if (fondo != null) {
            java.math.BigDecimal tot = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
            fondo.setImporto_totale_scadenze_non_doc(tot);
            fondo.setSpesaReintegrata(fondo.IGNORA);
            fondo.setSpesaDocumentata(fondo.IGNORA);
        }
    }

    public Forward doSalva(ActionContext context)
            throws RemoteException {
        FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
        Fondo_economaleBulk fondo = (Fondo_economaleBulk) bp.getModel();
        if (fondo.getCd_sospeso() != null && fondo.isReversaleNecessaria()) {
            try {
                return openConfirm(
                        context,
                        "Sei sicuro di voler chiudere il fondo economale con un sospeso " + fondo.getSospeso_di_chiusura().getEsercizio() + "?",
                        it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doConfermaSalvaChiudiFondo");
            } catch (BusinessProcessException e) {
                return super.handleException(context, e);
            }
        }
        return super.doSalva(context);
    }

    public it.cnr.jada.action.Forward doConfermaSalvaChiudiFondo(
            it.cnr.jada.action.ActionContext context,
            int option) {
        FondoEconomaleBP bp = (FondoEconomaleBP) getBusinessProcess(context);
        Fondo_economaleBulk fondo = (Fondo_economaleBulk) bp.getModel();
        if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
            try {
                FondoEconomaleComponentSession session = (FondoEconomaleComponentSession) bp.createComponentSession();
                fondo = session.chiudeFondo(context.getUserContext(), fondo);
                bp.commitUserTransaction();
                bp.edit(context, fondo);
            } catch (Throwable e) {
                return super.handleException(context, e);
            }
        }
        return context.findDefaultForward();
    }

}
