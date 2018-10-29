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
