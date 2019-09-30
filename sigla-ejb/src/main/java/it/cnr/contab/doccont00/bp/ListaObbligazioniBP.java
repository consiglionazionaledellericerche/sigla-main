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
 * Business process che gestisce alcune informazioni relative alle obbligazioni.
 * E' utilizzato in combinazione con la <code>CRUDListaObbligazioniAction</code>.
 */

public class ListaObbligazioniBP extends it.cnr.jada.util.action.SelezionatoreListaBP implements FindBP
{
	private String componentSessioneName;
	private Class bulkClass;
	private BulkInfo bulkInfo;
	private String statoObbligazione;
	private boolean editable;
public ListaObbligazioniBP() 
{
	super();
	table.setMultiSelection(true);

//	table.setOnselect("javascript:select");
//	table.setReadonly(false);
//	table.setStatus(FormController.EDIT);	
}
public ListaObbligazioniBP( String function ) 
{
	table.setMultiSelection(true);
	editable = function != null && function.indexOf('M') >= 0;
}
/**
 * Gestisce un comando "Conferma"
     * @param context <code>ActionContext</code> in uso.
 	 *
 	 * @exception <code>BusinessProcessException</code>, <code>ValidationException</code>
 *
 */

public void confirm(ActionContext context) throws it.cnr.jada.action.BusinessProcessException,ValidationException 
{
	
	try {
		it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession crud = (it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession) createComponentSession();

		if (selection.size() > 0) 
		{
			for (SelectionIterator i = selection.iterator();i.hasNext();) 
			{
				ObbligazioneBulk bulk = (ObbligazioneBulk) getElementAt( context, i.nextIndex());
//				bulk.setToBeDeleted();
				crud.confermaObbligazioneProvvisoria(context.getUserContext(),bulk);
			}
		}
		/*
		else if (selection.getFocus() > 0) 
		{
			crud.eliminaConBulk(context.getUserContext(),getElemenrAt( conetxt, selection.getFocus()));
		} */
		else 
		{
			throw new MessageToUser( "E' necessario selezionare le obbligazioni da confermare" );
		}
		refreshList( context );
		
	} 
	catch(Exception e) 
	{
		throw handleException(e);
	}
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
		return createComponentSession().inizializzaBulkPerRicercaLibera(context.getUserContext(),createModel( context));
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
		V_obbligazione_im_mandatoBulk obbligazione = new V_obbligazione_im_mandatoBulk();
		obbligazione.setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		obbligazione.setCd_uo_origine( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());
		obbligazione.setStato_obbligazione( getStatoObbligazione() );
		obbligazione.setRiportato( "N" );		
		return obbligazione;


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
		toolbar = new Button[2];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.freeSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.visualizzaOrdine");
	}
	else
	{
		if ( isObbligazioniDefinitive() )
			toolbar = new Button[3];
		else
			toolbar = new Button[7];
			int i = 0;
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.freeSearch");
		//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.new");
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.edit");
		if ( !isObbligazioniDefinitive())
		{
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.confirm");
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.delete");
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.selectAll");
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.deselectAll");
			
		}		
	//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.bringBack");
	//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.print");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.emetteOrdine");
		
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
				ObbligazioneBulk bulk = (ObbligazioneBulk) getElementAt( context, i.nextIndex());
				bulk = (ObbligazioneBulk) crud.inizializzaBulkPerModifica( context.getUserContext(), bulk );
				if (bulk.isObbligazioneResiduo() || bulk.isImpegnoResiduo())
					throw new MessageToUser( "Eliminazione Impegni Residui non possibile!" );
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
public it.cnr.jada.util.RemoteIterator find(ActionContext context,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk model) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator( context, createComponentSession().cerca(context.getUserContext(),clauses,model));
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * find method comment.
 */
public it.cnr.jada.util.RemoteIterator find(ActionContext actionContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk,OggettoBulk context,String property) throws it.cnr.jada.action.BusinessProcessException {
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
 * Metodo con cui si ottiene il valore della variabile <code>statoObbligazione</code>.
 *
 * @return statoObbligazione Stato corrente dell'obbligazione
 */
public java.lang.String getStatoObbligazione() {
	return statoObbligazione;
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		super.init(config,context);
		setPageSize( 10 );
		
		setBulkClassName(config.getInitParameter("bulkClassName"));
		setComponentSessioneName(config.getInitParameter("componentSessionName"));
		setStatoObbligazione(config.getInitParameter("statoObbligazione"));
		
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
/**
 * @return editable TRUE Se l'obbligazione è editabile
 *					FALSE in caso contrario
 */
public boolean isEditable() {
	return editable;
}
/**
 * Ritorna TRUE se l'obbligazione e' in fase di modifica/inserimento
 */

public boolean isEditButtonEnabled() {
	return getSelection().getFocus() != -1;

}
/**
 *	Abilito il bottone di emissione dell'ordine solo se l'obbligazione e'
 *  in fase di modifica/inserimento
 */

public boolean isEmettiOrdineButtonEnabled() {
	return getSelection().getFocus() != -1;

}
/**
 * Ritorna TRUE se l'obbligazione e' definitiva
 *		   FALSE se l'obbligazione è provvisoria.
 */
public boolean isObbligazioniDefinitive()
{
	return (statoObbligazione.equals( ObbligazioneBulk.STATO_OBB_DEFINITIVO));
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
		V_obbligazione_im_mandatoBulk obbligazione = (V_obbligazione_im_mandatoBulk) createModel( context );
		setIterator(context,createComponentSession().cerca(context.getUserContext(),null,obbligazione ));
		selection.clear();
		
	} catch(Exception e) 
	{
		throw handleException(e);
	} 

}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'bulkClass'
 *
 * @param newBulkClass	Il valore da assegnare a 'bulkClass'
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
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'componentSessioneName'
 *
 * @param newComponentSessioneName	Il valore da assegnare a 'componentSessioneName'
 */
public void setComponentSessioneName(java.lang.String newComponentSessioneName) {
	componentSessioneName = newComponentSessioneName;
}
public void setEditable(boolean newEditable) {
	editable = newEditable;
}
/**
 * Metodo con cui si definisce il valore della variabile <code>statoObbligazione</code>.
 *
 * @param newStatoObbligazione Stato dell'obbligazione
 */
public void setStatoObbligazione(java.lang.String newStatoObbligazione) {
	statoObbligazione = newStatoObbligazione;
}
public void deselectAll(ActionContext actioncontext) {}
}
