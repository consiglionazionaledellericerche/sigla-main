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

import java.util.Date;

import it.cnr.contab.docamm00.comp.DocumentoAmministrativoComponentSession;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.bp.TitoloDiCreditoDebitoBP;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession;
import it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.doccont00.bp.CRUDVirtualAccertamentoBP;
import it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP;
import it.cnr.contab.docamm00.bp.RisultatoEliminazioneBP;
import it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.docamm00.bp.CRUDFatturaAttivaIBP;
import it.cnr.contab.docamm00.bp.CRUDNotaDiCreditoAttivaBP;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attiva_rigaBulk;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.*;

/**
 * Insert the type's description here.
 * Creation date: (10/30/2001 3:11:45 PM)
 * @author: Roberto Peli
 */
public class CRUDNotaDiCreditoAttivaAction extends CRUDFatturaAttivaAction {
/**
 * CRUDNotaDiCreditoAction constructor comment.
 */
public CRUDNotaDiCreditoAttivaAction() {
	super();
}
/**
 * Ricalcola l'importo disponibile per le note di credito sul dettagli riga rispetto al vecchio totale.
 * Reimplementato
 */
protected void basicCalcolaImportoDisponibileNC(
	ActionContext context, 
	Fattura_attiva_rigaBulk riga,
	java.math.BigDecimal vecchioTotale)
	throws it.cnr.jada.bulk.FillException {

	Nota_di_credito_attiva_rigaBulk rigaND = (Nota_di_credito_attiva_rigaBulk)riga;
	if (rigaND.getQuantita() == null) rigaND.setQuantita(new java.math.BigDecimal(1));
	if (rigaND.getPrezzo_unitario() == null) rigaND.setPrezzo_unitario(new java.math.BigDecimal(0));
	if (rigaND.getIm_iva() == null) rigaND.setIm_iva(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));

