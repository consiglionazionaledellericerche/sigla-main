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

package it.cnr.contab.fondecon00.action;

import it.cnr.contab.docamm00.docs.bulk.Voidable;
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_passivoBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaBulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.fondecon00.ejb.FondoSpesaComponentSession;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.fondecon00.bp.*;
import it.cnr.contab.fondecon00.core.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.CRUDBP;

public class FondoSpesaAction extends it.cnr.jada.util.action.CRUDAction {

	private Fondo_spesaBulk spesaSospesa;

public FondoSpesaAction() {
	super();
}
public Forward doBlankSearchCitta(
	ActionContext context,
	Fondo_spesaBulk spesa) {
		
	try{
		spesa.setCitta(new ComuneBulk());
		spesa.setCap_fornitore(null);
		spesa.setCaps_fornitore(null);
		
		return context.findDefaultForward();

	} catch(Exception e) {
		return handleException(context,e);
	}
}
public Forward doBlankSearchFornitore(
	ActionContext context,
	Fondo_spesaBulk spesa) {
		
	try{
		TerzoBulk tb = new TerzoBulk();
		tb.setAnagrafico(new AnagraficoBulk());
		spesa.setFornitore(tb);
		spesa.setDenominazione_fornitore(null);
		spesa.setCodice_fiscale(null);
		spesa.setPartita_iva(null);
		spesa.setIndirizzo_fornitore(null);
		spesa.setCitta(null);
		spesa.setCap_fornitore(null);
		spesa.setCaps_fornitore(null);
		
		return context.findDefaultForward();

	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Riport il doc amm selezionato come documentazione della spesa in questione,
 * ereditandone il fornitore, l'importo
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doBringBackCercaDocumento(it.cnr.jada.action.ActionContext context) {

	HookForward caller = (HookForward)context.getCaller();
	IDocumentoAmministrativoSpesaBulk docAmmSelected = (IDocumentoAmministrativoSpesaBulk)caller.getParameter("documentoAmministrativoSelezionato");

	try {
		if (docAmmSelected != null) {
			if (docAmmSelected instanceof Voidable && ((Voidable)docAmmSelected).isAnnullato())
				throw new it.cnr.jada.comp.ApplicationException("Il documento amministrativo selezionato è stato annullato! Operazione non consentita.");

			    // Gennaro Borriello - (02/11/2004 16.48.21)
				// 	Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato 
				//	DA UN ES. PRECEDENTE a quello di scrivania.
			if (docAmmSelected.getEsercizio().intValue() != it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue()) {
				if (!docAmmSelected.isRiportataInScrivania())
					throw new it.cnr.jada.comp.ApplicationException("Per poter selezionare un documento amministrativo con esercizio diverso da quello corrente, è necessario che questo sia stato riportato nell'esercizio corrente.");
				if (!IDocumentoAmministrativoSpesaBulk.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(docAmmSelected.getRiportataInScrivania()))
					throw new it.cnr.jada.comp.ApplicationException("Il documento amministrativo selezionato NON è stato riportato completamente! Operazione non consentita.");
			} else {			
				if (docAmmSelected.isRiportata())
					throw new it.cnr.jada.comp.ApplicationException("Il documento amministrativo selezionato è stato riportato in esercizi futuri! Operazione non consentita.");
			}
				
			FondoSpesaBP bp = (FondoSpesaBP)context.getBusinessProcess();
			Fondo_spesaBulk spesa = (Fondo_spesaBulk)bp.getModel();
			if (docAmmSelected.getImporto_spesa() == null)
				throw new it.cnr.jada.comp.ApplicationException("Impossibile ottenere l'importo per la spesa dal documento amministrativo selezionato!");
			spesa.setIm_ammontare_spesa(docAmmSelected.getImporto_spesa());
			spesa.setImportoNettoSpesa(docAmmSelected.getImporto_netto_spesa());
			TerzoBulk terzoDocAmm = docAmmSelected.getTerzo_spesa();
			if (terzoDocAmm == null)
				throw new it.cnr.jada.comp.ApplicationException("Impossibile ottenere il fornitore per la spesa dal documento amministrativo selezionato!");
			doBringBackSearchFornitore(context, spesa, terzoDocAmm);
			spesa.setDocumento(docAmmSelected);
			if (spesa.getDs_spesa() == null || spesa.getDs_spesa().equals(""))
				spesa.setDs_spesa("Spesa documentata" + ((docAmmSelected.getDescrizione_spesa() == null) ? "" : " da \"" + docAmmSelected.getDescrizione_spesa() + "\"."));
		}
	} catch (Throwable e) {
		return handleException(context, e);
	}
	return context.findDefaultForward();
}
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di ricerca del searchtool "citta"
 *
 * @param context	L'ActionContext della richiesta
 * @param spesa	L'OggettoBulk padre del searchtool
 * @param comune	L'OggettoBulk selezionato dall'utente
 * @return Il Forward alla pagina di risposta
 * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
 */
public it.cnr.jada.action.Forward doBringBackSearchCitta(it.cnr.jada.action.ActionContext context, 
										Fondo_spesaBulk spesa,
										it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune)
											throws java.rmi.RemoteException {

	FondoSpesaBP spesaBP = (FondoSpesaBP)getBusinessProcess(context);

	if (comune != null) {
		spesa.setCitta(comune);

		if( comune.getPg_comune() != null && !("".equals(comune.getPg_comune())) ) {
			try {
				getBusinessProcess(context).setModel(
					context,
					((FondoSpesaComponentSession)spesaBP.createComponentSession()).setCitta(
										context.getUserContext(), 
										spesa, 
										comune)
				);
				//getBusinessProcess(context).resyncChildren(context);
			} catch(Throwable e) {
				return handleException(context,e);
			}
		}
	}

	return context.findDefaultForward();
}
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di ricerca del searchtool "citta"
 *
 * @param context	L'ActionContext della richiesta
 * @param spesa	L'OggettoBulk padre del searchtool
 * @param comune	L'OggettoBulk selezionato dall'utente
 * @return Il Forward alla pagina di risposta
 * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
 */
public it.cnr.jada.action.Forward doBringBackSearchFornitore(
	it.cnr.jada.action.ActionContext context, 
	Fondo_spesaBulk spesa,
	TerzoBulk fornitore)
	throws java.rmi.RemoteException {

	if (fornitore != null) {
		spesa.setFornitore(fornitore);
		spesa.setDenominazione_fornitore(fornitore.getDenominazione_sede());
		spesa.setCodice_fiscale(fornitore.getAnagrafico().getCodice_fiscale());
		spesa.setPartita_iva(fornitore.getAnagrafico().getPartita_iva());
		spesa.setIndirizzo_fornitore(fornitore.getVia_sede());
		spesa.setCitta(fornitore.getComune_sede());
		spesa.setCap_fornitore(fornitore.getCap_comune_sede());
		spesa.setCaps_fornitore(fornitore.getCaps_comune());
	}

	return context.findDefaultForward();
}
/**
 * Ricerca il documento amministrativo in base alla selezione della tipologia effettuata
 * dall'utente per il collegamento alla spesa
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doCercaDocumento(it.cnr.jada.action.ActionContext context) {

	try {
		fillModel(context);
		FondoSpesaBP bp = (FondoSpesaBP)context.getBusinessProcess();
		Fondo_spesaBulk spesa = (Fondo_spesaBulk)bp.getModel();
		if (spesa.getDt_spesa() == null)
			throw new it.cnr.jada.comp.ApplicationException("Inserire la data spesa prima di selezionare un documento amministrativo.");
		if (spesa.getDateCalendar(spesa.getDt_spesa()).get(java.util.Calendar.YEAR) != 
			spesa.getEsercizio().intValue())
			throw new it.cnr.jada.comp.ApplicationException("La data spesa deve appartenere all'esercizio del documento!");
		IDocumentoAmministrativoSpesaBulk documentoDiSpesa = (IDocumentoAmministrativoSpesaBulk)spesa.getClasseDocAmm().newInstance();
		if (documentoDiSpesa != null) {
			CRUDBP docAmmBP = (CRUDBP)context.createBusinessProcess(
												documentoDiSpesa.getManagerName(),
												new String[] { documentoDiSpesa.getManagerOptions() });
			context.addHookForward("bringback",this,"doBringBackCercaDocumento");
			HookForward hook = (HookForward)context.findForward("bringback");
			it.cnr.jada.action.Forward fwd = context.addBusinessProcess(docAmmBP);
			//Necesse est dopo l'add al context per la corretta inizializzazione
			docAmmBP.getModel().initializeForSearch(docAmmBP, context);
			return fwd;
		}
	} catch (Throwable e) {
		return handleException(context, e);
	}
	return context.findDefaultForward();
}
/**
 * Salva o riporta la spesa del fondo dopo la conferma dell'utente nonostante
 * lo sfondamento  del massimale
 *
 * @param context	L'ActionContext della richiesta
 * @param option	
 * @return Il Forward alla pagina di risposta
 * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
 */
public Forward doConfermaSfondamentoMassimale(ActionContext context,int option)  throws java.rmi.RemoteException 
{
	if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) 
	{
		try 
		{
			CRUDBP bp = getBusinessProcess(context);
			((Fondo_spesaBulk)bp.getModel()).setCheckSfondamentoMassimaleEseguito(true);
			if (bp.isBringBack())
				return doConfermaRiporta(context,option);
			else
				doSalva(context);
			((Fondo_spesaBulk)bp.getModel()).setCheckSfondamentoMassimaleEseguito(false);			
		} 
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
		return context.findDefaultForward();
}
/**
 * Gestisce il comando "doCloseForm".
 * Chiude il BusinessProcess corrente e restituisce il default forward.
 * @param context Il contesto della action
 * @return Il default forward.
 */
public Forward doConfirmCloseForm(ActionContext context, int option) throws it.cnr.jada.action.BusinessProcessException {

	if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
		Forward defaultForward = super.doConfirmCloseForm(context, option);
		Forward forward = context.findForward("chiusuraSpese");
		if (forward == null)
			return defaultForward;
		return forward;
	}
	return super.doConfirmCloseForm(context, option);
}
/**
 * Abilita o meno la ricerca del fornitore (true = flatuario)
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doOnFlFornitoreSaltuarioChange(it.cnr.jada.action.ActionContext context) {

	try {
		fillModel(context);
		FondoSpesaBP bp = (FondoSpesaBP)context.getBusinessProcess();
		Fondo_spesaBulk spesa = (Fondo_spesaBulk)bp.getModel();
		if (spesa.getFl_fornitore_saltuario() == null) 
			return context.findDefaultForward();
		if (spesa.getFl_fornitore_saltuario().booleanValue()) {
			spesa.setFornitore(null);
		} else {
			doBlankSearchFornitore(context, spesa);
			//TerzoBulk tb = new TerzoBulk();
			//tb.setAnagrafico(new AnagraficoBulk());
			//spesa.setFornitore(tb);
		}
		spesa.setCitta(new ComuneBulk());
		spesa.setCaps_fornitore(null);
	} catch (it.cnr.jada.bulk.FillException e) {
		return handleException(context, e);
	}
	return context.findDefaultForward();
}
/**
 * Abilita o meno, preparando di conseguenza il modello, all'emissione di una 
 * spesa documentata/non documentata
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doOnFlSpesaDocumentataChange(it.cnr.jada.action.ActionContext context) {

	try {
		FondoSpesaBP bp = (FondoSpesaBP)context.getBusinessProcess();
		Fondo_spesaBulk spesa = (Fondo_spesaBulk)bp.getModel();
		fillModel(context);
		if (spesa.getFl_documentata() != null) {
			spesa.setIm_ammontare_spesa(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
			spesa.setImportoNettoSpesa(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
			spesa.setDs_spesa(null);
			spesa.setFornitore(null);
			spesa.setDocumento(null);
			if (!spesa.getFl_documentata().booleanValue()) {
				spesa.setFl_fornitore_saltuario(Boolean.TRUE);
			} else {
				spesa.setFl_fornitore_saltuario(Boolean.FALSE);
			}
		}
//		spesa.setFl_obbligazione(Boolean.FALSE);
	} catch (it.cnr.jada.bulk.FillException e) {
		return handleException(context, e);
	}
	return context.findDefaultForward();
}
/**
 * Gestisce l'opzione per la ricerca nel filtro ricerca spesa
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doOnFlSpesaDocumentataForSearchChange(
	it.cnr.jada.action.ActionContext context) {

	try {
		fillModel(context);
		FondoSpesaBP bp = (FondoSpesaBP)context.getBusinessProcess();
		Fondo_spesaBulk spesa = (Fondo_spesaBulk)bp.getModel();
		if (spesa.getFl_documentata() == null || spesa.getFl_documentata().booleanValue())
			spesa.setSpesaAssociata(spesa.IGNORA);
	} catch (it.cnr.jada.bulk.FillException e) {
		return handleException(context, e);
	}
	return context.findDefaultForward();
}
/**
 * Riporta la città selezionata nel modello correte
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
 */
public it.cnr.jada.action.Forward doRiportaSelezioneCitta(it.cnr.jada.action.ActionContext context)  throws java.rmi.RemoteException {

	it.cnr.jada.action.HookForward caller = (it.cnr.jada.action.HookForward)context.getCaller();

	ComuneBulk comune = (ComuneBulk)caller.getParameter("selezione");
	FondoSpesaBP spesaBP = (FondoSpesaBP)getBusinessProcess(context);
	if (comune != null) {
		Fondo_spesaBulk spesa = (Fondo_spesaBulk)spesaBP.getModel();
		spesa.setCitta(comune);

		if( comune.getPg_comune() != null && !(comune.getPg_comune()).equals("") ) {
			try {
				getBusinessProcess(context).setModel(
					context,
					((FondoSpesaComponentSession)spesaBP.createComponentSession()).setCitta(
										context.getUserContext(), 
										spesa,
										comune)
				);
			} catch(it.cnr.jada.action.BusinessProcessException bpe) {
				return handleException(context, bpe);
			} catch(it.cnr.jada.comp.ComponentException ce) {
				return handleException(context, ce);
			}
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
 * @param ex Eccezione da gestire.
 *
 * @return Forward
 *
 * @throws RemoteException
 *
 * @see #
 */

public it.cnr.jada.action.Forward handleException(it.cnr.jada.action.ActionContext context, Throwable ex) {
	try {
		throw ex;
	} catch(it.cnr.contab.fondecon00.comp.ErroreSquadraturaFondo e) {

		FondoSpesaBP bp = (FondoSpesaBP)getBusinessProcess(context);
		try {
			return openConfirm(context, e.getMessage(), it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doConfermaSfondamentoMassimale");
		} catch(Throwable twb) {
			bp.setErrorMessage(e.getMessage());
			return context.findDefaultForward();
		}

	} catch(Throwable e) {
		return super.handleException(context,e);
	}
}
}
