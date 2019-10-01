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

import java.util.List;

import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.comp.DocumentoAmministrativoComponentSession;
import it.cnr.contab.docamm00.bp.TitoloDiCreditoDebitoBP;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.bp.RisultatoEliminazioneBP;
import it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.docamm00.bp.CRUDNotaDiDebitoBP;
import it.cnr.contab.docamm00.bp.CRUDFatturaPassivaIBP;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_rigaBulk;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP;
import it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * Insert the type's description here.
 * Creation date: (10/30/2001 3:11:45 PM)
 * @author: Roberto Peli
 */
public class CRUDNotaDiDebitoAction extends CRUDFatturaPassivaAction {
/**
 * CRUDNotaDiCreditoAction constructor comment.
 */
public CRUDNotaDiDebitoAction() {
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

	Nota_di_debito_rigaBulk rigaND = (Nota_di_debito_rigaBulk)riga;
	if (rigaND.getQuantita() == null) rigaND.setQuantita(new java.math.BigDecimal(1));
	if (rigaND.getPrezzo_unitario() == null) rigaND.setPrezzo_unitario(new java.math.BigDecimal(0));
	if (rigaND.getIm_iva() == null) rigaND.setIm_iva(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));

	rigaND.calcolaCampiDiRiga();
	java.math.BigDecimal totaleDiRiga = rigaND.getIm_imponibile().add(rigaND.getIm_iva());
	Fattura_passiva_rigaIBulk rigaFP = rigaND.getRiga_fattura_origine();
	java.math.BigDecimal nuovoImportoDisponibile = rigaFP.getIm_diponibile_nc().add(totaleDiRiga.subtract(vecchioTotale));
	if (nuovoImportoDisponibile.signum() < 0)
		throw new it.cnr.jada.bulk.FillException("Attenzione: l'importo di storno massimo ancora disponibile è di " + rigaFP.getIm_diponibile_nc() + " EUR!");
	rigaFP.setIm_diponibile_nc(nuovoImportoDisponibile.setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
}
/**
 * Richiama sulla component il metodo per addebitare i dettagli selezionati e ritorna la nota di debito aggiornata
 */
 
private Nota_di_debitoBulk basicDoAddebita(
	ActionContext context,
	Nota_di_debitoBulk notaDiDebito, 
	java.util.List dettagliDaAddebitare,
	java.util.Hashtable relationsHash)
	throws it.cnr.jada.comp.ComponentException {

	CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP)context.getBusinessProcess();

	try {
		FatturaPassivaComponentSession h = (FatturaPassivaComponentSession)bp.createComponentSession();
		notaDiDebito = h.addebitaDettagli(
									context.getUserContext(), 
									notaDiDebito, 
									dettagliDaAddebitare,
									relationsHash);
	} catch (java.rmi.RemoteException e) {
		bp.handleException(e);
	} catch (it.cnr.jada.action.BusinessProcessException e) {
		bp.handleException(e);
	}
	return notaDiDebito;
}
/**
 * Gestisce la richiesta di addebito dei dettagli selezionati (contabilizzazione)
 * Richiesta la quadratura dei conti
 */