	rigaND.calcolaCampiDiRiga();
	java.math.BigDecimal totaleDiRiga = rigaND.getIm_imponibile().add(rigaND.getIm_iva());
	Fattura_attiva_rigaIBulk rigaFP = rigaND.getRiga_fattura_associata();
	java.math.BigDecimal nuovoImportoDisponibile = rigaFP.getIm_diponibile_nc().subtract(totaleDiRiga.subtract(vecchioTotale));
	if (nuovoImportoDisponibile.signum() < 0)
		throw new it.cnr.jada.bulk.FillException("Attenzione: l'importo di storno massimo ancora disponibile è di " + rigaFP.getIm_diponibile_nc() + " EUR!");
	rigaFP.setIm_diponibile_nc(nuovoImportoDisponibile.setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
}
/**
 * Gestisce l'associazione della scadenza riportata con il documento amm.
 * Se non esiste l'aggiunge, altrimenti sincronizza l'istanza già presente
 *
 * @param context	L'ActionContext della richiesta
 * @param scadenza	scadenza selezionata dall'utente con riporta
 * @return Il Forward alla pagina di risposta
 */
private Forward basicDoBringBackOpenObbligazioniWindow(
	ActionContext context,
	Obbligazione_scadenzarioBulk scadenza) {

	try {
		CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
		Obbligazione_scadenzarioBulk oldScadenza = (Obbligazione_scadenzarioBulk)bp.getObbligazioniController().getModel();
		resyncObbligazioneScadenzario(context, oldScadenza, scadenza);
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
	Fattura_attiva_rigaBulk riga,
	java.math.BigDecimal vecchioTotale) 
	throws it.cnr.jada.bulk.FillException {

	Nota_di_credito_attiva_rigaBulk rigaNC = (Nota_di_credito_attiva_rigaBulk)riga;
	fillModel( context );

	if (rigaNC.getQuantita() == null) rigaNC.setQuantita(new java.math.BigDecimal(1));
	if (rigaNC.getPrezzo_unitario() == null) rigaNC.setPrezzo_unitario(new java.math.BigDecimal(0));
	if (rigaNC.getIm_iva() == null) rigaNC.setIm_iva(new java.math.BigDecimal(0));

	rigaNC.setFl_iva_forzata(Boolean.FALSE);
	rigaNC.calcolaCampiDiRiga();
	java.math.BigDecimal totaleDiRiga = rigaNC.getIm_imponibile().add(rigaNC.getIm_iva());
	Fattura_attiva_rigaIBulk rigaFP = rigaNC.getRiga_fattura_associata();
	java.math.BigDecimal nuovoImportoDisponibile = rigaFP.getIm_diponibile_nc().subtract(totaleDiRiga.subtract(vecchioTotale));
	if (nuovoImportoDisponibile.signum() < 0)
		throw new it.cnr.jada.bulk.FillException("Attenzione: l'importo di storno massimo ancora disponibile è di " + rigaFP.getIm_diponibile_nc() + " EUR!");
	rigaFP.setIm_diponibile_nc(nuovoImportoDisponibile.setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	doSelectAccertamenti(context);
	doSelectObbligazioni(context);
}
/**
 * Richiede all'accertamento di modificare in automatico la sua scadenza (quella
 * selezionata) portando la stessa ad importo pari alla sommatoria degli importi 
 * di riga dei dettagli associati. Aggiorna la mappa dei saldi per le variazioni
 * subite dall'accertamento
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
private Forward basicDoModificaScadenzaAccertamentoInAutomatico(ActionContext context) {

	try {
		CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
		Nota_di_credito_attivaBulk notaDiCredito = (Nota_di_credito_attivaBulk)bp.getModel();

		Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk)bp.getAccertamentiController().getModel();

		if (scadenza == null)
			throw new it.cnr.jada.comp.ApplicationException("Selezionare l'accertamento da modificare in automatico!");
        //controllo che la scadenza appartenga all'esercizio di scrivania nella fase di cancellazione
		if (bp.isDeleting() &&
			!bp.isViewing() &&
			!it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).equals(scadenza.getEsercizio()))
			throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata appartiene all'esercizio " + scadenza.getEsercizio().intValue() + "! Operazione annullata.");

		AccertamentoAbstractComponentSession h = CRUDVirtualAccertamentoBP.getVirtualComponentSession(context, true);

		java.math.BigDecimal importoAttuale = notaDiCredito.getImportoTotalePerAccertamento();
		java.math.BigDecimal importoOriginale = (java.math.BigDecimal)notaDiCredito.getFattura_attiva_ass_totaliMap().get(scadenza);
		java.math.BigDecimal delta = importoOriginale.subtract(importoAttuale);
		if (new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP).compareTo(delta) == 0)
			throw new it.cnr.jada.comp.ApplicationException("La modifica in automatico non è disponibile!");
		try {
			scadenza = (Accertamento_scadenzarioBulk)h.modificaScadenzaInAutomatico(
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

		notaDiCredito.addToFattura_attiva_ass_totaliMap(scadenza, importoAttuale);
		
		bp.getAccertamentiController().getSelection().clear();
		bp.getAccertamentiController().setModelIndex(context, -1);
		bp.getAccertamentiController().setModelIndex(context,it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(bp.getAccertamentiController().getDetails(), scadenza));
		bp.setDirty(true);
		if (bp instanceof TitoloDiCreditoDebitoBP)
			((TitoloDiCreditoDebitoBP)bp).addToDocumentiContabiliModificati(scadenza);

	} catch(Exception e) {
		return handleException(context,e);
	}
	return context.findDefaultForward();
}
/**
 * Richiede all'obbligazione di modificare in automatico la sua scadenza (quella
 * selezionata) portando la stessa ad importo pari alla sommatoria degli importi 
 * di riga dei dettagli associati. Aggiorna la mappa dei saldi per le variazioni
 * subite dall'obbligazione
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
private Forward basicDoModificaScadenzaObbligazioneInAutomatico(ActionContext context) {

	try {
		CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
		Nota_di_credito_attivaBulk notaDiCredito = (Nota_di_credito_attivaBulk)bp.getModel();

		Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)bp.getObbligazioniController().getModel();

		if (scadenza == null)
			throw new it.cnr.jada.comp.ApplicationException("Selezionare l'impegno da modificare in automatico!");
        //controllo che la scadenza appartenga all'esercizio di scrivania nella fase di cancellazione
		if (bp.isDeleting() &&
			!bp.isViewing() &&
			!it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).equals(scadenza.getEsercizio()))
			throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata appartiene all'esercizio " + scadenza.getEsercizio().intValue() + "! Operazione annullata.");

		ObbligazioneAbstractComponentSession h = CRUDVirtualObbligazioneBP.getVirtualComponentSession(context, true);

		java.math.BigDecimal importoAttuale = notaDiCredito.getImportoTotalePerObbligazioni();
		java.math.BigDecimal importoOriginale = (java.math.BigDecimal)notaDiCredito.getFattura_attiva_ass_totaliMap().get(scadenza);
		java.math.BigDecimal delta = importoOriginale.subtract(importoAttuale);
		if (new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP).compareTo(delta) == 0)
			throw new it.cnr.jada.comp.ApplicationException("La modifica in automatico non è disponibile!");
		try {
			scadenza = (Obbligazione_scadenzarioBulk)h.modificaScadenzaInAutomatico(
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

		notaDiCredito.addToFattura_attiva_ass_totaliMap(scadenza, importoAttuale);
		
		bp.getObbligazioniController().getSelection().clear();
		bp.getObbligazioniController().setModelIndex(context, -1);
		bp.getObbligazioniController().setModelIndex(context,it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(bp.getObbligazioniController().getDetails(), scadenza));
		bp.setDirty(true);
	} catch(Exception e) {
		return handleException(context,e);
	}
	return context.findDefaultForward();
}
/**
 * Richiama sulla component il metodo per stornare i dettagli selezionati e ritorna la nota di credito aggiornata
 */
private Nota_di_credito_attivaBulk basicDoStorna(
	ActionContext context,
	Nota_di_credito_attivaBulk notaDiCredito, 
	java.util.List dettagliDaStornare,
	java.util.Hashtable relationsHash)
	throws it.cnr.jada.comp.ComponentException {

	CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)context.getBusinessProcess();

	try {
		FatturaAttivaSingolaComponentSession h = (FatturaAttivaSingolaComponentSession)bp.createComponentSession();
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
	it.cnr.jada.util.action.Selection selection) {

	try {
		CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
		Nota_di_credito_attivaBulk notaDiCredito = (Nota_di_credito_attivaBulk)bp.getModel();
		//controllaQuadraturaConti(context, notaDiCredito);

		Forward forward = context.findDefaultForward();
		java.util.Vector dettagliDaStornare = (java.util.Vector)getDettagliInStato(
													context, 
													selection.iterator(notaDiCredito.getFattura_attiva_dettColl()), 
													new String[] { Nota_di_credito_attiva_rigaBulk.STATO_CONTABILIZZATO });

		if (dettagliDaStornare != null && !dettagliDaStornare.isEmpty()) {
			if (notaDiCredito.getObbligazioniHash() != null && !notaDiCredito.getObbligazioniHash().isEmpty()) {
				throw new it.cnr.jada.comp.ApplicationException("Non è possibile procedere all'inserimento di un accertamento, perchè questa nota di credito contiene delle obbligazioni!");
			}
			notaDiCredito = basicDoStorna(
									context, 
									notaDiCredito,
									dettagliDaStornare,
									null);
			bp.setModel(context,notaDiCredito);
		}

		dettagliDaStornare = (java.util.Vector)getDettagliInStato(
										context, 
										selection.iterator(notaDiCredito.getFattura_attiva_dettColl()), 
										new String[] { Nota_di_credito_attivaBulk.STATO_PARZIALE, Nota_di_credito_attivaBulk.STATO_PAGATO });

		if (dettagliDaStornare != null && !dettagliDaStornare.isEmpty()) {
			//it.cnr.jada.util.RemoteIterator ri = ((FatturaAttivaSingolaComponentSession)bp.createComponentSession()).findAccertamentiFor(context.getUserContext(), notaDiCredito, calcolaTotaleSelezionati(dettagliDaStornare));
			//if (ri != null && ri.hasMoreElements()) {
				//if (notaDiCredito.getObbligazioniHash() != null && !notaDiCredito.getObbligazioniHash().isEmpty()) {
					//throw new it.cnr.jada.comp.ApplicationException("Non è possibile procedere all'inserimento di un accertamento, perchè questa nota di credito contiene delle obbligazioni!");
				//}
				//it.cnr.jada.util.action.SelezionatoreListaBP slbp = select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(Fattura_attiva_rigaIBulk.class), "default", "doSelezionaDettagli", bp);
				//HookForward hook = (HookForward)context.findForward("seleziona");
				//hook.addParameter("dettagliDaStornare", dettagliDaStornare);
				//forward = slbp;
			//} else {
				//it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(ri);
			//r.p. 22/11/2007 chiamiamo la maschera di ricerca delle obbligazioni
			forward = basicDoRicercaObbligazione(context, notaDiCredito, dettagliDaStornare);
			/*OptionBP optionBP = (OptionBP)openConfirm(context,"Alcuni dettagli sono già associati a reversale. Vuoi creare un'obbligazione?",OptionBP.CONFIRM_YES_NO,"doConfermaApriObbligazione");
			//optionBP.addAttribute("dettagliDaStornare", dettagliDaStornare);
			forward = optionBP;*/
			//}
		}
		return forward;
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Costruiscce le relazioni (per gestire lo storno) tra la riga della nota di credito e la riga della fattura
 * attiva selezionata (potrebbe essere quella di un'altra fattura, non necessariamente quella di origine) da
 * cui ottenere l'accertamento su cui stornare
 */
private Forward buildRelations(
	ActionContext context, 
	HookForward hook,
	java.util.List dettagliDaStornare,
	java.util.Enumeration details,
	java.util.Hashtable relationsHash) {

// DA FARE

		
	try {
		CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
		Nota_di_credito_attivaBulk notaDiCredito = (Nota_di_credito_attivaBulk)bp.getModel();

		Forward fwd = null;
		if (details.hasMoreElements()) {
			Nota_di_credito_attiva_rigaBulk rigaNdC = (Nota_di_credito_attiva_rigaBulk)details.nextElement();
			if (relationsHash == null)
				relationsHash = new java.util.Hashtable();
			Fattura_attiva_rigaIBulk selectedRigaFattura = null;
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
			bp.setModel(context,notaDiCredito);
			fwd = context.findDefaultForward();
		}
		return fwd;
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
 * figlio del ricevente
 */
public Forward doAddToCRUDMain_Dettaglio(ActionContext context) {

    try {
	    CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
	    bp.getDettaglio().getSelection().clearSelection();
	    fillModel(context);
	    it.cnr.jada.util.RemoteIterator ri = ((FatturaAttivaSingolaComponentSession)bp.createComponentSession()).cercaFatturaPerNdC(context.getUserContext(), null, (Nota_di_credito_attivaBulk)bp.getModel());
	    return select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(Fattura_attiva_IBulk.class), "default", "doSelezionaRighe");
    } catch(Throwable e) {
		return handleException(context,e);
    }
}
/**
 * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
 * figlio del ricevente
 */
public Forward doAddToCRUDMain_Obbligazioni_DettaglioObbligazioni(ActionContext context) {

    try {
        CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP) getBusinessProcess(context);
        bp.getDettaglio().getSelection().clearSelection();
        fillModel(context);

        java.util.Vector selectedModels = (java.util.Vector) getDettagliInStato(context,
									                bp.getDettaglio().getDetails().iterator(),
									                new String[] { Nota_di_credito_attiva_rigaBulk.STATO_INIZIALE });
        if (selectedModels.isEmpty())
            throw new it.cnr.jada.comp.ApplicationException(
                "Tutti i dettagli sono già stati stornati!");
        it.cnr.jada.util.action.SelezionatoreListaBP slbp =
            (it.cnr.jada.util.action.SelezionatoreListaBP) select(context,
                new it.cnr.jada.util.ListRemoteIterator(selectedModels),
                it.cnr.jada.bulk.BulkInfo.getBulkInfo(Nota_di_credito_attiva_rigaBulk.class),
                "righeNdCSet",
                "doBringBackAddToCRUDMain_Obbligazioni_DettaglioObbligazioni");
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
public Forward doBringBackAddToCRUDMain_Obbligazioni_DettaglioObbligazioni(ActionContext context) {

    try {
        HookForward fwd = (HookForward) context.getCaller();
        Forward forward = context.findDefaultForward();
        it.cnr.jada.util.action.Selection selection =
            (it.cnr.jada.util.action.Selection) fwd.getParameter("selection");
        if (selection != null && !selection.isEmpty()) {
            CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP) getBusinessProcess(context);
            java.util.List dett = ((Nota_di_credito_attivaBulk)bp.getModel()).getFattura_attiva_dettColl();
            Selection newSelection = getIndexSelectionOn(selection, dett, "stornato");
            forward = basicDoStornaDettagli(context, newSelection);

            if (!(forward instanceof it.cnr.jada.util.action.SelezionatoreListaBP)) {
                bp.getDettaglio().reset(context);
                bp.getObbligazioniController().setModelIndex(context,-1);

                bp.setDirty(true);
            }
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

	HookForward caller = (HookForward)context.getCaller();

	CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
	try {
			if (caller.getParameter("undoBringBack") != null)
				throw new it.cnr.jada.comp.ApplicationException("Cancellazione annullata!");
			Risultato_eliminazioneVBulk re = (Risultato_eliminazioneVBulk)caller.getParameter("bringback");
			if (!re.getDocumentiAmministrativiScollegati().isEmpty())
				throw new it.cnr.jada.comp.ApplicationException("Eseguire il controllo di quadratura per tutti i dettagli in elenco!");
			
			if (bp.isCarryingThrough()) {
				try {
					bp.riportaAvanti(context);
				} catch (Throwable t) {
					bp.rollbackToSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
					bp.setModel(context, (Nota_di_credito_attivaBulk)caller.getParameter("originalClone"));
					throw t;
				}
			}
			bp.commitUserTransaction();
			bp.setModel(context,bp.initializeModelForEdit(context,bp.getModel()));
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
 * Richiede l'associazione dell'obbligazione selezionata dall'utente ai dettagli 
 * di ndc selezionati per la contabilizzazione ('basicDoBringBackOpenObbligazioniWindow')
 * Ricalcola i totali di scadenza
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doBringBackOpenObbligazioniWindow(ActionContext context) {

	HookForward caller = (HookForward)context.getCaller();

	Forward fwd = context.findDefaultForward();
	CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
	Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)caller.getParameter("obbligazioneSelezionata");
	if (scadenza==null)
		scadenza = (Obbligazione_scadenzarioBulk)caller.getParameter("bringback");
	if (scadenza != null) {
		fwd = basicDoBringBackOpenObbligazioniWindow(context, scadenza);

		bp.getDettaglio().reset(context);
		bp.getObbligazioniController().setModelIndex(context,-1);
		
		doCalcolaTotalePerObbligazione(context, (Obbligazione_scadenzarioBulk)bp.getObbligazioniController().getModel());
		
		bp.setDirty(true);
	}
	if (!"tabFatturaAttivaObbligazioni".equals(bp.getTab("tab")))
		bp.setTab("tab", "tabFatturaAttivaObbligazioni");

	return fwd;
}
/**
 * Ricalcola il valore totale degli importi associati alla scadenza
 */
 
public Forward doCalcolaTotalePerObbligazione(ActionContext context, Obbligazione_scadenzarioBulk scadenza) {

	it.cnr.jada.util.action.FormBP bulkBP = (it.cnr.jada.util.action.FormBP)context.getBusinessProcess();
	if (bulkBP instanceof CRUDNotaDiCreditoAttivaBP) {
		CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)bulkBP;
		Nota_di_credito_attivaBulk ndC = (Nota_di_credito_attivaBulk)bp.getModel();
		if (ndC.getObbligazioniHash() != null && scadenza != null)
			try {
				ndC.setImportoTotalePerObbligazioni(calcolaTotaleSelezionati((java.util.List)ndC.getObbligazioniHash().get(scadenza),ndC.quadraturaInDeroga()));
			} catch (it.cnr.jada.comp.ApplicationException e) {
				ndC.setImportoTotalePerObbligazioni(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
			}
		else
			ndC.setImportoTotalePerObbligazioni(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	}
	return context.findDefaultForward();	
}
/**
 * Gestisce una richiesta di ricerca.
 *
 * L'implementazione di default utilizza il metodo astratto <code>read</code>
 * di <code>CRUDBusinessProcess</code>.
 * Se la ricerca fornisce più di un risultato viene creato un
 * nuovo <code>SelezionatoreListaBP</code> per la selezione di un elemento.
 * Al business process viene anche chiesto l'elenco delle colonne da
 * visualizzare.
 */
public Forward doCerca(ActionContext context) throws java.rmi.RemoteException,InstantiationException,javax.ejb.RemoveException {

	try {
		CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
		if (bp.getParent() != null && bp.getParent() instanceof CRUDFatturaAttivaIBP) {
			fillModel(context);
			Fattura_attiva_IBulk fa = (Fattura_attiva_IBulk)((CRUDFatturaAttivaIBP)bp.getParent()).getModel();
			FatturaAttivaSingolaComponentSession h = (FatturaAttivaSingolaComponentSession)bp.createComponentSession();
			it.cnr.jada.util.RemoteIterator ri = h.findNotaDiCreditoFor(context.getUserContext(), fa);
			ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
			if (ri == null || ri.countElements() == 0) {
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
				bp.setMessage("La ricerca non ha fornito alcun risultato.");
				return context.findDefaultForward();
			}
			else if (ri.countElements() == 1) {
				OggettoBulk bulk = (OggettoBulk)ri.nextElement();
				bp.setMessage(FormBP.INFO_MESSAGE,"La ricerca ha fornito un solo risultato.");
				bp.edit(context,bulk);
				return context.findDefaultForward();
			} else {
				bp.setModel(context,bp.getModel());
				SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
				nbp.setIterator(context,ri);
				nbp.setBulkInfo(bp.getBulkInfo());
				nbp.setColumns(bp.getSearchResultColumns());
				context.addHookForward("seleziona",this,"doRiportaSelezione");
				return context.addBusinessProcess(nbp);
			}
		}
		return super.doCerca(context);
	} catch(Throwable e) {
		return handleException(context,e);
	} 
}
/**
 * Chiede conferma all'utente per proseguire nell'operazione di contabilizzazione su una obbligazione 
 * nel caso in cui non esistano scadenze di accertamenti valide.
 *
 * @param context Il contesto della action
 * @return Il default forward.
 */
public Forward doConfermaApriObbligazione(ActionContext context, OptionBP optionBP) {
	//r.p. 22/11/2007 non viene + richiamata viene chiamata il bp RicercaObbligazioniBP
	CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
	if (optionBP.getOption() == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
		try {
			it.cnr.contab.docamm00.docs.bulk.AccertamentiTable accertamenti = ((Nota_di_credito_attivaBulk)bp.getModel()).getFattura_attiva_accertamentiHash();
			if (accertamenti != null && !accertamenti.isEmpty()) {
				throw new it.cnr.jada.comp.ApplicationException("Non è possibile procedere all'inserimento di un'obbligazione, perchè questa nota di credito contiene degli accertamenti!");
			}
			java.util.List dettagliDaStornare = (java.util.List)optionBP.getAttribute("dettagliDaStornare");

			it.cnr.contab.doccont00.bp.CRUDObbligazioneBP obbligazioneBP = (it.cnr.contab.doccont00.bp.CRUDObbligazioneBP)context.getUserInfo().createBusinessProcess(context,"CRUDObbligazioneBP",new Object[] { "MRSWTh" });
			obbligazioneBP.reset(context);
			ObbligazioneBulk obbligazione = (ObbligazioneBulk)obbligazioneBP.getModel();
			obbligazione.completeFrom(context, (Nota_di_credito_attivaBulk)bp.getModel(), dettagliDaStornare);
			obbligazioneBP.getScadenzario().setModelIndex(context,0);
			
			context.addHookForward("bringback",this,"doBringBackOpenObbligazioniWindow");
			HookForward hook = (HookForward)context.findForward("bringback");
			hook.addParameter("dettagliDaStornare", dettagliDaStornare);
			return context.addBusinessProcess(obbligazioneBP);
		} catch (it.cnr.jada.comp.ApplicationException e) {
			return handleException(context, e);
		} catch(Exception e) {
			return handleException(context,e);
		}		
	}

	bp.getDettaglio().reset(context);
	bp.getObbligazioniController().setModelIndex(context,-1);
	bp.setDirty(true);
	if (!"tabFatturaAttivaObbligazioni".equals(bp.getTab("tab")))
		bp.setTab("tab", "tabFatturaAttivaObbligazioni");

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
	doSelectObbligazioni(context);
	return fwd;
}
/**
 * Richiede al documento contabile di aggiornare l'importo della scadenza selezionata in automatico
 * Reimplementato per gestire anche le scadenze di obbligazioni
 *
 * @param context	L'ActionContext della richiesta
 * @param prefix	
 * @return Il Forward alla pagina di risposta
 */
public Forward doModificaScadenzaInAutomatico(ActionContext context, String prefix) {

	try {
		CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
		fillModel(context);
		Nota_di_credito_attivaBulk notaDiCredito = (Nota_di_credito_attivaBulk)bp.getModel();

		if ("main.Accertamenti".equals(prefix))
			return basicDoModificaScadenzaAccertamentoInAutomatico(context);
		else
			return basicDoModificaScadenzaObbligazioneInAutomatico(context);
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce il cambiamento delle modalità di pagamento del creditore per collegamenti ad obbligazioni
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doOnModalitaPagamentoChange(ActionContext context) {

	try {
		fillModel(context);
		CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
		Nota_di_credito_attivaBulk notaDiCredito = (Nota_di_credito_attivaBulk)bp.getModel();
		if (notaDiCredito.getModalita_pagamento() != null) {
			FatturaAttivaSingolaComponentSession facs = (FatturaAttivaSingolaComponentSession)bp.createComponentSession();
			java.util.Collection coll = facs.findListabanche(context.getUserContext(), notaDiCredito);
			notaDiCredito.setBanca((coll == null || coll.isEmpty()) ? null : (BancaBulk)new java.util.Vector(coll).firstElement());
		} else {
			notaDiCredito.setBanca(null);
		}
		bp.setModel(context,notaDiCredito);
	} catch (Throwable t) {
		return handleException(context, t);
	}
	return context.findDefaultForward();
}
/**
 * richiede l'apertura del pannello dell'accertamento per la modifica della
 * scadenza selezionata
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doOpenAccertamentiWindow(ActionContext context) {

	try {
		CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
		fillModel(context);

		Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk)bp.getAccertamentiController().getModel();
		boolean viewMode = bp.isViewing();
		if (scadenza == null)
			throw new it.cnr.jada.comp.ApplicationException("Selezionare l'accertamento da " + (viewMode?"visualizzare":"modificare") + " in manuale!");
        //controllo che la scadenza appartenga all'esercizio di scrivania nella fase di cancellazione
		if (bp.isDeleting() &&
			!bp.isViewing() &&
			!it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).equals(scadenza.getEsercizio()))
			throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata appartiene all'esercizio " + scadenza.getEsercizio().intValue() + "! Operazione annullata.");

		if (!viewMode && bp instanceof IDocumentoAmministrativoBP)
				viewMode = !((IDocumentoAmministrativoBP)bp).getDocumentoAmministrativoCorrente().isEditable();
		String status = viewMode ?"V":"M";
		CRUDVirtualAccertamentoBP nbp = CRUDVirtualAccertamentoBP.getBusinessProcessFor(context, scadenza.getAccertamento(), status + "RSWTh");
		nbp.edit(context,scadenza.getAccertamento(), true);
		nbp.selezionaScadenza(scadenza, context);

		context.addHookForward("bringback",this,"doBringBackOpenAccertamentiWindow");
		HookForward hook = (HookForward)context.findForward("bringback");
		return context.addBusinessProcess(nbp);
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * richiede l'apertura del pannello dell'obbligazione per la modifica della
 * scadenza selezionata
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doOpenObbligazioniWindow(ActionContext context) {

	try {
		CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
		fillModel(context);

		Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)bp.getObbligazioniController().getModel();
		boolean viewMode = bp.isViewing();
		if (scadenza == null)
			throw new it.cnr.jada.comp.ApplicationException("Selezionare l'impegno da " + (viewMode?"visualizzare":"modificare") + " in manuale!");
        //controllo che la scadenza appartenga all'esercizio di scrivania nella fase di cancellazione
		if (bp.isDeleting() &&
			!bp.isViewing() &&
			!it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).equals(scadenza.getEsercizio()))
			throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata appartiene all'esercizio " + scadenza.getEsercizio().intValue() + "! Operazione annullata.");

		if (!viewMode && bp instanceof IDocumentoAmministrativoBP)
				viewMode = !((IDocumentoAmministrativoBP)bp).getDocumentoAmministrativoCorrente().isEditable();
		String status = viewMode ?"V":"M";
		CRUDVirtualObbligazioneBP obp = CRUDVirtualObbligazioneBP.getBusinessProcessFor(context, scadenza.getObbligazione(), status + "RSWTh");
		obp.edit(context, scadenza.getObbligazione(), true);
		obp.selezionaScadenza(scadenza, context);
		
		context.addHookForward("bringback",this,"doBringBackOpenObbligazioniWindow");
		HookForward hook = (HookForward)context.findForward("bringback");
		return context.addBusinessProcess(obp);
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Risponde all'evento di fine cancellazione dei documenti amministrativi di tipo
 * attivo. Reimplementato
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
 public Forward doPostConfirmDelete(
	ActionContext context, 
	Risultato_eliminazioneVBulk re) 
	throws it.cnr.jada.action.BusinessProcessException {

	it.cnr.jada.util.action.CRUDBP bp = getBusinessProcess(context);
	Fattura_attivaBulk deletedDoc = (Fattura_attivaBulk)bp.getModel();
	doConfirmCloseForm(context, it.cnr.jada.util.action.OptionBP.YES_BUTTON);
	bp = getBusinessProcess(context);
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
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
protected Forward doPostSaveEvent(
	ActionContext context,
	CRUDNotaDiCreditoAttivaBP bp,
	Nota_di_credito_attivaBulk originalClone) 
	throws it.cnr.jada.action.BusinessProcessException, it.cnr.jada.bulk.ValidationException {

	Nota_di_credito_attivaBulk ndc = (Nota_di_credito_attivaBulk)bp.getModel();
	
	RisultatoEliminazioneBP rebp = (RisultatoEliminazioneBP)context.createBusinessProcess("RisultatoEliminazioneBP", new String[] { "MRSWTh" });
	Risultato_eliminazioneVBulk deleteManager = bp.getDeleteManager();

	if(ndc.getDettagliCancellati() != null)
		for (java.util.Iterator i = ndc.getDettagliCancellati().iterator(); i.hasNext();) {
			IDocumentoAmministrativoRigaBulk riga = (IDocumentoAmministrativoRigaBulk)i.next();
			deleteManager.add(riga);
			java.math.BigDecimal totRiga = riga.getIm_imponibile().add(riga.getIm_iva());
			IDocumentoAmministrativoRigaBulk originalDetail = riga.getOriginalDetail();
			if (originalDetail != null ) {
				java.math.BigDecimal impDisponibile = originalDetail.getIm_diponibile_nc();
				originalDetail.setIm_diponibile_nc(impDisponibile.add(riga.getFather().getImportoSignForDelete(totRiga)));
				try {
					((DocumentoAmministrativoComponentSession)bp.createComponentSession()).update(
										context.getUserContext(), 
										originalDetail);
				} catch (Throwable e) {
					return handleException(context, e);
				}
			}
		}
	//if(ndc.getDocumentiContabiliCancellati() != null)
		//for (java.util.Iterator i = ndc.getDocumentiContabiliCancellati().iterator(); i.hasNext();)
			//deleteManager.add((IDocumentoContabileBulk)i.next());
	//)
	if (deleteManager != null && 
		(!deleteManager.getDocumentiAmministrativiScollegati().isEmpty() || !deleteManager.getDocumentiContabiliScollegati().isEmpty())) {

		rebp.initializeControllers(context, ndc);
		rebp.edit(context, deleteManager);
						
		context.addHookForward("bringback",this,"doBringBackConfirmDeleteRow");
		HookForward hook = (HookForward)context.findForward("bringback");
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
	bp.setModel(context,bp.initializeModelForEdit(context,bp.getModel()));
	bp.setStatus(bp.EDIT);
	bp.setCarryingThrough(false);
	bp.setDirty(false);
	return context.findDefaultForward();
}
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di cancellazione dal controller "accertamenti"
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doRemoveFromCRUDMain_Accertamenti(ActionContext context) {
	
	CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)context.getBusinessProcess();
	it.cnr.jada.util.action.Selection selection = bp.getAccertamentiController().getSelection();
	try {
		if (selection.isEmpty())
			throw new it.cnr.jada.comp.ApplicationException("Selezionare le scadenze che si desidera eliminare!");
	} catch (it.cnr.jada.comp.ApplicationException e) {
		return handleException(context, e);
	}
	java.util.List accertamenti = bp.getAccertamentiController().getDetails();
	for (it.cnr.jada.util.action.SelectionIterator i = selection.iterator();i.hasNext();) {
		Accertamento_scadenzarioBulk accertamento = (Accertamento_scadenzarioBulk)accertamenti.get(i.nextIndex());
		if (bp.isDocumentoContabileModificato(accertamento))
			return handleException(
							context, 
							new it.cnr.jada.comp.ApplicationException("La scadenza \"" + accertamento.getDs_scadenza() + "\" è stata già modificata. Impossibile cancellarla."));
		Nota_di_credito_attivaBulk ndC = (Nota_di_credito_attivaBulk)bp.getModel();
		java.util.Vector models = (java.util.Vector)ndC.getFattura_attiva_accertamentiHash().get(accertamento);
		try {
			if (models != null && models.isEmpty()) {
				ndC.getFattura_attiva_accertamentiHash().remove(accertamento);
				ndC.addToDocumentiContabiliCancellati(accertamento);
			} else {
				scollegaDettagliDaAccertamento(context, (java.util.List)models.clone());
			}
		} catch (it.cnr.jada.comp.ComponentException e) {
			return handleException(context, e);
		}

		setAndVerifyStatusFor(context, ndC);
		
		bp.getAccertamentiController().getSelection().clear();
		bp.getAccertamentiController().setModelIndex(context,-1);

		doCalcolaTotalePerAccertamento(context, null);

		bp.setDirty(true);
	}
	return context.findDefaultForward();
}
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di cancellazione dal controller "accertamenti_DettaglioAccertamenti"
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doRemoveFromCRUDMain_Accertamenti_DettaglioAccertamenti(ActionContext context) {

	CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)context.getBusinessProcess();
	try {
		it.cnr.jada.util.action.Selection selection = bp.getDettaglioAccertamentoController().getSelection();
		if (selection.isEmpty())
			throw new it.cnr.jada.comp.ApplicationException("Selezionare i dettagli che si desidera scollegare!");
		java.util.List models = selection.select(bp.getDettaglioAccertamentoController().getDetails());
		if (models != null)
			for (java.util.Iterator i = models.iterator(); i.hasNext();) {
				Nota_di_credito_attiva_rigaBulk dettaglio = (Nota_di_credito_attiva_rigaBulk)i.next();
				Accertamento_scadenzarioBulk accertamentoSelezionato = dettaglio.getAccertamento_scadenzario();
				if (bp.isDocumentoContabileModificato(accertamentoSelezionato))
					throw new it.cnr.jada.comp.ApplicationException("La scadenza \"" + accertamentoSelezionato.getDs_scadenza() + "\" è stata già modificata. Impossibile cancellarla.");
			}
		scollegaDettagliDaAccertamento(context, models);
	} catch (it.cnr.jada.comp.ComponentException e) {
		return handleException(context, e);
	}

	doCalcolaTotalePerAccertamento(context, (Accertamento_scadenzarioBulk)bp.getAccertamentiController().getModel());

	Nota_di_credito_attivaBulk ndC = (Nota_di_credito_attivaBulk)bp.getModel();

	setAndVerifyStatusFor(context, ndC);

	bp.getDettaglioAccertamentoController().getSelection().clear();
	bp.getDettaglioAccertamentoController().setModelIndex(context,-1);
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
		CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)context.getBusinessProcess();
		java.util.List dettagli = bp.getDettaglio().getDetails();
		for (it.cnr.jada.util.action.SelectionIterator i = bp.getDettaglio().getSelection().iterator();i.hasNext();) {
			Fattura_attiva_rigaBulk dettaglio = (Fattura_attiva_rigaBulk)dettagli.get(i.nextIndex());
			if (bp.isDocumentoContabileModificato(dettaglio.getAccertamento_scadenzario()))
				return handleException(
								context, 
								new it.cnr.jada.comp.ApplicationException("La scadenza associata a \"" + dettaglio.getDs_riga_fattura() + "\" è stata già modificata. Impossibile cancellare."));
		}
		bp.getDettaglio().remove(context);
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di cancellazione dal controller "obbligazioni"
 * Reimplementato
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doRemoveFromCRUDMain_Obbligazioni(ActionContext context) {
	
	CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)context.getBusinessProcess();
	it.cnr.jada.util.action.Selection selection = bp.getObbligazioniController().getSelection();
	try {
		if (selection.isEmpty())
			throw new it.cnr.jada.comp.ApplicationException("Selezionare le scadenze che si desidera eliminare!");
	} catch (it.cnr.jada.comp.ApplicationException e) {
		return handleException(context, e);
	}
	java.util.List obbligazioni = bp.getObbligazioniController().getDetails();
	for (it.cnr.jada.util.action.SelectionIterator i = selection.iterator();i.hasNext();) {
		Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)obbligazioni.get(i.nextIndex());
		Nota_di_credito_attivaBulk ndC = (Nota_di_credito_attivaBulk)bp.getModel();
		java.util.Vector models = (java.util.Vector)ndC.getObbligazioniHash().get(scadenza);
		try {
			if (models != null && models.isEmpty()) {
				ndC.getObbligazioni_scadenzarioHash().remove(scadenza);
				ndC.addToDocumentiContabiliCancellati(scadenza);
			} else {
				scollegaDettagliDaObbligazione(context, (java.util.List)models.clone());
			}
		} catch (it.cnr.jada.comp.ComponentException e) {
			return handleException(context, e);
		}

		doCalcolaTotalePerObbligazione(context, null);

		setAndVerifyStatusFor(context, ndC);

		bp.getObbligazioniController().getSelection().clear();
		bp.getObbligazioniController().setModelIndex(context,-1);
		bp.setDirty(true);
	}
	return context.findDefaultForward();
}
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di cancellazione dal controller "obbligazioni_DettaglioObbligazioni"
 * Reimplementato
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doRemoveFromCRUDMain_Obbligazioni_DettaglioObbligazioni(ActionContext context) {

	CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)context.getBusinessProcess();
	try {
		it.cnr.jada.util.action.Selection selection = bp.getDettaglioObbligazioneController().getSelection();
		if (selection.isEmpty())
			throw new it.cnr.jada.comp.ApplicationException("Selezionare i dettagli che si desidera scollegare!");
		java.util.List models = selection.select(bp.getDettaglioObbligazioneController().getDetails());
		scollegaDettagliDaObbligazione(context, models);
	} catch (it.cnr.jada.comp.ComponentException e) {
		return handleException(context, e);
	}

	doCalcolaTotalePerObbligazione(context, (Obbligazione_scadenzarioBulk)bp.getObbligazioniController().getModel());

	Nota_di_credito_attivaBulk ndC = (Nota_di_credito_attivaBulk)bp.getModel();

	setAndVerifyStatusFor(context, ndC);

	bp.getDettaglioObbligazioneController().getSelection().clear();
	bp.getDettaglioObbligazioneController().setModelIndex(context,-1);
	java.util.List dettagli = bp.getDettaglioObbligazioneController().getDetails();
	if (dettagli == null || dettagli.isEmpty()) {
		bp.getObbligazioniController().getSelection().clear();
		bp.getObbligazioniController().setModelIndex(context, -1);
	}

	bp.setDirty(true);
	
	return context.findDefaultForward();
}
public Forward doRiportaAvanti(ActionContext context) throws java.rmi.RemoteException {

	CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
	Nota_di_credito_attivaBulk originalClone = (Nota_di_credito_attivaBulk)bp.getModel();
	try {
		fillModel(context);

		bp.setSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
		bp.salvaRiportandoAvanti(context);

		Nota_di_credito_attivaBulk ndc = (Nota_di_credito_attivaBulk)bp.getModel();
		if (bp.isEditing() &&
			((ndc.getDettagliCancellati() != null && !ndc.getDettagliCancellati().isEmpty()) ||
			(ndc.getDocumentiContabiliCancellati() != null && !ndc.getDocumentiContabiliCancellati().isEmpty())))
			return doPostSaveEvent(context, bp, originalClone);

		bp.riportaAvanti(context);
		bp.commitUserTransaction();
		bp.setModel(context,bp.initializeModelForEdit(context,bp.getModel()));
		bp.setStatus(bp.EDIT);
		bp.setCarryingThrough(false);
		
		bp.getObbligazioniController().setModelIndex(context, -1);
		bp.getAccertamentiController().setModelIndex(context, -1);
		
		bp.setDirty(false);
		return context.findDefaultForward();
	} catch(it.cnr.jada.bulk.ValidationException e) {
		getBusinessProcess(context).setErrorMessage(e.getMessage());
		return context.findDefaultForward();
	} catch(Throwable e) {
		try {
			bp.rollbackToSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
			bp.setModel(context, originalClone);
		} catch (it.cnr.jada.action.BusinessProcessException ex) {
			return handleException(context, ex);
		}
		return handleException(context,e);
	}
}
/**
 * Gestisce una richiesta di salvataggio. Reimplementato
 */
public Forward doSalva(ActionContext context) throws java.rmi.RemoteException {

	CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
	Nota_di_credito_attivaBulk originalClone = (Nota_di_credito_attivaBulk)bp.getModel();
	try {
		fillModel(context);

		bp.setSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
		bp.save(context);
		postSalvataggio(context);

		Nota_di_credito_attivaBulk ndc = (Nota_di_credito_attivaBulk)bp.getModel();
		if (bp.isEditing() &&
			((ndc.getDettagliCancellati() != null && !ndc.getDettagliCancellati().isEmpty()) ||
			(ndc.getDocumentiContabiliCancellati() != null && !ndc.getDocumentiContabiliCancellati().isEmpty())))
			return doPostSaveEvent(context, bp, originalClone);

		bp.commitUserTransaction();
		bp.setModel(context,bp.initializeModelForEdit(context,bp.getModel()));
		bp.setStatus(bp.EDIT);
		bp.setCarryingThrough(false);
		bp.setDirty(false);
		return context.findDefaultForward();
	} catch(it.cnr.jada.bulk.ValidationException e) {
		getBusinessProcess(context).setErrorMessage(e.getMessage());
		return context.findDefaultForward();
	} catch(Throwable e) {
		try {
			bp.rollbackToSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
			bp.setModel(context, originalClone);
		} catch (it.cnr.jada.action.BusinessProcessException ex) {
			return handleException(context, ex);
		}
		return handleException(context,e);
	}
}
/**
 * Ricerca le banche valide per il creditore (nel caso di collegamentio ad obbligazioni)
 */
public Forward doSearchListabanche(ActionContext context) {

	Nota_di_credito_attivaBulk ndc = (Nota_di_credito_attivaBulk)getBusinessProcess(context).getModel();
	return search(context, getFormField(context, "main.listabanche"), ndc.getModalita_pagamento().getTiPagamentoColumnSet());
}
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di selezione dal controller "obbligazioni"
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doSelectObbligazioni(ActionContext context) {

	CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)context.getBusinessProcess();
	try {
		bp.getObbligazioniController().setSelection(context);
	} catch (Throwable e) {
		return handleException(context, e);
	}

	doCalcolaTotalePerObbligazione(context, (Obbligazione_scadenzarioBulk)bp.getObbligazioniController().getModel());
	return context.findDefaultForward();	
}
	
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di storno dei dettagli selezionati scelti da una fattura attiva divera da quella di origine
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doSelezionaDettagli(ActionContext context) {

	CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)context.getBusinessProcess();
	HookForward caller = (HookForward)context.getCaller();
	Fattura_attiva_rigaIBulk riga = (Fattura_attiva_rigaIBulk)caller.getParameter("focusedElement");
	if (riga != null) {
		java.util.Hashtable relationsHash = new java.util.Hashtable();
		java.util.List dettagliDaStornare = (java.util.List)caller.getParameter("dettagliDaStornare");
		if (dettagliDaStornare != null) {
			for (java.util.Iterator i = dettagliDaStornare.iterator(); i.hasNext();)
				relationsHash.put(i.next(),riga);
			if (!relationsHash.isEmpty()) {
				Nota_di_credito_attivaBulk notaDiCredito = (Nota_di_credito_attivaBulk)bp.getModel();
				try {
					notaDiCredito = basicDoStorna(
										context, 
										notaDiCredito,
										dettagliDaStornare,
										relationsHash);
					bp.setModel(context,notaDiCredito);
				} catch (it.cnr.jada.comp.ComponentException e) {
					return handleException(context, e);
				} catch (it.cnr.jada.action.BusinessProcessException e) {
					return handleException(context, e);
				}
			}

			bp.getDettaglio().reset(context);
			bp.getAccertamentiController().setModelIndex(context,-1);

			bp.setDirty(true);
		}
	}
	if (!"tabFatturaAttivaAccertamenti".equals(bp.getTab("tab")))
		bp.setTab("tab", "tabFatturaAttivaAccertamenti");
	return context.findDefaultForward();
}
/**
 * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
 * figlio del ricevente
 */ 
public Forward doSelezionaDettaglioPerNdC(ActionContext context) {

	try {
		it.cnr.jada.action.HookForward caller = (it.cnr.jada.action.HookForward)context.getCaller();
		java.util.List selectedElements = (java.util.List)caller.getParameter("selectedElements");
		if (selectedElements != null && !selectedElements.isEmpty()) {
			CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)context.getBusinessProcess();
			java.util.Vector elementsToBeAdded = new java.util.Vector();
			java.util.Vector elementsToDischarged = new java.util.Vector();
			for (java.util.Iterator els = selectedElements.iterator(); els.hasNext();) {
				Fattura_attiva_rigaIBulk dettaglio = (Fattura_attiva_rigaIBulk)els.next();
				try {
					for (java.util.Iterator i = bp.getDettaglio().getDetails().iterator(); i.hasNext();) {
						if (((Nota_di_credito_attiva_rigaBulk)i.next()).getRiga_fattura_associata().equalsByPrimaryKey(dettaglio))
							throw new it.cnr.jada.bulk.ValidationException();
					}
					elementsToBeAdded.add(dettaglio);
				} catch (it.cnr.jada.bulk.ValidationException e) {
					if (!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(elementsToDischarged, dettaglio))
						elementsToDischarged.add(dettaglio);
				}
			}
			for (java.util.Iterator i = elementsToBeAdded.iterator(); i.hasNext();) {
				Fattura_attiva_rigaIBulk dettaglio = (Fattura_attiva_rigaIBulk)i.next();
				if (dettaglio.getCd_tariffario() != null && dettaglio.getTariffario() == null) {
					it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk tariffario = ((FatturaAttivaSingolaComponentSession)bp.createComponentSession()).findTariffario(context.getUserContext(), dettaglio);
					dettaglio.setTariffario(tariffario);
				}
				if (dettaglio.getIm_diponibile_nc() == null || 
					dettaglio.getIm_diponibile_nc().compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)) != 0) {

					Nota_di_credito_attiva_rigaBulk rigaNdC = new Nota_di_credito_attiva_rigaBulk();
					Nota_di_credito_attivaBulk notaDiCredito = (Nota_di_credito_attivaBulk)bp.getModel();
					notaDiCredito.addToFattura_attiva_dettColl(rigaNdC);
					rigaNdC.setUser(context.getUserInfo().getUserid());
					rigaNdC.copyFrom(dettaglio);
  				    if(rigaNdC.getRiga_fattura_associata().getFattura_attiva().getDt_emissione()==null ||(rigaNdC.getRiga_fattura_associata().getFattura_attiva().getDt_emissione()!=null && it.cnr.jada.util.DateUtils.daysBetweenDates(new Date(rigaNdC.getRiga_fattura_associata().getFattura_attiva().getDt_emissione().getTime()),new Date(rigaNdC.getFattura_attiva().getDt_registrazione().getTime()))<366))
						   notaDiCredito.setIvaRecuperabile(true);
					else
						   notaDiCredito.setIvaRecuperabile(false);				    
				} else {
					elementsToDischarged.add(dettaglio);
				}
			}
			if (elementsToDischarged != null && !elementsToDischarged.isEmpty()) {
				String msg = null;
				Fattura_attiva_rigaIBulk dettaglio = null;
				if (elementsToDischarged.size() == 1) {
					dettaglio = (Fattura_attiva_rigaIBulk)elementsToDischarged.firstElement();
					msg = "Il dettaglio per \"" + ((dettaglio.getDs_riga_fattura() == null)?"":dettaglio.getDs_riga_fattura()) + "\" è già stato inserito  o la disponibilità per le note di credito è 0!";
				} else {
					msg = "I dettagli per ";
					for (java.util.Iterator i = elementsToDischarged.iterator(); i.hasNext();) {
						dettaglio = (Fattura_attiva_rigaIBulk)i.next();
						msg += "\"" + ((dettaglio.getDs_riga_fattura() == null)?"":dettaglio.getDs_riga_fattura()) + "\"";
						if (!(elementsToDischarged.indexOf(dettaglio) == elementsToDischarged.size()-1))
							msg += ", ";
					}
					msg += " sono già stati inseriti o la disponibilità per le note di credito è 0!";
				}
				bp.setMessage(msg);
			}

		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di selezione dal controller "righe" preparando il selezionatore (caso di ricerca di dettagli
 * di fatture diverse e compatibili da quella di origine)
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
 
public Forward doSelezionaRighe(ActionContext context) {

	try {
		it.cnr.jada.action.HookForward caller = (it.cnr.jada.action.HookForward)context.getCaller();
		Fattura_attiva_IBulk fatturaAttiva = (Fattura_attiva_IBulk)caller.getParameter("focusedElement");
		if (fatturaAttiva != null)	{
			it.cnr.jada.util.RemoteIterator ri = ((FatturaAttivaSingolaComponentSession)((CRUDNotaDiCreditoAttivaBP)context.getBusinessProcess()).createComponentSession()).cercaDettagliFatturaPerNdC(context.getUserContext(), fatturaAttiva);
			SelezionatoreListaBP sbp = select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(Fattura_attiva_rigaIBulk.class), "righiSet", "doSelezionaDettaglioPerNdC");
			sbp.setMultiSelection(true);
			return sbp;
		}
	} catch(Throwable e) {
		return handleException(context,e);
	}
		return context.findDefaultForward();
}
/**
 * Storna i dettagli selezionati previo controllo della selezione
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doStornaDettagli(ActionContext context) {

	try {
		CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
		fillModel(context);
		it.cnr.jada.util.action.Selection models = bp.getDettaglio().getSelection(context);
		Forward forward = context.findDefaultForward();
		if (models == null || models.isEmpty())
			bp.setErrorMessage("Per procedere, selezionare i dettagli da stornare!");
		else {
			Nota_di_credito_attivaBulk ndc = (Nota_di_credito_attivaBulk)bp.getModel();
			controllaSelezione(context, models.iterator(ndc.getFattura_attiva_dettColl()));
		
			forward = basicDoStornaDettagli(context, models);

			bp.getDettaglio().reset(context);
			bp.getAccertamentiController().setModelIndex(context,-1);

			if (ndc.getObbligazioniHash() != null && !ndc.getObbligazioniHash().isEmpty() &&
				!"tabFatturaAttivaObbligazioni".equals(bp.getTab("tab"))) {
					bp.getObbligazioniController().setModelIndex(context,-1);
					bp.setTab("tab", "tabFatturaAttivaObbligazioni");
			} else if (!"tabFatturaAttivaAccertamenti".equals(bp.getTab("tab"))) {
				bp.setTab("tab", "tabFatturaAttivaAccertamenti");
			}
			bp.setDirty(true);
		}
		return forward;
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Restituisce l'importo che deve assumere la scadenza dell'accertamento nel caso di modifica automatica
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @param scadenza	
 * @param fatturaPassiva	
 * @param delta	
 * @return 
 */
protected java.math.BigDecimal getImportoPerAggiornamentoScadenzaInAutomatico(
	ActionContext context,
	Accertamento_scadenzarioBulk scadenza,
	Fattura_attivaBulk fatturaAttiva,
	java.math.BigDecimal delta) {

	return scadenza.getIm_scadenza().add(delta);
}
/**
 * Restituisce l'importo che deve assumere la scadenza dell'obbligazione nel caso di modifica automatica
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @param scadenza	
 * @param fatturaPassiva	
 * @param delta	
 * @return 
 */
protected java.math.BigDecimal getImportoPerAggiornamentoScadenzaInAutomatico(
	ActionContext context,
	Obbligazione_scadenzarioBulk scadenza,
	Fattura_attivaBulk fatturaAttiva,
	java.math.BigDecimal delta) {

	return scadenza.getIm_scadenza().subtract(delta);
}
/**
 * Risincronizza la collezione delle obbligazioni (richiamato dopo la modifica di
 * una scadenza associata al doc amm).
 * Se questa collezione contiene in chiave la oldScadenza (scadenza vecchia), essa
 * viene eliminata e i dettagli ad essa associati vengono ricontabilizzati sulla
 * newScadenza (scadenza modificata dall'utente); se non ha ancora dettagli associati
 * viene semplicemente eliminata 
 * ('scollegaDettagliDaObbligazione')
 */
private void resyncObbligazioneScadenzario(
	ActionContext context,
	Obbligazione_scadenzarioBulk oldScadenza, 
	Obbligazione_scadenzarioBulk newScadenza) 
	throws it.cnr.jada.comp.ComponentException {

	CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)getBusinessProcess(context);
	Nota_di_credito_attivaBulk notaDiCredito = (Nota_di_credito_attivaBulk)bp.getModel();
	java.util.Vector clone = new java.util.Vector();
	if (oldScadenza == null) {
		java.util.Vector models = (java.util.Vector)((HookForward)context.getCaller()).getParameter("dettagliDaStornare");
		clone = (java.util.Vector)models.clone();
	} else {
		java.util.Vector models = (java.util.Vector)notaDiCredito.getObbligazioni_scadenzarioHash().get(oldScadenza);
		clone = (java.util.Vector)models.clone();
		if (!clone.isEmpty())
			scollegaDettagliDaObbligazione(context, clone);
		else
			notaDiCredito.getObbligazioni_scadenzarioHash().remove(oldScadenza);
		oldScadenza.setIm_associato_doc_amm(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
		newScadenza.setIm_associato_doc_amm(newScadenza.getIm_scadenza());
	}

	java.util.Hashtable relationsHash = new java.util.Hashtable();
	for (java.util.Iterator i = clone.iterator(); i.hasNext();)
		relationsHash.put(i.next(),newScadenza);

	notaDiCredito = basicDoStorna(
								context,
								notaDiCredito, 
								clone,
								relationsHash);

	try {
		notaDiCredito.setCd_cds(newScadenza.getObbligazione().getCd_cds());
		notaDiCredito.setCd_unita_organizzativa(newScadenza.getObbligazione().getCd_unita_organizzativa());
		if (notaDiCredito.getModalita_pagamento() == null) {
			FatturaAttivaSingolaComponentSession h = (FatturaAttivaSingolaComponentSession)bp.createComponentSession();
			notaDiCredito = h.completaCliente(context.getUserContext(), notaDiCredito);
		}
		bp.setModel(context,notaDiCredito);
	} catch (java.rmi.RemoteException e) {
		throw new it.cnr.jada.comp.ComponentException(e);
	} catch (it.cnr.jada.action.BusinessProcessException e) {
		throw new it.cnr.jada.comp.ComponentException(e);
	}

	bp.getObbligazioniController().setModelIndex(context,-1);
	bp.setDirty(true);

	if (!"tabFatturaAttivaObbligazioni".equals(bp.getTab("tab")))
		bp.setTab("tab", "tabFatturaAttivaObbligazioni");
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
		for (java.util.Iterator i = models.iterator(); i.hasNext();) {
			Nota_di_credito_attiva_rigaBulk dettaglio = (Nota_di_credito_attiva_rigaBulk)i.next();
			try {
				if (!dettaglio.STATO_CONTABILIZZATO.equals(dettaglio.getStato_cofi()))
					throw new it.cnr.jada.comp.ApplicationException("Non è possibile scollegare il dettaglio \"" + dettaglio.getDs_riga_fattura() + "\". Questa operazione è permessa solo per dettagli in stato \"" + dettaglio.STATO.get(dettaglio.STATO_CONTABILIZZATO) + "\".");
			} catch (it.cnr.jada.comp.ApplicationException e) {
				try {
					CRUDVirtualAccertamentoBP.rollbackToSafePoint(context);
				} catch (Throwable t) {
					throw new it.cnr.jada.comp.ComponentException(t);
				}
				throw e;
			}

			dettaglio.getFattura_attiva().removeFromFattura_attiva_accertamentiHash(dettaglio);
			dettaglio.setAccertamento_scadenzario(null);

			dettaglio.setStato_cofi(dettaglio.STATO_INIZIALE);

			//dettaglio.setRiga_fattura_associata(null);
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
		for (java.util.Iterator i = models.iterator(); i.hasNext();) {
			Nota_di_credito_attiva_rigaBulk dettaglio = (Nota_di_credito_attiva_rigaBulk)i.next();
			try {
				if (!dettaglio.STATO_CONTABILIZZATO.equals(dettaglio.getStato_cofi()))
					throw new it.cnr.jada.comp.ApplicationException("Non è possibile scollegare il dettaglio \"" + dettaglio.getDs_riga_fattura() + "\". Questa operazione è permessa solo per dettagli in stato \"" + dettaglio.STATO.get(dettaglio.STATO_CONTABILIZZATO) + "\".");
			} catch (it.cnr.jada.comp.ApplicationException e) {
				try {
					CRUDVirtualObbligazioneBP.rollbackToSafePoint(context);
				} catch (Throwable t) {
					throw new it.cnr.jada.comp.ComponentException(t);
				}
				throw e;
			}
			((Nota_di_credito_attivaBulk)dettaglio.getFattura_attiva()).removeFromObbligazioni_scadenzarioHash(dettaglio);
			dettaglio.setStato_cofi(dettaglio.STATO_INIZIALE);
			dettaglio.setObbligazione_scadenzario(null);
			dettaglio.setToBeUpdated();
		}
	}
}
/**
 * Verifica o imposta lo stato della fattura
 */ 
protected void setAndVerifyStatusFor(ActionContext context, Fattura_attivaBulk fatturaAttiva) {

	Nota_di_credito_attivaBulk notaDiCredito = (Nota_di_credito_attivaBulk)fatturaAttiva;
	if (notaDiCredito.getStato_cofi() != notaDiCredito.STATO_PAGATO) {
		notaDiCredito.setStato_cofi(
			((notaDiCredito.getFattura_attiva_accertamentiHash() == null || notaDiCredito.getFattura_attiva_accertamentiHash().isEmpty()) &&
			 (notaDiCredito.getObbligazioniHash() == null || notaDiCredito.getObbligazioniHash().isEmpty())) ?
								notaDiCredito.STATO_INIZIALE : 
								notaDiCredito.STATO_PARZIALE);
	}
}
private Forward basicDoRicercaObbligazione(
		ActionContext context, 
		Nota_di_credito_attivaBulk nc_attiva,
		java.util.List models) {

		try {
			 //controlla che gli importi dei dettagli siano diversi da 0
			Nota_di_credito_attiva_rigaBulk riga = null;
	        if (models != null)
		        for (java.util.Iterator i =
		            models.iterator();
		            i.hasNext();
		            ) {
		            riga = (Nota_di_credito_attiva_rigaBulk) i.next();
		            if (riga.getIm_totale_divisa().compareTo(new java.math.BigDecimal(0))==0)
		                throw new it.cnr.jada.comp.ApplicationException("Non è possibile contabilizzare poiché un dettaglio\nselezionato ha un importo pari a 0");
}
			Filtro_ricerca_obbligazioniVBulk filtro = new Filtro_ricerca_obbligazioniVBulk();
			filtro.setData_scadenziario(nc_attiva.getDt_scadenza());
			filtro.setFornitore(nc_attiva.getCliente());
			filtro.setIm_importo(calcolaTotaleSelezionati(models,nc_attiva.quadraturaInDeroga()));
			filtro.setCd_unita_organizzativa(nc_attiva.getCd_uo_origine());
			filtro.setHasDocumentoCompetenzaCOGEInAnnoPrecedente(nc_attiva.hasCompetenzaCOGEInAnnoPrecedente());
	        filtro.setHasDocumentoCompetenzaCOGESoloInAnnoCorrente(
		        !nc_attiva.hasCompetenzaCOGEInAnnoPrecedente() &&
		        nc_attiva.getDateCalendar(nc_attiva.getDt_a_competenza_coge()).get(java.util.Calendar.YEAR) == nc_attiva.getEsercizio().intValue());
			if (filtro.getData_scadenziario() == null)
				filtro.setFl_data_scadenziario(Boolean.FALSE);		
			if (models == null || models.isEmpty())
				filtro.setFl_importo(Boolean.FALSE);
						BulkBP robp = (BulkBP)context.getUserInfo().createBusinessProcess(context,"RicercaObbligazioniBP", new Object[] { "MRSWTh" });
			robp.setModel(context,filtro);
			
			context.addHookForward("bringback",this,"doBringBackOpenObbligazioniWindow");
			HookForward hook = (HookForward)context.findForward("bringback");
			hook.addParameter("dettagliDaStornare", models);
			return context.addBusinessProcess(robp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
}
