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

import it.cnr.contab.docamm00.bp.*;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 2:36:05 PM)
 * @author: Roberto Peli
 */
public class RicercaAccertamentiAction extends it.cnr.jada.util.action.BulkAction {
/**
 * RicercaObbligazioniAction constructor comment.
 */
public RicercaAccertamentiAction() {
	super();
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
public Forward doBlankSearchFornitore(ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) {

	try {
		if (bulk != null) {
			Filtro_ricerca_accertamentiVBulk filtro = (Filtro_ricerca_accertamentiVBulk)bulk;
			filtro.setCliente(new it.cnr.contab.anagraf00.core.bulk.TerzoBulk());
			filtro.getCliente().setAnagrafico(new it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk());
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Rimanda l'accertamento creato o selezionato al doc amm
 */
public Forward doBringBackOpenAccertamentiWindow(ActionContext context) {

	try {
		HookForward caller = (HookForward)context.getCaller();
		Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk)caller.getParameter("bringback");
		if (scadenza != null) {
			Filtro_ricerca_accertamentiVBulk filtro = (Filtro_ricerca_accertamentiVBulk)((BulkBP)context.getBusinessProcess()).getModel();
			context.closeBusinessProcess();
			HookForward forward = (HookForward)context.findForward("bringback");
			forward.addParameter("accertamentoSelezionato", scadenza);
			return forward;
		}
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
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
		fillModel(context);
		RicercaAccertamentiBP bp = (RicercaAccertamentiBP)context.getBusinessProcess();
		Filtro_ricerca_accertamentiVBulk filtro = (Filtro_ricerca_accertamentiVBulk)bp.getModel();
		it.cnr.jada.util.RemoteIterator ri = bp.findAccertamenti(context, filtro);
		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
		if (ri == null || ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
			bp.setMessage("La ricerca non ha fornito alcun risultato.");
			return context.findDefaultForward();
		} else {
			bp.setModel(context,filtro);
			SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
			nbp.setIterator(context,ri);
			nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk.class));
			context.addHookForward("seleziona",this,"doRiportaSelezione");
			return context.addBusinessProcess(nbp);
		}
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce la chiusura del pannello
 */
 
public Forward doCloseForm(ActionContext context) {

	try {
		return doConfirmCloseForm(context,OptionBP.YES_BUTTON);
	} catch (BusinessProcessException e) {
		return handleException(context, e);
	}
}
/**
 * Gestisce un comando "nuovo ricerca".
 *
 * L'implementazione di default utilizza il metodo astratto <code>resetForSearch</code>
 * di <code>CRUDBusinessProcess</code>
 */
public Forward doConfermaNuovaRicerca(ActionContext context,int option) {
	try {
		RicercaAccertamentiBP bp = (RicercaAccertamentiBP)context.getBusinessProcess();
		if (option == OptionBP.YES_BUTTON)
			bp.resetForSearch(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce un comando "nuova ricerca".
 */
public Forward doNuovaRicerca(ActionContext context) {
	try {
		RicercaAccertamentiBP bp = (RicercaAccertamentiBP)context.getBusinessProcess();
		fillModel(context);
		if (bp.isDirty())
			return openContinuePrompt(context, "doConfermaNuovaRicerca");
		return doConfermaNuovaRicerca(context,OptionBP.YES_BUTTON);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Abilita o disabilita la condizione di filtro selezionata
 */
private Forward doOnFlChange(ActionContext context, String flName) {
	
	try {

		RicercaAccertamentiBP bp = (RicercaAccertamentiBP)context.getBusinessProcess();
		fillModel(context);
		Filtro_ricerca_accertamentiVBulk filtro =  (Filtro_ricerca_accertamentiVBulk)bp.getModel();
		if (filtro.getCliente() != null && 
			filtro.getCliente().getAnagrafico() != null &&
			it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk.DIVERSI.equals(filtro.getCliente().getAnagrafico().getTi_entita()))
			filtro.setFl_cliente(Boolean.TRUE);
		else if ("fl_fornitore".equalsIgnoreCase(flName)) {
			if (filtro.getCliente() != null && filtro.getCliente().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.UNDEFINED)
				filtro.setCliente(null);
		} else if ("fl_importo".equalsIgnoreCase(flName)) {
			if (filtro.getIm_importo() == null)
				filtro.setIm_importo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
		} else if ("fl_data_scadenziario".equalsIgnoreCase(flName)) {
			if (filtro.getData_scadenziario() == null)
				filtro.setData_scadenziario(filtro.getCurrentDate());
		}
		bp.setModel(context,filtro);
		bp.resyncChildren(context);
		return context.findDefaultForward();
		
	} catch (Throwable t) {
		return handleException(context, t);
	}
}
/**
 * Viene richiamato nel momento in cui si seleziona una valuta dal combo Valuta nella 
 * testata della fattura.
 * Richiama a sua volta il metodo cercaCambio dalla component.
 */
public Forward doOnFlClienteChange(ActionContext context) {
	
	return doOnFlChange(context, "fl_cliente");
}
/**
 * Viene richiamato nel momento in cui si seleziona una valuta dal combo Valuta nella 
 * testata della fattura.
 * Richiama a sua volta il metodo cercaCambio dalla component.
 */
public Forward doOnFlDataScadenziarioChange(ActionContext context) {
	
	return doOnFlChange(context, "fl_data_scadenziario");
}
/**
 * Viene richiamato nel momento in cui si seleziona una valuta dal combo Valuta nella 
 * testata della fattura.
 * Richiama a sua volta il metodo cercaCambio dalla component.
 */
public Forward doOnFlImportoChange(ActionContext context) {
	
	return doOnFlChange(context, "fl_importo");
}
/**
 * Viene richiamato nel momento in cui si seleziona una valuta dal combo Valuta nella 
 * testata della fattura.
 * Richiama a sua volta il metodo cercaCambio dalla component.
 */
public Forward doOnFlNrAccertamentoChange(ActionContext context) {
	
	return doOnFlChange(context, "fl_nr_accertamento");
}
/**
 * Crea, prepara e apre un nuovo accertamento
 */
 
public Forward doOpenAccertamentiWindow(ActionContext context) {

	try {
		RicercaAccertamentiBP bp = (RicercaAccertamentiBP)context.getBusinessProcess();
		Filtro_ricerca_accertamentiVBulk filtro = (Filtro_ricerca_accertamentiVBulk)bp.getModel();
		fillModel(context);

		if (filtro.isAttivoCds())
			throw new it.cnr.jada.comp.ApplicationException("Impossibile creare accertamenti per un documento generico attivo del Cds!");

		context.addHookForward("bringback",this,"doBringBackOpenAccertamentiWindow");

		it.cnr.contab.doccont00.bp.CRUDAccertamentoBP accertamentoBP = (it.cnr.contab.doccont00.bp.CRUDAccertamentoBP)context.getUserInfo().createBusinessProcess(context,"CRUDAccertamentoBP",new Object[] { "MRSWTh" });
		accertamentoBP.reset(context);

		AccertamentoOrdBulk accertamento = (AccertamentoOrdBulk)accertamentoBP.getModel();
		accertamento.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_ACR);
		it.cnr.contab.anagraf00.core.bulk.TerzoBulk debitore = null;
		if (filtro.getCliente() != null)
			debitore = filtro.getCliente();
		else {
			debitore = new it.cnr.contab.anagraf00.core.bulk.TerzoBulk();
			debitore.setAnagrafico(new it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk());
		}
		accertamento.setDebitore(debitore);

		java.sql.Timestamp dataReg = null;
		try {
			dataReg = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
		} catch (javax.ejb.EJBException e) {
			return handleException(context, e);
		}
		java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
		calendar.setTime(dataReg);
		int esercizioInScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue();
		if (calendar.get(java.util.Calendar.YEAR) != esercizioInScrivania)
			dataReg = new java.sql.Timestamp(new java.text.SimpleDateFormat("dd/MM/yyyy").parse("31/12/" + esercizioInScrivania).getTime());
		accertamento.setDt_registrazione(dataReg);

		accertamento.setDs_accertamento("Accertamento per documenti amministrativi attivi");
		accertamento.setIm_accertamento(filtro.getIm_importo());
		accertamento.setRiportato("N");
		accertamento.setEsercizio_competenza(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		
		//Accertamento_scadenzarioBulk scadenza = new Accertamento_scadenzarioBulk(accertamento);
		//scadenza.setDs_scadenza("Scadenza per fattura passiva");
		//scadenza.setDt_scadenza_incasso(new java.sql.Timestamp(System.currentTimeMillis()));
		//scadenza.setDt_scadenza_emissione_fattura(null);
		//scadenza.setIm_scadenza(filtro.getIm_importo());
		//scadenza.setToBeCreated();
		//accertamento.addToAccertamento_scadenzarioColl(scadenza);
	
		return context.addBusinessProcess(accertamentoBP);
	} catch(Exception e) {
		return handleException(context,e);
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
public Forward doRiportaSelezione(ActionContext context) {

	try {
		HookForward caller = (HookForward)context.getCaller();
		it.cnr.jada.bulk.OggettoBulk selezione = (it.cnr.jada.bulk.OggettoBulk)caller.getParameter("focusedElement");
		if (selezione != null) {
			context.closeBusinessProcess();
			HookForward forward = (HookForward)context.findForward("bringback");
			forward.addParameter("accertamentoSelezionato", selezione);
			return forward;
		}
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
}
