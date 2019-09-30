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

import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneOrdBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.fondecon00.bp.RicercaObbScadBP;
import it.cnr.contab.fondecon00.core.bulk.Filtro_ricerca_obbligazioniVBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.OptionBP;

import java.rmi.RemoteException;

import javax.ejb.RemoveException;

/**
 * Gestione delle azioni relative alla ricerca delle obbligazioni (scadenze)
 * da associare alle spese di un fondo economale da reintegrare
 */

public class RicercaObbScadAction extends it.cnr.jada.util.action.BulkAction {
public RicercaObbScadAction() {
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
 * Riporta la scadenza selezionata nella spesa del fondo economale
 * NON USATO solo preparato
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doBringBackOpenObbligazioniWindow(ActionContext context) {

	try {
		HookForward caller = (HookForward)context.getCaller();
		Obbligazione_scadenzarioBulk obblig = (Obbligazione_scadenzarioBulk)caller.getParameter("bringback");
		if (obblig != null) {
			context.closeBusinessProcess();
			HookForward forward = (HookForward)context.findForward("bringback");
			forward.addParameter("scadenzaSelezionata", obblig);
			return forward;
		}
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Cerca le scdenze disponibili per l'associazione alle spese del fondo eco
 * (Le spese sono NON documentate)
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
 * @throws InstantiationException	
 * @throws RemoveException	
 */
public Forward doCerca(ActionContext context) throws java.rmi.RemoteException,InstantiationException,javax.ejb.RemoveException {

		try {
			fillModel(context);
			RicercaObbScadBP bp = (RicercaObbScadBP)context.getBusinessProcess();
			it.cnr.jada.util.RemoteIterator ri = bp.findObb_scad(context);
			ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
			if (ri == null || ri.countElements() == 0) {
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
				bp.setMessage("La ricerca non ha fornito alcun risultato.");
				return context.findDefaultForward();
			} else {
				return select(context,ri,it.cnr.jada.bulk.BulkInfo.getBulkInfo(it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk.class),null,"doRiportaSelezione");
			}
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
/**
 * Gestisce la chiusura del pannello di ricerca
 */

public Forward doCloseForm(ActionContext context) {

    try {
        return doConfirmCloseForm(context, OptionBP.YES_BUTTON);
    } catch (BusinessProcessException e) {
        return handleException(context, e);
    }
}
/**
 * Prepara il modello filtro per una nuova ricerca
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @param option	
 * @return Il Forward alla pagina di risposta
 */
public Forward doConfermaNuovaRicerca(ActionContext context,int option) {
		try {
			RicercaObbScadBP bp = (RicercaObbScadBP)context.getBusinessProcess();
			if (option == OptionBP.YES_BUTTON)
				bp.resetForSearch(context);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
/**
 * Chiede all'utente conferma per una nuova ricerca
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doNuovaRicerca(ActionContext context) {
		try {
			RicercaObbScadBP bp = (RicercaObbScadBP)context.getBusinessProcess();
			fillModel(context);
			if (bp.isDirty())
				return openContinuePrompt(context, "doConfermaNuovaRicerca");
			return doConfermaNuovaRicerca(context,OptionBP.YES_BUTTON);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
/**
 * Prepara e imposta il filtro in base all'abilitazioni delle condizioni di
 * ricerca
 */
private Forward doOnFlChange(ActionContext context, String flName) {
	
	try {

		RicercaObbScadBP bp = (RicercaObbScadBP)context.getBusinessProcess();
		fillModel(context);
		Filtro_ricerca_obbligazioniVBulk filtro =  (Filtro_ricerca_obbligazioniVBulk)bp.getModel();
		if("fl_fornitore".equalsIgnoreCase(flName)
			&& filtro.getFornitore() == null
			&& filtro.getFl_fornitore().booleanValue())
		{
				filtro.setFornitore(new it.cnr.contab.anagraf00.core.bulk.TerzoBulk());
		} else
			if("fl_importo".equalsIgnoreCase(flName)
				&& filtro.getIm_importo() == null
				&& filtro.getFl_importo().booleanValue())
		{
				filtro.setIm_importo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
		} else
			if("fl_data_scadenziario".equalsIgnoreCase(flName)
				&& filtro.getData_scadenziario() == null
				&& filtro.getFl_data_scadenziario().booleanValue())
		{
				filtro.setData_scadenziario(filtro.getCurrentDate());
		} else
			if("fl_nr_obbligazione".equalsIgnoreCase(flName) &&
				filtro.getFl_nr_obbligazione().booleanValue())
		{
				filtro.setNr_obbligazione(null);
				filtro.setNr_scadenza(null);
		}
		return context.findDefaultForward();
		
	} catch (Throwable t) {
		return handleException(context, t);
	}
}

public Forward doOnFlDataScadenziarioChange(ActionContext context) {
	
	return doOnFlChange(context, "fl_data_scadenziario");
}

public Forward doOnFlFornitoreChange(ActionContext context) {
	
	return doOnFlChange(context, "fl_fornitore");
}

public Forward doOnFlImportoChange(ActionContext context) {
	
	return doOnFlChange(context, "fl_importo");
}

public Forward doOnFlNrObbligazioneChange(ActionContext context) {
	
	return doOnFlChange(context, "fl_nr_obbligazione");
}
/**
 * NON USATO (solo preparato)
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doOpenObbligazioniWindow(ActionContext context) {

	try {
		RicercaObbScadBP bp = (RicercaObbScadBP)context.getBusinessProcess();
		Filtro_ricerca_obbligazioniVBulk filtro = (Filtro_ricerca_obbligazioniVBulk)bp.getModel();
		fillModel(context);

		context.addHookForward("bringback",this,"doBringBackOpenObbligazioniWindow");

		it.cnr.contab.doccont00.bp.CRUDObbligazioneBP obbligazioneBP = (it.cnr.contab.doccont00.bp.CRUDObbligazioneBP)context.getUserInfo().createBusinessProcess(context,"CRUDObbligazioneBP",new Object[] { "MRSWTh" });
		obbligazioneBP.reset(context);

		ObbligazioneBulk obbligazione = (ObbligazioneBulk)obbligazioneBP.getModel();
		obbligazione.setStato_obbligazione(ObbligazioneOrdBulk.STATO_OBB_DEFINITIVO);
		obbligazione.setCreditore(filtro.getFornitore()!=null?filtro.getFornitore():new it.cnr.contab.anagraf00.core.bulk.TerzoBulk());
		obbligazione.setDs_obbligazione("Impegno per fondo economale");
		try {
			obbligazione.setDt_registrazione(it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp());
		} catch (javax.ejb.EJBException e) {
			return handleException(context, e);
		}
		obbligazione.setFl_calcolo_automatico(Boolean.TRUE);
		obbligazione.setIm_obbligazione(filtro.getIm_importo());
		obbligazione.setRiportato("N");
		
		Obbligazione_scadenzarioBulk scadenza = new Obbligazione_scadenzarioBulk(obbligazione);
		scadenza.setDs_scadenza("Scadenza per fondo economale");
		try {
			scadenza.setDt_scadenza(it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp());
		} catch (javax.ejb.EJBException e) {
			throw new it.cnr.jada.DetailedRuntimeException(e);
		}
		scadenza.setIm_scadenza(
			(filtro.getIm_importo() == null)?
				new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP) :
				filtro.getIm_importo());
		scadenza.setUser(obbligazione.getUser());
		scadenza.setToBeCreated();
		obbligazione.addToObbligazione_scadenzarioColl(scadenza);
		
		return context.addBusinessProcess(obbligazioneBP);
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Riporta nella spesa la scadenza selezionata
 */
public Forward doRiportaSelezione(ActionContext context) {

	try {
		HookForward caller = (HookForward)context.getCaller();
		it.cnr.jada.bulk.OggettoBulk selezione = (it.cnr.jada.bulk.OggettoBulk)caller.getParameter("focusedElement");
		if (selezione != null) {
			HookForward forward = (HookForward)context.findForward("bringback");
			forward.addParameter("filtroUtilizzato", ((BulkBP)context.getBusinessProcess()).getModel());
			context.closeBusinessProcess();
			forward.addParameter("scadenzaSelezionata", selezione);
			return forward;
		}
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}

}
