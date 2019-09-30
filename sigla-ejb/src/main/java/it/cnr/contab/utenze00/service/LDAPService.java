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

package it.cnr.contab.utenze00.service;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Optional;

import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.ejb.EJBCommonServices;
import org.springframework.beans.factory.InitializingBean;

public class LDAPService implements InitializingBean {
	private GestioneLoginComponentSession gestioneLoginComponent;
	private Hashtable<Integer, String[]> ldapUsers = new Hashtable<Integer, String[]>();
	
	public void setGestioneLoginComponent(
			GestioneLoginComponentSession gestioneLoginComponent) {
		this.gestioneLoginComponent = gestioneLoginComponent;
	}
	
	public String[] getLdapUserFromMatricola(UserContext userContext, Integer matricola) throws ComponentException, RemoteException{
		if (ldapUsers.get(matricola) == null)
			ldapUsers.put(matricola, gestioneLoginComponent.getLdapUserFromMatricola(userContext, matricola));
		return ldapUsers.get(matricola);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.gestioneLoginComponent = Optional.ofNullable(EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession"))
				.filter(GestioneLoginComponentSession.class::isInstance)
				.map(GestioneLoginComponentSession.class::cast)
				.orElseThrow(() -> new DetailedRuntimeException("cannot find ejb CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession"));
	}
}
