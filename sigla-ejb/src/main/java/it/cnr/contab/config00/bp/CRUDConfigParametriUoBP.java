/*
 * Created on Sep 15, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.bp;

import java.rmi.RemoteException;

import it.cnr.contab.config00.bulk.Parametri_uoBulk;
import it.cnr.contab.config00.ejb.Parametri_uoComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;


public class CRUDConfigParametriUoBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	/**
	 * Primo costruttore della classe <code>CRUDConfigParametriUoBP</code>.
	 */
	public CRUDConfigParametriUoBP() {
		super();
	}
	/**
	 * Secondo costruttore della classe <code>CRUDConfigParametriUoBP</code>.
	 * @param String function
	 */
	public CRUDConfigParametriUoBP(String function) {
		super(function);
	}
	
	protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
		super.initialize(actioncontext);
		Parametri_uoComponentSession sessione = (Parametri_uoComponentSession) createComponentSession();
		try {
			Parametri_uoBulk parUoBulk = sessione.getParametriUo(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext()), new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(actioncontext.getUserContext())));
			if (parUoBulk != null)
				this.setModel(actioncontext, parUoBulk);
				this.setStatus(it.cnr.jada.util.action.FormController.EDIT);
		} catch (DetailedRuntimeException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}

	public boolean isFlGestioneModuliEnabled(UserContext userContext, Parametri_uoBulk parUo)  throws it.cnr.jada.action.BusinessProcessException {
		try {
			Parametri_uoComponentSession sessione = (Parametri_uoComponentSession) createComponentSession();
			return sessione.isFlGestioneModuliEnabled(userContext, parUo);
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
}