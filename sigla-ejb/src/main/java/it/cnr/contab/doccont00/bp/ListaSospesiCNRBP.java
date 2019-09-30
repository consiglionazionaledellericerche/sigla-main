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

import it.cnr.contab.doccont00.comp.SospesoRiscontroComponent;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.doccont00.ejb.ReversaleComponentSession;
import it.cnr.contab.doccont00.ejb.SospesoRiscontroComponentSession;

import java.util.*;
import it.cnr.contab.doccont00.intcass.bulk.*;
import it.cnr.contab.inventario00.bp.AssBeneFatturaBP;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.*;
/**
 * Business process che gestisce alcune informazioni relative alle distinte emesse.
 * E' utilizzato in combinazione con la <code>ListaDistinteEmesseAction</code>.
 */
public class ListaSospesiCNRBP extends ConsultazioniBP implements it.cnr.jada.util.action.SearchProvider
{
	private String componentSessioneName;
	private Class bulkClass;
	private BulkInfo bulkInfo;

//	private final SimpleDetailCRUDController sospesi = new SimpleDetailCRUDController("Sospesi",SospesoBulk.class,"sospesi_cnrColl",this);
public ListaSospesiCNRBP() {
	super();
}
public ListaSospesiCNRBP(String function) {
	super(function);
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
 */

public OggettoBulk createModel(ActionContext context) 
{
		ListaSospesiBulk lista = new ListaSospesiBulk();
		return lista;
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
	toolbar = new Button[1];
	toolbar[0] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.edit");
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
public String getFormTitle() 
{
	return "Lista Sospesi CNR";
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try {
		super.init(config,context);
		setPageSize( 10 );
		
		setBulkClassName(config.getInitParameter("bulkClassName"));
		setComponentSessioneName(config.getInitParameter("componentSessionName"));
		
		refreshList( context );

		setModel(context, createNewBulk(context));
	} catch(ClassNotFoundException e) {
		throw new RuntimeException("Non trovata la classe bulk");
	} catch(Throwable e) {
		throw new BusinessProcessException(e);
	}
/*
	setEditable( false );
	super.init(config,context);
*/	
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
		ListaSospesiBulk sospesi = (ListaSospesiBulk) createModel( context );
		setIterator(context,createComponentSession().cerca(context.getUserContext(),null,sospesi ));
		selection.clear();
		
	} catch(Exception e) 
	{
		throw handleException(e);
	} 

/*	
	try
	{
		setModel(context, createComponentSession().inizializzaBulkPerRicerca(context.getUserContext(),getModel() ));
		resyncChildren( context);
		
	} catch(Exception e) 
	{
		throw handleException(e);
	} 
*/
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
	bulkClass = getClass().getClassLoader().loadClass("it.cnr.contab.doccont00.core.bulk.SospesoBulk");
	bulkInfo = it.cnr.jada.bulk.BulkInfo.getBulkInfo(bulkClass);
	setColumns(bulkInfo.getColumnFieldPropertyDictionary("SospesiCNR"));
	setFreeSearchSet("SospesiCNR");
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
public RemoteIterator search(
		ActionContext actioncontext,
		CompoundFindClause compoundfindclause,
		OggettoBulk oggettobulk)
		throws BusinessProcessException {
			/*
			 * Mi conservo la findClause per poi utilizzarla
			 * nel selectAll
			 */
			((SospesoBulk)oggettobulk).setStatoTextForSearch(SospesoBulk.STATO_DOCUMENTO_TUTTI);
			super.setModel(actioncontext, ((SospesoBulk)oggettobulk));
			setFindclause(compoundfindclause);
			return findFreeSearch(actioncontext,
								  compoundfindclause,
								  new ListaSospesiBulk());
	}

@Override
public void setModel(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
	if (this.getModel()!=null && oggettobulk instanceof SospesoBulk)
		((SospesoBulk)oggettobulk).setStatoTextForSearch(((SospesoBulk)this.getModel()).getStatoTextForSearch());
	super.setModel(actioncontext, oggettobulk);
}
}
