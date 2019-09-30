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

public class RicercaEconomaleAction extends it.cnr.jada.util.action.SelezionatoreListaAction {

	public RicercaEconomaleAction() {
		super();
	}

/**
 * Ricerca i fondi economali disponibili
 */
public it.cnr.jada.action.Forward doApriRicercaSpese(it.cnr.jada.action.ActionContext context, Fondo_economaleBulk fondo) {
	try {

		RicercaSpeseBP bp =
			(RicercaSpeseBP)context.getUserInfo(
				).createBusinessProcess(
						context,
						"RicercaSpeseBP",
						new Object[] { "M" }
				);
		context.addHookForward("bringback",this,"doApriSpese");
		Filtro_ricerca_speseVBulk filtro = new Filtro_ricerca_speseVBulk();
		filtro.setFondo_economale( fondo );
		bp.setFondo( filtro.getFondo_economale() );
		bp.setModel(context, filtro );
		return context.addBusinessProcess(bp);

	} catch(Throwable e) {
		return handleException(context,e);
	}
}

/**
 * Prepara e apre per la ricerca il pannello delle spese di un fondo economale
 */
public it.cnr.jada.action.Forward doApriSpese(it.cnr.jada.action.ActionContext context) {

	try {

		RicercaEconomaleBP bpCorrente = (RicercaEconomaleBP)context.getBusinessProcess();
		FondoSpesaBP bp = (FondoSpesaBP)context.createBusinessProcess(
														"FondoSpesaBP", 
														new Object[] {
																bpCorrente.getLastFondoSelected(),
																(bpCorrente.isEditable()?"M":"V")
															}
													);
		//bp.createEmptyModelForSearch(context);
		//bp.edit(context, (Fondo_spesaBulk)selezione);
		((Fondo_spesaBulk)bp.getModel()).initializeForSearch(bp, context);
		context.addHookForward("chiusuraSpese",this,"doChiusuraSpese");
		return context.addBusinessProcess(bp);

	} catch(Throwable e) {
		return handleException(context,e);
	}
}

/**
 * Riporto il fondo economale selezionato dall'utente e apro il pannello delle
 * spese in ricerca sul fondo stesso
 */
public Forward doBringBack(ActionContext context) throws BusinessProcessException {

	RicercaEconomaleBP bp = (RicercaEconomaleBP)context.getBusinessProcess();
	Fondo_economaleBulk selezione = (Fondo_economaleBulk)bp.getFocusedElement(context);

	selezione.setOnlyForClose(
		Fondo_spesaBulk.getDateCalendar(null).get(java.util.Calendar.YEAR) !=
			 	selezione.getEsercizio().intValue());
	bp.setLastFondoSelected(selezione);

	return doApriSpese(context);
}

/**
 * Riapre il selezionatore dei fondi economali per una nuova selezione dopo la 
 * chiusura del pannello delle spese
 */
 
public Forward doChiusuraSpese(ActionContext context) {

	try {
		RicercaEconomaleBP bp = (RicercaEconomaleBP)context.getBusinessProcess();
		try {
			bp.setIterator(
				context,
				bp.createComponentSession().cercaFondi(context.getUserContext())
			);
		} catch(Throwable e) {
			throw new it.cnr.jada.action.BusinessProcessException(e);
		}
		return context.findDefaultForward();
	} catch (BusinessProcessException e) {
		return handleException(context, e);
	}
}
}
