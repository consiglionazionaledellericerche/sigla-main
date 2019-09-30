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
 * Created on Sep 15, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.bp;

import java.rmi.RemoteException;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Parametri_cnrComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;


public class CRUDConfigParametriCnrBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	/**
	 * Primo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 */
	public CRUDConfigParametriCnrBP() {
		super();
	}
	/**
	 * Secondo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 * @param String function
	 */
	public CRUDConfigParametriCnrBP(String function) {
		super(function);
	}
	
	protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
		super.initialize(actioncontext);
		Parametri_cnrComponentSession sessione = (Parametri_cnrComponentSession) createComponentSession();
		try {
			Parametri_cnrBulk parCnrBulk = sessione.getParametriCnr(actioncontext.getUserContext(),	CNRUserContext.getEsercizio(actioncontext.getUserContext()));
			if (parCnrBulk != null)
				this.setModel(actioncontext, parCnrBulk);
				this.setStatus(it.cnr.jada.util.action.FormController.EDIT);
		} catch (DetailedRuntimeException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}

	public boolean isLivelloPdgDecisionaleSpeEnabled(UserContext userContext, Parametri_cnrBulk parCnr)  throws it.cnr.jada.action.BusinessProcessException {
		try {
			Parametri_cnrComponentSession sessione = (Parametri_cnrComponentSession) createComponentSession();
			return sessione.isLivelloPdgDecisionaleSpeEnabled(userContext, parCnr);
		} catch (BusinessProcessException e) {
			throw handleException(e);
		} catch (DetailedRuntimeException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	} 

	public boolean isLivelloPdgDecisionaleEtrEnabled(UserContext userContext, Parametri_cnrBulk parCnr)  throws it.cnr.jada.action.BusinessProcessException {
		try {
			Parametri_cnrComponentSession sessione = (Parametri_cnrComponentSession) createComponentSession();
			return sessione.isLivelloPdgDecisionaleEtrEnabled(userContext, parCnr);
		} catch (BusinessProcessException e) {
			throw handleException(e);
		} catch (DetailedRuntimeException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	} 

	public boolean isLivelloPdgContrattazioneSpeEnabled(UserContext userContext, Parametri_cnrBulk parCnr)  throws it.cnr.jada.action.BusinessProcessException {
		try {
			Parametri_cnrComponentSession sessione = (Parametri_cnrComponentSession) createComponentSession();
			return sessione.isLivelloPdgContrattazioneSpeEnabled(userContext, parCnr);
		} catch (BusinessProcessException e) {
			throw handleException(e);
		} catch (DetailedRuntimeException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	} 

	public boolean isSearchButtonHidden() {
		return true;
	}

	public boolean isFreeSearchButtonHidden() {
		return true;
	}

	public boolean isNewButtonHidden() {
		return true;
	}
	
	protected void resetTabs(ActionContext context) {
		setTab("tab","tabTestata");
	}	
}