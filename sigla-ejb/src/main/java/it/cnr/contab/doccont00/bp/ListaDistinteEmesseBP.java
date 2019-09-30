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

import java.util.*;
import it.cnr.contab.doccont00.intcass.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.*;
/**
 * Business process che gestisce alcune informazioni relative alle distinte emesse.
 * E' utilizzato in combinazione con la <code>ListaDistinteEmesseAction</code>.
 */
public class ListaDistinteEmesseBP extends it.cnr.jada.util.action.SelezionatoreListaBP implements it.cnr.jada.util.action.FindBP {
	private String componentSessioneName;
	private Class bulkClass;
	private BulkInfo bulkInfo;
public ListaDistinteEmesseBP() {
	super();
	table.setMultiSelection(true);
}
public ListaDistinteEmesseBP(String function) {
	super(function);
	table.setMultiSelection(true);
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
		V_distinta_cass_im_man_revBulk distinte_emesse = new V_distinta_cass_im_man_revBulk();
		// distinte_emesse.setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		// distinte_emesse.setCd_uo_origine( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());
		return distinte_emesse;
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
		toolbar[0] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.inviaDistinta");

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
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		super.init(config,context);
		setPageSize( 10 );
		
		setBulkClassName(config.getInitParameter("bulkClassName"));
		setComponentSessioneName(config.getInitParameter("componentSessionName"));
		
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
 * Gestisce un comando di Invio della distinta.
     * @param context <code>ActionContext</code> in uso.
 	 *
 	 * @exception <code>BusinessProcessException</code>, <code>ValidationException</code>
 *
 */

public void inviaDistinta(ActionContext context, Collection distinte_emesse) throws it.cnr.jada.action.BusinessProcessException,ValidationException 
{
	
	try {
		it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession distintaComp = (it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession) createComponentSession();

		if (selection.size() > 0) 
			distintaComp.inviaDistinte(context.getUserContext(), distinte_emesse);
		else 
			throw new MessageToUser( "E' necessario selezionare le distinte da inviare" );

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
 * @param context <code>ActionContext</code> in uso.
 *
 * @exception <code>BusinessProcessException</code>, <code>ValidationException</code>
 */

public void refreshList(ActionContext context) throws it.cnr.jada.action.BusinessProcessException,ValidationException 
{
	
	try
	{
		V_distinta_cass_im_man_revBulk distinte_emesse = (V_distinta_cass_im_man_revBulk) createModel( context );
		setIterator(context,createComponentSession().cerca(context.getUserContext(),null,distinte_emesse ));
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
}
