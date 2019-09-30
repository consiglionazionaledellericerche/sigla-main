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
public class RicercaObbligazioniAction extends it.cnr.jada.util.action.BulkAction {
/**
 * RicercaObbligazioniAction constructor comment.
 */
public RicercaObbligazioniAction() {
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
			Filtro_ricerca_obbligazioniVBulk filtro = (Filtro_ricerca_obbligazioniVBulk)bulk;
			filtro.setFornitore(new it.cnr.contab.anagraf00.core.bulk.TerzoBulk());
			filtro.getFornitore().setAnagrafico(new it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk());
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Rimanda l'obbligazione creato o selezionato al doc amm
 */
public Forward doBringBackOpenObbligazioniWindow(ActionContext context) {

	try {
		HookForward caller = (HookForward)context.getCaller();
		Obbligazione_scadenzarioBulk obblig = (Obbligazione_scadenzarioBulk)caller.getParameter("bringback");
		if (obblig != null) {
			Filtro_ricerca_obbligazioniVBulk filtro = (Filtro_ricerca_obbligazioniVBulk)((BulkBP)context.getBusinessProcess()).getModel();

			context.closeBusinessProcess();
			HookForward forward = (HookForward)context.findForward("bringback");
			forward.addParameter("obbligazioneSelezionata", obblig);
			forward.addParameter("filtroRicercaUtilizzato", filtro);

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
		RicercaObbligazioniBP bp = (RicercaObbligazioniBP)context.getBusinessProcess();
		Filtro_ricerca_obbligazioniVBulk filtro = (Filtro_ricerca_obbligazioniVBulk)bp.getModel();
		it.cnr.jada.util.RemoteIterator ri = bp.findObbligazioni(context, filtro);
		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
		if (ri == null || ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
			bp.setMessage("La ricerca non ha fornito alcun risultato.");
			return context.findDefaultForward();
/*		}
		else if (ri.countElements() == 1) {
			OggettoBulk bulk = (OggettoBulk)ri.nextElement();
			if (ri instanceof javax.ejb.EJBObject)
				((javax.ejb.EJBObject)ri).remove();
			bp.setMessage("La ricerca ha fornito un solo risultato.");
			bp.edit(context,bulk);
			return context.findDefaultForward();*/
		} else {
			bp.setModel(context,filtro);
			SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
			nbp.setIterator(context,ri);
			nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk.class));
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
		RicercaObbligazioniBP bp = (RicercaObbligazioniBP)context.getBusinessProcess();
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
		RicercaObbligazioniBP bp = (RicercaObbligazioniBP)context.getBusinessProcess();
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

		RicercaObbligazioniBP bp = (RicercaObbligazioniBP)context.getBusinessProcess();
		fillModel(context);
		Filtro_ricerca_obbligazioniVBulk filtro =  (Filtro_ricerca_obbligazioniVBulk)bp.getModel();
		if (filtro.getFornitore()!=null &&
			filtro.getFornitore().getAnagrafico() != null &&
			it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk.DIVERSI.equals(filtro.getFornitore().getAnagrafico().getTi_entita()))
			filtro.setFl_fornitore(Boolean.TRUE);
		else if ("fl_fornitore".equalsIgnoreCase(flName)) {
			if (filtro.getFornitore() != null && filtro.getFornitore().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.UNDEFINED)
				filtro.setFornitore(null);
		} else if ("fl_importo".equalsIgnoreCase(flName)) {
			if (filtro.getIm_importo() == null)
				filtro.setIm_importo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
		} else if ("fl_data_scadenziario".equalsIgnoreCase(flName)) {
			if (filtro.getData_scadenziario() == null)
				filtro.setData_scadenziario(filtro.getCurrentDate());
		} else if ("tipo_obbligazione".equalsIgnoreCase(flName)) {
			if (!bp.isEsercizioOriObbligazioneVisible())
				filtro.setEsercizio_ori_obbligazione(null);
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
public Forward doOnFlDataScadenziarioChange(ActionContext context) {
	
	return doOnFlChange(context, "fl_data_scadenziario");
}
/**
 * Viene richiamato nel momento in cui si seleziona una valuta dal combo Valuta nella 
 * testata della fattura.
 * Richiama a sua volta il metodo cercaCambio dalla component.
 */
public Forward doOnFlFornitoreChange(ActionContext context) {
	
	return doOnFlChange(context, "fl_fornitore");
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
 * Viene richiamato nel momento in cui si seleziona il tipo di Impegno da ricercare. 
 */
public Forward doOnTipoObbligazioneChange(ActionContext context) {

	return doOnFlChange(context, "tipo_obbligazione");
}
/**
 * Viene richiamato nel momento in cui si seleziona una valuta dal combo Valuta nella 
 * testata della fattura.
 * Richiama a sua volta il metodo cercaCambio dalla component.
 */
public Forward doOnFlNrObbligazioneChange(ActionContext context) {
	
	return doOnFlChange(context, "fl_nr_obbligazione");
}
/**
 * Crea, prepara e apre una nuova obbligazione
 */
 
public Forward doOpenObbligazioniWindow(ActionContext context) {

	try {
		RicercaObbligazioniBP bp = (RicercaObbligazioniBP)context.getBusinessProcess();
		Filtro_ricerca_obbligazioniVBulk filtro = (Filtro_ricerca_obbligazioniVBulk)bp.getModel();
		fillModel(context);

		if (filtro.isPassivo_ente())
			throw new it.cnr.jada.comp.ApplicationException("Impossibile creare obbligazioni per un documento generico dell'ente");

		//to verify
		if (filtro.getTipo_documento()!=null && filtro.getTipo_documento().getFl_solo_partita_giro().booleanValue())
			throw new it.cnr.jada.comp.ApplicationException("Impossibile creare partite di giro");
		
		context.addHookForward("bringback",this,"doBringBackOpenObbligazioniWindow");

		it.cnr.contab.doccont00.bp.CRUDObbligazioneBP obbligazioneBP=null;

		if (filtro.getTipo_obbligazione()==null || filtro.getTipo_obbligazione().equals(ObbligazioneBulk.TIPO_COMPETENZA))
			obbligazioneBP = (it.cnr.contab.doccont00.bp.CRUDObbligazioneBP)context.getUserInfo().createBusinessProcess(context,"CRUDObbligazioneBP",new Object[] { "MRSWTh" });
		else if (filtro.getTipo_obbligazione().equals(ObbligazioneBulk.TIPO_RESIDUO_IMPROPRIO)) 
			obbligazioneBP = (it.cnr.contab.doccont00.bp.CRUDObbligazioneBP)context.getUserInfo().createBusinessProcess(context,"CRUDObbligazioneResImpropriaBP",new Object[] { "MRSWTh" });			
		else if (filtro.getTipo_obbligazione().equals(ObbligazioneBulk.TIPO_RESIDUO_PROPRIO))
			throw new it.cnr.jada.comp.ApplicationException("Impossibile creare un nuovo impegno residuo proprio. Scegliere una diversa tipologia di impegno da creare.");
			
		obbligazioneBP.reset(context);

		ObbligazioneBulk obbligazione = (ObbligazioneBulk)obbligazioneBP.getModel();
		obbligazione.setStato_obbligazione(ObbligazioneOrdBulk.STATO_OBB_DEFINITIVO);
		it.cnr.contab.anagraf00.core.bulk.TerzoBulk creditore = null;
		if (filtro.getFornitore() != null)
			creditore = filtro.getFornitore();
		else {
			creditore = new it.cnr.contab.anagraf00.core.bulk.TerzoBulk();
			creditore.setAnagrafico(new it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk());
		}
		obbligazione.setCreditore(creditore);
		obbligazione.setDs_obbligazione(filtro.getDs_obbligazione());
		
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
		obbligazione.setDt_registrazione(dataReg);
		obbligazione.setContratto(filtro.getContratto());
		obbligazione.setListaVociSelezionabili(filtro.getListaVociSelezionabili());
		obbligazione.setFl_calcolo_automatico(Boolean.TRUE);
		if (filtro.getIm_importo()==null)
			filtro.setIm_importo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
		obbligazione.setIm_obbligazione(filtro.getIm_importo());
		if (filtro.getElemento_voce() != null)
			obbligazione.getElemento_voce().setCd_elemento_voce(filtro.getElemento_voce().getCd_elemento_voce());
		obbligazione.setRiportato("N");
		//obbligazione.setFl_netto_sospeso(new Boolean( false ));
				
		Obbligazione_scadenzarioBulk scadenza = new Obbligazione_scadenzarioBulk(obbligazione);
		obbligazione.addToObbligazione_scadenzarioColl(scadenza);
		scadenza.setDs_scadenza(filtro.getDs_scadenza());
		scadenza.setDt_scadenza(dataReg);
		scadenza.setIm_scadenza(filtro.getIm_importo());
		scadenza.setUser(obbligazione.getUser());
		scadenza.setToBeCreated();
		
		return context.addBusinessProcess(obbligazioneBP);
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

			try	{
				RicercaObbligazioniBP bp = (RicercaObbligazioniBP)context.getBusinessProcess();
				bp.validaObbligazione(context, selezione);
			}catch ( BusinessProcessException be ){
				return handleException(context, be);
			}
		
			context.closeBusinessProcess();
			HookForward forward = (HookForward)context.findForward("bringback");
			forward.addParameter("obbligazioneSelezionata", selezione);
			return forward;
		}
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
}
