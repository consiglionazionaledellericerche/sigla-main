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

package it.cnr.contab.docamm00.actions;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.docamm00.bp.*;
import it.cnr.contab.docamm00.comp.DocumentoAmministrativoComponentSession;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.doccont00.bp.CRUDVirtualAccertamentoBP;
import it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP;
import it.cnr.contab.doccont00.core.bulk.AccertamentoOrdBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession;
import it.cnr.contab.inventario01.bp.CRUDScaricoInventarioBP;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.ejb.NumerazioneTempBuonoComponentSession;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Insert the type's description here.
 * Creation date: (10/30/2001 3:11:45 PM)
 *
 * @author: Roberto Peli
 */
public class CRUDNotaDiCreditoAction extends CRUDFatturaPassivaAction {
    /**
     * CRUDNotaDiCreditoAction constructor comment.
     */

    public CRUDNotaDiCreditoAction() {
        super();
    }

    /**
     * Ricalcola l'importo disponibile per le note di credito sul dettagli riga rispetto al vecchio totale.
     * Reimplementato
     */
    protected void basicCalcolaImportoDisponibileNC(
            ActionContext context,
            Fattura_passiva_rigaBulk riga,
            java.math.BigDecimal vecchioTotale)
            throws it.cnr.jada.bulk.FillException {

        Nota_di_credito_rigaBulk rigaND = (Nota_di_credito_rigaBulk) riga;
        if (rigaND.getQuantita() == null) rigaND.setQuantita(new java.math.BigDecimal(1));
        if (rigaND.getPrezzo_unitario() == null) rigaND.setPrezzo_unitario(new java.math.BigDecimal(0));
        if (rigaND.getIm_iva() == null)
            rigaND.setIm_iva(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));

        rigaND.calcolaCampiDiRiga();
        java.math.BigDecimal totaleDiRiga = rigaND.getIm_imponibile().add(rigaND.getIm_iva());
        Fattura_passiva_rigaIBulk rigaFP = rigaND.getRiga_fattura_origine();
        java.math.BigDecimal nuovoImportoDisponibile = rigaFP.getIm_diponibile_nc().subtract(totaleDiRiga.subtract(vecchioTotale));
        if (nuovoImportoDisponibile.signum() < 0)
            throw new it.cnr.jada.bulk.FillException("Attenzione: l'importo di storno massimo ancora disponibile è di " + rigaFP.getIm_diponibile_nc() + " EUR!");
        rigaFP.setIm_diponibile_nc(nuovoImportoDisponibile.setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
    }

    /**
     * Gestisce l'associazione della scadenza riportata con il documento amm.
     * Se non esiste l'aggiunge, altrimenti sincronizza l'istanza già presente
     *
     * @param context  L'ActionContext della richiesta
     * @param scadenza scadenza selezionata dall'utente con riporta
     * @return Il Forward alla pagina di risposta
     */