private Forward basicDoAddebitaDettagli(
	ActionContext context, 
	it.cnr.jada.util.action.Selection selection) {

	try {
		CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP)getBusinessProcess(context);
		Nota_di_debitoBulk notaDiDebito = (Nota_di_debitoBulk)bp.getModel();
		controllaQuadraturaConti(context, notaDiDebito);

		Forward forward = context.findDefaultForward();
		java.util.Vector dettagliDaAddebitare = (java.util.Vector)getDettagliInStato(
													context, 
													selection.iterator(notaDiDebito.getFattura_passiva_dettColl()), 
													new String[] { Nota_di_debito_rigaBulk.STATO_CONTABILIZZATO });

		if (dettagliDaAddebitare != null && !dettagliDaAddebitare.isEmpty()) {
			notaDiDebito = basicDoAddebita(
									context, 
									notaDiDebito,
									dettagliDaAddebitare,
									null);
			bp.setModel(context,notaDiDebito);
		}

		dettagliDaAddebitare = (java.util.Vector)getDettagliInStato(
										context, 
										selection.iterator(notaDiDebito.getFattura_passiva_dettColl()), 
										new String[] { Nota_di_debitoBulk.STATO_PARZIALE, Nota_di_debitoBulk.STATO_PAGATO });

		if (dettagliDaAddebitare != null && !dettagliDaAddebitare.isEmpty()) {
			it.cnr.jada.util.RemoteIterator ri = ((FatturaPassivaComponentSession)bp.createComponentSession()).findObbligazioniFor(
																			context.getUserContext(), 
																			notaDiDebito, 
																			calcolaTotaleSelezionati(dettagliDaAddebitare, notaDiDebito.quadraturaInDeroga()));
			ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
			if (ri != null && ri.hasMoreElements()) {
				it.cnr.jada.util.action.SelezionatoreListaBP slbp = select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(Fattura_passiva_rigaIBulk.class), "default", "doSelezionaDettagli");
				HookForward hook = (HookForward)context.findForward("seleziona");
				hook.addParameter("dettagliDaAddebitare", dettagliDaAddebitare);
				forward = slbp;
			} else {
				throw new it.cnr.jada.comp.ApplicationException("Non sono disponibili scadenze di obbligazioni di altre fatture dello stesso fornitore per i dettagli già pagati!");
			}
		}
		return forward;
	} catch(Throwable e) {
		return handleException(context,e);
	}
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

	Nota_di_debito_rigaBulk rigaND = (Nota_di_debito_rigaBulk)riga;
	fillModel( context );

	if (rigaND.getQuantita() == null) rigaND.setQuantita(new java.math.BigDecimal(1));
	if (rigaND.getPrezzo_unitario() == null) rigaND.setPrezzo_unitario(new java.math.BigDecimal(0));
	if (rigaND.getIm_iva() == null) rigaND.setIm_iva(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));

	rigaND.setFl_iva_forzata(Boolean.FALSE);
	rigaND.calcolaCampiDiRiga();
	java.math.BigDecimal totaleDiRiga = rigaND.getIm_imponibile().add(rigaND.getIm_iva());
	Fattura_passiva_rigaIBulk rigaFP = rigaND.getRiga_fattura_origine();
	java.math.BigDecimal nuovoImportoDisponibile = rigaFP.getIm_diponibile_nc().add(totaleDiRiga.subtract(vecchioTotale));
	if (nuovoImportoDisponibile.signum() < 0)
		throw new it.cnr.jada.bulk.FillException("Attenzione: l'importo di storno massimo ancora disponibile è di " + rigaFP.getIm_diponibile_nc() + " EUR!");
	rigaFP.setIm_diponibile_nc(nuovoImportoDisponibile.setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	doSelectObbligazioni(context);
}
/**
 * Costruiscce le relazioni (per gestire l'addebito) tra la riga della nota di debito e la riga della fattura
 * passiva selezionata (potrebbe essere quella di un'altra fattura, non necessariamente quella di origine) da
 * cui ottenere l'obbligazione su cui addebitare
 */
