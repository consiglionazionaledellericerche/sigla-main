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
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.*;

/**
 * Business process che gestisce alcune informazioni relative agli accertamenti.
 * E' utilizzato in combinazione con la <code>CRUDListaAccertamentiAction</code>.
 */
public class ListaAccertamentiBP extends it.cnr.jada.util.action.SelezionatoreListaBP implements it.cnr.jada.util.action.FindBP {
	private boolean editable;
	private String componentSessioneName;
	private Class bulkClass;
	private BulkInfo bulkInfo;
public ListaAccertamentiBP() {
	super();
	table.setMultiSelection(true);
}
public ListaAccertamentiBP( String function ) 
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
		return createComponentSession().inizializzaBulkPerRicercaLibera(context.getUserContext(),createModel(context));
// TODO: initializaForFreeSearch con CRUDListaBP
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * @param context <code>ActionContext</code> in uso.
 *
 */

public OggettoBulk createModel(ActionContext context) 
{
		AccertamentoOrdBulk accertamento = new AccertamentoOrdBulk();
//		accertamento.initializeForSearch( this, context );
		accertamento.setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
//		accertamento.setUnita_organizzativa( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));
		accertamento.setCd_unita_organizzativa( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());
		return accertamento;

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
	
	if ( !isEditable() )
	{
		toolbar = new Button[1];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.freeSearch");
	}

	else
	{
			toolbar = new Button[3];
		
			int i = 0;
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.freeSearch");
		//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.new");
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.edit");
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.delete");

	//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.bringBack");
	//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.print");
	}

	return toolbar; 
}
/**
 * Gestisce un comando "Cancella"
	 * @param context <code>ActionContext</code> in uso.
 	 *
 	 * @exception <code>BusinessProcessException</code>, <code>ValidationException</code>
 */
public void delete(ActionContext context) throws it.cnr.jada.action.BusinessProcessException,ValidationException 
{
	
	try {
		CRUDComponentSession crud = createComponentSession();
		if (selection.size() > 0) 
		{
			for (SelectionIterator i = selection.iterator();i.hasNext();) 
			{
				OggettoBulk bulk = (OggettoBulk) getElementAt( context,i.nextIndex());
				bulk.setToBeDeleted();
				crud.eliminaConBulk(context.getUserContext(),bulk);
			}
		}
		/*
		else if (selection.getFocus() > 0) 
		{
			crud.eliminaConBulk(context.getUserContext(),pageContents[selection.getFocus()]);
		} */
		else 
		{
			throw new MessageToUser( "E' necessario selezionare gli elementi da cancellare" );
		}
		refreshList( context );
		
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch(ComponentException e) {
		throw handleException(e);
	} catch(javax.ejb.EJBException e) {
		throw handleException(e);
	}

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
 * @param config it.cnr.jada.action.Config
 * @param context Il contesto dell'azione
 */
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		super.init(config,context);
		setPageSize( 10 );
		
		setBulkClassName(config.getInitParameter("bulkClassName"));
		setComponentSessioneName(config.getInitParameter("componentSessionName"));
		// setStatoObbligazione(config.getInitParameter("statoObbligazione"));
		
		refreshList( context );

	} catch(ClassNotFoundException e) {
		throw new RuntimeException("Non trovata la classe bulk");
	} catch(Throwable e) {
		throw new BusinessProcessException(e);
	}
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @param bulks	
 * @return 
 * @throws BusinessProcessException	
 */
public OggettoBulk[] initializeBulks(ActionContext context,OggettoBulk[] bulks) throws BusinessProcessException {
	return bulks;
	/*
	try {
		CRUDComponentSession crud = createComponentSession();
		if (crud instanceof MultipleCRUDComponentSession)
			return ((MultipleCRUDComponentSession)crud).modificaConBulk(context.getUserContext(),bulks);
		else {
			for (int i = 0;i < bulks.length;i++)
				bulks[i] = crud.inizializzaBulkPerModifica(context.getUserContext(),bulks[i]);
		}
		return bulks;
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
	*/
}
public boolean isEditable() {
	return editable;
}
/**
 * Ritorna TRUE se l'accertamento e' in fase di modifica/inserimento
 */

public boolean isEditButtonEnabled() {
	return getSelection().getFocus() != -1;

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
		AccertamentoOrdBulk accertamento = (AccertamentoOrdBulk) createModel( context );
//		accertamento.initializeForSearch( this, context );
		accertamento.setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
//		accertamento.setUnita_organizzativa( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));
		accertamento.setCd_unita_organizzativa( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());		

		setIterator(context,createComponentSession().cerca(context.getUserContext(),null,accertamento ));
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
public void setBulkInfo(it.cnr.jada.bulk.BulkInfo newBulkInfo) {
	bulkInfo = newBulkInfo;
}
/**
 * @param newComponentSessioneName java.lang.String
 */
public void setComponentSessioneName(java.lang.String newComponentSessioneName) {
	componentSessioneName = newComponentSessioneName;
}
/**
 * @param newEditable boolean
 */
public void setEditable(boolean newEditable) {
	editable = newEditable;
}
}
