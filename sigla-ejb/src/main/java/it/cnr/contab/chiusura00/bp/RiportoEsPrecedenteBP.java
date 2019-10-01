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

package it.cnr.contab.chiusura00.bp;

import it.cnr.contab.chiusura00.ejb.*;
import it.cnr.contab.chiusura00.bulk.*;
import it.cnr.contab.doccont00.intcass.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.util.*;

/**
 * BP che gestisce il riporto indietro massivo di documenti contabili dall'esercizio successivo
 */

public class RiportoEsPrecedenteBP extends it.cnr.jada.util.action.BulkBP implements it.cnr.jada.util.action.SelectionListener
{

	private String componentSessioneName;
	private Class bulkClass;
	private BulkInfo bulkInfo;
	private java.lang.Class searchBulkClass;
	private it.cnr.jada.bulk.BulkInfo searchBulkInfo;
	private java.lang.String searchResultColumnSet;

	
	
public RiportoEsPrecedenteBP() {
		super("Tn");
//		super();
}
public RiportoEsPrecedenteBP(String function) {
	super(function+"Tn");
//	super(function);	
}
/**
 * Ricerca di documenti contabili idonei al riporto indietro
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @param model	
 * @return 
 * @throws BusinessProcessException	
 */
public it.cnr.jada.util.RemoteIterator cercaDocDaRiportare(ActionContext context,V_obb_acc_xxxBulk model) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		RicercaDocContComponentSession sessione = (RicercaDocContComponentSession) createComponentSession();
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,sessione.cercaPerRiportaIndietro(context.getUserContext(),model));
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * annulla la selezione dei doc. contabili effettuata fino ad ora
 */
public void clearSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	try 
	{
		((RicercaDocContComponentSession)createComponentSession()).clearSelectionPerRiportaIndietro(context.getUserContext(), (V_obb_acc_xxxBulk)getModel());

	} catch(Exception e) {
		throw handleException(e);
	} 
}
/**
 * richiama la stored procedure che effettua il riporto massivo sui documenti
 * selezionati dall'utente
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @throws BusinessProcessException	
 */
public void confermaRiporto(ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		RicercaDocContComponentSession sessione = (RicercaDocContComponentSession) createComponentSession();
		sessione.callRiportoPrevEsDocCont(context.getUserContext(), ((V_obb_acc_xxxBulk)getModel()).getPg_call());
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
 */
public RicercaComponentSession createComponentSession() throws BusinessProcessException {
	return (RicercaComponentSession)createComponentSession(componentSessioneName,RicercaComponentSession.class);
}
/**
 * crea una istanza di V_obb_acc_xxxBulk per effettuare la ricerca dei doc. contabili 
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return 
 * @throws BusinessProcessException	
 */
public OggettoBulk createEmptyModelForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try
	{
		V_obb_acc_xxxBulk doc = new V_obb_acc_xxxBulk();
		return ((it.cnr.contab.chiusura00.ejb.RicercaDocContComponentSession)createComponentSession()).inizializzaBulkPerRicerca( context.getUserContext(), doc );
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}		
}
/**
 * deselectAll method comment.
 */
public void deselectAll(it.cnr.jada.action.ActionContext context) {}
/**
 * non usato
 * 
 *
 */
public it.cnr.jada.util.RemoteIterator find(ActionContext context,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk model) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,createComponentSession().cerca(context.getUserContext(),clauses,model));
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * non usato
 * 
 *
 */

public RemoteIterator find(ActionContext actionContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk,OggettoBulk context,String property) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
	} catch(Exception e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
/**
 * Restituisce il valore della proprietà 'bulkClass'
 *
 * @return Il valore della proprietà 'bulkClass'
 */
public java.lang.Class getBulkClass() {
	return bulkClass;
}
/**
 * Restituisce il valore della proprietà 'bulkInfo'
 *
 * @return Il valore della proprietà 'bulkInfo'
 */

public it.cnr.jada.bulk.BulkInfo getBulkInfo() {
	return bulkInfo;
}
/**
 * Restituisce il valore della proprietà 'componentSessioneName'
 *
 * @return Il valore della proprietà 'componentSessioneName'
 */
public java.lang.String getComponentSessioneName() {
	return componentSessioneName;
}
/**
 * Restituisce il valore della proprietà 'searchBulkClass'
 *
 * @return Il valore della proprietà 'searchBulkClass'
 */
public java.lang.Class getSearchBulkClass() {
	return searchBulkClass;
}
/**
 * Restituisce il valore della proprietà 'searchBulkInfo'
 *
 * @return Il valore della proprietà 'searchBulkInfo'
 */
public it.cnr.jada.bulk.BulkInfo getSearchBulkInfo() {
	return searchBulkInfo;
}
/**
 * Restituisce il valore della proprietà 'searchResultColumns'
 *
 * @return Il valore della proprietà 'searchResultColumns'
 */
public java.util.Dictionary getSearchResultColumns() {
	if (getSearchResultColumnSet() == null)
		return getModel().getBulkInfo().getColumnFieldPropertyDictionary();
	return getModel().getBulkInfo().getColumnFieldPropertyDictionary(getSearchResultColumnSet());
}
/**
 * Restituisce il valore della proprietà 'searchResultColumnSet'
 *
 * @return Il valore della proprietà 'searchResultColumnSet'
 */
public java.lang.String getSearchResultColumnSet() {
	return searchResultColumnSet;
}
/**
 * ritorna la currentSelection
 */
public java.util.BitSet getSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet currentSelection) {
	//for (int i = 0;i < bulks.length;i++) {
		//if (Boolean.TRUE.equals(((Cdr_ass_tipo_laBulk)bulks[i]).getFl_associato()))
			//currentSelection.set(i);
	//}
	return currentSelection;
}
/**
 * Inizializza il BP coi parametri di inizializzazione presenti nel file di
 * configurazione delle action.
 * @param config l'elenco dei parametri di configurazione del business
 * @param context	L'ActionContext della richiesta 
 */

protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		setBulkClassName(config.getInitParameter("bulkClassName"));
		setComponentSessioneName(config.getInitParameter("componentSessionName"));
		if (searchBulkClass == null)
			setSearchBulkClass(bulkClass);
		setSearchResultColumnSet(config.getInitParameter("searchResultColumnSet"));			
		setModel( context, createEmptyModelForSearch( context));
	} catch(ClassNotFoundException e) {
		throw new RuntimeException("Non trovata la classe bulk");
	}
	super.init(config,context);
}
/**
 * inizializza il pg_call del protocollo VSX in modo da consentire l'inserimento dei doc. contabili
 * selezionat dall'utente nella tabella VSX_CHIUSURA
 */
public void initializeSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	try 
	{
		V_obb_acc_xxxBulk doc = ((RicercaDocContComponentSession)createComponentSession()).initializeSelectionPerRiportaIndietro(
						context.getUserContext(),(V_obb_acc_xxxBulk)getModel());
		setModel( context, doc );

	} catch(Exception e) 
	{
		throw handleException(e);
	} 
}

/**
 * gestisce la selezione massiva di tutti i doc.contabili visualizzati all'utente
 */
public void selectAll(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try 
	{
		((RicercaDocContComponentSession)createComponentSession()).selectAllPerRiportaIndietro(
						context.getUserContext(),(V_obb_acc_xxxBulk)getModel());
	} catch(Exception e) {
		throw handleException(e);
	} 
	
}
/**
 * Imposta il valore della proprietà 'bulkClass'
 *
 * @param newClass	Il valore da assegnare a 'bulkClass'
 */
public void setBulkClass(java.lang.Class newClass) {
	bulkInfo = BulkInfo.getBulkInfo(this.bulkClass = newClass);
}
/**
 * Imposta il valore della proprietà 'bulkClassName'
 *
 * @param bulkClassName	Il valore da assegnare a 'bulkClassName'
 * @throws ClassNotFoundException	
 */
public void setBulkClassName(java.lang.String bulkClassName) throws ClassNotFoundException {
	setBulkClass(getClass().getClassLoader().loadClass(bulkClassName));
}
/**
 * Imposta il valore della proprietà 'bulkInfo'
 *
 * @param newInfo	Il valore da assegnare a 'bulkInfo'
 */
public void setBulkInfo(it.cnr.jada.bulk.BulkInfo newInfo) {
	bulkInfo = newInfo;
}
/**
 * Imposta il valore della proprietà 'componentSessioneName'
 *
 * @param newSessioneName	Il valore da assegnare a 'componentSessioneName'
 */
public void setComponentSessioneName(java.lang.String newSessioneName) {
	componentSessioneName = newSessioneName;
}
/**
 * Imposta il valore delle proprietà 'searchBulkClass' e 'searchBulkInfo'
 *
 * @param searchBulkClass	Il valore da assegnare a 'searchBulkClass'
 */

private void setSearchBulkClass(java.lang.Class searchBulkClass) {
	searchBulkInfo = BulkInfo.getBulkInfo(this.searchBulkClass = searchBulkClass);
}
/**
 * Imposta il valore della proprietà 'searchBulkClassName'
 *
 * @param searchBulkClassName	Il valore da assegnare a 'searchBulkClassName'
 * @throws ClassNotFoundException	
 */
public void setSearchBulkClassName(String searchBulkClassName) throws ClassNotFoundException {
	if (searchBulkClassName != null)
		setSearchBulkClass(getClass().getClassLoader().loadClass(searchBulkClassName));
}
/**
 * Imposta il valore della proprietà 'searchBulkInfo'
 *
 * @param newSearchBulkInfo	Il valore da assegnare a 'searchBulkInfo'
 */
public void setSearchBulkInfo(it.cnr.jada.bulk.BulkInfo newSearchBulkInfo) {
	searchBulkInfo = newSearchBulkInfo;
}
/**
 * Imposta il valore della proprietà 'searchResultColumnSet'
 *
 * @param newSearchResultColumnSet	Il valore da assegnare a 'searchResultColumnSet'
 */
public void setSearchResultColumnSet(java.lang.String newSearchResultColumnSet) {
	searchResultColumnSet = newSearchResultColumnSet;
}
/**
 * inserisce/elimina dalla tabella VSX_CHIUSURA le chiavi dei doc.contabili
 * selezionati/deselezionati dall'utente
 */
public java.util.BitSet setSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet oldSelection, java.util.BitSet newSelection) throws it.cnr.jada.action.BusinessProcessException {

	try 
	{
		((RicercaDocContComponentSession)createComponentSession()).setSelectionPerRiportaIndietro(
			context.getUserContext(),
			(V_obb_acc_xxxBulk)getModel(),
			bulks,
			oldSelection,
			newSelection);
		return newSelection;
	} catch(Exception e) {
		throw handleException(e);
	} 
}
/**
 * Imposta il valore della proprietà 'sessioneName'
 *
 * @param newSessioneName	Il valore da assegnare a 'sessioneName'
 */
public void setSessioneName(java.lang.String newSessioneName) {
	componentSessioneName = newSessioneName;
}
}