private Forward buildRelations(
	ActionContext context, 
	HookForward hook,
	java.util.List dettagliDaAddebitare,
	java.util.Enumeration details,
	java.util.Hashtable relationsHash) {

// DA FARE

		
	try {
		CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP)getBusinessProcess(context);
		Nota_di_debitoBulk notaDiDebito = (Nota_di_debitoBulk)bp.getModel();

		Forward fwd = null;
		if (details.hasMoreElements()) {
			Nota_di_debito_rigaBulk rigaNdD = (Nota_di_debito_rigaBulk)details.nextElement();
			if (relationsHash == null)
				relationsHash = new java.util.Hashtable();
			Fattura_passiva_rigaIBulk selectedRigaFattura = null;
			if (selectedRigaFattura != null) {
				relationsHash.put(rigaNdD, selectedRigaFattura);

			} else {
				dettagliDaAddebitare.remove(rigaNdD);
			}
			
			fwd = buildRelations(
							context,
							hook,
							dettagliDaAddebitare,
							details,
							relationsHash);
		} else {

			notaDiDebito = basicDoAddebita(
									context, 
									notaDiDebito,
									dettagliDaAddebitare,
									relationsHash);
			bp.setModel(context,notaDiDebito);
			fwd = context.findDefaultForward();
		}
		return fwd;
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Addebita i dettagli selezionati previo controllo della selezione
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
 
public Forward doAddebitaDettagli(ActionContext context) {

	try {
		CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP)getBusinessProcess(context);
		fillModel(context);
		it.cnr.jada.util.action.Selection models = bp.getDettaglio().getSelection(context);
		Forward forward = context.findDefaultForward();
		if (models == null || models.isEmpty())
			bp.setErrorMessage("Per procedere, selezionare i dettagli da addebitare!");
		else {
			controllaSelezionePerContabilizzazione(context, models.iterator(((Nota_di_debitoBulk)bp.getModel()).getFattura_passiva_dettColl()));
			//controllaSelezionePerTitoloCapitolo(context, models.iterator(((Nota_di_debitoBulk)bp.getModel()).getFattura_passiva_dettColl()));
			List titoloCapitoloValidolist = controllaSelezionePerTitoloCapitoloLista(context, models.iterator(((Nota_di_debitoBulk)bp.getModel()).getFattura_passiva_dettColl()));

			forward = basicDoAddebitaDettagli(context, models);

			bp.getDettaglio().reset(context);
			bp.getObbligazioniController().setModelIndex(context,-1);

			bp.setDirty(true);
			if (!"tabFatturaPassivaObbligazioni".equals(bp.getTab("tab")))
				bp.setTab("tab", "tabFatturaPassivaObbligazioni");
		}
		return forward;
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
	    CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP)getBusinessProcess(context);
	    bp.getDettaglio().getSelection().clearSelection();
	    fillModel(context);
	    it.cnr.jada.util.RemoteIterator ri = ((FatturaPassivaComponentSession)bp.createComponentSession()).cercaFatturaPerNdD(context.getUserContext(), (Nota_di_debitoBulk)bp.getModel());
	    return select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(Fattura_passiva_IBulk.class), "default", "doSelezionaRighe");
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
		CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP)getBusinessProcess(context);
		bp.getDettaglio().getSelection().clearSelection();
		fillModel(context);

		java.util.Vector selectedModels = new java.util.Vector();
		for (java.util.Enumeration e = bp.getDettaglio().getElements(); e.hasMoreElements();) {
			Nota_di_debito_rigaBulk riga = (Nota_di_debito_rigaBulk)e.nextElement();
			if (riga.STATO_INIZIALE.equals(riga.getStato_cofi()))
				selectedModels.add(riga);
		}
		if (selectedModels.isEmpty())
			throw new it.cnr.jada.comp.ApplicationException("Tutti i dettagli sono già stati addebitati!");
		it.cnr.jada.util.action.SelezionatoreListaBP slbp = (it.cnr.jada.util.action.SelezionatoreListaBP) select(
					context,
					new it.cnr.jada.util.ListRemoteIterator(selectedModels),
					it.cnr.jada.bulk.BulkInfo.getBulkInfo(Nota_di_debito_rigaBulk.class),
					"righeNdDSet",
					"doBringBackAddToCRUDMain_Obbligazioni_DettaglioObbligazioni");
		slbp.setMultiSelection(true);
		return slbp;
	} catch(Throwable e) {
		return handleException(context,e);
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
            CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP) getBusinessProcess(context);
            java.util.List dett = ((Nota_di_debitoBulk)bp.getModel()).getFattura_passiva_dettColl();
            Selection newSelection = getIndexSelectionOn(selection, dett, "addebitato");
            forward = basicDoAddebitaDettagli(context, newSelection);

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

	CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP)getBusinessProcess(context);
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
					bp.setModel(context, (Nota_di_debitoBulk)caller.getParameter("originalClone"));
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
		CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP)getBusinessProcess(context);
		if (bp.getParent() != null && bp.getParent() instanceof CRUDFatturaPassivaIBP) {
			fillModel(context);
			Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk)((CRUDFatturaPassivaIBP)bp.getParent()).getModel();
			FatturaPassivaComponentSession h = (FatturaPassivaComponentSession)bp.createComponentSession();
			it.cnr.jada.util.RemoteIterator ri = h.findNotaDiDebitoFor(context.getUserContext(), fp);
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
 * Richiama il metodo di gestione della chiusura del pannello ndd
 */
