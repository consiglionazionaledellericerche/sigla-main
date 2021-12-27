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

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.docamm00.bp.*;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.intrastat.bulk.Fattura_attiva_intraBulk;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.doccont00.bp.CRUDVirtualAccertamentoBP;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession;
import it.cnr.contab.inventario00.bp.AssBeneFatturaBP;
import it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario00.docs.bulk.V_ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario01.bp.CRUDScaricoInventarioBP;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession;
import it.cnr.contab.inventario01.ejb.NumerazioneTempBuonoComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

import javax.ejb.EJBException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.*;

public class CRUDFatturaAttivaAction extends EconomicaAction {
    public CRUDFatturaAttivaAction() {
        super();
    }

    /**
     * Aggiunge sul modello corrente del target i dettagli selzionati sul documento amministrativo di origine
     * Ogni dettaglio deve essere in stato iniziale.
     */
    private java.util.Vector basicAddDetailsTo(ActionContext context, CRUDNotaDiCreditoAttivaBP target) {

        //aggiunge dettagli alla nota di credito

        Nota_di_credito_attivaBulk notaDiCredito = (Nota_di_credito_attivaBulk) target.getModel();
        it.cnr.jada.bulk.BulkList dettagliNdC = notaDiCredito.getFattura_attiva_dettColl();
        if (dettagliNdC == null) {
            dettagliNdC = new it.cnr.jada.bulk.BulkList();
            notaDiCredito.setFattura_attiva_dettColl(dettagliNdC);
        }
        java.util.Vector addedElements = new java.util.Vector();
        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) context.getBusinessProcess();
        //controlla se uno o più dettagli non sono stati aggiunti perchè la nota di credito è già stata pagata o stampata su registro IVA
        if (!notaDiCredito.isEditable() || target.isViewing()) {
            if (!bp.getDettaglio().getSelection().isEmpty())
                target.setErrorMessage("Uno o più dettagli non sono stati aggiunti perchè la nota di credito è già stata pagata o annullata o riportata!");
        } else {
            for (java.util.Iterator i = bp.getDettaglio().getSelection().iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaIBulk dettaglio = (Fattura_attiva_rigaIBulk) bp.getDettaglio().getDetails().get(((Integer) i.next()).intValue());
                //controlla se uno o più dettagli non sono stati aggiunti per mancanza di disponibiltà
                if (!dettaglio.STATO_INIZIALE.equals(dettaglio.getStato_cofi())) {
                    Nota_di_credito_attiva_rigaBulk dettaglioNdC = new Nota_di_credito_attiva_rigaBulk();
                    dettaglioNdC.setNotaDiCredito(notaDiCredito);
                    try {
                        dettaglioNdC.copyFrom(dettaglio);
                        if (!isRigaContainedInDetails(dettaglioNdC, dettagliNdC)) {
                            dettaglioNdC.setUser(context.getUserInfo().getUserid());
                            dettagliNdC.add(dettaglioNdC);
                            addedElements.add(dettaglioNdC);
                        }
                    } catch (it.cnr.jada.bulk.FillException e) {
                        target.setErrorMessage("Uno o più dettagli non sono stati aggiunti per mancanza di disponibiltà!");
                    }
                }
            }
        }

        if (!addedElements.isEmpty()) {
            notaDiCredito.setIm_totale_fattura(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        }
        return addedElements;
    }

    /**
     * Aggiunge sul modello corrente del target i dettagli selzionati sul documento amministrativo di origine
     * Ogni dettaglio deve essere in stato iniziale.
     */
    private java.util.Vector basicAddDetailsTo(ActionContext context, CRUDNotaDiDebitoAttivaBP target) {

        //aggiunge dettagli alla nota di debito
        Nota_di_debito_attivaBulk notaDiDebito = (Nota_di_debito_attivaBulk) target.getModel();
        it.cnr.jada.bulk.BulkList dettagliNdD = notaDiDebito.getFattura_attiva_dettColl();
        if (dettagliNdD == null) {
            dettagliNdD = new it.cnr.jada.bulk.BulkList();
            notaDiDebito.setFattura_attiva_dettColl(dettagliNdD);
        }
        java.util.Vector addedElements = new java.util.Vector();
        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) context.getBusinessProcess();
        if (!notaDiDebito.isEditable() || target.isViewing()) {
            if (!bp.getDettaglio().getSelection().isEmpty())
                target.setErrorMessage("Uno o più dettagli non sono stati aggiunti perchè la nota di debito è già stata pagata o annullata o riportata!");
        } else {
            for (java.util.Iterator i = bp.getDettaglio().getSelection().iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaIBulk dettaglio = (Fattura_attiva_rigaIBulk) bp.getDettaglio().getDetails().get(((Integer) i.next()).intValue());

                if (!dettaglio.STATO_INIZIALE.equals(dettaglio.getStato_cofi())) {
                    Nota_di_debito_attiva_rigaBulk dettaglioNdD = new Nota_di_debito_attiva_rigaBulk();
                    dettaglioNdD.setNotaDiDebito(notaDiDebito);
                    try {
                        dettaglioNdD.copyFrom(dettaglio);
                        if (!isRigaContainedInDetails(dettaglioNdD, dettagliNdD)) {
                            dettaglioNdD.setUser(context.getUserInfo().getUserid());
                            dettagliNdD.add(dettaglioNdD);
                            addedElements.add(dettaglioNdD);
                        }
                    } catch (it.cnr.jada.bulk.FillException e) {
                        target.setErrorMessage("Uno o più dettagli non sono stati aggiunti per mancanza di disponibiltà!");
                    }
                }
            }
        }

        if (!addedElements.isEmpty()) {
            notaDiDebito.setIm_totale_fattura(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        }
        return addedElements;
    }

    /**
     * Calcola l'importo disponibile per la nota di credito
     *
     * @param context       L'ActionContext della richiesta
     * @param riga
     * @param vecchioTotale
     * @throws FillException
     */
    protected void basicCalcolaImportoDisponibileNC(
            //note di credito
            ActionContext context,
            Fattura_attiva_rigaBulk riga,
            java.math.BigDecimal vecchioTotale)
            throws it.cnr.jada.bulk.FillException {

        Fattura_attiva_rigaIBulk rigaFP = (Fattura_attiva_rigaIBulk) riga;
        if (rigaFP.getQuantita() == null) rigaFP.setQuantita(new java.math.BigDecimal(1));
        if (rigaFP.getPrezzo_unitario() == null) rigaFP.setPrezzo_unitario(new java.math.BigDecimal(0));
        if (rigaFP.getIm_iva() == null)
            rigaFP.setIm_iva(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));

        java.math.BigDecimal nuovoTotale = rigaFP.getIm_imponibile().add(rigaFP.getIm_iva());
        java.util.HashMap stroni = rigaFP.getFattura_attivaI().getStorniHashMap();
        if (stroni == null ||
                stroni.get(rigaFP) == null ||
                ((java.util.List) stroni.get(rigaFP)).isEmpty())
            rigaFP.setIm_diponibile_nc(nuovoTotale);
        else {
            java.math.BigDecimal impStorni = vecchioTotale.subtract(rigaFP.getIm_diponibile_nc());
            java.math.BigDecimal impDisponibile = nuovoTotale.subtract(impStorni);
            if (impDisponibile.signum() < 0)
                throw new it.cnr.jada.bulk.FillException("Attenzione: l'importo totale di riga (" + nuovoTotale + ") è inferiore all'importo totale di " + impStorni + " degli storni ad essa associati!");
            rigaFP.setIm_diponibile_nc(impDisponibile);
        }
    }

    /**
     * richiama il metodo del bulk che calcola i totali di riga
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward basicCalcolaTotaliDiRiga(ActionContext context) {
        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
        Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) bp.getDettaglio().getModel();
        java.math.BigDecimal qta = riga.getQuantita();
        java.math.BigDecimal pu = riga.getPrezzo_unitario();
        try {

            riga.calcolaCampiDiRiga();
        } catch (Throwable e) {
            riga.setQuantita(qta);
            riga.setPrezzo_unitario(pu);
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }

    /**
     * Apre il pannello delle note di credito sul modello passato 'notaDiCebito' e nel
     * caso in cui alcune righe sono selezionate in fattura, cerca di aggiungerle come
     * dettagli della nota di credito stessa ('basicAddDetailsTo').
     */

    private Forward basicDoApriNotaDiCredito(
            ActionContext context,
            Nota_di_credito_attivaBulk notaDiCredito)
            throws it.cnr.jada.comp.ApplicationException {
//note di credito
        try {
            CRUDFatturaAttivaIBP bp = (CRUDFatturaAttivaIBP) context.getBusinessProcess();
            if (bp.isDirty() && bp.getDettaglio().getSelection().size() != 0)
                throw new it.cnr.jada.comp.ApplicationException("Il documento risulta modificato! Per continuare o salvare o deselezionare i dettagli da aggiungere alla nota di credito e ricercarli successivamente.");

            //Assolutamente necessario eseguirlo DOPO i controlli
            bp.rollbackAndCloseUserTransaction();

            String status = bp.isEditing() ? "M" : "V";
            CRUDNotaDiCreditoAttivaBP notaBp = (CRUDNotaDiCreditoAttivaBP) context.createBusinessProcess("CRUDNotaDiCreditoAttivaBP", new Object[]{status + "Tn"});
            FatturaAttivaSingolaComponentSession h = (FatturaAttivaSingolaComponentSession) notaBp.createComponentSession();

            if (notaDiCredito.getCrudStatus() == notaDiCredito.NORMAL) {
                notaBp.edit(context, notaDiCredito);
                notaDiCredito = (Nota_di_credito_attivaBulk) notaBp.getModel();
            } else {
                notaDiCredito.setDt_termine_creazione_docamm(((Fattura_attivaBulk) notaBp.getModel()).getDt_termine_creazione_docamm());
                notaBp.setModel(context, (Nota_di_credito_attivaBulk) h.calcoloConsuntivi(context.getUserContext(), notaDiCredito));
            }

            java.util.List addedElements = basicAddDetailsTo(context, notaBp);
            notaDiCredito = (Nota_di_credito_attivaBulk) notaBp.getModel();
            if (!addedElements.isEmpty())
                notaBp.setModel(context, (Nota_di_credito_attivaBulk) h.calcoloConsuntivi(context.getUserContext(), notaDiCredito));

            notaBp.setAutoGenerated(Boolean.TRUE);

            context.addHookForward("chiusuraNotaDiCredito", this, "doChiusuraNotaDiCredito");
            return context.addBusinessProcess(notaBp);

        } catch (it.cnr.jada.comp.ComponentException e) {
            return handleException(context, e);
        } catch (java.rmi.RemoteException e) {
            return handleException(context, e);
        } catch (BusinessProcessException e) {
            return handleException(context, e);
        }
    }

    /**
     * Apre il pannello delle note di debito sul modello passato 'notaDiDebito' e nel
     * caso in cui alcune righe sono selezionate in fattura, cerca di aggiungerle come
     * dettagli della nota di debito stessa ('basicAddDetailsTo').
     */
    private Forward basicDoApriNotaDiDebito(
            ActionContext context,
            Nota_di_debito_attivaBulk notaDiDebito)
            throws it.cnr.jada.comp.ApplicationException {
//note di debito
        try {
            CRUDFatturaAttivaIBP bp = (CRUDFatturaAttivaIBP) context.getBusinessProcess();
            if (bp.isDirty() && bp.getDettaglio().getSelection().size() != 0)
                throw new it.cnr.jada.comp.ApplicationException("Il documento risulta modificato! Per continuare o salvare o deselezionare i dettagli da aggiungere alla nota di debito e ricercarli successivamente.");

            //Assolutamente necessario eseguirlo DOPO i controlli
            bp.rollbackAndCloseUserTransaction();

            String status = bp.isEditing() ? "M" : "V";
            CRUDNotaDiDebitoAttivaBP notaBp = (CRUDNotaDiDebitoAttivaBP) context.createBusinessProcess("CRUDNotaDiDebitoAttivaBP", new Object[]{status + "Tn"});
            FatturaAttivaSingolaComponentSession h = (FatturaAttivaSingolaComponentSession) notaBp.createComponentSession();

            if (notaDiDebito.getCrudStatus() == notaDiDebito.NORMAL) {
                notaBp.edit(context, notaDiDebito);
                notaDiDebito = (Nota_di_debito_attivaBulk) notaBp.getModel();
            } else {
                notaDiDebito.setDt_termine_creazione_docamm(((Fattura_attivaBulk) notaBp.getModel()).getDt_termine_creazione_docamm());
                notaBp.setModel(context, (Nota_di_debito_attivaBulk) h.calcoloConsuntivi(context.getUserContext(), notaDiDebito));
            }

            java.util.List addedElements = basicAddDetailsTo(context, notaBp);
            notaDiDebito = (Nota_di_debito_attivaBulk) notaBp.getModel();
            if (!addedElements.isEmpty())
                notaBp.setModel(context, (Nota_di_debito_attivaBulk) h.calcoloConsuntivi(context.getUserContext(), notaDiDebito));

            notaBp.setAutoGenerated(Boolean.TRUE);

            context.addHookForward("chiusuraNotaDiDebito", this, "doChiusuraNotaDiDebito");
            return context.addBusinessProcess(notaBp);

        } catch (it.cnr.jada.comp.ComponentException e) {
            return handleException(context, e);
        } catch (java.rmi.RemoteException e) {
            return handleException(context, e);
        } catch (BusinessProcessException e) {
            return handleException(context, e);
        }
    }

    /**
     * gestisce un operazione bringback sull'accertamento
     *
     * @param context     L'ActionContext della richiesta
     * @param newScadenza
     * @return Il Forward alla pagina di risposta
     */
    protected Forward basicDoBringBackOpenAccertamentiWindow(
            ActionContext context,
            Accertamento_scadenzarioBulk newScadenza) {

        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
        try {
            TerzoBulk debitore = newScadenza.getAccertamento().getDebitore();
            if (!((Fattura_attivaBulk) bp.getModel()).getCliente().equalsByPrimaryKey(debitore) &&
                    !AnagraficoBulk.DIVERSI.equalsIgnoreCase(debitore.getAnagrafico().getTi_entita()))
                throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata deve appartenere ad un accertamento che ha come debitore il cliente della fattura!");
            Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) bp.getAccertamentiController().getModel();
            if (scadenza != null) {
                resyncAccertamento(context, scadenza, newScadenza);
            } else {
                basicDoContabilizza(context, newScadenza, null);
            }
        } catch (Throwable t) {
            it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi defSaldiBulk = bp.getDefferedUpdateSaldiParentBP().getDefferedUpdateSaldiBulk();
            if (newScadenza.getAccertamento().getPg_ver_rec().equals((Long) newScadenza.getAccertamento().getSaldiInfo().get("pg_ver_rec")))
                defSaldiBulk.removeFromDefferredSaldi(newScadenza.getAccertamento());
            try {
                CRUDVirtualAccertamentoBP.rollbackToSafePoint(context);
            } catch (Exception e) {
                handleException(context, e);
            }
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * calcola i totali di riga
     *
     * @param context       L'ActionContext della richiesta
     * @param riga
     * @param vecchioTotale
     * @throws FillException
     */
    protected void basicDoCalcolaTotaliDiRiga(
            ActionContext context,
            Fattura_attiva_rigaBulk riga,
            java.math.BigDecimal vecchioTotale)
            throws it.cnr.jada.bulk.FillException {


        if (riga.getQuantita() == null) riga.setQuantita(new java.math.BigDecimal(1));
        if (riga.getPrezzo_unitario() == null) riga.setPrezzo_unitario(new java.math.BigDecimal(0));
        if (riga.getIm_iva() == null)
            riga.setIm_iva(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));

        riga.setFl_iva_forzata(Boolean.FALSE);
        riga.calcolaCampiDiRiga();
        if (riga instanceof Fattura_attiva_rigaIBulk)
            basicCalcolaImportoDisponibileNC(context, (Fattura_attiva_rigaIBulk) riga, vecchioTotale);
        doSelectAccertamenti(context);
    }

    private void basicDoContabilizza(
            ActionContext context,
            Accertamento_scadenzarioBulk scadenza,
            java.util.List selectedModels)
            throws it.cnr.jada.comp.ComponentException {

        //richiama il metodo della componponent per contabilizzare i dettagli selezionati

        if (scadenza != null && selectedModels != null) {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) context.getBusinessProcess();

            try {
                FatturaAttivaSingolaComponentSession h = (FatturaAttivaSingolaComponentSession) bp.createComponentSession();
                Fattura_attivaBulk fattura = h.contabilizzaDettagliSelezionati(
                        context.getUserContext(),
                        (Fattura_attivaBulk) bp.getModel(),
                        selectedModels,
                        scadenza);
                try {
                    bp.setModel(context, fattura);
                    bp.setDirty(true);
                } catch (BusinessProcessException e) {
                }
            } catch (java.rmi.RemoteException e) {
                bp.handleException(e);
            } catch (BusinessProcessException e) {
                bp.handleException(e);
            }

            doCalcolaTotalePerAccertamento(context, scadenza);
        }
    }

    /**
     * Creo una nuova istanza di nota di credito e ne richiedo l'apertura con il
     * metodo 'basicDoApriNotaDiCredito'
     */
    private Forward basicDoGeneraNotaDiCredito(ActionContext context)
            throws it.cnr.jada.comp.ComponentException, BusinessProcessException {
//NDC
        CRUDFatturaAttivaIBP bp = (CRUDFatturaAttivaIBP) context.getBusinessProcess();

        Integer esercizioScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());
        Fattura_attiva_IBulk fa = (Fattura_attiva_IBulk) bp.getModel();

        if (fa.isRiportata() && esercizioScrivania.intValue() == fa.getEsercizio().intValue())
            throw new it.cnr.jada.comp.ApplicationException("Non è possibile generare note di credito per fatture riportate!");

        // Gennaro Borriello - (02/11/2004 16.48.21)
        // 	Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato
        //	riportato DA UN ES. PRECEDENTE a quello di scrivania.
        // rospuc 11/05/2017
