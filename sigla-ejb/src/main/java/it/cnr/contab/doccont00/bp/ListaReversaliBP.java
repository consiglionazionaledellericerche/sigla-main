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

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.util.jsp.*;
/**
 * Business process che gestisce alcune informazioni relative alle reversali.
 * E' utilizzato in combinazione con la <code>CRUDListaReversaliAction</code>.
 */
public class ListaReversaliBP extends it.cnr.jada.util.action.SelezionatoreListaBP implements it.cnr.jada.util.action.FindBP {
	private boolean editable;
	private String componentSessioneName;
	private Class bulkClass;
	private BulkInfo bulkInfo;
	private String tipoDocumento;
public ListaReversaliBP() {
	super();
	table.setMultiSelection(true);
}
public ListaReversaliBP( String function ) 
{
	table.setMultiSelection(true);
	editable = function != null && function.indexOf('M') >= 0;
}
/**
 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
 */
public CRUDComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
	return (CRUDComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(componentSessioneName,CRUDComponentSession.class);
}
/** 
	 * @param context <code>ActionContext</code> in uso.
 	 *
 	 * @return <code>OggettoBulk</code>
 	 *
 	 * @exception <code>BusinessProcessException</code>
 */
public OggettoBulk createEmptyModelForFreeSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return createComponentSession().inizializzaBulkPerRicercaLibera(context.getUserContext(),createModel( context ));
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * @param context <code>ActionContext</code> in uso.
 *
 * @exception <code>BusinessProcessException</code>, <code>ValidationException</code>
 */

public OggettoBulk createModel(ActionContext context) 
{
		ReversaleIBulk reversale = new ReversaleIBulk();
		reversale.setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		reversale.setCd_uo_origine( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());
		reversale.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_REV_PROVV);
		reversale.setUnita_organizzativa( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa( context ));
		return reversale;


}
/** 
	 * @param context <code>ActionContext</code> in uso.
 	 *
 	 * @return <code>OggettoBulk</code>
 	 *
 	 * @exception <code>BusinessProcessException</code>
 */
public OggettoBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		OggettoBulk bulk = (OggettoBulk)bulkClass.newInstance();
		bulk.setUser(context.getUserInfo().getUserid());
		return bulk;
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Metodo utilizzato per creare una toolbar applicativa personalizzata.
 	 * @return toolbar La nuova toolbar creata
 *
 */

public it.cnr.jada.util.jsp.Button[] createToolbar() 
{
	Button[] toolbar;
	
		toolbar = new Button[2];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.freeSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.edit");

	return toolbar; 
}
/**
 * find method comment.
 */
public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext context, it.cnr.jada.persistency.sql.CompoundFindClause clause, it.cnr.jada.bulk.OggettoBulk model) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator( context, createComponentSession().cerca(context.getUserContext(),clause,model));
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * find method comment.
 */
public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, String property) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator( actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
	} catch(Exception e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
/**
 * @return java.lang.Class
 */
public java.lang.Class getBulkClass() {
	return bulkClass;
}
/**
 * @return it.cnr.jada.bulk.BulkInfo
 */
public it.cnr.jada.bulk.BulkInfo getBulkInfo() {
	return bulkInfo;
}
/**
 * @return java.lang.String
 */
public java.lang.String getComponentSessioneName() {
	return componentSessioneName;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'tipoDocumento'
 *
 * @return Il valore della proprietà 'tipoDocumento'
 */
public java.lang.String getTipoDocumento() {
	return tipoDocumento;
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		super.init(config,context);
		setPageSize( 10 );
		
		setBulkClassName(config.getInitParameter("bulkClassName"));
		setComponentSessioneName(config.getInitParameter("componentSessionName"));
		setTipoDocumento(config.getInitParameter("tipoDocumento"));
		
		refreshList( context );

	} catch(ClassNotFoundException e) {
		throw new RuntimeException("Non trovata la classe bulk");
	} catch(Throwable e) {
		throw new BusinessProcessException(e);
	}
}
/**
 * @return editable TRUE Se la reversale è editabile
 *					FALSE in caso contrario
 */
public boolean isEditable() {
	return editable;
}
/**
 * Ritorna TRUE se la reversale e' in fase di modifica/inserimento
 */

public boolean isEditButtonEnabled() {
	// return isEditable() && getSelection().getFocus() != -1;
	return getSelection().getFocus() != -1;

}
/**
 *	Abilito il bottone di cancellazione documento solo se non ho scadenze in fase di modifica/inserimento
 */

public boolean isFreeSearchButtonHidden() 
{
	
	return !isEditable();
}
/**
 * @param context <code>ActionContext</code> in uso.
 *
 * @exception <code>BusinessProcessException</code>, <code>ValidationException</code>
 */

public void refreshList(ActionContext context) throws it.cnr.jada.action.BusinessProcessException,ValidationException 
{
	
	try
	{
		ReversaleIBulk reversale = (ReversaleIBulk)createModel( context );

		setIterator(context,createComponentSession().cerca(context.getUserContext(),null,reversale ));
		selection.clear();
		
	} catch(Exception e) 
	{
		throw handleException(e);
	} 

}
/**
 * @param newBulkClass java.lang.Class
 */
public void setBulkClass(java.lang.Class newBulkClass) {
	bulkClass = newBulkClass;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'bulkClassName'
 *
 * @param bulkClassName	Il valore da assegnare a 'bulkClassName'
 * @throws ClassNotFoundException	
 */
public void setBulkClassName(java.lang.String bulkClassName) throws ClassNotFoundException {
	bulkClass = getClass().getClassLoader().loadClass(bulkClassName);
	bulkInfo = it.cnr.jada.bulk.BulkInfo.getBulkInfo(bulkClass);
	setColumns(bulkInfo.getColumnFieldPropertyDictionary());
}
/**
 * @param newBulkInfo it.cnr.jada.bulk.BulkInfo
 */
public void setBulkInfo(it.cnr.jada.bulk.BulkInfo newBulkInfo) {
	bulkInfo = newBulkInfo;
}
/**
 * @param newComponentSessioneName java.lang.String
 */
public void setComponentSessioneName(java.lang.String newComponentSessioneName) {
	componentSessioneName = newComponentSessioneName;
}
public void setEditable(boolean newEditable) {
	editable = newEditable;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'tipoDocumento'
 *
 * @param newTipoDocumento	Il valore da assegnare a 'tipoDocumento'
 */
public void setTipoDocumento(java.lang.String newTipoDocumento) {
	tipoDocumento = newTipoDocumento;
}
}
