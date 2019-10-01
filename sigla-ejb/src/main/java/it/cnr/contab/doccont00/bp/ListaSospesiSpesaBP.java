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

/*
 * Created on Jan 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.bp;


import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.doccont00.ejb.MandatoComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;
/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ListaSospesiSpesaBP extends SelezionatoreListaBP implements it.cnr.jada.util.action.SearchProvider{

	private String componentSessioneName;
	private Class bulkClass;
	private BulkInfo bulkInfo;
				
	public Button[] createToolbar()
	{
		Button abutton[] = new Button[5];
		int i = 0;
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.print");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.excel");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.multiSelection");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.selectAll");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.freeSearch");		
		return abutton;
	}
	
	public it.cnr.jada.util.RemoteIterator search(
		it.cnr.jada.action.ActionContext context, 
		it.cnr.jada.persistency.sql.CompoundFindClause clauses, 
		it.cnr.jada.bulk.OggettoBulk prototype) 
		throws it.cnr.jada.action.BusinessProcessException {
		
		return findFreeSearch(
							context,
							clauses,
							prototype);
	}
	
	public it.cnr.jada.util.RemoteIterator findFreeSearch(
		ActionContext context,
		it.cnr.jada.persistency.sql.CompoundFindClause clauses,
		OggettoBulk model) 
		throws it.cnr.jada.action.BusinessProcessException {
	
		try {
			MandatoComponentSession session = (MandatoComponentSession) createRicercaComponentSession();
			CRUDAbstractMandatoBP mandatoBP = (CRUDAbstractMandatoBP)getParent();
			return session.cercaSospesi( context.getUserContext(), clauses, (MandatoBulk)mandatoBP.getModel());			
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	/**
	 * Crea la CRUDComponentSession da usare per effettuare le operazioni di Ricerca
	 */
	public CRUDComponentSession createRicercaComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (CRUDComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_MandatoComponentSession",CRUDComponentSession.class);
	}
		
	/**
	 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
	 */
	public CRUDComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (CRUDComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(componentSessioneName,CRUDComponentSession.class);
	}
	/**
	 * @param config it.cnr.jada.action.Config
	 * @param context Il contesto dell'azione
	 */
	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			super.init(config,context);
			setPageSize( 20 );	
			setBulkClassName(config.getInitParameter("bulkClassName"));	
			setComponentSessioneName(config.getInitParameter("componentSessionName"));		
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}		
	/**
	 * @return
	 */
	public String getComponentSessioneName() {
		return componentSessioneName;
	}

	/**
	 * @param string
	 */
	public void setComponentSessioneName(String string) {
		componentSessioneName = string;
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
			return createComponentSession().inizializzaBulkPerRicercaLibera(context.getUserContext(),new SospesoBulk());
		} catch(Exception e) {
			throw handleException(e);
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
}
