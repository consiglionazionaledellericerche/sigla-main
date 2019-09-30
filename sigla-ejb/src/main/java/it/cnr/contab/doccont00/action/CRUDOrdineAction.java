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

package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.ordine.bulk.*;
import it.cnr.contab.doccont00.bp.*;
import it.cnr.contab.doccont00.ejb.*;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.jada.action.*;

/**
 * <!-- @TODO: da completare -->
 */

public class CRUDOrdineAction extends it.cnr.jada.util.action.CRUDAction {
public CRUDOrdineAction() {
	super();
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doBringBackAddBanca(ActionContext context){

	HookForward caller = (HookForward)context.getCaller();
	OrdineBulk ordine= (OrdineBulk)((CRUDOrdineBP)getBusinessProcess(context)).getModel();
	it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = (it.cnr.contab.anagraf00.core.bulk.BancaBulk)caller.getParameter("focusedElement");

	ordine.setBanca(banca);

	return context.findDefaultForward();
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doOnModalitaPagamentoChange(ActionContext context) {

	try {
		fillModel(context);
		CRUDOrdineBP bp = (CRUDOrdineBP)getBusinessProcess(context);
		OrdineBulk ordine = (OrdineBulk)bp.getModel();
		if (ordine.getModalitaPagamento() != null) {
			OrdineComponentSession component = (OrdineComponentSession)bp.createComponentSession();
			java.util.List coll = component.findListabanche(context.getUserContext(), ordine);

			//	Assegno di default la prima banca tra quelle selezionate
			if(coll == null || coll.isEmpty()){
				ordine.setBanca(null);
				throw new MessageToUser("La modalità di pagamento selezionata non ha banche associate.\nNon sarà possibile salvare l'ordine.");
			}
			else
				ordine.setBanca((it.cnr.contab.anagraf00.core.bulk.BancaBulk)new java.util.Vector(coll).firstElement());
		}else
			ordine.setBanca(null);

	}catch (Throwable t) {
		return handleException(context, t);
	}
	return context.findDefaultForward();
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doSearchListabanche(ActionContext context) {
	
	try {
		fillModel(context);
		CRUDOrdineBP bp = (CRUDOrdineBP)getBusinessProcess(context);
		OrdineBulk ordine = (OrdineBulk)bp.getModel();
		OrdineComponentSession comp = (OrdineComponentSession)bp.createComponentSession();
		java.util.List list = comp.findListabanche(context.getUserContext(),(OrdineBulk)bp.getModel());

		if (list.isEmpty())
			throw new it.cnr.jada.comp.ApplicationException("Nessuna banca da selezionare!");

		it.cnr.jada.util.action.SelezionatoreListaBP slbp = (it.cnr.jada.util.action.SelezionatoreListaBP) select(
			context,
			new it.cnr.jada.util.ListRemoteIterator(list),
			it.cnr.jada.bulk.BulkInfo.getBulkInfo(it.cnr.contab.anagraf00.core.bulk.BancaBulk.class),
			ordine.getModalitaPagamento().getTiPagamentoColumnSet(),
			"doBringBackAddBanca");

		return slbp;
	}catch(Throwable ex){
		return handleException(context, ex);
	}
}
/**
 * Gestisce la Stampa di un Ordine.
 *
 * @param context <code>ActionContext</code> in uso.
 * @return <code>Forward</code>
 */
public Forward doStampaOrdine(ActionContext context) {

	try {
		fillModel(context);
		CRUDOrdineBP bp = (CRUDOrdineBP)getBusinessProcess(context);
		OrdineBulk ordine = (OrdineBulk)bp.getModel();

		ParametricPrintBP ppbp = (ParametricPrintBP)context.createBusinessProcess("StampaOrdineBP");
		Stampa_ordineBulk stampa = (Stampa_ordineBulk)ppbp.createNewBulk(context);
		if (ordine != null && ordine.getPg_ordine() != null){
			stampa.setPgInizio(new Integer(ordine.getPg_ordine().intValue()));
			stampa.setPgFine(new Integer(ordine.getPg_ordine().intValue()));
		}
		ppbp.setModel(context, stampa);
		

		return context.addBusinessProcess(ppbp)	;

	}catch (Throwable t) {
		return handleException(context, t);
	}
}
}