//	else if (!fa.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fa.getRiportataInScrivania()) && esercizioScrivania.intValue() != fa.getEsercizio().intValue())
//		throw new it.cnr.jada.comp.ApplicationException("Non è possibile generare note di credito per fatture non completamente riportate nell'esercizio di scrivania!");

        //if (fa.isRiportata() && !fa.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fa.getRiportata()))
        //throw new it.cnr.jada.comp.ApplicationException("Non è possibile generare note di credito per fatture non riportate completamente!");
        try {
            java.sql.Timestamp date = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
            int annoSolare = fa.getDateCalendar(date).get(java.util.Calendar.YEAR);
            if (annoSolare != esercizioScrivania.intValue())
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile inserire note di credito in esercizi non corrispondenti all'anno solare!");
        } catch (javax.ejb.EJBException e) {
            return handleException(context, e);
        }

        //Rimanadato a basicDoApriNotaDiCredito
        //bp.rollbackUserTransaction();

        Nota_di_credito_attivaBulk notaDiCredito = new Nota_di_credito_attivaBulk(
                fa,
                esercizioScrivania);

        return basicDoApriNotaDiCredito(context, notaDiCredito);
    }

    /**
     * Creo una nuova istanza di nota di debito e ne richiedo l'apertura con il
     * metodo 'basicDoApriNotaDiDebito'
     */

    private Forward basicDoGeneraNotaDiDebito(ActionContext context)
            throws it.cnr.jada.comp.ComponentException, BusinessProcessException {
//NDD
        CRUDFatturaAttivaIBP bp = (CRUDFatturaAttivaIBP) context.getBusinessProcess();

        Integer esercizioScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());
        Fattura_attiva_IBulk fa = (Fattura_attiva_IBulk) bp.getModel();

        if (fa.isRiportata() && esercizioScrivania.intValue() == fa.getEsercizio().intValue())
            throw new it.cnr.jada.comp.ApplicationException("Non è possibile generare note di debito per fatture riportate!");

        // Gennaro Borriello - (02/11/2004 16.48.21)
        // 	Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato
        //	riportato DA UN ES. PRECEDENTE a quello di scrivania.

        // rospuc 11/05/2017
        //	else if (!fa.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fa.getRiportataInScrivania()) && esercizioScrivania.intValue() != fa.getEsercizio().intValue())
        //		throw new it.cnr.jada.comp.ApplicationException("Non è possibile generare note di credito per fatture non completamente riportate nell'esercizio di scrivania!");

        //if (fa.isRiportata() && !fa.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fa.getRiportata()))
        //throw new it.cnr.jada.comp.ApplicationException("Non è possibile generare note di debito per fatture non riportate completamente!");
        try {
            java.sql.Timestamp date = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
            int annoSolare = fa.getDateCalendar(date).get(java.util.Calendar.YEAR);
            if (annoSolare != esercizioScrivania.intValue())
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile inserire note di debito in esercizi non corrispondenti all'anno solare!");
        } catch (javax.ejb.EJBException e) {
            return handleException(context, e);
        }

        //Rimanadato a basicDoApriNotaDiCredito
        //bp.rollbackUserTransaction();

        Nota_di_debito_attivaBulk notaDiDebito = new Nota_di_debito_attivaBulk(
                fa,
                esercizioScrivania);

        return basicDoApriNotaDiDebito(context, notaDiDebito);
    }

    /**
     * creo una nuova istanza di buono di scarico; lo inizializzo, aggiungo i dettagli
     * selezionati e ne richiedo l'apertura. Se esistevano già dei buoni di scarico, li
     * elimino
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     * @throws BusinessProcessException
     * @throws ComponentException
     * @throws RemoteException          Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward basicDoInventariaDettagli(ActionContext context)
            throws BusinessProcessException,
            it.cnr.jada.comp.ComponentException,
            java.rmi.RemoteException {
//inventario

        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) context.getBusinessProcess();
        Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();
        CarichiInventarioTable scarichi = fattura.getCarichiInventarioHash();

        if (scarichi != null && !scarichi.isEmpty()) {
            it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession h = (it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession) bp.createComponentSession(
                    "CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",
                    it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession.class);
            for (java.util.Enumeration e = ((CarichiInventarioTable) scarichi.clone()).keys(); e.hasMoreElements(); ) {
                Buono_carico_scaricoBulk buono = (Buono_carico_scaricoBulk) e.nextElement();
                buono.setToBeDeleted();
                h.eliminaConBulk(context.getUserContext(), buono);
                scarichi.remove(buono);
            }
        }
        for (java.util.Iterator i = fattura.getFattura_attiva_dettColl().iterator(); i.hasNext(); ) {
            Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) i.next();
            if (riga.isInventariato() && !fattura.getHa_beniColl()) riga.setInventariato(false);
        }

        java.util.List dettagliDaInventariare = getDettagliDaInventariare(context, bp.getDettaglio().getDetails().iterator());
        if (dettagliDaInventariare != null && !dettagliDaInventariare.isEmpty()) {
            CRUDScaricoInventarioBP ibp = (CRUDScaricoInventarioBP) context.getUserInfo().createBusinessProcess(context, "CRUDScaricoInventarioBP", new Object[]{"MRSWTh"});
            ibp.setBy_fattura(true);
            Buono_carico_scaricoBulk bcs = new Buono_carico_scaricoBulk();
            bcs.setByFattura(Boolean.TRUE);
            bcs.setPerVendita(Boolean.TRUE);
            bcs.setTi_documento(Buono_carico_scaricoBulk.SCARICO);
            bcs.initializeForInsert(ibp, context);
            bcs = (Buono_carico_scaricoBulk) ibp.createComponentSession().inizializzaBulkPerInserimento(context.getUserContext(), bcs);
            bcs.completeFrom(dettagliDaInventariare);
            bcs.setUser(fattura.getUser());
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
        bp.setMessage("Nessun dettaglio è inventariabile o tutti i dettagli inventariabili sono già stati inseriti!");
        return context.findDefaultForward();
    }

    /**
     * Viene richiamato nel momento in cui si seleziona IntraUE/SMarino/ExtraUE dalla
     * testata della fattura.
     */
    private void basicDoOnIntraUESMarinoExtraUEChange(ActionContext context, Fattura_attivaBulk fattura)
            throws it.cnr.jada.comp.ComponentException {

        try {
            FatturaAttivaSingolaComponentSession h = (FatturaAttivaSingolaComponentSession) ((CRUDFatturaAttivaBP) getBusinessProcess(context)).createComponentSession();
            java.util.Vector sezionali = h.estraeSezionali(context.getUserContext(), fattura);
            fattura.setSezionali(sezionali);
            if (!getBusinessProcess(context).isSearching() &&
                    sezionali != null && !sezionali.isEmpty())
                fattura.setTipo_sezionale((Tipo_sezionaleBulk) sezionali.firstElement());
            else
                fattura.setTipo_sezionale(null);
        } catch (Throwable t) {
            throw new it.cnr.jada.comp.ComponentException(t);
        }
    }

    private Forward basicDoRicercaAccertamento(
            ActionContext context,
            Fattura_attivaBulk fatturaAttiva,
            java.util.List models) {

        try {

            //controlla che gli importi dei dettagli siano diversi da 0
            Fattura_attiva_rigaBulk riga = null;
            if (models != null)
                for (java.util.Iterator i =
                     models.iterator();
                     i.hasNext();
                ) {
                    riga = (Fattura_attiva_rigaBulk) i.next();
                    if (riga.getIm_totale_divisa().compareTo(new java.math.BigDecimal(0)) == 0)
                        throw new it.cnr.jada.comp.ApplicationException("Non è possibile contabilizzare poiché un dettaglio\nselezionato ha un importo pari a 0");
                }

            //imposta i valori per la pagina di filtro sull'accertamento
            Filtro_ricerca_accertamentiVBulk filtro =
                    new Filtro_ricerca_accertamentiVBulk();
            filtro.setData_scadenziario(fatturaAttiva.getDt_scadenza());
            filtro.setCliente(fatturaAttiva.getCliente());
            filtro.setIm_importo(calcolaTotaleSelezionati(models, fatturaAttiva.quadraturaInDeroga()));
            filtro.setCd_unita_organizzativa(fatturaAttiva.getCd_unita_organizzativa());
            filtro.setCd_uo_origine(fatturaAttiva.getCd_uo_origine());
            filtro.setHasDocumentoCompetenzaCOGEInAnnoPrecedente(fatturaAttiva.hasCompetenzaCOGEInAnnoPrecedente());
            filtro.setHasDocumentoCompetenzaCOGESoloInAnnoCorrente(
                    !fatturaAttiva.hasCompetenzaCOGEInAnnoPrecedente() &&
                            fatturaAttiva.getDateCalendar(fatturaAttiva.getDt_a_competenza_coge()).get(java.util.Calendar.YEAR) == fatturaAttiva.getEsercizio().intValue());
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
            context.addHookForward("bringback", this, "doContabilizza");
            HookForward hook = (HookForward) context.findForward("bringback");
            return context.addBusinessProcess(bp);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * calcola il totale per la selezione
     *
     * @param selectedModels
     * @return
     * @throws ApplicationException
     */
    protected java.math.BigDecimal calcolaTotaleSelezionati(java.util.List selectedModels,
                                                            boolean escludiIVA)
            throws it.cnr.jada.comp.ApplicationException {

        java.math.BigDecimal importo = new java.math.BigDecimal(0);

        if (selectedModels != null) {
            for (java.util.Iterator i = selectedModels.iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaBulk rigaSelected = (Fattura_attiva_rigaBulk) i.next();
                java.math.BigDecimal imTotale = (escludiIVA) ?
                        rigaSelected.getIm_imponibile() :
                        rigaSelected.getIm_imponibile().add(rigaSelected.getIm_iva());

                java.math.BigDecimal imStornati = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
                java.math.BigDecimal imAddebitati = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
                if (rigaSelected instanceof Fattura_attiva_rigaIBulk) {
                    Fattura_attiva_rigaIBulk dettaglioFatturaAttiva = (Fattura_attiva_rigaIBulk) rigaSelected;
                    java.math.BigDecimal impStorniDiRiga = (escludiIVA) ?
                            calcolaTotaleSelezionati((Vector) dettaglioFatturaAttiva.getFattura_attivaI().getStorniHashMap().get(dettaglioFatturaAttiva), true) :
                            dettaglioFatturaAttiva.getIm_totale_storni();
                    imStornati = imStornati.add(impStorniDiRiga);
                    java.math.BigDecimal impAddebitiDiRiga = (escludiIVA) ?
                            calcolaTotaleSelezionati((Vector) dettaglioFatturaAttiva.getFattura_attivaI().getAddebitiHashMap().get(dettaglioFatturaAttiva), true) :
                            dettaglioFatturaAttiva.getIm_totale_addebiti();
                    imAddebitati = imAddebitati.add(impAddebitiDiRiga);
                }
                importo = importo.add(imTotale.add(imAddebitati).subtract(imStornati));
            }
        }
        importo = importo.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        return importo;
    }

    private void controllaQuadraturaAccertamenti(ActionContext context, Fattura_attivaBulk fatturaAttiva)
            throws it.cnr.jada.comp.ComponentException {

        //richiama il metodo della component per la quadratura
        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) context.getBusinessProcess();

        try {
            FatturaAttivaSingolaComponentSession h = (FatturaAttivaSingolaComponentSession) bp.createComponentSession();
            h.controllaQuadraturaAccertamenti(context.getUserContext(), fatturaAttiva);
        } catch (java.rmi.RemoteException e) {
            bp.handleException(e);
        } catch (BusinessProcessException e) {
            bp.handleException(e);
        }
    }

    /**
     * Controlla che le righe siano non Annullate e non associate a Mandati
     */
    private void controllaRighePerMandatieAnnullati(java.util.List models, Accertamento_scadenzarioBulk scadenza) throws it.cnr.jada.comp.ApplicationException {
        String emsg;
        for (java.util.Iterator it = models.iterator(); it.hasNext(); ) {
            Fattura_attiva_rigaBulk far = (Fattura_attiva_rigaBulk) it.next();
            if (far.getTi_associato_manrev() != null && far.ASSOCIATO_A_MANDATO.equalsIgnoreCase(far.getTi_associato_manrev())) {
                if (scadenza != null)
                    emsg =
                            "Impossibile scollegare l'accertamento \""
                                    + scadenza.getEsercizio_originale().intValue()
                                    + "/" + scadenza.getPg_accertamento().longValue()
                                    + "\" perchè il dettaglio collegato \""
                                    + ((far.getDs_riga_fattura() != null) ? far.getDs_riga_fattura() : String.valueOf(far.getProgressivo_riga().longValue()))
                                    + "\" è associato a mandato.";
                else
                    emsg =
                            "Impossibile scollegare il dettaglio \"" + ((far.getDs_riga_fattura() != null) ? far.getDs_riga_fattura() : String.valueOf(far.getProgressivo_riga().longValue())) + "\" perchè associato a mandato.";
                throw new it.cnr.jada.comp.ApplicationException(emsg);
            }
            if (far.isAnnullato()) {
                if (scadenza != null)
                    emsg =
                            "Impossibile scollegare l'accertamento \""
                                    + scadenza.getEsercizio_originale().intValue()
                                    + "/" + scadenza.getPg_accertamento().longValue()
                                    + "\" perchè il dettaglio collegato \""
                                    + ((far.getDs_riga_fattura() != null) ? far.getDs_riga_fattura() : String.valueOf(far.getProgressivo_riga().longValue()))
                                    + "\" è in stato "
                                    + far.STATO_ANNULLATO
                                    + ".";
                else
                    emsg =
                            "Impossibile scollegare il dettaglio \""
                                    + ((far.getDs_riga_fattura() != null) ? far.getDs_riga_fattura() : String.valueOf(far.getProgressivo_riga().longValue()))
                                    + "\" perchè è in stato "
                                    + far.STATO_ANNULLATO
                                    + ".";
                throw new it.cnr.jada.comp.ApplicationException(emsg);
            }
        }
    }

    /**
     * Controlla che i dettagli selezionati per la contabilizzazione siano tutti in
     * stato iniziale (e --> non siano stati contabilizzati precedentemente)
     */
    protected void controllaSelezione(ActionContext context, java.util.Iterator selectedModels)
            throws it.cnr.jada.comp.ApplicationException {

        //controlla che la selezione non sia in stato iniziale
        if (selectedModels != null) {
            while (selectedModels.hasNext()) {
                Fattura_attiva_rigaBulk rigaSelected = (Fattura_attiva_rigaBulk) selectedModels.next();
                if (!Fattura_attiva_rigaBulk.STATO_INIZIALE.equals(rigaSelected.getStato_cofi()))
                    throw new it.cnr.jada.comp.ApplicationException("Il dettaglio \"" + rigaSelected.getDs_riga_fattura() + "\" è già stato contabilizzato! Modificare la selezione.");
                try {
                    rigaSelected.validaDateCompetenza();
                } catch (ValidationException e) {
                    throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
                }
            }
        }
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doAddToCRUDMain_Accertamenti(ActionContext context) {

        try {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            bp.getDettaglio().getSelection().clearSelection();
            fillModel(context);
            Fattura_attivaBulk fatturaAttiva = (Fattura_attivaBulk) bp.getModel();

            if (fatturaAttiva.getCliente() == null || fatturaAttiva.getCliente().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.UNDEFINED)
                throw new it.cnr.jada.comp.ApplicationException("Per eseguire questa operazione è necessario impostare un cliente!");
            return basicDoRicercaAccertamento(context, fatturaAttiva, null);
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
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            bp.getDettaglio().getSelection().clearSelection();
            fillModel(context);
            Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) bp.getAccertamentiController().getModel();
            if (scadenza == null)
                throw new it.cnr.jada.comp.ApplicationException("Selezionare l'accertamento a cui associare i dettagli.");

            java.util.Vector selectedModels = new java.util.Vector();
            for (java.util.Enumeration e = bp.getDettaglio().getElements(); e.hasMoreElements(); ) {
                Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) e.nextElement();
                if (riga.STATO_INIZIALE.equals(riga.getStato_cofi()))
                    selectedModels.add(riga);
            }
            if (selectedModels.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Tutti i dettagli sono già stati contabilizzati!");
            it.cnr.jada.util.action.SelezionatoreListaBP slbp = (it.cnr.jada.util.action.SelezionatoreListaBP) select(
                    context,
                    new it.cnr.jada.util.ListRemoteIterator(selectedModels),
                    it.cnr.jada.bulk.BulkInfo.getBulkInfo(Fattura_attiva_rigaBulk.class),
                    "righiSet",
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
            it.cnr.jada.util.action.CRUDBP bp = (it.cnr.jada.util.action.CRUDBP) getBusinessProcess(context);
            Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();
            if (fattura.getStato_cofi() != null && fattura.getStato_cofi().equals(fattura.STATO_PAGATO))
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile inserire righe in una fattura pagata");

            ((CRUDFatturaAttivaBP) bp).getDettaglio().add(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Apre la nota di credito selezionata come conseguenza della selezione del bottone
     * di testata 'Apri note...'
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doApriNdCSelezionata(ActionContext context) {
//ndc
        try {
            Nota_di_credito_attivaBulk ndc = (Nota_di_credito_attivaBulk) ((HookForward) context.getCaller()).getParameter("focusedElement");
            if (ndc != null)
                return basicDoApriNotaDiCredito(context, ndc);
            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Apre la nota di debito selezionata come conseguenza della selezione del bottone
     * di testata 'Apri note...'
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doApriNdDSelezionata(ActionContext context) {
//ndd
        try {
            Nota_di_debito_attivaBulk ndd = (Nota_di_debito_attivaBulk) ((HookForward) context.getCaller()).getParameter("focusedElement");
            if (ndd != null)
                return basicDoApriNotaDiDebito(context, ndd);
            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Come conseguenza della selezione del bottone di testata 'Apri note...', vengono
     * ricercate le note create dalla fattura e presenta all'utente il selezionatore
     * per la scelta. Se l'elenco della ricerca è = 1 questo elemento viene aperto
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doApriNotaDiCredito(ActionContext context) {
        it.cnr.jada.util.RemoteIterator ri = null;
        try {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) context.getBusinessProcess();
            fillModel(context);

            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, findNoteDiCreditoFor(context, (Fattura_attiva_IBulk) bp.getModel()));
            if (ri == null || ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                throw new it.cnr.jada.comp.ApplicationException("Nessuna nota di credito generata per questa fattura!");
            } else if (ri.countElements() == 1) {
                return basicDoApriNotaDiCredito(context, (Nota_di_credito_attivaBulk) ri.nextElement());
            } else {
                return select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(Nota_di_credito_attivaBulk.class), "default", "doApriNdCSelezionata");
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Come conseguenza della selezione del bottone di testata 'Apri note...', vengono
     * ricercate le note create dalla fattura e presenta all'utente il selezionatore
     * per la scelta. Se l'elenco della ricerca è = 1 questo elemento viene aperto
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doApriNotaDiDebito(ActionContext context) {
        it.cnr.jada.util.RemoteIterator ri = null;
        try {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) context.getBusinessProcess();
            fillModel(context);

            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, findNoteDiDebitoFor(context, (Fattura_attiva_IBulk) bp.getModel()));
            if (ri == null || ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                throw new it.cnr.jada.comp.ApplicationException("Nessuna nota di debito generata per questa fattura!");
            } else if (ri.countElements() == 1) {
                return basicDoApriNotaDiDebito(context, (Nota_di_debito_attivaBulk) ri.nextElement());
            } else {
                return select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(Nota_di_debito_attivaBulk.class), "default", "doApriNdDSelezionata");
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Associa ad un buono di scarico già creato i dettagli selezionati in fattura
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     * @throws BusinessProcessException
     * @throws EJBException
     * @throws RemoteException
     * @throws IntrospectionException
     * @throws PersistencyException
     * @throws ComponentException
     * @throws FillException
     */
    public Forward basicDoAssociaDettagli(ActionContext context) throws BusinessProcessException, ComponentException, PersistencyException, IntrospectionException, RemoteException, EJBException {

        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) context.getBusinessProcess();
        Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();
        for (java.util.Iterator i = fattura.getFattura_attiva_dettColl().iterator(); i.hasNext(); ) {
            Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) i.next();
            AssociazioniInventarioTable associazioni = fattura.getAssociazioniInventarioHash();
            if ((associazioni != null && !associazioni.isEmpty()) || (riga.getCrudStatus() != OggettoBulk.TO_BE_CREATED)) {
                Ass_inv_bene_fatturaBulk ass = fattura.getAssociationWithInventarioFor(riga);
                if ((ass != null) && !ass.isPerAumentoValore()) {
                    if (riga.isInventariato()) riga.setInventariato(false);
                } else if (riga.getCrudStatus() != OggettoBulk.TO_BE_CREATED && (Fattura_attivaBulk.BENEDUREVOLE.equalsIgnoreCase(fattura.getTi_causale_emissione()))) {
                    riga.setInventariato(false);
                }
            }
        }//ricerca
        java.util.List dettagliDaInventariare = getDettagliDaInventariare(context, bp.getDettaglio().getDetails().iterator());
        if (dettagliDaInventariare != null && !dettagliDaInventariare.isEmpty()) {
            AssBeneFatturaBP ibp = (AssBeneFatturaBP) context.getUserInfo().createBusinessProcess(context, "AssBeneFatturaBP", new Object[]{"MRSWTh"});
            Ass_inv_bene_fatturaBulk associa = new Ass_inv_bene_fatturaBulk();
            associa.setLocal_transactionID(ibp.getLocalTransactionID(context.getUserContext(), true));
            associa.completeFrom(dettagliDaInventariare);
            associa.setInventario(((BuonoCaricoScaricoComponentSession) EJBCommonServices.createEJB(
                    "CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",
                    BuonoCaricoScaricoComponentSession.class)).caricaInventario(context.getUserContext()));

            ibp.setModel(context, associa);
            ibp.setDirty(false);
            context.addHookForward("bringback", this, "doBringBackAssociaInventario");
            HookForward hook = (HookForward) context.findForward("bringback");
            hook.addParameter("dettagliDaInventariare", dettagliDaInventariare);
            return context.addBusinessProcess(ibp);
        } else {  //R.P. MODIFICA BENI già ASSOCIATI(IN SOSPESO)
            if (fattura.getCrudStatus() != OggettoBulk.TO_BE_CREATED) {
                List dettagli = bp.getDettaglio().getDetails();
                for (Iterator i = dettagli.iterator(); i.hasNext(); ) {
                    Fattura_attiva_rigaIBulk riga = (Fattura_attiva_rigaIBulk) i.next();
                    if ((Fattura_attivaBulk.BENEDUREVOLE.equalsIgnoreCase(fattura.getTi_causale_emissione())))
                        dettagliDaInventariare.add(riga);
                }
                if (dettagliDaInventariare.size() == 0) {
                    bp.setMessage("Nessun dettaglio è inventariabile o tutti i dettagli inventariabili sono già stati caricati!");
                    return context.findDefaultForward();
                }

                AssBeneFatturaBP ibp = (AssBeneFatturaBP) context.getUserInfo().createBusinessProcess(context, "AssBeneFatturaBP", new Object[]{"MRSWTh"});
                Ass_inv_bene_fatturaBulk associa = new Ass_inv_bene_fatturaBulk();
                associa.setLocal_transactionID(ibp.getLocalTransactionID(context.getUserContext(), true));
                associa.completeFrom(dettagliDaInventariare);
                associa.setInventario(((BuonoCaricoScaricoComponentSession) EJBCommonServices.createEJB(
                        "CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",
                        BuonoCaricoScaricoComponentSession.class)).caricaInventario(context.getUserContext()));

                ibp.setModel(context, associa);
                ibp.setDirty(false);

                context.addHookForward("bringback", this, "doBringBackAssociaInventario");
                HookForward hook = (HookForward) context.findForward("bringback");
                hook.addParameter("dettagliDaInventariare", dettagliDaInventariare);
                return context.addBusinessProcess(ibp);
            } else {
                bp.setMessage("Nessun dettaglio è inventariabile o tutti i dettagli inventariabili sono già stati caricati!");
                return context.findDefaultForward();
            }
        }
    }

    /**
     * Gestisce una richiesta di azzeramento del searchtool "cliente"
     *
     * @param context        L'ActionContext della richiesta
     * @param fattura_attiva L'OggettoBulk padre del searchtool
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBlankSearchCliente(ActionContext context, Fattura_attivaBulk fattura_attiva) throws java.rmi.RemoteException {

        try {

            //imposta i valori di default per il cliente
            TerzoBulk nt = new TerzoBulk();
            nt.setAnagrafico(new AnagraficoBulk());
            fattura_attiva.setCliente(nt);
            fattura_attiva.setNome(null);
            fattura_attiva.setCognome(null);
            fattura_attiva.setRagione_sociale(null);
            fattura_attiva.setCodice_fiscale(null);
            fattura_attiva.setPartita_iva(null);
            fattura_attiva.setCodiceUnivocoUfficioIpa(null);
            fattura_attiva.setCodiceDestinatarioFatt(null);
            fattura_attiva.setPecFatturaElettronica(null);
            fattura_attiva.setMailFatturaElettronica(null);
            //fattura_attiva.setFl_liquidazione_differita(Boolean.FALSE);


            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce una richiesta di azzeramento del searchtool "tariffario"
     *
     * @param context L'ActionContext della richiesta
     * @param riga    L'OggettoBulk padre del searchtool
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBlankSearchTariffario(ActionContext context, Fattura_attiva_rigaBulk riga) throws java.rmi.RemoteException {

        try {
            //imposta i valori di default per il tariffario
            riga.setTariffario(new TariffarioBulk());
            riga.setVoce_iva(null);
            riga.setDs_riga_fattura(null);
            riga.setPrezzo_unitario(new java.math.BigDecimal("0"));
            riga.setPercentuale(null);
            riga.setQuantita(new java.math.BigDecimal("1"));
            riga.setIm_imponibile(new java.math.BigDecimal("0"));
            riga.setIm_iva(new java.math.BigDecimal("0"));
            riga.setIm_totale_divisa(new java.math.BigDecimal("0"));
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce una richiesta di azzeramento del searchtool "voce_iva"
     *
     * @param context             L'ActionContext della richiesta
     * @param fattura_attiva_riga L'OggettoBulk padre del searchtool
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBlankSearchVoce_iva(ActionContext context, Fattura_attiva_rigaBulk fattura_attiva_riga) throws java.rmi.RemoteException {

        try {

            //imposta i valori di default per la voce iva
            fattura_attiva_riga.setFl_iva_forzata(Boolean.FALSE);
            fattura_attiva_riga.setIm_iva(new java.math.BigDecimal(0));
            fattura_attiva_riga.setVoce_iva(new Voce_ivaBulk());

            basicCalcolaTotaliDiRiga(context);
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doBringBackAddToCRUDMain_Accertamenti_DettaglioAccertamenti(ActionContext context) {

        try {
            HookForward fwd = (HookForward) context.getCaller();
            java.util.List selectedModels = (java.util.List) fwd.getParameter("selectedElements");
            if (selectedModels != null && !selectedModels.isEmpty()) {
                CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
                Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) bp.getAccertamentiController().getModel();
                if (scadenza != null) {
                    basicDoContabilizza(context, scadenza, selectedModels);
                    bp.setDirty(true);
                }
                doCalcolaTotalePerAccertamento(context, scadenza);
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Aggiunge, una volta terminata l'operazione di associazione, tale elenco nel
     * modello
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doBringBackAssociaInventario(ActionContext context) {

        try {
            HookForward hook = (HookForward) context.getCaller();
            Ass_inv_bene_fatturaBulk ass = (Ass_inv_bene_fatturaBulk) hook.getParameter("bringback");
            if (ass != null) {
                java.util.List dettagliInventariati = (java.util.List) hook.getParameter("dettagliDaInventariare");
                CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) context.getBusinessProcess();
                Fattura_attivaBulk fatturaPassiva = (Fattura_attivaBulk) bp.getModel();
                for (java.util.Iterator i = dettagliInventariati.iterator(); i.hasNext(); ) {
                    Fattura_attiva_rigaBulk dettaglio = (Fattura_attiva_rigaBulk) i.next();
                    if (ass.getPg_riga() == null) {
                        BuonoCaricoScaricoComponentSession h = (BuonoCaricoScaricoComponentSession) bp.createComponentSession(
                                "CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",
                                BuonoCaricoScaricoComponentSession.class);
                        ass.setPg_riga(h.findMaxAssociazione(context.getUserContext(), ass));
                    }
                    fatturaPassiva.addToAssociazioniInventarioHash(ass, dettaglio);
                    dettaglio.setInventariato(true);
                }
            } else {
                java.util.List dettagliInventariati = (java.util.List) hook.getParameter("dettagliDaInventariare");
                CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) context.getBusinessProcess();
                Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();
                for (java.util.Iterator i = dettagliInventariati.iterator(); i.hasNext(); ) {
                    Fattura_attiva_rigaBulk dettaglio = (Fattura_attiva_rigaBulk) i.next();
                    if (((FatturaAttivaSingolaComponentSession) bp.createComponentSession()).ha_beniColl(context.getUserContext(), dettaglio))
                        dettaglio.setInventariato(true);
                    else
                        dettaglio.setInventariato(false);
                }
            }

            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }
//public Forward doSearchFind_trovato(ActionContext context)
//{
//	try{
//		fillModel(context);
//		CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP)context.getBusinessProcess();
//
//		BulkList<TrovatoBulk> listaTrovati = bp.listaTrovati(context);
//
//		it.cnr.jada.util.action.SelezionatoreListaBP slbp=null;
//		if (!listaTrovati.isEmpty()) {
//			slbp = (it.cnr.jada.util.action.SelezionatoreListaBP) select(
//					context,
//					new it.cnr.jada.util.ListRemoteIterator((java.util.List)listaTrovati),
//					it.cnr.jada.bulk.BulkInfo.getBulkInfo(TrovatoBulk.class),
//					null,
//					"doBringBackSearchFind_trovato");
//
//			slbp.setMultiSelection(false);
//		} else
//			bp.setMessage("La ricerca non ha fornito alcun risultato.");
//		return slbp;
//	}catch (Throwable ex) {
//		return handleException(context, ex);
//	}
//}

    /**
     * Viene richiamato nel momento in cui viene inserito/cambiato il trovato
     * nel dettaglio della fattura.
     */

    public Forward doVerificaEsistenzaTrovato(ActionContext context) {
        try {
            fillModel(context);
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            bp.ricercaDatiTrovato(context);
        } catch (Exception e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }

    /**
     * //public Forward doFreeSearchFind_trovato(ActionContext actioncontext) {
     * //    try
     * //    {
     * //    	BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
     * //		CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP)bulkbp;
     * //		bp.fillModel(actioncontext);
     * //
     * //		//TrovatoBulk oggettobulk = ((Fattura_attiva_rigaIBulk)bp.getDettaglio().getModel()).getTrovato();
     * //		TrovatoBulk oggettobulk = new TrovatoBulk();
     * //		FormField formfield = getFormField(actioncontext,"main.Dettaglio.find_trovato");
     * //        OggettoBulk oggettobulk1 = formfield.getModel();
     * //        RicercaLiberaTrovatoBP ricercaliberabp = (RicercaLiberaTrovatoBP)actioncontext.createBusinessProcess("RicercaLiberaTrovato");
     * //        ricercaliberabp.setSearchProvider(bp.getSearchProvider(oggettobulk1, formfield.getField().getProperty()));
     * //        ricercaliberabp.setFreeSearchSet(formfield.getField().getFreeSearchSet());
     * //        ricercaliberabp.setPrototype(oggettobulk);
     * //        ricercaliberabp.setColumnSet(formfield.getField().getColumnSet());
     * //        actioncontext.addHookForward("seleziona", this, "doBringBackSearchFind_trovato");
     * //        HookForward hookforward = (HookForward)actioncontext.findForward("seleziona");
     * //        hookforward.addParameter("field", formfield);
     * //        Forward fricercaliberabp = actioncontext.addBusinessProcess(ricercaliberabp);
     * //
     * //		BulkList<TrovatoBulk> listaTrovati = bp.listaTrovati(actioncontext);
     * //        ricercaliberabp.setListaTtovati(listaTrovati);
     * //        return fricercaliberabp;
     * //    }
     * //    catch(Exception exception)
     * //    {
     * //        return handleException(actioncontext, exception);
     * //    }
     * //
     * //}
     * //public Forward doBringBackSearchFind_trovato(ActionContext context)
     * //{
     * //	try{
     * //		HookForward caller = (HookForward)context.getCaller();
     * //		TrovatoBulk trovato = (TrovatoBulk)caller.getParameter("focusedElement");
     * //
     * //		CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP)getBusinessProcess(context);
     * //		if (trovato != null) {
     * //			Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) bp.getDettaglio().getModel();
     * //			riga.setTrovato(trovato);
     * //			riga.setPg_trovato(trovato.getPg_trovato());
     * //			riga.setToBeUpdated();
     * //		}
     * //		return context.findDefaultForward();
     * //	}catch (Throwable ex) {
     * //		return handleException(context, ex);
     * //	}
     * //}
     * //public Forward doBlankSearchFind_trovato(ActionContext context, TrovatoBulk trovato) {
     * //
     * //	if (trovato!=null) {
     * //		TrovatoBulk newt = new TrovatoBulk();
     * //		CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP)getBusinessProcess(context);
     * //		Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) bp.getDettaglio().getModel();
     * //		riga.setTrovato(newt);
     * //		riga.setPg_trovato(null);
     * //	}
     * //	return context.findDefaultForward();
     * //}
     * /**
     * Richiede la conferma per la cancellazione
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doBringBackConfirmDelete(ActionContext context) {

        //richiede la conferma per la cancellazione
        HookForward caller = (HookForward) context.getCaller();

        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
        try {
            if (caller.getParameter("undoBringBack") != null)
                throw new it.cnr.jada.comp.ApplicationException("Cancellazione annullata!");
            Risultato_eliminazioneVBulk re = (Risultato_eliminazioneVBulk) caller.getParameter("bringback");
            if (!re.getDocumentiAmministrativiScollegati().isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Eseguire il controllo di quadratura per tutti i dettagli in elenco!");

            bp.commitUserTransaction();
            return doPostConfirmDelete(context, re);
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
     * Gestisce un HookForward sul ritorno da una creazione contestuale
     */
    public Forward doBringBackCRUDCrea_cliente(ActionContext context,
                                               Fattura_attivaBulk fattura_attiva,
                                               TerzoBulk fornitoreTrovato)
            throws java.rmi.RemoteException {

        HookForward caller = (HookForward) context.getCaller();

        doBringBackSearchCliente(context, fattura_attiva, fornitoreTrovato);
        return context.findDefaultForward();
    }

    public Forward doBringBackCRUDCliente(ActionContext context,
                                          Fattura_attivaBulk fattura_attiva,
                                          TerzoBulk fornitoreTrovato)
            throws java.rmi.RemoteException {
        return doBringBackCRUDCrea_cliente(context, fattura_attiva, fornitoreTrovato);
    }

    /**
     * Aggiunge, una volta terminata l'operazione di creazione di buono di scarico,
     * tale buono al modello
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doBringBackInventariaDettagli(ActionContext context) {
//inventario
        try {
            HookForward hook = (HookForward) context.getCaller();
            Buono_carico_scaricoBulk buonoS = (Buono_carico_scaricoBulk) hook.getParameter("bringback");
            if (buonoS != null) {
                java.util.List dettagliInventariati = (java.util.List) hook.getParameter("dettagliDaInventariare");
                CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) context.getBusinessProcess();
                Fattura_attivaBulk fatturaAttiva = (Fattura_attivaBulk) bp.getModel();
                for (java.util.Iterator i = dettagliInventariati.iterator(); i.hasNext(); ) {
                    Fattura_attiva_rigaBulk dettaglio = (Fattura_attiva_rigaBulk) i.next();
                    fatturaAttiva.addToCarichiInventarioHash(buonoS, dettaglio);
                    dettaglio.setInventariato(true);
                }
            }
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Imposta il bp al ritorno del'accertamento
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doBringBackOpenAccertamentiWindow(ActionContext context) {
//imposta il bp al ritorno del'accertamento
        HookForward caller = (HookForward) context.getCaller();
        Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) caller.getParameter("bringback");
        if (scadenza != null) {
            try {
                basicDoBringBackOpenAccertamentiWindow(context, scadenza);

                //resetta il controller dell'accertamento e imposta il nuovo index
                CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
                bp.getAccertamentiController().reset(context);
                bp.getAccertamentiController().setModelIndex(context, -1);
                bp.getAccertamentiController().setModelIndex(context, it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(bp.getAccertamentiController().getDetails(), scadenza));

                //ricalcola i totali
                doCalcolaTotalePerAccertamento(context, (Accertamento_scadenzarioBulk) bp.getAccertamentiController().getModel());

                //il bulk è cambiato
                bp.setDirty(true);
                if (bp instanceof TitoloDiCreditoDebitoBP)
                    ((TitoloDiCreditoDebitoBP) bp).addToDocumentiContabiliModificati(scadenza);
            } catch (Throwable t) {
                return handleException(context, t);
            }
        }
        return context.findDefaultForward();
    }

    /**
     * Gestisce una richiesta di ricerca del searchtool "cliente"
     *
     * @param context          L'ActionContext della richiesta
     * @param fattura_attiva   L'OggettoBulk padre del searchtool
     * @param fornitoreTrovato L'OggettoBulk selezionato dall'utente
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBringBackSearchCliente(
            ActionContext context,
            Fattura_attivaBulk fattura_attiva,
            TerzoBulk fornitoreTrovato)
            throws java.rmi.RemoteException {
        try {
            //controlli post selezione del cliente
            if (fornitoreTrovato != null) {
                CRUDFatturaAttivaBP crudFattura =
                        (CRUDFatturaAttivaBP) getBusinessProcess(context);
                FatturaAttivaSingolaComponentSession fpcs =
                        (FatturaAttivaSingolaComponentSession) crudFattura.createComponentSession();
//        	controlloCodiceIPA(fattura_attiva, fornitoreTrovato);
                //controllo se il terzo non può essere un creditore ne un diversi
                if (fornitoreTrovato.getAnagrafico().getTi_entita().equals(AnagraficoBulk.DIVERSI) || fornitoreTrovato.getTi_terzo().equals(TerzoBulk.CREDITORE))
                    throw new it.cnr.jada.comp.ApplicationException("Il terzo selezionato non è un cliente valido, non può essere un creditore ne un diversi!");
                //controllo se il rapporto con il terzo selezionato è finito precedentemente
                //alla data di registrazione della fattura attiva
                if (fornitoreTrovato.getDt_fine_rapporto() != null && !fornitoreTrovato.getDt_fine_rapporto().after(fattura_attiva.getDt_registrazione()))
                    throw new it.cnr.jada.comp.ApplicationException("Il rapporto con il terzo selezionato è finito precedentemente alla data di registrazione della fattura attiva");

                //rieffettuo i controlli per la liquidazione differita
                doOnLiquidazioneDifferitaChange(context);


                //richiamo il metodo della component completaterzo
                fattura_attiva =
                        fpcs.completaTerzo(context.getUserContext(), fattura_attiva, fornitoreTrovato);
                if (fattura_attiva.getFl_extra_ue() != null && fattura_attiva.getFl_intra_ue() != null && fattura_attiva.getFl_san_marino() != null) {
                    if (!(fattura_attiva.getFl_extra_ue() || fattura_attiva.getFl_intra_ue() || fattura_attiva.getFl_san_marino())) {
                        //imposto il flag liquidazione differita
                        if (fattura_attiva.getCliente().getAnagrafico() != null &&
                                (fattura_attiva.getCliente().getAnagrafico().getFl_fatturazione_differita() != null &&
                                        fattura_attiva.getCliente().getAnagrafico().getFl_fatturazione_differita().booleanValue()) &&
                                (fattura_attiva.getFl_liquidazione_differita() != null && !fattura_attiva.getFl_liquidazione_differita().booleanValue()))
                            fattura_attiva.setFl_liquidazione_differita(Boolean.TRUE);
                        if (fattura_attiva.getCliente().getAnagrafico() != null &&
                                (fattura_attiva.getCliente().getAnagrafico().isEntePubblico() && !fattura_attiva.getFl_liquidazione_differita().booleanValue()))
                            doConfermaSplit(context, OptionBP.YES_BUTTON);
                        if (fattura_attiva.getCliente().getAnagrafico() != null &&
                                (fattura_attiva.getCliente().getAnagrafico().getDichiarazioni_intento().size() != 0)) {
                            doConfermaDichiarazione(context, OptionBP.YES_BUTTON);
                        }
                    }
                }
                crudFattura.setModel(context, fattura_attiva);
                crudFattura.resyncChildren(context);
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doConfermaDichiarazione(ActionContext context, int choice) throws java.rmi.RemoteException {
        try {
            fillModel(context);
            if (choice == OptionBP.YES_BUTTON) {
                CRUDBP bp = getBusinessProcess(context);
                bp.setMessage("Esiste una dichiarazione di intento per l'anagrafica.");
            }

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doConfermaSplit(ActionContext context, int choice) throws java.rmi.RemoteException {
        try {
            fillModel(context);
            if (choice == OptionBP.YES_BUTTON) {
                CRUDBP bp = getBusinessProcess(context);

                bp.setMessage("Verificare che l'ente pubblico non sia soggetto a split payment ed eventualmente aggiornare l'anagrafica.");
            }

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }
//private void controlloCodiceIPA(Fattura_attivaBulk fattura_attiva,
//		TerzoBulk fornitoreTrovato) throws ApplicationException {
//    //Controllo codice IPA
//    if (fornitoreTrovato.getAnagrafico() != null &&
//    	fornitoreTrovato.getAnagrafico().getCodiceAmministrazioneIpa() != null &&
//    	fornitoreTrovato.getCodiceUnivocoUfficioIpa() == null &&
//    	!fattura_attiva.getDt_registrazione().before(fornitoreTrovato.getAnagrafico().getDataAvvioFattElettr())){
//	    	throw new it.cnr.jada.comp.ApplicationException(
//	    			"Il codice terzo utilizzato si riferisce ad un'anagrafica censita nell'indice delle " +
//	    			"pubbliche amministrazioni. Richiedere tramite helpdesk l'inserimento del codice IPA " +
//	    			"relativo al terzo per il quale si sta tentando di emettere fattura.");
//
//    }
//}

    /**
     * Gestisce una richiesta di ricerca del searchtool "tariffario"
     *
     * @param context             L'ActionContext della richiesta
     * @param fattura_attiva_riga L'OggettoBulk padre del searchtool
     * @param beneTrovato         L'OggettoBulk selezionato dall'utente
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBringBackSearchTariffario(ActionContext context,
                                               Fattura_attiva_rigaBulk fattura_attiva_riga,
                                               TariffarioBulk beneTrovato)
            throws java.rmi.RemoteException {

        try {
            Voce_ivaBulk voceIvaB = null;
            if (beneTrovato != null) {
                //imposto i valori risultanti dal nuovo tariffario
                fattura_attiva_riga.setTariffario(beneTrovato);
                fattura_attiva_riga.setCd_tariffario(beneTrovato.getCd_tariffario());
                fattura_attiva_riga.setPrezzo_unitario(beneTrovato.getIm_tariffario());
                fattura_attiva_riga.setDs_riga_fattura(beneTrovato.getDs_tariffario());

                voceIvaB = beneTrovato.getVoce_iva();

            }
            return doBringBackSearchVoce_iva(context, fattura_attiva_riga, voceIvaB);

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce una richiesta di ricerca del searchtool "voce_iva"
     *
     * @param context             L'ActionContext della richiesta
     * @param fattura_attiva_riga L'OggettoBulk padre del searchtool
     * @param ivaTrovata          L'OggettoBulk selezionato dall'utente
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBringBackSearchVoce_iva(
            ActionContext context,
            Fattura_attiva_rigaBulk fattura_attiva_riga,
            Voce_ivaBulk ivaTrovata)
            throws java.rmi.RemoteException {

        try {

            if (ivaTrovata != null) {

                //imposto sulla riga le voci relative alla voce iva trovata
                fattura_attiva_riga.setVoce_iva(ivaTrovata);
                fattura_attiva_riga.setPercentuale(ivaTrovata.getPercentuale());
                fattura_attiva_riga.setFl_iva_forzata(Boolean.FALSE);

            }
            basicDoCalcolaTotaliDiRiga(
                    context,
                    fattura_attiva_riga,
                    fattura_attiva_riga.getIm_imponibile().add(fattura_attiva_riga.getIm_iva()));
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Calcola il totale per l'accertamento
     *
     * @param context  L'ActionContext della richiesta
     * @param scadenza
     * @return Il Forward alla pagina di risposta
     */
    public Forward doCalcolaTotalePerAccertamento(ActionContext context, Accertamento_scadenzarioBulk scadenza) {

        it.cnr.jada.util.action.FormBP bulkBP = (it.cnr.jada.util.action.FormBP) context.getBusinessProcess();
        if (bulkBP instanceof CRUDFatturaAttivaBP) {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) bulkBP;
            Fattura_attivaBulk fatt = (Fattura_attivaBulk) bp.getModel();
            //calcolo il totale per l'accertamento
            if (fatt.getFattura_attiva_accertamentiHash() != null && scadenza != null)
                try {
                    fatt.setImportoTotalePerAccertamento(calcolaTotaleSelezionati((java.util.List) fatt.getFattura_attiva_accertamentiHash().get(scadenza), fatt.quadraturaInDeroga()));
                } catch (it.cnr.jada.comp.ApplicationException e) {
                    fatt.setImportoTotalePerAccertamento(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                }
            else
                fatt.setImportoTotalePerAccertamento(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        }
        return context.findDefaultForward();
    }

    /**
     * Ricalcola il totali di riga
     */

    public Forward doCalcolaTotaliDiRiga(ActionContext context) {

        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
        Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) bp.getDettaglio().getModel();
        java.math.BigDecimal qta = riga.getQuantita();
        java.math.BigDecimal pu = riga.getPrezzo_unitario();
        java.math.BigDecimal imiva = riga.getIm_iva();
        Boolean flimiva = riga.getFl_iva_forzata();
        java.math.BigDecimal impnc = riga.getIm_diponibile_nc();

        try {
            java.math.BigDecimal vecchioTotale = riga.getIm_imponibile().add(riga.getIm_iva());
            fillModel(context);
            if (riga.getPrezzo_unitario() == null)//|| riga.getPrezzo_unitario().compareTo(new java.math.BigDecimal(0))<0)
                throw new it.cnr.jada.comp.ApplicationException("Inserire un importo maggiore di zero");
            //richiamo il metodo basicDoCalcolaTotaliDiRiga
            basicDoCalcolaTotaliDiRiga(context, riga, vecchioTotale);
        } catch (Throwable e) {
            //in caso di errore riimposto i valori precedenti
            riga.setQuantita(qta);
            riga.setPrezzo_unitario(pu);
            riga.setIm_diponibile_nc(impnc);
            riga.setIm_iva(imiva);
            riga.setFl_iva_forzata(flimiva);
            riga.calcolaCampiDiRiga();
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }

    /**
     * Chide la nota di credito e riapre la fattura originante
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doChiusuraNotaDiCredito(ActionContext context) {
//ndc
        try {
            it.cnr.jada.util.action.CRUDBP bp = (it.cnr.jada.util.action.CRUDBP) context.getBusinessProcess();
            bp.initializeUserTransaction(context);
            bp.edit(context, bp.getModel());
            return context.findDefaultForward();
        } catch (BusinessProcessException e) {
            return handleException(context, e);
        }
    }

    /**
     * Chide la nota di debito e riapre la fattura originante
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doChiusuraNotaDiDebito(ActionContext context) {
//ndd
        try {
            it.cnr.jada.util.action.CRUDBP bp = (it.cnr.jada.util.action.CRUDBP) context.getBusinessProcess();
            bp.initializeUserTransaction(context);
            bp.edit(context, bp.getModel());
            return context.findDefaultForward();
        } catch (BusinessProcessException e) {
            return handleException(context, e);
        }
    }

    /**
     * Richiede conferma all'utente per la continuazione del processo di creazione di
     * buono di scarico
     */
    public Forward doConfermaInventaria(ActionContext context, int option) {
//inventario
        try {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
                return basicDoInventariaDettagli(context);
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * Gestisce la contabilizzazione
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doContabilizza(ActionContext context) {

        HookForward caller = (HookForward) context.getCaller();
        Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) caller.getParameter("accertamentoSelezionato");
        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) context.getBusinessProcess();
        java.util.List selectedModels = null;
        try {
            selectedModels = bp.getDettaglio().getSelectedModels(context);
            bp.getDettaglio().getSelection().clearSelection();
        } catch (Throwable e) {
        }
        if (scadenza != null) {

            try {
                Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();
                Accertamento_scadenzarioBulk accertamento = null;
                AccertamentiTable accHash = fattura.getAccertamentiHash();
                if (accHash != null && !accHash.isEmpty())
                    accertamento = accHash.getKey(scadenza);
                if (accertamento != null && accertamento.getAccertamento().isTemporaneo()) {
                    java.util.Vector models = ((java.util.Vector) accHash.get(accertamento));
                    java.util.Vector clone = (java.util.Vector) models.clone();
                    if (!clone.isEmpty()) {
                        scollegaDettagliDaAccertamento(context, clone);
                        clone.addAll(selectedModels);
                        basicDoContabilizza(context, scadenza, clone);
                    } else {
                        accHash.remove(accertamento);
                        basicDoContabilizza(context, scadenza, selectedModels);
                    }
                } else {
                    basicDoContabilizza(context, scadenza, selectedModels);
                }
            } catch (it.cnr.jada.comp.ComponentException e) {
                return handleException(context, e);
            }

            bp.getAccertamentiController().getSelection().clear();
            bp.getAccertamentiController().setModelIndex(context, -1);
            bp.getAccertamentiController().setModelIndex(context, it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(bp.getAccertamentiController().getDetails(), scadenza));

            bp.setDirty(true);
            if (!"tabFatturaAttivaAccertamenti".equals(bp.getTab("tab")))
                bp.setTab("tab", "tabFatturaAttivaAccertamenti");
        }
        return context.findDefaultForward();
    }

    /**
     * Gestisce un comando di cancellazione o annullamento.
     * Nel caso di ndc o ndd richiede la gestione della quadratura delle scadenze di doc cont
     * tramite il gestore 'RisultatoEleminazioneBP'
     */

    public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {

        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
        Fattura_attivaBulk fa = (Fattura_attivaBulk) bp.getModel();
        try {
            fillModel(context);

            if (!bp.isEditing()) {
                bp.setMessage("Non è possibile cancellare in questo momento");
            } else {
                bp.delete(context);

                RisultatoEliminazioneBP rebp = (RisultatoEliminazioneBP) context.createBusinessProcess("RisultatoEliminazioneBP", new String[]{"MRSWTh"});
                Risultato_eliminazioneVBulk deleteManager = null;
                if (!(bp instanceof CRUDFatturaAttivaIBP))
                    deleteManager = rebp.manageDelete(context, bp);
                if (deleteManager != null &&
                        (!deleteManager.getDocumentiAmministrativiScollegati().isEmpty() || !deleteManager.getDocumentiContabiliScollegati().isEmpty())) {
                    rebp.edit(context, deleteManager);

                    context.addHookForward("bringback", this, "doBringBackConfirmDelete");
                    HookForward hook = (HookForward) context.findForward("bringback");
                    return context.addBusinessProcess(rebp);
                } else {
                    bp.commitUserTransaction();
                    if (fa.isVoidable()) {
                        bp.setMessage("Annullamento effettuato.");
                        bp.edit(context, bp.getModel());
                    } else {
                        if (!(bp instanceof CRUDFatturaAttivaIBP)) {
                            //Nel caso in cui Ndc e Ndd vengano aggiornate completamente in
                            //automatico e non sia necessario il gestore cancellazioni riapro la
                            //fattura attiva di origine
                            doCloseForm(context);
                            Forward fwd = doChiusuraNotaDiCredito(context);
                            ((CRUDFatturaAttivaIBP) context.getBusinessProcess()).setMessage("Cancellazione effettuata.");
                            return fwd;
                        }
                        bp.reset(context);
                        bp.setMessage("Cancellazione effettuata");
                    }
                }
            }
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
     * gestisce l'iva forzata
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doForzaIVA(ActionContext context) {

        //gestisce l'iva forzata

        //ricavo il bp dal context
        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
        //prendo la riga
        Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) bp.getDettaglio().getModel();
        //prendo il valore del flag iva forzata
        Boolean flForzaIVA = riga.getFl_iva_forzata();
        //e l'importo dell'iva
        java.math.BigDecimal impIVA = riga.getIm_iva();
        try {
            //vecchi totale
            java.math.BigDecimal vecchioTotale = riga.getIm_imponibile().add(riga.getIm_iva());
            //controllo eventuali addebbiti
            if (riga instanceof Fattura_attiva_rigaIBulk)
                vecchioTotale = vecchioTotale.add(((Fattura_attiva_rigaIBulk) riga).getIm_totale_addebiti());
            //riempio il model
            fillModel(context);
            //nel caso l'iva sia nulll imposto 0
            if (riga.getIm_iva() == null)
                riga.setIm_iva(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
            //controllo che non sia negativo
            if (riga.getIm_iva().compareTo(new java.math.BigDecimal(0)) < 0)
                throw new it.cnr.jada.comp.ApplicationException("Inserire un valore non negativo!");

            //imposto il valore del flag iva forzata a true
            riga.setFl_iva_forzata(Boolean.TRUE);
            //ricalcolo i totali di riga
            riga.calcolaCampiDiRiga();
            //richiamo il metodo per calcolare l'importo disponibile sulla nota di credito
            basicCalcolaImportoDisponibileNC(context, riga, vecchioTotale);
            //richiamo il metodo per controllare gli accertamenti
            doSelectAccertamenti(context);
        } catch (Throwable t) {
            //in caso di errore riporto la situazione a quella iniziale
            riga.setFl_iva_forzata(flForzaIVA);
            riga.setIm_iva(impIVA);
            riga.calcolaCampiDiRiga();
            return handleException(context, t);
        }
        //effettuo il forward di default
        return context.findDefaultForward();
    }

    /**
     * Creo e apro una nuova nota di credito. Viene eseguito rollback prima di
     * questa operazione per permettere il rilascio della fattura!
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doGeneraNotaDiCredito(ActionContext context) {
//ndc
        try {
            fillModel(context);
            return basicDoGeneraNotaDiCredito(context);

        } catch (it.cnr.jada.bulk.FillException e) {
            return handleException(context, e);
        } catch (BusinessProcessException e) {
            return handleException(context, e);
        } catch (it.cnr.jada.comp.ComponentException e) {
            return handleException(context, e);
        }
    }

    /**
     * Creo e apro una nuova nota di debito. Viene eseguito rollback prima di
     * questa operazione per permettere il rilascio della fattura!
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doGeneraNotaDiDebito(ActionContext context) {
//ndd
        try {
            fillModel(context);
            return basicDoGeneraNotaDiDebito(context);

        } catch (it.cnr.jada.bulk.FillException e) {
            return handleException(context, e);
        } catch (BusinessProcessException e) {
            return handleException(context, e);
        } catch (it.cnr.jada.comp.ComponentException e) {
            return handleException(context, e);
        }
    }

    /**
     * Inventaria i dettagli
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doInventariaDettagli(ActionContext context) {
//inventario
        try {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            fillModel(context);

            Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();
            //controllaQuadraturaConti(context, fattura);
            java.util.List dettagliDaInventariare = getDettagliDaInventariare(context, bp.getDettaglio().getDetails().iterator());
            if (dettagliDaInventariare != null && !dettagliDaInventariare.isEmpty() &&
                    fattura.BENEDUREVOLE.equalsIgnoreCase(fattura.getTi_causale_emissione()))
                return basicDoInventariaDettagli(context);
            else {
                bp.setMessage("Nessun dettaglio è inventariabile o tutti i dettagli inventariabili sono già stati inseriti!");
                return context.findDefaultForward();
            }
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la modifica automatica della scadenza dell'accertamento che quadra l'importo con
     * quello delle righe associate
     *
     * @param context L'ActionContext della richiesta
     * @param prefix
     * @return Il Forward alla pagina di risposta
     */
    public Forward doModificaScadenzaInAutomatico(ActionContext context, String prefix) {
//gestisce la modifica automatica della scadenza dell'accertamento che quadra l'importo con
//quello delle righe associate
        try {
            //ricavo il bp dal context
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            //riempio il model
            fillModel(context);

            //prendo la scadenza
            Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) bp.getAccertamentiController().getModel();

            //controllo che ci sia almeno una scadenza
            if (scadenza == null)
                throw new it.cnr.jada.comp.ApplicationException("Selezionare un accertamento da modificare in automatico!");
            //ricavo la FA
            Fattura_attiva_IBulk fatturaAttiva = (Fattura_attiva_IBulk) bp.getModel();
            //e le righe associate alla scadenza
            java.util.Vector righeAssociate = (java.util.Vector) fatturaAttiva.getFattura_attiva_accertamentiHash().get(scadenza);
            //controllo che ci siano righe associate
            if (righeAssociate == null || righeAssociate.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Associare dei dettagli prima di aggiornare in automatico la scadenza accertamento!");
            //controllo che la scadenza appartenga all'esercizio di scrivania nella fase di cancellazione
            if (bp.isDeleting() &&
                    !bp.isViewing() &&
                    !it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).equals(scadenza.getEsercizio()))
                throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata appartiene all'esercizio " + scadenza.getEsercizio().intValue() + "! Operazione annullata.");

            //controllo che l'importo della scadenza e dei dettagli associati siano effettivamente diversi
            if (!scadenza.getIm_scadenza().setScale(2).equals(getImportoPerAggiornamentoScadenzaInAutomatico(context, scadenza, (Fattura_attivaBulk) bp.getModel()))) {

                //ricavo la component dell'accertamento
                AccertamentoAbstractComponentSession h = CRUDVirtualAccertamentoBP.getVirtualComponentSession(context, true);

                try {
                    //richiamo il metodo per la modifica in automantico
                    scadenza =
                            (Accertamento_scadenzarioBulk) h.modificaScadenzaInAutomatico(
                                    context.getUserContext(),
                                    scadenza,
                                    getImportoPerAggiornamentoScadenzaInAutomatico(context, scadenza, (Fattura_attivaBulk) bp.getModel()),
                                    false);
                    //richiamo il metodo per il calcolo di eventuali saldi
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
                //imposto il forward
                Forward fwd = basicDoBringBackOpenAccertamentiWindow(context, scadenza);

                //reimposto la schermata di visualizzazione della scadenza
                bp.getAccertamentiController().getSelection().clear();
                bp.getAccertamentiController().setModelIndex(context, -1);
                bp.getAccertamentiController().setModelIndex(context, it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(bp.getAccertamentiController().getDetails(), scadenza));
                //setto la FA come modificata
                bp.setDirty(true);
                return fwd;
            } else
                throw new it.cnr.jada.comp.ApplicationException("La scadenza non ha bisogno di essere aggiornata!");
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Effettua i controlli quando cambia la causale
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doOnCausaleChange(ActionContext context) {

        try {
            //riempio il model
            fillModel(context);
            //ricavo il bp dal context
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            //ricavo la FA
            Fattura_attivaBulk fatturaAttiva = (Fattura_attivaBulk) bp.getModel();

            //ricavo i dettagli
            it.cnr.jada.bulk.BulkList dettaglio = fatturaAttiva.getFattura_attiva_dettColl();

            if (dettaglio != null) {
                //ciclo i dettagli
                for (java.util.Iterator i = dettaglio.iterator(); i.hasNext(); ) {
                    //ricavo la riga
                    Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) i.next();
                    //reimposta il tariffario se la fattura non e' a tariffario
                    if (!fatturaAttiva.getTi_causale_emissione().equals(fatturaAttiva.TARIFFARIO)) {
                        riga.setTariffario(null);
                        riga.setCd_tariffario(null);
                    }
                }
            }
            //reimposto il model
            bp.setModel(context, fatturaAttiva);
        } catch (Throwable t) {
            return handleException(context, t);
        }
        //ritorno il forward di default
        return context.findDefaultForward();
    }

    /**
     * Effettua i controlli quando cambia la causale
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doOnCausaleEmissioneChange(ActionContext context) {


        try {
            // ricavo il bp dal context
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            //la FA dal bp
            Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();

            //ricavo il valore del tipo causale emissione
            String causale = fattura.getTi_causale_emissione();
            //riempio il model
            fillModel(context);
            if (!(fattura.getFattura_attiva_dettColl() == null || fattura.getFattura_attiva_dettColl().isEmpty())) {
                fattura.setTi_causale_emissione(causale);
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile cambiare tipo di fattura se sono già state inserite delle righe");
            } else {
                bp.setModel(context, fattura);
                return context.findDefaultForward();
            }

        } catch (Throwable t) {
            return handleException(context, t);
        }

    }

    /**
     * Gestisce l'eccezione CheckDisponibilitaCassaFailed generata dall'obbligazione
     * mantenendo traccia della scelta di conferma o annullamento dell'operazione
     * da parte dell'utente
     */
    public Forward doOnCheckDisponibilitaCassaFailed(
            ActionContext context,
            int option) {
//cassa
        if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            try {
                boolean modified = fillModel(context);
                OptionRequestParameter userConfirmation = new OptionRequestParameter();
                userConfirmation.setCheckDisponibilitaDiCassaRequired(Boolean.FALSE);
                bp.setUserConfirm(userConfirmation);
                if (bp.isBringBack())
                    doConfermaRiporta(context, it.cnr.jada.util.action.OptionBP.OK_BUTTON);
                else if (bp.isCarryingThrough())
                    doRiportaAvanti(context);
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
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            try {
                boolean modified = fillModel(context);
                OptionRequestParameter userConfirmation = new OptionRequestParameter();
                userConfirmation.setCheckDisponibilitaContrattoRequired(Boolean.FALSE);
                bp.setUserConfirm(userConfirmation);
                if (bp.isBringBack())
                    doConfermaRiporta(context, it.cnr.jada.util.action.OptionBP.OK_BUTTON);
                else if (bp.isCarryingThrough())
                    doRiportaAvanti(context);
                else
                    doSalva(context);
            } catch (Throwable e) {
                return handleException(context, e);
            }
        }
        return context.findDefaultForward();
    }

    /**
     * Gestisce il cambio per FA extraue
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doOnFlExtraUEChange(ActionContext context) {

        //gestisce il cambio per FA extraue
        try {

            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();

            Boolean intraUE = fattura.getFl_intra_ue();
            Boolean extraUE = fattura.getFl_extra_ue();
            Boolean sanMarino = fattura.getFl_san_marino();
            fillModel(context);
            try {
                if (Boolean.TRUE.equals(fattura.getFl_extra_ue())) {
                    fattura.setFl_intra_ue(Boolean.FALSE);
                    fattura.setFl_san_marino(Boolean.FALSE);
                    fattura.setTi_bene_servizio(null);
                }
                basicDoOnIntraUESMarinoExtraUEChange(context, fattura);
            } catch (it.cnr.jada.comp.ComponentException e) {
                fattura.setFl_intra_ue(intraUE);
                fattura.setFl_extra_ue(extraUE);
                fattura.setFl_san_marino(sanMarino);
                bp.setModel(context, fattura);
                throw e;

            }
            bp.setModel(context, fattura);
            return context.findDefaultForward();

        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Gestisce il cambio per FA intraue
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doOnFlIntraUEChange(ActionContext context) {

        try {

            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();

            Boolean intraUE = fattura.getFl_intra_ue();
            Boolean extraUE = fattura.getFl_extra_ue();
            Boolean sanMarino = fattura.getFl_san_marino();
            fillModel(context);
            try {
                if (Boolean.TRUE.equals(fattura.getFl_intra_ue())) {
                    fattura.setFl_extra_ue(Boolean.FALSE);
                    fattura.setFl_san_marino(Boolean.FALSE);
                    fattura.setTi_bene_servizio(null);
                }
                basicDoOnIntraUESMarinoExtraUEChange(context, fattura);
            } catch (it.cnr.jada.comp.ComponentException e) {
                fattura.setFl_intra_ue(intraUE);
                fattura.setFl_extra_ue(extraUE);
                fattura.setFl_san_marino(sanMarino);
                bp.setModel(context, fattura);
                throw e;
            }

            bp.setModel(context, fattura);
            return context.findDefaultForward();

        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Gestisce il cambio per FA San Marino
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doOnFlSanMarinoChange(ActionContext context) {

        try {

            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();

            Boolean intraUE = fattura.getFl_intra_ue();
            Boolean extraUE = fattura.getFl_extra_ue();
            Boolean sanMarino = fattura.getFl_san_marino();
            fillModel(context);
            try {
                if (Boolean.TRUE.equals(fattura.getFl_san_marino())) {
                    fattura.setFl_extra_ue(Boolean.FALSE);
                    fattura.setFl_intra_ue(Boolean.FALSE);
                    fattura.setTi_bene_servizio(null);
                }
                basicDoOnIntraUESMarinoExtraUEChange(context, fattura);
            } catch (it.cnr.jada.comp.ComponentException e) {
                fattura.setFl_intra_ue(intraUE);
                fattura.setFl_extra_ue(extraUE);
                fattura.setFl_san_marino(sanMarino);
                bp.setModel(context, fattura);
                throw e;
            }

            bp.setModel(context, fattura);
            return context.findDefaultForward();

        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */

    public Forward doOnLiquidazioneDifferitaChange(ActionContext context) {

        try {
            fillModel(context);
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();
            if (fattura.getFl_liquidazione_differita() != null
                    && fattura.getFl_liquidazione_differita().booleanValue()
                /*&& fattura.getCliente().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL*/) {
                if (fattura.getCliente() != null
                        && fattura.getCliente().getAnagrafico() != null) {
                    AnagraficoBulk anag = fattura.getCliente().getAnagrafico();
                    if (anag.getFl_fatturazione_differita() != null &&
                            !anag.getFl_fatturazione_differita().booleanValue()) {
                        fattura.setFl_liquidazione_differita(Boolean.FALSE);
                        throw new it.cnr.jada.comp.ApplicationException(
                                "Il cliente selezionato NON prevede la liquidazione differita! Operazione annullata.");
                    }
                }
            }
            //if (fattura.getCliente() == null ||
            //fattura.getCliente().getAnagrafico() == null ||
            //fattura.getCliente().getCrudStatus() != it.cnr.jada.bulk.OggettoBulk.NORMAL) {
            //fattura.setFl_liquidazione_differita(Boolean.FALSE);
            //throw new it.cnr.jada.comp.ApplicationException("Selezionare un cliente.");
            //} else if (fattura.getCliente().getAnagrafico().getFl_fatturazione_differita() == null ||
            //!fattura.getCliente().getAnagrafico().getFl_fatturazione_differita().booleanValue()) {
            //fattura.setFl_liquidazione_differita(Boolean.FALSE);
            //throw new it.cnr.jada.comp.ApplicationException(
            //"Non è possibile la liquidazione differita per il cliente selezionato.");
            //}

            bp.setModel(context, fattura);
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */

    public Forward doOnModalitaPagamentoUOChange(ActionContext context) {

        try {
            fillModel(context);
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            bp.setContoEnte(false);
            Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();
            if (fattura.getModalita_pagamento_uo() != null) {
                FatturaAttivaSingolaComponentSession fpcs =
                        (FatturaAttivaSingolaComponentSession) bp.createComponentSession();
                java.util.List coll = (java.util.List) fpcs.findListabancheuo(context.getUserContext(), fattura);
                if (coll == null || coll.isEmpty())
                    fattura.setBanca_uo(null);
                else if (coll.size() == 1)
                    fattura.setBanca_uo((BancaBulk) new java.util.Vector(coll).firstElement());
                else {
                    if (!Rif_modalita_pagamentoBulk.BANCARIO.equals(fattura.getModalita_pagamento_uo().getTi_pagamento()))
                        fattura.setBanca_uo((BancaBulk) new java.util.Vector(coll).firstElement());
                    else {
                        fattura = fpcs.setContoEnteIn(context.getUserContext(), fattura, coll);
                        bp.setContoEnte(true);
                    }
                }
            } else {
                fattura.setBanca_uo(null);
            }
            bp.setModel(context, fattura);
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */

    public Forward doOnOccasionaleChange(ActionContext context) {

        try {
            fillModel(context);
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();
            bp.setModel(context, fattura);
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */

    public Forward doOnQuantitaChange(ActionContext context) {

        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
        Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) bp.getDettaglio().getModel();

        java.math.BigDecimal qta = riga.getQuantita();
        java.math.BigDecimal pu = riga.getPrezzo_unitario();
        java.math.BigDecimal imiva = riga.getIm_iva();
        Boolean flimiva = riga.getFl_iva_forzata();
        java.math.BigDecimal impnc = riga.getIm_diponibile_nc();
        try {
            java.math.BigDecimal vecchioTotale = riga.getIm_imponibile().add(riga.getIm_iva());
            if (riga instanceof Fattura_attiva_rigaIBulk)
                vecchioTotale = vecchioTotale.add(((Fattura_attiva_rigaIBulk) riga).getIm_totale_addebiti());
            fillModel(context);
            if (riga.getQuantita() == null ||
                    riga.getQuantita().compareTo(new java.math.BigDecimal(0)) <= 0)
                throw new it.cnr.jada.comp.ApplicationException("Inserire una quantità maggiore di zero");
            basicDoCalcolaTotaliDiRiga(context, riga, vecchioTotale);
        } catch (Throwable e) {
            riga.setQuantita(qta);
            riga.setPrezzo_unitario(pu);
            riga.setIm_diponibile_nc(impnc);
            riga.setIm_iva(imiva);
            riga.setFl_iva_forzata(flimiva);
            riga.calcolaCampiDiRiga();
            return handleException(context, e);
        }
        riga.setInventariato(false);

        return context.findDefaultForward();
    }

    /**
     * Gestisce il cambiamento del sezionale
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOnSezionaliFlagsChange(ActionContext context) {

        try {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();
            fillModel(context);
            try {

                basicDoOnIntraUESMarinoExtraUEChange(context, fattura);
                bp.setModel(context, fattura);
                return context.findDefaultForward();
            } catch (it.cnr.jada.comp.ComponentException e) {
                bp.setModel(context, fattura);
                throw e;
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Gestisce l'apertura della schermata di gestione/visualizzazione dell'accertamento
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doOpenAccertamentiWindow(ActionContext context) {

        try {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            fillModel(context);

            Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) bp.getAccertamentiController().getModel();
            boolean viewMode = bp.isViewing();
            if (scadenza == null)
                throw new it.cnr.jada.comp.ApplicationException("Selezionare l'accertamento da " + (viewMode ? "visualizzare" : "modificare") + " in manuale!");
            if (bp.isDeleting() &&
                    !bp.isViewing() &&
                    !it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).equals(scadenza.getEsercizio()))
                throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata appartiene all'esercizio " + scadenza.getEsercizio().intValue() + "! Operazione annullata.");

            if (!viewMode && bp instanceof IDocumentoAmministrativoBP)
                viewMode = !((IDocumentoAmministrativoBP) bp).getDocumentoAmministrativoCorrente().isEditable();
            String status = viewMode ? "V" : "M";
            it.cnr.contab.doccont00.bp.CRUDVirtualAccertamentoBP abp = it.cnr.contab.doccont00.bp.CRUDVirtualAccertamentoBP.getBusinessProcessFor(context, scadenza.getAccertamento(), status + "RSWTh");
            abp.edit(context, scadenza.getAccertamento());
            abp.selezionaScadenza(scadenza, context);

            context.addHookForward("bringback", this, "doBringBackOpenAccertamentiWindow");
            HookForward hook = (HookForward) context.findForward("bringback");
            return context.addBusinessProcess(abp);
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Informa l'utente sull'esito dell'operazione di cancellazione
     *
     * @param context L'ActionContext della richiesta
     * @param re
     * @return Il Forward alla pagina di risposta
     * @throws BusinessProcessException
     */
    public Forward doPostConfirmDelete(
            ActionContext context,
            Risultato_eliminazioneVBulk re)
            throws BusinessProcessException {

        it.cnr.jada.util.action.CRUDBP bp = getBusinessProcess(context);
        Fattura_attivaBulk fa = (Fattura_attivaBulk) bp.getModel();
        String msg = "Cancellazione effettuata!";
        if (fa.isVoidable()) {
            msg = "Annullamento effettuato!";
            bp.edit(context, bp.getModel());
        } else {
            bp.reset(context);
        }

        if (!re.getDocumentiContabiliScollegati().isEmpty())
            msg = msg + " Alcuni documenti contabili creati contestualmente al documento amministrativo sono rimasti inalterati.";
        bp.setMessage(msg);
        return context.findDefaultForward();
    }

    /**
     * Gestisce una richiesta di cancellazione dal controller "accertamenti"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doRemoveFromCRUDMain_Accertamenti(ActionContext context) {

        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) context.getBusinessProcess();
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
            java.util.Vector models = (java.util.Vector) ((Fattura_attivaBulk) bp.getModel()).getFattura_attiva_accertamentiHash().get(scadenza);
            try {
                Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();
                if (models != null && models.isEmpty()) {
                    fattura.getFattura_attiva_accertamentiHash().remove(scadenza);
                    fattura.addToDocumentiContabiliCancellati(scadenza);
                } else {
                    controllaRighePerMandatieAnnullati(models, scadenza);
                    scollegaDettagliDaAccertamento(context, (java.util.List) models.clone());
                }
            } catch (it.cnr.jada.comp.ComponentException e) {
                return handleException(context, e);
            }

            doCalcolaTotalePerAccertamento(context, null);

            Fattura_attiva_IBulk fattura = (Fattura_attiva_IBulk) bp.getModel();
            setAndVerifyStatusFor(context, fattura);

            bp.getAccertamentiController().getSelection().clear();
            bp.getAccertamentiController().setModelIndex(context, -1);
            bp.setDirty(true);
        }
        return context.findDefaultForward();
    }

    /**
     * Gestisce una richiesta di cancellazione dal controller "accertamenti_DettaglioAccertamenti"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doRemoveFromCRUDMain_Accertamenti_DettaglioAccertamenti(ActionContext context) {

        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) context.getBusinessProcess();
        try {
            it.cnr.jada.util.action.Selection selection = bp.getDettaglioAccertamentoController().getSelection();
            if (selection.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Selezionare i dettagli che si desidera scollegare!");
            java.util.List models = selection.select(bp.getDettaglioAccertamentoController().getDetails());
            controllaRighePerMandatieAnnullati(models, null);
            scollegaDettagliDaAccertamento(context, models);
        } catch (it.cnr.jada.comp.ComponentException e) {
            return handleException(context, e);
        }

        doCalcolaTotalePerAccertamento(context, (Accertamento_scadenzarioBulk) bp.getAccertamentiController().getModel());

        Fattura_attiva_IBulk fattura = (Fattura_attiva_IBulk) bp.getModel();
        setAndVerifyStatusFor(context, fattura);

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
     * Gestisce un cambiamento di pagina su un controllo tabbed {@link it.cnr.jada.util.jsp.JSPUtils}
     */
    public Forward doRicercaAccertamento(ActionContext context) {

        try {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            fillModel(context);
            java.util.List models = bp.getDettaglio().getSelectedModels(context);
            Forward forward = context.findDefaultForward();
            if (models == null || models.isEmpty())
                bp.setErrorMessage("Per procedere, selezionare i dettagli da contabilizzare!");
            else {
                controllaSelezione(context, models.iterator());
                Fattura_attivaBulk fatturaAttiva = (Fattura_attivaBulk) bp.getModel();
                if (fatturaAttiva.getCliente() == null || fatturaAttiva.getCliente().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.UNDEFINED)
                    throw new it.cnr.jada.comp.ApplicationException("Per eseguire questa operazione è necessario impostare un cliente!");
                forward = basicDoRicercaAccertamento(context, fatturaAttiva, models);
            }
            return forward;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doRiportaAvanti(ActionContext context) throws java.rmi.RemoteException {

        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) context.getBusinessProcess();

        try {
            fillModel(context);
            bp.salvaRiportandoAvanti(context);
            bp.getAccertamentiController().setModelIndex(context, -1);
            return context.findDefaultForward();
        } catch (ValidationException e) {
            bp.setErrorMessage(e.getMessage());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doRiportaIndietro(ActionContext context) throws java.rmi.RemoteException {

        try {
            fillModel(context);
            ((CRUDFatturaAttivaBP) getBusinessProcess(context)).riportaIndietro(context);
            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }

    }

    public Forward doSalva(ActionContext actioncontext) throws java.rmi.RemoteException {
        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(actioncontext);
        try {
            fillModel(actioncontext);

            if (bp.getAccertamentiController() != null)
                bp.getAccertamentiController().setModelIndex(actioncontext, -1);
//        controlloCodiceIPA((Fattura_attivaBulk)bp.getModel(), ((Fattura_attivaBulk)bp.getModel()).getCliente());
            bp.save(actioncontext);
            postSalvataggio(actioncontext);
            return actioncontext.findDefaultForward();
        } catch (ValidationException validationexception) {
            getBusinessProcess(actioncontext).setErrorMessage(validationexception.getMessage());
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
        return actioncontext.findDefaultForward();
    }

    protected void postSalvataggio(ActionContext context) throws BusinessProcessException {

        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
        ristampaFatturaElettronica(context, bp);
    }

	private void ristampaFatturaElettronica(ActionContext context, CRUDFatturaAttivaBP bp)
			throws BusinessProcessException {
		bp.gestioneAllegatiFatturazioneElettronica(context);
	}

    /**
     * Viene richiamato nel momento in cui si seleziona la lista delle banche nella
     * riga del documento generico.
     * Viene passato un parametro relativo al tipo di banca.
     */
    public Forward doSearchListabancheuo(ActionContext context) {

        Fattura_attivaBulk fattura = (Fattura_attivaBulk) getBusinessProcess(context).getModel();
        return search(context, getFormField(context, "main.listabancheuo"), fattura.getModalita_pagamento_uo().getTiPagamentoColumnSet());
    }

    /**
     * Gestisce una richiesta di selezione dal controller "accertamenti"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doSelectAccertamenti(ActionContext context) {

        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) context.getBusinessProcess();
        try {
            bp.getAccertamentiController().setSelection(context);
        } catch (Throwable e) {
            return handleException(context, e);
        }

        doCalcolaTotalePerAccertamento(context, (Accertamento_scadenzarioBulk) bp.getAccertamentiController().getModel());
        return context.findDefaultForward();
    }

    /**
     * Viene richiamato nel momento in cui si seleziona una valuta dal combo Valuta nella
     * testata della fattura.
     * Richiama a sua volta il metodo cercaCambio dalla component.
     */
    public Forward doSelezionaValuta(ActionContext context) {
        try {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();
            DivisaBulk divisa = fattura.getValuta();
            fillModel(context);
            try {
                FatturaAttivaSingolaComponentSession h = (FatturaAttivaSingolaComponentSession) bp.createComponentSession();
                fattura = h.cercaCambio(context.getUserContext(), fattura);

                //basicDoCalcolaTotaleFatturaFornitoreInEur(fattura);
                bp.setModel(context, fattura);
                return context.findDefaultForward();
            } catch (it.cnr.jada.comp.ComponentException e) {
                fattura.setValuta(divisa);
                throw e;
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Gestisce un cambiamento di pagina su un controllo tabbed {@link it.cnr.jada.util.jsp.JSPUtils}
     */
    public Forward doTab(ActionContext context, String tabName, String pageName) {

        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
        Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();

        java.sql.Timestamp competenzaABck = fattura.getDt_a_competenza_coge();
        java.sql.Timestamp competenzaDaBck = fattura.getDt_da_competenza_coge();

        java.util.GregorianCalendar tsOdiernoGregorian = new GregorianCalendar();
        String cds = CNRUserContext.getCd_cds(context.getUserContext());

        try {
            fillModel(context);
            java.sql.Timestamp competenzaA = fattura.getDt_a_competenza_coge();
            java.sql.Timestamp competenzaDa = fattura.getDt_da_competenza_coge();

            if (competenzaA != competenzaABck) {
                tsOdiernoGregorian.setTime(new Date(competenzaA.getTime()));
                Integer esercizioCompetenzaA = new Integer(tsOdiernoGregorian.get(java.util.GregorianCalendar.YEAR));
                if (((FatturaAttivaSingolaComponentSession) bp.createComponentSession()).isEsercizioChiusoPerDataCompetenza(context.getUserContext(), esercizioCompetenzaA, cds))
                    throw new it.cnr.jada.comp.ApplicationException("Le date \"Competenza da\" e \"Competenza a\" non possono appartenere ad un esercizio chiuso");

            }
            if (competenzaDa != competenzaDaBck) {
                tsOdiernoGregorian.setTime(new Date(competenzaDa.getTime()));
                Integer esercizioCompetenzaDa = new Integer(tsOdiernoGregorian.get(java.util.GregorianCalendar.YEAR));
                if (((FatturaAttivaSingolaComponentSession) bp.createComponentSession()).isEsercizioChiusoPerDataCompetenza(context.getUserContext(), esercizioCompetenzaDa, cds))
                    throw new it.cnr.jada.comp.ApplicationException("Le date \"Competenza da\" e \"Competenza a\" non possono appartenere ad un esercizio chiuso");
            }

        } catch (Throwable t) {
            fattura.setDt_a_competenza_coge(competenzaABck);
            fattura.setDt_da_competenza_coge(competenzaDaBck);
            return handleException(context, t);
        }
        try {
            fillModel(context);
            FatturaAttivaSingolaComponentSession h = null;

            if ("tabFatturaAttiva".equalsIgnoreCase(bp.getTab(tabName))) {
                if (fattura.getTi_causale_emissione() == null && !bp.isSearching())
                    throw new it.cnr.jada.comp.ApplicationException("Tipo fattura non può essere nullo.");
                if ((fattura.getFl_intra_ue() != null && fattura.getFl_intra_ue().booleanValue()) &&
                        fattura.getTi_bene_servizio() == null && !bp.isSearching())
                    throw new it.cnr.jada.comp.ApplicationException("Il Tipo Servizi/beni non può essere nullo.");
                if (fattura.getTi_causale_emissione() != fattura.TARIFFARIO)
                    doOnCausaleChange(context);
                if (!bp.isSearching() && !bp.isViewing() && !fattura.isRODateCompetenzaCOGE())
                    fattura.validaDateCompetenza();
            }

            if ("tabFatturaAttivaAccertamenti".equalsIgnoreCase(bp.getTab(tabName))) {
                try {
                    fillModel(context);
                    controllaQuadraturaAccertamenti(context, fattura);
                } catch (it.cnr.jada.comp.ApplicationException e) {
                    bp.setErrorMessage(e.getMessage());
                }
            }
            if ("tabFatturaAttivaConsuntivo".equalsIgnoreCase(pageName)) {
                fillModel(context);
                h = (FatturaAttivaSingolaComponentSession) bp.createComponentSession();
                fattura = (Fattura_attivaBulk) h.calcoloConsuntivi(context.getUserContext(), fattura);
                bp.setModel(context, fattura);
            }

            if ("tabFatturaAttivaDettaglio".equalsIgnoreCase(bp.getTab(tabName))) {
                fillModel(context);
                bp.getDettaglio().validate(context);
                h = (FatturaAttivaSingolaComponentSession) bp.createComponentSession();
                fattura = (Fattura_attivaBulk) h.calcoloConsuntivi(context.getUserContext(), fattura);
                bp.setModel(context, fattura);
            }
            if ("tabFatturaAttivaIntrastat".equalsIgnoreCase(bp.getTab(tabName))) {
                fillModel(context);
                bp.getDettaglioIntrastatController().validate(context);
            }
            if ("tabFatturaAttivaAllegati".equalsIgnoreCase(bp.getTab(tabName))) {
                fillModel(context);
            }
            return super.doTab(context, tabName, pageName);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * ricerca le note di credito per la fattura attiva aperta
     */
    private it.cnr.jada.util.RemoteIterator findNoteDiCreditoFor(
            ActionContext context,
            Fattura_attiva_IBulk fatt) throws it.cnr.jada.comp.ComponentException {

        try {
            CRUDFatturaAttivaIBP bp = (CRUDFatturaAttivaIBP) context.getBusinessProcess();
            return ((FatturaAttivaSingolaComponentSession) bp.createComponentSession()).findNotaDiCreditoFor(context.getUserContext(), fatt);
        } catch (java.rmi.RemoteException e) {
            throw new it.cnr.jada.comp.ComponentException(e);
        } catch (BusinessProcessException e) {
            throw new it.cnr.jada.comp.ComponentException(e);
        }
    }

    /**
     * ricerca le note di debito per la fattura attiva aperta
     */

    private it.cnr.jada.util.RemoteIterator findNoteDiDebitoFor(
            ActionContext context,
            Fattura_attiva_IBulk fatt) throws it.cnr.jada.comp.ComponentException {

        try {
            CRUDFatturaAttivaIBP bp = (CRUDFatturaAttivaIBP) context.getBusinessProcess();
            return ((FatturaAttivaSingolaComponentSession) bp.createComponentSession()).findNotaDiDebitoFor(context.getUserContext(), fatt);
        } catch (java.rmi.RemoteException e) {
            throw new it.cnr.jada.comp.ComponentException(e);
        } catch (BusinessProcessException e) {
            throw new it.cnr.jada.comp.ComponentException(e);
        }
    }

    /**
     * Ricava i dettagli da inventariare
     *
     * @param context  L'ActionContext della richiesta
     * @param dettagli
     * @return
     */
    protected java.util.List getDettagliDaInventariare(
            ActionContext context,
            java.util.Iterator dettagli) {

        java.util.Vector coll = new java.util.Vector();
        if (dettagli != null) {
            while (dettagli.hasNext()) {
                Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) dettagli.next();
                if (Fattura_attivaBulk.BENEDUREVOLE.equalsIgnoreCase(riga.getFattura_attiva().getTi_causale_emissione()) &&
                        !riga.isInventariato())
                    coll.add(riga);
            }
        }
        return coll;
    }

    /**
     * Restituisce tra i dettagli passati in argomento quelli che sono in uno degli
     * stati specificati in statiDettaglio
     *
     * @param context        L'ActionContext della richiesta
     * @param dettagli
     * @param statiDettaglio
     * @return
     */
    protected java.util.List getDettagliInStato(
            ActionContext context,
            java.util.Iterator dettagli,
            String[] statiDettaglio) {

        java.util.Vector coll = new java.util.Vector();
        if (dettagli != null) {
            while (dettagli.hasNext()) {
                Nota_di_credito_attiva_rigaBulk rigaNdC = (Nota_di_credito_attiva_rigaBulk) dettagli.next();
                for (int cont = 0; cont < statiDettaglio.length; cont++) {
                    String statoDettaglio = statiDettaglio[cont];
                    if (statoDettaglio.equals(rigaNdC.getRiga_fattura_associata().getStato_cofi()))
                        if (!coll.contains(rigaNdC))
                            coll.add(rigaNdC);
                }
            }
        }
        return coll;
    }

    /**
     * Ricava l'importo per l'aggiornamento in automatico per la scadenza selezionata
     *
     * @param context       L'ActionContext della richiesta
     * @param scadenza
     * @param fatturaAttiva
     * @return
     */
    protected java.math.BigDecimal getImportoPerAggiornamentoScadenzaInAutomatico(
            ActionContext context,
            Accertamento_scadenzarioBulk scadenza,
            Fattura_attivaBulk fatturaAttiva) {

        return fatturaAttiva.getImportoTotalePerAccertamento();
    }

    protected it.cnr.jada.util.action.Selection getIndexSelectionOn(
            it.cnr.jada.util.action.Selection selection,
            java.util.List dett,
            String property) {

        int jindex = 0;
        int position = 0;
        it.cnr.jada.util.action.Selection newSelection = new it.cnr.jada.util.action.Selection();
        for (it.cnr.jada.util.action.SelectionIterator i = selection.iterator(); i.hasNext(); ) {
            int index = i.nextIndex();
            do {
                Object obj = null;
                do {
                    obj = dett.get(jindex++);
                } while (it.cnr.jada.util.Introspector.getBoolean(obj, property, true));
            } while (position++ != index);
            newSelection.addToSelection(jindex - 1);
        }
        return newSelection;
    }

    /**
     * Ricerca un boolean 'true' se tra i dettagli 'models' ne esiste qualcuno con
     * associati storni o addebiti
     */

    private boolean hasRangeDetailWithDocAmmAssociated(ActionContext context, java.util.List models) {

        if (models != null) {
            for (java.util.Iterator i = models.iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaBulk dettaglio = (Fattura_attiva_rigaBulk) i.next();
                if (dettaglio instanceof Fattura_attiva_rigaIBulk) {
                    Fattura_attiva_rigaIBulk rigaFA = (Fattura_attiva_rigaIBulk) dettaglio;
                    if (rigaFA.hasStorni() || rigaFA.hasAddebiti())
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Restituisce un boolean 'true' se tra i dettagli 'details' esiste un dettaglio
     * con chiave primaria uguale a quella di 'rigaNdC'
     */

    private boolean isRigaContainedInDetails(Nota_di_credito_attiva_rigaBulk rigaNdC, java.util.List details) {

        if (details == null || details.isEmpty() || rigaNdC == null)
            return false;

        for (java.util.Iterator i = details.iterator(); i.hasNext(); ) {
            Nota_di_credito_attiva_rigaBulk dettaglio = (Nota_di_credito_attiva_rigaBulk) i.next();
            if (dettaglio.getRiga_fattura_associata().equalsByPrimaryKey(rigaNdC.getRiga_fattura_associata()))
                return true;
        }
        return false;
    }

    public Forward doConfermaAssocia(ActionContext context, int option) {

        try {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
                return basicDoAssociaDettagli(context);
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * Restituisce un boolean 'true' se tra i dettagli 'details' esiste un dettaglio
     * con chiave primaria uguale a quella di 'rigaNdD'
     */
    private boolean isRigaContainedInDetails(Nota_di_debito_attiva_rigaBulk rigaNdD, java.util.List details) {

        if (details == null || details.isEmpty() || rigaNdD == null)
            return false;

        for (java.util.Iterator i = details.iterator(); i.hasNext(); ) {
            Nota_di_debito_attiva_rigaBulk dettaglio = (Nota_di_debito_attiva_rigaBulk) i.next();
            if (dettaglio.getRiga_fattura_associata().equalsByPrimaryKey(rigaNdD.getRiga_fattura_associata()))
                return true;
        }
        return false;
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

    private void resyncAccertamento(
            ActionContext context,
            Accertamento_scadenzarioBulk oldScadenza,
            Accertamento_scadenzarioBulk newScadenza)
            throws it.cnr.jada.comp.ComponentException {

        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
        Fattura_attivaBulk fatturaAttiva = (Fattura_attivaBulk) bp.getModel();
        java.util.Vector models = ((java.util.Vector) fatturaAttiva.getFattura_attiva_accertamentiHash().get(oldScadenza));
        java.util.Vector clone = (java.util.Vector) models.clone();
        if (fatturaAttiva.getStato_cofi().equals(fatturaAttiva.STATO_PAGATO))
            throw new it.cnr.jada.comp.ApplicationException("Questa operazione non è consentita su fatture in stato pagato");
        if (!clone.isEmpty())
            scollegaDettagliDaAccertamento(context, clone);
        else
            fatturaAttiva.getFattura_attiva_accertamentiHash().remove(oldScadenza);

        basicDoContabilizza(context, newScadenza, clone);
    }

    /**
     * Risincronizza la collezione degli accertamenti (richiamato dopo la modifica di
     * una scadenza associata al doc amm).
     * Se questa collezione contiene in chiave la oldScadenza (scadenza vecchia), essa
     * viene eliminata e i dettagli ad essa associati vengono ricontabilizzati sulla
     * newScadenza (scadenza modificata dall'utente); se non ha ancora dettagli associati
     * viene semplicemente eliminata
     * Se uno dei dettagli ha un'associazione con note di credito/debito e se non
     * sono in fase di cancellazione della fattura attiva, l'operazione viene interrotta
     */

    private void scollegaDettagliDaAccertamento(ActionContext context, java.util.List models)
            throws it.cnr.jada.comp.ComponentException {

        if (models != null) {
            try {
                if (!((CRUDFatturaAttivaBP) getBusinessProcess(context)).isDeleting() &&
                        hasRangeDetailWithDocAmmAssociated(context, models))
                    throw new it.cnr.jada.comp.ApplicationException("Uno o più dettagli hanno storni o addebiti collegati! Impossibile scollegare.");

                for (java.util.Iterator i = models.iterator(); i.hasNext(); ) {
                    Fattura_attiva_rigaBulk dettaglio = (Fattura_attiva_rigaBulk) i.next();
                    if (!dettaglio.STATO_CONTABILIZZATO.equals(dettaglio.getStato_cofi()))
                        throw new it.cnr.jada.comp.ApplicationException("Non è possibile scollegare il dettaglio \"" + dettaglio.getDs_riga_fattura() + "\". Questa operazione è permessa solo per dettagli in stato \"" + dettaglio.STATO.get(dettaglio.STATO_CONTABILIZZATO) + "\".");
                    dettaglio.getFattura_attiva().removeFromFattura_attiva_accertamentiHash(dettaglio);
                    dettaglio.setStato_cofi(dettaglio.STATO_INIZIALE);
                    dettaglio.setAccertamento_scadenzario(null);
                    dettaglio.setToBeUpdated();
                }
            } catch (it.cnr.jada.comp.ApplicationException e) {
                try {
                    CRUDVirtualAccertamentoBP.rollbackToSafePoint(context);
                } catch (Throwable t) {
                    throw new it.cnr.jada.comp.ComponentException(t);
                }
                throw e;
            }
        }
    }

    /**
     * Verifica o imposta lo stato della fattura
     */
    protected void setAndVerifyStatusFor(ActionContext context, Fattura_attivaBulk fatturaAttiva) {

        fatturaAttiva.setAndVerifyStatus();
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doRemoveFromCRUDMain_Dettaglio(ActionContext context) {

        try {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            Fattura_attivaBulk fp = (Fattura_attivaBulk) bp.getModel();
            java.util.Vector dettagliInventariatiEliminati = new java.util.Vector();
            for (it.cnr.jada.util.action.SelectionIterator i = bp.getDettaglio().getSelection().iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaBulk dett = (Fattura_attiva_rigaBulk) bp.getDettaglio().getDetails().get(i.nextIndex());
                if (Fattura_attivaBulk.BENEDUREVOLE.equalsIgnoreCase(fp.getTi_causale_emissione()) &&
                        dett.isInventariato()) {
                    dettagliInventariatiEliminati.add(dett);
                }

            }
            bp.getDettaglio().remove(context);
            for (java.util.Iterator i = dettagliInventariatiEliminati.iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaBulk dett = (Fattura_attiva_rigaBulk) i.next();
                AssociazioniInventarioTable associazioni = fp.getAssociazioniInventarioHash();
                if (associazioni != null && !associazioni.isEmpty() && dett instanceof Fattura_attiva_rigaIBulk) {
                    Ass_inv_bene_fatturaBulk ass = fp.getAssociationWithInventarioFor(dett);
                    if (ass != null && !ass.isPerAumentoValore()) {
                        Fattura_attiva_rigaIBulk dettaglio = (Fattura_attiva_rigaIBulk) dett;
                        FatturaAttivaSingolaComponentSession h = (FatturaAttivaSingolaComponentSession) bp.createComponentSession();
                        h.rimuoviDaAssociazioniInventario(
                                context.getUserContext(),
                                dettaglio,
                                ass);
                        fp.removeFromAssociazioniInventarioHash(ass, dettaglio);

                    } else if (ass != null && ass.isPerAumentoValore()) {
                        BuonoCaricoScaricoComponentSession buono_session = (BuonoCaricoScaricoComponentSession) bp.createComponentSession(
                                "CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",
                                BuonoCaricoScaricoComponentSession.class);
                        Buono_carico_scaricoBulk buono = ass.getTest_buono();
                        buono.setToBeDeleted();
                        buono_session.eliminaConBulk(context.getUserContext(), buono);
                        for (java.util.Iterator iter = fp.getFattura_attiva_dettColl().iterator(); iter.hasNext(); ) {
                            Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) iter.next();
                            if (riga.isInventariato() && !fp.getHa_beniColl()) riga.setInventariato(false);
                        }
                        Fattura_attiva_rigaIBulk dettaglio = (Fattura_attiva_rigaIBulk) dett;
                        FatturaAttivaSingolaComponentSession h = (FatturaAttivaSingolaComponentSession) bp.createComponentSession();
                        h.rimuoviDaAssociazioniInventario(
                                context.getUserContext(),
                                dettaglio,
                                ass);
                        fp.removeFromAssociazioniInventarioHash(ass, dettaglio);
                    }
                } else {
                    CarichiInventarioTable carichi = fp.getCarichiInventarioHash();
                    if (carichi != null && !carichi.isEmpty()) {
                        BuonoCaricoScaricoComponentSession h = (BuonoCaricoScaricoComponentSession) bp.createComponentSession(
                                "CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",
                                BuonoCaricoScaricoComponentSession.class);
                        for (java.util.Enumeration e = ((CarichiInventarioTable) carichi.clone()).keys(); e.hasMoreElements(); ) {
                            Buono_carico_scaricoBulk buono = (Buono_carico_scaricoBulk) e.nextElement();
                            buono.setToBeDeleted();
                            h.eliminaConBulk(context.getUserContext(), buono);
                            carichi.remove(buono);
                        }
                        for (java.util.Iterator iter = fp.getFattura_attiva_dettColl().iterator(); iter.hasNext(); ) {
                            Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) iter.next();
                            if (riga.isInventariato() && !fp.getHa_beniColl()) riga.setInventariato(false);
                        }
                    } else
                        bp.getDettaglio().setInventoriedChildDeleted(true);
                }
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce lo sdoppiamento della riga di dettaglio
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doSdoppiaDettaglio(ActionContext context) {
        try {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            fillModel(context);
            Fattura_attiva_rigaIBulk riga = (Fattura_attiva_rigaIBulk) bp.getDettaglio().getModel();
            Forward forward = context.findDefaultForward();
            if (riga == null)
                bp.setErrorMessage("Per procedere, selezionare il dettaglio da sdoppiare!");
            else {
                riga.setIm_riga_sdoppia(Utility.ZERO);
                bp.setDetailDoubling(true);
            }
            return forward;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce lo sdoppiamento della riga di dettaglio
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doConfirmSdoppiaDettaglio(ActionContext context) {
        try {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            fillModel(context);

            bp.sdoppiaDettaglioInAutomatico(context);

            Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();

            if (fattura != null) {
                for (Iterator s = fattura.getFattura_attiva_dettColl().iterator(); s.hasNext(); ) {
                    Fattura_attiva_rigaIBulk riga = (Fattura_attiva_rigaIBulk) s.next();
                    if ((riga.isToBeCreated() || riga.isToBeUpdated()) && riga.getAccertamento_scadenzario() != null)
                        basicDoBringBackOpenAccertamentiWindow(context, riga.getAccertamento_scadenzario());
                }
            }

            bp.setDirty(true);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doAssociaInventario(ActionContext context) {

        try {
            CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);
            fillModel(context);

            Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();

            if ((fattura.getAssociazioniInventarioHash() != null && !fattura.getAssociazioniInventarioHash().isEmpty()) || (fattura.getHa_beniColl()))
                return openConfirm(context, "Alcuni dettagli sono già stati associati. Si vuole continuare?", it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doConfermaAssocia");

            return basicDoAssociaDettagli(context);

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doBeni_coll(ActionContext context) {

        CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP) getBusinessProcess(context);

        Fattura_attivaBulk fattura = (Fattura_attivaBulk) bp.getModel();

        try {
            it.cnr.jada.util.RemoteIterator ri = ((FatturaAttivaSingolaComponentSession) bp.createComponentSession()).selectBeniFor(context.getUserContext(), fattura);
            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
            if (ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: Nessun dato disponibile.");
            }
            SelezionatoreListaBP nbp = (SelezionatoreListaBP) context.createBusinessProcess("Selezionatore");
            nbp.setIterator(context, ri);
            nbp.disableSelection();
            nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(V_ass_inv_bene_fatturaBulk.class));
            HookForward hook = (HookForward) context.findForward("seleziona");
            return context.addBusinessProcess(nbp);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackSearchNomenclatura_combinata(ActionContext context,
                                                           Fattura_attiva_intraBulk fattura_attiva_intra,
                                                           it.cnr.contab.docamm00.intrastat.bulk.Nomenclatura_combinataBulk trovato)
            throws java.rmi.RemoteException {
        try {
            if (trovato != null) {
                fattura_attiva_intra.setNomenclatura_combinata(trovato);
            }
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di ricerca del searchtool "bene_servizio"
     *
     * @param context              L'ActionContext della richiesta
     * @param fattura_riga         L'OggettoBulk padre del searchtool
     * @param beneTrovato          L'OggettoBulk selezionato dall'utente
     * @return Il Forward alla pagina di risposta
     * @throws ComponentException
     */
    public Forward doBringBackSearchBene_servizio(ActionContext context,
                                                  Fattura_attiva_rigaBulk fattura_riga,
                                                  Bene_servizioBulk beneTrovato)
            throws it.cnr.jada.comp.ComponentException {

        try {
            Voce_ivaBulk voceIvaB = null;
            CRUDFatturaAttivaIBP bp = (CRUDFatturaAttivaIBP) context.getBusinessProcess();
            if (beneTrovato != null) {
                fattura_riga.setBene_servizio(beneTrovato);
                fattura_riga.setDs_riga_fattura(beneTrovato.getDs_bene_servizio());
                voceIvaB = beneTrovato.getVoce_iva();

                bp.gestioneBeneBolloVirtuale(context);
            }
            return doBringBackSearchVoce_iva(context, fattura_riga, voceIvaB);

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doGeneraNotaDiCreditoAutomatica(ActionContext context) {
        try {
            fillModel(context);
            CRUDFatturaAttivaIBP bp = (CRUDFatturaAttivaIBP) context.getBusinessProcess();

            Integer esercizioScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());
            Fattura_attiva_IBulk fa = (Fattura_attiva_IBulk) bp.getModel();

            if (bp.isDirty() && bp.getDettaglio().getSelection().size() != 0)
                throw new it.cnr.jada.comp.ApplicationException("Il documento risulta modificato! Per continuare o salvare o deselezionare i dettagli da aggiungere alla nota di credito e ricercarli successivamente.");

            if (fa.isRiportata() && esercizioScrivania.intValue() == fa.getEsercizio().intValue())
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile generare note di credito per fatture riportate!");
            else if (!fa.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fa.getRiportataInScrivania()) && esercizioScrivania.intValue() != fa.getEsercizio().intValue())
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile generare note di credito per fatture non completamente riportate nell'esercizio di scrivania!");
            try {
                java.sql.Timestamp date = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
                int annoSolare = fa.getDateCalendar(date).get(java.util.Calendar.YEAR);
                if (annoSolare != esercizioScrivania.intValue())
                    throw new it.cnr.jada.comp.ApplicationException("Non è possibile inserire note di credito in esercizi non corrispondenti all'anno solare!");
            } catch (javax.ejb.EJBException e) {
                return handleException(context, e);
            }

            return openConfirm(context, "Attenzione! Sarà generata una nota credito a storno totale della fattura. Vuoi continuare?", OptionBP.CONFIRM_YES_NO, "doConfirmGeneraNotaDiCreditoAutomatica");
        } catch (it.cnr.jada.bulk.FillException e) {
            return handleException(context, e);
        } catch (it.cnr.jada.comp.ComponentException e) {
            return handleException(context, e);
        } catch (BusinessProcessException e) {
            return handleException(context, e);
        }
    }

    /**
     * Creo in automatico una nuova nota di credito di storno totale della fattura.
     * Viene eseguito rollback prima di questa operazione per permettere il rilascio della fattura!
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doConfirmGeneraNotaDiCreditoAutomatica(ActionContext context, int option) {
        try {
            if (option == OptionBP.YES_BUTTON) {
                CRUDFatturaAttivaIBP bp = (CRUDFatturaAttivaIBP) context.getBusinessProcess();

                Integer esercizioScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());

                Nota_di_credito_attivaBulk notaDiCredito = bp.generaNotaCreditoAutomatica(context, (Fattura_attiva_IBulk) bp.getModel(), esercizioScrivania);

                bp.setMessage("Operazione effettuata!");
                return doChiusuraNotaDiCredito(context);
            }
            return context.findDefaultForward();
        } catch (it.cnr.jada.comp.ComponentException e) {
            return handleException(context, e);
        } catch (RemoteException e) {
            return handleException(context, e);
        } catch (PersistencyException e) {
            return handleException(context, e);
        } catch (BusinessProcessException e) {
            return handleException(context, e);
        }
    }
}