    private Forward basicDoBringBackOpenAccertamentiWindow(
            ActionContext context,
            Accertamento_scadenzarioBulk scadenza) {

        try {
            CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
            Accertamento_scadenzarioBulk oldScadenza = (Accertamento_scadenzarioBulk) bp.getAccertamentiController().getModel();
            resyncAccertamentoScadenzario(context, oldScadenza, scadenza);
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * Calcola i totali del dettaglio 'riga' e se necessario aggiorna l'importo disp
     * per le note di credito relativo al dettaglio stesso.
     * Reimplementato (cambia il calcolo dell'importoDisponibileNC)
     */

    protected void basicDoCalcolaTotaliDiRiga(
            ActionContext context,
            Fattura_passiva_rigaBulk riga,
            java.math.BigDecimal vecchioTotale)
            throws it.cnr.jada.bulk.FillException {

        Nota_di_credito_rigaBulk rigaNC = (Nota_di_credito_rigaBulk) riga;
        fillModel(context);

        if (rigaNC.getQuantita() == null) rigaNC.setQuantita(new java.math.BigDecimal(1));
        if (rigaNC.getPrezzo_unitario() == null) rigaNC.setPrezzo_unitario(new java.math.BigDecimal(0));
        if (rigaNC.getIm_iva() == null)
            rigaNC.setIm_iva(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));

        rigaNC.setFl_iva_forzata(Boolean.FALSE);
        rigaNC.calcolaCampiDiRiga();
        java.math.BigDecimal totaleDiRiga = rigaNC.getIm_imponibile().add(rigaNC.getIm_iva());
        Fattura_passiva_rigaIBulk rigaFP = rigaNC.getRiga_fattura_origine();
        java.math.BigDecimal nuovoImportoDisponibile = rigaFP.getIm_diponibile_nc().subtract(totaleDiRiga.subtract(vecchioTotale));
        if (nuovoImportoDisponibile.signum() < 0)
            throw new it.cnr.jada.bulk.FillException("Attenzione: l'importo di storno massimo ancora disponibile è di " + rigaFP.getIm_diponibile_nc() + " EUR!");
        rigaFP.setIm_diponibile_nc(nuovoImportoDisponibile.setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        doSelectObbligazioni(context);
        doSelectAccertamenti(context);
    }

    /**
     * Richiede all'accertamento di modificare in automatico la sua scadenza (quella
     * selezionata) portando la stessa ad importo pari alla sommatoria degli importi
     * di riga dei dettagli associati. Aggiorna la mappa dei saldi per le variazioni
     * subite dall'accertamento
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    private Forward basicDoModificaScadenzaAccertamentoInAutomatico(ActionContext context) {

        try {
            CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
            it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk notaDiCredito = (it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk) bp.getModel();

            Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) bp.getAccertamentiController().getModel();

            if (scadenza == null)
                throw new it.cnr.jada.comp.ApplicationException("Selezionare l'accertamento da modificare in automatico!");
            //controllo che la scadenza appartenga all'esercizio di scrivania nella fase di cancellazione
            if (bp.isDeleting() &&
                    !bp.isViewing() &&
                    !it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).equals(scadenza.getEsercizio()))
                throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata appartiene all'esercizio " + scadenza.getEsercizio().intValue() + "! Operazione annullata.");

            AccertamentoAbstractComponentSession h = CRUDVirtualAccertamentoBP.getVirtualComponentSession(context, true);

            java.math.BigDecimal importoAttuale = notaDiCredito.getImportoTotalePerAccertamenti();
            java.math.BigDecimal importoOriginale = (java.math.BigDecimal) notaDiCredito.getFattura_passiva_ass_totaliMap().get(scadenza);
            java.math.BigDecimal delta = importoOriginale.subtract(importoAttuale);
            if (new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP).compareTo(delta) == 0)
                throw new it.cnr.jada.comp.ApplicationException("La modifica in automatico non è disponibile!");
            try {
                scadenza = (Accertamento_scadenzarioBulk) h.modificaScadenzaInAutomatico(
                        context.getUserContext(),
                        scadenza,
                        getImportoPerAggiornamentoScadenzaInAutomatico(
                                context,
                                scadenza,
                                notaDiCredito,
                                delta),
                        delta.signum() < 0);
                bp.getDefferedUpdateSaldiParentBP().getDefferedUpdateSaldiBulk().addToDefferredSaldi(
                        scadenza.getAccertamento(),
                        scadenza.getAccertamento().getSaldiInfo());
            } catch (it.cnr.jada.comp.ComponentException e) {
                if (e.getDetail() instanceof it.cnr.contab.doccont00.comp.CheckDisponibilitaCassaFailed)
                    throw new it.cnr.jada.comp.ApplicationException(e.getDetail().getMessage());
                if (e.getDetail() instanceof it.cnr.contab.doccont00.comp.SfondamentoPdGException)
                    throw new it.cnr.jada.comp.ApplicationException(e.getDetail().getMessage());
                throw e;
            }

            basicDoBringBackOpenAccertamentiWindow(context, scadenza);

            notaDiCredito.addToFattura_passiva_ass_totaliMap(scadenza, importoAttuale);

            bp.getAccertamentiController().getSelection().clear();
            bp.getAccertamentiController().setModelIndex(context, -1);
            bp.getAccertamentiController().setModelIndex(context, it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(bp.getAccertamentiController().getDetails(), scadenza));
            bp.setDirty(true);
        } catch (Exception e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }

    /**
     * Richiede all'obbligazione di modificare in automatico la sua scadenza (quella
     * selezionata) portando la stessa ad importo pari alla sommatoria degli importi
     * di riga dei dettagli associati. Aggiorna la mappa dei saldi per le variazioni
     * subite dall'obbligazione
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    private Forward basicDoModificaScadenzaObbligazioneInAutomatico(ActionContext context) {

        try {
            CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
            it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk notaDiCredito = (it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk) bp.getModel();

            Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel();

            if (scadenza == null)
                throw new it.cnr.jada.comp.ApplicationException("Selezionare l'impegno da modificare in automatico!");
            //controllo che la scadenza appartenga all'esercizio di scrivania nella fase di cancellazione
            if (bp.isDeleting() &&
                    !bp.isViewing() &&
                    !it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).equals(scadenza.getEsercizio()))
                throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata appartiene all'esercizio " + scadenza.getEsercizio().intValue() + "! Operazione annullata.");

            ObbligazioneAbstractComponentSession h = CRUDVirtualObbligazioneBP.getVirtualComponentSession(context, true);

            java.math.BigDecimal importoAttuale = notaDiCredito.getImportoTotalePerObbligazione();
            java.math.BigDecimal importoOriginale = (java.math.BigDecimal) notaDiCredito.getFattura_passiva_ass_totaliMap().get(scadenza);
            java.math.BigDecimal delta = importoOriginale.subtract(importoAttuale);
            if (new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP).compareTo(delta) == 0)
                throw new it.cnr.jada.comp.ApplicationException("La modifica in automatico non è disponibile!");
            try {
                scadenza = (Obbligazione_scadenzarioBulk) h.modificaScadenzaInAutomatico(
                        context.getUserContext(),
                        scadenza,
                        getImportoPerAggiornamentoScadenzaInAutomatico(
                                context,
                                scadenza,
                                notaDiCredito,
                                delta),
                        delta.signum() < 0);
                bp.getDefferedUpdateSaldiParentBP().getDefferedUpdateSaldiBulk().addToDefferredSaldi(
                        scadenza.getObbligazione(),
                        scadenza.getObbligazione().getSaldiInfo());
            } catch (it.cnr.jada.comp.ComponentException e) {
                if (e.getDetail() instanceof it.cnr.contab.doccont00.comp.CheckDisponibilitaCassaFailed)
                    throw new it.cnr.jada.comp.ApplicationException(e.getDetail().getMessage());
                if (e.getDetail() instanceof it.cnr.contab.doccont00.comp.SfondamentoPdGException)
                    throw new it.cnr.jada.comp.ApplicationException(e.getDetail().getMessage());
                throw e;
            }

            basicDoBringBackOpenObbligazioniWindow(context, scadenza);

            notaDiCredito.addToFattura_passiva_ass_totaliMap(scadenza, importoAttuale);

            bp.getObbligazioniController().getSelection().clear();
            bp.getObbligazioniController().setModelIndex(context, -1);
            bp.getObbligazioniController().setModelIndex(context, it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(bp.getObbligazioniController().getDetails(), scadenza));
            bp.setDirty(true);
            if (bp instanceof TitoloDiCreditoDebitoBP)
                ((TitoloDiCreditoDebitoBP) bp).addToDocumentiContabiliModificati(scadenza);
        } catch (Exception e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }

    /**
     * Richiama sulla component il metodo per stornare i dettagli selezionati e ritorna la nota di credito aggiornata
     */

    private Nota_di_creditoBulk basicDoStorna(
            ActionContext context,
            Nota_di_creditoBulk notaDiCredito,
            java.util.List dettagliDaStornare,
            java.util.Hashtable relationsHash)
            throws it.cnr.jada.comp.ComponentException {

        CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) context.getBusinessProcess();

        try {
            FatturaPassivaComponentSession h = (FatturaPassivaComponentSession) bp.createComponentSession();
            notaDiCredito = h.stornaDettagli(
                    context.getUserContext(),
                    notaDiCredito,
                    dettagliDaStornare,
                    relationsHash);
        } catch (java.rmi.RemoteException e) {
            bp.handleException(e);
        } catch (it.cnr.jada.action.BusinessProcessException e) {
            bp.handleException(e);
        }
        return notaDiCredito;
    }

    /**
     * Gestisce la richiesta di storno dei dettagli selezionati (contabilizzazione)
     * Richiesta la quadratura dei conti
     */

    private Forward basicDoStornaDettagli(
            ActionContext context,
            it.cnr.jada.util.action.Selection selection)
            throws it.cnr.jada.comp.ComponentException {

        try {
            CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
            Nota_di_creditoBulk notaDiCredito = (Nota_di_creditoBulk) bp.getModel();
            controllaQuadraturaConti(context, notaDiCredito);

            Forward forward = context.findDefaultForward();
            java.util.Vector dettagliDaStornare = (java.util.Vector) getDettagliInStato(
                    context,
                    selection.iterator(notaDiCredito.getFattura_passiva_dettColl()),
                    new String[]{Nota_di_credito_rigaBulk.STATO_CONTABILIZZATO});

            if (dettagliDaStornare != null && !dettagliDaStornare.isEmpty()) {
                if (notaDiCredito.getAccertamentiHash() != null && !notaDiCredito.getAccertamentiHash().isEmpty()) {
                    throw new it.cnr.jada.comp.ApplicationException("Non è possibile procedere all'inserimento di un impegno, perchè questa nota di credito contiene degli accertamenti!");
                }
                notaDiCredito = basicDoStorna(
                        context,
                        notaDiCredito,
                        dettagliDaStornare,
                        null);
                bp.setModel(context, notaDiCredito);
            }

            dettagliDaStornare = (java.util.Vector) getDettagliInStato(
                    context,
                    selection.iterator(notaDiCredito.getFattura_passiva_dettColl()),
                    new String[]{Nota_di_creditoBulk.STATO_PARZIALE, Nota_di_creditoBulk.STATO_PAGATO});

            if (dettagliDaStornare != null && !dettagliDaStornare.isEmpty()) {
                it.cnr.jada.util.RemoteIterator ri = ((FatturaPassivaComponentSession) bp.createComponentSession()).findObbligazioniFor(
                        context.getUserContext(),
                        notaDiCredito,
                        calcolaTotaleSelezionati(dettagliDaStornare, notaDiCredito.quadraturaInDeroga()));
                ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
                if (ri != null && ri.hasMoreElements()) {
                    if (notaDiCredito.getAccertamentiHash() != null && !notaDiCredito.getAccertamentiHash().isEmpty()) {
                        throw new it.cnr.jada.comp.ApplicationException("Non è possibile procedere all'inserimento di un impegno, perchè questa nota di credito contiene degli accertamenti!");
                    }
                    it.cnr.jada.util.action.SelezionatoreListaBP slbp = select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(Fattura_passiva_rigaIBulk.class), "default", "doSelezionaDettagli");
                    HookForward hook = (HookForward) context.findForward("seleziona");
                    hook.addParameter("dettagliDaStornare", dettagliDaStornare);
                    forward = slbp;
                } else {
				/*OptionBP optionBP = (OptionBP)openConfirm(context,"Non sono disponibili scadenze di obbligazioni di altre fatture dello stesso fornitore per i dettagli già pagati. Vuoi creare un accertamento?",OptionBP.CONFIRM_YES_NO,"doConfermaApriAccertamento");
				 * optionBP.addAttribute("dettagliDaStornare", dettagliDaStornare);
				forward = optionBP;
				 * 
				 */
                    forward = basicDoRicercaAccertamento(context, notaDiCredito, dettagliDaStornare);
                }
            }
            return forward;
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw e;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Costruiscce le relazioni (per gestire lo storno) tra la riga della nota di credito e la riga della fattura
     * passiva selezionata (potrebbe essere quella di un'altra fattura, non necessariamente quella di origine) da
     * cui ottenere l'obbligazione su cui stornare
     */

    private Forward buildRelations(
            ActionContext context,
            HookForward hook,
            java.util.List dettagliDaStornare,
            java.util.Enumeration details,
            java.util.Hashtable relationsHash) {

// DA FARE


        try {
            CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
            Nota_di_creditoBulk notaDiCredito = (Nota_di_creditoBulk) bp.getModel();

            Forward fwd = null;
            if (details.hasMoreElements()) {
                Nota_di_credito_rigaBulk rigaNdC = (Nota_di_credito_rigaBulk) details.nextElement();
                if (relationsHash == null)
                    relationsHash = new java.util.Hashtable();
                Fattura_passiva_rigaIBulk selectedRigaFattura = null;
                if (selectedRigaFattura != null) {
                    relationsHash.put(rigaNdC, selectedRigaFattura);

                } else {
                    dettagliDaStornare.remove(rigaNdC);
                }

                fwd = buildRelations(
                        context,
                        hook,
                        dettagliDaStornare,
                        details,
                        relationsHash);
            } else {

                notaDiCredito = basicDoStorna(
                        context,
                        notaDiCredito,
                        dettagliDaStornare,
                        relationsHash);
                bp.setModel(context, notaDiCredito);
                fwd = context.findDefaultForward();
            }
            return fwd;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doAddToCRUDMain_Accertamenti_DettaglioAccertamenti(ActionContext context) {

        try {
            CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
            bp.getDettaglio().getSelection().clearSelection();
            fillModel(context);

            java.util.Vector selectedModels = (java.util.Vector) getDettagliInStato(context,
                    bp.getDettaglio().getDetails().iterator(),
                    new String[]{Nota_di_credito_rigaBulk.STATO_INIZIALE});
            if (selectedModels.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException(
                        "Tutti i dettagli sono già stati stornati!");
            it.cnr.jada.util.action.SelezionatoreListaBP slbp =
                    select(context,
                            new it.cnr.jada.util.ListRemoteIterator(selectedModels),
                            it.cnr.jada.bulk.BulkInfo.getBulkInfo(Nota_di_credito_rigaBulk.class),
                            "righeNdCSet",
                            "doBringBackAddToCRUDMain_Accertamenti_DettaglioAccertamenti");
            slbp.setMultiSelection(true);
            return slbp;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doAddToCRUDMain_Dettaglio(ActionContext context) {

        try {
            CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
            bp.getDettaglio().getSelection().clearSelection();
            fillModel(context);
            it.cnr.jada.util.RemoteIterator ri = bp.search(context, null, bp.getModel());
            final SelezionatoreListaBP nbp = select(context, ri, BulkInfo.getBulkInfo(Fattura_passiva_IBulk.class), "default", "doSelezionaRighe");
            nbp.setFormField(new FormField(nbp, bp.getBulkInfo().getFieldProperty("nr_fattura_fornitore"), new Fattura_passiva_IBulk()));
            return nbp;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doAddToCRUDMain_Obbligazioni_DettaglioObbligazioni(ActionContext context) {

        try {
            CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
            bp.getDettaglio().getSelection().clearSelection();
            fillModel(context);

            java.util.Vector selectedModels = new java.util.Vector();
            for (java.util.Enumeration e = bp.getDettaglio().getElements(); e.hasMoreElements(); ) {
                Nota_di_credito_rigaBulk riga = (Nota_di_credito_rigaBulk) e.nextElement();
                if (Fattura_passiva_rigaBulk.STATO_INIZIALE.equals(riga.getStato_cofi()))
                    selectedModels.add(riga);
            }
            if (selectedModels.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Tutti i dettagli sono già stati stornati!");
            it.cnr.jada.util.action.SelezionatoreListaBP slbp = select(
                    context,
                    new it.cnr.jada.util.ListRemoteIterator(selectedModels),
                    it.cnr.jada.bulk.BulkInfo.getBulkInfo(Nota_di_credito_rigaBulk.class),
                    "righeNdCSet",
                    "doBringBackAddToCRUDMain_Obbligazioni_DettaglioObbligazioni");
            slbp.setMultiSelection(true);
            return slbp;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }
/**
 * Non usato nelle ndc. Preparato nel caso in cui dovesse servire
 */
 
/*public Forward doAssociaInventario(ActionContext context) {
		CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP)getBusinessProcess(context);
		Fattura_passivaBulk fattura = (Fattura_passivaBulk)bp.getModel();
		controllaQuadraturaConti(context,fattura);
		
		for (java.util.Iterator i = fattura.getFattura_passiva_dettColl().iterator(); i.hasNext();) {
			Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk)i.next();
			AssociazioniInventarioTable associazioni = fattura.getAssociazioniInventarioHash();	
			if ((associazioni != null && !associazioni.isEmpty())|| (riga.getCrudStatus()!=OggettoBulk.TO_BE_CREATED)) {
				Ass_inv_bene_fatturaBulk ass = fattura.getAssociationWithInventarioFor(riga);
				if ((ass != null) && !ass.isPerAumentoValore()) {
					if (riga.isInventariato()) riga.setInventariato(false);
				}
				else if (riga.getCrudStatus()!=OggettoBulk.TO_BE_CREATED && (riga.getBene_servizio().getFl_gestione_inventario().booleanValue())){
					riga.setInventariato(false);					
				}
			}			
		}//ricerca
		java.util.List dettagliDaInventariare = getDettagliDaInventariare(context, bp.getDettaglio().getDetails().iterator());
		if (dettagliDaInventariare != null && !dettagliDaInventariare.isEmpty()) {
			AssBeneFatturaBP ibp = (AssBeneFatturaBP)context.getUserInfo().createBusinessProcess(context,"AssBeneFatturaBP",new Object[] { "MRSWTh" });
			Ass_inv_bene_fatturaBulk associa = new Ass_inv_bene_fatturaBulk();
			associa.setLocal_transactionID(ibp.getLocalTransactionID(context.getUserContext(),true));
			associa.completeFrom(dettagliDaInventariare);
			associa.setInventario(((BuonoCaricoScaricoComponentSession)EJBCommonServices.createEJB(
									"CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",
									BuonoCaricoScaricoComponentSession.class)).caricaInventario(context.getUserContext()));
				
			ibp.setModel(context,associa);			
			ibp.setDirty(false);		
			context.addHookForward("bringback",this,"doBringBackAssociaInventario");
			HookForward hook = (HookForward)context.findForward("bringback");
			hook.addParameter("dettagliDaInventariare", dettagliDaInventariare);
			return context.addBusinessProcess(ibp);
		}else
		{  //R.P. MODIFICA BENI già ASSOCIATI(IN SOSPESO)
			if (fattura.getCrudStatus()!=OggettoBulk.TO_BE_CREATED){
				   List dettagli = bp.getDettaglio().getDetails();
				   for(Iterator i=dettagli.iterator();i.hasNext();){
					   Fattura_passiva_rigaIBulk riga=(Fattura_passiva_rigaIBulk)i.next();
					   if (riga.getBene_servizio().getFl_gestione_inventario().booleanValue())
						   dettagliDaInventariare.add(riga);
				   }
				if (dettagliDaInventariare.size()==0){
					bp.setMessage("Nessun dettaglio è inventariabile o tutti i dettagli inventariabili sono già stati caricati!");
					return context.findDefaultForward();
				}
					
				AssBeneFatturaBP ibp = (AssBeneFatturaBP)context.getUserInfo().createBusinessProcess(context,"AssBeneFatturaBP",new Object[] { "MRSWTh" });
				Ass_inv_bene_fatturaBulk associa = new Ass_inv_bene_fatturaBulk();
				associa.setLocal_transactionID(ibp.getLocalTransactionID(context.getUserContext(),true));
				associa.completeFrom(dettagliDaInventariare);
				associa.setInventario(((BuonoCaricoScaricoComponentSession)EJBCommonServices.createEJB(
						"CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",
						BuonoCaricoScaricoComponentSession.class)).caricaInventario(context.getUserContext()));
			
				ibp.setModel(context,associa);			
				ibp.setDirty(false);		
					
				context.addHookForward("bringback",this,"doBringBackAssociaInventario");
				HookForward hook = (HookForward)context.findForward("bringback");
				hook.addParameter("dettagliDaInventariare", dettagliDaInventariare);
				return context.addBusinessProcess(ibp);
			}
			else{
				bp.setMessage("Nessun dettaglio è inventariabile o tutti i dettagli inventariabili sono già stati caricati!");
				return context.findDefaultForward();
			}
		}
}*/

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doBringBackAddToCRUDMain_Accertamenti_DettaglioAccertamenti(ActionContext context) {

        try {
            HookForward fwd = (HookForward) context.getCaller();
            Forward forward = context.findDefaultForward();
            it.cnr.jada.util.action.Selection selection =
                    (it.cnr.jada.util.action.Selection) fwd.getParameter("selection");
            if (selection != null && !selection.isEmpty()) {
                CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
                java.util.List dett = ((Nota_di_creditoBulk) bp.getModel()).getFattura_passiva_dettColl();
                Selection newSelection = getIndexSelectionOn(selection, dett, "stornato");
                forward = basicDoStornaDettagli(context, newSelection);

                if (!(forward instanceof it.cnr.jada.util.action.SelezionatoreListaBP)) {
                    bp.getDettaglio().reset(context);
                    bp.getAccertamentiController().setModelIndex(context, -1);

                    bp.setDirty(true);
                }

                //			doCalcolaTotalePerObbligazione(context, (Obbligazione_scadenzarioBulk)bp.getObbligazioniController().getModel());
            }
            return forward;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doBringBackAddToCRUDMain_Obbligazioni_DettaglioObbligazioni(ActionContext context) {

        try {
            HookForward fwd = (HookForward) context.getCaller();
            Forward forward = context.findDefaultForward();
            it.cnr.jada.util.action.Selection selection =
                    (it.cnr.jada.util.action.Selection) fwd.getParameter("selection");

            if (selection != null && !selection.isEmpty()) {
                CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
                java.util.List dett = ((Nota_di_creditoBulk) bp.getModel()).getFattura_passiva_dettColl();
                Selection newSelection = getIndexSelectionOn(selection, dett, "stornato");
                forward = basicDoStornaDettagli(context, newSelection);

                if (!(forward instanceof it.cnr.jada.util.action.SelezionatoreListaBP)) {
                    bp.getDettaglio().reset(context);
                    bp.getObbligazioniController().setModelIndex(context, -1);

                    bp.setDirty(true);
                }

                //			doCalcolaTotalePerObbligazione(context, (Obbligazione_scadenzarioBulk)bp.getObbligazioniController().getModel());
            }
            return forward;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doBringBackConfirmDeleteRow(ActionContext context) {

        HookForward caller = (HookForward) context.getCaller();

        CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
        try {
            if (caller.getParameter("undoBringBack") != null)
                throw new it.cnr.jada.comp.ApplicationException("Cancellazione annullata!");
            Risultato_eliminazioneVBulk re = (Risultato_eliminazioneVBulk) caller.getParameter("bringback");
            if (!re.getDocumentiAmministrativiScollegati().isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Eseguire il controllo di quadratura per tutti i dettagli in elenco!");

            if (bp.isCarryingThrough()) {
                try {
                    bp.riportaAvanti(context);
                } catch (Throwable t) {
                    bp.rollbackToSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
                    bp.setModel(context, (Nota_di_creditoBulk) caller.getParameter("originalClone"));
                    throw t;
                }
            }
            bp.commitUserTransaction();
            bp.setModel(context, bp.initializeModelForEdit(context, bp.getModel()));
            bp.setStatus(bp.EDIT);
            bp.setCarryingThrough(false);
            bp.setDirty(false);
            return context.findDefaultForward();
        } catch (Throwable e) {
            try {
                bp.rollbackUserTransaction();
                bp.edit(context, bp.getModel());
            } catch (it.cnr.jada.action.BusinessProcessException ex) {
                return handleException(context, ex);
            }
            return handleException(context, e);
        }
    }

    /**
     * Richiede l'associazione dell'accertamento selezionata dall'utente ai dettagli
     * di ndc selezionati per la contabilizzazione ('basicDoBringBackOpenAccertamentiWindow')
     * Ricalcola i totali di scadenza
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doBringBackOpenAccertamentiWindow(ActionContext context) {

        HookForward caller = (HookForward) context.getCaller();

        Forward fwd = context.findDefaultForward();
        CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
        Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) caller.getParameter("accertamentoSelezionato");
        if (scadenza == null)
            scadenza = (Accertamento_scadenzarioBulk) caller.getParameter("bringback");
        if (scadenza != null) {
            fwd = basicDoBringBackOpenAccertamentiWindow(context, scadenza);

            bp.getDettaglio().reset(context);
            bp.getAccertamentiController().setModelIndex(context, -1);

            doCalcolaTotalePerAccertamento(context, (Accertamento_scadenzarioBulk) bp.getAccertamentiController().getModel());

            bp.setDirty(true);
        }
        if (!"tabFatturaPassivaAccertamenti".equals(bp.getTab("tab")))
            bp.setTab("tab", "tabFatturaPassivaAccertamenti");

        return fwd;
    }

    /**
     * Ricalcola il valore totale degli importi associati alla scadenza
     */

    public Forward doCalcolaTotalePerAccertamento(ActionContext context, Accertamento_scadenzarioBulk accertamento) {

        it.cnr.jada.util.action.FormBP bulkBP = (it.cnr.jada.util.action.FormBP) context.getBusinessProcess();
        if (bulkBP instanceof CRUDNotaDiCreditoBP) {
            CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) bulkBP;
            Nota_di_creditoBulk ndC = (Nota_di_creditoBulk) bp.getModel();
            if (ndC.getAccertamentiHash() != null && accertamento != null)
                try {
                    ndC.setImportoTotalePerAccertamenti(
                            calcolaTotaleSelezionati(
                                    (java.util.List) ndC.getAccertamentiHash().get(accertamento),
                                    (ndC.quadraturaInDeroga() || (ndC.getFl_split_payment() != null && ndC.getFl_split_payment()))));

                } catch (it.cnr.jada.comp.ApplicationException e) {
                    ndC.setImportoTotalePerAccertamenti(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                }
            else
                ndC.setImportoTotalePerAccertamenti(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));

        }
        return context.findDefaultForward();
    }

    /**
     * Gestisce una richiesta di ricerca.
     * <p>
     * L'implementazione di default utilizza il metodo astratto <code>read</code>
     * di <code>CRUDBusinessProcess</code>.
     * Se la ricerca fornisce più di un risultato viene creato un
     * nuovo <code>SelezionatoreListaBP</code> per la selezione di un elemento.
     * Al business process viene anche chiesto l'elenco delle colonne da
     * visualizzare.
     */
    public Forward doCerca(ActionContext context) throws java.rmi.RemoteException, InstantiationException, javax.ejb.RemoveException {

        try {
            CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
            if (bp.getParent() != null && bp.getParent() instanceof CRUDFatturaPassivaIBP) {
                fillModel(context);
                Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk) ((CRUDFatturaPassivaIBP) bp.getParent()).getModel();
                FatturaPassivaComponentSession h = (FatturaPassivaComponentSession) bp.createComponentSession();
                it.cnr.jada.util.RemoteIterator ri = h.findNotaDiCreditoFor(context.getUserContext(), fp);
                ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
                if (ri == null || ri.countElements() == 0) {
                    it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                    bp.setMessage("La ricerca non ha fornito alcun risultato.");
                    return context.findDefaultForward();
                } else if (ri.countElements() == 1) {
                    OggettoBulk bulk = (OggettoBulk) ri.nextElement();
                    bp.setMessage(FormBP.INFO_MESSAGE, "La ricerca ha fornito un solo risultato.");
                    bp.edit(context, bulk);
                    return context.findDefaultForward();
                } else {
                    bp.setModel(context, bp.getModel());
                    SelezionatoreListaBP nbp = (SelezionatoreListaBP) context.createBusinessProcess("Selezionatore");
                    nbp.setIterator(context, ri);
                    nbp.setBulkInfo(bp.getBulkInfo());
                    nbp.setColumns(bp.getSearchResultColumns());
                    context.addHookForward("seleziona", this, "doRiportaSelezione");
                    return context.addBusinessProcess(nbp);
                }
            }
            return super.doCerca(context);

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Chiede conferma all'utente per proseguire nell'operazione di contabilizzazione su un accertamento
     * nel caso in cui non esistano scadenze di obbligazioni valide.
     *
     * @param context Il contesto della action
     * @return Il default forward.
     */
    public Forward doConfermaApriAccertamento(ActionContext context, OptionBP optionBP) {

        CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
        if (optionBP.getOption() == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
            try {
                it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable obbligazioni = ((Nota_di_creditoBulk) bp.getModel()).getFattura_passiva_obbligazioniHash();
                if (obbligazioni != null && !obbligazioni.isEmpty()) {
                    throw new it.cnr.jada.comp.ApplicationException("Non è possibile procedere all'inserimento di un accertamento, perchè questa nota di credito contiene delle obbligazioni!");
                }
                java.util.List dettagliDaStornare = (java.util.List) optionBP.getAttribute("dettagliDaStornare");

                it.cnr.contab.doccont00.bp.CRUDAccertamentoBP accertamentoBP = (it.cnr.contab.doccont00.bp.CRUDAccertamentoBP) context.getUserInfo().createBusinessProcess(context, "CRUDAccertamentoBP", new Object[]{"MRSWTh"});
                accertamentoBP.reset(context);
                AccertamentoOrdBulk accertamento = (AccertamentoOrdBulk) accertamentoBP.getModel();
                accertamento.completeFrom(context, (Nota_di_creditoBulk) bp.getModel(), dettagliDaStornare);
                FatturaPassivaComponentSession fpcs = (FatturaPassivaComponentSession) bp.createComponentSession();
                it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk uoEnte = fpcs.findUOEnte(context.getUserContext(), it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
                accertamento.setCd_cds(uoEnte.getCd_unita_padre());
                accertamento.setCd_unita_organizzativa(uoEnte.getCd_unita_organizzativa());
                //accertamentoBP.getScadenzario().setModelIndex(0);

                context.addHookForward("bringback", this, "doBringBackOpenAccertamentiWindow");
                HookForward hook = (HookForward) context.findForward("bringback");
                hook.addParameter("dettagliDaStornare", dettagliDaStornare);
                return context.addBusinessProcess(accertamentoBP);
            } catch (it.cnr.jada.comp.ApplicationException e) {
                return handleException(context, e);
            } catch (Exception e) {
                return handleException(context, e);
            }
        }

        bp.getDettaglio().reset(context);
        bp.getAccertamentiController().setModelIndex(context, -1);
        bp.setDirty(true);
        if (!"tabFatturaPassivaAccertamenti".equals(bp.getTab("tab")))
            bp.setTab("tab", "tabFatturaPassivaAccertamenti");

        return context.findDefaultForward();
    }

    /**
     * Richiama il metodo di gestione della chiusura del pannello ndc
     */
    public Forward doConfirmCloseForm(ActionContext context, int option) throws it.cnr.jada.action.BusinessProcessException {

        if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
            Forward defaultForward = super.doConfirmCloseForm(context, option);
            Forward forward = context.findForward("chiusuraNotaDiCredito");
            if (forward == null)
                return defaultForward;
            return forward;
        }
        return super.doConfirmCloseForm(context, option);
    }

    /**
     * Forza il flag 'ForzaIVA' e ricalcola i totali di riga
     * Reimplementato
     */
    public Forward doForzaIVA(ActionContext context) {

        Forward fwd = super.doForzaIVA(context);
        doSelectAccertamenti(context);
        return fwd;
    }

    /**
     * Non usato nelle ndc. Preparato nel caso in cui dovesse servire
     */

    public Forward doInventariaDettagli(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            fillModel(context);

            controllaQuadraturaConti(context, (Fattura_passivaBulk) bp.getModel());

            java.util.List dettagliDaInventariare = getDettagliDaInventariare(context, bp.getDettaglio().getDetails().iterator());
            if (dettagliDaInventariare != null && !dettagliDaInventariare.isEmpty()) {
                CRUDScaricoInventarioBP ibp = (CRUDScaricoInventarioBP) context.getUserInfo().createBusinessProcess(context, "CRUDScaricoInventarioBP", new Object[]{"MRSWTh"});
                ibp.setBy_fattura(true);
                Buono_carico_scaricoBulk bcs = new Buono_carico_scaricoBulk();
                bcs.setByFattura(Boolean.TRUE);
                bcs.setTi_documento(Buono_carico_scaricoBulk.SCARICO);
                bcs.initializeForInsert(ibp, context);
                bcs = (Buono_carico_scaricoBulk) ibp.createComponentSession().inizializzaBulkPerInserimento(context.getUserContext(), bcs);
                bcs.completeFrom(dettagliDaInventariare);
                try {

                    bcs.setPg_buono_c_s(((NumerazioneTempBuonoComponentSession) EJBCommonServices.createEJB(
                            "CNRINVENTARIO01_EJB_NumerazioneTempBuonoComponentSession",
                            NumerazioneTempBuonoComponentSession.class)).getNextTempPG(context.getUserContext(), bcs));

                } catch (Throwable e) {
                    throw new ComponentException(e);
                }
                ibp.setModel(context, bcs);
                ibp.setStatus(ibp.INSERT);
                ibp.setDirty(false);
                ibp.resetChildren(context);
                context.addHookForward("bringback", this, "doBringBackInventariaDettagli");
                HookForward hook = (HookForward) context.findForward("bringback");
                hook.addParameter("dettagliDaInventariare", dettagliDaInventariare);
                return context.addBusinessProcess(ibp);
            }
            bp.setMessage("Nessun dettaglio è inventariabile o tutti i dettagli inventariabili sono già stati caricati!");
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Richiede al documento contabile di aggiornare l'importo della scadenza selezionata in automatico
     * Reimplementato per gestire anche le scadenze di accertamenti
     *
     * @param context L'ActionContext della richiesta
     * @param prefix
     * @return Il Forward alla pagina di risposta
     */

    public Forward doModificaScadenzaInAutomatico(ActionContext context, String prefix) {

        try {
            CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
            fillModel(context);
            it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk notaDiCredito = (it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk) bp.getModel();

            controllaQuadraturaConti(context, notaDiCredito);

            if ("main.Obbligazioni".equals(prefix))
                return basicDoModificaScadenzaObbligazioneInAutomatico(context);
            else
                return basicDoModificaScadenzaAccertamentoInAutomatico(context);
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il cambiamento delle modalità di pagamento della UO per collegamenti ad accertamenti
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOnModalitaPagamentoUOChange(ActionContext context) {

        try {
            fillModel(context);
            CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
            Nota_di_creditoBulk notaDiCredito = (Nota_di_creditoBulk) bp.getModel();
            if (notaDiCredito.getModalita_pagamento_uo() != null) {
                FatturaPassivaComponentSession fpcs = (FatturaPassivaComponentSession) bp.createComponentSession();
                if (notaDiCredito.getEnte() == null)
                    notaDiCredito.setEnte(fpcs.findTerzoUO(context.getUserContext(), notaDiCredito.getEsercizio()));
                java.util.List coll = (java.util.List) fpcs.findListabancheuo(context.getUserContext(), notaDiCredito);
//			notaDiCredito.setBanca_uo((coll == null || coll.isEmpty()) ? null : (BancaBulk)new java.util.Vector(coll).firstElement());
                bp.setContoEnte(false);
                if (coll == null || coll.isEmpty())
                    notaDiCredito.setBanca_uo(null);
                else if (coll.size() == 1)
                    notaDiCredito.setBanca_uo((BancaBulk) new java.util.Vector(coll).firstElement());
                else {
                    if (!Rif_modalita_pagamentoBulk.BANCARIO.equals(notaDiCredito.getModalita_pagamento_uo().getTi_pagamento()))
                        notaDiCredito.setBanca_uo((BancaBulk) new java.util.Vector(coll).firstElement());
                    else {
                        notaDiCredito = fpcs.setContoEnteIn(context.getUserContext(), notaDiCredito, coll);
                        bp.setContoEnte(true);
                    }
                }
            } else {
                notaDiCredito.setBanca_uo(null);
            }
            bp.setModel(context, notaDiCredito);
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * richiede l'apertura del pannello dell'accertamento per la modifica della
     * scadenza selezionata
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOpenAccertamentiWindow(ActionContext context) {

        try {
            CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
            fillModel(context);

            Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) bp.getAccertamentiController().getModel();
            boolean viewMode = bp.isViewing();
            if (scadenza == null)
                throw new it.cnr.jada.comp.ApplicationException("Selezionare l'accertamento da " + (viewMode ? "visualizzare" : "modificare") + " in manuale!");
            //controllo che la scadenza appartenga all'esercizio di scrivania nella fase di cancellazione
            if (bp.isDeleting() &&
                    !bp.isViewing() &&
                    !it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).equals(scadenza.getEsercizio()))
                throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata appartiene all'esercizio " + scadenza.getEsercizio().intValue() + "! Operazione annullata.");

            controllaQuadraturaConti(context, (it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk) bp.getModel());

            if (!viewMode && bp instanceof IDocumentoAmministrativoBP)
                viewMode = !((IDocumentoAmministrativoBP) bp).getDocumentoAmministrativoCorrente().isEditable();
            String status = viewMode ? "V" : "M";
            it.cnr.contab.doccont00.bp.CRUDVirtualAccertamentoBP abp = it.cnr.contab.doccont00.bp.CRUDVirtualAccertamentoBP.getBusinessProcessFor(context, scadenza.getAccertamento(), status + "RSWTh");
            abp.edit(context, scadenza.getAccertamento(), true);
            abp.selezionaScadenza(scadenza, context);

            context.addHookForward("bringback", this, "doBringBackOpenAccertamentiWindow");
            HookForward hook = (HookForward) context.findForward("bringback");
            return context.addBusinessProcess(abp);
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * richiede l'apertura del pannello dell'obbligazione per la modifica della
     * scadenza selezionata
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOpenObbligazioniWindow(ActionContext context) {

        try {
            CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
            fillModel(context);

            Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel();
            boolean viewMode = bp.isViewing();
            if (scadenza == null)
                throw new it.cnr.jada.comp.ApplicationException("Selezionare l'impegno da " + (viewMode ? "visualizzare" : "modificare") + " in manuale!");
            //controllo che la scadenza appartenga all'esercizio di scrivania nella fase di cancellazione
            if (bp.isDeleting() &&
                    !bp.isViewing() &&
                    !it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).equals(scadenza.getEsercizio()))
                throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata appartiene all'esercizio " + scadenza.getEsercizio().intValue() + "! Operazione annullata.");

            controllaQuadraturaConti(context, (it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk) bp.getModel());

            if (!viewMode && bp instanceof IDocumentoAmministrativoBP)
                viewMode = !((IDocumentoAmministrativoBP) bp).getDocumentoAmministrativoCorrente().isEditable();
            String status = viewMode ? "V" : "M";
            it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP nbp = it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP.getBusinessProcessFor(context, scadenza.getObbligazione(), status + "RSWTh");
            nbp.edit(context, scadenza.getObbligazione(), true);
            nbp.selezionaScadenza(scadenza, context);

            context.addHookForward("bringback", this, "doBringBackOpenObbligazioniWindow");
            HookForward hook = (HookForward) context.findForward("bringback");
            return context.addBusinessProcess(nbp);
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Risponde all'evento di fine cancellazione dei documentio amministrativi di tipo
     * passivo. Reimplementato
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doPostConfirmDelete(
            ActionContext context,
            Risultato_eliminazioneVBulk re)
            throws BusinessProcessException {

        it.cnr.jada.util.action.CRUDBP bp = getBusinessProcess(context);
        Fattura_passivaBulk deletedDoc = (Fattura_passivaBulk) bp.getModel();
        doConfirmCloseForm(context, it.cnr.jada.util.action.OptionBP.YES_BUTTON);
        bp = getBusinessProcess(context);
//	Fattura_passivaBulk fp = (Fattura_passivaBulk)bp.getModel();
        String msg = "Cancellazione effettuata!";
        if (deletedDoc.isVoidable())
            msg = "Annullamento effettuato!";
        if (!re.getDocumentiContabiliScollegati().isEmpty())
            msg = msg + " Alcuni documenti contabili creati contestualmente al documento amministrativo sono rimasti inalterati.";
        bp.setMessage(msg);
        return doChiusuraNotaDiCredito(context);
    }

    /**
     * Risponde all'evento di fine salvataggio dei documenti amministrativi di tipo
     * ndc.
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    protected Forward doPostSaveEvent(
            ActionContext context,
            CRUDNotaDiCreditoBP bp,
            Nota_di_creditoBulk originalClone)
            throws it.cnr.jada.action.BusinessProcessException, it.cnr.jada.bulk.ValidationException {

        Nota_di_creditoBulk ndc = (Nota_di_creditoBulk) bp.getModel();

        RisultatoEliminazioneBP rebp = (RisultatoEliminazioneBP) context.createBusinessProcess("RisultatoEliminazioneBP", new String[]{"MRSWTh"});
        Risultato_eliminazioneVBulk deleteManager = bp.getDeleteManager();

        if (ndc.getDettagliCancellati() != null)
            for (java.util.Iterator i = ndc.getDettagliCancellati().iterator(); i.hasNext(); ) {
                IDocumentoAmministrativoRigaBulk riga = (IDocumentoAmministrativoRigaBulk) i.next();
                deleteManager.add(riga);
                java.math.BigDecimal totRiga = riga.getIm_imponibile().add(riga.getIm_iva());
                IDocumentoAmministrativoRigaBulk originalDetail = riga.getOriginalDetail();
                if (originalDetail != null) {
                    java.math.BigDecimal impDisponibile = originalDetail.getIm_diponibile_nc();
                    originalDetail.setIm_diponibile_nc(impDisponibile.add(riga.getFather().getImportoSignForDelete(totRiga)));
                    try {
                        ((DocumentoAmministrativoComponentSession) bp.createComponentSession()).update(
                                context.getUserContext(),
                                originalDetail);
                    } catch (Throwable e) {
                        return handleException(context, e);
                    }
                }
            }

        if (deleteManager != null &&
                (!deleteManager.getDocumentiAmministrativiScollegati().isEmpty() || !deleteManager.getDocumentiContabiliScollegati().isEmpty())) {
            rebp.initializeControllers(context, ndc);
            rebp.edit(context, deleteManager);

            context.addHookForward("bringback", this, "doBringBackConfirmDeleteRow");
            HookForward hook = (HookForward) context.findForward("bringback");
            hook.addParameter("originalClone", originalClone);
            return context.addBusinessProcess(rebp);
        }

        if (bp.isCarryingThrough()) {
            try {
                bp.riportaAvanti(context);
            } catch (Throwable t) {
                bp.rollbackToSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
                bp.setModel(context, originalClone);
                return handleException(context, t);
            }
        }
        bp.commitUserTransaction();
        bp.setModel(context, bp.initializeModelForEdit(context, bp.getModel()));
        bp.setStatus(bp.EDIT);
        bp.setCarryingThrough(false);
        bp.setDirty(false);
        return context.findDefaultForward();
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di cancellazione dal controller "accertamenti"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doRemoveFromCRUDMain_Accertamenti(ActionContext context) {

        CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) context.getBusinessProcess();
        it.cnr.jada.util.action.Selection selection = bp.getAccertamentiController().getSelection();
        try {
            if (selection.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Selezionare le scadenze che si desidera eliminare!");
        } catch (it.cnr.jada.comp.ApplicationException e) {
            return handleException(context, e);
        }
        java.util.List accertamenti = bp.getAccertamentiController().getDetails();
        for (it.cnr.jada.util.action.SelectionIterator i = selection.iterator(); i.hasNext(); ) {
            Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) accertamenti.get(i.nextIndex());
            Nota_di_creditoBulk ndC = (Nota_di_creditoBulk) bp.getModel();
            java.util.Vector models = (java.util.Vector) ndC.getAccertamentiHash().get(scadenza);
            try {
                if (models != null && models.isEmpty()) {
                    ndC.getAccertamenti_scadenzarioHash().remove(scadenza);
                    ndC.addToDocumentiContabiliCancellati(scadenza);
                } else {
                    for (java.util.Iterator it = models.iterator(); it.hasNext(); ) {
                        Fattura_passiva_rigaBulk fpr = (Fattura_passiva_rigaBulk) it.next();
                        if (fpr.getTi_associato_manrev() != null && Fattura_passiva_rigaBulk.ASSOCIATO_A_MANDATO.equalsIgnoreCase(fpr.getTi_associato_manrev()))
                            throw new it.cnr.jada.comp.ApplicationException("Impossibile scollegare l'accertamento \"" +
                                    scadenza.getEsercizio_originale().intValue()
                                    + "/" + scadenza.getPg_accertamento().longValue() +
                                    "\" perchè il dettaglio collegato \"" +
                                    ((fpr.getDs_riga_fattura() != null) ?
                                            fpr.getDs_riga_fattura() :
                                            String.valueOf(fpr.getProgressivo_riga().longValue())) +
                                    "\" è associato a mandato.");
                    }
                    scollegaDettagliDaAccertamento(context, (java.util.List) models.clone());
                }
            } catch (it.cnr.jada.comp.ComponentException e) {
                return handleException(context, e);
            }

            doCalcolaTotalePerAccertamento(context, null);

            setAndVerifyStatusFor(context, ndC);

            bp.getAccertamentiController().getSelection().clear();
            bp.getAccertamentiController().setModelIndex(context, -1);
            bp.setDirty(true);
        }
        return context.findDefaultForward();
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di cancellazione dal controller "accertamenti_DettaglioAccertamenti"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doRemoveFromCRUDMain_Accertamenti_DettaglioAccertamenti(ActionContext context) {

        CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) context.getBusinessProcess();
        try {
            it.cnr.jada.util.action.Selection selection = bp.getDettaglioAccertamentoController().getSelection();
            if (selection.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Selezionare i dettagli che si desidera scollegare!");
            java.util.List models = selection.select(bp.getDettaglioAccertamentoController().getDetails());
            for (java.util.Iterator i = models.iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaBulk fpr = (Fattura_passiva_rigaBulk) i.next();
                if (fpr.getTi_associato_manrev() != null && Fattura_passiva_rigaBulk.ASSOCIATO_A_MANDATO.equalsIgnoreCase(fpr.getTi_associato_manrev()))
                    throw new it.cnr.jada.comp.ApplicationException("Impossibile scollegare il dettaglio \"" +
                            ((fpr.getDs_riga_fattura() != null) ?
                                    fpr.getDs_riga_fattura() :
                                    String.valueOf(fpr.getProgressivo_riga().longValue())) +
                            "\" perchè associato a mandato.");
            }
            scollegaDettagliDaAccertamento(context, models);
        } catch (it.cnr.jada.comp.ComponentException e) {
            return handleException(context, e);
        }

        doCalcolaTotalePerAccertamento(context, (Accertamento_scadenzarioBulk) bp.getAccertamentiController().getModel());

        Nota_di_creditoBulk ndC = (Nota_di_creditoBulk) bp.getModel();

        setAndVerifyStatusFor(context, ndC);

        bp.getDettaglioAccertamentoController().getSelection().clear();
        bp.getDettaglioAccertamentoController().setModelIndex(context, -1);
        java.util.List dettagli = bp.getDettaglioAccertamentoController().getDetails();
        if (dettagli == null || dettagli.isEmpty()) {
            bp.getAccertamentiController().getSelection().clear();
            bp.getAccertamentiController().setModelIndex(context, -1);
        }
        bp.setDirty(true);

        return context.findDefaultForward();
    }

    /**
     * Gestisce il comando di eliminazione di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doRemoveFromCRUDMain_Dettaglio(ActionContext context) {
        try {
            fillModel(context);
            CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) context.getBusinessProcess();
            java.util.List dettagli = bp.getDettaglio().getDetails();
            for (it.cnr.jada.util.action.SelectionIterator i = bp.getDettaglio().getSelection().iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk) dettagli.get(i.nextIndex());
                if (bp.isDocumentoContabileModificato(dettaglio.getObbligazione_scadenziario()))
                    return handleException(
                            context,
                            new it.cnr.jada.comp.ApplicationException("La scadenza associata a \"" + dettaglio.getDs_riga_fattura() + "\" è stata già modificata. Impossibile cancellare."));
            }
            return super.doRemoveFromCRUDMain_Dettaglio(context);
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di cancellazione dal controller "obbligazioni"
     * Reimplementato
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doRemoveFromCRUDMain_Obbligazioni(ActionContext context) {

        CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) context.getBusinessProcess();
        it.cnr.jada.util.action.Selection selection = bp.getObbligazioniController().getSelection();
        try {
            if (selection.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Selezionare le scadenze che si desidera eliminare!");
        } catch (it.cnr.jada.comp.ApplicationException e) {
            return handleException(context, e);
        }
        java.util.List obbligazioni = bp.getObbligazioniController().getDetails();
        for (it.cnr.jada.util.action.SelectionIterator i = selection.iterator(); i.hasNext(); ) {
            Obbligazione_scadenzarioBulk obbligazione = (Obbligazione_scadenzarioBulk) obbligazioni.get(i.nextIndex());
            if (bp.isDocumentoContabileModificato(obbligazione))
                return handleException(
                        context,
                        new it.cnr.jada.comp.ApplicationException("La scadenza \"" + obbligazione.getDs_scadenza() + "\" è stata già modificata. Impossibile cancellarla."));
            Nota_di_creditoBulk ndC = (Nota_di_creditoBulk) bp.getModel();
            java.util.Vector models = (java.util.Vector) ndC.getFattura_passiva_obbligazioniHash().get(obbligazione);
            try {
                if (models != null && models.isEmpty()) {
                    ndC.getFattura_passiva_obbligazioniHash().remove(obbligazione);
                    ndC.addToDocumentiContabiliCancellati(obbligazione);
                } else {
                    for (java.util.Iterator it = models.iterator(); it.hasNext(); ) {
                        Fattura_passiva_rigaBulk fpr = (Fattura_passiva_rigaBulk) it.next();
                        if (fpr.getTi_associato_manrev() != null && Fattura_passiva_rigaBulk.ASSOCIATO_A_MANDATO.equalsIgnoreCase(fpr.getTi_associato_manrev()))
                            throw new it.cnr.jada.comp.ApplicationException("Impossibile scollegare l'impegno \"" +
                                    +obbligazione.getEsercizio_originale().intValue()
                                    + "/" + obbligazione.getPg_obbligazione().longValue() +
                                    "\" perchè il dettaglio collegato \"" +
                                    ((fpr.getDs_riga_fattura() != null) ?
                                            fpr.getDs_riga_fattura() :
                                            String.valueOf(fpr.getProgressivo_riga().longValue())) +
                                    "\" è associato a mandato.");
                    }
                    scollegaDettagliDaObbligazione(context, (java.util.List) models.clone());
                }
            } catch (it.cnr.jada.comp.ComponentException e) {
                return handleException(context, e);
            }

            setAndVerifyStatusFor(context, ndC);

            bp.getObbligazioniController().getSelection().clear();
            bp.getObbligazioniController().setModelIndex(context, -1);

            doCalcolaTotalePerObbligazione(context, null);

            bp.setDirty(true);
        }
        return context.findDefaultForward();
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di cancellazione dal controller "obbligazioni_DettaglioObbligazioni"
     * Reimplementato
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doRemoveFromCRUDMain_Obbligazioni_DettaglioObbligazioni(ActionContext context) {

        CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) context.getBusinessProcess();
        try {
            it.cnr.jada.util.action.Selection selection = bp.getDettaglioObbligazioneController().getSelection();
            if (selection.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Selezionare i dettagli che si desidera scollegare!");
            java.util.List models = selection.select(bp.getDettaglioObbligazioneController().getDetails());
            if (models != null)
                for (java.util.Iterator i = models.iterator(); i.hasNext(); ) {
                    Nota_di_credito_rigaBulk dettaglio = (Nota_di_credito_rigaBulk) i.next();
                    Obbligazione_scadenzarioBulk scadenzaSelezionata = dettaglio.getObbligazione_scadenziario();
                    if (bp.isDocumentoContabileModificato(scadenzaSelezionata))
                        throw new it.cnr.jada.comp.ApplicationException("La scadenza \"" + scadenzaSelezionata.getDs_scadenza() + "\" è stata già modificata. Impossibile cancellarla.");
                    if (dettaglio.getTi_associato_manrev() != null && Fattura_passiva_rigaBulk.ASSOCIATO_A_MANDATO.equalsIgnoreCase(dettaglio.getTi_associato_manrev()))
                        throw new it.cnr.jada.comp.ApplicationException("Impossibile scollegare il dettaglio \"" +
                                ((dettaglio.getDs_riga_fattura() != null) ?
                                        dettaglio.getDs_riga_fattura() :
                                        String.valueOf(dettaglio.getProgressivo_riga().longValue())) +
                                "\" perchè associato a mandato.");
                }
            scollegaDettagliDaObbligazione(context, models);
        } catch (it.cnr.jada.comp.ComponentException e) {
            return handleException(context, e);
        }

        doCalcolaTotalePerObbligazione(context, (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel());

        Nota_di_creditoBulk ndC = (Nota_di_creditoBulk) bp.getModel();

        setAndVerifyStatusFor(context, ndC);

        bp.getDettaglioObbligazioneController().getSelection().clear();
        bp.getDettaglioObbligazioneController().setModelIndex(context, -1);
        java.util.List dettagli = bp.getDettaglioObbligazioneController().getDetails();
        if (dettagli == null || dettagli.isEmpty()) {
            bp.getObbligazioniController().getSelection().clear();
            bp.getObbligazioniController().setModelIndex(context, -1);
        }
        bp.setDirty(true);

        return context.findDefaultForward();
    }

    public Forward doRiportaAvanti(ActionContext context) throws java.rmi.RemoteException {

        CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
        Nota_di_creditoBulk originalClone = (Nota_di_creditoBulk) bp.getModel();
        try {
            fillModel(context);
            bp.setSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
            bp.salvaRiportandoAvanti(context);

            Nota_di_creditoBulk ndc = (Nota_di_creditoBulk) bp.getModel();
            if (bp.isEditing() &&
                    ((ndc.getDettagliCancellati() != null && !ndc.getDettagliCancellati().isEmpty()) ||
                            (ndc.getDocumentiContabiliCancellati() != null && !ndc.getDocumentiContabiliCancellati().isEmpty())))
                return doPostSaveEvent(context, bp, originalClone);

            bp.riportaAvanti(context);
            bp.commitUserTransaction();
            bp.setModel(context, bp.initializeModelForEdit(context, bp.getModel()));
            bp.setStatus(bp.EDIT);
            bp.setCarryingThrough(false);

            bp.getObbligazioniController().setModelIndex(context, -1);
            bp.getAccertamentiController().setModelIndex(context, -1);

            bp.setDirty(false);
            return context.findDefaultForward();
        } catch (it.cnr.jada.bulk.ValidationException e) {
            getBusinessProcess(context).setErrorMessage(e.getMessage());
            return context.findDefaultForward();
        } catch (Throwable e) {
            try {
                bp.rollbackToSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
                bp.setModel(context, originalClone);
            } catch (BusinessProcessException ex) {
                return handleException(context, ex);
            }
            return handleException(context, e);
        }
    }

    /**
     * Gestisce una richiesta di salvataggio. Reimplementato
     */
    public Forward doSalva(ActionContext context) throws java.rmi.RemoteException {

        CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
        Nota_di_creditoBulk originalClone = (Nota_di_creditoBulk) bp.getModel();
        try {
            fillModel(context);

            bp.setSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
            bp.save(context);

            Nota_di_creditoBulk ndc = (Nota_di_creditoBulk) bp.getModel();
            if (bp.isEditing() &&
                    ((ndc.getDettagliCancellati() != null && !ndc.getDettagliCancellati().isEmpty()) ||
                            (ndc.getDocumentiContabiliCancellati() != null && !ndc.getDocumentiContabiliCancellati().isEmpty())))
                return doPostSaveEvent(context, bp, originalClone);

            bp.commitUserTransaction();
            bp.setCarryingThrough(false);
            bp.setModel(context, bp.initializeModelForEdit(context, bp.getModel()));
            bp.setStatus(bp.EDIT);

            bp.getObbligazioniController().setModelIndex(context, -1);
            bp.getAccertamentiController().setModelIndex(context, -1);

            bp.setDirty(false);
            return context.findDefaultForward();
        } catch (it.cnr.jada.bulk.ValidationException e) {
            getBusinessProcess(context).setErrorMessage(e.getMessage());
            return context.findDefaultForward();
        } catch (Throwable e) {
            try {
                bp.rollbackToSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
                bp.setModel(context, originalClone);
            } catch (BusinessProcessException ex) {
                return handleException(context, e);
            }
            return handleException(context, e);
        }
    }

    /**
     * Ricerca le banche valide per la UO (nel caso di collegamentio ad accertamenti)
     */
    public Forward doSearchListabancheuo(ActionContext context) {

        Nota_di_creditoBulk ndc = (Nota_di_creditoBulk) getBusinessProcess(context).getModel();
        return search(context, getFormField(context, "main.listabancheuo"), ndc.getModalita_pagamento_uo().getTiPagamentoColumnSet());
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di selezione dal controller "accertamenti"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doSelectAccertamenti(ActionContext context) {

        CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) context.getBusinessProcess();
        try {
            bp.getAccertamentiController().setSelection(context);
        } catch (Throwable e) {
            return handleException(context, e);
        }

        doCalcolaTotalePerAccertamento(context, (Accertamento_scadenzarioBulk) bp.getAccertamentiController().getModel());
        return context.findDefaultForward();
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di storno dei dettagli selezionati scelti da una fattura passiva divera da quella di origine
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doSelezionaDettagli(ActionContext context) {

        CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) context.getBusinessProcess();
        HookForward caller = (HookForward) context.getCaller();
        Fattura_passiva_rigaIBulk riga = (Fattura_passiva_rigaIBulk) caller.getParameter("focusedElement");
        if (riga != null) {
            java.util.Hashtable relationsHash = new java.util.Hashtable();
            java.util.List dettagliDaStornare = (java.util.List) caller.getParameter("dettagliDaStornare");
            if (dettagliDaStornare != null) {
                for (java.util.Iterator i = dettagliDaStornare.iterator(); i.hasNext(); )
                    relationsHash.put(i.next(), riga);
                if (!relationsHash.isEmpty()) {
                    Nota_di_creditoBulk notaDiCredito = (Nota_di_creditoBulk) bp.getModel();
                    try {
                        notaDiCredito = basicDoStorna(
                                context,
                                notaDiCredito,
                                dettagliDaStornare,
                                relationsHash);
                        bp.setModel(context, notaDiCredito);
                    } catch (it.cnr.jada.comp.ComponentException e) {
                        return handleException(context, e);
                    } catch (it.cnr.jada.action.BusinessProcessException e) {
                        return handleException(context, e);
                    }
                }

                bp.getDettaglio().reset(context);
                bp.getObbligazioniController().setModelIndex(context, -1);

                bp.setDirty(true);
            }
        }
        if (!"tabFatturaPassivaObbligazioni".equals(bp.getTab("tab")))
            bp.setTab("tab", "tabFatturaPassivaObbligazioni");
        return context.findDefaultForward();
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doSelezionaDettaglioPerNdC(ActionContext context) {

        try {
            it.cnr.jada.action.HookForward caller = (it.cnr.jada.action.HookForward) context.getCaller();
            java.util.List selectedElements = (java.util.List) caller.getParameter("selectedElements");
            if (selectedElements != null && !selectedElements.isEmpty()) {
                CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) context.getBusinessProcess();
                java.util.Vector elementsToBeAdded = new java.util.Vector();
                java.util.Vector elementsToDischarged = new java.util.Vector();
                for (java.util.Iterator els = selectedElements.iterator(); els.hasNext(); ) {
                    Fattura_passiva_rigaIBulk dettaglio = (Fattura_passiva_rigaIBulk) els.next();
                    try {
                        if (((FatturaPassivaComponentSession) bp.createComponentSession()).isBeneServizioPerSconto(
                                context.getUserContext(),
                                dettaglio))
                            throw new it.cnr.jada.bulk.ValidationException();
                        for (java.util.Iterator i = bp.getDettaglio().getDetails().iterator(); i.hasNext(); ) {
                            if (((Nota_di_credito_rigaBulk) i.next()).getRiga_fattura_origine().equalsByPrimaryKey(dettaglio))
                                throw new it.cnr.jada.bulk.ValidationException();
                        }
                        elementsToBeAdded.add(dettaglio);
                    } catch (it.cnr.jada.bulk.ValidationException e) {
                        if (!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(elementsToDischarged, dettaglio))
                            elementsToDischarged.add(dettaglio);
                    }
                }

                //Test per gestire il collegamento NotaFattura Split
                List<Fattura_passiva_rigaBulk> allDetail = new ArrayList<Fattura_passiva_rigaBulk>();
                allDetail.addAll(elementsToBeAdded);
                allDetail.addAll(((Nota_di_creditoBulk) bp.getModel()).getFattura_passiva_dettColl());

                boolean isOriginalNotaSplit = Optional.ofNullable(bp)
                        .flatMap(crudNotaDiCreditoBP -> Optional.ofNullable(crudNotaDiCreditoBP.getModel()))
                        .filter(Nota_di_creditoBulk.class::isInstance)
                        .map(Nota_di_creditoBulk.class::cast)
                        .map(nota_di_creditoBulk -> Optional.ofNullable(nota_di_creditoBulk.getFl_split_payment()).orElse(Boolean.FALSE) ||
                                Optional.ofNullable(nota_di_creditoBulk.getDocumentoEleTestata()).map(DocumentoEleTestataBulk::isAttivoSplitPayment).orElse(false))
                        .orElse(Boolean.FALSE);

                long contaSplit = allDetail.stream()
                        .filter(e -> {
                            return Optional.ofNullable(e.getFattura_passiva().getFl_split_payment())
                                    .map(i -> i.booleanValue())
                                    .orElse(Boolean.FALSE);
                        })
                        .count();
                long contaNoSplit = allDetail.size() - contaSplit;

                if (contaSplit > 0 && contaNoSplit > 0) {
                    bp.setMessage("Attenzione! Non è possibile associare alla Nota Credito dettagli di fattura Split Payment e dettagli di fattura non Split Payment.");
                    return context.findDefaultForward();
                } else if (contaSplit > 0) {
                    if (((Nota_di_creditoBulk) bp.getModel()).getFl_split_payment() != null && !((Nota_di_creditoBulk) bp.getModel()).getFl_split_payment()) {
                        //se la nota originariamente era split la rimetto tale
                        if (isOriginalNotaSplit) {
                            ((Nota_di_creditoBulk) bp.getModel()).setFl_split_payment(Boolean.TRUE);
                            basicDoOnIstituzionaleCommercialeChange(context, ((Fattura_passivaBulk) bp.getModel()));
                        } else {
                            bp.setMessage("Attenzione! La Nota Credito non è di tipo Split Payment. Non è possibile associare Fatture di tipo Split Payment.");
                            return context.findDefaultForward();
                        }
                    }
                } else if (contaNoSplit > 0) {
                    if (((Nota_di_creditoBulk) bp.getModel()).getFl_split_payment() == null || ((Nota_di_creditoBulk) bp.getModel()).getFl_split_payment()) {
                        ((Nota_di_creditoBulk) bp.getModel()).setFl_split_payment(Boolean.FALSE);
                        basicDoOnIstituzionaleCommercialeChange(context, ((Fattura_passivaBulk) bp.getModel()));
                    }
                }

                for (java.util.Iterator i = elementsToBeAdded.iterator(); i.hasNext(); ) {
                    Fattura_passiva_rigaIBulk dettaglio = (Fattura_passiva_rigaIBulk) i.next();

                    if (dettaglio.getIm_diponibile_nc() == null ||
                            dettaglio.getIm_diponibile_nc().compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)) != 0) {
                        Nota_di_credito_rigaBulk rigaNdC = new Nota_di_credito_rigaBulk();
                        Nota_di_creditoBulk notaDiCredito = (Nota_di_creditoBulk) bp.getModel();
                        notaDiCredito.addToFattura_passiva_dettColl(rigaNdC);
                        rigaNdC.setUser(context.getUserInfo().getUserid());
                        rigaNdC.copyFrom(dettaglio);
                        //rigaNdC.setModalita(((FatturaPassivaComponentSession)bp.createComponentSession()).findModalita(context.getUserContext(),rigaNdC));
                        notaDiCredito.setIvaRecuperabile(rigaNdC.getRiga_fattura_origine() != null && rigaNdC.getRiga_fattura_origine().getFattura_passiva().getDt_fattura_fornitore() == null ||
                                (rigaNdC.getTi_istituz_commerc().compareTo(TipoIVA.ISTITUZIONALE.value()) == 0) ||
                                (rigaNdC.getRiga_fattura_origine() != null && rigaNdC.getRiga_fattura_origine().getFattura_passiva().getDt_fattura_fornitore() != null &&
                                        it.cnr.jada.util.DateUtils.daysBetweenDates(new Date(rigaNdC.getRiga_fattura_origine().getFattura_passiva().getDt_fattura_fornitore().getTime()), new Date(rigaNdC.getFattura_passiva().getDt_fattura_fornitore().getTime())) < 366));
                    } else {
                        elementsToDischarged.add(dettaglio);
                    }
                }
                if (elementsToDischarged != null && !elementsToDischarged.isEmpty()) {
                    String msg = null;
                    Fattura_passiva_rigaIBulk dettaglio = null;
                    if (elementsToDischarged.size() == 1) {
                        dettaglio = (Fattura_passiva_rigaIBulk) elementsToDischarged.firstElement();
                        msg = "Il dettaglio per \"" + dettaglio.getBene_servizio().getDs_bene_servizio() + "\" è già stato inserito\no la disponibilità per le note di credito è 0\no il bene servizio inserito è un bene di tipo sconto/abbuono!";
                    } else {
                        msg = "I dettagli per ";
                        for (java.util.Iterator i = elementsToDischarged.iterator(); i.hasNext(); ) {
                            dettaglio = (Fattura_passiva_rigaIBulk) i.next();
                            msg += "\"" + dettaglio.getBene_servizio().getDs_bene_servizio() + "\"";
                            if (!(elementsToDischarged.indexOf(dettaglio) == elementsToDischarged.size() - 1))
                                msg += ", ";
                        }
                        msg += " sono già stati inseriti\no la disponibilità per le note di credito è 0\no il bene servizio inserito è un bene di tipo sconto/abbuono!";
                    }
                    bp.setMessage(msg);
                }
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di selezione dal controller "righe" preparando il selezionatore (caso di ricerca di dettagli
     * di fatture diverse e compatibili da quella di origine)
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doSelezionaRighe(ActionContext context) {

        try {
            it.cnr.jada.action.HookForward caller = (it.cnr.jada.action.HookForward) context.getCaller();
            Fattura_passiva_IBulk fatturaPassiva = (Fattura_passiva_IBulk) caller.getParameter("focusedElement");
            if (fatturaPassiva != null) {
                it.cnr.jada.util.RemoteIterator ri = ((FatturaPassivaComponentSession) ((CRUDNotaDiCreditoBP) context.getBusinessProcess()).createComponentSession()).cercaDettagliFatturaPerNdC(context.getUserContext(), fatturaPassiva);
                SelezionatoreListaBP sbp = select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(Fattura_passiva_rigaIBulk.class), "righiSet", "doSelezionaDettaglioPerNdC");
                sbp.setMultiSelection(true);
                return sbp;
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }

    /**
     * Storna i dettagli selezionati previo controllo della selezione
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doStornaDettagli(ActionContext context) {

        try {
            CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
            fillModel(context);
            it.cnr.jada.util.action.Selection models = bp.getDettaglio().getSelection(context);
            Forward forward = context.findDefaultForward();
            if (models == null || models.isEmpty())
                bp.setErrorMessage("Per procedere, selezionare i dettagli da stornare!");
            else {
                Nota_di_creditoBulk ndc = (Nota_di_creditoBulk) bp.getModel();
                controllaSelezionePerContabilizzazione(context, models.iterator(ndc.getFattura_passiva_dettColl()));
                //controllaSelezionePerTitoloCapitolo(context, models.iterator(ndc.getFattura_passiva_dettColl()));
                List titoloCapitoloValidolist = controllaSelezionePerTitoloCapitoloLista(context, models.iterator(ndc.getFattura_passiva_dettColl()));

                forward = basicDoStornaDettagli(context, models);
                ndc = (Nota_di_creditoBulk) bp.getModel();

                bp.getDettaglio().reset(context);
                bp.getObbligazioniController().setModelIndex(context, -1);

                if (ndc.getObbligazioniHash() != null && !ndc.getObbligazioniHash().isEmpty() &&
                        !"tabFatturaPassivaObbligazioni".equals(bp.getTab("tab"))) {
                    bp.getAccertamentiController().setModelIndex(context, -1);
                    bp.setTab("tab", "tabFatturaPassivaObbligazioni");
                } else if (!"tabFatturaPassivaAccertamenti".equals(bp.getTab("tab"))) {
                    bp.setTab("tab", "tabFatturaPassivaAccertamenti");
                }

                bp.setDirty(true);
            }
            return forward;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Restituisce l'importo che deve assumere la scadenza dell'accertamento nel caso di modifica automatica
     *
     * @param context        L'ActionContext della richiesta
     * @param scadenza
     * @param fatturaPassiva
     * @param delta
     * @return
     */
    protected java.math.BigDecimal getImportoPerAggiornamentoScadenzaInAutomatico(
            ActionContext context,
            Accertamento_scadenzarioBulk scadenza,
            Fattura_passivaBulk fatturaPassiva,
            java.math.BigDecimal delta) {

        return scadenza.getIm_scadenza().subtract(delta);
    }

    /**
     * Restituisce l'importo che deve assumere la scadenza dell'obbligazione nel caso di modifica automatica
     *
     * @param context        L'ActionContext della richiesta
     * @param scadenza
     * @param fatturaPassiva
     * @param delta
     * @return
     */
    protected java.math.BigDecimal getImportoPerAggiornamentoScadenzaInAutomatico(
            ActionContext context,
            Obbligazione_scadenzarioBulk scadenza,
            Fattura_passivaBulk fatturaPassiva,
            java.math.BigDecimal delta) {

        return scadenza.getIm_scadenza().add(delta);
    }

    /**
     * Risincronizza la collezione degli accertamenti (richiamato dopo la modifica di
     * una scadenza associata al doc amm).
     * Se questa collezione contiene in chiave la oldScadenza (scadenza vecchia), essa
     * viene eliminata e i dettagli ad essa associati vengono ricontabilizzati sulla
     * newScadenza (scadenza modificata dall'utente); se non ha ancora dettagli associati
     * viene semplicemente eliminata
     * ('scollegaDettagliDaAccertamento')
     */
    private void resyncAccertamentoScadenzario(
            ActionContext context,
            Accertamento_scadenzarioBulk oldScadenza,
            Accertamento_scadenzarioBulk newScadenza)
            throws it.cnr.jada.comp.ComponentException {

        CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP) getBusinessProcess(context);
        Nota_di_creditoBulk notaDiCredito = (Nota_di_creditoBulk) bp.getModel();
        java.util.Vector clone = new java.util.Vector();
        if (oldScadenza == null) {
            java.util.Vector models = (java.util.Vector) ((HookForward) context.getCaller()).getParameter("dettagliDaStornare");
            clone = (java.util.Vector) models.clone();
        } else {
            java.util.Vector models = ((java.util.Vector) notaDiCredito.getAccertamenti_scadenzarioHash().get(oldScadenza));
            clone = (java.util.Vector) models.clone();
            if (!clone.isEmpty())
                scollegaDettagliDaAccertamento(context, clone);
            else
                notaDiCredito.getAccertamenti_scadenzarioHash().remove(oldScadenza);
            oldScadenza.setIm_associato_doc_amm(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
            newScadenza.setIm_associato_doc_amm(newScadenza.getIm_scadenza());
        }

        java.util.Hashtable relationsHash = new java.util.Hashtable();
        for (java.util.Iterator i = clone.iterator(); i.hasNext(); )
            relationsHash.put(i.next(), newScadenza);

        notaDiCredito = basicDoStorna(
                context,
                notaDiCredito,
                clone,
                relationsHash);

        try {
            notaDiCredito.setCd_cds(newScadenza.getAccertamento().getCd_cds());
            notaDiCredito.setCd_unita_organizzativa(newScadenza.getAccertamento().getCd_unita_organizzativa());
            if (notaDiCredito.getEnte() == null) {
                FatturaPassivaComponentSession h = (FatturaPassivaComponentSession) bp.createComponentSession();
                notaDiCredito = h.completaEnte(context.getUserContext(), notaDiCredito);
            }
            bp.setModel(context, notaDiCredito);
        } catch (java.rmi.RemoteException e) {
            throw new it.cnr.jada.comp.ComponentException(e);
        } catch (it.cnr.jada.action.BusinessProcessException e) {
            throw new it.cnr.jada.comp.ComponentException(e);
        }

        bp.getAccertamentiController().setModelIndex(context, -1);
        bp.setDirty(true);

        if (!"tabFatturaPassivaAccertamenti".equals(bp.getTab("tab")))
            bp.setTab("tab", "tabFatturaPassivaAccertamenti");
    }

    /**
     * Risincronizza la collezione degli accertamenti (richiamato dopo la modifica di
     * una scadenza associata al doc amm).
     * Se questa collezione contiene in chiave la oldAcc (scadenza vecchia), essa
     * viene eliminata e i dettagli ad essa associati vengono ricontabilizzati sulla
     * newAcc (scadenza modificata dall'utente); se non ha ancora dettagli associati
     * viene semplicemente eliminata
     */

    private void scollegaDettagliDaAccertamento(ActionContext context, java.util.List models)
            throws it.cnr.jada.comp.ComponentException {

        if (models != null) {
            for (java.util.Iterator i = models.iterator(); i.hasNext(); ) {
                Nota_di_credito_rigaBulk dettaglio = (Nota_di_credito_rigaBulk) i.next();
                try {
                    if (!Fattura_passiva_rigaBulk.STATO_CONTABILIZZATO.equals(dettaglio.getStato_cofi()))
                        throw new it.cnr.jada.comp.ApplicationException("Non è possibile scollegare il dettaglio \"" + dettaglio.getDs_riga_fattura() + "\". Questa operazione è permessa solo per dettagli in stato \"" + Nota_di_credito_rigaBulk.STATO.get(Fattura_passiva_rigaBulk.STATO_CONTABILIZZATO) + "\".");
                } catch (it.cnr.jada.comp.ApplicationException e) {
                    try {
                        CRUDVirtualAccertamentoBP.rollbackToSafePoint(context);
                    } catch (Throwable t) {
                        throw new it.cnr.jada.comp.ComponentException(t);
                    }
                    throw e;
                }
                ((Nota_di_creditoBulk) dettaglio.getFattura_passiva()).removeFromAccertamenti_scadenzarioHash(dettaglio);
                dettaglio.setStato_cofi(Fattura_passiva_rigaBulk.STATO_INIZIALE);
                dettaglio.setAccertamento_scadenzario(null);
                dettaglio.setToBeUpdated();
            }
        }
    }

    /**
     * Risincronizza la collezione delle obbligazioni (richiamato dopo la modifica di
     * una scadenza associata al doc amm).
     * Se questa collezione contiene in chiave la oldObblig (scadenza vecchia), essa
     * viene eliminata e i dettagli ad essa associati vengono ricontabilizzati sulla
     * newObblig (scadenza modificata dall'utente); se non ha ancora dettagli associati
     * viene semplicemente eliminata
     */

    private void scollegaDettagliDaObbligazione(ActionContext context, java.util.List models)
            throws it.cnr.jada.comp.ComponentException {

        if (models != null) {
            for (java.util.Iterator i = models.iterator(); i.hasNext(); ) {
                Nota_di_credito_rigaBulk dettaglio = (Nota_di_credito_rigaBulk) i.next();
                try {
                    if (!Fattura_passiva_rigaBulk.STATO_CONTABILIZZATO.equals(dettaglio.getStato_cofi()))
                        throw new it.cnr.jada.comp.ApplicationException("Non è possibile scollegare il dettaglio \"" + dettaglio.getDs_riga_fattura() + "\". Questa operazione è permessa solo per dettagli in stato \"" + Nota_di_credito_rigaBulk.STATO.get(Fattura_passiva_rigaBulk.STATO_CONTABILIZZATO) + "\".");
                } catch (it.cnr.jada.comp.ApplicationException e) {
                    try {
                        CRUDVirtualObbligazioneBP.rollbackToSafePoint(context);
                    } catch (Throwable t) {
                        throw new it.cnr.jada.comp.ComponentException(t);
                    }
                    throw e;
                }

                dettaglio.getFattura_passiva().removeFromFattura_passiva_obbligazioniHash(dettaglio);
                dettaglio.setObbligazione_scadenziario(null);

                dettaglio.setStato_cofi(Fattura_passiva_rigaBulk.STATO_INIZIALE);

                dettaglio.setRiga_fattura_associata(null);
                dettaglio.setToBeUpdated();

            }
        }
    }

    private Forward basicDoRicercaAccertamento(
            ActionContext context,
            Nota_di_creditoBulk nc,
            java.util.List models) {

        try {

            //controlla che gli importi dei dettagli siano diversi da 0
            Nota_di_credito_rigaBulk riga = null;
            if (models != null)
                for (java.util.Iterator i =
                     models.iterator();
                     i.hasNext();
                ) {
                    riga = (Nota_di_credito_rigaBulk) i.next();
                    if (riga.getIm_totale_divisa().compareTo(new java.math.BigDecimal(0)) == 0)
                        throw new it.cnr.jada.comp.ApplicationException("Non è possibile contabilizzare poiché un dettaglio\nselezionato ha un importo pari a 0");
                }

            //imposta i valori per la pagina di filtro sull'accertamento
            Filtro_ricerca_accertamentiVBulk filtro =
                    new Filtro_ricerca_accertamentiVBulk();
            filtro.setData_scadenziario(nc.getDt_scadenza());
            filtro.setCliente(nc.getFornitore());
            filtro.setIm_importo(calcolaTotaleSelezionati(models, (nc.quadraturaInDeroga() || (nc.getFl_split_payment() != null && nc.getFl_split_payment()))));
            //filtro.setCd_unita_organizzativa(nc.getCd_unita_organizzativa());
            filtro.setCd_uo_origine(nc.getCd_uo_origine());
            filtro.setHasDocumentoCompetenzaCOGEInAnnoPrecedente(nc.hasCompetenzaCOGEInAnnoPrecedente());
            filtro.setHasDocumentoCompetenzaCOGESoloInAnnoCorrente(
                    !nc.hasCompetenzaCOGEInAnnoPrecedente() &&
                            Fattura_passivaBulk.getDateCalendar(nc.getDt_a_competenza_coge()).get(java.util.Calendar.YEAR) == nc.getEsercizio().intValue());
            if (models == null || models.isEmpty())
                filtro.setFl_importo(Boolean.FALSE);
            if (filtro.getData_scadenziario() == null)
                filtro.setFl_data_scadenziario(Boolean.FALSE);

            //richiama il filtro
            BulkBP bp =
                    (BulkBP) context.getUserInfo().createBusinessProcess(
                            context,
                            "RicercaAccertamentiBP");
            bp.setModel(context, filtro);
            //imposto il bringback
            context.addHookForward("bringback", this, "doBringBackOpenAccertamentiWindow");
            HookForward hook = (HookForward) context.findForward("bringback");
            hook.addParameter("dettagliDaStornare", models);
            return context.addBusinessProcess(bp);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }
}
