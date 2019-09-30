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

package it.cnr.contab.docamm00.bp;

import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;

import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_ammVBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.ejb.EJBCommonServices;


public class ListaDocumentiAmministrativiBP extends BulkBP {

	private int status = INSERT;
	private java.util.HashMap bpCache = null;
/**
 * ListaDocumentiAmministrativiBP constructor comment.
 */
public ListaDocumentiAmministrativiBP() {
	super();
	resetBpCache();
}
/**
 * ListaDocumentiAmministrativiBP constructor comment.
 * @param function java.lang.String
 */
public ListaDocumentiAmministrativiBP(String function) {
	super(function);
	resetBpCache();
}
/**
 * Insert the method's description here.
 * Creation date: (3/28/2002 2:35:20 PM)
 * @return java.util.HashMap
 */
public void addToBpCache(IDocumentoAmministrativoBP bp) {
	
	bpCache.put(
			((it.cnr.jada.util.action.CRUDBP)bp).getName(),
			bp);
}
/**
 * Effettua una operazione di ricerca per un attributo di un modello.
 * @param actionContext contesto dell'azione in corso
 * @param clauses Albero di clausole da utilizzare per la ricerca
 * @param bulk prototipo del modello di cui si effettua la ricerca
 * @param context modello che fa da contesto alla ricerca (il modello del FormController padre del
 * 			controller che ha scatenato la ricerca)
 * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
 */
public it.cnr.jada.ejb.CRUDComponentSession createComponentSession(ActionContext actionContext) throws BusinessProcessException {

	Filtro_ricerca_doc_ammVBulk filtro = (Filtro_ricerca_doc_ammVBulk)getModel();
	String bpName = filtro.getManagerName();
	String bpOptions = filtro.getManagerOptions();
	if (bpName == null) return null;
	if (bpOptions == null) bpOptions = "VTh";
	IDocumentoAmministrativoBP bp = (IDocumentoAmministrativoBP)actionContext.createBusinessProcess(bpName, new Object[] { bpOptions });
	if (bp == null) return null;
	if (bp instanceof IGenericSearchDocAmmBP)
		return ((IGenericSearchDocAmmBP)bp).initializeModelForGenericSearch(this, actionContext);
	return bp.createComponentSession();
}
/**
 * Invocato per creare un modello vuoto da usare su una nuova richiesta di ricerca.
 */
public Filtro_ricerca_doc_ammVBulk createEmptyModelForSearch(ActionContext context) throws BusinessProcessException {

	try {
		return createNewBulk(context).initializeForSearch(this,context);
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public Filtro_ricerca_doc_ammVBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		Filtro_ricerca_doc_ammVBulk bulk = new Filtro_ricerca_doc_ammVBulk();
		bulk.setUser(context.getUserInfo().getUserid());
		return bulk;
	} catch(Exception e) {
		throw handleException(e);
	}
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() {
	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[2];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.search");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.startSearch");
	return toolbar;
}
/**
 * Effettua una operazione di ricerca per un attributo di un modello.
 * @param actionContext contesto dell'azione in corso
 * @param clauses Albero di clausole da utilizzare per la ricerca
 * @param bulk prototipo del modello di cui si effettua la ricerca
 * @param context modello che fa da contesto alla ricerca (il modello del FormController padre del
 * 			controller che ha scatenato la ricerca)
 * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
 */
public it.cnr.jada.util.RemoteIterator find(ActionContext actionContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.action.BusinessProcessException {

	try {
		it.cnr.jada.ejb.CRUDComponentSession cs = createComponentSession(actionContext);
		if (cs == null) return null;
		return EJBCommonServices.openRemoteIterator(
											actionContext, 
											cs.cerca(actionContext.getUserContext(),clauses,bulk));
	} catch (it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch (java.rmi.RemoteException e) {
		throw handleException(e);
	}
}
/**
 * Effettua una operazione di ricerca per un attributo di un modello.
 * @param actionContext contesto dell'azione in corso
 * @param clauses Albero di clausole da utilizzare per la ricerca
 * @param bulk prototipo del modello di cui si effettua la ricerca
 * @param context modello che fa da contesto alla ricerca (il modello del FormController padre del
 * 			controller che ha scatenato la ricerca)
 * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
 */
public it.cnr.jada.util.RemoteIterator find(ActionContext actionContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk,OggettoBulk context,String property) throws it.cnr.jada.action.BusinessProcessException {

	try {
		it.cnr.jada.ejb.CRUDComponentSession cs = createComponentSession(actionContext);
		if (cs == null) return null;
		return EJBCommonServices.openRemoteIterator(
							actionContext,
							cs.cerca(
								actionContext.getUserContext(),
								clauses,
								bulk, 
								getModel(),
								//(OggettoBulk)((Filtro_ricerca_doc_ammVBulk)getModel()).getInstance(),
								property));
	} catch (it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch (java.rmi.RemoteException e) {
		throw handleException(e);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (3/28/2002 2:35:20 PM)
 * @return java.util.HashMap
 */
public java.util.HashMap getBpCache() {
	return bpCache;
}
/**
 * Ottiene il business process responsabile del documento amministativo docAmm.
 */
public IDocumentoAmministrativoBP getBusinessProcessForDocAmm(
		ActionContext context,
		IDocumentoAmministrativoBulk docAmm) 
		throws BusinessProcessException {

	if (docAmm == null) return null;

	IDocumentoAmministrativoBP docAmmBP = (IDocumentoAmministrativoBP)getBpCache().get(docAmm.getManagerName());
	if (docAmmBP == null) {
		docAmmBP = (IDocumentoAmministrativoBP)context.createBusinessProcess(
													docAmm.getManagerName(), 
													new Object[] { docAmm.getManagerOptions() });
		addToBpCache(docAmmBP);
	}
	return docAmmBP;
}

public java.util.Dictionary getSearchResultColumns() {
	return ((OggettoBulk)((Filtro_ricerca_doc_ammVBulk)getModel()).getInstance()).getBulkInfo().getColumnFieldPropertyDictionary();
}
public int getStatus() {
	return status;
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);
	setStatus(SEARCH);
	resetForSearch(context);
}
public boolean isBringbackButtonEnabled() {
	return isSearching();
}
public boolean isSearchButtonHidden() {
	return isSearching();
}
public boolean isSearching() {
	return status == SEARCH;
}
public boolean isStartSearchButtonHidden() {
	return !isSearching();
}
/**
 * Insert the method's description here.
 * Creation date: (3/28/2002 2:35:20 PM)
 * @return java.util.HashMap
 */
public void removeFromBpCache(IDocumentoAmministrativoBP bp) {
	
	bpCache.remove(((it.cnr.jada.util.action.CRUDBP)bp).getName());
}
/**
 * Insert the method's description here.
 * Creation date: (3/28/2002 2:35:20 PM)
 * @param newBpCache java.util.HashMap
 */
public void resetBpCache() {
	bpCache = new java.util.HashMap();
}
/**
 * Inzializza il ricevente nello stato di SEARCH.
 */
public void resetForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		setModel(context,createEmptyModelForSearch(context));
		setStatus(SEARCH);
		setDirty(false);
		resetChildren(context);
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
public void setStatus(int newStatus) {
	status = newStatus;
}
}
