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

import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 2:18:04 PM)
 * @author: Roberto Peli
 */
public class RicercaAccertamentiBP 
	extends it.cnr.jada.util.action.BulkBP
	implements IDefferedUpdateSaldiBP {

	private int status = INSERT;
	private boolean bringBack = true;
/**
 * RicercaObbligazioniBP constructor comment.
 */
public RicercaAccertamentiBP() {
	super("Tr");
}
/**
 * RicercaObbligazioniBP constructor comment.
 * @param function java.lang.String
 */
public RicercaAccertamentiBP(String function) {
	super(function+"Tr");
}
/**
 * Invocato per creare un modello vuoto da usare su una nuova richiesta di ricerca.
 */
public Filtro_ricerca_accertamentiVBulk createEmptyModelForSearch(ActionContext context) throws BusinessProcessException {

	try {
		return createNewBulk(context).initializeForSearch(this,context);
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public Filtro_ricerca_accertamentiVBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		Filtro_ricerca_accertamentiVBulk bulk = new Filtro_ricerca_accertamentiVBulk();
		bulk.setUser(context.getUserInfo().getUserid());
		return bulk;
	} catch(Exception e) {
		throw handleException(e);
	}
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() {
	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[3];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.search");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.startSearch");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.bringBack");
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
public it.cnr.jada.util.RemoteIterator find(ActionContext actionContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,it.cnr.jada.bulk.OggettoBulk bulk,it.cnr.jada.bulk.OggettoBulk context,String property) throws it.cnr.jada.action.BusinessProcessException {

	try {
		it.cnr.jada.util.RemoteIterator ri = null;
		if (bulk instanceof Fattura_attivaBulk ){
			FatturaAttivaSingolaComponentSession component = (FatturaAttivaSingolaComponentSession)actionContext.getBusinessProcess().createComponentSession("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession", FatturaAttivaSingolaComponentSession.class);
			ri = component.cerca(actionContext.getUserContext(),clauses,bulk,context,property);
		}
		else {
			it.cnr.contab.docamm00.ejb.DocumentoGenericoComponentSession component = (it.cnr.contab.docamm00.ejb.DocumentoGenericoComponentSession)actionContext.getBusinessProcess().createComponentSession("CNRDOCAMM00_EJB_DocumentoGenericoComponentSession", it.cnr.contab.docamm00.ejb.DocumentoGenericoComponentSession.class);
			ri = component.cerca(actionContext.getUserContext(),clauses,bulk,context,property);
		}
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext, ri);
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
public it.cnr.jada.util.RemoteIterator findAccertamenti(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.bulk.OggettoBulk context) throws it.cnr.jada.action.BusinessProcessException {

	try {
		if ((actionContext.getBusinessProcess().getParent() instanceof CRUDFatturaAttivaIBP )||
		    (actionContext.getBusinessProcess().getParent() instanceof CRUDFatturaPassivaBP )){
			FatturaAttivaSingolaComponentSession component = (FatturaAttivaSingolaComponentSession)actionContext.getBusinessProcess().getParent().createComponentSession("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession", FatturaAttivaSingolaComponentSession.class);
			return component.cercaAccertamenti(actionContext.getUserContext(), (Filtro_ricerca_accertamentiVBulk)getModel());
		}
		else{
			it.cnr.contab.docamm00.ejb.DocumentoGenericoComponentSession component = (it.cnr.contab.docamm00.ejb.DocumentoGenericoComponentSession)actionContext.getBusinessProcess().getParent().createComponentSession("CNRDOCAMM00_EJB_DocumentoGenericoComponentSession", it.cnr.contab.docamm00.ejb.DocumentoGenericoComponentSession.class);
			return component.cercaAccertamenti(actionContext.getUserContext(), (Filtro_ricerca_accertamentiVBulk)getModel());
		}
		
	} catch (it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch (java.rmi.RemoteException e) {
		throw handleException(e);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (5/22/2002 10:50:25 AM)
 */
public it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi getDefferedUpdateSaldiBulk() {
	return getDefferedUpdateSaldiParentBP().getDefferedUpdateSaldiBulk();
}
/**
 * Insert the method's description here.
 * Creation date: (5/22/2002 10:50:25 AM)
 */
public it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP getDefferedUpdateSaldiParentBP() {

	if (getParent() != null)
		return ((IDefferedUpdateSaldiBP)getParent()).getDefferedUpdateSaldiParentBP();
	return null;
}

public java.util.Dictionary getSearchResultColumns() {
	return getModel().getBulkInfo().getColumnFieldPropertyDictionary();
}
public int getStatus() {
	return status;
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);
	setStatus(SEARCH);
}
public boolean isBringBack() {
	return bringBack;
}
public boolean isBringbackButtonEnabled() {
	return isSearching();
}
public boolean isBringbackButtonHidden() {
	return !isBringBack();
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
public void setBringBack(boolean newBringBack) {
	bringBack = newBringBack;
}
public void setStatus(int newStatus) {
	status = newStatus;
}
}
