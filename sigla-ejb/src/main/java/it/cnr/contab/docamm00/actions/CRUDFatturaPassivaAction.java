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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.EJBException;

import it.cnr.contab.ordmag.ordini.bulk.*;
import it.cnr.contab.util.enumeration.TipoIVA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.compensi00.bp.CRUDCompensoBP;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.docamm00.bp.CRUDFatturaPassivaBP;
import it.cnr.contab.docamm00.bp.CRUDFatturaPassivaIBP;
import it.cnr.contab.docamm00.bp.CRUDNotaDiCreditoBP;
import it.cnr.contab.docamm00.bp.CRUDNotaDiDebitoBP;
import it.cnr.contab.docamm00.bp.ContabilizzaOrdineBP;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoSpesaBP;
import it.cnr.contab.docamm00.bp.RisultatoEliminazioneBP;
import it.cnr.contab.docamm00.bp.TitoloDiCreditoDebitoBP;
import it.cnr.contab.docamm00.docs.bulk.AssociazioniInventarioTable;
import it.cnr.contab.docamm00.docs.bulk.CarichiInventarioTable;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable;
import it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk;
import it.cnr.contab.docamm00.docs.bulk.TrovatoBulk;
import it.cnr.contab.docamm00.ejb.CategoriaGruppoInventComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.contab.docamm00.ejb.VoceIvaComponentSession;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_voceBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession;
import it.cnr.contab.inventario00.bp.AssBeneFatturaBP;
import it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario00.docs.bulk.V_ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario01.bp.CRUDCaricoInventarioBP;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession;
import it.cnr.contab.inventario01.ejb.NumerazioneTempBuonoComponentSession;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
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
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.FormBP;
import it.cnr.jada.util.action.FormField;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.action.Selection;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class CRUDFatturaPassivaAction extends EconomicaAction {
    private transient static final Logger logger = LoggerFactory.getLogger(CRUDFatturaPassivaAction.class);
    public CRUDFatturaPassivaAction() {
        super();
    }

    /**
     * Aggiunge sul modello corrente del target i dettagli selzionati sul documento amministrativo di origine
     * Ogni dettaglio deve essere in stato iniziale.
     */
    private java.util.Vector basicAddDetailsTo(
            ActionContext context,
            CRUDNotaDiCreditoBP target)
            throws BusinessProcessException {

        Nota_di_creditoBulk notaDiCredito = (Nota_di_creditoBulk) target.getModel();
        it.cnr.jada.bulk.BulkList dettagliNdC = notaDiCredito.getFattura_passiva_dettColl();
        if (dettagliNdC == null) {
            dettagliNdC = new it.cnr.jada.bulk.BulkList();
            notaDiCredito.setFattura_passiva_dettColl(dettagliNdC);
        }
        java.util.Vector addedElements = new java.util.Vector();
        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
        if (!notaDiCredito.isEditable() || target.isViewing()) {
            if (!bp.getDettaglio().getSelection().isEmpty())
                target.setErrorMessage("Uno o più dettagli non sono stati aggiunti perchè la nota di credito è già stata pagata o annullata o riportata!");
        } else {
            for (java.util.Iterator i = bp.getDettaglio().getSelection().iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaIBulk dettaglio = (Fattura_passiva_rigaIBulk) bp.getDettaglio().getDetails().get(((Integer) i.next()).intValue());

                if (!dettaglio.STATO_INIZIALE.equals(dettaglio.getStato_cofi())) {
                    Nota_di_credito_rigaBulk dettaglioNdC = new Nota_di_credito_rigaBulk();
                    dettaglioNdC.setNotaDiCredito(notaDiCredito);
                    try {
                        try {
                            if (((FatturaPassivaComponentSession) bp.createComponentSession()).isBeneServizioPerSconto(context.getUserContext(), dettaglio))
                                throw new it.cnr.jada.bulk.FillException("Uno dei dettagli selezionati è un \"bene sconto\". Dettaglio non aggiunto.");
                        } catch (ComponentException exc) {
                            throw new BusinessProcessException(exc);
                        } catch (java.rmi.RemoteException exc) {
                            throw new BusinessProcessException(exc);
                        }
                        dettaglioNdC.copyFrom(dettaglio);
                        if (!isRigaContainedInDetails(dettaglioNdC, dettagliNdC)) {
                            dettaglioNdC.setUser(context.getUserInfo().getUserid());
                            dettagliNdC.add(dettaglioNdC);
                            addedElements.add(dettaglioNdC);
                        }
                    } catch (it.cnr.jada.bulk.FillException e) {
                        target.setErrorMessage("Uno o più dettagli non sono stati aggiunti o per mancanza di disponibiltà o perchè il bene contenuto è di tipo sconto/abbuono!");
                    }
                }
            }
        }

        if (!addedElements.isEmpty() && notaDiCredito.isToBeCreated()) {
            //try {
            //java.math.BigDecimal totNdC = notaDiCredito.getIm_totale_fattura().add(calcolaTotaleSelezionati(addedElements));
            //notaDiCredito.setIm_totale_fattura(totNdC);
            //} catch (it.cnr.jada.comp.ApplicationException e) {
            //notaDiCredito.setIm_totale_fattura(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
            //}
            notaDiCredito.setIm_totale_fattura(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
            notaDiCredito.calcolaTotaleFatturaFornitoreInEur();
        }
        return addedElements;
    }

    /**
     * Aggiunge sul modello corrente del target i dettagli selzionati sul documento amministrativo di origine
     * Ogni dettaglio deve essere in stato iniziale.
     */

    private java.util.Vector basicAddDetailsTo(ActionContext context, CRUDNotaDiDebitoBP target)
            throws BusinessProcessException {

        Nota_di_debitoBulk notaDiDebito = (Nota_di_debitoBulk) target.getModel();
        it.cnr.jada.bulk.BulkList dettagliNdD = notaDiDebito.getFattura_passiva_dettColl();
        if (dettagliNdD == null) {
            dettagliNdD = new it.cnr.jada.bulk.BulkList();
            notaDiDebito.setFattura_passiva_dettColl(dettagliNdD);
        }
        java.util.Vector addedElements = new java.util.Vector();
        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
        if (!notaDiDebito.isEditable() || target.isViewing()) {
            if (!bp.getDettaglio().getSelection().isEmpty())
                target.setErrorMessage("Uno o più dettagli non sono stati aggiunti perchè la nota di debito è già stata pagata o annullata o riportata!");
        } else {
            for (java.util.Iterator i = bp.getDettaglio().getSelection().iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaIBulk dettaglio = (Fattura_passiva_rigaIBulk) bp.getDettaglio().getDetails().get(((Integer) i.next()).intValue());

                if (!dettaglio.STATO_INIZIALE.equals(dettaglio.getStato_cofi())) {
                    Nota_di_debito_rigaBulk dettaglioNdD = new Nota_di_debito_rigaBulk();
                    dettaglioNdD.setNotaDiDebito(notaDiDebito);
                    try {
                        try {
                            if (((FatturaPassivaComponentSession) bp.createComponentSession()).isBeneServizioPerSconto(context.getUserContext(), dettaglio))
                                throw new it.cnr.jada.bulk.FillException("Uno dei dettagli selezionati è un \"bene sconto\". Dettaglio non aggiunto.");
                        } catch (ComponentException exc) {
                            throw new BusinessProcessException(exc);
                        } catch (java.rmi.RemoteException exc) {
                            throw new BusinessProcessException(exc);
                        }
                        dettaglioNdD.copyFrom(dettaglio);
                        if (!isRigaContainedInDetails(dettaglioNdD, dettagliNdD)) {
                            dettaglioNdD.setUser(context.getUserInfo().getUserid());
                            dettagliNdD.add(dettaglioNdD);
                            addedElements.add(dettaglioNdD);
                        }
                    } catch (it.cnr.jada.bulk.FillException e) {
                        target.setErrorMessage("Uno o più dettagli non sono stati aggiunti o per mancanza di disponibiltà o perchè il bene contenuto è di tipo sconto/abbuono!");
                    }
                }
            }
        }

        if (!addedElements.isEmpty()) {
            //try {
            //java.math.BigDecimal totNdD = notaDiDebito.getIm_totale_fattura().add(calcolaTotaleSelezionati(addedElements));
            //notaDiDebito.setIm_totale_fattura(totNdD);
            //} catch (it.cnr.jada.comp.ApplicationException e) {
            //notaDiDebito.setIm_totale_fattura(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
            //}
            notaDiDebito.setIm_totale_fattura(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
            notaDiDebito.calcolaTotaleFatturaFornitoreInEur();
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
            ActionContext context,
            Fattura_passiva_rigaBulk riga,
            java.math.BigDecimal vecchioTotale)
            throws it.cnr.jada.bulk.FillException {

        Fattura_passiva_rigaIBulk rigaFP = (Fattura_passiva_rigaIBulk) riga;

        if (rigaFP.getQuantita() == null) rigaFP.setQuantita(new java.math.BigDecimal(1));
        if (rigaFP.getPrezzo_unitario() == null) rigaFP.setPrezzo_unitario(new java.math.BigDecimal(0));
        if (rigaFP.getIm_iva() == null)
            rigaFP.setIm_iva(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));

        java.math.BigDecimal nuovoTotale = rigaFP.getIm_imponibile().add(rigaFP.getIm_iva()).add(rigaFP.getIm_totale_addebiti());
        java.util.HashMap storni = rigaFP.getFattura_passivaI().getStorniHashMap();
        if (storni == null ||
                storni.get(rigaFP) == null ||
                ((java.util.List) storni.get(rigaFP)).isEmpty())
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
     * Apre il pannello delle note di credito sul modello passato 'notaDiCebito' e nel
     * caso in cui alcune righe sono selezionate in fattura, cerca di aggiungerle come
     * dettagli della nota di credito stessa ('basicAddDetailsTo').
     */

    private Forward basicDoApriNotaDiCredito(
            ActionContext context,
            Nota_di_creditoBulk notaDiCredito)
            throws it.cnr.jada.comp.ApplicationException {

        try {
            CRUDFatturaPassivaIBP bp = (CRUDFatturaPassivaIBP) context.getBusinessProcess();
            if (bp.isDirty() && bp.getDettaglio().getSelection().size() != 0)
                throw new it.cnr.jada.comp.ApplicationException("Il documento risulta modificato! Per continuare o salvare o deselezionare i dettagli da aggiungere alla nota di credito e ricercarli successivamente.");

            //Assolutamente necessario eseguirlo DOPO i controlli
            bp.rollbackUserTransaction();

            String status = bp.isEditing() ? "M" : "V";
            CRUDNotaDiCreditoBP notaBp = (CRUDNotaDiCreditoBP) context.createBusinessProcess("CRUDNotaDiCreditoBP", new Object[]{status + "Tn"});

            if (notaDiCredito.getCrudStatus() == notaDiCredito.NORMAL) {
                notaBp.edit(context, notaDiCredito);
                notaDiCredito = (Nota_di_creditoBulk) notaBp.getModel();
            } else {
                notaDiCredito.setDt_termine_creazione_docamm(((Fattura_passivaBulk) notaBp.getModel()).getDt_termine_creazione_docamm());
                notaBp.setModel(context, notaDiCredito);
            }

            basicAddDetailsTo(context, notaBp);

            notaBp.setAutoGenerated(Boolean.TRUE);

            context.addHookForward("chiusuraNotaDiCredito", this, "doChiusuraNotaDiCredito");

            return context.addBusinessProcess(notaBp);

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
            Nota_di_debitoBulk notaDiDebito)
            throws it.cnr.jada.comp.ApplicationException {

        try {
            CRUDFatturaPassivaIBP bp = (CRUDFatturaPassivaIBP) context.getBusinessProcess();
            if (bp.isDirty() && bp.getDettaglio().getSelection().size() != 0)
                throw new it.cnr.jada.comp.ApplicationException("Il documento risulta modificato! Per continuare o salvare o deselezionare i dettagli da aggiungere alla nota di debito e ricercarli successivamente.");

            //Assolutamente necessario eseguirlo DOPO i controlli
            bp.rollbackUserTransaction();

            String status = bp.isEditing() ? "M" : "V";
            CRUDNotaDiDebitoBP notaBp = (CRUDNotaDiDebitoBP) context.createBusinessProcess("CRUDNotaDiDebitoBP", new Object[]{status + "Tn"});

            if (notaDiDebito.getCrudStatus() == notaDiDebito.NORMAL) {
                notaBp.edit(context, notaDiDebito);
                notaDiDebito = (Nota_di_debitoBulk) notaBp.getModel();
            } else {
                notaDiDebito.setDt_termine_creazione_docamm(((Fattura_passivaBulk) notaBp.getModel()).getDt_termine_creazione_docamm());
                notaBp.setModel(context, notaDiDebito);
            }

            basicAddDetailsTo(context, notaBp);

            notaBp.setAutoGenerated(Boolean.TRUE);

            context.addHookForward("chiusuraNotaDiDebito", this, "doChiusuraNotaDiDebito");
            return context.addBusinessProcess(notaBp);

        } catch (BusinessProcessException e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce l'associazione della scadenza riportata con il documento amm.
     * Se non esiste l'aggiunge, altrimenti sincronizza l'istanza già presente
     *
     * @param context   L'ActionContext della richiesta
     * @param newObblig scadenza selezionata dall'utente con riporta
     * @return Il Forward alla pagina di risposta
     */
    protected Forward basicDoBringBackOpenObbligazioniWindow(
            ActionContext context,
            Obbligazione_scadenzarioBulk newObblig) {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
        try {
            TerzoBulk creditore = newObblig.getObbligazione().getCreditore();
            Fattura_passivaBulk fp = (Fattura_passivaBulk) bp.getModel();
            if (!fp.getFornitore().equalsByPrimaryKey(creditore) &&
                    !AnagraficoBulk.DIVERSI.equalsIgnoreCase(creditore.getAnagrafico().getTi_entita()))
                throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata deve appartenere ad un'obbligazione che ha come creditore il fornitore della fattura!");
/* 	Rospuc 15/01/2015 Controllo SOSPESO  compatibilità dell'obbligazione con il titolo capitolo selezionato
       SOSPESO PER ESERCIZIO 2015	*/
            if (fp instanceof Fattura_passiva_IBulk) {
                java.util.List dettagliDaContabilizzare = (java.util.List) fp.getObbligazioniHash().get(newObblig);
                if (dettagliDaContabilizzare != null && !dettagliDaContabilizzare.isEmpty()) {
                    List titoloCapitoloValidolist = controllaSelezionePerTitoloCapitoloLista(context, dettagliDaContabilizzare.iterator());
                    Elemento_voceBulk titoloCapitoloObbligazione = newObblig.getObbligazione().getElemento_voce();
                    //Controllo la compatibilità dell'obbligazione con il titolo capitolo selezionato
                    Boolean compatibile = null;
                    if (titoloCapitoloValidolist != null && titoloCapitoloValidolist.size() != 0)
                        for (Iterator i = titoloCapitoloValidolist.iterator(); (i.hasNext() && (compatibile == null || !compatibile)); ) {
                            Categoria_gruppo_voceBulk bulk = (Categoria_gruppo_voceBulk) i.next();
                            if (bulk.getCd_elemento_voce().compareTo(titoloCapitoloObbligazione.getCd_elemento_voce()) == 0)
                                compatibile = new Boolean(true);
                            else
                                compatibile = new Boolean(false);
                        }
                    if (compatibile != null && !compatibile)
                        throw new it.cnr.jada.comp.ApplicationException("L'impegno selezionato non è compatibile con il titolo capitolo della categoria");//+ titoloCapitoloValido.getCd_ds_elemento_voce() + "\"!");
                }
            }

            Obbligazione_scadenzarioBulk obbligazione = (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel();
            if (obbligazione != null) {
                resyncObbligazione(context, obbligazione, newObblig);
            } else {
                basicDoContabilizza(context, newObblig, null);
            }
        } catch (Throwable t) {
            it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi defSaldiBulk = bp.getDefferedUpdateSaldiParentBP().getDefferedUpdateSaldiBulk();
            if (newObblig.getObbligazione().getPg_ver_rec().equals((Long) newObblig.getObbligazione().getSaldiInfo().get("pg_ver_rec")))
                defSaldiBulk.removeFromDefferredSaldi(newObblig.getObbligazione());
            try {
                CRUDVirtualObbligazioneBP.rollbackToSafePoint(context);
            } catch (Exception e) {
                handleException(context, e);
            }
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * Viene richiamato nel momento in cui si seleziona una valuta dal combo Valuta nella
     * testata della fattura.
     * Richiama a sua volta il metodo cercaCambio dalla component.
     */
    private void basicDoCalcolaTotaleFatturaFornitoreInEur(Fattura_passivaBulk fattura) {

        if (fattura != null)
            fattura.calcolaTotaleFatturaFornitoreInEur();
    }

    /**
     * Calcola i totali del dettaglio 'riga' e se necessario aggiorna l'importo disp
     * per le note di credito relativo al dettaglio stesso
     */

    protected void basicDoCalcolaTotaliDiRiga(
            ActionContext context,
            Fattura_passiva_rigaBulk riga,
            java.math.BigDecimal vecchioTotale)
            throws it.cnr.jada.bulk.FillException {

        if (riga.getQuantita() == null) riga.setQuantita(new java.math.BigDecimal(1));
        if (riga.getPrezzo_unitario() == null) riga.setPrezzo_unitario(new java.math.BigDecimal(0));
        if (riga.getIm_iva() == null)
            riga.setIm_iva(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));

        riga.setFl_iva_forzata(Boolean.FALSE);
        riga.calcolaCampiDiRiga();
        if (riga instanceof Fattura_passiva_rigaIBulk)
            basicCalcolaImportoDisponibileNC(context, (Fattura_passiva_rigaIBulk) riga, vecchioTotale);
        doSelectObbligazioni(context);
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
    protected Forward basicDoCerca(ActionContext context) throws java.rmi.RemoteException, InstantiationException, javax.ejb.RemoveException {
        try {
            fillModel(context);
            CRUDBP bp = getBusinessProcess(context);
            OggettoBulk model = (OggettoBulk) bp.getModel();
            it.cnr.jada.util.RemoteIterator ri = bp.find(context, null, model);
            if (ri == null || ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                bp.setMessage("La ricerca non ha fornito alcun risultato.");
                return context.findDefaultForward();
                //} else if (ri.countElements() == 1) {
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
     * Gestisce la contabilizzazione dei dettagli selezionati, cioè crea
     * l'associazione della scadenza riportata con i dettagli selezionati.
     * Se non esiste la scadenza viene aggiunta, altrimenti aggiunge alla
     * scadenza esistente il dettaglio risincronizzando infine le istanze
     * e ricalcolando i totali di scadenza
     */

    private void basicDoContabilizza(
            ActionContext context,
            Obbligazione_scadenzarioBulk obbligazione,
            java.util.List selectedModels)
            throws it.cnr.jada.comp.ComponentException {

        if (obbligazione != null && selectedModels != null) {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();

            java.util.Vector dettagliDaContabilizzare = new java.util.Vector();
            dettagliDaContabilizzare.addAll(selectedModels);
            ObbligazioniTable obbs = ((Fattura_passivaBulk) bp.getModel()).getObbligazioniHash();
            if (obbs != null) {
                java.util.List dettagliContabilizzati = (java.util.List) obbs.get(obbligazione);
                if (dettagliContabilizzati != null && !dettagliContabilizzati.isEmpty())
                    dettagliDaContabilizzare.addAll(dettagliContabilizzati);
            }
            //Elemento_voceBulk titoloCapitoloValido = controllaSelezionePerTitoloCapitolo(context, dettagliDaContabilizzare.iterator());
            Elemento_voceBulk titoloCapitoloObbligazione = obbligazione.getObbligazione().getElemento_voce();

            // MI - controllo se l'obbligazione ha voce coerente con il tipo di bene
            if (selectedModels != null && !selectedModels.isEmpty()) {
                for (java.util.Iterator i = selectedModels.iterator(); i.hasNext(); ) {
                    Object rigaObj = i.next();
                    if (rigaObj instanceof Fattura_passiva_rigaIBulk) {
                        Fattura_passiva_rigaIBulk fpRiga = (Fattura_passiva_rigaIBulk) rigaObj;
                        if (!titoloCapitoloObbligazione.getFl_inv_beni_patr().equals(fpRiga.getBene_servizio().getFl_gestione_inventario())) {
                            final Optional<Boolean> flGestioneInventario = Optional.ofNullable(fpRiga)
                                    .flatMap(fattura_passiva_rigaIBulk -> Optional.ofNullable(fattura_passiva_rigaIBulk.getBene_servizio()))
                                    .flatMap(bene_servizioBulk -> Optional.ofNullable(bene_servizioBulk.getFl_gestione_inventario()));
                            if (flGestioneInventario.isPresent() && flGestioneInventario.get())
                                throw new it.cnr.jada.comp.ApplicationException("Il titolo capitolo dell'impegno selezionato non è utilizzabile per beni patrimoniali da inventariare!");
                            else
                                throw new it.cnr.jada.comp.ApplicationException("Il titolo capitolo dell'impegno selezionato non è utilizzabile per beni/servizi da non inventariare!");
                        }
                    }
                }
            }
            try {
                List titoloCapitoloValidolist;
                if (dettagliDaContabilizzare != null && !dettagliDaContabilizzare.isEmpty()) {
                    titoloCapitoloValidolist = controllaSelezionePerTitoloCapitoloLista(context, dettagliDaContabilizzare.iterator());

                    //Controllo la compatibilità dell'obbligazione con il titolo capitolo selezionato
                    Boolean compatibile = null;
                    if (titoloCapitoloValidolist != null && titoloCapitoloValidolist.size() != 0)
                        for (Iterator i = titoloCapitoloValidolist.iterator(); (i.hasNext() && (compatibile == null || !compatibile)); ) {
                            Categoria_gruppo_voceBulk bulk = (Categoria_gruppo_voceBulk) i.next();
                            if (bulk.getCd_elemento_voce().compareTo(titoloCapitoloObbligazione.getCd_elemento_voce()) == 0)
                                compatibile = new Boolean(true);
                            else
                                compatibile = new Boolean(false);
                        }
                    if (compatibile != null && !compatibile)
                        throw new it.cnr.jada.comp.ApplicationException("L'impegno selezionato non è compatibile con il titolo capitolo della categoria");//+ titoloCapitoloValido.getCd_ds_elemento_voce() + "\"!");
                }
            } catch (PersistencyException e1) {
                bp.handleException(e1);
            } catch (IntrospectionException e1) {
                bp.handleException(e1);
            } catch (RemoteException e1) {
                bp.handleException(e1);
            } catch (BusinessProcessException e1) {
                bp.handleException(e1);
            }
        /*	Rospuc 15/01/2015 Controllo SOSPESO  compatibilità dell'obbligazione con il titolo capitolo selezionato
		    SOSPESO PER ESERCIZIO 2015
		//Controllo la compatibilità dell'obbligazione con il titolo capitolo selezionato
		if (titoloCapitoloValido != null &&
			!(titoloCapitoloObbligazione.getCd_elemento_voce().startsWith(titoloCapitoloValido.getCd_elemento_voce()) ||
			titoloCapitoloValido.getCd_elemento_voce().startsWith(titoloCapitoloObbligazione.getCd_elemento_voce())))
			throw new it.cnr.jada.comp.ApplicationException("L'impegno selezionato non è compatibile con il titolo capitolo \"" + titoloCapitoloValido.getCd_ds_elemento_voce() + "\"!");
		 */
            try {
                FatturaPassivaComponentSession h = (FatturaPassivaComponentSession) bp.createComponentSession();
                Fattura_passivaBulk fattura = h.contabilizzaDettagliSelezionati(
                        context.getUserContext(),
                        (Fattura_passivaBulk) bp.getModel(),
                        selectedModels,
                        obbligazione);
                try {
                    bp.setModel(context, fattura);
                    bp.setDirty(true);

                    doCalcolaTotalePerObbligazione(context, obbligazione);
                	if (!(fattura.isEstera() || fattura.isSanMarinoConIVA() || fattura.isSanMarinoSenzaIVA()) && 
                		!(obbligazione != null && obbligazione.getObbligazione() != null && obbligazione.getObbligazione().getContratto() != null && obbligazione.getObbligazione().getContratto().getCig() != null
                				&& obbligazione.getObbligazione().getContratto().getCig().getCdCig() != null)) {
                        bp.setMessage("L'impegno selezionato non ha il CIG. Indicare il CIG.");
                	}
                } catch (BusinessProcessException e) {
                }
            } catch (java.rmi.RemoteException e) {
                bp.handleException(e);
            } catch (BusinessProcessException e) {
                bp.handleException(e);
            }
        }
    }

    /**
     * Creo una nuova istanza di nota di credito e ne richiedo l'apertura con il
     * metodo 'basicDoApriNotaDiCredito'
     */

    private Forward basicDoGeneraNotaDiCredito(ActionContext context)
            throws ComponentException, BusinessProcessException {

        CRUDFatturaPassivaIBP bp = (CRUDFatturaPassivaIBP) context.getBusinessProcess();

        Integer esercizioScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());
        Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk) bp.getModel();

        if (fp.isRiportata() && esercizioScrivania.intValue() == fp.getEsercizio().intValue())
            throw new it.cnr.jada.comp.ApplicationException("Non è possibile generare note di credito per fatture riportate!");

        // Gennaro Borriello - (02/11/2004 16.48.21)
        // 	Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato
        //	riportato DA UN ES. PRECEDENTE a quello di scrivania.
        // RP 16/03/2010 Da commentare per generare NC di fatture di anni precedenti
        //else if (!fp.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fp.getRiportataInScrivania()) && esercizioScrivania.intValue() != fp.getEsercizio().intValue())
        //throw new it.cnr.jada.comp.ApplicationException("Non è possibile generare note di credito per fatture non completamente riportate nell'esercizio di scrivania!");

        //if (fp.isRiportata() && !fp.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fp.getRiportata()))
        //throw new it.cnr.jada.comp.ApplicationException("Non è possibile generare note di credito per fatture non riportate completamente!");
        try {
            java.sql.Timestamp date = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
            int annoSolare = fp.getDateCalendar(date).get(java.util.Calendar.YEAR);
            if (annoSolare != esercizioScrivania.intValue())
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile inserire note di credito in esercizi non corrispondenti all'anno solare!");
        } catch (javax.ejb.EJBException e) {
            return handleException(context, e);
        }

        //Rimanadato a basicDoApriNotaDiCredito
        //bp.rollbackUserTransaction();

        Nota_di_creditoBulk notaDiCredito = new Nota_di_creditoBulk(
                fp,
                esercizioScrivania);
        creaEsercizioPerFatturaFornitore(context, notaDiCredito);

        return basicDoApriNotaDiCredito(context, notaDiCredito);
    }

    /**
     * Creo una nuova istanza di nota di debito e ne richiedo l'apertura con il
     * metodo 'basicDoApriNotaDiDebito'
     */

    private Forward basicDoGeneraNotaDiDebito(ActionContext context)
            throws ComponentException, BusinessProcessException {

        CRUDFatturaPassivaIBP bp = (CRUDFatturaPassivaIBP) context.getBusinessProcess();

        Integer esercizioScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());
        Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk) bp.getModel();

        if (fp.isRiportata() && esercizioScrivania.intValue() == fp.getEsercizio().intValue())
            throw new it.cnr.jada.comp.ApplicationException("Non è possibile generare note di debito per fatture riportate!");

        // Gennaro Borriello - (02/11/2004 16.48.21)
        // 	Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato
        //	riportato DA UN ES. PRECEDENTE a quello di scrivania.
//	else if (!fp.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fp.getRiportataInScrivania()) && esercizioScrivania.intValue() != fp.getEsercizio().intValue())
//		throw new it.cnr.jada.comp.ApplicationException("Non è possibile generare note di credito per fatture non completamente riportate nell'esercizio di scrivania!");

        //if (fp.isRiportata() && !fp.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fp.getRiportata()))
        //throw new it.cnr.jada.comp.ApplicationException("Non è possibile generare note di debito per fatture non riportate completamente!");
        try {
            java.sql.Timestamp date = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
            int annoSolare = fp.getDateCalendar(date).get(java.util.Calendar.YEAR);
            if (annoSolare != esercizioScrivania.intValue())
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile inserire note di debito in esercizi non corrispondenti all'anno solare!");
        } catch (javax.ejb.EJBException e) {
            return handleException(context, e);
        }

        //Rimanadato a basicDoApriNotaDiDebito
        //bp.rollbackUserTransaction();

        Nota_di_debitoBulk notaDiDebito = new Nota_di_debitoBulk(
                fp,
                esercizioScrivania);
        creaEsercizioPerFatturaFornitore(context, notaDiDebito);

        return basicDoApriNotaDiDebito(context, notaDiDebito);
    }

    /**
     * creo una nuova istanza di buono di carico; lo inizializzo, aggiungo i dettagli
     * selezionati e ne richiedo l'apertura. Se esistevano già dei buoni di carico, li
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
            ComponentException,
            java.rmi.RemoteException {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
        Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
        CarichiInventarioTable carichi = fattura.getCarichiInventarioHash();
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
            for (java.util.Iterator i = fattura.getFattura_passiva_dettColl().iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) i.next();
                if (riga.isInventariato() && !fattura.getHa_beniColl()) riga.setInventariato(false);
            }
        }

        java.util.List dettagliDaInventariare = getDettagliDaInventariare(context, bp.getDettaglio().getDetails().iterator());
        if (dettagliDaInventariare != null && !dettagliDaInventariare.isEmpty()) {
            CRUDCaricoInventarioBP ibp = (CRUDCaricoInventarioBP) context.createBusinessProcess("CRUDCaricoInventarioBP", new Object[]{"MRSWTh"});
            Buono_carico_scaricoBulk bcs = new Buono_carico_scaricoBulk();
            ibp.setBy_fattura(true);
            bcs.setTi_documento(Buono_carico_scaricoBulk.CARICO);
            bcs.setByFattura(Boolean.TRUE);
            bcs.initializeForInsert(ibp, context);

            bcs = (it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk) ibp.createComponentSession().inizializzaBulkPerInserimento(context.getUserContext(), bcs);
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
    }

    public Forward basicDoAssociaDettagli(ActionContext context)
            throws BusinessProcessException,
            ComponentException,
            java.rmi.RemoteException, PersistencyException, IntrospectionException, EJBException {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
        Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();


        for (java.util.Iterator i = fattura.getFattura_passiva_dettColl().iterator(); i.hasNext(); ) {
            Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) i.next();
            AssociazioniInventarioTable associazioni = fattura.getAssociazioniInventarioHash();
            if ((associazioni != null && !associazioni.isEmpty()) || (riga.getCrudStatus() != OggettoBulk.TO_BE_CREATED)) {
                Ass_inv_bene_fatturaBulk ass = fattura.getAssociationWithInventarioFor(riga);
                if ((ass != null) && !ass.isPerAumentoValore()) {
					/*
					R.p. Non elimino le associazioni gia' fatte
					Fattura_passiva_rigaIBulk dettaglio = (Fattura_passiva_rigaIBulk)riga;
					FatturaPassivaComponentSession h = (FatturaPassivaComponentSession)bp.createComponentSession();
					h.rimuoviDaAssociazioniInventario(
												context.getUserContext(),
												dettaglio,
												ass);
					fattura.removeFromAssociazioniInventarioHash(ass, dettaglio);*/

                    if (riga.isInventariato()) riga.setInventariato(false);
                } else if (riga.getCrudStatus() != OggettoBulk.TO_BE_CREATED && (riga.getBene_servizio().getFl_gestione_inventario().booleanValue())) {
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
                    if (dettagli.get(0) instanceof Fattura_passiva_rigaIBulk) {
                        Fattura_passiva_rigaIBulk riga = (Fattura_passiva_rigaIBulk) i.next();
                        if (riga.getBene_servizio().getFl_gestione_inventario().booleanValue())
                            dettagliDaInventariare.add(riga);
                    } else if (dettagli.get(0) instanceof Nota_di_credito_rigaBulk) {
                        Nota_di_credito_rigaBulk riga = (Nota_di_credito_rigaBulk) i.next();
                        if (riga.getBene_servizio().getFl_gestione_inventario().booleanValue())
                            dettagliDaInventariare.add(riga);
                    } else if (dettagli.get(0) instanceof Nota_di_debito_rigaBulk) {
                        Nota_di_debito_rigaBulk riga = (Nota_di_debito_rigaBulk) i.next();
                        if (riga.getBene_servizio().getFl_gestione_inventario().booleanValue())
                            dettagliDaInventariare.add(riga);
                    }
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
     * Viene richiamato nel momento in cui si seleziona una valuta dal combo Valuta nella
     * testata della fattura.
     * Richiama a sua volta il metodo cercaCambio dalla component.
     */
    protected void basicDoOnIstituzionaleCommercialeChange(ActionContext context, Fattura_passivaBulk fattura)
            throws it.cnr.jada.comp.ComponentException {

        try {
            FatturaPassivaComponentSession h = (FatturaPassivaComponentSession) ((CRUDFatturaPassivaBP) getBusinessProcess(context)).createComponentSession();
            java.util.Vector sezionali = h.estraeSezionali(context.getUserContext(), fattura);
            fattura.setSezionali(sezionali);
            if (!getBusinessProcess(context).isSearching() &&
                    sezionali != null && !sezionali.isEmpty())
            	fattura.setTipo_sezionale((Tipo_sezionaleBulk) sezionali.firstElement());
            else
                fattura.setTipo_sezionale(null);

            if (fattura.getFattura_passiva_dettColl() != null) {
                Iterator dettagli = fattura.getFattura_passiva_dettColl().iterator();
                while (dettagli.hasNext()) {
                    Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) dettagli.next();

                    if (riga.getTi_istituz_commerc() != null && fattura.getTi_istituz_commerc() != null && !riga.getTi_istituz_commerc().equals(fattura.getTi_istituz_commerc())) {
                        riga.setTi_istituz_commerc(fattura.getTi_istituz_commerc());
                        riga.setToBeUpdated();
                    }
                }
            }
		/*commentato perchè da problemi con la fatturazione elettronica che trova già il fornitore caricato
		if (fattura.getFornitore() != null && fattura.getFornitore().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL) {
			doBlankSearchFornitore(context, fattura);
			((it.cnr.jada.util.action.CRUDBP)context.getBusinessProcess()).setMessage("Attenzione: il fornitore non è più valido. Selezionare un altro fornitore!");
		}*/
        } catch (Throwable t) {
            throw new it.cnr.jada.comp.ComponentException(t);
        }
    }

    /**
     * Prepara e apre il filtro di ricerca per le obbligazioni valide al collegamento
     * con documenti amministrativi.
     */

    private Forward basicDoRicercaObbligazione(
            ActionContext context,
            Fattura_passivaBulk fatturaPassiva,
            java.util.List models, boolean notaCreditoPerOrdini) {

        try {
            if (!fatturaPassiva.isDaOrdini()){
                controllaQuadraturaConti(context, fatturaPassiva);
            }

            Filtro_ricerca_obbligazioniVBulk filtro = new Filtro_ricerca_obbligazioniVBulk();
            filtro.setData_scadenziario(fatturaPassiva.getDt_scadenza());
            filtro.setFornitore(fatturaPassiva.getFornitore());
            filtro.setIm_importo(calcolaTotaleSelezionati(models, fatturaPassiva.quadraturaInDeroga()));
            filtro.setCd_unita_organizzativa(fatturaPassiva.getCd_uo_origine());
            filtro.setHasDocumentoCompetenzaCOGEInAnnoPrecedente(fatturaPassiva.hasCompetenzaCOGEInAnnoPrecedente());
            filtro.setHasDocumentoCompetenzaCOGESoloInAnnoCorrente(
                    !fatturaPassiva.hasCompetenzaCOGEInAnnoPrecedente() &&
                            fatturaPassiva.getDateCalendar(fatturaPassiva.getDt_a_competenza_coge()).get(java.util.Calendar.YEAR) == fatturaPassiva.getEsercizio().intValue());
            if (filtro.getData_scadenziario() == null)
                filtro.setFl_data_scadenziario(Boolean.FALSE);
            if (models == null || models.isEmpty())
                filtro.setFl_importo(Boolean.FALSE);
            else {
                Fattura_passiva_rigaBulk firstRow = (Fattura_passiva_rigaBulk) models.get(0);

                //	Rospuc 15/01/2015 Controllo SOSPESO  compatibilità dell'obbligazione con il titolo capitolo selezionato
                //SOSPESO PER ESERCIZIO 2015
//			if (firstRow.getBene_servizio().getFl_gestione_inventario().booleanValue()) {
//				Elemento_voceBulk ev = getElementoVoce(context, firstRow.getBene_servizio().getCategoria_gruppo());
//				filtro.setElemento_voce(ev);
//			}
            }

            BulkBP robp = (BulkBP) context.getUserInfo().createBusinessProcess(context, "RicercaObbligazioniBP", new Object[]{"MRSWTh"});
            robp.setModel(context, filtro);
            if (notaCreditoPerOrdini){
                context.addHookForward("bringback", this, "doContabilizzaOrdiniNotaCredito");
            } else {
                context.addHookForward("bringback", this, "doContabilizza");
            }
            HookForward hook = (HookForward) context.findForward("bringback");
            return context.addBusinessProcess(robp);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Prepara e apre la ricerca per l'evasione dell'ordine
     */
    private Forward basicDoRicercaEvasioneOrdine(ActionContext context, Fattura_passivaBulk fatturaPassiva, List<Fattura_passiva_rigaBulk> models, boolean manually) {
        return context.findDefaultForward();
    }
    /**
     * Contabilizza i dettagli selezionati previo controllo della selezione
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doContabilizzaOrdine(ActionContext context) {
        HookForward caller = (HookForward) context.getCaller();
        final Supplier<Stream<EvasioneOrdineRigaBulk>> selectedElements = () ->
                Optional.ofNullable(caller.getParameter("selectedElements"))
                .filter(List.class::isInstance)
                .map(List.class::cast)
                .filter(list -> !list.isEmpty())
                .map(list ->
                        list.stream()
                                .filter(EvasioneOrdineRigaBulk.class::isInstance)
                                .map(EvasioneOrdineRigaBulk.class::cast)
                ).orElse(Stream.empty());
        Optional<CRUDFatturaPassivaBP> crudFatturaPassivaBP = Optional.ofNullable(context.getBusinessProcess())
                .filter(CRUDFatturaPassivaBP.class::isInstance)
                .map(CRUDFatturaPassivaBP.class::cast);
        if (!crudFatturaPassivaBP.isPresent())
            return context.findDefaultForward();

        try {
                selectedElements.get().forEach(evasioneOrdineRigaBulk -> {
                    try {
                        crudFatturaPassivaBP.get().associaOrdineFattura(context, evasioneOrdineRigaBulk);
                    } catch (BusinessProcessException e) {
                        throw new DetailedRuntimeException(e);
                    }
                });
            return context.findDefaultForward();
        } catch (Throwable _ex) {
            return handleException(context, _ex);
        }

    }
    /**
     * Riporta il documento amministrativo selezionato dall'utente (caso delle cancellazioni
     * di note di debito e credito)
     */
    protected Forward basicDoRiportaSelezione(ActionContext context, it.cnr.jada.bulk.OggettoBulk selezione) throws java.rmi.RemoteException {

        try {
            if (selezione != null) {
                CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
                bp.edit(context, selezione);
                selezione = bp.getModel();
                //if (!Fattura_passivaBulk.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(((Fattura_passivaBulk)selezione).getRiportata()) ||
                //((Fattura_passivaBulk)selezione).isCongelata())
                //throw new it.cnr.jada.comp.ApplicationException("Il documento amministrativo risulta riportato o congelato! Operazione annullata.");

                // Borriello: integrazione Err. CNR 775
                Integer esScriv = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());

                if ((((Fattura_passivaBulk) selezione).getEsercizio().compareTo(esScriv) == 0) && ((Fattura_passivaBulk) selezione).isRiportata()) {
                    throw new it.cnr.jada.comp.ApplicationException("Il documento amministrativo risulta (parzialmente) riportato! Operazione annullata.");
                }

                // Gennaro Borriello - (09/11/2004 18.08.57)
                //	Nuova gestione dello stato <code>getRiportata()</code>
                if ((((Fattura_passivaBulk) selezione).getEsercizio().compareTo(esScriv) != 0) && (!Fattura_passivaBulk.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(((Fattura_passivaBulk) selezione).getRiportataInScrivania()))) {
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
     * Calcola i totali per l'eelnco dei dettagli selezionati. Nel caso di fattura
     * vengono tolti gli importi di storno e aggiunti gli addebiti
     *
     * @param selectedModels
     * @return
     * @throws ApplicationException
     */
    protected java.math.BigDecimal calcolaTotaleSelezionati(
            java.util.List selectedModels,
            boolean escludiIVA)
            throws it.cnr.jada.comp.ApplicationException {

        java.math.BigDecimal importo = new java.math.BigDecimal(0);
        boolean escludiIVAInt = false;
        boolean escludiIVAOld = escludiIVA;
        if (selectedModels != null) {
            for (java.util.Iterator i = selectedModels.iterator(); i.hasNext(); ) {
                escludiIVA = escludiIVAOld;
                Fattura_passiva_rigaBulk rigaSelected = (Fattura_passiva_rigaBulk) i.next();
                boolean autofattura = Optional.ofNullable(rigaSelected)
                        .map(fattura_passiva_rigaBulk -> Optional.ofNullable(fattura_passiva_rigaBulk.getVoce_iva()).orElse(new Voce_ivaBulk()))
                        .map(voce_ivaBulk -> voce_ivaBulk.getFl_autofattura()).orElse(false);
                //RP 20/03/2015
                if (!escludiIVA && autofattura)
                    escludiIVAInt = true;
                else if (!escludiIVA && !autofattura)
                    escludiIVAInt = false;
                if (escludiIVAInt)
                    escludiIVA = escludiIVAInt;

                // fine RP 20/03/2015
                java.math.BigDecimal imTotale = (escludiIVA) ?
                        rigaSelected.getIm_imponibile() :
                        rigaSelected.getIm_imponibile().add(rigaSelected.getIm_iva());
                java.math.BigDecimal imStornati = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
                java.math.BigDecimal imAddebitati = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
                if (rigaSelected instanceof Fattura_passiva_rigaIBulk) {
                    Fattura_passiva_rigaIBulk dettaglioFatturaPassiva = (Fattura_passiva_rigaIBulk) rigaSelected;
                    java.math.BigDecimal impStorniDiRiga = (escludiIVA) ?
                            calcolaTotaleSelezionati((Vector) dettaglioFatturaPassiva.getFattura_passivaI().getStorniHashMap().get(dettaglioFatturaPassiva), true) :
                            dettaglioFatturaPassiva.getIm_totale_storni();
                    imStornati = imStornati.add(impStorniDiRiga);
                    java.math.BigDecimal impAddebitiDiRiga = (escludiIVA) ?
                            calcolaTotaleSelezionati((Vector) dettaglioFatturaPassiva.getFattura_passivaI().getAddebitiHashMap().get(dettaglioFatturaPassiva), true) :
                            dettaglioFatturaPassiva.getIm_totale_addebiti();
                    imAddebitati = imAddebitati.add(impAddebitiDiRiga);
                }
                importo = importo.add(imTotale.add(imAddebitati).subtract(imStornati));
            }
        }

        importo = importo.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        return importo;
    }

    /**
     * Viene richiamato nel momento in cui si seleziona una valuta dal combo Valuta nella
     * testata della fattura.
     * Richiama a sua volta il metodo cercaCambio dalla component.
     */
    private Voce_ivaBulk caricaVoceIVADefault(ActionContext context)
            throws it.cnr.jada.comp.ComponentException {

        try {
            VoceIvaComponentSession h = (VoceIvaComponentSession)
                    context.getBusinessProcess().createComponentSession("CNRDOCAMM00_EJB_VoceIvaComponentSession", VoceIvaComponentSession.class);
            return h.caricaVoceIvaDefault(context.getUserContext());
        } catch (Throwable t) {
            throw new it.cnr.jada.comp.ComponentException(t);
        }
    }

    /**
     * Gestisce un comando di cancellazione o annullamento.
     * Nel caso di ndc o ndd richiede la gestione della quadratura delle scadenze di doc cont
     * tramite il gestore 'RisultatoEleminazioneBP'
     */
    public Forward confermaEliminazione(ActionContext context, int option) {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
        try {
            if (it.cnr.jada.util.action.OptionBP.YES_BUTTON == option) {
                Fattura_passivaBulk fp = (Fattura_passivaBulk) bp.getModel();

                if (!bp.isEditing()) {
                    bp.setMessage("Non è possibile cancellare in questo momento");
                } else {
                    bp.delete(context);

                    RisultatoEliminazioneBP rebp = (RisultatoEliminazioneBP) context.createBusinessProcess("RisultatoEliminazioneBP", new String[]{"MRSWTh"});
                    Risultato_eliminazioneVBulk deleteManager = null;
                    if (!(bp instanceof CRUDFatturaPassivaIBP))
                        deleteManager = rebp.manageDelete(context, bp);
                    if (deleteManager != null &&
                            (!deleteManager.getDocumentiAmministrativiScollegati().isEmpty() || !deleteManager.getDocumentiContabiliScollegati().isEmpty())) {
                        rebp.edit(context, deleteManager);

                        context.addHookForward("bringback", this, "doBringBackConfirmDelete");
                        HookForward hook = (HookForward) context.findForward("bringback");
                        return context.addBusinessProcess(rebp);
                    } else {
                        bp.commitUserTransaction();
                        if (fp.isVoidable()) {
                            bp.setMessage("Annullamento effettuato.");
                            bp.edit(context, bp.getModel());
                        } else {
                            if (!(bp instanceof CRUDFatturaPassivaIBP)) {
                                //Nel caso in cui Ndc e Ndd vengano aggiornate completamente in
                                //automatico e non sia necessario il gestore cancellazioni riapro la
                                //fattura passiva di origine
                                doCloseForm(context);
                                Forward fwd = doChiusuraNotaDiCredito(context);
                                ((CRUDFatturaPassivaIBP) context.getBusinessProcess()).setMessage("Cancellazione effettuata.");
                                return fwd;
                            }
                            bp.reset(context);
                            bp.setMessage("Cancellazione effettuata.");
                        }
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
     * Invoca il metodo sulla component per la validazione e quadratura dei totali
     * tra testata e dettagli
     */
    protected void controllaQuadraturaConti(ActionContext context, Fattura_passivaBulk fatturaPassiva)
            throws it.cnr.jada.comp.ComponentException {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();

        try {
            FatturaPassivaComponentSession h = (FatturaPassivaComponentSession) bp.createComponentSession();
            h.controllaQuadraturaConti(context.getUserContext(), fatturaPassiva);
        } catch (java.rmi.RemoteException e) {
            bp.handleException(e);
        } catch (BusinessProcessException e) {
            bp.handleException(e);
        }
    }

    /**
     * Invoca il metodo sulla component per la validazione e quadratura dei totali
     * tra l'importo totale dei dettagli associati ad una scadenza di documento
     * contabile e l'importo della scadenza stessa
     */

    private void controllaQuadraturaObbligazioni(ActionContext context, Fattura_passivaBulk fatturaPassiva)
            throws it.cnr.jada.comp.ComponentException {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();

        try {
            FatturaPassivaComponentSession h = (FatturaPassivaComponentSession) bp.createComponentSession();
            h.controllaQuadraturaObbligazioni(context.getUserContext(), fatturaPassiva);
        } catch (java.rmi.RemoteException e) {
            bp.handleException(e);
        } catch (BusinessProcessException e) {
            bp.handleException(e);
        }
    }

    /**
     * Controlla che i dettagli selezionati per la contabilizzazione siano tutti in
     * stato iniziale (e --> non siano stati contabilizzati precedentemente)
     */
    protected void controllaSelezionePerContabilizzazione(ActionContext context, java.util.Iterator selectedModels)
            throws it.cnr.jada.comp.ApplicationException {

        if (selectedModels != null) {
            while (selectedModels.hasNext()) {
                Fattura_passiva_rigaBulk rigaSelected = (Fattura_passiva_rigaBulk) selectedModels.next();
                if (!Fattura_passiva_rigaBulk.STATO_INIZIALE.equals(rigaSelected.getStato_cofi()))
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
     * In base alla data fattura fornitore di testata calcola e imposta l'esercizio
     * di validità del fornitore
     *
     * @param context         L'ActionContext della richiesta
     * @param fattura_passiva
     * @throws ComponentException
     */
    protected void creaEsercizioPerFatturaFornitore(ActionContext context, Fattura_passivaBulk fattura_passiva) throws ComponentException {

        if (fattura_passiva == null) return;

        if (fattura_passiva.getDt_fattura_fornitore() != null) {
            java.util.GregorianCalendar gc = (java.util.GregorianCalendar) java.util.GregorianCalendar.getInstance();
            gc.setTime(new java.util.Date(fattura_passiva.getDt_fattura_fornitore().getTime()));
            Integer year = new Integer(gc.get(java.util.GregorianCalendar.YEAR));
            fattura_passiva.setEsercizio_fattura_fornitore(year);
        } else
            fattura_passiva.setEsercizio_fattura_fornitore(null);

    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doAddToCRUD(ActionContext context, String tableName) {

        it.cnr.jada.util.action.CRUDBP bp = getBusinessProcess(context);
        Fattura_passivaBulk fatturaPassiva = (Fattura_passivaBulk) bp.getModel();
        if (fatturaPassiva.getTi_istituz_commerc() == null || fatturaPassiva.getTipo_sezionale() == null) {
            bp.setErrorMessage("Specificare prima una tipologia e un sezionale per la fattura.");
            return context.findDefaultForward();
        }
        return super.doAddToCRUD(context, tableName);
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doAddToCRUDMain_Dettaglio(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            bp.getDettaglio().add(context);
            Optional.ofNullable(bp.getDettaglio().getModel())
                    .filter(Fattura_passiva_rigaBulk.class::isInstance)
                    .map(Fattura_passiva_rigaBulk.class::cast)
                    .ifPresent(fattura_passiva_rigaBulk -> {
                        fattura_passiva_rigaBulk.setBene_servizio(new Bene_servizioBulk());
                        fattura_passiva_rigaBulk.setVoce_iva(new Voce_ivaBulk());
                    });
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doAddToCRUDMain_Obbligazioni(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            bp.getDettaglio().getSelection().clearSelection();
            fillModel(context);
            Fattura_passivaBulk fatturaPassiva = (Fattura_passivaBulk) bp.getModel();

            if (fatturaPassiva.isGestione_doc_ele() && fatturaPassiva.isGenerataDaCompenso())
                throw new it.cnr.jada.comp.ApplicationException("La fattura deve essere associata a compenso, la contabilizzazione verrà fatta direttamente nel compenso!");

            if (fatturaPassiva.getFornitore() == null || fatturaPassiva.getFornitore().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.UNDEFINED)
                throw new it.cnr.jada.comp.ApplicationException("Per eseguire questa operazione è necessario selezionare un fornitore!");
            return basicDoRicercaObbligazione(context, fatturaPassiva, null, false);
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
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            bp.getDettaglio().getSelection().clearSelection();
            fillModel(context);
            Obbligazione_scadenzarioBulk obbligazione = (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel();
            if (obbligazione == null)
                throw new it.cnr.jada.comp.ApplicationException("Selezionare l'impegno a cui associare i dettagli.");

            java.util.Vector selectedModels = new java.util.Vector();
            for (java.util.Enumeration e = bp.getDettaglio().getElements(); e.hasMoreElements(); ) {
                Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) e.nextElement();
                if (riga.STATO_INIZIALE.equals(riga.getStato_cofi()))
                    selectedModels.add(riga);
            }
            if (selectedModels.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Tutti i dettagli sono già stati contabilizzati!");
            it.cnr.jada.util.action.SelezionatoreListaBP slbp = (it.cnr.jada.util.action.SelezionatoreListaBP) select(
                    context,
                    new it.cnr.jada.util.ListRemoteIterator(selectedModels),
                    it.cnr.jada.bulk.BulkInfo.getBulkInfo(Fattura_passiva_rigaBulk.class),
                    "righiSet",
                    "doBringBackAddToCRUDMain_Obbligazioni_DettaglioObbligazioni");
            slbp.setMultiSelection(true);
            return slbp;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Chiede conferma per l'apertura del pannello delle fatture sul modello fattura
     * estera specificato per le fatture di tipo bolla doganale e spedizioniere
     */

    public Forward doApriFatturaEstera(ActionContext context) {

        try {
            CRUDBP bp = getBusinessProcess(context);
            fillModel(context);
            if (bp.isDirty())
                return openContinuePrompt(context, "doConfermaApriFatturaEstera");
            return doConfermaApriFatturaEstera(context, it.cnr.jada.util.action.OptionBP.YES_BUTTON);
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

        try {
            Nota_di_creditoBulk ndc = (Nota_di_creditoBulk) ((HookForward) context.getCaller()).getParameter("focusedElement");
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

        try {
            Nota_di_debitoBulk ndd = (Nota_di_debitoBulk) ((HookForward) context.getCaller()).getParameter("focusedElement");
            if (ndd != null)
                return basicDoApriNotaDiDebito(context, ndd);
            return context.findDefaultForward();
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

    public Forward doApriNotaDiCredito(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
            fillModel(context);

            it.cnr.jada.util.RemoteIterator ri = findNoteDiCreditoFor(context, (Fattura_passiva_IBulk) bp.getModel());
            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
            if (ri == null || ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                throw new it.cnr.jada.comp.ApplicationException("Nessuna nota di credito generata per questa fattura!");
            } else if (ri.countElements() == 1)
                return basicDoApriNotaDiCredito(context, (Nota_di_creditoBulk) ri.nextElement());
            else
                return select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(Nota_di_creditoBulk.class), "default", "doApriNdCSelezionata");
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

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
            fillModel(context);

            it.cnr.jada.util.RemoteIterator ri = findNoteDiDebitoFor(context, (Fattura_passiva_IBulk) bp.getModel());
            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
            if (ri == null || ri.countElements() == 0)
                throw new it.cnr.jada.comp.ApplicationException("Nessuna nota di debito generata per questa fattura!");
            else if (ri.countElements() == 1)
                return basicDoApriNotaDiDebito(context, (Nota_di_debitoBulk) ri.nextElement());
            else
                return select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(Nota_di_debitoBulk.class), "default", "doApriNdDSelezionata");
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Associa ad un buono di carico già creato i dettagli selezionati in fattura
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doAssociaInventario(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            fillModel(context);

            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            controllaQuadraturaConti(context, fattura);

            if ((fattura.getAssociazioniInventarioHash() != null && !fattura.getAssociazioniInventarioHash().isEmpty()) || (fattura.getHa_beniColl()))
                return openConfirm(context, "Alcuni dettagli sono già stati associati. Si vuole continuare?", it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doConfermaAssocia");

            return basicDoAssociaDettagli(context);

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doSelezionaOrdini(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            fillModel(context);

            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();

                try {
                    final Fattura_passivaBulk fatturaPassiva = fattura;
                    final RemoteIterator contabilizzaRigaIterator = bp.find(context, new CompoundFindClause(),
                            new EvasioneOrdineRigaBulk(), fattura, "ricercaOrdini");
                    return Optional.ofNullable(contabilizzaRigaIterator)
                            .map(remoteIterator -> {
                                try {
                                    return remoteIterator.countElements();
                                } catch (RemoteException e) {
                                    throw new DetailedRuntimeException(e);
                                }
                            })
                            .filter(elements -> elements != 0)
                            .map(integer -> {
                                try {
                                    ContabilizzaOrdineBP nbp = (ContabilizzaOrdineBP) context.createBusinessProcess("ContabilizzaOrdineBP", new Object[]{"MRSWTh"});
                                    nbp.setFattura_passivaBulk(fatturaPassiva);
                                    nbp.setMultiSelection(true);
                                    nbp.setIterator(context, contabilizzaRigaIterator);
                                    context.addHookForward("seleziona", this, "doContabilizzaOrdine");
                                    return (Forward)context.addBusinessProcess(nbp);
                                } catch (BusinessProcessException | RemoteException e) {
                                    throw new DetailedRuntimeException(e);
                                }
                            }).orElseGet(() -> {
                                bp.setMessage("Non ci sono dati per i criteri impostati!");
                                try {
                                    contabilizzaRigaIterator.close();
                                } catch (RemoteException e) {
                                    throw new DetailedRuntimeException(e);
                                }
                                return context.findDefaultForward();
                            });
                } catch (Throwable e) {
                    return handleException(context, e);
                }

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di azzeramento del searchtool "fornitore"
     *
     * @param context         L'ActionContext della richiesta
     * @param fattura_passiva L'OggettoBulk padre del searchtool
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBlankSearchFatturaEstera(ActionContext context,
                                              Fattura_passivaBulk fattura_passiva)
            throws java.rmi.RemoteException {

        try {
            ((Fattura_passiva_IBulk) fattura_passiva).setFattura_estera(
                    new Fattura_passiva_IBulk(
                            fattura_passiva.getCd_cds(),
                            fattura_passiva.getCd_unita_organizzativa(),
                            fattura_passiva.getEsercizio(),
                            null
                    ));

            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di azzeramento del searchtool "fornitore"
     *
     * @param context         L'ActionContext della richiesta
     * @param fattura_passiva L'OggettoBulk padre del searchtool
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBlankSearchFornitore(ActionContext context,
                                          Fattura_passivaBulk fattura_passiva)
            throws java.rmi.RemoteException {

        try {
            TerzoBulk tb = new TerzoBulk();
            tb.setAnagrafico(new AnagraficoBulk());
            fattura_passiva.setFornitore(tb);
            fattura_passiva.setNome(null);
            fattura_passiva.setCognome(null);
            fattura_passiva.setRagione_sociale(null);
            fattura_passiva.setCodice_fiscale(null);
            fattura_passiva.setPartita_iva(null);
            fattura_passiva.setModalita(null);
            fattura_passiva.setTermini(null);
            fattura_passiva.setBanca(null);
            fattura_passiva.setCessionario(null);
            fattura_passiva.setModalita_pagamento(null);
            fattura_passiva.setTermini_pagamento(null);

            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di azzeramento del searchtool "sospeso"
     *
     * @param context         L'ActionContext della richiesta
     * @param fattura_passiva L'OggettoBulk padre del searchtool
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBlankSearchSospeso(ActionContext context,
                                        Fattura_passivaBulk fattura_passiva)
            throws java.rmi.RemoteException {

        try {
            Lettera_pagam_esteroBulk lettera = fattura_passiva.getLettera_pagamento_estero();
            SospesoBulk vecchioSospeso = lettera.getSospeso();
            if (vecchioSospeso != null)
                lettera.addToSospesiCancellati(vecchioSospeso);

            SospesoBulk sospeso = new SospesoBulk();
            sospeso.setEsercizio(lettera.getEsercizio());
            if (!Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(), lettera.getEsercizio()).getFl_tesoreria_unica().booleanValue())
                sospeso.setCd_cds(lettera.getCd_cds());
            sospeso.setTi_entrata_spesa(sospeso.TIPO_SPESA);
            sospeso.setTi_sospeso_riscontro(sospeso.TI_SOSPESO);
            lettera.setSospeso(sospeso);
            java.math.BigDecimal zero = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
            lettera.setIm_pagamento(zero);
            //lettera.setIm_commissioni(zero);
            //lettera.setDt_registrazione(new java.sql.Timestamp(System.currentTimeMillis()));
            ((CRUDBP) context.getBusinessProcess()).setDirty(true);
            return context.findDefaultForward();

        } catch (Exception e) {
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
            java.util.List selectedModels = (java.util.List) fwd.getParameter("selectedElements");
            if (selectedModels != null && !selectedModels.isEmpty()) {
                CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
                Obbligazione_scadenzarioBulk obbligazione = (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel();
                if (obbligazione != null) {
                    basicDoContabilizza(context, obbligazione, selectedModels);
                    bp.setDirty(true);
                }
                doCalcolaTotalePerObbligazione(context, obbligazione);
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
                CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
                Fattura_passivaBulk fatturaPassiva = (Fattura_passivaBulk) bp.getModel();
                for (java.util.Iterator i = dettagliInventariati.iterator(); i.hasNext(); ) {
                    Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk) i.next();
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
                CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
                Fattura_passivaBulk fatturaPassiva = (Fattura_passivaBulk) bp.getModel();
                for (java.util.Iterator i = dettagliInventariati.iterator(); i.hasNext(); ) {
                    Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk) i.next();
                    if (((FatturaPassivaComponentSession) bp.createComponentSession()).ha_beniColl(context.getUserContext(), dettaglio))
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

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doBringBackConfirmDelete(ActionContext context) {

        HookForward caller = (HookForward) context.getCaller();

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
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
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di assegnamento al crudtool "crea_fornitore"
     *
     * @param context          L'ActionContext della richiesta
     * @param fattura_passiva
     * @param fornitoreTrovato
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBringBackCRUDCrea_fornitore(ActionContext context,
                                                 Fattura_passivaBulk fattura_passiva,
                                                 TerzoBulk fornitoreTrovato)
            throws java.rmi.RemoteException {

        return doBringBackSearchFornitore(context, fattura_passiva, fornitoreTrovato);
    }
    public Forward doBringBackCRUDFornitore(ActionContext context,
            Fattura_passivaBulk fattura_passiva,
            TerzoBulk fornitoreTrovato)
            throws java.rmi.RemoteException {
    	return doBringBackCRUDCrea_fornitore(context, fattura_passiva, fornitoreTrovato);
	}
    
    /**
     * Aggiunge, una volta terminata l'operazione di creazione di buono di carico,
     * tale buono al modello
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doBringBackInventariaDettagli(ActionContext context) {

        try {
            HookForward hook = (HookForward) context.getCaller();
            Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk) hook.getParameter("bringback");

            if (buonoCS != null) {
                buonoCS.setByFattura(true);
                java.util.List dettagliInventariati = (java.util.List) hook.getParameter("dettagliDaInventariare");
                CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
                Fattura_passivaBulk fatturaPassiva = (Fattura_passivaBulk) bp.getModel();
                for (java.util.Iterator i = dettagliInventariati.iterator(); i.hasNext(); ) {
                    Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk) i.next();
                    fatturaPassiva.addToCarichiInventarioHash(buonoCS, dettaglio);
                    dettaglio.setInventariato(true);
                }
            }
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * chiede l'associazione dell'obbligazione selezionata dall'utente ai dettagli
     * di fattura selezionati per la contabilizzazione ('basicDoBringBackOpenObbligazioniWindow')
     * Ricalcola i totali di scadenza e aggiunge ai documenti contabili modificati tale
     * scadenza
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doBringBackOpenObbligazioniWindow(ActionContext context) {

        HookForward caller = (HookForward) context.getCaller();
        Obbligazione_scadenzarioBulk obblig = (Obbligazione_scadenzarioBulk) caller.getParameter("bringback");
        if (obblig != null) {
            try {
                basicDoBringBackOpenObbligazioniWindow(context, obblig);

                CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
                bp.getObbligazioniController().reset(context);
                bp.getObbligazioniController().setModelIndex(context, it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(bp.getObbligazioniController().getDetails(), obblig));

                doCalcolaTotalePerObbligazione(context, (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel());

                bp.setDirty(true);
                if (bp instanceof TitoloDiCreditoDebitoBP)
                    ((TitoloDiCreditoDebitoBP) bp).addToDocumentiContabiliModificati(obblig);
            } catch (Throwable t) {
                return handleException(context, t);
            }
        }
        return context.findDefaultForward();
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di ricerca del searchtool "bene_servizio"
     *
     * @param context              L'ActionContext della richiesta
     * @param fattura_passiva_riga L'OggettoBulk padre del searchtool
     * @param beneTrovato          L'OggettoBulk selezionato dall'utente
     * @return Il Forward alla pagina di risposta
     * @throws ComponentException
     */
    public Forward doBringBackSearchBene_servizio(ActionContext context,
                                                  Fattura_passiva_rigaBulk fattura_passiva_riga,
                                                  Bene_servizioBulk beneTrovato)
            throws it.cnr.jada.comp.ComponentException {

        try {
            Voce_ivaBulk voceIvaB = null;
            if (beneTrovato != null) {

                fattura_passiva_riga.setBene_servizio(beneTrovato);
                if (fattura_passiva_riga.getDs_riga_fattura() == null)
                    fattura_passiva_riga.setDs_riga_fattura(beneTrovato.getDs_bene_servizio());
                fattura_passiva_riga.setInventariato(false);
                Fattura_passivaBulk fatturaPassiva = fattura_passiva_riga.getFattura_passiva();
                if (TipoIVA.ISTITUZIONALE.value().equalsIgnoreCase(fatturaPassiva.getTi_istituz_commerc())) {
                    voceIvaB = caricaVoceIVADefault(context);
                    //if (voceIvaB == null)
                    //((it.cnr.jada.util.action.CRUDBP)context.getBusinessProcess()).setErrorMessage("Attenzione: non è stata caricata una voce IVA di default per le fatture istituzionali!");
                } else
                    voceIvaB = beneTrovato.getVoce_iva();
            }
            return doBringBackSearchVoce_iva(context, fattura_passiva_riga, voceIvaB);

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di ricerca del searchtool "fornitore"
     *
     * @param context          L'ActionContext della richiesta
     * @param fattura_passiva  L'OggettoBulk padre del searchtool
     * @param fornitoreTrovato L'OggettoBulk selezionato dall'utente
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBringBackSearchFornitore(ActionContext context,
                                              Fattura_passivaBulk fattura_passiva,
                                              TerzoBulk fornitoreTrovato)
            throws java.rmi.RemoteException {

        try {
            CRUDFatturaPassivaBP crudFattura = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            if (fornitoreTrovato != null) {
                if (fattura_passiva.getFl_intra_ue() != null && fattura_passiva.getFl_intra_ue().booleanValue()) {
                    if (fornitoreTrovato.getAnagrafico().getComune_fiscale() != null && fornitoreTrovato.getAnagrafico().getComune_fiscale().getNazione() != null) {
                        fornitoreTrovato.getAnagrafico().setNazionalita(fornitoreTrovato.getAnagrafico().getComune_fiscale().getNazione());
                        if (fornitoreTrovato.getAnagrafico().getNazionalita().getDivisa() != null &&
                                fattura_passiva.getValuta().getCd_divisa().compareTo(fornitoreTrovato.getAnagrafico().getNazionalita().getDivisa().getCd_divisa()) != 0)
                            crudFattura.setMessage("La valuta del fornitore " + fornitoreTrovato.getAnagrafico().getNazionalita().getDivisa().getDs_divisa() + " non è coerente con quella indicata in testata.");
                    }
                }
                FatturaPassivaComponentSession fpcs = (FatturaPassivaComponentSession) crudFattura.createComponentSession();
                fattura_passiva = fpcs.completaFornitore(context.getUserContext(), fattura_passiva, fornitoreTrovato);

                Fattura_passivaBulk originale = (Fattura_passivaBulk) crudFattura.getBulkClone();
                if (originale != null && !fattura_passiva.NON_REGISTRATO_IN_COGE.equalsIgnoreCase(fattura_passiva.getStato_coge())) {
                    TerzoBulk oldTerzo = originale.getFornitore();
                    if (oldTerzo != null && !oldTerzo.equalsByPrimaryKey(fornitoreTrovato))
                        fattura_passiva.setStato_coge(fattura_passiva.DA_RICONTABILIZZARE_IN_COGE);
                    else
                        fattura_passiva.setStato_coge(fattura_passiva.REGISTRATO_IN_COGE);
                }
                crudFattura.setModel(context, fattura_passiva);
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di ricerca del searchtool "sospeso"
     *
     * @param context         L'ActionContext della richiesta
     * @param fattura_passiva L'OggettoBulk padre del searchtool
     * @param sospesoTrovato  L'OggettoBulk selezionato dall'utente
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBringBackSearchSospeso(ActionContext context,
                                            Fattura_passivaBulk fattura_passiva,
                                            SospesoBulk sospesoTrovato)
            throws java.rmi.RemoteException {

        try {
            Lettera_pagam_esteroBulk lettera = fattura_passiva.getLettera_pagamento_estero();
            if (sospesoTrovato != null && lettera != null) {
                lettera.removeFromSospesiCancellati(sospesoTrovato);
                lettera.setSospeso(sospesoTrovato);
                lettera.setIm_pagamento(sospesoTrovato.getIm_sospeso());
                //lettera.setDt_registrazione(sospesoTrovato.getDt_registrazione());
                ((CRUDBP) context.getBusinessProcess()).setDirty(true);
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di ricerca del searchtool "voce_iva"
     *
     * @param context              L'ActionContext della richiesta
     * @param fattura_passiva_riga L'OggettoBulk padre del searchtool
     * @param ivaTrovata           L'OggettoBulk selezionato dall'utente
     * @return Il Forward alla pagina di risposta
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public Forward doBringBackSearchVoce_iva(ActionContext context,
                                             Fattura_passiva_rigaBulk fattura_passiva_riga,
                                             Voce_ivaBulk ivaTrovata)
            throws java.rmi.RemoteException {

        try {

            if (ivaTrovata != null) {
                fattura_passiva_riga.setVoce_iva(ivaTrovata);
                basicDoCalcolaTotaliDiRiga(
                        context,
                        fattura_passiva_riga,
                        fattura_passiva_riga.getIm_imponibile().add(fattura_passiva_riga.getIm_iva()));
            }
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Viene richiamato nel momento in cui si seleziona una valuta dal combo Valuta nella
     * testata della fattura.
     * Ricalcola il valore di testata in EUR in base all'importo di fattura fornitore.
     */
    public Forward doCalcolaTotaleFatturaFornitoreInEur(ActionContext context) {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
        Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
        java.math.BigDecimal impFornitore = fattura.getIm_totale_fattura();
        java.math.BigDecimal impFornitoreEuro = fattura.getIm_importo_totale_fattura_fornitore_euro();

        try {
            fillModel(context);
            if (fattura.getIm_totale_fattura() != null)
                basicDoCalcolaTotaleFatturaFornitoreInEur(fattura);
            return context.findDefaultForward();
        } catch (Throwable t) {
            fattura.setIm_totale_fattura(impFornitore);
            fattura.setIm_importo_totale_fattura_fornitore_euro(impFornitoreEuro);
            return handleException(context, t);
        }
    }

    /**
     * Ricalcola il valore totale degli importi associati alla scadenza
     */

    public Forward doCalcolaTotalePerObbligazione(ActionContext context, Obbligazione_scadenzarioBulk obbligazione) {

        it.cnr.jada.util.action.FormBP bulkBP = (it.cnr.jada.util.action.FormBP) context.getBusinessProcess();
        if (bulkBP instanceof CRUDFatturaPassivaBP) {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) bulkBP;
            Fattura_passivaBulk fatt = (Fattura_passivaBulk) bp.getModel();
            if (fatt.getFattura_passiva_obbligazioniHash() != null && obbligazione != null) {
                try {
                    fatt.setImportoTotalePerObbligazione(
                            calcolaTotaleSelezionati(
                                    (java.util.List) fatt.getFattura_passiva_obbligazioniHash().get(obbligazione),
                                    fatt.quadraturaInDeroga()));
                } catch (it.cnr.jada.comp.ApplicationException e) {
                    fatt.setImportoTotalePerObbligazione(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                }
            } else
                fatt.setImportoTotalePerObbligazione(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        }
        return context.findDefaultForward();
    }

    /**
     * Ricalcola il totali di riga
     */

    public Forward doCalcolaTotaliDiRiga(ActionContext context) {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
        Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) bp.getDettaglio().getModel();
        java.math.BigDecimal qta = riga.getQuantita();
        java.math.BigDecimal pu = riga.getPrezzo_unitario();
        java.math.BigDecimal imiva = riga.getIm_iva();
        Boolean flimiva = riga.getFl_iva_forzata();
        java.math.BigDecimal impnc = riga.getIm_diponibile_nc();
        try {
            java.math.BigDecimal vecchioTotale = riga.getIm_imponibile().add(riga.getIm_iva());
            if (riga instanceof Fattura_passiva_rigaIBulk)
                vecchioTotale = vecchioTotale.add(((Fattura_passiva_rigaIBulk) riga).getIm_totale_addebiti());
            fillModel(context);
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
        return context.findDefaultForward();
    }

    /**
     * Viene richiamato nel momento in cui viene inserito/cambiato il trovato
     * nel dettaglio della fattura.
     */

    public Forward doVerificaEsistenzaTrovato(ActionContext context) {

        try {
            fillModel(context);
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            bp.ricercaDatiTrovato(context);
        } catch (Exception e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
//	try {
//		CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP)getBusinessProcess(context);
//		Fattura_passivaBulk fattura = (Fattura_passivaBulk)bp.getModel();
//		java.sql.Timestamp dataEmissione = fattura.getDt_fattura_fornitore();
//		try	{
//			fillModel( context );
//			if (!bp.isSearching())
//				fattura.validateDate();
//			creaEsercizioPerFatturaFornitore(context, fattura);
//
//			return context.findDefaultForward();
//		} catch(Throwable e) {
//			fattura.setDt_fattura_fornitore(dataEmissione);
//			bp.setModel(context,fattura);
//			throw e;
//		}
//	} catch(Throwable e) {
//		return handleException(context, e);
//	}
    }

    /**
     * Viene richiamato nel momento in cui viene cambiata la data di emissione
     * fattura fornitore nella testata della fattura.
     */

    public Forward doCambiaDataEmissioneFattura(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            java.sql.Timestamp dataEmissione = fattura.getDt_fattura_fornitore();
            boolean hasAccesso = ((it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession")).controllaAccesso(context.getUserContext(), "AMMFATTURDOCSFATPASA");
            try {
                fillModel(context);
                if (!bp.isSearching())
                    if (fattura.isGestione_doc_ele() &&
                            (fattura.getDt_fattura_fornitore() != null && !(fattura.getDt_fattura_fornitore().compareTo(fattura.getDataInizioFatturaElettronica()) < 0)) &&
                            !fattura.isElettronica() &&
                            !fattura.isEstera() &&
                            !fattura.isSanMarinoSenzaIVA() &&
                            !fattura.isSanMarinoConIVA() &&
                            !fattura.isBollaDoganale() &&
                            !hasAccesso) {
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                        throw new it.cnr.jada.comp.ApplicationException("Non è possibile registrare una fattura che non sia elettronica, che non sia estera e che abbia data di emissione uguale o successiva al " + sdf.format(fattura.getDataInizioFatturaElettronica()) + "!");
                    }
                fattura.validateDate();
                //NON ELIMINARE QUESTO COMMENTO: POSSIBILE VARIAZIONE IN FUTURO
                //java.sql.Timestamp dataFatturaFornitore = fattura.getDt_fattura_fornitore();
                //java.sql.Timestamp dataInizioValuta = fattura.getInizio_validita_valuta();
                //java.sql.Timestamp dataFineValuta = fattura.getFine_validita_valuta();
                //if (dataInizioValuta == null || dataFineValuta == null) {
                //creaEsercizioPerFatturaFornitore(context, fattura);
                //return doSelezionaValuta(context);
                //}
                //if (dataFatturaFornitore != null &&
                //!dataFatturaFornitore.equals(dataInizioValuta) &&
                //!dataFatturaFornitore.equals(dataFineValuta)) {

                //if (dataFatturaFornitore.before(dataInizioValuta) || dataFatturaFornitore.after(dataFineValuta)) {
                //creaEsercizioPerFatturaFornitore(context, fattura);
                //return doSelezionaValuta(context);
                //}
                //}
                if (fattura instanceof Nota_di_creditoBulk && fattura.getDt_scadenza() == null)
                    fattura.setDt_scadenza(fattura.getDt_fattura_fornitore());
                creaEsercizioPerFatturaFornitore(context, fattura);

                return context.findDefaultForward();
            } catch (Throwable e) {
                fattura.setDt_fattura_fornitore(dataEmissione);
                bp.setModel(context, fattura);
                throw e;
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Viene richiamato nel momento in cui viene cambiata la data di registrazione
     * Richiesta la validazione delle date
     */

    public Forward doCambiaDataRegistrazione(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            java.sql.Timestamp dataRegistrazione = fattura.getDt_registrazione();
            try {
                fillModel(context);
                if (!bp.isSearching())
                    fattura.validateDate();
                //NON ELIMINARE QUESTO COMMENTO: POSSIBILE VARIAZIONE IN FUTURO
                //java.sql.Timestamp dataFatturaFornitore = fattura.getDt_fattura_fornitore();
                //if (dataFatturaFornitore != null &&
                //!dataFatturaFornitore.equals(fattura.getInizio_validita_valuta()) &&
                //!dataFatturaFornitore.equals(fattura.getFine_validita_valuta())) {

                //if (fattura.getInizio_validita_valuta() == null || fattura.getFine_validita_valuta() == null ||
                //(!dataFatturaFornitore.after(fattura.getInizio_validita_valuta()) && !dataFatturaFornitore.before(fattura.getFine_validita_valuta()))) {

                //return doSelezionaValuta(context);
                //}
                //}
                //bp.valorizzaInfoDocEle(context, fattura);
                return context.findDefaultForward();
            } catch (Throwable e) {
                fattura.setDt_registrazione(dataRegistrazione);
                bp.setModel(context, fattura);
                throw e;
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }


    }

    /**
     * Viene richiamato nel momento in cui viene cambiata la data di scadenza fattura fornitore
     * Richiesta la validazione delle date
     */
    public Forward doCambiaDataScadenzaFatturaFornitore(ActionContext context) {
        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            java.sql.Timestamp dataScadenza = fattura.getDt_scadenza();
            try {
                fillModel(context);
                if (!bp.isSearching()) {
               	  	bp.setModel(context, fattura);
                    fattura.validateDate();
                    java.util.Calendar cal = Calendar.getInstance();
                    if (fattura.getData_protocollo() != null)
                        cal.setTime(fattura.getData_protocollo());
                    else
                        throw new ValidationException("La data di protocollo/ricezione non può essere nulla!");
                    cal.add(Calendar.DAY_OF_MONTH, 45);

                    if (fattura.getDt_scadenza() != null && fattura.getDt_scadenza().after(cal.getTime())) {
                        OptionBP optionBP = openConfirm(context, "Attenzione: la data di scadenza indicata è superiore a quanto previsto dalla normativa. Vuoi continuare?", OptionBP.CONFIRM_YES_NO, "doConfirmDtScadenza");
                        return optionBP;
                    }
                }
                return context.findDefaultForward();
            } catch (Throwable e) {
                fattura.setDt_scadenza(dataScadenza);
                bp.setModel(context, fattura);
                throw e;
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }


    }
    public Forward doCambiaDataProtocollo(ActionContext context) {
        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            java.sql.Timestamp dataprotocollo= fattura.getData_protocollo();
            try {
                fillModel(context);
                if (!bp.isSearching()) {
                	if(fattura.getTi_fattura().compareTo(Fattura_passivaBulk.TIPO_FATTURA_PASSIVA)==0)
               	  		basicDoOnIstituzionaleCommercialeChange(context, fattura);
               	  	bp.setModel(context, fattura);
                    fattura.validateDate();
                    java.util.Calendar cal = Calendar.getInstance();
                    if (fattura.getData_protocollo() != null)
                        cal.setTime(fattura.getData_protocollo());
                    else
                        throw new ValidationException("La data di protocollo/ricezione non può essere nulla!");
                    cal.add(Calendar.DAY_OF_MONTH, 45);

                }
                return context.findDefaultForward();
            } catch (Throwable e) {
                fattura.setData_protocollo(dataprotocollo);
                bp.setModel(context, fattura);
                throw e;
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }


    }
    /**
     * Crea e imposta la lettera di pagamento estero
     */
    public Forward doCancellaLettera(ActionContext context) {

        try {
            fillModel(context);
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk model = (Fattura_passivaBulk) bp.getModel();
            if (model != null) {
                if (model.getLettera_pagamento_estero() == null)
                    return handleException(
                            context,
                            new it.cnr.jada.comp.ApplicationException("Lettera di pagamento estero NON presente!"));

                Lettera_pagam_esteroBulk lettera = model.getLettera_pagamento_estero();
                int status = lettera.getCrudStatus();
                if (status == OggettoBulk.UNDEFINED || status == OggettoBulk.TO_BE_CREATED) {
                    model.setLettera_pagamento_estero(null);

                } else {
                    model = ((FatturaPassivaComponentSession) bp.createComponentSession()).eliminaLetteraPagamentoEstero(context.getUserContext(), model);
                    bp.setModel(context, model);
                }
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
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

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
        if (bp instanceof IDocumentoAmministrativoSpesaBP && ((IDocumentoAmministrativoSpesaBP) bp).isSpesaBP())
            return basicDoCerca(context);
        return super.doCerca(context);
    }

    /**
     * Chide la nota di credito e riapre la fattura originante
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doChiusuraNotaDiCredito(ActionContext context) {

        try {
            it.cnr.jada.util.action.CRUDBP bp = (it.cnr.jada.util.action.CRUDBP) context.getBusinessProcess();
            if (bp instanceof CRUDFatturaPassivaBP && ((CRUDFatturaPassivaBP) bp).isFromFatturaElettronica())
                return this.doCloseForm(context);
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

        try {
            it.cnr.jada.util.action.CRUDBP bp = (it.cnr.jada.util.action.CRUDBP) context.getBusinessProcess();
            bp.edit(context, bp.getModel());
            return context.findDefaultForward();
        } catch (BusinessProcessException e) {
            return handleException(context, e);
        }
    }

    /**
     * Apre il pannello delle fatture sul modello fattura estera
     * specificato per le fatture di tipio bolla doganale e spedizioniere
     */

    public Forward doConfermaApriFatturaEstera(ActionContext context, int option) {

        try {
            if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
                CRUDFatturaPassivaIBP bp = (CRUDFatturaPassivaIBP) context.getBusinessProcess();
                Fattura_passiva_IBulk fattura = (Fattura_passiva_IBulk) bp.getModel();
                if (!fattura.isBollaDoganale() && !fattura.isSpedizioniere())
                    throw new it.cnr.jada.comp.ApplicationException("Comando disponibile solo se la fattura è di tipo bolla doganale o spedizioniere");
                Fattura_passiva_IBulk fatturaEstera = fattura.getFattura_estera();
                if (fatturaEstera == null || fatturaEstera.getCrudStatus() != OggettoBulk.NORMAL)
                    throw new it.cnr.jada.comp.ApplicationException("Specificare la fattura estera collegata!");

                bp.edit(context, fatturaEstera);
            }

            return context.findDefaultForward();

        } catch (it.cnr.jada.comp.ApplicationException e) {
            return handleException(context, e);
        } catch (BusinessProcessException e) {
            return handleException(context, e);
        }
    }

    /**
     * Richiede conferma all'utente per la continuazione del processo di creazione di
     * buono di carico
     */
    public Forward doConfermaInventaria(ActionContext context, int option) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
                return basicDoInventariaDettagli(context);
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    public Forward doConfermaAssocia(ActionContext context, int option) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
                return basicDoAssociaDettagli(context);
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * Contabilizza i dettagli selezionati previo controllo della selezione
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doContabilizza(ActionContext context) {

        HookForward caller = (HookForward) context.getCaller();
        Obbligazione_scadenzarioBulk obblig = (Obbligazione_scadenzarioBulk) caller.getParameter("obbligazioneSelezionata");
        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
        java.util.List selectedModels = null;
        try {
            selectedModels = bp.getDettaglio().getSelectedModels(context);
            bp.getDettaglio().getSelection().clearSelection();
        } catch (Throwable e) {
        }

        if (obblig != null) {
            try {
                Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
                TerzoBulk creditore = obblig.getObbligazione().getCreditore();
                if (!fattura.getFornitore().equalsByPrimaryKey(creditore) &&
                        !AnagraficoBulk.DIVERSI.equalsIgnoreCase(creditore.getAnagrafico().getTi_entita())) {
                    ((IDocumentoAmministrativoBulk) fattura).addToDocumentiContabiliCancellati(obblig);
                    throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata deve appartenere ad un'obbligazione che ha come creditore il fornitore della fattura!");
                }
                Filtro_ricerca_obbligazioniVBulk filtro = (Filtro_ricerca_obbligazioniVBulk) caller.getParameter("filtroRicercaUtilizzato");
                if (filtro != null) {
                    Elemento_voceBulk ev = filtro.getElemento_voce();
                    if (ev != null) {
                        if (!obblig.getObbligazione().getElemento_voce().getCd_elemento_voce().startsWith(ev.getCd_elemento_voce())) {
                            if (!ev.getCd_elemento_voce().startsWith(obblig.getObbligazione().getElemento_voce().getCd_elemento_voce())) {
                                ((IDocumentoAmministrativoBulk) fattura).addToDocumentiContabiliCancellati(obblig);
                                throw new it.cnr.jada.comp.ApplicationException("Il titolo capitolo dell'impegno deve essere uguale o appartenere al titolo capitolo della categoria inventario dei beni selezionati (\"" + ev.getCd_elemento_voce() + "\")!");
                            }
                        }
                    }
                }
                Obbligazione_scadenzarioBulk obbligazione = null;
                ObbligazioniTable obbHash = fattura.getObbligazioniHash();
                if (obbHash != null && !obbHash.isEmpty())
                    obbligazione = obbHash.getKey(obblig);
                if (obbligazione != null && obbligazione.getObbligazione().isTemporaneo()) {
                    java.util.Vector models = ((java.util.Vector) obbHash.get(obbligazione));
                    java.util.Vector clone = (java.util.Vector) models.clone();
                    if (!clone.isEmpty()) {
                        scollegaDettagliDaObbligazione(context, clone);
                        clone.addAll(selectedModels);basicDoContabilizza(context, obblig, clone);
                    } else {
                        obbHash.remove(obbligazione);
                        basicDoContabilizza(context, obblig, selectedModels);
                    }
                } else {
                    basicDoContabilizza(context, obblig, selectedModels);
                }
            } catch (it.cnr.jada.comp.ComponentException e) {
                return handleException(context, e);
            }
            bp.getObbligazioniController().getSelection().clear();
            bp.getObbligazioniController().setModelIndex(context, -1);
            bp.getObbligazioniController().setModelIndex(context, it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(bp.getObbligazioniController().getDetails(), obblig));

            bp.setDirty(true);
            if (!"tabFatturaPassivaObbligazioni".equals(bp.getTab("tab")))
                bp.setTab("tab", "tabFatturaPassivaObbligazioni");
        }
        return context.findDefaultForward();
    }

    /**
     * Crea e imposta la lettera di pagamento estero
     */
    public Forward doCreaLettera(ActionContext context) {

        try {
            fillModel(context);
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk model = (Fattura_passivaBulk) bp.getModel();
            if (model != null) {
                if (model.getLettera_pagamento_estero() == null) {
                    if (model instanceof Fattura_passiva_IBulk) {
                        Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk) model;
                        // //RP 23/03/2010  ?? commentato per permettere la generazione delle nc/nd di fatture con lettera di pagamento
					/*if (fp.hasAddebiti() || fp.hasStorni())
						return handleException(
									context,
									new it.cnr.jada.comp.ApplicationException("La lettera di pagamento estero non puo' essere creata se la fattura è ha storni o addebiti!")); */
                        if (fp.isByFondoEconomale())
                            return handleException(
                                    context,
                                    new it.cnr.jada.comp.ApplicationException("La lettera di pagamento estero non puo' essere creata perchè la fattura è destinata al pagamento tramite fondo economale!"));
                    }
                    Lettera_pagam_esteroBulk lettera = new Lettera_pagam_esteroBulk(
                            model.getCd_cds_origine(),
                            model.getCd_uo_origine(),
                            it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()),
                            null);
                    lettera.completeFrom(context);
                    lettera.setToBeCreated();
                    model.setLettera_pagamento_estero(lettera);
                } else {
                    return handleException(
                            context,
                            new it.cnr.jada.comp.ApplicationException("La lettera per il pagamento estero è già stata creata!"));
                }
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce un comando di cancellazione o annullamento.
     * Nel caso di ndc o ndd richiede la gestione della quadratura delle scadenze di doc cont
     * tramite il gestore 'RisultatoEleminazioneBP'
     */
    public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
        Fattura_passivaBulk fp = (Fattura_passivaBulk) bp.getModel();
        if (fp.existARowToBeInventoried())
            try {
                return openConfirm(
                        context,
                        "Si sta per eliminare un documento amministrativo passivo contenente beni soggetti ad inventario.\nSi vuole continuare?",
                        it.cnr.jada.util.action.OptionBP.WARNING_MESSAGE,
                        "confermaEliminazione");
            } catch (BusinessProcessException e) {
                return handleException(context, e);
            }
        return confermaEliminazione(context, it.cnr.jada.util.action.OptionBP.YES_BUTTON);
    }

    /**
     * Forza il flag 'ForzaIVA' e ricalcola i totali di riga
     */
    public Forward doForzaIVA(ActionContext context) {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
        Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) bp.getDettaglio().getModel();
        Boolean flForzaIVA = riga.getFl_iva_forzata();
        java.math.BigDecimal impIVA = riga.getIm_iva();
        try {
            java.math.BigDecimal vecchioTotale = riga.getIm_imponibile().add(riga.getIm_iva());
            if (riga instanceof Fattura_passiva_rigaIBulk)
                vecchioTotale = vecchioTotale.add(((Fattura_passiva_rigaIBulk) riga).getIm_totale_addebiti());
            fillModel(context);
            riga.setFl_iva_forzata(Boolean.TRUE);
            basicCalcolaImportoDisponibileNC(context, riga, vecchioTotale);
            doSelectObbligazioni(context);
        } catch (Throwable t) {
            riga.setFl_iva_forzata(flForzaIVA);
            riga.setIm_iva(impIVA);
            riga.calcolaCampiDiRiga();
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doFreeSearchFatturaEstera(ActionContext context) {

        FormField field = getFormField(context, "main.fatturaEstera");
        try {
            OggettoBulk fp = ((CRUDFatturaPassivaBP) context.getBusinessProcess()).createEmptyModelForFreeSearch(context);
            return freeSearch(context, field, fp);
        } catch (BusinessProcessException e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di ricerca libera del searchtool "fornitore"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doFreeSearchFornitore(ActionContext context) {
        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
        TerzoBulk tb = new TerzoBulk();
        tb.setAnagrafico(new AnagraficoBulk());
        return freeSearch(context, bp.getFormField("fornitore"), tb);
    }

    /**
     * Creo e apro una nuova nota di credito. Viene eseguito rollback prima di
     * questa operazione per permettere il rilascio della fattura!
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doGeneraNotaDiCredito(ActionContext context) {

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
     * Richiesta la quadratura tra dettagli e testata
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doInventariaDettagli(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            fillModel(context);

            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            controllaQuadraturaConti(context, fattura);
            java.util.List dettagliDaInventariare = getDettagliDaInventariare(context, bp.getDettaglio().getDetails().iterator());
            if (dettagliDaInventariare != null && !dettagliDaInventariare.isEmpty())
                //if (fattura.getCarichiInventarioHash() != null && !fattura.getCarichiInventarioHash().isEmpty())
                //return openConfirm(context,"Alcuni dettagli sono già stati inventariati. Se si procede tutti i buoni carico/scarico già creati verranno persi. Continuare?",it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO,"doConfermaInventaria");
                return basicDoInventariaDettagli(context);
            else
                bp.setMessage("Nessun dettaglio è inventariabile o tutti i dettagli inventariabili sono già stati caricati!");
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }

    }

    /**
     * Inventaria i dettagli per aumento di valore
     * Richiesta la quadratura tra dettagli e testata
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doInventariaDettagliPerAumentoValore(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            fillModel(context);

            controllaQuadraturaConti(context, (Fattura_passivaBulk) bp.getModel());

            java.util.List dettagliDaInventariare = getDettagliDaInventariare(context, bp.getDettaglio().getDetails().iterator());
            if (dettagliDaInventariare != null && !dettagliDaInventariare.isEmpty()) {

                AssBeneFatturaBP ibp = (AssBeneFatturaBP) context.getUserInfo().createBusinessProcess(context, "AssBeneFatturaBP", new Object[]{"MRSWTh"});
                Ass_inv_bene_fatturaBulk associa = new Ass_inv_bene_fatturaBulk();
                associa.setLocal_transactionID(ibp.getLocalTransactionID(context.getUserContext(), true));

                associa.completeFrom(dettagliDaInventariare);
                associa.setPerAumentoValore(Boolean.TRUE);
                //???????
                // Crea ed inizializza il Buono di Carico
                Buono_carico_scaricoBulk buonoC = new Buono_carico_scaricoBulk();
                buonoC.setToBeCreated();
                buonoC.setByFatturaPerAumentoValore(Boolean.TRUE);
                buonoC.setTi_documento(Buono_carico_scaricoBulk.CARICO);
                buonoC = (Buono_carico_scaricoBulk) ibp.createComponentSession().inizializzaBulkPerInserimento(context.getUserContext(), buonoC);
                associa.setTest_buono(buonoC);
                associa.setInventario(buonoC.getInventario());
                try {

                    associa.getTest_buono().setPg_buono_c_s(((NumerazioneTempBuonoComponentSession) EJBCommonServices.createEJB(
                            "CNRINVENTARIO01_EJB_NumerazioneTempBuonoComponentSession",
                            NumerazioneTempBuonoComponentSession.class)).getNextTempPG(context.getUserContext(), associa.getTest_buono()));

                } catch (Throwable e) {
                    throw new ComponentException(e);
                }

                //

                ibp.setModel(context, associa);
                ibp.setDirty(false);
                ibp.setPerAumentoValore(Boolean.TRUE);

                context.addHookForward("bringback", this, "doBringBackAssociaInventario");
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

    public Forward doBeni_coll(ActionContext context) {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);

        Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();

        try {
            it.cnr.jada.util.RemoteIterator ri = ((FatturaPassivaComponentSession) bp.createComponentSession()).selectBeniFor(context.getUserContext(), fattura);
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

    /**
     * Richiede all'obbligazione di modificare in automatico la sua scadenza (quella
     * selezionata) portando la stessa ad importo pari alla sommatoria degli importi
     * di riga dei dettagli associati. Aggiorna la mappa dei saldi per le variazioni
     * subite dall'obbligazione
     *
     * @param context L'ActionContext della richiesta
     * @param prefix
     * @return Il Forward alla pagina di risposta
     */
    public Forward doModificaScadenzaInAutomatico(ActionContext context, String prefix) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            fillModel(context);
            Fattura_passiva_IBulk fatturaPassiva = (Fattura_passiva_IBulk) bp.getModel();
            controllaQuadraturaConti(context, fatturaPassiva);

            Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel();

            if (scadenza == null)
                throw new it.cnr.jada.comp.ApplicationException("Selezionare l'impegno da modificare in automatico!");
            java.util.Vector righeAssociate = (java.util.Vector) fatturaPassiva.getFattura_passiva_obbligazioniHash().get(scadenza);
            if (righeAssociate == null || righeAssociate.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Associare dei dettagli prima di aggiornare in automatico la scadenza impegno!");
            if (bp.isDeleting() &&
                    !bp.isViewing() &&
                    !it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).equals(scadenza.getEsercizio()))
                throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata appartiene all'esercizio " + scadenza.getEsercizio().intValue() + "! Operazione annullata.");

            ObbligazioneAbstractComponentSession h = CRUDVirtualObbligazioneBP.getVirtualComponentSession(context, true);

            try {
                scadenza = (Obbligazione_scadenzarioBulk) h.modificaScadenzaInAutomatico(
                        context.getUserContext(),
                        scadenza,
                        getImportoPerAggiornamentoScadenzaInAutomatico(
                                context,
                                scadenza,
                                fatturaPassiva,
                                new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)),
                        false);
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

            Forward fwd = basicDoBringBackOpenObbligazioniWindow(context, scadenza);

            bp.getObbligazioniController().getSelection().clear();
            bp.getObbligazioniController().setModelIndex(context, -1);
            bp.getObbligazioniController().setModelIndex(context, it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(bp.getObbligazioniController().getDetails(), scadenza));
            bp.setDirty(true);

            return fwd;
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la variazione manuale del valore del cambio e ricalcola tutti i totali
     */
    public Forward doOnChangeModified(ActionContext context) {
        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
            Fattura_passivaBulk fp = (Fattura_passivaBulk) bp.getModel();
            it.cnr.jada.bulk.PrimaryKeyHashtable vecchiTotali = new it.cnr.jada.bulk.PrimaryKeyHashtable();
            if (fp instanceof Fattura_passiva_IBulk) {
                for (java.util.Iterator i = fp.getFattura_passiva_dettColl().iterator(); i.hasNext(); ) {
                    Fattura_passiva_rigaIBulk dettaglio = (Fattura_passiva_rigaIBulk) i.next();
                    java.math.BigDecimal vecchioTotale = dettaglio.getIm_imponibile().add(dettaglio.getIm_iva());
                    vecchioTotale = vecchioTotale.add(dettaglio.getIm_totale_addebiti());
                    vecchiTotali.put(dettaglio, vecchioTotale);
                }
            }
            java.math.BigDecimal vecchioCambio = fp.getCambio();
            fillModel(context);
            if (fp != null) {
                java.math.BigDecimal cambioAttuale = fp.getCambio();
                if (cambioAttuale == null)
                    fp.setCambio((cambioAttuale = new java.math.BigDecimal(0)));
                cambioAttuale = cambioAttuale.setScale(4, java.math.BigDecimal.ROUND_HALF_UP);
                fp.setCambio(cambioAttuale);
                if (cambioAttuale.compareTo(new java.math.BigDecimal(0)) == 0) {
                    fp.setCambio(vecchioCambio);
                    throw new it.cnr.jada.comp.ApplicationException("Non è stato inserito un cambio valido (>0)");
                }
                if (fp.getObbligazioniHash() != null && !fp.getObbligazioniHash().isEmpty())
                    bp.setErrorMessage("Attenzione: si ricorda che modificando il valore del cambio è necessario riportare in quadratura le scadenze delle obbligazioni!");
                if (fp.getAccertamentiHash() != null && !fp.getAccertamentiHash().isEmpty())
                    bp.setErrorMessage("Attenzione: si ricorda che modificando il valore del cambio è necessario riportare in quadratura le scadenze degli accertamenti!");
                fp.aggiornaImportiTotali();
                basicDoCalcolaTotaleFatturaFornitoreInEur(fp);
                for (java.util.Iterator i = fp.getFattura_passiva_dettColl().iterator(); i.hasNext(); ) {
                    Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk) i.next();
                    if (dettaglio instanceof Fattura_passiva_rigaIBulk) {
                        java.math.BigDecimal vecchioTotale = (java.math.BigDecimal) vecchiTotali.get(dettaglio);
                        if (vecchioTotale == null)
                            vecchioTotale = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
                        basicCalcolaImportoDisponibileNC(context, (Fattura_passiva_rigaIBulk) dettaglio, vecchioTotale);
                    }
                }
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * Gestisce l'eccezione CheckDisponibilitaCassaFailed generata dall'obbligazione
     * mantenendo traccia della scelta di conferma o annullamento dell'operazione
     * da parte dell'utente
     */

    public Forward doOnCheckDisponibilitaCassaFailed(
            ActionContext context,
            int option) {

        if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
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
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
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
     * Gestisce il cambiamento della data competenza coge 'a'
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doOnDataCompetenzaACogeChange(ActionContext context) {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
        Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
        java.sql.Timestamp competenzaABck = fattura.getDt_a_competenza_coge();
        try {
            fillModel(context);
            java.sql.Timestamp competenzaDa = fattura.getDt_da_competenza_coge();
            java.sql.Timestamp competenzaA = fattura.getDt_a_competenza_coge();
            if (competenzaA != null) {
                java.util.GregorianCalendar tsOdiernoGregorian = new GregorianCalendar();
                tsOdiernoGregorian.setTime(new Date(competenzaA.getTime()));

                Integer esercizioCompetenzaA = new Integer(tsOdiernoGregorian.get(java.util.GregorianCalendar.YEAR));
                String cds = fattura.getCd_cds();

                if (competenzaA != null && competenzaDa != null)
                    if (!competenzaDa.equals(competenzaA) && !competenzaDa.before(competenzaA))
                        throw new it.cnr.jada.comp.ApplicationException("La data \"competenza da\" deve essere precedente o uguale a \"competenza a\"!");
                if (((FatturaPassivaComponentSession) bp.createComponentSession()).isEsercizioChiusoPerDataCompetenza(context.getUserContext(), esercizioCompetenzaA, cds) && !fattura.getStato_coge().equals("NON_PROCESSARE_IN_COGE"))
                    throw new it.cnr.jada.comp.ApplicationException("Le date \"Competenza da\" e \"Competenza a\" non possono appartenere ad un esercizio chiuso");
            }
            bp.setModel(context, fattura);
            return context.findDefaultForward();
        } catch (Throwable t) {
            fattura.setDt_a_competenza_coge(competenzaABck);
            try {
                bp.setModel(context, fattura);
            } catch (BusinessProcessException e) {
                return handleException(context, e);
            }
            return handleException(context, t);
        }
    }

    /**
     * Gestisce il cambiamento della data competenza coge 'da' *
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doOnDataCompetenzaDaCogeChange(ActionContext context) {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
        Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
        java.sql.Timestamp competenzaDaBck = fattura.getDt_da_competenza_coge();
        try {
            fillModel(context);
            java.sql.Timestamp competenzaDa = fattura.getDt_da_competenza_coge();
            java.sql.Timestamp competenzaA = fattura.getDt_a_competenza_coge();
            if (competenzaDa != null) {
                java.util.GregorianCalendar tsOdiernoGregorian = new GregorianCalendar();
                tsOdiernoGregorian.setTime(new Date(competenzaDa.getTime()));

                Integer esercizioCompetenzaDa = new Integer(tsOdiernoGregorian.get(java.util.GregorianCalendar.YEAR));
                String cds = fattura.getCd_cds();

                if (competenzaA != null && competenzaDa != null)
                    if (!competenzaDa.equals(competenzaA) && !competenzaDa.before(competenzaA))
                        throw new it.cnr.jada.comp.ApplicationException("La data \"competenza a\" deve essere successiva o uguale a \"competenza da\"!");
                if (((FatturaPassivaComponentSession) bp.createComponentSession()).isEsercizioChiusoPerDataCompetenza(context.getUserContext(), esercizioCompetenzaDa, cds) && !fattura.getStato_coge().equals("NON_PROCESSARE_IN_COGE"))
                    throw new it.cnr.jada.comp.ApplicationException("Le date \"Competenza da\" e \"Competenza a\" non possono appartenere ad un esercizio chiuso");
            }
            bp.setModel(context, fattura);
            return context.findDefaultForward();
        } catch (Throwable t) {
            fattura.setDt_da_competenza_coge(competenzaDaBck);
            try {
                bp.setModel(context, fattura);
            } catch (BusinessProcessException e) {
                return handleException(context, e);
            }
            return handleException(context, t);
        }
    }

    /**
     * Gestisce il cambiamento del flag autofattura ricaricando i sezionali
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOnFlAutofatturaChange(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            Boolean intraUE = fattura.getFl_intra_ue();
            Boolean extraUE = fattura.getFl_extra_ue();
            Boolean sanMarinoCI = fattura.getFl_san_marino_con_iva();
            Boolean sanMarinoSI = fattura.getFl_san_marino_senza_iva();
            Boolean spedizioniere = fattura.getFl_spedizioniere();
            Boolean bollaDoganale = fattura.getFl_bolla_doganale();
            String fattServizi = fattura.getTi_bene_servizio();
            fillModel(context);
            try {
                if (!bp.isSearching()) {
                    basicDoOnIstituzionaleCommercialeChange(context, fattura);
                    bp.setModel(context, fattura);
                }
                return context.findDefaultForward();
            } catch (it.cnr.jada.comp.ComponentException e) {
                fattura.setFl_intra_ue(intraUE);
                fattura.setFl_extra_ue(extraUE);
                fattura.setFl_san_marino_con_iva(sanMarinoCI);
                fattura.setFl_san_marino_senza_iva(sanMarinoSI);
                fattura.setFl_spedizioniere(spedizioniere);
                fattura.setFl_bolla_doganale(bollaDoganale);
                fattura.setTi_bene_servizio(fattServizi);
                bp.setModel(context, fattura);
                throw e;
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Gestisce il cambiamento del flag bolla doganale
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOnFlBollaDoganaleChange(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            Boolean intraUE = fattura.getFl_intra_ue();
            Boolean extraUE = fattura.getFl_extra_ue();
            Boolean sanMarinoCI = fattura.getFl_san_marino_con_iva();
            Boolean sanMarinoSI = fattura.getFl_san_marino_senza_iva();
            Boolean spedizioniere = fattura.getFl_spedizioniere();
            Boolean bollaDoganale = fattura.getFl_bolla_doganale();
            String spesa = fattura.getStato_pagamento_fondo_eco();
            Boolean autof = fattura.getFl_autofattura();
            String fattServizi = fattura.getTi_bene_servizio();
            Boolean merceextraUE = fattura.getFl_merce_extra_ue();
            Boolean merceintraUE = fattura.getFl_merce_intra_ue();
            Boolean liqDiff = fattura.getFl_liquidazione_differita();
            fillModel(context);
            try {
                if (fattura.isGestione_doc_ele() &&
                        (fattura.getDt_fattura_fornitore() != null && !(fattura.getDt_fattura_fornitore().compareTo(fattura.getDataInizioFatturaElettronica()) < 0)) &&
                        !fattura.isElettronica() &&
                        !fattura.isEstera() &&
                        !fattura.isSanMarinoSenzaIVA() &&
                        !fattura.isSanMarinoConIVA() &&
                        !fattura.isBollaDoganale()) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                    throw new it.cnr.jada.comp.ApplicationException("Non è possibile registrare una fattura che non sia elettronica, che non sia estera e che abbia data di emissione uguale o successiva al " + sdf.format(fattura.getDataInizioFatturaElettronica()) + "!");
                }
                if (Boolean.TRUE.equals(fattura.getFl_bolla_doganale())) {
                    fattura.setFl_intra_ue(Boolean.FALSE);
                    fattura.setFl_extra_ue(Boolean.FALSE);
                    fattura.setFl_san_marino_con_iva(Boolean.FALSE);
                    fattura.setFl_san_marino_senza_iva(Boolean.FALSE);
                    fattura.setFl_spedizioniere(Boolean.FALSE);
                    fattura.setStato_pagamento_fondo_eco(fattura.getStato_pagamento_fondo_eco());
                    //fattura.setStato_pagamento_fondo_eco(fattura.NO_FONDO_ECO);
                    fattura.setFl_autofattura(Boolean.FALSE);
                    fattura.setFl_merce_extra_ue(Boolean.FALSE);
                    fattura.setFl_merce_intra_ue(Boolean.FALSE);
                    fattura.setAutoFatturaNeeded(false);
                    fattura.setFl_liquidazione_differita(Boolean.FALSE);
                    if (fattura instanceof Fattura_passiva_IBulk)
                        doBlankSearchFatturaEstera(context, (Fattura_passiva_IBulk) fattura);
                    if (!fattura.isDefaultValuta())
                        fattura = doSelezionaValutaDefault(context, fattura);
                } else if (fattura instanceof Fattura_passiva_IBulk)
                    ((Fattura_passiva_IBulk) fattura).setFattura_estera(null);
                fattura.setTi_bene_servizio(null);
                basicDoOnIstituzionaleCommercialeChange(context, fattura);
                bp.setModel(context, fattura);
                return context.findDefaultForward();
            } catch (it.cnr.jada.comp.ComponentException e) {
                fattura.setFl_intra_ue(intraUE);
                fattura.setFl_extra_ue(extraUE);
                fattura.setFl_san_marino_con_iva(sanMarinoCI);
                fattura.setFl_san_marino_senza_iva(sanMarinoSI);
                fattura.setFl_bolla_doganale(bollaDoganale);
                fattura.setFl_spedizioniere(spedizioniere);
                fattura.setStato_pagamento_fondo_eco(spesa);
                fattura.setFl_autofattura(autof);
                fattura.setTi_bene_servizio(fattServizi);
                fattura.setFl_merce_extra_ue(merceextraUE);
                fattura.setFl_merce_intra_ue(merceintraUE);
                fattura.setFl_liquidazione_differita(liqDiff);
                bp.setModel(context, fattura);
                throw e;
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Gestisce il cambiamento del flag Extra UE ricaricando i sezionali
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOnFlExtraUEChange(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            Boolean intraUE = fattura.getFl_intra_ue();
            Boolean extraUE = fattura.getFl_extra_ue();
            Boolean sanMarinoCI = fattura.getFl_san_marino_con_iva();
            Boolean sanMarinoSI = fattura.getFl_san_marino_senza_iva();
            Boolean spedizioniere = fattura.getFl_spedizioniere();
            Boolean bollaDoganale = fattura.getFl_bolla_doganale();
            String spesa = fattura.getStato_pagamento_fondo_eco();
            Boolean autof = fattura.getFl_autofattura();
            String fattServizi = fattura.getTi_bene_servizio();
            Boolean merceextraUE = fattura.getFl_merce_extra_ue();
            Boolean merceintraUE = fattura.getFl_merce_intra_ue();
            Boolean liqDiff = fattura.getFl_liquidazione_differita();
            fillModel(context);

            try {
                if (fattura.isGestione_doc_ele() &&
                        (fattura.getDt_fattura_fornitore() != null && !(fattura.getDt_fattura_fornitore().compareTo(fattura.getDataInizioFatturaElettronica()) < 0)) &&
                        !fattura.isElettronica() &&
                        !fattura.isEstera() &&
                        !fattura.isSanMarinoSenzaIVA() &&
                        !fattura.isSanMarinoConIVA() &&
                        !fattura.isBollaDoganale()) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                    throw new it.cnr.jada.comp.ApplicationException("Non è possibile registrare una fattura che non sia elettronica, che non sia estera e che abbia data di emissione uguale o successiva al " + sdf.format(fattura.getDataInizioFatturaElettronica()) + "!");
                }

                if (Boolean.TRUE.equals(fattura.getFl_extra_ue())) {
                    fattura.setFl_intra_ue(Boolean.FALSE);
                    fattura.setFl_san_marino_con_iva(Boolean.FALSE);
                    fattura.setFl_san_marino_senza_iva(Boolean.FALSE);
                    fattura.setFl_bolla_doganale(Boolean.FALSE);
                    fattura.setFl_spedizioniere(Boolean.FALSE);
                    fattura.setStato_pagamento_fondo_eco(fattura.NO_FONDO_ECO);
                    fattura.setFl_merce_extra_ue(Boolean.FALSE);
                    fattura.setFl_merce_intra_ue(Boolean.FALSE);
                    fattura.setFl_liquidazione_differita(Boolean.FALSE);
                    if (!fattura.isPromiscua())
                        fattura.setTi_bene_servizio(fattura.FATTURA_DI_BENI);
                    fattura.setFl_autofattura((fattura.isFatturaDiServizi()) ? Boolean.TRUE : Boolean.FALSE);
                    //fattura.setAutoFatturaNeeded(fattura.isFatturaDiServizi());
                } else {
                    fattura.setTi_bene_servizio(null);
                    fattura.setFl_autofattura(Boolean.FALSE);
                    //fattura.setAutoFatturaNeeded(false);
                    if (!fattura.isDefaultValuta())
                        fattura = doSelezionaValutaDefault(context, fattura);
                }

                if (fattura instanceof Fattura_passiva_IBulk)
                    ((Fattura_passiva_IBulk) fattura).setFattura_estera(null);
                basicDoOnIstituzionaleCommercialeChange(context, fattura);
                bp.setModel(context, fattura);
                return context.findDefaultForward();
            } catch (it.cnr.jada.comp.ComponentException e) {
                fattura.setFl_intra_ue(intraUE);
                fattura.setFl_extra_ue(extraUE);
                fattura.setFl_san_marino_con_iva(sanMarinoCI);
                fattura.setFl_san_marino_senza_iva(sanMarinoSI);
                fattura.setFl_bolla_doganale(bollaDoganale);
                fattura.setFl_spedizioniere(spedizioniere);
                fattura.setStato_pagamento_fondo_eco(spesa);
                fattura.setFl_autofattura(autof);
                fattura.setTi_bene_servizio(fattServizi);
                fattura.setFl_merce_extra_ue(merceextraUE);
                fattura.setFl_merce_intra_ue(merceintraUE);
                fattura.setFl_liquidazione_differita(liqDiff);
                bp.setModel(context, fattura);
                throw e;
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Gestisce il cambiamento del flag Intra UE ricaricando i sezionali
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOnFlIntraUEChange(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            Boolean intraUE = fattura.getFl_intra_ue();
            Boolean extraUE = fattura.getFl_extra_ue();
            Boolean sanMarinoCI = fattura.getFl_san_marino_con_iva();
            Boolean sanMarinoSI = fattura.getFl_san_marino_senza_iva();
            Boolean spedizioniere = fattura.getFl_spedizioniere();
            Boolean bollaDoganale = fattura.getFl_bolla_doganale();
            String spesa = fattura.getStato_pagamento_fondo_eco();
            Boolean autof = fattura.getFl_autofattura();
            String fattServizi = fattura.getTi_bene_servizio();
            Boolean merceextraUE = fattura.getFl_merce_extra_ue();
            Boolean merceintraUE = fattura.getFl_merce_intra_ue();
            Boolean liqDiff = fattura.getFl_liquidazione_differita();
            fillModel(context);

            try {
                if (fattura.isGestione_doc_ele() &&
                        (fattura.getDt_fattura_fornitore() != null && !(fattura.getDt_fattura_fornitore().compareTo(fattura.getDataInizioFatturaElettronica()) < 0)) &&
                        !fattura.isElettronica() &&
                        !fattura.isEstera() &&
                        !fattura.isSanMarinoSenzaIVA() &&
                        !fattura.isSanMarinoConIVA() &&
                        !fattura.isBollaDoganale()) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                    throw new it.cnr.jada.comp.ApplicationException("Non è possibile registrare una fattura che non sia elettronica, che non sia estera e che abbia data di emissione uguale o successiva al " + sdf.format(fattura.getDataInizioFatturaElettronica()) + "!");
                }

                if (Boolean.TRUE.equals(fattura.getFl_intra_ue())) {
                    fattura.setFl_extra_ue(Boolean.FALSE);
                    fattura.setFl_san_marino_con_iva(Boolean.FALSE);
                    fattura.setFl_san_marino_senza_iva(Boolean.FALSE);
                    fattura.setFl_bolla_doganale(Boolean.FALSE);
                    fattura.setFl_spedizioniere(Boolean.FALSE);
                    fattura.setFl_merce_extra_ue(Boolean.FALSE);
                    fattura.setFl_merce_intra_ue(Boolean.FALSE);
                    fattura.setStato_pagamento_fondo_eco(fattura.NO_FONDO_ECO);
                    fattura.setFl_liquidazione_differita(Boolean.FALSE);
                    if (!fattura.isPromiscua())
                        fattura.setTi_bene_servizio(fattura.FATTURA_DI_BENI);
                    if (fattura.isCommerciale()) {
                        fattura.setFl_autofattura(Boolean.TRUE);
                        fattura.setAutoFatturaNeeded(true);
                    }
                } else {
                    fattura.setTi_bene_servizio(null);
                    fattura.setFl_autofattura(Boolean.FALSE);
                    fattura.setAutoFatturaNeeded(false);
                    if (!fattura.isDefaultValuta())
                        fattura = doSelezionaValutaDefault(context, fattura);
                }
                if (fattura instanceof Fattura_passiva_IBulk)
                    ((Fattura_passiva_IBulk) fattura).setFattura_estera(null);
                basicDoOnIstituzionaleCommercialeChange(context, fattura);
                bp.setModel(context, fattura);
                return context.findDefaultForward();
            } catch (it.cnr.jada.comp.ComponentException e) {
                fattura.setFl_intra_ue(intraUE);
                fattura.setFl_extra_ue(extraUE);
                fattura.setFl_san_marino_con_iva(sanMarinoCI);
                fattura.setFl_san_marino_senza_iva(sanMarinoSI);
                fattura.setFl_bolla_doganale(bollaDoganale);
                fattura.setFl_spedizioniere(spedizioniere);
                fattura.setStato_pagamento_fondo_eco(spesa);
                fattura.setFl_autofattura(autof);
                fattura.setTi_bene_servizio(fattServizi);
                fattura.setFl_merce_extra_ue(merceextraUE);
                fattura.setFl_merce_intra_ue(merceintraUE);
                fattura.setFl_liquidazione_differita(liqDiff);
                bp.setModel(context, fattura);
                throw e;
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Gestisce il cambiamento del flag San Marino con IVA UE ricaricando i sezionali
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOnFlSanMarinoConIVAChange(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            Boolean intraUE = fattura.getFl_intra_ue();
            Boolean extraUE = fattura.getFl_extra_ue();
            Boolean sanMarinoCI = fattura.getFl_san_marino_con_iva();
            Boolean sanMarinoSI = fattura.getFl_san_marino_senza_iva();
            Boolean spedizioniere = fattura.getFl_spedizioniere();
            Boolean bollaDoganale = fattura.getFl_bolla_doganale();
            String spesa = fattura.getStato_pagamento_fondo_eco();
            Boolean autof = fattura.getFl_autofattura();
            String fattServizi = fattura.getTi_bene_servizio();
            Boolean merceextraUE = fattura.getFl_merce_extra_ue();
            Boolean merceintraUE = fattura.getFl_merce_intra_ue();
            Boolean liqDiff = fattura.getFl_liquidazione_differita();
            fillModel(context);
            try {
                if (fattura.isGestione_doc_ele() &&
                        (fattura.getDt_fattura_fornitore() != null && !(fattura.getDt_fattura_fornitore().compareTo(fattura.getDataInizioFatturaElettronica()) < 0)) &&
                        !fattura.isElettronica() &&
                        !fattura.isEstera() &&
                        !fattura.isSanMarinoSenzaIVA() &&
                        !fattura.isSanMarinoConIVA() &&
                        !fattura.isBollaDoganale()) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                    throw new it.cnr.jada.comp.ApplicationException("Non è possibile registrare una fattura che non sia elettronica, che non sia estera e che abbia data di emissione uguale o successiva al " + sdf.format(fattura.getDataInizioFatturaElettronica()) + "!");
                }
                if (Boolean.TRUE.equals(fattura.getFl_san_marino_con_iva())) {
                    fattura.setFl_intra_ue(Boolean.FALSE);
                    fattura.setFl_extra_ue(Boolean.FALSE);
                    fattura.setFl_san_marino_senza_iva(Boolean.FALSE);
                    fattura.setFl_bolla_doganale(Boolean.FALSE);
                    fattura.setFl_spedizioniere(Boolean.FALSE);
                    fattura.setFl_autofattura(Boolean.FALSE);
                    fattura.setFl_merce_extra_ue(Boolean.FALSE);
                    fattura.setFl_merce_intra_ue(Boolean.FALSE);
                    fattura.setStato_pagamento_fondo_eco(fattura.NO_FONDO_ECO);
                    fattura.setAutoFatturaNeeded(false);
                    fattura.setFl_liquidazione_differita(Boolean.FALSE);
                }
                if (!fattura.isDefaultValuta())
                    fattura = doSelezionaValutaDefault(context, fattura);
                fattura.setTi_bene_servizio(null);
                if (fattura instanceof Fattura_passiva_IBulk)
                    ((Fattura_passiva_IBulk) fattura).setFattura_estera(null);
                basicDoOnIstituzionaleCommercialeChange(context, fattura);
                bp.setModel(context, fattura);
                return context.findDefaultForward();
            } catch (it.cnr.jada.comp.ComponentException e) {
                fattura.setFl_intra_ue(intraUE);
                fattura.setFl_extra_ue(extraUE);
                fattura.setFl_san_marino_con_iva(sanMarinoCI);
                fattura.setFl_san_marino_senza_iva(sanMarinoSI);
                fattura.setFl_bolla_doganale(bollaDoganale);
                fattura.setFl_spedizioniere(spedizioniere);
                fattura.setFl_autofattura(autof);
                fattura.setTi_bene_servizio(fattServizi);
                fattura.setFl_merce_extra_ue(merceextraUE);
                fattura.setFl_merce_intra_ue(merceintraUE);
                fattura.setFl_liquidazione_differita(liqDiff);
                bp.setModel(context, fattura);
                throw e;
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Gestisce il cambiamento del flag San Marino senza IVA UE ricaricando i sezionali
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOnFlSanMarinoSenzaIVAChange(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            Boolean intraUE = fattura.getFl_intra_ue();
            Boolean extraUE = fattura.getFl_extra_ue();
            Boolean sanMarinoCI = fattura.getFl_san_marino_con_iva();
            Boolean sanMarinoSI = fattura.getFl_san_marino_senza_iva();
            Boolean spedizioniere = fattura.getFl_spedizioniere();
            Boolean bollaDoganale = fattura.getFl_bolla_doganale();
            String spesa = fattura.getStato_pagamento_fondo_eco();
            Boolean autof = fattura.getFl_autofattura();
            String fattServizi = fattura.getTi_bene_servizio();
            Boolean merceextraUE = fattura.getFl_merce_extra_ue();
            Boolean merceintraUE = fattura.getFl_merce_intra_ue();
            Boolean liqDiff = fattura.getFl_liquidazione_differita();
            fillModel(context);

            try {
                if (fattura.isGestione_doc_ele() &&
                        (fattura.getDt_fattura_fornitore() != null && !(fattura.getDt_fattura_fornitore().compareTo(fattura.getDataInizioFatturaElettronica()) < 0)) &&
                        !fattura.isElettronica() &&
                        !fattura.isEstera() &&
                        !fattura.isSanMarinoSenzaIVA() &&
                        !fattura.isSanMarinoConIVA() &&
                        !fattura.isBollaDoganale()) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                    throw new it.cnr.jada.comp.ApplicationException("Non è possibile registrare una fattura che non sia elettronica, che non sia estera e che abbia data di emissione uguale o successiva al " + sdf.format(fattura.getDataInizioFatturaElettronica()) + "!");
                }

                if (Boolean.TRUE.equals(fattura.getFl_san_marino_senza_iva())) {
                    fattura.setFl_intra_ue(Boolean.FALSE);
                    fattura.setFl_extra_ue(Boolean.FALSE);
                    fattura.setFl_san_marino_con_iva(Boolean.FALSE);
                    fattura.setFl_bolla_doganale(Boolean.FALSE);
                    fattura.setFl_spedizioniere(Boolean.FALSE);
                    fattura.setFl_merce_extra_ue(Boolean.FALSE);
                    fattura.setFl_merce_intra_ue(Boolean.FALSE);
                    fattura.setStato_pagamento_fondo_eco(fattura.NO_FONDO_ECO);
                    fattura.setFl_liquidazione_differita(Boolean.FALSE);
                    if (!fattura.isPromiscua())
                        fattura.setTi_bene_servizio(fattura.FATTURA_DI_BENI);
                    if (fattura.isCommerciale()) {
                        fattura.setFl_autofattura(Boolean.TRUE);
                        fattura.setFl_split_payment(Boolean.FALSE);
                        //fattura.setAutoFatturaNeeded(true);
                    }
                } else {
                    fattura.setTi_bene_servizio(null);
                    fattura.setFl_autofattura(Boolean.FALSE);
                    fattura.setAutoFatturaNeeded(false);
                }
                if (!fattura.isDefaultValuta())
                    fattura = doSelezionaValutaDefault(context, fattura);
                if (fattura instanceof Fattura_passiva_IBulk)
                    ((Fattura_passiva_IBulk) fattura).setFattura_estera(null);
                basicDoOnIstituzionaleCommercialeChange(context, fattura);
                bp.setModel(context, fattura);
                return context.findDefaultForward();
            } catch (it.cnr.jada.comp.ComponentException e) {
                fattura.setFl_intra_ue(intraUE);
                fattura.setFl_extra_ue(extraUE);
                fattura.setFl_san_marino_con_iva(sanMarinoCI);
                fattura.setFl_san_marino_senza_iva(sanMarinoSI);
                fattura.setFl_bolla_doganale(bollaDoganale);
                fattura.setFl_spedizioniere(spedizioniere);
                fattura.setStato_pagamento_fondo_eco(spesa);
                fattura.setFl_autofattura(autof);
                fattura.setTi_bene_servizio(fattServizi);
                fattura.setFl_merce_extra_ue(merceextraUE);
                fattura.setFl_merce_intra_ue(merceintraUE);
                fattura.setFl_liquidazione_differita(liqDiff);
                bp.setModel(context, fattura);
                throw e;
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Gestisce il cambiamento del flag spedizioniere ricaricando i sezionali
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOnFlSpedizioniereChange(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            Boolean intraUE = fattura.getFl_intra_ue();
            Boolean extraUE = fattura.getFl_extra_ue();
            Boolean sanMarinoCI = fattura.getFl_san_marino_con_iva();
            Boolean sanMarinoSI = fattura.getFl_san_marino_senza_iva();
            Boolean spedizioniere = fattura.getFl_spedizioniere();
            Boolean bollaDoganale = fattura.getFl_bolla_doganale();
            String spesa = fattura.getStato_pagamento_fondo_eco();
            Boolean autof = fattura.getFl_autofattura();
            String fattServizi = fattura.getTi_bene_servizio();
            Boolean merceextraUE = fattura.getFl_merce_extra_ue();
            Boolean merceintraUE = fattura.getFl_merce_intra_ue();
            fillModel(context);
            try {
                if (Boolean.TRUE.equals(fattura.getFl_spedizioniere())) {
                    fattura.setFl_extra_ue(Boolean.FALSE);
                    fattura.setFl_intra_ue(Boolean.FALSE);
                    fattura.setFl_san_marino_con_iva(Boolean.FALSE);
                    fattura.setFl_san_marino_senza_iva(Boolean.FALSE);
                    fattura.setFl_bolla_doganale(Boolean.FALSE);
                    fattura.setStato_pagamento_fondo_eco(fattura.getStato_pagamento_fondo_eco());
                    //fattura.setStato_pagamento_fondo_eco(fattura.NO_FONDO_ECO);
                    fattura.setFl_autofattura(Boolean.FALSE);
                    fattura.setAutoFatturaNeeded(false);
                    fattura.setFl_merce_extra_ue(Boolean.FALSE);
                    fattura.setFl_merce_intra_ue(Boolean.FALSE);
                    if (fattura instanceof Fattura_passiva_IBulk)
                        doBlankSearchFatturaEstera(context, (Fattura_passiva_IBulk) fattura);
                    if (!fattura.isDefaultValuta())
                        fattura = doSelezionaValutaDefault(context, fattura);
                } else if (fattura instanceof Fattura_passiva_IBulk)
                    ((Fattura_passiva_IBulk) fattura).setFattura_estera(null);

                fattura.setTi_bene_servizio(null);
                basicDoOnIstituzionaleCommercialeChange(context, fattura);
                bp.setModel(context, fattura);
                return context.findDefaultForward();
            } catch (it.cnr.jada.comp.ComponentException e) {
                fattura.setFl_intra_ue(intraUE);
                fattura.setFl_extra_ue(extraUE);
                fattura.setFl_san_marino_con_iva(sanMarinoCI);
                fattura.setFl_san_marino_senza_iva(sanMarinoSI);
                fattura.setFl_bolla_doganale(bollaDoganale);
                fattura.setFl_spedizioniere(spedizioniere);
                fattura.setStato_pagamento_fondo_eco(spesa);
                fattura.setFl_autofattura(autof);
                fattura.setTi_bene_servizio(fattServizi);
                fattura.setFl_merce_extra_ue(merceextraUE);
                fattura.setFl_merce_intra_ue(merceintraUE);
                bp.setModel(context, fattura);
                throw e;
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Gestisce il cambiamento del tipo sezionale ricaricandoli
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOnIstituzionaleCommercialeChange(ActionContext context) {
        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();

            if (fattura.existARowInventoried())
                throw new it.cnr.jada.comp.ApplicationException("Alcuni dettagli sono già stati associati all'inventario. Modifica non possibile!");

            java.util.Collection sezionaliOld = fattura.getSezionali();
            Boolean intraUE = fattura.getFl_intra_ue();
            Boolean extraUE = fattura.getFl_extra_ue();
            Boolean sanMarinoCI = fattura.getFl_san_marino_con_iva();
            Boolean sanMarinoSI = fattura.getFl_san_marino_senza_iva();
            Boolean spedizioniere = fattura.getFl_spedizioniere();
            Boolean bollaDoganale = fattura.getFl_bolla_doganale();
            Boolean autof = fattura.getFl_autofattura();
            String fattServizi = fattura.getTi_bene_servizio();
            Boolean merceextraUE = fattura.getFl_merce_extra_ue();
            Boolean merceintraUE = fattura.getFl_merce_intra_ue();
            Boolean liqDiff = fattura.getFl_liquidazione_differita();
            fillModel(context);
            try {
                fattura.setFl_intra_ue(Boolean.FALSE);
                fattura.setFl_extra_ue(Boolean.FALSE);
                fattura.setFl_san_marino_con_iva(Boolean.FALSE);
                fattura.setFl_san_marino_senza_iva(Boolean.FALSE);
                fattura.setFl_spedizioniere(Boolean.FALSE);
                fattura.setFl_bolla_doganale(Boolean.FALSE);
                fattura.setFl_autofattura(Boolean.FALSE);
                fattura.setAutoFatturaNeeded(false);
                fattura.setTi_bene_servizio(null);
                fattura.setFl_merce_extra_ue(Boolean.FALSE);
                fattura.setFl_merce_intra_ue(Boolean.FALSE);
                fattura.setFl_liquidazione_differita(Boolean.FALSE);
                if (fattura instanceof Fattura_passiva_IBulk)
                    ((Fattura_passiva_IBulk) fattura).setFattura_estera(null);
                basicDoOnIstituzionaleCommercialeChange(context, fattura);
                bp.setModel(context, fattura);
                return context.findDefaultForward();
            } catch (it.cnr.jada.comp.ComponentException e) {
                fattura.setSezionali(sezionaliOld);
                fattura.setFl_intra_ue(intraUE);
                fattura.setFl_extra_ue(extraUE);
                fattura.setFl_san_marino_con_iva(sanMarinoCI);
                fattura.setFl_san_marino_senza_iva(sanMarinoSI);
                fattura.setFl_spedizioniere(spedizioniere);
                fattura.setFl_bolla_doganale(bollaDoganale);
                fattura.setFl_autofattura(autof);
                fattura.setTi_bene_servizio(fattServizi);
                fattura.setFl_merce_extra_ue(merceextraUE);
                fattura.setFl_merce_intra_ue(merceintraUE);
                fattura.setFl_liquidazione_differita(liqDiff);
                bp.setModel(context, fattura);
                throw e;
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    /**
     * Gestisce il cambiamento delle modalità di pagamento del fornitore
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOnModalitaPagamentoChange(ActionContext context) {

        try {
            CRUDFatturaPassivaBP crudFattura = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            //Fattura_passivaBulk originale = (Fattura_passivaBulk)crudFattura.getBulkClone();
            Fattura_passivaBulk originale = (Fattura_passivaBulk) crudFattura.getModel();
            Rif_modalita_pagamentoBulk old_modalita = originale.getModalita_pagamento();

            fillModel(context);
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            //se c'è almeno un dettaglio non faccio più modificare la modalità di pagamento
            //commentato perchè per le fatture elettroniche hanno già i dettagli caricati
		/*
		if(fattura.getFattura_passiva_dettColl().size()>0)
		{
			if (old_modalita!=null)
			   fattura.setModalita_pagamento(old_modalita);
			throw new it.cnr.jada.comp.ApplicationException("Esiste almeno un dettaglio e quindi la Modalità di pagamento deve essere cambiata sul dettaglio stesso.");
		}
		*/
            if (fattura.getModalita_pagamento() != null) {
                FatturaPassivaComponentSession fpcs = (FatturaPassivaComponentSession) bp.createComponentSession();
                java.util.Collection coll = fpcs.findListabanche(context.getUserContext(), fattura);
                fattura.setBanca((coll == null || coll.isEmpty()) ? null : (BancaBulk) new java.util.Vector(coll).firstElement());
                fattura.setCessionario(fpcs.findCessionario(context.getUserContext(), fattura));
                if ((getBusinessProcess(context).isInserting()) && (fattura.getFattura_passiva_dettColl().size() > 0)) {
                    for (Iterator i = fattura.getFattura_passiva_dettColl().iterator(); i.hasNext(); ) {
                        Fattura_passiva_rigaIBulk fattura_riga = (Fattura_passiva_rigaIBulk) i.next();
                        fattura_riga.setModalita_pagamento(fattura.getModalita_pagamento());
                        fattura_riga.setBanca((coll == null || coll.isEmpty()) ? null : (BancaBulk) new java.util.Vector(coll).firstElement());
                        fattura_riga.setCessionario(fpcs.findCessionario(context.getUserContext(), fattura_riga));
                    }
                }

            } else {
                fattura.setBanca(null);
                fattura.setCessionario(null);
            }

            //se c'è un solo dettaglio non pagato cambio la modalità anche al dettaglio
		/*
		if(fattura.getFattura_passiva_dettColl().size()==1)
		{
			for (java.util.Iterator i = fattura.getFattura_passiva_dettColl().iterator(); i.hasNext();) {
				Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk)i.next();

				riga.setModalita_pagamento(fattura.getModalita_pagamento());
				doOnModalitaPagamentoDetChange(context);
			}
		}
		*/
            bp.setModel(context, fattura);
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    /**
     * Gestisce il cambiamento della quantità del dettaglio
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOnQuantitaChange(ActionContext context) {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
        Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) bp.getDettaglio().getModel();

        java.math.BigDecimal qta = riga.getQuantita();
        java.math.BigDecimal pu = riga.getPrezzo_unitario();
        java.math.BigDecimal imiva = riga.getIm_iva();
        Boolean flimiva = riga.getFl_iva_forzata();
        java.math.BigDecimal impnc = riga.getIm_diponibile_nc();
        try {
            java.math.BigDecimal vecchioTotale = riga.getIm_imponibile().add(riga.getIm_iva());
            if (riga instanceof Fattura_passiva_rigaIBulk)
                vecchioTotale = vecchioTotale.add(((Fattura_passiva_rigaIBulk) riga).getIm_totale_addebiti());
            fillModel(context);
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
     * Gestisce il cambiamento delle modalità di pagamento del fornitore
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOnModalitaPagamentoDetChange(ActionContext context) {

        try {
            fillModel(context);
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passiva_rigaBulk fattura_riga = (Fattura_passiva_rigaBulk) bp.getDettaglio().getModel();
            if (fattura_riga.getModalita_pagamento() != null) {
                FatturaPassivaComponentSession fpcs = (FatturaPassivaComponentSession) bp.createComponentSession();
                java.util.Collection coll = fpcs.findListabanchedett(context.getUserContext(), fattura_riga);

                fattura_riga.setBanca((coll == null || coll.isEmpty()) ? null : (BancaBulk) new java.util.Vector(coll).firstElement());
                fattura_riga.setCessionario(fpcs.findCessionario(context.getUserContext(), fattura_riga));
            } else {
                fattura_riga.setBanca(null);
                fattura_riga.setCessionario(null);
            }
            bp.setModel(context, fattura_riga.getFattura_passiva());
        } catch (Throwable t) {
            return handleException(context, t);
        }
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
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            fillModel(context);
            try {

                basicDoOnIstituzionaleCommercialeChange(context, fattura);
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
     * Gestisce il cambiamento del flag Intra UE ricaricando i sezionali
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOnTiBeneServizioChange(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            Boolean intraUE = fattura.getFl_intra_ue();
            Boolean extraUE = fattura.getFl_extra_ue();
            Boolean sanMarinoCI = fattura.getFl_san_marino_con_iva();
            Boolean sanMarinoSI = fattura.getFl_san_marino_senza_iva();
            Boolean spedizioniere = fattura.getFl_spedizioniere();
            Boolean bollaDoganale = fattura.getFl_bolla_doganale();
            String spesa = fattura.getStato_pagamento_fondo_eco();
            Boolean autof = fattura.getFl_autofattura();
            String fattServizi = fattura.getTi_bene_servizio();
            Boolean merceextraUE = fattura.getFl_merce_extra_ue();
            Boolean merceintraUE = fattura.getFl_merce_intra_ue();
            fillModel(context);
            try {
                if (fattura.getFl_extra_ue() != null &&
                        fattura.getFl_extra_ue().booleanValue()) {
                    if (fattura.getTi_bene_servizio() != null &&
                            Bene_servizioBulk.SERVIZIO.equalsIgnoreCase(fattura.getTi_bene_servizio())) {
                        fattura.setFl_merce_intra_ue(Boolean.FALSE);
                        if (fattura.isCommerciale()) {
                            fattura.setFl_autofattura(Boolean.TRUE);
                            fattura.setAutoFatturaNeeded(false);
                        }
                    } else {
                        fattura.setFl_autofattura(Boolean.FALSE);
                        //fattura.setAutoFatturaNeeded(false);
                    }
                }
                if (fattura.isCommerciale() && fattura.getFl_intra_ue() != null &&
                        fattura.getFl_intra_ue().booleanValue()) {
                    if (fattura.isFatturaDiServizi()) {
                        fattura.setFl_autofattura(Boolean.TRUE);
                        fattura.setAutoFatturaNeeded(false);
                        fattura.setFl_merce_extra_ue(Boolean.FALSE);
                    } else {
                        fattura.setFl_autofattura(Boolean.TRUE);
                        fattura.setAutoFatturaNeeded(true);
                    }
                }
                if (fattura.isCommerciale() && fattura.getFl_san_marino_senza_iva() != null &&
                        fattura.getFl_san_marino_senza_iva().booleanValue()) {
                    if (fattura.isFatturaDiServizi()) {
                        fattura.setFl_autofattura(Boolean.TRUE);
                        fattura.setAutoFatturaNeeded(false);
                    } else {
                        fattura.setFl_autofattura(Boolean.TRUE);
                        fattura.setAutoFatturaNeeded(true);
                    }
                }
                basicDoOnIstituzionaleCommercialeChange(context, fattura);
                //bp.setModel(context,fattura);
                return context.findDefaultForward();
            } catch (it.cnr.jada.comp.ComponentException e) {
                fattura.setFl_intra_ue(intraUE);
                fattura.setFl_extra_ue(extraUE);
                fattura.setFl_san_marino_con_iva(sanMarinoCI);
                fattura.setFl_san_marino_senza_iva(sanMarinoSI);
                fattura.setFl_bolla_doganale(bollaDoganale);
                fattura.setFl_spedizioniere(spedizioniere);
                fattura.setStato_pagamento_fondo_eco(spesa);
                fattura.setFl_autofattura(autof);
                fattura.setTi_bene_servizio(fattServizi);
                fattura.setFl_merce_extra_ue(merceextraUE);
                fattura.setFl_merce_intra_ue(merceintraUE);
                bp.setModel(context, fattura);
                throw e;
            }
        } catch (Throwable t) {
            return handleException(context, t);
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
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            fillModel(context);

            if (((Fattura_passivaBulk) bp.getModel()).isGenerataDaCompenso())
                throw new it.cnr.jada.comp.ApplicationException("La fattura passiva è stata generata da un compenso. Obbligazione non presente!");

            Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel();
            boolean viewMode = bp.isViewing();
            if (scadenza == null)
                throw new it.cnr.jada.comp.ApplicationException("Selezionare l'impegno da " + (viewMode ? "visualizzare" : "modificare") + " in manuale!");
            if (bp.isDeleting() &&
                    !bp.isViewing() &&
                    !it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).equals(scadenza.getEsercizio()))
                throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata appartiene all'esercizio " + scadenza.getEsercizio().intValue() + "! Operazione annullata.");

            controllaQuadraturaConti(context, (Fattura_passivaBulk) bp.getModel());

            if (!viewMode && bp instanceof IDocumentoAmministrativoBP) {
                IDocumentoAmministrativoBP docAmmBP = (IDocumentoAmministrativoBP) bp;
                viewMode = !docAmmBP.getDocumentoAmministrativoCorrente().isEditable();
                //Nel caso di fp riportate con 1210...
                if (viewMode && bp instanceof CRUDFatturaPassivaIBP)
                    viewMode = !((CRUDFatturaPassivaIBP) docAmmBP).isManualModify();
            }

            //Fattura_passivaBulk fat_pas = (Fattura_passivaBulk)bp.getModel();
            //if ( fat_pas.getEsercizio().intValue() != fat_pas.getEsercizioInScrivania().intValue()){
            //viewMode = true;
            //}

            String status = viewMode ? "V" : "M";
            it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP nbp = it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP.getBusinessProcessFor(context, scadenza.getObbligazione(), status + "RSWTh");
            nbp.edit(context, scadenza.getObbligazione());
            ((ObbligazioneBulk) nbp.getModel()).setFromDocAmm(true);
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
     * passivo
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doPostConfirmDelete(
            ActionContext context,
            Risultato_eliminazioneVBulk re)
            throws BusinessProcessException {

        it.cnr.jada.util.action.CRUDBP bp = getBusinessProcess(context);
        Fattura_passivaBulk fp = (Fattura_passivaBulk) bp.getModel();
        String msg = "Cancellazione effettuata!";
        if (fp.isVoidable()) {
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
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doRemoveFromCRUDMain_Dettaglio(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fp = (Fattura_passivaBulk) bp.getModel();
            java.util.Vector dettagliInventariatiEliminati = new java.util.Vector();
            for (it.cnr.jada.util.action.SelectionIterator i = bp.getDettaglio().getSelection().iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaBulk dett = (Fattura_passiva_rigaBulk) bp.getDettaglio().getDetails().get(i.nextIndex());
                if (Optional.ofNullable(dett)
                        .filter(fattura_passiva_rigaBulk -> fattura_passiva_rigaBulk.isInventariato())
                        .flatMap(fattura_passiva_rigaBulk-> Optional.ofNullable(fattura_passiva_rigaBulk.getBene_servizio()))
                        .map(bene_servizioBulk ->
                                Optional.ofNullable(bene_servizioBulk.getFl_gestione_inventario()).orElse(Boolean.FALSE) &&
                                        Optional.ofNullable(bene_servizioBulk.getCrudStatus()).orElse(-1) != it.cnr.jada.bulk.OggettoBulk.UNDEFINED)
                        .orElse(Boolean.FALSE)) {
                    dettagliInventariatiEliminati.add(dett);
                }
            }
            bp.getDettaglio().remove(context);
            if (dettagliInventariatiEliminati.size() != 0) {
                for (java.util.Iterator i = dettagliInventariatiEliminati.iterator(); i.hasNext(); ) {
                    Fattura_passiva_rigaBulk dett = (Fattura_passiva_rigaBulk) i.next();
                    AssociazioniInventarioTable associazioni = fp.getAssociazioniInventarioHash();
                    if (associazioni != null && !associazioni.isEmpty() && dett instanceof Fattura_passiva_rigaIBulk) {
                        Ass_inv_bene_fatturaBulk ass = fp.getAssociationWithInventarioFor(dett);
                        if (ass != null && !ass.isPerAumentoValore()) {
                            Fattura_passiva_rigaIBulk dettaglio = (Fattura_passiva_rigaIBulk) dett;
                            FatturaPassivaComponentSession h = (FatturaPassivaComponentSession) bp.createComponentSession();
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
                            for (java.util.Iterator iter = fp.getFattura_passiva_dettColl().iterator(); iter.hasNext(); ) {
                                Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) iter.next();
                                if (riga.isInventariato() && !fp.getHa_beniColl()) riga.setInventariato(false);
                            }
                            Fattura_passiva_rigaIBulk dettaglio = (Fattura_passiva_rigaIBulk) dett;
                            FatturaPassivaComponentSession h = (FatturaPassivaComponentSession) bp.createComponentSession();
                            h.rimuoviDaAssociazioniInventario(
                                    context.getUserContext(),
                                    dettaglio,
                                    ass);
                            fp.removeFromAssociazioniInventarioHash(ass, dettaglio);
                        }
                    } else if (associazioni != null && !associazioni.isEmpty() && dett instanceof Nota_di_credito_rigaBulk) {
                        Ass_inv_bene_fatturaBulk ass = fp.getAssociationWithInventarioFor(dett);
                        if (ass != null && !ass.isPerAumentoValore()) {
                            Nota_di_credito_rigaBulk dettaglio = (Nota_di_credito_rigaBulk) dett;
                            FatturaPassivaComponentSession h = (FatturaPassivaComponentSession) bp.createComponentSession();
                            h.rimuoviDaAssociazioniInventario(
                                    context.getUserContext(),
                                    ass);
                            fp.removeFromAssociazioniInventarioHash(ass, dettaglio);
                        }
                    } else if (associazioni != null && !associazioni.isEmpty() && dett instanceof Nota_di_debito_rigaBulk) {
                        Ass_inv_bene_fatturaBulk ass = fp.getAssociationWithInventarioFor(dett);
                        if (ass != null && !ass.isPerAumentoValore()) {
                            Nota_di_debito_rigaBulk dettaglio = (Nota_di_debito_rigaBulk) dett;
                            FatturaPassivaComponentSession h = (FatturaPassivaComponentSession) bp.createComponentSession();
                            h.rimuoviDaAssociazioniInventario(
                                    context.getUserContext(),
                                    ass);
                            fp.removeFromAssociazioniInventarioHash(ass, dettaglio);
                        } else if (ass != null && ass.isPerAumentoValore()) {
                            BuonoCaricoScaricoComponentSession buono_session = (BuonoCaricoScaricoComponentSession) bp.createComponentSession(
                                    "CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",
                                    BuonoCaricoScaricoComponentSession.class);
                            Buono_carico_scaricoBulk buono = ass.getTest_buono();
                            buono.setToBeDeleted();
                            buono_session.eliminaConBulk(context.getUserContext(), buono);
                            for (java.util.Iterator iter = fp.getFattura_passiva_dettColl().iterator(); iter.hasNext(); ) {
                                Nota_di_debito_rigaBulk riga = (Nota_di_debito_rigaBulk) iter.next();
                                if (riga.isInventariato() && !fp.getHa_beniColl()) riga.setInventariato(false);
                            }
                            Nota_di_debito_rigaBulk dettaglio = (Nota_di_debito_rigaBulk) dett;
                            FatturaPassivaComponentSession h = (FatturaPassivaComponentSession) bp.createComponentSession();
                            h.rimuoviDaAssociazioniInventario(
                                    context.getUserContext(),
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
                            for (java.util.Iterator iter = fp.getFattura_passiva_dettColl().iterator(); iter.hasNext(); ) {
                                Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) iter.next();
                                if (riga.isInventariato() && !fp.getHa_beniColl()) riga.setInventariato(false);
                            }
                        } else
                            bp.getDettaglio().setInventoriedChildDeleted(true);
                    }
                }
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }


    public Forward doRemoveFromCRUDMain_Ordini(ActionContext context) throws ApplicationException {
        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
        Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
        Selection selection = bp.getFatturaOrdiniController().getSelection();
        Optional.ofNullable(selection)
                .filter(selection1 -> !selection1.isEmpty())
                .orElseThrow(() -> new ApplicationException("Selezionare le consegne che si desidera eliminare!"));


        final List<FatturaOrdineBulk> details = bp.getFatturaOrdiniController().getDetails();
        final Iterator<Integer> iterator = selection.iterator();
        List<FatturaOrdineBulk> bulksToRemove = new ArrayList<FatturaOrdineBulk>();
        iterator.forEachRemaining(index -> {
            try {
                final FatturaOrdineBulk fatturaOrdineBulk = details.get(index);
                OrdineAcqConsegnaBulk ordineAcqConsegna = (OrdineAcqConsegnaBulk) bp.createComponentSession()
                        .findByPrimaryKey(context.getUserContext(), fatturaOrdineBulk.getOrdineAcqConsegna());
                ordineAcqConsegna.setStatoFatt(OrdineAcqConsegnaBulk.STATO_FATT_NON_ASSOCIATA);
                ordineAcqConsegna.setToBeUpdated();
                bp.createComponentSession().modificaConBulk(
                        context.getUserContext(),
                        ordineAcqConsegna);
                bulksToRemove.add(fatturaOrdineBulk);
            } catch (ComponentException |RemoteException|BusinessProcessException e) {
                throw new DetailedRuntimeException(e);
            }
        });
        bulksToRemove.stream()
                .forEach(fatturaOrdineBulk -> {
                    fatturaOrdineBulk.setToBeDeleted();
                    bp.getFatturaOrdiniController().getDetails().remove(fatturaOrdineBulk);
                });
        bp.getFatturaOrdiniController().getSelection().clear();
        return context.findDefaultForward();
    }
    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di cancellazione dal controller "obbligazioni"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doRemoveFromCRUDMain_Obbligazioni(ActionContext context) {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
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
            java.util.Vector models = (java.util.Vector) ((Fattura_passivaBulk) bp.getModel()).getFattura_passiva_obbligazioniHash().get(obbligazione);
            try {
                if (models != null && models.isEmpty()) {
                    Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
                    fattura.getFattura_passiva_obbligazioniHash().remove(obbligazione);
                    fattura.addToDocumentiContabiliCancellati(obbligazione);
                } else {
                    for (java.util.Iterator it = models.iterator(); it.hasNext(); ) {
                        Fattura_passiva_rigaBulk fpr = (Fattura_passiva_rigaBulk) it.next();
                        if (fpr.getTi_associato_manrev() != null && fpr.ASSOCIATO_A_MANDATO.equalsIgnoreCase(fpr.getTi_associato_manrev()))
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

            doCalcolaTotalePerObbligazione(context, null);

            Fattura_passiva_IBulk fattura = (Fattura_passiva_IBulk) bp.getModel();
            setAndVerifyStatusFor(context, fattura);

            bp.getObbligazioniController().getSelection().clear();
            bp.getObbligazioniController().setModelIndex(context, -1);
            bp.setDirty(true);
        }
        return context.findDefaultForward();
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di cancellazione dal controller "obbligazioni_DettaglioObbligazioni"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doRemoveFromCRUDMain_Obbligazioni_DettaglioObbligazioni(ActionContext context) {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
        try {
            it.cnr.jada.util.action.Selection selection = bp.getDettaglioObbligazioneController().getSelection();
            if (selection.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Selezionare i dettagli che si desidera scollegare!");
            java.util.List models = selection.select(bp.getDettaglioObbligazioneController().getDetails());
            for (java.util.Iterator i = models.iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaBulk fpr = (Fattura_passiva_rigaBulk) i.next();
                if (fpr.getTi_associato_manrev() != null && fpr.ASSOCIATO_A_MANDATO.equalsIgnoreCase(fpr.getTi_associato_manrev()))
                    throw new it.cnr.jada.comp.ApplicationException("Impossibile scollegare il dettaglio \"" +
                            ((fpr.getDs_riga_fattura() != null) ?
                                    fpr.getDs_riga_fattura() :
                                    String.valueOf(fpr.getProgressivo_riga().longValue())) +
                            "\" perchè associato a mandato.");
            }
            scollegaDettagliDaObbligazione(context, models);
        } catch (it.cnr.jada.comp.ComponentException e) {
            return handleException(context, e);
        }

        doCalcolaTotalePerObbligazione(context, (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel());

        Fattura_passiva_IBulk fattura = (Fattura_passiva_IBulk) bp.getModel();
        setAndVerifyStatusFor(context, fattura);

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

    public Forward doRicercaObbligazione(ActionContext context) {
        return doRicercaObbligazione(context, true);
    }
    /**
     * Ricerca un'obbligazione valida da associare al doc amm
     * richeide la validità delle selezioni effettuate
     */
    public Forward doRicercaObbligazione(ActionContext context, boolean manually) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            fillModel(context);
            Fattura_passivaBulk fatturaPassiva = (Fattura_passivaBulk) bp.getModel();
            Optional<List> models = Optional.ofNullable(bp.getDettaglio().getSelectedModels(context))
                    .map(list -> {
                        final int focus = bp.getDettaglio().getSelection().getFocus();
                        if (list.isEmpty() && Optional.ofNullable(fatturaPassiva.getFlDaOrdini())
                                .filter(isDaOrdini -> isDaOrdini.equals(Boolean.TRUE)).isPresent() && focus != -1) {
                            list.add(bp.getDettaglio().getDetails().get(focus));
                        }
                        return list;
                    })
                    .filter(list -> !list.isEmpty());
            if (!models.isPresent()) {
                bp.setErrorMessage("Per procedere, selezionare i dettagli da contabilizzare!");
                return context.findDefaultForward();
            }
            final Boolean isDaOrdini = Optional.ofNullable(fatturaPassiva.getFlDaOrdini())
                    .filter(daOrdini -> daOrdini.equals(Boolean.TRUE))
                    .orElse(false);

            if (fatturaPassiva.isGestione_doc_ele() && fatturaPassiva.isGenerataDaCompenso())
                throw new it.cnr.jada.comp.ApplicationException("La fattura deve essere associata a compenso, la contabilizzazione verrà fatta direttamente nel compenso!");
            if (fatturaPassiva.getFornitore() == null || fatturaPassiva.getFornitore().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.UNDEFINED)
                throw new it.cnr.jada.comp.ApplicationException("Per eseguire questa operazione è necessario impostare un fornitore!");
            if (!isDaOrdini) {
                controllaSelezionePerContabilizzazione(context, models.get().iterator());
                try {
                    controllaSelezionePerTitoloCapitoloLista(context, models.get().iterator());
                } catch (ApplicationException e) {
                    throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
                }
            }
            if (isDaOrdini)
                return basicDoRicercaEvasioneOrdine(context, fatturaPassiva, models.get(), manually);
            else
                return basicDoRicercaObbligazione(context, fatturaPassiva, models.get(), false);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }
    /**
     * Ricerca un'obbligazione valida da associare al doc amm
     * richeide la validità delle selezioni effettuate
     */
    public Forward doContabilzzaRighePerOrdini(ActionContext context) {
        return doRicercaObbligazione(context, false);
    }

    public Forward doRiportaAvanti(ActionContext context) throws java.rmi.RemoteException {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();

        try {
            fillModel(context);
            bp.salvaRiportandoAvanti(context);
            bp.getObbligazioniController().setModelIndex(context, -1);
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
            ((CRUDFatturaPassivaBP) getBusinessProcess(context)).riportaIndietro(context);
            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }

    }

    /**
     * Apre il doc amm selezionato.
     * Nel caso di apertura da spesa per fondo economale, viene riportato l'elemento
     * nel fondo
     */
    public Forward doRiportaSelezione(ActionContext context) throws java.rmi.RemoteException {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
        if (bp instanceof IDocumentoAmministrativoSpesaBP && ((IDocumentoAmministrativoSpesaBP) bp).isSpesaBP()) {
            HookForward caller = (HookForward) context.getCaller();
            it.cnr.jada.bulk.OggettoBulk selezione = (it.cnr.jada.bulk.OggettoBulk) caller.getParameter("focusedElement");
            return basicDoRiportaSelezione(context, selezione);
        }
        return super.doRiportaSelezione(context);
    }

    /**
     * Gestisce una richiesta di salvataggio. Rimplementato
     */
    public Forward doSalva(ActionContext context) throws java.rmi.RemoteException {

        Forward fwd = super.doSalva(context);

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);

        //Azzero la selezione del controller obbligazioni per forzare il calcolo dei dettagli
        //associati all'obbligazione selezionata

        bp.getObbligazioniController().setModelIndex(context, -1);

        return fwd;
    }

    /**
     * Ricerca le banche valide
     */
    public Forward doSearchListabanche(ActionContext context) {

        Fattura_passivaBulk fattura = (Fattura_passivaBulk) getBusinessProcess(context).getModel();
        if (getBusinessProcess(context).isInserting())
            return search(context, getFormField(context, "main.listabanche"), fattura.getModalita_pagamento().getTiPagamentoColumnSet());
        else {
            getBusinessProcess(context).setMessage("Esiste almeno un dettaglio e quindi la Modalità di pagamento deve essere cambiata sul dettaglio stesso.");
            return context.findDefaultForward();
        }
    }

    public Forward doSearchListabanchedett(ActionContext context) {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
        Fattura_passiva_rigaBulk fatturaRiga = (Fattura_passiva_rigaBulk) bp.getDettaglio().getModel();
        return search(context, getFormField(context, "main.Dettaglio.listabanchedett"), fatturaRiga.getModalita_pagamento().getTiPagamentoColumnSet());
    }

    /**
     * Ricerca i sospesi validi per associazione a lettera di pagamento
     */
    public Forward doSearchSospeso(ActionContext context) {

        Fattura_passivaBulk fattura = (Fattura_passivaBulk) getBusinessProcess(context).getModel();
        if (fattura != null && fattura.getLettera_pagamento_estero() != null) {
            Lettera_pagam_esteroBulk lettera = fattura.getLettera_pagamento_estero();
            if (lettera.getSospeso() == null)
                try {
                    doBlankSearchSospeso(context, fattura);
                } catch (Exception e) {
                    return handleException(context, e);
                }
            return search(context, getFormField(context, "main.sospeso"), null);
        }
        return null;
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di selezione dal controller "obbligazioni"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doSelectObbligazioni(ActionContext context) {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
        try {
            bp.getObbligazioniController().setSelection(context);
        } catch (Throwable e) {
            return handleException(context, e);
        }

        doCalcolaTotalePerObbligazione(context, (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel());
        return context.findDefaultForward();
    }

    /**
     * Viene richiamato nel momento in cui si seleziona una valuta dal combo Valuta nella
     * testata della fattura.
     * Viene ricercato il cambio valido, vengono ricalcolati i totali e ricalcolato il
     * totale in eur
     */
    public Forward doSelezionaValuta(ActionContext context) {
        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            DivisaBulk divisa = fattura.getValuta();
            it.cnr.jada.bulk.PrimaryKeyHashtable vecchiTotali = new it.cnr.jada.bulk.PrimaryKeyHashtable();
            if (fattura instanceof Fattura_passiva_IBulk) {
                for (java.util.Iterator i = fattura.getFattura_passiva_dettColl().iterator(); i.hasNext(); ) {
                    Fattura_passiva_rigaIBulk dettaglio = (Fattura_passiva_rigaIBulk) i.next();
                    java.math.BigDecimal vecchioTotale = dettaglio.getIm_imponibile().add(dettaglio.getIm_iva());
                    vecchioTotale = vecchioTotale.add(dettaglio.getIm_totale_addebiti());
                    vecchiTotali.put(dettaglio, vecchioTotale);
                }
            }
            fillModel(context);
            try {
                if (!bp.isSearching()) {
                    FatturaPassivaComponentSession h = (FatturaPassivaComponentSession) bp.createComponentSession();
                    fattura = h.cercaCambio(context.getUserContext(), fattura);
                    basicDoCalcolaTotaleFatturaFornitoreInEur(fattura);
                    for (java.util.Iterator i = fattura.getFattura_passiva_dettColl().iterator(); i.hasNext(); ) {
                        Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk) i.next();
                        if (dettaglio instanceof Fattura_passiva_rigaIBulk) {
                            java.math.BigDecimal vecchioTotale = (java.math.BigDecimal) vecchiTotali.get(dettaglio);
                            if (vecchioTotale == null)
                                vecchioTotale = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
                            basicCalcolaImportoDisponibileNC(context, (Fattura_passiva_rigaIBulk) dettaglio, vecchioTotale);
                        }
                    }

                    bp.setModel(context, fattura);
                    if (fattura.getObbligazioniHash() != null && !fattura.getObbligazioniHash().isEmpty())
                        bp.setErrorMessage("Attenzione: si ricorda che modificando il valore del cambio è necessario riportare in quadratura le scadenze delle obbligazioni!");
                    if (fattura.getAccertamentiHash() != null && !fattura.getAccertamentiHash().isEmpty())
                        bp.setErrorMessage("Attenzione: si ricorda che modificando il valore del cambio è necessario riportare in quadratura le scadenze degli accertamenti!");
                }
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
     * Seleziona la valuta di default
     */
    public Fattura_passivaBulk doSelezionaValutaDefault(ActionContext context, Fattura_passivaBulk fattura)
            throws ComponentException {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            FatturaPassivaComponentSession h = (FatturaPassivaComponentSession) bp.createComponentSession();
            fattura = h.selezionaValutaDiDefault(context.getUserContext(), fattura);
            basicDoCalcolaTotaleFatturaFornitoreInEur(fattura);
            if (fattura.getObbligazioniHash() != null && !fattura.getObbligazioniHash().isEmpty())
                bp.setErrorMessage("Attenzione: si ricorda che modificando il valore del cambio è necessario riportare in quadratura le scadenze delle obbligazioni!");
            if (fattura.getAccertamentiHash() != null && !fattura.getAccertamentiHash().isEmpty())
                bp.setErrorMessage("Attenzione: si ricorda che modificando il valore del cambio è necessario riportare in quadratura le scadenze degli accertamenti!");
            return fattura;
        } catch (BusinessProcessException e) {
            throw new ComponentException(e);
        } catch (java.rmi.RemoteException e) {
            throw new ComponentException(e);
        }
    }

    /**
     * Gestisce un cambiamento di pagina su un controllo tabbed {@link it.cnr.jada.util.jsp.JSPUtils.tabbed}
     */
    public Forward doTab(ActionContext context, String tabName, String pageName) {

            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
        Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
        try {
            if ("tabFatturaPassiva".equalsIgnoreCase(bp.getTab(tabName))) {
                fillModel(context);
//			try {
                if (!bp.isSearching() && !bp.isViewing() && !fattura.isRODateCompetenzaCOGE()){
                    fattura.validaDateCompetenza();
                }
                if (!bp.isSearching() && !bp.isViewing()){
                	if ((((FatturaPassivaComponentSession) bp.createComponentSession()).estraeSezionali(context.getUserContext(), fattura)!= null) && ! (((FatturaPassivaComponentSession) bp.createComponentSession()).estraeSezionali(context.getUserContext(), fattura).isEmpty())){ 
            			boolean trovato=false;
            			for (java.util.Iterator i = ((FatturaPassivaComponentSession) bp.createComponentSession()).estraeSezionali(context.getUserContext(), fattura).iterator(); i.hasNext(); ) {
            				Tipo_sezionaleBulk tipo=(Tipo_sezionaleBulk)i.next();
            				if (tipo.getCd_tipo_sezionale().compareTo(fattura.getCd_tipo_sezionale())==0)
            	 					trovato=true;
            			}
            			if(!trovato)
            	  				throw new it.cnr.jada.comp.ApplicationException("Attenzione: verificare il sezionale selezionato.");
                	}
                }
                //} catch (ValidationException e) {
                //bp.setErrorMessage(e.getMessage());
                //}
            }
            if ("tabFatturaPassivaDettaglio".equalsIgnoreCase(bp.getTab(tabName))) {
                fillModel(context);
                bp.getDettaglio().validate(context);
                try {
                    if (!bp.isSearching())
                        controllaQuadraturaConti(context, fattura);
                } catch (it.cnr.jada.comp.ApplicationException e) {
                    bp.setErrorMessage(e.getMessage());
                }
            }
            if ("tabFatturaPassivaObbligazioni".equalsIgnoreCase(bp.getTab(tabName))) {
                try {
                    fillModel(context);
                    if (!bp.isSearching())
                        controllaQuadraturaObbligazioni(context, fattura);
                } catch (it.cnr.jada.comp.ApplicationException e) {
                    bp.setErrorMessage(e.getMessage());
                }
            }
            if ("tabFatturaPassivaIntrastat".equalsIgnoreCase(bp.getTab(tabName))) {
                fillModel(context);
                bp.getDettaglioIntrastatController().validate(context);
            }
            if (pageName.equalsIgnoreCase("tabFatturaPassivaConsuntivo")) {
                fillModel(context);
                FatturaPassivaComponentSession h = (FatturaPassivaComponentSession) bp.createComponentSession();

                fattura = (Fattura_passivaBulk) h.calcoloConsuntivi(context.getUserContext(), fattura);
                bp.setModel(context, fattura);
            }
        return super.doTab(context, tabName, pageName);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * ricerca le note di credito per la fattura passiva aperta
     */

    private it.cnr.jada.util.RemoteIterator findNoteDiCreditoFor(
            ActionContext context,
            Fattura_passiva_IBulk fatt) throws it.cnr.jada.comp.ComponentException {

        try {
            CRUDFatturaPassivaIBP bp = (CRUDFatturaPassivaIBP) context.getBusinessProcess();
            return ((FatturaPassivaComponentSession) bp.createComponentSession()).findNotaDiCreditoFor(context.getUserContext(), fatt);
        } catch (java.rmi.RemoteException e) {
            throw new it.cnr.jada.comp.ComponentException(e);
        } catch (BusinessProcessException e) {
            throw new it.cnr.jada.comp.ComponentException(e);
        }
    }

    /**
     * ricerca le note di debito per la fattura passiva aperta
     */

    private it.cnr.jada.util.RemoteIterator findNoteDiDebitoFor(
            ActionContext context,
            Fattura_passiva_IBulk fatt) throws it.cnr.jada.comp.ComponentException {

        try {
            CRUDFatturaPassivaIBP bp = (CRUDFatturaPassivaIBP) context.getBusinessProcess();
            return ((FatturaPassivaComponentSession) bp.createComponentSession()).findNotaDiDebitoFor(context.getUserContext(), fatt);
        } catch (java.rmi.RemoteException e) {
            throw new it.cnr.jada.comp.ComponentException(e);
        } catch (BusinessProcessException e) {
            throw new it.cnr.jada.comp.ComponentException(e);
        }
    }

    /**
     * Restituisce un vettore di dettagli ancora da inventariare
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
                Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) dettagli.next();
                if (riga.getBene_servizio() != null && riga.getBene_servizio().getCd_bene_servizio() != null &&
                        riga.getBene_servizio().getFl_gestione_inventario() != null &&
                        riga.getBene_servizio().getFl_gestione_inventario().booleanValue() &&
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
                Nota_di_credito_rigaBulk rigaNdC = (Nota_di_credito_rigaBulk) dettagli.next();
                for (int cont = 0; cont < statiDettaglio.length; cont++) {
                    String statoDettaglio = statiDettaglio[cont];
                    if (statoDettaglio.equals(rigaNdC.getRiga_fattura_origine().getStato_cofi()))
                        if (!coll.contains(rigaNdC))
                            coll.add(rigaNdC);
                }
            }
        }
        return coll;
    }

    /**
     * Restituisce l'importo che deve assumere la scadenza nel caso di modifica automatica
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

        //if (fatturaPassiva instanceof Fattura_passiva_IBulk) {
        //Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk)fatturaPassiva;
        //if (fp.isDoc1210Associato()) {
        //java.math.BigDecimal impLettera = fp.getLettera_pagamento_estero().getIm_pagamento();
        //return (fp.quadraturaInDeroga1210()) ? impLettera.add(fp.getIm_totale_iva()) : impLettera;
        //}
        //}
        return fatturaPassiva.getImportoTotalePerObbligazione();
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

    protected Forward handleException(ActionContext context, Throwable ex) {
        try {
            throw ex;
        } catch (it.cnr.contab.doccont00.comp.CheckDisponibilitaCassaFailed e) {
            Fattura_passivaBulk doc = (Fattura_passivaBulk) ((CRUDFatturaPassivaBP) context.getBusinessProcess()).getModel();
            if (doc instanceof Fattura_passiva_IBulk) {
                Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk) doc;
                if (fp.isDoc1210Associato()) {
                    String msg = "L'importo della lettera di pagamento 1210 supera la disponiblità di cassa relativa al capitolo! Operazione interrotta.";
                    return super.handleException(context, new it.cnr.jada.comp.ApplicationException(msg));
                }
            }
            return super.handleException(context, e);
        } catch (Throwable e) {
            return super.handleException(context, e);
        }
    }

    /**
     * Ricerca un boolean 'true' se tra i dettagli 'models' ne esiste qualcuno con
     * associati storni o addebiti
     */

    private boolean hasRangeDetailWithDocAmmAssociated(ActionContext context, java.util.List models) {

        if (models != null) {
            for (java.util.Iterator i = models.iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk) i.next();
                if (dettaglio instanceof Fattura_passiva_rigaIBulk) {
                    Fattura_passiva_rigaIBulk rigaFP = (Fattura_passiva_rigaIBulk) dettaglio;
                    if (rigaFP.hasStorni() || rigaFP.hasAddebiti())
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

    private boolean isRigaContainedInDetails(Nota_di_credito_rigaBulk rigaNdC, java.util.List details) {

        if (details == null || details.isEmpty() || rigaNdC == null)
            return false;

        for (java.util.Iterator i = details.iterator(); i.hasNext(); ) {
            Nota_di_credito_rigaBulk dettaglio = (Nota_di_credito_rigaBulk) i.next();
            if (dettaglio.getRiga_fattura_origine().equalsByPrimaryKey(rigaNdC.getRiga_fattura_origine()))
                return true;
        }
        return false;
    }

    /**
     * Restituisce un boolean 'true' se tra i dettagli 'details' esiste un dettaglio
     * con chiave primaria uguale a quella di 'rigaNdD'
     */

    private boolean isRigaContainedInDetails(Nota_di_debito_rigaBulk rigaNdD, java.util.List details) {

        if (details == null || details.isEmpty() || rigaNdD == null)
            return false;

        for (java.util.Iterator i = details.iterator(); i.hasNext(); ) {
            Nota_di_debito_rigaBulk dettaglio = (Nota_di_debito_rigaBulk) i.next();
            if (dettaglio.getRiga_fattura_origine().equalsByPrimaryKey(rigaNdD.getRiga_fattura_origine()))
                return true;
        }
        return false;
    }

    /**
     * Risincronizza la collezione delle obbligazioni (richiamato dopo la modifica di
     * una scadenza associata al doc amm).
     * Se questa collezione contiene in chiave la oldObblig (scadenza vecchia), essa
     * viene eliminata e i dettagli ad essa associati vengono ricontabilizzati sulla
     * newObblig (scadenza modificata dall'utente); se non ha ancora dettagli associati
     * viene semplicemente eliminata
     * ('scollegaDettagliDaObbligazione')
     */

    private void resyncObbligazione(
            ActionContext context,
            Obbligazione_scadenzarioBulk oldObblig,
            Obbligazione_scadenzarioBulk newObblig)
            throws it.cnr.jada.comp.ComponentException {

        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
        Fattura_passivaBulk fatturaPassiva = (Fattura_passivaBulk) bp.getModel();
        java.util.Vector models = ((java.util.Vector) fatturaPassiva.getFattura_passiva_obbligazioniHash().get(oldObblig));
        java.util.Vector clone = (java.util.Vector) models.clone();
        if (!clone.isEmpty())
            scollegaDettagliDaObbligazione(context, clone);
        else
            fatturaPassiva.getFattura_passiva_obbligazioniHash().remove(oldObblig);

        basicDoContabilizza(context, newObblig, clone);
    }

    /**
     * Risincronizza la collezione delle obbligazioni (richiamato dopo la modifica di
     * una scadenza associata al doc amm).
     * Se questa collezione contiene in chiave la oldObblig (scadenza vecchia), essa
     * viene eliminata e i dettagli ad essa associati vengono ricontabilizzati sulla
     * newObblig (scadenza modificata dall'utente); se non ha ancora dettagli associati
     * viene semplicemente eliminata
     * Se uno dei dettagli ha un'associazione con note di credito/debito e se non
     * sono in fase di cancellazione della fattura passiva, l'operazione viene interrotta
     */

    private void scollegaDettagliDaObbligazione(ActionContext context, java.util.List models)
            throws it.cnr.jada.comp.ComponentException {

        if (models != null) {
            try {
//			if (!((CRUDFatturaPassivaBP)getBusinessProcess(context)).isDeleting() &&
//				hasRangeDetailWithDocAmmAssociated(context, models))
//				throw new it.cnr.jada.comp.ApplicationException("Uno o più dettagli hanno storni o addebiti collegati! Impossibile scollegare.");

                for (java.util.Iterator i = models.iterator(); i.hasNext(); ) {
                    Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk) i.next();
                    if (!dettaglio.STATO_CONTABILIZZATO.equals(dettaglio.getStato_cofi()))
                        throw new it.cnr.jada.comp.ApplicationException("Non è possibile scollegare il dettaglio \"" + dettaglio.getDs_riga_fattura() + "\". Questa operazione è permessa solo per dettagli in stato \"" + dettaglio.STATO.get(dettaglio.STATO_CONTABILIZZATO) + "\".");
                    dettaglio.getFattura_passiva().removeFromFattura_passiva_obbligazioniHash(dettaglio);
                    dettaglio.setStato_cofi(dettaglio.STATO_INIZIALE);
                    dettaglio.setObbligazione_scadenziario(null);
                    dettaglio.setToBeUpdated();
                }
            } catch (it.cnr.jada.comp.ApplicationException e) {
                try {
                    CRUDVirtualObbligazioneBP.rollbackToSafePoint(context);
                } catch (Throwable t) {
                    throw new ComponentException(t);
                }
                throw e;
            }
        }
    }

    /**
     * Gestisce una richiesta di ricerca su un searchtool
     */
    public Forward search(ActionContext context, FormField field, String columnSet) {

        try {
            if (field != null && "fornitore".equals(field.getField().getName())) {
                CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
                Fattura_passivaBulk fatturaPassiva = (Fattura_passivaBulk) bp.getModel();
                if (!bp.isSearching() && fatturaPassiva != null && fatturaPassiva.getDt_fattura_fornitore() == null)
                    throw new it.cnr.jada.comp.ApplicationException("Impostare la data di emissione del documento del fornitore");
            }
            return super.search(context, field, columnSet);
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Verifica o imposta lo stato della fattura
     */
    protected void setAndVerifyStatusFor(ActionContext context, Fattura_passivaBulk fatturaPassiva) {

        if (fatturaPassiva != null)
            fatturaPassiva.setAndVerifyStatus();
    }

    /**
     * Gestisce il cambiamento del flag Extra UE ricaricando i sezionali
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */

    public Forward doOnFlMerceExtraUEChange(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            Boolean intraUE = fattura.getFl_intra_ue();
            Boolean extraUE = fattura.getFl_extra_ue();
            Boolean sanMarinoCI = fattura.getFl_san_marino_con_iva();
            Boolean sanMarinoSI = fattura.getFl_san_marino_senza_iva();
            Boolean spedizioniere = fattura.getFl_spedizioniere();
            Boolean bollaDoganale = fattura.getFl_bolla_doganale();
            String spesa = fattura.getStato_pagamento_fondo_eco();
            Boolean autof = fattura.getFl_autofattura();
            String fattServizi = fattura.getTi_bene_servizio();
            Boolean merceextraUE = fattura.getFl_merce_extra_ue();
            Boolean merceintraUE = fattura.getFl_merce_intra_ue();
            fillModel(context);
            try {
                if (Boolean.TRUE.equals(fattura.getFl_merce_extra_ue())) {
                    fattura.setFl_san_marino_con_iva(Boolean.FALSE);
                    fattura.setFl_san_marino_senza_iva(Boolean.FALSE);
                    fattura.setFl_bolla_doganale(Boolean.FALSE);
                    fattura.setFl_spedizioniere(Boolean.FALSE);
                    fattura.setStato_pagamento_fondo_eco(fattura.NO_FONDO_ECO);
                    fattura.setFl_extra_ue(Boolean.FALSE);
                    fattura.setFl_merce_intra_ue(Boolean.FALSE);
                    if (!fattura.isPromiscua())
                        fattura.setTi_bene_servizio(fattura.FATTURA_DI_BENI);
                    fattura.setFl_autofattura(Boolean.FALSE);
                    fattura.setAutoFatturaNeeded(true);
                } else {
                    fattura.setTi_bene_servizio(null);
                    fattura.setFl_intra_ue(Boolean.FALSE);
                    fattura.setFl_autofattura(Boolean.FALSE);
                    fattura.setAutoFatturaNeeded(false);
                    if (!fattura.isDefaultValuta())
                        fattura = doSelezionaValutaDefault(context, fattura);
                }

                if (fattura instanceof Fattura_passiva_IBulk)
                    ((Fattura_passiva_IBulk) fattura).setFattura_estera(null);
                basicDoOnIstituzionaleCommercialeChange(context, fattura);
                bp.setModel(context, fattura);
                return context.findDefaultForward();
            } catch (it.cnr.jada.comp.ComponentException e) {
                fattura.setFl_intra_ue(intraUE);
                fattura.setFl_extra_ue(extraUE);
                fattura.setFl_san_marino_con_iva(sanMarinoCI);
                fattura.setFl_san_marino_senza_iva(sanMarinoSI);
                fattura.setFl_bolla_doganale(bollaDoganale);
                fattura.setFl_spedizioniere(spedizioniere);
                fattura.setStato_pagamento_fondo_eco(spesa);
                fattura.setFl_autofattura(autof);
                fattura.setTi_bene_servizio(fattServizi);
                fattura.setFl_merce_extra_ue(merceextraUE);
                fattura.setFl_merce_intra_ue(merceintraUE);
                bp.setModel(context, fattura);
                throw e;
            }
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    public Forward doOnLiquidazioneDifferitaChange(ActionContext context) {

        try {
            fillModel(context);
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            if (fattura.getPg_fattura_passiva() != null) {
                it.cnr.jada.util.RemoteIterator ric = findNoteDiCreditoFor(context, (Fattura_passiva_IBulk) bp.getModel());
                ric = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ric);
                if (ric != null && ric.countElements() != 0) {
                    it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ric);
                    fattura.setFl_liquidazione_differita((fattura.getFl_liquidazione_differita()) ? Boolean.FALSE : Boolean.TRUE);
                    bp.setModel(context, fattura);
                    throw new it.cnr.jada.comp.ApplicationException("Non è possibile effettuare tale modifica poichè esistono note di credito collegate per questa fattura!");
                    //bp.setMessage("Non è possibile effettuare tale modifica poichè esistono note di credito collegate per questa fattura!");
                }
                it.cnr.jada.util.RemoteIterator rid = findNoteDiDebitoFor(context, (Fattura_passiva_IBulk) bp.getModel());
                rid = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, rid);
                if (rid != null && rid.countElements() != 0) {
                    it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, rid);
                    fattura.setFl_liquidazione_differita((fattura.getFl_liquidazione_differita()) ? Boolean.FALSE : Boolean.TRUE);
                    bp.setModel(context, fattura);
                    throw new it.cnr.jada.comp.ApplicationException("Non è possibile effettuare tale modifica poichè esistono note di debito collegate per questa fattura!");
                    //bp.setMessage("Non è possibile effettuare tale modifica poichè esistono note di debito collegate per questa fattura!");
                }
            }

        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    public Forward doOnStatoFondoEconChange(ActionContext context) {

        try {
            fillModel(context);
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();

            if (!fattura.getStato_pagamento_fondo_eco().equals(fattura.NO_FONDO_ECO)
                    && Boolean.TRUE.equals(fattura.getFl_liquidazione_differita()))

                fattura.setFl_liquidazione_differita(Boolean.FALSE);

            bp.setModel(context, fattura);
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    public Forward doOnFlMerceIntraUEChange(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            Boolean intraUE = fattura.getFl_intra_ue();
            Boolean extraUE = fattura.getFl_extra_ue();
            Boolean sanMarinoCI = fattura.getFl_san_marino_con_iva();
            Boolean sanMarinoSI = fattura.getFl_san_marino_senza_iva();
            Boolean spedizioniere = fattura.getFl_spedizioniere();
            Boolean bollaDoganale = fattura.getFl_bolla_doganale();
            String spesa = fattura.getStato_pagamento_fondo_eco();
            Boolean autof = fattura.getFl_autofattura();
            String fattServizi = fattura.getTi_bene_servizio();
            Boolean merceextraUE = fattura.getFl_merce_extra_ue();
            Boolean merceintraUE = fattura.getFl_merce_intra_ue();
            fillModel(context);
            try {
                if (Boolean.TRUE.equals(fattura.getFl_merce_intra_ue())) {
                    fattura.setFl_san_marino_con_iva(Boolean.FALSE);
                    fattura.setFl_san_marino_senza_iva(Boolean.FALSE);
                    fattura.setFl_bolla_doganale(Boolean.FALSE);
                    fattura.setFl_spedizioniere(Boolean.FALSE);
                    fattura.setStato_pagamento_fondo_eco(fattura.NO_FONDO_ECO);
                    fattura.setFl_intra_ue(Boolean.FALSE);
                    fattura.setFl_merce_extra_ue(Boolean.FALSE);
                    if (!fattura.isPromiscua())
                        fattura.setTi_bene_servizio(fattura.FATTURA_DI_BENI);
                    if (fattura.isCommerciale()) {
                        fattura.setFl_autofattura(Boolean.TRUE);
                        fattura.setAutoFatturaNeeded(true);
                    }
                } else {
                    fattura.setTi_bene_servizio(null);
                    fattura.setFl_extra_ue(Boolean.FALSE);
                    fattura.setFl_autofattura(Boolean.FALSE);
                    fattura.setAutoFatturaNeeded(false);
                    if (!fattura.isDefaultValuta())
                        fattura = doSelezionaValutaDefault(context, fattura);
                }

                if (fattura instanceof Fattura_passiva_IBulk)
                    ((Fattura_passiva_IBulk) fattura).setFattura_estera(null);
                basicDoOnIstituzionaleCommercialeChange(context, fattura);
                bp.setModel(context, fattura);
                return context.findDefaultForward();
            } catch (it.cnr.jada.comp.ComponentException e) {
                fattura.setFl_intra_ue(intraUE);
                fattura.setFl_extra_ue(extraUE);
                fattura.setFl_san_marino_con_iva(sanMarinoCI);
                fattura.setFl_san_marino_senza_iva(sanMarinoSI);
                fattura.setFl_bolla_doganale(bollaDoganale);
                fattura.setFl_spedizioniere(spedizioniere);
                fattura.setStato_pagamento_fondo_eco(spesa);
                fattura.setFl_autofattura(autof);
                fattura.setTi_bene_servizio(fattServizi);
                fattura.setFl_merce_extra_ue(merceextraUE);
                fattura.setFl_merce_intra_ue(merceintraUE);
                bp.setModel(context, fattura);
                throw e;
            }
        } catch (Throwable t) {
            return handleException(context, t);
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
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            fillModel(context);
            Fattura_passiva_rigaIBulk riga = (Fattura_passiva_rigaIBulk) bp.getDettaglio().getModel();
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
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            fillModel(context);

            bp.sdoppiaDettaglioInAutomatico(context);

            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();

            if (fattura != null) {
                for (Iterator s = fattura.getFattura_passiva_dettColl().iterator(); s.hasNext(); ) {
                    Fattura_passiva_rigaIBulk riga = (Fattura_passiva_rigaIBulk) s.next();
                    if ((riga.isToBeCreated() || riga.isToBeUpdated()) && riga.getObbligazione_scadenziario() != null)
                        basicDoBringBackOpenObbligazioniWindow(context, riga.getObbligazione_scadenziario());
                }
            }

            bp.setDirty(true);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackSearchListabanchedett(ActionContext context, Fattura_passiva_rigaBulk riga, BancaBulk banca) {
        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            FatturaPassivaComponentSession fpcs = (FatturaPassivaComponentSession) bp.createComponentSession();

            if (banca != null) {
                riga.setBanca(banca);
                if (banca.getCd_terzo_delegato() != null)
                    riga.setCessionario(fpcs.findCessionario(context.getUserContext(), riga));
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackSearchListabanche(ActionContext context, Fattura_passivaBulk fattura, BancaBulk banca) {
        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            FatturaPassivaComponentSession fpcs = (FatturaPassivaComponentSession) bp.createComponentSession();

            if (banca != null) {
                fattura.setBanca(banca);
                if (banca.getCd_terzo_delegato() != null)
                    fattura.setCessionario(fpcs.findCessionario(context.getUserContext(), fattura));
            }
            if ((getBusinessProcess(context).isInserting()) && (fattura.getFattura_passiva_dettColl().size() > 0)) {
                for (Iterator i = fattura.getFattura_passiva_dettColl().iterator(); i.hasNext(); ) {
                    Fattura_passiva_rigaIBulk fattura_riga = (Fattura_passiva_rigaIBulk) i.next();
                    fattura_riga.setBanca(banca);
                    fattura_riga.setCessionario(fpcs.findCessionario(context.getUserContext(), fattura_riga));
                }
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doBlankSearchFind_trovato(ActionContext context, TrovatoBulk trovato) {

        if (trovato != null) {
            TrovatoBulk newt = new TrovatoBulk();
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) bp.getDettaglio().getModel();
            riga.setTrovato(newt);
            riga.setPg_trovato(null);
        }
        return context.findDefaultForward();
    }

    public Forward doOnStatoLiquidazioneChange(ActionContext context) {
        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            String oldCausale = fattura.getCausale();
            fillModel(context);
            if (fattura.getStato_liquidazione() != null && fattura.getStato_liquidazione().equals(fattura.LIQ)) {
                if (fattura.getCausale() != null) {
                    fattura.setCausale(null);
                }
            } else if (fattura.getStato_liquidazione() != null && fattura.getStato_liquidazione().equals(fattura.SOSP)) {
                fattura.setCausale(fattura.ATTLIQ);
            } else if (fattura.getStato_liquidazione() != null && fattura.getStato_liquidazione().equals(fattura.NOLIQ)) {
                if (fattura.getCausale() != null && fattura.getCausale().equals(fattura.ATTLIQ)) {
                    if (oldCausale != null && !oldCausale.equals(fattura.ATTLIQ))
                        fattura.setCausale(oldCausale);
                    else
                        fattura.setCausale(null);
                    throw new ApplicationException("Causale non valida, per lo stato della Liquidazione");
                }
            }
            bp.setModel(context, fattura);
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    public Forward doOnCausaleChange(ActionContext context) {
        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            String oldCausale = fattura.getCausale();
            fillModel(context);
            if (fattura.getStato_liquidazione() != null && fattura.getStato_liquidazione().equals(fattura.LIQ)) {
                if (fattura.getCausale() != null) {
                    fattura.setCausale(null);
                    throw new ApplicationException("Causale non valida, per lo stato della Liquidazione");
                }
            } else if (fattura.getStato_liquidazione() != null && fattura.getStato_liquidazione().equals(fattura.NOLIQ)) {
                if (fattura.getCausale() != null && fattura.getCausale().equals(fattura.ATTLIQ)) {
                    if (oldCausale != null && !oldCausale.equals(fattura.ATTLIQ))
                        fattura.setCausale(oldCausale);
                    else
                        fattura.setCausale(null);
                    throw new ApplicationException("Causale non valida, per lo stato della Liquidazione");
                }
            } else if (fattura.getStato_liquidazione() != null && fattura.getStato_liquidazione().equals(fattura.SOSP)) {
                if (fattura.getCausale() != null && !fattura.getCausale().equals(fattura.ATTLIQ)) {
                    if (oldCausale != null)
                        fattura.setCausale(oldCausale);
                    else
                        fattura.setCausale(null);
                    throw new ApplicationException("Causale non valida, per lo stato della Liquidazione");
                }
            }

            bp.setModel(context, fattura);
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    protected java.util.List controllaSelezionePerTitoloCapitoloLista(ActionContext context, java.util.Iterator selectedModels)
            throws ComponentException, PersistencyException, IntrospectionException, RemoteException, BusinessProcessException {

        if (selectedModels != null) {
            java.util.Vector soggettiAInventario = new java.util.Vector();
            java.util.List titoliCapitoli = null;
            java.util.Vector categorieGruppo = new java.util.Vector();
            int count = 0;

            while (selectedModels.hasNext()) {
                count += 1;
                Fattura_passiva_rigaBulk rigaSelected = (Fattura_passiva_rigaBulk) selectedModels.next();
                Bene_servizioBulk beneServizio = rigaSelected.getBene_servizio();
                if (beneServizio == null)
                    throw new it.cnr.jada.comp.ApplicationException("Valorizzare il bene/servizio per il dettaglio " + ((rigaSelected.getDs_riga_fattura() == null) ? "" : "\"" + rigaSelected.getDs_riga_fattura() + "\"") + "! Operazione interrotta.");
                if (beneServizio.getFl_gestione_inventario() != null && beneServizio.getFl_gestione_inventario().booleanValue()) {
                    soggettiAInventario.add(rigaSelected);
                    if (beneServizio.getCategoria_gruppo() == null)
                        throw new it.cnr.jada.comp.ApplicationException("Il bene/servizio \"" + beneServizio.getDs_bene_servizio() + "\" non ha definito alcuna categoria di appartenenza! Operazione interrotta.");
                    else if (categorieGruppo.isEmpty())
                        categorieGruppo.add(beneServizio.getCategoria_gruppo());
                    else
                        for (java.util.Iterator i = ((java.util.Vector) categorieGruppo.clone()).iterator(); i.hasNext(); ) {
                            Categoria_gruppo_inventBulk cat = (Categoria_gruppo_inventBulk) i.next();
                            if (!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(categorieGruppo, beneServizio.getCategoria_gruppo()))
                                categorieGruppo.add(beneServizio.getCategoria_gruppo());
                        }

                    CategoriaGruppoInventComponentSession h = (CategoriaGruppoInventComponentSession)
                            context.getBusinessProcess().createComponentSession(
                                    "CNRDOCAMM00_EJB_CategoriaGruppoInventComponentSession",
                                    CategoriaGruppoInventComponentSession.class);
                    titoliCapitoli = h.findAssVoceFList(context.getUserContext(), beneServizio.getCategoria_gruppo());
                    if (titoliCapitoli == null)
                        throw new it.cnr.jada.comp.ApplicationException("Selezione non omogenea: il bene/servizio \"" + beneServizio.getDs_bene_servizio() + "\" non è stato attribuito ad alcuna categoria gruppo per l'inventario!");
                }
            }

            int size = soggettiAInventario.size();
            if (size != 0) {
                if (size != count)
                    throw new it.cnr.jada.comp.ApplicationException("Selezione non omogenea: selezionare solo dettagli non inventariabili o solo dettagli inventariabili");
                else if (categorieGruppo.size() != 1)
                    throw new it.cnr.jada.comp.ApplicationException("Selezione non omogenea: selezionare solo dettagli inventariabili con stesso titolo capitolo!");
            }
            if (titoliCapitoli != null && !titoliCapitoli.isEmpty())
                return titoliCapitoli;
        }
        return null;
    }

    public Forward doCreaCompenso(ActionContext context) {

        try {
            fillModel(context);
            CRUDFatturaPassivaIBP bp = (CRUDFatturaPassivaIBP) context.getBusinessProcess();

            Integer esercizioScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());
            //java.math.BigDecimal quotaEsente = new java.math.BigDecimal(0);
            //java.math.BigDecimal quotaEsenteNonImpo = new java.math.BigDecimal(0);
            java.math.BigDecimal imBollo = new java.math.BigDecimal(0);

            Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk) bp.getModel();

            if (fp.getCompenso() != null)
                throw new it.cnr.jada.comp.ApplicationException("Esiste già un compenso associato alla fattura!");

            bp.validaFatturaPerCompenso(context);

            V_terzo_per_compensoBulk v_terzo = new V_terzo_per_compensoBulk(fp.getFornitore().getCd_terzo(), new String("A"));
            v_terzo.setTerzo(fp.getFornitore());
            v_terzo.setAnagrafico(fp.getFornitore().getAnagrafico());
            v_terzo.setRagione_sociale(fp.getFornitore().getAnagrafico().getRagione_sociale());
            v_terzo.setCognome(fp.getFornitore().getAnagrafico().getCognome());
            v_terzo.setNome(fp.getFornitore().getAnagrafico().getNome());
            v_terzo.setCodice_fiscale(fp.getFornitore().getAnagrafico().getCodice_fiscale());
            v_terzo.setPartita_iva(fp.getFornitore().getAnagrafico().getPartita_iva());


            context.addHookForward("bringback", this, "doBringBackCompenso");
//		context.addHookForward("close",this,"doBringBackCompenso");

            CRUDCompensoBP compensoBP = (CRUDCompensoBP) creaCompensoBP(context, true);
            try {
                compensoBP.reset(context);
                CompensoBulk compenso = (CompensoBulk) compensoBP.getModel();

                compenso.setV_terzo(v_terzo);

                if (fp.getDocumentoEleTestata() != null && fp.isElettronica()) {
                    //quotaEsente = fp.getDocumentoEleTestata().calcolaImQuotaEsente(fp.getDocumentoEleTestata());
                    //quotaEsenteNonImpo = fp.getDocumentoEleTestata().calcolaImQuotaEsenteNonImpo(fp.getDocumentoEleTestata());
                    if (fp.getDocumentoEleTestata().getImportoBollo() != null)
                        imBollo = fp.getDocumentoEleTestata().getImportoBollo();

                    compenso.setIm_lordo_percipiente(fp.getDocumentoEleTestata().calcolaImLordoPercipiente(fp.getDocumentoEleTestata()));
                    compenso.setQuota_esente(imBollo);
                    compenso.setQuota_esente_no_iva(imBollo);
                /*
				compenso.setIm_lordo_percipiente(fp.getDocumentoEleTestata().calcolaImLordoPercipiente(fp.getDocumentoEleTestata()).add(quotaEsente).add(quotaEsenteNonImpo).add(imBollo));
				compenso.setQuota_esente(quotaEsenteNonImpo.add(imBollo));
				compenso.setQuota_esente_no_iva(quotaEsente.add(quotaEsenteNonImpo).add(imBollo));
				*/
                }

                it.cnr.contab.compensi00.ejb.CompensoComponentSession component = (it.cnr.contab.compensi00.ejb.CompensoComponentSession) bp.createComponentSession("CNRCOMPENSI00_EJB_CompensoComponentSession", it.cnr.contab.compensi00.ejb.CompensoComponentSession.class);
                compenso = component.inizializzaCompensoPerFattura(
                        context.getUserContext(),
                        compenso,
                        fp);
                compensoBP.setModel(context, compenso);
            } catch (Throwable t) {
                compensoBP.rollbackToSavePoint(context, bp.SAVE_POINT_NAME);
                throw t;
            }

            return context.addBusinessProcess(compensoBP);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    private Forward creaCompensoBP(ActionContext context, boolean setSafePoint)
            throws BusinessProcessException {
        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fp = (Fattura_passivaBulk) bp.getModel();
            if (fp.getDocumentiContabiliCancellati() != null &&
                    !fp.getDocumentiContabiliCancellati().isEmpty()) {
                OptionRequestParameter userConfirmation = new OptionRequestParameter();
                userConfirmation.setCheckDisponibilitaContrattoRequired(Boolean.FALSE);
                userConfirmation.setCheckDisponibilitaDiCassaRequired(Boolean.FALSE);
                ((FatturaPassivaComponentSession) bp.createComponentSession()).
                        aggiornaObblSuCancPerCompenso(context.getUserContext(),
                                fp,
                                fp.getDocumentiContabiliCancellati(),
                                userConfirmation);

                fp.getDocumentiContabiliCancellati().removeAllElements();
                //aggiornaObbligazioni(context.getUserContext(), (Fattura_passivaBulk) bp.getModel(), userConfirmation);
            }
        } catch (RemoteException e) {
            return handleException(context, e);
        } catch (ComponentException e) {
            return handleException(context, e);
        } catch (Throwable e) {
            return handleException(context, e);
        }

        CRUDCompensoBP compensoBP = (CRUDCompensoBP) context.getUserInfo().createBusinessProcess(
                context,
                "CRUDCompensoBP",
                new Object[]{"MRSWTh"}
        );
        if (setSafePoint)
            compensoBP.setSavePoint(context, CRUDFatturaPassivaIBP.SAVE_POINT_NAME);

        return compensoBP;
    }

    /**
     * Al ritorno della creazione di un compenso, associo alle rate selezionate questo documento
     */

    public Forward doBringBackCompenso(ActionContext context) {

        HookForward caller = (HookForward) context.getCaller();
        CompensoBulk compenso = (CompensoBulk) caller.getParameter("bringback");

        CRUDFatturaPassivaIBP bp = (CRUDFatturaPassivaIBP) getBusinessProcess(context);
        if (compenso == null) {
            try {
                ((CRUDCompensoBP) creaCompensoBP(context, false)).rollbackToSavePoint(context, bp.SAVE_POINT_NAME);
            } catch (BusinessProcessException e) {
                return handleException(context, e);
            }
            return context.findDefaultForward();
        }

        try {

            Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk) bp.getModel();

            fp.setFl_fattura_compenso(true);
            fp.setStato_cofi(fp.STATO_CONTABILIZZATO);
            fp.setStato_coge(fp.NON_PROCESSARE_IN_COGE);
            fp.setStato_coan(fp.NON_PROCESSARE_IN_COAN);
            //fp.setStato_liquidazione(fp.LIQ);
            //fp.setCausale(null);
            fp.setStato_pagamento_fondo_eco(fp.NO_FONDO_ECO);
            fp.setCompenso(compenso);
            for (java.util.Iterator i = fp.getFattura_passiva_dettColl().iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) i.next();

                riga.setStato_cofi(fp.STATO_CONTABILIZZATO);
            }
            bp.setModel(context, fp);
            bp.setDirty(true);

            bp.setMessage("Associazione terminata con successo.");

            return context.findDefaultForward();

        } catch (Throwable t) {
            try {
                ((CRUDCompensoBP) creaCompensoBP(context, false)).rollbackToSavePoint(context, bp.SAVE_POINT_NAME);
            } catch (BusinessProcessException e) {
                return handleException(context, e);
            }
            return handleException(context, t);
        }
    }

    public Forward doApriCompenso(ActionContext context) {

        try {
            fillModel(context);
            CRUDFatturaPassivaIBP bp = (CRUDFatturaPassivaIBP) context.getBusinessProcess();
            Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk) bp.getModel();

            if (fp == null || fp.getCompenso() == null)
                throw new it.cnr.jada.comp.ApplicationException("Non esiste alcun Compenso da visualizzare!");

            context.addHookForward("bringback", this, "doBringBackApriCompenso");

            CRUDCompensoBP compensoBP = (CRUDCompensoBP) context.createBusinessProcess(
                    "CRUDCompensoBP",
                    new Object[]{"VRSWTh"});
            compensoBP.setSavePoint(context, bp.SAVE_POINT_NAME);

            try {
                compensoBP.edit(context, fp.getCompenso());
            } catch (Throwable t) {
                compensoBP.rollbackToSavePoint(context, bp.SAVE_POINT_NAME);
                throw t;
            }

            return context.addBusinessProcess(compensoBP);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackApriCompenso(ActionContext context) {
        try {
            ((CRUDCompensoBP) creaCompensoBP(context, false)).rollbackToSavePoint(
                    context,
                    CRUDFatturaPassivaIBP.SAVE_POINT_NAME);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doOnFlFatturaCompensoChange(ActionContext context) {
        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            Boolean fatcomp = fattura.getFl_fattura_compenso();
            fillModel(context);
            if (Boolean.TRUE.equals(fatcomp) && fattura.getCompenso() != null) {
                fattura.setFl_fattura_compenso(fatcomp);
                bp.setModel(context, fattura);
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile modificare questa informazione poichè esiste già un compenso collegato!");
            }
            if (Boolean.FALSE.equals(fatcomp) && fattura.hasDettagliContabilizzati()) {
                fattura.setFl_fattura_compenso(fatcomp);
                bp.setModel(context, fattura);
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile modificare questa informazione poichè risultano già contabilizzati i dettagli della fattura!");

            }
            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }


    }

    public Forward doConfirmDtScadenza(ActionContext context, it.cnr.jada.util.action.OptionBP option) {
        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
            if (option.getOption() == it.cnr.jada.util.action.OptionBP.NO_BUTTON) {
                fattura.setDt_scadenza(null);
            }
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doDisassociaLettera(ActionContext context) {

        try {
            fillModel(context);
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk model = (Fattura_passivaBulk) bp.getModel();
            CNRUserInfo ui = (CNRUserInfo) context.getUserInfo();
            UtenteBulk utente = ui.getUtente();
            if (utente.isSupervisore()) {
                if (model != null) {
                    if (model.getLettera_pagamento_estero() != null) {
                        model = ((FatturaPassivaComponentSession) bp.createComponentSession()).eliminaLetteraPagamentoEstero(context.getUserContext(), model, false);
                        bp.setModel(context, model);
                    }
                }
            } else throw new it.cnr.jada.comp.ApplicationException("Utente non abilitato!");
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doOnFlSplitPaymentChange(ActionContext context) {
        try {
            fillModel(context);
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();

            if (fattura.getFl_split_payment() != null && fattura.getFl_split_payment() && !fattura.isGestioneSplitPayment() && fattura.getDt_fattura_fornitore() != null) {
                fattura.setFl_split_payment(Boolean.FALSE);
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                throw new ValidationException("Non è possibile registrare una fattura di tipo Split Payment che abbia data di emissione inferiore al " + sdf.format(fattura.getDataInizioSplitPayment()) + "!");
            }
            basicDoOnIstituzionaleCommercialeChange(context, fattura);
            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    public Forward doOnFlDaOrdiniChange(ActionContext context) {
        try {
            fillModel(context);
            CRUDFatturaPassivaBP bp = Optional.ofNullable(getBusinessProcess(context))
                    .filter(CRUDFatturaPassivaBP.class::isInstance)
                    .map(CRUDFatturaPassivaBP.class::cast)
                    .orElseThrow(() -> new DetailedRuntimeException("Business Process non valido"));
            Fattura_passivaBulk fattura_passivaBulk = (Fattura_passivaBulk) bp.getModel();
            if (!fattura_passivaBulk.isDaOrdini() && Optional.ofNullable(fattura_passivaBulk.getDocumentoEleTestata()).isPresent()  && fattura_passivaBulk.getFattura_passiva_dettColl().isEmpty()) {
                bp.caricaRigheFatturaDaFatturazioneElettronica(context, fattura_passivaBulk, this, fattura_passivaBulk.getDocumentoEleTestata());
            }
            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    public Forward doRettificaConsegna(ActionContext context) {
        try {
            fillModel(context);
            CRUDFatturaPassivaBP bp = Optional.ofNullable(getBusinessProcess(context))
                    .filter(CRUDFatturaPassivaBP.class::isInstance)
                    .map(CRUDFatturaPassivaBP.class::cast)
                    .orElseThrow(() -> new DetailedRuntimeException("Business Process non valido"));
            Fattura_passivaBulk fattura_passivaBulk = (Fattura_passivaBulk) bp.getModel();
            FatturaOrdineBulk fatturaOrdineBulk = (FatturaOrdineBulk) bp.getFatturaOrdiniController().getModel();
            bp.calcolaRettificaOrdine(context, fatturaOrdineBulk);
            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    public Forward doBringBackSearchVoceIva(ActionContext context,
                                              FatturaOrdineBulk fatturaOrdineBulk,
                                              Voce_ivaBulk voceIva) {
        try {
            CRUDFatturaPassivaBP bp = Optional.ofNullable(getBusinessProcess(context))
                    .filter(CRUDFatturaPassivaBP.class::isInstance)
                    .map(CRUDFatturaPassivaBP.class::cast)
                    .orElseThrow(() -> new DetailedRuntimeException("Business Process non valido"));
            if (voceIva != null){
                fatturaOrdineBulk.setVoceIva(voceIva);
                bp.setDirty(true);
            }
            bp.calcolaRettificaOrdine(context, fatturaOrdineBulk);
            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    public Forward doBlankSearchVoceIva(ActionContext context,
                                            FatturaOrdineBulk fatturaOrdineBulk) {
        try {
            CRUDFatturaPassivaBP bp = Optional.ofNullable(getBusinessProcess(context))
                    .filter(CRUDFatturaPassivaBP.class::isInstance)
                    .map(CRUDFatturaPassivaBP.class::cast)
                    .orElseThrow(() -> new DetailedRuntimeException("Business Process non valido"));
            fatturaOrdineBulk.setVoceIva(new Voce_ivaBulk());
            bp.calcolaRettificaOrdine(context, fatturaOrdineBulk);
            bp.setDirty(true);
            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }
    }

    public Forward doToggleOrdiniRettifiche(ActionContext context) {
        CRUDFatturaPassivaBP bp = Optional.ofNullable(getBusinessProcess(context))
                .filter(CRUDFatturaPassivaBP.class::isInstance)
                .map(CRUDFatturaPassivaBP.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("Business Process non valido"));
        Optional.ofNullable(bp.getFatturaOrdiniController())
                .ifPresent(ordiniCRUDController -> ordiniCRUDController.setRettificheCollapse(!ordiniCRUDController.isRettificheCollapse()));
        return context.findDefaultForward();
    }
	public Forward doBringBackCRUDCrea_cig(ActionContext context, Obbligazione_scadenzarioBulk obbl, CigBulk cig) 
	{
		CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP)getBusinessProcess(context);
		try 
		{
			if (cig != null )
			{
				obbl.setCig(cig);
			}	
			return context.findDefaultForward();
		}
		catch(it.cnr.jada.action.MessageToUser e) 
		{
			getBusinessProcess(context).setErrorMessage(e.getMessage());
			return context.findDefaultForward();
		}		
	
		catch(Throwable e) {return handleException(context,e);}
	}
	public Forward doBlankSearchCig(ActionContext context, Obbligazione_scadenzarioBulk obbl) {

		if (obbl!=null){
			CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP)getBusinessProcess(context);
			obbl.setCig(null);
			bp.setDirty(true);
		}
		return context.findDefaultForward();

	}
    public Forward doContabilizzaOrdiniNotaCredito(ActionContext context) {

        HookForward caller = (HookForward) context.getCaller();
        Obbligazione_scadenzarioBulk obblig = (Obbligazione_scadenzarioBulk) caller.getParameter("obbligazioneSelezionata");
        CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) context.getBusinessProcess();
        Fattura_passivaBulk fattura = (Fattura_passivaBulk) bp.getModel();
        java.util.List selectedModels = null;
        try {
            selectedModels = getListaRigheFatturaOrdiniNotaCredito(context, bp, fattura, true);
            bp.getFatturaOrdiniController().getSelection().clearSelection();
        } catch (Throwable e) {
        }

        if (obblig != null) {
            try {
                TerzoBulk creditore = obblig.getObbligazione().getCreditore();
                if (!fattura.getFornitore().equalsByPrimaryKey(creditore) &&
                        !AnagraficoBulk.DIVERSI.equalsIgnoreCase(creditore.getAnagrafico().getTi_entita())) {
                    ((IDocumentoAmministrativoBulk) fattura).addToDocumentiContabiliCancellati(obblig);
                    throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata deve appartenere ad un'obbligazione che ha come creditore il fornitore della fattura!");
                }
                Filtro_ricerca_obbligazioniVBulk filtro = (Filtro_ricerca_obbligazioniVBulk) caller.getParameter("filtroRicercaUtilizzato");
                if (filtro != null) {
                    Elemento_voceBulk ev = filtro.getElemento_voce();
                    if (ev != null) {
                        if (!obblig.getObbligazione().getElemento_voce().getCd_elemento_voce().startsWith(ev.getCd_elemento_voce())) {
                            if (!ev.getCd_elemento_voce().startsWith(obblig.getObbligazione().getElemento_voce().getCd_elemento_voce())) {
                                ((IDocumentoAmministrativoBulk) fattura).addToDocumentiContabiliCancellati(obblig);
                                throw new it.cnr.jada.comp.ApplicationException("Il titolo capitolo dell'impegno deve essere uguale o appartenere al titolo capitolo della categoria inventario dei beni selezionati (\"" + ev.getCd_elemento_voce() + "\")!");
                            }
                        }
                    }
                }
                Obbligazione_scadenzarioBulk obbligazione = null;
                ObbligazioniTable obbHash = fattura.getObbligazioniHash();
                if (obbHash != null && !obbHash.isEmpty())
                    obbligazione = obbHash.getKey(obblig);
                if (obbligazione != null && obbligazione.getObbligazione().isTemporaneo()) {
                    java.util.Vector models = ((java.util.Vector) obbHash.get(obbligazione));
                    java.util.Vector clone = (java.util.Vector) models.clone();
                    if (!clone.isEmpty()) {
                        scollegaDettagliDaObbligazione(context, clone);
                        clone.addAll(selectedModels);basicDoContabilizza(context, obblig, clone);
                    } else {
                        obbHash.remove(obbligazione);
                        basicDoContabilizza(context, obblig, selectedModels);
                    }
                } else {
                    basicDoContabilizza(context, obblig, selectedModels);
                }
            } catch (it.cnr.jada.comp.ComponentException e) {
                return handleException(context, e);
            }
            bp.getObbligazioniController().getSelection().clear();
            bp.getObbligazioniController().setModelIndex(context, -1);
            bp.getObbligazioniController().setModelIndex(context, it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(bp.getObbligazioniController().getDetails(), obblig));

            bp.setDirty(true);
        }
        return context.findDefaultForward();
    }

    public Forward doRicercaObbligazioneOrdiniNoteCredito(ActionContext context) {

        try {
            CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP) getBusinessProcess(context);
            fillModel(context);
            Fattura_passivaBulk fatturaPassiva = (Fattura_passivaBulk) bp.getModel();
            List listaRigheFattura = getListaRigheFatturaOrdiniNotaCredito(context, bp, fatturaPassiva, false);
            if (listaRigheFattura.isEmpty()) {
                bp.setErrorMessage("Per procedere, selezionare gli ordini in attesa di nota credito da contabilizzare!");
                return context.findDefaultForward();
            }
            if (fatturaPassiva.getFornitore() == null || fatturaPassiva.getFornitore().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.UNDEFINED)
                throw new it.cnr.jada.comp.ApplicationException("Per eseguire questa operazione è necessario impostare un fornitore!");

            controllaSelezionePerContabilizzazione(context, listaRigheFattura.iterator());
            try {
                controllaSelezionePerTitoloCapitoloLista(context, listaRigheFattura.iterator());
            } catch (ApplicationException e) {
                throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
            }
            return basicDoRicercaObbligazione(context, fatturaPassiva, listaRigheFattura, true);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    private List getListaRigheFatturaOrdiniNotaCredito(ActionContext context, CRUDFatturaPassivaBP bp, Fattura_passivaBulk fatturaPassiva, Boolean righeToAddToFatturaPassivaRigaList) throws ValidationException, BusinessProcessException {
        List listaRigheFattura = new ArrayList();
        Optional<List> models = Optional.ofNullable(bp.getFatturaOrdiniController().getSelectedModels(context))
                .map(list -> {
                    final int focus = bp.getFatturaOrdiniController().getSelection().getFocus();
                    FatturaOrdineBulk fatturaOrdineBulk = (FatturaOrdineBulk) bp.getFatturaOrdiniController().getDetails().get(focus);
                    if (fatturaOrdineBulk.isRigaAttesaNotaCredito()) {
                        Fattura_passiva_rigaBulk rigaPerNotaCredito = new Fattura_passiva_rigaIBulk();
                        if (righeToAddToFatturaPassivaRigaList){
                            fatturaPassiva.addToFattura_passiva_dettColl(rigaPerNotaCredito);
                        } else {
                            rigaPerNotaCredito.setStato_cofi(Fattura_passivaBulk.STATO_INIZIALE);
                            rigaPerNotaCredito.setFattura_passiva(fatturaPassiva);
                            rigaPerNotaCredito.setTi_associato_manrev(Fattura_passivaBulk.NON_ASSOCIATO_A_MANDATO);

                            try {
                                java.sql.Timestamp ts = it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp();
                                rigaPerNotaCredito.setDt_da_competenza_coge((fatturaPassiva.getDt_da_competenza_coge() == null) ? ts : fatturaPassiva.getDt_da_competenza_coge());
                                rigaPerNotaCredito.setDt_a_competenza_coge((fatturaPassiva.getDt_a_competenza_coge() == null) ? ts : fatturaPassiva.getDt_a_competenza_coge());
                            } catch (javax.ejb.EJBException e) {
                                throw new it.cnr.jada.DetailedRuntimeException(e);
                            }
                            rigaPerNotaCredito.setProgressivo_riga(new Long(listaRigheFattura.size() + 1));
                        }
                        rigaPerNotaCredito.setBene_servizio(fatturaOrdineBulk.getOrdineAcqConsegna().getOrdineAcqRiga().getBeneServizio());
                        rigaPerNotaCredito.setVoce_iva(getVoceIvaOrdini(fatturaOrdineBulk));
                        rigaPerNotaCredito.setQuantita(BigDecimal.ONE);
                        rigaPerNotaCredito.setPrezzo_unitario(fatturaOrdineBulk.getImponibilePerNotaCredito().subtract(fatturaOrdineBulk.getImImponibile()));
                        rigaPerNotaCredito.setIm_iva(fatturaOrdineBulk.getImportoIvaPerNotaCredito().subtract(fatturaOrdineBulk.getImIva()));
                        rigaPerNotaCredito.setIm_imponibile(rigaPerNotaCredito.getPrezzo_unitario());
                        rigaPerNotaCredito.setIm_totale_divisa(rigaPerNotaCredito.getIm_imponibile());
                        rigaPerNotaCredito.setIm_diponibile_nc(rigaPerNotaCredito.getIm_imponibile().add(rigaPerNotaCredito.getIm_iva()));


                        rigaPerNotaCredito.setDs_riga_fattura("ppp");

                        listaRigheFattura.add(rigaPerNotaCredito);
                    }
                    return list;
                })
                .filter(list -> !list.isEmpty());
        return listaRigheFattura;
    }

    private Voce_ivaBulk getVoceIvaOrdini(FatturaOrdineBulk fatturaOrdineBulk) {
        Voce_ivaBulk iva = fatturaOrdineBulk.getOrdineAcqConsegna().getOrdineAcqRiga().getVoceIva();
        if (fatturaOrdineBulk.getVoceIva() != null && fatturaOrdineBulk.getCdVoceIvaRett() != null){
            iva = fatturaOrdineBulk.getVoceIva();
        }
        return iva;
    }

}
