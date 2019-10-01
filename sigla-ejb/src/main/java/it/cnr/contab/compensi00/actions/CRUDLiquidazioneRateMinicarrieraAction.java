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

package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.ejb.*;
import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.contab.compensi00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
/**
 * Insert the type's description here.
 * Creation date: (24/12/2002 11.38.50)
 * @author: Roberto Fantino
 */
public class CRUDLiquidazioneRateMinicarrieraAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * LiquidazioneRateMinicarrieraAction constructor comment.
 */
public CRUDLiquidazioneRateMinicarrieraAction() {
	super();
}
public Forward doBlankSearchFindElementoVoce(ActionContext context, Liquidazione_rate_minicarrieraBulk bulk) {

	if (bulk!=null)
		bulk.resetElementoVoce();
	return context.findDefaultForward();
}
public Forward doBlankSearchFindLineaAttivita(ActionContext context, Liquidazione_rate_minicarrieraBulk bulk) {

	if (bulk!=null)
		bulk.resetLineaAttivita();
	return context.findDefaultForward();
}
public Forward doBringBackSearchFindElementoVoce(ActionContext context, Liquidazione_rate_minicarrieraBulk bulk, Elemento_voceBulk elementoVoce) {

	if (elementoVoce!=null){
		bulk.setElementoVoce(elementoVoce);
		bulk.resetLineaAttivita();
	}
	return context.findDefaultForward();
}
public Forward doBringBackSearchFindLineaAttivita(ActionContext context, Liquidazione_rate_minicarrieraBulk bulk, WorkpackageBulk lineaAttivita) {

	try{
		if (lineaAttivita!=null){
			CRUDLiquidazioneRateMinicarrieraBP bp = (CRUDLiquidazioneRateMinicarrieraBP)getBusinessProcess(context);
			bulk.setLineaAttivita(lineaAttivita);
			bulk.setVoceF(bp.findVoceF(context));
		}
		return context.findDefaultForward();
	}catch(BusinessProcessException ex){
		return handleException(context, ex);
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
 *
 * Personalizzazione:
 *	- Eliminato il caso in cui viene trovato un solo record
 *	- Inserita la possibilita di selezione multipla
 */
public Forward doCerca(ActionContext context) {

	try{
		
		CRUDLiquidazioneRateMinicarrieraBP bp = (CRUDLiquidazioneRateMinicarrieraBP)getBusinessProcess(context);
		if (bp.isDirty())
			return openContinuePrompt(context,"doConfirmRicercaRate");

		return doConfirmRicercaRate(context,OptionBP.YES_BUTTON);

	}catch(BusinessProcessException ex){
		return handleException(context, ex);
	}
}
public Forward doConfirmRicercaRate(ActionContext context, int option){

	try {
		if (option == OptionBP.NO_BUTTON)
			return context.findDefaultForward();

		fillModel(context);
		CRUDLiquidazioneRateMinicarrieraBP bp = (CRUDLiquidazioneRateMinicarrieraBP)getBusinessProcess(context);
		OggettoBulk model = bp.getModel();
		it.cnr.jada.util.RemoteIterator ri = bp.find(context,null,model);
		if (ri == null || ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			bp.setMessage("La ricerca non ha fornito alcun risultato.");
			return context.findDefaultForward();
		} else {
			bp.setModel(context,model);
			SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
			nbp.setMultiSelection(true);
			nbp.setIterator(context,ri);
			nbp.setBulkInfo(bp.getSearchBulkInfo());
			
			nbp.setColumns(nbp.getBulkInfo().getColumnFieldPropertyDictionary("LIQUIDAZIONE_RATE_COLUMN_SET"));
			context.addHookForward("seleziona",this,"doRiportaSelezione");
			return context.addBusinessProcess(nbp);
		}

	}catch(FillException ex){
		return handleException(context, ex);
	}catch(BusinessProcessException ex){
		return handleException(context, ex);
	}catch(java.rmi.RemoteException ex){
		return handleException(context, ex);
	} 
}
/**
 * Esegue l'operazione di liquidazione Massiva delle Rate selezionate
 *
 */
public Forward doLiquidaRate(ActionContext context) {

	try{
		CRUDLiquidazioneRateMinicarrieraBP bp = (CRUDLiquidazioneRateMinicarrieraBP)getBusinessProcess(context);
		bp.doLiquidaRate(context);

		setMessage(context, FormBP.WARNING_MESSAGE, "Liquidazione completata. Controllare i files di log per maggiori dettagli.");
		return context.findDefaultForward();
	}catch(BusinessProcessException ex){
		return handleException(context, ex);
	}
}
/**
 * Gestisce un HookForward di ritorno da un risultato di una ricerca (SelezionatoreBP)
 *
 * Personalizzazione:
 *	- I record selezionati vengono inseriti in una collezione dell'oggetto Bulk
 */
public Forward doRiportaSelezione(ActionContext context)  throws java.rmi.RemoteException {

	try {
		HookForward caller = (HookForward)context.getCaller();
		CRUDLiquidazioneRateMinicarrieraBP bp = (CRUDLiquidazioneRateMinicarrieraBP)getBusinessProcess(context);
		Liquidazione_rate_minicarrieraBulk obj = (Liquidazione_rate_minicarrieraBulk)bp.getModel();

		java.util.List l = (java.util.List)caller.getParameter("selectedElements");
		if (l!=null && !l.isEmpty()){
			obj.setRate(l);
			bp.setDirty(true);
		}
		bp.setStatus(bp.SEARCH);
		bp.setTab("tab","tabRateDaLiquidare");

		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
}
