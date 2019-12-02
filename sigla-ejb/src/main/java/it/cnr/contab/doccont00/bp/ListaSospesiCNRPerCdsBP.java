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

package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.intcass.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.util.*;
/**
 * Insert the type's description here.
 * Creation date: (21/03/2003 10.07.03)
 * @author: Simonetta Costa
 */
public class ListaSospesiCNRPerCdsBP extends it.cnr.jada.util.action.BulkBP {

	private String componentSessioneName;
	private Class bulkClass;
	private BulkInfo bulkInfo;
	private java.lang.Class searchBulkClass;
	private it.cnr.jada.bulk.BulkInfo searchBulkInfo;
	private java.lang.String searchResultColumnSet;	
	
	
/**
 * ListaSospesiCNRPerCdsBP constructor comment.
 */
public ListaSospesiCNRPerCdsBP() {
	super();
}
/**
 * ListaSospesiCNRPerCdsBP constructor comment.
 * @param function java.lang.String
 */
public ListaSospesiCNRPerCdsBP(String function) {
	super(function);
}
/**
 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
 */
public CRUDComponentSession createComponentSession() throws BusinessProcessException {
	return (CRUDComponentSession)createComponentSession(componentSessioneName,CRUDComponentSession.class);
}
public it.cnr.jada.util.RemoteIterator find(ActionContext context,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk model) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,createComponentSession().cerca(context.getUserContext(),clauses,model));
	} catch(Exception e) {
		throw handleException(e);
	}
}
public RemoteIterator find(ActionContext actionContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk,OggettoBulk context,String property) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
	} catch(Exception e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (21/03/2003 10.15.59)
 * @return java.lang.Class
 */
public java.lang.Class getBulkClass() {
	return bulkClass;
}
/**
 * Insert the method's description here.
 * Creation date: (21/03/2003 10.15.59)
 * @return it.cnr.jada.bulk.BulkInfo
 */
public it.cnr.jada.bulk.BulkInfo getBulkInfo() {
	return bulkInfo;
}
/**
 * Insert the method's description here.
 * Creation date: (21/03/2003 10.19.06)
 * @return java.lang.String
 */
public java.lang.String getComponentSessioneName() {
	return componentSessioneName;
}
/**
 * Insert the method's description here.
 * Creation date: (21/03/2003 10.27.41)
 * @return java.lang.Class
 */
public java.lang.Class getSearchBulkClass() {
	return searchBulkClass;
}
/**
 * Insert the method's description here.
 * Creation date: (21/03/2003 10.27.41)
 * @return it.cnr.jada.bulk.BulkInfo
 */
public it.cnr.jada.bulk.BulkInfo getSearchBulkInfo() {
	return searchBulkInfo;
}
public java.util.Dictionary getSearchResultColumns() {
	if (getSearchResultColumnSet() == null)
		return getModel().getBulkInfo().getColumnFieldPropertyDictionary();
	return getModel().getBulkInfo().getColumnFieldPropertyDictionary(getSearchResultColumnSet());
}

/**
 * Insert the method's description here.
 * Creation date: (21/03/2003 10.37.40)
 * @return java.lang.String
 */
public java.lang.String getSearchResultColumnSet() {
	return searchResultColumnSet;
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		setBulkClassName(config.getInitParameter("bulkClassName"));
		setComponentSessioneName(config.getInitParameter("componentSessionName"));
		if (searchBulkClass == null)
			setSearchBulkClass(bulkClass);
		setSearchResultColumnSet(config.getInitParameter("searchResultColumnSet"));			
		setModel( context, new SelezionaSospesiCNRBulk());
	} catch(ClassNotFoundException e) {
		throw new RuntimeException("Non trovata la classe bulk");
	}
	super.init(config,context);
}
/**
 * Insert the method's description here.
 * Creation date: (21/03/2003 10.15.59)
 * @param newClass java.lang.Class
 */
public void setBulkClass(java.lang.Class newClass) {
	bulkInfo = BulkInfo.getBulkInfo(this.bulkClass = newClass);
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'bulkClassName'
 *
 * @param bulkClassName	Il valore da assegnare a 'bulkClassName'
 * @throws ClassNotFoundException	
 */
public void setBulkClassName(java.lang.String bulkClassName) throws ClassNotFoundException {
	setBulkClass(getClass().getClassLoader().loadClass(bulkClassName));
}
/**
 * Insert the method's description here.
 * Creation date: (21/03/2003 10.15.59)
 * @param newInfo it.cnr.jada.bulk.BulkInfo
 */
public void setBulkInfo(it.cnr.jada.bulk.BulkInfo newInfo) {
	bulkInfo = newInfo;
}
/**
 * Insert the method's description here.
 * Creation date: (21/03/2003 10.19.06)
 * @param newSessioneName java.lang.String
 */
public void setComponentSessioneName(java.lang.String newSessioneName) {
	componentSessioneName = newSessioneName;
}
private void setSearchBulkClass(java.lang.Class searchBulkClass) {
	searchBulkInfo = BulkInfo.getBulkInfo(this.searchBulkClass = searchBulkClass);
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'bulkClassName'
 *
 * @param bulkClassName	Il valore da assegnare a 'bulkClassName'
 * @throws ClassNotFoundException	
 */
public void setSearchBulkClassName(String searchBulkClassName) throws ClassNotFoundException {
	if (searchBulkClassName != null)
		setSearchBulkClass(getClass().getClassLoader().loadClass(searchBulkClassName));
}
/**
 * Insert the method's description here.
 * Creation date: (21/03/2003 10.27.41)
 * @param newSearchBulkInfo it.cnr.jada.bulk.BulkInfo
 */
public void setSearchBulkInfo(it.cnr.jada.bulk.BulkInfo newSearchBulkInfo) {
	searchBulkInfo = newSearchBulkInfo;
}
/**
 * Insert the method's description here.
 * Creation date: (21/03/2003 10.37.40)
 * @param newSearchResultColumnSet java.lang.String
 */
public void setSearchResultColumnSet(java.lang.String newSearchResultColumnSet) {
	searchResultColumnSet = newSearchResultColumnSet;
}
/**
 * Insert the method's description here.
 * Creation date: (21/03/2003 10.19.06)
 * @param newSessioneName java.lang.String
 */
public void setSessioneName(java.lang.String newSessioneName) {
	componentSessioneName = newSessioneName;
}
}
