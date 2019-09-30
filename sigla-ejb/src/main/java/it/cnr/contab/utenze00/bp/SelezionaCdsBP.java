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

package it.cnr.contab.utenze00.bp;
import it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDBulk;
import it.cnr.contab.compensi00.ejb.CompensoComponentSession;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utente00.ejb.UtenteComponentSession;
import it.cnr.contab.utente00.nav.ejb.*;
import it.cnr.contab.utenze00.action.SelezionaCdsAction;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * Business Process di gestione della lista delle UO di scrivania
 */

public class SelezionaCdsBP extends it.cnr.jada.util.action.BulkBP {
	private it.cnr.contab.utenze00.bulk.CNRUserInfo userInfo;

public SelezionaCdsBP() {
	super();
}
public SelezionaCdsBP(String function) {
	super(function);
}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'userInfo'
 *
 * @return Il valore della proprietà 'userInfo'
 */
public it.cnr.contab.utenze00.bulk.CNRUserInfo getUserInfo() {
	return userInfo;
}

protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	//setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class));
	try {
		SelezionaCdsBulk scds = new SelezionaCdsBulk();

		CNRUserInfo ui = (CNRUserInfo)context.getUserInfo();
		scds.setCdr(new CdrBulk());
		scds.setUo(new Unita_organizzativaBulk());
		scds.setCds(new CdsBulk());
		setModel(context, scds);
		completeSearchTools(context,getController());
	} catch(Throwable e) {
		throw handleException(e);
	}
	super.init(config,context);
}

/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'userInfo'
 *
 * @param newUserInfo	Il valore da assegnare a 'userInfo'
 */
public void setUserInfo(it.cnr.contab.utenze00.bulk.CNRUserInfo newUserInfo) {
	userInfo = newUserInfo;
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
	int i = 0;

	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.selezionaCds");

	return toolbar;
}
public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, String property) throws it.cnr.jada.action.BusinessProcessException {
	try {
		CNRUserContext userContext = new CNRUserContext(
			getUserInfo().getUtente().getCd_utente(),
			actionContext.getSessionId(),
			getUserInfo().getEsercizio(),
			CNRUserContext.getCd_unita_organizzativa(actionContext.getUserContext()),
			CNRUserContext.getCd_cds(actionContext.getUserContext()),
			CNRUserContext.getCd_cdr(actionContext.getUserContext()));
        actionContext.setUserContext(userContext);
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
	} catch(Exception e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
public UtenteComponentSession createComponentSession()
throws javax.ejb.EJBException,
		java.rmi.RemoteException,
		BusinessProcessException {
	return (UtenteComponentSession)createComponentSession("CNRUTENZE00_EJB_UtenteComponentSession",UtenteComponentSession.class);
}
public void findCds(ActionContext context) throws BusinessProcessException {

	try{
		CNRUserContext userContext = new CNRUserContext(
				getUserInfo().getUtente().getCd_utente(),
				context.getSessionId(),
				getUserInfo().getEsercizio(),
				CNRUserContext.getCd_unita_organizzativa(context.getUserContext()),
				CNRUserContext.getCd_cds(context.getUserContext()),
				CNRUserContext.getCd_cdr(context.getUserContext()));
		context.setUserContext(userContext);
		SelezionaCdsBulk scds = createComponentSession().findCds(context.getUserContext(), (SelezionaCdsBulk)getModel());
		setModel(context, scds);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}catch(ComponentException ex){
		throw handleException(ex);
	}
}
public void findUo(ActionContext context) throws BusinessProcessException {

	try{
		CNRUserContext userContext = new CNRUserContext(
				getUserInfo().getUtente().getCd_utente(),
				context.getSessionId(),
				getUserInfo().getEsercizio(),
				CNRUserContext.getCd_unita_organizzativa(context.getUserContext()),
				CNRUserContext.getCd_cds(context.getUserContext()),
				CNRUserContext.getCd_cdr(context.getUserContext()));
		context.setUserContext(userContext);
		SelezionaCdsBulk scds = createComponentSession().findUo(context.getUserContext(), (SelezionaCdsBulk)getModel());
		setModel(context, scds);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}catch(ComponentException ex){
		throw handleException(ex);
	}
}
public void validaSelezionaCds(ActionContext context, Integer esercizio) throws BusinessProcessException {
	try{
		createComponentSession().validaSelezionaCds(context.getUserContext(), (SelezionaCdsBulk)getModel(), esercizio);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}catch(ComponentException ex){
		throw handleException(ex);
	}
}
}
