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

import java.util.Dictionary;

import it.cnr.contab.compensi00.ejb.*;
import it.cnr.contab.cori00.docs.bulk.Liquidazione_massa_coriBulk;
import it.cnr.contab.cori00.ejb.Liquid_coriComponentSession;
import it.cnr.contab.logs.bulk.Batch_log_rigaBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.*;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.Table;

public class LiquidazioneMassaCoriBP extends it.cnr.jada.util.action.BulkBP {
	private final SimpleDetailCRUDController batch_log_riga = new SimpleDetailCRUDController("batch_log_riga",Batch_log_rigaBulk.class,"batch_log_riga",this) {
	};
	private boolean isLiquidato = false;
	
public LiquidazioneMassaCoriBP() {
	super();
}
public LiquidazioneMassaCoriBP(String function) {
	super(function);
}
public final it.cnr.jada.util.action.SimpleDetailCRUDController getBatch_log_riga() {
	return batch_log_riga;
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[2];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.startSearch");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.reset");
	return toolbar;
}

public Liquid_coriComponentSession createComponentSession()
	throws javax.ejb.EJBException,
			java.rmi.RemoteException,
			BusinessProcessException {
	return (Liquid_coriComponentSession)createComponentSession("CNRCORI00_EJB_Liquid_coriComponentSession",Liquid_coriComponentSession.class);
}
public void doEseguiLiquidMassaCori(ActionContext context,Liquidazione_massa_coriBulk eMassa) throws BusinessProcessException {

	try{
		Liquid_coriComponentSession sess = (Liquid_coriComponentSession)createComponentSession();
		setModel(context,sess.eseguiLiquidazioneMassaCori(context.getUserContext(), eMassa));

	}catch(javax.ejb.EJBException e){
		throw handleException(e);
	} catch(java.rmi.RemoteException re){
		throw handleException(re);
	} catch(ComponentException ce){
		throw handleException(ce);
	}
}
public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, String property) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
	} catch(Exception e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}

public void doReset(ActionContext context) throws BusinessProcessException{
	try {
			Liquidazione_massa_coriBulk liq = new Liquidazione_massa_coriBulk();		
			liq.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
	
			setModel(context, liq);
			setIsLiquidato(false);
		} catch(Throwable e) {
			throw handleException(e);
		}
}
public void doCercaBatch(ActionContext context,Liquidazione_massa_coriBulk eMassa) throws BusinessProcessException{
	try{
		eMassa.setData_da(null);
		eMassa.setData_a(null);	
		eMassa.setDa_esercizio_precedente(null);			
		Liquid_coriComponentSession sess = (Liquid_coriComponentSession)createComponentSession();
		setModel(context,sess.cercaBatch(context.getUserContext(), eMassa));
	}catch(javax.ejb.EJBException e){
		throw handleException(e);
	} catch(java.rmi.RemoteException re){
		throw handleException(re);
	} catch(ComponentException ce){
		throw handleException(ce);
}
}
			
protected void init(Config config,ActionContext context) throws BusinessProcessException {

	try {
		Liquidazione_massa_coriBulk liq = new Liquidazione_massa_coriBulk();		
		liq.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
		setModel(context, liq);
		setIsLiquidato(false);
		batch_log_riga.setMultiSelection(false);
	} catch(Throwable e) {
		throw handleException(e);
	}
	
	super.init(config,context);
}
public boolean isLiquidato() {
	return isLiquidato;
}
public void setIsLiquidato(boolean newIsLiquidato) {
	isLiquidato = newIsLiquidato;
}
}
