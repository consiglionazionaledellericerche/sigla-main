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
import java.util.Dictionary;
import java.util.Iterator;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.ejb.Parametri_enteComponentSession;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.comp.ComponentException;
/**
 * Business Process di gestione del login
 */

public class LoginBP extends it.cnr.jada.util.action.BulkBP {
	private static final Object unregisterUsersLock = new Object();

	private static boolean unregisterUsers = false;
	private boolean bootstrap = false;
	
	public LoginBP() {
		super();
	}
	/**
	 * Effettua una operazione di ricerca per un attributo di un modello.
	 * @param actionContext contesto dell'azione in corso
	 * @param clauses Albero di clausole da utilizzare per la ricerca
	 * @param bulk prototipo del modello di cui si effettua la ricerca
	 * @param context modello che fa da contesto alla ricerca (il modello del FormController padre del
	 * 			controller che ha scatenato la ricerca)
	 * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
	 */
	public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, java.lang.String property) throws it.cnr.jada.action.BusinessProcessException {
		return null;
	}
	public CNRUserInfo getUserInfo() {
		return (CNRUserInfo)getModel();
	}
	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		super.init(config,context);
		setModel(context,new it.cnr.contab.utenze00.bulk.CNRUserInfo());
		synchronized (unregisterUsersLock) {
			if (!unregisterUsers)
				try {
					it.cnr.contab.utenze00.action.LoginAction.getComponentSession().unregisterUsers(context.getApplicationId());
					unregisterUsers = true;
				} catch(Throwable e) {
				}
		}
	}
	public boolean isDirty() {
		return false;
	}
	/**
	 * Crea la ParametriEnteComponentSession da usare per effettuare operazioni
	 */
	public Parametri_enteComponentSession createParametriEnteComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (Parametri_enteComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_enteComponentSession", Parametri_enteComponentSession.class);
	}
	public String getLdapLinkCambioPassword(UserContext uc) throws BusinessProcessException {
		Parametri_enteBulk par=null;
		try {
			par = ((Parametri_enteComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_enteComponentSession",Parametri_enteComponentSession.class)).getParametriEnte(uc);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
		if (par!=null)
			return par.getLdap_link_cambio_password();
		return null;
	}
	/**
	 * numero dei giorni rimanenti entro i quali si deve effettuare il login su LDAP
	 * 
	 * @param uc
	 * @return
	 * @throws BusinessProcessException
	 */
	public int getGiorniRimanenti(UserContext uc) throws BusinessProcessException {
		try {
			return ((Parametri_enteComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_enteComponentSession",Parametri_enteComponentSession.class)).getGiorniRimanenti(uc);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}
	public void utentiMultipli(UserContext uc, UtenteBulk utente) throws BusinessProcessException {
		try {
			java.util.List list = it.cnr.contab.utenze00.action.LoginAction.getComponentSession().utentiMultipli(uc, utente);
			Dictionary utentiMultipliKeys = new it.cnr.jada.util.OrderedHashtable();
			for (Iterator it = list.iterator(); it.hasNext();) {
				UtenteBulk ute = (UtenteBulk) it.next();
				utentiMultipliKeys.put(ute.getCd_utente(), ute.getCd_utente());
			}
			CNRUserInfo ui = getUserInfo();
			ui.setUtentiMultipliKeys(utentiMultipliKeys);
			java.util.Enumeration en = utentiMultipliKeys.elements();
			if (en.hasMoreElements()) {
				ui.setUtente_multiplo((String)en.nextElement());
			}
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}
	
	@Override
	public boolean isBootstrap() {
		return bootstrap;
	}
	
	public void setBootstrap(boolean bootstrap) {
		this.bootstrap = bootstrap;
	}
	
	
}
