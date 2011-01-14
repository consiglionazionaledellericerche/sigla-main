package it.cnr.contab.progettiric00.ejb;

import javax.ejb.Remote;

@Remote
public interface ProgettoRicercaPadreComponentSession extends it.cnr.contab.progettiric00.ejb.ProgettoRicercaComponentSession {
	void aggiornaGECO(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