public Forward doConfirmCloseForm(ActionContext context, int option) throws it.cnr.jada.action.BusinessProcessException {

	if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
		Forward defaultForward = super.doConfirmCloseForm(context, option);
		Forward forward = context.findForward("chiusuraNotaDiDebito");
		if (forward == null)
			return defaultForward;
		return forward;
	}
	return super.doConfirmCloseForm(context, option);
}
/**
 * Richiede all'obbligazione di modificare in automatico la sua scadenza (quella
 * selezionata) portando la stessa ad importo pari alla sommatoria degli importi 
 * di riga dei dettagli associati. Aggiorna la mappa dei saldi per le variazioni
 * subite dall'obbligazione
 *
 * @param context	L'ActionContext della richiesta
 * @param prefix	
 * @return Il Forward alla pagina di risposta
 */
public Forward doModificaScadenzaInAutomatico(ActionContext context, String prefix) {

	try {
		CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP)getBusinessProcess(context);
		fillModel(context);

		Nota_di_debitoBulk notaDiDebito = (Nota_di_debitoBulk)bp.getModel();
		
		controllaQuadraturaConti(context, notaDiDebito);

		Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)bp.getObbligazioniController().getModel();

		if (scadenza == null)
			throw new it.cnr.jada.comp.ApplicationException("Selezionare l'impegno da modificare in automatico!");
        //controllo che la scadenza appartenga all'esercizio di scrivania nella fase di cancellazione
		if (bp.isDeleting() &&
			!bp.isViewing() &&
			!it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).equals(scadenza.getEsercizio()))
			throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata appartiene all'esercizio " + scadenza.getEsercizio().intValue() + "! Operazione annullata.");

		ObbligazioneAbstractComponentSession h = CRUDVirtualObbligazioneBP.getVirtualComponentSession(context, true);

		java.math.BigDecimal importoAttuale = notaDiDebito.getImportoTotalePerObbligazione();
		java.math.BigDecimal importoOriginale = (java.math.BigDecimal)notaDiDebito.getFattura_passiva_ass_totaliMap().get(scadenza);
		java.math.BigDecimal delta = importoAttuale.subtract(importoOriginale);
		if (new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP).compareTo(delta) == 0)
			throw new it.cnr.jada.comp.ApplicationException("La modifica in automatico non è disponibile!");
		try {
			scadenza = (Obbligazione_scadenzarioBulk)h.modificaScadenzaInAutomatico(
														context.getUserContext(), 
														scadenza, 
														getImportoPerAggiornamentoScadenzaInAutomatico(
																							context,
																							scadenza,
																							notaDiDebito,
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

		notaDiDebito.addToFattura_passiva_ass_totaliMap(scadenza, importoAttuale);

		bp.getObbligazioniController().getSelection().clear();
		bp.getObbligazioniController().setModelIndex(context, -1);
		bp.getObbligazioniController().setModelIndex(context,it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(bp.getObbligazioniController().getDetails(), scadenza));
		bp.setDirty(true);
		if (bp instanceof TitoloDiCreditoDebitoBP)
			((TitoloDiCreditoDebitoBP)bp).addToDocumentiContabiliModificati(scadenza);
	} catch(Exception e) {
		return handleException(context,e);
	}
	return context.findDefaultForward();
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
		CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP)getBusinessProcess(context);
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

		controllaQuadraturaConti(context, (Nota_di_debitoBulk)bp.getModel());

		if (!viewMode && bp instanceof IDocumentoAmministrativoBP)
				viewMode = !((IDocumentoAmministrativoBP)bp).getDocumentoAmministrativoCorrente().isEditable();
		String status = viewMode ?"V":"M";
		it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP nbp = it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP.getBusinessProcessFor(context, scadenza.getObbligazione(), status + "RSWTh");
		nbp.edit(context,scadenza.getObbligazione(), true);
		nbp.selezionaScadenza(scadenza, context);

		context.addHookForward("bringback",this,"doBringBackOpenObbligazioniWindow");
		HookForward hook = (HookForward)context.findForward("bringback");
		return context.addBusinessProcess(nbp);
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Risponde all'evento di fine cancellazione dei documentio amministrativi di tipo
 * passivo. Reimplementato
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doPostConfirmDelete(
	ActionContext context, 
	Risultato_eliminazioneVBulk re) 
	throws it.cnr.jada.action.BusinessProcessException {

	it.cnr.jada.util.action.CRUDBP bp = getBusinessProcess(context);
	Fattura_passivaBulk deletedDoc = (Fattura_passivaBulk)bp.getModel();
	doConfirmCloseForm(context, it.cnr.jada.util.action.OptionBP.YES_BUTTON);
	bp = getBusinessProcess(context);
	String msg = "Cancellazione effettuata!";
	if (deletedDoc.isVoidable())
		msg = "Annullamento effettuato!";
	if (!re.getDocumentiContabiliScollegati().isEmpty())
		msg = msg + " Alcuni documenti contabili creati contestualmente al documento amministrativo sono rimasti inalterati.";
	bp.setMessage(msg);
	return doChiusuraNotaDiDebito(context);
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
	CRUDNotaDiDebitoBP bp,
	Nota_di_debitoBulk originalClone) 
	throws it.cnr.jada.action.BusinessProcessException, it.cnr.jada.bulk.ValidationException {

	Nota_di_debitoBulk ndd = (Nota_di_debitoBulk)bp.getModel();
	
	RisultatoEliminazioneBP rebp = (RisultatoEliminazioneBP)context.createBusinessProcess("RisultatoEliminazioneBP", new String[] { "MRSWTh" });
	Risultato_eliminazioneVBulk deleteManager = bp.getDeleteManager();

	if(ndd.getDettagliCancellati() != null)
		for (java.util.Iterator i = ndd.getDettagliCancellati().iterator(); i.hasNext();) {
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

	if (deleteManager != null && 
		(!deleteManager.getDocumentiAmministrativiScollegati().isEmpty() || !deleteManager.getDocumentiContabiliScollegati().isEmpty())) {
		rebp.initializeControllers(context, ndd);
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
 * Gestisce una richiesta di cancellazione dal controller "obbligazioni"
 * Reimplementato
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */

public Forward doRemoveFromCRUDMain_Obbligazioni(ActionContext context) {
	
	CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP)context.getBusinessProcess();
	it.cnr.jada.util.action.Selection selection = bp.getObbligazioniController().getSelection();
	try {
		if (selection.isEmpty())
			throw new it.cnr.jada.comp.ApplicationException("Selezionare le scadenze che si desidera eliminare!");
	} catch (it.cnr.jada.comp.ApplicationException e) {
		return handleException(context, e);
	}
	java.util.List obbligazioni = bp.getObbligazioniController().getDetails();
	for (it.cnr.jada.util.action.SelectionIterator i = selection.iterator();i.hasNext();) {
		Obbligazione_scadenzarioBulk obbligazione = (Obbligazione_scadenzarioBulk)obbligazioni.get(i.nextIndex());
		if (bp.isDocumentoContabileModificato(obbligazione))
			return handleException(
							context, 
							new it.cnr.jada.comp.ApplicationException("La scadenza \"" + obbligazione.getDs_scadenza() + "\" è stata già modificata. Impossibile cancellarla."));
		Nota_di_debitoBulk ndD = (Nota_di_debitoBulk)bp.getModel();
		java.util.Vector models = (java.util.Vector)ndD.getFattura_passiva_obbligazioniHash().get(obbligazione);
		try {
			if (models != null && models.isEmpty()) {
				ndD.getFattura_passiva_obbligazioniHash().remove(obbligazione);
				ndD.addToDocumentiContabiliCancellati(obbligazione);
			} else {
				for (java.util.Iterator it = models.iterator(); it.hasNext();) {
					Fattura_passiva_rigaBulk fpr = (Fattura_passiva_rigaBulk)it.next();
					if (fpr.getTi_associato_manrev() != null && fpr.ASSOCIATO_A_MANDATO.equalsIgnoreCase(fpr.getTi_associato_manrev()))
						throw new it.cnr.jada.comp.ApplicationException("Impossibile scollegare l'impegno \"" + 
														+ obbligazione.getEsercizio_originale().intValue()
														+ "/" + obbligazione.getPg_obbligazione().longValue() + 
														"\" perchè il dettaglio collegato \"" + 
														((fpr.getDs_riga_fattura() != null) ?
															fpr.getDs_riga_fattura() :
															String.valueOf(fpr.getProgressivo_riga().longValue())) + 
														"\" è associato a mandato.");
				}
				scollegaDettagliDaObbligazione(context, (java.util.List)models.clone());
			}
		} catch (it.cnr.jada.comp.ComponentException e) {
			return handleException(context, e);
		}

		setAndVerifyStatusFor(context, ndD);
		
		bp.getObbligazioniController().getSelection().clear();
		bp.getObbligazioniController().setModelIndex(context,-1);

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
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doRemoveFromCRUDMain_Obbligazioni_DettaglioObbligazioni(ActionContext context) {

	CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP)context.getBusinessProcess();
	try {
		it.cnr.jada.util.action.Selection selection = bp.getDettaglioObbligazioneController().getSelection();
		if (selection.isEmpty())
			throw new it.cnr.jada.comp.ApplicationException("Selezionare i dettagli che si desidera scollegare!");
		java.util.List models = selection.select(bp.getDettaglioObbligazioneController().getDetails());
		if (models != null)
			for (java.util.Iterator i = models.iterator(); i.hasNext();) {
				Nota_di_debito_rigaBulk dettaglio = (Nota_di_debito_rigaBulk)i.next();
				Obbligazione_scadenzarioBulk obbligazioneSelezionata = dettaglio.getObbligazione_scadenziario();
				if (bp.isDocumentoContabileModificato(obbligazioneSelezionata))
					throw new it.cnr.jada.comp.ApplicationException("La scadenza \"" + obbligazioneSelezionata.getDs_scadenza() + "\" è stata già modificata. Impossibile cancellarla.");
				if (dettaglio.getTi_associato_manrev() != null && dettaglio.ASSOCIATO_A_MANDATO.equalsIgnoreCase(dettaglio.getTi_associato_manrev()))
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

	doCalcolaTotalePerObbligazione(context, (Obbligazione_scadenzarioBulk)bp.getObbligazioniController().getModel());

	Nota_di_debitoBulk ndD = (Nota_di_debitoBulk)bp.getModel();

	setAndVerifyStatusFor(context, ndD);

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

	CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP)getBusinessProcess(context);
	Nota_di_debitoBulk originalClone = (Nota_di_debitoBulk)bp.getModel();
	try {
		fillModel(context);
	
		bp.setSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
		bp.salvaRiportandoAvanti(context);

		Nota_di_debitoBulk ndd = (Nota_di_debitoBulk)bp.getModel();
		if (bp.isEditing() &&
			((ndd.getDettagliCancellati() != null && !ndd.getDettagliCancellati().isEmpty()) ||
			(ndd.getDocumentiContabiliCancellati() != null && !ndd.getDocumentiContabiliCancellati().isEmpty())))
			return doPostSaveEvent(context, bp, originalClone);

		if (bp.isCarryingThrough())
			bp.riportaAvanti(context);
		bp.commitUserTransaction();
		bp.setModel(context,bp.initializeModelForEdit(context,bp.getModel()));
		bp.setStatus(bp.EDIT);
		bp.setCarryingThrough(false);

		bp.getObbligazioniController().setModelIndex(context, -1);
		
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
 * Gestisce una richiesta di salvataggio. Rimplementato
 */
public Forward doSalva(ActionContext context) throws java.rmi.RemoteException {

	CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP)getBusinessProcess(context);
	Nota_di_debitoBulk originalClone = (Nota_di_debitoBulk)bp.getModel();
	try {
		fillModel(context);

		bp.setSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
		bp.save(context);

		Nota_di_debitoBulk ndd = (Nota_di_debitoBulk)bp.getModel();
		if (bp.isEditing() &&
			((ndd.getDettagliCancellati() != null && !ndd.getDettagliCancellati().isEmpty()) ||
			(ndd.getDocumentiContabiliCancellati() != null && !ndd.getDocumentiContabiliCancellati().isEmpty())))
			return doPostSaveEvent(context, bp, originalClone);

		bp.commitUserTransaction();
		bp.setCarryingThrough(false);
		bp.setModel(context,bp.initializeModelForEdit(context,bp.getModel()));

		bp.getObbligazioniController().setModelIndex(context, -1);
		
		bp.setStatus(bp.EDIT);
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
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di storno dei dettagli selezionati scelti da una fattura passiva divera da quella di origine
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */

public Forward doSelezionaDettagli(ActionContext context) {

	CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP)context.getBusinessProcess();
	HookForward caller = (HookForward)context.getCaller();
	Fattura_passiva_rigaIBulk riga = (Fattura_passiva_rigaIBulk)caller.getParameter("focusedElement");
	if (riga != null) {
		java.util.Hashtable relationsHash = new java.util.Hashtable();
		java.util.List dettagliDaAddebitare = (java.util.List)caller.getParameter("dettagliDaAddebitare");
		if (dettagliDaAddebitare != null) {
			for (java.util.Iterator i = dettagliDaAddebitare.iterator(); i.hasNext();)
				relationsHash.put(i.next(),riga);
			if (!relationsHash.isEmpty()) {
				Nota_di_debitoBulk notaDiDebito = (Nota_di_debitoBulk)bp.getModel();
				try {
					notaDiDebito = basicDoAddebita(
										context, 
										notaDiDebito,
										dettagliDaAddebitare,
										relationsHash);
					bp.setModel(context,notaDiDebito);
					bp.resyncChildren(context);
				} catch (it.cnr.jada.comp.ComponentException e) {
					return handleException(context, e);
				} catch (it.cnr.jada.action.BusinessProcessException e) {
					return handleException(context, e);
				}
			}

			bp.getDettaglio().reset(context);
			bp.getObbligazioniController().setModelIndex(context,-1);

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
public Forward doSelezionaDettaglioPerNdD(ActionContext context) {

	try {
		it.cnr.jada.action.HookForward caller = (it.cnr.jada.action.HookForward)context.getCaller();
		java.util.List selectedElements = (java.util.List)caller.getParameter("selectedElements");
		if (selectedElements != null && !selectedElements.isEmpty()) {
			CRUDNotaDiDebitoBP bp = (CRUDNotaDiDebitoBP)context.getBusinessProcess();
			java.util.Vector elementsToBeAdded = new java.util.Vector();
			java.util.Vector elementsToDischarged = new java.util.Vector();
			for (java.util.Iterator els = selectedElements.iterator(); els.hasNext();) {
				Fattura_passiva_rigaIBulk dettaglio = (Fattura_passiva_rigaIBulk)els.next();
				try {
					if (((FatturaPassivaComponentSession)bp.createComponentSession()).isBeneServizioPerSconto(
																						context.getUserContext(),
																						dettaglio))
						throw new it.cnr.jada.bulk.ValidationException();
					for (java.util.Iterator i = bp.getDettaglio().getDetails().iterator(); i.hasNext();) {
						if (((Nota_di_debito_rigaBulk)i.next()).getRiga_fattura_origine().equalsByPrimaryKey(dettaglio))
							throw new it.cnr.jada.bulk.ValidationException();
					}
					elementsToBeAdded.add(dettaglio);
				} catch (it.cnr.jada.bulk.ValidationException e) {
					if (!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(elementsToDischarged, dettaglio))
						elementsToDischarged.add(dettaglio);
				}
			}
			for (java.util.Iterator i = elementsToBeAdded.iterator(); i.hasNext();) {
				Fattura_passiva_rigaIBulk dettaglio = (Fattura_passiva_rigaIBulk)i.next();
				Nota_di_debito_rigaBulk rigaNdD = new Nota_di_debito_rigaBulk();
				Nota_di_debitoBulk notaDiDebito = (Nota_di_debitoBulk)bp.getModel();
				notaDiDebito.addToFattura_passiva_dettColl(rigaNdD);
				rigaNdD.setUser(context.getUserInfo().getUserid());
				rigaNdD.copyFrom(dettaglio);
			}
			if (elementsToDischarged != null && !elementsToDischarged.isEmpty()) {
				String msg = null;
				Fattura_passiva_rigaIBulk dettaglio = null;
				if (elementsToDischarged.size() == 1) {
					dettaglio = (Fattura_passiva_rigaIBulk)elementsToDischarged.firstElement();
					msg = "Il dettaglio per \"" + dettaglio.getBene_servizio().getDs_bene_servizio() + "\" è già stato inserito\no il bene servizio inserito è un bene di tipo sconto/abbuono!";
				} else {
					msg = "I dettagli per ";
					for (java.util.Iterator i = elementsToDischarged.iterator(); i.hasNext();) {
						dettaglio = (Fattura_passiva_rigaIBulk)i.next();
						msg += "\"" + dettaglio.getBene_servizio().getDs_bene_servizio() + "\"";
						if (!(elementsToDischarged.indexOf(dettaglio) == elementsToDischarged.size()-1))
							msg += ", ";
					}
					msg += " sono già stati inseriti\no il bene servizio inserito è un bene di tipo sconto/abbuono!";
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
		Fattura_passiva_IBulk fatturaPassiva = (Fattura_passiva_IBulk)caller.getParameter("focusedElement");
		if (fatturaPassiva != null)	{
			it.cnr.jada.util.RemoteIterator ri = ((FatturaPassivaComponentSession)((CRUDNotaDiDebitoBP)context.getBusinessProcess()).createComponentSession()).cercaDettagliFatturaPerNdD(context.getUserContext(), fatturaPassiva);
			SelezionatoreListaBP sbp = select(context, ri, it.cnr.jada.bulk.BulkInfo.getBulkInfo(Fattura_passiva_rigaIBulk.class), "righiSet", "doSelezionaDettaglioPerNdD");
			sbp.setMultiSelection(true);
			return sbp;
		}
	} catch(Throwable e) {
		return handleException(context,e);
	}
	return context.findDefaultForward();
}
/**
 * Restituisce tra i dettagli passati in argomento quelli che sono in uno degli
 * stati specificati in statiDettaglio
 *
 * @param context	L'ActionContext della richiesta
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
			Nota_di_debito_rigaBulk rigaNdD = (Nota_di_debito_rigaBulk)dettagli.next();
			for (int cont = 0; cont < statiDettaglio.length; cont++) {
				String statoDettaglio = statiDettaglio[cont];
				if (statoDettaglio.equals(rigaNdD.getRiga_fattura_origine().getStato_cofi()))
					if (!coll.contains(rigaNdD))
						coll.add(rigaNdD);
			}
		}
	}
	return coll;
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
	Fattura_passivaBulk fatturaPassiva,
	java.math.BigDecimal delta) {

	return scadenza.getIm_scadenza().add(delta);
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
			Nota_di_debito_rigaBulk dettaglio = (Nota_di_debito_rigaBulk)i.next();
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

			Obbligazione_scadenzarioBulk obbligazioneSelezionata = dettaglio.getObbligazione_scadenziario();

			dettaglio.getFattura_passiva().removeFromFattura_passiva_obbligazioniHash(dettaglio);
			dettaglio.setObbligazione_scadenziario(null);

			dettaglio.setStato_cofi(dettaglio.STATO_INIZIALE);

			dettaglio.setRiga_fattura_associata(null);
			dettaglio.setToBeUpdated();
		}
	}
}
@Override
public Forward doInventariaDettagli(ActionContext context) {
	return super.doInventariaDettagliPerAumentoValore(context);
}
}
