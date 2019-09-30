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

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.doccont00.bp.ListaAccertamentiBP;
import it.cnr.contab.docamm00.bp.*;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.action.*;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 2:36:05 PM)
 * @author: Roberto Peli
 */
public class ListaDocumentiAmministrativiAction extends it.cnr.jada.util.action.BulkAction {
/**
 * RicercaObbligazioniAction constructor comment.
 */
public ListaDocumentiAmministrativiAction() {
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
public TerzoBulk basicDoBlankSearchSoggetto(ActionContext context) {

	TerzoBulk soggetto = new it.cnr.contab.anagraf00.core.bulk.TerzoBulk();
	soggetto.setAnagrafico(new it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk());
	return soggetto;
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
protected void completaSoggetto(ActionContext context) 
	throws	BusinessProcessException,
			java.rmi.RemoteException,
			java.lang.reflect.InvocationTargetException,
			java.beans.IntrospectionException,
			it.cnr.jada.comp.ComponentException,
			it.cnr.jada.bulk.ValidationException {

	ListaDocumentiAmministrativiBP bp = (ListaDocumentiAmministrativiBP)context.getBusinessProcess();
	Filtro_ricerca_doc_ammVBulk filtro = (Filtro_ricerca_doc_ammVBulk)bp.getModel();
	if (filtro.getSoggetto() != null && 
		filtro.getSoggetto().getCrudStatus() != OggettoBulk.NORMAL) {

		IDocumentoAmministrativoBP docAmmBP = (IDocumentoAmministrativoBP)getBusinessProcessForDocAmm(context, filtro.getInstance());
		if (!(docAmmBP instanceof IGenericSearchDocAmmBP) || ((IGenericSearchDocAmmBP)docAmmBP).getPropertyForGenericSearch() == null) {
			filtro.setSoggetto(null);
			throw new it.cnr.jada.comp.ApplicationException("Il soggetto non è una clausola valida per il gruppo selezionato!");
		}
		
		it.cnr.jada.bulk.FieldProperty p = BulkInfo.getBulkInfo(filtro.getInstance().getClass()).getFieldProperty(((IGenericSearchDocAmmBP)docAmmBP).getPropertyForGenericSearch());
		it.cnr.jada.util.RemoteIterator i = bp.find(context,null,filtro.getSoggetto(),filtro,p.getProperty());
		try {
			int count = i.countElements();
			if (count == 0)
				throw new it.cnr.jada.bulk.ValidationException("La ricerca non ha fornito alcun risultato per il terzo!");
			else if (count == 1)
				doBringBackSearchSoggetto(context, filtro,(TerzoBulk)i.nextElement());
			else
				throw new ValidationException("La ricerca ha fornito più di un risultato per il terzo!");
		} finally {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,i);
		}
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
public Forward doBlankSearchSoggetto(ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) {

	try {
		if (bulk != null) {
			Filtro_ricerca_doc_ammVBulk filtro = (Filtro_ricerca_doc_ammVBulk)bulk;
			doBringBackSearchSoggetto(
							context,
							filtro,
							basicDoBlankSearchSoggetto(context));
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Riporta il terzo selezionato nel filtro di ricerca
 */
public Forward doBringBackSearchSoggetto(ActionContext context,
	Filtro_ricerca_doc_ammVBulk filtro,
	TerzoBulk soggettoTrovato) 
	throws java.rmi.RemoteException {

	filtro.setSoggetto(soggettoTrovato);
	IDocumentoAmministrativoBulk docAmm = filtro.getInstance();
	if (docAmm !=null) {
		try {
			IDocumentoAmministrativoBP docAmmBP = getBusinessProcessForDocAmm(context, docAmm);
			if (docAmmBP instanceof IGenericSearchDocAmmBP && ((IGenericSearchDocAmmBP)docAmmBP).getPropertyForGenericSearch() != null) {
				IGenericSearchDocAmmBP genericSearchBP = (IGenericSearchDocAmmBP)docAmmBP;
				Introspector.invoke(
								docAmm,
								Introspector.buildMetodName("set", genericSearchBP.getPropertyForGenericSearch()),
								soggettoTrovato);
			}
		} catch (Throwable e) {
			return handleException(context, e);
		}
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
		fillModel(context);
		completaSoggetto(context);
		ListaDocumentiAmministrativiBP bp = (ListaDocumentiAmministrativiBP)context.getBusinessProcess();
		Filtro_ricerca_doc_ammVBulk filtro = (Filtro_ricerca_doc_ammVBulk)bp.getModel();
		OggettoBulk instance = (OggettoBulk)filtro.getInstance();
		it.cnr.jada.util.RemoteIterator ri = bp.find(context, null, instance);
		IDocumentoAmministrativoBP docAmmBP = null;
		if (ri == null || ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
			bp.setMessage("La ricerca non ha fornito alcun risultato.");
			return context.findDefaultForward();
		/*}
		else if (ri.countElements() == 1) {
			IDocumentoAmministrativoBulk selezione = (IDocumentoAmministrativoBulk)ri.nextElement();
			if (ri instanceof javax.ejb.EJBObject)
				((javax.ejb.EJBObject)ri).remove();
			Object[] docAmmSpecs = getDocAmmBP(context, selezione);
			docAmmBP = (IDocumentoAmministrativoBP)docAmmSpecs[0];
			selezione = (IDocumentoAmministrativoBulk)docAmmSpecs[1];
				
			((CRUDBP)docAmmBP).setMessage("La ricerca ha fornito un solo risultato.");
			((CRUDBP)docAmmBP).edit(context, (OggettoBulk)selezione);
			return context.addBusinessProcess((BusinessProcess)docAmmBP);*/
		} else {
			bp.setModel(context,filtro);
			SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
			nbp.setIterator(context,ri);
			BulkInfo bulkInfo = BulkInfo.getBulkInfo(filtro.getInstance().getClass());
			nbp.setBulkInfo(bulkInfo);
			docAmmBP = getBusinessProcessForDocAmm(context, filtro.getInstance());
			if (docAmmBP instanceof IGenericSearchDocAmmBP) {
				String columnsetName = ((IGenericSearchDocAmmBP)docAmmBP).getColumnsetForGenericSearch();
				if (columnsetName != null)
					nbp.setColumns(bulkInfo.getColumnFieldPropertyDictionary(columnsetName));
			}
			context.addHookForward("seleziona",this,"doRiportaSelezione");
			return context.addBusinessProcess(nbp);
		}
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce un comando di chiusura
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
		ListaDocumentiAmministrativiBP bp = (ListaDocumentiAmministrativiBP)context.getBusinessProcess();
		if (option == OptionBP.YES_BUTTON)
			bp.resetForSearch(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
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
public Forward doFreeSearchSoggetto(ActionContext context) {
	try {
		ListaDocumentiAmministrativiBP bp = (ListaDocumentiAmministrativiBP)context.getBusinessProcess();
		Filtro_ricerca_doc_ammVBulk filtro = (Filtro_ricerca_doc_ammVBulk)bp.getModel();
		IDocumentoAmministrativoBulk docAmm = filtro.getInstance();
		IDocumentoAmministrativoBP docAmmBP = getBusinessProcessForDocAmm(context, docAmm);
		
		if (!(docAmmBP instanceof IGenericSearchDocAmmBP) || ((IGenericSearchDocAmmBP)docAmmBP).getPropertyForGenericSearch() == null) {
			filtro.setSoggetto(null);
			throw new it.cnr.jada.comp.ApplicationException("Il soggetto non è una clausola valida per il gruppo selezionato!");
		}

		IGenericSearchDocAmmBP docAmmGenericSearchBP = (IGenericSearchDocAmmBP)docAmmBP;
		String property = docAmmGenericSearchBP.getPropertyForGenericSearch();
		it.cnr.contab.anagraf00.core.bulk.TerzoBulk soggetto = filtro.getSoggetto();
		if (soggetto == null)
			soggetto = basicDoBlankSearchSoggetto(context);

		it.cnr.jada.util.action.RicercaLiberaBP fsbp = (it.cnr.jada.util.action.RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
		fsbp.setSearchProvider(bp.getSearchProvider((OggettoBulk)docAmm,property));
		fsbp.setPrototype(soggetto);
		context.addHookForward("seleziona",this,"doBringBackSearchResult");
		HookForward hook = (HookForward)context.findForward("seleziona");
		hook.addParameter("field",getFormField(context,"main.soggetto"));
		return context.addBusinessProcess(fsbp);
	} catch(Exception ex) {
		return handleException(context,ex);
	}		
}
/**
 * Gestisce un comando "nuova ricerca".
 */
public Forward doNuovaRicerca(ActionContext context) {
	try {
		ListaDocumentiAmministrativiBP bp = (ListaDocumentiAmministrativiBP)context.getBusinessProcess();
		fillModel(context);
		if (bp.isDirty())
			return openContinuePrompt(context, "doConfermaNuovaRicerca");
		return doConfermaNuovaRicerca(context,OptionBP.YES_BUTTON);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Aggiorna le opzioni sul cambiamento del tipo di doc amm
 */
public Forward doOnGroupChange(ActionContext context) {

	try {
		fillModel(context);
		ListaDocumentiAmministrativiBP bp = (ListaDocumentiAmministrativiBP)context.getBusinessProcess();
		Filtro_ricerca_doc_ammVBulk filtro = (Filtro_ricerca_doc_ammVBulk)bp.getModel();
		filtro.updateOptions(context);
		filtro.setInstance(filtro.createInstance(bp, context));
		doBringBackSearchSoggetto(context, filtro, filtro.getSoggetto());
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce una richiesta di cambiamento del tipo doc amm da ricercare.
 *
 */
public Forward doOnOptionChange(ActionContext context) {

	try {
		fillModel(context);
		ListaDocumentiAmministrativiBP bp = (ListaDocumentiAmministrativiBP)context.getBusinessProcess();
		Filtro_ricerca_doc_ammVBulk filtro = (Filtro_ricerca_doc_ammVBulk)bp.getModel();
		filtro.setInstance(filtro.createInstance(bp, context));
		doBringBackSearchSoggetto(context, filtro, filtro.getSoggetto());
		return context.findDefaultForward();
	} catch(Throwable e) {
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
		IDocumentoAmministrativoBulk selezione = (IDocumentoAmministrativoBulk)caller.getParameter("focusedElement");
		if (selezione != null) {
			ListaDocumentiAmministrativiBP bp = (ListaDocumentiAmministrativiBP)context.getBusinessProcess();
			Object[] docAmmSpecs = getDocAmmBP(context, selezione);
			IDocumentoAmministrativoBP docAmmBP = (IDocumentoAmministrativoBP)docAmmSpecs[0];
			selezione = (IDocumentoAmministrativoBulk)docAmmSpecs[1];

			((CRUDBP)docAmmBP).edit(context, (OggettoBulk)selezione);

			return context.addBusinessProcess((BusinessProcess)docAmmBP);
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
public Forward doSearchSoggetto(ActionContext context) {
	try {
		ListaDocumentiAmministrativiBP bp = (ListaDocumentiAmministrativiBP)context.getBusinessProcess();
		Filtro_ricerca_doc_ammVBulk filtro = (Filtro_ricerca_doc_ammVBulk)bp.getModel();
		IDocumentoAmministrativoBulk docAmm = filtro.getInstance();
		IDocumentoAmministrativoBP docAmmBP = getBusinessProcessForDocAmm(context, docAmm);
		
		if (!(docAmmBP instanceof IGenericSearchDocAmmBP) || ((IGenericSearchDocAmmBP)docAmmBP).getPropertyForGenericSearch() == null) {
			filtro.setSoggetto(null);
			throw new it.cnr.jada.comp.ApplicationException("Il soggetto non è una clausola valida per il gruppo selezionato!");
		}

		IGenericSearchDocAmmBP docAmmGenericSearchBP = (IGenericSearchDocAmmBP)docAmmBP;
		String property = docAmmGenericSearchBP.getPropertyForGenericSearch();
		it.cnr.contab.anagraf00.core.bulk.TerzoBulk soggetto = filtro.getSoggetto();
		if (soggetto == null)
			soggetto = basicDoBlankSearchSoggetto(context);
		return selectFromSearchResult(
							context,
							getFormField(context,"main.soggetto"),
							bp.find(
								context,
								null,
								soggetto,
								bp.getModel(),
								property),
							"default");
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Ottiene un'istanza dell'oggetto reale partendo al DocumentoAmministrativoGenerico
 * selezionato e relativo business process responsabile.
 */
protected Object[] getBPAndRealInstanceFor(
		ActionContext context,
		IDocumentoAmministrativoGenericoBulk docAmmGenerico) 
		throws it.cnr.jada.comp.ComponentException,
				BusinessProcessException,
				java.rmi.RemoteException {

	if (docAmmGenerico == null) return null;
	IDocumentoAmministrativoBulk docAmm = docAmmGenerico.getSpecializedInstance();
	if (docAmm == null)
		return null;
	IDocumentoAmministrativoBP docAmmBP = getBusinessProcessForDocAmm(context, docAmm);
	it.cnr.jada.util.RemoteIterator ri = null;
	try {
		ri = docAmmBP.createComponentSession().cerca(
											context.getUserContext(), 
											null,
											(OggettoBulk)docAmm);
		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
		docAmm = (IDocumentoAmministrativoBulk)ri.nextElement();
	} finally {
		if (ri != null)
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
	}
	if (docAmm == null)
		throw new it.cnr.jada.comp.ApplicationException("Impossibile caricare il documento amministrativo selezionato!");
	return new Object[] { docAmmBP, docAmm };
}
/**
 * Ottiene il business process responsabile del documento amministativo docAmm.
 */
protected IDocumentoAmministrativoBP getBusinessProcessForDocAmm(
		ActionContext context,
		IDocumentoAmministrativoBulk docAmm) 
		throws BusinessProcessException {

	if (docAmm == null) return null;

	ListaDocumentiAmministrativiBP bp = (ListaDocumentiAmministrativiBP)context.getBusinessProcess();
	return bp.getBusinessProcessForDocAmm(context, docAmm);
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
protected Object[] getDocAmmBP(
	ActionContext context,
	IDocumentoAmministrativoBulk docAmm) 
	throws BusinessProcessException,
			it.cnr.jada.comp.ComponentException,
			java.rmi.RemoteException {

	if (docAmm instanceof IDocumentoAmministrativoGenericoBulk)
		return getBPAndRealInstanceFor(
								context,
								(IDocumentoAmministrativoGenericoBulk)docAmm);

	Object[] docAmmSpecs = new Object[2];
	docAmmSpecs[0] = getBusinessProcessForDocAmm(context, docAmm);
	docAmmSpecs[1] = docAmm;
	
	return docAmmSpecs;
}
}
