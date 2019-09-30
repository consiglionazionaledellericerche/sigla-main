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

package it.cnr.contab.cori00.bp;

import it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_centroBulk;
import it.cnr.contab.cori00.ejb.Liquid_coriComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.RemoteIterator;

public class LiquidGruppoCentroBP extends it.cnr.jada.util.action.SelezionatoreListaBP implements it.cnr.jada.util.action.FindBP {
	private String componentSessioneName;
	private Class bulkClass;
	private BulkInfo bulkInfo;
public LiquidGruppoCentroBP() {
	super();
	table.setMultiSelection(false);
}
public LiquidGruppoCentroBP(String function) {
	super(function);
	table.setMultiSelection(false);
}

public it.cnr.jada.util.jsp.Button[] createToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[2];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.ribalta");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.reset");
	return toolbar;
}
/**
 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
 */
public CRUDComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
	return (CRUDComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(componentSessioneName,CRUDComponentSession.class);
}
public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext context, it.cnr.jada.persistency.sql.CompoundFindClause clause, it.cnr.jada.bulk.OggettoBulk model) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator( context, createComponentSession().cerca(context.getUserContext(),clause,model));
	} catch(Exception e) {
		throw handleException(e);
	}
}
public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, String property) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator( actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
	} catch(Exception e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
protected void init(Config config,ActionContext context) throws BusinessProcessException {
	try {
			super.init(config,context);
			setBulkClassName(config.getInitParameter("bulkClassName"));
			setComponentSessioneName(config.getInitParameter("componentSessionName"));
			Liquid_gruppo_centroBulk liq = new Liquid_gruppo_centroBulk();		
			setIterator(context,createComponentSession().cerca(context.getUserContext(),null,liq ));
		} catch(ClassNotFoundException e) {
			throw new RuntimeException("Non trovata la classe bulk");
		} catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
}
public void ribalta(ActionContext context,Liquid_gruppo_centroBulk liquid) throws BusinessProcessException {
	try{
		Liquid_coriComponentSession sess = (Liquid_coriComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCORI00_EJB_Liquid_coriComponentSession",Liquid_coriComponentSession.class);
		sess.callRibalta(context.getUserContext(), liquid);
		refresh(context);
		getIterator().moveTo(0);
		RemoteIterator it = getIterator();
		int i=0;
		while(it.hasMoreElements()) {
			Liquid_gruppo_centroBulk elem = (Liquid_gruppo_centroBulk) it.nextElement();
			i=i+1;
			if (
				elem.getEsercizio().equals(liquid.getEsercizio())
				&&
				elem.getCd_gruppo_cr().equals(liquid.getCd_gruppo_cr())
				&&
				elem.getCd_regione().equals(liquid.getCd_regione())
				&&
				elem.getPg_comune().equals(liquid.getPg_comune())
				&&
				elem.getPg_gruppo_centro().equals(liquid.getPg_gruppo_centro())
				)
			{
				getSelection().setFocus(i-1);
				setFocusedElement(context,elem);
				break;
			}
		}
	}catch(javax.ejb.EJBException e){
		throw handleException(e);
	} catch(java.rmi.RemoteException re){
		throw handleException(re);
	} catch(ComponentException ce){
		throw handleException(ce);
	}
}
public void cambiaStato(ActionContext context,Liquid_gruppo_centroBulk liquid) throws BusinessProcessException {
	try{
		Liquid_coriComponentSession sess = (Liquid_coriComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCORI00_EJB_Liquid_coriComponentSession",Liquid_coriComponentSession.class);
		sess.cambiaStato(context.getUserContext(), liquid);
		refresh(context);
		getIterator().moveTo(0);
		RemoteIterator it = getIterator();
		int i=0;
		while(it.hasMoreElements()) {
			Liquid_gruppo_centroBulk elem = (Liquid_gruppo_centroBulk) it.nextElement();
			i=i+1;
			if (
				elem.getEsercizio().equals(liquid.getEsercizio())
				&&
				elem.getCd_gruppo_cr().equals(liquid.getCd_gruppo_cr())
				&&
				elem.getCd_regione().equals(liquid.getCd_regione())
				&&
				elem.getPg_comune().equals(liquid.getPg_comune())
				&&
				elem.getPg_gruppo_centro().equals(liquid.getPg_gruppo_centro())
				)
			{
				getSelection().setFocus(i-1);
				setFocusedElement(context,elem);
				break;
			}
		}
	}catch(javax.ejb.EJBException e){
		throw handleException(e);
	} catch(java.rmi.RemoteException re){
		throw handleException(re);
	} catch(ComponentException ce){
		throw handleException(ce);
	}
}
public boolean esisteRiga(ActionContext context,Liquid_gruppo_centroBulk liquid) throws BusinessProcessException {
	try{
		Liquid_coriComponentSession sess = (Liquid_coriComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCORI00_EJB_Liquid_coriComponentSession",Liquid_coriComponentSession.class);
		return sess.esisteRiga(context.getUserContext(), liquid);
	}catch(javax.ejb.EJBException e){
		throw handleException(e);
	} catch(java.rmi.RemoteException re){
		throw handleException(re);
	} catch(ComponentException ce){
		throw handleException(ce);
	}
}
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
 * @param newBulkClass java.lang.Class
 */
public void setBulkClass(java.lang.Class newBulkClass) {
	bulkClass = newBulkClass;
}
public void setBulkClassName(java.lang.String bulkClassName) throws ClassNotFoundException {
	bulkClass = getClass().getClassLoader().loadClass(bulkClassName);
	bulkInfo = it.cnr.jada.bulk.BulkInfo.getBulkInfo(bulkClass);
	setColumns(bulkInfo.getColumnFieldPropertyDictionary());
}
public void setBulkInfo(it.cnr.jada.bulk.BulkInfo newBulkInfo) {
	bulkInfo = newBulkInfo;
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
public java.lang.String getComponentSessioneName() {
	return componentSessioneName;
}
public void setComponentSessioneName(java.lang.String newComponentSessioneName) {
	componentSessioneName = newComponentSessioneName;
}
}
