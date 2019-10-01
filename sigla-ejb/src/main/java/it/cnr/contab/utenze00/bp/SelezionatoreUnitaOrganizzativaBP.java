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
import java.rmi.RemoteException;

import javax.ejb.EJBException;

import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utente00.ejb.UtenteComponentSession;
import it.cnr.contab.utente00.nav.ejb.*;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;

/**
 * Business Process di gestione della lista delle UO di scrivania
 */

public class SelezionatoreUnitaOrganizzativaBP extends it.cnr.jada.util.action.SelezionatoreListaBP {
	private it.cnr.contab.utenze00.bulk.CNRUserInfo userInfo;

	public SelezionatoreUnitaOrganizzativaBP() {
		super();
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
		super.init(config,context);
		setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class));
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
	public void selezionaUO(ActionContext context) throws ComponentException, RemoteException, EJBException, BusinessProcessException {
		Unita_organizzativaBulk	uo = (it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk)getFocusedElement();
		selezionaUO(context, null, uo, null);
	}
	public void selezionaUO(ActionContext context, Integer esercizio, Unita_organizzativaBulk uo, CdrBulk cdr) throws ComponentException, RemoteException, EJBException, BusinessProcessException {
		GestioneUtenteBP bp = (GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP");
		
		if (esercizio!=null)
			((CNRUserInfo)getUserInfo()).setEsercizio(esercizio);
		if (uo != null){
			getUserInfo().setUnita_organizzativa(uo);
			if(cdr==null)
				cdr=(CdrBulk)getComponentSession().cdrDaUo(context.getUserContext(), uo);
		}
		if (cdr != null)
			getUserInfo().setCdr(cdr);
		
		CNRUserInfo userInfo = (CNRUserInfo)getUserInfo();
		UserContext userContext = new CNRUserContext(
			userInfo.getUtente().getCd_utente(),
			context.getSessionId(),
			userInfo.getEsercizio(),
			userInfo.getUnita_organizzativa().getCd_unita_organizzativa(),
			userInfo.getUnita_organizzativa().getCd_unita_padre(),
			userInfo.getCdr().getCd_centro_responsabilita());
		
		if (context instanceof HttpActionContext) {
			it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().registerUser(userContext,context.getApplicationId());
			//	Remmato Marco Spasiano 28/02/2006 per problema di sessioni attive
			//UnregisterUser.registerUnregisterUser((HttpActionContext)context);
		}
		try{
			SessionTraceBulk sessionTrace = (SessionTraceBulk)createCRUDComponentSession().inizializzaBulkPerModifica(userContext, new SessionTraceBulk(userContext.getSessionId()));
			sessionTrace.setCd_cds(CNRUserContext.getCd_cds(userContext));
			sessionTrace.setToBeUpdated();
			createCRUDComponentSession().modificaConBulk(context.getUserContext(), sessionTrace);		
		}catch(Exception e){			
		}
		
		bp.setUserInfo((CNRUserInfo)getUserInfo());
		context.setUserInfo(bp.getUserInfo());
		context.setUserContext(userContext);
		bp.setRadiceAlbero_main(context, getComponentSession().generaAlberoPerUtente(context.getUserContext(),bp.getUserInfo().getUtente(),uo.getCd_unita_organizzativa(),null,(short)0));
	}
	
	private CRUDComponentSession createCRUDComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException{
		return (CRUDComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession");
	}
	
	public GestioneLoginComponentSession getComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (GestioneLoginComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession",GestioneLoginComponentSession.class);
	}
	
	public UtenteComponentSession createUtenteComponentSession()
	throws javax.ejb.EJBException,
			java.rmi.RemoteException,
			BusinessProcessException {
		return (UtenteComponentSession)createComponentSession("CNRUTENZE00_EJB_UtenteComponentSession",UtenteComponentSession.class);
	}
	
	public SelezionaCdsBulk findUo(ActionContext context, SelezionaCdsBulk scds) throws BusinessProcessException {
	
		try{
			return scds = createUtenteComponentSession().findUo(context.getUserContext(), scds);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}catch(ComponentException ex){
			throw handleException(ex);
		}
	}
}
