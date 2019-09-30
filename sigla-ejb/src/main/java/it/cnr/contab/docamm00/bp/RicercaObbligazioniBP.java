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

import it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 2:18:04 PM)
 * @author: Roberto Peli
 */
public class RicercaObbligazioniBP 
	extends it.cnr.jada.util.action.BulkBP
	implements it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP, it.cnr.contab.doccont00.bp.IValidaDocContBP {

	private int status = INSERT;
	private boolean bringBack = true;
/**
 * RicercaObbligazioniBP constructor comment.
 */
public RicercaObbligazioniBP() {
	super("Tr");
}
/**
 * RicercaObbligazioniBP constructor comment.
 * @param function java.lang.String
 */
public RicercaObbligazioniBP(String function) {
	super(function+"Tr");
}
/**
 * Invocato per creare un modello vuoto da usare su una nuova richiesta di ricerca.
 */
public Filtro_ricerca_obbligazioniVBulk createEmptyModelForSearch(ActionContext context) throws BusinessProcessException {

	try {
		return createNewBulk(context).initializeForSearch(this,context);
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public Filtro_ricerca_obbligazioniVBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		Filtro_ricerca_obbligazioniVBulk bulk = new Filtro_ricerca_obbligazioniVBulk();
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

	if (actionContext.getBusinessProcess().getParent() instanceof IDocumentoAmministrativoBP){
		IDocumentoAmministrativoBP bp = (IDocumentoAmministrativoBP)actionContext.getBusinessProcess().getParent();
		return bp.findObbligazioniAttributes(actionContext, clauses, bulk, context, property);
	}

	return null;
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
public it.cnr.jada.util.RemoteIterator findObbligazioni(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.bulk.OggettoBulk context) throws it.cnr.jada.action.BusinessProcessException{

	if (actionContext.getBusinessProcess().getParent() instanceof IDocumentoAmministrativoBP){
		IDocumentoAmministrativoBP bp = (IDocumentoAmministrativoBP)actionContext.getBusinessProcess().getParent();
		return bp.findObbligazioni(actionContext.getUserContext(), (Filtro_ricerca_obbligazioniVBulk)getModel());
	}

	return null;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'componentSessioneName'
 *
 * @return Il valore della proprietà 'componentSessioneName'
 */
public java.lang.String getComponentSessioneName() {

	if ( getParent() instanceof it.cnr.contab.doccont00.bp.IValidaDocContBP )
		return ((it.cnr.contab.doccont00.bp.IValidaDocContBP)getParent()).getComponentSessioneName();
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (5/22/2002 10:31:47 AM)
 */
public it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi getDefferedUpdateSaldiBulk() {
	return getDefferedUpdateSaldiParentBP().getDefferedUpdateSaldiBulk();
}
/**
 * Insert the method's description here.
 * Creation date: (5/22/2002 10:31:47 AM)
 */
public it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP getDefferedUpdateSaldiParentBP() {

	if (getParent() != null)
		return ((IDefferedUpdateSaldiBP)getParent()).getDefferedUpdateSaldiParentBP();
	return null;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'docAmmModel'
 *
 * @return Il valore della proprietà 'docAmmModel'
 */
public it.cnr.jada.bulk.OggettoBulk getDocAmmModel() {

	if ( getParent() instanceof it.cnr.contab.doccont00.bp.IValidaDocContBP )
		return ((it.cnr.contab.doccont00.bp.IValidaDocContBP)getParent()).getDocAmmModel();

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
public void setModel(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
	if (oggettobulk!=null && oggettobulk instanceof Filtro_ricerca_obbligazioniVBulk &&
		((Filtro_ricerca_obbligazioniVBulk)oggettobulk).getTipo_obbligazione()==null)
			((Filtro_ricerca_obbligazioniVBulk)oggettobulk).setTipo_obbligazione(ObbligazioneBulk.TIPO_COMPETENZA);
	super.setModel(actioncontext, oggettobulk);
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
public boolean isEsercizioOriObbligazioneVisible() {
	return (((Filtro_ricerca_obbligazioniVBulk)getModel()).getTipo_obbligazione() == null || 
			ObbligazioneBulk.TIPO_RESIDUO_PROPRIO.equals(((Filtro_ricerca_obbligazioniVBulk)getModel()).getTipo_obbligazione()) || 
			ObbligazioneBulk.TIPO_RESIDUO_IMPROPRIO.equals(((Filtro_ricerca_obbligazioniVBulk)getModel()).getTipo_obbligazione()));
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
public void validaObbligazione(ActionContext actionContext, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.action.BusinessProcessException {

	if (actionContext.getBusinessProcess().getParent() instanceof IDocumentoAmministrativoBP){
		IDocumentoAmministrativoBP bp = (IDocumentoAmministrativoBP)actionContext.getBusinessProcess().getParent();
		bp.validaObbligazionePerDocAmm(actionContext, bulk);
	}
}
}
