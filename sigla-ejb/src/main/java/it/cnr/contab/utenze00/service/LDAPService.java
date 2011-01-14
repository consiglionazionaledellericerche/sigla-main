package it.cnr.contab.utenze00.service;

import java.rmi.RemoteException;
import java.util.Hashtable;

import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

public class LDAPService {
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
}
