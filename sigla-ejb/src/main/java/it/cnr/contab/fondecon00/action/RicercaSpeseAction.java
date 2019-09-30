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

import it.cnr.contab.fondecon00.bp.*;
import it.cnr.contab.fondecon00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

/**
 * Gestione delle azioni relative alla ricerca delle spese di un fondo economale
 */

public class RicercaSpeseAction extends it.cnr.jada.util.action.BulkAction {
public RicercaSpeseAction() {
	super();
}
/**
 * Ricerca le spese reintegrabili del fondo economale appoggiandosi al 
 * filtro ricerca
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
		RicercaSpeseBP bp = (RicercaSpeseBP)context.getBusinessProcess();
		Filtro_ricerca_speseVBulk filtro = (Filtro_ricerca_speseVBulk)bp.getModel();
		it.cnr.jada.util.RemoteIterator ri = bp.findSpeseReintegrabili(context, filtro);
		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
		if (ri == null || ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
			bp.setMessage("La ricerca non ha fornito alcun risultato.");
			return context.findDefaultForward();
		} else {
			bp.setModel(context, filtro);
			SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
			nbp.setIterator(context,ri);
			nbp.setMultiSelection(filtro.isMultiSelection());
			nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk.class));
			context.addHookForward("seleziona",this,"doRiportaSelezione");
			return context.addBusinessProcess(nbp);
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
		return doConfirmCloseForm(context,OptionBP.YES_BUTTON);
	} catch(Throwable e) {
		return handleException(context,e);
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
		RicercaSpeseBP bp = (RicercaSpeseBP)context.getBusinessProcess();
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
		RicercaSpeseBP bp = (RicercaSpeseBP)context.getBusinessProcess();
		fillModel(context);
		if (bp.isDirty())
			return openContinuePrompt(context, "doConfermaNuovaRicerca");
		return doConfermaNuovaRicerca(context,OptionBP.YES_BUTTON);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * NON USATO solo preparato
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doNuovo(it.cnr.jada.action.ActionContext context) {
		try {

			FondoSpesaBP bp =
				(FondoSpesaBP)context.getUserInfo(
					).createBusinessProcess(
							context,
							"FondoSpesaBP",
							new Object[] { "M" }
					);

			Fondo_spesaBulk spesa = new Fondo_spesaBulk();
			spesa.initializeForInsert(bp, context);
			bp.reset( context, spesa );

			spesa.setFondo_economale(
				(Fondo_economaleBulk)((RicercaSpeseBP)context.getBusinessProcess()).getFondo()
			);
			spesa.initializeForInsert(bp, context);

			return context.addBusinessProcess(bp);

		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
/**
 * Riporta la spesa selezionata
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
		java.util.List selezione = (java.util.List)caller.getParameter("selectedElements");
		if (selezione != null) {
			context.closeBusinessProcess();
			HookForward forward = (HookForward)context.findForward("bringback");
			forward.addParameter("speseSelezionate", selezione);
			return forward;
		}
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
}
